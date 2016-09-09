package com.taotao.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 附件上传接口
 * @author Administrator
 *
 */
public interface FileUploadService {
	Map<String, ?> uploadImage(MultipartFile uploadFile);
	
}
