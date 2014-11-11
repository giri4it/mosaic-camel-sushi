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
public abstract class AbstractRegExPathByProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(AbstractRegExPathByProcessor.class);

    /**
     * The parameter name used in PTC configuration to declare regular
     * expressions for transforming HTML resources.
     */
    protected static final String HTML_PATTERNS = "html";

    /**
     * The parameter name used in PTC configuration to declare regular
     * expressions for transforming JavaScript resources.
     */
    protected static final String JAVASCRIPT_PATTERNS = "javascript";

    /**
     * The parameter name used in PTC configuration to declare regular
     * expressions for transforming CSS resources.
     */
    protected static final String CSS_PATTERNS = "css";

    /**
     * The parameter name used in PTC configuration to declare regular
     * expressions for transforming TEXT/PLAIN (some AJAX responses are returned
     * as text/plain) resources.
     */
    protected static final String TEXT_PLAIN_PATTERNS = "text";

    /**
     * The parameter name used in PTC configuration to declare regular
     * expressions for transforming TEXT/XML (some AJAX responses are returned
     * as text/xml) resources.
     */
    protected static final String TEXT_XML_PATTERNS = "textXML";

    protected static final int DEFAULT_NEW_NUMBER_OF_CHARS = 2048;


    protected String transform(String body, ApplicationViewState applicationViewState, RemoteRequest remoteRequest, List<String> patterns, boolean escapeXml) throws IOException {
        Reader reader = new StringReader(body);

        int newNumberOfChars = getNewNumberOfChars();
        // a chain of RegexModifier instances is created, each created for a
        // regular expression defined in the ptc configuration file
        for (String patternItem : patterns) {
            Modifier modifier = new RegexModifier(patternItem, 0, new PathReplacingProcessor(applicationViewState.getProxyUrl(), remoteRequest.getFullUrl(), escapeXml),
                    1, newNumberOfChars);
            reader = new ModifyingReader(reader, modifier);
        }
        try {
            body = IOUtils.toString(reader);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                if (log.isWarnEnabled()) {
                    log.warn("I/O error while transforming response: " + e.getMessage(), e);
                }
            }
        }
        return body;
    }

    private int getNewNumberOfChars() {
        return DEFAULT_NEW_NUMBER_OF_CHARS;
    }

}
