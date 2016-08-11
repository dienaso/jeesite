package com.thinkgem.jeesite.common.utils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * 
 * @author hubiao
 * @dateTime 2014-06-07 本工具对实现对IMG与PDF相互转换。 运行测试需要导入以下2个jar包 itext-2.0.2.jar
 *           PDFRenderer.jar
 *
 */
@SuppressWarnings("unused")
public class ImagePdfUtils {
	public static void main(String[] args) throws Exception {
		// PDF包提取 pdf
		// pdfExtraction();

		// pdf转jpg
		// pdfToJpg("E:\\java\\资料pdf\\1.pdf","E:\\java\\资料pdf\\1.jpg",1);

		// 将多个jpg直接合并成pdf包
		// extractionPdf("F:\\temp\\Project\\数据\\dfdsfds\\巴黎公社活动家传略_img","F:\\temp\\Project\\数据\\dfdsfds\\巴黎公社活动家传略_img.pdf");

		// jpg转pdf
		// jpgToPdf();

		// 文件排序
		// listOrder();

		imgMerageToPdf(new File("F:\\GitHub\\jeesite\\src\\main\\webapp\\userfiles\\images\\tm\\report\\201608010004").listFiles(),
				new File("F:\\GitHub\\jeesite\\src\\main\\webapp\\userfiles\\images\\tm\\report\\201608010004\\201608010004.pdf"));
	}

	private static void listOrder() {

		File[] listFiles = new File("F:\\temp\\Project\\数据\\dfdsfds\\巴黎公社活动家传略_img").listFiles();
		TreeMap<Integer, File> tree = new TreeMap<Integer, File>();
		for (File f : listFiles) {
			tree.put(Integer.parseInt(f.getName().replaceAll(".jpg$", "")), f);
		}
		for (Entry<Integer, File> eif : tree.entrySet()) {
			System.out.println(eif.getKey() + "=" + eif.getValue().toString());
		}
	}

	/**
	 * @param list
	 *            图片集合
	 * @param file
	 *            保存路径
	 * @return true,合并完成 如果文件名不是1.jpg，2.jpg，3.jpg，4.jpg这样的。则需要自己重写TreeMap的排序方式！
	 */
	public static boolean imgMerageToPdf(File[] list, File file) throws Exception {
		// 1：对图片文件通过TreeMap以名称进行自然排序
		Map<Integer, File> mif = new TreeMap<Integer, File>();
		for (File f : list){
			if(f.getName().indexOf("large") > 0) {
				mif.put(Integer.parseInt(f.getName().replaceAll("-large.jpg$", "")), f);
			}
		}

		// 2：获取第一个Img的宽、高做为PDF文档标准
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048 * 3);
		InputStream is = new FileInputStream(mif.get(1));
		for (int len; (len = is.read()) != -1;)
			baos.write(len);

		baos.flush();
		Image image = Image.getInstance(baos.toByteArray());
		float width = image.getWidth();
		float height = image.getHeight();
		baos.close();

		// 3:通过宽高 ，实例化PDF文档对象。
		Document document = new Document(new Rectangle(width, height));
		PdfWriter pdfWr = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		// 4：获取每一个图片文件，转为IMG对象。装载到Document对象中
		for (Entry<Integer, File> eif : mif.entrySet()) {
			// 4.1:读取到内存中
			baos = new ByteArrayOutputStream(2048 * 3);
			is = new FileInputStream(eif.getValue());
			for (int len; (len = is.read()) != -1;)
				baos.write(len);
			baos.flush();

			// 4.2通过byte字节生成IMG对象
			image = Image.getInstance(baos.toByteArray());
			Image.getInstance(baos.toByteArray());
			image.setAbsolutePosition(0.0f, 0.0f);

			// 4.3：添加到document中
			document.add(image);
			document.newPage();
			baos.close();
		}

		// 5：释放资源
		document.close();
		pdfWr.close();
		is.close();
		return true;
	}

	/**
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 * @param x
	 *            读取源文件中的第几页
	 */
	private static void pdfToJpg(String source, String target, int x) throws Exception {
		// 创建从中读取和向其中写入（可选）的随机访问文件流，R表示对其只是访问模式
		RandomAccessFile rea = new RandomAccessFile(new File(source), "r");

		// 将流读取到内存中，然后还映射一个PDF对象
		FileChannel channel = rea.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdfFile = new PDFFile(buf);
		PDFPage page = pdfFile.getPage(x);

		// get the width and height for the doc at the default zoom
		java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, (int) page.getBBox().getWidth(),
				(int) page.getBBox().getHeight());

		// generate the image
		java.awt.Image img = page.getImage(rect.width, rect.height, // width &
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true // block until drawing is done
		);

		BufferedImage tag = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0, rect.width, rect.height, null);

		ImageIO.write(tag, "JPEG", new File(target));
		img.flush();
		rea.close();
	}

	/**
	 * @param source
	 *            源PDF文件路径
	 * @param target
	 *            保存PDF文件路径
	 * @param pageNum
	 *            提取PDF中第pageNum页
	 * @throws Exception
	 */
	private static void pdfExtraction(String source, String target, int pageNum) throws Exception {
		// 1：创建PDF读取对象
		PdfReader pr = new PdfReader(source);
		System.out.println("this document " + pr.getNumberOfPages() + " page");

		// 2：将第page页转为提取，创建document对象
		Document doc = new Document(pr.getPageSize(pageNum));

		// 3：通过PdfCopy转其单独存储
		PdfCopy copy = new PdfCopy(doc, new FileOutputStream(new File(target)));
		doc.open();
		doc.newPage();

		// 4：获取第1页，装载到document中。
		PdfImportedPage page = copy.getImportedPage(pr, pageNum);
		copy.addPage(page);

		// 5：释放资源
		copy.close();
		doc.close();
		pr.close();
	}

	/**
	 * @param pdfFile
	 *            源PDF文件
	 * @param imgFile
	 *            图片文件
	 */
	private static void jpgToPdf(File pdfFile, File imgFile) throws Exception {
		// 文件转img
		InputStream is = new FileInputStream(pdfFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i; (i = is.read()) != -1;) {
			baos.write(i);
		}
		baos.flush();

		// 取得图像的宽和高。
		Image img = Image.getInstance(baos.toByteArray());
		float width = img.getWidth();
		float height = img.getHeight();
		img.setAbsolutePosition(0.0F, 0.0F);// 取消偏移
		System.out.println("width = " + width + "\theight" + height);

		// img转pdf
		Document doc = new Document(new Rectangle(width, height));
		PdfWriter pw = PdfWriter.getInstance(doc, new FileOutputStream(imgFile));
		doc.open();
		doc.add(img);

		// 释放资源
		System.out.println(doc.newPage());
		pw.flush();
		baos.close();
		doc.close();
		pw.close();
		is.close();
	}

}
