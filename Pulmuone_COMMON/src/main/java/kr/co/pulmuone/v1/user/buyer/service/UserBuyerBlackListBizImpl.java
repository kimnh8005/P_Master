package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserBuyerBlackListBizImpl implements UserBuyerBlackListBiz {

    @Autowired
    private UserBuyerBlackListService userBuyerBlackListService;

    @Override
    public GetUserBlackListResponseDto getBlackListUserList(GetUserBlackListRequestDto dto) throws Exception {
        return userBuyerBlackListService.getBlackListUserList(dto);
    }

    @Override
    public GetUserBlackHistoryListResponseDto getUserBlackHistoryList(GetUserBlackHistoryListRequestDto dto) throws Exception {
        return userBuyerBlackListService.getUserBlackHistoryList(dto);
    }

    @Override
    public ApiResult<?> addUserBlack(AddUserBlackRequestDto dto) throws Exception {
        return ApiResult.success(userBuyerBlackListService.addUserBlack(dto));
    }

    @Override
    public ExcelDownloadDto getBlackListUserListExportExcel(GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception {
        return userBuyerBlackListService.getBlackListUserListExportExcel(getUserBlackListRequestDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public ApiResult<?> addBlackListUserExcelUpload(MultipartFile file) throws Exception {
        return userBuyerBlackListService.addClaimExcelUpload(file);
    }
}
