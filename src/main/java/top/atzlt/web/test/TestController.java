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



}
