package com.backbase.expert.extensions.sushi.processors.http;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.util.CookieUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bartv on 24/08/14.
 * <p/>
 * VERY WIP
 */
public class BuildHttpRequestFromApplicationViewStateProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(BuildHttpRequestFromApplicationViewStateProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        Message in = exchange.getIn();
        Message out = exchange.getOut();

        ApplicationViewState applicationViewState = exchange.getProperty("SushiApplicationViewState", ApplicationViewState.class);
        RemoteRequest currentRemoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);

        setBody(currentRemoteRequest, in, out);
//        out.setBody(in.getBody());
        out.setHeaders(new HashMap<String, Object>());
        out.setHeader(Exchange.HTTP_URI, currentRemoteRequest.getFullUrl());
        out.setHeader(Exchange.HTTP_METHOD, currentRemoteRequest.getMethod());
        String cookieString = CookieUtils.getCookieHeaderString(applicationViewState.getCookies());
        if (cookieString != null) {
            out.setHeader("Cookie", cookieString);
        }
        for (String headerName : currentRemoteRequest.getHeaderNames()) {
            List<String> values = currentRemoteRequest.getHeaderValues(headerName);
//            if (headerName.equals("Accept-Encoding")) {
//                LOG.info("Do not send Accept-Encoding");
//            } else {
                out.setHeader(headerName, StringUtils.join(values, ","));
//            }
        }
        LOG.debug("HTTP Request Headers:  {}", out.getHeaders());
        LOG.debug("Assembled HTTP Request Body: {}", out.getBody());


    }


    public void setBody(RemoteRequest remoteRequest, Message in, Message out) throws UnsupportedEncodingException {
        String method = remoteRequest.getMethod();
        String contentType = in.getHeader("Content-Type", String.class);
        if ("POST".equalsIgnoreCase(method) && SushiConstants.SUSHI_CONTENT_TYPE_URL_ENCODED.equalsIgnoreCase(contentType)) {
            StringBuilder postData = new StringBuilder();
            List postParameterNames = in.getHeader("SushiParameterNames", List.class);
            for (Object parameterName : postParameterNames) {
                String name = (String) parameterName;
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(name, "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(in.getHeader(name)), "UTF-8"));
            }
            LOG.trace("Post Data: [{}]", postData);

            byte[] bodyBytes = postData.toString().getBytes("UTF-8");
            InputStream inputStream = new ByteArrayInputStream(bodyBytes);

            int length = bodyBytes.length;
            InputStreamEntity httpEntity = new InputStreamEntity(inputStream, length);
            httpEntity.setContentType(contentType);
            out.setBody(httpEntity);
            out.setHeader("Content-Type", length);
        } else {
            out.setBody(in.getBody());
        }


    }
}
