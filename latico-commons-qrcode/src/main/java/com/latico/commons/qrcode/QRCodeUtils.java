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
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-02 11:51:23
 * @version: 1.0
 */
public class QRCodeUtils {
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(QRCodeUtils.class);

    private static final String CHARSET = "utf-8";
    /**
     *
     */
    private static final String FORMAT_NAME = "JPG";
    /**
     * 二维码尺寸，正方形
     */
    private static final int DEFAULT_QRCODE_SIZE = 300;
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
     * @param imgFilePath
     * @return
     * @throws IOException
     */
    private static BufferedImage readImageByFile(String imgFilePath) throws IOException {
        return ImageIO.read(new File(imgFilePath));
    }


    /**
     * 写图片到文件
     * @param image 图片
     * @param filePath 文件路径
     * @throws IOException
     */
    private static void writeImageToFile(BufferedImage image, String filePath) throws IOException {
        ImageIO.write(image, FORMAT_NAME, new File(filePath));
    }

    /**
     * 核心方法
     * 创建的二维码图片对象
     * @param content 二维码里面存储的内容
     * @param width   宽度
     * @param height 高度
     * @return 二维码的图片对象
     * @throws Exception
     */
    private static BufferedImage createQrCodeImage(String content, int width, int height) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        int qrCodeWidth = bitMatrix.getWidth();
        int  qrCodeHeight = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(qrCodeWidth,  qrCodeHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < qrCodeWidth; x++) {
            for (int y = 0; y <  qrCodeHeight; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return image;
    }

    /**
     * 核心方法
     * 插入LOGO图片到二维码中间
     * @param qrCodeImg 二维码图片对象
     * @param logoImg LOGO图片对象
     * @param compressLogoToDefaultSize 是否压缩LOGO图片到默认大小
     * @throws Exception
     */
    private static void insertLogoImageToQrCode(BufferedImage qrCodeImg, Image logoImg, boolean compressLogoToDefaultSize) throws Exception {
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);

        // 压缩LOGO
        if (compressLogoToDefaultSize) {
            if (logoWidth > DEFAULT_LOGO_IMAGE_WIDTH) {
                logoWidth = DEFAULT_LOGO_IMAGE_WIDTH;
            }
            if (logoHeight > DEFAULT_LOGO_IMAGE_HEIGHT) {
                logoHeight = DEFAULT_LOGO_IMAGE_HEIGHT;
            }
            Image image = logoImg.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = tag.getGraphics();
            // 绘制缩小后的图
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            logoImg = image;
        }

        // 插入LOGO
        Graphics2D graph = qrCodeImg.createGraphics();
        int x = (qrCodeImg.getWidth() - logoWidth) / 2;
        int y = (qrCodeImg.getHeight() - logoHeight) / 2;
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 核心方法
     * 解析二维码
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


    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createQrCodeImage(content, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE);
        // 插入图片
        insertLogoImageToQrCode(image, imgPath, needCompress);
        writeImageToFile(image, destPath);
    }


    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = createQrCodeImage(content, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE);
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
        BufferedImage image = createQrCodeImage(content, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE);
        // 插入图片
        insertLogoImageToQrCode(image, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        return decodeQrCode(image);
    }

    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }

}