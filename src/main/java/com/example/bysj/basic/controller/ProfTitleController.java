package com.example.bysj.basic.controller;
import com.example.bysj.domain.ProfTitle;
import com.example.bysj.service.ProfTitleService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.util.JSONUtil;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class ProfTitleController {
    //Post http://49.235.35.115:8080/profTitle.ctl
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().add(profTitleToAdd);
            //加入数据信息
            resp.put("message", "增加成功");
        } catch (SQLException e) {
            resp.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            resp.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        return resp;
    }

    //Delete http://49.235.35.115:8080/profTitle.ctl?id=8
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.DELETE)
    public String delete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //删除学院
            int id = Integer.parseInt(id_str);
            ProfTitleService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        return message.toString();
    }
    //Put http://49.235.35.115:8080/profTitle.ctl
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库profTitle对象对应的记录
            ProfTitleService.getInstance().update(profTitleToAdd);
            //加入数据信息
            message.put("message", "修改成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }
    //Get http://49.235.35.115:8080/profTitle.ctl
    //    http://49.235.35.115:8080/profTitle.ctl?id=5
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.GET)
    public String list(@RequestParam(value = "id",required = false) String id_str) throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有profTitle的方法来响应
                return responseProfTitles();
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的profTitle的方法来响应
                return responseProfTitle(id);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        }
    }

    //响应一个头衔对象
    private String responseProfTitle(int id)
            throws SQLException, ClassNotFoundException {
        //根据id查找title
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        //把json类型转化为字符串
        String profTitle_json = JSON.toJSONString(profTitle);
        //响应message到前端
        return profTitle_json;
    }

    //响应所有学位对象
    private String responseProfTitles()
            throws SQLException, ClassNotFoundException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        //把json类型转化为字符串
        String profTitles_json = JSON.toJSONString(profTitles);
        //响应message到前端
        return profTitles_json;
    }
}
