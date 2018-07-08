package cn.itcast.bos.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.itcast.bos.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
	/**
	 * 说明：
	 * 
	 * @author wangkai
	 * @time：2017年11月6日 上午9:19:04
	 */
	private static final long serialVersionUID = -2374518494631302739L;
	@Autowired
	private AreaService areaService;
	// 在值栈中放入文件上传需要的属性
	private File upload;// 文件对象
	public String uploadFileName;// 文件名
	public String uploadContentType; // 文件类型

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	@Action("area_importData")
	public String improtData() {
		List<Area> areaList = new ArrayList<>();
		// 定义一个结果map
		Map<String, Object> resultMap = new HashMap<>();
		// 1、打开共工作簿
		HSSFWorkbook hssfWorkbook = null;
		try {
			hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
			// 2、打开工作表
			for (Row row : sheetAt) {
				// 第一行一般是标题，要跳过
				if (row.getRowNum() == 0) {
					continue;
				}
				// 一格一格读数据
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				// 封装数据
				Area area = new Area();
				area.setId(id);
				area.setProvince(province);
				area.setCity(city);
				area.setDistrict(district);
				area.setPostcode(postcode);

				areaList.add(area);
				// 汉字转拼音
				// 准备数据:截掉字符串的最后一个字符
				String provinceStr = StringUtils.substring(province, 0, -1);
				String cityStr = StringUtils.substring(city, 0, -1);
				String districtStr = StringUtils.substring(district, 0, -1);
				// 区域简码
				String shortcode = PinyinHelper.getShortPinyin(provinceStr + cityStr + districtStr).toUpperCase();
				area.setShortcode(shortcode);
				// 城市编码
				// 参数1：要转拼音的中文字符串
				// 参数2：拼音分隔符
				// 参数3：拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
				String citycode = PinyinHelper.convertToPinyinString(cityStr, "", PinyinFormat.WITHOUT_TONE);
				area.setCitycode(citycode);
				areaService.saveArea(areaList);
				resultMap.put("result", true);

			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
		} finally {
			try {
				hssfWorkbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 要转换为json的java对象压入root栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		return JSON;
	}

	// 属性驱动获取q
	private String q;

	public void setQ(String q) {
		this.q = q;
	}

	@Action("area_findForList")
	public String findForList() {
		List<Area> areaList = null;
		if (StringUtils.isNotBlank(q)) {
			// 根据条件查询
			areaList = areaService.findAreaListByParam(q);
		} else {
			areaList = areaService.findForList();
		}
		// 要转换为json的java对象压入root栈顶
		ActionContext.getContext().getValueStack().push(areaList);
		return JSON;
	}

	@Action("area_listPage")
	public String listPage() {
		// 参数1：当前页码的索引 （索引从0开始）Pages are zero indexed
		// 参数2：每页显示的最大记录数
		Pageable pageable = new PageRequest(page - 1, rows);
		// 2)请求的业务条件封装对象:拼接业务条件
		Specification<Area> spec = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 提供
				Predicate andPredicate = cb.conjunction();// and关系
				// Predicate disjunction = cb.disjunction();// or的关系
				if (StringUtils.isNotBlank(model.getProvince())) {
					andPredicate.getExpressions()
							.add(cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%"));
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					andPredicate.getExpressions().add(cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%"));
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					andPredicate.getExpressions()
							.add(cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%"));
				}
				return andPredicate;
			}
		};
		Page<Area> pageResponse = areaService.findFixedAreaListPage(spec, pageable);
		pushPageDataToValustackRoot(pageResponse);
		return JSON;
	}
}
