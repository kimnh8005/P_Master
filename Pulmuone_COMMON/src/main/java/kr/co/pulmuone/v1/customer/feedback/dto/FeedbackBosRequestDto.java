package kr.co.pulmuone.v1.customer.feedback.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackBosRequestDto extends BaseRequestPageDto {

	  @JsonSerialize(using =  ToStringSerializer.class)
	    @ApiModelProperty(value = "상품유형")
	    private String feedbackProductType;

	    @JsonSerialize(using =  ToStringSerializer.class)
	    @ApiModelProperty(value = "후기유형")
	    private String feedbackType;

		@ApiModelProperty(value = "만족도 코드")
		private String satisfactionScore;

		@ApiModelProperty(value = "공개여부")
		private String displayYn;

		@ApiModelProperty(value = "공개여부 상세페이지")
		private String popupDisplayYn;

	    @ApiModelProperty(value = "공개여부")
	    private List<String> displayYnList;

		@ApiModelProperty(value = "후기필터")
		private String feedbackFilter;

	    @ApiModelProperty(value = "후기필터")
	    private List<String> feedbackFilterList;

		@ApiModelProperty(value = "등록일 시작")
		private String createDateStart;

		@ApiModelProperty(value = "등록일 종료")
		private String createDateEnd;

		@ApiModelProperty(value = "단일조건 검색기준")
		private String searchSelect;

		@ApiModelProperty(value = "단일조건 검색조건")
		private String findKeyword;

		@ApiModelProperty(value = "단일조건 검색조건")
		private ArrayList<String> findKeywordArray;

		@ApiModelProperty(value = "후기관리 ID")
		private String feedbackId;

		@ApiModelProperty(value = "체험단 우수후기여부")
		private String adminExcellentYnCheck;

		@ApiModelProperty(value = "베스트후기관리(관리자)")
		private String adminBestYn;


}
