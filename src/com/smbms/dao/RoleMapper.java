package com.smbms.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.smbms.pojo.Role;

public interface RoleMapper {
	
	public List<Role> getRoleList();

}
