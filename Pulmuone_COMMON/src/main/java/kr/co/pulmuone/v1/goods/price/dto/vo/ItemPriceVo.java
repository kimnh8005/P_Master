package kr.co.pulmuone.v1.goods.price.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPriceVo {

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목가격PK")
  private String ilItemPriceId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목PK")
	private String ilItemCd;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "적용시작일")
  private String startDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "적용종료일")
  private String endDt;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

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

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록구분")
  private String regTp;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록구분명")
  private String regTpNm;

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

}
