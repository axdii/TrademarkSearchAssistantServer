package top.atzlt.wordSeg.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.*;

public class ReplenishWord {

    private static final Logger logger = LoggerFactory.getLogger(ReplenishWord.class);

    private static Map<String, ReplenishWordDes> REPLENISH_WORD_DIC = new HashMap<>();

    public static class ReplenishWordDes{

        private String intent;
        private Set<String> wordSet;

        public ReplenishWordDes(String intent, Set<String> wordSet) {
            this.intent = intent;
            this.wordSet = wordSet;
        }

        public ReplenishWordDes(){}

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public Set<String> getWordSet() {
            return wordSet;
        }

        public void setWordSet(Set<String> wordSet) {
            this.wordSet = wordSet;
        }


    }

    static {
        reload();
    }

    public static void reload(){
        Detector.loadAndWatch(new ResourceLoader() {
            @Override
            public void clear() {
                REPLENISH_WORD_DIC.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化补充词汇数据...");
                int count = 0;
                for (String line : lines) {
                    try {
                        String[] attr = line.split("=");
                        ReplenishWordDes replenishWordDes = new ReplenishWordDes();
                        replenishWordDes.setIntent(attr[0]);
                        String[] words = attr[1].split(":");
                        Set<String> wordSet = new HashSet<>(Arrays.asList(words));
                        replenishWordDes.setWordSet(wordSet);
                        REPLENISH_WORD_DIC.put(replenishWordDes.intent, replenishWordDes);
                    } catch (Exception e) {
                        logger.error("错误的自定义补充说明数据：" + line);
                    }
                }
                logger.info("自定义补充说明词初始化完毕，数据条数：" + count);
            }

            @Override
            public void add(String line) {

            }

            @Override
            public void remove(String line) {

            }
        }, WordConfTools.get("replenish_word_dic.path", "classpath:replenish_word_dic.txt"));
    }

    public static Map<String, ReplenishWordDes> getReplenishWordDic() {
        return REPLENISH_WORD_DIC;
    }

    public static void setReplenishWordDic(Map<String, ReplenishWordDes> replenishWordDic) {
        REPLENISH_WORD_DIC = replenishWordDic;
    }

    public static String getReplenishWordType(Word word){
        for (Map.Entry<String, ReplenishWordDes> entry : getReplenishWordDic().entrySet()) {

            if (entry.getValue().getWordSet().contains(word.getText())) {
                return entry.getKey();
            }
        }

        return "";
    }
}
