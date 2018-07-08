package cn.itcast.bos.action.base;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月6日 上午9:18:20
	 */
	private static final long serialVersionUID = 2500967114335255763L;
	@Autowired
	private FixedAreaService fixedAreaService;
	// 分区名字
	private String subareaName;

	public void setSubareaName(String subareaName) {
		this.subareaName = subareaName;
	}

	private Integer courierId;
	private Integer takeTimeId;

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	/**
	 * 说明：定区添加
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午8:52:40
	 * @return
	 */
	@Action(value = "fixedArea_add", results = { @Result(type = REDIRECT, location = "/pages/base/fixed_area.html") })
	public String add() {
		// 调用业务层
		fixedAreaService.saveFixedArea(model);
		return SUCCESS;
	}

	/**
	 * 说明：组合条件分页查询
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午8:52:51
	 * @return
	 */
	@Action("fixedArea_listPage")
	public String listPage() {
		// 1)请求的分页bean对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 2)请求的业务条件封装对象:拼接业务条件
		Specification<FixedArea> spec = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 提供
				Predicate andPredicate = cb.conjunction();// and关系
				// Predicate disjunction = cb.disjunction();// or的关系
				// ============单表
				// 定区编码
				if (StringUtils.isNotBlank(model.getId())) {
					andPredicate.getExpressions().add(cb.like(root.get("id").as(String.class), "%" + model.getId() + "%"));
				}
				// 所属单位
				if (StringUtils.isNotBlank(model.getCompany())) {
					andPredicate.getExpressions()
							.add(cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%"));
				}
				// ===============多表
				// 分区名字
				if (StringUtils.isNotBlank(subareaName)) {
					// 连接
					Join<Object, Object> subareasJoin = root.join("subareas");
					// 添加条件
					andPredicate.getExpressions()
							.add(cb.like(subareasJoin.get("name").as(String.class), "%" + subareaName + "%"));
				}
				return andPredicate;
			}
		};
		Page<FixedArea> pageResponse = fixedAreaService.findFixedAreaListPage(spec, pageable);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}

	/**
	 * 说明：关联快递员
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午8:52:13
	 * @return
	 */
	@Action(value = "fixedArea_associationCourierToFixedArea", results = {
			@Result(type = REDIRECT, location = "/pages/base/fixed_area.html") })
	public String associationCourierToFixedArea() {
		fixedAreaService.associationCourierToFixedArea(model.getId(), courierId, takeTimeId);
		return SUCCESS;
	}

	/**
	 * 说明：查询没有定区的客户列表
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午8:52:23
	 * @return
	 */
	@Action("fixedArea_findCustomerListNoFixedAreaId")
	public String findCustomerListNoFixedAreaId() {
		// Webservice调用
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/customers").path("/nofixedareaid")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return JSON;
	}

	/**
	 * 说明：查询有定区的客户列表
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午9:34:06
	 * @return
	 */
	@Action("fixedArea_findCustomerListByFixedAreaId")
	public String findCustomerListByFixedAreaId() {
		// Webservice调用
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/customers").path("/fixedareaid")
				.path("/" + model.getId()).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return JSON;
	}

	// 属性驱动封装客户编号s
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * 说明：将客户关联到定区id上
	 * 
	 * @author wangkai
	 * @time：2017年11月8日 下午9:34:33
	 * @return
	 */
	@Action(value = "fixedArea_associationCustomersToFixedArea", results = {
			@Result(type = REDIRECT, location = "/pages/base/fixed_area.html") })
	public String associationCustomersToFixedArea() {
		// 将客户id数组转换为ids，逗号分割
		String cIds = StringUtils.join(customerIds, ",");
		// Webservice调用
		WebClient.create("http://localhost:9002/crm_management/services/customerService/customers").path("/fixedareaid")
				.path("/" + model.getId()).path("/" + cIds).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.put(null);
		return SUCCESS;
	}

}
