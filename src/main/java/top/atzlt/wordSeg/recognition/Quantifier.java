
package top.atzlt.wordSeg.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.util.Detector;
import top.atzlt.wordSeg.util.ResourceLoader;
import top.atzlt.wordSeg.util.WordConfTools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数量词识别
 */
public class Quantifier {
    private static final Logger logger = LoggerFactory.getLogger(Quantifier.class);
    private static final Set<Character> quantifiers=new HashSet<>();
    static{
        reload();
    }
    public static void reload(){
        Detector.loadAndWatch(new ResourceLoader(){

            @Override
            public void clear() {
                quantifiers.clear();
            }

            @Override
            public void load(List<String> lines) {
                logger.info("初始化数量词");
                for(String line : lines){
                    if(line.length() == 1){
                        char _char = line.charAt(0);
                        if(quantifiers.contains(_char)){
                            logger.debug("配置文件有重复项："+line);
                        }else{
                            quantifiers.add(_char);
                        }
                    }else{
                        logger.debug("忽略不合法数量词："+line);
                    }
                }
                logger.info("数量词初始化完毕，数量词个数："+quantifiers.size());
            }

            @Override
            public void add(String line) {
                if (line.length() == 1) {
                    char _char = line.charAt(0);
                    quantifiers.add(_char);
                } else {
                    logger.debug("忽略不合法数量词：" + line);
                }
            }

            @Override
            public void remove(String line) {
                if (line.length() == 1) {
                    char _char = line.charAt(0);
                    quantifiers.remove(_char);
                } else {
                    logger.debug("忽略不合法数量词：" + line);
                }
            }
        
        }, WordConfTools.get("quantifier.path", "classpath:quantifier.txt"));
    }
    public static boolean is(char _char){
        return quantifiers.contains(_char);
    }

}
