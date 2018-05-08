package com.smbms.dao.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;
import com.smbms.dao.BaseDao;
import com.smbms.pojo.Provider;

@Repository
public class ProviderDaoImpl implements ProviderDao {

	@Override
	public int addProvider(Connection conn, Provider provider) {
		int updateCount = 0;
		PreparedStatement ps = null;
		try {
			if (null != conn) {
				String sql = "insert INTO smbms_provider (proCode,proName,proDesc,proContact,proPhone,proAddress,`proFax`,createdBy,creationDate,modifyDate,modifyBy,businessLicense,organizationCodeCertificate)"
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] params = { provider.getProCode(),
						provider.getProName(), provider.getProDesc(),
						provider.getProContact(), provider.getProPhone(),
						provider.getProAddress(), provider.getProFax(),
						provider.getCreatedBy(), provider.getCreationDate(),
						provider.getModifyDate(), provider.getModifyBy(),
						provider.getBusinessLicense(),
						provider.getOrganizationCodeCertificate() };
				updateCount = BaseDao.execute(conn, ps, sql, params);
				BaseDao.closeResource(null, ps, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateCount;
	}

	@Override
	public List<Provider> getProviderList(Connection connection,
			String proCode, String proName) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Provider> userList = new ArrayList<Provider>();
		if (connection != null) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id,proCode,proName,proDesc,proContact,proPhone,proAddress,proFax,createdBy,creationDate,modifyDate,modifyBy FROM smbms_provider  WHERE 1=1");
			List<Object> list = new ArrayList<Object>();
			if (!StringUtils.isNullOrEmpty(proCode)) {
				sql.append(" and  like ?");
				list.add("%" + proCode + "%");
			}
			if (!StringUtils.isNullOrEmpty(proName)) {
				sql.append(" and proName like ?");
				list.add("%" + proName + "%");
			}
			Object[] params = list.toArray();
			System.out.println("sql ----> " + sql.toString());
			rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
			while (rs.next()) {
				Provider der = new Provider();
				der.setId(rs.getInt("id"));
				der.setProCode(rs.getString("proCode"));
				der.setProName(rs.getString("proName"));
				der.setProDesc(rs.getString("proDesc"));
				der.setProContact(rs.getString("proContact"));
				der.setProPhone(rs.getString("proPhone"));
				der.setProAddress(rs.getString("proAddress"));
				der.setProFax(rs.getString("proFax"));
				der.setCreatedBy(rs.getInt("createdBy"));
				der.setCreationDate(rs.getDate("creationDate"));
				der.setModifyDate(rs.getDate("modifyDate"));
				der.setModifyBy(rs.getInt("modifyBy"));
				userList.add(der);
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return userList;
	}

	@Override
	public Provider idSelectProvider(Connection conn, int id) {
		Provider provider = new Provider();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM `smbms_provider` where id=?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				provider.setId(rs.getInt(1));
				provider.setProCode(rs.getString(2));
				provider.setProName(rs.getString(3));
				provider.setProDesc(rs.getString(4));
				provider.setProContact(rs.getString(5));
				provider.setProPhone(rs.getString(6));
				provider.setProAddress(rs.getString(7));
				provider.setProFax(rs.getString(8));
				provider.setCreatedBy(rs.getInt(9));
				provider.setCreationDate(rs.getDate(10));
				provider.setModifyDate(rs.getDate(11));
				provider.setModifyBy(rs.getInt(12));
				provider.setBusinessLicense(rs.getString(13));
				provider.setOrganizationCodeCertificate(rs.getString(14));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return provider;
	}

	@Override
	public int updateProvider(Connection conn, Provider provider) {
		int count = 0;
		PreparedStatement pstm = null;
		try {
			String sql = "UPDATE  smbms_provider SET proCode=?,proName=?,"
					+ "proDesc=?,proContact=?,proPhone=?,proAddress=?,proFax=?"
					+ ",createdBy=?,creationDate=?,modifyDate=?,modifyBy=? where id=?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, provider.getProCode());
			pstm.setString(2, provider.getProName());
			pstm.setString(3, provider.getProDesc());
			pstm.setString(4, provider.getProContact());
			pstm.setString(5, provider.getProPhone());
			pstm.setString(6, provider.getProAddress());
			pstm.setString(7, provider.getProFax());
			pstm.setInt(8, provider.getCreatedBy());
			pstm.setObject(9, provider.getCreationDate());
			pstm.setObject(10, provider.getModifyDate());
			pstm.setInt(11, provider.getModifyBy());
			pstm.setInt(12, provider.getId());
			System.out.println(pstm);
			count = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseDao.closeResource(null, pstm, null);
		return count;
	}
}
