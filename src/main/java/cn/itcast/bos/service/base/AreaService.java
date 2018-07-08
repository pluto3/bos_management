package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

public interface AreaService {

	/**
	 * 说明：保存区域
	 * @author wangkai
	 * @time：2017年11月5日 下午3:38:47
	 */
	void saveArea(List<Area> areaList);

	/**
	 * 说明：查询所有
	 * @author wangkai
	 * @time：2017年11月8日 下午10:22:40
	 * @return
	 */
	List<Area> findForList();

	/**
	 * 说明：条件查询对象
	 * @author wangkai
	 * @time：2017年11月8日 下午11:02:06
	 * @param q
	 * @return
	 */
	List<Area> findAreaListByParam(String param);

	/**
	 * 说明：分页查询
	 * @author wangkai
	 * @time：2017年11月14日 下午3:14:55
	 * @param pageable
	 * @return
	 */
	Page<Area> findStandardListPage(Pageable pageable);

	/**
	 * 说明：条件分页查询
	 * @author wangkai
	 * @time：2017年11月14日 下午3:21:13
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<Area> findFixedAreaListPage(Specification<Area> spec, Pageable pageable);

}
