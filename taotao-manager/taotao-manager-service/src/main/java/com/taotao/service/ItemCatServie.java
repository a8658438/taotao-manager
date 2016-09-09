package com.taotao.service;

import java.util.List;

import com.taotao.pojo.TbItemCat;

/**
 * 商品分类Servie
 * @author Administrator
 *
 */
public interface ItemCatServie {
	/**
	 * 根据父Id查询商品分类列表
	 * @param parentId
	 * @return
	 */
	List<TbItemCat> getItemCatList(Long parentId);
}
