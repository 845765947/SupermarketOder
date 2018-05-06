package com.smbms.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.smbms.Service.UserService;
import com.smbms.ServiceImpl.UserServiceImpl;
import com.smbms.dao.BaseDao;
import com.smbms.pojo.User;

import java.sql.Connection;
import java.util.List;

public class SjTest {
	UserService service = new UserServiceImpl();

	// 上机一
	@Test
	public void Addtest() {
		User user = new User();
		user.setUserName("张三");
		boolean count = service.add(user);
		System.out.println(count);
	}
}
