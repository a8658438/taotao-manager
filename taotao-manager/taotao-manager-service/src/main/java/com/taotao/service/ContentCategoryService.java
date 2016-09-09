package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;

/**
 * 页面分类内容管理Service
 * @author Administrator
 *
 */
public interface ContentCategoryService {
	/**
	 * 查询内容分类树
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getContentCategoryList(Long parentId);
	
	/**
	 * 新增一个内容分类
	 * @param parentId
	 * @param name
	 * @return
	 */
	TaotaoResult createContentCategory(Long parentId,String name);
	
	/**
	 * 更新内容分类
	 * @param id
	 * @param name
	 * @return
	 */
	TaotaoResult updateContentCategory(Long id,String name);
	
	/**
	 * 删除内容分类
	 * @param id
	 * @param name
	 * @return
	 */
	TaotaoResult deleteContentCategory(Long id);
}
