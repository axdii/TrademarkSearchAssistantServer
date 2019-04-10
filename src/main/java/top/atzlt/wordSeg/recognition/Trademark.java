package top.atzlt.wordSeg.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.domain.TrademarkListDomain;
import top.atzlt.wordSeg.segmentation.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trademark {
    private static final Logger logger = LoggerFactory.getLogger(Trademark.class);
    private static final Set<String> TRADEMARK_NAME = new HashSet<>();//商标名
    private static final Set<String> TRADEMARK_FILE_NAME = new HashSet<>();//商标申请文件名称



    public static void reload(List<TrademarkListDomain> trademarkListDomains){

        for (TrademarkListDomain trademarkListDomain : trademarkListDomains) {
            TRADEMARK_NAME.add(trademarkListDomain.getTrademarkName());
        }

    }

    public static List<String> getTrademarkNames(){
        return new ArrayList<>(TRADEMARK_NAME);
    }

    public static List<String> getTrademarkFileNames(){
        return new ArrayList<>(TRADEMARK_FILE_NAME);
    }

    public static boolean isContains(String text) {
        return TRADEMARK_NAME.contains(text)||TRADEMARK_FILE_NAME.contains(text);
    }



}
