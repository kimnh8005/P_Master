package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 *  1.2    2021. 01. 11.            김명진         엑셀양식 추가
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 리스트 검색조건 Request Dto")
public class OrderListRequestDto extends BaseRequestPageDto {
    @ApiModelProperty(value = "정렬방식 order / claim")
    private String orderByType;

	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String selectConditionType;

	@ApiModelProperty(value = "주문상세구분")
	private String orderDetailType;

    @ApiModelProperty(value = "판매처 그룹")
    private String sellersGroup;

    @ApiModelProperty(value = "판매처")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "주문상태")
    private String orderState;

    @ApiModelProperty(value = "주문상태제외")
    private String notOrderStateYn;

    @ApiModelProperty(value = "주문상태 리스트")
    private List<String> orderStateList;

    @ApiModelProperty(value = "클레임상태")
    private String claimState;

    @ApiModelProperty(value = "클레임상태제외")
    private String notClaimStateYn;

    @ApiModelProperty(value = "클레임상태 리스트")
    private List<String> claimStateList;

    @ApiModelProperty(value = "결제수단")
    private String paymentMethodCode;

    @ApiModelProperty(value = "결제수단 리스트")
    private List<String> paymentMethodCodeList;

    @ApiModelProperty(value = "주문자유형")
    private String buyerTypeCode;

    @ApiModelProperty(value = "주문자유형 리스트")
    private List<String> buyerTypeCodeList;

    @ApiModelProperty(value = "유형")
    private String agentTypeCode;

    @ApiModelProperty(value = "유형 리스트")
    private List<String> agentTypeCodeList;

    @ApiModelProperty(value = "복수조건 검색조건")
	private String searchMultiType;

	@ApiModelProperty(value = "복수조건 검색어")
	private String findKeyword;

    @ApiModelProperty(value = "단일조건 검색조건")
	private String searchSingleType;

	@ApiModelProperty(value = "단일조건 검색어")
	private String codeSearch;

    @ApiModelProperty(value = "단일조건 검색어 리스트")
    private ArrayList<String> codeSearchList;

	@ApiModelProperty(value = "반품 단일조건 주문상태")
	private String claimStateSingle;

    @ApiModelProperty(value = "반품 단일조건 리스트")
    private List<String> claimStateSingleList;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처ID")
    private String warehouseId;

    @ApiModelProperty(value = "엑셀 양식")
    private String psExcelTemplateId;

    @ApiModelProperty(value = "엑셀 파일명")
    private String psExcelNm;

    @ApiModelProperty(value = "배송방법")
    private String searchDelivType;

    @ApiModelProperty(value = "배송방법 리스트")
    private List<String> delivTypeList;

    @ApiModelProperty(value = "주문유형")
    private String orderTypeCode;

    @ApiModelProperty(value = "주문유형 리스트")
    private List<String> orderTypeCodeList;

    @ApiModelProperty(value = "CS환불 승인상태")
    private String csRefundApproveCd;

    @ApiModelProperty(value = "CS환불 승인상태 리스트")
    private List<String> csRefundApproveCdList;

    @ApiModelProperty(value = "CS환불 구분")
    private String csRefundTp;

    @ApiModelProperty(value = "CS환불 구분 리스트")
    private List<String> csRefundTpList;

    @ApiModelProperty(value = "클레임 정렬여부")
    private String claimOrderBy;

    @ApiModelProperty(value = "미출상태")
    private String missStockStatus;

    @ApiModelProperty(value = "미출사유")
    private String missStockReason;

    @ApiModelProperty(value = "반품사유(MALL 클레임 사유 PK)")
    private String returnMallReason;

    @ApiModelProperty(value = "반품회수여부")
    private String recallYN;

    @ApiModelProperty(value = "CS 조회 유저 Id")
    private String urUserId;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

	@ApiModelProperty(value = "접근권한 매장 ID 리스트")
    private List<String> listAuthStoreId;

	@ApiModelProperty(value = "접근권한 판매처 ID 리스트")
    private List<String> listAuthSellersId;

	@ApiModelProperty(value = "수량 체크 유무")
    private String orderCntYn;

    @ApiModelProperty(value = "클레임 사용여부(거부,철회시 'N')")
    private String claimYn;

    @ApiModelProperty(value = "송장없음 체크 유무")
    private String trackingNoYn;

    @ApiModelProperty(value = "정산일없음 체크 유무")
    private String settleInfoYn;

    @ApiModelProperty(value = "추가 기간 검색 여부")
    private String checkBoxMutilDateYn;

    @ApiModelProperty(value = "기간검색유형")
    private String dateMutilSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateMutilSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateMutilSearchEnd;

    @ApiModelProperty(value = "검색기타")
    private String searchEtc;

    @ApiModelProperty(value = "미처리 내역만 조회 여부")
    private String unprocessMissYn;

    @ApiModelProperty(value = "반품 미출 여부")
    private String returnMissYn;

    @ApiModelProperty(value = "엑셀여부")
    private String excelYn;

    @ApiModelProperty(value = "BOS클레임사유 대분류")
    private String lclaimCtgryId;

    @ApiModelProperty(value = "BOS클레임사유 중분류")
    private String mclaimCtgryId;

    @ApiModelProperty(value = "BOS클레임사유 소분류")
    private String sclaimCtgryId;

    @ApiModelProperty(value = "매장상태")
    private String shopStockStatus;

    @ApiModelProperty(value = "매장 거래처의 경우 매장 ID")
    private String urStoreId;

    @ApiModelProperty(value = "회차")
    private String scheduleNo;

    @ApiModelProperty(value = "배송타입")
    private String deliveryType;

    @ApiModelProperty(value = "복수조건 검색조건 (고객 검색)")
	private String searchMultiTypeForBuyer;

    @ApiModelProperty(value = "복수조건 검색조건 (고객명 검색) - SPMO-247")
    private String searchMultiTypeForCustomer;

    @ApiModelProperty(value = "복수조건 검색조건 (연락처 검색) - SPMO-247")
    private String searchMultiTypeForContact;

    @ApiModelProperty(value = "복수조건 검색조건 (주문 검색)")
	private String searchMultiTypeForOrder;

    @ApiModelProperty(value = "복수조건 검색조건 (상품 검색)")
	private String searchMultiTypeForGoods;

    @ApiModelProperty(value = "복수조건 검색어 (고객 검색)")
	private String findKeywordForBuyer;

    @ApiModelProperty(value = "복수조건 검색어 (고객명 검색) - SPMO-247")
    private String findKeywordForCustomer;

    @ApiModelProperty(value = "복수조건 검색어 (연락처 검색) - SPMO-247")
    private String findKeywordForContact;

    @ApiModelProperty(value = "복수조건 검색어 (주문 검색)")
	private String findKeywordForOrder;

    @ApiModelProperty(value = "복수조건 검색어 (상품 검색)")
	private String findKeywordForGoods;

    @ApiModelProperty(value = "반품접수 연동 성공여부")
    private String recallType;

    @ApiModelProperty(value = "복수조건 검색어 (주소 검색) - SPMO-247")
    private String findKeywordForAddr;

    @ApiModelProperty(value = "녹즙, 내 맘대로 주문")
    private String selectGreenjuice;
}

