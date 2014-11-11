/**
 * 
 */
package com.backbase.expert.extensions.sushi.processors.transformers.regex;

import com.googlecode.streamflyer.internal.thirdparty.ZzzValidate;
import com.googlecode.streamflyer.regex.MatchProcessorResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.MatchResult;

/**
 * Character stream processor used by <code>PathRegExTransformer</code>.
 * 
 * @author Lazarius
 * 
 */
public class PathReplacingProcessor extends CharacterStreamModifyingProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(PathReplacingProcessor.class);

	private static final String REPLACEMENT_TEMPLATE = "/portalserver/proxy?pipe=%1$s&url=%2$s://%3$s";

	private final String proxyUrl;

	private final String targetUrl;

	private final boolean escapeXml;

	public PathReplacingProcessor(String proxyUrl, String targetUrl) {
		this(proxyUrl, targetUrl, false);
	}

	public PathReplacingProcessor(String proxyUrl, String targetUrl, boolean escapeXml) {
		// validate arguments
		ZzzValidate.notNull(proxyUrl, "pipeName must not be null");
		ZzzValidate.notNull(targetUrl, "targetUrl must not be null");
		this.proxyUrl = proxyUrl;
		this.targetUrl = targetUrl;
		this.escapeXml = escapeXml;
	}

	/**
	 * Replaces the match as given by the {@link java.util.regex.MatchResult} with the
	 * configured replacement.
	 *
	 * @see com.googlecode.streamflyer.regex.MatchProcessor#process(StringBuilder,
	 *      int, java.util.regex.MatchResult)
	 */
	@Override
	public MatchProcessorResult process(StringBuilder characterBuffer, int firstModifiableCharacterInBuffer, MatchResult matchResult) {

		int start = matchResult.start();
		int end = matchResult.end();

		String matchString = characterBuffer.substring(start, end);

		String replacement = null;

//		try {
//			URL url = new URL(targetUrl);
//			int port = url.getPort();
//			String urlParam = url.getHost();
//			if (port != -1) {
//				urlParam += ":" + port;
//			}
//			String path = url.getPath();
//			if (!matchString.startsWith("/")) {
//				urlParam += path;
//				if (urlParam.charAt(urlParam.length() - 1) != '/') {
//					urlParam = urlParam.substring(0, urlParam.lastIndexOf('/') + 1);
//				}
//			} else {
//				urlParam += matchString;
//			}
			replacement = proxyUrl + matchString;
//		} catch (MalformedURLException e) {
//			throw new IllegalArgumentException(e);
//		}

		if (escapeXml) {
			replacement = StringEscapeUtils.escapeXml(replacement);
		}

		StringBuilder localReplacement = null;
		List<Object> parts = parseReplacement(replacement);
		localReplacement = new StringBuilder((CharSequence) parts.get(0));

		int levelsUp = StringUtils.countMatches(matchString, "../");
		if (levelsUp > 0) {
			if (localReplacement.toString().endsWith("/")) {
				localReplacement.setLength(localReplacement.length() - 1);
			}
			while (levelsUp > 0) {
				levelsUp--;
				localReplacement.setLength(localReplacement.lastIndexOf("/"));
			}
			localReplacement.append("/");
		}

		int offset = start == end ? 1 : 0;

		characterBuffer.delete(start, end);
		characterBuffer.insert(start, localReplacement);
		return new MatchProcessorResult(start + localReplacement.length() + offset, true);

	}

}
