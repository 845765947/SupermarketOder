package com.smbms.dao.role;

import java.sql.Connection;
import java.util.List;

import com.smbms.pojo.Role;

public interface RoleDao {
	
	public List<Role> getRoleList(Connection connection)throws Exception;

}
