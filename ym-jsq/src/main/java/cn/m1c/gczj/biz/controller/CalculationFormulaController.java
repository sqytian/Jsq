package cn.m1c.gczj.biz.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.CalculationFormula;
import cn.m1c.gczj.biz.service.CalculationFormulaService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 其他计算器计算公式
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/calculationformula")
public class CalculationFormulaController {
	private static Logger logger = LoggerFactory.getLogger(CalculationFormulaController.class);

	@Resource
	private CalculationFormulaService calculationFormulaService;

	/**
	 * 计算公式列表
	 * 
	 * @param request
	 * @param response
	 * @param calculatorType
	 */
	@Security
	@RequestMapping(value = "/list")
	public void calculationFormulaList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize, 
			Integer calculatorType,String calculatorItem,String formulaId
			) {
		logger.info("/calculationformula/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		Admin admin = (Admin)HttpHelper.getAuthUser(request);
		AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
		try {
			Map<Integer, List<CalculationFormula>> map = calculationFormulaService.getCalculationFormulaList(pageSize,
					pageNum, calculatorType,calculatorItem,formulaId);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<CalculationFormula> calculationFormulaList = map.get(totalCount);
			if (calculationFormulaList != null && calculationFormulaList.size() > 0) {
				for (int i = 0; i < calculationFormulaList.size(); i++) {
					CalculationFormula calculationFormula = calculationFormulaList.get(i);
					rpcResult.addArray().addDatabody("id", calculationFormula.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(calculationFormula.getUpdated(),
											DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 计算项目
							.addDatabody("calculatorItem", calculationFormula.getCalculatorItem())
							// 计算器类型
							.addDatabody("calculatorType", calculationFormula.getCalculatorType())
							// 价格
							.addDatabody("price", calculationFormula.getPrice())
							// 公式
							.addDatabody("rate", calculationFormula.getRate())
							// 公式级别
							.addDatabody("rateLevel", calculationFormula.getRateLevel())
							.addDatabody("remark", calculationFormula.getRemark());
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该类型计算公式的信息！ ");
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
	 * 公式详情
	 * @param request
	 * @param response
	 * @param id
	 */
	@Security
	@RequestMapping(value = "/tail")
	public void calculationFormulaTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/calculationformula/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		Admin admin = (Admin)HttpHelper.getAuthUser(request);
		AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
		try {
			CalculationFormula calculationFormula = (CalculationFormula) calculationFormulaService
					.selectByPrimaryKey(id);
			if (calculationFormula != null) {
				rpcResult.addDatabody("id", calculationFormula.getId())
						// 更新时间
						.addDatabody("updateTime",
								DateUtils.format(calculationFormula.getUpdated(),
										DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
						// 计算项目
						.addDatabody("calculatorItem", calculationFormula.getCalculatorItem())
						// 计算器类型
						.addDatabody("calculatorType", calculationFormula.getCalculatorType())
						// 价格
						.addDatabody("price", calculationFormula.getPrice())
						// 公式
						.addDatabody("rate", calculationFormula.getRate())
						// 公式级别
						.addDatabody("rateLevel", calculationFormula.getRateLevel())
						.addDatabody("remark", calculationFormula.getRemark());
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
	 * 新建/修改计算公式
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param rate
	 *            公式
	 * @param rateLevel
	 *            公式级别
	 * @param price
	 *            价格
	 * @param calculatorType
	 *            计算器类型
	 * @param calculatorItem
	 *            计算项目
	 * @param remark
	 */
	@Security
	@RequestMapping(value = "/savecalculationformula")
	public void saveCalculationFormula(HttpServletRequest request, HttpServletResponse response
			, String id, String rate,
			Integer rateLevel, BigDecimal price, Integer calculatorType, String formulaId, String remark
			) {
		logger.info("/calculationformula/savecalculationformula");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String rateId = calculationFormulaService.updateCalculationFormula(id, rate, Integer.valueOf(rateLevel), price,
					Integer.valueOf(calculatorType), formulaId, remark);
			rpcResult.addDatabody("id", rateId);
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
	 * 删除单行公式
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            表单单行id
	 */
	@Security
	@RequestMapping(value = "/deletedcalculationformula")
	public void deletedAnswer(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/calculationformula/deletedcalculationformula");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.calculation_formula_id_null);
			calculationFormulaService.deletedCalculationFormula(id);
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
