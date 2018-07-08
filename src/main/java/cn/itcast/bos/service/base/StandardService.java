package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {

	/**
	 * 说明：数据保存或更新
	 * @author wangkai
	 * @time：2017年11月2日 下午5:16:46
	 */
	void saveStandard(Standard standard);

	/**
	 * 说明：分页查询
	 * @author wangkai
	 * @time：2017年11月2日 下午5:16:48
	 */
	Page<Standard> findStandardListPage(Pageable pageable);

	/**
	 * 说明：删除数据
	 * @author wangkai
	 * @time：2017年11月2日 下午7:26:27
	 */
	void deleteById(Integer id);

	/**
	 * 说明：查找所有standard
	 * @author wangkai
	 * @time：2017年11月3日 下午3:01:07
	 */
	List<Standard> findStandardList();
}
