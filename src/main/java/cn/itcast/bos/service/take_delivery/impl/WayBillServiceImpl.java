package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.repository.base.WayBillRepository;
import cn.itcast.bos.repository.index.WayBillEsRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillRepository wayBillRepository;

	@Autowired
	private WayBillEsRepository wayBillEsRepository;

	@Override
	public void saveWayBillQuick(WayBill wayBill) {
		wayBillRepository.save(wayBill);
		wayBillEsRepository.save(wayBill);
	}

	@Override
	public Page<WayBill> findWayBillListPage(PageRequest pageable, String fieldName, String fieldValue) {
		if (StringUtils.isBlank(fieldValue)) {
			return wayBillRepository.findAll(pageable);
		}
		// 目标：基于全文检索es搜索结果
		// 第一步：定义检索规则
		// 词条精确匹配
		TermQueryBuilder termQueryBuilder = new TermQueryBuilder(fieldName, fieldValue);
		// 通配符匹配
		WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder(fieldName, "*" + fieldValue + "*");
		// 组合条件对象
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.should(termQueryBuilder);// 并集
		query.should(wildcardQueryBuilder);
		return wayBillEsRepository.search(query, pageable);
	}

}
