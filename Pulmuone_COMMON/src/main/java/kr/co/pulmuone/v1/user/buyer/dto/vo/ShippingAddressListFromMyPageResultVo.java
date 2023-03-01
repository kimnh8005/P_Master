package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ShippingAddressListFromMyPageResultVo {

    private String urShippingAddrId;

    private String defaultYn;

    private String receiverZipCode;

    private String shippingName;

    private String receiverAddress1;

    private String receiverAddress2;

    private String buildingCode;

    private String shippingComment;

    private String accessInformationType;

    private String accessInformationName;

    private String accessInformationPassword;

    private String receiverTelephone;

    @UserMaskingUserName
    private String receiverName;

    @UserMaskingMobile
    private String receiverMobile;

    @UserMaskingAddress
    private String receiverAddress;

    @ApiModelProperty(value = "배송지관리 목록  resultVo")
    ShippingAddressPossibleDeliveryInfoDto delivery;

}
