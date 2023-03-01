package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 배송지 저장 요청 DTO")
public class PutCartShippingAddressRequestDto{

	@ApiModelProperty(value = "장바구니 타입")
	private String cartType;

	@ApiModelProperty(value = "장바구니 PK")
	private List<Long> spCartId;

	@ApiModelProperty(value = "배송지 수령인명")
	private String receiverName;

	@ApiModelProperty(value = "배송지 우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "배송지 주소")
	private String receiverAddress1;

	@ApiModelProperty(value = "배송지 상세주소")
	private String receiverAddress2;

	@ApiModelProperty(value = "건물관리번호")
	private String buildingCode;

	@ApiModelProperty(value = "배송지 모바일")
	private String receiverMobile;

	@ApiModelProperty(value = "출입정보타입")
	private String accessInformationType;

	@ApiModelProperty(value = "출입정보 비밀번호(암호화)")
	private String accessInformationPassword;

	@ApiModelProperty(value = "배송 요청 사항")
	private String shippingComment; /* lastComment */

	@ApiModelProperty(value = "기본 배송지 설정 여부 ")
	private String basicYn;

	@ApiModelProperty(value = "배송지 목록에 추가")
	private String addShippingAddress;

	@ApiModelProperty(value = "기본 배송지 여부")
	private String selectBasicYn;

	@ApiModelProperty(value = "배송지 PK")
	private Long shippingAddressId;

}
