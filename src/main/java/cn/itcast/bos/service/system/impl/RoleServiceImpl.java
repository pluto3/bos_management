package cn.itcast.bos.service.system.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.repository.system.MenuRepository;
import cn.itcast.bos.repository.system.PermissionRepository;
import cn.itcast.bos.repository.system.RoleRepository;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Override
	public List<Role> findRoleList() {
		return roleRepository.findAll();
	}

	@Override
	public void saveRole(Role role, Integer[] permissionIds, String menuIds) {
		// 1)保存角色
		Role result = roleRepository.save(role);
		// 2)角色关联功能权限：持久态的关系
		if (permissionIds != null) {
			List<Permission> permissionList = permissionRepository.findByIdIn(Arrays.asList(permissionIds));
			for (Permission permission : permissionList) {
				result.getPermissions().add(permission);
			}
		}
		// 3）角色关联菜单
		if (StringUtils.isNotBlank(menuIds)) {
			String[] strings = menuIds.split(",");
			Integer[] menuIdArray = new Integer[strings.length];
			int i = 0;
			for (String string :strings ) {
				menuIdArray[i++]=(Integer.parseInt(string));
			}
			List<Menu> menuList = menuRepository.findByIdIn(Arrays.asList(menuIdArray));
			for (Menu menu : menuList) {
				result.getMenus().add(menu);
			}
		}
	}
}
