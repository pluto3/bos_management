package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

public interface MenuService {

	/**
	 * 说明：查询所有的菜单列表
	 * @author wangkai
	 * @time：2017年11月24日 下午9:39:08
	 * @return
	 */
	List<Menu> findMenuList();

	/**
	 * 说明：添加菜单
	 * @author wangkai
	 * @time：2017年11月24日 下午10:19:06
	 * @param model
	 */
	void saveMenu(Menu menu);

	/**
	 * 说明：根据用户的权限添加菜单
	 * @author wangkai
	 * @time：2017年11月25日 下午8:53:41
	 * @param user
	 * @return
	 */
	List<Menu> findMenuByUser(User user);

}
