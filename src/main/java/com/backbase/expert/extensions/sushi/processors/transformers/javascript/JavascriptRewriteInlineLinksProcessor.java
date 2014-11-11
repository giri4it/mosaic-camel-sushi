package com.backbase.expert.extensions.sushi.processors.transformers.javascript;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import com.backbase.expert.extensions.sushi.processors.transformers.regex.AbstractRegExPathByProcessor;
import com.backbase.expert.extensions.sushi.util.LinkRewriteUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bartv on 27/08/14.
 */
public class JavascriptRewriteInlineLinksProcessor extends AbstractRegExPathByProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(JavascriptRewriteInlineLinksProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        RemoteRequest remoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);
        SushiRecipe recipe = applicationViewState.getSushiRecipe();

        if (recipe == null) {
            LOG.debug("No recipe found. Exiting processor ");
        }

        LOG.debug("Start rewrite JavascriptInlineLinks Processor");
        Element resultNode = exchange.getIn().getBody(Element.class);
        Elements elements = resultNode.select("script");
        for (Element scriptElement : elements) {
            if (!scriptElement.hasAttr("src")) {
                rewriteInlineScript(scriptElement, applicationViewState, recipe, remoteRequest);
            }
        }

        LOG.debug("Finished rewrite JavascriptInlineLinks Processor");

    }

    private void rewriteInlineScript(Element scriptElement, ApplicationViewState applicationViewState, SushiRecipe recipe, RemoteRequest remoteRequest) throws IOException {

        List<String> patterns = recipe.getTransformerPatterns(JAVASCRIPT_PATTERNS);
        if(patterns == null)
            return;

        for (DataNode node : scriptElement.dataNodes()) {
            String script = node.getWholeData();

            LOG.info("Transforming script: {}", script);
            script = transform(script, applicationViewState, remoteRequest, patterns, false);
            LOG.info("Transformed script: {}", script);
            node.setWholeData(script);
        }

    }
}
