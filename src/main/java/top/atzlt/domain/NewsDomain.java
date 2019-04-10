package top.atzlt.domain;

import java.util.ArrayList;
import java.util.List;

public class NewsDomain extends BaseDomain<NewsDomain> {

    private int oldId = -1;
    private List<Integer> requestIds;
    private List<News> newsList;

    public NewsDomain(){
        newsList = new ArrayList<>();
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public void addNews(News news) {
        newsList.add(news);
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public List<Integer> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(List<Integer> requestIds) {
        this.requestIds = requestIds;
    }

    public static class News{

        private int newsId = -1;
        private String title = "";
        private String firstImgUrl = "";
        private String time = "";
        private String author = "";
//        private String info = "";
        private String summaryContent = "";
        private String content = "";
        private List<Integer> recommendNewsList;


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getNewsId() {
            return newsId;
        }

        public void setNewsId(int newsId) {
            this.newsId = newsId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirstImgUrl() {
            return firstImgUrl;
        }

        public void setFirstImgUrl(String firstImgUrl) {
            this.firstImgUrl = firstImgUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }


        public String getSummaryContent() {
            return summaryContent;
        }

        public void setSummaryContent(String summaryContent) {
            this.summaryContent = summaryContent;
        }

        public List<Integer> getRecommendNewsList() {
            return recommendNewsList;
        }

        public void setRecommendNewsList(List<Integer> recommendNewsList) {
            this.recommendNewsList = recommendNewsList;
        }
    }

}

