package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "이지어드민 runOrderInfo API Request DTO")
public class EZAdminRunOrderInfoRequestDto {

    @ApiModelProperty(value = "API action")
    private ApiEnums.EZAdminGetOrderInfoOrderCs orderCsEnum;

    @ApiModelProperty(value = "이지어드민 Info Id")
    private long paramIfEasyadminInfoId;

    @ApiModelProperty(value = "Thread Max Count")
    private int paramThreadMaxCount;

    @ApiModelProperty(value = "시작일시")
    private LocalDateTime startDateTime;

    @ApiModelProperty(value = "종료일시")
    private LocalDateTime endDateTime;

    @ApiModelProperty(value = "shop id list")
    private String shopIdList;
}