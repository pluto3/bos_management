package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	/**
	 * 说明：定区添加
	 * @author wangkai
	 * @time：2017年11月5日 下午7:23:19
	 */
	void saveFixedArea(FixedArea fixedArea);
	
	/**
	 * 说明：根据id查询一个
	 * @author wangkai
	 * @time：2017年11月6日 下午5:01:47
	 */
	FixedArea findFiedAreaById(String fixedAreaId);

	/**
	 * 说明：组合条件分页查询
	 * @author wangkai
	 * @time：2017年11月5日 下午7:51:01
	 */
	Page<FixedArea> findFixedAreaListPage(Specification<FixedArea> spec, Pageable pageable);

	/**
	 * 说明：关联快递员业务
	 * @author wangkai
	 * @time：2017年11月6日 下午4:31:43
	 */
	void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId);

}
