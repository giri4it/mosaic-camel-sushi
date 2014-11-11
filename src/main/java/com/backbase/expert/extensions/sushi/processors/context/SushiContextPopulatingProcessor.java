package com.backbase.expert.extensions.sushi.processors.context;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bartv on 15/09/14.
 */
public class SushiContextPopulatingProcessor implements Processor {

    private static final String SUSHI_CONTEXT_PATH = "SushiContextPath";
    private static final String SUSHI_SERVLET_PATH = "SushiServletPath";

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpServletRequest request = exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST,
                HttpServletRequest.class);

        exchange.getIn().setHeader(SUSHI_CONTEXT_PATH, request.getContextPath());
        exchange.getIn().setHeader(SUSHI_SERVLET_PATH, request.getServletPath());
    }
}
