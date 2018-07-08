package cn.itcast.bos.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;
public interface StandardRepository extends JpaRepository<Standard, Integer> {
	
	/**
	 * 根据name查询对象集合
	 * @param name
	 * @return
	 */
	List<Standard> findByName(String name);
	
	/**
	 * 模糊查询
	 * @param name
	 * @return
	 */
	List<Standard> findByNameLike(String name);
	
	/**
	 * 根据id查找name
	 * @param id
	 * @return
	 */
	@Query("select name from Standard where id=?")
	String findNameById(Integer id);
	
	/**
	 * 根据id更新name
	 * @param name
	 * @param id
	 */
	@Query("update Standard set name=? where id=?")
	@Modifying
	void updateNameById(String name,Integer id);
}
