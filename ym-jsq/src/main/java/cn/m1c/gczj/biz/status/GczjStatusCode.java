package cn.m1c.gczj.biz.status;

import cn.m1c.frame.constants.StatusCode;

public class GczjStatusCode extends StatusCode{

	public static GczjStatusCode token_null= new GczjStatusCode(-1101,"token为空!");
	public static GczjStatusCode id_null= new GczjStatusCode(-1102,"id为空!");
	
	/**
	 * formula 模块  code 范围1201 - 1300
	 */
	public static GczjStatusCode get_formulalist_success= new GczjStatusCode(1201,"查询咨询项目名称成功!");
	
	public static GczjStatusCode formula_name_not_exist_error= new GczjStatusCode(-1201,"没有该造价咨询项目存在!");
	
	public static GczjStatusCode rate_not_exist_error= new GczjStatusCode(-1202,"没有该公式存在!");
	
	public static GczjStatusCode area_code_null= new GczjStatusCode(-1203,"区号为空!");
	
	public static GczjStatusCode project_name_null= new GczjStatusCode(-1204,"咨询项目为空!");
	
	public static GczjStatusCode total_money_null= new GczjStatusCode(-1205,"造价总价为空!");
	
	public static GczjStatusCode acrual_money_error= new GczjStatusCode(-1206,"计算结果小于零，请核对核减额价格！");
	
	public static GczjStatusCode calculator_typ_null= new GczjStatusCode(-1207,"计算器类型为空！");
	
	public static GczjStatusCode user_times_error= new GczjStatusCode(-1208,"不能生成文档，请充值！");
	
	public static GczjStatusCode special_discountnot_exist_error= new GczjStatusCode(-1209,"优惠折扣为空！");
	
	public static GczjStatusCode regulation_factor_null= new GczjStatusCode(-1210,"专业调整系数为空！");
	public static GczjStatusCode complex_factor_null= new GczjStatusCode(-1211,"复杂调整系数为空！");
	public static GczjStatusCode elevation_factor_null= new GczjStatusCode(-1212,"高程调整系数为空！");
	public static GczjStatusCode float_factor_null= new GczjStatusCode(-1213,"浮动幅度为空！");
	
	public static GczjStatusCode complex_factor_error= new GczjStatusCode(-1214,"工程复杂程度调整系数位0.8~1.2！");
	public static GczjStatusCode landform_factor_null= new GczjStatusCode(-1215,"地貌类型调整系数为空！");
	public static GczjStatusCode workload_factor_null= new GczjStatusCode(-1216,"工作量比例表为空！");
	public static GczjStatusCode evaluation_level_null= new GczjStatusCode(-1217,"评估级别/地址环境复杂程度为空！");
	public static GczjStatusCode engineering_category_null= new GczjStatusCode(-1218,"工程类别调整系数为空！");
	public static GczjStatusCode number_format_error= new GczjStatusCode(-1219,"填写的内容不是正确的数字格式！");
	public static GczjStatusCode elevation_level_null= new GczjStatusCode(-1220,"行业调整系数为空！");
	public static GczjStatusCode environmental_elevation_factor_null= new GczjStatusCode(-1221,"环境敏感程度调整系数为空！");
	public static GczjStatusCode environmental_complex_factor_null= new GczjStatusCode(-1222,"环评专题为空！");
	
	
	/**
	 * information 模块  code 范围1301 - 1400
	 */
	public static GczjStatusCode type_null= new GczjStatusCode(-1301,"类型为空!");
	
	public static GczjStatusCode classify_null= new GczjStatusCode(-1302,"具体分类为空!");
	
	public static GczjStatusCode putaway_null= new GczjStatusCode(-1303,"是否上架为空!");
	
	public static GczjStatusCode title_null= new GczjStatusCode(-1304,"造价指标标题为空!");
	
	public static GczjStatusCode content_null= new GczjStatusCode(-1305,"造价指标内容为空!");
	
	public static GczjStatusCode number_null= new GczjStatusCode(-1306,"造价指标工程地点为空!");
	
