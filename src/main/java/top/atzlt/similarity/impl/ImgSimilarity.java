package top.atzlt.similarity.impl;

import java.awt.*;
import java.awt.image.Raster;
import java.io.IOException;

/**
 * 图像相似度计算
 * https://blog.csdn.net/weixin_42267525/article/details/80797112    java中各种图像相似度计算算法评价
 * https://blog.csdn.net/10km/article/details/70949272   均值哈希算法
 * https://www.sunjs.com/article/detail/24dd9a9e436e489185430c4c45034d69.html    java 图片相似度算法
 * https://baike.baidu.com/item/SIFT/1396275?fr=aladdin   SIFT算法百度百科
 * https://blog.csdn.net/abcd_d_/article/details/22288599  SIFT算法，java实现   https://download.csdn.net/download/abcd_d_/7107483  源码
 * https://blog.csdn.net/zddblog/article/details/7521424  SIFT算法详解
 * https://wenku.baidu.com/view/1b8b430e6fdb6f1aff00bed5b9f3f90f76c64d84.html   java实现的一些算法
 * https://www.cnblogs.com/wavky/p/3888903.html java实现
 * <p>
 * https://github.com/nivance/image-similarity java实现，具体例子
 * <p>
 * 判定商标近似：https://www.zhihu.com/question/24666141
 * 判定商标近似：https://wiki.mbalib.com/wiki/%E8%BF%91%E4%BC%BC%E5%95%86%E6%A0%87
 */
public class ImgSimilarity extends ImageSimCaculate{
    private int[][] scale_mx1 = new int[final_size][final_size];
    private int[][] scale_mx2 = new int[final_size][final_size];
    private int average_mx1 = 0;
    private int average_mx2 = 0;
    private int hammingDistance = 0;


    public int getAverage_mx1() {
        return average_mx1;
    }

    public int getAverage_mx2() {
        return average_mx2;
    }

    public int getHammingDistance() {
        return hammingDistance;
    }

    public ImgSimilarity(String path_dog, String path_cat) throws IOException {
            super(path_dog , path_cat);
    }

    /**
     * 定义归一化大小
     * 但是如果选择了需要对图像进一步卷积池化，则图片归一化不可取宽高太小的结果；否则是直接对缩略图进行相似度计算
     * 庆幸的是现所收集存档商标都定义统一图片规格，不需要做size初步归一化，也免去因归一化宽高比不同导致横向或纵向压缩的情形
     */

    /*get pixels to one line*/
    public int[] getPixels(){
        int[] pixels = bf_img1.getRGB(0,0,bfimg1_w,bfimg1_h,null,0,bfimg1_w);
        Raster raster = bf_img1.getData();
        int[] temp = new int[raster.getWidth()*raster.getHeight()*raster.getNumBands()];
        pixels = raster.getPixels(0,0,bfimg1_w,bfimg1_h,temp);
//        for (int i=0;i<pixels.length;i+=3) {
//            if((i%raster.getWidth()*raster.getNumBands())==0)
//                System.out.printf("%x%x%x\t",pixels[i],pixels[i+1],pixels[i+2]);
//        }
//        System.out.println("\n");
        return pixels;
    }

    /**
     * get the average of the matrix and return
     */
    public void setAvgColor() {
        Color color;
        long mx1_sumRed = 0;
        long mx2_sumRed = 0;
        for (int i = 0; i < final_size; i++) {
            for ( int j=0 ; j<final_size ; j++){
                color = new Color(scale_mx1[i][j],true);
                mx1_sumRed+= color.getRed();
                color = new Color(scale_mx2[i][j],true);
                mx2_sumRed+= color.getRed();
            }
        }
        average_mx1 = (int) (mx1_sumRed / (final_size*final_size));
        average_mx2 = (int) (mx2_sumRed / (final_size*final_size));
//        print("averageColorDog:"+average_mx1+"\taverageColorCat:"+average_mx2);
    }

    /**
     * get hamming distance between two matrix,request the sizes of these matrix is the same
     */
    public void setHammingDistance(){
        hammingDistance = 0;
        for(int i=0; i<final_size ; i++){
            for (int j=0 ; j<final_size ; j++){
                hammingDistance+= (scale_mx1[i][j]==scale_mx2[i][j])?0:1;
            }
        }
    }

    /**
     * caculate the similarity with the hamming distance between two matrix
     */
    public double calSimilarity(int hammingDistance){
        int area = final_size * final_size;
        double similarity = (area - hammingDistance)/(double)area;
//        System.out.println(similarity);
//        similarity = Math.pow(similarity,4);//4次方
        return Math.pow(similarity,4);
    }

    public void setMatrixBinary(){
//        if ()进行avg值的检查
        Color color = null;
        for(int i=0 ; i<final_size ; i++){
            for (int j=0 ; j<final_size ; j++){
                color = new Color(scale_mx1[i][j] , true);
                scale_mx1[i][j] = ( color.getRed() - average_mx1 )>0?1:0;
                color = new Color(scale_mx2[i][j] , true);
                scale_mx2[i][j] = ( color.getRed() - average_mx2 )>0?1:0;
            }
        }
    }
    public void printM(){
        for( int i = 0 ; i<final_size ; i++){
        for (int j=0 ; j<final_size ; j++) {
            System.out.printf("%x\t", scale_mx1[i][j]);
        }
        System.out.println();
    }
        print("matrix printed over ...");
    }
    /**
     *
     */
    public void initMatrix(){
        //通过getRGB()方式获得像素矩阵返回RGBA值
        for(int i=0;i<bfimg1_w;i++){
            for(int j=0;j<bfimg1_h;j++){
                scale_mx1[i][j]=bf_img1.getRGB(i,j);
            }
        }for(int i=0;i<bfimg2_w;i++){
            for(int j=0;j<bfimg2_h;j++){
                scale_mx2[i][j]=bf_img2.getRGB(i,j);
            }
        }
        return ;
    }

    public double caculateSimilarity(){
        /**
         * caculate logic realize here
         * 子类覆盖该方法，因为部分相似度计算逻辑实现在子类
         */
        //已将img归一化
        scaleImageFinalsize();
        //初始化bfimg及其宽高
        initImage();
        //bfimg灰度化
        toGrayBfimg();
        //将bfimg数据转存为该子类的int[][]
        initMatrix();
//        printM();//
        //设置图片平均灰度（注意：计算平均值前就应当将图片归一化，请检查！
        setAvgColor();
        // 获取平均灰度颜色
//        System.out.println("averageColorDog:"+getAverage_mx1()+"\taverageColorCat:"+getAverage_mx2());
        // 设置灰度像素的比较数组（即图像指纹序列）
        setMatrixBinary();
        // 计算两个图的汉明距离（图已得到灰度比较数组）
        setHammingDistance();
//        System.out.println("hammingDistance:"+getHammingDistance());

        // 通过汉明距离计算相似度，取值范围 [0.0, 1.0]
//        similarity =  calSimilarity(getHammingDistance());
//        print("Similarity: "+getSimilarity());
        return calSimilarity(getHammingDistance());
    }

}
