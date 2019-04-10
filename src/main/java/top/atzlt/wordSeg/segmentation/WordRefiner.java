package top.atzlt.wordSeg.segmentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.WordSegmenter;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.GenericTrie;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 对分词结果进行微调,对词组进行重组或拆分
 */
public class WordRefiner {
    private static final Logger logger = LoggerFactory.getLogger(WordRefiner.class);

    private WordRefiner(){}

    private static final GenericTrie<String> GENERIC_TRIE = new GenericTrie<>();
    static{
        reload();
    }
    public static void reload(){
        Detector.loadAndWatch(new ResourceLoader() {

            @Override
            public void clear() {
                GENERIC_TRIE.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化WordRefiner");
                int count = 0;
                for (String line : lines) {
                    try {
                        String[] attr = line.split("=");
                        GENERIC_TRIE.put(attr[0].trim(), attr[1].trim().replaceAll("\\s+", " "));
                        count++;
                    } catch (Exception e) {
                        logger.error("错误的WordRefiner数据：" , line);
                    }
                }
                logger.info("WordRefiner初始化完毕，数据条数：" + count);
            }

            @Override
            public void add(String line) {
                try {
                    String[] attr = line.split("=");
                    GENERIC_TRIE.put(attr[0].trim(), attr[1].trim().replaceAll("\\s+", " "));
                } catch (Exception e) {
                    logger.error("错误的WordRefiner数据：" + line, e);
                }
            }

            @Override
            public void remove(String line) {
                try {
                    String[] attr = line.split("=");
                    GENERIC_TRIE.remove(attr[0].trim());
                } catch (Exception e) {
                    logger.error("错误的WordRefiner数据：" + line, e);
                }
            }

        }, WordConfTools.get("word.refine.path", "classpath:word_refine.txt"));
    }
    /**
     * 将一个词拆分成几个，返回null表示不能拆分
     * @param word
     * @return
     */
    public static List<Word> split(Word word){
        String value = GENERIC_TRIE.get(word.getText());
        if(value==null){
            return null;
        }
        List<Word> words = new ArrayList<>();
        for(String val : value.split("\\s+")){
            words.add(new Word(val));
        }
        if(words.isEmpty()){
            return null;
        }
        return words;
    }

    /**
     * 将多个词合并成一个，返回null表示不能合并
     * @param words
     * @return
     */
    public static Word combine(List<Word> words){
        if(words==null || words.size() < 2){
            return null;
        }
        String key = "";
        for(Word word : words){
            key += word.getText();
            key += " ";
        }
        key=key.trim();
        String value = GENERIC_TRIE.get(key);
        if(value==null){
            return null;
        }
        return new Word(value);
    }

    /**
     * 先拆词，再组词
     * @param words
     * @return
     */
    public static List<Word> refine(List<Word> words){
//        logger.info("对分词结果进行refine之前：{}" , words);

        List<Word> result = new ArrayList<>(words.size());
        //一：拆词
        for(Word word : words){
            List<Word> splitWords = WordRefiner.split(word);
            if(splitWords==null){
                result.add(word);
            }else{
//                logger.info("词： " + word.getText() + " 被拆分为：" + splitWords);

                result.addAll(splitWords);
            }
        }
//        logger.info("对分词结果进行refine阶段的拆词之后：{}" , result);

        //二：组词
        if(result.size()<2){
            return result;
        }
        int combineMaxLength = WordConfTools.getInt("word.refine.combine.max.length", 3);
        if(combineMaxLength < 2){
            combineMaxLength = 2;
        }
        List<Word> finalResult = new ArrayList<>(result.size());
        for(int i=0; i<result.size(); i++){
            List<Word> toCombineWords = null;
            Word combinedWord = null;
            for(int j=2; j<=combineMaxLength; j++){
                int to = i+j;
                if(to > result.size()){
                    to = result.size();
                }
                toCombineWords = result.subList(i, to);
                combinedWord = WordRefiner.combine(toCombineWords);
                if(combinedWord != null){
                    i += j;
                    i--;
                    break;
                }
            }
            if(combinedWord == null){
                finalResult.add(result.get(i));
            }else{
//                logger.info("词： " + toCombineWords + " 被合并为：" + combinedWord);

                finalResult.add(combinedWord);
            }
        }
//        logger.info("对分词结果进行refine阶段的组词之后：{}" + finalResult);

        return finalResult;
    }

    public static void main(String[] args) {
        List<Word> words = WordSegmenter.segWithStopWords("我国工人阶级和广大劳动群众要更加紧密地团结在党中央周围");
        logger.info(words.toString());
        words = WordSegmenter.segWithStopWords("在实现“两个一百年”奋斗目标的伟大征程上再创新的业绩");
        logger.info(words.toString());
    }
}
