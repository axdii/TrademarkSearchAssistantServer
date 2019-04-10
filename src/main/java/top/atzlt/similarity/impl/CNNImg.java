package top.atzlt.similarity.impl;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: logMath
 * Description:
 * Date: 2019-03-18
 * Time: 22:10
 */
public class CNNImg extends ImageSimCaculate{
    /**
     * 使用两个矩阵记录图片灰度图，
     */
    protected int[][] init_mx1 = new int[init_size][init_size];
    protected int[][] init_mx2 = new int[init_size][init_size];

    private int[][] final_mx1 = new int[final_size][final_size];
    private int[][] final_mx2 = new int[final_size][final_size];
    private int avg1=0;
    private int avg2=0;
    private int distance = 0;
    private int hammingdistance=0;

    private CNNImg(){}

    public CNNImg(String path_dog, String path_cat) throws IOException {
        super(path_dog , path_cat);
    }

    /**
     *卷积
     */
    private  final int convolution_templeate = 5;

    private int[][] convolution(int[][] matrix ,int width , int height){
        //如果模板为5*5，则需要为矩阵增加两层外围才能够使得卷积后矩阵大小维持不变
        int sum = 0;
        double avg = 0;
        int[][] extend_mx = new int[width+4][height+4];
        int[][] result_mx = new int[width][height];

        for (int i=0 ; i<width; i++){
            for (int j = 0 ; j<height; j++){
                extend_mx[i+2][j+2] = matrix[i][j];
            }
        }
        for( int i=0 ; i<width ; i++){
            extend_mx[i][0] = extend_mx[i][2];
            extend_mx[i][1] = extend_mx[i][2];
            extend_mx[i][width] = extend_mx[i][width-1];
            extend_mx[i][width+1] = extend_mx[i][width-1];
        }
        for( int j=0 ; j<height+4 ; j++){
            extend_mx[0][j] = extend_mx[2][j];
            extend_mx[1][j] = extend_mx[2][j];
            extend_mx[height][j] = extend_mx[height-1][j];
            extend_mx[height+1][j] = extend_mx[height-1][j];
        }

        width+=4;height+=4;
        for (int i=0 ; i<convolution_templeate ; i++){
            for (int j = 0 ; j<convolution_templeate ; j++){
                sum+=extend_mx[i][j];
            }
        }
//        print("sum:"+sum);

        int linestart = sum, moveblock =sum;
        for(int i = 2 ; i < width-2 ; i++ ){
            for(int j = 2 ; j < height-2 ; j++ ){
                avg =  moveblock/25.0;
                result_mx[i-2][j-2] = (int)avg;
                if (j<height-3){
                    moveblock = moveblock
                            + ( extend_mx[i-2][j+3] +  extend_mx[i-1][j+3] +  extend_mx[i][j+3] +  extend_mx[i+1][j+3] +  extend_mx[i+2][j+3])
                            -( extend_mx[i-2][j-2] +  extend_mx[i-1][j-2] +  extend_mx[i][j-2] +  extend_mx[i+1][j-2] +  extend_mx[i+2][j-2] );
                }
            }
            if (i<width-3){
                linestart = linestart
                        + ( extend_mx[i+3][0] +  extend_mx[i+3][1] +  extend_mx[i+3][2] +  extend_mx[i+3][3] +  extend_mx[i+3][4])
                        -( extend_mx[i-2][0] +  extend_mx[i-2][1] +  extend_mx[i-2][2] +  extend_mx[i-2][3] +  extend_mx[i-2][4] );
                moveblock = linestart ;
            }
        }
//        printMatrix(result_mx,width-4,height-4);
        return result_mx;
    }

    /**
     *池化
     */
    private final int pool_templeate = 2;
    private int[][] pooling(int[][] matrix ,int width , int height){
        int[][] even_mx = evenMatrix(matrix , width , height);
        int result_w = (width+1)/2;
        int result_h = (height+1)/2;
        int[][] result = new int[result_w][result_h];
        for (int i=0 ; i<result_w ; i++){
            for (int j=0 ; j<result_h ; j++){
                result[i][j] = ( even_mx[2*i][2*j] + even_mx[2*i+1][2*j] + even_mx[2*i][2*j+1] +even_mx[2*i+1][2*j+1]) /4;
            }
        }
        return result;
    }

    /**
     * 如果matri2矩阵宽高有奇数的，则复制临界行/列，追加至边界
     * @param res_mx       输入矩阵2
     * @param res_h           init_mx2宽
     * @param res_w           init_mx2高
     * @return
     */
    private int[][] evenMatrix( int[][] res_mx ,int res_w , int res_h){
        boolean hori = res_w%2==1;
        boolean vert = res_h%2==1;
        int result_w = res_w + (hori?1:0);
        int result_h = res_h + (vert?1:0);
        int[][] result_mx = new int[res_w][res_h];
        for(int i=0 ; i<res_w; i++){
            for(int j=0 ; j<res_h ; j++){
                result_mx[i][j] = res_mx[i][j];
            }
        }
        int flag=0;
        if (hori){
            for (int i=0; i<res_h; i++){
                result_mx[i][result_w-1] = result_mx[i][result_w-2];
            }
            flag=1;
        }
        if (vert){
            for (int i=0; i<res_w+flag; i++){
                result_mx[result_h-1][i] = result_mx[result_h-2][i];
            }
        }
        return result_mx;
    }

