package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	/**
	 * 说明：用户列表查询
	 * @author wangkai
	 * @time：2017年11月25日 下午8:15:56
	 * @return
	 */
	List<User> findUserList();

	/**
	 * 说明：保存用户
	 * @author wangkai
	 * @time：2017年11月25日 下午8:26:31
	 * @param model
	 * @param roleIds
	 */
	void saveUser(User user, Integer[] roleIds);

}
