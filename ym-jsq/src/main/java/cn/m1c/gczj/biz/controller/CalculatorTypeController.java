package cn.m1c.gczj.biz.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.CalculatorType;
import cn.m1c.gczj.biz.service.CalculatorTypeService;
import cn.m1c.gczj.biz.status.GczjStatusCode;

@Controller
@RequestMapping("/api/calculator")
public class CalculatorTypeController {
	private static Logger logger = LoggerFactory.getLogger(CalculatorTypeController.class);

	@Resource
	private CalculatorTypeService calculatorTypeService;

	/**
	 * 计算器类型
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/calculatortypelist")
	public void calculatorTypeList(HttpServletRequest request, HttpServletResponse response
			,Integer type,String formulaId) {
		logger.info("/calculator/calculatortypelist");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			// 校验参数不为空
			AssertUtil.notNull(type, GczjStatusCode.calculator_type_null);
			AssertUtil.notNull(formulaId, GczjStatusCode.formula_id_null);
			List<CalculatorType> calculatorTypeList = calculatorTypeService.getCalculatorTypeList(Integer.valueOf(type), formulaId);
			if (calculatorTypeList != null && calculatorTypeList.size() > 0) {
				for (int i = 0; i < calculatorTypeList.size(); i++) {
					CalculatorType calculatorType = calculatorTypeList.get(i);
					String presetValue = calculatorType.getPresetValue();
					JSONArray jsonArrayss = null;
					if (presetValue != null) {
						JSONObject json = JSONObject.parseObject(presetValue);
						jsonArrayss = JSONObject.parseArray(json.getString("data"));
					}
					rpcResult.addArray().addDatabody("id", calculatorType.getId())
							// 计算项
							.addDatabody("item", calculatorType.getItem())
							// 内容类型
							.addDatabody("contentType", calculatorType.getContentType())
							// 单位
							.addDatabody("unit", calculatorType.getUnit())
							// 预置值
							.addDatabody("presetValue", jsonArrayss)
							// 参数名称
							.addDatabody("parameters", calculatorType.getParameters())
							// 范围
							.addDatabody("valueRange", calculatorType.getValueRange());
				}
			}else{
				List<CalculatorType> calculatorTypeDefault = calculatorTypeService.getCalculatorTypeList(Integer.valueOf(type), String.valueOf(0));
					if (calculatorTypeDefault != null && calculatorTypeDefault.size() > 0) {
						for (int i = 0; i < calculatorTypeDefault.size(); i++) {
							CalculatorType calculatorType = calculatorTypeDefault.get(i);
							rpcResult.addArray().addDatabody("id", calculatorType.getId())
									// 计算项
									.addDatabody("item", calculatorType.getItem())
									// 内容类型
									.addDatabody("contentType", calculatorType.getContentType())
									// 单位
									.addDatabody("unit", calculatorType.getUnit())
									// 预置值
									.addDatabody("presetValue", null)
									// 参数名称
									.addDatabody("parameters", calculatorType.getParameters())
									// 范围
									.addDatabody("valueRange", calculatorType.getValueRange());
						}
			        }
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
