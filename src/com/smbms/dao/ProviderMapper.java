package com.smbms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.smbms.pojo.Provider;

public interface ProviderMapper {
	/**
	 * 通过条件查询-供应商表记录数
	 * 
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public int getProviderCount(@Param("queryProCode") String queryProCode,
			@Param("queryProName") String queryProName);

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
	public List<Provider> getProviderList(
			@Param("queryProName") String providerName,
			@Param("queryProCode") String providerRole,
			@Param("currentPageNo") Integer currentPageNo,
			@Param("pageSize") Integer pageSize);

	/**
	 * 根据id获取Provider
	 */
	public Provider selectId(@Param("providerid") String providerid);

	/**
	 * 添加供应商信息
	 */
	public boolean addProvider(Provider provider);

	/**
	 * 根据id更新供应商信息
	 */
	public int updateProvider(Provider provider);

	/**
	 * 根据id删除供应商
	 */
	public int deleteProvider(@Param("proid") Integer proid);

}
