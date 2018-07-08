package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.repository.base.CourierRepository;
import cn.itcast.bos.repository.base.FixedAreaRepository;
import cn.itcast.bos.repository.base.TakeTimeRepository;
import cn.itcast.bos.service.base.FixedAreaService;

@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void saveFixedArea(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}

	@Override
	public Page<FixedArea> findFixedAreaListPage(Specification<FixedArea> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return fixedAreaRepository.findAll(spec, pageable);
	}

	@Override
	public void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId) {
		FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		courier.setTakeTime(takeTime);
		fixedArea.getCouriers().add(courier);

	}

	@Override
	public FixedArea findFiedAreaById(String fixedAreaId) {
		return fixedAreaRepository.findOne(fixedAreaId);
	}



}
