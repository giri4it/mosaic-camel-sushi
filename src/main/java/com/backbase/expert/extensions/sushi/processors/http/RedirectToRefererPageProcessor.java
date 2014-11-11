package com.backbase.expert.extensions.sushi.processors.http;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bartv on 18/09/14.
 */
public class RedirectToRefererPageProcessor implements Processor {

    private static final String COOKIE = "Set-Cookie";
    private static final String LOCATION = "Location";

    private static final Logger LOG = LoggerFactory.getLogger(RedirectToRefererPageProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        RemoteRequest remoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);

        applicationViewState.addRemoteHistoryRequest(remoteRequest);

        String refererUrl = applicationViewState.getRefererUrl();

        // Strip query string
        if(refererUrl.contains("?")) {
            refererUrl = StringUtils.substringBeforeLast(refererUrl,"?");
        }


        if (refererUrl.endsWith("/")) {
            refererUrl = refererUrl.substring(0, refererUrl.length() - 1);
        }


        LOG.debug("RefererUrl: " + refererUrl);

        String url2state = "//" + applicationViewState.getId();

        // remove url2state from current refererUrl
        if (refererUrl.contains(url2state)) {
            refererUrl = StringUtils.substringBeforeLast(refererUrl, "//");
        }
        StringBuilder sb = new StringBuilder(refererUrl);
        sb.append(url2state).append("/").append(remoteRequest.getId());

        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 302);
        exchange.getOut().setHeader(LOCATION, sb.toString());
        exchange.getOut().setHeader(COOKIE, exchange.getIn().getHeader(COOKIE));

        LOG.debug("Redirecting to: {}", sb.toString());
    }
}
