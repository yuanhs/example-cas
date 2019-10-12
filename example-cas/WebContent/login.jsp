 <%@ page language="java" contentType="text/html; charset=utf-8"  
        pageEncoding="utf-8"%>  
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  

 <html>     
 <head>  
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
    <title>自定义客户端登录</title>  
    <script type="text/javascript">  
        function getParam(name)  
        {  
            var queryString = window.location.search;  
            var param = queryString.substr(1, queryString.length - 1).split("&");  
            for (var i = 0; i < param.length; i++)  
            {  
                var keyValue = param[i].split("=");  
                if (keyValue[0] == name) {
                    return keyValue[1];  
                } 
            }  
            return null;  
        }  
        function init()  
        {  
            // 显示异常信息  
            var error = getParam("errorMessage");  
            if (error)  
            {  
                document.getElementById("errorMessage").innerHTML = decodeURIComponent(error);  
            }  

            // 注入service  
            var service = getParam("service");  
            console.log("service:"+service);
            if (service) {
                document.getElementById("service").value = decodeURIComponent(service);  
            }else {
                document.getElementById("service").value = location.href;  
            }

            var loginTicket = getParam("loginTicket");  
            console.log("loginTicket:"+loginTicket);
            if (loginTicket){
                document.getElementById("loginTicket").value = decodeURIComponent(loginTicket);  
            }else{
                document.getElementById("loginTicket").value = ''; 
            }
            var flowExecutionKey = getParam("flowExecutionKey");  
            console.log("flowExecutionKey:"+flowExecutionKey);
            if (flowExecutionKey) {
                document.getElementById("execution").value = decodeURIComponent(flowExecutionKey);  
            } else {
                document.getElementById("execution").value = '';
            }

        }  
    </script>  
    </head>  
    <body>  
        <h1>自定义客户端登录</h1>  
        <div id="errorMessage" style="color: red;"></div>  
        <form id="myLoginForm" action="http://zhimao1.3322.org:8088/mh-sso-hb"  
            method="post">  
             <input type="hidden" name="service" value="http://localhost:8080/cas-test/index.jsp">  
            <input type="hidden" name="loginUrl" value="http://localhost:8080/cas-test/login.jsp">  
            <input type="hidden" name="submit" value="LOGIN" /> 
            <input type="hidden" name="msubmit" value="true" />   
            <input type="hidden" name="lt" id="loginTicket" value="" /> 
             <input type="hidden" name="execution" id="execution" value="" /> 
             <input type="hidden" name="service" id="service" value="" />
             <input type="hidden" name="_eventId" value="submit" />
            <table>  
                <tr>  
                    <td>用户名:</td>  
                    <td><input type="text" name="username"></td>  
                </tr>  
                <tr>  
                    <td>密&nbsp;&nbsp;码:</td>  
                    <td><input type="password" name="password"></td>  
                </tr>  
                <tr>  
                    <td colspan="2"><input type="submit" value="登陆" /></td>  
                </tr>  
            </table>  
        </form>  
        <script type="text/javascript">  
            init();
        </script>  
    </body>  
    </html>