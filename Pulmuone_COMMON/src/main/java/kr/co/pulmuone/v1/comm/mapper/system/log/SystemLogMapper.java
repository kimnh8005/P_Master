package kr.co.pulmuone.v1.comm.mapper.system.log;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.*;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;
import kr.co.pulmuone.v1.system.log.dto.vo.ExcelDownloadLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.MenuOperLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.PrivacyMenuOperLogResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemLogMapper {
	int getConnectLogListCount(GetConnectLogListRequestDto dto);
	Page<GetConnectLogListResultVo> getConnectLogList(GetConnectLogListRequestDto dto);

	Page<GetBatchLogListResultVo> getBatchLogList(GetBatchLogListRequestDto dto);

	int addExcelDownReason(ExcelDownLogRequestDto excelDownLogRequestDto);

	Page<ExcelDownloadLogResultVo> getExcelDownloadLogList(ExcelDownLogRequestDto excelDownLogRequestDto);

	int addExcelDownloadAsync(ExcelDownloadAsyncRequestDto requestDto);

	int putExcelDownloadAsyncSetUse(Long stExcelDownloadAsyncId);

	int putExcelDownloadAsyncSetError(Long stExcelDownloadAsyncId);

	String getExcelDownloadAsyncUseYn(Long stExcelDownloadAsyncId);

	Page<MenuOperLogResultVo> getMenuOperLogList(MenuOperLogRequestDto dto);

	Page<PrivacyMenuOperLogResultVo> getPrivacyMenuOperLogList(PrivacyMenuOperLogRequestDto dto);

	int addDeviceLog(DeviceLogRequestDto dto);

	int addIllegalLog(IllegalLogRequestDto dto);

	int addIllegalLogUser(@Param("stIllegalLogId") Long stIllegalLogId, @Param("urUserId") Long urUserId);

	int addIllegalLogUserList(@Param("stIllegalLogId") Long stIllegalLogId, @Param("userIdList") List<Long> userIdList);

	int addIllegalLogOrderList(@Param("stIllegalLogId") Long stIllegalLogId, @Param("orderIdList") List<Long> orderIdList);

	List<DeviceLogVo> getDeviceLogDetect(DetectDeviceLogRequestDto dto);

	List<Long> getDeviceLogDetectUser(@Param("dto") DetectDeviceLogRequestDto dto, @Param("urPcidCd") String urPcidCd);

	List<DeviceLogVo> getLoginFailDetect(DetectDeviceLogRequestDto dto);

}
