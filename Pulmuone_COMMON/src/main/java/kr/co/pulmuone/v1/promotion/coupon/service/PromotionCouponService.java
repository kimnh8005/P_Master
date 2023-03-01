package kr.co.pulmuone.v1.promotion.coupon.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.promotion.coupon.PromotionCouponMapper;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApplyCouponDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.promotion.coupon.dto.*;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.*;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponApplicationListRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 천혜현            최초작성
 * 1.1    20200903   	 이원호            쿠폰목록조회 추가
 * 1.2    20200910   	 이원호            유저 쿠폰 등록 Validation, 유저 쿠폰 등록 추가
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionCouponService {
    private final PromotionCouponMapper promotionCouponMapper;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    /**
     * @Desc 쿠폰 리스트 조회
     * @param CouponRequestDto
     * @return Page<CouponListResultVo>
     */
    protected Page<CouponListResultVo> getCpnMgm(CouponRequestDto couponRequestDto) throws Exception{
        PageMethod.startPage(couponRequestDto.getPage(), couponRequestDto.getPageSize());
        return promotionCouponMapper.getCpnMgm(couponRequestDto);
    }

    /**
     * @Desc 쿠폰조회
     * @param CouponRequestDto
     * @return CouponDetailVo
     */
    protected CouponDetailVo getCoupon(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.getCoupon(couponRequestDto.getPmCouponId());
    }


    /**
     * @Desc 조직정보 조회
     * @param CouponRequestDto
     * @return List<OrganizationListResultVo>
     */
    protected OrganizationListResultVo getOrganizationList(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.getOrganizationList(couponRequestDto);
    }

    /**
     * 적용범위 조회
     * @param couponRequestDto
     * @return
     * @throws Exception
     */
    protected List<CoverageVo> getCoverageList(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.getCoverageList(couponRequestDto);
    }

    /**
     * 개별난수 조회
     * @param couponRequestDto
     * @return
     * @throws Exception
     */
    protected List<AccountInfoVo> getSerialNumberList(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.getSerialNumberList(couponRequestDto);
    }


    /**
     * 회원정보 조회
     * @param couponRequestDto
     * @return
     * @throws Exception
     */
    protected List<AccountInfoVo> getUserList(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.getUserList(couponRequestDto);
    }



    /**
     * @Desc 조직 조회
     * @param CouponRequestDto
     * @return Page<EmployeeVo>
     */
    protected Page<OrganizationPopupListResultVo> getOrganizationPopupList(OrganizationPopupListRequestDto organizationPopupListRequestDto) throws Exception{
        PageMethod.startPage(organizationPopupListRequestDto.getPage(), organizationPopupListRequestDto.getPageSize());
        return promotionCouponMapper.getOrganizationPopupList(organizationPopupListRequestDto);
    }

    /**
     * @Desc 쿠폰 정보 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int addCoupon(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.addCoupon(couponRequestDto);
    }



    /**
     * @Desc 쿠폰 적용범위 등록
     * @param List<CoverageVo>
     * @throws Exception
     * @return int
     */
    protected int addCouponCoverage(List<CoverageVo> coverageVoList) throws Exception{
        return promotionCouponMapper.addCouponCoverage(coverageVoList);
    }


    /**
     * @Desc 분담정보 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int addOrganization(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.addOrganization(couponRequestDto);
    }


    /**
     * @Desc 분담정보 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int addSecondOrganization(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.addSecondOrganization(couponRequestDto);
    }



    /**
     * @Desc 쿠폰 상태 이력 조회
     * @param CouponRequestDto
     * @return CouponDetailVo
     */
    protected ApprovalStatusVo getCouponStatusHistory(CouponRequestDto couponRequestDto, String getType) throws Exception{
    	ApprovalStatusVo history = promotionCouponMapper.getCouponStatusHistory(couponRequestDto);

    	if("PREV".equals(getType)) {
        	if(history != null) {
    			// 이전 쿠폰정보 조회
    			history.setPrevMasterStat(history.getMasterStat());
    			history.setPrevApprStat(history.getApprStat());

    			if (!StringUtils.isEmpty(couponRequestDto.getCouponMasterStat())) {
        			history.setMasterStat(couponRequestDto.getCouponMasterStat());
    			}

    			if (!StringUtils.isEmpty(couponRequestDto.getApprStat())) {
        			history.setApprStat(couponRequestDto.getApprStat());
    			}

                if (StringUtils.isEmpty(history.getApprSubUserId())) {
                    history.setApprSubUserId(couponRequestDto.getApprSubUserId());
                }

                if (StringUtils.isEmpty(history.getApprUserId())) {
                    history.setApprUserId(couponRequestDto.getApprUserId());
                }
        	}
        }
        return history;
    }


    /**
     * @Desc 쿠폰상태이력 등록
     * @param ApprovalStatusVo
     * @return int
     */
    protected int addCouponStatusHistory(ApprovalStatusVo history){
    	return promotionCouponMapper.addCouponStatusHistory(history);
    }

    /**
     * @Desc 쿠폰상태이력 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int putCouponIssueList(CouponIssueParamDto couponIssueParamDto) throws Exception{
        return promotionCouponMapper.putCouponIssueList(couponIssueParamDto);
    }

    /**
     * @Desc 쿠폰상태이력 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int putCouponIssueHistoryList(CouponIssueParamDto couponIssueParamDto) throws Exception{
        return promotionCouponMapper.putCouponIssueHistoryList(couponIssueParamDto);
    }

    /**
     * 배송비 쿠폰 재발급 여부 업데이트
     * @param pmCouponIssueId
     * @throws Exception
     * @return int
     */
    protected int putShippingFeeCouponReIssueYn(Long pmCouponIssueId) throws Exception{
        return promotionCouponMapper.putShippingFeeCouponReIssueYn(pmCouponIssueId);
    }

    /**
     * @Desc 쿠폰상태이력 등록
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int addSerialNumber(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.addSerialNumber(couponRequestDto);
    }

    /**
     * @Desc 이용권 중복 SerialNumber 조회
     * @param UploadInfoVo
     * @throws Exception
     * @return int
     */
    protected int getDuplicateSerialNumber(UploadInfoVo uploadInfoVo) throws Exception{
        return promotionCouponMapper.getDuplicateSerialNumber(uploadInfoVo);
    }



    /**
     * @Desc 이용권 단일코드 중복조회
     * @param UploadInfoVo
     * @throws Exception
     * @return int
     */
    protected int getDuplicateFixedNumber(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.getDuplicateFixedNumber(couponRequestDto);
    }

    /**
     * @Desc 쿠폰정보 수정
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int putCoupon(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.putCoupon(couponRequestDto);
    }

    /**
     * @Desc 쿠폰 적용범위 삭제
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int removeCouponCoverage(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.removeCouponCoverage(couponRequestDto);
    }

    /**
     * @Desc 분담정보 삭제
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int removeOrganization(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.removeOrganization(couponRequestDto);
    }

    /**
     * @Desc 쿠폰 발급/사용 삭제
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int removeCouponIssue(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.removeCouponIssue(couponRequestDto);
    }

    /**
     * @Desc 개별난수번호 삭제
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int removeSerialNumber(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.removeSerialNumber(couponRequestDto);
    }

    /**
     * @Desc 쿠폰 삭제
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int removeCoupon(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.removeCoupon(couponRequestDto);
    }

    /**
     * @Desc 쿠폰명 수정
     * @param CouponRequestDto
     * @throws Exception
     * @return int
     */
    protected int updateCouponName(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.updateCouponName(couponRequestDto);
    }


    /**
     * @Desc BOS계정관리 조회
     * @param CouponRequestDto
     * @return CouponDetailVo
     */
    protected CouponDetailVo getCopyCoupon(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.getCopyCoupon(couponRequestDto);
    }


    /**
     * @Desc 쿠폰지급 내역 조회
     * @param CouponRequestDto
     * @return Page<IssueListResultVo>
     */
    protected Page<IssueListResultVo> getCpnMgmList(CouponRequestDto couponRequestDto) throws Exception{
        PageMethod.startPage(couponRequestDto.getPage(), couponRequestDto.getPageSize());
        return promotionCouponMapper.getCpnMgmList(couponRequestDto);
    }


    /**
     * @Desc 쿠폰지급 (선택회원)
     * @param CouponRequestDto
     * @return Page<BuyerInfoListResultVo>
     */
    protected Page<BuyerInfoListResultVo> putCouponIssueList(CouponRequestDto couponRequestDto) throws Exception{
        PageMethod.startPage(couponRequestDto.getPage(), couponRequestDto.getPageSize());
        return promotionCouponMapper.putCouponIssueList(couponRequestDto);
    }

    /**
     * @Desc 쿠폰지정 조회
     * @param BuyerInfoRequestDto
     * @return Page<BuyerInfoListResultVo>
     */
    @UserMaskingRun(system="BOS")
    protected Page<BuyerInfoListResultVo> getCpnMgmIssueList(BuyerInfoRequestDto buyerInfoRequestDto) throws Exception{
        PageMethod.startPage(buyerInfoRequestDto.getPage(), buyerInfoRequestDto.getPageSize());
        return promotionCouponMapper.getCpnMgmIssueList(buyerInfoRequestDto);
    }


    /**
     * @Desc 쿠폰지급  (검색회원)
     * @param IssueUserRequestDto
     * @return List<BuyerInfoListResultVo>
     */
    protected List<BuyerInfoListResultVo> getCpnMgmIssueDuplicateParamList(IssueUserRequestDto issueUserRequestDto) throws Exception{
        return promotionCouponMapper.getCpnMgmIssueDuplicateParamList(issueUserRequestDto);
    }


    /**
     * @Desc 쿠폰_발급/사용 정보 수정(미사용->발급취소)
     * @param CouponIssueParamDto
     * @return
     */
    protected int putCancelDepositList(CouponIssueParamDto couponIssueParamDto) throws Exception{
    	return promotionCouponMapper.putCancelDepositList(couponIssueParamDto);
    }

    /**
     * @Desc 쿠폰목록   쿠폰회수
     * @param CouponRequestDto
     * @return
     */
    protected int putCouponIssueWithdraw(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.putCouponIssueWithdraw(couponRequestDto);
    }

    /**
     * @Desc 쿠폰 상태변경  (쿠폰중지)
     * @param CouponRequestDto
     * @return
     */
    protected int putPmCouponStatus(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.putPmCouponStatus(couponRequestDto);
    }


    /**
     * @Desc 쿠폰정보 조회 (미사용)
     * @param CouponRequestDto
     * @return List<IssueListResultVo>
     */
    protected List<IssueListResultVo> getCouponIssueInfo(CouponRequestDto couponRequestDto) throws Exception{
        return promotionCouponMapper.getCouponIssueInfo(couponRequestDto);
    }


    /**
     * @Desc 쿠폰 발급/사용 상태변경
     * @param CouponRequestDto
     * @return
     */
    protected int putPmCouponIssueStatus(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.putPmCouponIssueStatus(couponRequestDto);
    }

    /**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 *
	 * @param getBuyerListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
//	protected ExcelDownloadDto getCouponIssueListExcelDownload(CouponRequestDto couponRequestDto) throws Exception {
//		String excelFileName = "쿠폰("+couponRequestDto.getPmCouponId() +")발급내역_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
//        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
//
//        /*
//         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
//         *
//         */
//
//        /*
//         * 컬럼별 width 목록 : 단위 pixel
//         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
//         */
//        Integer[] widthListOfFirstWorksheet = { //
//                400, 250, 350, 250, 250, 250, 250, 500 };
//
//        /*
//         * 본문 데이터 컬럼별 정렬 목록
//         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
//         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
//         */
//        String[] alignListOfFirstWorksheet = { //
//                "center", "center", "center", "center","center", "center", "center", "center","left" };
//
//        /*
//         * 본문 데이터 컬럼별 데이터 property 목록
//         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
//         */
//        String[] propertyListOfFirstWorksheet = { //
//                "bosCouponName", "discountValue", "validityPeroid", "issueDate", "remainingPeriod", "couponUseDate", "userInfo", "statusComment" };
//
//        /*
//         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
//         *
//         */
//        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
//                "쿠폰명", "할인", "유효기간", "발급일자", "잔여기간", "사용일자", "회원정보(ID)", "사유"
//        };
//
//        /*
//         * 워크시트 DTO 생성 후 정보 세팅
//         *
//         */
//        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
//                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
//                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
//                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
//                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
//                .build();
//
//		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);
//
//		List<IssueListResultVo> couponIssueList = null;
//		try
//		{
//			couponIssueList = promotionCouponMapper.getCouponIssueListExcel(couponRequestDto);
//			log.info("couponIssueList {}", couponIssueList);
//		}
//		catch (Exception e)
//		{
//			log.error(e.getMessage());
//			throw e; // 추후 CustomException 으로 변환 예정
//		}
//		firstWorkSheetDto.setExcelDataList(couponIssueList);
//
//		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();
//
//		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
//
//		return excelDownloadDto;
//	}



	/**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 *
	 * @param couponRequestDto
	 * @return List<IssueListResultVo>
	 * @throws Exception
	 */
	@UserMaskingRun(system = "BOS")
	protected List<IssueListResultVo> getCouponIssueListExcel(CouponRequestDto couponRequestDto) throws Exception
	{

//		 if (StringUtils.isNotEmpty(couponRequestDto.getInputSearchValue())) {
//			 couponRequestDto.setSearchValueList(Stream.of(couponRequestDto.getInputSearchValue().split("\n|,"))
//	                    .map(String::trim)
//	                    .filter(StringUtils::isNotEmpty)
//	                    .collect(Collectors.toList()));
//	        }

		 List<IssueListResultVo> result = promotionCouponMapper.getCouponIssueListExcel(couponRequestDto);

		return result;
	}















    /**
     * 상품 적용가능한 쿠폰 리스트
     *
     * @param ilGoodsId
     * @return List<GoodsApplyCouponDto>
     * @throws Exception
     */
    protected List<GoodsApplyCouponDto> getGoodsApplyCouponList(Long ilGoodsId, Long urUserId) throws Exception {
        return promotionCouponMapper.getGoodsApplyCouponList(ilGoodsId, urUserId);
    }

    /**
     * 신규회원특가 쿠폰 조회 - 비회원일 경우
     *
     * @param ilGoodsId
     * @return HashMap
     * @throws Exception
     */
    protected int getNewBuyerSpecialsCouponByNonMember(Long ilGoodsId, String deviceInfo, boolean isApp) throws Exception {
        Integer result = promotionCouponMapper.getNewBuyerSpecialsCouponByNonMember(ilGoodsId, deviceInfo, isApp);
        if(result == null){
            return 0;
        }
        return result;
    }

    /**
     * 쿠폰 정보 목록조회 - User기준
     *
     * @param dto CommonGetCouponListByUserRequestDto
     * @return CommonGetCouponListByUserResponseDto
     * @throws Exception exception
     */
    protected CouponListByUserResponseDto getCouponListByUser(CouponListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<CouponListByUserVo> rows = promotionCouponMapper.getCouponListByUser(dto);

        //무료배송여부, 신규가입여부
        List<CouponListByUserVo> list = rows.getResult();
        for (CouponListByUserVo vo : list) {
            //무료배송구분
            if (vo.getCouponType().equals(CouponEnums.CouponType.SHIPPING_PRICE.getCode()) && vo.getPercentageMaxDiscountAmount().equals("0")) {
                vo.setShippingFreeYn("Y");
            }
            //신규가입여부
            if (vo.getIssueType().equals(CouponEnums.IssueType.AUTO_PAYMENT.getCode()) && vo.getIssueDetailType().equals(CouponEnums.IssueDetailType.USER_JOIN.getCode())) {
                vo.setUserJoinYn("Y");
            }
        }

        return CouponListByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(list)
                .build();
    }

    /**
     * 쿠폰 적용대상 조회
     *
     * @param pmCouponId Long
     * @return CommonGetCouponCoverageResponseDto
     * @throws Exception exception
     */
    protected CouponCoverageResponseDto getCouponCoverage(Long pmCouponId) throws Exception {
        return CouponCoverageResponseDto.builder()
                .coverage(couponCoverageVoToDto(promotionCouponMapper.getCouponCoverage(pmCouponId, "Y")))
                .notCoverage(couponCoverageVoToDto(promotionCouponMapper.getCouponCoverage(pmCouponId, "N")))
                .build();
    }

    private List<CouponCoverageDto> couponCoverageVoToDto(List<CouponCoverageVo> coverageVoList) throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        for (CouponCoverageVo vo : coverageVoList) {
            // 출고처 제외처리
            if(CouponEnums.ApplyCoverage.WAREHOUSE.getCode().equals(vo.getCoverageType())){
                continue;
            }
            List<String> name = map.getOrDefault(vo.getCoverageType(), new ArrayList<>());
            name.add(vo.getCoverageName());
            map.put(vo.getCoverageType(), name);
        }

        List<CouponCoverageDto> result = new ArrayList<>();
        for (String key : map.keySet()) {
            result.add(new CouponCoverageDto(key, map.get(key)));
        }

        // 출고처 검색 처리
        List<Long> wareHouseIdList = coverageVoList.stream()
                .filter(vo -> CouponEnums.ApplyCoverage.WAREHOUSE.getCode().equals(vo.getCoverageType()))
                .map(CouponCoverageVo::getCoverageId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(wareHouseIdList != null && wareHouseIdList.size() > 0){
            List<String> goodsNameList = goodsSearchBiz.getGoodsCouponCoverageByUrWareHouseId(wareHouseIdList);
            result.add(new CouponCoverageDto(CouponEnums.ApplyCoverage.GOODS.getCode(), goodsNameList));
        }

        return result;
    }

    /**
     * 유저 쿠폰 등록 Validation
     *
     * @param urUserId   Long
     * @param pmCouponId Long
     * @return String
     * @throws Exception exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class})
    public CouponValidationByUserResponseDto checkAddCouponValidationByUser(Long urUserId, Long pmCouponId) throws Exception {
        CouponValidationByUserResponseDto result = new CouponValidationByUserResponseDto();
        CouponValidationInfoVo vo = promotionCouponMapper.getAddCouponValidationInfo(urUserId, pmCouponId);
        result.setData(vo);

        if (vo == null){
            result.setValidationEnum(CouponEnums.AddCouponValidation.NOT_EXIST_COUPON); //존재하지 않는 쿠폰번호
            return result;
        }

        if (!vo.getCouponMasterStat().equals(CouponEnums.CouponMasterStatus.APPROVED.getCode())) {   //이용권 승인 상태 확인  (21.01.04 승인 상태값 변경 수정)
            result.setValidationEnum(CouponEnums.AddCouponValidation.NOT_ACCEPT_APPROVAL);
            return result;
        }
        if (vo.getIssueQty() != 0 && vo.getIssueQty() <= vo.getIssueCnt()) {            //발급수량 비교
            result.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY);
            return result;
        }

        // 1인 발급 제한 수량 비교
        // 자동발급 -> 회원등급은 제외
        if(!(CouponEnums.PaymentType.AUTO_PAYMENT.getCode().equals(vo.getIssueType()) && CouponEnums.IssueDetailType.USER_GRADE.getCode().equals(vo.getIssueDetailType()))) {
            if (vo.getIssueQtyLimit() != 0 && vo.getIssueQtyLimit() <= vo.getUserIssueCnt()) {
                result.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY_LIMIT);
                return result;
            }
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate issueStartDate = LocalDate.parse(vo.getIssueStartDate(), dateFormatter);
        LocalDate issueEndDate = LocalDate.parse(vo.getIssueEndDate(), dateFormatter);

        if (now.isBefore(issueStartDate)) {                                             // 발급시작일 이전
            result.setValidationEnum(CouponEnums.AddCouponValidation.NOT_ISSUE_DATE);
            return result;
        } else if (now.isAfter(issueEndDate)) {                                         // 발급종료일 지남
            result.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_DATE);
            return result;
        }

        //자동발급 -> 회원등급 발급가능개월수 확인
        if(CouponEnums.PaymentType.AUTO_PAYMENT.getCode().equals(vo.getIssueType()) && CouponEnums.IssueDetailType.USER_GRADE.getCode().equals(vo.getIssueDetailType())) {
            LocalDate limitDate = issueStartDate.plusMonths(vo.getIssueQtyLimit());
            if (now.isAfter(limitDate)){   // 설정한 개월수 지남
                result.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_DATE);
            }
        }

        // 전체 발급 제한수량 - 동시성 처리 확인
        // 예외사항 : 무제한일 경우는 제외
        if(vo.getIssueQty() != 0){
            if (promotionCouponMapper.putCouponIssue(pmCouponId) != 1) {
                result.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY);
                return result;
            }
        }

        result.setValidationEnum(CouponEnums.AddCouponValidation.PASS_VALIDATION);
        return result;
    }

    /**
     * 유저 쿠폰 등록
     *
     * @param dto              CommonGetAddCouponValidationInfoVo
     * @param urUserId         Long
     * @param pmSerialNumberId Long
     * @throws Exception exception
     */
    protected void addCouponByUser(CouponValidationInfoVo dto, Long urUserId, Long pmSerialNumberId) throws Exception {
        CouponIssueRequestDto addDto = new CouponIssueRequestDto();
        addDto.setPmCouponId(dto.getPmCouponId());
        addDto.setPmSerialNumberId(pmSerialNumberId);
        addDto.setUrUserId(urUserId);
        addDto.setCouponIssueType(CouponEnums.CouponIssueType.NORMAL.getCode());
        addDto.setStatus(CouponEnums.CouponStatus.NOTUSE.getCode());
        if (dto.getValidityType().equals(CouponEnums.ValidityType.PERIOD.getCode())) {
            addDto.setValidityStartDate(dto.getValidityStartDate());
            addDto.setExpirationDate(dto.getValidityEndDate());
        } else if (dto.getValidityType().equals(CouponEnums.ValidityType.VALIDITY.getCode())) {
            addDto.setValidityStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            addDto.setExpirationDate(LocalDate.now().plusDays(dto.getValidityDay()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        Integer issueTargetCount = getIssueTargetCount(dto);
        if (issueTargetCount == null) return;

        // 쿠폰 등록
        for (int i = 0; i < issueTargetCount; i++) {
            promotionCouponMapper.addCouponIssue(addDto);
            promotionCouponMapper.addCouponIssueStatusHistory(addDto.getPmCouponIssueId(), CouponEnums.CouponStatus.NOTUSE.getCode(), urUserId);
        }

    }

    /*
    쿠폰발급 수량 계산
     */
    private Integer getIssueTargetCount(CouponValidationInfoVo dto) {
        // 상품상세, 이용권, 자동발급, 다운로드 케이스
        if(CouponEnums.PaymentType.GOODS_DETAIL.getCode().equals(dto.getIssueType()) ||
                CouponEnums.PaymentType.TICKET.getCode().equals(dto.getIssueType()) ||
                CouponEnums.PaymentType.AUTO_PAYMENT.getCode().equals(dto.getIssueType()) ||
                CouponEnums.PaymentType.DOWNLOAD.getCode().equals(dto.getIssueType())) {
            return 1;
        }

        // 발급수량계산
        int issueTargetCount = dto.getIssueQtyLimit();

        // 예외처리 - 개인별 기 발급수량
        if(dto.getUserIssueCnt() >= issueTargetCount) return null;

        // 예외처리 - 회원 전체 기 발급수량
        if(dto.getIssueQty() != 0 && (dto.getIssueCnt() + issueTargetCount) > dto.getIssueQty()){
            issueTargetCount = dto.getIssueQty() - dto.getIssueCnt();
        }
        return issueTargetCount;
    }

    /**
     * 쿠폰 사용 Validation
     *
     * @param urUserId   Long
     * @param pmCouponIssueId Long
     * @return String
     * @throws Exception exception
     */
    protected CouponEnums.UseCouponValidation checkUseCouponValidation(Long urUserId, Long pmCouponIssueId) throws Exception {
        UseCouponValidationInfoVo vo = promotionCouponMapper.getUseCouponValidationInfo(urUserId, pmCouponIssueId);

        // 조회결과 없음
        if (vo == null){
            return CouponEnums.UseCouponValidation.NOT_EXIST_COUPON;
        }

        // 이용권 승인 상태 확인
       	if (!(vo.getCouponStatus().equals(CouponEnums.CouponMasterStatus.APPROVED.getCode()) || vo.getCouponStatus().equals(CouponEnums.CouponMasterStatus.STOP.getCode()))) {    // (21.01.04 승인 상태값 변경 수정)
            return CouponEnums.UseCouponValidation.NOT_COUPON_STATUS;
        }

        // 쿠폰 사용여부 확인
        if (vo.getIssueStatus().equals(CouponEnums.CouponStatus.USE.getCode())) {       // 사용한 쿠폰
            return CouponEnums.UseCouponValidation.USE_ISSUE_STATUS;
        }
        if (vo.getIssueStatus().equals(CouponEnums.CouponStatus.CANCEL.getCode())) {    // 취소된 쿠폰
            return CouponEnums.UseCouponValidation.CANCEL_ISSUE_STATUS;
        }

        // 유효기간 확인
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate expirationDate = LocalDate.parse(vo.getExpirationDate(), dateFormatter);

        if (now.isAfter(expirationDate)) {      // 유효기간 지남
            return CouponEnums.UseCouponValidation.OVER_EXPIRATION;
        }

        return CouponEnums.UseCouponValidation.PASS_VALIDATION;
    }

    /**
     * 쿠폰 사용
     *
     * @param urUserId         Long
     * @param pmCouponIssueId  Long
     * @throws Exception exception
     */
    protected boolean useCoupon(Long urUserId, Long pmCouponIssueId) throws Exception {
        int cnt = promotionCouponMapper.putCouponIssueUse(pmCouponIssueId);
        if(cnt > 0) {
        	promotionCouponMapper.addCouponIssueStatusHistory(pmCouponIssueId, CouponEnums.CouponStatus.USE.getCode(), urUserId);
        	return true;
        } else {
        	return false;
        }
    }

    /**
     * 쿠폰 수량 조회
     *
     * @param urUserId Long
     * @param status   String
     * @return CommonGetCouponCountByUserVo
     * @throws Exception exception
     */
    protected CouponCountByUserVo getCouponCountByUser(Long urUserId, String status) throws Exception {
        return promotionCouponMapper.getCouponCountByUser(urUserId, status);
    }

    /**
     * 회원가입 - 대상 쿠폰 조회
     *
     * @return List<CouponInfoByUserJoinVo>
     * @throws Exception exception
     */
    protected List<CouponInfoByUserJoinVo> getCouponInfoByUserJoin() throws Exception {
        return promotionCouponMapper.getCouponInfoByUserJoin();
    }

    /**
     * 회원가입 - 쿠폰 상품 조회
     *
     * @return List<CouponGoodsByUserJoinVo>
     * @throws Exception exception
     */
    protected List<CouponGoodsByUserJoinVo> getUserJoinGoods() throws Exception {
        return promotionCouponMapper.getUserJoinGoods();
    }

    /**
     * 주문 사용 가능 상품,장바구니 쿠폰 조회
     *
     * @param	dto CouponApplicationListRequestDto
     * @return  List<CouponApplicationListByUserVo>
     * @throws	Exception
     */
    protected List<CouponApplicationListByUserVo> getCouponApplicationListByUser(CouponApplicationListRequestDto dto) throws Exception {
    	return promotionCouponMapper.getCouponApplicationListByUser(dto);
    }


    /**
     * 쿠폰 할인금액 계산
     *
     * @param	salePrice
     * @param	discountMethodType
     * @param	discountRate
     * @return	DiscountCalculationResultDto
     * @throws	Exception
     */
    protected DiscountCalculationResultDto calculationDiscount(int salePrice, String discountMethodType, int discountRate, int maxDiscountValue) throws Exception {
    	DiscountCalculationResultDto discountCalculationResultDto = new DiscountCalculationResultDto();
    	int discountAppliedPrice = 0;
    	int discountPrice = 0;
    	boolean isActive = false;

		if(discountMethodType.equals(GoodsEnums.CouponDiscountStatus.PERCENTAGE_DISCOUNT.getCode())) {
			discountAppliedPrice = PriceUtil.getPriceByRate(salePrice, discountRate);
			discountPrice = salePrice - discountAppliedPrice;
			if(discountPrice > 0) {
				isActive = true;
			}
		} else if(discountMethodType.equals(GoodsEnums.CouponDiscountStatus.FIXED_DISCOUNT.getCode())) {
			discountAppliedPrice = PriceUtil.getPriceByPrice(salePrice, discountRate);
			if (salePrice < discountRate) {
				discountAppliedPrice = salePrice;
				discountPrice = discountRate;
			} else {
				discountPrice = salePrice - discountAppliedPrice;
				isActive = true;
			}
		}

		if(maxDiscountValue > 0 && discountPrice > maxDiscountValue) {
			discountAppliedPrice = discountAppliedPrice + (discountPrice - maxDiscountValue);
			discountPrice = maxDiscountValue;
		}

		discountCalculationResultDto.setActive(isActive);
		discountCalculationResultDto.setDiscountAppliedPrice(discountAppliedPrice);
		discountCalculationResultDto.setDiscountPrice(discountPrice);

    	return discountCalculationResultDto;
    }

    /**
     * 주문 사용 가능 배송비 쿠폰 조회
     *
     * @param	urUserId
     * @param 	urWarehouseId
     * @throws	Exception
     */
    protected List<CouponApplicationListByUserVo> getShippingCouponApplicationListByUser(Long urUserId, Long urWarehouseId) throws Exception {
    	return promotionCouponMapper.getShippingCouponApplicationListByUser(urUserId,urWarehouseId);
    }

    /**
     * 주문 사용 가능 장바구니 쿠폰 조회
     *
     * @param	urUserId
     * @throws	Exception
     */
    protected List<CouponApplicationListByUserVo> getCartCouponApplicationListByUser(Long urUserId) throws Exception {
    	return promotionCouponMapper.getCartCouponApplicationListByUser(urUserId);
    }


    /**
     * 쿠폰발행 PK로 주문 사용 가능 쿠폰 조회
     *
     * @param	pmCouponIssueId
     * @throws	Exception
     */
    protected List<CouponApplicationListByUserVo> getCouponApplicationListByPmCouponIssueId(Long pmCouponIssueId) throws Exception {
    	return promotionCouponMapper.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);
    }

    /**
     * 쿠폰정보조회 by 쿠폰 PK list
     *
     * @param   pmCouponIdList List<Long>
     * @param   urUserId Long
     * @return  List<GoodsApplyCouponDto>
     * @throws  Exception
     */
    protected List<GoodsApplyCouponDto> getCouponByPmCouponIdList(List<Long> pmCouponIdList, Long urUserId) throws Exception {
        return promotionCouponMapper.getCouponByPmCouponIdList(pmCouponIdList, urUserId);
    }

    /**
	 * 쿠폰승인 목록 조회
	 *
	 * @param CouponApprovalRequestDto
	 * @return CouponApprovalResponseDto
	 */
    @UserMaskingRun
    protected CouponApprovalResponseDto getApprovalCouponList(CouponApprovalRequestDto requestDto) {
    	CouponApprovalResponseDto result = new CouponApprovalResponseDto();
    	ArrayList<String> approvalStatusArray = null;

    	if (!StringUtil.isEmpty(requestDto.getSearchApprovalStatus())) {
    		approvalStatusArray = StringUtil.getArrayList(requestDto.getSearchApprovalStatus().replace(" ", ""));
    		requestDto.setApprovalStatusArray(approvalStatusArray);
        }

    	PageMethod.startPage(requestDto.getPage(), requestDto.getPageSize());
    	Page<CouponApprovalResultVo> rows = promotionCouponMapper.getApprovalCouponList(requestDto);
    	result.setTotal((int)rows.getTotal());
    	result.setRows(rows.getResult());
    	return result;
    }

    /**
     * 쿠폰승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalCoupon(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionCouponMapper.putCancelRequestApprovalCoupon(approvalVo) > 0
			&& this.addCouponStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 쿠폰승인 폐기처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalCoupon(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionCouponMapper.putDisposalApprovalCoupon(approvalVo) > 0
			&& this.addCouponStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 쿠폰승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessCoupon(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionCouponMapper.putApprovalProcessCoupon(approvalVo) > 0
			&& this.addCouponStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }
	/**
     * 회원탈퇴 - 쿠폰 취소 처리
     *
     * @param   urUserId Long
     * @throws  Exception
     */
    protected void putWithdrawalMemberCoupon(Long urUserId) throws Exception {
        promotionCouponMapper.putWithdrawalMemberCoupon(urUserId);
    }


    /**
     * 이벤트-쿠폰명 검색
     * @param couponRequestDto
     * @return
     */
	protected ApiResult<?> getEventCallCouponInfo(CouponRequestDto couponRequestDto) {
		CouponListResponseDto result = new CouponListResponseDto();

		List<CouponListResultVo> rows = promotionCouponMapper.getEventCallCouponInfo(couponRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}


	/**
	 * 쿠폰 설정상태 조회
	 * @param couponRequestDto
	 * @return
	 */
	protected ApiResult<?> getCouponSearchStatus(CouponRequestDto couponRequestDto) {
		CouponListResponseDto result = new CouponListResponseDto();

		List<CouponListResultVo> rows = promotionCouponMapper.getCouponSearchStatus(couponRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
     * 승인 관리자정보 조회
     * @param couponRequestDto
     * @return
     * @throws Exception
     */
    protected List<ApprovalAuthManagerVo> getApprUserList(CouponRequestDto couponRequestDto) throws Exception{
    	return promotionCouponMapper.getApprUserList(couponRequestDto);
    }


	/**
	 * 이용권 수금 상태 변경
	 * @param couponRequestDto
	 * @return
	 * @throws Exception
	 */
	protected int putTicketCollectStatus(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponMapper.putTicketCollectStatus(couponRequestDto);
	}

    /**
     * 쿠폰발급PK로 쿠폰 정보 조회
     *
     * @param pmCouponIssueId
     * @return CouponIssueVo
     * @throws Exception
     */
	protected CouponIssueVo getCouponIssueByPmCouponIssueId(Long pmCouponIssueId) throws Exception{
		return promotionCouponMapper.getCouponIssueByPmCouponIssueId(pmCouponIssueId);
	}

    /**
     * 쿠폰PK기준 쿠폰 적용범위 등록
     *
     * @param pmCouponId
     * @param originPmCouponId
     * @return int
     * @throws Exception
     */
	protected int addPmCouponCoverageByPmCouponId(Long pmCouponId, Long originPmCouponId) throws Exception{
		return promotionCouponMapper.addPmCouponCoverageByPmCouponId(pmCouponId,originPmCouponId);
	}

    /**
     * 쿠폰PK기준 분담조직정보 등록
     *
     * @param pmCouponId
     * @param originPmCouponId
     * @return int
     * @throws Exception
     */
	protected int addPmCouponPointShareOrganizaionByPmCouponId(Long pmCouponId, Long originPmCouponId) throws Exception{
		return promotionCouponMapper.addPmCouponPointShareOrganizaionByPmCouponId(pmCouponId,originPmCouponId);
	}


	/**
	 * 분담조직 정보 조회
	 * @param couponRequestDto
	 * @return
	 */
	protected ApiResult<?> getOrgInfo(CouponRequestDto couponRequestDto) {
		CouponListResponseDto result = new CouponListResponseDto();

		List<CouponListResultVo> rows = promotionCouponMapper.getOrgInfo(couponRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
    * @Desc 쿠폰이용권 승인 상태 변경
    * @param CouponApprovalRequestDto
    * @throws Exception
    * @return int
    */
   protected int putSerialNumberStatus(CouponRequestDto couponRequestDto) throws Exception{
       return promotionCouponMapper.putSerialNumberStatus(couponRequestDto);
   }

	/**
	 * 쿠폰 active 체크
	 * @param deviceType
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	protected boolean isDeviceTypeActive(DeviceType deviceType, CouponApplicationListByUserVo coupon) throws Exception {
		boolean isActive = true;
		if ((deviceType.equals(GoodsEnums.DeviceType.APP) && !"Y".equals(coupon.getUseMobileAppYn()))
				|| (deviceType.equals(GoodsEnums.DeviceType.PC) && !"Y".equals(coupon.getUsePcYn()))
				|| (deviceType.equals(GoodsEnums.DeviceType.MOBILE) && !"Y".equals(coupon.getUseMobileWebYn()))) {
			isActive = false;
		}
		return isActive;
	}

	protected String getCouponNameById(Long pmCouponId) throws Exception {
	    return promotionCouponMapper.getCouponNameById(pmCouponId);
    }

    /**
     * 회원인증 프로모션 쿠폰지급관련 AS-IS 에 쿠폰을 지급해줄 지급 대상을 저장.. (PF-3858)..
     * 배치는 AS-IS 에 생성.
     * @param couponJoinCertEventRequestDto
     * @return
     * @throws Exception
     */
    protected int addPmCouponJoinEventListByJoinUrUserId(CouponJoinCertEventRequestDto couponJoinCertEventRequestDto) throws Exception {
        return promotionCouponMapper.addPmCouponJoinEventListByJoinUrUserId(couponJoinCertEventRequestDto);
    }

    protected boolean isPmCouponJoinDup(String customerNumber) throws Exception {
        return (promotionCouponMapper.getPmCouponJoinDupleCnt(customerNumber) > 0);
    }


//    /**
//     * @Desc 쿠폰 발급 유효성 확인
//     * @param CouponRequestDto
//     * @throws Exception
//     * @return int
//     */
//    protected int getIssueCouponChk(CouponRequestDto couponRequestDto) throws Exception{
//        return promotionCouponMapper.getIssueCouponChk(couponRequestDto);
//    }
//
//    /**
//     * @Desc 쿠폰 상태변경
//     * @param CouponRequestDto
//     * @throws Exception
//     * @return int
//     */
//    protected int putPmCouponIssueCancel(CouponRequestDto couponRequestDto) throws Exception{
//        return promotionCouponMapper.putPmCouponIssueCancel(couponRequestDto);
//    }




    /**
     * @Desc 회원정보 조회
     * @param CouponIssueParamDto
     * @throws Exception
     * @return int
     */
    protected int getUserIdCnt(CouponRequestDto couponRequestDto) throws Exception{

    	List<UploadInfoVo> accountUserList = couponRequestDto.getUploadUserList();
		CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
		int userIdCnt = 0;
		if (accountUserList != null && !accountUserList.isEmpty()) {
			for(int i=0 ; i<accountUserList.size() ; i++) {
				if(accountUserList.get(i).getUrUserId() == null) {
					couponIssueParamDto.setLoginId(accountUserList.get(i).getLoginId());
					int checkCnt = promotionCouponMapper.getUserIdCnt(couponIssueParamDto);
					if(checkCnt > 0) {
						userIdCnt++;
					}
				}else {
					userIdCnt++;
				}
			}
		}
        return userIdCnt;
    }




}