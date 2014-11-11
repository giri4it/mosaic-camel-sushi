package com.backbase.expert.extensions.sushi.aggregator;

import com.backbase.expert.extensions.sushi.*;
import com.backbase.expert.extensions.sushi.exception.ApplicationViewStateNotFoundException;
import com.backbase.expert.extensions.sushi.util.CookieUtils;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.RuntimeExchangeException;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by bartv on 28/08/14.
 */
public class ProxyApplicationViewStateAggregationStrategy implements AggregationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyApplicationViewStateAggregationStrategy.class);

    @Override
    public Exchange aggregate(Exchange exchange, Exchange viewStateExchange) {

        ApplicationViewState applicationViewState = viewStateExchange.getIn().getBody(ApplicationViewState.class);

        if (applicationViewState == null) {
            String applicationViewStateId = exchange.getIn().getHeader(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, String.class);
            throw new ApplicationViewStateNotFoundException("Application View State " + applicationViewStateId + " does not exist or is expired.");
        }

        RemoteRequest proxyRequest;
        try {
            proxyRequest = RemoteRequestUtils.createProxyRemoteRequest(applicationViewState, exchange);
            LOG.debug("Setup Remote Proxy Request: {}", proxyRequest);
            RemoteRequest initialRemoteRequest = applicationViewState.getInitialRemoteRequest();
            String referer = exchange.getIn().getHeader("Referer", String.class);
            String remoteBaseUrl = initialRemoteRequest.getBasePath();
            applicationViewState.setRefererUrl(referer);
            CookieOrigin cookieOrigin = CookieUtils.createCookieOrigin(new URI(remoteBaseUrl));
            RemoteRequestUtils.mapCookies(applicationViewState, exchange, cookieOrigin);

            exchange.setProperty(SushiConstants.SUSHI_COOKIE_ORIGIN, cookieOrigin);
            exchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST, proxyRequest);
            exchange.setProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, applicationViewState);
            exchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST_ID, proxyRequest.getId());
            return exchange;
        } catch (Exception e) {
            throw new CamelExecutionException("Could not create remote request", exchange);

        }
    }
}
