package kr.co.pulmuone.v1.user.employee.dto;

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
@ApiModel(description = "BOS 계정관리 Request")
public class EmployeeRequestDto extends BaseRequestPageDto{

    // BOS 계정관리 목록
	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "검색키워드")
	private String findKeyword;

	@ApiModelProperty(value = "관리자유형")
	private String adminType;

    @ApiModelProperty(value = "관리자유형")
    private List<String> adminTypeList;

	@ApiModelProperty(value = "회원상태")
	private String userStatus;

    @ApiModelProperty(value = "회원상태")
    private List<String> userStatusList;

	@ApiModelProperty(value = "등록일 시작")
	private String createDateStart;

	@ApiModelProperty(value = "등록일 종료")
	private String createDateEnd;

	// BOS 계정관리 등록 & 수정
    @ApiModelProperty(value = "사번")
    private String employeeNumber;

    @ApiModelProperty(value = "로그인 ID")
    private String loginId;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "역할타입 ID")
    private Long roleTypeId;

    @ApiModelProperty(value = "회원 ID")
    private Long userId;
}
