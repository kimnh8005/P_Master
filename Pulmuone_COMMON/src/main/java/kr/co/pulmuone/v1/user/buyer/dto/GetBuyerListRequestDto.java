package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.ArrayList;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerListRequestDto")
public class GetBuyerListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String condiType;

	@ApiModelProperty(value = "")
	private String condiValue;

	@ApiModelProperty(value = "")
	private String mobile;

	@ApiModelProperty(value = "")
	private String mail;

	@ApiModelProperty(value = "")
	private String searchUserType;

	@ApiModelProperty(value = "")
	private String searchUserGroup;

	@ApiModelProperty(value = "")
	private String searchUserStatus;

	@ApiModelProperty(value = "")
	private String startCreateDate;

	@ApiModelProperty(value = "")
	private String endCreateDate;

	@ApiModelProperty(value = "")
	private String startLastLoginDate;

	@ApiModelProperty(value = "")
	private String endLastLoginDate;

	@ApiModelProperty(value = "")
	private String smsYn;

	@ApiModelProperty(value = "")
	private String mailYn;

	@ApiModelProperty(value = "마케팅활용동의여부")
	private String marketingYn;

	@ApiModelProperty(value = "")
	private String pushYn;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

	@ApiModelProperty(value = "사용자 ID")
	private String urUserId;

	@ApiModelProperty(value = "포인트 상세구분")
	private String searchPointDetailType;

	@ApiModelProperty(value = "포인트 구분")
	private String paymentType;

	@ApiModelProperty(value = "포인트 사용 내용")
	private String pointContent;

	@ApiModelProperty(value = "포인트 사용 발생일 조회 시작")
	private String mileageStartCreateDate;

	@ApiModelProperty(value = "포인트 사용 발생일 조회 종료")
	private String mileageEndCreateDate;

	@ApiModelProperty(value = "CS 회원구분")
	private String csSearchBuyerType;

	@ApiModelProperty(value = "CS 회원검색 키워드")
	private String csFindKeyword;

	@ApiModelProperty(value = "CS 회원검색 조건")
	private String csSearchSelect;


}
