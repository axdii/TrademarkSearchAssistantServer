
package top.atzlt.wordSeg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 获取配置信息的工具类
 */
public class WordConfTools {
    private static final Logger logger = LoggerFactory.getLogger(WordConfTools.class);

    private static final Map<String, String> conf = new HashMap<>();//配置信息保存在这里
    public static void set(String key, String value){
        conf.put(key, value);
    }

    public static boolean getBoolean(String key, boolean defaultValue){
        String value = conf.get(key) == null ? Boolean.valueOf(defaultValue).toString() : conf.get(key);
        return value.contains("true");
    }

    public static int getInt(String key, int defaultValue){
        return conf.get(key) == null ? defaultValue : Integer.parseInt(conf.get(key).trim());
    }

    public static String get(String key, String defaultValue){
        return conf.get(key) == null ? defaultValue : conf.get(key);
    }
    public static String get(String key){
        return conf.get(key);
    }
    static{
        reload();
    }
    /**
     * 重新加载配置文件
     */
    public static void reload(){
        conf.clear();
        logger.info("开始加载配置文件");
        long start = System.currentTimeMillis();
        loadConf("top/atzlt/wordSeg/word.conf");
        loadConf("word.local.conf");
        checkSystemProperties();
        long cost = System.currentTimeMillis() - start;
        logger.info("配置文件加载完毕，耗时"+cost+" 毫秒，配置项数目："+conf.size());
        logger.info("配置信息：");
        AtomicInteger i = new AtomicInteger();
        conf.keySet().stream().sorted().forEach(key -> {
            logger.info(i.incrementAndGet()+"、"+key+"="+conf.get(key));
        });
    }

    /**
     * 加载配置文件
     * @param confFile 类路径下的配置文件
     */
    private static void loadConf(String confFile) {
        InputStream in = WordConfTools.class.getClassLoader().getResourceAsStream(confFile);
        if(in == null){
            logger.debug("未找到配置文件："+confFile);
            return;
        }
        logger.info("加载配置文件："+confFile);
        loadConf(in);
    }
    /**
     * 加载配置文件
     * @param in 文件输入流
     */
    private static void loadConf(InputStream in) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){
            String line;
            while((line = reader.readLine()) != null){
                line = line.trim();
                //跳过空行或#号或//行
                if("".equals(line) || line.startsWith("#") || line.startsWith("//")){
                    continue;
                }
                int index = line.indexOf("=");
                if(index==-1){
                    logger.debug("错误的配置："+line);
                    continue;
                }
                //有K V
                if(index>0 && line.length()>index+1) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1, line.length()).trim();
                    conf.put(key, value);//这句最重要
                }
                //有K无V
                else if(index>0 && line.length()==index+1) {
                    String key = line.substring(0, index).trim();
                    conf.put(key, "");
                }else{
                    logger.debug("错误的配置："+line);
                }
            }
        } catch (IOException ex) {
            logger.error("配置文件加载失败:", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    /**
     * 使用系统属性覆盖配置文件
     */
    private static void checkSystemProperties() {
        for(String key : conf.keySet()){
            String value = System.getProperty(key);
            if(value != null){
                conf.put(key, value);
                logger.info("系统属性覆盖默认配置："+key+"="+value);
            }
        }
    }
}