package cn.m1c.gczj.biz.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.model.BaseModel;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.StringUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.CalculationFormula;
import cn.m1c.gczj.biz.model.Formula;
import cn.m1c.gczj.biz.model.SuperviseRate;
import cn.m1c.gczj.biz.service.CalculationFormulaService;
import cn.m1c.gczj.biz.service.FormulaService;
import cn.m1c.gczj.biz.service.SuperviseRateService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.utils.ComputeUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;


/**
 * 监理费计算器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/supervise")
public class SuperviseRateController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	public SuperviseRateService superviseRateService;
	@Resource
	public CalculationFormulaService calculationFormulaService;
	@Resource
	public FormulaService formulaService;
	
	
	/**
	 * 监理费计算
	 * @param request
	 * @param response
	 * @param totalMoney  总投资，例如2000万元
	 * @param regulationFactor  专业调整系数
	 * @param complexFactor  复杂调整系数
	 * @param elevationFactor    高程调整系数
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 */
	@RequestMapping(value="/projectcost",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectCost(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String regulationFactor,String complexFactor,
			String elevationFactor,String floatFactor,String specialDiscount,Integer calculatorType
			,String calculatorTtem){
		logger.info("/supervise/projectcost");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(regulationFactor, GczjStatusCode.regulation_factor_null);
			AssertUtil.notNull(complexFactor, GczjStatusCode.complex_factor_null);
			AssertUtil.notNull(elevationFactor, GczjStatusCode.elevation_factor_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(regulationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(complexFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(elevationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal standardPrice =null;
			BigDecimal totailPrice =null;
			BigDecimal discountCost=null;
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//监理费基价
//				BigDecimal bmoney=new BigDecimal(totalMoney); 
			    basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				//(1)施工监理服务收费=施工监理服务收费基准价×（1±浮动幅度值）
				//(2)施工监理服务收费基准价=施工监理服务收费基价×专业调整系数×工程复杂程度调整系数×高程调整系数
				//施工监理服务收费基准价
				standardPrice = basePrice.multiply(new BigDecimal(regulationFactor)).multiply(new BigDecimal(complexFactor))
				.multiply(new BigDecimal(elevationFactor));
				//施工监理服务收费
				totailPrice = standardPrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				//折后费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("basePrice",basePrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("standardPrice",standardPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			 //接口返回值
		 } catch (M1CRuntimeException e) {
				rpcResult = RpcResult.status(e.getStatusCode());
				logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	
	/**
	 * 建设项目管理费计算（完毕）
	 * @param request
	 * @param response
	 * @param calculatorType 计算器类型
	 * @param totalMoney  总投资，例如2000万元
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 */
	@RequestMapping(value="/projectmanagement",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectManagement(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String floatFactor,String specialDiscount,Integer calculatorType
			,String calculatorTtem
			){
		logger.info("/supervise/projectmanagement");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			BigDecimal standardPrice=null;
			
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//费基价
//				BigDecimal bmoney=new BigDecimal(totalMoney); 
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				
				//折扣前收费
				totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("basePrice",basePrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	
	/**
	* 项目前期工程咨询费-项目建议书与可行性研究报告收费（完毕）
	* @param request
	* @param response
	* @param calculatorType 计算器类型
	* @param calculatorTtem  计算项目
	* @param totalMoney  总投资，例如2000万元
	* @param regulationFactor  行业调整系数
	* @param complexFactor  复杂调整系数  需要做限制（0.8~1.2）
	* @param floatFactor       浮动幅度
	* @param specialDiscount  优惠折扣
	*/
	@RequestMapping(value="/projectbefore",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectBefore(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String regulationFactor,String complexFactor,
			String floatFactor,String specialDiscount,Integer calculatorType
			,String calculatorTtem
			){
		logger.info("/supervise/projectbefore");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(calculatorTtem, GczjStatusCode.project_name_null);
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(regulationFactor, GczjStatusCode.regulation_factor_null);
			AssertUtil.notNull(complexFactor, GczjStatusCode.complex_factor_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(regulationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(complexFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			//需要做限制（0.8~1.2）
			AssertUtil.hasErrors(new Double(complexFactor)>=1.2||0.8>=new Double(complexFactor), GczjStatusCode.complex_factor_error);
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			BigDecimal standardPrice=null;
			
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//费基价
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				//项目前期工程咨询费-项目建议书与可行性研究报告收费
				standardPrice = basePrice.multiply(new BigDecimal(regulationFactor)).multiply(new BigDecimal(complexFactor));
				
				//折扣前收费
				totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
//			rpcResult.addDatabody("basePrice",basePrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 招标代理费计算器（完毕） 
	 * @param request
	 * @param response
	 * @param calculatorType 计算器类型 
	 * @param calculatorTtem  计算项目 （根据项目）计算
	 * @param totalMoney  总投资，例如2000万元
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 */
	@RequestMapping(value="/projectbidding",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectBidding(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String floatFactor,String specialDiscount,
			Integer calculatorType,String calculatorTtem
			){
		logger.info("/supervise/projectbidding");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//基价  货物、服务 工程 对应的上限值350万元、300万元和450万元
				BigDecimal bmoney=new BigDecimal(totalMoney); 
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),bmoney);
				//货物、服务 工程 对应的上限值350万元、300万元和450万元
				if("工程招标".equals(calculatorTtem)){
					if(0<basePrice.compareTo(new BigDecimal(450))){
						basePrice=new BigDecimal(450);
					}
				}
				if("货物招标".equals(calculatorTtem)){
					if(0<basePrice.compareTo(new BigDecimal(350))){
						basePrice=new BigDecimal(350);
					}
				}
				if("服务招标".equals(calculatorTtem)){
					if(0<basePrice.compareTo(new BigDecimal(300))){
						basePrice=new BigDecimal(300);
					}
				}
				//折扣前收费
				totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 施工图审查计算器（完毕）  ，以工程勘察设计收费为基准计费的，其收费标准应不高于工程勘察设计收费标准的6.5%；
	 * 以工程概（预）算投资额比率计费的，
	 * 其收费标准应不高于工程概（预）算投资额的2‰；按照建筑面积计费的，其收费标准应不高于2元/平方米。（只录入咨询项目不录入公式）
	 * @param request
	 * @param response
	 * @param calculatorType 计算器类型 
	 * @param calculatorTtem  计算项目 （根据项目）计算
	 * @param totalMoney  总投资，例如2000万元
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 */
	@RequestMapping(value="/projectinvestigate",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectInvestigate(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String floatFactor,String specialDiscount
			,String formulaId
			){
		logger.info("/supervise/projectinvestigate");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			Formula formula =(Formula)formulaService.selectByPrimaryKey(formulaId);
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			//以工程勘察设计收费为基准计费，其收费标准应不高于工程勘察设计收费标准的6.5%；
			if("以工程勘察设计收费为基准".equals(formula.getFormulaName())){
				basePrice = money.multiply(new BigDecimal(0.065));
			}
			//以工程概（预）算投资额比率计费的，其收费标准应不高于工程概（预）算投资额的2‰；
			if("以工程概（预）算投资额比率计费".equals(formula.getFormulaName())){
				basePrice = money.multiply(new BigDecimal(0.002));
			}
			//按照建筑面积计费的，其收费标准应不高于2元/平方米。（只录入咨询项目不录入公式）
			if("按照建筑面积计费".equals(formula.getFormulaName())){
				basePrice = money.multiply(new BigDecimal(2));
			}
			//折扣前收费
			totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
			//折扣后收费
			discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	
	/**
	 * 水土保持费计算器（完毕）  ，只有水土保持施工期监测费 才有调整系数，其他的没有
	 * @param request
	 * @param response
	 * @param calculatorType 计算器类型 
	 * @param calculatorTtem  计算项目 （根据项目）计算
	 * @param totalMoney  总投资，例如2000万元
	 * @param regulationFactor  地貌类型调整系数
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 */
	@RequestMapping(value="/projectconservation",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectConservation(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String regulationFactor,
			String floatFactor,String specialDiscount,Integer calculatorType
			,String calculatorTtem
			){
		logger.info("/supervise/projectconservation");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
//			Long money =new Long(totalMoney);
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//费基价
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				//只有水土保持施工期监测费 才有调整系数，其他的没有
				//折扣前收费
				if("水土保持施工期监测费".equals(calculatorTtem)){
					AssertUtil.notNull(regulationFactor, GczjStatusCode.landform_factor_null);
					AssertUtil.isTrue(ComputeUtils.regEX(regulationFactor), GczjStatusCode.number_format_error);
					totailPrice = basePrice.multiply(new BigDecimal(regulationFactor)).multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				}else{
					totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				}
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 设计费费计算器（完毕）  
	 * @param request
	 * @param response
	 * @param totalMoney  总投资，例如2000万元
	 * @param regulationFactor  专业调整系数
	 * @param complexFactor   复杂调整系数
	 * @param elevationFactor  高程调整系数
	 * @param workload   工作量
	 * @param floatFactor  浮动幅度
	 * @param specialDiscount  优惠折扣
	 * @param calculatorType  计算器类型 
	 * @param calculatorTtem  计算项目 （根据项目）计算
	 */
	@RequestMapping(value="/projectdesign",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectDesign(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String regulationFactor,String complexFactor,String elevationFactor,   
			String workload,String floatFactor,String specialDiscount,Integer calculatorType
			,String calculatorTtem
			){
		logger.info("/supervise/projectdesign");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(regulationFactor, GczjStatusCode.regulation_factor_null);
			AssertUtil.notNull(complexFactor, GczjStatusCode.complex_factor_null);
			AssertUtil.notNull(elevationFactor, GczjStatusCode.elevation_factor_null);
			AssertUtil.notNull(workload, GczjStatusCode.workload_factor_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(regulationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(complexFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(elevationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(workload), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,null);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			BigDecimal elementary=null;
			BigDecimal projectelEmentary=null;
			
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//工程设计收费基价
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				
				//基本设计收费=basePrice*专业调整系数*工程复杂程度调整系数*附加调整系数*工作量比例
				elementary=basePrice.multiply(new BigDecimal(regulationFactor))
				                    .multiply(new BigDecimal(complexFactor))
				           			.multiply(new BigDecimal(elevationFactor))
				           			.multiply(new BigDecimal(Double.parseDouble(workload)/100));
				//工程设计收费基准价=基本设计收费
				projectelEmentary=elementary;
				
				//折扣前收费
				totailPrice = elementary.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
			}
			rpcResult.addDatabody("basePrice",basePrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("elementary",elementary.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("projectelEmentary",elementary.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	

	/**
	 * 环境影响评价费
	 * @param request
	 * @param response
	 * @param totalMoney    工程投资额
	 * @param evaluationLevel   行业调整系数
	 * @param elevationFactor  环境敏感程度调整系数
	 * @param complexFactor   环评专题
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 * @param formulaId 咨询项目id
	 * @param calculatorType  计算器类型 
	 * @param calculatorTtem  计算项目 （根据项目）计算
	 */
	@RequestMapping(value="/environmental",method = { RequestMethod.GET, RequestMethod.POST })
	public void getEnvironmental(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String evaluationLevel,String elevationFactor,String complexFactor,   
			String floatFactor,String specialDiscount,String formulaId,
			Integer calculatorType,String calculatorTtem
			){
		logger.info("/supervise/environmental");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//校验参数不为空
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(evaluationLevel, GczjStatusCode.elevation_level_null);
			AssertUtil.notNull(elevationFactor, GczjStatusCode.environmental_elevation_factor_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);
			
			
			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(evaluationLevel), GczjStatusCode.number_format_error);
			
			AssertUtil.isTrue(ComputeUtils.regEX(elevationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			
			
			//初始化返回值（默认正确）
			BigDecimal money=new BigDecimal(totalMoney); 
			List<CalculationFormula> calculationFormulaList = calculationFormulaService.getCalculationFormulaListByMoney(
					Integer.valueOf(calculatorType),money,calculatorTtem,formulaId);
			BigDecimal basePrice =null;
			BigDecimal totailPrice=null;
			BigDecimal discountCost=null;
			BigDecimal elementary=null;
			BigDecimal projectelEmentary=null;
			
			
			if(calculationFormulaList!=null && calculationFormulaList.size()>0){
				CalculationFormula superviseRate = calculationFormulaList.get(0);
				//工程设计收费基价
				basePrice = ComputeUtils.getPrice(superviseRate.getRate(),money);
				
				//基本价=basePrice*行业调整系数*环境敏感程度调整系数
				elementary=basePrice.multiply(new BigDecimal(evaluationLevel))
						.multiply(new BigDecimal(elevationFactor));
				
				if("编制环境影响报告表".equals(superviseRate.getRemark())){
					AssertUtil.notNull(complexFactor, GczjStatusCode.environmental_complex_factor_null);
					AssertUtil.isTrue(ComputeUtils.regEX(complexFactor), GczjStatusCode.number_format_error);
					projectelEmentary=elementary.add(elementary.multiply(new BigDecimal(complexFactor)).multiply(new BigDecimal(0.5)));
				
					//折扣前收费
					totailPrice = projectelEmentary.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				}else{
					//折扣前收费
					totailPrice = elementary.multiply(new BigDecimal(Double.parseDouble(floatFactor)/100));
				}
				//折扣后收费
				discountCost = totailPrice.multiply(new BigDecimal(1+Double.parseDouble(specialDiscount)/100));
				
				
			}
			rpcResult.addDatabody("totailPrice",totailPrice.multiply(new BigDecimal(100000000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",discountCost.multiply(new BigDecimal(100000000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			//接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 地质灾害评估收费计算器（完毕）
	 * @param request
	 * @param response
	 * @param totalMoney  总价
	 * @param evaluationLevel 评估级别/地址环境复杂程
	 * @param elevationFactor   复杂调整系数
	 * @param floatFactor       浮动幅度
	 * @param specialDiscount  优惠折扣
	 * @param formulaId 咨询项目id
	 */
	@RequestMapping(value="/projectgeology",method = { RequestMethod.GET, RequestMethod.POST })
	public void getProjectGeology(HttpServletRequest request, HttpServletResponse response
			,String totalMoney,String evaluationLevel,String elevationFactor, String floatFactor,
			String specialDiscount,String formulaId
	) {
		logger.info("/supervise/projectgeology");
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			// 校验参数不为空
			AssertUtil.notNull(formulaId, GczjStatusCode.formula_id_null);
			AssertUtil.notNull(totalMoney, GczjStatusCode.total_money_null);
			AssertUtil.notNull(evaluationLevel, GczjStatusCode.evaluation_level_null);
			AssertUtil.notNull(elevationFactor, GczjStatusCode.engineering_category_null);
			AssertUtil.notNull(floatFactor, GczjStatusCode.float_factor_null);
			AssertUtil.notNull(specialDiscount, GczjStatusCode.special_discountnot_exist_error);

			//判断输入内容是否是数字格式
			AssertUtil.isTrue(ComputeUtils.regEX(totalMoney), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(elevationFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(floatFactor), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(specialDiscount), GczjStatusCode.number_format_error);
			AssertUtil.isTrue(ComputeUtils.regEX(evaluationLevel), GczjStatusCode.number_format_error);
			// 初始化返回值（默认正确）
			BigDecimal money = new BigDecimal(totalMoney);
			// Long money =new Long(totalMoney);
			Formula model = (Formula) formulaService.selectByPrimaryKey(formulaId);
			BigDecimal basePrice = null;
			BigDecimal totailPrice = null;
			BigDecimal discountCost = null;
			// 工程规模调整系数
			BigDecimal engineeringScale = null;
			if ("线性工程".equals(model.getFormulaName())) {
				if (0 < money.compareTo(new BigDecimal(30))) {
					// 1+(s-30)/50
					engineeringScale = new BigDecimal(1)
							.add((money.subtract(new BigDecimal(30))).divide(new BigDecimal(50)));
				} else {
					engineeringScale = new BigDecimal(1);
				}
			}
			if ("矿山工程".equals(model.getFormulaName())) {
				if (0 < money.compareTo(new BigDecimal(5))) {
					// 1+(s-5)*0.1
					engineeringScale = new BigDecimal(1)
							.add((money.subtract(new BigDecimal(5))).multiply(new BigDecimal(0.1)));
				} else {
					engineeringScale = new BigDecimal(1);
				}
			}
			if ("水利水电工程".equals(model.getFormulaName())) {
				if (0 < money.compareTo(new BigDecimal(15))) {
					// 1+(s-15)/30
					engineeringScale = new BigDecimal(1)
							.add((money.subtract(new BigDecimal(15))).divide(new BigDecimal(30),10,RoundingMode.HALF_DOWN));
				} else {
					engineeringScale = new BigDecimal(1);
				}
			}
			if ("工业与民用建筑工程".equals(model.getFormulaName())) {
				if (0 < money.compareTo(new BigDecimal(1))) {
					// 1+(s-1)*0.5
					engineeringScale = new BigDecimal(1)
							.add((money.subtract(new BigDecimal(5))).multiply(new BigDecimal(0.5)));
				} else {
					engineeringScale = new BigDecimal(1);
				}
			}
			// 评估级别/地址环境复杂程*复杂调整系数
			basePrice = new BigDecimal(evaluationLevel).multiply(new BigDecimal(elevationFactor))
					.multiply(engineeringScale);

			// 折扣前收费
			totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor) / 100));
			// 折扣后收费
			discountCost = totailPrice.multiply(new BigDecimal(1 + Double.parseDouble(specialDiscount) / 100));
			rpcResult.addDatabody("totailPrice",
					totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",
					discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			// 接口返回值
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :", e);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		BigDecimal pri = new BigDecimal(11);
		if(0<pri.compareTo(new BigDecimal(10.9999999))){
			System.out.println("大于");
		}
		
//		BigDecimal price2 = ComputeUtils.getPrice("x*0.12",price);
//		BigDecimal f1 = price2.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP);  
//		
//		System.out.println(f1);
	}
}

	