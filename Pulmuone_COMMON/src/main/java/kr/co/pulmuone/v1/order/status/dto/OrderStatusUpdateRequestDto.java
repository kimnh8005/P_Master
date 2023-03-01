package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 업데이트 관련 RequestDto")
public class OrderStatusUpdateRequestDto extends BaseRequestDto {



    @ApiModelProperty(value = "관리자 ID")
    long userId;

    @ApiModelProperty(value = "관리자명")
    String loginName;

    @ApiModelProperty(value = "변경 상태")
    String statusCd;

    @ApiModelProperty(value = "주문상세 Pk 리스트")
    List<Long> detlIdList;

    @ApiModelProperty(value = "택배사 PK 리스트")
    List<Long> shippingCompIdList;

    @ApiModelProperty(value = "송장번호 리스트")
    List<String> trackingNoList;



}
