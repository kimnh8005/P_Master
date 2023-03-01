package kr.co.pulmuone.v1.outmall.sellers.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellersListVo {
    @ApiModelProperty(value="외부몰 PK")
    private long omSellersId;

    @ApiModelProperty(value="외부몰코드")
    private String sellersCode;

    @ApiModelProperty(value="외부몰명")
    private String sellersName;

    @ApiModelProperty(value="물류구분")
    private String logisticsGubun;

    @ApiModelProperty(value="외부몰그룹명")
    private String sellersGroupName;

    @ApiModelProperty(value="공급처 갯수")
    private int sellersSupplierCount;

    @ApiModelProperty(value="외부몰URL")
    private String sellersUrl;

    @ApiModelProperty(value="외부몰관리자 URL")
    private String sellersAdminUrl;

    @ApiModelProperty(value="연동여부")
    private String interfaceYn;

    @ApiModelProperty(value="물류IF 연동여부")
    private String erpInterfaceYn;

    @ApiModelProperty(value="사용여부")
    private String useYn;

    @ApiModelProperty(value="등록일")
    private String createDate;
}
