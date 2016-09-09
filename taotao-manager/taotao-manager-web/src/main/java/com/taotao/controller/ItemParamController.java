package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.service.ItemParamService;

/**
 * 商品分类规格参数模版controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/itemparam")
public class ItemParamController {
	@Autowired
	private ItemParamService itemParamService;
	
	/**
	 * 根据商品分类查询商品规格参数模版
	 * @param cid
	 * @return
	 */
	@RequestMapping("/query/itemcatid/{cid}")
	@ResponseBody
	public TaotaoResult getItemParam(@PathVariable Long cid){
		return itemParamService.getItemParamByCid(cid);
	}
	
	/**
	 * 保存商品规格参数模版
	 * @param cid
	 * @param paramData
	 * @return
	 */
	@RequestMapping(value="/save/{cid}",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveItemParam(@PathVariable Long cid,String paramData){
		return itemParamService.saveItemParam(cid, paramData);
	}
	
	/**
	 * 查询商品目录规格参数模版列表
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDatagridResult getItemParamListByPage(int rows,int page){
		return itemParamService.getItemListByPage(page, rows);
	}
	
	/**
	 * 根据id删除商品目录规格参数
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteItemParamByIds(Long[] ids){
		return itemParamService.deleteItemParamByIds(ids);
	}
}
