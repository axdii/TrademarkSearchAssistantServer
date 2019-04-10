package top.atzlt.webNewsSpider.analyzer;

import us.codecraft.webmagic.Spider;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       InitURL
 * version:     1.0
 * last modified time:      2019-3-14
 * function:    初始新闻存库，但如果中间有断页则处理起来比较麻烦（且现今没有做合法性判断）
 */

public class InitURL {
    private String common = "http://news.gbicom.cn/wz/new";

    public static void main(String[] args) {
        InitURL initurl = new InitURL();
        int suffix = 100000;
        int end = suffix + 10000;
        MyPageProcessor processor = new MyPageProcessor();
        for (int i = suffix; i < end; i++) {
            Spider.create(processor)
                    .addUrl(initurl.common + i + ".html")
                    .run();
        }
    }
}
