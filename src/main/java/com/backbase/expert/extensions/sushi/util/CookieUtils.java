package com.backbase.expert.extensions.sushi.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BestMatchSpec;
import org.apache.http.impl.cookie.SushiCookieSpec;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.util.List;

/**
 * Created by bartv on 21/10/14.
 */
public class CookieUtils {

    public static CookieSpec cookieSpec = new SushiCookieSpec();

    public static String getCookieHeaderString(List<Cookie> cookies) {
        if (cookies.isEmpty()) {
            return null;
        }
        List<Header> cookieHeaders = cookieSpec.formatCookies(cookies);
        return StringUtils.join(cookieHeaders, "; ");
    }


    public static List<Cookie> parseCookies(String setCookieString, CookieOrigin cookieOrigin) throws MalformedCookieException {
        return cookieSpec.parse(new BasicHeader("Set-Cookie", setCookieString), cookieOrigin);

    }

    public static CookieOrigin createCookieOrigin(URI baseURI) {
        int port = baseURI.getPort();
        if (port == -1) {
            port = baseURI.getScheme().endsWith("https") ? 433 : 80;
        }


        CookieOrigin cookieOrigin = new CookieOrigin(
                baseURI.getHost(),
                port,
                "",
                baseURI.getScheme().equals("https"));

        return cookieOrigin;
    }
}
