package com.example.bysj.basic.controller;
import com.example.bysj.domain.Teacher;
import com.example.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.util.JSONUtil;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class TeacherController {
    //Post http://49.235.35.115:8080/teacher.ctl
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象,以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //向数据库中添加Teacher类型的对象
            TeacherService.getInstance().add(teacherToAdd);
            //加入数据信息
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        return message;
    }
    // Delete http://49.235.35.115:8080/teacher.ctl?id=
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.DELETE)
    public String delete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //转化为int类型你
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //到数据库中删除对应的老师
            TeacherService.getInstance().delete(id);
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
    //Put http://49.235.35.115:8080/teacher.ctl
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher象
        Teacher teacherToUpdate = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加加Teacher对象
            TeacherService.getInstance().update(teacherToUpdate);
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
    //Get http://49.235.35.115:8080/teacher.ctl
    //    http://49.235.35.115:8080/teacher.ctl?id=
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.GET)
    public String list(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有teacher的方法来响应
                return responseTeachers();
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的teacher的方法来响应
                return responseTeacher(id);
            }
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
    //响应一个老师对象
    private String responseTeacher(int id)
            throws SQLException,ClassNotFoundException {
        //根据id查找老师
        Teacher teacher = TeacherService.getInstance().find(id);
        //把json类型转化为字符串
        String teacher_json = JSON.toJSONString(teacher, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        //响应message到前端
        return teacher_json;
    }
    //响应所有老师对象
    private String responseTeachers()
            throws SQLException, ClassNotFoundException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        //将对象转化为json格式
        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        //响应message到前端
        return teachers_json;
    }
}
