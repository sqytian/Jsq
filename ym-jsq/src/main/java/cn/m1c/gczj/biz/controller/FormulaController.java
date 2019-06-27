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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.Formula;
import cn.m1c.gczj.biz.service.FormulaService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 造价计算器计算项控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/formulaproject")
public class FormulaController {
	private static Logger logger = LoggerFactory.getLogger(FormulaController.class);

	@Resource
	private FormulaService formulaService;

	/**
	 * 造价计算器计算项列表
	 * 
	 * @param request
	 * @param response
	 * @param areaCode 地区区号
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void formulaList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize,
			String areaCode,Integer type
			) {
		logger.info("/api/formulaproject/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			Map<Integer, List<Formula>> map = formulaService.getFormulaList(Integer.valueOf(pageSize), Integer.valueOf(pageNum), 
					areaCode,type);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<Formula> formulaList = map.get(totalCount);
			if (formulaList != null && formulaList.size() > 0) {
				for (int i = 0; i < formulaList.size(); i++) {
					Formula formula = formulaList.get(i);
					rpcResult.addArray().addDatabody("id", formula.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(formula.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 地区区号  
							.addDatabody("areaCode", formula.getAreaCode())
							// 地区名称 
							.addDatabody("areaName", formula.getAreaName())
							// 计算项名称
							.addDatabody("formulaName", formula.getFormulaName())
							// 标准名称
							.addDatabody("standardName", formula.getStandardName());
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该类型造价计算器计算项信息！ ");
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
	 * 造价计算器计算项单行详情
	 * 
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/tail", method = { RequestMethod.GET, RequestMethod.POST })
	public void formulaTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/formulaproject/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			Formula formula = (Formula) formulaService.selectByPrimaryKey(id);
			if (formula != null) {
				rpcResult.addDatabody("id", formula.getId())
						// 更新时间
						.addDatabody("updateTime",
								DateUtils.format(formula.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
						// 计算项名称
						.addDatabody("formulaName", formula.getAreaCode())
						// 地区区号
						.addDatabody("areaCode", formula.getAreaName())
						// 地区名称
						.addDatabody("areaName", formula.getFormulaName())
						// 标准名称
						.addDatabody("standardName", formula.getStandardName());
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
	 * 新建/修改造价计算器计算项
	 * @param request
	 * @param response
	 * @param id
	 * @param formulaName  计算项名称
	 * @param areaCode     地区区号
	 * @param areaName     地区名称
	 * @param standardName  标准名称
	 */
	@Security
	@RequestMapping(value = "/saveformula", method = { RequestMethod.GET, RequestMethod.POST })
	public void saveFormula(HttpServletRequest request, HttpServletResponse response
			, String id,String formulaName, String areaCode, String areaName, 
			String standardName,Integer type
			) {
		logger.info("/api/formulaproject/saveformula");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			String formulaId = formulaService.updateFormula(id,formulaName,areaCode,areaName,standardName,type);
			rpcResult.addDatabody("id", formulaId);
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
	 * 删除造价计算器计算项
	 * 
	 * @param request
	 * @param response
	 * @param id     造价计算器计算项id
	 */
	@Security
	@RequestMapping(value = "/deletedformula", method = { RequestMethod.GET, RequestMethod.POST })
	public void deletedFormula(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/formulaproject/deletedformula");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.formula_id_null);
			formulaService.deletedFormula(id);
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
