package com.backbase.expert.extensions.sushi.processors.transformers.javascript;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by bartv on 03/11/14.
 */
public class ExternalJavaScriptProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalJavaScriptProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.info("Processing External Javascript: {}", exchange.getIn().getHeader(Exchange.HTTP_PATH));
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        SushiRecipe recipe = applicationViewState.getSushiRecipe();

        String currentEncoding = exchange.getIn().getHeader(Exchange.CHARSET_NAME, String.class);
        String contentType = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);

        String encoding = "UTF-8";
        if (recipe.getExternalResourceEncoding() != null) {
            encoding = recipe.getExternalResourceEncoding();
        }

        if (currentEncoding == null) {
            if (!contentType.contains("charset")) {
                contentType += ";charset=" + encoding;
                exchange.getIn().setHeader(Exchange.CONTENT_TYPE, contentType);
            }
            exchange.getIn().setHeader(Exchange.CHARSET_NAME, encoding);
        }

    }
}
