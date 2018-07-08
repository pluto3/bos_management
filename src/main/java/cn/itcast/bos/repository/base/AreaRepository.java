package cn.itcast.bos.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {
	// 根据省市区模糊，以及英文精确
	List<Area> findByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode(String string, String string2, String string3,
			String param, String param2);

	// 根据省市区模糊 查询区域信息
	Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
