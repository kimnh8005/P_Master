package kr.co.pulmuone.v1.goods.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품공통공지 Dto")
public class GoodsNoticeDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "상품공통공지 조회 리스트")
	private	List<GoodsNoticeVo> rows;

	@ApiModelProperty(value = "상품공통공지 조회 카운트")
	private	int	total;

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

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

	private String conditionType;
	private String conditionValue;
	private String startCreateDate;
	private String endCreateDate;

}
