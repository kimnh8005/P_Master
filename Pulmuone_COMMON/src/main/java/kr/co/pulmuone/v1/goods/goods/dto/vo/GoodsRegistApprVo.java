package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "상품등록승인 Request")
public class GoodsRegistApprVo extends BaseRequestPageDto {

	@ApiModelProperty(value = "상품승인 ID", required = true)
	private String ilGoodsApprId;

	@ApiModelProperty(value = "상품ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.GOODS_REGIST : 상품등록, APPR_KIND_TP.GOODS_CLIENT : 거래처 상품수정)", required = true)
	private String apprKindTp;

	@ApiModelProperty(value = "이전 상품 승인상태(APPR_STAT)", required = true)
	private String prevApprStat;

	@ApiModelProperty(value = "상품 승인상태(APPR_STAT)", required = true)
	private String apprStat;

	@ApiModelProperty(value = "상품 승인상태명", required = true)
	private String apprStatName;

	@ApiModelProperty(value = "상품 승인 요청 가능 여부", required = true)
	private boolean apprReqUserYn;

	@ApiModelProperty(value = "상태변경_메세지")
	private String statusCmnt;

	@ApiModelProperty(value = "승인요청일", required = true)
	private String apprReqDt;

	@ApiModelProperty(value = "승인요청자", required = true)
	private String apprReqUserId;

	@ApiModelProperty(value = "승인 1차 담당자", required = true)
	private String apprSubUserId;

	@ApiModelProperty(value = "승인 1차 처리자", required = true)
	private String apprSubChgUserId;

	@ApiModelProperty(value = "승인 1차 처리일", required = true)
	private String apprSubChgDt;

	@ApiModelProperty(value = "승인 2차 담당자", required = true)
	private String apprUserId;

	@ApiModelProperty(value = "승인 2차 처리자", required = true)
	private String apprChgUserId;

	@ApiModelProperty(value = "승인 2차 처리일", required = true)
	private String apprChgDt;

	@ApiModelProperty(value = "등록자", required = true)
	private String createId;

	@ApiModelProperty(value = "수정자", required = true)
	private String modifyId;

	@ApiModelProperty(value = "승인자", required = true)
	private String apprId;

	@ApiModelProperty(value = "승인자명", required = true)
	@UserMaskingUserName
	private String userName;

	@ApiModelProperty(value = "승인자 LOGID ID", required = true)
	private String loginId;

	@ApiModelProperty(value = "승인 날짜", required = true)
	private String apprDate;

	/* 거래처 변경 상품 마스터 내역 시작 */
	@ApiModelProperty(value = "품목코드", required = true)
	private String ilItemCode;

	@ApiModelProperty(value = "출고처 ID", required = true)
	private String urWarehouseId;

	@ApiModelProperty(value = "상품 유형 ID", required = true)
	private String goodsType;

	@ApiModelProperty(value = "판매유형", required = false)
	private String saleType;

	@ApiModelProperty(value = "외부몰 판매 상태", required = false)
	private String goodsOutmallSaleStatus;

	@ApiModelProperty(value = "상품명", required = true)
	private String goodsName;

	@ApiModelProperty(value = "표장용량 구성정보 노출여부", required = false)
	private String packageUnitDisplayYn;

	@ApiModelProperty(value = "표장용량 구성정보 노출 내용", required = false)
	private String packageUnitDesc;

	@ApiModelProperty(value = "프로모션 상품명", required = false)
	private String promotionName;

	@ApiModelProperty(value = "프로모션 상품명 시작일", required = false)
	private String promotionNameStartDate;

	@ApiModelProperty(value = "프로모션 상품명 종료일", required = false)
	private String promotionNameEndDate;

	@ApiModelProperty(value = "상품설명", required = false)
	private String goodsDesc;

	@ApiModelProperty(value = "키워드 입력", required = false)
	private String searchKeyword;

