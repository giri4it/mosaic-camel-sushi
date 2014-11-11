package com.backbase.expert.extensions.sushi.css.converter;

import com.steadystate.css.parser.CSSOMParser;
import org.apache.camel.Converter;
import org.apache.camel.converter.stream.CachedOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * User: bartv
 * Date: 20-08-14
 * Time: 17:27
 */
@Converter
public class CssBodyConvertor {

    static CSSOMParser parser = new CSSOMParser();

    private static final String COMMENT_REGEX = "(/\\*[^*]*\\*+([^/][^*]*\\*+)*/)";

    private static final Pattern COMMENTS_PATTERN = Pattern.compile(
            COMMENT_REGEX, Pattern.DOTALL);

    private static final Logger LOG = LoggerFactory.getLogger(CssBodyConvertor.class);



    @Converter
    public static String toString(CSSStyleSheet styleSheet) {
        return styleSheet.toString();
    }

    @Converter
    public static CSSStyleSheet toCSSStyleSheet(InputStream inputStream) throws IOException {
        LOG.debug("Converting input stream to CSSStyleSheet");

//        String css = IOUtils.toString(inputStream);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bytes.length < 3)
            return null;
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b == '\n' || b == ';') {
                index = b == '\n' ? i : i + 1;
                break;
            } else {
                baos.write(b);
            }
        }


        String firstLine = baos.toString();
        String encoding = "UTF-8";
        if (firstLine.startsWith("@charset")) {
            LOG.debug("First line contains @charset. Extracting encoding and removing first line from CSS");

            encoding = StringUtils.substringBetween(firstLine, "\"");
            bytes = Arrays.copyOfRange(bytes, index, bytes.length);
        }


        String css = new String(bytes, encoding);

        LOG.trace("Removing comments from CSS: {}", css);
        String cssWithoutComments = COMMENTS_PATTERN.matcher(css).replaceAll("");
        LOG.trace("Comments removed: {}", cssWithoutComments);
        // Get first line to determine charset if set

        LOG.debug("Trying to parse Style Sheet");

        CSSStyleSheet cssStyleSheet = null;
        try {
            cssStyleSheet = parser.parseStyleSheet(new InputSource(new StringReader(cssWithoutComments)), null, null);
        } catch (Exception e) {
            LOG.warn("Unable to parse Style Sheet due to: {}", e.getMessage());
        }
        if(cssStyleSheet == null) {
            LOG.warn("Unable to parse Style Sheet");
        }else {
            LOG.debug("Style Sheet parsed with {} rules", cssStyleSheet.getCssRules());
        }

        return cssStyleSheet;
    }

    @Converter
    public static CSSStyleSheet toCssStyleSheet(CachedOutputStream cachedOutputStream) throws IOException {
        return toCSSStyleSheet(cachedOutputStream.getInputStream());
    }
}
