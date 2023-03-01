package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPoRequestVo {

	@ApiModelProperty(value = "순번")
	private int rownum;

	@ApiModelProperty(value = "행사발주 아이디(SEQ)")
	private String ilPoEventId;

	@ApiModelProperty(value = "상품코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "품목명")
	private String itemNm;

	@ApiModelProperty(value = "아이템바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품유형")
	private String goodsTp;

	@ApiModelProperty(value = "상품유형명")
	private String goodsTpName;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "출고처ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "출고처")
	private String warehouseNm;

	@ApiModelProperty(value = "행사발주수량")
	private String poEventQty;

	@ApiModelProperty(value = "SEQ")
	private String ilPoTpId;

	@ApiModelProperty(value = "발주유형명")
	private String poTpNm;

	@ApiModelProperty(value = "발주 예정일")
	private String poScheduleDt;

	@ApiModelProperty(value = "입고 요청일")
	private String recevingReqDt;

	@ApiModelProperty(value = "행사 시작일")
	private String eventStartDt;

	@ApiModelProperty(value = "행사 종료일")
	private String eventEndDt;

	@ApiModelProperty(value = "판매처 아이디(FK)")
	private String omSellersId;

	@ApiModelProperty(value = "판매처")
	private String sellersNm;

	@ApiModelProperty(value = "판매처그룹")
	private String sellersGroupCd;

	@ApiModelProperty(value = "박스입수량")
	private String pcsPerBox;

	@ApiModelProperty(value = "UOM/OMS")
	private String oms;

	@ApiModelProperty(value = "사유")
	private String memoSplit;

	@ApiModelProperty(value = "사유")
	private String memo;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "수정일")
	private String modifyDt;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "일평균 주문수량")
	private int orderAvgCnt;

	@ApiModelProperty(value = "엑셀업로드 파일이름")
    private String fileNm;

    @ApiModelProperty(value = "성공여부")
    private String successYn;

    @ApiModelProperty(value = "메시지")
    private String msg;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "상품할인 일괄 업로드 유형(GOODS_DISP(상품할인 일괄업로드), EMPLOYEE_DISC(임직원할인 일괄업로드))")
    private String uploadTp;

    @ApiModelProperty(value = "성공갯수")
    private String successCnt;

    @ApiModelProperty(value = "실패갯수")
    private String failCnt;

    @ApiModelProperty(value = "로그ID")
    private String logId;

    @ApiModelProperty(value = "판매처코드")
    private String sellersCd;

    @ApiModelProperty(value = "SEQ")
    private String ilPoEventExcelUploadLogId;

    @ApiModelProperty(value = "등록자 ID")
	@UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "수정자정보")
    private String modifyId;

    @ApiModelProperty(value = "SEQ")
    private String ilPoEventExcelUploadDetlLogId;

    @ApiModelProperty(value = "사용자명")
	@UserMaskingUserName
    private String userNm;

    @ApiModelProperty(value = "수정자 아이디")
	@UserMaskingLoginId
    private String modifyLoginId;

    @ApiModelProperty(value = "수정자명")
	@UserMaskingUserName
    private String modifyUserNm;

    @ApiModelProperty(value = "등록자정보")
    private String createInfo;

	@ApiModelProperty(value = "차이수량 엑셀")
	private int diffCntEx;

}
