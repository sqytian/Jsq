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
import cn.m1c.gczj.biz.model.CostTable;
import cn.m1c.gczj.biz.service.CostTableService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 造价计算器控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/cost")
public class CostTableController {
	private static Logger logger = LoggerFactory.getLogger(CostTableController.class);

	@Resource
	private CostTableService costTableService;

	/**
	 * costtable列表
	 * 
	 * @param request
	 * @param response
	 * @param type  计算器类型
	 */
//	@Security
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void costTableList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize, String areaCode
			) {
		logger.info("/api/cost/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			Map<Integer, List<CostTable>> map = costTableService.getTableList(Integer.valueOf(pageSize), Integer.valueOf(pageNum), areaCode);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<CostTable> tableList = map.get(totalCount);
			if (tableList != null && tableList.size() > 0) {
				for (int i = 0; i < tableList.size(); i++) {
					CostTable table = tableList.get(i);
					rpcResult.addArray().addDatabody("id", table.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(table.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 内容类型：text,select
							.addDatabody("contentType", table.getContentType())
							// 表行名：工程造价，专业调整，折扣浮动
							.addDatabody("item", table.getItem())
							// 参数名称
							.addDatabody("parameters", table.getParameters())
							// 预置值，json
							.addDatabody("presetValue", table.getPresetValue()).addDatabody("remark", table.getRemark())
							// 表类型：1内容表；2，结果表
							.addDatabody("tableType", table.getTableType())
							// 地区区号
							.addDatabody("areaCode", table.getAreaCode())
							// 单位:万元，%
							.addDatabody("unit", table.getUnit())
							// 表行名key
							.addDatabody("itemKey", table.getItemKey())
							// 默认值
							.addDatabody("defaultValue", table.getDefaultValue())
							// 序号
							.addDatabody("orderNumber", table.getOrderNumber())
							.addDatabody("additionId", table.getAdditionId())
							// 取值范围(0,n);(-100,100)
							.addDatabody("valueRange", table.getValueRange());
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该地区造价计算器信息！ ");
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
	 * costtable单行详情
	 * 
	 * @param request
	 * @param response
	 * @param id
	 */
//	@Security
	@RequestMapping(value = "/tail", method = { RequestMethod.GET, RequestMethod.POST })
	public void costTableTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/cost/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			CostTable table = (CostTable) costTableService.selectByPrimaryKey(id);
			if (table != null) {
				rpcResult.addDatabody("id", table.getId())
						// 更新时间
						.addDatabody("updateTime",
								DateUtils.format(table.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
						// 内容类型：text,select
						.addDatabody("contentType", table.getContentType())
						// 表行名：工程造价，专业调整，折扣浮动
						.addDatabody("item", table.getItem())
						// 参数名称
						.addDatabody("parameters", table.getParameters())
						// 预置值，json
						.addDatabody("presetValue", table.getPresetValue()).addDatabody("remark", table.getRemark())
						// 表类型：1内容表；2，结果表
						.addDatabody("tableType", table.getTableType())
						// 地区区号
						.addDatabody("areaCode", table.getAreaCode())
						// 单位:万元，%
						.addDatabody("unit", table.getUnit())
						// 表行名key
						.addDatabody("itemKey", table.getItemKey())
						// 默认值
						.addDatabody("defaultValue", table.getDefaultValue())
						// 序号
						.addDatabody("orderNumber", table.getOrderNumber())
						.addDatabody("additionId", table.getAdditionId())
						// 取值范围(0,n);(-100,100)
						.addDatabody("valueRange", table.getValueRange());
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
	 * 新建/修改costtable
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param areaCode      地区区号
	 * @param item         表行名：工程造价，专业调整，折扣浮动
	 * @param contentType   内容类型：text,select
	 * @param valueRange    取值范围(0,n);(-100,100)
	 * @param unit          单位:万元，%
	 * @param presetValue   预置值，json
	 * @param parameters    参数名称
	 * @param tableType     表类型：1内容表；2，结果表
	 * @param remark         备用
	 * @param itemKey        表行名key
	 * @param defaultValue   默认值
	 * @param orderNumber     序号
	 */
	@Security
	@RequestMapping(value = "/savetable", method = { RequestMethod.GET, RequestMethod.POST })
	public void saveTable(HttpServletRequest request, HttpServletResponse response
			, String id, String areaCode,
			String item, String contentType, String valueRange, String unit, String presetValue, String parameters,
			Integer tableType, String remark, String itemKey, String defaultValue, Integer orderNumber,String additionId
			) {
		logger.info("/api/cost/savetable");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			String tableId = costTableService.updateTable(id, areaCode, item,contentType, valueRange, unit, presetValue,
					parameters, Integer.valueOf(tableType), remark, defaultValue, itemKey,
					Integer.valueOf(orderNumber),additionId);
			rpcResult.addDatabody("id", tableId);
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
	 * 删除表单单行记录
	 * 
	 * @param request
	 * @param response
	 * @param id     表单单行id
	 */
	@Security
	@RequestMapping(value = "/deletedtable", method = { RequestMethod.GET, RequestMethod.POST })
	public void deletedTable(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/cost/deletedtable");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.delete_table_success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.table_line_id_null);
			costTableService.deletedTable(id);
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
