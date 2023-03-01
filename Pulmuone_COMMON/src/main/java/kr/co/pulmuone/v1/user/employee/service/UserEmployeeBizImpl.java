package kr.co.pulmuone.v1.user.employee.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import kr.co.pulmuone.v1.system.auth.service.SystemAuthBiz;
import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import kr.co.pulmuone.v1.system.itgc.service.SystemItgcBiz;
import kr.co.pulmuone.v1.user.certification.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeResponseDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;
import kr.co.pulmuone.v1.user.join.dto.vo.AccountVo;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
* <PRE>
* Forbiz Korea
* 관리자회원 BizImpl
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
public class UserEmployeeBizImpl  implements UserEmployeeBiz {

    @Autowired
    UserEmployeeService userEmployeeService;

    @Autowired
    SystemAuthBiz systemAuthBiz;

    @Autowired
    UserJoinBiz userJoinBiz;

    @Autowired
    UserCertificationBiz userCertificationBiz;

    @Autowired
    SendTemplateBiz sendTemplateBiz;

    @Autowired
    ComnBizImpl comnBizImpl;

    @Autowired
    private SystemItgcBiz systemItgcBiz;

    /**
     * @Desc BOS 계정관리 조회
     * @param employeeRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getEmployeeList(EmployeeRequestDto employeeRequestDto){
        EmployeeResponseDto result = new EmployeeResponseDto();

        if( StringUtils.isNotEmpty(employeeRequestDto.getAdminType()) && employeeRequestDto.getAdminType().indexOf("ALL") < 0 ) {
            employeeRequestDto.setAdminTypeList(Stream.of(employeeRequestDto.getAdminType().split(Constants.ARRAY_SEPARATORS))
                                                      .map(String::trim)
                                                      .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                      .collect(Collectors.toList()));
        }

        if( StringUtils.isNotEmpty(employeeRequestDto.getUserStatus()) && employeeRequestDto.getUserStatus().indexOf("ALL") < 0 ) {
            employeeRequestDto.setUserStatusList(Stream.of(employeeRequestDto.getUserStatus().split(Constants.ARRAY_SEPARATORS))
                                                       .map(String::trim)
                                                       .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                       .collect(Collectors.toList()));
        }

        Page<EmployeeVo> employeeList = userEmployeeService.getEmployeeList(employeeRequestDto);

        result.setTotal(employeeList.getTotal());
        result.setRows(employeeList.getResult());

        return ApiResult.success(result);
    }

    /**
     * @Desc BOS 회원정보 조회
     * @param employeeNumber
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getEmployeeInfo(String employeeNumber) {
        EmployeeResponseDto result = new EmployeeResponseDto();

        EmployeeVo employeeInfo = userEmployeeService.getEmployeeInfo(employeeNumber);

        result.setEmployeeInfo(employeeInfo);

        if( !ObjectUtils.isEmpty(employeeInfo) ) {

            List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthBiz.getAuthUserRoleTypeList(employeeInfo.getUserId());
            result.setAuthRoleList(authUserRoleTypeList);

            EmployeeAuthVo authSupplierVo = new EmployeeAuthVo();
            authSupplierVo.setUrEmployeeCd(employeeNumber);
            authSupplierVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.SUPPLIER.getCode());
            List<EmployeeAuthVo> authSupplierList = this.getEmployeeAuthList(authSupplierVo);
            result.setAuthSupplierList(authSupplierList);

            EmployeeAuthVo authWarehouseVo = new EmployeeAuthVo();
            authWarehouseVo.setUrEmployeeCd(employeeNumber);
            authWarehouseVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.WAREHOUSE.getCode());
        	List<EmployeeAuthVo> authWarehouseList = this.getEmployeeAuthList(authWarehouseVo);
            result.setAuthWarehouseList(authWarehouseList);
        }

        return ApiResult.success(result);
    }

    /**
     * @Desc ERP 임직원 정보 조회
     * @param employeeNumber
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getErpEmployeeInfo(String employeeNumber){
        EmployeeResponseDto result = new EmployeeResponseDto();

        EmployeeVo erpEmployeeInfo = userEmployeeService.getErpEmployeeInfo(employeeNumber);
        result.setErpEmployeeInfo(erpEmployeeInfo);

        return ApiResult.success(result);
    }

    /**
     * @Desc 회원정보 등록
     * 1. 로그인 ID 소문자로 변환
     * 2. 데이터 검증
     * 3. UR_USER (회원기본정보) 등록
     * 4. 비밀번호 랜덤 생성
     * 5. UR_CERTIFICATION (로그인 인증 정보) 등록
     * 6. UR_ACCOUNT (회원정보활동정보) 등록
     * 7. UR_EMPLOYEE (회원상세정보) 등록
     * 8. UR_EMPLOYEE_AUTH (권한 공급업체 및 출고처) 등록
     * 9. 권한 역할 등록
     * 10. 등록된 회원정보 메일 전송
     * 11. ITGC 등록
     * @param employeeInfoRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addEmployeeInfo(EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception{

        // 로그인 ID 소문자로 변환
        employeeInfoRequestDto.setLoginId(employeeInfoRequestDto.getLoginId().toLowerCase());

        // 데이터 검증
        if( !ObjectUtils.isEmpty( userEmployeeService.getEmployeeInfo(employeeInfoRequestDto.getEmployeeNumber()) ) ) {
            return ApiResult.result(UserEnums.Join.EMPLOYEE_NUMBER_DUPLICATE);
        }

        if( !ObjectUtils.isEmpty( userJoinBiz.getUserInfo(employeeInfoRequestDto.getLoginId()) ) ) {
            return ApiResult.result(UserEnums.Join.ID_DUPLICATE);
        }

        if( CompanyEnums.CompanyType.CLIENT.getCode().equals(employeeInfoRequestDto.getAdminType())
                && userEmployeeService.getEmailDuplocateCheck(employeeInfoRequestDto) ) {
            return ApiResult.result(UserEnums.Join.EMAIL_DUPLICATE);
        }

        if( CollectionUtils.isEmpty(employeeInfoRequestDto.getRoleList()) ) {
            return ApiResult.result(SystemEnums.UserAuth.NEED_USER_AUTH);
        }

        // 회원기본정보 등록
        UserVo userVo = this.setUserParamVo(employeeInfoRequestDto);
        userJoinBiz.addUser(userVo);

        // 비밀번호 랜덤 생성 TODO : 임시로 패스워드 로그인 ID 로 생성되도록 수정
        //String randomPassword = RandomStringUtils.randomAlphanumeric(10);
        String randomPassword = employeeInfoRequestDto.getLoginId();

        // 로그인 인증정보 등록
        CertificationVo certificationVo = this.setCertificationParamVo(userVo, randomPassword);
        userCertificationBiz.addCertification(certificationVo);

        // 회원정보활동정보 등록
        AccountVo accountVo = new AccountVo();
        accountVo.setUserId(userVo.getUserId());
        userJoinBiz.addAccount(accountVo);

        // 회원상세정보 등록
        EmployeeVo employeeInfoVo = this.setEmployeeInfoVo(employeeInfoRequestDto, userVo);
        userEmployeeService.addEmployee(employeeInfoVo);

        // 권한 공급업체 및 출고처 등록
        this.addEmployeeAuthList(employeeInfoRequestDto);

        // 권한 역할 등록
        this.addAuthUserMappingList(employeeInfoRequestDto, userVo.getUserId(), new ArrayList<AuthUserRoleTypeVo>());

        // 관리자 가입완료 자동메일 발송
        this.emailPush(employeeInfoVo);

        // ITGC 등록 - 계정등록
        List<ItgcRequestDto> itgcList = new ArrayList<>();
        itgcList.add(ItgcRequestDto.builder()
                .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                .itgcType(SystemEnums.ItgcType.ACCOUNT_ADD)
                .itsmId(employeeInfoRequestDto.getItsmId())
                .itgcDetail(SystemEnums.ItgcDetailType.ACCOUNT_ADD.getCodeName())
                .itgcAddType(SystemEnums.ItgcAddType.ADD)
                .targetInfo(userVo.getUserName())
                .targetUserId(userVo.getUserId())
                .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                .build());

        // ITGC 등록 - 개인정보접근권한
        itgcList.add(ItgcRequestDto.builder()
                .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                .itgcType(SystemEnums.ItgcType.PERSONAL_INFO)
                .itsmId(employeeInfoRequestDto.getItsmId())
                .itgcDetail(SystemEnums.ItgcDetailType.PERSONAL_INFO.getCodeName())
                .itgcAddType(SystemEnums.ItgcAddType.ADD)
                .targetInfo(userVo.getUserName())
                .targetUserId(userVo.getUserId())
                .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                .build());

        // ITGC 등록 - 역할정보 저장
        String[] masterArr = Constants.MASTER_AUTH_ST_ROLE_TP_ID;
        List<String> masterList = Arrays.stream(masterArr).collect(Collectors.toList());
        if(!employeeInfoRequestDto.getRoleList().isEmpty()){
            for (AuthUserRoleTypeVo vo : employeeInfoRequestDto.getRoleList()) {
                // ITGC 등록 - 역할정보 저장
                itgcList.add(ItgcRequestDto.builder()
                        .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                        .itgcType(SystemEnums.ItgcType.ROLE_ADD)
                        .itsmId(employeeInfoRequestDto.getItsmId())
                        .itgcDetail(vo.getRoleTypeName())
                        .itgcAddType(SystemEnums.ItgcAddType.ADD)
                        .targetInfo(userVo.getUserName())
                        .targetUserId(userVo.getUserId())
                        .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                        .build());

                // ITGC 등록 - SUPER 역할그룹 반영
                if(masterList.contains(String.valueOf(vo.getRoleTypeId()))){
                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPER_ROLE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getRoleTypeName())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(userVo.getUserName())
                            .targetUserId(userVo.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }
        }

        // ITGC 등록 - 공급처, 출고처 저장
        if (CompanyEnums.CompanyType.HEADQUARTERS.getCode().equals(employeeInfoRequestDto.getAdminType())) {    // 본사회원인지 체크
            // 공급업체
            if(!employeeInfoRequestDto.getAuthSupplierIdList().isEmpty()){
                // 공급업체 등록
                for (EmployeeAuthVo vo : employeeInfoRequestDto.getAuthSupplierIdList()) {
                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPPLIER)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(userVo.getUserName())
                            .targetUserId(userVo.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }

            // 출고처
            if(!employeeInfoRequestDto.getAuthWarehouseIdList().isEmpty()){
                // 출고처 등록
                for (EmployeeAuthVo vo : employeeInfoRequestDto.getAuthWarehouseIdList()) {
                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.WAREHOUSE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(userVo.getUserName())
                            .targetUserId(userVo.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }
        }
        
        // ITGC 등록 - Biz 호출
        if(!itgcList.isEmpty()){
            systemItgcBiz.addItgcList(itgcList);
        }

        return ApiResult.success();
    }

    /**
     * @Desc 회원정보 수정
     * 1. 회원상세정보 조회
     * 2. 회원정보 수정
     *   2-1. 본사
     *       : 회원상세정보 수정
     *   2-2. 거래처
     *       : 회원기본정보 조회
     *       : 회원기본정보 수정
     *       : 회원상세정보 수정
     * 3. UR_EMPLOYEE_AUTH (권한 공급업체 및 출고처) 등록
     * 4. 권한 역할 삭제
     * 5. 권한 역할 등록
     * 6. ITGC 등록
     * @param employeeInfoRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putEmployeeInfo(EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception{

        // 회원상세정보 조회
        EmployeeVo employeeInfo = userEmployeeService.getEmployeeInfo(employeeInfoRequestDto.getEmployeeNumber());

        if( CompanyEnums.CompanyType.HEADQUARTERS.getCode().equalsIgnoreCase(employeeInfoRequestDto.getAdminType()) ) {

            // 회원상세정보 수정
            EmployeeVo employeeVo = this.setHeadquartersEmployeeParamVo(employeeInfoRequestDto, employeeInfo);
            userEmployeeService.putEmployee(employeeVo);

        }else {

            // 회원 기본정보 조회
            UserVo userVo = userJoinBiz.getUserInfo(employeeInfoRequestDto.getLoginId());

            if( !employeeInfoRequestDto.getUserName().equalsIgnoreCase(userVo.getUserName()) ) {
                // 회원 기본정보 수정
                UserVo userParamVo = new UserVo();
                userParamVo.setUserId(employeeInfoRequestDto.getUserId());
                userParamVo.setUserName(employeeInfoRequestDto.getUserName());

                userJoinBiz.putUser(userParamVo);
            }

            // 회원상세정보 수정
            EmployeeVo employeeVo = this.setClientEmployeeParamVo(employeeInfoRequestDto, employeeInfo);
            userEmployeeService.putEmployee(employeeVo);
        }

        // 권한 공급업체 및 출고처 정보 조회
        EmployeeAuthVo employeeAuthVo = new EmployeeAuthVo();
        employeeAuthVo.setUrEmployeeCd(employeeInfoRequestDto.getEmployeeNumber());
        employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.SUPPLIER.getCode());
        List<EmployeeAuthVo> employeeSAuthVoList = userEmployeeService.getEmployeeAuthList(employeeAuthVo);
        employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.WAREHOUSE.getCode());
        List<EmployeeAuthVo> employeeWAuthVoList = userEmployeeService.getEmployeeAuthList(employeeAuthVo);

        // 권한 공급업체 및 출고처 등록
        this.addEmployeeAuthList(employeeInfoRequestDto);

        // 등록된 권한 조회
        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthBiz.getAuthUserRoleTypeList(employeeInfoRequestDto.getUserId());

        // 권한 역할 삭제
        this.delAuthUserMappingList(employeeInfoRequestDto, authUserRoleTypeList);

        // 권한 역할 등록
        this.addAuthUserMappingList(employeeInfoRequestDto, employeeInfoRequestDto.getUserId(), authUserRoleTypeList);

        // ITGC 등록 - 수정 케이스
        addItgcLogPutProcess(employeeInfoRequestDto, authUserRoleTypeList, employeeSAuthVoList, employeeWAuthVoList);

        return ApiResult.success();
    }

    /**
     * ITGC 등록 - 수정 케이스
     * 1. 개인정보접근권한
     * 2. 역할정보 저장 - 삭제, 추가
     * 3. 공급업체, 출고처 - 삭제, 추가
     * @param employeeInfoRequestDto
     * @return ApiResult
     * @throws Exception
     */
    private void addItgcLogPutProcess(EmployeeInfoRequestDto employeeInfoRequestDto, List<AuthUserRoleTypeVo> authUserRoleTypeList, List<EmployeeAuthVo> employeeSAuthVoList, List<EmployeeAuthVo> employeeWAuthVoList) {
        // 1. ITGC 등록 - 개인정보접근권한
        List<ItgcRequestDto> itgcList = new ArrayList<>();
        if(!StringUtil.isEmpty(employeeInfoRequestDto.getPrivacyItsmId())){
            //삭제
            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.PERSONAL_INFO)
                    .itsmId(employeeInfoRequestDto.getPrivacyItsmId())
                    .itgcDetail(SystemEnums.ItgcDetailType.PERSONAL_INFO.getCodeName())
                    .itgcAddType(SystemEnums.ItgcAddType.DEL)
                    .targetInfo(employeeInfoRequestDto.getUserName())
                    .targetUserId(employeeInfoRequestDto.getUserId())
                    .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                    .build());

