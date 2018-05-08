package com.smbms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.dao.ProviderMapper;
import com.smbms.pojo.Provider;
import com.smbms.pojo.User;
import com.smbms.service.ProviderService;

@Service
public class ProviderServiceImpl implements ProviderService {

	@Resource
	private ProviderMapper providerdao;

	@Override
	public int getProviderCount(String queryProCode, String queryProName) {
		return providerdao.getProviderCount(queryProCode, queryProName);
	}

	@Override
	public List<Provider> getProviderList(String providerName,
			String queryUserRole, Integer currentPageNo, Integer pageSize) {
		return providerdao.getProviderList(providerName, queryUserRole,
				currentPageNo, pageSize);
	}

	@Override
	public Provider selectId(String providerid) {
		return providerdao.selectId(providerid);
	}

	@Override
	public boolean addProvider(Provider provider) {
		return providerdao.addProvider(provider);
	}

	@Override
	public boolean updateProvider(Provider provider) {
		return providerdao.updateProvider(provider) > 0 ? true : false;
	}

	@Override
	public boolean deleteProvider(Integer proid) {
		return providerdao.deleteProvider(proid) > 0 ? true : false;
	}

}
