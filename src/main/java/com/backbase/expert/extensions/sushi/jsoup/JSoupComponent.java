package com.backbase.expert.extensions.sushi.jsoup;

import java.util.Map;

import com.backbase.expert.extensions.sushi.processors.transformers.html.JSoupProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;

/**
 * User: bartv
 * Date: 19-08-14
 * Time: 09:04
 */
public class JSoupComponent extends DefaultComponent {
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        Processor processor = new JSoupProcessor();

        return new ProcessorEndpoint(uri,this,processor);

    }


}
