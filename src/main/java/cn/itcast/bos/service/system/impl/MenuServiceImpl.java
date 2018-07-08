package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.repository.system.MenuRepository;
import cn.itcast.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuRepository menuRepository;

	@Override
	@Cacheable(value="bos_menu_cache",key="#user.id")
	public List<Menu> findMenuList() {
		return menuRepository.findAll();
	}

	@Override
	@CacheEvict(value="bos_menu_cache",allEntries=true)
	public void saveMenu(Menu menu) {
		//处理父菜单不选择的问题
		if(menu.getParentMenu()!=null &&menu.getParentMenu().getId()==null){
			menu.setParentMenu(null);
		}

		menuRepository.save(menu);	
	}

	@Override
	@Cacheable(value="bos_menu_cache",key="#user.id")
	public List<Menu> findMenuByUser(User user) {
		//判断用户
		if("admin".equals(user.getUsername())){
			//管理员,所有菜单
			return menuRepository.findByOrderByPriority();
		}else{
			//普通用户，部分菜单
			return menuRepository.findByUser(user);
		}
	}

}
