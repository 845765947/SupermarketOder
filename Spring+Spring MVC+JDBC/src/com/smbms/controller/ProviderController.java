package com.smbms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.junit.runners.Parameterized.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.jrockit.jfr.Producer;
import com.smbms.Service.ProviderService;
import com.smbms.ServiceImpl.ProviderServiceImpl;
import com.smbms.pojo.Provider;
import com.smbms.pojo.User;
import com.smbms.tools.Constants;

@Controller
@RequestMapping("/provider") 
public class ProviderController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProviderService service;

	@RequestMapping(value = "/provideradd", method = RequestMethod.GET)
	public String providerAddView() {
		return "provideradd";
	}

	@RequestMapping(value = "/provideradd", method = RequestMethod.POST)
	public String providerAdd(
			@ModelAttribute Provider provider,
			HttpSession session,
			HttpServletRequest requset,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs,
			Model model) {
		String businessLicensePath = null;
		String organizationCodeCertificatePath = null;
		String errorInfo = null;
		String path = requset.getSession().getServletContext()
				.getRealPath("statics" + File.separator + "uploadfiles");
		logger.info("uploadFile path==========================>" + path);
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			if (!attach.isEmpty()) {
				if (i == 0) {
					errorInfo = "uploadFileError";
				} else if (i == 1) {
					errorInfo = "uploadWpError";
				}
				String oldFileName = attach.getOriginalFilename();// 原文件名
				logger.info("uploadFile oldFileName=============================>"
						+ oldFileName);
				String perfix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
				logger.debug("uploadFile prefix==========================?>"
						+ perfix);
				int filesize = 500000;
				logger.debug("uploadFIle size=================================>"
						+ attach.getSize());
				if (attach.getSize() > filesize) {
					requset.setAttribute("uploadFileError", "上传的文件大小不能超过500KB");
					return "useradd";
				} else if (perfix.equalsIgnoreCase("jpg")
						|| perfix.equalsIgnoreCase("png")
						|| perfix.equalsIgnoreCase("jpeg")
						|| perfix.equalsIgnoreCase("pneg")) {
					String fileName = System.currentTimeMillis()
							+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
					logger.debug("new fileName===========" + attach.getName());
					File targetFile = new File(path, fileName);
					// 如果没有当前路径，则创建一个，包括子包
					if (!targetFile.exists()) {
						targetFile.mkdirs();
					}
					// 保存
					try {
						attach.transferTo(targetFile);
					} catch (Exception e) {
						e.printStackTrace();
						requset.setAttribute("uploadFileError", "*上传失败!");
						return "useradd";
					}
					if (i == 0) {
						businessLicensePath = path + File.separator + fileName;
					} else if (i == 1) {
						organizationCodeCertificatePath = path + File.separator
								+ fileName;
					}
				} else {
					requset.setAttribute("uploadFileError", "*上传图片格式不正确!");
					return "useradd";
				}
			}
		}
		logger.info("ProviderAdd========================");
		// 设置更新时间
		provider.setCreatedBy(((User) session
				.getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		// 设置路径
		provider.setBusinessLicense(businessLicensePath);
		provider.setOrganizationCodeCertificate(organizationCodeCertificatePath);
		boolean fig = service.addProvider(provider);
		if (fig) {
			return "redirect:/provider/providerlist";
		} else {
			return "redirect:/provideradd";
		}
	}

	@RequestMapping(value = "/providerlist")
	public String providerlist(
			@RequestParam(value = "queryProCode", required = false) String proCode,
			@RequestParam(value = "queryProName", required = false) String proName,
			Model model, HttpSession session) {
		List<Provider> list = new ArrayList<Provider>();
		list = service.getProviderList(proCode, proName);
		model.addAttribute("providerList", list);
		return "providerlist";
	}

	@RequestMapping(value = "/selectId/{id}")
	public String selectId(@PathVariable int id, HttpSession session,
			Model model) {
		Provider provider = new Provider();
		provider = service.idSelectProvider(id);
		if (provider.getBusinessLicense() != null) {
			// 截取数据库中企业营业执照最后的文件名和后缀
			int index = provider.getBusinessLicense().indexOf(
					"\\ch10_ShortAnswer");
			provider.setBusinessLicense(provider.getBusinessLicense()
					.substring(index, provider.getBusinessLicense().length()));
			// 截取数据库中组织机构代码证最后的文件名和后缀
			index = provider.getOrganizationCodeCertificate().indexOf(
					"\\ch10_ShortAnswer");
			provider.setOrganizationCodeCertificate(provider
					.getOrganizationCodeCertificate().substring(index,
							provider.getOrganizationCodeCertificate().length()));
		}
		// 添加到model模型对象中
		model.addAttribute("provider", provider);
		return "providerview";
	}

	@RequestMapping(value = "/updateProvider/{id}", method = RequestMethod.GET)
	public String updatePeoviderShow(@PathVariable int id, Model model) {
		Provider provider = new Provider();
		provider = service.idSelectProvider(id);
		model.addAttribute("provider", provider);
		return "providermodify";
	}

	@RequestMapping(value = "/updateProvider", method = RequestMethod.POST)
	public String updateProvider(@ModelAttribute Provider provider,
			HttpSession session) {
		provider.setModifyBy(((User) session
				.getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		boolean fig = service.updateProvider(provider);
		if (fig) {
			return "redirect:/provider/providerlist";
		} else {
			return "/provider/providermodify";
		}
	}

}
