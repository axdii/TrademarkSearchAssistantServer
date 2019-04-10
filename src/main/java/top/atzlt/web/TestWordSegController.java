package top.atzlt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import top.atzlt.service.HMIService;

@Controller
public class TestWordSegController {


    private HMIService hmiService;

    @Autowired
    public void setHmiService(HMIService hmiService) {
        this.hmiService = hmiService;
    }


    @RequestMapping(value = "/segTest.do", method = RequestMethod.POST)
    public ModelAndView segTest(String rawText) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("testWordSeg");
        modelAndView.addObject("rawText", rawText);
        modelAndView.addObject("result",hmiService.segText(rawText));
        return modelAndView;
    }

    @RequestMapping(value = "/segTest.do", method = RequestMethod.GET)
    public String segTest(){
        return "testWordSeg";
    }



}
