//package cn.m1c.gczj.utils;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SignatureException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.auth0.jwt.JWTAudienceException;
//import com.auth0.jwt.JWTExpiredException;
//import com.auth0.jwt.JWTIssuerException;
//import com.auth0.jwt.JWTSigner;
//import com.auth0.jwt.JWTVerifier;
//
//import cn.m1c.frame.constants.StatusCode;
//import cn.m1c.frame.exception.M1CRuntimeException;
//
//
///**
// * @author  phil E-mail: s@m1c.cn
// * @date 2016年5月20日
// * @version 
// * @since y:2016
// * @description 使用jwt方式来替代传统的token  有项目引入时 拷贝本代码 然后替换掉 bsecret 变为私有工具
// */
//public class JwtUtil {
//	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
//	private static final byte[] BSECRET = new byte[]{107, 68, 81, 106, 73, 64, 72, 118, 107, 86, 103, 102, 70, 85, 110, 78, 109, 56, 48, 82, 121, 119, 94, 99, 53, 78, 80, 87, 68, 122, 107, 68, 81, 106, 73, 64, 72, 118, 107, 86, 103, 102, 70, 85, 110, 78, 109, 56, 48, 82, 121, 119, 94, 99, 53, 107, 68, 81, 106, 73, 64, 72, 118, 107, 86, 103, 102, 70, 85, 110, 121, 119, 94, 99, 53, 78, 80, 87, 68, 122};
//    
//	 private static JWTSigner signer = new JWTSigner(BSECRET);
//	 private static JWTVerifier verifier = new JWTVerifier(BSECRET);
//	 /**
//	  * @param object  入参为user对象就可以
//	  * @return 签名之后的数据
//	  */
//	 public static String sign(Object object){
//		 HashMap<String, Object> claims = new HashMap<String, Object>();
//	        claims.put("user", JSON.toJSONString(object));
//	        String token = signer.sign(claims,new JWTSigner.Options().setJwtId(true).
//	        		setExpirySeconds(7*24*60*60).setNotValidBeforeLeeway(2).setIssuedAt(true));
//	       return token;
//	 }
//	 
//	public static Object unSign(String token) {
//		Map<String, Object> decoded = new HashMap<String, Object>();
//		try {
//			decoded = verifier.verify(token);
//		} catch (InvalidKeyException e) {
//			logger.error("无效key",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (NoSuchAlgorithmException e) {
//			logger.error("参数错误",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (IllegalStateException e) {
//			logger.error("不合适的状态",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (SignatureException e) {
//			logger.error("签名错误",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (IOException e) {
//			logger.error("io异常",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (JWTExpiredException e) {
//			logger.error("token过期",e);
//			throw new M1CRuntimeException(StatusCode.timeout);
//		} catch (JWTIssuerException e) {
//			logger.error("issuser无效",e);//common_error_illegal_error 非法操作
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		} catch (JWTAudienceException e) {
//			logger.error("用户无效",e);
//			throw new M1CRuntimeException(StatusCode.forbidden);
//		}  
//		return decoded.get("user");
//	}
//	
//}
