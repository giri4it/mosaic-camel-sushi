package com.backbase.expert.extensions.sushi.processors;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import com.backbase.expert.extensions.sushi.processors.transformers.javascript.ExternalJavaScriptProcessor;
import com.backbase.expert.extensions.sushi.processors.transformers.regex.ContentTypeBasedRegExPathByProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bartv on 03/11/14.
 */
public class TestExternalJavascriptRewriteProcessor extends CamelTestSupport {


    @Test
    public void testRewriteJavaScriptPaths() throws Exception {
//        byte[] input = IOUtils.toByteArray(getClass().getResourceAsStream("/javascripts/bossjavalib.js"));
//        System.out.println(new String(input, "SJIS"));

        DefaultExchange exchange = new DefaultExchange(context());
        exchange.setProperty(SushiConstants.SUSHI_REMOTE_REQUEST, new RemoteRequest("GET", new URI("http://blabla"), new HashMap<String, List<String>>()));
        exchange.setProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, createApplicationViewState());
        InputStream resourceAsStream = getClass().getResourceAsStream("/javascripts/paths.js");
        String body = IOUtils.toString(resourceAsStream,"UTF-8");
        exchange.getIn().setBody(getClass().getResourceAsStream("/javascripts/paths.js"));
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/x-javascript");
        System.out.println("**************************BEFORE**********************");
        System.out.println(body);

        template.send("direct:start", exchange);

        System.out.println("**************************AFTER**********************");
        System.out.println(exchange.getIn().getBody());


    }

    private SushiRecipe createSushiRecipe() throws IOException {
        SushiRecipe recipe = new ObjectMapper().readValue(getClass().getResourceAsStream("/recipes/new-payment.json"), SushiRecipe.class);
        recipe.setServletPath("/services");
        recipe.setContextPath("/portalserver");

        return recipe;
    }

    private ApplicationViewState createApplicationViewState() throws Exception {


        RemoteRequest remoteRequest = new RemoteRequest("GET", new URI("https://o2o.sbtst2.moneykit.net/TDGate010300/gate/NBW010300/&NBPO2OTMPG01"), new HashMap<String, List<String>>());
        ApplicationViewState applicationViewState = new ApplicationViewState(createSushiRecipe(), remoteRequest);

        return applicationViewState;
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        return new RouteBuilder() {
            public void configure() {
                from("direct:start").process(new ExternalJavaScriptProcessor()).process(new ContentTypeBasedRegExPathByProcessor());
            }
        };
    }
}
