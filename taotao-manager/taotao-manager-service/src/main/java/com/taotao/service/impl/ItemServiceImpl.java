package com.taotao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.constant.TbItemConstant;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.BeanToMapUtil;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemService;

/**
 * 商品service实现类
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Value("${SOLR_BASE_URL}"+"${SOLR_UPDATE_ITEM_URL}")
	private String SOLR_UP_ITEM_URL;
	
	@Value("${SOLR_BASE_URL}"+"${SOLR_DEL_ITEM_URL}")
	private String SOLR_DEL_ITEM_URL;
	
	@Override
	public TbItem getItemById(long id) {
		//通过主键查询
//		TbItem item = itemMapper.selectByPrimaryKey(id);
		
		//通过条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		return list != null && list.size() > 0 ? list.get(0) : null ;
	}

	@Override
	public EasyUIDatagridResult getItemListByPage(int page, int rows) {
		TbItemExample example = new TbItemExample();
		//不查询已删除数据
		example.createCriteria().andStatusNotEqualTo(TbItemConstant.STATUS_DEL);
		//分页
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//生成easyui Bean
		EasyUIDatagridResult data = new EasyUIDatagridResult();
		data.setRows(list);
		
		//取分页数据
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		data.setTotal(pageInfo.getTotal());
		return data;
	}

	@Override
	public TaotaoResult saveItem(TbItem item,String desc, String itemParams) throws Exception{
		item.setCreated(new Date());
		item.setUpdated(new Date());
		item.setStatus(TbItemConstant.STATUS_NORMAL);
		item.setId(IDUtils.genItemId());
		
		int i = itemMapper.insert(item);
		//保存商品描述信息
		TaotaoResult result = saveItemDesc(item.getId(),desc);
		if (result.getStatus() != 200) {
			//抛出异常，事务回滚
			throw new Exception("保存商品描述信息出错");
		}
		//保存商品规格信息
		result = saveItemParam(item,itemParams);
		if (result.getStatus() != 200) {
			//抛出异常，事务回滚
			throw new Exception("保存商品规格参数出错");
		}
		
		//更新数据到solr
		updateToSolr(item, desc);
		return result;
	}

	
	/**
	 * 保存商品规格参数
	 * @param item
	 * @param itemParams
	 * @return
	 */
	private TaotaoResult saveItemParam(TbItem item, String itemParams) {
		TbItemParamItem params = new TbItemParamItem();
		params.setItemId(item.getId());
		params.setParamData(itemParams);
		params.setCreated(new Date());
		params.setUpdated(new Date());
		itemParamItemMapper.insert(params);
		return TaotaoResult.ok();
	}

	/**
	 * 保存商品描述信息
	 * @param id
	 * @param desc
	 * @return 
	 */
	private TaotaoResult saveItemDesc(Long id, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setCreated(new Date());
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(id);
		itemDesc.setUpdated(new Date());
		descMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateItemStatus(Long[] ids, byte status) {
		//根据主键循环更新
		for (Long id : ids) {
			TbItem item = itemMapper.selectByPrimaryKey(id);
			item.setUpdated(new Date());
			item.setStatus(status);
			itemMapper.updateByPrimaryKey(item);
		}
		//如果是删除
		if (status == TbItemConstant.STATUS_DEL) {
			Map<String, String> map =new HashMap<String,String>();
			String id = Arrays.toString(ids);
			map.put("ids",id.substring(1, id.length()-1));
			HttpClientUtil.doPost(SOLR_DEL_ITEM_URL,map);
		}
		
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult getItemDescByItemId(Long itemId) {
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(itemId);
		return TaotaoResult.ok(itemDesc);
	}

	@Override
	public TaotaoResult updateItem(TbItem item, String desc, String itemParams) throws Exception {
		//查询出原商品
		TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
		if (tbItem == null) {
			throw new Exception("找不到要更新的商品");
		}
		item.setCreated(tbItem.getCreated());
		item.setUpdated(new Date());
		item.setStatus(tbItem.getStatus());
		itemMapper.updateByPrimaryKey(item);//更新商品
		
		//更新商品描述
		TaotaoResult result = updateItemDesc(item, desc);
		if (result.getStatus() != 200) {
			throw new Exception("更新商品描述失败");
		}
		//更新商品规格
		result = updateItemParam(item, itemParams);
		if (result.getStatus() != 200) {
			throw new Exception("更新商品规格失败");
		}
		//更新数据到solr
		updateToSolr(item, desc);
		return result;
	}

	/**
	 * 将数据同步到solr的方法
	 * @param item
	 * @param desc
	 */
	private void updateToSolr(TbItem item, String desc) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", item.getId()+"");
		map.put("title", item.getTitle());
		map.put("sell_point", item.getSellPoint());
		map.put("price", item.getPrice()+"");
		map.put("cid", item.getCid()+"");
		map.put("item_desc",desc);
		map.put("image", item.getImage());
		
		HttpClientUtil.doPost(SOLR_UP_ITEM_URL,map);
	}

	/**
	 * 更新商品规格参数信息
	 * @param item
	 * @param itemParams
	 * @return 
	 */
	private TaotaoResult updateItemParam(TbItem item, String itemParams) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		example.createCriteria().andItemIdEqualTo(item.getId());
		List<TbItemParamItem> list = itemParamItemMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			saveItemParam(item, itemParams);
		}else{
			TbItemParamItem params = list.get(0);
			params.setUpdated(new Date());
			params.setParamData(itemParams);
			itemParamItemMapper.updateByPrimaryKeyWithBLOBs(params);
		}
		return TaotaoResult.ok();
	}

	/**
	 * 更新商品描述
	 * @param item
	 * @param desc
	 * @return 
	 */
	private TaotaoResult updateItemDesc(TbItem item, String desc) {
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(item.getId());
		if (itemDesc == null) {
			saveItemDesc(item.getId(), desc);
		}else{
			itemDesc.setItemDesc(desc);
			itemDesc.setUpdated(new Date());
			descMapper.updateByPrimaryKeyWithBLOBs(itemDesc);
		}
		
		return TaotaoResult.ok();
	}

}
