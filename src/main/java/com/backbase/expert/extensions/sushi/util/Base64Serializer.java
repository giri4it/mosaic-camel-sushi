package com.backbase.expert.extensions.sushi.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Class for serializing and deserializing some serializable class instance into a String.
 * <p>
 * License URLs for biz.source_code.base64Coder.Base64Coder:
 * <ul>
 * <li>EPL (Eclipse Public License), V1.0 or later	http://www.eclipse.org/legal</li>
 * <li>GNU LGPL (GNU Lesser General Public License), V2.1 or later	http://www.gnu.org/licenses/lgpl.html</li>
 * <li>GNU LGP (GNU General Public License), V2 or later	http://www.gnu.org/licenses/lgpl.html</li>
 * <li>Apache License, V2.0 or later	http://www.apache.org/licenses</li>
 * <li>BSD License	http://www.opensource.org/licenses/bsd-license.php</li>
 * </ul>
 *
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 9-7-13
 */
public class Base64Serializer implements ViewStateSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(Base64Serializer.class);

    /**
     * Read the object from Base64 string.
     *
     * @param str
     * @param useUrlEncoding
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static Object fromString(String str, boolean useUrlEncoding) {

       return new Base64Serializer().readObject(str);
    }

    /**
     * Write the object to a Base64 string.
     *
     * @param obj
     * @param useUrlEncoding
     * @return
     * @throws java.io.IOException
     */
    public static String toString(Serializable obj, boolean useUrlEncoding) {
        return new Base64Serializer().writeObject(obj);
    }

    /**
     * Checks if an Base64 encoded string is also URL encoded.
     * Base64 characters which will change during URL encoding are =, + and /.
     * This will result in a % sign
     *
     * @param base64
     * @return
     */
    public static boolean isBase64StringURLEncoded(String base64) {
        if (StringUtils.containsAny(base64, "=+/")) {
            return false;
        }
        return true;
    }

    @Override
    public String writeObject(Serializable serializable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        String str = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(serializable);
            byte[] data = baos.toByteArray();
            str = new String(Base64.encodeBase64(data), "UTF-8");
        } catch (IOException e) {
            LOG.error("Could not encode the object to a base64 string.", e);
        } finally {
            IOUtils.closeQuietly(oos);
        }
        return str;
    }

    @Override
    public Object readObject(String string) {
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            String base64String = string;
            byte[] data = Base64.decodeBase64(base64String);
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            obj = ois.readObject();
        } catch (Exception e) {
            LOG.error("Could not decode the provided base64 string. Stream will be closed, and null is returned.", e);
        } finally {
            IOUtils.closeQuietly(ois);
        }
        return obj;
    }
}
