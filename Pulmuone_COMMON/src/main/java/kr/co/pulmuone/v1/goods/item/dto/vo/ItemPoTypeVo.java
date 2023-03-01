package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoTypeVo")
public class ItemPoTypeVo {

	@ApiModelProperty(value = "순번")
	private int rowNumber;

	@ApiModelProperty(value = "발주유형 PK")
	private String ilPoTpId;

	@ApiModelProperty(value = "공급업체 PK")
	private String urSupplierId;

	@ApiModelProperty(value = "업체 PK")
	private long urCompanyId;

	@ApiModelProperty(value = "업체이름")
	private String compNm;

	@ApiModelProperty(value = "공급업체 이름")
	private String supplierName;

	@ApiModelProperty(value = "발주유형명")
	private String poTpNm;

	@ApiModelProperty(value = "발주유형공통코드(PO_TYPE.MOVING:이동발주, PO_TYPE.PRODUCTION:생산발주)")
	private String poTp;

	@ApiModelProperty(value = "발주유형공통코드명")
	private String poTpName;

	@ApiModelProperty(value = "ERP 발주유형")
	private String erpPoTp;

	@ApiModelProperty(value = "ERP 발주유형명")
	private String erpPoTpName;

	@ApiModelProperty(value = "발주유형공통코드명")
	private String poTypeDicName;

	@ApiModelProperty(value = "물류 입고예정일")
	private Integer stockPlannedDays;

	@ApiModelProperty(value = "ERP 발주유형")
	private String erpPoType;

	@ApiModelProperty(value = "ERP 발주유형명")
	private String erpPoTypeName;

	@ApiModelProperty(value = "품목별 상이 여부(Y:품목별 상이)")
	private String poPerItemYn;

	@ApiModelProperty(value = "발주마감시간")
	private String poDeadline;

	@ApiModelProperty(value = "발주유형정렬")
	private String poTypeNum;

	@ApiModelProperty(value = "발주유형정렬")
	private String poType;

	@ApiModelProperty(value = "월요일-발주값")
	private String monValue;

	@ApiModelProperty(value = "화요일-발주값")
	private String tueValue;

	@ApiModelProperty(value = "수요일-발주값")
	private String wedValue;

	@ApiModelProperty(value = "목요일-발주값")
	private String thuValue;

	@ApiModelProperty(value = "금요일-발주값")
	private String friValue;

	@ApiModelProperty(value = "토요일-발주값")
	private String satValue;

	@ApiModelProperty(value = "일요일-발주값")
	private String sunValue;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "수정일")
	private String modifyDate;

	@ApiModelProperty(value = "등록일/수정일")
	private String createModifyDate;

	@ApiModelProperty(value = "월요일-발주값")
	private String checkSun;

	@ApiModelProperty(value = "화요일-발주값")
	private String checkMon;

	@ApiModelProperty(value = "수요일-발주값")
	private String checkTue;

	@ApiModelProperty(value = "목요일-발주값")
	private String checkWed;

	@ApiModelProperty(value = "금요일-발주값")
	private String checkThu;

	@ApiModelProperty(value = "토요일-발주값")
	private String checkFri;

	@ApiModelProperty(value = "일요일-발주값")
	private String checkSat;

	@ApiModelProperty(value = "입고예정일(일)")
	private int scheduledSun;

	@ApiModelProperty(value = "입고예정일(월)")
	private int scheduledMon;

	@ApiModelProperty(value = "입고예정일(화)")
	private int scheduledTue;

	@ApiModelProperty(value = "입고예정일(수)")
	private int scheduledWed;

	@ApiModelProperty(value = "입고예정일(목)")
	private int scheduledThu;

	@ApiModelProperty(value = "입고예정일(금)")
	private int scheduledFri;

	@ApiModelProperty(value = "입고예정일(토)")
	private int scheduledSat;


	@ApiModelProperty(value = "이동요청일(일)")
	private int moveReqSun;

	@ApiModelProperty(value = "이동요청일(월)")
	private int moveReqMon;

	@ApiModelProperty(value = "이동요청일(화)")
	private int moveReqTue;

	@ApiModelProperty(value = "이동요청일(수)")
	private int moveReqWed;

	@ApiModelProperty(value = "이동요청일(목)")
	private int moveReqThu;

	@ApiModelProperty(value = "이동요청일(금)")
	private int moveReqFri;

	@ApiModelProperty(value = "이동요청일(토)")
	private int moveReqSat;

	@ApiModelProperty(value = "PO요청일(일)")
	private int poReqSun;

	@ApiModelProperty(value = "PO요청일(월)")
	private int poReqMon;

	@ApiModelProperty(value = "PO요청일(화)")
	private int poReqTue;

	@ApiModelProperty(value = "PO요청일(수)")
	private int poReqWed;

	@ApiModelProperty(value = "PO요청일(목)")
	private int poReqThu;

	@ApiModelProperty(value = "PO요청일(금)")
	private int poReqFri;

	@ApiModelProperty(value = "PO요청일(토)")
	private int poReqSat;

	@ApiModelProperty(value = "발주마감시간(시)")
	private String poDeadlineHour;

	@ApiModelProperty(value = "발주마감시간(분)")
	private String poDeadlineMin;

	@ApiModelProperty(value = "입고요청일")
	private String recevingReqDt;

	@ApiModelProperty(value = "발주예정일")
	private String poScheduleDt;

	private String poSunYn;

	private String poMonYn;

	private String poTueYn;

	private String poWedYn;

	private String poThuYn;

	private String poFriYn;

	private String poSatYn;

	private String ilItemCd;

	private String templateYn;

	private String weekDay;

	private String poWeek;

	private String scheduledWeek;

	@ApiModelProperty(value = "사용자 로그인 정보")
	private UserVo userVo;

	@ApiModelProperty(value = "공급업체 코드")
	private String supplierCd;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createNm;

	@ApiModelProperty(value = "등록자 로그인 아이디")
	@UserMaskingLoginId
	private String createLoginId;

	@ApiModelProperty(value = "수정자명")
	@UserMaskingUserName
	private String modifyNm;

	@ApiModelProperty(value = "수정자 로그인 아이디")
	@UserMaskingLoginId
	private String modifyLoginId;
}
