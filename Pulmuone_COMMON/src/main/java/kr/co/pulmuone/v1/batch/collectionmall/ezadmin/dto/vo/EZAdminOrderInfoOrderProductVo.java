package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo;

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
@ApiModel(description = "이지어드민 주문 조회 API 주문상품정보 응답 VO")
public class EZAdminOrderInfoOrderProductVo{

	@ApiModelProperty(value = "상품관리번호")
	private int prd_seq;

	@ApiModelProperty(value = "상품 CS상태")
	private int order_cs;

	@ApiModelProperty(value = "상품코드")
	private String product_id;

	@ApiModelProperty(value = "상품명")
	private String name;

	@ApiModelProperty(value = "옵션")
	private String options;

	@ApiModelProperty(value = "상품수량")
	private int qty;

	@ApiModelProperty(value = "판매중")
	private int enable_sale;

	@ApiModelProperty(value = "공급처코드")
	private int supply_code;

	@ApiModelProperty(value = "공급처이름")
	private String supply_name;

	@ApiModelProperty(value = "공급처 상품명")
	private String brand;

	@ApiModelProperty(value = "바코드")
	private String barcode;

	@ApiModelProperty(value = "상품 판매가")
	private int shop_price;

	@ApiModelProperty(value = "상품별 정산예상금액")
	private int prd_supply_price;

	@ApiModelProperty(value = "상품별 판매금액")
	private int prd_amount;

	@ApiModelProperty(value = "상품별 추가금액")
	private int extra_money;

	@ApiModelProperty(value = "취소일자")
	private String cancel_date;

	@ApiModelProperty(value = "교환일자")
	private String change_date;

    @ApiModelProperty(value = "이지어드민 정보 PK")
    private Long ifEasyadminInfoId;

    /* 성공 시 */
    @ApiModelProperty(value = "이지어드민 주문 성공 정보 PK")
    private Long ifEasyadminOrderSuccId;

    /* 실패 시 */
    @ApiModelProperty(value = "이지어드민 주문 실패 정보 PK")
    private Long ifEasyadminOrderFailId;

    @ApiModelProperty(value = "오류메세지")
    private String errorMessage;

    /* 클레임 주문 */
    @ApiModelProperty(value = "이지어드민 주문 클레임 정보 PK")
    private Long ifEasyadminOrderClaimId;
}
