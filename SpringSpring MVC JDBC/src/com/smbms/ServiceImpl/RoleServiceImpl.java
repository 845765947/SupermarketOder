package com.smbms.ServiceImpl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.Service.RoleService;
import com.smbms.dao.BaseDao;
import com.smbms.dao.role.RoleDao;
import com.smbms.pojo.Role;

@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleDao roleDao;

	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Role> roleList = null;
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			BaseDao.closeResource(connection, null, null);
		}
		return roleList;
	}

}
