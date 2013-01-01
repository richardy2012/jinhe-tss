package com.jinhe.tss.portal.engine.releasehtml;

import java.io.File;
import java.io.IOException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.xml.sax.SAXException;

import com.jinhe.tss.util.FileHelper;

public class TestOpenHtmlParser {
    
    public static void testHtmlParser() {
        try {
            Parser parser = new Parser("D:/Temp/Portal/index2.html");
            parser.setEncoding("GBK");

            StringBuffer sb = new StringBuffer();
            
            NodeList list = parser.parse(null);
            for (NodeIterator it = list.elements(); it.hasMoreNodes();) {
                Node node = it.nextNode();
                replaceDynamicUrl(node);
                replaceOtherResourcesUrl(node);

                sb.append(node.toHtml());
            }
            
            FileHelper.writeFile(new File("D:/Temp/Portal/index3.html"), sb.toString());
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    static class LinkNodeFilter implements NodeFilter {
        private static final long serialVersionUID = 1L;

        public boolean accept(Node node) {
            if (node.getText().indexOf("a href") != -1)
                return true;
            return false;
        }
    }

    private static void replaceDynamicUrl(Node bigNode) throws ParserException {
        NodeList linkElements = new NodeList();
        bigNode.collectInto(linkElements, new LinkRegexFilter(""));
        for (NodeIterator it2 = linkElements.elements(); it2.hasMoreNodes();) {
            Node node2 = it2.nextNode();
            // System.out.println(node2.getText());

            org.htmlparser.tags.LinkTag linkTag = (LinkTag) node2;
            // System.out.println(linkTag.getLink());
            // System.out.println(linkTag.getLinkText());
            System.out.println(linkTag.getAttribute("href"));

            linkTag.setAttribute("href", "http://www.google.com");
            // node2.setText("<a href='http://www.google.com'></a>");
        }
    }

    private static void replaceOtherResourcesUrl(Node bigNode)
            throws ParserException {
        // org.htmlparser.filters.TagNameFilter filter;
        // org.htmlparser.tags.ImageTag imageTag;
        // org.htmlparser.tags.ScriptTag scriptTag;
        // org.htmlparser.tags.StyleTag styleTag;

        NodeList linkElements = new NodeList();
        bigNode.collectInto(linkElements, new TagNameFilter("script"));
        for (NodeIterator iter = linkElements.elements(); iter.hasMoreNodes();) {
            Node node = iter.nextNode();
            String link = node.getText();
            System.out.println(link);

            org.htmlparser.tags.ScriptTag scriptTag = (ScriptTag) node;

            System.out.println(scriptTag.getAttribute("src"));
            scriptTag.setAttribute("src", "yyyyyyyyyyy");
        }
        
        linkElements = new NodeList();
        bigNode.collectInto(linkElements, new TagNameFilter("style"));
        for (NodeIterator iter = linkElements.elements(); iter.hasMoreNodes();) {
            StyleTag tag = (StyleTag) iter.nextNode();
            
            System.out.println(tag.getStyleCode());
        }
    }
    
    public static void main(String[] agrs) throws SAXException, IOException {
        testHtmlParser();
    }
}
