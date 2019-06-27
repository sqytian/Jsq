package cn.m1c.gczj.biz.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.model.SelfFile;
import cn.m1c.gczj.biz.service.SelfFileService;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.constants.Constants;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.utils.DateUtils;
import cn.m1c.gczj.utils.FileUtils;

/**
 * 文件上传，管理控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/file")
public class SelfFileController {
	private static Logger logger = LoggerFactory.getLogger(SelfFileController.class);

	@Resource
	private SelfFileService modelService;

	/**
	 * 文件列表
	 * 
	 * @param request
	 * @param response
	 * @param typeName 
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public void selfFileList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize, 
			String typeName,String codeName,String fileName
			) {
		logger.info("/api/file/list");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			Map<Integer, List<SelfFile>> map = modelService.getListByType(pageSize, pageNum, typeName,codeName,fileName);
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<SelfFile> list = map.get(totalCount);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					SelfFile model = list.get(i);
					rpcResult.addArray().addDatabody("id", model.getId())
							// 更新时间
							.addDatabody("updateTime",
									DateUtils.format(model.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
							// 1：清单规则；2:定额规则；3：合同招投标规范；4：自制案例
							.addDatabody("typeName", model.getTypeName())
							// 排序
							.addDatabody("orderNum", model.getOrderNum())
							// 文档名称
							.addDatabody("fileName", model.getDocumentName())
							//download 下载链接 link 跳转链接 html 描述html链接 pdf 描述pdf链接 content 内容描述
							.addDatabody("urlType", model.getUrlType())
							.addDatabody("codeName", model.getReservedFirst())
							// 服务器文件url地址
							.addDatabody("url", model.getFileUrl())
							.addDatabody("imageUrl", model.getImageUrl())
							.addDatabody("toolType", model.getReservedSecond())
							.addDatabody("documentDescription", model.getDocumentDescription())
							;
				}
			} else {
				rpcResult.addDatabody("noresult", "暂时没有该种类信息！ ");
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
	 * 详情
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/tail", method = { RequestMethod.GET, RequestMethod.POST })
	public void selfFileTail(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/file/tail");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			SelfFile model = (SelfFile) modelService.selectByPrimaryKey(id);
			if (model != null) {
				rpcResult.addArray().addDatabody("id", model.getId())
					// 更新时间
					.addDatabody("updateTime",
							DateUtils.format(model.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
					// 1：清单规则；2:定额规则；3：合同招投标规范；4：自制案例
					.addDatabody("typeName", model.getTypeName())
					// 排序
					.addDatabody("orderNum", model.getOrderNum())
					// 文档名称
					.addDatabody("fileName", model.getDocumentName())
					//download 下载链接 link 跳转链接 html 描述html链接 pdf 描述pdf链接 content 内容描述
					.addDatabody("urlType", model.getUrlType())
					// 服务器文件url地址
					.addDatabody("url", model.getFileUrl())
					.addDatabody("toolType", model.getReservedSecond())
					.addDatabody("imageUrl", model.getImageUrl())
					.addDatabody("documentDescription", model.getDocumentDescription())
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
	 * 通过文件名称查询文件
	 * @param request
	 * @param response
	 * @param fileName
	 */
	@RequestMapping(value = "/standard", method = { RequestMethod.GET, RequestMethod.POST })
	public void standard(HttpServletRequest request, HttpServletResponse response
			, String fileName
			) {
		logger.info("/api/file/standard");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			SelfFile model =  modelService.getFileByName(fileName);
			if (model != null) {
				rpcResult.addArray().addDatabody("id", model.getId())
				// 更新时间
				.addDatabody("updateTime",
						DateUtils.format(model.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
				// 1：清单规则；2:定额规则；3：合同招投标规范；4：自制案例
				.addDatabody("typeName", model.getTypeName())
				// 排序
				.addDatabody("orderNum", model.getOrderNum())
				// 文档名称
				.addDatabody("fileName", model.getDocumentName())
				//download 下载链接 link 跳转链接 html 描述html链接 pdf 描述pdf链接 content 内容描述
				.addDatabody("urlType", model.getUrlType())
				// 服务器文件url地址
				.addDatabody("url", model.getFileUrl())
				.addDatabody("imageUrl", model.getImageUrl())
				.addDatabody("documentDescription", model.getDocumentDescription())
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
	 * 保存
	 * @param request
	 * @param response
	 * @param typeName 
	 * @param orderNum
	 * @param fileName
	 * @param urlType  download 下载链接 link 跳转链接 html 描述html链接 pdf 描述pdf链接 content 内容描述
	 * @param url
	 * @param imageUrl
	 * @param documentDescription
	 * @param codeName  地区名称
	 * @param toolType  工具图标URL
	 */
	 @Security
	@RequestMapping(value = "/save", method = { RequestMethod.GET, RequestMethod.POST })
	public void save(HttpServletRequest request, HttpServletResponse response
			,String id,String typeName,Integer orderNum,String fileName,
			String urlType,String url,String imageUrl,String documentDescription,
			String codeName,String toolType
			) {
		logger.info("/api/file/save");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			if(fileName.contains(".")){
				String substring = fileName.substring(0, fileName.indexOf("."));
				fileName=substring;
			}
			String modelId = modelService.updatemodel(id,typeName,orderNum,
					fileName,urlType,url,imageUrl,documentDescription,codeName,toolType);
			rpcResult.addDatabody("id", modelId);
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
	 * 文件上传
	 * @param request
	 * @param response
	 * @param file
	 */
//	 @Security
	@RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST })
	public void fileUpload(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(required=false, value = "file") MultipartFile file
			) {
		logger.info("/api/file/upload");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//2007年6月.html
			String originalFilename= file.getOriginalFilename();
			//2007年6月
			String fileName =originalFilename.substring(0, originalFilename.indexOf("."));
			//文件服务器的路径  http://d.anyfees.com
			String serverPath = Constants.SERVER_PATH;
			String serverName = file.getName();
			// 文件Nginx代理路径 "/data/repository/file"+"/costtarget"
			String path = Constants.ROOT+"/"+serverName;
//			String path = "H:\\ccc\\"+serverName;
			
			InputStream inputStream = null;
			try {
				inputStream = file.getInputStream();
			} catch (IOException e) {
				logger.error("上传文件失败！", e);
				e.printStackTrace();
			}
			File outFile = new File(path);
			if (!outFile.exists() && !outFile.isDirectory()) {
				outFile.mkdirs();// 如果目录不存在就创建
			}
			String fileNameNew = FileUtils.fileNameGenerator(originalFilename);
			String filePath = path +fileNameNew;
			if (filePath != null) {
				FileUtils.saveFile(inputStream, filePath);
			}
			
			// /data/repository/file/costtarget/2018111310429682007年11月.html
			logger.info(filePath);
			rpcResult.addDatabody("url", Constants.SERVER_PATH+"/"+serverName+fileNameNew);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			logger.error(rpcResult.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			rpcResult = RpcResult.status(StatusCode.failed);
			logger.error(StatusCode.failed.getMessage(), e);
			e.printStackTrace();
		}
		// 接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	

	/**
	 * 删除表单单行记录
	 * 
	 * @param request
	 * @param response
	 * @param id     
	 */
//	 @Security
	@RequestMapping(value = "/deleted", method = { RequestMethod.GET, RequestMethod.POST })
	public void deletedmodel(HttpServletRequest request, HttpServletResponse response
			, String id
			) {
		logger.info("/api/file/deleted");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.id_null);
			modelService.deletedmodel(id);
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
	
	@RequestMapping(value = "/download", method = { RequestMethod.GET, RequestMethod.POST })
	public void download(HttpServletRequest request, 
			HttpServletResponse response,String id) {
		logger.info("/api/cost/download");
		// 初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.success);
		try {
			SelfFile model = (SelfFile)modelService.selectByPrimaryKey(id);
			HttpHeaders headers;
			File file = new File(model.getFileUrl());
	        headers = new HttpHeaders();
	        String fileName = new String(model.getDocumentName().getBytes("UTF-8"), "iso-8859-1");
	        headers.setContentDispositionFormData("attachment", fileName);
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        rpcResult.addDatabody("model", new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK));
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
