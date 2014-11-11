package com.backbase.expert.extensions.sushi;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.backbase.expert.extensions.sushi.SushiConstants.*;

/**
 * User: bartv Date: 21-06-14 Time: 11:59
 */
public class SushiTests extends CamelSpringTestSupport {

    @Override
    public CamelContext context() {
        CamelContext camelContext = super.context();
        camelContext.setTracing(true);
        return camelContext;
    }

    @Test
    public void testStartup() {
    }


    @Test
    public void testNewApplicationViewState() {

        Map<String, Object> headers = new HashMap<String, Object>();
        // Sushi Recipe
        headers.put(SUSHI_REMOTE_APPLICATION_URL, "https://www.degroof.be/sites/degroof/en-US/fonds/Pages/Fondsdeplacement.aspx");
        headers.put(SUSHI_REMOTE_HTTP_METHOD, "GET");

        headers.put(SUSHI_HTML_RESULT_NODE_SELECTOR, "div#center");
        headers.put(SUSHI_HTML_RESULT_NODE_FILTER, ".CheckBox");

        headers.put(SUSHI_TRANSFER_BODY_ATTRIBUTES_TO_RESULT_NODE, true);
        headers.put(SUSHI_TRANSFER_BODY_CLASSES_TO_RESULT_NODE, true);
        headers.put(SUSHI_TRANSFER_HTML_CLASSES_TO_RESULT_NODE, true);

        headers.put(SUSHI_TRANSFER_CSS_LINKS_TO_RESULT_NODE, true);

        headers.put(SUSHI_TRANSFER_HEAD_SCRIPTS_TO_RESULT_NODE1, false);
        headers.put(SUSHI_TRANSFER_INLINE_HEAD_SCRIPTS_TO_RESULT_NODE, false);

        headers.put(SUSHI_TRANSFER_BODY_SCRIPTS_TO_RESULT_NODE, false);
        headers.put(SUSHI_TRANSFER_INLINE_BODY_SCRIPTS_TO_RESULT_NODE, false);

        headers.put(SUSHI_MOVE_SCRIPTS_TO_END, true);
        headers.put(SUSHI_MOVE_INLINE_SCRIPTS_TO_END, false);

        headers.put(SUSHI_HEAD_SCRIPT_EXCLUDE_FILTER, "");
        headers.put(SUSHI_HEAD_SCRIPT_INCLUDE_FILTER, "/**/*.js");

        headers.put(SUSHI_BODY_SCRIPT_EXCLUDE_FILTER, "");
        headers.put(SUSHI_BODY_SCRIPT_INCLUDE_FILTER, "/**/*.js");

        headers.put(SUSHI_CSS_INCLUDE_FILTER, "/**/*.css");
        headers.put(SUSHI_CSS_EXCLUDE_FILTER, "");

        headers.put(SUSHI_CSS_PREFIX, "nu");

        // Headers set by Restlets
        headers.put(Exchange.HTTP_PATH, "/sushi");
        headers.put(SUSHI_SERVLET_PATH, "/services");
        headers.put(SUSHI_CONTEXT_PATH, "/portalserver");

        // Standard HTTP Headers
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("Accept-Language", "en,nl;q=0.8,en-GB;q=0.6,en-US;q=0.4,ar;q=0.2,de;q=0.2");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Cookie", "anonymousUserId=2296ca37-9c61-4c73-963d-685c9126fec7; JSESSIONID=1gyfdec8vk6dg1097up4fif3qi; sessionLastRequest=1408949293617; redirectPortal=dashboard; BBTracking=\"Mw==\"");
        headers.put("Host", "localhost:7777");
        headers.put("Origin", "http://localhost:7777");
        headers.put("referer", "http://localhost:7777/portalserver/batman/index?designmode=true&useragent=undefined");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
        headers.put("Authorization", "Basic ZGVtbzoxMjM0NTY=");


        Element response = template.requestBodyAndHeaders("direct:sushi/html", null, headers, Element.class);
        System.out.println(response);
        Elements scripts = response.select("script");
        for (Element script : scripts) {
            String src = script.attr("src");
            headers = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(src)) {
                String viewStateId = src.replaceFirst("http://portalserver:7777/portalserver/services/rest/sushi/", "");
                viewStateId = viewStateId.substring(0, viewStateId.indexOf("/"));

                String remotePath = src.substring(src.lastIndexOf(viewStateId)).replaceFirst(viewStateId, "");
                headers.put(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, viewStateId);
                // Here I should filter the remote http path
                headers.put(SushiConstants.SUSHI_REMOTE_APPLICATION_URL, remotePath);
                Object linkedResource = template.requestBodyAndHeaders("direct://sushi/w00t", null, headers, Object.class);
                System.out.println(linkedResource);
            }
        }

    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/backbase-mashup.xml");
    }
}
