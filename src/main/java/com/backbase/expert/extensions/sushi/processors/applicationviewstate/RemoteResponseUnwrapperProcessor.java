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
public class RemoteResponseUnwrapperProcessor implements Processor {



    private static final Logger LOG = LoggerFactory.getLogger(RemoteResponseUnwrapperProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {


        RemoteResponse remoteResponse = exchange.getIn().getBody(RemoteResponse.class);
        LOG.debug("Getting wrapped response from exchange: {}", remoteResponse);
        exchange.getOut().setBody(remoteResponse.getResponse());
        exchange.getOut().setHeaders(remoteResponse.getHeaders());
        // set response to 200
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
    }
}
