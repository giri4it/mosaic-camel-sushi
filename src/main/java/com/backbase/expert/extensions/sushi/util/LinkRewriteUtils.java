package com.backbase.expert.extensions.sushi.util;

import com.backbase.expert.extensions.sushi.ApplicationViewState;
import com.backbase.expert.extensions.sushi.RemoteRequest;
import com.backbase.expert.extensions.sushi.SushiRecipe;
import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bartv on 30/08/14.
 */
public class LinkRewriteUtils {

    public static String transformRelativeUrl(String relativeUrl, ApplicationViewState applicationViewState, SushiRecipe recipe, RemoteRequest currentRequest) {




        if (relativeUrl.startsWith("#") || relativeUrl.toLowerCase().startsWith("javascript")) {
            return relativeUrl;
        }


        String remoteBasePath = currentRequest.getBasePath();


        if (relativeUrl.startsWith(remoteBasePath)) {
            relativeUrl = relativeUrl.replaceFirst(remoteBasePath, "");
        } else if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("//")) {
            return relativeUrl;

        }

        StringBuilder transformedUrl = new StringBuilder(applicationViewState.getProxyUrl());

        if(!relativeUrl.startsWith("/")) {
            // get url for current remote url
            try {


                URI currentRemoteUrl = new URI(currentRequest.getFullUrl());
                String path = StringUtils.substringBeforeLast(currentRemoteUrl.getPath(),"/");
                transformedUrl.append(path);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            transformedUrl.append("/");
        }

        transformedUrl.append(relativeUrl);


        return transformedUrl.toString();
    }
}
