package com.backbase.expert.extensions.sushi;

import org.apache.camel.Exchange;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Class representing the state of the remote application.
 *
 * @author dirk
 * @since 8-7-13
 */
public class ApplicationViewState implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationViewState.class);


    // ID of Application View State
    private String id;

    // Initial Remote Request
    private final RemoteRequest initialRemoteRequest;

    // Remote Request History (For Navigation Support)
    private Map<String, RemoteRequest> remoteRequestHistory = new HashMap<String, RemoteRequest>();

    // Cookie Store
    private CookieStore cookieJar;

    // Sushi Recipe
    private SushiRecipe sushiRecipe;

    // Referer Url (URL that made the request to g:include
    private String refererUrl;

    public ApplicationViewState(SushiRecipe sushiRecipe, RemoteRequest initialRemoteRequest) throws URISyntaxException, MalformedCookieException {
        this.sushiRecipe = sushiRecipe;
        this.id = RandomStringUtils.randomAlphabetic(5);
        this.initialRemoteRequest = initialRemoteRequest;
        this.cookieJar = new BasicCookieStore();
        LOG.debug("Created Application View State", this);

    }


    public void addRemoteHistoryRequest(RemoteRequest remoteRequest) {
        LOG.debug("Adding request: {} to history", remoteRequest);
        this.remoteRequestHistory.put(remoteRequest.getId(), remoteRequest);
    }

    /**
     * Sushi Proxy Url
     *
     * @return
     */
    public String getProxyUrl() {
        StringBuilder transformedUrl = new StringBuilder(sushiRecipe.getContextPath()).append(sushiRecipe.getServletPath());
        transformedUrl.append("/sushi/proxy/").append(id);
        return transformedUrl.toString();
    }


    public RemoteRequest getRemoteRequest(String requestId) {
        return this.remoteRequestHistory.get(requestId);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public RemoteRequest getInitialRemoteRequest() {
        return initialRemoteRequest;
    }


    public void addCookie(Cookie cookie) {
        cookieJar.addCookie(cookie);
    }

    public List<Cookie> getCookies() {
        return cookieJar.getCookies();
    }

    public String getRefererUrl() {
        return refererUrl;
    }

    public void setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
    }

    public SushiRecipe getSushiRecipe() {
        return sushiRecipe;
    }

    public void setSushiRecipe(SushiRecipe sushiRecipe) {
        this.sushiRecipe = sushiRecipe;
    }


    public void addCookies(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            LOG.debug("Adding cookie: {} to application view state: {}", this);
            addCookie(cookie);
        }
    }
}
