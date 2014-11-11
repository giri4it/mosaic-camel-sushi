package org.apache.http.impl.cookie;

import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

import java.util.Date;

/**
 * Created by bartv on 30/10/14.
 */
public class SushiMaxAgeHandler  extends AbstractCookieAttributeHandler {

    /**
     * Parses the value of the max age attribute. Negative values are also supported and are
     * made positive in the case of Sushi.
     *
     * Negative max age attribute indicate to browsers that they need to be removed if the browser closes.
     *
     * For Sushi we'll store the cookie any way as we do not have a browser.
     * @param cookie
     * @param value
     * @throws MalformedCookieException
     */
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (value == null) {
            throw new MalformedCookieException("Missing value for max-age attribute");
        }
        int age;
        try {
            age = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new MalformedCookieException ("Invalid max-age attribute: "
                    + value);
        }
        if (age < 0) {
            age = Math.abs(age);
        }
        cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
    }


}
