package com.example.bysj.service;


import com.example.bysj.dao.TeacherDao;
import com.example.bysj.domain.Teacher;

import java.sql.SQLException;
import java.util.Collection;

public final class TeacherService {
	private static TeacherDao teacherDao= TeacherDao.getInstance();
	private static TeacherService teacherService=new TeacherService();
	private TeacherService(){}
	
	public static TeacherService getInstance(){
		return teacherService;
	}
	
	public Collection<Teacher> findAll() throws SQLException, ClassNotFoundException {
		return teacherDao.findAll();
	}
	
	public Teacher find(Integer id) throws SQLException, ClassNotFoundException {
		return teacherDao.find(id);
	}
	
	public boolean update(Teacher teacher) throws SQLException {
		return teacherDao.update(teacher);
	}
	
	public boolean add(Teacher teacher) throws SQLException {
		return teacherDao.add(teacher);
	}

	public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
		Teacher teacher = this.find(id);
		return teacherDao.delete(teacher);
	}
	
	public boolean delete(Teacher teacher) throws SQLException {
		//如果该教师有关联的课题，不能删除
		if(teacher.getProjects().size()>0){
			return false;
		}
		return teacherDao.delete(teacher);
	}	
}
