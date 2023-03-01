package kr.co.pulmuone.v1.policy.bbs.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsAuthVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "게시판권한설정 Dto")
public class PolicyBbsAuthDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "게시판권한설정 조회 리스트")
	private	List<PolicyBbsAuthVo> rows;

	@ApiModelProperty(value = "게시판권한설정 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "게시판권한설정.SEQ")
	private String csBbsConfigId;

	@ApiModelProperty(value = "게시판권한설정.게시판분류 SEQ")
	private String csCategoryId;

	@ApiModelProperty(value = "게시판권한설정.게시판명")
	private String bbsNm;

	@ApiModelProperty(value = "게시판권한설정.이미지 사용여부(Y:사용)")
	private String imageYn;

	@ApiModelProperty(value = "게시판권한설정.첨부여부(Y:사용)")
	private String attachYn;

	@ApiModelProperty(value = "게시판권한설정.답변여부(Y:사용)")
	private String replyYn;

	@ApiModelProperty(value = "게시판권한설정.댓글 사용 여부(Y:사용)")
	private String commentYn;

	@ApiModelProperty(value = "게시판권한설정.댓글 비밀 여부(Y:사용)")
	private String commentSecretYn;

	@ApiModelProperty(value = "게시판권한설정.추천 여부(Y:사용)")
	private String recommendYn;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

	@ApiModelProperty(value = "게시판 타입 공통코드(BBS_TP)")
	private String bbsTp;


}
