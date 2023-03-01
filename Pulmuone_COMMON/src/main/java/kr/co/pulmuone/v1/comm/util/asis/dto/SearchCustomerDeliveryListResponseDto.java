package kr.co.pulmuone.v1.comm.util.asis.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "통합몰 회원가입시 AS-IS 회원 배송지 정보 조회 API 응답 DTO")
public class SearchCustomerDeliveryListResponseDto {

	@ApiModelProperty(value = "배송지 리스트")
    private List<SearchCustomerDeliveryDto> data;

}
