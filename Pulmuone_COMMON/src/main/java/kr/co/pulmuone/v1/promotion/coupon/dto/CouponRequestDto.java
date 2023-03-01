package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.BuyerInfoListResultVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.UploadInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponRequestDto")
public class CouponRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "아이디/회원 조회값")
	private String condiValue;

	@ApiModelProperty(value = "아이디/회원 타입")
	private String condiType;

	@ApiModelProperty(value = "사용유무")
	private String couponUseYn;

	@ApiModelProperty(value = "사유")
	private String searchStatusComment;

	@ApiModelProperty(value = "검색일자 타입")
	private String searchDateType;

	@ApiModelProperty(value = "검색 시작일")
	private String startCreateDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endCreateDate;

	@ApiModelProperty(value = "전시쿠폰명")
	private String searchDisplayCouponName;

	@ApiModelProperty(value = "관리자쿠폰명")
	private String searchBosCouponName;

	@ApiModelProperty(value = "기간조회 타입")
	private String searchDateSelect;

	@ApiModelProperty(value = "검색어 조회 타입")
	private String searchSelect;

	@ApiModelProperty(value = "검색키워드")
	private String findKeyword;

	@ApiModelProperty(value = "지급방법")
	private String searchIssueType;

	@ApiModelProperty(value = "팝업 지급방법")
	private String issueType;

	@ApiModelProperty(value = "쿠폰종류")
	private String searchCouponType;

	@ApiModelProperty(value = "발급상태")
	private String searchApprovalStatus;

	@ApiModelProperty(value = "이용권 수금 상태")
	private String searchIssueTicketType;

	@ApiModelProperty(value = "발급상태")
	private String approvalStatus;

	@ApiModelProperty(value = "쿠폰 Issue 상태")
	private String couponIssueStatus;

	@ApiModelProperty(value = "쿠폰 Issue 상태")
	private String couponStatus;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

	@ApiModelProperty(value = "수정 데이터")
    String updateData;

	@ApiModelProperty(value = "발급대상 Dto 리스트")
    List<BuyerInfoListResultVo> updateRequestDtoList;

	@ApiModelProperty(value = "쿠폰 ID" ,required = false)
	private String pmCouponId;

	@ApiModelProperty(value = "쿠폰/발급사용 이력 ID" ,required = false)
	private String pmCouponIssueId;

	@ApiModelProperty(value = "쿠폰 반려 사유" ,required = false)
	private String statusComment;

	@ApiModelProperty(value = "쿠폰발급 사유 중복체크 유무" ,required = false)
	private String duplicateChecked;


    @ApiModelProperty(value = "쿠폰저장 Data", required = false)
    String insertData;

    @ApiModelProperty(value = "쿠폰저장 Data List", hidden = true)
    List<CoverageVo> insertRequestDtoList = new ArrayList<CoverageVo>();

    @ApiModelProperty(value = "분담조직코드")
	private String erpOrganizationCode;

    @ApiModelProperty(value = "법인코드")
	private String erpRegalCode;

    @ApiModelProperty(value = "조직명")
	private String erpOrganizationName;

    @ApiModelProperty(value = "법인명")
	private String erpRegalName;

    @ApiModelProperty(value = "첫번째 조직 분담율")
	private String percentageFirst;

    @ApiModelProperty(value = "두번째 분담조직코드")
	private String secondErpOrganizationCode;

    @ApiModelProperty(value = "두번째  조직 분담율")
	private String percentageSecond;

    @ApiModelProperty(value = "쿠폰타입")
	private String couponType;


    @ApiModelProperty(value = "발급방법 상품")
	private String paymentType;

    @ApiModelProperty(value = "발급방법 장바구니")
	private String paymentTypeCart;

    @ApiModelProperty(value = "판매가지정 배송비")
	private String paymentTypeSaleprice;

    @ApiModelProperty(value = "발급방법 배송비")
	private String paymentTypeShippingPrice;


    @ApiModelProperty(value = "자동발급 타입")
	private String autoIssueType;

    @ApiModelProperty(value = "발급 상세 타입")
	private String issueDetailType;

    @ApiModelProperty(value = "시작 발급기간")
	private String issueStartDate;

    @ApiModelProperty(value = "종료 발급기간")
	private String issueEndDate;

    @ApiModelProperty(value = "시작 유효기간")
	private String validityStartDate;

    @ApiModelProperty(value = "종료 유효기간")
	private String validityEndDate;

    @ApiModelProperty(value = "유효일")
	private String validityDay;

    @ApiModelProperty(value = "발급 수량 제한")
	private String issueQtyLimit;

    @ApiModelProperty(value = "발급 예산")
	private String issueBudget;

    @ApiModelProperty(value = "발급 수량")
	private int issueQty;

    @ApiModelProperty(value = "사용 PC")
	private String usePcYn;

    @ApiModelProperty(value = "사용 모바일")
	private String useMobileWebYn;

    @ApiModelProperty(value = "사용 App")
	private String useMobileAppYn;

    @ApiModelProperty(value = "할인방식")
	private String discountType;

    @ApiModelProperty(value = "할인값 퍼센트")
	private String discountValuePercent;

    @ApiModelProperty(value = "최대할인금액")
	private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "정액 금액")
	private String discountValueFixed;

    @ApiModelProperty(value = "최소결제금액")
	private String minPaymentAmount;

    @ApiModelProperty(value = "발급목적")
	private String issuePurposeType;

    @ApiModelProperty(value = "고정난수번호")
	private String fixSerialNumber;

    @ApiModelProperty(value = "자동생성 난수번호")
	private String serialNumber;

    @ApiModelProperty(value = "제휴구분 유무")
	private String pgPromotionYn;

    @ApiModelProperty(value = "제휴구분 PG")
	private String pgPromotionPayConfigId;

    @ApiModelProperty(value = "제휴구분 결제수단")
	private String pgPromotionPayGroupId;

    @ApiModelProperty(value = "제휴구분 결제수단 상세")
	private String pgPromotionPayId;

    @ApiModelProperty(value = "제휴구분 결제수단 상세")
	private String discountVal;

    @ApiModelProperty(value = "난수번호 타입")
	private String serialNumberType;

    @ApiModelProperty(value = "쿠폰명")
	private String bosCouponName;

    @ApiModelProperty(value = "전시쿠폰명")
	private String displayCouponName;

    @ApiModelProperty(value = "유효기간 타입")
	private String validityType;

    @ApiModelProperty(value = "발급타입")
	private String coverageType;

    @ApiModelProperty(value = "쿠폰상태")
	private String status;

    @ApiModelProperty(value = "적용범위 상품")
	private String goodsCoverageType;

    @ApiModelProperty(value = "적용범위 장바구니")
	private String cartCoverageType;

    @ApiModelProperty(value = "적용범위 배송비")
	private String shippingCoverageType;

    @ApiModelProperty(value = "적용범위 판매가지정")
	private String appintCoverageType;

    @ApiModelProperty(value = "사유")
	private String statusCmnt;

    @ApiModelProperty(value = "쿠폰발급일")
	private String createDate;

    @ApiModelProperty(value = "발급사유")
	private String issueReason;

    @ApiModelProperty(value = "쿠폰사용일")
	private String useDate;

    @ApiModelProperty(value = "계정발급 회원")
	private String uploadUser;


    @ApiModelProperty(value = "계정발급 회원 Data List", hidden = true)
    List<UploadInfoVo> uploadUserList = new ArrayList<UploadInfoVo>();


    @ApiModelProperty(value = "이용권난수 엑셀 정보")
	private String uploadTicket;

    @ApiModelProperty(value = "계정발급 회원 Data List", hidden = true)
    List<UploadInfoVo> uploadTicketList = new ArrayList<UploadInfoVo>();

    @ApiModelProperty(value = "쿠폰복사 구분")
	private String couponCopyYn;

    @ApiModelProperty(value = "적립금 고유값")
	private String pmPointId;

    @ApiModelProperty(value = "개별난수번호 사용타입")
	private String serialNumberUseType;

    @ApiModelProperty(value = "개별난수번호 사용타입")
	private String approvalCheck;

    @ApiModelProperty(value = "배송비 최대할인금액")
	private String discountValueCart;

    @ApiModelProperty(value = "장바구니쿠폰 적용 구분")
	private String cartCouponApplyYn;

    @ApiModelProperty(value = "이용권 상태")
	private String serialNumberStatus;

    @ApiModelProperty(value = "판매가지정 할인금액")
	private String discountValueSalePrice;

    @ApiModelProperty(value = "조회 데이터")
	private String inputSearchValue;

	@ApiModelProperty(value = "조회 데이터 리스트")
	private List<String> searchValueList;

	@ApiModelProperty(value = "이전 쿠폰 마스터 상태")
	private String prevCouponMasterStat;

	@ApiModelProperty(value = "이전 쿠폰 승인 상태")
	private String prevApprStat;

	@ApiModelProperty(value = "변경 쿠폰 마스터 상태")
	private String couponMasterStat;

	@ApiModelProperty(value = "변경 쿠폰 승인 상태")
	private String apprStat;

	@ApiModelProperty(value = "사용유무")
	private String useYn;

	@ApiModelProperty(value = "1차승인담당자 ID")
	private String apprSubUserId;

	@ApiModelProperty(value = "2차승인담당자 ID")
	private String apprUserId;

	@ApiModelProperty(value = "승인 처리 타입")
	private String apprKindType;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "장바구니쿠폰 재발급시 원 쿠폰PK")
	private Long originPmCouponId;

	@ApiModelProperty(value = "공통코드")
	private String stCommonCodeMasterCode;

	@ApiModelProperty(value = "등록 Id")
	private String createId;

}
