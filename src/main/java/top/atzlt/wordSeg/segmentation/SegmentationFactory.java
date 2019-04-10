package top.atzlt.wordSeg.segmentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 中文分词工厂类
 * 根据指定的分词算法返回分词实现
 */
public class SegmentationFactory {
    private static final Logger logger = LoggerFactory.getLogger(SegmentationFactory.class);
    private static final Map<String, Segmentation> pool = new HashMap<>();
    private SegmentationFactory(){};
    public static synchronized Segmentation getSegmentation(SegAlgorithm segmentationAlgorithm){
        String clazz = "top.atzlt.wordSeg.segmentation.impl."+segmentationAlgorithm.name();
        Segmentation segmentation = pool.get(clazz);
        if(segmentation == null){
            logger.info("构造分词实现类：" + clazz);
            try {
                segmentation = (Segmentation)Class.forName(clazz).newInstance();
                pool.put(clazz, segmentation);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                logger.error("构造分词实现类失败：" , ex);
            }
        }
        return segmentation;
    }
}