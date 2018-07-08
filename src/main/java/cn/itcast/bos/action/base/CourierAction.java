package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bosCommon.action.BaseAction;

@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier> {

	/**
	 * 说明：联系人序列化id
	 * 
	 * @author wangkai
	 * @time：2017年11月3日 下午3:30:34
	 */
	private static final long serialVersionUID = 1L;
	protected static final int Set = 0;
	@Autowired
	private CourierService courierService;

	private String fixedAreaId;
	public void setFixedAreaId(String fixedAreaId) {
		this.fixedAreaId = fixedAreaId;
	}

	@Action(value = "courier_add", results = { @Result(type = REDIRECT, location = "/pages/base/courier.html") })
	public String add() {
		courierService.saveCourier(model);
		return SUCCESS;
	}

	@Action("courier_delete")
	public String deleteBatch() {
		// 定义一个结果map
		Map<String, Object> resultMap = new HashMap<>();
		try {
			courierService.deleteBatch(ids);
			resultMap.put("result", true);
		} catch (Exception e) {
			resultMap.put("result", false);
		}
		// 要转换为json的java对象压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		return JSON;
	}

	@Action("courier_listPage")
	public String listPage() {
		// 1)请求的分页bean对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 2)请求的业务条件封装对象:拼接业务条件
		Specification<Courier> spec = new Specification<Courier>() {
			// 参数1：主（根root）查询对象，相当于sql语句中主表select * from roottable
			// 参数2：jpa的简单查询条件对象
			// 参数3：jpa的复杂条件构建对象-用
			// 返回：拼接后的条件结果对象
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> andPredicate = new ArrayList<>();// 约定：这里都是and
				// ============单表查询
				// 参数1：属性路径表达式
				// 参数2：属性的值
				// 类似于：Hibernate：Restrictions.eq(属性，属性值)
				// 结果p:相当于sql：courierNum=?
				// 工号
				if (StringUtils.isNotBlank(model.getCourierNum())) {
					Predicate p = cb.like(root.get("courierNum").as(String.class), "%" + model.getCourierNum() + "%");
					andPredicate.add(p);

				}
				// 所属单位
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					andPredicate.add(p);
				}
				// 类型
				if (StringUtils.isNotBlank(model.getType())) {
					Predicate p = cb.like(root.get("type").as(String.class), "%" + model.getType() + "%");
					andPredicate.add(p);
				}
				// ============多表查询
				// 收派标准
				if (model.getStandard() != null) {
					// 需要对象关联（表关联）
					// 参数1：关联对象属性的路径
					// 参数2：连接类型，默认内联，可以省略
					// Join<Courier, Standard> join2 =
					// root.join(root.getModel().getSingularAttribute("standard",
					// Standard.class),JoinType.INNER);
					Join<Object, Object> standardJoin = root.join("standard");
					if (StringUtils.isNotBlank(model.getStandard().getName())) {
						Predicate p = cb.like(standardJoin.get("name").as(String.class), "%" + model.getStandard().getName() + "%");
						andPredicate.add(p);
					}
				}
				// 并且关系
				Predicate predicate = cb.and(andPredicate.toArray(new Predicate[0]));
				return predicate;
			}
		};

		Page<Courier> pageResponse = courierService.findCourierListPage(spec, pageable);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}
	
	/**
	 * 说明：查询正常工作快递员
	 * @author wangkai
	 * @time：2017年11月8日 下午10:08:45
	 * @return
	 */
	@Action("courier_findNoAssociation")
	public String findNoAssociation() {
		List<Courier> courierList = courierService.findNoAssociation();
		ActionContext.getContext().getValueStack().push(courierList);
		return JSON;
	}
	
	@Action("courier_findListCouriers")
	public String findListCouriers() {
//		HashMap<String, Object> resultMap = new HashMap<>();
//		FixedArea fixedArea = fixedAreaService.findFiedAreaById(fixedAreaId);
//		Set<Courier> couriers = fixedArea.getCouriers();
//		// 总记录数
//		resultMap.put("total",couriers.size() );
//		// 当页数据列表
//		resultMap.put("rows", couriers);
//		ActionContext.getContext().getValueStack().push(resultMap);

		
		// 1)请求的分页bean对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 2)请求的业务条件封装对象:拼接业务条件
		Specification<Courier> spec = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 收派标准
				//if (model.getFixedAreas()!= null) {
					// 需要对象关联（表关联）
					// 参数1：关联对象属性的路径
					// 参数2：连接类型，默认内联，可以省略
					 //Join<Courier, Standard> join2 =root.join(root.getModel().getSingularAttribute("standard",Standard.class),JoinType.INNER);
					// Join<Courier, Set> join =root.join(root.getModel().getSingularAttribute("fixedAreas",Set.class),JoinType.INNER);
					 Join<Courier, FixedArea> join1 =root.join(root.getModel().getSet("fixedAreas",FixedArea.class),JoinType.INNER);
					//Join<Object, Object> standardJoin = root.join("standard");
					//Join<Object, Object> standardJoin = root.join("fixedAreas");
					//if (StringUtils.isNotBlank(model.getStandard().getName())) {
						Predicate p = cb.like(join1.get("id").as(String.class), fixedAreaId);
					//}
				//}
						// 并且关系
						//Predicate predicate = cb.and(andPredicate.toArray(new Predicate[0]));
						return p;
			}
		};
		Page<Courier> pageResponse = courierService.findCourierListPage(spec, pageable);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}
}
