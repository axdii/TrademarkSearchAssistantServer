package top.atzlt.web;

import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 用用户名保持会话状态
 * 用SessionId保持连接状态
 * 当用户登陆在多台设备同时登陆一个账号时，sessionId会对将较先登陆的设备使之登出的功能有帮助
 * 应当默认服务器端的sessionID为最新值，当出现同账号不同session值时，应当告知客户端该应用已在别的设备重新登陆
 * 登陆最具优先权，更改一切sessionID
 * UUID比较用equal()
 *
 *
 * 用户有登陆、交互、登出等三种动作
 * 登陆又可分为普通登陆、在别的设备登陆（例如某一时刻，已在A设备中登陆了，并且处于会话期间，然后又在B设备中登陆，然后A交互时发现sessionId不相同）
 * 即交互时会有两种情况，第一种正常交互，第二种，交互时发现sessionId不相同了
 */
public class UserSessionManager {

    private static Map<String, UserSession> sessionMap = new HashMap<String, UserSession>();


    //-----交互时使用
    public static String updateSessionStatus(String userPhone, UUID clientSessionID) {

        //如果session相同，即没有发生二次登陆的情况
        if (isSameSessionId(userPhone, clientSessionID)) {

            //更新时间
            boolean updateSuccess = updateSessionTime(userPhone);

            //如果更新成功，即没有超时
            if (updateSuccess) {

                return CommonConstant.OPERATE_SUCCESS;

            } else {

                //发生超时
                return CommonConstant.LINKAGE_INTERRUPT;

            }

        } else {
            return CommonConstant.LOGIN_IN_NEW_DEVICE;
        }


    }

    //除去sessionId，----登出时使用
    public static void removeSessionId(String userPhone) {

        UserSession userSession = sessionMap.get(userPhone);

        //没有userSession的话不做处理
        if (userSession == null) {
            return;
        }

        //有的话设置为空
        userSession.setSessionId(null);
    }


    //当用户没有登陆时，创建新的，登陆时调用，----登陆时使用
    public static UUID addSession(String userPhone) {

        UserSession session = sessionMap.get(userPhone);

        //session不存在时
        if (session == null) {
            session = new UserSession();
        }

        UUID sessionId = UUID.randomUUID();
        session.setSessionId(sessionId);
        session.setLastConnectTime(System.currentTimeMillis());
        sessionMap.put(userPhone, session);
        return sessionId;

    }




    private static boolean isSessionIdExists(String userPhone) {

        UserSession userSession = sessionMap.get(userPhone);
        return userSession != null && userSession.getSessionId() != null;

    }

    //是否连接断开（是否大于5分钟）
    private static boolean isLinkageInterrupt(String userPhone) {

        UserSession userSession = sessionMap.get(userPhone);
        return System.currentTimeMillis() - userSession.getLastConnectTime() > 20000;

    }

    //该方法返回 false 时，代表账号在别的设备中登陆，要在该设备登出
    private static boolean isSameSessionId(String userPhone, UUID clientSessionId) {

        UserSession userSession = sessionMap.get(userPhone);
        return userSession != null && userSession.getSessionId().equals(clientSessionId);//为空或sessionId不同

    }

    private static boolean updateSessionTime(String userPhone) {

        //如果sessionId存在，且没过时,则更新连接时间
        if (isSessionIdExists(userPhone) && !isLinkageInterrupt(userPhone)) {

            UserSession userSession = sessionMap.get(userPhone);
            userSession.setLastConnectTime(System.currentTimeMillis());
            return true;

        }

        //如果sessionID不存在或sessionID过时，返回false，让别人更新
        return false;
    }


}
