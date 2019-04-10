package top.atzlt.wordSeg.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.recognition.CommandWord;
import top.atzlt.wordSeg.recognition.ReplenishWord;
import top.atzlt.wordSeg.recognition.Trademark;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 词典工厂
 通过系统属性及配置文件指定词典实现类（dic.class）和词典文件（dic.path）
 指定方式一，编程指定（高优先级）：
      WordConfTools.set("dic.class", "wordSeg.dictionary.impl.DoubleArrayDictionaryTrie");
      WordConfTools.set("dic.path", "classpath:dic.txt");
 指定方式二，配置文件指定（低优先级）：
      在类路径下的word.conf中指定配置信息
      dic.class=wordSeg.dictionary.impl.DoubleArrayDictionaryTrie
      dic.path=classpath:dic.txt
 如未指定，则默认使用词典实现类（wordSeg.dictionary.impl.DoubleArrayDictionaryTrie）和词典文件（类路径下的dic.txt）
 */
public final class DictionaryFactory {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryFactory.class);

    private static final int INTERCEPT_LENGTH = WordConfTools.getInt("intercept.length", 16);//一个词最大的长度
    private DictionaryFactory(){}//不允许自定义构建该类
    public static Dictionary getDictionary(){
        return DictionaryHolder.DIC;
    }
    public static void reload(){
        DictionaryHolder.reload();
    }


    //词典建立
    private static final class DictionaryHolder{

        private static Dictionary DIC;

        static{
            try {
                String dicClazz = WordConfTools.get("dic.class", "top.atzlt.wordSeg.dictionary.impl.DoubleArrayDictionaryTrie");
                DIC = (Dictionary)Class.forName(dicClazz.trim()).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            reload();
        }
        static void reload(){
            Detector.loadAndWatch(new ResourceLoader() {

                @Override
                public void clear() {
                    DIC.clear();
                }

                @Override
                public void load(List<String> lines) {
                    logger.info("初始化词典");
                    long start = System.currentTimeMillis();

                    //加载商标名称
//                    Trademark.reload();
                    int count = 0;
                    for (String trademarkName : Trademark.getTrademarkNames()){
                        count++;
                        lines.add(trademarkName);
                    }

                    logger.info("共加载" + count + "个商标名称");


                    List<String> words = getAllWords(lines);
                    lines.clear();

                    //构造词典
                    logger.info("构造词典中...");
                    DIC.addAll(words);
                    //输出统计信息
                    showStatistics(words);
                    System.gc();//回收系统垃圾
                    logger.info("词典初始化完毕，耗时：" + (System.currentTimeMillis() - start) + " 毫秒");

//                    ReplenishWord.reload();
                }

                //显示词的统计信息
                private void showStatistics(List<String> words) {
                    Map<Integer, AtomicInteger> map = new HashMap<Integer, AtomicInteger>();
                    words.forEach(word -> {
                        map.putIfAbsent(word.length(), new AtomicInteger());
                        map.get(word.length()).incrementAndGet();
                    });
                    //统计词数
                    int wordCount = 0;
                    //统计平均词长
                    int totalLength = 0;
                    for (int len : map.keySet()) {
                        totalLength += len * map.get(len).get();
                        wordCount += map.get(len).get();
                    }
                    logger.info("词数目：" + wordCount + "，词典最大词长：" + DIC.getMaxLength());
                    for (int len : map.keySet()) {
                        if (len < 10) {
                            logger.info("词长  " + len + " 的词数为：" + map.get(len));
                        } else {
                            logger.info("词长 " + len + " 的词数为：" + map.get(len));
                        }
                    }
                    logger.info("词典平均词长：" + (float) totalLength / wordCount);
                }

                @Override
                public void add(String line) {
                    //加入词典
                }

                @Override
                public void remove(String line) {
                    //移除词
                }

                private List<String> getAllWords(List<String> lines) {
                    return lines.stream().flatMap(line -> getWords(line).stream()).filter(w -> w.length() <= INTERCEPT_LENGTH).collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
                }

                //获取单词
                private List<String> getWords(String line) {
                    List<String> words = new ArrayList<>();
                    //一行以空格分隔可以放多个词
                    for (String word : line.split("\\s+")) {
                        //处理词性词典
                        if (word.length() > 2 && word.contains(":")) {
                            String[] attr = word.split(":");
                            if (attr != null && attr.length > 1) {
                                //忽略单字
                                if (attr[0].length() > 1) {
                                    word = attr[0];
                                } else {
                                    word = null;
                                }
                            }
                        }
                        if (word != null) {
                            words.add(word);
                        }
                    }
                    return words;
                }
            }, WordConfTools.get("dic.path", "classpath:dic.txt")
                    + "," + WordConfTools.get("punctuation.path", "classpath:punctuation.txt")
                    + "," + WordConfTools.get("part.of.speech.dic.path", "classpath:part_of_speech_dic.txt")
                    + "," + WordConfTools.get("trademark.name.path", "classpath:trademark_name.txt"));
        }
    }

}