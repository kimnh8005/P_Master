package kr.co.pulmuone.v1.outmall.sellers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "판매처 등록/수정 Request Dto")
public class SellersRequestDto extends BaseRequestPageDto {
        @ApiModelProperty(value="판매처 PK", required = true)
        private Long omSellersId;

        @ApiModelProperty(value="판매처 PK", required = true)
        private List<Long> omSellersIdList;

        @ApiModelProperty(value="판매처그룹코드", required = true)
        private String sellersGroupCode;

        @ApiModelProperty(value="판매처코드", required = true)
        private String sellersCode;

        @ApiModelProperty(value="판매처명", required = true)
        private String sellersName;

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

        @ApiModelProperty(value="외부몰코드", required = true)
        private String outmallCode;

        @ApiModelProperty(value="외부몰명", required = true)
        private String outmallName;

        @ApiModelProperty(value="등록자", required = true)
        private String createId;

        @ApiModelProperty(value="등록일", required = true)
        private String createDt;

        @ApiModelProperty(value="수정자", required = true)
        private String modifyId;

        @ApiModelProperty(value="수정일", required = true)
        private String modifyDt;

        @ApiModelProperty(value = "공급처 리스트")
        private List<OmBasicFeeVo> sellersSupplierList;

        @ApiModelProperty(value="사용여부")
        private String useYn;

}
