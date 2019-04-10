package top.atzlt.similarity.impl;

import org.junit.Test;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Test_test {
    @Test
    public  void test() throws IOException {
        String rootPath = Test_test.class.getResource("/").getPath();
        String path_dog = rootPath + "tb43.JPG";
        String path_cat = rootPath + "tb81.JPG";
        ImgSimilarity imgs = new ImgSimilarity(path_dog , path_cat);
        System.out.println(imgs.caculateSimilarity());
    }

    @Test
    public void test2() throws IOException {
        String rootPath = Test_test.class.getResource("/").getPath();
        String path_dog = rootPath + "tb43.JPG";
        String path_cat = rootPath + "tb81.JPG";
        System.out.println(path_dog);
        CNNImg cnnImg = new CNNImg(path_dog , path_cat);
        double i = cnnImg.caculateSimilarity();
        System.out.println(i);
    }
    @Test
    public void test3() throws IOException {
        String rootPath = Test_test.class.getResource("/").getPath();
        String path_dog = rootPath + "tb43.JPG";
        String path_cat = rootPath + "tb87.JPG";
        CNNImg cnnImg = new CNNImg(path_dog , path_cat);
        System.out.println(cnnImg.getBinarySimilarity());
    }


    @Test
    public void test4(){
        TextSimilarity textSimilarity = new TextSimilarity();
        double sim = textSimilarity.calSimilarityValues("格力","得力");
        System.out.println("综合方法计算得到相似度：\t" + sim);
    }
}
