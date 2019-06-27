package cn.m1c.gczj.biz.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.utils.StringUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.Information;
import cn.m1c.gczj.biz.service.InformationService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;

@Controller
@RequestMapping("/api/infor")
public class InformaticaController {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	public InformationService informationService;
	
	/**
	 * 获取造价指标列表
	 * @param request
	 * @param response
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @param putaway  是否上架：上架 1
	 * @param type     类型 ：造价实例
	 */
	@RequestMapping(value="/inforlist")
	public void inforList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize,
			Integer putaway,String type){
		logger.info("/infor/inforlist");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			Map<Integer, List<Information>> map = informationService.getInformationList(Integer.valueOf(pageSize), Integer.valueOf(pageNum),Integer.valueOf(putaway),type);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<Information> informationList = map.get(totalCount);
			if(informationList!=null && informationList.size()>0){
				for(int i=0;i<informationList.size();i++){
					Information information = informationList.get(i);
					rpcResult.addArray().addDatabody("id", information.getId())
					          //标题
					          .addDatabody("title", information.getTitle())
					          //整理时间
					          .addDatabody("time", information.getTime())
					          //类型
					          .addDatabody("type", information.getType())
					          //工程地点
					          .addDatabody("projectSite", information.getNumber())
					          //是否上架
					          .addDatabody("putaway", information.getPutaway())
					          //造价实例的结构类型
					          .addDatabody("classify", information.getClassify());
				}
			}else{
				rpcResult.addDatabody("noresule", "目前没有信息！ ");
			}
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 造价指标详情
	 * @param request
	 * @param response
	 * @param id 造价指标id
	 */
	@RequestMapping(value="/infordetails")
	public void inforDetails(HttpServletRequest request, HttpServletResponse response
			,String id){
		logger.info("/infor/infordetails");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.infor_id_null);
			Information information = (Information)informationService.selectByPrimaryKey(id);
			if(information!=null){
					rpcResult.addDatabody("id", information.getId())
					          //信息的标题
					          .addDatabody("title", information.getTitle())
					          //内容
					          .addDatabody("content", information.getContent());
			}
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 造价指标上下架
	 * @param request
	 * @param response
	 * @param id       造价指标id
	 * @param putaway  1：上架；0：下架
	 */
//	@Security
	@RequestMapping(value="/putawayinfor")
	public void putawayInfor(HttpServletRequest request, HttpServletResponse response
			,String id,Integer putaway){
		logger.info("/infor/putawayinfor");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//获取用户信息
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.infor_id_null);
			AssertUtil.notNull(putaway, GczjStatusCode.putaway_null);
			Information information = new Information();
			information.setId(id);
			if(putaway == 1) {
				information.setPutaway(true);
			} else if(putaway == 0) {
				information.setPutaway(false);
			}
			informationService.updateByPrimaryKeySelective(information);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 新建/修改造价指标
	 * @param request
	 * @param response
	 * @param id  造价指标id
	 * @param title     标题
	 * @param content   内容
	 * @param classify  造价实例的结构类型
	 * @param projectSite    工程地点
	 * @param time      整理时间
	 * @param type      类型
	 */
//	@Security
	@RequestMapping(value="/saveinfor")
	public void saveInfor(HttpServletRequest request, HttpServletResponse response){
		logger.info("/infor/saveinfor");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//获取用户信息
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String paramJson = (String)request.getAttribute("paramJson");
			logger.info("attribute:"+paramJson);
			JSONObject parseObject = JSON.parseObject(paramJson);
			logger.info("parseObject:"+parseObject);
			String id = parseObject.getString("id");
			logger.info("id:"+id);
			String title = parseObject.getString("title");
			logger.info("title:"+title);
			String content = parseObject.getString("content");
			logger.info("content:"+content);
			String classify = parseObject.getString("classify");
			logger.info("classify:"+classify);
			String projectSite = parseObject.getString("projectSite");
			logger.info("projectSite:"+projectSite);
			String time = parseObject.getString("time");
			logger.info("time:"+time);
			String type = parseObject.getString("type");
			logger.info("type:"+type);
			//校验参数不为空
			AssertUtil.notNull(title, GczjStatusCode.title_null);
			AssertUtil.notNull(content, GczjStatusCode.content_null);
			AssertUtil.notNull(type, GczjStatusCode.type_null);
			informationService.updateInfor(id,title,content,classify,projectSite,time,type);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 删除造价指标
	 * @param request
	 * @param response
	 * @param id  指标id
	 */
//	@Security
	@RequestMapping(value="/deletedinfor")
	public void deletedInfor(HttpServletRequest request, HttpServletResponse response){
		logger.info("/infor/deletedinfor");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {  
			//获取用户信息
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String paramJson = (String)request.getAttribute("paramJson");
			logger.info("attribute:"+paramJson);
			JSONObject parseObject = JSON.parseObject(paramJson);
			logger.info("parseObject:"+parseObject);
			String id = parseObject.getString("id");
			logger.info("id:"+id);
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.infor_id_null);
			informationService.deletedInfor(id);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
}
