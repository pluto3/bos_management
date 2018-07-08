package cn.itcast.bos.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

	/**
	 * 说明：作废快递员记录
	 * 
	 * @author wangkai
	 * @time：2017年11月3日 下午8:37:30
	 */
	@Query("update Courier set deltag=? where id=?")
	@Modifying
	void updateDeltagById(Character deltag, Integer id);

	List<Courier> findByDeltag(Character deltag);

}
