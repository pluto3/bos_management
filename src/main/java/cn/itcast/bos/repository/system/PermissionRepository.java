package cn.itcast.bos.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>{
	List<Permission> findByIdIn(List<Integer> idList);
}
