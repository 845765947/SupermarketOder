package com.smbms.dao.Provider;

import java.sql.Connection;
import java.util.List;

import com.smbms.pojo.Provider;

public interface ProviderDao {
	/**
	 * 添加供应商信息
	 */
	public int addProvider(Connection conn, Provider provider);

	/**
	 * 分页供应商列表
	 * 
	 * @param connection
	 * @param proCode
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	public List<Provider> getProviderList(Connection connection,
			String proCode, String proName) throws Exception;

	/**
	 * 根据id查询信息
	 */
	public Provider idSelectProvider(Connection conn, int id);

	/**
	 * 根据id更新
	 */
	public int updateProvider(Connection conn, Provider provider);
}
