package com.latico.commons.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

/**
 * <PRE>
 * 二维码工具
 * <p>
 * 二维码存的信息越多，二维码图片也就越复杂，容错率也就越低，识别率也越低，
 * 并且二维码能存的内容大小也是有限的（大概500个汉字左右）
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-02 11:51:23
 * @version: 1.0
 */
public class QrCodeUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(QrCodeUtils.class);

    private static final String CHARSET = "utf-8";
    /**
     * 写图片文件的格式
     */
    private static final String DEFAULT_OUT_IMAGE_FORMAT_NAME = "JPG";
    /**
     * 二维码尺寸，正方形
     */
    private static final int DEFAULT_QRCODE_SIZE = 300;

    /**
     * 默认的错误更正等级
     */
    private static final ErrorCorrectionLevel DEFAULT_ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.M;

    /**
     * 默认的自定义LOGO宽度
     */
    private static final int DEFAULT_LOGO_IMAGE_WIDTH = 60;
    /**
     * 默认的自定义LOGO高度
     */
    private static final int DEFAULT_LOGO_IMAGE_HEIGHT = 60;

    /**
     * 读取图片文件
     *
     * @param imgFilePath
     * @return
     * @throws IOException
     */
    private static BufferedImage readImageByFile(String imgFilePath) throws IOException {
        File file = new File(imgFilePath);
        return readImageByFile(file);
    }

    /**
     * 读取图片文件
     * @param file
     * @return
     * @throws IOException
     */
    private static BufferedImage readImageByFile(File file) throws IOException {
        return ImageIO.read(file);
    }

    /**
     * 写图片到文件
     *
     * @param image    图片
     * @param filePath 文件路径
     * @throws IOException
     */
    private static void writeImageToFile(BufferedImage image, String filePath) throws IOException {
        //获取格式
        String format = filePath.substring(filePath.lastIndexOf(".") + 1);
        ImageIO.write(image, format, new File(filePath));
    }

    /**
     * 把图片写到输出流，图片格式使用默认的JPG
     * @param image
     * @param outputStream
     * @throws IOException
     */
    private static void writeImageToFile(BufferedImage image, OutputStream outputStream) throws IOException {
        ImageIO.write(image, DEFAULT_OUT_IMAGE_FORMAT_NAME, outputStream);
    }

    /**
     * 创建一个二维码图片对象
     * @param content 二维码内容
     * @param width 二维码图片的宽度
     * @param height 二维码图片的高度
     * @param errorCorrectionLevel 二维码的纠错等级，能对被破坏的程度进行自动修复能力
     * @return 二维码图片对象
     * @throws Exception
     */
    private static BufferedImage createQrCodeImage(String content, int width, int height, ErrorCorrectionLevel errorCorrectionLevel) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < qrCodeWidth; x++) {
            for (int y = 0; y < qrCodeHeight; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return image;
    }

    /**
     * 核心方法
     * 插入LOGO图片到二维码中间
     *
     * @param qrCodeImg  二维码图片对象
     * @param logoImg    LOGO图片对象
     * @param logoWidth  指定logo的宽度
     * @param logoHeight 指定logo的高度
     * @throws Exception
     */
    private static void insertLogoImageToQrCode(BufferedImage qrCodeImg, Image logoImg, int logoWidth, int logoHeight) throws Exception {
        int logoImgWidth = logoImg.getWidth(null);
        int logoImgHeight = logoImg.getHeight(null);

        // 压缩LOGO
        if (logoImgWidth != logoWidth || logoImgHeight != logoHeight) {
            logoImgWidth = logoWidth;
            logoImgHeight = logoHeight;
            Image image = logoImg.getScaledInstance(logoImgWidth, logoImgHeight, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(logoImgWidth, logoImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = tag.getGraphics();
            // 绘制缩小后的图
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            logoImg = image;
        }

        // 插入LOGO
        Graphics2D graph = qrCodeImg.createGraphics();
        int x = (qrCodeImg.getWidth() - logoImgWidth) / 2;
        int y = (qrCodeImg.getHeight() - logoImgHeight) / 2;
        graph.drawImage(logoImg, x, y, logoImgWidth, logoImgHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoImgWidth, logoImgWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 核心方法
     * 解析二维码
     *
     * @param qrCodeImage 二维码图片对象
     * @return 二维码内容
     * @throws Exception
     */
    public static String decodeQrCode(BufferedImage qrCodeImage) throws Exception {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Map hints = new HashMap();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 默认参数 创建的二维码图片对象
     *
     * @param content 二维码里面存储的内容
     * @return 二维码的图片对象
     * @throws Exception
     */
    private static BufferedImage createQrCodeImage(String content) throws Exception {
        return createQrCodeImage(content, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE, DEFAULT_ERROR_CORRECTION_LEVEL);
    }

    /**
     * 插入LOGO图片到二维码中间
     *
     * @param qrCodeImg                 二维码图片对象
     * @param logoImg                   LOGO图片对象
     * @param compressLogoToDefaultSize 是否压缩LOGO图片到默认大小
     * @throws Exception
     */
    private static void insertLogoImageToQrCode(BufferedImage qrCodeImg, Image logoImg, boolean compressLogoToDefaultSize) throws Exception {
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        if (compressLogoToDefaultSize) {
            if (logoWidth > DEFAULT_LOGO_IMAGE_WIDTH) {
                logoWidth = DEFAULT_LOGO_IMAGE_WIDTH;
            }
            if (logoHeight > DEFAULT_LOGO_IMAGE_HEIGHT) {
                logoHeight = DEFAULT_LOGO_IMAGE_HEIGHT;
            }
        }

        insertLogoImageToQrCode(qrCodeImg, logoImg, logoWidth, logoHeight);

    }

    private static void insertLogoImageToQrCode(BufferedImage qrCodeImg, String logoImgFilePath, boolean needCompress) throws Exception {
        if (logoImgFilePath == null || "".equals(logoImgFilePath)) {
            return;
        }

        File file = new File(logoImgFilePath);
        if (!file.exists()) {
            LOG.warn("{} 文件不存在！", logoImgFilePath);
            return;
        }
        BufferedImage src = readImageByFile(logoImgFilePath);
        insertLogoImageToQrCode(qrCodeImg, src, needCompress);
    }


    public static void encode(String content, String logoImgPath, String outQrCodeFilePath, boolean needCompress) throws Exception {
        BufferedImage image = createQrCodeImage(content);
        // 插入LOGO图片
        insertLogoImageToQrCode(image, logoImgPath, needCompress);
        writeImageToFile(image, outQrCodeFilePath);
    }


    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = createQrCodeImage(content);
        // 插入图片
        insertLogoImageToQrCode(image, imgPath, needCompress);
        return image;
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        encode(content, imgPath, destPath, false);
    }

    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = createQrCodeImage(content);
        // 插入图片
        insertLogoImageToQrCode(image, imgPath, needCompress);
        writeImageToFile(image, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image = readImageByFile(file);
        if (image == null) {
            return null;
        }
        return decodeQrCode(image);
    }

    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }

}