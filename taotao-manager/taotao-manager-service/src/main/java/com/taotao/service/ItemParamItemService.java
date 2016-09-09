package com.taotao.service;

import org.springframework.stereotype.Service;

import com.taotao.common.utils.TaotaoResult;

/**
 * 商品规格service
 * @author Administrator
 *
 */
public interface ItemParamItemService {
	/**
	 * 根据商品Id查询对应的规格参数，并返回html代码
	 * @param itemId
	 * @return
	 */
	String getItemParamHtmlByItemId(Long itemId);
	
	/**
	 * 根据商品Id查询对应的规格参数
	 * @param itemId
	 * @return
	 */
	TaotaoResult getItemParamByItemId(Long itemId);
}
