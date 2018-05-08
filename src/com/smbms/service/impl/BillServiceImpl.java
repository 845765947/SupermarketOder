package com.smbms.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.dao.BillMapper;
import com.smbms.service.BillService;

@Service
public class BillServiceImpl implements BillService {

	@Resource
	private BillMapper dao;

	@Override
	public boolean deleteBill(Integer providerId) {
		return dao.deleteBill(providerId) > 0 ? true : false;
	}

}
