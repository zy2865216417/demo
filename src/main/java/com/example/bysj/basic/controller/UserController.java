package com.example.bysj.basic.controller;
import com.example.bysj.domain.User;
import com.example.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.util.JSONUtil;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserController {
    @RequestMapping(value = "/user.ctl",method = RequestMethod.POST)
    public JSONObject login(@RequestParam(value = "username",required = false) String username_str,
                            @RequestParam(value = "password",required = false) String password_str) {
        User user = null;
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            //调用登录方法
            user = UserService.getInstance().login(username_str,password_str);
            String user_json = JSONObject.toJSONString(user);
            JSONObject jsonObject = JSONObject.parseObject(user_json);
            //加入数据信息
            message.put("message", "登录成功");
            return jsonObject;
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message;
        }catch (Exception e){
            message.put("message","网络异常");
            e.getMessage();
            return message;
        }
    }
    @RequestMapping(value = "/user.ctl",method = RequestMethod.GET)
    public String list(@RequestParam(value = "id",required = false) String id_str,
                       @RequestParam(value = "username",required = false) String username_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            if(id_str!=null){
                //转化int
                int id = Integer.parseInt(id_str);
                //调用按照ID查找的方法
                return responseById(id);
            }else {
                //调用按照用户名查找的方法
                return responseByUsername(username_str);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toString();
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            return message.toString();
        }
    }

    public String responseById(int id) throws SQLException, ClassNotFoundException{
        User user = UserService.getInstance().find(id);
        //将对象转化为json字串
        String users_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        return users_json;
    }
    public String responseByUsername(String username)throws SQLException, ClassNotFoundException{
        User user = UserService.getInstance().findByUsername(username);
        //将对象转化为json字串
        String users_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        return users_json;
    }
    @RequestMapping(value = "/user.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        User passwordTochange = JSON.parseObject(user_json, User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改User对象对应的记录
        try {
            UserService.getInstance().changePassword(passwordTochange);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        return message;
    }
}
