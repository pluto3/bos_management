package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.repository.base.AreaRepository;
import cn.itcast.bos.repository.base.FixedAreaRepository;
import cn.itcast.bos.repository.base.OrderRepository;
import cn.itcast.bos.repository.base.workBillRepository;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.utils.Constants;
import cn.itcast.crm.domain.Customer;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private workBillRepository workBillRepository;

	@Override
	public void saveOrder(Order order) {
		//测试接口业务，打印order对象
		if(true){
			System.out.println(order);
			return;
		}
		// 将区域对象变成持久态
		// 寄件
		Area sendAreaPersist = areaRepository.findByProvinceAndCityAndDistrict(order.getSendArea().getProvince(),
				order.getSendArea().getCity(), order.getSendArea().getDistrict());
		System.out.println(sendAreaPersist.getProvince() + sendAreaPersist.getCity() + sendAreaPersist.getDistrict());
		// 收件
		Area recAreaPersist = areaRepository.findByProvinceAndCityAndDistrict(order.getRecArea().getProvince(),
				order.getRecArea().getCity(), order.getRecArea().getDistrict());

		// 放进去
		order.setSendArea(sendAreaPersist);
		order.setRecArea(recAreaPersist);
		order.setOrderType("人工分担");
		order.setOrderTime(new Date());
		order.setStatus("代取件");

		orderRepository.save(order);

		// 地址：省市区+地址详情
		String address = order.getSendArea().getProvince() + order.getSendArea().getCity() + order.getSendArea().getDistrict()
				+ order.getSendAddress();
		Customer customer = null;
		try {
			// 调用crm接口
			customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customers")
					.path("/fixedareaid/address")
					.path("/" + address)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Customer.class);
		} catch (Exception e) {
			e.printStackTrace();
			//使用日志框架
		}

		// ==================1.客户下单地址完全匹配规则实现自动分单
		// 判断是否为空
		if (customer != null && StringUtils.isNotBlank(customer.getFixedAreaId())) {
			// 找到了定区编号
			// 根据定区编号，查询定区
			FixedArea fixedArea = fixedAreaRepository.findOne(customer.getFixedAreaId());
			// 判断定区是否存在
			if (fixedArea != null) {
				// 存在定区,找到快递员
				Set<Courier> courierSet = fixedArea.getCouriers();
				/*实际业务：快递员的选择会根据多个纬度来，
				 * 比如：收派时间、收派标准、是否有替班，等。。。
				 */
				// 这里随便选一个
				if (courierSet != null && !courierSet.isEmpty()) {
					// 找到快递员
					Courier courier = courierSet.iterator().next();
					// 1）设置订单状态
					// 分单类型，改为自动
					order.setOrderType("自动分单");
					// 关联快递员
					order.setCourier(courier);
					// 2）生成工单(保存操作)
					saveWorkBill(order, courier);
					return;
				}
			}
		}
		// ==================2.客户下单地址关键字匹配规则实现自动分单
		// 根据区域查询所有的分区:区域vs分区-一对多
		Set<SubArea> subareaSet = sendAreaPersist.getSubareas();
		// 循环查找需要的分区
		for (SubArea subArea : subareaSet) {
			// 通过地址中的关键字定位分区
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				// 如果找到,通过分区找到关联的定区
				FixedArea fixedArea = subArea.getFixedArea();
				if (fixedArea != null) {
					// 存在定区,找到快递员
					Set<Courier> courierSet = fixedArea.getCouriers();
					/*实际业务：快递员的选择会根据多个纬度来，
					 * 比如：收派时间、收派标准、是否有替班，等。。。
					 */
					// 这里随便选一个
					if (courierSet != null && !courierSet.isEmpty()) {
						// 找到快递员
						Courier courier = courierSet.iterator().next();

						// 1）设置订单状态
						// 分单类型，改为自动
						order.setOrderType("自动分单");
						// 关联快递员
						order.setCourier(courier);
						// 2）生成工单(保存操作)
						saveWorkBill(order, courier);
						// 停止代码运行
						return;
					}
				}
				// 停止查找
				break;
			}
		}

	}

	// 保存生成工单
	private void saveWorkBill(Order order, Courier courier) {
		// 2）生成工单(保存操作)
		WorkBill workBill = new WorkBill();
		workBill.setType("新");// 工单类型 新,追,销
		workBill.setPickstate("新单");// 主要给快递员使用的取件状态
		workBill.setAttachbilltimes(0);// 追单次数，每次追单数字+1，默认是0
		workBill.setBuildtime(new Date());// 创建时间
		workBill.setOrder(order);// 工单关联订单
		workBill.setCourier(courier);// 工单指派快递员，快递员查询自己工单条件
		// workBill.setSmsNumber(smsNumber);//短信序号：短信表的id
		workBill.setRemark(order.getRemark());// 备注（快递员捎话），冗余设计。该数据来自于order
		// 保存
		workBillRepository.save(workBill);
	}
}
