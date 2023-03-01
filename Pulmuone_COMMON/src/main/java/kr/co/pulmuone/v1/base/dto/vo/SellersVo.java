package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersSuppilerVo;
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
    private String sellersGroupCd;

    @ApiModelProperty(value="판매처코드", required = true)
    private String sellersCd;

    @ApiModelProperty(value="판매처명", required = true)
    private String sellersNm;

}
