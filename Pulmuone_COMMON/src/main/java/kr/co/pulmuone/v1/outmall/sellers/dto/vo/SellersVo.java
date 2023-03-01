package kr.co.pulmuone.v1.outmall.sellers.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellersVo {

    @ApiModelProperty(value="판매처 PK", required = true)
    private long omSellersId;

    @ApiModelProperty(value="판매처그룹코드", required = true)
    private String sellersGroupCode;

    @ApiModelProperty(value="판매처코드", required = true)
    private String sellersCode;

    @ApiModelProperty(value="판매처", required = true)
    private String sellersCd;

    @ApiModelProperty(value="판매처그룹코드", required = true)
    private String sellersGroupCd;

    @ApiModelProperty(value="판매처명", required = true)
    private String sellersName;

    @ApiModelProperty(value="판매처명", required = true)
    private String sellersNm;

    @ApiModelProperty(value="물류구분")
    private String logisticsGubun;

    @ApiModelProperty(value="판매처URL", required = false)
    private String sellersUrl;

    @ApiModelProperty(value="판매처관리자 URL", required = false)
    private String sellersAdminUrl;

    @ApiModelProperty(value="연동여부", required = true)
    private String interfaceYn;

    @ApiModelProperty(value="물류IF 연동여부", required = true)
    private String erpInterfaceYn;

    @ApiModelProperty(value="사용여부", required = true)
    private String useYn;

    @ApiModelProperty(value="외부몰코드", required = true)
    private String outmallCode;

    @ApiModelProperty(value="외부몰코드", required = true)
    private String outmallCd;

    @ApiModelProperty(value="외부몰명", required = true)
    private String outmallName;

    @ApiModelProperty(value="외부몰명", required = true)
    private String outmallNm;

    @ApiModelProperty(value="등록자", required = true)
    private String createId;

    @ApiModelProperty(value="등록일", required = true)
    private String createDate;

    @ApiModelProperty(value="수정자", required = true)
    private String modifyId;

    @ApiModelProperty(value="수정일", required = true)
    private String modifyDate;

    @ApiModelProperty(value="생성일", required = true)
    private String createDt;

    @ApiModelProperty(value="수정일", required = true)
    private String modifyDt;

    @ApiModelProperty(value = "등록자")
    @UserMaskingUserName
    private String createUserName;

    @ApiModelProperty(value = "수정자")
    @UserMaskingUserName
    private String modifyUserName;

    @ApiModelProperty(value = "공급처 리스트")
    private List<OmBasicFeeListDto> sellersSupplierList;

}
