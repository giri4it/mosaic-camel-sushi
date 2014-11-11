package com.backbase.expert.extensions.sushi.processors.http;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.RemoteRequestUtils;
import com.backbase.expert.extensions.sushi.SushiConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bartv on 24/10/14.
 */
public class PrepareRedirectRequestProcessor implements Processor {


    private static final Logger LOG = LoggerFactory.getLogger(PrepareRedirectRequestProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        Integer redirectCount = exchange.getProperty("SushiRedirectCount", Integer.class);
        int count = redirectCount != null ? redirectCount : 1;
        if (count == 5) {
            throw new Exception("To many redirects");
        }

        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);

        Map<String, List<String>> incomingHeaders = RemoteRequestUtils.mapIncomingHeaders(applicationViewState.getSushiRecipe().getInitialHttpHeadersToTransfer(), exchange);
        String location = exchange.getIn().getHeader("Location", String.class);

        RemoteRequest currentRemoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);

        RemoteRequest proxyRequest = new RemoteRequest(currentRemoteRequest.getId(), "GET", new URI(location), incomingHeaders);

        LOG.info("Preparing redirect request: {}", proxyRequest);

        exchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST, proxyRequest);
        exchange.setProperty("SushiRedirectCount", count);


    }
}
