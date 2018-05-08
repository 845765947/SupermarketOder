package com.smbms.ServiceImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smbms.Service.ProviderService;
import com.smbms.dao.BaseDao;
import com.smbms.dao.Provider.ProviderDao;
import com.smbms.pojo.Provider;

@Service
public class ProviderServiceImpl implements ProviderService {

	@Resource  
	private ProviderDao dao;

	@Override
	public boolean addProvider(Provider provider) {
		boolean fig = false;
		Connection conn = null;
		try {
			conn = BaseDao.getConnection();
			fig = dao.addProvider(conn, provider) > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseDao.closeResource(conn, null, null);
		}
		return fig;
	}

	@Override
	public List<Provider> getProviderList(String proCode, String proName) {
		List<Provider> list = new ArrayList<Provider>();
		Connection conn = null;
		try {
			conn = BaseDao.getConnection();
			list = dao.getProviderList(conn, proCode, proName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseDao.closeResource(conn, null, null);
		}
		return list;
	}

	@Override
	public Provider idSelectProvider(int id) {
		Provider provider = new Provider();
		Connection conn = null;
		try {
			conn = BaseDao.getConnection();
			provider = dao.idSelectProvider(conn, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseDao.closeResource(conn, null, null);
		}
		return provider;
	}
 
	@Override
	public boolean updateProvider(Provider provider) {
		boolean fig = false;
		Connection conn = null;
		try {
			conn = BaseDao.getConnection();
			fig = dao.updateProvider(conn, provider) > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fig;
	}

}
