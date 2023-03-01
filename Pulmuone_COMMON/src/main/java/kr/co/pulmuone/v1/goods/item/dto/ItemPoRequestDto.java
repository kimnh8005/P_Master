package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoRequestDto")
public class ItemPoRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "행사발주 아이디(SEQ)")
    private String ilPoEventId;

	@ApiModelProperty(value = "단일_복수조건검색")
	private String selectConditionType;

	@ApiModelProperty(value = "코드검색값List")
	private List<String> searchItemCdArray;

	@ApiModelProperty(value = "코드검색값")
	private String searchItemCd;

	@ApiModelProperty(value = "행사시작일 검색값")
	private String searchEventStartDt;

	@ApiModelProperty(value = "행사종료일 검색값")
	private String searchEventEndDt;

	@ApiModelProperty(value = "판매처 검색값")
	private String sellersDetail;

	@ApiModelProperty(value = "출고처 검색값")
	private String warehouseId;

	@ApiModelProperty(value = "검색어 조회 타입")
	private String searchNameType;

	@ApiModelProperty(value = "검색어 조회 ")
	private String searchName;

	@ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

	@ApiModelProperty(value = "품목코드")
    private String ilItemCd;

	@ApiModelProperty(value = "품목명")
    private String itemNm;

	@ApiModelProperty(value = "박스입수량")
    private String pcsPerBox;

	@ApiModelProperty(value = "출고처명")
    private String warehouseName;

	@ApiModelProperty(value = "발주유형")
    private String poTpNm;

	@ApiModelProperty(value = "행사발주수량")
    private String poEventQty;

	@ApiModelProperty(value = "입고 요청일")
    private String recevingReqDt;

	@ApiModelProperty(value = "행사시작일")
    private String eventStartDt;

	@ApiModelProperty(value = "행사종료일")
    private String eventEndDt;

	@ApiModelProperty(value = "발주 예정일")
	private String poScheduleDt;

	@ApiModelProperty(value = "판매처 그룹")
    private String inputSellersGroup;

	@ApiModelProperty(value = "판매처")
    private String inputSellersDetail;

	@ApiModelProperty(value = "사유")
    private String memo;

	@ApiModelProperty(value = "업로드")
	private String upload;

	@ApiModelProperty(value = "업로드 리스트")
	private List<ItemPoRequestVo> uploadList;

	@ApiModelProperty(value = "성공갯수")
	private int successCnt;

	@ApiModelProperty(value = "실패갯수")
	private int failCnt;

	@ApiModelProperty(value = "업로드 LogId")
	private String logId;

	@ApiModelProperty(value = "검색시작일자")
	private String startDate;

	@ApiModelProperty(value = "검색종료일자")
	private String endDate;

	@ApiModelProperty(value = "입고요청일 범위")
	private String searchRecevingType;

	@ApiModelProperty(value = "행사종료일 범위")
	private String searchEventType;

	@ApiModelProperty(value = "관리자")
	private String searchUserId;

	@ApiModelProperty(value = "공급업체")
	private String supplierId;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

}
