package kr.co.pulmuone.v1.promotion.adminpointsetting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingDetailResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.RoleGroupListResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;

@Service
public class AdminPointSettingBizImpl implements AdminPointSettingBiz {

	  @Autowired
	  AdminPointSettingService adminPointSettingService;

	  @Override
	  public ApiResult<?> getAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		AdminPointSettingResponseDto result = new AdminPointSettingResponseDto();

	  	Page<AdminPointSettingVo> adminPointSettingVoList = adminPointSettingService.getAdminPointSetting(adminPointSettingRequestDto);

	    result.setRows(adminPointSettingVoList.getResult());
	    result.setTotal(adminPointSettingVoList.getTotal());

	    return  ApiResult.success(result);
	  }

	  @Override
	  public ApiResult<?> getRoleGroupList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
	      RoleGroupListResponseDto result = new RoleGroupListResponseDto();

		  Page<AdminPointSettingVo> adminPointSettingVoList = adminPointSettingService.getRoleGroupList(adminPointSettingRequestDto);

	      result.setRows(adminPointSettingVoList.getResult());

	      return ApiResult.success(result);
	  }

	  @Override
	  @UserMaskingRun(system="BOS")
	  public ApiResult<?> getAdminPointSettingList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {

		  AdminPointSettingResponseDto result = new AdminPointSettingResponseDto();

		  Page<AdminPointSettingVo> adminPointSettingVoList = adminPointSettingService.getAdminPointSettingList(adminPointSettingRequestDto);

          result.setRows(adminPointSettingVoList.getResult());
	      result.setTotal(adminPointSettingVoList.getTotal());

	      return ApiResult.success(result);
	  }

	  @Override
	  @UserMaskingRun(system="BOS")
	  public ApiResult<?> getAdminPointSettingDetail(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception{
		  AdminPointSettingDetailResponseDto result = new AdminPointSettingDetailResponseDto();
	  	result = adminPointSettingService.getAdminPointSettingDetail(adminPointSettingRequestDto);
	   	return ApiResult.success(result);
	  }


	  @Override
	  public ApiResult<?> addAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception{
		  adminPointSettingService.addAdminPointSetting(adminPointSettingRequestDto);
	    	return ApiResult.success();
	  }

	  @Override
	  public ApiResult<?> putAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception{
		  adminPointSettingService.putAdminPointSetting(adminPointSettingRequestDto);
	   	return ApiResult.success();
	  }

	  @Override
	  public ApiResult<?> removeAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception{
		  adminPointSettingService.removeAdminPointSetting(adminPointSettingRequestDto);
	   	return ApiResult.success();
	  }


}
