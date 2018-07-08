package cn.itcast.bos.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bosCommon.action.BaseAction;
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission>{
	/**
	 * 说明：
	 * @author wangkai
	 * @time：2017年11月24日 下午10:31:15
	 */
	private static final long serialVersionUID = 1L;
		//注入业务层
		@Autowired
		private PermissionService permissionService;

		//列表查询
		@Action("permission_list")
		public String list(){
			List<Permission> permissionList= permissionService.findPermissionList();
			ActionContext.getContext().getValueStack().push(permissionList);
			return JSON;
		}

}