    /**
     *归一化处理
     */
    public void normalize(int[][] matrix ,int width , int height){
        return  ;
    }

    /**
     * 初始化矩阵，按照要求，bfimg1与bfimg2宽高应当一致，以下两个for嵌套可合并
     */
    public void initMatrix(){
        Color color = null;
        //通过getRGB()方式获得像素矩阵返回RGBA值
        for(int i=0;i<bfimg1_w;i++){
            for(int j=0;j<bfimg1_h;j++){
                init_mx1[i][j]=bf_img1.getRGB(i,j);
                color = new Color(init_mx1[i][j] , true);
                init_mx1[i][j] = color.getRed();
            }
        }for(int i=0;i<bfimg2_w;i++){
            for(int j=0;j<bfimg2_h;j++){
                init_mx2[i][j]=bf_img2.getRGB(i,j);
                color = new Color(init_mx2[i][j] , true);
                init_mx2[i][j] = color.getRed();
            }
        }
        return ;
    }

    /**
     * 打印归一化后矩阵1结果
     */
    public void printM(){
        for( int i = 0 ; i<init_size ; i++){
            for (int j=0 ; j<init_size ; j++) {
                System.out.printf("%x\t", init_mx1[i][j]);
            }
            System.out.println();
        }
        print("matrix printed over ...");
    }

    /**
     * 运行前提：img1，img2已被初始化
     * @return
     */
    public double caculateSimilarity(){
        //已将img归一化（100*100
        scaleImageInitSize();
        //初始化bfimg及其宽高
        initImage();
        //bfimg灰度化
        toGrayBfimg();
        //将bfimg数据转存为该子类的int[][]
        initMatrix();
        //bfimg灰度化
//        printM();//
        int[][] fir_cnn_out = convolution(init_mx1 , init_size , init_size);
        int[][] fir_spooling_out = pooling(fir_cnn_out , init_size , init_size);
        int[][] sec_cnn_out = convolution(fir_spooling_out , init_size/2 , init_size/2);
        int[][] sec_spooling_out = pooling(sec_cnn_out , init_size/2 , init_size/2);
        final_mx1 = sec_spooling_out;
//        printMatrix(sec_spooling_out , init_size/4 , init_size/4);

        fir_cnn_out = convolution(init_mx2 , init_size , init_size);
        fir_spooling_out = pooling(fir_cnn_out , init_size , init_size);
        sec_cnn_out = convolution(fir_spooling_out , init_size/2 , init_size/2);
        sec_spooling_out = pooling(sec_cnn_out , init_size/2 , init_size/2);
        final_mx2 = sec_spooling_out;
//        printMatrix(sec_spooling_out , init_size/4 , init_size/4);
        setDistance();
//        calSimilarity();
//        print("Similarity: "+getSimilarity());
        return calSimilarity();
    }

    /**
     * 池化卷积之后得到25*25矩阵，还是使用ImgSimilarity部分方法进行相似度计算
     * 其实也就是在ImgSimilarity中resize过程穿插卷积池化算法
     */


    public void setAvgColor() {
        long mx1_sumRed = 0;
        long mx2_sumRed = 0;
        for (int i = 0; i < final_size; i++) {
            for ( int j=0 ; j<final_size ; j++){
                mx1_sumRed += final_mx1[i][j];
                mx2_sumRed += final_mx2[i][j];
            }
        }
        avg1 = (int) (mx1_sumRed / (final_size*final_size));
        avg2 = (int) (mx2_sumRed / (final_size*final_size));
//        print("avg1:"+avg1+"\t\ttavg2:"+avg2);
    }

    public void setDistance(){
        distance = 0;
        for(int i=0; i<final_size ; i++){
            for (int j=0 ; j<final_size ; j++){
                distance += Math.abs(final_mx1[i][j]-final_mx2[i][j]);
            }
        }
//        print("Distance :\t"+distance);
    }


    public double calSimilarity(){
        int area = final_size * final_size;
        similarity = (area - distance/255)/(double)area;
        System.out.println(similarity);
        similarity = Math.pow(similarity,10);//4次方
        return similarity;
    }
    public void setMatrixBinary(){
//        if ()进行avg值的检查
        Color color = null;
        for(int i=0 ; i<final_size ; i++){
            for (int j=0 ; j<final_size ; j++){
                final_mx1[i][j] = ( final_mx1[i][j] - avg1 )>0?1:0;
                final_mx2[i][j] = ( final_mx2[i][j] - avg2 )>0?1:0;
            }
        }
    }

//    方法2：同ImgSimilarity，先二值化后再进行汉明距离计算
    public double getBinarySimilarity(){
        setAvgColor();
        setMatrixBinary();
        setHammingDistance();
        int area = final_size * final_size;
        similarity = (area - hammingdistance)/(double)area;
        similarity = Math.pow(similarity,5);
//        System.out.println(similarity);
        return similarity;
    }

    public void setHammingDistance(){
        hammingdistance = 0;
        for(int i=0; i<final_size ; i++){
            for (int j=0 ; j<final_size ; j++){
                hammingdistance += (final_mx1[i][j]==final_mx2[i][j])?0:1;
            }
        }
//        print("hammingdistance :\t"+hammingdistance);
    }

    /**
     * 256*256归一化结果
     */
}


