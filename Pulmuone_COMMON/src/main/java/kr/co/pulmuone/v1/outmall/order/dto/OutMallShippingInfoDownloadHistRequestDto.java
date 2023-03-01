package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 배송정보 내역 다운로드 내역 팝업 검색조건 Request Dto")
public class OutMallShippingInfoDownloadHistRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "판매처 그룹 코드")
    private String popupSellersGroupCd;

    @ApiModelProperty(value = "기간검색 시작일")
    private String popupSearchStartDate;

    @ApiModelProperty(value = "기간검색 종료일")
    private String popupSearchEndDate;

    @ApiModelProperty(value = "출고처 PK")
    private Long popupWarehouseId;

    @ApiModelProperty(value = "수집몰 구분")
    private String popupOutmallType;


    @ApiModelProperty(value = "판매처 (다운로드내역)")
    private String popupOmSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

}

