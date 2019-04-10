package top.atzlt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.NewsDomain;
import top.atzlt.service.NewsPickerService;
import top.atzlt.web.DirManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class NewsController extends BaseController{

    private NewsPickerService newsPickerService;

    @Autowired
    public void setNewsPickerService(NewsPickerService newsPickerService) {
        this.newsPickerService = newsPickerService;
    }

    @RequestMapping(value = "/getTopTenNews", method = RequestMethod.GET)
    public NewsDomain getTopTenNews(HttpServletRequest request, HttpServletResponse response) {
        return newsPickerService.getTopTenLastNews();
    }

    @RequestMapping(value = "/getTopTenSimpleNews", method = RequestMethod.GET)
    public NewsDomain getTopTenSimpleNews(HttpServletRequest request, HttpServletResponse response) {
        return newsPickerService.getTopTenLastSimpleNews();
    }


    @RequestMapping(value = "/getLastNewsFromOldNews", method = RequestMethod.POST)
    public NewsDomain getLastNewsFromOldNews(HttpServletRequest request, HttpServletResponse response) {
        NewsDomain newsDomain = (NewsDomain) getObjectFromRequest(request, NewsDomain.class);
        return newsPickerService.getLastNewsFromOldNewsId(newsDomain.getOldId());
    }

    @RequestMapping(value = "/getOldNewsFromOldNews", method = RequestMethod.POST)
    public NewsDomain getOldNewsFromOldNews(HttpServletRequest request, HttpServletResponse response) {
        NewsDomain newsDomain = (NewsDomain) getObjectFromRequest(request, NewsDomain.class);
        return newsPickerService.getOldNewsFromOldNewsId(newsDomain.getOldId());
    }

    @RequestMapping(value = "/getLastNews", method = RequestMethod.GET)
    public NewsDomain getLastNews(HttpServletRequest request, HttpServletResponse response) {
        return newsPickerService.getLastNews();
    }

    @RequestMapping(value = "/getNewsById", method = RequestMethod.POST)
    public NewsDomain getNewsById(HttpServletRequest request, HttpServletResponse response) {

        NewsDomain requestDomain = (NewsDomain) getObjectFromRequest(request, NewsDomain.class);
        return newsPickerService.getNewsById(requestDomain.getOldId());

    }

    @RequestMapping(value = "/getNewsByIds", method = RequestMethod.POST)
    public NewsDomain getNewsByIds(HttpServletRequest request, HttpServletResponse response) {
        NewsDomain requestDomain = (NewsDomain) getObjectFromRequest(request, NewsDomain.class);
        return newsPickerService.getNewsSimpleInfoFromNewsIdList(requestDomain.getRequestIds());
    }





}
