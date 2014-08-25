<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/jquery.loadmask.css" />
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/jquery.loadmask.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#tranform").validate({
		submitHandler:function(form){
			$(form).mask("正在传输数据，请等待...");//显示loading
			var options={
					success: showResponse,//当成功提交表单后执行的函数
						
					dataType:"xml"
			};
			$(form).ajaxSubmit(options);//用ajax提交表单
		},
		rules:{
				desAcc: "required",
				money:{
					required:true,//money为必填属性
					number:true
				}
				
		},
		messages:{
			desAcc:"请输入对方账户",
			money:{
				required:"请输入金额",//当money不满足验证要求时的提示信息
				number:"金额必须为数字形式"
			}
		}
	});
	
});
function showResponse(responseXML,statusText, xhr, form){
	alert(statusText);
	$("#msg").text($(responseXML).find("#msg").text());
	$(form).unmask();    //隐藏loading
}
</script>
<title>Insert title here</title>
</head>
<body>
<div id="msg"></div>
<s:form id="tranform" action="tran">
<s:textfield name="desAcc" label="对方账户"></s:textfield>
<s:textfield name="money" label="金额"></s:textfield>
<s:submit value="提交"></s:submit>
</s:form>
</body>
</html>