	public static GczjStatusCode time_null= new GczjStatusCode(-1307,"造价指标整理时间为空!");
	
	public static GczjStatusCode infor_id_null= new GczjStatusCode(-1308,"造价指数id为空!");
	
	
	
	
	/**
	 * use 模块  code 范围1401 - 1500
	 */
	
	
	public static GczjStatusCode sms_sned_success= new GczjStatusCode(1401,"发送验证码成功!");
	public static GczjStatusCode user_register_success= new GczjStatusCode(1402,"用户注册成功!");
	public static GczjStatusCode user_login_success= new GczjStatusCode(1403,"用户登录成功!");
	public static GczjStatusCode user_change_password_success= new GczjStatusCode(1404,"用户修改密码成功!");
	public static GczjStatusCode get_users_success= new GczjStatusCode(1405,"获取用户列表成功!");
	public static GczjStatusCode user_find_password_success= new GczjStatusCode(1406,"用户找回密码成功!");
	
	public static GczjStatusCode mobile_empty_null= new GczjStatusCode(-1401,"手机号码为空!");
	public static GczjStatusCode sms_often_error = new GczjStatusCode(-1402,"获取验证码太频繁!");
	public static GczjStatusCode sms_sned_success_failed= new GczjStatusCode(-1403,"发送验证码失败!");
	public static GczjStatusCode smscode_interval_error= new GczjStatusCode(-1404,"验证码不匹配!");
	public static GczjStatusCode password_empty_null= new GczjStatusCode(-1405,"密码为空!");
	public static GczjStatusCode smscode_empty_null= new GczjStatusCode(-1406,"验证码为空!");
	public static GczjStatusCode person_login_error= new GczjStatusCode(-1407,"该手机号已注册过!");
	public static GczjStatusCode user_notexist_error= new GczjStatusCode(-1408,"用户不存在!");
	public static GczjStatusCode user_login_error_lock= new GczjStatusCode(-1409,"用户已经被锁定!");
	public static GczjStatusCode user_login_password_error= new GczjStatusCode(-1410,"密码错误!");
	public static GczjStatusCode user_change_password_failed= new GczjStatusCode(-1411,"用户修改密码失败!");
	public static GczjStatusCode user_find_password_failed= new GczjStatusCode(-1412,"用户找回密码失败!");
	
	/**
	 * answer 模块  code 范围1501 - 1600
	 */
	public static GczjStatusCode get_answer_success= new GczjStatusCode(1501,"获取问答列表成功!");
	
	public static GczjStatusCode delete_answer_success= new GczjStatusCode(1502,"删除问答成功!");
	
	public static GczjStatusCode answer_id_null= new GczjStatusCode(-1501,"问答id为空!");
	public static GczjStatusCode answer_null_error= new GczjStatusCode(-1502,"问答回答内容空!");
	public static GczjStatusCode question_null_error= new GczjStatusCode(-1503,"问答提问内容为空!");
	
	/**
	 * 计算器类型 模块  code 范围1601 - 1700
	 */
	public static GczjStatusCode calculator_type_null= new GczjStatusCode(-1601,"计算器类型为空!");
	public static GczjStatusCode formula_id_null= new GczjStatusCode(-1602,"咨询项目名称id为空!");
	
	/**
	 * table 模块  code 范围1701 - 1800
	 */
	public static GczjStatusCode table_line_id_null= new GczjStatusCode(-1701,"表单单行id为空!");
	
    public static GczjStatusCode get_table_list_success= new GczjStatusCode(1701,"获取计算器表单列表成功!");
	
	public static GczjStatusCode delete_table_success= new GczjStatusCode(1702,"删除表单单行成功!");
	
	/**
	 * rate 模块  code 范围1801 - 1900
	 */
	public static GczjStatusCode rate_id_null= new GczjStatusCode(-1801,"单行造价计算器计算公式id为空!");
	
	/**
	 * 计算公式 模块  code 范围1901 - 2000
	 */
	public static GczjStatusCode calculation_formula_id_null= new GczjStatusCode(-1901,"单行计算公式id为空!");
	
	
	
	protected GczjStatusCode(Integer code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

}
