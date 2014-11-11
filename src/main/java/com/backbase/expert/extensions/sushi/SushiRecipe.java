package com.backbase.expert.extensions.sushi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: bartv
 * Date: 13-10-13
 * Time: 17:10
 */
public class SushiRecipe implements Serializable {

    public Map<String, Object> initialRequest = new HashMap<String, Object>();

    public Map<String, Object> eachRequest = new HashMap<String, Object>();

    public Map<String, Object> transformers = new HashMap<String, Object>();

    private String servletPath;
    private String contextPath;

    private String htmlResultNodeSelector;
    private List<String> htmlResultNodeFilter = new ArrayList<String>();


    private boolean wrapBodyInlineOnLoadScript;
    private String externalResourceEncoding;


    private boolean convertExternalResourceEncoding;

    private boolean transferBodyAttributesToResultNode;
    private boolean transferBodyClassesToResultNode;
    private boolean transferHtmlClassesToResultNode;

    private boolean transferBodyScriptsToResultNode;
    private boolean transferInlineBodyScriptsToResultNode;

    private boolean transferHeadScriptsToResultNode;
    private boolean transferInlineHeadScriptsToResultNode;

    private boolean moveScriptsToEnd;
    private boolean moveInlineScriptsToEnd;

    private String headerScriptIncludeFilter;
    private String headerScriptExcludeFilter;

    private String bodyScriptIncludeFilter;
    private String bodyScriptExcludeFilter;

    private boolean transferCssLinksToResultNode;

    private String cssIncludeFilter;
    private String cssExcludeFilter;
    private String cssPrefix;


    public SushiRecipe() {
        super();
    }

    public Map<String, Object> getInitialRequest() {
        return initialRequest;
    }

    public String getInitialRequestRemoteApplicationUrl() {
        return (String) initialRequest.get("remoteApplicationUrl");
    }

    public void setInitialRemoteApplicationUrl(String remoteApplicationUrl) {
        initialRequest.put("remoteApplicationUrl", remoteApplicationUrl);
    }

    public String getInitialRemoteHttpMethod() {
        return (String) this.initialRequest.get("remoteHttpMethod");
    }

    public void setInitialRemoteHttpMethod(String remoteHttpMethod) {

        this.initialRequest.put("remoteHttpMethod", remoteHttpMethod);
    }

    public String[] getInitialCookiesToTransfer() {
        return (String[]) getInitialRequest().get("cookies");
    }


    public Map<String, String> getInitialHttpHeadersToTransfer() {
        return (Map<String, String>) getInitialPopulateHeaders().get("httpHeaders");
    }


    public void setInitialRequest(Map<String, Object> initialRequest) {
        this.initialRequest = initialRequest;
    }

    public Map<String, Object> getInitialPopulateHeaders() {
        return (Map<String, Object>) this.initialRequest.get("populateHeaders");
    }

    public void setInitialCookiesToTransfer(String cookiesToTransfer) {
        String[] cookies = cookiesToTransfer.split(",");
        this.getInitialPopulateHeaders().put("cookies", cookies);
    }

    public void setInitialHeadersToTransfer(String headersToTransfer) {
        String headerNames = headersToTransfer;
        Map<String, String> headerMap = new HashMap<String, String>();
        for (String name : headerNames.split(",")) {
            headerMap.put(name, name);
        }
        this.getInitialPopulateHeaders().put("headers", headerMap);
    }

    public String[] getEachCookiesToTransfer() {
        return (String[]) getEachRequest().get("cookies");
    }


    public Map<String, String> getEachHttpHeadersToTransfer() {
        return (Map<String, String>) getEachPopulateHeaders().get("httpHeaders");
    }


    public Map<String, Object> getEachPopulateHeaders() {
        return (Map<String, Object>) this.eachRequest.get("populateHeaders");
    }

    public void setEachCookiesToTransfer(String cookiesToTransfer) {
        String[] cookies = cookiesToTransfer.split(",");
        this.getEachPopulateHeaders().put("cookies", cookies);
    }

    public void setEachHeadersToTransfer(String headersToTransfer) {
        String headerNames = headersToTransfer;
        Map<String, String> headerMap = new HashMap<String, String>();
        for (String name : headerNames.split(",")) {
            headerMap.put(name, name);
        }
        this.getEachPopulateHeaders().put("headers", headerMap);
    }

    public List getInitialPostParameters() {
        return (List) getInitialRequest().get("postParameterNames");
    }


    public String getHtmlResultNodeSelector() {

        return this.htmlResultNodeSelector;
    }

    public void setHtmlResultNodeSelector(String htmlResultNodeSelector) {
        this.htmlResultNodeSelector = htmlResultNodeSelector;
    }

    public boolean isTransferBodyAttributesToResultNode() {
        return transferBodyAttributesToResultNode;
    }

    public void setTransferBodyAttributesToResultNode(boolean transferBodyAttributesToResultNode) {
        this.transferBodyAttributesToResultNode = transferBodyAttributesToResultNode;
    }

    public boolean isTransferBodyClassesToResultNode() {
        return transferBodyClassesToResultNode;
    }

    public void setTransferBodyClassesToResultNode(boolean transferBodyClassesToResultNode) {
        this.transferBodyClassesToResultNode = transferBodyClassesToResultNode;
    }

    public boolean isTransferHtmlClassesToResultNode() {
        return transferHtmlClassesToResultNode;
    }

    public void setTransferHtmlClassesToResultNode(boolean transferHtmlClassesToResultNode) {
        this.transferHtmlClassesToResultNode = transferHtmlClassesToResultNode;
    }

