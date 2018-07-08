package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.repository.base.AreaRepository;
import cn.itcast.bos.service.base.AreaService;

@Transactional
@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaRepository areaRepository;

	@Override
	public void saveArea(List<Area> areaList) {
		areaRepository.save(areaList);
	}

	@Override
	public List<Area> findForList() {
		return areaRepository.findAll();
	}

	@Override
	public List<Area> findAreaListByParam(String param) {
		return areaRepository.findByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode("%" + param + "%",
				"%" + param + "%", "%" + param + "%", "%" + param + "%", "%" + param + "%");
	}

	@Override
	public Page<Area> findStandardListPage(Pageable pageable) {
		return areaRepository.findAll(pageable);
	}

	@Override
	public Page<Area> findFixedAreaListPage(Specification<Area> spec, Pageable pageable) {
		return areaRepository.findAll(spec, pageable);
	}

}
