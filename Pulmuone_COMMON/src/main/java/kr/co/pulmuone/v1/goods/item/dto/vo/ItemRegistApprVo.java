package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "품목등록승인 Request")
public class ItemRegistApprVo extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목승인 ID", required = true)
	private String ilItemApprId;

	@ApiModelProperty(value = "품목ID", required = true)
	private String ilItemCd;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.ITEM_REGIST : 품목등록, APPR_KIND_TP.ITEM_CLIENT : 거래처 품목수정)", required = true)
	private String apprKindTp;

	@ApiModelProperty(value = "이전 품목 승인상태(APPR_STAT)", required = true)
	private String prevApprStat;

	@ApiModelProperty(value = "품목 승인상태(APPR_STAT)", required = true)
	private String apprStat;

	@ApiModelProperty(value = "품목 승인상태명", required = true)
	private String apprStatName;

	@ApiModelProperty(value = "품목 승인 요청 가능 여부", required = true)
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

	private String nowSaleStatus;
	/* 거래처 변경 품목 마스터 내역 시작 */
	@ApiModelProperty(value = "출고처 ID", required = true)
	private String itemTp;

	@ApiModelProperty(value = "마스터 품목명", required = true)
	private String itemNm;

	@ApiModelProperty(value = "품목바코드", required = true)
	private String itemBarcode;

	@ApiModelProperty(value = "ERP 연동여부(Y:연동, N:미연동)", required = true)
	private boolean erpIfYn;

	@ApiModelProperty(value = "ERP 재고연동여부(Y:연동, N:미연동)(삭제예정 : 20210125 원종한)", required = true)
	private boolean erpStockIfYn;

	@ApiModelProperty(value = "표준 카테고리 PK", required = true)
	private String ilCtgryStdId;

	@ApiModelProperty(value = "공급업체 PK", required = true)
	private String urSupplierId;

	@ApiModelProperty(value = "표준 브랜드 PK", required = true)
	private String urBrandId;

	@ApiModelProperty(value = "전시 브랜드 아이디(FK)", required = true)
	private String dpBrandId;

	@ApiModelProperty(value = "상품군", required = true)
	private String itemGrp;

	@ApiModelProperty(value = "보관방법", required = true)
	private String storageMethodTp;

	@ApiModelProperty(value = "원산지(해외/국내/기타)", required = true)
	private String originTp;

	@ApiModelProperty(value = "원산지 상세(해외:국가코드, 기타:입력)", required = true)
	private String originDetl;

	@ApiModelProperty(value = "원산지 상세(해외:국가코드, 기타:입력)", required = true)
	private int distributionPeriod;

	@ApiModelProperty(value = "박스 가로", required = true)
	private Double boxWidth; // 박스 가로

	@ApiModelProperty(value = "박스 세로", required = true)
    private Double boxDepth; // 박스 세로

	@ApiModelProperty(value = "박스 높이", required = true)
    private Double boxHeight; // 박스 높이

	@ApiModelProperty(value = "박스 입수량", required = true)
    private Integer piecesPerBox; // 박스 입수량

	@ApiModelProperty(value = "UOM/OMS", required = true)
    private String oms;

	@ApiModelProperty(value = "포장단위별 용량", required = true)
    private Double sizePerPackage; // 포장단위별 용량

	@ApiModelProperty(value = "용량단위", required = true)
    private String sizeUnit; // 용량단위

	@ApiModelProperty(value = "용량단위 (기타)", required = true)
    private String sizeUnitEtc; // 용량단위 (기타)

	@ApiModelProperty(value = "포장 구성 수량", required = true)
    private Integer qtyPerPackage; // 포장 구성 수량

	@ApiModelProperty(value = "포장 구성 단위", required = true)
    private String packageUnit; // 포장 구성 단위

	@ApiModelProperty(value = "포장 구성 단위 (기타)", required = true)
    private String packageUnitEtc; // 포장 구성 단위 (기타)

	@ApiModelProperty(value = "PDM 그룹코드", required = true)
    private String pdmGroupCd; // PDM 그룹코드

	@ApiModelProperty(value = "과세구분(Y: 과세, N: 면세)", required = true)
    private boolean taxYn;

	@ApiModelProperty(value = "상품정보제공고시분류 PK", required = true)
    private Integer ilSpecMasterId;

	@ApiModelProperty(value = "영양정보 표시여부(Y:표시)", required = true)
    private boolean nutritionDispYn; // 영양정보 표시여부 ( DB 저장시 => Y:표시 )

	@ApiModelProperty(value = "영양정보 기본정보(NUTRITION_DISP_Y 값이 N일때 노출되는 항목)", required = true)
    private String nutritionDispDefault; // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )

	@ApiModelProperty(value = "영양분석 단위 (1회 분량)", required = true)
    private String nutritionQtyPerOnce; // 영양분석 단위 (1회 분량)

	@ApiModelProperty(value = "영양분석 단위 (총분량)", required = true)
    private String nutritionQtyTotal; // 영양분석 단위 (총분량)

	@ApiModelProperty(value = "영양성분 기타", required = true)
    private String nutritionEtc;

	@ApiModelProperty(value = "발주유형 PK(사용안함. IL_ITEM_WAREHOUSE로 이관 - 20210218 원종한)", required = true)
    private String ilPoTpId;

	@ApiModelProperty(value = "선주문 가능 여부(Y: 선주문 가능)", required = true)
    private String preOrderYn;

	@ApiModelProperty(value = "배송불가지역 공통코드(UNDELIVERABLE_AREA_TP)", required = true)
    private String undeliverableAreaTp;

	@ApiModelProperty(value = "단종여부 ( DB 저장시 => Y:단종 )", required = true)
    private boolean extinctionYn; // 단종여부 ( DB 저장시 => Y:단종 )

	@ApiModelProperty(value = "반품가능기간", required = true)
    private String returnPeriod;

	@ApiModelProperty(value = "동영상 URL", required = true)
    private String videoUrl;

	@ApiModelProperty(value = "비디오 자동재생 유무(Y:자동재생)", required = true)
    private boolean videoAutoplayYn;

	@ApiModelProperty(value = "상품상세 기본정보", required = true)
    private String basicDesc;

	@ApiModelProperty(value = "상품상세 주요정보", required = true)
    private String detlDesc;

	@ApiModelProperty(value = "기타정보", required = true)
    private String etcDesc;

	@ApiModelProperty(value = "ERP 품목명", required = true)
    private String erpItemNm;

	@ApiModelProperty(value = "ERP 품목바코드", required = true)
    private String erpItemBarcode;

	@ApiModelProperty(value = "ERP 박스바코드", required = true)
    private String erpBoxBarcode;

	@ApiModelProperty(value = "ERP 대카테고리", required = true)
    private String erpCtgryLv1Id;

	@ApiModelProperty(value = "ERP 중카테고리", required = true)
    private String erpCtgryLv2Id;

	@ApiModelProperty(value = "ERP 소카테고리", required = true)
    private String erpCtgryLv3Id;

	@ApiModelProperty(value = "ERP 세부카테고리", required = true)
    private String erpCtgryLv4Id;

	@ApiModelProperty(value = "ERP 브랜드명", required = true)
    private String erpBrandNm;

	@ApiModelProperty(value = "ERP 상품군", required = true)
    private String erpItemGrp;

	@ApiModelProperty(value = "ERP 보관정보", required = true)
    private String erpStorageMethod;

	@ApiModelProperty(value = "ERP 원산지", required = true)
    private String erpOriginNm;

	@ApiModelProperty(value = "ERP 유통기간", required = true)
    private String erpDistributionPeriod;

	@ApiModelProperty(value = "ERP 박스 가로", required = true)
    private String erpBoxWidth; // ERP 박스 가로

	@ApiModelProperty(value = "ERP 박스 세로", required = true)
    private String erpBoxDepth; // ERP 박스 세로

	@ApiModelProperty(value = "ERP 박스 높이", required = true)
    private String erpBoxHeight; // ERP 박스 높이

	@ApiModelProperty(value = "ERP 품목 가로 ", required = true)
    private Double erpItemWidth; // ERP 품목 가로

	@ApiModelProperty(value = "ERP 품목 세로 ", required = true)
    private Double erpItemDepth; // ERP 품목 세로

	@ApiModelProperty(value = "ERP 품목 높이 ", required = true)
    private Double erpItemHeight; // ERP 품목 높이

	@ApiModelProperty(value = "ERP 박스입수량 ", required = true)
    private Integer erpPcsPerBox; // ERP 박스입수량

	@ApiModelProperty(value = "ERP 과세구분", required = true)
    private Boolean erpTaxYn; // ERP 과세구분

	@ApiModelProperty(value = "ERP 발주정보", required = true)
    private String erpPoType;

	@ApiModelProperty(value = "ERP 발주가능여부", required = true)
    private String erpCanPoYn;

	@ApiModelProperty(value = "ERP 영양분석단위(1회분량)", required = true)
    private String erpNutritionQtyPerOnce;

	@ApiModelProperty(value = "ERP 영양분석단위(총분량)", required = true)
    private String erpNutritionQtyTotal;

	@ApiModelProperty(value = "ERP 영양성분 기타", required = true)
    private String erpNutritionEtc;

	@ApiModelProperty(value = "ERP O2O 매장상품 전시구분(매장품목유형)", required = true)
    private String erpO2oExposureTp;

	@ApiModelProperty(value = "ERP 제품/상품 구분(상품판매유형)", required = true)
    private String erpProductTp;

	@ApiModelProperty(value = "ERP 법인코드", required = true)
    private String erpRegalCd;

	@ApiModelProperty(value = "ERP 공급업체", required = true)
	private String erpSupplierName;

	@ApiModelProperty(value = "올가 오프라인 발주상태", required = true)
    private String poProArea;

	@ApiModelProperty(value = "배치에 의한 가격, 할인 변경 시간", required = true)
    private String batchPriceChangeDt;

	@ApiModelProperty(value = "상품가격변경처리여부", required = true)
    private String priceChangeProcYn;

	@ApiModelProperty(value = "상품가격변경처리시간", required = true)
    private String priceChangeProcDt;

	@ApiModelProperty(value = "렌탈료(월)", required = true)
    private Integer rentalFeePerMonth;	// 렌탈료(월)

	@ApiModelProperty(value = "의무사용기간(월)", required = true)
    private Integer rentalDueMonth;		// 의무사용기간(월)

	@ApiModelProperty(value = "계약금", required = true)
    private Integer rentalDeposit;		// 계약금

	@ApiModelProperty(value = "설치비", required = true)
    private Integer rentalInstallFee;	// 설치비

	@ApiModelProperty(value = "등록비", required = true)
    private Integer rentalRegistFee;   // 등록비

	/* 거래처 변경 품목 마스터 내역 끝*/

}
