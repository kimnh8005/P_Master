package kr.co.pulmuone.v1.batch.goods.item.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ErpLinkItemVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "ErpBaekamGoodsItemResultVo")
public class ErpBaekamGoodsItemResultVo {

	@ApiModelProperty(value = "입력 시스템(예:ERP, ORGA …)")
	private String srcSvr;

	@ApiModelProperty(value = "시스템 구분값(ERPFM/ERPPFF/ERPOG/FD/DM/LDS/ORGAOMS/CJ/TOKTOK/ORGADIR)")
	private String crpCd;

	@ApiModelProperty(value = "ERP 상품명")
	private String itmNam;

	@ApiModelProperty(value = "ERP 상품번호")
	private String itmNo;

	@ApiModelProperty(value = "KANCODE")
	private String kanCd;

	@ApiModelProperty(value = "상품박스규격(mm)")
	private String boxSiz;

	@ApiModelProperty(value = "상품낱개규격(mm)")
	private String eaSiz;

	@ApiModelProperty(value = "입수수량")
	private String boxEa;

	@ApiModelProperty(value = "박스바코드")
	private String boxKanCd;

	@ApiModelProperty(value = "고객사코드")
	private String strKey;

	@ApiModelProperty(value = "파렛트당박스수")
	private String boxCrr;

	@ApiModelProperty(value = "외박스코드")
	private String extBoxCd;

	@ApiModelProperty(value = "풀무원 전송일시")
	private String regDat;

	@ApiModelProperty(value = "정상유통기간정보")
	private String durDay;

	@ApiModelProperty(value = "온라인출고기한")
	private String limOut;

	@ApiModelProperty(value = "마감임박유통기한")
	private String lifDay;

	@ApiModelProperty(value = "온도값")
	private String tmpVal;

	@ApiModelProperty(value = "정보업데이트여부")
	private String updFlg;

	@ApiModelProperty(value = "정보 업데이트 일시")
	private String updDat;

	@ApiModelProperty(value = "CJ접수일시")
	private String itfDat;

	@ApiModelProperty(value = "CJ접수여부")
	private String itfFlg;

}
