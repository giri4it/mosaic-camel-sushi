package com.backbase.expert.extensions.sushi.binding;


import org.apache.camel.component.http.DefaultHttpBinding;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.component.http.helper.HttpHelper;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by bartv on 23/10/14.
 */
public class SushiHttpBinding extends DefaultHttpBinding{

    private static final Logger LOG = LoggerFactory.getLogger(SushiHttpBinding.class);

    protected void populateRequestParameters(HttpServletRequest request, HttpMessage message) throws Exception {

        //we populate the http request parameters without checking the request method
        Map<String, Object> headers = message.getHeaders();
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String)names.nextElement();
            // there may be multiple values for the same name
            String[] values = request.getParameterValues(name);
            LOG.trace("HTTP parameter {} = {}", name, values);

            if (values != null) {
                for (String value : values) {
                    if (getHeaderFilterStrategy() != null
                            && !getHeaderFilterStrategy().applyFilterToExternalHeaders(name, value, message.getExchange())) {
                        HttpHelper.appendHeader(headers, name, value);
                    }
                }
            }
        }

        LOG.trace("HTTP method {} with Content-Type {}", request.getMethod(), request.getContentType());




        if (request.getMethod().equals("POST") && request.getContentType() != null
                && request.getContentType().startsWith(HttpConstants.CONTENT_TYPE_WWW_FORM_URLENCODED)) {
            List<String> postParameterNames = new ArrayList<String>();
            String charset = request.getCharacterEncoding();
            if (charset == null) {
                charset = "UTF-8";
            }
            // Push POST form params into the headers to retain compatibility with DefaultHttpBinding
            String body = message.getBody(String.class);
            if (ObjectHelper.isNotEmpty(body)) {
                for (String param : body.split("&")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        String name = URLDecoder.decode(pair[0], charset);
                        String value = URLDecoder.decode(pair[1], charset);
                        postParameterNames.add(name);
                        if (getHeaderFilterStrategy() != null
                                && !getHeaderFilterStrategy().applyFilterToExternalHeaders(name, value, message.getExchange())) {
                            HttpHelper.appendHeader(headers, name, value);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid parameter, expected to be a pair but was " + param);
                    }
                }
            }

            message.setHeader("SushiParameterNames", postParameterNames);
        }
    }



}