    public boolean isTransferBodyScriptsToResultNode() {
        return transferBodyScriptsToResultNode;
    }

    public void setTransferBodyScriptsToResultNode(boolean transferBodyScriptsToResultNode) {
        this.transferBodyScriptsToResultNode = transferBodyScriptsToResultNode;
    }

    public boolean isTransferHeadScriptsToResultNode() {
        return transferHeadScriptsToResultNode;
    }

    public void setTransferHeadScriptsToResultNode(boolean transferHeadScriptsToResultNode) {
        this.transferHeadScriptsToResultNode = transferHeadScriptsToResultNode;
    }

    public boolean isMoveScriptsToEnd() {
        return moveScriptsToEnd;
    }

    public void setMoveScriptsToEnd(boolean moveScriptsToEnd) {
        this.moveScriptsToEnd = moveScriptsToEnd;
    }

    public String getHeaderScriptIncludeFilter() {
        return headerScriptIncludeFilter;
    }

    public void setHeaderScriptIncludeFilter(String headerScriptIncludeFilter) {
        this.headerScriptIncludeFilter = headerScriptIncludeFilter;
    }

    public String getBodyScriptIncludeFilter() {
        return bodyScriptIncludeFilter;
    }

    public void setBodyScriptIncludeFilter(String bodyScriptIncludeFilter) {
        this.bodyScriptIncludeFilter = bodyScriptIncludeFilter;
    }

    public String getCssIncludeFilter() {
        return cssIncludeFilter;
    }

    public void setCssIncludeFilter(String cssIncludeFilter) {
        this.cssIncludeFilter = cssIncludeFilter;
    }

    public String getCssPrefix() {
        return cssPrefix;
    }

    public void setCssPrefix(String cssPrefix) {
        this.cssPrefix = cssPrefix;
    }


    public boolean isTransferInlineBodyScriptsToResultNode() {
        return transferInlineBodyScriptsToResultNode;
    }

    public void setTransferInlineBodyScriptsToResultNode(boolean transferInlineBodyScriptsToResultNode) {
        this.transferInlineBodyScriptsToResultNode = transferInlineBodyScriptsToResultNode;
    }

    public boolean isTransferInlineHeadScriptsToResultNode() {
        return transferInlineHeadScriptsToResultNode;
    }

    public void setTransferInlineHeadScriptsToResultNode(boolean transferInlineHeadScriptsToResultNode) {
        this.transferInlineHeadScriptsToResultNode = transferInlineHeadScriptsToResultNode;
    }

    public boolean isMoveInlineScriptsToEnd() {
        return moveInlineScriptsToEnd;
    }

    public void setMoveInlineScriptsToEnd(boolean moveInlineScriptsToEnd) {
        this.moveInlineScriptsToEnd = moveInlineScriptsToEnd;
    }

    public String getHeaderScriptExcludeFilter() {
        return headerScriptExcludeFilter;
    }

    public void setHeaderScriptExcludeFilter(String headerScriptExcludeFilter) {
        this.headerScriptExcludeFilter = headerScriptExcludeFilter;
    }

    public String getBodyScriptExcludeFilter() {
        return bodyScriptExcludeFilter;
    }

    public void setBodyScriptExcludeFilter(String bodyScriptExcludeFilter) {
        this.bodyScriptExcludeFilter = bodyScriptExcludeFilter;
    }

    public String getCssExcludeFilter() {
        return cssExcludeFilter;
    }

    public void setCssExcludeFilter(String cssExcludeFilter) {
        this.cssExcludeFilter = cssExcludeFilter;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public boolean isTransferCssLinksToResultNode() {
        return transferCssLinksToResultNode;
    }

    public void setTransferCssLinksToResultNode(boolean transferCssLinksToResultNode) {
        this.transferCssLinksToResultNode = transferCssLinksToResultNode;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public List<String> getHtmlResultNodeFilter() {
        return htmlResultNodeFilter;
    }

    public void setHtmlResultNodeFilter(List<String> htmlResultNodeFilter) {
        this.htmlResultNodeFilter = htmlResultNodeFilter;
    }

    public Map<String, Object> getEachRequest() {
        return eachRequest;
    }

    public boolean isWrapBodyInlineOnLoadScript() {
        return wrapBodyInlineOnLoadScript;
    }

    public void setWrapBodyInlineOnLoadScript(boolean wrapBodyInlineOnLoadScript) {
        this.wrapBodyInlineOnLoadScript = wrapBodyInlineOnLoadScript;
    }

    public String getExternalResourceEncoding() {
        return externalResourceEncoding;
    }

    public void setExternalResourceEncoding(String externalResourceEncoding) {
        this.externalResourceEncoding = externalResourceEncoding;
    }

    public boolean isConvertExternalResourceEncoding() {
        return convertExternalResourceEncoding;
    }

    public void setConvertExternalResourceEncoding(boolean convertExternalResourceEncoding) {
        this.convertExternalResourceEncoding = convertExternalResourceEncoding;
    }

    public void setEachRequest(Map<String, Object> eachRequest) {
        this.eachRequest = eachRequest;
    }

    public Map<String, Object> getTransformers() {
        return transformers;
    }

    public void setTransformers(Map<String, Object> transformers) {
        this.transformers = transformers;
    }

    public Map<String, Object> getTransformer(String key) {
        if (getTransformers().isEmpty())
            return null;
        return (Map<String, Object>) getTransformers().get(key);

    }

    public List<String> getTransformerPatterns(String parametersKey) {
        Map<String, Object> transformer = getTransformer(parametersKey);
        if (transformer != null) {
            return (List<String>) transformer.get("patterns");
        } else {
            return null;
        }
    }
}
