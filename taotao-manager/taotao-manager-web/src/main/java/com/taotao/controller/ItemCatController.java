package com.taotao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.pojo.TbItemCat;
import com.taotao.service.ItemCatServie;

/**
 * 商品分类controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/cat")
public class ItemCatController {
	@Autowired
	private ItemCatServie itemCatServie;
	
	/**
	 * 查询商品分类，并返回easyui tree 结构
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> categoryList(@RequestParam(defaultValue="0",value="id") Long parentId) {
		List<TbItemCat> catList = itemCatServie.getItemCatList(parentId);
		//组装tree
		List<EasyUITreeNode> treeList = new ArrayList<EasyUITreeNode>();
		for (TbItemCat cat : catList) {
			EasyUITreeNode tree = new EasyUITreeNode();
			tree.setId(cat.getId());
			tree.setText(cat.getName());
			tree.setState(cat.getIsParent() ? "closed" : "open");
			
			treeList.add(tree);
		}
		return treeList;
	}
}
