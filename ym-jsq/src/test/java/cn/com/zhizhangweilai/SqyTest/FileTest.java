package cn.com.zhizhangweilai.SqyTest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.m1c.gczj.biz.dao.SelfFileDao;
import cn.m1c.gczj.biz.model.CalculationFormula;
import cn.m1c.gczj.biz.model.CreateTable;
import cn.m1c.gczj.biz.model.Formula;
import cn.m1c.gczj.biz.service.CalculationFormulaService;
import cn.m1c.gczj.biz.service.CreateTableService;
import cn.m1c.gczj.biz.service.FormulaService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring.xml",
		"classpath*:/spring-mvc.xml",
		"classpath*:/spring-mybatis.xml"})
public class FileTest {
	@Resource
	private CreateTableService service;
	@Resource
	private FormulaService forservice;
	@Resource
	private CalculationFormulaService cforservice;
	
	
	@Test
	public void modify(){
		 Map<Integer, List<Formula>> map = forservice.getFormulaList(100, 1, "001", 12);
		 Integer totalCount = map.keySet().iterator().next();
		 List<Formula> formulaList = map.get(totalCount);
		
		for (Formula model : formulaList) {
			System.out.println(model.getFormulaName());
			System.out.println("==========");
			Map<Integer, List<CalculationFormula>> map2 = cforservice.getCalculationFormulaList(100, 1, 120,model.getFormulaName(),null);
			
			 List<CalculationFormula> cformulaList = map2.get(map2.keySet().iterator().next());
			for (CalculationFormula calculationFormula : cformulaList) {
				
//				System.out.println(calculationFormula.getCalculatorItem());
//				calculationFormula.setRemark(calculationFormula.getCalculatorItem());
				calculationFormula.setCalculatorItem(model.getId());
				cforservice.updateByPrimaryKeySelective(calculationFormula);
			}
		}
	}
	
//	@Test
	public void getmode(){
		Map<Integer, List<CalculationFormula>> map2 = cforservice.getCalculationFormulaList(100, 1, 120,null,null);
		 List<CalculationFormula> cformulaList = map2.get(map2.keySet().iterator().next());
			for (CalculationFormula calculationFormula : cformulaList) {
				
				calculationFormula.setCalculatorType(15);
				cforservice.updateByPrimaryKeySelective(calculationFormula);
			}
	}

}
