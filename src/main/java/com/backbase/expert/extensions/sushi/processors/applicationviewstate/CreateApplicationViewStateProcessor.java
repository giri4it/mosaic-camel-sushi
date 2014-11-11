package com.backbase.expert.extensions.sushi.processors.applicationviewstate;

import com.backbase.expert.extensions.sushi.*;
import com.backbase.expert.extensions.sushi.util.CookieUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.cookie.CookieOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Created by bartv on 24/08/14.
 */
public class CreateApplicationViewStateProcessor implements Processor {


    private static final Logger LOG = LoggerFactory.getLogger(CreateApplicationViewStateProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {

        LOG.debug("Creating Application View State");
        SushiRecipe sushiRecipe = exchange.getProperty("SushiRecipe", SushiRecipe.class);
        String referer = exchange.getIn().getHeader("Referer", String.class);

        String baseUrl = exchange.getIn().getHeader("CamelHttpUrl", String.class);
        URI baseURI = new URI(baseUrl);

        RemoteRequest remoteRequest = RemoteRequestUtils.createInitialRemoteRequest(sushiRecipe, exchange);


        LOG.debug("Created Remote Request URL: {}", remoteRequest);
        ApplicationViewState applicationViewState = new ApplicationViewState(sushiRecipe, remoteRequest);
        applicationViewState.setSushiRecipe(sushiRecipe);
        applicationViewState.setRefererUrl(referer);

        CookieOrigin cookieOrigin = CookieUtils.createCookieOrigin(baseURI);

        RemoteRequestUtils.mapCookies(applicationViewState, exchange, cookieOrigin);


        exchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST, remoteRequest);
        exchange.setProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, applicationViewState);


        if (remoteRequest.isPost() && sushiRecipe.getInitialPostParameters() != null) {
            exchange.getIn().setHeader("Content-Type", SushiConstants.SUSHI_CONTENT_TYPE_URL_ENCODED);
            exchange.getIn().setHeader("SushiParameterNames", sushiRecipe.getInitialPostParameters());
        }


        LOG.debug("Created ApplicationViewState: {}", applicationViewState);

    }

}
