package top.atzlt.api;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.cons.CommonConstant;
import top.atzlt.domain.BaseDomain;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class BaseController {

    //从请求中获取对象
    Object getObjectFromRequest(HttpServletRequest request, Class clazz) {
        try {
            //设置解码为UTF-8，不然中文会乱码
            request.setCharacterEncoding("UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
        String jsonString = (String)request.getParameter(CommonConstant.JSON_STRING);
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);

    }

    boolean objectIsExists(BaseDomain baseDomain) {
        return baseDomain != null && baseDomain.getUserPhone() != null && !baseDomain.getUserPhone().equals("");
    }

}
