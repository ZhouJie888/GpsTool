<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加主题--管理后台</title>
<link href="../css/admin.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">
	function check(){
		var tname = document.getElementById("tname");

		if(tname.value == ""){
			alert("请输入主题名称！！");
			tname.focus();
			return false;
		}		
		return true;
	}
	
	function save(){
		$.ajax({
			"url":"../util/topics",
			"type":"GET",
			"data":{"opr":"modify","tid":$("#tid").val(),"tname":$("#tname").val()},
			"success":function(){
				alert("保存成功!");
			}
		});
	}
</script>
</head>
<body>
<div id="header">
  <div id="welcome">欢迎使用新闻管理系统！</div>
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
    <h1 id="opt_type"> 编辑主题： </h1>
    <form action="../util/topics?opr=modify" method="post" >
        <input name="tid" type="text" class="opt_input" id="tid" value="${param.tid}"/>

      <p>
        <label> 主题名称 </label>
        <input name="tname" type="text" class="opt_input" id="tname" value="${param.tname}"/>
      </p>
      <input name="action" type="hidden" value="addtopic"/>
      <input type=button value="提交" class="opt_sub" onclick="save()" />
      <input type="reset" value="重置" class="opt_sub" />
    </form>
  </div>
</div>
<div id="footer">
  <%@ include file="console_element/bottom.html" %>
</div>
</body>
</html>
