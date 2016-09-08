package com.thinkgem.jeesite.common.utils;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.tm.entity.Registration;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;

public class ImageUtils {

    public static String IMAGE_TYPE_GIF = "gif"; // 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg"; // 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg"; // 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp"; // 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png"; // 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd"; // Photoshop的专用格式Photoshop

    public static String path1 = "d:/test.jpg";
    public static String path2 = "d:/scale.jpg";

    public static void main(String[] args) {
        // 1-缩放图像：
        // 方法一：按比例缩放
        // ImageUtils.scale(path1, path2, 2, false);// false 缩小 true 放大
        // 方法二：按高度和宽度缩放
        // ImageUtils.scale2(path1,path2, 500, 300, true);//测试OK

        // 2-旋转图像：
        // 方法一：旋转图像
        // ImageUtils.rotate(path1, path2, 120);
        // 水平翻转图像
        // ImageUtils.flip(path1, path2);

        // 3-切割图像：
        // 方法一：按指定起点坐标和宽高切割
        // ImageUtils.cut(path1, path2, 0, 0, 150, 200 );//测试OK
        // 方法二：指定切片的行数和列数
        // ImageUtils.cut2(path1, path2, 8, 8 );//测试OK
        // 方法三：指定切片的宽度和高度
        // ImageUtils.cut3(path1, path2, 100, 100 );//测试OK

        // 4-图像类型转换：
        // ImageUtils.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK

        // 5-彩色转黑白：
        // ImageUtils.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK

        // 6-给图片添加文字水印：
        // 方法一：
        // ImageUtils.pressText("报告编号：201607290001", "d:/abc.jpg",
        // "d:/abc_pressText.jpg", "微软雅黑", Font.BOLD, Color.black,
        // 40, 0, 0, 0.5f);// 测试OK
        // 方法二：
        // ImageUtils.pressText2("我也是水印文字",
        // "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white, 80, 0,
        // 0, 0.5f);//测试OK
        // 方法三：
        // Color color = Color.decode("#000000");
        // ImageUtils.pressTextWithLine("报告编号：201607290001", "d:/abc.jpg",
        // "d:/abc_pressText.jpg", "微软雅黑", Font.PLAIN,
        // color, 40, 0, 300, 1.0f);// 测试OK
        ImageUtils.scale2("D:\\a.jpg", 400, 500);// false
        // 缩小
        // true
        // 放大

        // 7-给图片添加图片水印：
        // ImageUtils.pressImage("e:/abc2.jpg",
        // "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
    }

    /**
     * 以JPEG编码保存图片
     *
     * @param image_to_save   要处理的图像图片
     * @param JPEGcompression 压缩比
     * @throws IOException
     */
    public static void saveAsJPEG(BufferedImage image_to_save, float JPEGcompression, String destFilePath)
            throws IOException {

        // Image writer
        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
        FileOutputStream fos = new FileOutputStream(destFilePath); // 输出到文件流
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);

        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        // old write and clean
        // jpegEncoder.encode(image_to_save, jpegEncodeParam);

