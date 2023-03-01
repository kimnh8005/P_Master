package kr.co.pulmuone.v1.promotion.notissue.dto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "PointNotIssueListRequestDto")
public class PointNotIssueListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색어 타입")
	private String searchSelect;

	@ApiModelProperty(value = "검색어")
	private String findKeyword;


	@ApiModelProperty(value = "지급구분")
	private String searchPointDetailType;

	@ApiModelProperty(value = "등록 시작일")
	private String startDate;

	@ApiModelProperty(value = "등록 종료일")
	private String endDate;

	@ApiModelProperty(value = "단일조건 검색조건")
	private ArrayList<String> findKeywordArray;

	@ApiModelProperty(value = "미지급 적립금 고유값")
	private Long pmPointNotIssueId;

	@ApiModelProperty(value = "사용자 Id")
	private Long urUserId;

	@ApiModelProperty(value = "최대 적립 적립금")
	private int maxPoint;

}
