package kr.co.pulmuone.v1.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SellersRequestDto")
public class SellersRequestDto extends BaseRequestPageDto {

        @ApiModelProperty(value="판매처그룹코드", required = true)
        private String sellersGroupCd;

}
