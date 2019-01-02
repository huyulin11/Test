<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.kaifantech.util.serial.*"%>
<%@ page import="com.ytgrading.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>简易调试工具</title>
<%
	String address = request.getParameter("address");
	String com = request.getParameter("com");
	String cmd = request.getParameter("cmd");
	String result = "";
%>
</head>
<body>
	<h1>AGV</h1>
	<form action="agv.jsp" method="GET">
		COM口: <input type="text" name="com" value='COM4' /><br />
		ZigBee地址（低位在前，高位在后）: <input type="text" name="address"
			value='4303'> <br /> 动作: <select name="cmd">
			<option value="05">启动</option>
			<option value="06">停止</option>
			<option value="02">查询</option>
			<option value="0A01">挂钩切换</option>
			<option value="0702">左转</option>
			<option value="0703">右转</option>
		</select> <input type="submit" value="提交" />
	</form>
	<ul>
		<li><b>COM口:</b> <%=com%></li>
		<li><b>ZigBee地址:</b> <%=address%></li>
		<li><b>动作:</b> <%=cmd%></li>
		<li><b>执行结果:</b> <%=result%></li>
	</ul>
</body>
</html>