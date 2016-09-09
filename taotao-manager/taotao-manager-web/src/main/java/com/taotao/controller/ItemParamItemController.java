package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.service.ItemParamItemService;

/**
 * 商品规格controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/itemparamitem")
public class ItemParamItemController {
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@RequestMapping("/param/html/{itemId}")
	public String getItemHtmlParam(@PathVariable Long itemId,Model model){
		String paramHtml = itemParamItemService.getItemParamHtmlByItemId(itemId);
		model.addAttribute("paramHtml", paramHtml);
		return "show-item-param";
	}
	
	@RequestMapping("/param/{itemId}")
	@ResponseBody
	public TaotaoResult getItemParam(@PathVariable Long itemId){
		return itemParamItemService.getItemParamByItemId(itemId);
	}
}
