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
	if (!AppTool.isAnyNull(com, cmd)) {
		result = RdtBaseUtil.get(com).sendToDev(address, cmd);
	}
%>
</head>
<body>
	<h1>电梯</h1>
	<form action="dev.jsp" method="GET">
		COM口: <input type="text" name="com" value='COM4' /><br />
		ZigBee地址（低位在前，高位在后）: <input type="text" name="address" value='4301'>
		<br /> <select name="cmd">
			<option value="A3E0">到三楼</option>
			<option value="A2E1">到二楼</option>
			<option value="A1E2">到一楼</option>
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