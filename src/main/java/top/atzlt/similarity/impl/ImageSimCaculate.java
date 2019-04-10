package top.atzlt.similarity.impl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * Author: logMath
 * Description:
 * Date: 2019-03-20
 * Time: 19:05
 */
public class ImageSimCaculate {
//    public static ImageSimCaculate isc;

    protected Image img1 = null, img2 = null;
    protected BufferedImage bf_img1 = null, bf_img2 = null;
    /**
     * 图片1的宽高
     */
    protected int bfimg1_w = 0;
    protected int bfimg1_h = 0;
    /**
     * 图片2的宽高
     */
    protected int bfimg2_w = 0 ;
    protected int bfimg2_h = 0 ;

    protected double similarity = 0 ;  //相似度

    protected final int init_size = 100 ;      //归一化（预处理）大小
    protected final int final_size = 25 ;      //最终大小

    public int getInit_size() {
        return init_size;
    }

    public int getFinal_size() {
        return final_size;
    }


    /**
     * 4个初始化方法，建议使用最后一种
     */
    public ImageSimCaculate(){print("请使用其他带参构造方法");}
    public ImageSimCaculate( Image img1 , Image img2){
        this.img1 = img1 ;
        this.img2 = img2 ;
    }
    ImageSimCaculate(String path1 , String path2) throws IOException {
        /**
         * 此处使用路径对图片进行初始化
         */
        img1 = ImageIO.read(new FileInputStream(path1));
        img2 = ImageIO.read(new FileInputStream(path2));
    }

    ///////////////////////////////////////////////
    /**
     * print the matrix value to the console window , not only for single size
     */
    protected void printMatrix(int[][] matrix, int sizew , int sizeh){
        print("sizew:\t"+sizew+"& szieh:\t"+sizeh);
        for( int i = 0 ; i<sizew ; i++){
            for (int j=0 ; j<sizeh ; j++) {
                System.out.printf("%x\t", matrix[i][j]);
            }
            System.out.println();
        }
        print("matrix printed over ...");
    }

    /**
     *
     */
    protected void toGrayBfimg() {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);//init a new option
        bf_img1 = op.filter(bf_img1, null);
        bf_img2 = op.filter(bf_img2, null);
        return ;
    }

    protected void initImage() {
        if (img1==null || img2==null){
//            print("img1/img2 为空，检查一下代码唔该");
            return;
        }
        bf_img1 = new BufferedImage(img1.getWidth(null), img1.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D sdg1 = bf_img1.createGraphics();
        sdg1.drawImage(img1, null, null);
        sdg1.dispose();
        bf_img2 = new BufferedImage(img2.getWidth(null), img2.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D sdg2 = bf_img2.createGraphics();
        sdg2.drawImage(img2, null, null);
        sdg2.dispose();

        initBufferImage();
    }


    protected void initBufferImage(){
        if(bf_img1==null || bf_img2==null){
//            print("bf_img1/bf_img2 is null , please check your code...");
            return ;
        }
        bfimg1_w = bf_img1.getWidth();
        bfimg1_h = bf_img1.getHeight();
        bfimg2_w = bf_img2.getWidth();
        bfimg2_h = bf_img2.getHeight();
//        print("bf_img1 width & height : "+bfimg1_w+bfimg1_h+"\t\tbf_img2 width & height : "+bfimg2_w+bfimg2_h);
    }

    /**
     * 二值化处理
     *  averageColor：图片平均灰度
     */
    protected int[][] getBinary(int[][] pi , int wpi , int hpi,int averageColor){
        Color color = null;
        int[][] bin = new int[wpi][hpi];
        for(int i=0 ; i<wpi ; i++){
            for (int j=0 ; j<hpi ; j++){
                color = new Color(pi[i][j] , true);
                bin[i][j] = color.getRed() - averageColor>0?1:0;
            }
        }
        return bin;
    }

    /**
     *归一化处理
     */
    public void normalize(){
        return  ;
    }
    protected void scaleImageInitSize() {
        //Image.SCALE_SMOOTH is a way that resize an image ,smaller
        img1 = img1.getScaledInstance(init_size, init_size, Image.SCALE_SMOOTH);
        img2 = img2.getScaledInstance(init_size, init_size, Image.SCALE_SMOOTH);
    }

    protected void scaleImageFinalsize() {
        //Image.SCALE_SMOOTH is a way that resize an image ,smaller
        img1 = img1.getScaledInstance(final_size, final_size, Image.SCALE_SMOOTH);
        img2 = img2.getScaledInstance(final_size, final_size, Image.SCALE_SMOOTH);
    }
    /////////////////////////////////////////
    protected void print(String mes){
        System.out.println("Attention: "+mes);
    }
    public double getSimilarity() {
        return similarity;
    }
    /**
     * 检查是否路径出现错误(这要怎么检查呢)
     */
    public boolean checkupPath(String path1 , String path2){
        File file1 = new File(path1);
        if (file1==null)
            print("路径1出错，非有效地址，请检查！");
//        这样检查会不会多余了
        return false;
    }
    public boolean checkupImg(){
        if (img1==null || img2==null)
            return false;
        return true;
    }
    public boolean checkupBfimg(){
        if (bf_img1==null || bf_img2==null)
            return false;
        return true;
    }
}
