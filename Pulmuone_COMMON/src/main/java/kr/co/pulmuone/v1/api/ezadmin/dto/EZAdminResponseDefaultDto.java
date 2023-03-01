package kr.co.pulmuone.v1.api.ezadmin.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 API Response Default")
public class EZAdminResponseDefaultDto{

    @ApiModelProperty(value = "에러여부")
    private String error;

    @ApiModelProperty(value = "메세지")
    private String msg;

    @ApiModelProperty(value = "총건수")
    private String total;

    @ApiModelProperty(value = "페이지")
    private String page;

    @ApiModelProperty(value = "조회값")
    private String limit;

    @ApiModelProperty(value = "API DATA")
    private String responseData;

    private List<?> data;

    public EZAdminResponseDefaultDto (List<?> list) {
    	this.data = list;
    }


}
