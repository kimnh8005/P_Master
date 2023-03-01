package kr.co.pulmuone.v1.api.ezadmin.dto;

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
@ApiModel(description = "이지어드민 API Request")
public class EZAdminRequestDto{

    @ApiModelProperty(value = "호출 API 종류")
    private String action;

    @ApiModelProperty(value = "조회항목")
    private String searchType;
    
    //문의글 답변
    @ApiModelProperty(value = "문의글 관리번호")
	private int seq;

    @ApiModelProperty(value = "문의글 답변")
	private String answer;
    

}
