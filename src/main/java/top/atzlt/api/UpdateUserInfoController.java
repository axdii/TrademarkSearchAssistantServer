package top.atzlt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.domain.User;
import top.atzlt.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UpdateUserInfoController extends BaseController{

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public User updateUserInfo(HttpServletRequest request) {

        User requestUser = (User) getObjectFromRequest(request, User.class);
        return userService.updateUserInfo(requestUser);

    }


}
