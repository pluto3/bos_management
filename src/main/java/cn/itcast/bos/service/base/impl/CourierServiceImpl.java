package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.repository.base.CourierRepository;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierRepository courierRepository;

	@Override
	public void saveCourier(Courier courier) {
		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findCourierListPage(Specification<Courier> spec, Pageable pageable) {
		return courierRepository.findAll(spec, pageable);
	}

	@Override
	public void deleteBatch(String ids) {
		for (String id : ids.split(",")) {
			courierRepository.updateDeltagById('1', Integer.parseInt(id));
		}
	}

	@Override
	public List<Courier> findNoAssociation() {
		return courierRepository.findByDeltag('0');
	}

}
