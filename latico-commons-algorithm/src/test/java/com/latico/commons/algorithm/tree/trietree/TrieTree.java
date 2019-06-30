package com.latico.commons.algorithm.tree.trietree;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-05-21 10:10:45
 * @Version: 1.0
 */
class TrieTree {
    Node root = new Node(' ');

    //构建Trie Tree
    public void insert(String words) {
        char[] arr = words.toCharArray();
        Node currentNode = root;
        for (char c : arr) {
            Node node = currentNode.findNode(c);
            //如果不存在该节点则添加
            if (node == null) {
                Node n = new Node(c);
                currentNode.childList.add(n);
                currentNode = n;
            } else {
                currentNode = node;
            }
        }
        //在词的最后一个字节点标记为true
        currentNode.isEnd = true;
    }

    //判断Trie Tree中是否包含该词
    public boolean search(String word) {
        char[] arr = word.toCharArray();
        Node currentNode = root;
        for (int i = 0; i < arr.length; i++) {
            Node n = currentNode.findNode(arr[i]);
            if (n != null) {
                currentNode = n;
                //判断是否为词的尾节点节点
                if (n.isEnd) {
                    if (n.word == arr[arr.length - 1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //最大匹配优先原则
    public Map<String, Integer> tokenizer(String words) {
        char[] arr = words.toCharArray();
        Node currentNode = root;
        Map<String, Integer> map = new HashMap<String, Integer>();
        //记录Trie Tree 从root开始匹配的所有字
        StringBuilder sb = new StringBuilder();
        ;
        //最后一次匹配到的词，最大匹配原则，可能会匹配到多个字，以最长的那个为准
        String word = "";
        //记录记录最后一次匹配坐标
        int idx = 0;
        for (int i = 0; i < arr.length; i++) {
            Node n = currentNode.findNode(arr[i]);
            if (n != null) {
                sb.append(n.word);
                currentNode = n;
                //匹配到词
                if (n.isEnd) {
                    //记录最后一次匹配的词
                    word = sb.toString();
                    //记录最后一次匹配坐标
                    idx = i;
                }
            } else {
                //判断word是否有值
                if (word != null && word.length() > 0) {
                    Integer num = map.get(word);
                    if (num == null) {
                        map.put(word, 1);
                    } else {
                        map.put(word, num + 1);
                    }
                    //i回退到最后匹配的坐标
                    i = idx;
                    //从root的开始匹配
                    currentNode = root;
                    //清空匹配到的词
                    word = null;
                    //清空当前路径匹配到的所有字
                    sb = new StringBuilder();
                }
            }
            if (i == arr.length - 2) {
                if (word != null && word.length() > 0) {
                    Integer num = map.get(word);
                    if (num == null) {
                        map.put(word, 1);
                    } else {
                        map.put(word, num + 1);
                    }
                }
            }
        }

        return map;
    }
}