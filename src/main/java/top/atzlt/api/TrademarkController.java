package top.atzlt.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.InitDataDomain;
import top.atzlt.domain.SimilarTrademarkEnquiriesDomain;
import top.atzlt.domain.TrademarkDomain;
import top.atzlt.service.TrademarkService;
import top.atzlt.web.DirManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class TrademarkController extends BaseController{

    private TrademarkService trademarkService;

    @Autowired
    public void setTrademarkService(TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
    }


    @RequestMapping(value = "/initData", method = RequestMethod.GET)
    public InitDataDomain initData(){
        return trademarkService.initData();
    }

    @RequestMapping(value = "/getTrademarkDomainById", method = RequestMethod.POST)
    public TrademarkDomain getTrademarkDomainById(HttpServletRequest request){
        TrademarkDomain requestTrademarkDomain = (TrademarkDomain)getObjectFromRequest(request, TrademarkDomain.class);
        return trademarkService.getTrademarkDomainById(requestTrademarkDomain);
    }


    @RequestMapping(value = "/trademarkImg", method = RequestMethod.GET)
    public void trademarkImg(@RequestParam(value = "trademarkId") String trademarkId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = DirManager.getDirPath(CommonConstant.TRADEMARK_IMG_DIR) + "/" + trademarkId + ".JPG";
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码
        trademarkId = URLEncoder.encode(trademarkId,"UTF-8");

//        System.out.println("path = " + path + "\n" + "trademark = " + trademarkId);
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + trademarkId + ".JPG");
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }

    @RequestMapping(value = "/trademarkTbImg", method = RequestMethod.GET)
    public void trademarkTbImg(@RequestParam(value = "trademarkId") String trademarkId, HttpServletRequest request, HttpServletResponse response) throws IOException{
        String path = DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR ) + "/tb" + trademarkId + ".JPG";
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码
        trademarkId = URLEncoder.encode(trademarkId,"UTF-8");

//        System.out.println("path = " + path + "\n" + "trademark = " + trademarkId);
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=tb" + trademarkId + ".JPG");
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }


    @RequestMapping(value = "/getSimilarTrademarkEnquiriesDomainByImageEnquiries", method = RequestMethod.POST)
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkEnquiriesDomainByImageEnquiries(HttpServletRequest request) {
        SimilarTrademarkEnquiriesDomain requestDomain = (SimilarTrademarkEnquiriesDomain)getObjectFromRequest(request, SimilarTrademarkEnquiriesDomain.class);
        return trademarkService.getSimilarTrademarkListByImageEnquiries(requestDomain);
    }

    @RequestMapping(value = "/getSimilarTrademarkEnquiriesDomainByTextEnquiries", method = RequestMethod.POST)
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkEnquiriesDomainByTextEnquiries(HttpServletRequest request) {
        SimilarTrademarkEnquiriesDomain requestDomain = (SimilarTrademarkEnquiriesDomain) getObjectFromRequest(request, SimilarTrademarkEnquiriesDomain.class);
        return trademarkService.getSimilarTrademarkListByTextEnquiries(requestDomain);
    }


}
