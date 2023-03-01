package kr.co.pulmuone.v1.goods.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품공통공지 VO")
public class GoodsNoticeVo{

	@ApiModelProperty(value = "상품공통공지.SEQ")
	private String ilNoticeId;

	@ApiModelProperty(value = "상품공통공지.상품 공지 구분 공통코드(GOODS_NOTICE_TP - TOP(상단), BOTTOM(하단))")
	private String goodsNoticeTp;

	@ApiModelProperty(value = "상품공통공지.상품 공지 구분 공통코드명(GOODS_NOTICE_TP)")
	private String goodsNoticeTpName;

	@ApiModelProperty(value = "상품공통공지.공지제목")
	private String noticeNm;

	@ApiModelProperty(value = "상품공통공지.노출범위(Y:전체, N:출고처별)")
	private String dispAllYn;

	@ApiModelProperty(value = "상품공통공지.출고처 그룹 공통코드")
	private String warehouseGroup;

	@ApiModelProperty(value = "상품공통공지.출고처 그룹명")
	private String warehouseGroupName;

	@ApiModelProperty(value = "상품공통공지.출고처 FK")
	private String urWarehouseId;

	@ApiModelProperty(value = "상품공통공지.출고처명")
	private String warehouseName;

	@ApiModelProperty(value = "상품공통공지.노출 시작일")
	private String noticeStartDt;

	@ApiModelProperty(value = "상품공통공지.노출 종료일")
	private String noticeEndDt;

	@ApiModelProperty(value = "상품공통공지.사용여부(Y:사용, N:미사용)")
	private String useYn;

	@ApiModelProperty(value = "상품공통공지.상세정보")
	private String detlDesc;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createUserName;

	@ApiModelProperty(value = "등록자ID")
//	@UserMaskingLoginId
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자명")
	@UserMaskingUserName
	private String modifyUserName;

	@ApiModelProperty(value = "수정자ID")
//	@UserMaskingLoginId
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
