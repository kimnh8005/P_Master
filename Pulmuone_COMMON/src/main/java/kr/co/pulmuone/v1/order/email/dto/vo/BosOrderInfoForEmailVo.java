package kr.co.pulmuone.v1.order.email.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 알림 자동메일 발송위한 정보 vo")
public class BosOrderInfoForEmailVo {

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "이메일")
    private String mail;

    @ApiModelProperty(value = "휴대폰")
    private String mobile;

    /* BOS 주문 상태 알림 */
    @ApiModelProperty(value = "알림 발송 일자")
    private String sendDate;

    @ApiModelProperty(value = "거래처 PK")
    private String urClientId;

    @ApiModelProperty(value = "결제완료 count")
    private int ICCount;

    @ApiModelProperty(value = "주문 취소요청 count")
    private int CACount;

    @ApiModelProperty(value = "배송준비중 count")
    private int DRCount;
}
