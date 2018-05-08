package com.smbms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.dao.RoleMapper;
import com.smbms.pojo.Role;
import com.smbms.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper roledao;

	@Override
	public List<Role> getRoleList() {
		return roledao.getRoleList();
	}

} 
