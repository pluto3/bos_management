package cn.itcast.bos.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bosCommon.action.BaseAction;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月25日 下午4:41:52
	 */
	private static final long serialVersionUID = 1L;
	// 属性驱动封装参数
	private Integer[] permissionIds;// 功能权限
	private String menuIds;// 菜单ids

	public void setPermissionIds(Integer[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	/**
	 * 说明：添加角色并关联权限和菜单。
	 * 
	 * @author wangkai
	 * @time：2017年11月25日 下午5:19:05
	 * @return
	 */
	@Action(value = "role_add", results = { @Result(type = REDIRECT, location = "/pages/system/role.html") })
	public String add() {
		roleService.saveRole(model, permissionIds, menuIds);
		return SUCCESS;
	}

	// 注入业务层
	@Autowired
	private RoleService roleService;

	@Action("role_list")
	public String list() {
		List<Role> roleList = roleService.findRoleList();
		ActionContext.getContext().getValueStack().push(roleList);
		return JSON;
	}

}
