package com.latico.commons.html.htmlparser;

import com.latico.commons.common.util.io.FileUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.junit.Test;

import java.io.IOException;

public class HtmlParserUtilsTest {

    @Test
    public void createParser() throws IOException {
        String str = FileUtils.readFileToString("./doc/百度一下，你就知道.html", "UTF-8");
    }

    @Test
    public void createParserByFile() throws IOException {
        Parser parser = HtmlParserUtils.createParserByFile("./doc/百度一下，你就知道.html", "UTF-8");
    }

    @Test
    public void extractNodes() throws Exception {
        Parser parser = HtmlParserUtils.createParserByFile("./doc/百度一下，你就知道.html", "UTF-8");
        LinkStringFilter linkStringFilter = new LinkStringFilter("https://www.baidu.com");
        NodeList nodeList = HtmlParserUtils.extractNodes(parser, linkStringFilter);

        SimpleNodeIterator iterator = nodeList.elements();
        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            if (node instanceof LinkTag) {
                LinkTag linkTag = (LinkTag) node;
                System.out.println(linkTag.getLink() + "  文本:" + ((LinkTag) node).getLinkText());
            } else {
                System.out.println(node);
            }


        }
    }
}