<%@page import="java.net.URLDecoder"%>
<%@page import="org.jasig.cas.client.util.AbstractCasFilter"%>
<%@page import="org.jasig.cas.client.validation.Assertion"%>
<%@page import="org.jasig.cas.client.authentication.AttributePrincipal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="jquery.min.js"></script>
<script type="text/javascript">
/* function logout() {
	$.post("http://119.97.130.92:8010/mh-sso/logout",{},function(result){
    	window.location.href="http://localhost:8080/hello";
    });
}  */
</script>	
  </head>
  
  <body>
    欢迎您管理员！
    <%
    Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);  
	AttributePrincipal principal = assertion.getPrincipal();  
	String username = principal.getName(); 
    out.print(username);
     %>
     用户信息
     <%
	Map<String, String> attributes = principal.getAttributes();  
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
    	out.print("<br/>");
        out.print("key= " + entry.getKey() + " ===== value= " + URLDecoder.decode(entry.getValue(), "UTF-8"));
    }
     %>
  </body>
</html>
