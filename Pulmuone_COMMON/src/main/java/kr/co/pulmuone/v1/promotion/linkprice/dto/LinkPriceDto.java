package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@ToString
public class LinkPriceDto{

    @ApiModelProperty(value = "주문번호")
    private String orderId;

    @ApiModelProperty(value = "상품번호")
    private String productId;

    @ApiModelProperty(value = "LPINFO쿠키")
    private String lpinfo;

    @ApiModelProperty(value = "user-agent")
    private String userAgent;

    @ApiModelProperty(value = "Client IP Address")
    private String ip;

    @ApiModelProperty(value = "Device Type")
    private String deviceType;

    @ApiModelProperty(value = "고객 이름")
    private String urUserName;

    @ApiModelProperty(value = "주문 상품정보 리스트")
    private List<LinkPriceOrderDetailVo> orderGoodsList;

    @ApiModelProperty(value = "링크프라이스 저장용 정보")
    public List<LinkPriceDto> linkPriceList;

    @ApiModelProperty(value = "링크프라이스 전송여부")
    private String sendYn;

    @ApiModelProperty(value = "링크프라이스 성공여부")
    private String successYn;

    @ApiModelProperty(value = "링크프라이스 전송값 json")
    private String sendData;

    @ApiModelProperty(value = "링크프라이스 반환값")
    private String returnData;

    @ApiModelProperty(value = "수정일")
    private Date updateDate;

    @ApiModelProperty(value = "총 결제금액")
    private long finalPaidPrice;

    @ApiModelProperty(value = "주문완료일")
    private Date paidYmd;

    @ApiModelProperty(value = "주문확정일")
    private Date confirmedYmd;

    @ApiModelProperty(value = "주문취소일")
    private Date canceledYmd;
}
