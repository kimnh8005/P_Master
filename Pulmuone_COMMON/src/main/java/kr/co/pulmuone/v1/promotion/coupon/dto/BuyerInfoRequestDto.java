package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BuyerInfoRequestDto")
public class BuyerInfoRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "아이디/회원 조회값")
	private String condiValue;

	@ApiModelProperty(value = "아이디/회원 타입")
	private String condiType;

	@ApiModelProperty(value = "휴대폰/email 타입")
	private String searchType;

	@ApiModelProperty(value = "조회값")
	private String searchValue;

	@ApiModelProperty(value = "회원등급")
	private String searchUserGroup;

	@ApiModelProperty(value = "검색일자 타입")
	private String searchDateType;

	@ApiModelProperty(value = "검색 시작일")
	private String startCreateDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endCreateDate;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

	@ApiModelProperty(value = "중복허용 구분")
	private String duplicateChecked;

	@ApiModelProperty(value = "중복허용 구분")
	private String statusComment;

	@ApiModelProperty(value = "쿠폰 ID")
	private String pmCouponId;

	@ApiModelProperty(value = "수정 데이터")
    String updateData;

	@ApiModelProperty(value = "발급대상 Dto 리스트")
    List<CouponIssueParamDto> updateRequestDtoList;

}
