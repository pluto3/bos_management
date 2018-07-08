package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeService {

	/**
	 * 说明：查询没有删除的取派时间记录
	 * @author wangkai
	 * @time：2017年11月6日 下午4:00:35
	 */
	List<TakeTime> listNoDel();

}
