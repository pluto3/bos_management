package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	/**
	 * 说明：保存
	 * @author wangkai
	 * @time：2017年11月3日 下午3:33:57
	 */
	void saveCourier(Courier courier);

	/**
	 * 说明：分页查询
	 * @author wangkai
	 * @time：2017年11月3日 下午4:23:52
	 */
	Page<Courier> findCourierListPage(Specification<Courier> spec,Pageable pageable);

	/**
	 * 说明：作废一批快递员记录
	 * @author wangkai
	 * @time：2017年11月3日 下午8:34:30
	 */
	void deleteBatch(String ids);

	List<Courier> findNoAssociation();

}
