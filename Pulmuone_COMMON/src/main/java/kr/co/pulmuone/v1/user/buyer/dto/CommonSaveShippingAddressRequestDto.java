package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원 배송지 추가&수정 RequestDto")
public class CommonSaveShippingAddressRequestDto
{

	@ApiModelProperty(value = "회원 ID")
	private Long urUserId;

	@ApiModelProperty(value = "회원 배송지 PK")
	private Long urShippingAddrId;

	@ApiModelProperty(value = "기본배송지")
	private String defaultYn;

	@ApiModelProperty(value = "배송지명")
	private String shippingName;

	@ApiModelProperty(value = "받는사람")
	private String receiverName;

	@ApiModelProperty(value = "휴대폰")
	private String receiverMobile;

	@ApiModelProperty(value = "전화번호")
	private String receiverTelephone;

	@ApiModelProperty(value = "우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "주소")
	private String receiverAddress1;

	@ApiModelProperty(value = "상세주소")
	private String receiverAddress2;

	@ApiModelProperty(value = "건물관리번호")
	private String buildingCode;

	@ApiModelProperty(value = "배송요청사항")
	private String shippingComment;

	@ApiModelProperty(value = "출입정보")
	private String accessInformationType;

	@ApiModelProperty(value = "공동현관 비밀번호")
	private String accessInformationPassword;

	@ApiModelProperty(value = "회원상태변경 로그값 배열")
	private String changeLogArray;

	@ApiModelProperty(value = "기본 배송지 여부")
	private String selectBasicYn;

	// 주문상세 > 배송정보 > 변경이력 팝업
	@ApiModelProperty(value = "주문 배송지 pk" ,hidden=true)
	private Long odShippingZoneId;

	@ApiModelProperty(value = "주문번호 pk" ,hidden=true)
	private Long odOrderId;

	@ApiModelProperty(value = "배송타입",hidden=true)
	private String deliveryType;

	@ApiModelProperty(value = "주문타입",hidden=true)
	 private int shippingType;

	@ApiModelProperty(value = "이메일 주소",hidden=true)
	private String receiverMail;

	@ApiModelProperty(value = "배송지 변경 적용일자(녹즙만)")
	private String deliveryDt;

	@ApiModelProperty(value = "일일상품 타입")
	private String goodsDailyType;

	@ApiModelProperty(value = "기획전 유형")
	private String promotionTp;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "스토어(매장가맹점) PK")
	private String urStoreId;
}
