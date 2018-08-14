<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.io.*,java.util.*"%>
<%@page import="org.apache.commons.fileupload.*"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%
		String uploadFilePath = request.getSession().getServletContext().getRealPath("upload/" );
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			//解析form表单中所有文件

			List<FileItem> items = upload.parseRequest(request);
			FileItem item = items.get(0);
			File saveFile = new File(uploadFilePath,"abc.txt");
			item.write(saveFile);
			String uploadFileName = saveFile.getAbsolutePath();
			out.print("上传成功后的文件名是："+uploadFileName);	
	} catch (Exception e) {
	e.printStackTrace();
}
%>

