package com.backbase.expert.extensions.sushi;

import com.backbase.expert.extensions.sushi.util.CookieUtils;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeExchangeException;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.language.bean.BeanExpression;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bartv on 31/10/14.
 */
public class RemoteRequestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteRequestUtils.class);

    public static RemoteRequest createInitialRemoteRequest(SushiRecipe sushiRecipe, Exchange exchange) throws URISyntaxException {
        mapContextToHeaders(exchange, sushiRecipe.getInitialPopulateHeaders());
        Map<String, List<String>> incomingHeaders = mapIncomingHeaders(sushiRecipe.getInitialHttpHeadersToTransfer(), exchange);
        URI remoteApplicationUrl = new URI(sushiRecipe.getInitialRequestRemoteApplicationUrl());
        return new RemoteRequest(sushiRecipe.getInitialRemoteHttpMethod(), remoteApplicationUrl, incomingHeaders);
    }

    public static RemoteRequest createProxyRemoteRequest(ApplicationViewState applicationViewState, Exchange exchange) throws Exception {

        SushiRecipe sushiRecipe = applicationViewState.getSushiRecipe();
        mapContextToHeaders(exchange, sushiRecipe.getEachPopulateHeaders());
        RemoteRequest initialRemoteRequest = applicationViewState.getInitialRemoteRequest();

        String method = exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class);
        String requestPath = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
        String queryString = exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class);

        String sushiRemotePath = requestPath.replaceFirst("/sushi/proxy/" + applicationViewState.getId(), "");
        String remoteBaseUrl = initialRemoteRequest.getBasePath();

        String newRemoteRequestUrl = remoteBaseUrl + sushiRemotePath;
        if (StringUtils.isNotEmpty(queryString)) {
            newRemoteRequestUrl += "?" + queryString;
        }

        URI remoteApplicationUrl = new URI(newRemoteRequestUrl);

        Map<String, List<String>> headers = mapIncomingHeaders(sushiRecipe.getEachHttpHeadersToTransfer(), exchange);


        return new RemoteRequest(method, remoteApplicationUrl, headers);

    }


    public static Map<String, List<String>> mapIncomingHeaders(Map<String, String> headersMapping, Exchange exchange) {

        Map<String, List<String>> headers = new HashMap<String, List<String>>();

        LOG.debug("Transferring exchange headers from exchange: {}", headers);
        for (Map.Entry<String, String> header : headersMapping.entrySet()) {
            String value = exchange.getIn().getHeader(header.getKey(), String.class);
            if (StringUtils.isNotEmpty(value)) {
                List<String> values = Arrays.asList(value.split(","));
                headers.put(header.getValue(), values);
                LOG.debug("Transferring exchange header: {} with value: {} to application view state", header.getKey(), values);
            } else {
                LOG.debug("Did not transfer header: {} because it has no value", header.getKey());
            }
        }

        return headers;

    }


    public static void mapCookies(ApplicationViewState applicationViewState, Exchange exchange, CookieOrigin cookieOrigin) throws MalformedCookieException {
        SushiRecipe sushiRecipe = applicationViewState.getSushiRecipe();

        String cookieHeader = exchange.getIn().getHeader("Cookie", String.class);

        List<Cookie> cookies = CookieUtils.parseCookies(cookieHeader, cookieOrigin);
        String[] cookiesToTransferFromExchange = sushiRecipe.getInitialCookiesToTransfer();

        if (cookiesToTransferFromExchange != null) {
            for (Cookie cookie : cookies) {
                if (ArrayUtils.contains(cookiesToTransferFromExchange, cookie.getName())) {
                    LOG.debug("Transferring cookie: {} ", cookie);
                    applicationViewState.addCookie(cookie);
                } else {
                    LOG.debug("Skipping Cookie: {} as it is not in the recipe", cookie);
                }
            }
        }
        LOG.debug("Transferring Cookies from exchange: {}", cookies);
        exchange.setProperty("SushiCookieOrigin", cookieOrigin);
    }

    private static void mapContextToHeaders(Exchange exchange, Map<String, Object> populateHeaders) {
        Map<String, String> authenticationMapping = (Map<String, String>) populateHeaders.get("authentication");
        Map<String, String> principalMapping = (Map<String, String>) populateHeaders.get("backbaseUser");
        Map<String, String> contantsMapping = (Map<String, String>) populateHeaders.get("constants");
        List<String> cookiesMapping = (List<String>) populateHeaders.get("cookies");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authenticationMapping != null && authentication != null) {
            LOG.info("Mapping properties from authentication token: {}", authentication);
            mapDynamicExpressions(authentication, authenticationMapping, exchange);
        }
        if (principalMapping != null && authentication != null && authentication.getPrincipal() != null) {
            LOG.info("Mapping properties from principal: {}", authentication);
            mapDynamicExpressions(authentication.getPrincipal(), authenticationMapping, exchange);
        }
        if (contantsMapping != null) {
            LOG.info("Mapping constants: {}", contantsMapping);
            exchange.getIn().getHeaders().putAll(contantsMapping);
        }
        if (cookiesMapping != null) {
            //Not sure what to do now
        }
    }


    public static void mapDynamicExpressions(Object source, Map<String, String> headerMap, Exchange exchange) {
        LOG.debug("Mapping Dynamic Expressions from {} using {}", source, headerMap);

        DefaultExchange sourceExchange = new DefaultExchange(exchange.getContext());
        sourceExchange.getIn().setBody(source);

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            String headerName = entry.getKey();
            String expression = entry.getValue();

            BeanExpression beanExpression = new BeanExpression(source, expression);
            Object value = beanExpression.evaluate(sourceExchange);
            LOG.debug("Mapping header: {} with expression: {} with value: {}", new Object[]{headerName, expression, value});
            exchange.getIn().setHeader(headerName, value);
        }
    }

}
