package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.HttpRequestUtil;
import util.JSONUtil;
import bean.ResultInfo;
import bean.SystemConfigurationInfo;
import bean.UserInfo;
import cache.ResultCodeStorage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import exception.IllegalEmptyStringException;

@WebServlet("/initUser")
public class InitUserInfoAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2317460895038408468L;
	
	/**
	 * Constructor of the object.
	 */
	public InitUserInfoAction() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fromUsrUid = request.getParameter("from");
			String toUsrUid = request.getParameter("to");
			if (fromUsrUid == null || toUsrUid == null || "".equals(fromUsrUid) || "".equals(toUsrUid)) {
				throw new IllegalEmptyStringException("传入参数非法!");
			}
			String initUrl = SystemConfigurationInfo.getMainSystemBasePath() + "systemapis.action";
			String initParam = "which=getChatUsersInfo&from=" + fromUsrUid + "&to=" + toUsrUid;
			String apiReply = HttpRequestUtil.sendPost(initUrl, initParam);
			JSONUtil json = new JSONUtil(apiReply);
			if ("success".equals(json.resolveJSONString("msgType"))) {
				Gson gson = new Gson();
				Type resultType2 = new TypeToken<Map<String, UserInfo>>(){}.getType();
				String str_msgContent = json.resolveJSONString("msgContent");
				Map<String, UserInfo> fin = gson.fromJson(str_msgContent, resultType2);
				UserInfo fromUsrInfo = fin.get("from");
				UserInfo toUsrInfo = fin.get("to");
				request.getSession().setAttribute("from", fromUsrInfo);
				request.getSession().setAttribute("to", toUsrInfo);
				ResultInfo<Map<String, UserInfo>> rs = new ResultInfo<Map<String,UserInfo>>();
				rs.setMsgCode(ResultCodeStorage.code_success);
				rs.setMsgType("success");
				rs.setMsgContent(fin);
				sendMsgtoWeb(rs, response);
			} else {
				Gson gson = new Gson();
				Type resultType2 = new TypeToken<ResultInfo<String>>(){}.getType();
				ResultInfo<String> rs = gson.fromJson(apiReply, resultType2);
				sendMsgtoWeb(rs, response);
			}
		} catch (IllegalEmptyStringException e) {
			e.printStackTrace();
			ResultInfo<String> rs = new ResultInfo<String>();
			rs.setMsgCode(ResultCodeStorage.code_err_missing_parameter);
			rs.setMsgType("error");
			rs.setMsgContent(ResultCodeStorage.getResultCodeDescription(ResultCodeStorage.code_err_missing_parameter));
			sendMsgtoWeb(rs, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ResultInfo<String> rs = new ResultInfo<String>();
			rs.setMsgType("error");
			rs.setMsgCode(ResultCodeStorage.code_err_unknown_server_internal_exception);
			rs.setMsgContent(ResultCodeStorage.getResultCodeDescription(ResultCodeStorage.code_err_unknown_server_internal_exception));
			sendMsgtoWeb(rs, response);
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	private void sendMsgtoWeb(ResultInfo<?> result, HttpServletResponse response) {
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;

		try {
			out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(result));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
	}

}
