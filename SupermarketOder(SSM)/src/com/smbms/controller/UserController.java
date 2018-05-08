package com.smbms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.smbms.pojo.Role;
import com.smbms.pojo.User;
import com.smbms.service.RoleService;
import com.smbms.service.UserService;
import com.smbms.tools.Constants;
import com.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private UserService userService;

	@Resource
	private RoleService roleService;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// 登录方法
	@RequestMapping("/dologin.html")
	public String dologin(@RequestParam("userCode") String userCode,
			@RequestParam("userPassword") String pwd, HttpSession session) {
		User user = userService.getLoginUser(userCode, pwd);
		if (user != null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/user/sys/main.html";
		} else {
			throw new RuntimeException("用户名或密码错误");
		}
	}

	// 注销方法
	@RequestMapping("/Logout.html")
	public String ogOut(HttpSession session) {
		session.removeAttribute(Constants.USER_SESSION);
		return "redirect:/user/login";
	}

	// 登录页面跳转
	@RequestMapping(value = "/sys/main.html")
	public String main() {
		return "frame";
	}

	// 跳转用户管理页面
	@RequestMapping(value = "/userlist.html")
	public String getUserList(
			Model model,
			@RequestParam(value = "queryname", required = false) String queryUserName,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getUserList ---- > queryUserName: " + queryUserName);
		logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
		logger.info("getUserList ---- > pageIndex: " + pageIndex);
		int _queryUserRole = 0;
		List<User> userList = null;
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
				return "redirect:/user/syserror.html";
				// response.sendRedirect("syserror.jsp");
			}
		}
		// 总数量（表）
		int totalCount = userService
				.getUserCount(queryUserName, _queryUserRole);

		// 总页数

		PageSupport pages = new PageSupport();
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		pages.setCurrentPageNo((currentPageNo - 1) * pageSize);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}

		userList = userService.getUserList(queryUserName, _queryUserRole,
				currentPageNo, pageSize);
		model.addAttribute("userList", userList);
		List<Role> roleList = null;
		roleList = roleService.getRoleList();
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "userlist";
	}

	// 查看方法
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

	// 跳转到Add页面
	@RequestMapping(value = "/sys/useradd.html", method = RequestMethod.GET)
	public String addUserSysDo() {
		return "useradd";
	}

	// add角色方法
	@RequestMapping(value = "/user.do")
	public String addUser(User user, Model model) {
		return "redirect:/user/userlist.html";
	}

	// 判断UserCode是否可使用
	@RequestMapping(value = "/userexist")
	@ResponseBody
	public Object VerifyId(@RequestParam("userCode") String userCode) {
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtils.isNullOrEmpty(userCode)) {
			User user = userService.getLoginUser(userCode);
			System.out.println(user.getUserName());
			if (user.getUserName() != null) {
				map.put("userCode", "exist");
			} else {
				map.put("userCode", "Noexist");
			}
		} else {
			map.put("userCode", "Noexist");
		}
		return map;
	}

	// 获取用户列表
	@RequestMapping("/selectRole")
	@ResponseBody
	public List<Role> seleceListRole(Model model) {
		List<Role> list = new ArrayList<Role>();
		list = roleService.getRoleList();
		return list;
	}

	// 密码修改跳转页面
	@RequestMapping("/sys/pwdUpdateView")
	public String pwdUpdateView() {
		return "pwdmodify";
	}

	// 密码修改
	@RequestMapping("/sys/pwdUpdate")
	public String updatePwd(@RequestParam("rnewpassword") String pwd,
			HttpSession session, Model model) {
		int id = ((User) session.getAttribute(Constants.USER_SESSION)).getId();
		boolean fig = userService.updatePwd(id, pwd);
		if (fig) {
			return "redirect:/user/login";
		} else {
			model.addAttribute("message", "修改密码失败 ，请重试");
			return "pwdmodify";
		}
	}

	// 判断原密码是否一致
	@RequestMapping("/pwdVerification")
	@ResponseBody
	public Object DedicatedAnswer(
			@RequestParam("oldpassword") String oldpassword, HttpSession session) {
		Map<String, String> map = new HashMap<String, String>();
		// 判断session是否失效
		if (((User) session.getAttribute(Constants.USER_SESSION)).getId() != null) {
			String pwd = ((User) session.getAttribute(Constants.USER_SESSION))
					.getUserPassword();
			if (pwd.equals(oldpassword)) {
				map.put("result", "true");
			} else if (!(pwd.equals(oldpassword))) {
				map.put("result", "false");
			} else {
				map.put("result", "error");
			}
		} else {
			map.put("result", "sessionerror");
		}
		return map;
	}

	// 用户修改页面跳转
	@RequestMapping("/sys/usermodify/{uid}")
	public String usermodifyDo(@PathVariable String uid, Model model) {
		User user = new User();
		user = userService.getUserById(uid);
		model.addAttribute("user", user);
		System.out.println(user.getUserName());
		return "usermodify";
	}

	// 用户全部修改
	@RequestMapping("/usermodify.html")
	public String usermodifyDo(User user) {
		boolean fig = userService.modify(user);
		if (fig) {
			return "redirect:/uesr/userlist.html";
		} else {
			throw new RuntimeException("用户修改失败,请和管理员取得联系");
		}
	}

	// 删除用户
	@RequestMapping("/delete")
	@ResponseBody
	public Object deleteUser(@RequestParam("uid") Integer userid) {
		Map<String, String> map = new HashMap<String, String>();
		User user = new User();
		user = userService.getUserById(userid.toString());
		boolean fig = userService.deleteUserById(userid);
		if (fig) {
			if (user.getWorkPicPath() != null) {
				File fileid = new File(user.getWorkPicPath());
				if (fileid.exists()) {
					fileid.delete();
					System.out.println("删除单个文件成功！");
				} else {
					System.out.println("删除单个文件失败！");
				}
			}
			if (user.getIdPicPath() != null) {
				File file = new File(user.getIdPicPath()); 
				if (file.exists()) {
					file.delete();
					System.out.println("删除单个文件成功！");
				} else {
					System.out.println("删除单个文件失败！");
				}
			}
			map.put("delResult", "true");
		} else {
			map.put("delResult", "false");
		}
		return map;
	}

}
