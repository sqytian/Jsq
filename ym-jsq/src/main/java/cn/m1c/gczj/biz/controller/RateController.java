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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.service.RateService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 造价计算器计算公式控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/rate")
public class RateController {
	private static Logger logger = LoggerFactory.getLogger(RateController.class);

	@Resource
	private RateService rateService;

	/**
	 * 造价计算器计算公式列表
	 * 
	 * @param request
	 * @param response
	 * @param formulaId 造价计算器计算项id
	 */
	@Security
	@RequestMapping(value = "/list")
	public void rateList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize, String formulaId
			) {
		logger.info("/rate/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			Map<Integer, List<Rate>> map = rateService.getRateList(Integer.valueOf(pageSize),
					Integer.valueOf(pageNum),formulaId);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<Rate> rateList = map.get(totalCount);
			if (rateList != null && rateList.size() > 0) {
				for (int i = 0; i < rateList.size(); i++) {
					Rate rateTable = rateList.get(i);
					rpcResult.addArray().addDatabody("id", rateTable.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(rateTable.getUpdated(),
											DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 地区区号
							.addDatabody("areaCode", rateTable.getAreaCode())
							// 计算项Id
							.addDatabody("formulaId", rateTable.getFormulaId())
							// 价格
							.addDatabody("price", rateTable.getPrice())
							// 公式
							.addDatabody("rate", rateTable.getRate())
							// 公式级别
							.addDatabody("rateLevel", rateTable.getRateLevel())
							.addDatabody("remark", rateTable.getRemark());
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
	 * 造价计算器计算公式详情
	 * @param request
	 * @param response
	 * @param id
	 */
	@Security
	@RequestMapping(value = "/tail")
	public void rateTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/rate/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			Rate rateTable = (Rate) rateService
					.selectByPrimaryKey(id);
			if (rateTable != null) {
				rpcResult.addDatabody("id", rateTable.getId())
						// 更新时间
						.addDatabody("updateTime",
								DateUtils.format(rateTable.getUpdated(),
										DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
						// 地区区号
						.addDatabody("areaCode", rateTable.getAreaCode())
						// 计算项Id
						.addDatabody("formulaId", rateTable.getFormulaId())
						// 价格
						.addDatabody("price", rateTable.getPrice())
						// 公式
						.addDatabody("rate", rateTable.getRate())
						// 公式级别
						.addDatabody("rateLevel", rateTable.getRateLevel())
						.addDatabody("remark", rateTable.getRemark());
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
	 * 新建/修改造价计算器计算公式
	 * @param request
	 * @param response
	 * @param id
	 * @param rate
	 *            公式
	 * @param rateLevel
	 *            公式级别
	 * @param price
	 *            价格
	 * @param areaCode 地区区号
	 * @param formulaId 造价计算器计算项
	 * @param remark  
	 */
	@Security
	@RequestMapping(value = "/saverate")
	public void saveRate(HttpServletRequest request, HttpServletResponse response
			, String id, String rate,Integer rateLevel,
			BigDecimal price, String areaCode, String formulaId, String remark
			) {
		logger.info("/rate/saverate");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			String rateId = rateService.updateRate(id, rate, Integer.valueOf(rateLevel), price,
					areaCode, formulaId, remark);
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
	 * 删除造价计算器单行计算公式
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单行公式id
	 */
	@Security
	@RequestMapping(value = "/deletedrate")
	public void deletedRate(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/rate/deletedrate");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.rate_id_null);
			rateService.deletedRate(id);
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
