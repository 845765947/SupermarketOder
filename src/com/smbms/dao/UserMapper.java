package com.smbms.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.smbms.pojo.User;

public interface UserMapper {
	/**
	 * 通过userCode获取User
	 * 
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public User getLoginUser(@Param("userCode") String userCode);

	/**
	 * 获取用户列表
	 */
	/**
	 * 通过条件查询-userList
	 * 
	 * @param connection
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(@Param("queryname") String userName,
			@Param("queryUserRole") Integer userRole,
			@Param("currentPageNo") Integer currentPageNo,
			@Param("pageSize") Integer pageSize);

	/**
	 * 通过条件查询-用户表记录数
	 * 
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public int getUserCount(@Param("userName") String userName,
			@Param("userRole") int userRole);

	/**
	 * 根据ID查找user
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(@Param("id") String id);

	/**
	 * 增加用户信息
	 * 
	 * @param connection
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int add(User user);

	/**
	 * 修改当前用户密码
	 * 
	 * @param connection
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public int updatePwd(@Param("id") int id, @Param("pwd") String pwd);

	/**
	 * 修改用户信息
	 * 
	 * @param connection
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int modify(User user);

	/**
	 * 通过userId删除user
	 * 
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	public int deleteUserById(@Param("id") Integer delId);

}
