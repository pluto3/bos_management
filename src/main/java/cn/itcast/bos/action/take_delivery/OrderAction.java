package cn.itcast.bos.action.take_delivery;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;

@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
	// 属性驱动获取区域信息:格式 :省/市/区

	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月13日 下午4:38:27
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private OrderService orderService;
	// 属性驱动
	private String sendAreaInfo;
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	//@Action(value = "order_add", results = { @Result(type = REDIRECT, location = "/pages/take_delivery/order.html") })
	@Action(value = "order_add", results = { @Result(type = REDIRECT, location = "/pages/take_delivery/order.html") })

	public String add() {
		System.out.println("122222221221111111");
		// 封装数据
		// 寄件人省市县信息
		String[] sendAreaInfoArray = sendAreaInfo.split("/");
		Area sendArea = new Area();
		sendArea.setProvince(sendAreaInfoArray[0]);
		sendArea.setCity(sendAreaInfoArray[1]);
		sendArea.setDistrict(sendAreaInfoArray[2]);
		model.setSendArea(sendArea);
		// 收件人省市县信息
		String[] recAreaInfoArray = recAreaInfo.split("/");
		Area recArea = new Area();
		recArea.setProvince(recAreaInfoArray[0]);
		recArea.setCity(recAreaInfoArray[1]);
		recArea.setDistrict(recAreaInfoArray[2]);
		model.setRecArea(recArea);
		// 调用业务层保存
		orderService.saveOrder(model);
		return SUCCESS;
	}
}
