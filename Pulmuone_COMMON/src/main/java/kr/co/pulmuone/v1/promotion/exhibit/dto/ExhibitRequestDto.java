package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ExhibitRequestDto {

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK", hidden = true)
    private Long urGroupId;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "회원 임직원 사번 PK", hidden = true)
 	private String urErpEmployeeCode;
}
