package cn.itcast.bos.action.base;

import java.util.List;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;

import static cn.itcast.bos.action.common.BaseAction.JSON;

@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {
	/**
	 * 说明：
	 * @author wangkai
	 * @time：2017年11月6日 下午4:07:49
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	public TakeTimeService takeTimeService;
	
	@Action("takeTime_listNoDel")
	public String listNoDel() {
		List<TakeTime> takeTimeList = takeTimeService.listNoDel();
		ActionContext.getContext().getValueStack().push(takeTimeList);
		return JSON;
	}
}
