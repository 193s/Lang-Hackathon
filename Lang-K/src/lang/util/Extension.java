package lang.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extension {
	public static String getSpace(int k) {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<k; i++) s.append(' ');
		return s.toString();
	}

	
	/* this code has been copied from processing.core.PApplet */
	public static HashMap<String, Pattern> matchPatterns;

	public static Pattern matchPattern(String regexp) {
		Pattern p = null;
		if (matchPatterns == null) {
			matchPatterns = new HashMap<String, Pattern>();
		} else {
			p = matchPatterns.get(regexp);
		}
		if (p == null) {
			if (matchPatterns.size() == 10) {
				// Just clear out the match patterns here if more than 10 are
				// being
				// used. It's not terribly efficient, but changes that you have
				// >10
				// different match patterns are very slim, unless you're doing
				// something really tricky (like custom match() methods), in
				// which
				// case match() won't be efficient anyway. (And you should just
				// be
				// using your own Java code.) The alternative is using a queue
				// here,
				// but that's a silly amount of work for negligible benefit.
				matchPatterns.clear();
			}
			p = Pattern.compile(regexp, Pattern.MULTILINE | Pattern.DOTALL);
			matchPatterns.put(regexp, p);
		}
		return p;
	}

	public static String[] match(String str, String regexp) {
		Pattern p = matchPattern(regexp);
		Matcher m = p.matcher(str);
		if (m.find()) {
			int count = m.groupCount() + 1;
			String[] groups = new String[count];
			for (int i = 0; i < count; i++) {
				groups[i] = m.group(i);
			}
			return groups;
		}
		return null;
	}

	public static String[][] matchAll(String str, String regexp) {
		Pattern p = matchPattern(regexp);
		Matcher m = p.matcher(str);
		ArrayList<String[]> results = new ArrayList<String[]>();
		int count = m.groupCount() + 1;
		while (m.find()) {
			String[] groups = new String[count];
			for (int i = 0; i < count; i++) {
				groups[i] = m.group(i);
			}
			results.add(groups);
		}
		if (results.isEmpty()) {
			return null;
		}
		String[][] matches = new String[results.size()][count];
		for (int i = 0; i < matches.length; i++) {
			matches[i] = results.get(i);
		}
		return matches;
	}
}
