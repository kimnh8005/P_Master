package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetPageInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetShopInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

public interface StComnBiz {

	ApiResult<?> getCodeList(GetCodeListRequestDto convertRequestToObject);

	ApiResult<?> getProgramList(GetProgramListRequestDto dto);

	ApiResult<?> getMenuList(GetMenuListRequestDto dto);

	ApiResult<?> getShopInfo(GetShopInfoRequestDto dto);

	ApiResult<?> getPageInfo(GetPageInfoRequestDto dto) throws Exception;

	boolean isRoleMenuAuthUrl(String stRoleTpId, String stMenuId, String url) throws Exception;

	public String getCodeName(String code);

	List<GetCodeListResultVo> getCommonCodeList(GetCodeListRequestDto getCodeListRequestDto);
}


