package top.atzlt.wordSeg.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.*;

/**
 * 判断一个词是否是命令词
 */
public class CommandWord {

    private static final Logger logger = LoggerFactory.getLogger(CommandWord.class);

    private static Map<String, CommandWordDes> COMMAND_WORD_DES = new HashMap<>();
    private static Map<String, CommandWordDes> COMMAND_WORD_DIC = new HashMap<>();

    private static class CommandWordDes{
        private String intent;
        private String des;

        public CommandWordDes(String intent, String des) {
            this.intent = intent;
            this.des=des;
        }

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }

    static {
        reload();
    }

    public static void reload() {
        loadInNetWork();
        loadInLocal();
    }

    private static void loadInNetWork(){
        logger.info("从网络中加载命令词..");
    }

    private static void loadInLocal(){
        logger.info("从本地中加载命令词..");

        //加载command_word_des.txt
        Detector.loadAndWatch(new ResourceLoader() {
            @Override
            public void clear() {
                COMMAND_WORD_DES.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化自定义命令词说明");
                int count = 0;
                for (String line : lines) {
                    try {
                        String[] attr = line.split("=");
                        COMMAND_WORD_DES.put(attr[0], new CommandWordDes(attr[0], attr[1]));
//                        System.out.println(attr[0]);
                        count++;
                    } catch (Exception e) {
                        logger.error("错误的自定义命令词说明数据：" + line);
                    }
                }

                logger.info("自定义命令词说明初始化完毕，数据条数：" + count);
            }

            @Override
            public void add(String line) {
                try {
                    String[] attr = line.split("=");
                    COMMAND_WORD_DES.put(attr[0], new CommandWordDes(attr[0], attr[1]));
                } catch (Exception e) {
                    logger.error("错误的自定义命令词说明数据：" + line);
                }
            }

            @Override
            public void remove(String line) {
                try {
                    String[] attr = line.split("=");
                    COMMAND_WORD_DES.remove(attr[0]);
                } catch (Exception e) {
                    logger.error("错误的自定义词性说明数据：" + line);
                }
            }
        }, WordConfTools.get("command.word.des.path", "classpath:command_word_des.txt"));

        //加载command_word_dic.txt
        Detector.loadAndWatch(new ResourceLoader() {
            @Override
            public void clear() {
                COMMAND_WORD_DIC.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化命令词标注器");
                int count = 0;
                for (String line : lines) {
                    try {
                        String[] attr = line.split(":");
                        COMMAND_WORD_DIC.put(attr[0], getCommandWordDes(attr[1]));
                        count++;
                    } catch (Exception e) {
                        logger.error("错误的命令词数据：" , line);
                    }
                }
                logger.info("自定义命令词数据初始化完毕，数据条数：" + count);
            }

            @Override
            public void add(String line) {
                try {
                    String[] attr = line.split("=");
                    COMMAND_WORD_DIC.put(attr[0], new CommandWordDes(attr[0], attr[1]));
                } catch (Exception e) {
                    logger.error("错误的自定义命令词数据：" + line);
                }
            }

            @Override
            public void remove(String line) {
                try {
                    String[] attr = line.split("=");
                    COMMAND_WORD_DIC.remove(attr[0]);
                } catch (Exception e) {
                    logger.error("错误的自定义词性说明数据：" + line);
                }
            }
        }, WordConfTools.get("command.word.dic.path", "classpath:command_word_dic.txt"));
    }

    private static CommandWordDes getCommandWordDes(String text) throws Exception{
        if (!COMMAND_WORD_DES.containsKey(text)){
            throw new Exception();
        }
        return COMMAND_WORD_DES.get(text);
    }

    public static String getCommandWordIntent(Word word) {
        if (!COMMAND_WORD_DIC.containsKey(word.getText())) {
            return "unknown";
        }
        return COMMAND_WORD_DIC.get(word.getText()).intent;
    }

    public static Map<String, CommandWordDes> getCommandWordDes() {
        return COMMAND_WORD_DES;
    }

    public static Map<String, CommandWordDes> getCommandWordDic() {
        return COMMAND_WORD_DIC;
    }

}
