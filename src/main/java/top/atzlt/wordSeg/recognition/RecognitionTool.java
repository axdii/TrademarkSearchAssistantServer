
package top.atzlt.wordSeg.recognition;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.util.WordConfTools;
import top.atzlt.wordSeg.util.WordSegCommonConstant;

import java.util.Map;

/**
 * 分词特殊情况识别工具
 * 如英文单词、数字、时间等
 */
public class RecognitionTool {
    private static final Logger logger = LoggerFactory.getLogger(RecognitionTool.class);

    private static final boolean RECOGNITION_TOOL_ENABLED = WordConfTools.getBoolean("recognition.tool.enabled", true);
    //'〇'不常用，放到最后
    private static final char[] chineseNumbers = {'一','二','三','四','五','六','七','八','九','十','百','千','万','亿','零','壹','贰','叁','肆','伍','陆','柒','捌','玖','拾','佰','仟','〇'};
    /**
     * 识别文本（英文单词、数字、时间等）
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean recog(final String text){
        return recog(text, 0, text.length());
    }
    /**
     * 识别文本（英文单词、数字、时间等）
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean recog(final String text, final int start, final int len){
        if(!RECOGNITION_TOOL_ENABLED){
            return false;
        }
        return isEnglishAndNumberMix(text, start, len) 
                || isFraction(text, start, len)
                || isQuantifier(text, start, len)
                || isChineseNumber(text, start, len);
    }
    /**
     * 小数和分数识别
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean isFraction(final String text){
        return isFraction(text, 0, text.length());
    }
    /**
     * 小数和分数识别
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isFraction(final String text, final int start, final int len){
        if(len < 3){
            return false;
        }
        int index = -1;
        for(int i=start; i<start+len; i++){
            char c = text.charAt(i);
            if(c=='.' || c=='/' || c=='／' || c=='．' || c=='·'){
                index = i;
                break;
            }
        }
        if(index == -1 || index == start || index == start+len-1){
            return false;
        }
        int beforeLen = index-start;
        return isNumber(text, start, beforeLen) && isNumber(text, index+1, len-(beforeLen+1));
    }
    /**
     * 英文字母和数字混合识别，能识别纯数字、纯英文单词以及混合的情况
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isEnglishAndNumberMix(final String text, final int start, final int len){
        for(int i=start; i<start+len; i++){
            char c = text.charAt(i);
            if(!(isEnglish(c) || isNumber(c))){
                return false;
            }
        }
        //指定的字符串已经识别为英文字母和数字混合串
        //下面要判断英文字母和数字混合串是否完整
        if(start>0){
            //判断前一个字符，如果为英文字符或数字则识别失败
            char c = text.charAt(start-1);
            if(isEnglish(c) || isNumber(c)){
                return false;
            }
        }
        if(start+len < text.length()){
            //判断后一个字符，如果为英文字符或数字则识别失败
            char c = text.charAt(start+len);
            if(isEnglish(c) || isNumber(c)){
                return false;
            }
        }
        logger.info("识别出英文字母和数字混合串：" + text.substring(start, start + len));

        return true;
    }
    /**
     * 英文单词识别
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean isEnglish(final String text){
        return isEnglish(text, 0, text.length());
    }
    /**
     * 英文单词识别
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isEnglish(final String text, final int start, final int len){
        for(int i=start; i<start+len; i++){
            char c = text.charAt(i);
            if(!isEnglish(c)){
                return false;
            }
        }
        //指定的字符串已经识别为英文串
        //下面要判断英文串是否完整
        if(start>0){
            //判断前一个字符，如果为英文字符则识别失败
            char c = text.charAt(start-1);
            if(isEnglish(c)){
                return false;
            }
        }
        if(start+len < text.length()){
            //判断后一个字符，如果为英文字符则识别失败
            char c = text.charAt(start+len);
            if(isEnglish(c)){
                return false;
            }
        }
        logger.info("识别出英文单词：" + text.substring(start, start + len));

        return true;
    }
    /**
     * 英文字符识别，包括大小写，包括全角和半角
     * @param c 字符
     * @return 是否是英文字符
     */
    public static boolean isEnglish(char c){
        //大部分字符在这个范围
        if(c > 'z' && c < 'Ａ'){
            return false;
        }
        if(c < 'A'){
            return false;
        }
        if(c > 'Z' && c < 'a'){
            return false;
        }
        if(c > 'Ｚ' && c < 'ａ'){
            return false;
        }
        if(c > 'ｚ'){
            return false;
        }
        return true;
    }
    /**
     * 数量词识别，如日期、时间、长度、容量、重量、面积等等
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean isQuantifier(final String text){
        return isQuantifier(text, 0, text.length());
    }
    /**
     * 数量词识别，如日期、时间、长度、容量、重量、面积等等
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isQuantifier(final String text, final int start, final int len){
        if(len < 2){
            return false;
        }
        //避免量词和不完整小数结合
        //.的值是46,/的值是47
        //判断前一个字符是否是.或/
        int index = start-1;
        if(index > -1 && (text.charAt(index) == 46 || text.charAt(index) == 47)){
            return false;
        }
        char lastChar = text.charAt(start+len-1);
        if(Quantifier.is(lastChar)
                && 
                (isNumber(text, start, len-1) || 
                isChineseNumber(text, start, len-1) || 
                isFraction(text, start, len-1)) ){
            logger.info("识别数量词：" + text.substring(start, start + len));

            return true;
        }
        return false;
    }
    /**
     * 数字识别
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean isNumber(final String text){
        return isNumber(text, 0, text.length());
    }
    /**
     * 数字识别
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isNumber(final String text, final int start, final int len){
        for(int i=start; i<start+len; i++){
            char c = text.charAt(i);
            if(!isNumber(c)){
                return false;
            }
        }
        //指定的字符串已经识别为数字串
        //下面要判断数字串是否完整
        if(start>0){
            //判断前一个字符，如果为数字字符则识别失败
            char c = text.charAt(start-1);
            if(isNumber(c)){
                return false;
            }
        }
        if(start+len < text.length()){
            //判断后一个字符，如果为数字字符则识别失败
            char c = text.charAt(start+len);
            if(isNumber(c)){
                return false;
            }
        }
        logger.info("识别出数字：" + text.substring(start, start + len));

        return true;
    }
    /**
     * 阿拉伯数字识别，包括全角和半角
     * @param c 字符
     * @return 是否是阿拉伯数字
     */
    public static boolean isNumber(char c){
        //大部分字符在这个范围
        if(c > '9' && c < '０'){
            return false;
        }
        if(c < '0'){
            return false;
        }
        if(c > '９'){
            return false;
        }
        return true;
    }
    /**
     * 中文数字识别，包括大小写
     * @param text 识别文本
     * @return 是否识别
     */
    public static boolean isChineseNumber(final String text){
        return isChineseNumber(text, 0, text.length());
    }
    /**
     * 中文数字识别，包括大小写
     * @param text 识别文本
     * @param start 待识别文本开始索引
     * @param len 识别长度
     * @return 是否识别
     */
    public static boolean isChineseNumber(final String text, final int start, final int len){
        for(int i=start; i<start+len; i++){
            char c = text.charAt(i);
            boolean isChineseNumber = false;
            for(char chineseNumber : chineseNumbers){
                if(c == chineseNumber){
                    isChineseNumber = true;
                    break;
                }
            }
            if(!isChineseNumber){
                return false;
            }
        }
        //指定的字符串已经识别为中文数字串
        //下面要判断中文数字串是否完整
        if(start>0){
            //判断前一个字符，如果为中文数字字符则识别失败
            char c = text.charAt(start-1);
            for(char chineseNumber : chineseNumbers){
                if(c == chineseNumber){
                    return false;
                }
            }
        }
        if(start+len < text.length()){
            //判断后一个字符，如果为中文数字字符则识别失败
            char c = text.charAt(start+len);
            for(char chineseNumber : chineseNumbers){
                if(c == chineseNumber){
                    return false;
                }
            }
        }
        logger.info("识别出中文数字：" + text.substring(start, start + len));

        return true;
    }


