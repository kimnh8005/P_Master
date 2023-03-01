package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AddBuyerStatusLogParamDto extends BaseDto {

	//회원 ID
	private String urUserId;

	//정상전환 사유
	private String reason;

	//회원 상태
	private String status;

	//첨부파일 경로
	private String attachmentPath;

	//첨부파일 원본명
	private String attachmentOriginName;

	//회원상태로그 PK
	private String urBuyerStatusLogId;
}
