package com.smbms.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.dao.UserMapper;
import com.smbms.pojo.User;
import com.smbms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userdao;

	/**
	 * 登录方法
	 */
	@Override
	public User getLoginUser(String userCode, String pwd) {
		User user = null;
		try {
			user = userdao.getLoginUser(userCode);
			if (!(user.getUserPassword().equals(pwd))) {
				user = null;
			}
		} catch (Exception e) {
		}

		return user;
	}

	@Override
	public List<User> getUserList(String userName, Integer userRole,
			Integer currentPageNo, Integer pageSize) {
		return userdao.getUserList(userName, userRole, currentPageNo, pageSize);
	}

	@Override
	public int getUserCount(String userName, Integer userRole) {
		return userdao.getUserCount(userName, userRole);
	}

	@Override
	public User getUserById(String id) {
		return userdao.getUserById(id);
	}

	@Override
	public int add(User user) {
		return userdao.add(user);
	}

	@Override
	public boolean updatePwd(int id, String pwd) {
		boolean fig = userdao.updatePwd(id, pwd) > 0 ? true : false;
		return fig;
	}

	@Override
	public boolean modify(User user) {
		boolean fig = userdao.modify(user) > 0 ? true : false;
		return fig;
	}

	@Override
	public User getLoginUser(String userCode) {
		return userdao.getLoginUser(userCode);
	}

	@Override
	public boolean deleteUserById(Integer delId) {
		return userdao.deleteUserById(delId) > 0 ? true : false;
	}

}
