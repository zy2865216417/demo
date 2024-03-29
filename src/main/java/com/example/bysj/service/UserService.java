package com.example.bysj.service;


import com.example.bysj.dao.UserDao;
import com.example.bysj.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers() throws SQLException, ClassNotFoundException {
		return userDao.findAll();
	}

	public User find(Integer id) throws SQLException, ClassNotFoundException {
		return userDao.find(id);
	}
	public User findByUsername(String username) throws SQLException, ClassNotFoundException {
		return userDao.findByUsername(username);
	}
	public boolean changePassword(User user) throws SQLException {
		return userDao.changePassword(user);
	}
	public User getUser(Integer id) throws SQLException, ClassNotFoundException {
		return userDao.find(id);
	}
	
	public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
		userDao.delete(user);
		return userDao.add(user);
	}
	
	public boolean add(User user) throws SQLException {
		return userDao.add(user);
	}

	public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
		return userDao.delete(id);
	}
	
	public boolean delete(User user) throws SQLException, ClassNotFoundException {
		return userDao.delete(user);
	}
	
	
	public User login(String username, String password) throws SQLException, ClassNotFoundException {
		return userDao.login(username,password);
	}	
}
