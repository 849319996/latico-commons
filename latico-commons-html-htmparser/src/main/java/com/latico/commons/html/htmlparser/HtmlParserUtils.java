package com.latico.commons.html.htmlparser;

import com.latico.commons.common.util.io.FileUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-26 23:38
 * @Version: 1.0
 */
public class HtmlParserUtils {

    /**
     * 针对一个网元，生成一个解析器总对象
     * @param html html源码
     * @param charset 字符集
     * @return
     */
    public static Parser createParser(String html, String charset) {
        Parser parser = Parser.createParser(html, charset);
        return parser;
    }
    /**
     * 针对一个网元，生成一个解析器总对象
     * @param htmlFilePath html文件路径
     * @param charset 字符集
     * @return
     */
    public static Parser createParserByFile(String htmlFilePath, String charset) throws IOException {
        String html = FileUtils.readFileToString(htmlFilePath, charset);
        return createParser(html, charset);
    }

    /**
     * 抽取符合过滤规则的节点
     * 拿到节点后，就进行节点的解析和遍历了
     * @param parser 解析器
     * @param nodeFilters 节点过滤，可以多个，可以使用{@link org.htmlparser.filters}中的类，
     *                    比如：
     *                    1、对于<a></a>标签提取URL，构造URL连接的过滤器，匹配百度下面所有的URL，
     *                    有两种方式，
     *                    第一种是字符串开头的方式：LinkStringFilter linkStringFilter = new LinkStringFilter("https://www.baidu.com");
     *                    第二种是使用正则方式：LinkRegexFilter linkRegexFilter = new LinkRegexFilter("https://www\\.baidu\\.com.*");
     *
     *                    也可以通过{@link NodeClassFilter}结合{@link org.htmlparser.tags}来构造所需要的过滤器，
     *                    比如：
     *                    1、一个表节点的列的过滤器：NodeFilter tdFilter = new NodeClassFilter(TableColumn.class);
     *                    2、一个DIV节点的过滤器：NodeFilter divFilter = new NodeClassFilter(Div.class);
     * @return 所有过滤出来的
     * @throws ParserException
     */
    public static NodeList extractNodes(Parser parser, NodeFilter... nodeFilters) throws ParserException {
        if (nodeFilters.length == 1) {
            return parser.parse(nodeFilters[0]);
        } else {
            OrFilter orFilter = new OrFilter();
            orFilter.setPredicates(nodeFilters);
            return parser.extractAllNodesThatMatch(orFilter);
        }

    }

}
