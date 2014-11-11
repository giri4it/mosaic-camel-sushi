package com.backbase.expert.extensions.sushi.processors.transformers.regex;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import com.googlecode.streamflyer.core.Modifier;
import com.googlecode.streamflyer.core.ModifyingReader;
import com.googlecode.streamflyer.regex.RegexModifier;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Transforms the URLs in the fetched resources to include the proxy pipe. That
 * way all subsequent requests will go trough the pipe as well. Regular
 * expression are used for transforming the path. The response transformation is
 * performed on a character stream by using the functionality defined in the
 * streamflyer library.
 *
 * @author Lazarius
 * @see <a href=""https://code.google.com/p/streamflyer/>Streamflyer</a>
 */
public class ContentTypeBasedRegExPathByProcessor extends AbstractRegExPathByProcessor {

    private static final Logger log = LoggerFactory.getLogger(ContentTypeBasedRegExPathByProcessor.class);

    private static final String TEXT_JAVASCRIPT = "text/javascript";

    private static final String APPLICATION_JAVASCRIPT = "application/javascript";

    private static final String APPLICATION_X_JAVASCRIPT = "application/x-javascript";

    private static final String TEXT_XML = "text/xml";

    private static final String TEXT_PLAIN = "text/plain";

    private static final String TEXT_CSS = "text/css";

    private static final String TEXT_HTML = "text/html";


    private static final int DEFAULT_NEW_NUMBER_OF_CHARS = 2048;

    @Override
    public void process(Exchange exchange) throws Exception {
        String contentType = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);
        if (contentType == null) {
            return;
        }
        if (StringUtils.containsIgnoreCase(contentType, TEXT_HTML)) {
            transform(exchange, HTML_PATTERNS);
        } else if (isJavascriptContentType(contentType)) {
            transform(exchange, JAVASCRIPT_PATTERNS);
        } else if (StringUtils.containsIgnoreCase(contentType, TEXT_CSS)) {
            transform(exchange, CSS_PATTERNS);
        } else if (StringUtils.containsIgnoreCase(contentType, TEXT_PLAIN)) {
            transform(exchange, TEXT_PLAIN_PATTERNS);
        } else if (StringUtils.containsIgnoreCase(contentType, TEXT_XML)) {
            transform(exchange, TEXT_XML_PATTERNS, true);
        }
    }


    /**
     * @param contentType
     * @return
     */
    private boolean isJavascriptContentType(String contentType) {
        return StringUtils.containsIgnoreCase(contentType, APPLICATION_X_JAVASCRIPT)
                || StringUtils.containsIgnoreCase(contentType, APPLICATION_JAVASCRIPT)
                || StringUtils.containsIgnoreCase(contentType, TEXT_JAVASCRIPT);
    }

    /**
     * @param parametersKey
     */
    private void transform(Exchange exchange, String parametersKey) throws IOException {
        transform(exchange, parametersKey, false);
    }

    /**
     * @param parametersKey
     * @param escapeXml
     */
    private void transform(Exchange exchange, String parametersKey, boolean escapeXml) throws IOException {

        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        SushiRecipe recipe = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class).getSushiRecipe();
        RemoteRequest remoteRequest = exchange.getProperty(SushiConstants.SUSHI_REMOTE_REQUEST, RemoteRequest.class);
        List<String> patterns = recipe.getTransformerPatterns(parametersKey);


        if (patterns != null && !patterns.isEmpty()) {

            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
            String encoding = exchange.getIn().getHeader(Exchange.CHARSET_NAME, String.class);

            String body = IOUtils.toString(inputStream, encoding);

            body = transform(body, applicationViewState, remoteRequest, patterns, false);

            exchange.getIn().setBody(body);
        }
    }

    private int getNewNumberOfChars() {
        return DEFAULT_NEW_NUMBER_OF_CHARS;
    }

}
