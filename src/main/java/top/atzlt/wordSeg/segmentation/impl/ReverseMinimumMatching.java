package top.atzlt.wordSeg.segmentation.impl;

import top.atzlt.wordSeg.recognition.RecognitionTool;
import top.atzlt.wordSeg.segmentation.SegAlgorithm;
import top.atzlt.wordSeg.segmentation.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ReverseMinimumMatching extends AbstractSegmentation {
    @Override
    public List<Word> segImpl(String text) {
        Stack<Word> result = new Stack<>();
        //文本长度
        final int textLen=text.length();
        //从未分词的文本中截取的长度
        int len=1;
        //剩下未分词的文本的索引
        int start=textLen-len;
        //只要有词未切分完就一直继续
        while(start>=0){
            //用长为len的字符串查词典
            while(!getDictionary().contains(text, start, len) && !RecognitionTool.recog(text, start, len)){
                //如果查不到，则长度加一后继续
                //索引向前移动一个字，然后继续
                len++;
                start--;
                //如果长度为词典最大长度且在词典中未找到匹配
                //或已经遍历完剩下的文本且在词典中未找到匹配
                //则按长度为一切分
                if(len>getInterceptLength() || start<0){
                    //重置截取长度为一
                    //向后移动start索引
                    start+=len-1;
                    len=1;
                    break;
                }
            }
            addWord(result, text, start, len);
            //每一次成功切词后都要重置开始索引位置
            start--;
            //每一次成功切词后都要重置截取长度
            len=1;
        }
        len=result.size();
        List<Word> list = new ArrayList<>(len);
        for(int i=0;i<len;i++){
            list.add(result.pop());
        }
        return list;
    }

    @Override
    public SegAlgorithm getSegAlgorithm() {
        return SegAlgorithm.ReverseMinimumMatching;
    }
}
