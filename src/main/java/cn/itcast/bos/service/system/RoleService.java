package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;

public interface RoleService {

	/**
	 * 说明：查询所有角色列表
	 * @author wangkai
	 * @time：2017年11月25日 下午4:45:46
	 * @return
	 */
	public List<Role> findRoleList();

	/**
	 * 说明：保存角色并关联权限和菜单
	 * @author wangkai
	 * @time：2017年11月25日 下午5:19:47
	 * @param model
	 * @param permissionIds
	 * @param menuIds
	 */
	public void saveRole(Role role, Integer[] permissionIds, String menuIds); 

}
