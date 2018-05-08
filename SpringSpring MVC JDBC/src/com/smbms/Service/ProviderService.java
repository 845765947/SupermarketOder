package com.smbms.Service;

import java.util.List;

import com.smbms.pojo.Provider;

public interface ProviderService {
	/**
	 * 添加供应商信息
	 */
	public boolean addProvider(Provider provider);

	/**
	 * 分页供应商列表
	 * 
	 * @param connection
	 * @param proCode
	 * @param proName
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Provider> getProviderList(String proCode, String proName);

	/**
	 * 根据id查询信息
	 */
	Provider idSelectProvider(int id);

	/**
	 * 根据id更新
	 */
	public boolean updateProvider(Provider provider);
}
