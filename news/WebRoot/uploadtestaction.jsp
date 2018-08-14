<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.*" %>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//用commons-fileupload来接受文件上传，保存到服务器的磁盘
DiskFileItemFactory factory = new DiskFileItemFactory(); 

//创建上传的对象
ServletFileUpload upload = new ServletFileUpload(factory);

//获取上传的文件
List<FileItem> fileItems = upload.parseRequest(request);

for(int i=0;i<fileItems.size();i++){
	FileItem item = fileItems.get(i);
	
	if (item.isFormField()){
		//表单普通输入框
		out.println(item.getFieldName() + "  :  " + item.getString());
		continue;
	}
	
	//检查文件类型
	//通过判断后缀名
	String[] extNames =  {"jpg","png","bmp"};
	String uploadName = item.getName();
	int pos = uploadName.lastIndexOf(".");
	String s = uploadName.substring(pos+1);
	List<String> list = Arrays.asList(extNames);	
	if (!list.contains(s)){
		//提示错误
		out.println("<script>alert('文件类型不允许，请选择jpg,png,bmp文件.');</script>");
		return;
	}
	
	//文件大小的检查
	//item.getSize()
	
	//文件保存的路径和文件名
	String fileName = request.getRealPath("upload");
	
	System.out.println(fileName);
	fileName = fileName + "\\"+java.util.UUID.randomUUID().toString()+"_"+item.getName(); 
	
	java.io.File file = new java.io.File(fileName);
	
	//保存文件
	item.write(file);
	
	
	//数据访问对象
	//插入记录
	
	out.println("ok:" + fileName);
}

%>