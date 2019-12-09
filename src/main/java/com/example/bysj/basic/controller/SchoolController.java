package com.example.bysj.basic.controller;

import com.example.bysj.domain.School;
import com.example.bysj.service.SchoolService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.util.JSONUtil;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

// http://49.235.35.115:8080/bysj1845/myschool/school
@RestController
public class SchoolController {
    // Post http://49.235.35.115:8080/school.ctl  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/addCollege  前台
    @RequestMapping(value = "/school.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException{
        JSONObject message = new JSONObject();
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        try {
            //增加School对象
            boolean result = SchoolService.getInstance().add(schoolToAdd);
            if(result){
                //加入数据信息
                message.put("message", "增加成功");
            }else {
                //加入数据信息
                message.put("message", "增加失败");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        return message;
    }
    // Delete http://49.235.35.115:8080/school.ctl?id=26  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/school?id=8  前台
    @RequestMapping(value = "/school.ctl",method = RequestMethod.DELETE)
    public String delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            int id = Integer.parseInt(id_str);
            //删除学院
            SchoolService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
            return message.toString();
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            return message.toString();
        }
    }

    //Put http://49.235.35.115:8080/school.ctl  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/updateCollege  前端
    @RequestMapping(value = "/school.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库表修改School对象对应的记录
            SchoolService.getInstance().update(schoolToAdd);
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
    //Get http://49.235.35.115:8080/school.ctl  后台
    //    http://49.235.35.115:8080/school.ctl?id=8 后台
    //  http://49.235.35.115:8080/bysj1845/myschool/school
    //  http://49.235.35.115:8080/bysj1845/myschool/detail/
    @RequestMapping(value = "/school.ctl",method = RequestMethod.GET)
    public String list(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有school的方法来响应
                 return responseSchools();
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的school的方法来响应
                return responseSchool(id);
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
    //  http://49.235.35.115:8080/bysj1845/myschool/school前台
    //响应一个学院对象
    private String responseSchool(int id)
            throws SQLException,ClassNotFoundException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        //把json类型转化为字符串
        String school_json = JSON.toJSONString(school);
        //响应
        //响应message到前端
        return school_json;
    }
    //  http://49.235.35.115:8080/bysj1845/myschool/detail/
    //响应所有学位对象
    private String responseSchools()
            throws SQLException, IOException, ClassNotFoundException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        //将对象转化为json格式
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        return schools_json;
    }
}
