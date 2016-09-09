package com.taotao.service;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

/**
 * 商品service接口
 * @author Administrator
 *
 */
public interface ItemService {
	/**
	 * 通过商品逐渐查询商品
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(long id);
	
	/**
	 * 分页查询商品列表
	 * @param page
	 * @param row
	 * @return
	 */
	EasyUIDatagridResult getItemListByPage(int page,int rows);
	
	/**
	 * 保存一个商品
	 * @param item
	 * @param itemParams 
	 * @return
	 */
	TaotaoResult saveItem(TbItem item,String desc, String itemParams) throws Exception; 
	
	/**
	 * 更新商品的状态
	 * @param ids
	 * @param status
	 * @return
	 */
	TaotaoResult updateItemStatus(Long[] ids,byte status);
	
	/**
	 * 根据商品Id查询商品描述信息
	 * @param itemId
	 * @return
	 */
	TaotaoResult getItemDescByItemId(Long itemId);

	/**
	 * 更新商品数据
	 * @param item
	 * @param desc
	 * @param itemParams
	 * @return
	 * @throws Exception 
	 */
	TaotaoResult updateItem(TbItem item, String desc, String itemParams) throws Exception;
}
