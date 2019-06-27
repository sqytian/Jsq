package cn.m1c.gczj.biz.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.Answer;
import cn.m1c.gczj.biz.service.AnswerService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

@Controller
@RequestMapping("/api/answer")
public class AnswerController {
	private static Logger logger = LoggerFactory.getLogger(AnswerController.class);
	
	@Resource
	private AnswerService answerService;
	
	/**
	 * 问答列表
	 * @param request
	 * @param response
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 */
	@RequestMapping(value="/answerlist" ,method={RequestMethod.GET,RequestMethod.POST})
	public void answerList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue="1", required = false)Integer pageNum,
			@RequestParam(defaultValue="10", required = false)Integer pageSize
			){
		logger.info("/answer/answerlist");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_answer_success);
		try {
		
			Map<Integer, List<Answer>> map = answerService.getAnswerList(Integer.valueOf(pageSize),Integer.valueOf(pageNum));
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<Answer> answerList = map.get(totalCount);
			if(answerList!=null && answerList.size()>0){
				for(int i=0;i<answerList.size();i++){
					Answer answer = answerList.get(i);
					rpcResult.addArray().addDatabody("id", answer.getId())
					          //更新时间
					          .addDatabody("updateTime", DateUtils.format(answer.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
					          //回答内容
					          .addDatabody("answerContent", answer.getAnswer())
					          //提问内容
					          .addDatabody("question", answer.getQuestion());
				}
			}else{
				rpcResult.addDatabody("noresult", "暂时没有查到精华问答信息！ ");
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
	 * 问答详情
	 * @param request
	 * @param response
	 * @param id 问答id
	 */
	@RequestMapping(value="/answerdetails")
	public void inforDetails(HttpServletRequest request, HttpServletResponse response
			,String id
			){
		logger.info("/answer/answerdetails");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.answer_id_null);
			Answer answer = (Answer)answerService.selectByPrimaryKey(id);
			if(answer!=null){
					rpcResult.addDatabody("id", answer.getId())
									  //回答内容
							          .addDatabody("answerContent", answer.getAnswer())
							          //提问内容
							          .addDatabody("question", answer.getQuestion());
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
	 * 新建/修改问答
	 * @param request
	 * @param response
	 * @param id 问答id
	 * @param answerContent     回答内容
	 * @param question   提问内容
	 */
	@Security
	@RequestMapping(value="/saveanswer")
	public void saveAnswer(HttpServletRequest request, HttpServletResponse response
			,String id,String answerContent,String question
			){
		logger.info("/answer/saveanswer");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			//校验参数不为空
			AssertUtil.notNull(answerContent, GczjStatusCode.answer_null_error);
			AssertUtil.notNull(question, GczjStatusCode.question_null_error);
			String answerId = answerService.updateAnswer(id,answerContent,question);
			rpcResult.addDatabody("id", answerId);
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
	 * 删除问答
	 * @param request
	 * @param response
	 * @param id  信息id
	 */
	@Security
	@RequestMapping(value="/deletedanswer")
	public void deletedAnswer(HttpServletRequest request, HttpServletResponse response
			,String id
			){
		logger.info("/answer/deletedanswer");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.delete_answer_success);
		try {  
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.answer_id_null);
			answerService.deletedAnswer(id);
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
