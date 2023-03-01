package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(description = "통합몰 매출 대사 조회 Request Dto")
public class CalSalesListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "ERP 기간 시작일")
    private String erpDateSearchStart;

    @ApiModelProperty(value = "ERP 기간 종료일")
    private String erpDateSearchEnd;

    @ApiModelProperty(value = "기간 조건")
    private String dateSearchType;

    @ApiModelProperty(value = "기간 시작일")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간 종료일")
    private String dateSearchEnd;

    @ApiModelProperty(value = "구분")
    private String salesGubun;

    @ApiModelProperty(value = "구분")
    private List<String> salesGubunList;

    @ApiModelProperty(value = "매칭여부")
    private String findMatchingType;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처")
    private String warehouseId;

    @ApiModelProperty(value = "BOS 주문상태")
    private String orderState;

    @ApiModelProperty(value = "BOS 주문상태")
    private List<String> orderStateList;

    @ApiModelProperty(value = "BOS 클레임상태")
    private String claimState;

    @ApiModelProperty(value = "BOS 클레임상태")
    private List<String> claimStateList;

    @ApiModelProperty(value = "검색어 타입")
    private String searchMultiType;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;

    @ApiModelProperty(value = "엑셀구분")
    private String excelYn;

}
