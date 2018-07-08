package cn.itcast.bos.service.system.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.repository.system.RoleRepository;
import cn.itcast.bos.repository.system.UserRepository;
import cn.itcast.bos.service.system.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<User> findUserList() {
		return userRepository.findAll();
	}

	@Override
	public void saveUser(User user, Integer[] roleIds) {
		User result = userRepository.save(user);
		if (roleIds != null) {
			List<Role> userList = roleRepository.findByIdIn(Arrays.asList(roleIds));
			for (Role role : userList) {
				result.getRoles().add(role);
			}
		}
	}

}