	@ApiModelProperty(value = "회원 구매여부", required = false)
	private String purchaseMemberYn;

	@ApiModelProperty(value = "임직원 구매여부", required = false)
	private String purchaseEmployeeYn;

	@ApiModelProperty(value = "비회원 구매여부", required = false)
	private String purchaseNonmemberYn;

	@ApiModelProperty(value = "WEB PC 전시여부", required = false)
	private String displayWebPcYn;

	@ApiModelProperty(value = "WEB MOBILE 전시여부", required = false)
	private String displayWebMobilePcYn;

	@ApiModelProperty(value = "APP 전시여부", required = false)
	private String displayAppYn;

	@ApiModelProperty(value = "판매 기간 시작일", required = false)
	private String saleStartDate;

	@ApiModelProperty(value = "판매 기간 종료일", required = false)
	private String saleEndDate;

	@ApiModelProperty(value = "전시 상태", required = true)
	private String displayYn;

	@ApiModelProperty(value = "현재 상품 판매 상태", required = false)
	private String nowSaleStatus;

	@ApiModelProperty(value = "판매 상태", required = false)
	private String saleStatus;

	@ApiModelProperty(value = "이전 판매 상태", required = false)
	private String savedSaleStatus;

	@ApiModelProperty(value = "단위별 용량정보", required = false)
	private String autoDisplaySizeYn;

	@ApiModelProperty(value = "단위별 용량정보 > 자동표기안함 시에 입력값", required = false)
	private String sizeEtc;

	@ApiModelProperty(value = "매장판매", required = false)
	private String saleShopYn;

	@ApiModelProperty(value = "혜택 설정 > 쿠폰사용허용", required = false)
	private String couponUseYn;

	@ApiModelProperty(value = "최소구매 수량기준", required = false)
	private String limitMinimumCnt;

	@ApiModelProperty(value = "최대 구매 기준", required = false)
	private String limitMaximumType;

	@ApiModelProperty(value = "최대 구매 기간", required = false)
	private String limitMaximumDuration;

	@ApiModelProperty(value = "최대 구매 수량", required = false)
	private String limitMaximumCnt;

	@ApiModelProperty(value = "MD추천 노출여부", required = false)
	private String mdRecommendYn;

	@ApiModelProperty(value = "상품 메모", required = false)
	private String goodsMemo;

	@ApiModelProperty(value = "상세 하단공지1 이미지 URL", required = false)
	private String noticeBelow1ImageUrl;

	@ApiModelProperty(value = "상세 하단공지1 시작일", required = false)
	private String noticeBelow1StartDate;

	@ApiModelProperty(value = "상세 하단공지1 종료일", required = false)
	private String noticeBelow1EndDate;

	@ApiModelProperty(value = "상세 하단공지2 이미지 URL", required = false)
	private String noticeBelow2ImageUrl;

	@ApiModelProperty(value = "상세 하단공지2 시작일", required = false)
	private String noticeBelow2StartDate;

	@ApiModelProperty(value = "상세 하단공지2 종료일", required = false)
	private String noticeBelow2EndDate;

	@ApiModelProperty(value = "(묶음상품)상품이미지형식", required = false)
	private String goodsPackageImageType;

	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보 직접등록 여부", required = false)
	private String goodsPackageBasicDescYn;

	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보", required = false)
	private String goodsPackageBasicDesc;

	@ApiModelProperty(value = "(묶음상품)동영상 URL", required = false)
	private String goodsPackageVideoUrl;

	@ApiModelProperty(value = "(묶음상품)비디오 자동재생 유무", required = false)
	private String goodsPackageVideoAutoplayYn;

	@ApiModelProperty(value = "일일상품 유형", required = false)
	private String goodsDailyType;

	@ApiModelProperty(value = "알러지 식단 포함여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배달 설정 허용 여부")
	private String goodsDailyBulkYn;
	/* 거래처 변경 상품 마스터 내역 끝*/
}
