package top.atzlt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.User;
import top.atzlt.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginAndLogoutController extends BaseController{

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //登陆交互
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User loginAPI(HttpServletRequest request) {

        //待添加检查requestUser为空的情况
        User requestUser = (User) getObjectFromRequest(request, User.class);
        return userService.login(requestUser);

    }

    //登出交互
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public User logoutAPI(HttpServletRequest request) {

        User requestUser = (User) getObjectFromRequest(request, User.class);
        userService.logout(requestUser);
        requestUser.setStatusCode(CommonConstant.USER_LOGOUT);
        return requestUser;

    }

}
