package cn.itcast.bos.action.system;

import java.util.List;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;

@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {

	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月24日 下午9:36:54
	 */
	private static final long serialVersionUID = 1L;
	// 注入service
	@Autowired
	private MenuService menuService;

	// 列表查询
	@Action("menu_list")
	public String list() {
		List<Menu> menuList = menuService.findMenuList();
		ActionContext.getContext().getValueStack().push(menuList);
		return JSON;
	}

	// 添加菜单
	@Action(value = "menu_add", results = { @Result(type = REDIRECT, location = "/pages/system/menu.html") })
	public String add() {
		menuService.saveMenu(model);
		return SUCCESS;
	}

	// 根据用户权限查询菜单
	@Action("menu_listByUser")
	public String listByUser() {
		// 获取当前登录用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Menu> menuList = menuService.findMenuByUser(user);
		ActionContext.getContext().getValueStack().push(menuList);
		return JSON;
	}

}
