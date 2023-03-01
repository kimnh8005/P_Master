package kr.co.pulmuone.v1.goods.item.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "마스터 품목 리스트 검색 조건 request dto")
public class MasterItemListRequestDto extends BaseRequestPageDto {

    /*
     * 마스터 품목 리스트 검색 조건 request dto
     */
    @ApiModelProperty(value = "품목코드검색조건종류", required = false)
    private String ilItemCodeKind;

    @ApiModelProperty(value = "품목코드", required = false)
    private String ilItemCode;

    @ApiModelProperty(value = "코드검색_문자형판별")
    private String ilItemCodeStrFlag;

    @ApiModelProperty(value = "품목코드Array", required = false)
    private ArrayList<String> ilItemCodeArray;

    @ApiModelProperty(value = "마스터품목명", required = false)
    private String itemName;

    @ApiModelProperty(value = "ERP연동여부", required = false)
    private String erpLinkIfYn;

    @ApiModelProperty(value = "표준 카테고리 ID", required = false)
    private String ilCategoryStandardId;

    @ApiModelProperty(value = "날짜종류", required = false)
    private String dateType;

    @ApiModelProperty(value = "검색날짜시작일", required = false)
    private String startDate;

    @ApiModelProperty(value = "검색날짜종료일", required = false)
    private String endDate;

    @ApiModelProperty(value = "공급업체", required = false)
    private String urSupplierId;

    @ApiModelProperty(value = "출고처", required = false)
    private String warehouseId;

    @ApiModelProperty(value = "브랜드", required = false) // 작업보류
    private String urBrandId;

    @ApiModelProperty(value = "전시브랜드", required = false)
    private String dpBrandId;

    @ApiModelProperty(value = "선주문가능여부", required = false)
    private String preOrderAvailable;

    @ApiModelProperty(value = "마스터품목유형", required = false)
    private String masterItemType;

    @ApiModelProperty(value = "마스터품목유형Array", required = false)
    private ArrayList<String> masterItemTypeArray;

    @ApiModelProperty(value = "보관방법", required = false)
    private String storageMethod;

    @ApiModelProperty(value = "엑셀양식관리.SEQ")
	private String psExcelTemplateId;

    @ApiModelProperty(value = "보관방법Array", required = false)
    private ArrayList<String> storageMethodArray;

    private String ilPoTpId;

    @ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

	@ApiModelProperty(value = "품목 상태 유형 공통코드(ITEM_STATUS_TP.SAVE : 저장, ITEM_STATUS_TP.REGISTER : 등록, ITEM_STATUS_TP.DISPOSAL : 폐기)")
	private String itemStatusTp;

	@ApiModelProperty(value = "품목가격 승인대상 여부")
	private String approvalDiv;

}
