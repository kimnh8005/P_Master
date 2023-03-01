package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserBuyerBlackListBiz {

	GetUserBlackListResponseDto getBlackListUserList(GetUserBlackListRequestDto dto) throws Exception;

	GetUserBlackHistoryListResponseDto getUserBlackHistoryList(GetUserBlackHistoryListRequestDto dto) throws Exception;

	ApiResult<?> addUserBlack(AddUserBlackRequestDto dto) throws Exception;

	ExcelDownloadDto getBlackListUserListExportExcel(GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception;

	ApiResult<?> addBlackListUserExcelUpload(MultipartFile file) throws Exception;
}
