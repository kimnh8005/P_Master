package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 업데이트 관련 RequestDto")
public class OrderStatusUpdateDto extends BaseRequestDto {



    @ApiModelProperty(value = "관리자 ID")
    long userId;

    @ApiModelProperty(value = "관리자명")
    String loginName;

    @ApiModelProperty(value = "변경 상태")
    String orderStatusCd;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;




    @ApiModelProperty(value = "배송준비중등록자")
    private long drId;

    @ApiModelProperty(value = "배송중등록자")
    private long diId;

    @ApiModelProperty(value = "배송완료등록자")
    private long dcId;

    @ApiModelProperty(value = "구매확정등록자")
    private long bfId;


    @ApiModelProperty(value = "처리이력내용")
    String histMsg;

}
