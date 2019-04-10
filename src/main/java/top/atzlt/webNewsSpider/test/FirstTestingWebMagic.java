package top.atzlt.webNewsSpider.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * author:      logMath
 * date:        2019-3-13
 * package:     test
 * class:       FirstTestingWebMagic
 * version:     1.0
 * last modified time:      2019-3-13
 * function:
 */
public class FirstTestingWebMagic implements PageProcessor {
    //爬虫的配置,                            重试时间             抓取间隔
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    //计数器
    private static int count = 0;

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new FirstTestingWebMagic()).addUrl("https://www.cnblogs.com/").thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束,耗时约" + ((endTime - startTime) / 1000) + "秒,爬取了" + count + "条记录");
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        //判断链接是否符合http://www.cnblogs.com/数字字母-/p/7个数字.html格式
        if (!(page.getUrl().regex("http://www.cnblogs.com/[a-z 0-9 -]+/p/[0-9]{7}.html").match())) {
            //加入满足条件的链接
            page.addTargetRequests(
                    page.getHtml().xpath("//*[@id=\"post_list\"]/div/div[@class='post_item_body']/h3/a/@href").all()
            );
        } else {
            //获取页面需要的内容
            System.out.println("抓取的内容:" + page.getHtml().xpath("//*[@id=\"Header1_HeaderTitle\"]/text()").get());
            count++;
        }
    }
}