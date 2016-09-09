package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	@Value("${FTP_HOST}")
	private String FTP_HOST;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	
	@Override
	public Map<String, ?> uploadImage(MultipartFile uploadFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//获取文件原始的名字
			String oldName = uploadFile.getOriginalFilename();
			//生成新的文件名称
			String newName = IDUtils.genImageName()+oldName.substring(oldName.lastIndexOf("."));
			//获取文件路径
			String filePath = new DateTime().toString("/yyyy/MM/dd");
			//进行文件上传
			boolean result = FtpUtil.uploadFile(FTP_HOST, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH,
					filePath, newName, uploadFile.getInputStream());
			
			//上传失败
			if (!result) {
				resultMap.put("error", 1);
				resultMap.put("message", "文件上传失败");
			}
			resultMap.put("error", 0);
			resultMap.put("url", IMAGE_BASE_URL + filePath +"/"+ newName);
		} catch (IOException e) {
			resultMap.put("error", 1);
			resultMap.put("message", "文件上传异常");
			e.printStackTrace();
		}
		return resultMap;
	}

}
