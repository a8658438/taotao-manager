package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

/**
 * 内容管理controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	/**
	 * 分页查询内容信息
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/query/list")
	@ResponseBody
	public EasyUIDatagridResult getContentListPage(int page,int rows,Long categoryId){
		return contentService.getContentListPage(categoryId, page, rows);
	}
	
	/**
	 * 保存内容信息
	 * @param content
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult saveContent(TbContent content){
		return contentService.saveContent(content);
	}
	
	/**
	 * 更新内容信息
	 * @param content
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public TaotaoResult updateContent(TbContent content){
		return contentService.updateContent(content);
	}
	
	/**
	 * 删除内容信息
	 * @param content
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContent(Long[] ids){
		return contentService.deleteContent(ids);
	}
}
