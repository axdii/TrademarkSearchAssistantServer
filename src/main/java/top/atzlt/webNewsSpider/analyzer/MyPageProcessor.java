package top.atzlt.webNewsSpider.analyzer;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       MyPageProgressor
 * version:     1.0
 * last modified time:      2019-3-13
 * function:    webmagic初使用，简单元素抽取
 */
public class MyPageProcessor implements PageProcessor {
    private NewsInfo info = null;
    private String url = "http://news.gbicom.cn/wz/new117550.html";
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    MyPageProcessor(String str) {
        this.url = str;
    }

    MyPageProcessor() {
    }

    @Override
    public void process(Page page) {
        init();//
        info.setHtmlurl(this.url);
        pickTitle(page);
        pickInfo(page);
        pickContent(page);
        info.write();
    }

    private String pickTitle(Page page) {
//        page.putField("author",page.getUrl().regex("http"));
        String result = page.getHtml().xpath("//h1[@class='tittle_news']").toString();
        int start = result.indexOf(">") + 1;
        int end = result.lastIndexOf("<");
        result = result.substring(start , end);
        System.out.println(result);
        info.setTitle(result);
//        System.out.println(page.getHtml().xpath("//h1[@class='tittle_news']").toString());
//        System.out.println(page.getHtml().css("title"));
        return result;
    }

    private void pickInfo(Page page) {
        String result = page.getHtml().xpath("//div[@class='module_news_info']").toString();
        int start = result.indexOf("作者");
        int end = result.indexOf("<",start);
        String author = "作者" + result.substring(start,end);
        start = result.indexOf("info\">",end) + "info\">".length();
        end = result.indexOf("<" , start);
        String date = result.substring(start , end);
//        System.out.println(author + date);
        info.setAuthor(author);
        info.setDate(date);

        result = page.getHtml().xpath("//p[@class='news_introduction']").toString();
        start = result.indexOf(">") + 1;
        end = result.lastIndexOf("<");
        result = result.substring(start , end);
//        System.out.println(result);
        info.setIntroduce(result);
        return ;
    }

    private void pickContent(Page page) {
       String result = page.getHtml().xpath("//div[@class='news_content']").toString();
//        p标签为文本, center标签为图片;
        info.setContent(result);
    }

    private void init() {
        if (info == null) {
            info = new NewsInfo();
        }
        try {
            if (url == null) {
                throw new Throwable("你耍我");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {
//        long startTime, endTime;
//        System.out.println("开始爬取...");
//        startTime = System.currentTimeMillis();
//        Spider.create(new MyPageProcessor()).addUrl("https://www.cnblogs.com/").thread(5).run();
//        endTime = System.currentTimeMillis();
//        System.out.println("爬取结束,耗时约" + ((endTime - startTime) / 1000) + "秒");

        Spider.create(new MyPageProcessor())
                .addUrl("http://news.gbicom.cn/wz/new117550.html")
                .run();
    }
}