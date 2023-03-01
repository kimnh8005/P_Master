package kr.co.pulmuone.v1.system.itgc.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItgcRequestDto {

    @ApiModelProperty(value = "메뉴 PK")
    private Long stMenuId;

    @ApiModelProperty(value = "구분")
    private SystemEnums.ItgcType itgcType;

    @ApiModelProperty(value = "ITSM 계정")
    private String itsmId;

    @ApiModelProperty(value = "항목")
    private String itgcDetail;

    @ApiModelProperty(value = "추가/삭제")
    private SystemEnums.ItgcAddType itgcAddType;

    @ApiModelProperty(value = "대상자")
    private String targetInfo;

    @ApiModelProperty(value = "대상자 PK")
    private Long targetUserId;

    @ApiModelProperty(value = "등록일")
    private Long createId;

}
