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
@ApiModel(description = "외부몰 배송정보 송장등록이력 검색조건 Request Dto")
public class OutMallTrackingNumberHistRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기간검색 구분")
    private String searchTrackNumHistPeriodType;

    @ApiModelProperty(value = "기간검색 시작일")
    private String startDate;

    @ApiModelProperty(value = "기간검색 시작시")
    private String startHour;

    @ApiModelProperty(value = "기간검색 종료일")
    private String endDate;

    @ApiModelProperty(value = "기간검색 종료시")
    private String endHour;

    @ApiModelProperty(value = "출고처 그룹")
    private String trackNumHistWarehouseGroup;

    @ApiModelProperty(value = "출고처 PK")
    private Long trackNumHistWarehouseId;
}

