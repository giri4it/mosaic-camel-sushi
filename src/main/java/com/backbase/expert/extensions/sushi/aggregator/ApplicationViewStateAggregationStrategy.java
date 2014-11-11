package com.backbase.expert.extensions.sushi.aggregator;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.exception.ApplicationViewStateNotFoundException;
import com.backbase.expert.extensions.sushi.processors.context.SushiContextPopulatingProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bartv on 28/08/14.
 */
public class ApplicationViewStateAggregationStrategy implements AggregationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationViewStateAggregationStrategy.class);

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        ApplicationViewState applicationViewState = newExchange.getIn().getBody(ApplicationViewState.class);

        if (applicationViewState == null) {
            String applicationViewStateId = oldExchange.getIn().getHeader(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, String.class);
            throw new ApplicationViewStateNotFoundException("Application View State " + applicationViewStateId + " does not exist or is expired.");
        }

        String remoteRequestId = oldExchange.getIn().getHeader("SushiRemoteRequestId", String.class);
        RemoteRequest remoteRequest = applicationViewState.getRemoteRequest(remoteRequestId);


        String referer = oldExchange.getIn().getHeader("Referer", String.class);
        LOG.debug("Application View State Received: {}", applicationViewState);
        LOG.debug("Referer: {}", referer);

        applicationViewState.setRefererUrl(referer);
        oldExchange.setProperty("SushiPopulateHeaders", applicationViewState.getSushiRecipe().getInitialHttpHeadersToTransfer());
        oldExchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST, remoteRequest);
        oldExchange.setProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, applicationViewState);
        oldExchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST_ID, remoteRequest.getId());
        return oldExchange;
    }
}
