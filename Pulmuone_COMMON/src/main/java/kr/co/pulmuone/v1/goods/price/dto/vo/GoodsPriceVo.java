package kr.co.pulmuone.v1.goods.price.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
//import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsPriceVo {

  public GoodsPriceVo () {}

  public GoodsPriceVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {
        this.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        this.setCreateId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        this.setModifyId("0");
        this.setCreateId("0");
      }
    }
    else {
      this.setModifyId("0");
      this.setCreateId("0");
    }
  }

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품가격PK")
  private String ilGoodsPriceId;

  @ApiModelProperty(value = "임직원 할인 가격정보PK")
  private String ilGoodsEmployeePriceId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품PK")
	private String ilGoodsId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목가격PK")
	private String ilItemPriceId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품할인PK")
	private String ilGoodsDiscountId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인유형")
	private String discountTp;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "가격시작일")
	private String priceStartDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "가격종료일")
	private String priceEndDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매가")
  private int salePrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록자")
	private String createId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록일")
	private String createDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정자")
	private String modifyId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정일")
	private String modifyDt;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인율")
  private int discountRatio;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인판매가")
  private int discountSalePrice;

  // 할인하는금액
  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인가")
  private int discountPrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인유형명(우선,올가,즉시,임직원,묶음)")
  private String discountTpNm;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "할인방법유형명(정률,고정가)")
  private String discountMethodTpNm;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "승인상태")
  private String status;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "승인상태명")
  private String statusNm;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "승인담당자ID")
  private int approveId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "승인담당자명")
  private String approveNm;
  
  @ApiModelProperty(value = "승인요청자")
  private String apprReqInfo;
  
  @ApiModelProperty(value = "승인관리자")
  private String apprInfo;
}
