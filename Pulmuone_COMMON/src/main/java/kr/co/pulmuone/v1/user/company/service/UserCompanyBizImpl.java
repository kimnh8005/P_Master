package kr.co.pulmuone.v1.user.company.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.user.company.dto.BusinessInformationRequestDto;
import kr.co.pulmuone.v1.user.company.dto.BusinessInformationResponseDto;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.HeadQuartersVo;

/**
* <PRE>
* Forbiz Korea
* 회사 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 26.               손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class UserCompanyBizImpl implements UserCompanyBiz {

    @Autowired
    private UserCompanyService userCompanyService;

    /**
     * @Desc 본사 회사정보 조회
     * @throws Exception
     * @return CommmonHeadQuartersCompanyVo
     */
    public CommmonHeadQuartersCompanyVo getHeadquartersCompany() {
        return userCompanyService.getHeadquartersCompany();
    }

    /**
     * @Desc 사업자정보관리 조회
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getBizInfo(){
        BusinessInformationResponseDto businessInformationResponseDto = new BusinessInformationResponseDto();

        BusinessInformationVo businessInformationVo = userCompanyService.getBizInfo();

        businessInformationResponseDto.setBizInfo(businessInformationVo);

        return ApiResult.success(businessInformationResponseDto);
    }

    /**
     * @Desc 사업자정보 관리 등록
     * @param businessInformationRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addBizInfo(BusinessInformationRequestDto businessInformationRequestDto) throws Exception{

        // 회사정보 등록
        CompanyVo companyVo = this.setAddCompanyVo(businessInformationRequestDto);
        userCompanyService.addCompany(companyVo);

        // 본사정보 등록
        HeadQuartersVo headQuartersVo = this.setAddHeadQuartersVo(businessInformationRequestDto, companyVo);
        userCompanyService.addHeadQuarters(headQuartersVo);

        return ApiResult.success();
    }

    /**
     * @Desc 사업자정보 관리 수정
     * @param businessInformationRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putBizInfo(BusinessInformationRequestDto businessInformationRequestDto) throws Exception{

        // 사업자정보관리 조회
        BusinessInformationVo businessInformationVo = userCompanyService.getBizInfo();

        // 회사정보 수정
        CompanyVo companyVo = this.setPutCompanyVo( businessInformationRequestDto, businessInformationVo );
        userCompanyService.putCompany(companyVo);

        // 본사정보 수정
        HeadQuartersVo headQuartersVo = this.setPutHeadQuartersVo( businessInformationRequestDto, businessInformationVo );
        userCompanyService.putHeadQuarters(headQuartersVo);

        return ApiResult.success();
    }

    /**
     * @Desc 회사정보 등록 파라미터 셋팅
     * @param businessInformationRequestDto
     * @return CompanyVo
     */
    private CompanyVo setAddCompanyVo(BusinessInformationRequestDto businessInformationRequestDto) {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyType(CompanyEnums.CompanyType.HEADQUARTERS.getCode());
        companyVo.setCompanyName(businessInformationRequestDto.getBusinessName());
        companyVo.setCompanyCeoName(businessInformationRequestDto.getCompanyCeoName());
        companyVo.setBusinessNumber(businessInformationRequestDto.getBusinessNumber());
        companyVo.setCompanyMail(businessInformationRequestDto.getRepresentativeEmailInformation());
        companyVo.setZipCode(businessInformationRequestDto.getZipCode());
        companyVo.setAddress(businessInformationRequestDto.getAddress());
        companyVo.setDetailAddress(businessInformationRequestDto.getDetailAddress());
        companyVo.setCreateId(businessInformationRequestDto.getUserVo().getUserId());
        return companyVo;

    }

    /**
     * @Desc 본사정보 등록 파라미터 셋팅
     * @param businessInformationRequestDto
     * @param companyVo
     * @return HeadQuartersVo
     */
    private HeadQuartersVo setAddHeadQuartersVo(BusinessInformationRequestDto businessInformationRequestDto, CompanyVo companyVo) {
        HeadQuartersVo headQuartersVo = new HeadQuartersVo();
        headQuartersVo.setCompanyId(companyVo.getCompanyId());
        headQuartersVo.setCorporationNumber(businessInformationRequestDto.getCorporationNumber());
        headQuartersVo.setMailOrderNumber(businessInformationRequestDto.getMailOrderNumber());
        headQuartersVo.setHostingProvider(businessInformationRequestDto.getHostingProvider());
        headQuartersVo.setHealthFunctFoodReport(businessInformationRequestDto.getHealthFunctFoodReport());
        headQuartersVo.setServiceCenterPhoneNumber(businessInformationRequestDto.getServiceCenterPhoneNumber());
        headQuartersVo.setSecurityDirector(businessInformationRequestDto.getSecurityDirector());
        headQuartersVo.setServiceCenterOperatorOpenTime(businessInformationRequestDto.getServiceCenterOperatorOpenTime());
        headQuartersVo.setServiceCenterOperatorCloseTime(businessInformationRequestDto.getServiceCenterOperatorCloseTime());
        headQuartersVo.setServiceCenterLunchTimeStart(businessInformationRequestDto.getServiceCenterLunchTimeStart());
        headQuartersVo.setServiceCenterLunchTimeEnd(businessInformationRequestDto.getServiceCenterLunchTimeEnd());
        headQuartersVo.setLunchTimeYn(businessInformationRequestDto.getLunchTimeYn());
        headQuartersVo.setEscrowOriginFileName(businessInformationRequestDto.getAddFileList().get(0).getOriginalFileName());
        headQuartersVo.setEscrowFileName(businessInformationRequestDto.getAddFileList().get(0).getPhysicalFileName());
        headQuartersVo.setEscrowFilePath(businessInformationRequestDto.getAddFileList().get(0).getServerSubPath());
        headQuartersVo.setEscrowDescription(businessInformationRequestDto.getEscrowDescription());
        headQuartersVo.setEscrowSubscriptionUrl(businessInformationRequestDto.getEscrowSubscriptionUrl());
        headQuartersVo.setCreateId(businessInformationRequestDto.getUserVo().getUserId());
        return headQuartersVo;

    }

    /**
     * @Desc 회사정보 수정 파라미터 셋팅
     * @param businessInformationRequestDto
     * @param businessInformationVo
     * @return CompanyVo
     */
    private CompanyVo setPutCompanyVo( BusinessInformationRequestDto businessInformationRequestDto, BusinessInformationVo businessInformationVo ) {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyId(businessInformationRequestDto.getCompanyId());
        companyVo.setCompanyName(businessInformationVo.getBusinessName().equals(businessInformationRequestDto.getBusinessName()) ? null : businessInformationRequestDto.getBusinessName());
        companyVo.setCompanyCeoName(businessInformationVo.getCompanyCeoName().equalsIgnoreCase(businessInformationRequestDto.getCompanyCeoName()) ? null : businessInformationRequestDto.getCompanyCeoName());
        companyVo.setBusinessNumber(businessInformationVo.getBusinessNumber().equalsIgnoreCase(businessInformationRequestDto.getBusinessNumber()) ? null : businessInformationRequestDto.getBusinessNumber());
        companyVo.setCompanyMail(businessInformationVo.getRepresentativeEmailInformation().equals(businessInformationRequestDto.getRepresentativeEmailInformation()) ? null : businessInformationRequestDto.getRepresentativeEmailInformation());
        companyVo.setZipCode(businessInformationVo.getZipCode().equalsIgnoreCase(businessInformationRequestDto.getZipCode()) ? null : businessInformationRequestDto.getZipCode());
        companyVo.setAddress(businessInformationVo.getAddress().equalsIgnoreCase(businessInformationRequestDto.getAddress()) ? null : businessInformationRequestDto.getAddress());
        companyVo.setDetailAddress(businessInformationVo.getDetailAddress().equalsIgnoreCase(businessInformationRequestDto.getDetailAddress()) ? null : businessInformationRequestDto.getDetailAddress());
        companyVo.setCreateId(businessInformationRequestDto.getUserVo().getUserId());
        return companyVo;
    }

    /**
     * @Desc 본사정보 수정 파라미터 셋팅
     * @param businessInformationRequestDto
     * @param businessInformationVo
     * @return HeadQuartersVo
     */
    private HeadQuartersVo setPutHeadQuartersVo(BusinessInformationRequestDto businessInformationRequestDto, BusinessInformationVo businessInformationVo ) {
        HeadQuartersVo headQuartersVo = new HeadQuartersVo();
        headQuartersVo.setHeadquartersId(businessInformationRequestDto.getHeadquartersId());
        headQuartersVo.setCorporationNumber(businessInformationVo.getCorporationNumber().equalsIgnoreCase(businessInformationRequestDto.getCorporationNumber()) ? null : businessInformationRequestDto.getCorporationNumber());
        headQuartersVo.setMailOrderNumber(businessInformationVo.getMailOrderNumber().equalsIgnoreCase(businessInformationRequestDto.getMailOrderNumber()) ? null : businessInformationRequestDto.getMailOrderNumber());
        headQuartersVo.setHostingProvider(businessInformationVo.getHostingProvider().equalsIgnoreCase(businessInformationRequestDto.getHostingProvider()) ? null : businessInformationRequestDto.getHostingProvider());
        headQuartersVo.setHealthFunctFoodReport(businessInformationVo.getHealthFunctFoodReport().equalsIgnoreCase(businessInformationRequestDto.getHealthFunctFoodReport()) ? null : businessInformationRequestDto.getHealthFunctFoodReport());
        headQuartersVo.setServiceCenterPhoneNumber(businessInformationVo.getServiceCenterPhoneNumber().equalsIgnoreCase(businessInformationRequestDto.getServiceCenterPhoneNumber()) ? null : businessInformationRequestDto.getServiceCenterPhoneNumber());
        headQuartersVo.setSecurityDirector(businessInformationRequestDto.getSecurityDirector());
        headQuartersVo.setServiceCenterOperatorOpenTime(businessInformationVo.getServiceCenterOperatorOpenTime().equalsIgnoreCase(businessInformationRequestDto.getServiceCenterOperatorOpenTime()) ? null : businessInformationRequestDto.getServiceCenterOperatorOpenTime());
        headQuartersVo.setServiceCenterOperatorCloseTime(businessInformationVo.getServiceCenterOperatorCloseTime().equalsIgnoreCase(businessInformationRequestDto.getServiceCenterOperatorCloseTime()) ? null : businessInformationRequestDto.getServiceCenterOperatorCloseTime());
        headQuartersVo.setServiceCenterLunchTimeStart(businessInformationVo.getServiceCenterLunchTimeStart().equalsIgnoreCase(businessInformationRequestDto.getServiceCenterLunchTimeStart()) ? null : businessInformationRequestDto.getServiceCenterLunchTimeStart());
        headQuartersVo.setServiceCenterLunchTimeEnd(businessInformationVo.getServiceCenterLunchTimeEnd().equalsIgnoreCase(businessInformationRequestDto.getServiceCenterLunchTimeEnd()) ? null : businessInformationRequestDto.getServiceCenterLunchTimeEnd());
        headQuartersVo.setLunchTimeYn(businessInformationVo.getLunchTimeYn().equalsIgnoreCase(businessInformationRequestDto.getLunchTimeYn()) ? null : businessInformationRequestDto.getLunchTimeYn());
        headQuartersVo.setEscrowOriginFileName( CollectionUtils.isEmpty( businessInformationRequestDto.getAddFileList() ) ? null : businessInformationRequestDto.getAddFileList().get(0).getOriginalFileName());
        headQuartersVo.setEscrowFileName( CollectionUtils.isEmpty( businessInformationRequestDto.getAddFileList() ) ? null : businessInformationRequestDto.getAddFileList().get(0).getPhysicalFileName());
        headQuartersVo.setEscrowFilePath( CollectionUtils.isEmpty( businessInformationRequestDto.getAddFileList() ) ? null : businessInformationRequestDto.getAddFileList().get(0).getServerSubPath());
        headQuartersVo.setEscrowDescription(businessInformationVo.getEscrowDescription().equals(businessInformationRequestDto.getEscrowDescription()) ? null : businessInformationRequestDto.getEscrowDescription());
        headQuartersVo.setEscrowSubscriptionUrl(businessInformationVo.getEscrowSubscriptionUrl().equals(businessInformationRequestDto.getEscrowSubscriptionUrl()) ? null : businessInformationRequestDto.getEscrowSubscriptionUrl());
        headQuartersVo.setCreateId(businessInformationRequestDto.getUserVo().getUserId());
        return headQuartersVo;
    }
}
