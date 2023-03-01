package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 주문 조회 API 주문정보 응답 VO")
public class EZAdminOrderInfoOrderVo{

	@ApiModelProperty(value = "합포번호")
	private int pack;

	@ApiModelProperty(value = "관리번호")
	private int seq;

	@ApiModelProperty(value = "판매처코드")
	private int shop_id;

	@ApiModelProperty(value = "판매처 이름")
	private String shop_name;

	@ApiModelProperty(value = "주문번호")
	private String order_id;

	@ApiModelProperty(value = "주문상세번호")
	private String order_id_seq;

	@ApiModelProperty(value = "주문상세번호2")
	private String order_id_seq2;

	@ApiModelProperty(value = "판매처PK")
	private long omSellersId;

	@ApiModelProperty(value = "판매처 상품코드")
	private String shop_product_id;

	@ApiModelProperty(value = "판매처 상품명")
	private String product_name;

	@ApiModelProperty(value = "판매처 옵션")
	private String options;

	@ApiModelProperty(value = "수량")
	private int qty;

	@ApiModelProperty(value = "주문자 이름")
	private String order_name;

	@ApiModelProperty(value = "주문자 핸드폰번호")
	private String order_mobile;

	@ApiModelProperty(value = "주문자 전화번호")
	private String order_tel;

	@ApiModelProperty(value = "수령자 이름")
	private String recv_name;

	@ApiModelProperty(value = "수령자 핸드폰번호")
	private String recv_mobile;

	@ApiModelProperty(value = "수령자 전화번호")
	private String recv_tel;

	@ApiModelProperty(value = "수령자 주소")
	private String recv_address;

	@ApiModelProperty(value = "수령자 주소2")
	private String recv_address2;

	@ApiModelProperty(value = "수령자 우편번호")
	private String recv_zip;

	@ApiModelProperty(value = "배송메모")
	private String memo;

	@ApiModelProperty(value = "주문상태")
	private int status;

	@ApiModelProperty(value = "CS상태")
	private int order_cs;

	@ApiModelProperty(value = "발주일")
	private String collect_date;

	@ApiModelProperty(value = "주문일")
	private String order_date;

	@ApiModelProperty(value = "송장입력일")
	private String trans_date;

	@ApiModelProperty(value = "배송일")
	private String trans_date_pos;

	@ApiModelProperty(value = "정산예상금액")
	private int supply_price;

	@ApiModelProperty(value = "판매금액")
	private int amount;

	@ApiModelProperty(value = "추가금액")
	private int extra_money;

	@ApiModelProperty(value = "택배사")
	private String trans_corp;

	@ApiModelProperty(value = "송장번호")
	private String trans_no;

	@ApiModelProperty(value = "선착불")
	private String trans_who;

	@ApiModelProperty(value = "선결제금액")
	private int prepay_price;

	@ApiModelProperty(value = "사은품 내용")
	private String gift;

	@ApiModelProperty(value = "보류")
	private int	hold;

	@ApiModelProperty(value = "원주문 관리번호")
	private int org_seq;

	@ApiModelProperty(value = "딜번호")
	private String deal_no;

	@ApiModelProperty(value = "처리상태 W:미처리, I:처리중, E:처리완료")
	private String processCd;

	@ApiModelProperty(value = "주문 상품정보")
	private List<EZAdminOrderInfoOrderProductVo> order_products;

    @ApiModelProperty(value = "이지어드민 정보 PK")
    private Long ifEasyadminInfoId;

	@ApiModelProperty(value = "이지어드민 요청데이터 정보 PK")
	private Long ifEasyadminInfoReqDataId;

    /* 성공 시*/
    @ApiModelProperty(value = "이지어드민 주문 성공 정보 PK")
    private Long ifEasyadminOrderSuccId;

    /* 실패 시 */
    @ApiModelProperty(value = "이지어드민 주문 실패 정보 PK")
    private Long ifEasyadminOrderFailId;

    @ApiModelProperty(value = "오류메세지")
    private String errorMessage;

	@ApiModelProperty(value = "실패구분(U:업로드 B:배치)")
	private String failType;

    /* 클레임 주문 */
    @ApiModelProperty(value = "이지어드민 주문 클레임 정보 PK")
    private Long ifEasyadminOrderClaimId;
}
