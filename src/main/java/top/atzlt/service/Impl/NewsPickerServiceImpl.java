package top.atzlt.service.Impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.NewsDomain;
import top.atzlt.service.NewsPickerService;
import top.atzlt.web.DirManager;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsPickerServiceImpl implements NewsPickerService {

    private final static Logger logger = LoggerFactory.getLogger(NewsPickerServiceImpl.class);

    private static final String WINDOWS_DELIMITER = "\\";//delimiter
    private static final String LINUX_DELIMITER = "/";

    //####TODO 为新闻添加一个缓存

    private RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, NewsDomain.News> hash;
    private ValueOperations<String, String> string;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Resource(name = "redisTemplate")
    public void setHash(HashOperations<String, String, NewsDomain.News> hash) {
        this.hash = hash;
    }

    @Resource(name = "redisTemplate")
    public void setString(ValueOperations<String, String> string) {
        this.string = string;
    }

    @Override
    public List<Integer> getAllNewsId() {
        List<Integer> fileIds = new ArrayList<>();
        String newsFilePath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR);
        File newsFile = new File(newsFilePath);
        if (newsFile.isDirectory()) {
            File[] files = newsFile.listFiles();

            if (files == null) {
                return fileIds;
            }

            for (File file : files) {
                String path = file.getPath();
                String fileName = path.substring(path.lastIndexOf(LINUX_DELIMITER) + 1);
                int fileId = Integer.parseInt(fileName.substring(3));
                fileIds.add(fileId);
            }
        }

        Collections.sort(fileIds);
        return fileIds;
    }

    @Override
    public int getLastNewsId() {
        //不一定实现
        List<Integer> ids = getAllNewsId();
        return ids.get(ids.size() - 1);
    }

    @Override
    public List<Integer> getTopTenLastNewsId() {
        //需要实现
        List<Integer> newsIds = getAllNewsId();

        return newsIds.subList(newsIds.size() - 10, newsIds.size());
    }



    @Override
    public List<Integer> getLastNewsIdsFromOldNewsId(int oldLastNewsId) {
        //需要实现
//        int oldId = Integer.parseInt(oldLastNewsId);
        List<Integer> newsIds = getAllNewsId();
        List<Integer> updateNews = new ArrayList<>();
        int count = 0;
        int i = newsIds.indexOf((Integer) oldLastNewsId) + 1;
        while(count < 5 && i < newsIds.size()){

            updateNews.add(newsIds.get(i));
            count++;
            i++;
        }
        return updateNews;
    }

    @Override
    public List<Integer> getOldNewsIdsFromOldNewsId(int clientOldNewsId) {
        //需要实现
        List<Integer> newsIds = getAllNewsId();
        List<Integer> updateNews = new ArrayList<>();
        int count = 0;
        int i = newsIds.indexOf((Integer) clientOldNewsId) - 1;
        while(count < 5 && i > 0){

            updateNews.add(newsIds.get(i));
            count++;
            i--;
        }
        return updateNews;
    }

    @Override
    public NewsDomain getTopTenLastNews() {
        return getNewsDomainFromNewsIdList(getTopTenLastNewsId());
    }

    @Override
    public NewsDomain getLastNewsFromOldNewsId(int oldLastNewsId) {
        return getNewsSimpleInfoFromNewsIdList(getLastNewsIdsFromOldNewsId(oldLastNewsId));
    }

    @Override
    public NewsDomain getNewsSimpleInfoFromNewsIdList(List<Integer> ids) {
        NewsDomain newsDomain = new NewsDomain();
        List<NewsDomain.News> newsList = new ArrayList<>();
        for (int id :ids) {
            NewsDomain.News news = new NewsDomain.News();
            String newsPath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR) + "/new" + id;
            try {
                news.setNewsId(id);
                news.setRecommendNewsList(get4RandomIds(id));
                news = setNewsInfo(id, news);
                news.setFirstImgUrl(getFirstImgUrl(textFileToString(newsPath + "/content")));//第一张图片
            } catch (IOException e) {
                e.printStackTrace();
            }
            newsList.add(news);
        }
        newsDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        Collections.reverse(newsList);
        newsDomain.setNewsList(newsList);
        return newsDomain;
    }

    public NewsDomain getNewsDomainFromNewsIdList(List<Integer> ids) {
        NewsDomain newsDomain = new NewsDomain();
        List<NewsDomain.News> newsList = new ArrayList<>();
        for (int id : ids) {
            NewsDomain.News news = new NewsDomain.News();
            String newsPath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR) + "/new" + id;
            try {
                news.setContent(textFileToString(newsPath + "/content"));
                news.setNewsId(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newsList.add(news);
        }

        if (newsList.size() != 0) {
            newsDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        } else {
            newsDomain.setStatusCode(CommonConstant.SERVER_ERROR);
        }

        newsDomain.setNewsList(newsList);
        return newsDomain;
    }

    @Override
    public NewsDomain getOldNewsFromOldNewsId(int clientOldNewsId) {
        return getNewsSimpleInfoFromNewsIdList(getOldNewsIdsFromOldNewsId(clientOldNewsId));
    }

    @Override
    public NewsDomain getLastNews() {
        List<Integer> ids = new ArrayList<>();
        ids.add(getLastNewsId());
        return getNewsDomainFromNewsIdList(ids);
    }

    @Override
    public NewsDomain getTopTenLastSimpleNews() {
        return getNewsSimpleInfoFromNewsIdList(getTopTenLastNewsId());
    }

    @Override
    public NewsDomain getNewsById(int newsId) {
        String newsContentPath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR) + "/new" + newsId + "/content";
        NewsDomain newsDomain = new NewsDomain();
        List<NewsDomain.News> newsList = new ArrayList<>();
        NewsDomain.News news = new NewsDomain.News();
        try {
            news.setContent(textFileToString(newsContentPath));
            newsDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        } catch (IOException e) {
            news.setContent("");
            newsDomain.setStatusCode(CommonConstant.SERVER_ERROR);
            logger.debug("获取新闻文件内容出错" + e.getMessage());
        }
        newsList.add(news);
        newsDomain.setNewsList(newsList);
        return newsDomain;
    }


    public String textFileToString(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        //将file文件内容转成字符串
        BufferedReader bf = new BufferedReader(isr);

        String content = "";
        StringBuilder sb = new StringBuilder();
        while (true) {
            content = bf.readLine();
            if (content == null) {
                break;
            }
            sb.append(content).append("\n");
        }
        bf.close();
        return sb.toString();
    }

    public NewsDomain.News setNewsInfo(int newsId, NewsDomain.News news) throws IOException {
        String newsInfoPath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR) + "/new" + newsId + "/info";
        FileInputStream fis = new FileInputStream(newsInfoPath);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        //将file文件内容转成字符串
        BufferedReader bf = new BufferedReader(isr);

        String content = "";
        int count = 0;
        while (true) {
            content = bf.readLine();
            ++count;
            if (content == null) {
                break;
            }
            if (count == 2) {
                news.setTitle(content);
            }

            if (count == 3) {
                String[] tmp = content.split("：");
                news.setAuthor(tmp[1]);
            }

            if (count == 4) {
                news.setTime(content);
            }

            if (count == 5) {
                news.setSummaryContent(content);
            }
        }
        bf.close();
        return news;
    }
    public static String getFirstImgUrl(String s)
    {
        String regex;
        regex = "src=\"(.*?)\"";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(s);
        String src = "http://misc.gbicom.cn/uploads/ueditor/20190314/1552526850804939.png";
        if (ma.find())
        {
            src = ma.group();
//            System.out.println(ma.group());
            src = src.substring(5, src.length() - 1);
        }
        return src;
    }


    @Test
    public void test() throws Exception{
//        NewsDomain newsDomain = getNewsById(7336);
        List<Integer> ids = get4RandomIds(7336);
        for (int id : ids) {
            System.out.println(id);
        }
//        String newsPath = DirManager.getDirPath(CommonConstant.NEWS_FILE_DIR) + "/new" + 117558;
//        System.out.println(getFirstImgUrl(textFileToString(newsPath + "/content")));
    }

    public List<Integer> get4RandomIds(int id){
        List<Integer> ids = new ArrayList<>();
        List<Integer> allIds = getAllNewsId();
        while (ids.size() < 4) {

            int index = (int)(Math.random()*allIds.size());
            if (id != (int)allIds.get(index)) {
                ids.add(allIds.get(index));
                allIds.remove(index);
            }

        }
        return ids;

    }
}
