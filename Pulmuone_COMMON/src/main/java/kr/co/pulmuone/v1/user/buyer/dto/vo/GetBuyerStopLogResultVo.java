package kr.co.pulmuone.v1.user.buyer.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class GetBuyerStopLogResultVo {

	//회원상태로그 PK
	private String urBuyerStatusLogId;

	//회원정보명
	@UserMaskingUserName
	private String userInfoName;

	//회원정보
	@UserMaskingLoginId
	private String userInfo;

	//정지일
	private String stopCreateDate;

	//정지 처리자명
	private String stopAdminName;

	//정지 처리자
	private String stopAdminId;

	//정지사유
	private String stopReason;

	//정상전환일
	private String normalCreateDate;

	//정상전환 처리자명
	private String normalAdminName;

	//정상전환 처리자
	private String normalAdminId;

	//정상전환 사유
	private String normalReason;

	//정상전환 첨부파일 경로
	private String attachmentPath;

	//정상전환 첨부파일 원본명
	private String attachmentOriginName;

	//정상전환 첨부파일 물리명
	private String attachmentPhysicalName;

	public String getUserInfo() {
		if(!Objects.isNull(userInfoName)){
			return userInfoName + '(' + userInfo + ')';
		}
		return userInfo;
	}

	public String getStopAdminId() {
		if(!Objects.isNull(stopAdminName)){
			return stopAdminName + '(' + stopAdminId + ')';
		}
		return stopAdminId;
	}

	public String getNormalAdminId() {
		if(!Objects.isNull(normalAdminName)){
			return normalAdminName + '(' + normalAdminId + ')';
		}
		return normalAdminId;
	}
}
