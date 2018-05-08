package com.smbms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.smbms.pojo.Provider;
import com.smbms.pojo.User;

public interface ProviderService {
	/**
	 * 通过条件查询-供应商表记录数
	 * 
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public int getProviderCount(String queryProCode, String queryUserName);

	/**
	 * 获取供应商列表
	 */
	/**
	 * 
	 * @param connection
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public List<Provider> getProviderList(String providerName,
			String queryUserRole, Integer currentPageNo, Integer pageSize);

	/**
	 * 根据id获取Provider
	 */
	public Provider selectId(String providerid);

	/**
	 * 添加供应商信息
	 */
	public boolean addProvider(Provider provider);

	/**
	 * 根据id更新供应商信息
	 */
	public boolean updateProvider(Provider provider);

	/**
	 * 根据id删除供应商
	 */
	public boolean deleteProvider(Integer proid);
}
