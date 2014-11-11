package com.backbase.expert.extensions.sushi.processors.transformers.html;

import java.io.InputStream;

import javax.swing.text.html.parser.Element;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * User: bartv
 * Date: 19-08-14
 * Time: 09:20
 */
public class JSoupProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {

        String currentBaseUri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        // Retrieve Input Stream from Body
        InputStream inputStream = exchange.getIn().getBody(InputStream.class);


        String contentType = exchange.getIn().getHeader("Content-Type",String.class);
        String currentEncoding = "UTF-8";
        if(contentType.contains(";")) {
            String[] parts = contentType.split(";");
            for(String part : parts) {
                if(part.startsWith("charset")) {
                    currentEncoding = StringUtils.substringAfter(part,"=");
                    // Set Content Type to UTF-8
                    contentType = contentType.replace(currentEncoding, "UTF-8");
                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, contentType);
                }
            }
        }



        String body = IOUtils.toString(inputStream,currentEncoding);
        String encodedBody = new String(body.getBytes(),"UTF-8");


        // Parse input stream with JSoup
        Document document = Jsoup.parse(encodedBody, currentBaseUri);
        // Store parsed document in the OUT
        exchange.getIn().setBody(document);

    }
}
