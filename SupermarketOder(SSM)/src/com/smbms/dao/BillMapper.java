package com.smbms.dao;

import org.apache.ibatis.annotations.Param;

public interface BillMapper {
	/**
	 * 根据供应商Id删除订单
	 * 
	 * @param id
	 * @return
	 */
	public int deleteBill(@Param("providerId") Integer providerId);

}
