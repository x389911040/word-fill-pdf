package com.rd.modules.common.convert.utils;


import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

public class FileUtil {

    /**
     *  随机生成不重复的随机数 时间戳加四位随机数
     * @return
     */
    public static String randomFileName() {
        return System.currentTimeMillis()+""+(new Random().nextInt(9999)+1000)+"";
    }

    /**
     *  随机生成不重复的随机数 时间戳加四位随机数带文件后缀
     * @return
     */
    public static String randomFileName(String suffix) {
        return System.currentTimeMillis()+""+(new Random().nextInt(9999)+1000)+"."+suffix;
    }



    /**
     * 判断目录是否存在 不存在创建
     * @param path
     */
    public static void isExists(String path) {
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 文件删除
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件名称 带后缀
     * @param path
     */
    public static String getFileName(String path) {

        File file =new File( path.trim());

        return file.getName();
    }


    /**
     *获取不带后缀名的文件名
     * @param path
     * @return
     */
    public static String getFileNameWithoutSuffix(String path){
        File file = new File(path);
        String file_name = file.getName();
        return file_name.substring(0, file_name.lastIndexOf("."));
    }

    /**
     * 获取文件后缀 不带点
     * @return
     */
    public static String getFileSuffix(String path) {
        File file = new File(path);
        String fileName=file.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    /**
     * 获取文件后缀 带点
     * @return
     */
    public static String getFileSuffixWithDone(String path) {
        File file = new File(path);
        String fileName=file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取文件的大小
     * @param path
     * @return
     */
    public static Long getFileSize(String path) {
        File file = new File(path);
        return file.length();
    }

    /**
     * 文件复制
     * @param sourcePath 源文件路径
     * @param expectPath 目标路径
     */
    public static  void fileChannelCopy(String sourcePath,String expectPath){
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(new File(sourcePath));
            fo = new FileOutputStream( new File(expectPath));
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
			/*
			 * 		 public abstract long transferTo(long position, long count,
             		                     WritableByteChannel target)throws IOException;
			 * 			position - 文件中的位置，从此位置开始传输；必须为非负数
			 * 			count - 要传输的最大字节数；必须为非负数
			 * 			target - 目标通道
			 *			返回：
						实际已传输的字节数，可能为零
			 */
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道中
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }finally{
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param bos
     *            输出文件的位置
     * @param input
     *            原PDF位置
     * @param waterMarkName
     *            页脚添加水印
     * @param permission
     *            权限码
     * @throws DocumentException
     * @throws IOException
     */
    public static void setWatermark(BufferedOutputStream bos, String input, String waterMarkName, int permission)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
//        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        BaseFont base = BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
            // content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.2f);
            // content.setGState(gs);
            content.beginText();
            content.setColorFill(Color.LIGHT_GRAY);
            content.setFontAndSize(base, 50);
            content.setTextMatrix(70, 200);
            content.showTextAligned(Element.ALIGN_CENTER, "公司内部文件，请注意保密！", 300, 350, 55);
//            Image image = Image.getInstance("G:/2.jpeg");
            /*
              img.setAlignment(Image.LEFT | Image.TEXTWRAP);
              img.setBorder(Image.BOX); img.setBorderWidth(10);
              img.setBorderColor(BaseColor.WHITE); img.scaleToFit(100072);//大小
              img.setRotationDegrees(-30);//旋转
             */
//            image.setAbsolutePosition(200, 206); // set the first background
            // image of the absolute
//            image.scaleToFit(200, 200);
//            content.addImage(image);
            content.setColorFill(Color.BLACK);
            content.setFontAndSize(base, 8);
//            content.showTextAligned(Element.ALIGN_CENTER, "下载时间：" + waterMarkName + "", 300, 10, 0);
            content.endText();

        }
        stamper.close();
    }
}
