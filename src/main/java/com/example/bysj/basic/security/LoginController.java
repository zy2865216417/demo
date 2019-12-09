package com.example.bysj.basic.security;
import com.example.bysj.domain.User;
import com.example.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class LoginController extends HttpServlet {
    @RequestMapping(value = "/login.ctl",method = RequestMethod.GET)
    protected String get(@RequestParam(value = "username",required = false) String username, String password, HttpServletRequest request){
        //创建json对象message，以便王前端响应数据
        JSONObject message = new JSONObject();
        try{
            //定义User对象
            User loggedUser = UserService.getInstance().login(username,password);
            if(loggedUser != null){
                message.put("message","登录成功");
                HttpSession session = request.getSession();
                //十分钟没有操作，则使session失效
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
            }else {
                message.put("message","用户名或密码错误");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.getMessage();
        }
        return message.toString();
    }
}
