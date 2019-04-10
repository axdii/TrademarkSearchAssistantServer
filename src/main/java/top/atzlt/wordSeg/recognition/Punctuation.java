
package top.atzlt.wordSeg.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 判断一个字符是否是标点符号
 */
public class Punctuation {
    private static final Logger logger = LoggerFactory.getLogger(Punctuation.class);
    private static char[] chars = null;
    static{
        reload();
    }
    public static void reload(){
        Detector.loadAndWatch(new ResourceLoader(){

            @Override
            public void clear() {
                chars = null;
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化标点符号");
                Set<Character> set = new HashSet<>();
                for(String line : lines){
                    if(line.length() == 1){
                        set.add(line.charAt(0));
                    }else{
                        logger.info("长度不为一的标点符号："+line);
                    }
                }
                //增加空白字符
                set.add(' ');
                set.add('　');
                set.add('\t');
                set.add('\n');
                set.add('\r');
                List<Character> list = new ArrayList<>();
                list.addAll(set);
                Collections.sort(list);
                int len = list.size();
                chars = new char[len];
                for(int i=0; i<len; i++){
                    chars[i] = list.get(i);
                }
                set.clear();
                list.clear();
            }

            @Override
            public void add(String line) {
                if(line.length() != 1){
                    logger.info("长度不为一的标点符号："+line);
                    return;
                }
                List<String> lines = new ArrayList<>();
                lines.add(line);
                if(chars != null){
                    for(char c : chars){
                        lines.add(Character.toString(c));
                    }
                }
                clear();
                load(lines);
            }

            @Override
            public void remove(String line) {                
                if(line.length() != 1){
                    logger.info("长度不为一的标点符号："+line);
                    return;
                }
                if(chars == null || chars.length < 1){
                    return;
                }
                List<String> lines = new ArrayList<>();
                for(char c : chars){
                    lines.add(Character.toString(c));
                }
                int len = lines.size();
                lines.remove(line);
                if(len == lines.size()){
                    return;
                }
                clear();
                load(lines);
            }
        
        }, WordConfTools.get("punctuation.path", "classpath:punctuation.txt"));
    }
    /**
     * 判断文本中是否包含标点符号
     * @param text
     * @return 
     */
    public static boolean has(String text){
        for(char c : text.toCharArray()){
            if(is(c)){
                return true;
            }
        }
        return false;
    }
    /**
     * 将一段文本根据标点符号分割为多个不包含标点符号的文本
     * 可指定要保留那些标点符号
     * @param text 文本
     * @param withPunctuation 是否保留标点符号
     * @param reserve 保留的标点符号列表
     * @return 文本列表
     */
    public static List<String> seg(String text, boolean withPunctuation, char... reserve){
        List<String> list = new ArrayList<>();
        int start = 0;
        char[] array = text.toCharArray();
        int len = array.length;
        outer:for(int i=0; i<len; i++){
            char c = array[i];
            for(char t : reserve){
                if(c == t){
                    //保留的标点符号
                    continue outer;
                }
            }
            if(Punctuation.is(c)){
                if(i > start){
                    list.add(text.substring(start, i));
                    //下一句开始索引
                    start = i+1;
                }else{
                    //跳过标点符号
                    start++;
                }
                if(withPunctuation){
                    list.add(Character.toString(c));
                }
            }
        }
        if(len - start > 0){
            list.add(text.substring(start, len));
        }
        return list;
    }
    /**
     * 判断一个字符是否是标点符号
     * @param _char 字符
     * @return 是否是标点符号
     */
    public static boolean is(char _char){
        int index = Arrays.binarySearch(chars, _char);
        return index >= 0;
    }

}