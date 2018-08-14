<%@ page language="java" import="java.util.*,org.news.entity.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加主题--管理后台</title>
<link href="../css/admin.css" rel="stylesheet" type="text/css" />
<!-- 3 引入jquery库 -->
<script type="text/javascript" src="../js/jquery-1.12.4.min.js"></script>

<!-- 4 在文档加载完成后通过ajax读取主题的JSON数据 -->
<script>
$(document).ready(function (){	
	//AJAX读取JSON	
	load_data();
});
	
	//加载主题列表函数
	function load_data(){
		$.ajax({
				"url":"../util/topics?opr=listjson",
				"type":"GET",
				"dataType":"json",
				"success": success_callback
			});
		
			//根据JSON创建主题列表
			function success_callback(data){
				//alert(data);
				$("#ul").empty();  //清除原来的列表项
				$.each(data,function(i){
					$("#ul").append("<li>" + this.tname + 
						"&nbsp <a href='topic_modify.jsp?tid="+this.tid+"&tname="+this.tname+"'>编辑</a>" +
						"&nbsp <a href='javascript:del("+this.tid+")'>删除</a>" +
						"</li>");
				});
			}		
	}

	function del(id){
		$.ajax({
			"url":"../util/topics?opr=del&tid="+id,
			"success": del_callback
		});	
		function del_callback(){
			alert("删除成功");
			//向服务端发送一个ajax的请求，opr=del&tid=?
			load_data();
		}
	}
</script>

</head>
<body>
<div id="header">
  <div id="welcome">
    欢迎使用新闻管理系统！</div>
  <div id="nav">
    <div id="logo"><img src="../images/logo.jpg" alt="新闻中国" /></div>
    <div id="a_b01"><img src="../images/a_b01.gif" alt="" /></div>
  </div>
</div>
<div id="admin_bar">
  <div id="status">管理员： 登录  &#160;&#160;&#160;&#160; <a href="#">login out</a></div>
  <div id="channel"> </div>
</div>
<div id="main">
  <%@ include file="console_element/left.html" %>
  <div id="opt_area">
    <ul class="classlist" id="ul">  <!-- 2 增加UL的id属性 -->
		<!-- 1，删除原来用jstl的迭代标签生成列表的代码 -->
    </ul>
  </div>
</div>
<div id="footer">
  <%@ include file="console_element/bottom.html" %>
</div>
</body>
</html>
