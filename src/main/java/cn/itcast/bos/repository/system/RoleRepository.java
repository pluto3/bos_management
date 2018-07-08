package cn.itcast.bos.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	List<Role> findByUsers(User user);

	List<Role> findByIdIn(List<Integer> asList);
}
