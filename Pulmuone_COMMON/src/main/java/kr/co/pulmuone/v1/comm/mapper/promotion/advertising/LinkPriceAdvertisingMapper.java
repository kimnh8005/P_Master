package kr.co.pulmuone.v1.comm.mapper.promotion.advertising;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.promotion.advertising.dto.AddAdvertisingExternalRequestDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface LinkPriceAdvertisingMapper {

    int insertLinkPriceByOrder(LinkPriceDto dto) throws Exception;

    /**
     * 링크프라이스 내역조회
     */
    Page<LinkPriceResultVo> getLinkPriceList(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이스 내역조회 엑셀 다운로드
     */
    List<LinkPriceExcelResultVo> getLinkPriceListExcel(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이스 내역조회 Total data 조회
     */
    LinkPriceTotalResultVo getLinkPriceListTotal(LinkPriceRequestDto dto) throws Exception;

    /**
     * 링크프라이스 주문 내역조회
     */
    List<LinkPriceOrderDetailVo> getLinkPriceOrderDetailList(long odOrderId) throws Exception;

    /**
     * 링크프라이스 update
     */
    int updateLinkPrice(LinkPriceDto dto) throws Exception;

    /**
     * 링크프라이스 주문 내역조회(링크프라이스 제공 API 용도) - 주문
     */
    List<LinkPriceOrderListAPIResponseDto> getLinkPriceOrderListForPaid(HashMap<String, String> orderInfoSearch) throws Exception;

    /**
     * 링크프라이스 주문 내역조회(링크프라이스 제공 API 용도) - 취소
     */
    List<LinkPriceOrderListAPIResponseDto> getLinkPriceOrderListForCanceled(HashMap<String, String> orderInfoSearch) throws Exception;

    /**
     * 가상계좌 링크프라이스 주문 내역조회
     */
    List<LinkPriceOrderDetailVo> getVirtualBankLinkPriceOrderDetailList(String orderId) throws Exception;
}
