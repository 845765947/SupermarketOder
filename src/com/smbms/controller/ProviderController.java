package com.smbms.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smbms.pojo.Provider;
import com.smbms.pojo.Role;
import com.smbms.pojo.User;
import com.smbms.service.BillService;
import com.smbms.service.ProviderService;
import com.smbms.tools.Constants;
import com.smbms.tools.PageSupport;

@Controller
@RequestMapping("/provider")
public class ProviderController {
	Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private ProviderService service;

	@Resource
	private BillService billService;

	// 进入供应商管理界面
	@RequestMapping(value = "/providerlist.html")
	public String getUserList(
			Model model,
			@RequestParam(value = "queryProName", required = false) String queryUserName,
			@RequestParam(value = "queryProCode", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getUserList ---- > queryUserName: " + queryUserName);
		logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
		logger.info("getUserList ---- > pageIndex: " + pageIndex);
		List<Provider> Provider = null;
		int _queryUserRole = 0;
		// 设置页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		int currentPageNo = 1;

		if (queryUserName == null) {
			queryUserName = "";
		}

		if (queryUserRole != null && !queryUserRole.equals("")) {
			_queryUserRole = Integer.parseInt(queryUserRole);
		}

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				throw new RuntimeException("获取供应商列表出错");
			}
		}
		// 总数量（表）
		int totalCount = service.getProviderCount(queryUserRole, queryUserName);
		// 总页数
		PageSupport pages = new PageSupport();
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		pages.setCurrentPageNo(currentPageNo);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		Provider = service.getProviderList(queryUserName, queryUserRole,
				(currentPageNo - 1) * pageSize, pageSize);
		model.addAttribute("providerList", Provider);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "providerlist";
	}

	/**
	 * 进入查看视图页面
	 */
	@RequestMapping("/selectId/{proid}")
	public String providerView(@PathVariable String proid, Model model) {
		Provider provider = new Provider();
		provider = service.selectId(proid);
		if (provider != null) {
			// 截取数据库中企业营业执照最后的文件名和后缀
			if (provider.getBusinessLicense() != null) {
				int index = provider.getBusinessLicense().indexOf(
						"\\SupermarketOder");

				provider.setBusinessLicense(provider.getBusinessLicense()
						.substring(index,
								provider.getBusinessLicense().length()));
				// 截取数据库中组织机构代码证最后的文件名和后缀

			}

			if (provider.getOrganizationCodeCertificate() != null) {
				int index2 = provider.getOrganizationCodeCertificate().indexOf(
						"\\SupermarketOder");
				provider.setOrganizationCodeCertificate(provider
						.getOrganizationCodeCertificate().substring(
								index2,
								provider.getOrganizationCodeCertificate()
										.length()));
			}

		}
		model.addAttribute("provider", provider);
		return "providerview";
	}

	// 添加页面跳转
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

	// 进入供应商更新页面
	@RequestMapping("/updateProviderView/{proid}")
	public String updateProviderView(@PathVariable String proid, Model model) {
		Provider provider = new Provider();
		provider = service.selectId(proid);
		if (provider != null) {
			// 截取数据库中企业营业执照最后的文件名和后缀
			if (provider.getBusinessLicense() != null) {
				int index = provider.getBusinessLicense().indexOf(
						"\\SupermarketOder");

				provider.setBusinessLicense(provider.getBusinessLicense()
						.substring(index,
								provider.getBusinessLicense().length()));
				// 截取数据库中组织机构代码证最后的文件名和后缀

			}

			if (provider.getOrganizationCodeCertificate() != null) {
				int index2 = provider.getOrganizationCodeCertificate().indexOf(
						"\\SupermarketOder");
				provider.setOrganizationCodeCertificate(provider
						.getOrganizationCodeCertificate().substring(
								index2,
								provider.getOrganizationCodeCertificate()
										.length()));
			}
		}
		model.addAttribute("provider", provider);
		return "providermodify";
	}

	// 供应商更新提交
	@RequestMapping("/updateProvider")
	public String updateProvider(
			Provider provider,
			HttpServletRequest requset,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs,
			HttpSession session) {

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
					System.out.println(1);
					return "providermodify";
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
						System.out.println(2);
						return "providermodify";
					}
					if (i == 0) {
						businessLicensePath = path + File.separator + fileName;
					} else if (i == 1) {
						organizationCodeCertificatePath = path + File.separator
								+ fileName;
					}
				} else {
					requset.setAttribute("uploadFileError", "*上传图片格式不正确!");
					return "providermodify";
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
		boolean fig = service.updateProvider(provider);
		if (fig) {
			return "redirect:/provider/providerlist.html";
		} else {
			throw new RuntimeException("更新供应商失败，请联系管理员");
		}
	}

	@RequestMapping("/providerDelete")
	@ResponseBody
	public Object providerDelete(@RequestParam("proid") Integer proid) {
		Map<String, String> map = new HashMap<String, String>();
		Provider provider = service.selectId(proid.toString());
		boolean fig = service.deleteProvider(proid);
		if (fig) {
			boolean figBill = billService.deleteBill(proid);
			if (figBill) {
				if (null != provider.getBusinessLicense()) {
					File fileid = new File(provider.getBusinessLicense());
					if (fileid.exists()) {
						fileid.delete();
						System.out.println("删除单个文件成功！"); 
					} else {
						System.out.println("删除单个文件失败！");
					}
				} 
				if (provider.getOrganizationCodeCertificate() != null) {
					File file = new File(
							provider.getOrganizationCodeCertificate());
					if (file.exists()) { 
						file.delete();
						System.out.println("删除单个文件成功！");
					} else {
						System.out.println("删除单个文件失败！");
					}
				}
				map.put("delResult", "true");
			}
		} else {
			map.put("delResult", "false");
		}
		return map;
	}
}
