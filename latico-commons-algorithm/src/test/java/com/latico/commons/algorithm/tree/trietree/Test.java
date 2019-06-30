package com.latico.commons.algorithm.tree.trietree;

import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-21 10:06
 * @Version: 1.0
 */
class Test {
    public static void main(String[] args) {
        TrieTree tree = new TrieTree();
        tree.insert("北京");
        tree.insert("海淀区");
        tree.insert("中国");
        tree.insert("中国人民");
        tree.insert("中关村");

        String word = "中国";
        //查找该词是否存在 Trid Tree 中
        boolean flag = tree.search(word);
        if (flag) {
            System.out.println("Trie Tree 中已经存在【" + word + "】");
        } else {
            System.out.println("Trie Tree 不包含【" + word + "】");
        }

        //分词
        Map<String, Integer> map = tree.tokenizer("中国人民，中国首都是北京，中关村在海淀区,中国北京天安门。中国人");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }
}
