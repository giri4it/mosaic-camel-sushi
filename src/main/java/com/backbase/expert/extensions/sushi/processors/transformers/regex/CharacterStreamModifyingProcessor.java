/**
 * 
 */
package com.backbase.expert.extensions.sushi.processors.transformers.regex;

import com.googlecode.streamflyer.regex.MatchProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements common functionality used for transforming character streams by
 * using regular expressions.
 * 
 * @author Lazarius
 * 
 */
public abstract class CharacterStreamModifyingProcessor implements MatchProcessor {

	/**
	 * @return Returns the parts of the matchProcessor
	 */
	protected List<Object> parseReplacement(String replacement) {

		List<Object> compiledReplacement = new ArrayList<Object>();

		// we look for escaped literals and references to groups
		int position = 0;
		StringBuilder notGroupReference = new StringBuilder();

		while (position < replacement.length()) {
			char ch = replacement.charAt(position);

			// reference to group?
			if (ch == '$') {

				if (notGroupReference.length() != 0) {
					compiledReplacement.add(notGroupReference.toString());
					notGroupReference.setLength(0);
				}

				position++;

				int groupNumberStartPosition = position;
				if (groupNumberStartPosition == replacement.length()) {
					throw new IllegalArgumentException("group reference $ " + "without number at the end " + "of the "
					        + "replacement string (" + replacement + ")");
				}

				ch = replacement.charAt(position);

				while ('0' <= ch && ch <= '9') {
					position++;
					if (position == replacement.length()) {
						break;
					}
					ch = replacement.charAt(position);
				}
				// position is the position of the first character that does not
				// belong to the group reference

				if (position == groupNumberStartPosition) {
					throw new IllegalArgumentException("invalid reference to group at position " + position
					        + " in replacement string " + replacement.substring(0, position) + "[]" + replacement.substring(position));
				}

				String groupNumberString = replacement.substring(groupNumberStartPosition, position);
				int groupNumber = Integer.parseInt(groupNumberString);

				compiledReplacement.add(groupNumber);
			}
			// escaped literal?
			else if (ch == '\\') {

				// skip the backslash
				position++;
				ch = replacement.charAt(position);

				notGroupReference.append(ch);
				position++;
			}
			// this is a normal character
			else {

				notGroupReference.append(ch);
				position++;
			}
		}

		if (notGroupReference.length() != 0) {
			compiledReplacement.add(notGroupReference.toString());
		}

		return compiledReplacement;
	}

}