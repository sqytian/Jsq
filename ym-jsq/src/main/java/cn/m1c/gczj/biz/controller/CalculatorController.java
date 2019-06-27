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
import cn.m1c.gczj.biz.model.Calculator;
import cn.m1c.gczj.biz.service.CalculatorService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 计算器类型控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/calculator")
public class CalculatorController {
	private static Logger logger = LoggerFactory.getLogger(CalculatorController.class);

	@Resource
	private CalculatorService calculatorService;

	/**
	 * 计算器列表
	 * 
	 * @param request
	 * @param response
	 */
//	@Security
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void calculatorList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize
			) {
		logger.info("/api/calculator/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			Map<Integer, List<Calculator>> map = calculatorService.getCalculatorList(Integer.valueOf(pageSize), Integer.valueOf(pageNum));
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<Calculator> calculatorList = map.get(totalCount);
			if (calculatorList != null && calculatorList.size() > 0) {
				for (int i = 0; i < calculatorList.size(); i++) {
					Calculator calculator = calculatorList.get(i);
					rpcResult.addArray().addDatabody("id", calculator.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(calculator.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 计算器类型编号
							.addDatabody("type", calculator.getType())
							.addDatabody("standardName", calculator.getRemark())
							// 计算器类型名称
							.addDatabody("calculatorName", calculator.getCalculatorName());
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有计算器信息！ ");
			}
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
		}
		// 接口返回值
		rpcResult.outCrossOrigin(response);
	}

	/**
	 * 计算器详情
	 * 
	 * @param request
	 * @param response
	 * @param id
	 */
//	@Security
	@RequestMapping(value = "/tail", method = { RequestMethod.GET, RequestMethod.POST })
	public void calculatorTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/calculator/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			Calculator calculator = (Calculator) calculatorService.selectByPrimaryKey(id);
			if (calculator != null) {
				rpcResult.addDatabody("id", calculator.getId())
						// 更新时间
						.addDatabody("updateTime",
								DateUtils.format(calculator.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
						// 计算器类型编号
						.addDatabody("type", calculator.getType())
						.addDatabody("standardName", calculator.getRemark())
						// 计算器类型名称
						.addDatabody("calculatorName", calculator.getCalculatorName());
			}
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
		}
		// 接口返回值
		rpcResult.outCrossOrigin(response);
	}

	/**
	 * 新建/修改calculator
	 * @param request
	 * @param response
	 * @param id
	 * @param type         计算器类型
	 * @param calculatorName         
	 */
	@Security
	@RequestMapping(value = "/savecalculator", method = { RequestMethod.GET, RequestMethod.POST })
	public void saveCalculator(HttpServletRequest request, HttpServletResponse response
			, String id, Integer type,String calculatorName,String standardName
			) {
		logger.info("/api/calculator/savecalculator");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String calculatorId = calculatorService.updateCalculator(id, Integer.valueOf(type), calculatorName,standardName);
			rpcResult.addDatabody("id", calculatorId);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
		}
		// 接口返回值
		rpcResult.outCrossOrigin(response);
	}

	/**
	 * 删除计算器类型
	 * 
	 * @param request
	 * @param response
	 * @param id     表单单行id
	 */
	@Security
	@RequestMapping(value = "/deletedcalculator", method = { RequestMethod.GET, RequestMethod.POST })
	public void deletedCalculator(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/calculator/deletedcalculator");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.delete_table_success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			calculatorService.deletedCalculator(id);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
		}
		// 接口返回值
		rpcResult.outCrossOrigin(response);
	}
}
