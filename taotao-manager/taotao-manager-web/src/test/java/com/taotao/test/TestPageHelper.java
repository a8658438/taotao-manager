package com.taotao.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sun.net.ftp.FtpClient;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemCatServie;
import com.taotao.service.ItemService;

/**
 * 测试分页工具
 * 
 * @author Administrator
 * 
 */
public class TestPageHelper {
	// 加载spring配置文件
	ApplicationContext context = new ClassPathXmlApplicationContext(
			"classpath:spring/applicationContext-*.xml");

	@Test
	public void testItemPageHelper() {

		// 获取商品的mapper
		TbItemMapper mapper = context.getBean(TbItemMapper.class);
		// 进行分页查询
		TbItemExample example = new TbItemExample();
		PageHelper.startPage(1, 5);// 设置分页参数
		List<TbItem> list = mapper.selectByExample(example);

		// 遍历数据
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getTitle());
		}
		// 获取分页数据
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println("商品总数为：" + pageInfo.getTotal());
	}

	@Test
	public void testGetItemCatList() {
		// 获取商品的mapper
		ItemCatServie catService = context.getBean(ItemCatServie.class);
		List<TbItemCat> list = catService.getItemCatList(0L);
		for (TbItemCat cat : list) {
			System.out.println(cat.getName());
		}
	}

	/**
	 * 测试ftp上传功能
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	@Test
	public void testFtpUploa() throws SocketException, IOException {
		FTPClient client = new FTPClient();
		client.connect("192.168.21.130", 21);// 设置地址跟端口
		client.login("taotao", "taotao");
		client.setFileType(FTP.BINARY_FILE_TYPE);
		FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\test.jpg"));
		client.appendFile("333.jpg", inputStream);
		inputStream.close();
		client.logout();// 退出
	}

	/**
	 * 测试更新商品状态功能
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	@Test
	public void testUpdateItemStatus() {
		// 获取商品的mapper
		ItemService itemSer = context.getBean(ItemService.class);
		List<Long> ids = new ArrayList<Long>();
		ids.add(536563L);
		ids.add(562379L);
		
//		itemSer.updateItemStatus(ids, TbItemConstant.STATUS_NOSELL);
	}
}
