package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.*;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

import java.util.List;

@Service("stComnBizHangaram")
public class StComnBizImpl implements StComnBiz {

    @Autowired
    StComnService stComnService;

    @Override
    public ApiResult<?> getCodeList(GetCodeListRequestDto convertRequestToObject) {
        return stComnService.getCodeList(convertRequestToObject);
    }

    @Override
    public ApiResult<?> getProgramList(GetProgramListRequestDto dto) {
        return stComnService.getProgramList(dto);
    }

    @Override
    public ApiResult<?> getMenuList(GetMenuListRequestDto dto) {
        return stComnService.getMenuList(dto);
    }

    @Override
    public ApiResult<?> getShopInfo(GetShopInfoRequestDto dto) {
        return stComnService.getShopInfo(dto);
    }

    @Override
    public ApiResult<?> getPageInfo(GetPageInfoRequestDto dto) throws Exception {
        return stComnService.getPageInfo(dto);
    }

    @Override
    public boolean isRoleMenuAuthUrl(String stRoleTpId, String stMenuId, String url) throws Exception {
    	return stComnService.isRoleMenuAuthUrl(stRoleTpId, stMenuId, url);
    }

    @Override
    public String getCodeName(String code) {
    	return stComnService.getCodeName(code);
    }

    @Override
    public List<GetCodeListResultVo> getCommonCodeList(GetCodeListRequestDto getCodeListRequestDto){

        ApiResult<?> apiResult = this.getCodeList(getCodeListRequestDto);

        GetCodeListResponseDto codeResponseDto = (GetCodeListResponseDto) apiResult.getData();
        List<GetCodeListResultVo> codeListResultVoList = codeResponseDto.getRows();

        return codeListResultVoList;
    }
}
