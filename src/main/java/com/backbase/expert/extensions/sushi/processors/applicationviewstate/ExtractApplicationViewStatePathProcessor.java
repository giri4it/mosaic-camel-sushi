package com.backbase.expert.extensions.sushi.processors.applicationviewstate;

import com.backbase.expert.extensions.sushi.SushiConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;

/**
 * Created by bartv on 23/10/14.
 */
public class ExtractApplicationViewStatePathProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();


        String camelHttpPath = in.getHeader(Exchange.HTTP_PATH, String.class);

        String applicationViewStateId = StringUtils.substringBetween(camelHttpPath,"proxy/","/");
        in.setHeader(SushiConstants.SUSHI_APPLICATION_VIEW_STATE_ID, applicationViewStateId);

    }
}
