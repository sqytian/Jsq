package cn.m1c.gczj.biz.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.model.RateVo;

public interface RateService extends BaseService {

	/**
	 * 通过项目id和区号查询费率
	 * @param formulaId  项目id
	 * @param areaCode   区号
	 * @return
	 */
	List<Rate> getRateByForidArea(Long formulaId, String areaCode);

	/**
	 * 通过 项目id,公式等级对应的查询价格
	 * @param i  公式等级
	 * @param formulaId  项目id
	 * @param areaCode  区号
	 * @return
	 */
	Long getPriceByLevelForidArea(int i,String formulaId);

	/**
	 * 造价公式列表
	 * @param areaCode  区号
	 * @param formulaName  咨询项目名称
	 * @param pageNum   页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	Map<Integer, List<RateVo>> getRateVoList(Integer pageSize, Integer pageNum, String areaCode, String formulaName);

	/**
	 * 通过formulaId查询公式
	 * @param formulaId
	 */
	List<Rate> getRateByFormulaId(String formulaId);

	/**
	 * 通过formulaId查询公式（核增额/核减额）
	 * @param formulaId
	 */
	List<Rate> getRateByFormulaIdAcrualMoney(String formulaId);

	/**
	 * 通过 项目id,公式等级对应的查询价格（核增额/核减额）
	 * @param i  公式等级
	 * @param formulaId  项目id
	 */
	Long getPriceByLevelForidAreaAcrualMoney(int i, String formulaId);

	String updateRate(String id, String rate, Integer rateLevel, BigDecimal price, String areaCode, String formulaId,
			String remark);

	void deletedRate(String id);

	Map<Integer, List<Rate>> getRateList(Integer pageSize, Integer pageNum, String formulaId);

}
