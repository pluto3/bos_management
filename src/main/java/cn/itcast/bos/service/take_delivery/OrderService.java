package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.itcast.bos.domain.take_delivery.Order;
//SEI接口
@Path("/orders")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) // 消费者
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) // 生产者
public interface OrderService {

	/**
	 * 说明：保存order
	 * @author wangkai
	 * @time：2017年11月13日 下午4:39:40
	 * @param model
	 */
	@POST
	void saveOrder(Order order);

}
