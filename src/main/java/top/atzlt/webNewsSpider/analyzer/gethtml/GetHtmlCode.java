package top.atzlt.webNewsSpider.analyzer.gethtml;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       GetHtmlCode
 * version:     1.0
 * last modified time:      2019-3-13
 * function:    链接url，并读取HTML（代码来源于网络）
 */
public class GetHtmlCode {
    GetHtmlCode() {
        ;
    }


    public String getSource(String url) {
        String html = new String();
        HttpGet httpget = new HttpGet(url);     //创建Http请求实例，URL 如：https://cd.lianjia.com/
        // 模拟浏览器，避免被服务器拒绝，返回返回403 forbidden的错误信息
        httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();   // 使用默认的HttpClient
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {     // 返回 200 表示成功
                html = EntityUtils.toString(response.getEntity(), "utf-8");     // 获取服务器响应实体的内容
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return html;
    }

    public static void main(String[] args) {
        GetHtmlCode gh = new GetHtmlCode();
        String str = "http://news.gbicom.cn/";
//        System.out.println(gh.getSource(str));
    }
}
