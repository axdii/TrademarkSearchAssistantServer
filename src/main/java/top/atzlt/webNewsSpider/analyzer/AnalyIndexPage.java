package top.atzlt.webNewsSpider.analyzer;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       AnalyIndexPage
 * version:     1.0
 * last modified time:      2019-3-13
 * function:    对该网站主页内容进行抽取
 */
public class AnalyIndexPage implements PageProcessor {
    //    public static Filter.filter = new Filter();
    private String indexurl;

    public String getIndexurl() {
        return indexurl;
    }

    public void setIndexurl(String inurl) {
        this.indexurl = inurl;
    }

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        getHotNews(page);
        getNormalList(page);
//        getMoreBtn(page);
        getRightNews(page);
    }

    private void getHotNews(Page page) {
//        System.out.println(page.getHtml().xpath("//div[@class='hot_news']/a").toString());
        String result = page.getHtml().xpath("//div[@class='hot_news']").toString();
        List list = new ArrayList<String>();
        int start = 0;
        int end = 0;
        int size = 0;
        String a_target = "<a target=\"_blank\"";
        String mat_href = "href=\"";
        String mat_imgsrc = "<img src=\"";
        String mat_title = "alt=\"";
        while ((start = result.indexOf(a_target, start)) != -1) {
            size++;
            if ((start = result.indexOf(mat_href, start)) != -1) {
                start = start + mat_href.length();
                end = result.indexOf("\"", start);
                String url = result.substring(start, end);
//                System.out.println("href:" + url);

                start = result.indexOf(mat_imgsrc, end) + mat_imgsrc.length();
                end = result.indexOf("\"", start);
                String imgurl = result.substring(start, end);
//                System.out.println("imgsrc:" + imgurl);

                start = result.indexOf(mat_title, end) + mat_title.length();
                end = result.indexOf("\"", start);
                String title = result.substring(start, end);
//                System.out.println("title:" + title);

                list.add(url);
            }
            start = end;
        }
        Filter filter = new Filter(list);
        filter.matchList();
//        System.out.println(size+"\n"+result);
//        System.out.println(page.getHtml().xpath("//div[@class='hot_news']").toString());
    }

    private void getNormalList(Page page) {
//        <div class ni_newslist>
//            <div class module_newslink_box>
//                <div class mnb_news_info>
//                    <a href>
//                    <a href>
//                    <div class module_news_info>
//                        <span>*3;
//                <a href<img>>
//                </>
//            </>//一条新闻链接结束
//        </>

        List list = new ArrayList<String>();
        String result = page.getHtml().xpath("//div[@class='ni_newslist']").toString();
        int start = 0;
        int end = 0;
        int size = 0;
        String mat_box = "module_newslink_box";
        String mat_href = "href=\"";
        while ((start = result.indexOf(mat_box, start)) != -1) {
            size++;
            start = result.indexOf(mat_href, start) + mat_href.length();
            end = result.indexOf("\"" , start);
            String url = result.substring(start , end);
//            System.out.println(url);
            list.add(url);
        }
        Filter filter = new Filter(list);
        filter.matchList();
//        System.out.println(page.getHtml().xpath("//div[@class='module_newslink_box']").toString());
//        System.out.println(page.getHtml().xpath("//div[@class='mnb_news_info']/a").toString());


    }

    private void getMoreBtn(Page page) {
        System.out.println(page.getHtml().xpath("//div[@class='ni_newslist']/a").toString());
    }

    private void getRightNews(Page page) {
        List list = new ArrayList<String>();
        String result = page.getHtml().xpath("//div[@class='aside_news']").toString();
        int start = 0;
        int end = 0;
        int size = 0;
        String mat_href = "href=\"";
        while ((start = result.indexOf(mat_href, start)) != -1) {
            size++;
            start += mat_href.length();
            end = result.indexOf("\"" , start);
            String url = result.substring(start , end);
//            System.out.println(url);
            list.add(url);
        }
        Filter filter = new Filter(list);
        filter.matchList();
    }

    public String[] getUrlList() {
        int length = 6;//the length should be get from the sum of url from index page
//        ......

        String[] urllist = new String[length];
        return urllist;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new AnalyIndexPage())
                .addUrl("http://news.gbicom.cn/")
                .run();
    }
}
