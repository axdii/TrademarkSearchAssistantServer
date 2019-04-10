package top.atzlt.wordSeg.segmentation;


import top.atzlt.wordSeg.dictionary.Dictionary;

/**
 * 基于词典的中文分词接口
 */
public interface DictionaryBasedSegmentation extends Segmentation{

    /**
     * 为基于词典的中文分词接口指定词典操作接口
     * @param dictionary
     */
    public void setDictionary(Dictionary dictionary);

    /**
     * 获取词典操作接口
     * @return
     */
    public Dictionary getDictionary();

}
