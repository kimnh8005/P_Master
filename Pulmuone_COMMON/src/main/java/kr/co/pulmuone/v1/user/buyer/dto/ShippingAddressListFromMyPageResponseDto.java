package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.ShippingAddressListFromMyPageResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "공통 배송지관리 목록 조회 ResponseDto")
public class ShippingAddressListFromMyPageResponseDto {

    @ApiModelProperty(value = "배송지관리 목록 총 갯수")
    private int total;

    @ApiModelProperty(value = "배송지관리 목록  resultVo")
    private List<ShippingAddressListFromMyPageResultVo> rows;

}
