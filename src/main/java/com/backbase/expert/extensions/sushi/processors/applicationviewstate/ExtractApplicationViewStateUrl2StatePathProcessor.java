package com.backbase.expert.extensions.sushi.processors.applicationviewstate;

import com.backbase.expert.extensions.sushi.SushiConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bartv on 23/10/14.
 */
public class ExtractApplicationViewStateUrl2StatePathProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractApplicationViewStateUrl2StatePathProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();


        String camelHttpPath = in.getHeader(Exchange.HTTP_PATH, String.class);
        LOG.info("Processing url2state url: {}", camelHttpPath);
        if (camelHttpPath.contains("//")) {
            String url2state = StringUtils.substringAfter(camelHttpPath, "//");
            String[] parts = url2state.split("/");

            String applicationViewStateId = parts[0];
            String remoteRequestId = parts[1];
            LOG.debug("Found applicationViewStateId: {} and RemoteRequestId: {}", applicationViewStateId, remoteRequestId);
            if (parts.length >= 2) {
                in.setHeader(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, applicationViewStateId);
                in.setHeader(SushiConstants.SUSHI_REMOTE_REQUEST_ID, remoteRequestId);
            }
        }
    }
}
