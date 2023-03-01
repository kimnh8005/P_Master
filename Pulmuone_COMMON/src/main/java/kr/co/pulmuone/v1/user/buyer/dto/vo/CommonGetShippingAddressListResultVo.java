package kr.co.pulmuone.v1.user.buyer.dto.vo;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonGetShippingAddressListResultVo {

	//추가
    private String urShippingAddrId;

    private String shippingName;

	private String shippingAddressId;

	private String shippingAddressType;

	private String shippingAddressTypeName;

	@UserMaskingUserName
	private String receiverName;

	@UserMaskingMobile
	private String receiverMobile;

	private String receiverZipCode;

	@UserMaskingAddress
	private String receiverAddress1;

	private String receiverAddress2;

	private String receiverAddress;

	private String defaultYn;

	private String buildingCode;

	private String shippingCmnt;

	private String accessInformationType;

	private String accessInformationTypeName;

	private String accessInformationPassword;

	ShippingAddressPossibleDeliveryInfoDto delivery;

	private String latelyYn;

	private boolean isShippingPossibilityStoreDeliveryArea;

	// 일일상품 배송가능 기본일자
	private String defaultDelvDate;

	// 일일상품 배송가능 일자리스트
	private	List<String> delvDate;

	// 일일상품 배송가능 일자요일 리스트
	private List<String> delvDateWeekDay;

}
