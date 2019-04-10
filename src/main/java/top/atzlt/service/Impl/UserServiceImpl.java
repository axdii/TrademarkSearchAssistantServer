package top.atzlt.service.Impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.atzlt.cons.CommonConstant;
import top.atzlt.dao.UserDao;
import top.atzlt.domain.InteractionDomain;
import top.atzlt.domain.User;
import top.atzlt.service.UserService;
import top.atzlt.web.UserSessionManager;

import java.util.UUID;

@Service
public class
UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(final User user) {
        if (user == null) {
            return null;
        }

        if (userDao.isExist(user)) {
            user.setStatusCode(CommonConstant.USER_EXISTS);//如果用户已存在，返回错误码
            return user;
        }

        try {
            int userId = userDao.getLastUserId() + 1;
            user.setUserId(userId);
            userDao.addUser(user);
        } catch (Exception e) {
            user.setStatusCode(CommonConstant.SERVER_ERROR);//如果插入出错，返回错误码
            return user;
        }

        //注册成功
        user.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        return user;
    }

    public User login(final User user) {

        if (user == null) {
            return null;
        }

        //如果用户不存在
        if (!userDao.isExist(user)) {
            user.setStatusCode(CommonConstant.USER_NOT_EXISTS);
            return user;
        }

        User realUser = userDao.getUserByPhone(user.getUserPhone());

        if (realUser == null) {//如果寻到出现错误，设置错误码

            user.setStatusCode(CommonConstant.SERVER_ERROR);
            return user;

        }

        //如果密码不正确
        if (!realUser.getUserPassword().equals(user.getUserPassword())) {
            user.setStatusCode(CommonConstant.PASSWORD_WRONG);
            return user;
        }

        //登陆成功
        realUser.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        UUID sessionId = UserSessionManager.addSession(realUser.getUserPhone());
        realUser.setSessionID(sessionId);
        return realUser;
    }

    //登出，删除sessionId
    public void logout(User user) {

        UserSessionManager.removeSessionId(user.getUserPhone());

    }

    public InteractionDomain interaction(InteractionDomain interactionDomain) {

        String statusCode = UserSessionManager.updateSessionStatus(interactionDomain.getUserPhone(), interactionDomain.getSessionID());

        //如果连接超时或中断，移除sessionId
        if (statusCode.equals(CommonConstant.LINKAGE_INTERRUPT)) {
            UserSessionManager.removeSessionId(interactionDomain.getUserPhone());
        }

        interactionDomain.setStatusCode(statusCode);
        return interactionDomain;

    }

    public User updateUserInfo(User user) {

        boolean isUpdateSuccess = userDao.updateUser(user);
        User userTmp = userDao.getUserById(user.getUserId());
        Gson gson = new Gson();
        if (isUpdateSuccess) {
            user.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        } else if (userTmp == null) {
            user.setStatusCode(CommonConstant.UPDATE_FAULT);
        } else {
            String oldUserJsonString = gson.toJson(userTmp);//更新之后从数据库获取的
            String newUserJsonbString = gson.toJson(user);//从客户端获取的
            if (oldUserJsonString.equals(newUserJsonbString)) {
                user.setStatusCode(CommonConstant.OPERATE_SUCCESS);
            } else {
                user.setStatusCode(CommonConstant.UPDATE_FAULT);
            }
        }
        return user;

    }

}
