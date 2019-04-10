
package top.atzlt.wordSeg;

import org.apache.poi.sl.draw.BitmapImageRenderer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.cons.CommonConstant;
import top.atzlt.web.DirManager;
import top.atzlt.wordSeg.recognition.RecognitionTool;
import top.atzlt.wordSeg.recognition.StopWord;
import top.atzlt.wordSeg.segmentation.SegAlgorithm;
import top.atzlt.wordSeg.segmentation.SegmentationFactory;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.tagging.PartOfSpeechTagging;

import java.util.*;

/**
 * 中文分词基础入口
 * 默认使用逆向最大匹配算法
 * 也可指定其他分词算法
 * List<Word>是分词结果
 */
public class WordSegmenter {
    private static final Logger logger = LoggerFactory.getLogger(WordSegmenter.class);
    /**
     * 对文本进行分词，保留停用词
     * 可指定其他分词算法
     */
    public static List<Word> segWithStopWords(String text, SegAlgorithm segmentationAlgorithm){
        return SegmentationFactory.getSegmentation(segmentationAlgorithm).seg(text);
    }
    /**
     * 对文本进行分词，保留停用词
     * 使用逆向最大匹配算法
     */
    public static List<Word> segWithStopWords(String text){
        return SegmentationFactory.getSegmentation(SegAlgorithm.ReverseMaximumMatching).seg(text);
    }
    /**
     * 对文本进行分词，移除停用词
     * 可指定其他分词算法
     */
    public static List<Word> seg(String text, SegAlgorithm segmentationAlgorithm){
        List<Word> words = SegmentationFactory.getSegmentation(segmentationAlgorithm).seg(text);
        //停用词过滤
        StopWord.filterStopWords(words);
        return words;
    }
    /**
     * 对文本进行分词，移除停用词
     * 使用双向最大匹配算法
     * @param text 文本
     * @return 分词结果
     */
    public static List<Word> seg(String text){
        List<Word> words = SegmentationFactory.getSegmentation(SegAlgorithm.ReverseMaximumMatching).seg(text);
        //停用词过滤
        StopWord.filterStopWords(words);
        return words;
    }


    //测试用的demo
    public static void demo(){
        long start = System.currentTimeMillis();
        List<String> sentences = new ArrayList<>();

        sentences.add("这是一段用于测试分词性能的文本");
        sentences.add("帮我查找华为游戏商标");
        sentences.add("打开商标申请文件页面");

        int i=1;
        for(String sentence : sentences){
            List<Word> words = segWithStopWords(sentence);
            PartOfSpeechTagging.process(words);
            logger.info((i++)+"、切分句子: "+sentence);
            logger.info("    切分结果："+words);
        }
        long cost = System.currentTimeMillis() - start;
        logger.info("耗时: "+cost+" 毫秒");
    }

    public static List<String> getTestData(){
        List<String> sentences = new ArrayList<>();

        sentences.add("帮我查找格力的近似商标");
        sentences.add("查找华为游戏的申请人名称");
//         sentences.add("沿海南方向逃跑");
         sentences.add("他说的确实在理");
//         sentences.add("谢霆锋和张柏芝离婚了");
//         sentences.add("结婚的和尚未结婚的");
//         sentences.add("提高产品质量");
//         sentences.add("中外科学名著");
//         sentences.add("北京大学生前来应聘");
//         sentences.add("为人民服务");
         sentences.add("独立自主和平等互利的原则");
         sentences.add("为人民办公益");
         sentences.add("这事的确定不下来");
         sentences.add("这扇门把手");
         sentences.add("把手抬起来");
         sentences.add("学生会宣传部");
         sentences.add("学生会主动完成作业");
         sentences.add("学生会游戏");
         sentences.add("研究生活水平");
         sentences.add("中国有企业");
         sentences.add("我爱美国手球");
         sentences.add("中国喜欢狗");
         sentences.add("中国热爱狗");
         sentences.add("王军虎去广州了");
         sentences.add("王军虎头虎脑的");
         sentences.add("将军任命了一名中将");
         sentences.add("产量三年中将增长两倍");

        sentences.add("帮我查找华为游戏商标");
        sentences.add("打开商标申请文件页面");
        sentences.add("华南师范大学南海校区");
        sentences.add("世界很和平");
        sentences.add("这一段是一段继续用于测试分词的文本");
        sentences.add("这个分词器是模仿杨尚春大佬写的word分词器");
        sentences.add("帮我查找抖音商标");
        sentences.add("共产党是为人民服务的");
        sentences.add("你喜不喜欢看《平凡的世界》");
        sentences.add("我喜欢看啊");
        sentences.add("那你又喜不喜欢打羽毛球");
        sentences.add("嗯嗯，你觉得呢");
        sentences.add("我猜想你是个运动达人");
        sentences.add("这样都被你知道了，哈哈");
        sentences.add("这是一段用于测试分词性能的文本");


//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
//        sentences.addAll(sentences);
        return sentences;
    }

    public static void demo2(){

        List<String> testData = getTestData();
        long start = System.currentTimeMillis();
        int i=1;
        for(String sentence : testData){
            List<Word> words = segWithStopWords(sentence);
            PartOfSpeechTagging.process(words);
            logger.info((i++)+"、切分句子: "+sentence);
            logger.info("    切分结果："+words);
        }
        long cost = System.currentTimeMillis() - start;
        logger.info("耗时: "+cost+" 毫秒");
    }


    @Test
    public void test(){
        demo();
        demo2();
        System.exit(0);
    }
}