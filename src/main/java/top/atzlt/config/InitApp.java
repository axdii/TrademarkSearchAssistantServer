package top.atzlt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.atzlt.dao.TrademarkDao;
import top.atzlt.webNewsSpider.analyzer.Timer;
import top.atzlt.wordSeg.WordSegmenter;
import top.atzlt.wordSeg.recognition.Trademark;

import javax.annotation.PostConstruct;

@Component
public class InitApp {
    private static final Logger logger = LoggerFactory.getLogger(InitApp.class);
    private TrademarkDao trademarkDao;

    @Autowired
    public void setTrademarkDao(TrademarkDao trademarkDao) {
        this.trademarkDao = trademarkDao;
    }

    @PostConstruct
    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化商标名称
                logger.info("初始化服务端应用..");
                Trademark.reload(trademarkDao.getTrademarkLists());
//                DictionaryFactory.reload();//初始化词典
                WordSegmenter.demo();//运行demo测试分词功能，并初始化词典

                logger.info("初始化新闻爬虫..");
                Timer.runNewsPicker();
            }
        }).start();
    }

}
