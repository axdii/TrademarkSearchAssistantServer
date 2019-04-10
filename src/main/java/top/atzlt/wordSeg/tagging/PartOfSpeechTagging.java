
package top.atzlt.wordSeg.tagging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.recognition.RecognitionTool;
import top.atzlt.wordSeg.recognition.Trademark;
import top.atzlt.wordSeg.segmentation.PartOfSpeech;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.GenericTrie;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.List;

/**
 * 词性标注
 */
public class PartOfSpeechTagging {

    private static final Logger logger = LoggerFactory.getLogger(PartOfSpeechTagging.class);
    private PartOfSpeechTagging(){}
    private static final GenericTrie<String> GENERIC_TRIE = new GenericTrie<>();
    static{
        reload();
    }
    public static void reload(){
        if(!WordConfTools.getBoolean("pos.tag.enable", true)){
            logger.debug("未启用词性标注");
            return;
        }
        Detector.loadAndWatch(new ResourceLoader() {

            @Override
            public void clear() {
                GENERIC_TRIE.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化词性标注器");
                int count = 0;
                for (String line : lines) {
                    try {
                        String[] attr = line.split(":");
                        GENERIC_TRIE.put(attr[0], attr[1]);
                        count++;
                    } catch (Exception e) {
                        logger.error("错误的词性数据：" , line);
                    }
                }

                /**
                 * 加载商标词典的词性
                 */
                for (String trademarkName : Trademark.getTrademarkNames()) {
                    GENERIC_TRIE.put(trademarkName, "tn");
                    count++;
                }
                logger.info("词性标注器初始化完毕，词性数据条数：" + count);
            }

            @Override
            public void add(String line) {
                try {
                    String[] attr = line.split(":");
                    GENERIC_TRIE.put(attr[0], attr[1]);
                } catch (Exception e) {
                    logger.error("错误的词性数据：" , line);
                }
            }

            @Override
            public void remove(String line) {
                try {
                    String[] attr = line.split(":");
                    GENERIC_TRIE.remove(attr[0]);
                } catch (Exception e) {
                    logger.error("错误的词性数据：" , line);
                }
            }

        }, WordConfTools.get("part.of.speech.dic.path", "classpath:part_of_speech_dic.txt"));
    }

    /**
     * 标注词性
     * @param words
     */
    public static void process(List<Word> words){
        words.forEach(word->{
            if(word.getPartOfSpeech()!=null){
                logger.debug("忽略已经标注过的词：{}" + word);
                return;
            }
            String wordText = word.getText();
            String pos = GENERIC_TRIE.get(wordText);
            if(pos == null){
                //识别英文
                if(RecognitionTool.isEnglish(wordText)){
                    pos = "w";
                }
                //识别数字
                if(RecognitionTool.isNumber(wordText)){
                    pos = "m";
                }
                //中文数字
                if(RecognitionTool.isChineseNumber(wordText)){
                    pos = "mh";
                }
                //识别小数和分数
                if(RecognitionTool.isFraction(wordText)){
                    if(wordText.contains(".")||wordText.contains("．")||wordText.contains("·")){
                        pos = "mx";
                    }
                    if(wordText.contains("/")||wordText.contains("／")){
                        pos = "mf";
                    }
                }
                //识别数量词
                if(RecognitionTool.isQuantifier(wordText)){
                    //分数
                    if(wordText.contains("‰")||wordText.contains("%")||wordText.contains("％")){
                        pos = "mf";
                    }
                    //时间量词
                    else if(wordText.contains("时")||wordText.contains("分")||wordText.contains("秒")){
                        pos = "tq";
                    }
                    //日期量词
                    else if(wordText.contains("年")||wordText.contains("月")||wordText.contains("日")
                            ||wordText.contains("天")||wordText.contains("号")){
                        pos = "tdq";
                    }
                    //数量词
                    else{
                        pos = "mq";
                    }
                }
            }
            word.setPartOfSpeech(PartOfSpeech.valueOf(pos));
        });
    }

}
