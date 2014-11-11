package com.backbase.expert.extensions.sushi.processors.http;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.SushiConstants;
import com.backbase.expert.extensions.sushi.util.CookieUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bartv on 21/10/14.
 */
public class IncomingCookiesProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingCookiesProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationViewState applicationViewState = exchange.getProperty(SushiConstants.SUSHI_APPLICATION_VIEW_STATE, ApplicationViewState.class);
        CookieOrigin cookieOrigin = exchange.getProperty("SushiCookieOrigin", CookieOrigin.class);

        ArrayList<String> setCookieString = exchange.getIn().getHeader("Set-Cookie", ArrayList.class);
        ArrayList<String> setCookie2String = exchange.getIn().getHeader("Set-Cookie2", ArrayList.class);


        if (setCookieString != null) {

            for (String incomingCookie : setCookieString) {
                LOG.info("Processing incoming cookie: {}", incomingCookie);
                List<Cookie> cookies = CookieUtils.parseCookies(incomingCookie, cookieOrigin);
                applicationViewState.addCookies(cookies);
            }
        }

        if (setCookie2String != null) {
            for (String incomingCookie : setCookie2String) {
                LOG.info("Processing incoming cookie: {}", incomingCookie);
                List<Cookie> cookies = CookieUtils.parseCookies(incomingCookie, cookieOrigin);
                applicationViewState.addCookies(cookies);
            }
        }
    }
}
