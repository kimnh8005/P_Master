package kr.co.pulmuone.v1.system.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.system.auth.dto.*;
import kr.co.pulmuone.v1.system.auth.dto.vo.*;

import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import kr.co.pulmuone.v1.system.itgc.service.SystemItgcBiz;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.system.auth.SystemAuthMapper;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 권한관리 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 14.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class SystemAuthService {

    private final SystemAuthMapper mapper;

    private final SystemItgcBiz systemItgcBiz;

    /**
     * @Desc 회원 별 역할권한 조회
     * @param userId
     * @return List<AuthUserRoleTypeVo>
     */
    protected List<AuthUserRoleTypeVo> getAuthUserRoleTypeList(Long userId){
        return mapper.getAuthUserRoleTypeList(userId);
    }

    /**
     * @Desc 사용자권한 변경이력 등록
     * @param authUserChangeHistVo
     * @
     * @return int
     */
    protected int addAuthUserChangeHist(AuthUserChangeHistVo authUserChangeHistVo) {
        return mapper.addAuthUserChangeHist(authUserChangeHistVo);
    }

    /**
     * @Desc 사용자권한 매핑 등록
     * @param authUserRoleTypeVo
     * @
     * @return int
     */
    protected int addAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) {
        return mapper.addAuthUserMapping(authUserRoleTypeVo);
    }

    /**
     * @Desc 사용자권한 매핑 삭제
     * @param authUserRoleTypeVo
     * @
     * @return int
     */
    protected int delAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) {
        return mapper.delAuthUserMapping(authUserRoleTypeVo);
    }

    /**
     * @Desc 역할관리 조회
     * @return List<AuthUserRoleTypeVo>
     */
    protected List<AuthUserRoleTypeVo> getRoleTypeList(){
        return mapper.getRoleTypeList();
    }

    /**
     * 역할그룹관리 메인 조회
     */
    protected GetRoleListResponseDto getRoleList(GetRoleListRequestDto dto) {
        GetRoleListResponseDto result = new GetRoleListResponseDto();

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetRoleListResultVo> rows = mapper.getRoleList(dto); // rows

        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }

    /**
     * 역할그룹관리 팝업조회
     */
    protected GetRoleResponseDto getRole(GetRoleRequestDto dto) {
        GetRoleResponseDto result = new GetRoleResponseDto();
        GetRoleResultVo vo = mapper.getRole(dto);

        result.setRows(vo);

        return result;
    }

    /**
     * 역할그룹관리 신규저장
     */
    protected ApiResult<?> addRole(RoleRequestDto dto) {
        if (mapper.duplicateRoleCount(dto) > 0) {
            return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {

        	mapper.addRole(dto);

        	// 역할그룹관리 복사일 경우 권한 복사
        	if(StringUtils.isNotEmpty(dto.getAction()) && "COPY".equals(dto.getAction())) {
        		mapper.addRoleMenuAuthMapping(dto);
        	}

        }

        return ApiResult.success();
    }

    /**
     * 역할그룹관리 데이터 수정
     */
    protected ApiResult<?> putRole(RoleRequestDto dto) {
        if (mapper.duplicateRoleCount(dto) > 0) {
            return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
            mapper.putRole(dto);
        }

        return ApiResult.success();
    }

    /**
     * 역할그룹관리 데이터 삭제
     */
    protected ApiResult<?> delRole(Long inputStRoleTypeId) {
        if (mapper.existforeignKeyCount(inputStRoleTypeId) > 0) {
            return ApiResult.result(BaseEnums.CommBase.FOREIGN_KEY_DATA);
        } else {
            mapper.delRole(inputStRoleTypeId);
        }

        return ApiResult.success();
    }


    /**
     * 사용자권한관리 - 역할명 조회
     */
    protected GetRoleListWithoutPagingResponseDto getRoleListWithoutPaging() {
        GetRoleListWithoutPagingResponseDto result = new GetRoleListWithoutPagingResponseDto();

        List<getRoleListWithoutPagingResultVo> rows = mapper.getRoleListWithoutPaging(); // rows
        result.setRows(rows);

        return result;
    }

    /**
     * 사용자권한관리 -
     */
    protected GetAuthUserOutListResponseDto GetAuthUserOutList(GetAuthUserOutListRequestDto dto) {
        GetAuthUserOutListResponseDto result = new GetAuthUserOutListResponseDto();

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetAuthUserResultVo> rows = mapper.GetAuthUserOutList(dto); // rows

        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }

    /**
     * 사용자권한관리 -
     */
    protected GetAuthUserInListResponseDto getAuthUserInList(GetAuthUserInListRequestDto dto) {
        GetAuthUserInListResponseDto result = new GetAuthUserInListResponseDto();

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetAuthUserResultVo> rows = mapper.getAuthUserInList(dto); // rows

        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }

    /**
     * 사용자권한관 신규저장
     */
    protected ApiResult<?>  addAuthUser(SaveAuthUserRequestDto saveAuthUserRequestDto) {
        if(saveAuthUserRequestDto == null) {
            return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
        }

        // ITGC 등록 - 기존등록 정보 조회
        List<SaveAuthUserRequestSaveDto> authUserList = mapper.getAuthUserList(saveAuthUserRequestDto.getStRoleTypeId());

        //삭제 히스토리 생성
        if (!saveAuthUserRequestDto.getInsertSaveDataList().isEmpty()) {
            mapper.addListcheckDelAuthUsers(saveAuthUserRequestDto);
        }

        // 데이터 삭제
        mapper.delAuthUser(saveAuthUserRequestDto);

        // 신규 등록 히스토리 생성 및 데이터 저장
        if (!saveAuthUserRequestDto.getInsertSaveDataList().isEmpty()) {
            mapper.addListAddAuthUsers(saveAuthUserRequestDto);
            mapper.addListcheckAddAuthUsers(saveAuthUserRequestDto);
        }

        //ITGC 등록 - 기존 권한인원 정보 조회
        List<SaveAuthUserRequestSaveDto> insertSaveDataList = saveAuthUserRequestDto.getInsertSaveDataList();
        List<Long> insertUserList = insertSaveDataList.stream()
                .map(SaveAuthUserRequestSaveDto::getUrUserId)
                .collect(Collectors.toList());

        List<Long> getUserList = authUserList.stream()
                .map(SaveAuthUserRequestSaveDto::getUrUserId)
                .collect(Collectors.toList());

        //ITGC 등록 - 삭제대상자 정보
        List<ItgcRequestDto> itgcList = new ArrayList<>();
        for (SaveAuthUserRequestSaveDto dto : authUserList) {
            if(insertUserList.contains(dto.getUrUserId())) continue;

            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.ADMIN_AUTH.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.AUTH_DEL)
                    .itsmId(saveAuthUserRequestDto.getItsmId())
                    .itgcDetail(saveAuthUserRequestDto.getRoleName())
                    .itgcAddType(SystemEnums.ItgcAddType.DEL)
                    .targetInfo(dto.getUserName())
                    .targetUserId(dto.getUrUserId())
                    .createId(Long.valueOf(saveAuthUserRequestDto.getUserVo().getUserId()))
                    .build());
        }

        //ITGC 등록 - 추가대상자 정보
        for (SaveAuthUserRequestSaveDto dto : insertSaveDataList) {
            if(getUserList.contains(dto.getUrUserId())) continue;

            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.ADMIN_AUTH.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.AUTH_ADD)
                    .itsmId(saveAuthUserRequestDto.getItsmId())
                    .itgcDetail(saveAuthUserRequestDto.getRoleName())
                    .itgcAddType(SystemEnums.ItgcAddType.ADD)
                    .targetInfo(dto.getUserName())
                    .targetUserId(dto.getUrUserId())
                    .createId(Long.valueOf(saveAuthUserRequestDto.getUserVo().getUserId()))
                    .build());
        }

        //ITGC 등록 - Biz 호출
        if(!itgcList.isEmpty()){
            systemItgcBiz.addItgcList(itgcList);
        }

        return ApiResult.success();
    }

    /**
     * 메뉴권한관리 메뉴관리 리스트
     */
    protected GetAuthMenuListResponseDto getAuthMenuList(Long stRoleTypeId, Long stMenuGroupId) {
        GetAuthMenuListResponseDto result = new GetAuthMenuListResponseDto();

        List<GetAuthMenuListResultVo> rows = mapper.getAuthMenuList(stRoleTypeId, stMenuGroupId);

        result.setRows(rows);

        return result;
    }

    /**
     * 할당된 URL 리스트
     */
    protected GetRoleMenuAuthListResponseDto getRoleMenuAuthList(Long stRoleTypeId, Long stMenuId) {
        GetRoleMenuAuthListResponseDto result = new GetRoleMenuAuthListResponseDto();

        List<GetRoleMenuAuthListResultVo> rows = mapper.getRoleMenuAuthList(stRoleTypeId, stMenuId);

        result.setRows(rows);

        return result;
    }

    /**
     * 할당된 URL 리스트 저장
     */
    protected ApiResult<?>  saveAuthMenu(SaveRoleMenuAuthRequestDto dto) {
        List<SaveRoleMenuAuthRequestDtoSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
        List<SaveRoleMenuAuthRequestDtoSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

        if(insertRequestDtoList.size() == 0 && deleteRequestDtoList.size() == 0) {
            return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
        }

        if(!deleteRequestDtoList.isEmpty()) {
            mapper.delAuthMenu(deleteRequestDtoList);
        }

        if (!insertRequestDtoList.isEmpty()) {
            mapper.saveAuthMenu(dto.getStRoleTypeId(), dto.getStMenuId(), insertRequestDtoList);
        }

        //ITGC 등록 - 삭제대상자 정보
        List<ItgcRequestDto> itgcList = new ArrayList<>();
        for (SaveRoleMenuAuthRequestDtoSaveDto requestDto : deleteRequestDtoList) {
            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.MENU_AUTH.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.AUTH_DEL)
                    .itsmId(dto.getItsmId())
                    .itgcDetail(dto.getRoleName())
                    .itgcAddType(SystemEnums.ItgcAddType.DEL)
                    .targetInfo(dto.getMenuName() + ", " + requestDto.getProgramAuthCodeName())
                    .createId(Long.valueOf(dto.getUserVo().getUserId()))
                    .build());
        }

        //ITGC 등록 - 추가대상자 정보
        for (SaveRoleMenuAuthRequestDtoSaveDto requestDto : insertRequestDtoList) {
            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.MENU_AUTH.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.AUTH_ADD)
                    .itsmId(dto.getItsmId())
                    .itgcDetail(dto.getRoleName())
                    .itgcAddType(SystemEnums.ItgcAddType.ADD)
                    .targetInfo(dto.getMenuName() + ", " + requestDto.getProgramAuthCodeName())
                    .createId(Long.valueOf(dto.getUserVo().getUserId()))
                    .build());
        }

        //ITGC 등록 - Biz 호출
        if(!itgcList.isEmpty()){
            systemItgcBiz.addItgcList(itgcList);
        }

        return ApiResult.success();
    }

    /**
	 * 관리자 역활그룹의 권한 목록 리스트
	 *
	 * @param getBuyerListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	protected ExcelDownloadDto getRoleMenuAuthListExportExcel(Long stRoleTypeId) throws Exception {
		GetRoleRequestDto dto = new GetRoleRequestDto();
		dto.setStRoleTypeId(stRoleTypeId.toString());
		GetRoleResponseDto resRoleDto = getRole(dto);
		String excelFileName = "역할그룹(" + resRoleDto.getRows().getInputRoleName() + ") 할당된 권한 목록 엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = { 200, 200, 400, 200, 300};

		String[] alignListOfFirstWorksheet = { "left", "left", "left", "center", "left"};

		String[] propertyListOfFirstWorksheet = { "menuGroupName", "parentsMenuName", "menuName", "programAuthCode", "programAuthCodeName"};

		String[] firstHeaderListOfFirstWorksheet = { "대메뉴명", "중메뉴명", "소메뉴명", "권한코드", "권한코드명" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<RoleMenuAuthListExcelDto>  list = null;
		try
		{
			list = mapper.getRoleMenuAuthExcelList(stRoleTypeId);
		}
		catch (Exception e)
		{
			throw e; // 추후 CustomException 으로 변환 예정
		}
		firstWorkSheetDto.setExcelDataList(list);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}
}
