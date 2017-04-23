<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="bean.SystemConfigurationInfo"%>
<%@ page language="java" import="util.HttpRequestUtil"%>
<%@ page language="java" import="cache.ResultCodeStorage" %>
<%@ page language="java" import="com.google.gson.Gson" %>
<%@ page language="java" import="com.google.gson.reflect.TypeToken" %>
<%@ page language="java" import="bean.ResultInfo" %>
<%@ page language="java" import="java.lang.reflect.Type" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String fromUsrUid = request.getParameter("from");
	String toUsrUid = request.getParameter("to");
	if (fromUsrUid == null || toUsrUid == null) {
		response.sendRedirect("error.jsp?code=" + ResultCodeStorage.code_err_missing_parameter);
		return;
	}
	String accessCheckResult = HttpRequestUtil.sendGet(SystemConfigurationInfo.getMainSystemBasePath() + "systemapis.action",
	"which=checkUserAccess&uid=" + fromUsrUid);
	System.out.println(accessCheckResult);
	Gson gson = new Gson();
	Type resultType = new TypeToken<ResultInfo<String>>(){}.getType();
	ResultInfo<String> result = gson.fromJson(accessCheckResult, resultType);
	if ("error".equals(result.getMsgType())) {
		response.sendRedirect("error.jsp?code=" + ResultCodeStorage.code_err_invalid_access);
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta charset="UTF-8">
  <title>在线聊天</title>
  <meta http-equiv="Access-Control-Allow-Origin" content="*">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport"
        content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <link rel="alternate icon" type="image/png" href="assets/i/favicon.png">
  <link rel="stylesheet" href="assets/css/amazeui.min.css"/>
  
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<!-- UM相关资源 -->
<link href="assets/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="assets/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="assets/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="assets/umeditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body>
<header class="am-topbar am-topbar-fixed-top">
	<div class="am-container">
		<h1 class="am-topbar-brand">
			<a href="#">聊天</a>
		</h1>
		<div class="am-collapse am-topbar-collapse" id="collapse-head">
			<div class="am-topbar-right">
				<button class="am-btn am-btn-primary am-topbar-btn am-btn-sm"><span class="am-icon-close"></span> 退出</button>
			</div>
		</div>
	</div>
</header>
	<div id="main">
		<!-- 聊天内容展示区域 -->
	<div id="ChatBox" class="am-g am-g-fixed" >
	  <div class="am-u-lg-12" style="height:400px;border:1px solid #999;overflow-y:scroll;">
		<ul id="chatContent" class="am-comments-list am-comments-list-flip">
			<li id="msgtmp" class="am-comment" style="display:none;">
			    <a href="">
			        <img ff="usrimg" class="am-comment-avatar" src="assets/images/other.jpg" alt=""/>
			    </a>
			    <div class="am-comment-main" >
			        <header class="am-comment-hd">
			            <div class="am-comment-meta">
			              <a ff="nickname" href="#" class="am-comment-author">某人</a>
			              <span ff="msgdate">2014-7-12 15:30</span>
			            </div>
			        </header>
			     <div ff="content" class="am-comment-bd">此处是消息内容</div>
			    </div>
			</li>
		</ul>
	  </div>
	</div>
	<!-- 聊天内容发送区域 -->
	<div id="EditBox" class="am-g am-g-fixed">
	<!--style给定宽度可以影响编辑器的最终宽度-->
	<script type="text/plain" id="myEditor" style="width:100%;height:140px;"></script>
	<button id="send" type="button" class="am-btn am-btn-primary am-btn-block">发送</button>
	</div>
  
</div>
<script type="text/javascript">
function initUMEditor() {
	//实例化编辑器
    um = UM.getEditor('myEditor',{
    	initialContent:"请输入聊天信息...",
    	autoHeightEnabled:false,
    	toolbar:[
            'source | undo redo | bold italic underline strikethrough | forecolor backcolor | removeformat |',
            'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
            '| justifyleft justifycenter justifyright justifyjustify |',
            'link unlink | emotion image video  | map'
        ]
    });
}

function initBasicInfomation() {
	var success;
    $.ajax({
    	type: "post",
    	url: "<%=basePath%>initUser",
    	async: false,
    	dataType: "json",
    	data: {
    		from : "<%=fromUsrUid%>",
    		to : "<%=toUsrUid%>"
    	},
		beforeSend: function() {
		},
    	success: function(msg) {
    		if (msg.msgType == "success") {
    			var obj = msg.msgContent;
    			var fromUid = obj.from.usrUid;
    			var fromNickname = obj.from.usrNickname;
    			var toUid = obj.to.usrUid;
    			var toNickname = obj.to.usrNickname;
    			sessionStorage.setItem("fromUid", fromUid);
    			sessionStorage.setItem("fromNickname", fromNickname);
    			sessionStorage.setItem("toUid", toUid);
    			sessionStorage.setItem("toNickname", toNickname);
    			success = true;
    		} else {
    			console.log("错误代码:" + msg.msgCode);
    			console.log("错误内容" + msg.msgContent);
    			success = false;
    		}
    	},
    	error: function (XMLHttpRequest, textStatus, errorThrown) 
    	{
    		console.log("请求失败!原因:" + textStatus);
			success = false;
    	}
    });
    return success;
}

$(document).ready(function(){
	
	if (initBasicInfomation() != true) {
		console.log("初始化失败!");
		return;
	}
	
	initUMEditor();

	var fromUid = sessionStorage.getItem("fromUid");
	var toUid = sessionStorage.getItem("toUid");
	var socket = new WebSocket(
			"ws://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${pageContext.request.contextPath}/chat/"
			+ fromUid + "/" + toUid);
    //接收服务器的消息
    socket.onmessage = function(ev) {
    	//将json字符串转换为json对象
    	var obj = eval('('+ev.data+')');
    	if ($.isArray(obj)) {
			$.each(obj, function(index, value) {
				addMessage(value);
			});
		} else {
			addMessage(obj);
		}
    }
    
    $("#send").click(function() {
    	if (!um.hasContents()) {  // 判断消息输入框是否为空
            // 消息输入框获取焦点
            um.focus();
            // 添加抖动效果
            $('.edui-container').addClass('am-animation-shake');
            setTimeout("$('.edui-container').removeClass('am-animation-shake')", 1000);
        } else {
        	//获取输入框的内容
        	var txt = um.getContent();
        	var fromNickname = sessionStorage.getItem("fromNickname");
        	//构建一个标准格式的JSON对象
        	var obj = JSON.stringify({
	    		nickName: fromNickname,
	    		msgContent: txt
	    	});
            // 发送消息
            socket.send(obj);
            // 清空消息输入框
            um.setContent('');
            // 消息输入框获取焦点
            um.focus();
        }
    
    });
});

//人名nickname，时间date，是否自己isSelf，内容content，头像资源路径usrimgpath
function addMessage(msg){

	var box = $("#msgtmp").clone(); 	//复制一份模板，取名为box
	box.appendTo("#chatContent");		//把box追加到聊天面板中
	box.find('[ff="nickname"]').text(msg.nickName); //在box中设置昵称
	box.find('[ff="msgdate"]').html(msg.msgTime); 		//在box中设置时间
	box.find('[ff="content"]').html(msg.msgContent); 	//在box中设置内容
	box.addClass(msg.isSelf? 'am-comment-flip':'');	//右侧显示
	box.addClass(msg.isSelf? 'am-comment-warning':'am-comment-success');//颜色
	box.css((msg.isSelf? 'margin-left':'margin-right'),"20%");//外边距
	box.show();							//设置box状态为显示
	
	$("#ChatBox div:eq(0)").scrollTop(999999); 	//滚动条移动至最底部
	
}
</script>

</body>
</html>