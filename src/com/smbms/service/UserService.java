package com.smbms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.smbms.pojo.User;

@Service
public interface UserService {
	public User getLoginUser(String userCode, String pwd);

	public List<User> getUserList(String userName, Integer userRole,
			Integer currentPageNo, Integer pageSize);

	/**
	 * 通过条件查询-用户表记录数
	 * 
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public int getUserCount(String userName, Integer userRole);

	/**
	 * 根据ID查找user
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(String id);

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
	 * 通过userCode获取User
	 * 
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public User getLoginUser(String userCode);

	/**
	 * 修改当前用户密码
	 * 
	 * @param connection
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public boolean updatePwd(int id, String pwd);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean modify(User user);

	/**
	 * 通过userId删除user
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUserById(Integer userid);

}
