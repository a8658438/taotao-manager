package com.taotao.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.constant.TbItemConstant;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@ResponseBody
	@RequestMapping("/{id}")
	public TbItem getItemById(@PathVariable long id) {
		System.out.println(11);
		return itemService.getItemById(id);
	}
	
	/**
	 * 分页查询商品列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDatagridResult getItemListByPage(int page,int rows) {
		return itemService.getItemListByPage(page, rows);
	}
	
	/**
	 * 添加商品
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveItem(TbItem item,String desc,String itemParams) throws Exception {
		return itemService.saveItem(item,desc,itemParams);
	}
	
	/**
	 * 更新商品
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateItem(TbItem item,String desc,String itemParams) throws Exception {
		return itemService.updateItem(item,desc,itemParams);
	}
	
	/**
	 * 删除商品
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult delItem(Long[] ids) {
		return itemService.updateItemStatus(ids, TbItemConstant.STATUS_DEL);
	}

	/**
	 * 上架商品
	 * @return
	 */
	@RequestMapping("/reshelf")
	@ResponseBody
	public TaotaoResult upItem(Long[] ids) {
		return itemService.updateItemStatus(ids, TbItemConstant.STATUS_NORMAL);
	}

	/**
	 * 下架商品
	 * @return
	 */
	@RequestMapping("/instock")
	@ResponseBody
	public TaotaoResult downItem(Long[] ids) {
		return itemService.updateItemStatus(ids, TbItemConstant.STATUS_NOSELL);
	}
	
	/**
	 * 根据商品ID查询商品描述信息
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public TaotaoResult getItemDesc(@PathVariable Long itemId) {
		return itemService.getItemDescByItemId(itemId);
	}
}
