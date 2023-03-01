package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserNameLoginId;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutMallShippingExceldownHistVo {

    @ApiModelProperty(value = "판매처")
    private String sellersGroup;

    @ApiModelProperty(value = "기간 검색 유형")
    private String periodType;

    @ApiModelProperty(value = "기간 검색 시작일")
    private String startDt;

    @ApiModelProperty(value = "기간 검색 종료일")
    private String endDt;

    @ApiModelProperty(value = "출고처")
    private String warehouseNm;

    @ApiModelProperty(value = "수집몰")
    private String outmallType;

    @ApiModelProperty(value = "관리자")
    @UserMaskingUserNameLoginId
    private String userInfo;

    @ApiModelProperty(value = "다운로드일시")
    private String downloadDt;

}
