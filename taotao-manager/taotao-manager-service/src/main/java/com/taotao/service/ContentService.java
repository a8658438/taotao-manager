package com.taotao.service;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * 内容管理接口
 * @author Administrator
 *
 */
public interface ContentService {
	/**
	 * 根据分类id查询内容列表
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDatagridResult getContentListPage(Long categoryId,int page,int rows);
	
	/**
	 * 保存内容
	 * @param content
	 * @return
	 */
	TaotaoResult saveContent(TbContent content);

	/**
	 * 更新内容
	 * @param content
	 * @return
	 */
	TaotaoResult updateContent(TbContent content);

	/**
	 * 删除内容
	 * @param ids
	 * @return
	 */
	TaotaoResult deleteContent(Long[] ids);
}
