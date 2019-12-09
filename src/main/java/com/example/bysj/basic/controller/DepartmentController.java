package com.example.bysj.basic.controller;
import com.example.bysj.domain.Department;
import com.example.bysj.service.DepartmentService;
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
public class DepartmentController {
    //Post http://49.235.35.115:8080/department.ctl
    @RequestMapping(value = "/department.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象resp,以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //向数据库中添加Department类型的对象
            DepartmentService.getInstance().add(departmentToAdd);
            //加入数据信息
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应到前端
        return message;
    }
    //Delete http://49.235.35.115:8080/department.ctl?id=
    @RequestMapping(value = "/department.ctl",method = RequestMethod.DELETE)
    public String delete(@RequestParam(value = "id",required = false) String id_str){
        //转化为int类型
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //到数据库中删除对应的学位
            DepartmentService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应到前端
        return message.toString();
    }
    //Put http://49.235.35.115:8080/department.ctl
    @RequestMapping(value = "/department.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToUpdate = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加加Department对象
            DepartmentService.getInstance().update(departmentToUpdate);
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
    //Get http://49.235.35.115:8080/department.ctl
    //    http://49.235.35.115:8080/department.ctl?id=
    @RequestMapping(value = "/department.ctl",method = RequestMethod.GET)
    public String list(@RequestParam(value = "id",required = false) String id_str,
                       @RequestParam(value = "paraType",required = false) String paraType_id) {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //判断写入的paraType是否为空，如果为空，则判断id
            if(paraType_id == null){
                //如果id = null, 表示响应所有专业对象，否则响应id指定的专业对象
                if (id_str == null) {
                    //调用返回所有department的方法来响应
                    return responseDepartments();
                } else {
                    //转化int
                    int id = Integer.parseInt(id_str);
                    //调用返回指定ID的department的方法来响应
                    return responseDepartment(id);
                }
            }else{
                //如果不为空，判断传入的数据是否为school
                if(paraType_id.contains("school")){
                    //转化int
                    int id = Integer.parseInt(id_str);
                    //调用根据学院来查找系的方法
                    return responseDepSchool(id);
                }
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
        return message.toString();
    }

    //根据学院的id查找department对象
    private String responseDepSchool(int id) throws SQLException, ClassNotFoundException {
        //根据由学院id返回department数组
        Collection<Department> department = DepartmentService.getInstance().findAllBySchool(id);
        String degrees_json = JSON.toJSONString(department, SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;

    }
    //响应一个专业对象
    private String  responseDepartment(int id)
            throws SQLException, ClassNotFoundException {
        //根据id查找专业
        Department department = DepartmentService.getInstance().find(id);
        //将对象转化为json字串
        String department_json = JSON.toJSONString(department, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        //响应message到前端
        return department_json;
    }

    //响应所有专业对象
    private String responseDepartments()
            throws SQLException, ClassNotFoundException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        //将对象转化为json字串
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        return departments_json;
    }
}
