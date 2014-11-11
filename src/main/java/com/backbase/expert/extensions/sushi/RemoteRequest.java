package com.backbase.expert.extensions.sushi;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.UrlUtils;

import java.io.Serializable;
import java.net.URI;
import java.util.*;

/**
 * Created by bartv on 19/10/14.
 */
public class RemoteRequest implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteRequest.class);


    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

    //~ Instance fields ================================================================================================

    private final String id;
    private final ArrayList<Locale> locales = new ArrayList<Locale>();
    private final Map<String, List<String>> headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, String[]> parameters = new TreeMap<String, String[]>(String.CASE_INSENSITIVE_ORDER);
    private final String method;
    private final String queryString;
    private final String requestURI;
    private final String scheme;
    private final String serverName;
    private final int serverPort;

    // Temp Instance Fields;
    private String baseUrl;
    private String fullUrl;
    private String hostnameUrl;


    //~ Constructors ===================================================================================================



    public RemoteRequest(String id,  String remoteHttpMethod, URI remoteApplicationUrl, Map<String, List<String>> headers) {
        this.id = id == null ?  RandomStringUtils.randomAlphabetic(5) : id;
        this.method = remoteHttpMethod;
        this.queryString = remoteApplicationUrl.getQuery();
        this.scheme = remoteApplicationUrl.getScheme();
        this.serverName = remoteApplicationUrl.getHost();
        this.requestURI = remoteApplicationUrl.getPath();
        if (remoteApplicationUrl.getPort() != -1) {
            this.serverPort = remoteApplicationUrl.getPort();
        } else {
            this.serverPort = "http".equals(scheme) ? 80 : 443;
        }
        this.headers.putAll(headers);

    }

    public RemoteRequest(String initialRemoteHttpMethod, URI remoteApplicationUrl, Map<String, List<String>> incomingHeaders) {
        this(null,initialRemoteHttpMethod,remoteApplicationUrl,incomingHeaders);
    }

//    public RemoteRequest(String remoteHttpMethod, String remoteHost, String remoteHttpPath) {
//
//    }

    //~ Methods ========================================================================================================

    private void addHeader(String name, String value) {
        List<String> values = headers.get(name);

        if (values == null) {
            values = new ArrayList<String>();
            headers.put(name, values);
        }

        values.add(value);
    }


    private void addLocale(Locale locale) {
        locales.add(locale);
    }

    private void addParameter(String name, String[] values) {
        parameters.put(name, values);
    }


    public String getFullUrlWithoutQueryString() {
        if (baseUrl == null) {
            baseUrl = UrlUtils.buildFullRequestUrl(scheme, serverName, serverPort, requestURI, null);
        }
        return baseUrl;
    }

    public String getBasePath() {

        String basePath = StringUtils.substringBefore(requestURI, "/");
        return UrlUtils.buildFullRequestUrl(scheme, serverName, serverPort, basePath, null);

    }

    public String getHostnameUrl() {
        if (hostnameUrl == null) {
            hostnameUrl = UrlUtils.buildFullRequestUrl(scheme, serverName, serverPort, "", null);
        }
        return hostnameUrl;
    }

    /**
     * Get Full URL
     *
     * @return the full URL of this request
     */
    public String getFullUrl() {
        if (fullUrl == null) {
            fullUrl = UrlUtils.buildFullRequestUrl(scheme, serverName, serverPort, requestURI, queryString);
        }
        return fullUrl;
    }

    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public List<String> getHeaderValues(String name) {
        List<String> values = headers.get(name);

        if (values == null) {
            return Collections.emptyList();
        }

        return values;
    }

    public List<Locale> getLocales() {
        return locales;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    public Collection<String> getParameterNames() {
        return parameters.keySet();
    }

    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }


    public String getQueryString() {
        return (this.queryString);
    }

    public String getRequestURI() {
        return (this.requestURI);
    }

    public String getScheme() {
        return scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    private boolean propertyEquals(String log, Object arg1, Object arg2) {
        if ((arg1 == null) && (arg2 == null)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(log + ": both null (property equals)");
            }

            return true;
        }

        if (arg1 == null || arg2 == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property not equals)");
            }

            return false;
        }

        if (arg1.equals(arg2)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property equals)");
            }

            return true;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property not equals)");
            }

            return false;
        }
    }

    @Override
    public String toString() {
        return "RemoteRequest{" +
                "id='" + id + '\'' +
                ", fullUrl='" + getFullUrl() + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }


    public boolean isPost() {
        return "post".equalsIgnoreCase(method);
    }
}
