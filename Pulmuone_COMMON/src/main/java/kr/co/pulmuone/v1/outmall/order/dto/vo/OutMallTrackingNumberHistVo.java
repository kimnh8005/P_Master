package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserNameLoginId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutMallTrackingNumberHistVo {

    @ApiModelProperty(value = "기간 검색 유형")
    private String searchTrackNumHistPeriodType;

    @ApiModelProperty(value = "기간 검색 시작일")
    private String startDate;

    @ApiModelProperty(value = "기간 검색 시작시")
    private String startHour;

    @ApiModelProperty(value = "기간 검색 종료일")
    private String endDate;

    @ApiModelProperty(value = "기간 검색 종료시")
    private String endHour;

    @ApiModelProperty(value = "출고처 GROUP")
    private Long trackNumHistWarehouseGroup;

    @ApiModelProperty(value = "출고처 PK")
    private Long trackNumHistWarehouseId;

    @ApiModelProperty(value = "송장등록일시")
    private String sendEndDt;

    @ApiModelProperty(value = "출고처")
    private String shippingCompNm;

    @ApiModelProperty(value = "등록라인수")
    private Long updateCnt;
}
