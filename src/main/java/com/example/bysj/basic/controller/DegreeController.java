package com.example.bysj.basic.controller;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.bysj.domain.Degree;
import com.example.bysj.service.DegreeService;
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
public class DegreeController {
    //Post http://49.235.35.115:8080/degree.ctl
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加Degree对象
            DegreeService.getInstance().add(degreeToAdd);
            //加入数据信息
            resp.put("message", "增加成功");
        } catch (SQLException e) {
            resp.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            resp.put("message", "网络异常");
            e.printStackTrace();
        }
        return resp;
    }

    //Delete http://49.235.35.115:8080/degree.ctl?id=22
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.DELETE)
    public String delete(@RequestParam(value = "id",required = false) String id_str) {
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            int id = Integer.parseInt(id_str);
            //删除学位
            DegreeService.getInstance().delete(id);
            //加入数据信息
            resp.put("message", "删除成功");
            return resp.toString();
        } catch (SQLException e) {
            resp.put("message", "数据库操作异常");
            e.printStackTrace();
            return resp.toString();
        } catch (Exception e) {
            resp.put("message", "网络异常");
            e.printStackTrace();
            return resp.toString();
        }
    }
    //Put http://49.235.35.115:8080/degree.ctl
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        JSONObject message = new JSONObject();
        try {
            //到数据库表修改degree对象对应的记录
            DegreeService.getInstance().update(degreeToAdd);
            //加入数据信息
            message.put("message", "更新成功");
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

    //Get http://49.235.35.115:8080/degree.ctl
    //    http://49.235.35.115:8080/degree.ctl?id=16
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.GET)
    public String get(@RequestParam(value = "id",required = false) String id_str)
            throws ServletException, IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                return responseDegrees();
            } else {
                int id = Integer.parseInt(id_str);
                return responseDegree(id);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            e.printStackTrace();
            return message.toJSONString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            return message.toJSONString();
        }
    }
    //响应一个学位对象
    private String responseDegree(int id)
            throws ServletException, IOException, SQLException,ClassNotFoundException {
        //根据id查找学位
        Degree degree = DegreeService.getInstance().find(id);
        //把json类型转化为字符串
        String degree_json = JSON.toJSONString(degree);
        return degree_json;

    }
    //响应所有学位对象
    private String responseDegrees()
            throws SQLException,ClassNotFoundException {
        //获得所有学位
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        //把json类型转化为字符串
        String degrees_json = JSON.toJSONString(degrees, SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;
    }
}
