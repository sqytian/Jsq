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
import cn.m1c.gczj.biz.model.CostTargetUrl;
import cn.m1c.gczj.biz.service.CostTargetUrlService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.constants.Constants;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;

/**
 * 造价信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/standard")
public class CostTargetUrlController {
	private static Logger logger = LoggerFactory.getLogger(CostTargetUrlController.class);

	@Resource
	private CostTargetUrlService costTargetUrlService;

	/**
	 * 
	 * 造价信息列表
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param type
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void costTargetList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize, 
			Integer type
			) {
		logger.info("/api/standard/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			String filePath="";
			//2:造价指数；3：人工成本
			if(type==2){
				filePath =	Constants.COSTINDEX_PATH;
			}
			if(type==3){
				filePath = Constants.LABORCOST_PATH;
			}
			Map<Integer, List<CostTargetUrl>> map = costTargetUrlService.getList(pageSize, pageNum, type);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<CostTargetUrl> tableList = map.get(totalCount);
			if (tableList != null && tableList.size() > 0) {
				for (int i = 0; i < tableList.size(); i++) {
					CostTargetUrl model = tableList.get(i);
					rpcResult.addArray().addDatabody("id", model.getId())
							// 创建时间
							.addDatabody("createdTime",
									DateUtils.format(model.getCreated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 分类 1：造价实例；2:造价指数；3：人工成本；4：造价信息
							.addDatabody("type", model.getType())
							// 分类名称
							.addDatabody("typeName", model.getTypeName())
							// 文档名称
							.addDatabody("documentName", model.getDocumentName())
							// url地址
							.addDatabody("urlData", model.getUrlData())
							//服务器文件地址
							.addDatabody("fileUrl", Constants.SERVER_PATH+Constants.ROOT+filePath+"/")
							;
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该种类造价信息！ ");
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
	 * 造价信息详情
	 * 
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/tail", method = { RequestMethod.GET, RequestMethod.POST })
	public void costTargetTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/standard/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_table_list_success);
		try {
			AssertUtil.notNull(id, GczjStatusCode.id_null);
			CostTargetUrl model = (CostTargetUrl) costTargetUrlService.selectByPrimaryKey(id);
			if (model != null) {
				String filePath="";
				//2:造价指数；3：人工成本
				if(model.getType()==2){
					filePath =	Constants.COSTINDEX_PATH;
				}
				if(model.getType()==3){
					filePath = Constants.LABORCOST_PATH;
				}
				rpcResult.addDatabody("id", model.getId())
				// 创建时间
				.addDatabody("createdTime",
						DateUtils.format(model.getCreated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
				// 分类 1：造价实例；2:造价指数；3：人工成本；4：造价信息
				.addDatabody("type", model.getType())
				// 表行名：工程造价，专业调整，折扣浮动
				.addDatabody("typeName", model.getTypeName())
				// 文档名称
				.addDatabody("documentName", model.getDocumentName())
				// url地址
				.addDatabody("urlData", model.getUrlData())
				//服务器文件地址
				.addDatabody("url_data", Constants.SERVER_PATH+filePath+"/")
				;
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
	 * 删除造价信息单行记录
	 * 
	 * @param request
	 * @param response
	 * @param id     
	 */
	@Security
	@RequestMapping(value = "/deleted", method = { RequestMethod.GET, RequestMethod.POST })
	public void deletedCostTarget(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/standard/deleted");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.delete_table_success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.id_null);
			costTargetUrlService.deletedModel(id);
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
