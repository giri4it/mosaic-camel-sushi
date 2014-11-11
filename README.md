# Camel Sushi Bundle

## Short Description
Sushi is an extension on top of the Launchpad or camel enabled Portal Server that enables HTML integration with sophisticated Dom rewriting and
state management to support navigation of the remote application without leaving the page where the widget is integrated in.

## Long Description
Sushi is an extension on top of the Launchpad that enables HTML integration with sophisticated Dom rewriting and
state management to support navigation of the remote application without leaving the page where the widget is integrated in.

The behavior of Sushi is managed through Recipes. A Sushi Recipe contains all rules used to rewrite, filter the dom and how to transform links.

# Concepts
There are a number of important concepts you must understand in order to work with Sushi.

## Apache Camel
Working with Sushi as a stand alone component does not require extensive knowledge about Apache Camel. I do urge everyone to read up on it to get the [basic concepts](http://manning.com/ibsen/chapter1sample.pdf).

### Sushi Recipe
The Sushi Recipe describes the remote application and what transformation steps / options have to be performed. 

Below is an example of a simple recipe for a wordpress blog.

```
{
  "initialRequest": {
    "remoteApplicationUrl": "http://localhost/wordpress",
    "populateHeaders": {
      "httpHeaders": {
        "Accept": "Accept",
        "Accept-Encoding": "Accept-Encoding",
        "Accept-Language": "Accept-Language",
        "Origin": "Origin",
        "referer": "referer",
        "User-Agent": "User-Agent",
        "Content-Type": "Content-Type",
        "Cache-Control": "Cache-Control"
      }
    }
  },
  "eachRequest": {
    "populateHeaders": {
      "httpHeaders": {
        "Accept": "Accept",
        "Accept-Language": "Accept-Language",
        "Origin": "Origin",
        "referer": "referer",
        "User-Agent": "User-Agent",
        "Content-Type": "Content-Type"
      }
    }
  },
  "convertExternalResourceEncoding": true,
  "htmlResultNodeSelector": ".content-area",
  "htmlResultNodeFilter": []  ,
  "transferBodyAttributesToResultNode": true,
  "transferBodyClassesToResultNode": true,
  "transferHtmlClassesToResultNode": true,
  "transferBodyScriptsToResultNode": true,
  "transferInlineBodyScriptsToResultNode": true,
  "transferHeadScriptsToResultNode": true,
  "transferInlineHeadScriptsToResultNode": true,
  "moveScriptsToEnd": true,
  "moveInlineScriptsToEnd": true,
  "headerScriptIncludeFilter": "/**/*.js",
  "headerScriptExcludeFilter": "",
  "bodyScriptIncludeFilter": "/**/*.js",
  "bodyScriptExcludeFilter": "",
  "transferCssLinksToResultNode": false,
  "cssIncludeFilter": "/**/*.css",
  "cssExcludeFilter": "",
  "cssPrefix": "wordpress"
}
```
###initialRequest
The initialRequest describes the request that must be made to the remote application. The remoteApplicationUrl is mandatory and points to the remote application. Initial redirects are supported up to 5 redirects by the http4 camel component. By default a GET is performed to the remote resource. It is also possible to fire a POST by defining adding "remoteHttpMethod": "POST" to the initialRequest object. When setting the remote method to POST, you must also declare an array of all POST parameters you wan to send to the remote end point:

```
"remoteHttpMethod": "POST",
        "postParameterNames": ["LoginKS", "__type", "__gid", "__fid", "__uid", "__sid", "B_ID", "NextGid", "redirectUrl"]
        
```
The values for each array item are taken from the headers stored in the exchange. 

####populateHeaders

The headers that are send to the remote application, either in HTTP Headers or part of the POST body can be populated before sending to the remote endpoint. For this, the populateHeaders section is used. 

It consists of the following sections

#####authentication
Wen you need to transfer properties from the logged in user which are stored inside the Authentication Token set by Spring Security you can use the authentication section to transfer these properties. 

```
"populateHeaders": {
	....
	"authentication": {
    	"__uid": "uid",
    	"__sid": "sid",
    	"LoginKS": "loginKS" }
  	....
}
```
In the example above, the headers __uid, __sid and LoginKS are populated by the expressions executed on the authentication object found in the SpringSecurityContext. 

Expressions are defined by the [OGNL language](http://commons.apache.org/proper/commons-ognl/). 

If no property is found, or the expression could not be executed, the header will have an empty value (null). 

#####principal
As with authentication, the key defines the name of the header and the value is the expression executed on the Principal object (Normally User in the case of Backbase Portal Server). Expressions are defined using the [OGNL language](http://commons.apache.org/proper/commons-ognl/).

```
"populateHeaders": {
	....
 	"principal": {
    	"username": "username",
        "groups": "groups"}
    ...
}
```


#####constants
Constants are a way to always adding header with constant values to the exchange. This is very usefull for when the remote site is using Basic Authentication with a fixed username/password. In that case, you can add a constant like this: 

```
"populateHeaders": {
	....
	"constants": {
    	"Authorization": "Basic ZmpzZTAzOjcwMjE="
    	}
   .....
```

#####cookies
Here you can add a list of cookies you want to forward from the browser to the remote application. 

```
"populateHeaders": {
	....
 	"cookies": ["J_SESSIONID"]
 	...
 }
```

#####httpHeaders
This section is mandatory. In this section you discribe witch header from the exchange is sent as an httpHeader. You identiy a header from the exchange as key and the name of the http header as value. Most of the time, these are equal. The headers in the exchange are populated by the authentication, principal and constant options. Additional headers are sent from the client's browser (like Accept, Contetn-Type, User-Agent etc, etc)

```
"populateHeaders": {
	....
 	"httpHeaders": {
    	"Accept": "Accept",
        "Accept-Encoding": "Accept-Encoding",
        "Accept-Language": "Accept-Language",
        "Origin": "Origin",
        "referer": "referer",
        "User-Agent": "User-Agent",
        "Content-Type": "Content-Type",
        "Authorization": "Authorization",
        "Cache-Control": "Cache-Control"
            }
 	...
 }
```

###eachRequest
The populate headers section is equal to the initial request section. The httpHeaders are added to each request of the remote application. For Basic Authorization for instance, you must set the Authorization header and add it to the httpHeaders.

```
"eachRequest": {
        "populateHeaders": {
            "constants": {
                "Authorization": "Basic ZmpzZTAzOjcwMjE="
            },
            "httpHeaders": {
                "Accept": "Accept",
                "Accept-Language": "Accept-Language",
                "Origin": "Origin",
                "referer": "referer",
                "User-Agent": "User-Agent",
                "Content-Type": "Content-Type",
                "Authorization": "Authorization"
            }
        }

    }
```
    
###externalResourceEncoding
When retrieving text based external resources from the remote application, these could be in a different encoding than the default UTF-8. The configured encoding will be set to the response Content-Type and Charset. This configuration can be omitted if UTF-8 sources are served. 

```
"externalResourceEncoding" : "SJIS"
```


###convertExternalResourceEncoding
Converts the remote resource encoding to UTF-8 is set to true. 

(not implemented yet....)

```
 "convertExternalResourceEncoding": true,
```


###htmlResultNodeSelector
Often you would like to return only a node of the remote HTML DOM. With the htmlResutlNodeSelector you can use a [jQuery Style Expression](http://jsoup.org/cookbook/extracting-data/selector-syntax) to select the result node. 

If you write a selector that matches multiple nodes, if will only return the first node. If no match is made, it will return the contents of the body node.

```
"htmlResultNodeSelector": ".content-area",
```

###htmlResultNodeFilter
If you do not want to certail nodes to show in the result node, you can specify an array of selectors (same format as the htmlResultNodeSelector)

```
"htmlResultNodeFilter": [],
```

###transferBodyAttributesToResultNode
if set to true the attributes of the BODY node are copied to the result node.

```
"transferBodyAttributesToResultNode": true,
```


###transferBodyClassesToResultNode
if set to true the BODY classes are copied to the result node.

```
"transferBodyClassesToResultNode": true,
```

###transferHtmlClassesToResultNode
if set to true the HTML classes are copied to the result node.

```
"transferBodyAttributesToResultNode": true,
```

###transferBodyScriptsToResultNode
If set to true, links to external javascripts inside the BODY node are transferred to the result node.

```
"transferBodyScriptsToResultNode": true,
```

###transferInlineBodyScriptsToResultNode
If set to true, inline javascript nodes from the BODY node are transferred to the result node.

```
"transferInlineBodyScriptsToResultNode": true,
```
###transferHeadScriptsToResultNode
If set to true, links to external scripts from the HEAD tag are transferred to the result node.

```
"transferHeadScriptsToResultNode": true,
```


###transferInlineHeadScriptsToResultNode
If set to true, inline javascript nodes from the HEAD node are transferred to the result node.

```
"transferInlineHeadScriptsToResultNode": true,
```


###moveScriptsToEnd
If set to true, the copied scripts are added to the end of the result node. 

```
"moveScriptsToEnd": true,
```


###moveInlineScriptsToEnd
If set to true, the copied inline scripts are added to the end of the result node

```
""moveInlineScriptsToEnd" : true,
```

###headerScriptIncludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which scripts to include from the HEAD node to the result node. 

```
"headerScriptIncludeFilter": "/**/*.js",
```

###headerScriptExcludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which scripts to exclude from the HEAD node to the result node. 

```
"headerScriptExcludeFilter": "",
```

###bodyScriptIncludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which scripts to include from the BODY node to the result node. 

```
"bodyScriptIncludeFilter": "/**/*.js",
```

###bodyScriptExcludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which scripts to exclude from the BODY node to the result node. 

```
"bodyScriptExcludeFilter": "",
```

###transferCssLinksToResultNode
If set to true. CSS Links from the remote application are transferred to the result node. Caution: Links to CSS are placed inside the BODY!

```
"transferCssLinksToResultNode": false,
```


###cssIncludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which css links to include in the result node. 

```
"cssIncludeFilter": "/**/*.js",
```

###cssExcludeFilter
An [Apache ANT style pattern](http://ant.apache.org/manual/dirtasks.html#patterns) for specifying which css links to exclude in the result node. 

```
"cssExcludeFilter": "",
```

###cssPrefix
With the cssPrefix property, the result node is wrapped inside a DIV with this class. It is also prefixed to all rules set in the external CSS files.

```
"cssPrefix": "my-custom-prefix",
```

###transformers
In the transformers section you can specify which regular expressions are used to convert paths inside javascript for instance. 

```
"transformers": {
        "javascript": {
            "patterns": ["(?i)(?<=document.HOST.action=\")/",
                         "(?i)(?<=href=\")/"]

        }
    }
```


#### Application View State
The Application View State holds the state of the remote application. this includes:

- A generated ID which is used to retrieve the application view state from the Application View State Cache
- The Initial Request
- A Remote Request History
- A Cookie Jar for storing the remote application cookies
- The Sushi Recipe
- The Referer Url


When a request is made for the first time a new ApplicationViewState is created. This view state is then put inside a cache "SushiApplicationCache". 

The configuration of the SushiApplicationCache is setup by the 	applicationStateCacheConfiguration route and for now is set to an hour (3600 seconds). 


```
<route id="applicationStateCacheConfiguration">
	<from uri="cache://SushiApplicationCache?diskPersistent=true&amp;timeToLiveSeconds=3600&amp;timeToIdleSeconds=3600"/>
	<to uri="log:com.backbase.extensions.camel.sushi?level=INFO"/>
</route>
```

When accessing the Sushi HTML route for the first time, a new Application View State is created. For subsequent request, and requests to the Sushi Proxy, the Application View State ID must be part of the reqeust. For Sushi HTML, this must be passed as a HTTP Parameter (POST or GET) with the name sushiApplicationViewStateID

For requests to to the Sushi Proxy, the applicationViewState must be part of the path

	/portalserver/services/sushi/{applicationViewStateId}/{proxyPath}
	
The Sushi Proxy will extract the view state from the cache with the applicationViewStateId and it will then know which hosts it must use to proxy the request to. 


#### Transformer Orchestration Engine
The Transformer Orchestrator Engine takes selecting the right processors for proxies content based on their Content Type. This way, external javascript files, css files and other resources are transformed by the correct transformers.


## Setup Instructions
Just add the Sushi Dependency to you project :)

In backbase.properties you must specify the sushi.recipe.path to location on disk where recipes are stored. In you maven project you can use 

	sushi.recipe.path=${project.basedir}/configuration/backbase/recipe/
	
Recipes not cached yet, so changes inside the recipes are instant.

## Generic Sushi Widget
Sushi comes with a generic Sushi widget. This is a very simple widget that uses a g:include to include the remote application inside the widget
```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" xmlns:g="http://www.backbase.com/2008/gadget">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Sushi Widget</title>
</head>
<body>
<div class="sushi-widget">
    <g:include proxy="false" src="$(contextRoot)/services/sushi/html">
        <!-- Sushi Template ID. Must correspond to a json file in the recipe directory -->
        <g:http-param name="SushiTemplateId" value="new-payment"/>
        <!-- Application View State (used for Navigation) -->
        <g:http-param name="SushiApplicationViewStateId" value="${SushiApplicationViewStateId}"/>
        <!-- Remote Request ID  (used for Navigation) -->
        <g:http-param name="SushiRemoteRequestId" value="${SushiRemoteRequestId}"/>
    </g:include>
</div>
</body>
</html>
```

### Server Catalog Definition
For the Sushi Widget to correctly work with the url2state parameters, the properties ***sushiApplicationViewStateID*** and ***sushiRemoteRequestID*** must be added to the widget definition. 

The Sushi Recipe Template is identified by the 

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<catalog>
    <widget>
        <name>sushi-widget-generic</name>
        <contextItemName>[BBHOST]</contextItemName>
        <properties>
            <property name="TemplateName">
                <value type="string">Standard_Widget</value>
            </property>	
            <property name="title">
                <value type="string">Generic Sushi Widget</value>
            </property>
            <property name="thumbnailUrl">
                <value type="string">$(contextRoot)/static/sushi/widgets/generic/media/icon.png</value>
            </property>
            <property label="Widget Chrome" name="widgetChrome" viewHint="select-one,user,designModeOnly">
                <value type="string">$(contextRoot)/static/launchpad/chromes/blank/chrome-blank.html</value>
            </property>
            <property name="src">
                <value type="string">$(contextRoot)/static/sushi/widgets/generic/index.html</value>
            </property>
            <property name="sushiApplicationViewStateId">
                <value type="string"></value>
            </property>
            <property name="sushiApplicationViewStateId">
                <value type="string">[$1:null]</value>
            </property>
            <property name="sushiRemoteRequestId">
                <value type="string">[$2:null]</value>
            </property>
        </properties>
        <tags>
            <tag type="section">mosaic</tag>
            <tag type="regular">mosaic</tag>
        </tags>
    </widget>
</catalog>

```



## Limitation 
- Only one Sushi G:Include Widget per page if you want to support navigation
- Unlimited Sushi Iframe Widgets per page
- **Cache must not be disabled**

## Installation
Add the following profile to your Portalserver POM file profiles section

```
<profile>
    <id>with-mosaic-camel-sushi</id>
    <activation>
        <property>
            <name>!with-mosaic-camel-sushi</name>
        </property>
    </activation>
    <dependencies>
        <dependency>
            <groupId>com.backbase.extensions.camel</groupId>
            <artifactId>mosaic-camel-sushi</artifactId>
            <version>1.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>com.backbase.extensions.camel</groupId>
            <artifactId>mosaic-camel-sushi</artifactId>
            <version>1.1-SNAPSHOT</version>
            <classifier>bundle-dist</classifier>
            <type>zip</type>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>unpack-mosaic-sushi</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                <groupId>com.backbase.extensions.camel</groupId>
                                    <artifactId>mosaic-camel-sushi</artifactId>
                                    <version>1.1-SNAPSHOT</version>
                                    <classifier>bundle-dist</classifier>
                                    <type>zip</type>
                                    <outputDirectory>${work.dir}</outputDirectory>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>

``` 




### Component list
Title                	| Type
--------             	| --------
**Camel Sushi**        	| Camel Route Context
**Generic Sushi Widget** | Generic Sushi Widget

### Support
Camel Sushi Version  | Launchpad Version 
------------         | -------------
1.1                  | 0.10.0

### Bundle dependencies
- Launchpad Mashup Services (Sushi can be run in any Camel Runtime)

### Dependency Reference

```
<dependency>
    <groupId>com.backbase.extensions.camel</groupId>
    <artifactId>mosaic-camel-sushi</artifactId>
    <version>1.1-SNAPSHOT</version>
    <type>jar</type>
</dependency>
```

### Technical Type
- BE (Backend)

### Bundle Category
- Camel
