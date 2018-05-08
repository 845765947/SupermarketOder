package com.smbms.service;

import org.apache.ibatis.annotations.Param;

public interface BillService {
	/**
	 * 根据供应商Id删除订单
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteBill(@Param("providerId") Integer providerId);
}
