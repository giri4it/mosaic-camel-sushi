package com.backbase.expert.extensions.sushi.processors.applicationviewstate;

import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.RemoteResponse;
import com.backbase.expert.extensions.sushi.SushiConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by bartv on 04/11/14.
 */
public class RemoteResponseWrapperProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteResponseWrapperProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {

        RemoteRequest remoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);
        String body = exchange.getIn().getBody(String.class);
        String encoding = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);
        Map<String, Object> headers = exchange.getIn().getHeaders();

        RemoteResponse remoteResponse = new RemoteResponse(remoteRequest.getId(), body.getBytes(), encoding, headers);

        LOG.debug("Wrapped remote response for caching");

        exchange.getOut().setBody(remoteResponse);


    }
}
