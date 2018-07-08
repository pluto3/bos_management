package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	/**
	 * 说明：快速保存运单
	 * @author wangkai
	 * @time：2017年11月17日 下午8:44:42
	 * @param wayBill
	 */
	void saveWayBillQuick(WayBill wayBill);
	
	/**
	 * 说明：组合条件分页查询
	 * @author wangkai
	 * @time：2017年11月18日 上午8:48:02
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<WayBill> findWayBillListPage(PageRequest pageable,String filedName,String fieldValue);

}
