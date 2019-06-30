package com.latico.commons.elasticsearch6.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 向ES中添加数据
 * 
 * @author xuwenjin
 */
public class AddDataTest extends AbstractJunitTest {

    // private static final String INDEX = "test_index1";
    // private static final String TYPE = "test_type1";
    private static final String INDEX2 = "test_index2";
    private static final String TYPE2 = "test_type2";

    @Test
    public void saveData() {
        try {
            Map<String, Object> source = new HashMap<String, Object>();
            source.put("code", "01");
            source.put("name", "科技");
            source.put("info", "中共十九大");
            source.put("content", "中共十九大");
            source.put("my_title", "我的标题12323abcd");
            source.put("you_title", "你的标题1efg");
            source.put("isDelete", true);
            source.put("age", 16);

            Map<String, Object> source2 = new HashMap<String, Object>();
            source2.put("code", "02");
            source2.put("name", "新闻");
            source2.put("info", "中国周恩来");
            source2.put("content", "中国周恩来");
            source2.put("my_title", "我的标题235325abcd");
            source2.put("you_title", "你的标题346565efg");
            source2.put("isDelete", false);
            source2.put("age", 17);

            Map<String, Object> source3 = new HashMap<String, Object>();
            source3.put("code", "03");
            source3.put("name", "科学技术");
            source3.put("info", "用友建筑大武汉");
            source3.put("content", "用友建筑大武汉");
            source3.put("my_title", "我的标题65845abcd");
            source3.put("you_title", "你的标题237678efg");
            source3.put("isDelete", false);
            source3.put("age", 18);

            Map<String, Object> source4 = new HashMap<String, Object>();
            source4.put("code", "04");
            source4.put("name", "快手视频");
            source4.put("info", "中国特色社会主义");
            source4.put("content", "中国特色社会主义");
            source4.put("my_title", "我的标题6789dfgf");
            source4.put("you_title", "你的标题67458sdfdf");
            source4.put("isDelete", true);
            source4.put("age", 19);

            Map<String, Object> source5 = new HashMap<String, Object>();
            source5.put("code", "05");
            source5.put("name", "科技视频");
            source5.put("info", "最美天安门");
            source5.put("content", "最美天安门");
            source5.put("my_title", "132445dfgdfg");
            source5.put("you_title", "32557fdgfg");
            source5.put("isDelete", true);
            source5.put("age", 20);

            Map<String, Object> source6 = new HashMap<String, Object>();
            source6.put("code", "06");
            source6.put("name", "最快的技术");
            source6.put("info", "美丽大武汉");
            source6.put("content", "美丽大武汉");
            source6.put("my_title", "356thjmkj345");
            source6.put("you_title", "4gfjgfjg4523");
            source6.put("isDelete", false);
            source6.put("age", 21);

            client.prepareIndex(INDEX2, TYPE2).setId("1").setSource(source).get();
            client.prepareIndex(INDEX2, TYPE2).setId("2").setSource(source2).get();
            client.prepareIndex(INDEX2, TYPE2).setId("3").setSource(source3).get();
            client.prepareIndex(INDEX2, TYPE2).setId("4").setSource(source4).get();
            client.prepareIndex(INDEX2, TYPE2).setId("5").setSource(source5).get();
            client.prepareIndex(INDEX2, TYPE2).setId("6").setSource(source6).get();

        } catch (Exception e) {
            logger.error("保存数据失败！", e);
        }
    }

}