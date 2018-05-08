<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="fm" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head> 
<body>
	<fm:form method="post" modelAttribute="User">
		<fm:errors path="userCode" />
		用户编码:<fm:input path="userCode" />
		<fm:errors path="userName" />
		用户名称:<fm:input path="userName" />
		<fm:errors path="address" />
		用户地址:<fm:input path="address" />
		<fm:radiobutton path="userRole" value="1" />系统管理员
	<fm:radiobutton path="userRole" value="2" />经理
	<fm:radiobutton path="userRole" value="3" />普通用户
	<input type="submit" value="保存">
	</fm:form>
</body>
</html>