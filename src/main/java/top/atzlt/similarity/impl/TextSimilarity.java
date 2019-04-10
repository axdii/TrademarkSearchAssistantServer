package top.atzlt.similarity.impl;

import org.junit.Test;

import java.util.*;

public class TextSimilarity {
    private String word1 = "";
    private String word2 = "";
    private double[] sim = new double[4];

    private static TextSimilarity textSimilarity;

    private   HashMap<Integer, Character> pool = new HashMap<Integer, Character>();//出现重复的,插入前需要做判定

    //    private static HashSet<Character> pool = new HashSet<Character>();
    public TextSimilarity() {
    }


    /**
     * 使用余弦相似度计算方法计算文本的相似度
     *
     * @param obj1
     * @param obj2
     * @return 相似度
     */

    public double calSimilarityValues(String obj1, String obj2) {
        pool.clear();
        word1 = obj1;
        word2 = obj2;
        getCosPoint(word1 , word2);
        getCosPointAccurater(word1 , word2);
        getMutualSim(word1 , word2);
        double result =0.05*sim[0] +  0.7*sim[1] + 0.2*sim[2] + 0.05*sim[3];
        return result;
    }

    public static double staticCalSimilarityValues(String str1, String str2) {

        if (textSimilarity == null) {
            textSimilarity = new TextSimilarity();
        }
        return textSimilarity.calSimilarityValues(str1, str2);

    }



    /**
     * 字形Glyph，此方法计算字形相近度
     *
     * @param text1
     * @param text2
     */
    public void getGlyphPoint(String text1, String text2) {

    }

    /**
     * 字义或词义近似度
     *
     * @param text1
     * @param text2
     */
    public void getMeaningPoint(String text1, String text2) {

    }

    /**
     * 字词读音近似度
     *
     * @param text1
     * @param text2
     */
    public void getSoundPoint(String text1, String text2) {

    }

    /**
     * 单计算两个字符串，其字的相同率
     *
     * @param text1
     * @param text2
     */
    public void getWordPoint(String text1, String text2) {

    }

    /**
     * 参考余弦相似度计算方法计算文本的相似度方法，但此方法非常不适用，不过有改进的空间
     *
     * @param text1
     * @param text2
     */
    public void getCosPoint(String text1, String text2) {
        String combine = text1 + text2;
        combine.trim();
        int length = combine.length();
        int skip = 0;
        for (int i = 0; i < length; i++) {
            Character character = combine.charAt(i + skip);
            if (pool.containsValue(character)) {
                i--;
                length--;
                skip++;
                continue;
            }
            pool.put(i, character);
        }
        System.out.println(pool);
        /*
        没有contains判定：
        {0=调, 1=调, 2=阿, 3=德, 4=覅, 5=放, 6=你, 7=阿, 8=克, 9=划, 10=分, 11=的}
        加上contains判定：
        {0=调, 2=阿, 3=德, 4=覅, 5=放, 6=你, 8=克, 9=划, 10=分, 11=的}
        */
        int length1 = text1.length();
        int length2 = text2.length();

        int inner_product = 0;
        int extent1 = 0;
        int extent2 = 0;

        int size = pool.size();
        int max_length = length1 > length2 ? length1 : length2;
        for (int i = 0; i < max_length; i++) {
            Character character1 = null;
            Character character2 = null;

            int singleorder1 = 0;
            int singleorder2 = 0;

            if (i < length1) {
                character1 = text1.charAt(i);
            }
            if (i < length2) {
                character2 = text2.charAt(i);
            }
            for (int j = 0; j < size; j++) {
                Character charati = pool.get(j);
                if (charati.equals(character1) && character1 != null) {
                    singleorder1 = j;
                }
                if (charati.equals(character2) && character2 != null) {
                    singleorder2 = j;
                }
            }
            extent1 += singleorder1 * singleorder1;
            extent2 += singleorder2 * singleorder2;
            inner_product += singleorder1 * singleorder2;
        }

        double denominator = Math.sqrt((double) extent1 * extent2);
        double molecule = inner_product;
        double similarity = molecule / denominator;
        similarity = Math.pow(similarity,2);//
        System.out.println("两个字符串的相似度为（余弦方法1）： " + similarity);
        sim[0] = similarity;
    }
    /**
     *余弦相似度计算方法计算文本的相似度，但方法上有所小修改
     * @param text1
     * @param text2
     */
    public void getCosPointAccurater(String text1, String text2) {
        /**
         * 使用map记录单个字
         */
        text1.trim();
        text2.trim();
        String combine = text1 + text2;
        int length1 = text1.length();
        int length2 = text2.length();
        int clength = combine.length();
        HashMap<Integer, Character> map1 = new HashMap<Integer, Character>();
        HashMap<Integer, Character> map2 = new HashMap<Integer, Character>();
        for (int i = 0; i < length1; i++) {
            map1.put(i, text1.charAt(i));
        }
        for (int i = 0; i < length2; i++) {
            map2.put(i, text2.charAt(i));
        }
        System.out.println(map1);

        int inner_product = 0;
        int extent1 = 0;
        int extent2 = 0;

        for (int i = 0; i < clength; i++) {
            Character character = combine.charAt(i);
            int a = 0, b = 0;
            if (map1.containsValue(character)) {
                extent1 += 1;
                a = 1;
            }
            if (map2.containsValue(character)) {
                extent2 += 1;
                b = 1;
            }
            inner_product += a * b;
        }
        double denominator = Math.sqrt((double) extent1 * extent2);
        double molecule = inner_product;
        double similarity = molecule / denominator;
        similarity = Math.pow(similarity,2);//4次方
        System.out.println("两个字符串的相似度为（基于余弦相似度算法2）： " + similarity);
        sim[1] = similarity;
    }

    /**
     * 此方法找出两个词的共同字个数，然后与词条的长度作比较得出相似度
     * @param text1
     * @param text2
     */
    public void getMutualSim(String text1, String text2){
        int mutual = 0;
        int length1 = text1.length() ,length2 = text2.length();
        HashMap<Integer , Character> map1 = new HashMap();
        int skip=0;
        for (int i = 0; i < length1; i++) {
            Character character = text1.charAt(i);
            map1.put(i, character);
        }
        char[] chars = text2.toCharArray();
        for(char ch:chars){
            if (map1.containsValue(ch)){
                mutual++;
            }
        }
        double similarity = (double)(mutual*mutual)/(length1*length2);
        double similarity2 = (double)(2*mutual)/(length1+length2);
        similarity = Math.pow(similarity,2);//
        similarity2 = Math.pow(similarity2,2);//
        System.out.println("基于共同字数计算的相似度 ： "+similarity+"   或计算方法2 ： "+similarity2);
        sim[2] = similarity;
        sim[3] = similarity2;
    }

    /**
     * 通过记录两个词的共同字，根据这几个共同字的顺序与距离计算相似度
     * @param text1
     * @param text2
     */
    public void getMutualOrderDistanceSim(String text1, String text2){

    }

    @Test
    public void test(){
        double i = staticCalSimilarityValues("格力", "必胜客");
        double ii = staticCalSimilarityValues("格力", "肯德基");
        System.out.println(i);
        System.out.println(ii);
    }


}
