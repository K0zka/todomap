package org.todomap.o29.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagToken;

public class HtmlUtil {

	private static String serializeTokens(
			final CleanerProperties cleanerProperties,
			List<BaseToken> children) throws IOException {
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
	public static String getFirstParagraph(String html) {
		final CleanerProperties cleanerProperties = mkCleanerProperties();
		HtmlCleaner cleaner = new HtmlCleaner(cleanerProperties);
		try {
			final TagNode node = cleaner.clean(html);
			final TagNode body = node.findElementByName("body", true);
			final ArrayList<BaseToken> nodes = new ArrayList<BaseToken>();
			for(final BaseToken child : ((List<BaseToken>)body.getChildren())) {
				if(child instanceof TagToken && "br".equals(((TagToken)child).getName()) ) {
					break;
				} else if(child instanceof TagToken && "p".equals(((TagToken)child).getName())) {
					return serializeTokens(cleanerProperties, ((TagNode)child).getChildren());
				} else {
					nodes.add(child);
				}
			}
			return serializeTokens(cleanerProperties, nodes);
		} catch (IOException e) {
			return "";
		}
	}
}