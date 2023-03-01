package kr.co.pulmuone.v1.policy.origin.dto.basic;

import java.util.ArrayList;
import java.util.Arrays;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "원산지 목록 Request Dto")
public class GetPolicyOriginListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "원산지 구분")
    private String originTypes;

    @ApiModelProperty(value = "검색어 입력 항목")
    private String condiType;

    @ApiModelProperty(value = "검색어")
    private String condiValue;


    public ArrayList<String> getOriginTypeList() {
        if(!StringUtil.isNvl(StringUtil.nvl(this.originTypes)) && !Constants.ARRAY_SEPARATORS.equals(StringUtil.nvl(this.originTypes))) {
            return new ArrayList<String>(Arrays.asList(originTypes.split(Constants.ARRAY_SEPARATORS)));
        }else {
            return new ArrayList<String>();
        }
    }

}
