package kr.co.pulmuone.v1.promotion.linkprice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * 풀무원
 * LinkPrice Service
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0       2021.07.19.           최용호         최초작성
 * =======================================================================
 * </PRE>
 */

public interface LinkPriceBiz {

    ApiResult<?> insertLinkPriceByOrder(LinkPriceDto linkPriceDto) throws Exception;

    /**
     * 링크프라이스 내역조회
     */
    ApiResult<?> getLinkPriceList(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이스 내역조회 엑셀 다운로드
     */
    ExcelDownloadDto getLinkPriceListExcel(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이스 내역조회 Total data 조회
     */
    ApiResult<?> getLinkPriceListTotal(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이 실적 결제정보 저장 및 전송
     */
    void saveAndSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception;

    /**
     * 가상계좌 실적 결제정보 저장
     */
    void virtualBankSaveLinkPrice(PgApprovalOrderDataDto orderData) throws Exception;

    /**
     * 가상계좌 실적 결제정보 전송
     */
    void virtualBankSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception;
}
