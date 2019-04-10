package top.atzlt.web.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import top.atzlt.dao.UserDao;
import top.atzlt.domain.TrademarkDomain;
import top.atzlt.domain.User;
import top.atzlt.service.TrademarkService;
import top.atzlt.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    private UserService userService;
    private TrademarkService trademarkService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrademarkService(TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
    }

    @RequestMapping(value = "setData")
    public String setData(){

//        trademarkService.initServerData();

        return "setData";
    }

    @RequestMapping(value = "test3")
    public ModelAndView test3(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test3");
        TrademarkDomain trademarkDomain = new TrademarkDomain();
        trademarkDomain.setTrademarkId(2);

        modelAndView.addObject("trademark", trademarkService.getTrademarkDomainById(trademarkDomain));
        return modelAndView;

    }


    @RequestMapping(value = "test")
    public String test() {
        return "test";
    }

    @RequestMapping(value = "test2")
    public ModelAndView test2() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test2");

        User user = new User();
        user.setUserName("admin");
        user.setUserPassword("admin");
        User returnUser = userService.login(user);
        modelAndView.addObject("user", returnUser);


        return modelAndView;
    }

    @RequestMapping(value = "/trademarkImg", method = RequestMethod.GET)
    public ModelAndView trademarkImg(@RequestParam(value = "trademarkId") String trademarkId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //模拟文件，myfile.txt为需要下载的文件
        String path = request.getSession().getServletContext().getRealPath("WEB-INF/img")+"/"+ trademarkId + ".JPG";
        //获取输入流
//       InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码
        trademarkId = URLEncoder.encode(trademarkId,"UTF-8");

        System.out.println("path = " + path + "\n" + "trademark = " + trademarkId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test4");
        modelAndView.addObject("str", path);
        return modelAndView;

        //设置文件下载头
//        response.addHeader("Content-Disposition", "attachment;filename=" + trademarkId);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//        response.setContentType("multipart/form-data");
//        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//        int len = 0;
//        while((len = bis.read()) != -1){
//            out.write(len);
//            out.flush();
//        }
//        out.close();
    }




}
