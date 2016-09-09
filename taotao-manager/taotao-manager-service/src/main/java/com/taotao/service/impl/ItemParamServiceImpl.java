package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {
	@Autowired
	private TbItemParamMapper itemParamMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public TaotaoResult getItemParamByCid(Long itemCatId) {
		//设置查询参数
		TbItemParamExample example = new TbItemParamExample();
		example.createCriteria().andItemCatIdEqualTo(itemCatId);
		//查询
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok();
		}
		return TaotaoResult.ok(list.get(0));
	}

	@Override
	public TaotaoResult saveItemParam(Long itemCatId, String paramData) {
		//创建pojo
		TbItemParam param = new TbItemParam();
		param.setItemCatId(itemCatId);
		param.setParamData(paramData);
		param.setUpdated(new Date());
		param.setCreated(new Date());
		
		//保存
		itemParamMapper.insert(param);
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDatagridResult getItemListByPage(int page, int rows) {
		//查询模版分页列表
		TbItemParamExample example = new TbItemParamExample();
		PageHelper.startPage(page, rows);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		
		//查询分类目录数据
		for (TbItemParam param : list) {
			TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(param.getItemCatId());
			param.setItemCatName(itemCat.getName());
		}
		
		//设置分页数据
		PageInfo<TbItemParam> pageInfo = new PageInfo<TbItemParam>(list);
		EasyUIDatagridResult result = new EasyUIDatagridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult deleteItemParamByIds(Long[] ids) {
		TbItemParamExample example = new TbItemParamExample();
		example.createCriteria().andIdIn(Arrays.asList(ids));
		itemParamMapper.deleteByExample(example);
		return TaotaoResult.ok();
	}
}
