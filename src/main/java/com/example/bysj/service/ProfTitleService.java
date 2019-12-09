package com.example.bysj.service;


import com.example.bysj.dao.ProfTitleDao;
import com.example.bysj.domain.ProfTitle;

import java.sql.SQLException;
import java.util.Collection;

public final class ProfTitleService {
	private static ProfTitleDao profTitleDao= ProfTitleDao.getInstance();
	private static ProfTitleService profTitleService=new ProfTitleService();

	public static ProfTitleService getInstance(){
		return profTitleService;
	}

	public Collection<ProfTitle> findAll() throws SQLException, ClassNotFoundException {
		return profTitleDao.findAll();
	}

	public ProfTitle find(Integer id) throws SQLException, ClassNotFoundException {
		return profTitleDao.find(id);
	}

	public boolean update(ProfTitle profTitle) throws SQLException, ClassNotFoundException {
		return profTitleDao.update(profTitle);
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		return profTitleDao.add(profTitle);
	}

	public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
		ProfTitle profTitle = this.find(id);
		return this.delete(profTitle);
	}

	public boolean delete(ProfTitle profTitle) throws SQLException, ClassNotFoundException {
		return profTitleDao.delete(profTitle);
	}
}

