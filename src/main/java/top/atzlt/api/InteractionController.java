package top.atzlt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.InteractionDomain;
import top.atzlt.service.UserService;

import javax.servlet.http.HttpServletRequest;


/**
 * 进行交互，每隔30秒发送一次，保持在线状态
 */
@RestController
public class InteractionController extends BaseController{

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //保持在线连接，每30秒发送一个该请求
    @RequestMapping(value = "/interaction", method = RequestMethod.POST)
    public InteractionDomain interactionAPI(HttpServletRequest request) {

        InteractionDomain interactionDomain = (InteractionDomain) getObjectFromRequest(request, InteractionDomain.class);
        return userService.interaction(interactionDomain);

    }



}
