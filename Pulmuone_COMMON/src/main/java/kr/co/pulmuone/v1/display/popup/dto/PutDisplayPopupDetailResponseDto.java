package kr.co.pulmuone.v1.display.popup.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PutDisplayPopupDetailResponseDto {

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
	    @ApiModelProperty(value = "팝업 제목")
	    String popupSubject;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "노출시작기간")
	    String displayPopupStartDate;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "노출종료기간")
	    String displayPopupEndDate;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "노출채널")
	    String displayRangeType;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "사용여부")
	    String useYn;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "오늘 그만보기 노출")
	    String todayStopYn;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "html 팝업 내용")
	    String html;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "팝업유형")
	    String popupType;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "링크 url")
	    String linkUrl;

	    @JsonSerialize(using = ToStringSerializer.class)
	    @ApiModelProperty(value = "이미지 이름")
	    String popupImageOriginName;



}
