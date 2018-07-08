package cn.itcast.bos.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bosCommon.action.BaseAction;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月22日 下午4:26:41
	 */
	private static final long serialVersionUID = 1L;

	private Integer[] roleIds;

	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}

	@Autowired
	private UserService userService;

	@Action(value="user_add",results= {
			@Result(type=REDIRECT,location="/pages/system/userlist.html")
	})
	public String add() {
		userService.saveUser(model,roleIds);
		return SUCCESS;
	}
	/**
	 * 说明：用户列表查询
	 * 
	 * @author wangkai
	 * @time：2017年11月25日 下午8:15:43
	 * @return
	 */
	@Action("user_list")
	public String list() {
		List<User> userList = userService.findUserList();
		ActionContext.getContext().getValueStack().push(userList);
		return JSON;
	}

	// 登录
	@Action(value = "user_login", results = { @Result(type = REDIRECT, location = "/index.html"), // 主页
			@Result(name = INPUT, type = REDIRECT, location = "/login.html")// 输入表单
	})
	public String login() {
		// 获取subject
		Subject subject = SecurityUtils.getSubject();
		// 创建用户名密码令牌
		UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			// 认证（登录）操作,参数是令牌对象
			subject.login(token);
			// 登录成功
			return SUCCESS;
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			// 用户名找不到,登入失败
			return INPUT;
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			// 密码错误，登入失败
			return INPUT;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			// 其他异常，登入失败
			return INPUT;
		}

	}

	// 注销
	@Action(value = "user_logout", results = { @Result(type = REDIRECT, location = "/login.html") })
	public String logout() {
		// shiro
		SecurityUtils.getSubject().logout();
		return SUCCESS;
	}

}
