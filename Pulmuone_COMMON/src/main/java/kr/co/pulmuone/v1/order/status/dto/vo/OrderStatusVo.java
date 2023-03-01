package kr.co.pulmuone.v1.order.status.dto.vo;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상태 VO")
public class OrderStatusVo {

    @ApiModelProperty(value = "주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "주문상태명")
    private String statusNm;

    @ApiModelProperty(value = "주문상태검색조건구분값")
    private String searchGrp;

    @ApiModelProperty(value = "주문상태검색조건구분값리스트", required = false)
    List<String> searchGrpList;

    @ApiModelProperty(value = "주문상태정렬")
    private int statusSort;

    @ApiModelProperty(value = "배송예정일변경여부")
    private String ifDayChangeYn;

    @ApiModelProperty(value = "배송추적여부")
    private String deliverySearchYn;

    @ApiModelProperty(value = "front Json 데이터")
    private String frontJson;

    @ApiModelProperty(value = "bos Json 데이터")
    private String bosJson;

    @ApiModelProperty(value = "action Json 데이터")
    private String actionJson;

    @ApiModelProperty(value = "주문 통합 상태 우선순위")
    private int orderStatusSort;

    @ApiModelProperty(value = "주문클레임 통합 상태 우선순위")
    private int claimStatusSort;

    @ApiModelProperty(value = "사용여부")
    private String useYn;
}
