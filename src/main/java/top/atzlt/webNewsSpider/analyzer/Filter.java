package top.atzlt.webNewsSpider.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       Filter
 * version:     1.0
 * last modified time:      2019-3-13
 * function:    针对网页新闻链接进行过滤，丢弃重复链接
 */

public class Filter {
    private static final Logger logger = LoggerFactory.getLogger(Filter.class);
    private static FileInputStream fis;    //已爬记录
    private static List urllist = new ArrayList<String>();
    private String processurl;      //当前待处理url
    private List<String> purllist;
    private static int size = 0;               //记录所有已爬url数目

    static {
        try {
            //设置静态加载过滤器，提高过滤速度
//        1.加载inputstream;
            /*
            File file = new File("src/main/resources/filterurl/found.fd");
            InputStream in = new FileInputStream(file);
            if (in==null){System.out.println("in null");}
            */

            InputStream is = Filter.class.getResourceAsStream("../filterurl/found.fd");
//            System.out.println((char)is.read());
//            if (fis==null){System.out.println("null");}
//        2.初始size、urllist;
            BufferedReader reader = null;
            InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                System.out.println(tempString);
                size++;
                urllist.add(tempString);
            }

//        3.关闭输入流;
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Filter(String str) {
        processurl = str;
    }

    Filter(List list) {
        purllist = list;
    }

    public void matchList() {
        if (purllist == null) {
            return;
        }
        int length = purllist.size();
        for (int i = 0; i < length; i++) {
            match(purllist.get(i));
        }
    }

    public Filter() {
    }

    private String[] toMd5Result(String... str) {
        int size = str.length;
        String[] md5result = new String[size];
        for (int i = 0; i < size; i++) {
            md5result[i] = getSingleMd5(md5result[i]);
        }
        return md5result;
    }

    private String getSingleMd5(String str) {
        String result;
        result = str;//code here shound realize logic that convert a source string into a md5 result
        return result;
    }

    public boolean match(String url) {
        if (url == null) {
            return true;
        }
        String hashcode = getSingleMd5(url);
        for (int i = 0; i < size; i++) {
            if (hashcode == urllist.get(i)) {
                logger.info("Filter found repeated");
                return true;
            }
        }
        url = "http://news.gbicom.cn" + url;
        MyPageProcessor processor = new MyPageProcessor(url);
        Spider.create(processor).addUrl(url).run();
        add(url);
//        logger.info(url);
        size++;
        return false;
    }

    private void add(String str) {
        try {
//            urllist.add(processurl);
            urllist.add(str);
            String filePath = Filter.class.getResource("/").getPath() + "top/atzlt/webNewsSpider/filterurl/found.fd";
            File file = new File(filePath);
//            OutputStream os = new FileOutputStream(file);
//            OutputStreamWriter osr = new OutputStreamWriter(os, "UTF-8");
//            BufferedWriter writer = new BufferedWriter(osr);//此方法会将文件原内容清空，故使用下面这个方法实现内容追加
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            writer.write("\n" + str);
            writer.close();
//            osr.close();
//            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println("dblink building...");
        Filter filter = new Filter();
        filter.add("FileInputStrea");
        filter.setProcessurl("http://news.gbicom.cn/wz/new117550.html");
    }

    public void setProcessurl(String url) {
        url.trim();
//        String str[] = url.split("/");
//        url = str[str.length-1];
        this.processurl = url;
//        filter.add(url);
    }

    public String getProcessurl() {
        return processurl;
    }
}
