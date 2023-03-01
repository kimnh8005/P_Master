package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공통 배송지관리 목록 조회 RequestDto")
public class ShippingAddressListFromMyPageRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "회원 ID", hidden = true)
    private Long urUserId;

}
