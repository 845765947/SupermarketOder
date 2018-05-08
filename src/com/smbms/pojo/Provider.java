package com.smbms.pojo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

public class Provider {
	private int id;
	@NotNull(message = "供应商编码不能为空123213123123")
	private String proCode;
	@NotNull(message = "供应商名称不能为空")
	private String proName;
	private String proDesc;
	@NotNull(message = "联系人不能为空")
	private String proContact;
	@Pattern(message = "请输入正确的手机号", regexp = "^1[3|4|5|8][0-9]/d{4,8}$")
	private String proPhone;
	private String proAddress;
	private String proFax;
	private int createdBy;
 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date creationDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date modifyDate;

	private int modifyBy;
	private String businessLicense; // 企业营业执照
	private String organizationCodeCertificate;// 组织机构代码证

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getOrganizationCodeCertificate() {
		return organizationCodeCertificate;
	}

	public void setOrganizationCodeCertificate(
			String organizationCodeCertificate) {
		this.organizationCodeCertificate = organizationCodeCertificate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getProContact() {
		return proContact;
	}

	public void setProContact(String proContact) {
		this.proContact = proContact;
	}

	public String getProPhone() {
		return proPhone;
	}

	public void setProPhone(String proPhone) {
		this.proPhone = proPhone;
	}

	public String getProAddress() {
		return proAddress;
	}

	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}

	public String getProFax() {
		return proFax;
	}

	public void setProFax(String proFax) {
		this.proFax = proFax;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(int modifyBy) {
		this.modifyBy = modifyBy;
	}

}
