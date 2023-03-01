package kr.co.pulmuone.v1.order.status.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 등록/수정 RequestDto")
public class OrderStatusRequestDto extends BaseRequestDto {

	@ApiModelProperty(value="주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "주문상태명")
    private String statusNm;

    @ApiModelProperty(value = "주문상태 구분")
    private String searchGrp;

    @ApiModelProperty(value = "주문상태검색조건구분값리스트", required = false)
    List<String> searchGrpList;

    @ApiModelProperty(value = "정렬순서")
    private int statusSort;

    @ApiModelProperty(value = "I/F일자 변경가능여부")
    private String ifDayChangeYn;

    @ApiModelProperty(value = "배송추적 가능여부")
    private String deliverySearchYn;

    @ApiModelProperty(value = "주문 통합 상태 우선순위")
    private int orderStatusSort;

    @ApiModelProperty(value = "주문 클레임 통합 상태 우선순위")
    private int claimStatusSort;

    @ApiModelProperty(value = "사용여부")
    private String useYn;


}
