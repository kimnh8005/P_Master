package kr.co.pulmuone.v1.api.cjlogistics.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CJ상품택배접수 Dto")
public class CJLogisticsOrderAcceptDto extends BaseRequestDto
{

	@NotEmpty(message = "고객ID는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "고객ID는 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "고객ID", required = true)
	private String 	custId;

	@NotEmpty(message = "접수일자는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 9, message = "접수일자는 1 ~ 9 Byte 이여야 합니다!")
	@ApiModelProperty(value = "접수일자 YYYMMDD", required = true)
	private String 	rcptYmd;

	@NotEmpty(message = "고객사용번호는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 100, message = "고객사용번호는 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "고객사용번호 기업고객이 관리하는 주문번호/영수번호 등 내부 관리번호", required = true)
	private String 	custUseNo;

	@NotEmpty(message = "접수구분은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "접수구분은 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "접수구분 01: 일반, 02: 반품", required = true)
	private String 	rcptDv;

	@NotEmpty(message = "작업구분코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "작업구분코드는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "작업구분코드 01: 일반, 02 : 교환, 03 : A/S", required = true)
	private String 	workDvCd;

	@NotEmpty(message = "요청구분코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "요청구분코드는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "요청구분코드 01: 요청, 02: 취소", required = true)
	private String 	reqDvCd;

	@NotEmpty(message = "합포장키는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 100, message = "합포장키는 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "합포장키 다수데이터를 한 송장에 출력할 경우 처리 (합포 없는 경우 YYYYMMDD_고객ID_고객사용번호 or YYYYMMDD_고객ID_운송장번호)", required = true)
	private String 	mpckKey;

	@NotEmpty(message = "합포장순번은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "합포장순번은 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "합포장순번 합포장 처리건수가 다수일 경우 SEQ처리를 수행한다.(합포 없는 경우 무조건 1 )", required = true)
	private int    	mpckSeq;

	@NotEmpty(message = "정산구분코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "정산구분코드는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "정산구분코드	01: 계약 운임,  02: 자료 운임 (계약운임인지 업체에서 넣어주는 운임으로할지)", required = true)
	private String 	calDvCd;

	@NotEmpty(message = "운임구분코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "운임구분코드는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "운임구분코드	01: 선불,  02: 착불 ,  03: 신용", required = true)
	private String 	frtDvCd;

	@NotEmpty(message = "계약품목코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 50, message = "계약품목코드는 1 ~ 50 Byte 이여야 합니다!")
	@ApiModelProperty(value = "계약품목코드 01: 일반 품목", required = true)
	private String 	cntrItemCd;

	@NotEmpty(message = "박스타입코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "박스타입코드는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "박스타입코드 01: 극소,  02: 소,  03: 중,  04: 대,  05: 특대", required = true)
	private String 	boxTypeCd;

	@NotEmpty(message = "택배 박스 수량은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 11, message = "택배 박스 수량은 1 ~ 11 Byte 이여야 합니다!")
	@ApiModelProperty(value = "택배 박스 수량", required = true)
	private int 	boxQty;

	@ApiModelProperty(value = "운임적용구분이 자료 운임일 경우 등록 처리")
	private int 	frt;

	@NotEmpty(message = "고객관리거래처코드는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 30, message = "고객관리거래처코드는 1 ~ 30 Byte 이여야 합니다!")
	@ApiModelProperty(value = "고객관리거래처코드 주관사 관리 협력업체 코드 혹은 택배사 관리 업체코드", required = true)
	private String 	custMgmtDlcmCd;

	@NotEmpty(message = "송화인명은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 100, message = "송화인명은 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인명", required = true)
	private String 	sendrNm;

	@NotEmpty(message = "송화인전화번호1는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "송화인전화번호1는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인전화번호1", required = true)
	private String 	sendrTelNo1;

	@NotEmpty(message = "송화인전화번호2는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "송화인전화번호2는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인전화번호2", required = true)
	private String 	sendrTelNo2;

	@NotEmpty(message = "송화인전화번호3는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "송화인전화번호3는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인전화번호3, required = true")
	private String 	sendrTelNo3;

	@ApiModelProperty(value = "송화인휴대폰번호1")
	private String 	sendrCellNo1;

	@ApiModelProperty(value = "송화인휴대폰번호2")
	private String 	sendrCellNo2;

	@ApiModelProperty(value = "송화인휴대폰번호3")
	private String 	sendrCellNo3;

	@ApiModelProperty(value = "송화인안심번호1")
	private String 	sendrSafeNo1;

	@ApiModelProperty(value = "송화인안심번호2")
	private String 	sendrSafeNo2;

	@ApiModelProperty(value = "송화인안심번호3")
	private String 	sendrSafeNo3;

	@NotEmpty(message = "송화인우편번호는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 6, message = "송화인우편번호는 1 ~ 6 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인우편번호", required = true)
	private String 	sendrZipNo;

	@NotEmpty(message = "송화인주소는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max =100, message = "송화인주소는 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인주소", required = true)
	private String 	sendrAddr;

	@NotEmpty(message = "송화인상세주소는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 300, message = "송화인상세주소는 1 ~ 300 Byte 이여야 합니다!")
	@ApiModelProperty(value = "송화인상세주소", required = true)
	private String 	sendrDetailAddr;

	@NotEmpty(message = "수화인명은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 100, message = "수화인명은 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인명", required = true)
	private String 	rcvrNm;

	@NotEmpty(message = "수화인전화번호1는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "수화인전화번호1는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인전화번호1", required = true)
	private String 	rcvrTelNo1;

	@NotEmpty(message = "수화인전화번호2는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "수화인전화번호2는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인전화번호2", required = true)
	private String 	rcvrTelNo2;

	@NotEmpty(message = "수화인전화번호3는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 4, message = "수화인전화번호3는 1 ~ 4 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인전화번호3", required = true)
	private String 	rcvrTelNo3;

	@ApiModelProperty(value = "수화인휴대폰번호1")
	private String 	rcvrCellNo1;

	@ApiModelProperty(value = "수화인휴대폰번호2")
	private String 	rcvrCellNo2;

	@ApiModelProperty(value = "수화인휴대폰번호3")
	private String 	rcvrCellNo3;

	@ApiModelProperty(value = "수화인안심번호1")
	private String 	rcvrSafeNo1;

	@ApiModelProperty(value = "수화인안심번호2")
	private String 	rcvrSafeNo2;

	@ApiModelProperty(value = "수화인안심번호3")
	private String 	rcvrSafeNo3;

	@ApiModelProperty(value = "수화인우편번호")
	private String 	rcvrZipNo;

	@NotEmpty(message = "수화인주소는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 100, message = "수화인주소는 1 ~ 100 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인주소", required = true)
	private String 	rcvrAddr;

	@NotEmpty(message = "수화인상세주소는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 300, message = "수화인상세주소는 1 ~ 300 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수화인상세주소", required = true)
	private String 	rcvrDetailAddr;

	@ApiModelProperty(value = "주문자명")
	private String 	ordrrNm;

	@ApiModelProperty(value = "주문자전화번호1")
	private String 	ordrrTelNo1;

	@ApiModelProperty(value = "주문자전화번호2")
	private String 	ordrrTelNo2;

	@ApiModelProperty(value = "주문자전화번호3")
	private String 	ordrrTelNo3;

	@ApiModelProperty(value = "주문자휴대폰번호1")
	private String 	ordrrCellNo1;

	@ApiModelProperty(value = "주문자휴대폰번호2")
	private String 	ordrrCellNo2;

	@ApiModelProperty(value = "주문자휴대폰번호3")
	private String 	ordrrCellNo3;

	@ApiModelProperty(value = "주문자안심번호1")
	private String 	ordrrSafeNo1;

	@ApiModelProperty(value = "주문자안심번호2")
	private String 	ordrrSafeNo2;

	@ApiModelProperty(value = "주문자안심번호3")
	private String 	ordrrSafeNo3;

	@ApiModelProperty(value = "주문자우편번호")
	private String 	ordrrZipNo;

	@ApiModelProperty(value = "주문자주소")
	private String 	ordrrAddr;

	@ApiModelProperty(value = "주문자상세주소")
	private String 	ordrrDetailAddr;

	@ApiModelProperty(value = "운송장번호 (12자리)")
	private String 	invcNo;

	@ApiModelProperty(value = "원운송장번호")
	private String 	oriInvcNo;

	@ApiModelProperty(value = "원주문번호")
	private String 	oriOrdNo;

	@ApiModelProperty(value = "집화예정일자")
	private String 	colctExpctYmd;

	@ApiModelProperty(value = "집화예정시간")
	private String 	colctExpctHour;

	@ApiModelProperty(value = "배송예정일자")
	private String 	shipExpctYmd;

	@ApiModelProperty(value = "배송예정시간")
	private String 	shipExpctHour;

	@NotEmpty(message = "출력상태는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 3, message = "출력상태는 1 ~ 3 Byte 이여야 합니다!")
	@ApiModelProperty(value = "출력상태	01: 미출력,  02: 선출력,  03: 선발번 (반품은 선발번이 없음)", required = true)
	private String 	prtSt;

	@ApiModelProperty(value = "물품가액")
	private int 	articleAmt;

	@ApiModelProperty(value = "배송메세지1(비고)")
	private String 	remark1;

	@ApiModelProperty(value = "배송메세지2(송화인비고)")
	private String 	remark2;

	@ApiModelProperty(value = "배송메세지3(수화인비고)")
	private String 	remark3;

	@ApiModelProperty(value = "COD여부 대면결제 서비스 업체의 경우 대면결제 발생시 Y로 셋팅")
	private String 	codYn;

	@ApiModelProperty(value = "상품코드")
	private String 	gdsCd;

	@NotEmpty(message = "상품명은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "상품명은 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "상품명", required = true)
	private String 	gdsNm;

	@ApiModelProperty(value = "상품수량")
	private int 	gdsQty;

	@ApiModelProperty(value = "단품코드")
	private String 	unitCd;

	@ApiModelProperty(value = "단품명")
	private String 	unitNm;

	@ApiModelProperty(value = "상품가액")
	private int 	gdsAmt;

	@ApiModelProperty(value = "기타1")
	private String 	etc1;

	@ApiModelProperty(value = "기타2")
	private String 	etc2;

	@ApiModelProperty(value = "기타3")
	private String 	etc3;

	@ApiModelProperty(value = "기타4")
	private String 	etc4;

	@ApiModelProperty(value = "기타5")
	private String 	etc5;

	@NotEmpty(message = "택배구분은 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "택배구분은 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "택배구분	택배 : '01', 중량물(설치물류) : '02', 중량물(비설치물류) : '03'", required = true)
	private String 	dlvDv;

	@NotEmpty(message = "접수에러여부는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "접수에러여부는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "접수에러여부	DEFAULT : 'N'", required = true)
	private String 	rcptErrYn;

	@ApiModelProperty(value = "접수에러메세지")
	private String 	rcptErrMsg;

	@NotEmpty(message = "EAI전송상태는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 2, message = "EAI전송상태는 1 ~ 2 Byte 이여야 합니다!")
	@ApiModelProperty(value = "EAI전송상태		DEFAULT : '01'", required = true)
	private String 	eaiPrgsSt;

	@ApiModelProperty(value = "에러메세지")
	private String 	eaiErrMsg;

	@NotEmpty(message = "등록사원ID는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "등록사원ID는 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "등록사원ID", required = true)
	private String 	regEmpId;

	@NotEmpty(message = "등록일시는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "등록일시는 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "등록일시", required = true)
	private String 	regDtime;

	@NotEmpty(message = "수정사원ID는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "수정사원ID는 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수정사원ID", required = true)
	private String 	modiEmpId;

	@NotEmpty(message = "수정일시는 빈값을 입력 할 수 없습니다.")
	@Size(min = 1, max = 20, message = "수정일시는 1 ~ 20 Byte 이여야 합니다!")
	@ApiModelProperty(value = "수정일시", required = true)
	private String 	modiDtime;
}
