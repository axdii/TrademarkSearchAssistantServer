package top.atzlt.webNewsSpider.analyzer;

/**
 * author:      logMath
 * date:        2019-3-13
 * package:     analyzer
 * class:       NewsInfo
 * version:     1.0
 * last modified time:      2019-3-14
 * function:    该类对象储存一个新闻实例，内容包括标题、作者、发布时间、文章内容信息
 */


import java.io.*;

public class NewsInfo {
    private String title;
    private String author;
    private String date;
    private String introduce;

    private String contentstab;

    private String htmlurl;        //http://news.gbicom.cn/wz/new117515.html
    private String id;      //new117515

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String cont) {
        this.contentstab = cont;
    }

    public void setHtmlurl(String htmlurl) {
        this.htmlurl = htmlurl;
        this.id = htmlurl.substring(htmlurl.lastIndexOf("/")+1,htmlurl.lastIndexOf("."));
        System.out.println(id);
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void write() {
        try {
            System.out.println("");
            String path = Filter.class.getResource("/").getPath() + "top/atzlt/webNewsSpider/news/";
            File newsDir = new File(path);
            if (!newsDir.exists()) {
                newsDir.mkdir();
            }


            File file = new File(path+id);
            if (!file.exists()){
                file.mkdir();
                System.out.println("folder building...");
            }
            File info = new File(path+id+"/info");
            if (!info.exists()) {
                info.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(info)));
            writer.write(htmlurl+"\n");
            writer.write(title+"\n");
            writer.write(author+"\n");
            writer.write(date+"\n");
            writer.write(introduce+"\n");
            writer.close();
            File content = new File(path+id+"/content");
            if (!content.exists()) {
                content.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(content)));
            writer.write(contentstab);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}
