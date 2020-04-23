package com.latico.commons.net.socket;

import com.latico.commons.net.socket.bean.StrEndTagResult;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * socket的数据解析
 * 实用场景：收到个总的字符串后，里面是根据开始标志和结束标志进行分段的，
 * 所以需要调用下面方法进行切割，
 * 但是又由于可能存在目前收到的报文不完整的情况，
 * 所以会把补完整剩下部分也保留下来，用于跟后面接收的新数据进行组拼
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-17 17:15
 * @version: 1.0
 */
public class SocketDataParser {

    /**
     * 把数据报文根据结束符解析成一段段的数据
     * 1、如果同时指定了开始符号，也指定的结束符号，那么，会先找到开始符号，然后开始查找第一个结束符号才算一段报文结束（忽略其中是否有新的开始标志）
     * @param data 数据报文
     * @param startTag 非必须，如果指定了开始标志，从开始标志开始截取数据，如果没有指定开始标志，那就只以结束标志为准
     * @param endTag 指定结束符，必须
     * @return 字符串结束符解析结果,不完整的尾巴数据也返回
     */
    public static StrEndTagResult parseByTag(String data, String startTag, String endTag) {
        StrEndTagResult strEndTagResult = new StrEndTagResult();
        List<String> results = new ArrayList<>();
        strEndTagResult.setResults(results);
        if (data == null) {
            return null;
        }

        boolean hasStartTag = startTag != null && !"".equals(startTag);
        //如果指定了开始标志
        if (hasStartTag) {
            int index = data.indexOf(startTag);
            //如果开始标志为空，那直接返回
            if (index == -1) {
                strEndTagResult.setSurplusData(data);
                return strEndTagResult;
            }else{
                //从开始标志开始截取数据
                data = data.substring(index);
            }
        }

        final int endTagLen = endTag.length();

        while (true) {
            if (!hasStartTag) {
                int indexOfEndTag = data.indexOf(endTag);
                if (indexOfEndTag == -1) {
                    strEndTagResult.setSurplusData(data);
                    break;
                }

                //没有开始标志，那就以结束标志为准
                results.add(data.substring(0, indexOfEndTag + endTagLen));
                data = data.substring(indexOfEndTag + endTagLen);
                if (data == null || "".equals(data)) {
                    break;
                }


            }else{

                //有开始标志，需要结合开始和结束标志
                int indexOfStartTag = data.indexOf(startTag);
                int indexOfEndTag = data.indexOf(endTag);

                //如果结束标志在开始标志之前，那就需要进行结束标志后移，截取以开始标志为开头后进行下一轮
                if (indexOfStartTag > indexOfEndTag) {
                    data = data.substring(indexOfStartTag);
                    continue;
                }

                //必须同时存在开始和结束标志
                if (indexOfEndTag == -1 || indexOfStartTag == -1) {
                    strEndTagResult.setSurplusData(data);
                    break;

                } else {
                    results.add(data.substring(indexOfStartTag, indexOfEndTag + endTagLen));
                    data = data.substring(indexOfEndTag + endTagLen);
                    if (data == null || "".equals(data)) {
                        break;
                    }
                }

            }

        }

        return strEndTagResult;
    }
}
