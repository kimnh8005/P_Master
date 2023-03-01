package kr.co.pulmuone.v1.outmall.order.dto;

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
@ApiModel(description = "외부몰 배송정보 내역 다운로드 검색조건 Request Dto")
public class OutMallShippingInfoDownloadRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "판매처 그룹 코드")
    private String searchSellersGroupCd;

    @ApiModelProperty(value = "판매처")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

    @ApiModelProperty(value = "기간검색 구분")
    private String searchPeriodType;

    @ApiModelProperty(value = "기간검색 시작일")
    private String searchStartDate;

    @ApiModelProperty(value = "기간검색 시작시")
    private String searchStHour;

    @ApiModelProperty(value = "기간검색 종료일")
    private String searchEndDate;

    @ApiModelProperty(value = "기간검색 종료시")
    private String searchEdHour;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처 PK")
    private Long warehouseId;

    @ApiModelProperty(value = "수집몰 구분")
    private String outmallType;

    @ApiModelProperty(value = "이지어드민 구분(API:API, EXCEL:엑셀업로드)")
    private String ezadminType;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;
}