    /**
     * 查看词是否是商标名
     * @param word
     * @return 是/否
     */
    public static boolean isTrademarkName(Word word) {
        if (word.getPartOfSpeech() == null || word.getPartOfSpeech().getDes() == null) {
            return false;
        }
        return word.getPartOfSpeech().getPos().equals("tn");
    }

    /**
     * 查看词是否是商标文件名词
     * @param word
     * @return 是/否
     */
    public static boolean isTrademarkFileName(Word word) {
        if (word.getPartOfSpeech() == null || word.getPartOfSpeech().getDes() == null) {
            return false;
        }
        return word.getPartOfSpeech().getPos().equals("fn");
    }

    /**
     * 查看词是否是商标属性词
     * @param word
     * @return 是/否
     */
    public static boolean isTrademarkAttributeName(Word word) {
        if (word.getPartOfSpeech() == null || word.getPartOfSpeech().getDes() == null) {
            return false;
        }
        return word.getPartOfSpeech().getPos().equals("tpn");
    }

    /**
     *查看词是否是动词
     * @param word
     * @return 是/否
     */
    public static boolean isVerb(Word word) {
        if (word.getPartOfSpeech() == null || word.getPartOfSpeech().getDes() == null) {
            return false;
        }
        return word.getPartOfSpeech().getPos().equals("v");
    }

    public static boolean isReplenish(Word word) {
        return !ReplenishWord.getReplenishWordType(word).equals("");
    }

    /**
     * 根据一个单词判断单词的性质
     * @param word
     * @return 单词的性质
     */
    public static String judgeWordEssentialFeature(Word word) {

        if (word.getPartOfSpeech() == null || word.getPartOfSpeech().getPos() == null || word.getPartOfSpeech().getPos().equals("") || word.getPartOfSpeech().getPos().equals("i")) {
            return WordSegCommonConstant.NOT_KEY_WORD;
        }

        if (isVerb(word)) {
            if ("unknown".equals(CommandWord.getCommandWordIntent(word))) {
                return WordSegCommonConstant.NOT_KEY_WORD;
            }

            return WordSegCommonConstant.INTENT_WORD;

        }

        if (isTrademarkAttributeName(word)) {
//            logger.info("查找到商标属性名：" + word.getText());
            return WordSegCommonConstant.TRADEMARK_ATTRIBUTE_WORD;
        }

        if (isTrademarkFileName(word)) {
//            logger.info("查找到商标文件名：" + word.getText());
            return WordSegCommonConstant.TRADEMARK_FILE_NAME_WORD;
        }

        if (isTrademarkName(word)) {
//            logger.info("查找到商标名词：" + word.getText());
            return WordSegCommonConstant.TRADEMARK_NAME_WORD;
        }

        if (isReplenish(word)) {
            return WordSegCommonConstant.REPLENISH_WORD;
        }

        return WordSegCommonConstant.NOT_KEY_WORD;
    }



}
