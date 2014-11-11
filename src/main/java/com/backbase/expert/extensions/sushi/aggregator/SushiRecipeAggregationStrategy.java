package com.backbase.expert.extensions.sushi.aggregator;

import com.backbase.expert.extensions.sushi.SushiRecipe;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Arrays;

import static com.backbase.expert.extensions.sushi.SushiConstants.*;

/**
 * Created by bartv on 28/10/14.
 */
public class SushiRecipeAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange exchange, Exchange sushiExchange) {


        SushiRecipe recipe = sushiExchange.getIn().getBody(SushiRecipe.class);
        Message in = exchange.getIn();

        String remoteApplicationUrl = in.getHeader(SUSHI_REMOTE_APPLICATION_URL, String.class);
        String remoteHttpMethod = in.getHeader(SUSHI_REMOTE_HTTP_METHOD, String.class);
        String htmlResultNodeSelector = in.getHeader(SUSHI_HTML_RESULT_NODE_SELECTOR, String.class);
        String htmlResultNodeFilter = in.getHeader(SUSHI_HTML_RESULT_NODE_FILTER, String.class);
        Boolean transferBodyAttributesToResultNode = in.getHeader(SUSHI_TRANSFER_BODY_ATTRIBUTES_TO_RESULT_NODE,  Boolean.class);
        Boolean transferBodyClassesToResultNode = in.getHeader(SUSHI_TRANSFER_BODY_CLASSES_TO_RESULT_NODE,  Boolean.class);
        Boolean transferHtmlClassesToResultNode = in.getHeader(SUSHI_TRANSFER_HTML_CLASSES_TO_RESULT_NODE,  Boolean.class);
        Boolean transferBodyScriptsToResultNode = in.getHeader(SUSHI_TRANSFER_BODY_SCRIPTS_TO_RESULT_NODE,  Boolean.class);
        Boolean transferInlineBodyScriptsToResultNode = in.getHeader(SUSHI_TRANSFER_INLINE_BODY_SCRIPTS_TO_RESULT_NODE,  Boolean.class);
        Boolean transferHeadScriptsToResultNode = in.getHeader(SUSHI_TRANSFER_HEAD_SCRIPTS_TO_RESULT_NODE1,  Boolean.class);
        Boolean transferInlineHeadScriptsToResultNode = in.getHeader(SUSHI_TRANSFER_INLINE_HEAD_SCRIPTS_TO_RESULT_NODE,  Boolean.class);
        Boolean moveScriptsToEnd = in.getHeader(SUSHI_MOVE_SCRIPTS_TO_END,  Boolean.class);
        Boolean moveInlineScriptsToEnd = in.getHeader(SUSHI_MOVE_INLINE_SCRIPTS_TO_END,  Boolean.class);
        String headerScriptIncludeFilter = in.getHeader(SUSHI_HEAD_SCRIPT_INCLUDE_FILTER, String.class);
        String headerScriptExcludeFilter = in.getHeader(SUSHI_HEAD_SCRIPT_EXCLUDE_FILTER, String.class);
        String bodyScriptIncludeFilter = in.getHeader(SUSHI_BODY_SCRIPT_INCLUDE_FILTER, String.class);
        String bodyScriptExcludeFilter = in.getHeader(SUSHI_BODY_SCRIPT_EXCLUDE_FILTER, String.class);
        String cssIncludeFilter = in.getHeader(SUSHI_CSS_INCLUDE_FILTER, String.class);
        String cssExcludeFilter = in.getHeader(SUSHI_CSS_EXCLUDE_FILTER, String.class);
        String cssPrefix = in.getHeader(SUSHI_CSS_PREFIX, String.class);
        Boolean transferCssLinksToResultNode = in.getHeader(SUSHI_TRANSFER_CSS_LINKS_TO_RESULT_NODE,  Boolean.class);

        String servletPath = in.getHeader(SUSHI_SERVLET_PATH, String.class);
        String contextPath = in.getHeader(SUSHI_CONTEXT_PATH, String.class);

        String cookiesToTransfer = in.getHeader(SUSHI_COOKIES_TO_TRANSFER, String.class);
        String headersToTransfer = in.getHeader(SUSHI_HEADERS_TO_TRANSFER, String.class);


        if (remoteApplicationUrl != null)
            recipe.setInitialRemoteApplicationUrl(remoteApplicationUrl);
        if (remoteHttpMethod != null)
            recipe.setInitialRemoteHttpMethod(remoteHttpMethod);
        if (htmlResultNodeSelector != null)
            recipe.setHtmlResultNodeSelector(htmlResultNodeSelector);
        if (htmlResultNodeFilter != null)
            recipe.setHtmlResultNodeFilter(Arrays.asList(htmlResultNodeFilter.split("|")));
        if (transferBodyAttributesToResultNode != null)
            recipe.setTransferBodyAttributesToResultNode(transferBodyAttributesToResultNode);
        if (transferBodyClassesToResultNode != null)
            recipe.setTransferBodyClassesToResultNode(transferBodyClassesToResultNode);
        if (transferHtmlClassesToResultNode != null)
            recipe.setTransferHtmlClassesToResultNode(transferHtmlClassesToResultNode);
        if (transferBodyScriptsToResultNode != null)
            recipe.setTransferBodyScriptsToResultNode(transferBodyScriptsToResultNode);
        if (transferInlineBodyScriptsToResultNode != null)
            recipe.setTransferInlineBodyScriptsToResultNode(transferInlineBodyScriptsToResultNode);
        if (transferHeadScriptsToResultNode != null)
            recipe.setTransferHeadScriptsToResultNode(transferHeadScriptsToResultNode);
        if (transferInlineHeadScriptsToResultNode != null)
            recipe.setTransferInlineHeadScriptsToResultNode(transferInlineHeadScriptsToResultNode);

        if (moveScriptsToEnd != null)
            recipe.setMoveScriptsToEnd(moveScriptsToEnd);
        if (moveInlineScriptsToEnd != null)
            recipe.setMoveInlineScriptsToEnd(moveInlineScriptsToEnd);
        if (headerScriptExcludeFilter != null)
            recipe.setHeaderScriptIncludeFilter(headerScriptIncludeFilter);
        if (headerScriptExcludeFilter != null)
            recipe.setHeaderScriptExcludeFilter(headerScriptExcludeFilter);
        if (bodyScriptIncludeFilter != null)
            recipe.setBodyScriptIncludeFilter(bodyScriptIncludeFilter);
        if (bodyScriptExcludeFilter != null)
            recipe.setBodyScriptExcludeFilter(bodyScriptExcludeFilter);
        if (cssIncludeFilter != null)
            recipe.setCssIncludeFilter(cssIncludeFilter);
        if (cssExcludeFilter != null)
            recipe.setCssExcludeFilter(cssExcludeFilter);
        if (cssPrefix != null)
            recipe.setCssPrefix(cssPrefix);
        if (transferCssLinksToResultNode != null)
            recipe.setTransferCssLinksToResultNode(transferCssLinksToResultNode);


        if (cookiesToTransfer != null)
            recipe.setInitialCookiesToTransfer(cookiesToTransfer);
        if (headersToTransfer != null)
            recipe.setInitialHeadersToTransfer(headersToTransfer);

        recipe.setServletPath(servletPath);
        recipe.setContextPath(contextPath);

        exchange.setProperty("SushiPopulateHeaders", recipe.getInitialHttpHeadersToTransfer());
        exchange.setProperty(SUSHI_RECIPE, recipe);

        return exchange;


    }
}
