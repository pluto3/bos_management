package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Permission;

public interface PermissionService {

	/**
	 * 说明：查询所有的功能权限
	 * @author wangkai
	 * @time：2017年11月24日 下午10:31:51
	 * @return
	 */
	List<Permission> findPermissionList();

}
