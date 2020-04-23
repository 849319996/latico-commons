package com.latico.commons.net.httpclient;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.codec.Base64Utils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-03 0:31
 * @version: 1.0
 */
public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    private static final String DEFAULT_CHARSET = CharsetType.UTF8;
    /** 页面使用BASE64存储的图像信息正则 */
    private final static String RGX_BASE64_IMG = "data:image/([^;]+);base64,(.*)";
    /**
     * 判断HTTP响应状态码是否为成功
     * @param httpStatus 响应状态码
     * @return
     */
    public static boolean isOkStatusCode(int httpStatus) {
        return (httpStatus == HttpStatus.SC_OK);
    }

    /**
     * URL发送端对URL进行编码，解决URL不能传输中文问题
     * @param url
     * @return
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error(e);
        }
        return url;
    }
    /**
     * URL发送端对URL进行编码，解决URL不能传输中文问题
     * @param url
     * @param charset
     * @return
     */
    public static String encodeUrl(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (Exception e) {
            LOG.error(e);
        }
        return url;
    }

    /**
     * 解决URL不能传输中文问题，URL接收端对URL进行解码
     * @param url
     * @return
     */
    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error(e);
        }
        return url;
    }
    /**
     * 解决URL不能传输中文问题，URL接收端对URL进行解码
     * @param url
     * @param charset
     * @return
     */
    public static String decodeUrl(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (Exception e) {
            LOG.error(e);
        }
        return url;
    }

    /**
     * 保存Base64编码的图片数据到本地
     * @param dataUrl 图片数据编码地址，格式形如   data:imagebridger/png;base64,base64编码的图片数据
     * @param saveDir 希望保存的图片目录
     * @param imgName 希望保存的图片名称（不含后缀，后缀通过编码自动解析）
     * @return 图片保存路径（若保存失败则返回空字符串）
     */
    public static String saveBase64Img(String dataUrl,
                                          String saveDir, String imgName) {
        String savePath = "";
        Pattern ptn = Pattern.compile(RGX_BASE64_IMG);
        Matcher mth = ptn.matcher(dataUrl);
        if(mth.find()) {
            String ext = mth.group(1);	// 图片后缀
            String base64Data = mth.group(2);	// 图片数据
            savePath = StringUtils.join(saveDir, "/", imgName, ".", ext);

            try {
                byte[] data = Base64Utils.decode(base64Data);
                FileUtils.writeByteArrayToFile(new File(savePath), data, false);

            } catch (Exception e) {
                LOG.error("转换Base64编码图片数据到本地文件失败: [{}]", savePath, e);
            }
        }
        return savePath;
    }
}
