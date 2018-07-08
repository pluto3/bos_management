package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.repository.base.StandardRepository;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardRepository standardRepository;

	@Override
	@RequiresRoles("base1")
	public void saveStandard(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	public Page<Standard> findStandardListPage(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}

	@Override
	public void deleteById(Integer id) {
		standardRepository.delete(id);
	}

	@Override
	public List<Standard> findStandardList() {
		// TODO Auto-generated method stub
		return standardRepository.findAll();
	}

}
