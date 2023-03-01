package kr.co.pulmuone.v1.company.dmmail.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageRequestDto;
import org.springframework.web.servlet.ModelAndView;

public interface DmMailManageBiz {
    ApiResult<?> selectDmMailList (DmMailManageRequestDto dmMailManageRequestDto) throws BaseException;

    ApiResult<?> selectDmMailInfo (DmMailManageRequestDto dmMailManageRequestDto) throws BaseException;

    ApiResult<?> selectDmMailGroupList (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> selectDmMailGroupGoodsList (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> addDmMail (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> putDmMail (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> delDmMail (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> delDmMailGroup (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ApiResult<?> delDmMailGroupDetl (DmMailManageRequestDto exhibitManageRequestDto) throws BaseException;

    ModelAndView getDmMailContentsPreview(String dmMailId) throws BaseException;

    ApiResult<?> selectDmMailContents(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException;
}
