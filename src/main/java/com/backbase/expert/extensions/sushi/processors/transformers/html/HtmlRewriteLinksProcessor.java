package com.backbase.expert.extensions.sushi.processors.transformers.html;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import com.backbase.expert.extensions.sushi.util.LinkRewriteUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bartv on 27/08/14.
 */
public class HtmlRewriteLinksProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlRewriteLinksProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        RemoteRequest remoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);

        SushiRecipe recipe = applicationViewState.getSushiRecipe();
        Element doc = exchange.getIn().getBody(Element.class);
        makeUrlsAbsolute(applicationViewState, recipe, doc, remoteRequest);
//        makeUrlsAbsoluteUsingRegex(applicationViewState,recipe,baseUrl,doc);



        exchange.getIn().setBody(doc);


    }

    private void makeUrlsAbsolute(ApplicationViewState applicationViewState, SushiRecipe recipe, Element doc, RemoteRequest remoteRequest) {
        Elements elementsToFix = doc.select("link,script,img,a,form");
        Elements base = doc.select("base");

        if (base.size() > 0 && base.hasAttr("href")) {

            // fix urls relative to base
            base.remove();
            for (Element el : elementsToFix) {
//                if(el.hasAttr("href")) {
//                    String absoluteHref = el.attr("abs:href");
//                    el.attr("href", absoluteHref);
//                }
//                if(el.hasAttr("src")) {
//                    String absoluteSrc = el.attr("abs:src");
//                    el.attr("src", absoluteSrc);
//                }
                if (el.hasAttr("action")) {
                    String absoluteAction = el.attr("abs:action");
                    el.attr("action", absoluteAction);
                }
            }
        } else {


            // fix relative urls
//            String baseUrl = getPortalBaseUrl(proxyRequest.getURL());
            doc.setBaseUri(remoteRequest.getHostnameUrl());

            for (Element el : elementsToFix) {

                if (el.hasAttr("href")) {
                    String href = el.attr("href");
                    String absoluteHref = LinkRewriteUtils.transformRelativeUrl(href, applicationViewState, recipe, remoteRequest);
                    el.attr("href", absoluteHref);
                }
                if (el.hasAttr("src")) {
                    String src = el.attr("src");
                    String absoluteSrc = LinkRewriteUtils.transformRelativeUrl(src, applicationViewState, recipe, remoteRequest);
                    el.attr("src", absoluteSrc);
                }
                if (el.hasAttr("action")) {
                    String action = el.attr("action");
                    transformActionUrl(action, applicationViewState, el, recipe, remoteRequest);

                }
            }
        }
    }


    protected void transformActionUrl(String relativeUrl, ApplicationViewState applicationViewState, Element formElement, SushiRecipe recipe, RemoteRequest remoteRequest) {
        String transformedUrl = LinkRewriteUtils.transformRelativeUrl(relativeUrl, applicationViewState, recipe, remoteRequest);
        formElement.attr("action", transformedUrl);
//        formElement.append(createHiddenFormElement(SushiConstants.FORM_ACTION_URL, relativeUrl));
//        formElement.append(createHiddenFormElement(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, applicationViewState.getId()));
    }

//    private String createHiddenFormElement(String name, String value) {
//        return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />";
//
//    }


}
