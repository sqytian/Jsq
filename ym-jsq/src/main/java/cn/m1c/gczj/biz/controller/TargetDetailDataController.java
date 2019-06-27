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
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.TargetDetailData;
import cn.m1c.gczj.biz.service.CreateTableService;
import cn.m1c.gczj.biz.service.TargetDetailDataService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.utils.DateUtils;

@Controller
@RequestMapping("/api/targetdata")
public class TargetDetailDataController {
	private static Logger logger = LoggerFactory.getLogger(TargetDetailDataController.class);

	@Resource
	private TargetDetailDataService targetDetailDataService;

	/**
	 * 列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void tableList(HttpServletRequest request, HttpServletResponse response
			) {
		logger.info("/api/targetdata/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {

		   List<TargetDetailData> list= targetDetailDataService.getList();
			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					TargetDetailData model = list.get(i);
					rpcResult.addArray().addDatabody("id", model.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(model.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 月
							.addDatabody("monthData", model.getMonthData())
							// 价格
							.addDatabody("priceData", model.getPriceData())
							// 工程名
							.addDatabody("projectName", model.getProjectName())
							// 年
							.addDatabody("yearData", model.getYearData());
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该类型计算器信息！ ");
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
	

	
}
