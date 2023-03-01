package kr.co.pulmuone.v1.statics.user.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserCountStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기준기간-시작일자")
    private String startDe;

    @ApiModelProperty(value = "기준기간-종료일자")
    private String endDe;

    @ApiModelProperty(value = "대비기간-시작일자")
    private String contrastStartDe;

    @ApiModelProperty(value = "대비기간-종료일자")
    private String contrastEndDe;

//    @ApiModelProperty(value = "회원상태")
//    private String agentTypeCd;

    public String getSearchInfo() {
        StringBuilder result = new StringBuilder();
        result.append("기준기간:");
        result.append(DateUtil.convertFormatNew(this.getStartDe(), "yyyyMMdd", "yyyy-MM-dd"))
                .append("~")
                .append(DateUtil.convertFormatNew(this.getEndDe(), "yyyyMMdd", "yyyy-MM-dd"));
        result.append("/대비기간:");
        result.append(DateUtil.convertFormatNew(this.getContrastStartDe(), "yyyyMMdd", "yyyy-MM-dd"))
                .append("~")
                .append(DateUtil.convertFormatNew(this.getContrastEndDe(), "yyyyMMdd", "yyyy-MM-dd"));

        return result.toString();
    }

}
