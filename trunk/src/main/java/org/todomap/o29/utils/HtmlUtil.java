package org.todomap.o29.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CommentToken;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagToken;

public class HtmlUtil {

	private final static String[] acceptedTags = {"div","span","b","i","strong","a", "img", "br"};
	
	static boolean isAcceptedTag(final String name) {
		for(final String acceptedTag : acceptedTags) {
			if(acceptedTag.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static String cleanup(final String html) {
		final CleanerProperties cleanerProperties = mkCleanerProperties();
		final HtmlCleaner cleaner = new HtmlCleaner(cleanerProperties);
		try {
			final TagNode node = cleaner.clean(html);
			final TagNode body = node.findElementByName("body", true);
			cleanup(body);
			return serializeTokens(cleanerProperties, body.getChildren());
		} catch (IOException e) {
			return "";
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void cleanup(TagNode body) {
		final ArrayList<Object> eviltags = new ArrayList<Object>();
		for(final BaseToken token : (List<BaseToken>)body.getChildren()) {
			if(token instanceof TagToken) {
				if(isAcceptedTag(((TagNode)token).getName())) {
					cleanup((TagNode)token);
				} else {
					eviltags.add(token);
				}
			} else if(token instanceof CommentToken) {
				eviltags.add(token);
			}
		}
		for(final Object tag : eviltags) {
			body.removeChild(tag);
		}
	}

	private static String serializeTokens(
			final CleanerProperties cleanerProperties,
			final List<BaseToken> children) throws IOException {
		final StringWriter writer = new StringWriter();
		for (final Object child : children) {
			if (child instanceof TagNode) {
				((TagToken) child).serialize(new SimpleXmlSerializer(
						cleanerProperties), writer);
			} else {
				writer.write(child.toString());
			}
		}
		return writer.toString();
	}

	private static CleanerProperties mkCleanerProperties() {
		final CleanerProperties cleanerProperties = new CleanerProperties();
		cleanerProperties.setOmitComments(true);
		cleanerProperties.setOmitXmlDeclaration(true);
		cleanerProperties.setOmitDeprecatedTags(true);
		return cleanerProperties;
	}
	
	@SuppressWarnings("unchecked")
	public static String getFirstParagraph(final String html) {
		final CleanerProperties cleanerProperties = mkCleanerProperties();
		final HtmlCleaner cleaner = new HtmlCleaner(cleanerProperties);
		try {
			final TagNode node = cleaner.clean(html.replaceAll("&nbsp;", "<br/>"));
			final TagNode body = node.findElementByName("body", true);
			return extract(cleanerProperties, new ArrayList<BaseToken>(), (List<BaseToken>)body.getChildren());
		} catch (IOException e) {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	private static String extract(final CleanerProperties cleanerProperties,
			final ArrayList<BaseToken> nodes, final List<BaseToken> children)
			throws IOException {
		for(final BaseToken child : children) {
			if(child instanceof TagToken) {
				if("span".equals(((TagToken)child).getName()) ) {
					return extract(cleanerProperties, nodes, ((TagNode)child).getChildren());
				} else if("div".equals(((TagToken)child).getName()) ) {
					return extract(cleanerProperties, nodes, ((TagNode)child).getChildren());
				} else if("br".equals(((TagToken)child).getName()) ) {
					break;
				} else if("p".equals(((TagToken)child).getName())) {
					return serializeTokens(cleanerProperties, ((TagNode)child).getChildren());
				}
			} else {
				nodes.add(child);
			}
		}
		return serializeTokens(cleanerProperties, nodes);
	}
}
