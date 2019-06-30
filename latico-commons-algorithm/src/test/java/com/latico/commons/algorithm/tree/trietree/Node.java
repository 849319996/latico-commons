package com.latico.commons.algorithm.tree.trietree;

import java.util.LinkedList;
import java.util.List;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-05-21 10:10:50
 * @Version: 1.0
 */
class Node {
    //记录当前节点的字
    char word;
    //判断该字是否词语的末尾，如果是则为false
    boolean isEnd;
    //子节点
    List<Node> childList;

    public Node(char word) {
        super();
        this.word = word;
        isEnd = false;
        childList = new LinkedList<Node>();
    }

    //查找当前子节点中是否保护c的节点
    public Node findNode(char c) {
        for (Node node : childList) {
            if (node.word == c) {
                return node;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("word=").append(word);
        sb.append(", isEnd=").append(isEnd);
        sb.append(", childList=").append(childList);
        sb.append('}');
        return sb.toString();
    }
}