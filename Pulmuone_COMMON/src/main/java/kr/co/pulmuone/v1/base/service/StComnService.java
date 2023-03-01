package kr.co.pulmuone.v1.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.base.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.ConvertNavigationParamDto;
import kr.co.pulmuone.v1.base.dto.ConvertNavigationResultDto;
import kr.co.pulmuone.v1.base.dto.GetAuthMenuParamDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListResponseDto;
import kr.co.pulmuone.v1.base.dto.GetNavigationMenuNameParamDto;
import kr.co.pulmuone.v1.base.dto.GetNavigationMenuNameResultDto;
import kr.co.pulmuone.v1.base.dto.GetPageInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.GetPageInfoResponseDto;
import kr.co.pulmuone.v1.base.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetProgramListResponseDto;
import kr.co.pulmuone.v1.base.dto.GetShopInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.GetShopInfoResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.config.LangConfig;
import kr.co.pulmuone.v1.comm.config.SiteConfig;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.CommBase;
import kr.co.pulmuone.v1.comm.mapper.base.StComnMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetLangListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetStShopListResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class StComnService {

	private final StComnMapper stComnMapper;

	@Value("${resource.server.url.mall}")
	private String serverUrlMall;

	protected ApiResult<?> getCodeList(GetCodeListRequestDto dto) {
		GetCodeListResponseDto result = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = stComnMapper.getCodeList(dto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	protected String getCodeName(String code) {
		return stComnMapper.getCodeName(code);
	}

	protected ApiResult<?> getProgramList(GetProgramListRequestDto dto) {
		GetProgramListResponseDto result = new GetProgramListResponseDto();

        UserVo vo = SessionUtil.getBosUserVO();
		List<GetProgramListResultVo> rows = stComnMapper.getProgramList(dto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	protected ApiResult<?> getMenuList(GetMenuListRequestDto dto) {
		GetMenuListResponseDto result = new GetMenuListResponseDto();

		List<GetMenuGroupListResultVo> rows = stComnMapper.getMenuGroupList(dto);	// rows
		List<GetMenuListResultVo> menuList = stComnMapper.getMenuList(dto);			// rows

		result.setRows(rows);
		result.setMenuList(menuList);

		return ApiResult.success(result);
	}


    protected ApiResult<?> getShopInfo(GetShopInfoRequestDto dto) {

        GetShopInfoResponseDto result = new GetShopInfoResponseDto();
        GetShopInfoResultVo    rows   = new GetShopInfoResultVo()   ;

        GetStShopListResultVo shopInfoMap = SiteConfig.getInstance().getShopValue();

		rows.setId                  (StringUtil.nvl(shopInfoMap.getShopId()      ));
		rows.setShopName            (StringUtil.nvl(shopInfoMap.getShopName()    ));
		rows.setSiteDomain          (StringUtil.nvl(serverUrlMall));
		rows.setSiteDomainMobile    (StringUtil.nvl(shopInfoMap.getSiteDomainMo()));
		rows.setShopAuthorizationId (StringUtil.nvl(shopInfoMap.getShopAuthzId() ));
		rows.setShopAuthorizationKey(StringUtil.nvl(shopInfoMap.getShopAuthzKey()));
		rows.setCountryCode         (StringUtil.nvl(shopInfoMap.getCountryCode() ));

		result.setRows(rows);

		return ApiResult.success(result);
	}

	protected ApiResult<?> getPageInfo(GetPageInfoRequestDto dto) throws Exception {

		GetPageInfoResponseDto result = new GetPageInfoResponseDto();

		// 메뉴권한
		GetAuthMenuParamDto getAuthMenuParamDto = new GetAuthMenuParamDto();
		getAuthMenuParamDto.setStMenuId(dto.getStMenuId());

		String[] authMenu = getAuthMenu(getAuthMenuParamDto);

		if (authMenu.length == 0) {
			return ApiResult.result(CommBase.PAGE_AUTH_FAIL);
		}

		// Navigation
		GetNavigationMenuNameParamDto getNavigationMenuNameParamDto = new GetNavigationMenuNameParamDto();
		getNavigationMenuNameParamDto.setStMenuId(dto.getStMenuId());
		GetNavigationMenuNameResultDto navigationMenuNameResultDto = getNavigationMenuName(getNavigationMenuNameParamDto);

		GetMenuNameParamVo getMenuNameParamVo = new GetMenuNameParamVo();
		getMenuNameParamVo.setStMenuId(dto.getStMenuId());
		GetMenuNameResultVo menuInfo = stComnMapper.getMenuName(getMenuNameParamVo);

		result.setAuth(authMenu);
		result.setSession(SessionUtil.getBosUserVO());
		result.setNavi(navigationMenuNameResultDto.getPageNavi());
		result.setTitle(navigationMenuNameResultDto.getTitle());
		result.setStHelpId(menuInfo.getStHelpId());
//		result.setLang(this.getLangPageData());

		return ApiResult.success(result);
	}

	protected String[] getAuthMenu(GetAuthMenuParamDto dto) throws Exception {
		return stComnMapper.getAuthMenu(dto);
	}

	protected boolean isRoleMenuAuthUrl(String stRoleTpId, String stMenuId, String url) throws Exception {
		// 마스터 권한 여부
		if(new BaseDto().getMasterAuth()) {
			return true;
		} else {
			//return stComnMapper.isRoleMenuAuthUrl(stRoleTpId, stMenuId, url);
			return stComnMapper.isRoleMenuAuthUrl(SessionUtil.getBosUserVO().getListRoleId(), stMenuId, url);
		}
	}

	protected GetNavigationMenuNameResultDto getNavigationMenuName(GetNavigationMenuNameParamDto dto) throws Exception {
		GetMenuNameParamVo getMenuNameParamVo = new GetMenuNameParamVo();
		getMenuNameParamVo.setStMenuId(dto.getStMenuId());
		GetMenuNameResultVo menuInfo = stComnMapper.getMenuName(getMenuNameParamVo);

		ConvertNavigationParamDto convertNavigationParamDto = new ConvertNavigationParamDto();
		convertNavigationParamDto.setStMenuId(dto.getStMenuId());
		convertNavigationParamDto.setTitle(menuInfo.getMenuName());
		convertNavigationParamDto.setPageNavi(menuInfo.getMenuName());

		ConvertNavigationResultDto convertNavigationResultDto = this.convertNavigation(convertNavigationParamDto, menuInfo);

		GetNavigationMenuNameResultDto result = new GetNavigationMenuNameResultDto();
		result.setPageNavi(convertNavigationResultDto.getPageNavi());
		result.setTitle(convertNavigationResultDto.getTitle());

		return result;
	}

	protected ConvertNavigationResultDto convertNavigation( ConvertNavigationParamDto dto, GetMenuNameResultVo menuInfo ) throws Exception {
		dto.setStMenuId(menuInfo.getParentMenuId());

		GetMenuNameParamVo getMenuNameParamVo = new GetMenuNameParamVo();
		getMenuNameParamVo.setStMenuId(dto.getStMenuId());
		menuInfo = stComnMapper.getMenuName(getMenuNameParamVo);

		dto.setPageNavi(StringUtil.nvl(menuInfo.getMenuName()) + " &gt; " + dto.getPageNavi() );
		if( !"0".equals(StringUtil.nvl(menuInfo.getParentMenuId())) ){
			this.convertNavigation(dto, menuInfo);
		}

		ConvertNavigationResultDto result = new ConvertNavigationResultDto();
		result.setTitle(dto.getTitle());
		result.setPageNavi(dto.getPageNavi());

		return result;
	}


    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected HashMap<String, String> getLangPageData() throws Exception {

        Map<String, String> map = new HashMap<> ();
        UserVo vo = SessionUtil.getBosUserVO();
        map.put("GB_LANG_ID", vo.getLangCode());

        //------------------------------ 다국어 정보 가져오기 [S] ----------------------------------
        LangConfig ccfg = LangConfig.getInstance();
        List<GetLangListResultVo> list = ccfg.getValueList(map);
		//------------------------------ 다국어 정보 가져오기 [E] ----------------------------------

        HashMap<String, String> lang = new HashMap<String, String> ();

        if ( list != null ) {
            for(int i=0; i<list.size(); i++){
                String k = StringUtil.nvl(  ( (GetLangListResultVo)list.get(i) ).getGbDicMstId() );
                String v = StringUtil.nvl(  ( (GetLangListResultVo)list.get(i) ).getDicName()    );
                lang.put(k, v);
			}
        }

        return lang;
	}


}

