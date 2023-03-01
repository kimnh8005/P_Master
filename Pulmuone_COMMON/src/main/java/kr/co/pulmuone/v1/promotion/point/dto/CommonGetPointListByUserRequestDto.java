package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 정보 목록 조회 RequestDto")
public class CommonGetPointListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "회원 ID", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "조회 시작일", required = true)
    private String startDate;

    @ApiModelProperty(value = "조회 종료일", required = true)
    private String endDate;

    @ApiModelProperty(value = "지급유형")
    private String paymentType;

    @ApiModelProperty(value = "디바이스 별 문자길이")
    private int deviceCount;

}
