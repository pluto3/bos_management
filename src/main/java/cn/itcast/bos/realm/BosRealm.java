package cn.itcast.bos.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.repository.system.PermissionRepository;
import cn.itcast.bos.repository.system.RoleRepository;
import cn.itcast.bos.repository.system.UserRepository;

//自定义realm提供安全数据
@Component("bosRealm")
public class BosRealm extends AuthorizingRealm {

	// 注入UserDao
	@Autowired
	private UserRepository userRepository;
	// 注入角色dao
	@Autowired
	private RoleRepository roleRepository;
	// 注入功能权限dao
	@Autowired
	private PermissionRepository permissionRepository;
	
	//只需要向父类注入缓存区域即可
	
	//认证缓存区域
	@Value("bos_realm_authentication_cache")
	//方法上注入按照参数注入，和方法名无关
	public void setSuperAuthenticationCacheName(String authenticationCacheName){
		super.setAuthenticationCacheName(authenticationCacheName);
	}
	//授权缓存区域
	@Value("bos_realm_authorization_cache")
	public void setSuperAuthorizationCacheName(String authorizationCacheName){
		super.setAuthorizationCacheName(authorizationCacheName);
	}

	@Override
	// 提供授权数据
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权数据提供中。。。。");
		// 1.写死安全数据
		// SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 添加功能权限:底层是一个set集合
		// authorizationInfo.addStringPermission("courier:list");
		// 添加角色权限
		// authorizationInfo.addRole("base");
		// 方法2：
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = (User) principals.getPrimaryPrincipal();
		// 判断
		if ("admin".equals(user.getUsername())) {
			// 超级管理员
			// 角色
			List<Role> roleList = roleRepository.findAll();
			for (Role role : roleList) {
				// 添加角色
				authorizationInfo.addRole(role.getKeyword());
			}
			// 功能权限
			List<Permission> permissionList = permissionRepository.findAll();
			for (Permission permission : permissionList) {
				// 添加功能权限
				authorizationInfo.addStringPermission(permission.getKeyword());
			}
		} else {
			// 普通用户
			// 角色
			List<Role> roleList = roleRepository.findByUsers(user);
			for (Role role : roleList) {
				// 添加角色
				authorizationInfo.addRole(role.getKeyword());
				// 导航查询功能权限
				Set<Permission> permissionSet = role.getPermissions();// 持久态才可以导航查询
				for (Permission permission : permissionSet) {
					// 添加功能权限
					authorizationInfo.addStringPermission(permission.getKeyword());
				}
			}
		}
		return authorizationInfo;
	}

	@Override
	// 提供认证数据
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证数据提供中。。。。");
		// 目标：根据用户名查询用户对象，封装成认证信息对象，返回交给安全管理器
		// 用户名
		String username = ((UsernamePasswordToken) token).getUsername();
		// 查询数据库:根据用户查询对象
		User user = userRepository.findByUsername(username);
		// 判断
		if (null == user) {
			// 如果返回null，安全管理器认为用户名不存在。
			return null;
		} else {
			// 用户名存在，将用户信息封装到认证信息对象中，叫给安全管理器
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo
			/*
			 * 参数1：principal:首长，主要负责人，就是用户对象，将来shiro会将user对象放入“session”中
			 * 参数2：credential：凭证，这里就是真实密码
			 * 参数3：realm的对象的一个唯一的名字，类似uuid，底层就是一个随机数
			 */
			(user, user.getPassword(), super.getName());

			return authenticationInfo;
		}

	}

}
