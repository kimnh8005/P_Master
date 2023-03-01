package kr.co.pulmuone.v1.comm.mapper.promotion.adminpointsetting;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;

@Mapper
public interface AdminPointSettingMapper {

	Page<AdminPointSettingVo> getAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	int getAdminPointSettingCount(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	Page<AdminPointSettingVo> getRoleGroupList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	Page<AdminPointSettingVo> getAdminPointSettingList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	AdminPointSettingVo getAdminPointSettingDetail(AdminPointSettingRequestDto adminPointSettingRequestDto);

	AdminPointSettingVo getAdminPointSettingTarget(AdminPointSettingRequestDto adminPointSettingRequestDto);

	int getAdminPointSettingListCount(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception;

	int addAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto);

	int addAdminPointSettingHistory(AdminPointSettingRequestDto adminPointSettingRequestDto);

	int addAdminPointSettingTarget(AdminPointSettingRequestDto adminPointSettingRequestDto);

	int putAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto);

	int removeAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto);

}
