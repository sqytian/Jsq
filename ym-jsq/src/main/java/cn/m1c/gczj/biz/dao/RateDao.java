package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.model.RateVo;

public interface RateDao extends IBaseDao {

	/**
	 * 通过项目id和区号查询费率
	 * @param formulaId  项目id
	 * @param areaCode   区号
	 * @return
	 */
	List<Rate> getRateByForidArea(@Param("formulaId")Long formulaId,@Param("areaCode") String areaCode);

	/**
	 * 通过 项目id，公式等级对应的查询价格
	 * @param i  公式等级
	 * @param formulaId  项目id
	 */
	Long getPriceByLevelForidArea(@Param("rateLevel")int rateLevel,@Param("formulaId")String formulaId);

	/**
	 * 造价公式列表
	 */
	List<RateVo> getRateVoList(PageBaseModel page);

	/**
	 * 通过formulaId查询公式
	 * @param formulaId
	 */
	List<Rate> getRateByFormulaId(@Param("formulaId")String formulaId);

	/**
	 * 通过formulaId查询公式（核增额/核减额）
	 * @param formulaId
	 */
	List<Rate> getRateByFormulaIdAcrualMoney(@Param("formulaId")String formulaId);

	/**
	 * 通过 项目id,公式等级对应的查询价格（核增额/核减额）
	 * @param i  公式等级
	 * @param formulaId  项目id
	 */
	Long getPriceByLevelForidAreaAcrualMoney(@Param("rateLevel")int rateLevel,@Param("formulaId")String formulaId);

	List<Rate> getRateList(PageBaseModel page);
}