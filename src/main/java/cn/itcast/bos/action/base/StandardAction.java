package cn.itcast.bos.action.base;

import java.util.List;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Controller
@Scope("prototype")
public class StandardAction extends BaseAction<Standard> {

	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月2日 下午3:27:43
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private StandardService standardService;

	private String temID;

	public void setTemID(String temID) {
		this.temID = temID;
	}

	@Action(value = "standard_add", results = { @Result(type = REDIRECT, location = "/pages/base/standard.html") })
	public String add() {
		standardService.saveStandard(model);
		return SUCCESS;
	}

	@Action(value = "standard_delete", results = { @Result(type = REDIRECT, location = "/pages/base/standard.html") })
	public String delete() {
		for (String id : temID.split(",")) {
			standardService.deleteById(Integer.parseInt(id));
		}
		return SUCCESS;
	}

	@Action("standard_listPage")
	public String listPage() {
		// 参数1：当前页码的索引 （索引从0开始）Pages are zero indexed
		// 参数2：每页显示的最大记录数
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Standard> pageResponse = standardService.findStandardListPage(pageable);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}

	@Action("standard_findAll")
	public String findAll() {
		List<Standard> standardList = standardService.findStandardList();
		// 要转换为json的java对象压入root栈顶
		ActionContext.getContext().getValueStack().push(standardList);
		return JSON;
	}
}
