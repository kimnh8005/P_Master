package kr.co.pulmuone.v1.shopping.recently.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CommonGetRecentlyViewListByUserRequestDto")
public class CommonGetRecentlyViewListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "디바이스 정보", hidden = true)
    private String deviceInfo;

    @ApiModelProperty(value = "앱 여부", hidden = true)
    private boolean isApp;

    @ApiModelProperty(value = "회원 여부", hidden = true)
    private boolean isMember;

    @ApiModelProperty(value = "임직원 여부", hidden = true)
    private boolean isEmployee;

}