            //추가
            itgcList.add(ItgcRequestDto.builder()
                    .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                    .itgcType(SystemEnums.ItgcType.PERSONAL_INFO)
                    .itsmId(employeeInfoRequestDto.getPrivacyItsmId())
                    .itgcDetail(SystemEnums.ItgcDetailType.PERSONAL_INFO.getCodeName())
                    .itgcAddType(SystemEnums.ItgcAddType.ADD)
                    .targetInfo(employeeInfoRequestDto.getUserName())
                    .targetUserId(employeeInfoRequestDto.getUserId())
                    .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                    .build());
        }

        // 2. ITGC 등록 - 역할정보 저장 - 삭제, 추가
        if(!employeeInfoRequestDto.getRoleList().isEmpty() && !StringUtil.isEmpty(employeeInfoRequestDto.getItsmId())){
            List<Long> getRoleIdList = authUserRoleTypeList.stream()
                    .map(AuthUserRoleTypeVo::getRoleTypeId)
                    .collect(Collectors.toList());
            List<Long> requestRoleIdList = employeeInfoRequestDto.getRoleList().stream()
                    .map(AuthUserRoleTypeVo::getRoleTypeId)
                    .collect(Collectors.toList());
            String[] masterArr = Constants.MASTER_AUTH_ST_ROLE_TP_ID;
            List<String> masterList = Arrays.stream(masterArr).collect(Collectors.toList());

            // 삭제
            for (AuthUserRoleTypeVo vo : authUserRoleTypeList) {
                if(requestRoleIdList.contains(vo.getRoleTypeId())) continue;

                itgcList.add(ItgcRequestDto.builder()
                        .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                        .itgcType(SystemEnums.ItgcType.ROLE_ADD)
                        .itsmId(employeeInfoRequestDto.getItsmId())
                        .itgcDetail(vo.getRoleTypeName())
                        .itgcAddType(SystemEnums.ItgcAddType.DEL)
                        .targetInfo(employeeInfoRequestDto.getUserName())
                        .targetUserId(employeeInfoRequestDto.getUserId())
                        .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                        .build());


                // ITGC 등록 - SUPER 역할그룹 반영
                if(masterList.contains(String.valueOf(vo.getRoleTypeId()))){
                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPER_ROLE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getRoleTypeName())
                            .itgcAddType(SystemEnums.ItgcAddType.DEL)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }

            // 등록
            for (AuthUserRoleTypeVo vo : employeeInfoRequestDto.getRoleList()) {
                if(getRoleIdList.contains(vo.getRoleTypeId())) continue;

                itgcList.add(ItgcRequestDto.builder()
                        .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                        .itgcType(SystemEnums.ItgcType.ROLE_ADD)
                        .itsmId(employeeInfoRequestDto.getItsmId())
                        .itgcDetail(vo.getRoleTypeName())
                        .itgcAddType(SystemEnums.ItgcAddType.ADD)
                        .targetInfo(employeeInfoRequestDto.getUserName())
                        .targetUserId(employeeInfoRequestDto.getUserId())
                        .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                        .build());

                // ITGC 등록 - SUPER 역할그룹 반영
                if(masterList.contains(String.valueOf(vo.getRoleTypeId()))){
                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPER_ROLE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getRoleTypeName())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }
        }

        // 3. ITGC 등록 - 공급업체, 출고처 - 삭제, 추가
        if (CompanyEnums.CompanyType.HEADQUARTERS.getCode().equals(employeeInfoRequestDto.getAdminType())) {    // 본사회원인지 체크

            // 공급업체
            if(!StringUtil.isEmpty(employeeInfoRequestDto.getItsmId())){
                List<String> getSAuthIdList = employeeSAuthVoList.stream()
                        .map(EmployeeAuthVo::getAuthId)
                        .collect(Collectors.toList());
                List<String> requestSAuthIdList = employeeInfoRequestDto.getAuthSupplierIdList().stream()
                        .map(EmployeeAuthVo::getAuthId)
                        .collect(Collectors.toList());

                // 공급업체 삭제
                for (EmployeeAuthVo vo : employeeSAuthVoList) {
                    if(requestSAuthIdList.contains(vo.getAuthId())) continue;

                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPPLIER)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.DEL)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }

                // 공급업체 등록
                for (EmployeeAuthVo vo : employeeInfoRequestDto.getAuthSupplierIdList()) {
                    if(getSAuthIdList.contains(vo.getAuthId())) continue;

                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.SUPPLIER)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }

            // 출고처
            if(!StringUtil.isEmpty(employeeInfoRequestDto.getItsmId())){
                List<String> getWAuthIdList = employeeWAuthVoList.stream()
                        .map(EmployeeAuthVo::getAuthId)
                        .collect(Collectors.toList());
                List<String> requestWAuthIdList = employeeInfoRequestDto.getAuthWarehouseIdList().stream()
                        .map(EmployeeAuthVo::getAuthId)
                        .collect(Collectors.toList());

                // 출고처 삭제
                for (EmployeeAuthVo vo : employeeWAuthVoList) {
                    if(requestWAuthIdList.contains(vo.getAuthId())) continue;

                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.WAREHOUSE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.DEL)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }

                // 출고처 등록
                for (EmployeeAuthVo vo : employeeInfoRequestDto.getAuthWarehouseIdList()) {
                    if(getWAuthIdList.contains(vo.getAuthId())) continue;

                    itgcList.add(ItgcRequestDto.builder()
                            .stMenuId(SystemEnums.ItgcMenu.BOS_ACCOUNT.getStMenuId())
                            .itgcType(SystemEnums.ItgcType.WAREHOUSE)
                            .itsmId(employeeInfoRequestDto.getItsmId())
                            .itgcDetail(vo.getAuthId())
                            .itgcAddType(SystemEnums.ItgcAddType.ADD)
                            .targetInfo(employeeInfoRequestDto.getUserName())
                            .targetUserId(employeeInfoRequestDto.getUserId())
                            .createId(Long.valueOf(employeeInfoRequestDto.getUserVo().getUserId()))
                            .build());
                }
            }
        }

        // ITGC 등록 - Biz 호출
        if(!itgcList.isEmpty()){
            systemItgcBiz.addItgcList(itgcList);
        }
    }

    /**
     * @Desc 회원기본정보 조회
     * @param employeeInfoRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getUserInfo(String loginId){
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        UserVo userVo = userJoinBiz.getUserInfo(loginId);
        employeeResponseDto.setUserInfo(userVo);
        return ApiResult.success(employeeResponseDto);
    }

    /**
     * @Desc 이메일 중복검사
     * @param employeeInfoRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getEmailDuplocateCheck(EmployeeInfoRequestDto employeeInfoRequestDto){
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        boolean emailDuplocateYn = userEmployeeService.getEmailDuplocateCheck(employeeInfoRequestDto);
        employeeResponseDto.setEmailDuplocate(emailDuplocateYn);
        return ApiResult.success(employeeResponseDto);
    }

    /**
     * @Desc 권한설정 사용자권한 매핑 삭제
     * @param employeeRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delAuthUserMapping(EmployeeRequestDto employeeRequestDto) throws Exception{
        AuthUserRoleTypeVo authRoleResultVo = new AuthUserRoleTypeVo();
        authRoleResultVo.setRoleTypeId(employeeRequestDto.getRoleTypeId());
        authRoleResultVo.setUserId(employeeRequestDto.getUserId());
        authRoleResultVo.setCreateId(Long.parseLong(employeeRequestDto.getUserVo().getUserId()));

        systemAuthBiz.delByRoleTypeIdAuthUserMapping(authRoleResultVo);

        return ApiResult.success();
    }

    /**
     * @Desc 회원기본정보 파라미터 셋팅
     * @param employeeInfoRequestDto
     * @return UserVo
     */
    private UserVo setUserParamVo(EmployeeInfoRequestDto employeeInfoRequestDto) {
        String userStatus = "";

        if( UserEnums.EmployeeStatus.RESIGN.getCode().equals(employeeInfoRequestDto.getUserStatus()) ) {
            userStatus = UserEnums.UserStatus.INACTIVITY.getCode();
        }else {
            userStatus = UserEnums.UserStatus.ACTIVITY_POSSIBLE.getCode();
        }

        UserVo userVo = new UserVo();
        userVo.setLoginId(employeeInfoRequestDto.getLoginId());
        userVo.setUserName(employeeInfoRequestDto.getUserName());
        userVo.setUserType(UserEnums.UserType.EMPLOYEE.getCode());
        userVo.setUserStatus( userStatus );

        return userVo;
    }

    /**
     * @Desc 로그인 인증정보 파라미터 셋팅
     * @param userVo
     * @param randomPassword
     * @return CertificationVo
     */
    private CertificationVo setCertificationParamVo(UserVo userVo, String randomPassword) {
        CertificationVo certificationVo = new CertificationVo();
        certificationVo.setUserId(userVo.getUserId());
        certificationVo.setLoginId(userVo.getLoginId());
        certificationVo.setPassword(SHA256Util.getUserPassword(randomPassword));
        certificationVo.setTempPasswordYn(BaseEnums.UseYn.Y.name());

        return certificationVo;
    }

    /**
     * @Desc 회원상세정보 파라미터 셋팅
     * @param employeeInfoRequestDto
     * @param userVo
     * @return EmployeeVo
     */
    private EmployeeVo setEmployeeInfoVo(EmployeeInfoRequestDto employeeInfoRequestDto, UserVo userVo) {
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber(employeeInfoRequestDto.getEmployeeNumber());
        employeeVo.setUserId(userVo.getUserId());
        employeeVo.setCompanyId(employeeInfoRequestDto.getCompanyId());
        employeeVo.setUserStatus(employeeInfoRequestDto.getUserStatus());
        employeeVo.setEmail(employeeInfoRequestDto.getEmail());
        employeeVo.setMobile(employeeInfoRequestDto.getMobile());
        employeeVo.setRegalName(employeeInfoRequestDto.getRegalName());
        employeeVo.setPositionName(employeeInfoRequestDto.getPositionName());
        employeeVo.setOrganizationName(employeeInfoRequestDto.getOrganizationName());
        employeeVo.setTeamLeaderYn(employeeInfoRequestDto.getTeamLeaderYn());
        employeeVo.setPersonalInfoAccessYn(employeeInfoRequestDto.getPersonalInfoAccessYn());
        employeeVo.setGrantAuthEmployeeNumber(employeeInfoRequestDto.getGrantAuthEmployeeNumber());
        employeeVo.setGrantAuthDateStart(employeeInfoRequestDto.getGrantAuthDateStart());
        employeeVo.setGrantAuthDateEnd(employeeInfoRequestDto.getGrantAuthDateEnd());
        employeeVo.setGrantAuthStopYn(employeeInfoRequestDto.getGrantAuthStopYn());
        employeeVo.setAuthSupplierId(employeeInfoRequestDto.getAuthSupplierId());
        employeeVo.setCreateId(employeeInfoRequestDto.getUserVo().getUserId());
        employeeVo.setIsAuthListChanged(employeeInfoRequestDto.getIsAuthListChanged());

        return employeeVo;
    }

    /**
     * @Desc 관리자 가입완료 자동메일 발송
     * @param EmployeeVo
     */
    private void emailPush(EmployeeVo employeeInfoVo) throws Exception{
    	ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_SIGN_UP_COMPLETED.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getBosSignUpCompletedEmailTmplt?employeeNumber="+employeeInfoVo.getEmployeeNumber();
        	String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
            		.reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(employeeInfoVo.getUserId()))
                    .mail(employeeInfoVo.getEmail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, employeeInfoVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
	                .content(content)
	                .urUserId(String.valueOf(employeeInfoVo.getUserId()))
	                .mobile(employeeInfoVo.getMobile())
	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
	                .reserveYn(reserveYn)
	                .build();

            sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);
		}
    }

    /**
     * @Desc 본사 회원상세정보 수정 파라미터 셋팅
     * @param employeeInfoRequestDto
     * @param employeeInfo
     * @return EmployeeVo
     */
    private EmployeeVo setHeadquartersEmployeeParamVo(EmployeeInfoRequestDto employeeInfoRequestDto, EmployeeVo employeeInfo) {
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber(employeeInfoRequestDto.getEmployeeNumber());
        employeeVo.setUserStatus(employeeInfoRequestDto.getUserStatus().equalsIgnoreCase(employeeInfo.getUserStatus()) ? null : employeeInfoRequestDto.getUserStatus());
        employeeVo.setMobile(employeeInfoRequestDto.getMobile().equalsIgnoreCase(employeeInfo.getMobile()) ? null : employeeInfoRequestDto.getMobile());
        employeeVo.setTeamLeaderYn(employeeInfoRequestDto.getTeamLeaderYn().equalsIgnoreCase(employeeInfo.getTeamLeaderYn()) ? null : employeeInfoRequestDto.getTeamLeaderYn());
        employeeVo.setPersonalInfoAccessYn(employeeInfoRequestDto.getPersonalInfoAccessYn().equalsIgnoreCase(employeeInfo.getPersonalInfoAccessYn()) ? null : employeeInfoRequestDto.getPersonalInfoAccessYn());
        employeeVo.setGrantAuthEmployeeNumber(StringUtil.nvl(employeeInfoRequestDto.getGrantAuthEmployeeNumber()).equalsIgnoreCase(StringUtil.nvl(employeeInfo.getGrantAuthEmployeeNumber())) ? null : employeeInfoRequestDto.getGrantAuthEmployeeNumber());
        employeeVo.setGrantAuthDateStart(StringUtil.nvl(employeeInfoRequestDto.getGrantAuthDateStart()).equalsIgnoreCase(StringUtil.nvl(employeeInfo.getGrantAuthDateStart())) ? null : employeeInfoRequestDto.getGrantAuthDateStart());
        employeeVo.setGrantAuthDateEnd(StringUtil.nvl(employeeInfoRequestDto.getGrantAuthDateEnd()).equalsIgnoreCase(StringUtil.nvl(employeeInfo.getGrantAuthDateEnd())) ? null : employeeInfoRequestDto.getGrantAuthDateEnd());
        employeeVo.setGrantAuthStopYn(employeeInfoRequestDto.getGrantAuthStopYn().equalsIgnoreCase(employeeInfo.getGrantAuthStopYn()) ? null : employeeInfoRequestDto.getGrantAuthStopYn());
        employeeVo.setAuthSupplierId(employeeInfoRequestDto.getAuthSupplierId());
        employeeVo.setCreateId(employeeInfoRequestDto.getUserVo().getUserId());
        employeeVo.setIsAuthListChanged(employeeInfoRequestDto.getIsAuthListChanged());

        return employeeVo;
    }

    /**
     * @Desc 거래처 회원상세정보 수정 파라미터 셋팅
     * @param employeeInfoRequestDto
     * @param employeeInfo
     * @return EmployeeVo
     */
    private EmployeeVo setClientEmployeeParamVo(EmployeeInfoRequestDto employeeInfoRequestDto, EmployeeVo employeeInfo) {
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber(employeeInfoRequestDto.getEmployeeNumber());
        employeeVo.setCompanyId(employeeInfoRequestDto.getCompanyId().compareTo(employeeInfo.getCompanyId()) == 0 ? null : employeeInfoRequestDto.getCompanyId());
        employeeVo.setUserStatus(employeeInfoRequestDto.getUserStatus().equalsIgnoreCase(employeeInfo.getUserStatus()) ? null : employeeInfoRequestDto.getUserStatus());
        employeeVo.setEmail(employeeInfoRequestDto.getEmail().equalsIgnoreCase(employeeInfo.getEmail()) ? null : employeeInfoRequestDto.getEmail());
        employeeVo.setMobile(employeeInfoRequestDto.getMobile().equalsIgnoreCase(employeeInfo.getMobile()) ? null : employeeInfoRequestDto.getMobile());
        employeeVo.setRegalName(employeeInfoRequestDto.getRegalName().equalsIgnoreCase(employeeInfo.getRegalName()) ? null : employeeInfoRequestDto.getRegalName());
        employeeVo.setPersonalInfoAccessYn(employeeInfoRequestDto.getPersonalInfoAccessYn().equalsIgnoreCase(employeeInfo.getPersonalInfoAccessYn()) ? null : employeeInfoRequestDto.getPersonalInfoAccessYn());
        employeeVo.setCreateId(employeeInfoRequestDto.getUserVo().getUserId());
        employeeVo.setIsAuthListChanged("N");

        return employeeVo;
    }

    /**
     * @Desc 사용자 권한 매핑 리스트 등록
     * @param employeeInfoRequestDto
     * @param userId
     * @param authUserRoleTypeList
     * @throws Exception
     * @return void
     */
    private void addAuthUserMappingList(EmployeeInfoRequestDto employeeInfoRequestDto, Long userId, List<AuthUserRoleTypeVo> authUserRoleTypeList) throws Exception{

        boolean authRoleCreateYn = true;

        for(AuthUserRoleTypeVo roleVo : employeeInfoRequestDto.getRoleList()) {
            for(AuthUserRoleTypeVo authRoleResultVo : authUserRoleTypeList) {

                if( authRoleResultVo.getRoleTypeId().compareTo(roleVo.getRoleTypeId()) == 0 ) {

                    authRoleCreateYn = false;
                }
            }

            if( authRoleCreateYn ) {

                AuthUserRoleTypeVo paramAuthUserRoleTypeVo = new AuthUserRoleTypeVo();
                paramAuthUserRoleTypeVo.setUserId(userId);
                paramAuthUserRoleTypeVo.setRoleTypeId(roleVo.getRoleTypeId());
                paramAuthUserRoleTypeVo.setCreateId(Long.parseLong(employeeInfoRequestDto.getUserVo().getUserId()));
                systemAuthBiz.addByRoleTypeIdAuthUserMapping(paramAuthUserRoleTypeVo);
            }

            authRoleCreateYn = true;
        }
    }

    /**
     * @Desc 사용자 권한 매핑 리스트 삭제
     * @param employeeInfoRequestDto
     * @param authUserRoleTypeList
     * @throws Exception
     * @return void
     */
    private void delAuthUserMappingList(EmployeeInfoRequestDto employeeInfoRequestDto, List<AuthUserRoleTypeVo> authUserRoleTypeList) throws Exception {
        boolean authRoleDeleteYn = true;

        for(AuthUserRoleTypeVo authRoleResultVo : authUserRoleTypeList) {
            for(AuthUserRoleTypeVo roleVo : employeeInfoRequestDto.getRoleList()) {

                if( authRoleResultVo.getRoleTypeId().compareTo(roleVo.getRoleTypeId()) == 0 ) {

                    authRoleDeleteYn = false;
                }
            }

            if( authRoleDeleteYn ) {

                authRoleResultVo.setCreateId(Long.parseLong(employeeInfoRequestDto.getUserVo().getUserId()));
                systemAuthBiz.delByRoleTypeIdAuthUserMapping(authRoleResultVo);
            }

            authRoleDeleteYn = true;
        }
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 등록 로직
     * 	1. 관리자 회원 기존 공급처/출고처 권한 삭제
     * 	2. 관리자 회원 공급처 권한 등록
     * 	3. 관리자 회원 출고처 권한 등록
     * @param employeeInfoRequestDto
     * @throws Exception
     * @return void
     */
    private void addEmployeeAuthList(EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception {

        if (!CompanyEnums.CompanyType.HEADQUARTERS.getCode().equals(employeeInfoRequestDto.getAdminType())) // 본사회원인지 체크
        	return;

        List<String> listExcludeSupplierAuthId = new ArrayList<String>();
        if (employeeInfoRequestDto.getAuthSupplierIdList() != null) {
            for (EmployeeAuthVo authSupplier : employeeInfoRequestDto.getAuthSupplierIdList()) {
            	listExcludeSupplierAuthId.add(authSupplier.getAuthId());
            }
        }
        this.delEmployeeAuth(employeeInfoRequestDto.getEmployeeNumber(), UserEnums.EmployeeAuthIdType.SUPPLIER.getCode(), listExcludeSupplierAuthId); // 기존 공급업체 권한 정보 삭제

        if (employeeInfoRequestDto.getAuthSupplierIdList() != null) {
            for (EmployeeAuthVo authSupplier : employeeInfoRequestDto.getAuthSupplierIdList()) {
            	EmployeeAuthVo employeeAuthVo = new EmployeeAuthVo();
            	employeeAuthVo.setUrEmployeeCd(employeeInfoRequestDto.getEmployeeNumber());
            	employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.SUPPLIER.getCode());
            	employeeAuthVo.setAuthId(authSupplier.getAuthId());
            	employeeAuthVo.setCreateId(SessionUtil.getBosUserVO().getUserId());
            	this.addEmployeeAuth(employeeAuthVo); // 공급업체 권한 등록 : 중복일경우 쿼리의 merge에 의해 업데이트 하지 않는다.
            }
        }

        List<String> listExcludeWarehouseAuthId = new ArrayList<String>();
        if (employeeInfoRequestDto.getAuthWarehouseIdList() != null) {
            for (EmployeeAuthVo authWarehouse : employeeInfoRequestDto.getAuthSupplierIdList()) {
            	listExcludeSupplierAuthId.add(authWarehouse.getAuthId());
            }
        }
    	this.delEmployeeAuth(employeeInfoRequestDto.getEmployeeNumber(), UserEnums.EmployeeAuthIdType.WAREHOUSE.getCode(), listExcludeWarehouseAuthId); // 기존 출고처 권한 정보 삭제

    	if (employeeInfoRequestDto.getAuthWarehouseIdList() != null) {
            for (EmployeeAuthVo authWarehouse : employeeInfoRequestDto.getAuthWarehouseIdList()) {
            	EmployeeAuthVo employeeAuthVo = new EmployeeAuthVo();
            	employeeAuthVo.setUrEmployeeCd(employeeInfoRequestDto.getEmployeeNumber());
            	employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.WAREHOUSE.getCode());
            	employeeAuthVo.setAuthId(authWarehouse.getAuthId());
            	employeeAuthVo.setCreateId(SessionUtil.getBosUserVO().getUserId());
            	this.addEmployeeAuth(employeeAuthVo); // 출고처 권한 등록 : 중복일경우 merge에 의해 업데이트 하지 않는다.
            }
        }
    }

    /**
     * @Desc 관리자회원 수정
     * @param employeeInfoVo
     * @return int
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public int putEmployee(EmployeeVo employeeInfoVo) throws Exception{
        return userEmployeeService.putEmployee(employeeInfoVo);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 등록
     * @param EmployeeAuthVo
     * @return ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public int addEmployeeAuth(EmployeeAuthVo employeeAuthVo) throws Exception {
        return userEmployeeService.addEmployeeAuth(employeeAuthVo);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 삭제
     * @param urEmployeeCd
     * @return ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public int delEmployeeAuth(String urEmployeeCd, String authIdTp, List<String> listExcludeAuthId) throws Exception {
        return userEmployeeService.delEmployeeAuth(urEmployeeCd, authIdTp, listExcludeAuthId);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 조회 List
     * @param EmployeeAuthVo
     * @throws Exception
     * @return List<EmployeeAuthVo>
     */
    @Override
    public List<EmployeeAuthVo> getEmployeeAuthList(EmployeeAuthVo employeeAuthVo) {
    	return userEmployeeService.getEmployeeAuthList(employeeAuthVo);
    }

}
