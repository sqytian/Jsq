package cn.m1c.gczj.biz.controller;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.utils.StringUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.Formula;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.model.RateVo;
import cn.m1c.gczj.biz.service.FormulaService;
import cn.m1c.gczj.biz.service.RateService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.person.model.Admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/api/cost")
public class ProjectController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	public FormulaService formulaService;
	
	@Resource
	public RateService rateService;

	/**
	 * 工程造价计算价格(前)
	 * @param request
	 * @param response
	 * @param formulaId  咨询项目名称id
	 * @param totalMoney  工程总投资
	 * @param regulationFactor  专业调整系数
	 * @param specialDiscount  优惠折扣
	 * @param accrualMoney   核减额/核增额
	 * @param floatFactor      浮动幅度
	 */
	@RequestMapping(value="/getprojectcost",method = RequestMethod.POST)
	public void getProjectCost(HttpServletRequest request, HttpServletResponse response
			,String formulaId,String totalMoney,String regulationFactor,String specialDiscount,
			String accrualMoney,String floatFactor       
			){
		logger.info("/cost/getprojectcost");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
        try {
        	//校验参数不为空
			AssertUtil.isTrue(StringUtil.hasText(totalMoney), GczjStatusCode.total_money_null);
			AssertUtil.isTrue(StringUtil.hasText(formulaId), GczjStatusCode.formula_name_not_exist_error);
			AssertUtil.isTrue(StringUtil.hasText(specialDiscount), GczjStatusCode.special_discountnot_exist_error);
//			List<Rate> rateList = rateService.getRateByForidArea(formulaId,areaCode);
			//通过咨询项目名称id查询公式（不带核减额/核增额）
			//通过项目名称id查询公式集合
			List<Rate> rateList = rateService.getRateByFormulaId(formulaId);
			if(rateList==null || rateList.size()<=0){
				rpcResult.addDatabody("data","*");
				rpcResult.setCode(GczjStatusCode.rate_not_exist_error.getCode());
				rpcResult.setMessage(GczjStatusCode.rate_not_exist_error.getMessage());
				logger.error("This rate is not exist!");
				rpcResult.outCrossOrigin(response);
				return;
			}
			List<String> raList = new ArrayList<String>();
			for (Rate rate : rateList) {
				//获得价格
				//如果输入价格>该级别价格 就得到该级别的公式
				if(new BigDecimal(totalMoney).compareTo(new BigDecimal(rate.getPrice().toString()))==1){
					raList.add(rate.getRate());
				}
			}
			//原始计算总价
			Double oldTotailPrice =0d;
			//如果只有一个公式
			if(raList !=null && raList.size()==1){
				//价格
				Double price = getPrice(raList.get(0),new BigDecimal(totalMoney));
				oldTotailPrice+=price;
			}
			if(raList.size()>1){
				for(int i =0;i<raList.size();i++){
					if(i+2>raList.size()){
						Double price2 = getPrice(raList.get(i),new BigDecimal(totalMoney));
						oldTotailPrice+=price2;
					}else{
						Long price = rateService.getPriceByLevelForidArea(i+2,formulaId);
						Double price2 = getPrice(raList.get(i),new BigDecimal(price.toString()));
						oldTotailPrice+=price2;
					}
				}
			}
			//核减额/核增额的计算价格
			Double totailAcrualMoneyPrice=0d;
			//核减额/核增额不为空
			if(StringUtils.hasLength(accrualMoney)){
				Double accrua = new Double(accrualMoney);
				//取绝对值
				Double absoluteAccrualMoney = Math.abs(accrua);
				List<Rate> rateAcrualMoneyList = rateService.getRateByFormulaIdAcrualMoney(formulaId);
				if(rateAcrualMoneyList!=null && rateAcrualMoneyList.size()>0){
					List<String> raAcrualMoneyList = new ArrayList<String>();
					for (Rate rateAcrualMoney : rateAcrualMoneyList) {
						//获得价格
						//如果输入价格>该级别价格 就得到该级别的公式
						if(absoluteAccrualMoney>rateAcrualMoney.getPrice()){
							raAcrualMoneyList.add(rateAcrualMoney.getRate());
						}
					}
					//如果只有一个公式
					if(raAcrualMoneyList !=null && raAcrualMoneyList.size()==1){
						//价格
						Double price = getPrice(raAcrualMoneyList.get(0),new BigDecimal(absoluteAccrualMoney.toString()));
						totailAcrualMoneyPrice+=price;
					}
					if(raAcrualMoneyList.size()>1){
						for(int i =0;i<raAcrualMoneyList.size();i++){
							if(i+2>raAcrualMoneyList.size()){
								Double price2 = getPrice(raAcrualMoneyList.get(i),new BigDecimal(absoluteAccrualMoney.toString()));
								totailAcrualMoneyPrice+=price2;
							}else{
								Long price = rateService.getPriceByLevelForidArea(i+2,formulaId);
								rateService.getPriceByLevelForidAreaAcrualMoney(i+2,formulaId);
								Double price2 = getPrice(raAcrualMoneyList.get(i),new BigDecimal(price.toString()));
								totailAcrualMoneyPrice+=price2;
							}
						}
					}
					if(accrua<0){
						totailAcrualMoneyPrice=-totailAcrualMoneyPrice;
					}
			    }
			}
			//判断原始计算总价与核减额/核增额的计算价格的和是否小于零
			if(oldTotailPrice+totailAcrualMoneyPrice<0){
				AssertUtil.isTrue(false, GczjStatusCode.acrual_money_error);
			}
			BigDecimal basePrice = null;
			//折前价
			BigDecimal totailPrice = null;
			//折后价格
			BigDecimal discountCost = null;
			//调整系数
			if(StringUtils.hasLength(regulationFactor)){
				basePrice =(new BigDecimal(oldTotailPrice).add(new BigDecimal(totailAcrualMoneyPrice))).multiply(new BigDecimal(regulationFactor));
//				totailPrice=(oldTotailPrice+totailAcrualMoneyPrice)*Double.valueOf(regulationFactor);
			}else{
				basePrice=new BigDecimal(oldTotailPrice).add(new BigDecimal(totailAcrualMoneyPrice));
			}
			// 折扣前收费
			totailPrice = basePrice.multiply(new BigDecimal(Double.parseDouble(floatFactor) / 100));
			// 折扣后收费
			discountCost = totailPrice.multiply(new BigDecimal(1 + Double.parseDouble(specialDiscount) / 100));
			rpcResult.addDatabody("totailPrice",
					totailPrice.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
			rpcResult.addDatabody("discountCost",
					discountCost.multiply(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
        } catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(),e);
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error("Cost error :",e);
		}
        //接口返回值
  		rpcResult.outCrossOrigin(response);
    }
	
	/**
	 * 根据公式和价格计算
	 * @param rate  公式
	 * @param price 价格
	 * @return
	 */
	public Double getPrice(String rate,BigDecimal price){
	     Binding binding = new Binding();
	     binding.setVariable("x", price);
	     GroovyShell shell = new GroovyShell(binding);
	     Object string = shell.evaluate(rate);
	     
	     return  Double.valueOf(string.toString());
	     
	}
	
	/**
	 * 通过区号查询咨询项目名称(前)
	 * @param request
	 * @param response
	 * @param areaCode 区号
	 */
	@RequestMapping(value="/formulanamelist",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json;charset=UTF-8"})
	public void formulaNameList(HttpServletRequest request, HttpServletResponse response){
		logger.info("/cost/formulanamelist");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_formulalist_success);
		try {  
			String paramJson = (String)request.getAttribute("paramJson");
			logger.info("attribute:"+paramJson);
			JSONObject parseObject = JSON.parseObject(paramJson);
			logger.info("parseObject:"+parseObject);
			String areaCode = parseObject.getString("areaCode");
			logger.info("areaCode:"+areaCode);
			//校验参数不为空
			AssertUtil.isTrue(StringUtil.hasText(areaCode), GczjStatusCode.area_code_null);
			List<Formula> formulaList = formulaService.getFormulaListByArecaCode(areaCode);
			if(formulaList!=null && formulaList.size()>0){
				for(int i=0;i<formulaList.size();i++){
					Formula formula = formulaList.get(i);
					rpcResult.addArray().addDatabody("id", formula.getId())
					          //咨询标准
					          .addDatabody("standardName", formula.getStandardName())
					          //咨询项目名称
					          .addDatabody("formulaName", formula.getFormulaName());
				}
			}else{
				rpcResult.addDatabody("noresult", "目前该地区没有信息 ！");
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
	 * 造价公式列表(后)
	 * @param request
	 * @param response
	 * @param areaCode  区号
	 * @param formulaName  咨询项目名称
	 * @param pageNum   页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	@Security
	@RequestMapping(value="/ratelist",method = {RequestMethod.POST,RequestMethod.GET})
	public void rateList(HttpServletRequest request, HttpServletResponse response){
		logger.info("/cost/ratelist");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String paramJson = (String)request.getAttribute("paramJson");
			logger.info("attribute:"+paramJson);
			JSONObject parseObject = JSON.parseObject(paramJson);
			logger.info("parseObject:"+parseObject);
			String areaCode = parseObject.getString("areaCode");
			logger.info("areaCode:"+areaCode);
			String formulaName = parseObject.getString("formulaName");
			logger.info("formulaName:"+formulaName);
			String pageNum = parseObject.getString("pageNum");
			logger.info("pageNum:"+pageNum);
			if(!StringUtil.hasText(pageNum)){
				pageNum="1";
			}
			String pageSize = parseObject.getString("pageSize");
			logger.info("pageSize:"+pageSize);
			if(!StringUtil.hasText(pageSize)){
				pageNum="10";
			}
			Map<Integer, List<RateVo>> map = rateService.getRateVoList(Integer.valueOf(pageSize), Integer.valueOf(pageNum),areaCode,formulaName);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<RateVo> rateVoList = map.get(totalCount);
			if(rateVoList!=null && rateVoList.size()>0){
				for(int i=0;i<rateVoList.size();i++){
					RateVo rateVo = rateVoList.get(i);
					rpcResult.addArray().addDatabody("id", rateVo.getId())
					          //公式
					          .addDatabody("rate", rateVo.getRate())
					          //该公式对应的价格上限
					          .addDatabody("price", rateVo.getPrice())
					          //公式级别
					          .addDatabody("rateLevel", rateVo.getRateLevel())
					          //区号
					          .addDatabody("areaCode", rateVo.getAreaCode())
					          //
					          .addDatabody("formulaId", rateVo.getFormulaId())
					          //咨询项目名称
					          .addDatabody("formulaName", rateVo.getFormulaName())
					          //地区名称
					          .addDatabody("areaName", rateVo.getAreaName());
				}
			}else{
				rpcResult.addDatabody("noresult", "目前没有查询到公式信息！ ");
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
	 * 修改造价公式
	 * @param request
	 * @param response
	 * @param id  造价公式id
	 * @param rate  公式
	 * @param price  该公式对应的价格上限
	 * @param formulaId  咨询项目id
	 * @param formulaName 咨询项目名称
	 */
	@Security
	@RequestMapping(value="/modifyrate",method = {RequestMethod.POST,RequestMethod.GET})
	public void modifyRate(HttpServletRequest request, HttpServletResponse response){
		logger.info("/cost/modifyrate");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			String paramJson = (String)request.getAttribute("paramJson");
			logger.info("attribute:"+paramJson);
			JSONObject parseObject = JSON.parseObject(paramJson);
			logger.info("parseObject:"+parseObject);
			String id = parseObject.getString("id");
			logger.info("id:"+id);
			String rate = parseObject.getString("rate");
			logger.info("rate:"+rate);
			String price = parseObject.getString("price");
			logger.info("price:"+price);
			String formulaId = parseObject.getString("formulaId");
			logger.info("formulaId:"+formulaId);
			String formulaName = parseObject.getString("formulaName");
			logger.info("formulaName:"+formulaName);
			if(StringUtils.hasText(id) || StringUtils.hasText(formulaId)){
				if(StringUtils.hasText(id)){
					Rate rate1 = (Rate)rateService.selectByPrimaryKey(id);
					AssertUtil.notNull(rate1, GczjStatusCode.rate_not_exist_error);
					rate1.setRate(rate);
					rate1.setPrice(Long.valueOf(price));
					rateService.updateByPrimaryKeySelective(rate1);
				}
				if(StringUtils.hasText(formulaId)){
					Formula formula = (Formula)formulaService.selectByPrimaryKey(formulaId);
					AssertUtil.notNull(formula, GczjStatusCode.formula_name_not_exist_error);
					formula.setFormulaName(formulaName);
					formulaService.updateByPrimaryKeySelective(formula);
				}	
			}else{
				rpcResult.addDatabody("noresult", "造价公式id或咨询项目id必须传入一个！");
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
	
}
