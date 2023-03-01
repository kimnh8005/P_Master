package kr.co.pulmuone.v1.pg.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.EscrowRegistDeliveryDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptCancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueResponseDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Getter
public abstract class PgAbstractService<T, E> {

	/**
	 * get 결제 요청 form data list
	 *
	 * @return
	 * @throws Exception
	 */
	abstract public PgServiceType getServiceType() throws Exception;

	/**
	 * get 결제 요청 form data list
	 *
	 * @return
	 * @throws Exception
	 */
	abstract public BasicDataResponseDto getBasicData(BasicDataRequestDto reqDto) throws Exception;

	/**
	 * 가상계좌 정보 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	abstract public VirtualAccountDataResponseDto getVirtualAccountData(BasicDataRequestDto reqDto) throws Exception;

	/**
	 * 결제 승인
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	abstract public E approval(T reqDto) throws Exception;

	/**
	 * 취소
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	abstract public CancelResponseDto cancel(String pgAccountType, CancelRequestDto reqDto) throws Exception;

	/**
	 * 현금영수증 발행
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	abstract public ReceiptIssueResponseDto receiptIssue(ReceiptIssueRequestDto reqDto) throws Exception;

	/**
	 * 현금영수증 취소
	 *
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	abstract public ReceiptCancelResponseDto receiptCancel(String tid) throws Exception;

	/**
	 * 현금영수증 취소
	 *
	 * @param tid
	 * @return
	 * @throws Exception
	 */
	abstract public ReceiptCancelResponseDto receiptCancel(String tid, String ip) throws Exception;

	/**
	 * 기타 정보 (etcData) dto 를 String 으로 변환
	 *
	 * @return
	 * @throws Exception
	 */
	public String toStringEtcData(Object obj) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(obj).getBytes());
	}

	/**
	 * 기타 정보 (etcData) 를 dto로 변환
	 *
	 * @param <Z>
	 * @param jsonString
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public <Z> Z toDtoEtcData(String jsonString, Class<Z> obj) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(Base64.getDecoder().decode(jsonString), obj);
	}

	/**
	 * jsonString 을 dto로 변환
	 *
	 * @param <Z>
	 * @param jsonString
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public <Z> Z toDtoJsonString(String jsonString, Class<Z> obj) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonString, obj);
	}

	/**
	 * 에스크로 배송 등록 처리
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	abstract public boolean escrowRegistDelivery(EscrowRegistDeliveryDataRequestDto reqDto) throws Exception;
}
