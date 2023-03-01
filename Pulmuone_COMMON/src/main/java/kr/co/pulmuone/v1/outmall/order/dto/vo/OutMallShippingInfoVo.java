package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutMallShippingInfoVo {

    @ApiModelProperty(value = "관리번호")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "택배사명")
    private String shippingCompNm;

    @ApiModelProperty(value = "사방넷 택배사 코드")
    private String outmallShippingCompCd;

    @ApiModelProperty(value = "배송상태")
    private String orderStatus;

    @ApiModelProperty(value = "출고처")
    private String warehouseNm;

    @ApiModelProperty(value = "송장등록일")
    private String createDt;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outmallDetailId;

    @ApiModelProperty(value="판매처명")
    private String sellersNm;

}
