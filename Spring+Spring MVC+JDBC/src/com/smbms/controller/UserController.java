package com.smbms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.apache.catalina.connector.Request;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.smbms.dao.BaseController;
import com.smbms.pojo.Role;
import com.smbms.tools.PageSupport;
import com.smbms.Service.RoleService;
import com.smbms.Service.UserService;
import com.smbms.ServiceImpl.UserServiceImpl;
import com.smbms.pojo.User;
import com.smbms.tools.Constants;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private UserService userService;

	@Resource
	private RoleService roleService;

	@RequestMapping("/login.html")
	public String login() {
		System.out.println("Loggin============");
		return "login";
	}

	@RequestMapping(value = "/testAdd.html", method = RequestMethod.GET)
	public String testAdd(@ModelAttribute("User") User user) {
		return "TestUseradd";
	}

	@RequestMapping(value = "/testAdd.html", method = RequestMethod.POST)
	// 使用valid注解后面必须跟上一个ModelAttribute，否则会报错
	public String userTestAdd(@Valid @ModelAttribute("User") User user,
			BindingResult bindingResult, HttpSession session) {
		logger.info("userAdddo==========");
		// 切记要加上，否则出现了错误也会执行
		if (bindingResult.hasErrors()) {
			return "TestUseradd";
		}
		user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION))
				.getId());
		user.setCreationDate(new Date());
		boolean fig = userService.add(user);
		if (fig) {
			return "redirect:/user/userlist.html";
		} else {
			throw new RuntimeException("添加失败，请联系管理员");
		}
	}

	@RequestMapping("/dologin.html")
	public String dologin(@RequestParam String userCode,
			@RequestParam String userPassword, HttpSession session,
			HttpServletRequest requset) {
		User user = userService.login(userCode, userPassword);
		if (user != null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/user/main.html";
		} else {
			// 上机三
			throw new RuntimeException("用户名或密码错误");
		}
	}

	@RequestMapping("/main.html")
	public String mainhtml(HttpSession session) {
		if (session.getAttribute(Constants.USER_SESSION) == null) {
			return "login";
		}
		return "frame";
	}

	@RequestMapping("/Logout.html")
	public String logOut(HttpSession session) {
		session.removeAttribute(Constants.USER_SESSION);
		return "login";
	}

	@RequestMapping("/userlist.html")
	public String getUserList(Model model) {
		List<User> list = new ArrayList<User>();
		list = userService.getListUser();
		model.addAttribute("userList", list);
		return "userlist";
	}

	@RequestMapping("/useradd.html")
	public String userAddShow() {
		logger.info("UserAdd=================");
		return "useradd";
	}

	@RequestMapping("/user.do")
	public String userAdd(
			User user,
			HttpSession session,
			HttpServletRequest requset,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
		String idPicPath = null;
		String workPicPath = null;
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
						idPicPath = path + File.separator + fileName;
					} else if (i == 1) {
						workPicPath = path + File.separator + fileName;
					}
				} else {
					requset.setAttribute("uploadFileError", "*上传图片格式不正确!");
					return "useradd";
				}
			}
		}
		logger.info("userAdddo==========");
		user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION))
				.getId());
		user.setCreationDate(new Date());
		// 设置证件照路径
		user.setIdPicPath(idPicPath);
		// 工作证照片
		user.setWorkPicPath(workPicPath);
		boolean fig = userService.add(user);
		if (fig) {
			return "redirect:/user/userlist.html";
		} else {
			throw new RuntimeException("添加失败，请联系管理员");
		}
	}

	@RequestMapping(value = "/usermodify.html", method = RequestMethod.GET)
	public String getUserId(@RequestParam("uid") String uid, Model model) {
		logger.debug("getUserID===========================");
		User user = new User();
		user = userService.getUserById(uid);
		// 如果没有指定key值他会默认的根据名字给一个
		model.addAttribute("user", user);
		return "usermodify";
	}

	@RequestMapping(value = "/viewUser/{id}", method = RequestMethod.GET)
	public String getUser(@PathVariable String id, Model model) {
		logger.debug("getUser===========================");
		User user = new User();
		user = userService.getUserById(id);
		// 如果没有指定key值他会默认的根据名字给一个
		model.addAttribute("user", user);
		return "userview";
	}

	@RequestMapping(value = "/usermodify.html", method = RequestMethod.POST)
	public String modifyUserSave(User user, BindingResult bindingResult,
			HttpSession session) {
		if (bindingResult.hasErrors()) {
			logger.info("usermodify===========================================");
			return "usermodify";
		}
		user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION))
				.getId());
		user.setCreationDate(new Date());

		boolean fig = userService.modify(user);
		if (fig) {
			return "redirect:/user/userlist.html";
		} else {
			System.out.println("修改失败，请和管理员联系");
			return "redirect:/user/userlist.html";
		}
	}

	/**
	 * JSON
	 */
	@RequestMapping(value = "/userexist")
	// ResonseBody他是一个异步处理的注解
	@ResponseBody
	public Object userCodeIsExit(@RequestParam String userCode) {
		logger.debug("UserCodeISExit UserCode:" + userCode);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		// StringUtils为String的一个工具类，他提供了一系列的查询，截取，去除空白等方法
		if (StringUtils.isNullOrEmpty(userCode)) {
			resultMap.put("userCode", "exist");
		} else {
			User user = userService.selectUserCodeExist(userCode);
			if (null != user) {
				resultMap.put("userCode", "exist");
			} else {
				resultMap.put("userCode", "noexist");
			}
		}
		// 使用JSONArray jar包汇中的方法转换成JSON
		return JSONArray.toJSONString(resultMap);
	}
	

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public User view(@RequestParam String id) {
		logger.debug("view id=" + id);
		User user = new User();
		try {
			user = userService.getUserById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	// 根据id删除
	@RequestMapping("/delect")
	@ResponseBody
	public Object deleteUser(@RequestParam Integer uid) {
		HashMap<String, String> map = new HashMap<String, String>();
		boolean fig = userService.deleteUserById(uid);
		if (fig) {
			map.put("delResult", "true");
		} else if (!fig) {
			map.put("delResult", "false");
		} else {
			map.put("delResult", "notexist");
		}
		return map;

	}

	// 进入密码修改页面
	@RequestMapping(value = "/pwdUpdateView", method = RequestMethod.GET)
	public String pwdUpdateView() {
		return "pwdmodify";
	}

	// 进入密码修改页面
	@RequestMapping(value = "/pwdVerification", method = RequestMethod.POST)
	@ResponseBody
	public Object pwdVerificationView(@RequestParam String oldpassword,
			HttpSession session) {
		Map<String, String> map = new HashMap<String, String>();
		// 首先进行非空判断,如果不为空的话才能够进行赋值
		if (((User) session.getAttribute(Constants.USER_SESSION)) != null) {
			String modifiedPassword = ((User) session
					.getAttribute(Constants.USER_SESSION)).getUserPassword();
			// 各种情况的判断
			if (StringUtils.isNullOrEmpty(oldpassword)) {
				map.put("result", "error");
			} else if (oldpassword.equals(modifiedPassword)) {
				map.put("result", "true");
			} else {
				map.put("result", "false");
			}
		} else {
			map.put("result", "sessionerror");
		}
		return map;
	}

	// 修改用户密码
	@RequestMapping(value = "/pwdUpdate", method = RequestMethod.POST)
	public String pwdUpdate(
			@RequestParam(value = "rnewpassword") String modifiedPassword,
			HttpSession session) {
		// 获取当前登录的id
		int id = ((User) session.getAttribute(Constants.USER_SESSION)).getId();
		boolean fig = userService.updatePwd(id, modifiedPassword);
		if (fig) {
			return "redirect:/user/login.html";
		} else {
			session.setAttribute("message", "修改密码失败！");
			return "pwdmodify";
		}

	}

	// 获取下拉框的用户角色名
	@RequestMapping(value = "/selectRole")
	@ResponseBody
	public Object selectRole() {
		List<Role> list = new ArrayList<Role>();
		list = roleService.getRoleList();
		return list;
	}
	/*
	 * // 上机三
	 * 
	 * @ExceptionHandler(RuntimeException.class) public String
	 * handlerException(RuntimeException e, HttpServletRequest request) {
	 * request.setAttribute("error", "用户名或密码错误"); return "login"; }
	 */

}
