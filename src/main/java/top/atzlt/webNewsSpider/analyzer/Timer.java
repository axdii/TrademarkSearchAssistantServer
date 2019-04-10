package top.atzlt.webNewsSpider.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.util.Date;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       Timer
 * version:     1.0
 * last modified time:      2019-3-14
 * function:    作为程序定时抓取网页内容的计时器
 */

public class Timer {
    private static Logger logger = LoggerFactory.getLogger(Timer.class);
    private static AnalyIndexPage analyIndexPage = new AnalyIndexPage();
    private static Filter filter = new Filter();

    public static void runNewsPicker() {

        try {
            while (true) {
                logger.info(new Date().toString() + "：更新新闻爬虫");
                String[] args = null;
                AnalyIndexPage.main(args);
                logger.info("更新爬虫完成...开始等待一天..");
                Thread.sleep(86400000);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] str) {
        try {
            while (true) {
                String[] args = null;
                AnalyIndexPage.main(args);
                Thread.sleep(86400000);
                /*
                {
//            1设置爬虫起始页面（网站主页；
                    Spider.create(new AnalyIndexPage())
                            .addUrl("http://news.gbicom.cn/")
                            .run();
//            2提取index page 所包含 url；
                    String[] news = null;
                    news = analyIndexPage.getUrlList();

                    int size = news.length;
                    for (int i = 0; i < size; i++) {
                        filter.setProcessurl(news[i]);
//            3过滤url；
                        boolean ifrepeat = filter.match("http://news.gbicom.cn/");
                        if (ifrepeat) {
//                4对新页面进行内容提取；
                            MyPageProcessor processor = new MyPageProcessor(news[i]);

                        }
                    }
                    Thread.sleep(86400000);     //睡他个一天，尽管他要天荒地老呢
                }
                */
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
