package kr.co.pulmuone.batch.erp.domain.model.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "UserVo")
@SuppressWarnings("serial")
public class UserVo implements Serializable {

	@ApiModelProperty(value = "발급회원코드")
    private String userId;

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "회원명")
    private String loginName;

	@ApiModelProperty(value = "회원 타입")
    private String userType;

	@ApiModelProperty(value = "회원상태")
	private String statusType;

	@ApiModelProperty(value = "거래처명")
    private String companyName;

	@ApiModelProperty(value = "롤 ID")
    private String roleId;

	@ApiModelProperty(value = "롤 ID 리스트")
    private List<String> ListRoleId;

	@ApiModelProperty(value = "비밀번호 변경 필요여부")
    private String passwordChangeYn;

	@ApiModelProperty(value = "마지막 로그인 경과 일")
    private int lastLoginElapsedDay;

	@ApiModelProperty(value = "접속 로그 일련 ID")
	private long connectionId;

	@ApiModelProperty(value = "임시비밀번호 여부")
	private String temporaryYn;

	@ApiModelProperty(value = "개인정보 열람권한 유무(Y:보유, N:미보유)")
	private String personalInformationAccessYn;

	@ApiModelProperty(value = "임시 비밀번호 유효시간 비교")
    private int temporaryExpiration;

	//필요한가?
	@ApiModelProperty(value = "언어 코드")
    private String langCode;



	//필요한가?
	/*
	@ApiModelProperty(value = "등록 일자")
	private	 String	createDate ;

	@ApiModelProperty(value = "수정 일자")
    private String modifyDate;


	@ApiModelProperty(value = "언어 코드")
    private String langCode;


	@ApiModelProperty(value = "전자계약 체결여부")
    private String ceCnclYn;

*/
}