        // new Write and clean up
        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

    }

    /*
     * 图片缩放,w，h为缩放的目标宽度和高度 src为源文件目录
     */
    public static Image zoomImage(String src, int w, int h) throws Exception {

        double wr = 0, hr = 0;
        File srcFile = new File(src);

        BufferedImage bufImg = ImageIO.read(srcFile); // 读取图片
        Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);// 设置缩放目标图片模板

        wr = w * 1.0 / bufImg.getWidth(); // 获取缩放比例
        hr = h * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);

        // try {
        // ImageIO.write((BufferedImage) Itemp,
        // dest.substring(dest.lastIndexOf(".") + 1), destFile); // 写入缩减后的图片
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }

        return Itemp;
    }

    public final static void scale(String srcImageFile, String result, int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag) { // 放大
                width = width * scale;
                height = height * scale;
            } else { // 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();

            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                tag = op.filter(bi, null);
            }
            if (bb) { // 补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                g.dispose();
                tag = image;
            }

            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static Image scale2(String srcImageFile, int height, int width) {
        Image itemp = null;
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                tag = op.filter(bi, null);
            } else { // 补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                            itemp.getHeight(null), Color.white, null);
                g.dispose();
                tag = image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree) {
        int w = bufferedimage.getWidth();// 得到图片宽度。
        int h = bufferedimage.getHeight();// 得到图片高度。
        int type = bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
                .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.drawImage(bufferedimage, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.dispose();
        return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
    }

    public static BufferedImage flipImage(BufferedImage bufferedimage) {
        int w = bufferedimage.getWidth();// 得到宽度。
        int h = bufferedimage.getHeight();// 得到高度。
        BufferedImage img;// 空图片。
        Graphics2D graphics2d;// 空画笔。
        (graphics2d = (img = new BufferedImage(w, h, bufferedimage.getColorModel().getTransparency())).createGraphics())
                .drawImage(bufferedimage, 0, 0, w, h, w, 0, 0, h, null);
        graphics2d.dispose();
        return img;
    }

    public final static void rotate(String srcImageFile, String result, int num) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            src = rotateImage(src, num);
            Image image = src.getScaledInstance(src.getWidth(), src.getHeight(), Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void flip(String srcImageFile, String result) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            src = flipImage(src);
            Image image = src.getScaledInstance(src.getWidth(), src.getHeight(), Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit()
                        .createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void cut2(String srcImageFile, String descDir, int rows, int cols) {
        try {
            if (rows <= 0 || rows > 20)
                rows = 2; // 切片行数
            if (cols <= 0 || cols > 20)
                cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit()
                                .createImage(new FilteredImageSource(image.getSource(), cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
        try {
            if (destWidth <= 0)
                destWidth = 200; // 切片宽度
            if (destHeight <= 0)
                destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit()
                                .createImage(new FilteredImageSource(image.getSource(), cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void pressText(String pressText, String srcImageFile, String destImageFile, String fontName,
                                       int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressText2(String pressText, String srcImageFile, String destImageFile, String fontName,
                                        int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressSellInfo(String pressImg, String name, String no, String mobile, String srcImageFile,
                                           String destImageFile, String fontName, int fontStyle, Color color, int fontSize) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(name, 600, 878);
            g.drawString(no, 600, 1023);
            g.drawString(mobile, 600, 1169);
            System.out.println("pressImg--------------" + pressImg);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            // int wideth_biao = src_biao.getWidth(null);
            // int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g.drawImage(src_biao, 230, 780, 300, 400, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressMainInfo(String pressImg, String applicantCn, String businessType, String tmName,
                                           String srcImageFile, String destImageFile, String fontName, int fontStyle, Color color, int fontSize) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(applicantCn, 150, 410);
            g.drawString(businessType, 150, 570);
            g.drawString(tmName, 150, 730);
            if (pressImg != null && !pressImg.equals("")) {
                // 水印文件
                // Image src_biao = ImageIO.read(new File(pressImg));
                // int wideth_biao = src_biao.getWidth(null);
                // int height_biao = src_biao.getHeight(null);
                Image src_biao = zoomImage(pressImg, 300, 300);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                g.drawImage(src_biao, 150, 902, 300, 300, null);
            }

            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressSimilarInfo(String pressImg, String sTmName, String sCls, String sRegNoAndStatus,
                                              String sApplicantCn, String sSimilarity, String advise, String srcImageFile, String destImageFile) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.setColor(Color.decode("#000000"));
            g.setFont(new Font("微软雅黑", Font.BOLD, 50));
            g.drawString(sCls, 150, 373);

            g.setColor(Color.decode("#000000"));
            g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
            g.drawString(sTmName, 150, 565);
            g.drawString(sRegNoAndStatus, 150, 710);
            g.drawString(sApplicantCn, 150, 850);

            g.setColor(Color.decode("#e63917"));
            g.setFont(new Font("微软雅黑", Font.BOLD, 30));
            g.drawString(advise, 150, 1338);

            g.setColor(Color.decode("#e63917"));
            g.setFont(new Font("微软雅黑", Font.BOLD, 100));
            g.drawString(sSimilarity + "%", 836, 612);
            // 水印文件
            if (pressImg != null && !pressImg.equals("")) {
                // Image src_biao = ImageIO.read(new File(pressImg));
                // g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                // 1.0f));
                // g.drawImage(src_biao, 150, 964, 250, 250, null);
                Image src_biao = zoomImage(pressImg, 250, 250);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                g.drawImage(src_biao, 150, 962, 250, 250, null);
            }
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressAdviseInfo(Registration registration, String srcImageFile, String destImageFile,
                                             int fontSize) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.decode("#000000"));
            g.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));

            int recNormalY = fontSize + 469;
            int recUrgentY = fontSize + 560;
            int extNormalY = fontSize + 990;
            int extUrgentY = fontSize + 1079;

            // 注册类别
            int recNormalX = 384 - (getLength(registration.getRecNormal()) * fontSize / 2);
            int recUrgentX = 384 - (getLength(registration.getRecUrgent()) * fontSize / 2);
            int extNormalX = 384 - (getLength(registration.getExtNormal()) * fontSize / 2);
            int extUrgentX = 384 - (getLength(registration.getExtUrgent()) * fontSize / 2);

            g.drawString(registration.getRecNormal(), recNormalX, recNormalY);
            g.drawString(registration.getRecUrgent(), recUrgentX, recUrgentY);
            g.drawString(registration.getExtNormal(), extNormalX, extNormalY);
            g.drawString(registration.getExtUrgent(), extUrgentX, extUrgentY);

            // 注册件数
            Integer recNormalNum = countChar(registration.getRecNormal(), ',') + 1;
            Integer recUrgentNum = countChar(registration.getRecUrgent(), ',') + 1;
            Integer extNormalNum = countChar(registration.getExtNormal(), ',') + 1;
            Integer extUrgentNum = countChar(registration.getExtUrgent(), ',') + 1;

            int recNormalNumX = 545 - (getLength(recNormalNum.toString()) * fontSize / 2);
            int recUrgentNumX = 545 - (getLength(recUrgentNum.toString()) * fontSize / 2);
            int extNormalNumX = 545 - (getLength(extNormalNum.toString()) * fontSize / 2);
            int extUrgentNumX = 545 - (getLength(extNormalNum.toString()) * fontSize / 2);

            g.drawString(recNormalNum.toString(), recNormalNumX, recNormalY);
            g.drawString(recUrgentNum.toString(), recUrgentNumX, recUrgentY);
            g.drawString(extNormalNum.toString(), extNormalNumX, extNormalY);
            g.drawString(extUrgentNum.toString(), extUrgentNumX, extUrgentY);

            // 服务价格
            Double price = Double.parseDouble(Global.getConfig("tm.tmkoo.price"));// 单价
            Double raisePrice = Double.parseDouble(Global.getConfig("tm.tmkoo.raisePrice")) + price;// 加急单价
            int priceX = 600 + (705 - 600) - (getLength(price.toString()) * fontSize / 2);
            // 普通服务价格
            g.drawString(new DecimalFormat("#").format(price), priceX, recNormalY);
            g.drawString(new DecimalFormat("#").format(price), priceX, extNormalY);
            // 加急服务价格
            g.drawString(new DecimalFormat("#").format(raisePrice), priceX, recUrgentY);
            g.drawString(new DecimalFormat("#").format(raisePrice), priceX, extUrgentY);

            // 预计费用
            int recNormalTotalPriceX = 866
                    - (getLength(registration.getRecNormalTotalPrice().toString()) * fontSize / 2);
            int extNormalTotalPriceX = 866
                    - (getLength(registration.getExtNormalTotalPrice().toString()) * fontSize / 2);

            int recUrgentTotalPriceX = 866
                    - (getLength(registration.getRecUrgentTotalPrice().toString()) * fontSize / 2);
            int extUrgentTotalPriceX = 866
                    - (getLength(registration.getExtUrgentTotalPrice().toString()) * fontSize / 2);

            g.drawString(registration.getRecNormalTotalPrice().toString(), recNormalTotalPriceX, recNormalY);
            g.drawString(registration.getRecUrgentTotalPrice().toString(), recUrgentTotalPriceX, recUrgentY);
            g.drawString(registration.getExtNormalTotalPrice().toString(), extNormalTotalPriceX, extNormalY);
            g.drawString(registration.getExtUrgentTotalPrice().toString(), extUrgentTotalPriceX, extUrgentY);

            // 活动优惠价
            g.setColor(Color.decode("#e63917"));
            g.setFont(new Font("微软雅黑", Font.BOLD, fontSize));

            String recNormalDiscountPrice = registration.getRecNormalDiscountPrice();
            if (recNormalDiscountPrice.equals("0") || recNormalDiscountPrice.equals("")) {
                recNormalDiscountPrice = "暂无";
            }
            String recUrgentDiscountPrice = registration.getRecUrgentDiscountPrice();
            if (recUrgentDiscountPrice.equals("0") || recUrgentDiscountPrice.equals("")) {
                recUrgentDiscountPrice = "暂无";
            }
            String extNormalDiscountPrice = registration.getExtNormalDiscountPrice();
            if (extNormalDiscountPrice.equals("0") || extNormalDiscountPrice.equals("")) {
                extNormalDiscountPrice = "暂无";
            }
            String extUrgentDiscountPrice = registration.getExtUrgentDiscountPrice();
            if (extUrgentDiscountPrice.equals("0") || extUrgentDiscountPrice.equals("")) {
                extUrgentDiscountPrice = "暂无";
            }

            int recNormalDiscountPriceX = 1025
                    - (getLength(recNormalDiscountPrice) * fontSize / 2);
            int extNormalDiscountPriceX = 1025
                    - (getLength(extNormalDiscountPrice) * fontSize / 2);

            int recUrgentDiscountPriceX = 1025
                    - (getLength(recUrgentDiscountPrice) * fontSize / 2);
            int extUrgentDiscountPriceX = 1025
                    - (getLength(extUrgentDiscountPrice) * fontSize / 2);

            g.drawString(recNormalDiscountPrice, recNormalDiscountPriceX, recNormalY);
            g.drawString(recUrgentDiscountPrice, recUrgentDiscountPriceX, recUrgentY);
            g.drawString(extNormalDiscountPrice, extNormalDiscountPriceX, extNormalY);
            g.drawString(extUrgentDiscountPrice, extUrgentDiscountPriceX, extUrgentY);

            // 总价&活动价
            g.setColor(Color.decode("#000000"));
            g.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
            g.drawString("总价：", 705, 675);
            g.drawString("总价：", 705, 1195);
            g.drawString("活动价：", 900, 675);
            g.drawString("活动价：", 900, 1195);
            // 总价
            Integer recTotalPrice = registration.getRecNormalTotalPrice() + registration.getRecUrgentTotalPrice();
            Integer extTotalPrice = registration.getExtNormalTotalPrice() + registration.getExtUrgentTotalPrice();

            g.setColor(Color.decode("#000000"));
            g.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
            g.drawString(recTotalPrice + "元", 780, 675);
            g.drawString(extTotalPrice + "元", 780, 1195);
            // 活动价
            String recDiscountPrice = "";
            if (recNormalDiscountPrice.equals("暂无") && recUrgentDiscountPrice.equals("暂无")) {
                recDiscountPrice = "暂无";
            } else if (recNormalDiscountPrice.equals("暂无") && !recUrgentDiscountPrice.equals("暂无")) {
                recDiscountPrice = String.valueOf(registration.getRecNormalTotalPrice()
                        + Integer.parseInt(recUrgentDiscountPrice)) + "元";
            } else if (!recNormalDiscountPrice.equals("暂无") && recUrgentDiscountPrice.equals("暂无")) {
                recDiscountPrice = String.valueOf(Integer.parseInt(recNormalDiscountPrice)
                        + registration.getRecUrgentTotalPrice()) + "元";
            } else {
                recDiscountPrice = String.valueOf(registration.getRecNormalTotalPrice()
                        + registration.getRecUrgentTotalPrice()) + "元";
            }


            String extDiscountPrice = "";
            if (extNormalDiscountPrice.equals("暂无") && extUrgentDiscountPrice.equals("暂无")) {
                extDiscountPrice = "暂无";
            } else if (extNormalDiscountPrice.equals("暂无") && !extUrgentDiscountPrice.equals("暂无")) {
                extDiscountPrice = String.valueOf(registration.getExtNormalTotalPrice()
                        + Integer.parseInt(extUrgentDiscountPrice)) + "元";
            } else if (!extNormalDiscountPrice.equals("暂无") && extUrgentDiscountPrice.equals("暂无")) {
                extDiscountPrice = String.valueOf(Integer.parseInt(extNormalDiscountPrice)
                        + registration.getExtUrgentTotalPrice()) + "元";
            } else {
                extDiscountPrice = String.valueOf(registration.getExtNormalTotalPrice()
                        + registration.getExtUrgentTotalPrice()) + "元";
            }
            g.setColor(Color.decode("#e63917"));
            g.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
            g.drawString(recDiscountPrice, 1000, 675);
            g.drawString(extDiscountPrice, 1000, 1195);

            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressWxQr(String pressImg, String name, String srcImageFile, String destImageFile,
                                       String fontName, int fontStyle, Color color, int fontSize) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(name, 600 - (getLength(name) * fontSize / 2), 842);
            System.out.println("pressImg--------------" + pressImg);
            // 二维码
            Image src_biao = ImageIO.read(new File(pressImg));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g.drawImage(src_biao, 450, 480, 300, 300, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressTextWithLine(String pressText, String srcImageFile, String destImageFile,
                                               String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 添加下划线
            Font font = new Font(fontName, fontStyle, fontSize);
            AttributedString as = new AttributedString(pressText);
            as.addAttribute(TextAttribute.FONT, font);
            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 5, countInt(pressText) + 5);
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 在指定坐标绘制水印文字
            g.drawString(as.getIterator(), (width - (getLength(pressText) * fontSize)) / 2 + x,
                    (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressImage(String pressImg, String srcImageFile, String destImageFile, int x, int y,
                                        float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao,
                    null);
            // 水印文件结束
            g.dispose();
            ImageIO.write(image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    public final static int countChar(String text, char str) {
        int szCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == str) {
                szCount++;
            }
        }
        return szCount;
    }

    public static int countInt(String str) {
        int szCount = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                szCount++;
            }
        }
        return szCount;
    }
}