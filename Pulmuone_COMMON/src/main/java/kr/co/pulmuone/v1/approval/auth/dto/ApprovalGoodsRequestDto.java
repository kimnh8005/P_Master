package kr.co.pulmuone.v1.approval.auth.dto;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ApprovalGoodsRegistRequestDto")
public class ApprovalGoodsRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색조건")
    private String searchCondition;

    @ApiModelProperty(value = "단일_복수조건검색")
    private String selectConditionType;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;

    @ApiModelProperty(value = "검색어 리스트")
    private List<String> findKeywordList;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품유형")
    private String goodsType;

    @ApiModelProperty(value = "상품유형 리스트")
    private List<String> goodsTypeList;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처 그룹 기준 출고처")
    private String warehouseId;

    @ApiModelProperty(value = "승인권한.승인종류 유형 공통코드(APPR_KIND_TP)")
	private String apprKindType;

	@ApiModelProperty(value = "승인상태")
	private String searchApprovalStatus;

	@ApiModelProperty(value = "승인상태Array", required = false)
    private List<String> approvalStatusArray;

	@ApiModelProperty(value = "승인요청자 아이디/회원 타입")
	private String searchApprReqUserType;

	@ApiModelProperty(value = "승인요청자 아이디/회원 조회값")
	private String searchApprReqUser;

	@ApiModelProperty(value = "승인처리자 아이디/회원 타입")
	private String searchApprChgUserType;

	@ApiModelProperty(value = "승인처리자 아이디/회원 조회값")
	private String searchApprChgUser;

	@ApiModelProperty(value = "승인요청일자 검색 시작일")
	private String approvalReqStartDate;

	@ApiModelProperty(value = "승인요청일자 검색 종료일")
	private String approvalReqEndDate;

	@ApiModelProperty(value = "승인처리일자 검색 시작일")
	private String approvalChgStartDate;

	@ApiModelProperty(value = "승인처리일자 검색 종료일")
	private String approvalChgEndDate;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "상품.상품승인  PK 목록")
	private List<String> ilGoodsApprIdList;

	@ApiModelProperty(value = "승인처리 메세지")
	private String statusComment;

    @ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

}
