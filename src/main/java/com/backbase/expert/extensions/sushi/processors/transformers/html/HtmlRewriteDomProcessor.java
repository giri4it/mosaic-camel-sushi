package com.backbase.expert.extensions.sushi.processors.transformers.html;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by bartv on 27/08/14.
 */
public class HtmlRewriteDomProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlRewriteDomProcessor.class);

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public void process(Exchange exchange) throws Exception {

        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        SushiRecipe recipe = applicationViewState.getSushiRecipe();
        if (recipe == null) {
            LOG.info("No Sushi Recipe found. Leaving HTML as is");
            return;
        }

        Document document = exchange.getIn().getBody(Document.class);
        Element body = document.body();
        Element head = document.head();
        Elements headScripts = head.select("script");
        Elements bodyScripts = body.select("script");

        Set<Element> resultScripts = new LinkedHashSet<Element>();

        Element resultNode = extractResultNode(recipe, document, body);
        if (resultNode == null) {
//            throw new Exception("Could not extract result node from result node selector: " +  recipe.getHtmlResultNodeSelector());
            resultNode = body;
        }
        filterResultNode(resultNode, recipe);
        transferCssLinks(recipe, document, resultNode);
        transferHtmlClasses(recipe, body, resultNode);
        transferBodyClasses(recipe, body, resultNode);
        transferHeadScripts(recipe, headScripts, resultScripts);
        transferBodyScripts(recipe, bodyScripts, resultScripts);
        moveScriptsToEnd(recipe, resultNode, resultScripts);
        resultNode =  applyCssPrefixToResultNode(recipe, resultNode);

        exchange.getIn().setBody(resultNode);
    }

    private Element applyCssPrefixToResultNode(SushiRecipe recipe, Element resultNode) {
        String cssPrefix = recipe.getCssPrefix();
        if (StringUtils.isEmpty(cssPrefix)) {
            return resultNode;
        }
        resultNode.wrap("<div class=\"" + cssPrefix + "\"></div>");
        return resultNode.parent();
    }

    private void filterResultNode(Element resultNode, SushiRecipe recipe) {
        if (!recipe.getHtmlResultNodeFilter().isEmpty()) {
            for (String filterSelector : recipe.getHtmlResultNodeFilter()) {
                Elements filteredNodes = resultNode.select(filterSelector);
                for (Element filteredNode : filteredNodes) {
                    filteredNode.remove();
                }
            }
        } else {
            LOG.debug("No result node filters specified");
        }
    }

    private Element extractResultNode(SushiRecipe recipe, Document document, Element body) {
        Element resultNode = body;

        LOG.debug("Select result node with selector: {}", recipe.getHtmlResultNodeSelector());
        if (StringUtils.isNotEmpty(recipe.getHtmlResultNodeSelector())) {
            resultNode = document.select(recipe.getHtmlResultNodeSelector()).first();
            LOG.trace("Result node: {}", resultNode);
        } else {
            LOG.info("Result node selector not specified. Returning body instead");
        }
        return resultNode;
    }


    private void transferCssLinks(SushiRecipe recipe, Document document, Element resultNode) {
        Elements cssImports = document.select("link[href]");
        Set<Element> result = new LinkedHashSet<Element>();
        if (recipe.isTransferHeadScriptsToResultNode()) {
            LOG.trace("Transferring CSS Imports to Result Node with  CSS Import Filter: {}", recipe.getCssIncludeFilter());
            for (Element cssImport : cssImports) {
                String src = cssImport.attr("href");
                if (antPathMatcher.match(recipe.getCssIncludeFilter(), src)) {
                    LOG.trace("Css Link : {} matches pattern: {}", src, recipe.getCssIncludeFilter());
                    result.add(cssImport);
                } else {
                    LOG.trace("Css Link: {} does not match pattern: {}. Excluding link", src, recipe.getCssIncludeFilter());
                }
            }
            LOG.trace("Css Links added to result node: {}", result);
            resultNode.insertChildren(0, cssImports);
        } else {
            LOG.trace("No Css Links  transferred to result node");
        }
    }

    private void transferHeadScripts(SushiRecipe recipe, Elements headScripts, Set<Element> resultScripts) {

        if (recipe.isTransferHeadScriptsToResultNode()) {
            LOG.trace("Transferring Head Scripts to Result Node with Head Script Filter: {}", recipe.getHeaderScriptIncludeFilter());
            for (Element headScript : headScripts) {
                String src = headScript.attr("src");
                if (StringUtils.isEmpty(src) && recipe.isTransferInlineHeadScriptsToResultNode()) {
                    LOG.trace("Transferring inline head script: {}", headScript);
                    resultScripts.add(headScript);
                } else if (antPathMatcher.match(recipe.getHeaderScriptIncludeFilter(), src)) {
                    LOG.trace("Head script: {} matches pattern: {}", src, recipe.getHeaderScriptIncludeFilter());
                    resultScripts.add(headScript);
                } else {
                    LOG.trace("Head script: {} does not match pattern: {}. Excluding script", src, recipe.getHeaderScriptIncludeFilter());
                }
            }
            LOG.trace("Head Scripts added to result node: {}", resultScripts);
        } else {
            LOG.trace("No Head Scripts transferred to result node");
            headScripts.remove();
        }
    }

    private void transferBodyScripts(SushiRecipe recipe, Elements bodyScripts, Set<Element> resultScripts) {
        if (recipe.isTransferBodyScriptsToResultNode()) {
            LOG.trace("Transferring Body Scripts to Result Node with Body Script filter: {}", recipe.getBodyScriptIncludeFilter());
            for (Element bodyScript : bodyScripts) {
                String src = bodyScript.attr("src");
                if (StringUtils.isEmpty(src) && recipe.isTransferInlineBodyScriptsToResultNode()) {
                    LOG.trace("Transferring inline body script: {}", bodyScript);
                    resultScripts.add(bodyScript);
                } else
                    LOG.trace("Body script: {} matches pattern: {}", src, recipe.getBodyScriptIncludeFilter());
                if (antPathMatcher.match(recipe.getBodyScriptIncludeFilter(), src)) {
                    LOG.trace("Body script: {} matches pattern: {}", src, recipe.getBodyScriptIncludeFilter());
                    resultScripts.add(bodyScript);
                } else {
                    LOG.trace("Body script: {} does not match pattern: {}. Excluding script", src, recipe.getBodyScriptIncludeFilter());
                }
            }
        } else {
            bodyScripts.remove();
        }
    }

    private void transferHtmlClasses(SushiRecipe recipe, Element body, Element resultNode) {
        if (recipe.isTransferHtmlClassesToResultNode()) {
            String htmlClasses = body.className();
            resultNode.addClass(htmlClasses);
        }
    }

    private void transferBodyClasses(SushiRecipe recipe, Element body, Element resultNode) {
        if (recipe.isTransferBodyClassesToResultNode()) {
            String bodyClasses = body.className();
            resultNode.addClass(bodyClasses);
        }
    }

    private void moveScriptsToEnd(SushiRecipe recipe, Element resultNode, Set<Element> resultScripts) {
        if (recipe.isMoveScriptsToEnd()) {
            for (Element script : resultScripts) {
                resultNode.appendChild(script);
            }
        } else {
            resultNode.insertChildren(0, resultScripts);
        }
    }


}
