package kr.co.pulmuone.v1.display.popup.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetDisplayPopupListResultVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "전시팝업PK")
    int displayFrontPopupId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "순번")
    String sort;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "팝업노출대상")
    String displayTargetType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "노출채널")
    String displayRangeType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "팝업 제목")
    String popupSubject;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "팝업유형")
    String popupType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "노출기간")
    String displayPopupDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "작성자")
    String createId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "노출상태")
    String  exposureState;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    String useYn;
}
