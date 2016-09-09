package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理service
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
		//查询父节点下的子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		
		//封装成easyui tree
		List<EasyUITreeNode> result= new ArrayList<EasyUITreeNode>();
		for (TbContentCategory cat : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			
			result.add(node);
		}
		
		return result;
	}

	@Override
	public TaotaoResult createContentCategory(Long parentId, String name) {
		//创建对象
		TbContentCategory cat = new TbContentCategory();
		cat.setIsParent(false);
		cat.setName(name);
		cat.setParentId(parentId);
		cat.setSortOrder(1);
		cat.setStatus(1);
		cat.setCreated(new Date());
		cat.setUpdated(new Date());
		//插入数据
		contentCategoryMapper.insert(cat);
		
		//判断父节点的字段值
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			parentCat.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		
		return TaotaoResult.ok(cat);
	}

	@Override
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory cat = contentCategoryMapper.selectByPrimaryKey(id);
		cat.setName(name);
		cat.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKey(cat);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContentCategory(Long id) {
		//查询出当前节点
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);

		//删除当前节点以及子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(id);
		contentCategoryMapper.deleteByExample(example);
		contentCategoryMapper.deleteByPrimaryKey(id);
		
		//检查父节点是否还有子节点
		example.createCriteria().andParentIdEqualTo(category.getParentId());
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		//修改父节点值
		if (catList == null || catList.size() == 0) {
			TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(category.getParentId());
			parent.setIsParent(false);
			parent.setUpdated(new Date());
			
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		return TaotaoResult.ok();
	}

}
