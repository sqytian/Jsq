package cn.m1c.gczj.biz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.ProjectDocument;
import cn.m1c.gczj.biz.service.ProjectDocumentService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.User;
import cn.m1c.gczj.person.service.UserService;
import cn.m1c.gczj.utils.WordUtils;

/**
 * 生成文档控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/projectdocument")
public class ProjectDocumentController {
	private static Logger logger = LoggerFactory.getLogger(ProjectDocumentController.class);

	@Resource
	private ProjectDocumentService projectDocumentService;
	@Resource
	private UserService userService;
	
	/**
	 * 生成文档
	 * @param request
	 * @param response
	 * @param calculatorType 计算器类型
	 * @param projectName    工程名称
	 * @param constructionUnit   建设单位
	 * @param projectProfile   工程概况
	 * @param totalMoney     工程总投资
	 */
//	@Security
	@RequestMapping(value = "/createprojectdocument")
	public void createProjectDocument(HttpServletRequest request, HttpServletResponse response
			,String calculatorType,String projectName,String constructionUnit,String projectProfile,
			String totalMoney
			) {
		logger.info("/projectdocument/createprojectdocument");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			//获取用户信息
//			User user = (User)HttpHelper.getAuthUser(request);
//			AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
//			Integer times = user.getTimes();
//			if(times==0){
//				 AssertUtil.validate(false, GczjStatusCode.user_times_error);	
//			}
			// 校验参数不为空
//			String calculatorType="1";
//			String projectName=null;
//			String constructionUnit=null;
//			String projectProfile=null;
//			String totalMoney=null;
			 AssertUtil.notNull(calculatorType, GczjStatusCode.calculator_typ_null);
			 if(!StringUtils.hasLength(projectName)){
				 projectName=" ";
			 }
			 if(!StringUtils.hasLength(constructionUnit)){
				 constructionUnit=" ";
			 }
			 if(!StringUtils.hasLength(projectProfile)){
				 projectProfile=" ";
			 }
			 if(!StringUtils.hasLength(totalMoney)){
				 totalMoney=" ";
			 }
			// 获得数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("projectName", projectName);
			map.put("constructionUnit", constructionUnit);
			map.put("projectProfile", projectProfile);
			map.put("totalMoney", totalMoney);
			
			StringBuffer standardDocument = new StringBuffer();
			StringBuffer costDocument = new StringBuffer();
			String[] split = calculatorType.split(",");
			int n =0; 
			for (String type : split) {
				n++;
				List<ProjectDocument> projectDocumentList = projectDocumentService.createProjectDocument(Integer.valueOf(type));
					ProjectDocument projectDocument1 = projectDocumentList.get(0);
					standardDocument.append(n+"."+projectDocument1.getStandardDocument()+"<w:p></w:p>");
					ProjectDocument projectDocument2 = projectDocumentList.get(0);
					costDocument.append(n+"."+projectDocument2.getStandardDocument()+"<w:p></w:p>");
			}
			map.put("standardDocument", standardDocument.toString());
			map.put("costDocument", standardDocument.toString());
			WordUtils.exportMillCertificateWord(response,map);
//			user.setTimes(times-1);
//			userService.updateByPrimaryKeySelective(user);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
		}
		// 接口返回值
//		rpcResult.outCrossOrigin(response);
	}
	
	
	

}
