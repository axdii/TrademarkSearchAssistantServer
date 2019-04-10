package top.atzlt.service.Impl;

import com.google.gson.Gson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.SegmentationDomain;
import top.atzlt.service.HMIService;
import top.atzlt.wordSeg.WordSegmenter;
import top.atzlt.wordSeg.recognition.CommandWord;
import top.atzlt.wordSeg.recognition.RecognitionTool;
import top.atzlt.wordSeg.recognition.ReplenishWord;
import top.atzlt.wordSeg.segmentation.Word;
import top.atzlt.wordSeg.tagging.PartOfSpeechTagging;
import top.atzlt.wordSeg.util.WordSegCommonConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HMIServiceImpl implements HMIService {

    private static final Logger logger = LoggerFactory.getLogger(HMIServiceImpl.class);

    @Override
    public SegmentationDomain segAndDealText(SegmentationDomain segmentationDomain) {

        String rawText = segmentationDomain.getRawText();
        List<Word> words = WordSegmenter.segWithStopWords(rawText);
        PartOfSpeechTagging.process(words);
        System.out.println(words);
        return dealWords(words, segmentationDomain.getRawText());
    }

    @Override
    public String segText(String rawText) {
        List<Word> words = WordSegmenter.segWithStopWords(rawText);
        PartOfSpeechTagging.process(words);
        StringBuilder str = new StringBuilder("");
        for (Word word : words) {
            str.append(word.getText()).append("/");
        }
        return str.toString();
    }

    private SegmentationDomain dealWords(List<Word> words, String rawText) {

        SegmentationDomain obj = new SegmentationDomain();
        obj.setRawText(rawText);
        obj.setStatusCode(CommonConstant.OPERATE_SUCCESS);

        Map<String, String> commandMap = new HashMap<>();
        Map<String, String> objectMap = new HashMap<>();
        Map<String, String> replenishMap = new HashMap<>();
        StringBuilder str = new StringBuilder("");


        for (Word word : words) {
            str.append(word.getText()).append("/");
            switch (RecognitionTool.judgeWordEssentialFeature(word)) {
                case WordSegCommonConstant.INTENT_WORD:
                    commandMap.put(word.getText(), CommandWord.getCommandWordIntent(word));
                    break;
                case WordSegCommonConstant.REPLENISH_WORD:
                    replenishMap.put(ReplenishWord.getReplenishWordType(word), "replenish");
                    break;
                case WordSegCommonConstant.NOT_KEY_WORD:
                    break;
                default:
                    objectMap.put(word.getText(), RecognitionTool.judgeWordEssentialFeature(word));
                    break;
            }

        }
        SegmentationDomain.Results results = new SegmentationDomain.Results();

        boolean hasTrademarkName = objectMap.containsValue(WordSegCommonConstant.TRADEMARK_NAME_WORD);
        boolean hasTrademarkFileName = objectMap.containsValue(WordSegCommonConstant.TRADEMARK_FILE_NAME_WORD);
        boolean hasTrademarkAttributeName = objectMap.containsValue(WordSegCommonConstant.TRADEMARK_ATTRIBUTE_WORD);
        boolean hasReplenish = !replenishMap.isEmpty();


        if (objectMap.size() == 0 && commandMap.size() == 0 && replenishMap.size() == 0) {

            results.setDomain(WordSegCommonConstant.ERROR_DOMAIN);

        }else if (hasTrademarkName && hasTrademarkAttributeName && !hasTrademarkFileName) {

            results.setDomain(WordSegCommonConstant.ATTRIBUTE_DOMAIN);

        } else if (hasTrademarkName && !hasTrademarkAttributeName && !hasTrademarkFileName) {

            results.setDomain(WordSegCommonConstant.TRADEMARK_DOMAIN);

        } else if (hasTrademarkFileName && !hasTrademarkName && !hasTrademarkAttributeName) {

            results.setDomain(WordSegCommonConstant.FILE_DOMAIN);

        } else {

            results.setDomain(WordSegCommonConstant.MESS_DOMAIN);

        }

        if (hasReplenish) {

            if (results.getDomain().equals(WordSegCommonConstant.TRADEMARK_DOMAIN) && replenishMap.containsKey("similarity")) {
                results.setDomain(WordSegCommonConstant.SIMILARITY_TEXT_DOMAIN);
            }

        }

        results.setIntent(commandMap);
        results.setObject(objectMap);
        results.setReplenish(replenishMap);
        obj.setParsedText(str.toString());
        obj.setResults(results);

        return obj;
    }

    private String domainRecognition(){
        return null;
    }

    @Test
    public void test(){
        SegmentationDomain segmentationDomain = new SegmentationDomain();
        segmentationDomain.setRawText("帮我搜索格力的近似商标");
        System.out.println(segAndDealText(segmentationDomain).getResults().toString());
    }
}
