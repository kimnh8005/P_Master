package kr.co.pulmuone.v1.comm.mapper.order.regular;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 등록 SEQ Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderRegularRegistrationSeqMapper {

	/**
	 * 정기배송 주문신청 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularReqIdSeq() throws Exception;

	/**
	 * 정기배송 신청정보 생성
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
    String getOdRegularReqId(@Param(value = "odRegularReqId") long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 주문상세 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularReqOrderDetlIdSeq() throws Exception;

	/**
	 * 정기배송 정기배송 주문 신청 배송지 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularReqShippingZoneIdSeq() throws Exception;

	/**
	 * 정기배송 주문 신청 히스토리 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularReqHistoryIdSeq() throws Exception;

	/**
	 * 정기배송 주문 신청 메모 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularReqMemoIdSeq() throws Exception;

	/**
	 * 정기배송 주문 결과 SEQ
	 * @param
	 * @return
	 * @throws Exception
	 */
    long getOdRegularResultIdSeq() throws Exception;

    /**
     * 정기배송 주문 결과 상세 SEQ
     * @param
     * @return
     * @throws Exception
     */
    long getOdRegularResultDetlIdSeq() throws Exception;
}
