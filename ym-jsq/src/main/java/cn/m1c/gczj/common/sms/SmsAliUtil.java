//package cn.m1c.gczj.common.sms;
//
//import java.util.Random;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
//import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
//
//public class SmsAliUtil {
//	Logger logger = LoggerFactory.getLogger(SmsAliUtil.class);
//	
//	private static final String requestUrl = "http://gw.api.taobao.com/router/rest";
//
//    private static final String APP_KEY = "23448";
//    private static final String APP_SECRET = "4db922e155e17152f65";
//    
//    
//    public boolean sendSmsCode(String code,String phone) {
//		StringBuffer codesb = new StringBuffer();
//		Random random = new Random();
//	//生成4位随机数验证码
//	for ( int i = 0; i < 4; i++) {
//		codesb.append(random.nextInt(10));
//	}
//	
//    TaobaoClient client = new DefaultTaobaoClient(requestUrl, APP_KEY, APP_SECRET);
//    AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//    req.setExtend(phone);
//    req.setSmsType("normal");
//    req.setSmsFreeSignName("工程造价");
//    req.setSmsParamString("{\"code\":\"" + codesb.toString() + "\"}");
//    req.setRecNum(phone);
//    req.setSmsTemplateCode("模版id");//验证码 模版MessageConstant.SMS_CODE
//    AlibabaAliqinFcSmsNumSendResponse rsp = null;
//    try {
//        rsp = client.execute(req);
//        JSONObject json = JSON.parseObject(rsp.getBody());
//        logger.debug(rsp.getBody());
//        String resultCode = json.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result").getString("err_code");
//        logger.debug(resultCode);//输出短信回复内容
//        if (resultCode.equals("0")) {
//            return true;
//        } else {
//            return false;
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//    }
//}
