package top.atzlt.webNewsSpider.test;

/**
 *
 */
public class CnBlog {
    private String title;
    private String content;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return "CnBlog:\ttitle(" + title + ")\t\tcontent(" + content + ")\t\tdate(" + date + ")\n";
    }

    public static void main(String[] args) {
        CnBlog cn = new CnBlog();
        System.out.println(cn);
    }
}