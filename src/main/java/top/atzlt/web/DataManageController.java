package top.atzlt.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.TrademarkDomain;
import top.atzlt.domain.TrademarkListDomain;
import top.atzlt.domain.TrademarkTempDomain;
import top.atzlt.service.TrademarkService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;

@Controller
@RequestMapping("/dataManagement")
public class DataManageController {

    private TrademarkService trademarkService;
    @Autowired
    public void setTrademarkService(TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
    }

    @RequestMapping(value = "/addTrademarkDomain.do", method = RequestMethod.GET)
    public String addTrademarkDomain(){
        return "addTrademarkDomain";
    }

    @RequestMapping(value = "/addTrademarkDomain.do", method = RequestMethod.POST)
    public ModelAndView addTrademarkDomain(TrademarkTempDomain trademarkTempDomain, HttpServletRequest request){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showTrademarkDomains");
        boolean sign = false;
        String stateCode = "";
        try {
            sign = trademarkService.addTrademarkTempDomain(trademarkTempDomain);
        } catch (Throwable throwable) {
            stateCode = CommonConstant.ADD_TRADEMARK_DOMAIN_FAULT;
        }

        if (sign) {
            stateCode = CommonConstant.OPERATE_SUCCESS;
        }

        List<TrademarkListDomain> trademarkListDomains = trademarkService.getTrademarkListDomain();


        modelAndView.addObject("stateCode", stateCode);
        modelAndView.addObject("trademarkTempDomain", trademarkTempDomain);
        modelAndView.addObject("trademarkListDomains", trademarkListDomains);


        return modelAndView;

    }

    @RequestMapping(value = "showTrademarkDomains", method = RequestMethod.GET)
    public ModelAndView showTrademarkDomains(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showTrademarkDomains");

        List<TrademarkListDomain> trademarkListDomains = trademarkService.getTrademarkListDomain();
        modelAndView.addObject("trademarkListDomains", trademarkListDomains);

        return modelAndView;
    }

    @RequestMapping(value = "showTrademarkDomain.do", method = RequestMethod.GET)
    public ModelAndView showTrademarkDomain(@RequestParam(value = "trademarkId") String trademarkId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showTrademarkDomain");
        TrademarkDomain trademarkDomain = new TrademarkDomain();
        trademarkDomain.setTrademarkId(Integer.parseInt(trademarkId));
        trademarkDomain = trademarkService.getTrademarkDomainById(trademarkDomain);


        String trademarkImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_IMG_DIR) + "/" + trademarkId + ".JPG";
        String trademarkTbImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR) + "/tb" + trademarkId + ".JPG";

        modelAndView.addObject("trademarkDomain", trademarkDomain);
        modelAndView.addObject("trademarkImgPath", trademarkImgPath);
        modelAndView.addObject("trademarkTbImgPath", trademarkTbImgPath);

        return modelAndView;
    }

    @RequestMapping(value = "deleteTrademarkDomain.do", method = RequestMethod.GET)
    public ModelAndView deleteTrademarkDomain(@RequestParam(value = "trademarkId") String trademarkId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deleteResult");
        boolean sign = trademarkService.deleteTrademarkDomainById(Integer.parseInt(trademarkId));
        modelAndView.addObject("sign", sign);
        return modelAndView;
    }


}
