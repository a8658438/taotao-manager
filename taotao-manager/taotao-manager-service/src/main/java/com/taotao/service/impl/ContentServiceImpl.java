package com.taotao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;

/**
 * 内容管理service
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_SYNC_CONTENT_URL}")
	private String REST_SYNC_CONTENT_URL;
	
	
	@Override
	public EasyUIDatagridResult getContentListPage(Long categoryId,int page,int rows) {
		//查询列表数据
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//获取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		
		EasyUIDatagridResult result = new EasyUIDatagridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public TaotaoResult saveContent(TbContent content) {
		//补全信息保存
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		
		syncContent(content);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateContent(TbContent content) {
		TbContent con = contentMapper.selectByPrimaryKey(content.getId());
		//补全信息并更新
		content.setUpdated(new Date());
		content.setCreated(con.getCreated());
		contentMapper.updateByPrimaryKey(content);
		
		syncContent(content);
		return TaotaoResult.ok();
	}

	/**
	 * 同步redis数据
	 * @param content
	 */
	private void syncContent(TbContent content) {
		//捕获异常，不影响正常功能使用
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_SYNC_CONTENT_URL+ content.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TaotaoResult deleteContent(Long[] ids) {
		List<Long> list = Arrays.asList(ids);
		//先查询出其中一个
		TbContent content = contentMapper.selectByPrimaryKey(list.get(0));
		//批量删除
		TbContentExample example = new TbContentExample();
		example.createCriteria().andIdIn(list);
		contentMapper.deleteByExample(example);
		
		//更新缓存
		syncContent(content);
		return TaotaoResult.ok();
	}

}
