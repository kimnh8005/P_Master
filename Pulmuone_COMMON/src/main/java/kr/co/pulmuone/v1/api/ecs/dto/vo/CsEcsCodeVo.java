package kr.co.pulmuone.v1.api.ecs.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ECS 분류값 Vo")
public class CsEcsCodeVo {

	/*
	 * CS_ECS_CODE PK
	 */
	private Long csEcsCodeId;

	/*
	 * 상담대분류
	 */
	private String hdBcode;

	/*
	 * 상담대분류명
	 */
	private String hdBcodeName;

	/*
	 * 상담중분류
	 */
	private String hdScode;

	/*
	 * 상담중분류명
	 */
	private String hdScodeName;

	/*
	 * 상담소분류
	 */
	private String claimGubun;

	/*
	 * 상담소분류명
	 */
	private String claimGubunName;

}
