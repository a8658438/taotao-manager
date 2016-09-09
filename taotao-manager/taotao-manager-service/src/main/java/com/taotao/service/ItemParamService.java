package com.taotao.service;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;

/**
 * 商品分类参数模版service
 * @author Administrator
 *
 */
public interface ItemParamService {
	/**
	 * 根据商品分类Id查询对应的模版
	 * @param itemCatId
	 * @return
	 */
	TaotaoResult getItemParamByCid(Long itemCatId);
	
	/**
	 * 保存商品规格模版
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	TaotaoResult saveItemParam(Long itemCatId,String paramData);
	
	/**
	 * 查询商品规格模版分页列表
	 * @return
	 */
	EasyUIDatagridResult getItemListByPage(int page,int rows);
	
	/**
	 * 根据多个ID对模版参数进行删除
	 * @param ids
	 * @return
	 */
	TaotaoResult deleteItemParamByIds(Long[] ids);
}
