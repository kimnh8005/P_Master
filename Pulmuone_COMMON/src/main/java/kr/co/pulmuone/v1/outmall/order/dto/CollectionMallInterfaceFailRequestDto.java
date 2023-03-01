package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "수집몰 연동내역 FAIL 검색조건 Request Dto")
public class CollectionMallInterfaceFailRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "수집몰 연동 PK")
    private long ifEasyadminInfoId;

    @ApiModelProperty(value = "시작일시")
    private String batchStartDateTime;

    @ApiModelProperty(value = "실패구분(U:업로드 B:배치 T:송장)")
    private String failType;

}

