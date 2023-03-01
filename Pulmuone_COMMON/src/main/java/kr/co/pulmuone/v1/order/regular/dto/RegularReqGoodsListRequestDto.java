package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 상품 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 상품 리스트 Request Dto")
public class RegularReqGoodsListRequestDto {

	@ApiModelProperty(value = "시작페이지")
	private int startPage;

	@ApiModelProperty(value = "시작페이지")
	private String ePage;

	@ApiModelProperty(value = "현재페이지")
	private int page;

	@ApiModelProperty(value = "페이지사이즈")
	private int limit;

	@ApiModelProperty(value = "페이지사이즈")
	private int pageSize;

	@ApiModelProperty(value = "회원PK")
	private long urUserId;

	@ApiModelProperty(value = "정기배송 주문 상태")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "정기배송 주문상세 상태 목록")
	private List<String> reqDetailStatusCdList;
}
