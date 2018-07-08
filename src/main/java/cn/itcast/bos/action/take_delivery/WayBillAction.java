package cn.itcast.bos.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bosCommon.action.BaseAction;

/**
 * 说明运单操作action
 * @author wangkai
 * @version
 * @date 2017年11月21日
 */
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
	//属性驱动封装
		private String fieldName;
		private String fieldValue;
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}

	@Autowired
	private WayBillService wayBillService;

	/**
	 * 说明：serialVersionUID
	 * 
	 * @author wangkai
	 * @time：2017年11月17日 下午8:39:57
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 说明：快速录入
	 * 
	 * @author wangkai
	 * @time：2017年11月21日 下午4:48:55
	 * @return
	 */
	@Action("wayBill_addQuick")
	public String addQuick() {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			wayBillService.saveWayBillQuick(model);
			// 成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			// 失败
			resultMap.put("result", false);
		}
		ActionContext.getContext().getValueStack().push(resultMap);
		return JSON;
	}

	/**
	 * 说明：分页查找
	 * 
	 * @author wangkai
	 * @time：2017年11月21日 下午4:49:14
	 * @return
	 */
	@Action("wayBill_listPage")
	public String listPage() {
//		PageRequest pageable = new PageRequest(page - 1, rows);
/*		Specification<WayBill> spec = new Specification<WayBill>() {
			@Override
			public Predicate toPredicate(Root<WayBill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate andPredicate = cb.conjunction();// and关系

						if (StringUtils.isNotBlank(subareaName)) {
							// 连接
							Join<Object, Object> subareasJoin = root.join("subareas");
							// 添加条件
							andPredicate.getExpressions()
									.add(cb.like(subareasJoin.get("name").as(String.class), "%" + subareaName + "%"));
						}

				return andPredicate;
			}
		};*/
		PageRequest pageable = new PageRequest(page - 1, rows,new Sort(Direction.DESC, "id"));
		Page<WayBill> pageResponse = wayBillService.findWayBillListPage(pageable,fieldName,fieldValue);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}
}
