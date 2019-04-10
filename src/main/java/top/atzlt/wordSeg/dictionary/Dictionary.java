package top.atzlt.wordSeg.dictionary;

import java.util.List;

public interface Dictionary {

    /**
     * 词典中的词的最大长度，即有多少个字符
     * @return 长度
     */
    public int getMaxLength();

    /**
     * 判断指定的文本是不是一个词
     * @param item 文本
     * @param start 指定的文本从哪个下标索引开始
     * @param length 指定的文本的长度
     * 比如：contains("我爱中国",  2, 2);
     * 表示的意思是“中国”是不是一个定义在词典中的词
     */
    public boolean contains(String item, int start, int length);

    /**
     * 判断文本是不是一个词
     */
    public boolean contains(String item);

    /**
     * 批量将词加入词典
     * @param items 集合中的每一个元素是一个词
     */
    public void addAll(List<String> items);

    /**
     * 清空词典中的所有的词
     */
    public void clear();

}
