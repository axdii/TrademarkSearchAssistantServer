package top.atzlt.web.test;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.User;
import top.atzlt.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class TestAPIController {

    private UserService userService;

    @Autowired

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/test3")
    public void test3(HttpServletResponse response) {

        User user = new User();
        user.setUserName("admin");
        user.setUserPassword("admin");
        response.setContentType(CommonConstant.JSON_TYPE);
        PrintWriter out = null;
        User returnUser;
        try {
            out = response.getWriter();
            returnUser = userService.login(user);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                out.flush();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    @RequestMapping(value = "/test4")
    public User test4(){
        User user = new User();
        user.setUserId(1);
        user.setUserName("admin");
        user.setUserPassword("admin");
        return userService.login(user);
    }

    @RequestMapping(value = "/test5")
    public User test5(){
        String jsonStr = "{\"statusCode\":\"OPERATE_SUCCESS\",\"userName\":\"admin\",\"sessionID\":\"319b2cd0-d030-495a-8a77-67610b8c3c6f\",\"userId\":1,\"userPassword\":\"admin\",\"userRealName\":null,\"userPhone\":null,\"userEmail\":null,\"userAddress\":null,\"userType\":1,\"userLocked\":0}";
        Gson gson = new Gson();
        User user = gson.fromJson(jsonStr, User.class);
        if (user.getUserAddress() == null) {
            System.out.println("null");
            user.setStatusCode("null");
        } else {
            System.out.println("not null:" + user.getUserAddress());
            user.setStatusCode("nut null:" + user.getUserAddress());
        }
        return user;
    }
}
