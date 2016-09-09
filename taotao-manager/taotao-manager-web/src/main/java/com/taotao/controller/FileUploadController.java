package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.service.FileUploadService;

/**
 * 附件上传控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/upload")
public class FileUploadController {
	@Autowired
	private FileUploadService uploadService;
	
	@RequestMapping("/image")
	@ResponseBody
	public String uploadImge(MultipartFile uploadFile){
		Map<String, ?> result = uploadService.uploadImage(uploadFile);
		//为了保证上传插件对各个浏览器的兼容性。将map转为json返回
		return JsonUtils.objectToJson(result);
	}
}
