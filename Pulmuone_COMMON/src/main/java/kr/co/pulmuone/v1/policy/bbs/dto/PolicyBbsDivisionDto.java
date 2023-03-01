package kr.co.pulmuone.v1.policy.bbs.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsDivisionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "게시판분류설정 Dto")
public class PolicyBbsDivisionDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "게시판분류설정 조회 리스트")
	private	List<PolicyBbsDivisionVo> rows;

	@ApiModelProperty(value = "게시판분류설정 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "게시판분류설정.SEQ")
	private String csCategoryId;

	@ApiModelProperty(value = "게시판분류설정.게시판 타입 공통코드(BBS_TP)")
	private String bbsTp;

	@ApiModelProperty(value = "게시판분류설정.게시판 타입 공통코드명")
	private String bbsTpNm;

	@ApiModelProperty(value = "게시판분류설정.상위 PK")
	private String parentCategoryId;

	@ApiModelProperty(value = "게시판분류설정.게시판 분류명")
	private String categoryNm;

	@ApiModelProperty(value = "게시판분류설정.사용여부(Y:사용)")
	private String useYn;

	@ApiModelProperty(value = "게시판분류설정.정렬")
	private String sort;

	@ApiModelProperty(value = "게시판분류설정.사용자 정의 코드")
	private String userDefCd;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;


}
