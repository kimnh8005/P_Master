package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 조회 Request")
public class GetUserGroupListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "그룹등급")
	private String groupLevel;

    @ApiModelProperty(value = "쿠폰사용 가능여부")
    private String couponYn;

    @ApiModelProperty(value = "마일리지 사용/적립 가능여부")
    private String mileageYn;

    @ApiModelProperty(value = "기본 회원등급(최초회원) 여부")
    private String defalutYn;

}
