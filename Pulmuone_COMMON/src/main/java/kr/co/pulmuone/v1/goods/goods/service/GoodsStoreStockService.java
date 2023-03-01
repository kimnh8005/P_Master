package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopOrderSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopStockSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsStoreStockMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ErpGoodsOrgaShopStockHeaderRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ErpGoodsOrgaShopStockResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemErpStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoIfTempVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStorePriceLogVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 올가 매장 상품 정보 조회 API 호출
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 06. 08.             천혜현         최초작성
* =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsStoreStockService {

    @Autowired
    ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private GoodsStoreStockMapper goodsStoreStockMapper;


    /**
     * @Desc ERP 올가매장상품정보 조회 API 호출
     * @return List<ErpIfOrgaShopStockSearchResponseDto>
     */
    protected List<ErpIfOrgaShopStockSearchResponseDto> getErpIfOrgaShopStock() throws BaseException {

        List<ErpIfOrgaShopStockSearchResponseDto> eachPageDtoList = null;                   // 각 페이지별 품목 dto 목록
        List<ErpIfOrgaShopStockSearchResponseDto> erpItemStockApiList = new ArrayList<>();  // 전체 취합된 품목 dto 목록

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("target","header-ITF");
        parameterMap.put("itfFlg","N");

        //조회 검색일
        LocalDate now = LocalDate.now(); //현재일자
        LocalDate nowMinusDay =  now.minusDays(1);
        String baseDt = now.toString().replaceAll("-", ""); //조회기간
        String updDt = nowMinusDay.toString().replaceAll("-", "");
        parameterMap.put("updDat_toChar_YYYYMMDD",updDt+"~"+baseDt);

        BaseApiResponseVo baseApiResponseVo = null;

        try{
            // 최초 1페이지 조회
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_ORGASHOP_STOCK_SRCH);

            if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopStockSearchResponseDto.class);
            erpItemStockApiList.addAll(eachPageDtoList);

            // 2페이지부터 조회
            if(baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1){
                for(int page = 2 ; page <= baseApiResponseVo.getTotalPage(); page++){
                    parameterMap.put("page",String.valueOf(page));

                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_ORGASHOP_STOCK_SRCH);

                    if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopStockSearchResponseDto.class);
                    erpItemStockApiList.addAll(eachPageDtoList);
                }
            }

        }catch(Exception e){
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        return erpItemStockApiList;
    }


    /**
     * @Desc ERP 올가매장상품정보 조회 API 호출
     * @param urStoreId
     * @param ilItemCd
     * @return List<ErpIfOrgaShopStockSearchResponseDto>
     */
    protected List<ErpIfOrgaShopStockSearchResponseDto> getErpIfOrgaShopStock(String urStoreId, String ilItemCd) throws BaseException {

        List<ErpIfOrgaShopStockSearchResponseDto> eachPageDtoList = null;                   // 각 페이지별 품목 dto 목록
        List<ErpIfOrgaShopStockSearchResponseDto> erpItemStockApiList = new ArrayList<>();  // 전체 취합된 품목 dto 목록

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("target","header-ITF");
        parameterMap.put("itfFlg","N");

        //조회 검색일
        LocalDate now = LocalDate.now(); //현재일자
        LocalDate nowMinusDay = now.minusDays(1);
        String baseDt = now.toString().replaceAll("-", ""); //조회기간
        String updDt = nowMinusDay.toString().replaceAll("-", "");
        parameterMap.put("updDat_toChar_YYYYMMDD",updDt+"~"+baseDt);

        BaseApiResponseVo baseApiResponseVo = null;

        // 검색조건에 매장코드, ERP품목코드 추가
        parameterMap.put("shpCd", urStoreId);
        parameterMap.put("itmNo", ilItemCd);

        try{
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_ORGASHOP_STOCK_SRCH);

            if (!baseApiResponseVo.isSuccess()) {
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopStockSearchResponseDto.class);
            erpItemStockApiList.addAll(eachPageDtoList);

        }catch(Exception e){
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }


        return erpItemStockApiList;
    }


    /**
     * @Desc ERP 올가매장주문정보 조회 API 호출
     * @return List<ErpIfOrgaShopOrderSearchResponseDto>
     */
    protected List<ErpIfOrgaShopOrderSearchResponseDto> getErpIfOrgaShopOrder() throws BaseException{

        List<ErpIfOrgaShopOrderSearchResponseDto> eachPageDtoList = null;                   // 각 페이지별 품목 dto 목록
        List<ErpIfOrgaShopOrderSearchResponseDto> erpItemOrderApiList = new ArrayList<>();  // 전체 취합된 품목 dto 목록

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("target","line-ITF_ORD");
        parameterMap.put("crpCd","ORGAOMS");
//        parameterMap.put("itfOrdFlg","N");

        //조회 검색일
        LocalDate now = LocalDate.now(); //현재일자
        LocalDate nowMinusDay =  now.minusDays(1);
        String baseDt = now.toString().replaceAll("-", ""); //조회기간
        String updDt = nowMinusDay.toString().replaceAll("-", "");
        parameterMap.put("sysDat_toChar_YYYYMMDD",updDt+"~"+baseDt);

        BaseApiResponseVo baseApiResponseVo = null;

        try{
            // 최초 1페이지 조회
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_CUSTORD_SRCH);

            if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopOrderSearchResponseDto.class);
            erpItemOrderApiList.addAll(eachPageDtoList);

            // 2페이지부터 조회
            if(baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1){
                for(int page = 2 ; page <= baseApiResponseVo.getTotalPage(); page++){
                    parameterMap.put("page",String.valueOf(page));

                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_CUSTORD_SRCH);

                    if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopOrderSearchResponseDto.class);
                    erpItemOrderApiList.addAll(eachPageDtoList);
                }
            }

        }catch(Exception e){
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        return erpItemOrderApiList;
    }


    /**
     * @Desc ERP 올가매장주문정보 조회 API 호출
     * @param urStoreId
     * @param ilItemCd
     * @return List<ErpIfOrgaShopOrderSearchResponseDto>
     */
    protected List<ErpIfOrgaShopOrderSearchResponseDto> getErpIfOrgaShopOrder(String urStoreId, String ilItemCd) throws BaseException{

        List<ErpIfOrgaShopOrderSearchResponseDto> eachPageDtoList = null;                   // 각 페이지별 품목 dto 목록
        List<ErpIfOrgaShopOrderSearchResponseDto> erpItemOrderApiList = new ArrayList<>();  // 전체 취합된 품목 dto 목록

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("target","line-ITF_ORD");
        parameterMap.put("crpCd","ORGAOMS");
//        parameterMap.put("itfOrdFlg","N");

        //조회 검색일
        LocalDate now = LocalDate.now(); //현재일자
        LocalDate nowMinusDay =  now.minusDays(1);
        String baseDt = now.toString().replaceAll("-", ""); //조회기간
        String updDt = nowMinusDay.toString().replaceAll("-", "");
        parameterMap.put("sysDat_toChar_YYYYMMDD",updDt+"~"+baseDt);

        BaseApiResponseVo baseApiResponseVo = null;

        // 검색조건에 매장코드, ERP품목코드 추가
        parameterMap.put("shpCd", urStoreId);
        parameterMap.put("itmNo", ilItemCd);

        try{
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_CUSTORD_SRCH);

            if (!baseApiResponseVo.isSuccess()) {
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopOrderSearchResponseDto.class);
            erpItemOrderApiList.addAll(eachPageDtoList);

        }catch(Exception e){
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        return erpItemOrderApiList;
    }



    /**
      * @Desc 올가매장상품조회 IF 정보 임시테이블 저장
      * @param itemStoreInfoIfTempVo
      * @return int
      */
    protected int addIlItemStoreInfoIfTemp(ItemStoreInfoIfTempVo itemStoreInfoIfTempVo) {
         return goodsStoreStockMapper.addIlItemStoreInfoIfTemp(itemStoreInfoIfTempVo);
    }

    /**
     * @Desc 올가매장상품조회 IF 정보 임시테이블 삭제
     * @return int
     */
    protected int delIlItemStoreInfoIfTemp() {
        return goodsStoreStockMapper.delIlItemStoreInfoIfTemp();
    }

    /**
     * @Desc 올가매장상품조회 IF 정보 임시테이블 저장위한 요청값 세팅
     * @param dto
     * @return ItemStoreInfoIfTempVo
     */
    protected ItemStoreInfoIfTempVo setIlItemStoreInfoIfTempRequestDto(ErpIfOrgaShopStockSearchResponseDto dto) {
        ItemStoreInfoIfTempVo orgaShopStockVo = new ItemStoreInfoIfTempVo();
        orgaShopStockVo.setIfSeq(dto.getIfSeq());
        orgaShopStockVo.setIlItemCd(dto.getItmNo());
        orgaShopStockVo.setShopCd(dto.getShpCd());
        if(dto.getCurCnt() < 0) {
            dto.setCurCnt(0);
        }
        orgaShopStockVo.setCurrentStock((int)Math.floor(dto.getCurCnt()));
        orgaShopStockVo.setRecommendedPrice(dto.getNorPrc());
        orgaShopStockVo.setSalePrice(dto.getSalPrc());
        return orgaShopStockVo;
    }

    /**
     * @Desc 매장품목정보 수정
     * @param itemStoreInfoVo
     * @return int
     */
    protected int putIlItemStoreInfo(ItemErpStoreInfoVo itemStoreInfoVo) {
        return goodsStoreStockMapper.putIlItemStoreInfo(itemStoreInfoVo);
    }

    /**
     * @Desc 매장품목가격 로그 추가
     * @param itemStorePriceLogVo
     * @return int
     */
    protected int addIlItemStorePriceLog(ItemStorePriceLogVo itemStorePriceLogVo) {
        return goodsStoreStockMapper.addIlItemStorePriceLog(itemStorePriceLogVo);
    }

    /**
     * @Desc 매장품목가격 로그 수정
     * @param itemStorePriceLogVo
     * @return int
     */
    protected int putIlItemStorePriceLog(ItemStorePriceLogVo itemStorePriceLogVo) {
        return goodsStoreStockMapper.putIlItemStorePriceLog(itemStorePriceLogVo);
    }

    /**
     * @Desc 미연동된 매장주문수량 조회
     * @param urStoreId
     * @param ilItemCd
     * @return List<ErpIfOrgaShopOrderSearchResponseDto>
     */
    protected List<ErpIfOrgaShopOrderSearchResponseDto> getOrgaShopNonIfOrderCount(String urStoreId, String ilItemCd) {
        return goodsStoreStockMapper.getOrgaShopNonIfOrderCount(urStoreId, ilItemCd);
    }

    /**
     * @Desc 올가매장상품 재고업데이트위한 requestDto 세팅
     * @param erpIfOrgaShopStockList
     * @param erpIfOrgaShopOrderCntList
     * @param mallOrgaShopNonIfOrderList
     * @return HashMap<String,ItemErpStoreInfoVo>
     */
    protected HashMap<String, ItemErpStoreInfoVo> setStoreStockReqestDto(List<ErpIfOrgaShopStockSearchResponseDto> erpIfOrgaShopStockList, List<ErpIfOrgaShopOrderSearchResponseDto> erpIfOrgaShopOrderCntList, List<ErpIfOrgaShopOrderSearchResponseDto> mallOrgaShopNonIfOrderList){
        HashMap<String,ItemErpStoreInfoVo> storeStockMap = new HashMap<>();

        // 1. 중계서버 재고
        for(ErpIfOrgaShopStockSearchResponseDto erpIfStockDto : erpIfOrgaShopStockList){

            //매장코드||품목코드 조합으로 key 생성
            String searchKey = erpIfStockDto.getShpCd() + "||" + erpIfStockDto.getItmNo();

            if(erpIfStockDto.getCurCnt() < 0){
                erpIfStockDto.setCurCnt(0);
            }

            // key가 있을경우 -> 해당 value update
            if(storeStockMap.containsKey(searchKey)){
                //ERP 연동재고
                storeStockMap.get(searchKey).setStoreIfStock(String.valueOf(erpIfStockDto.getCurCnt()));
            // key가 없을경우 -> 새로 insert
            }else{
                ItemErpStoreInfoVo stockVo = new ItemErpStoreInfoVo();
                stockVo.setIlItemCd(erpIfStockDto.getItmNo());
                stockVo.setUrStoreId(erpIfStockDto.getShpCd());
                stockVo.setStoreIfStock(String.valueOf(erpIfStockDto.getCurCnt()));
                stockVo.setStoreSalePrice(String.valueOf(erpIfStockDto.getSalPrc()));
                storeStockMap.put(searchKey,stockVo);
            }
        }

        // 2. 중계서버 미처리된 주문재고
        for(ErpIfOrgaShopOrderSearchResponseDto erpIfOrderDto : erpIfOrgaShopOrderCntList){

            //매장코드||품목코드 조합으로 key 생성
            String searchKey = erpIfOrderDto.getShpCd() + "||" + erpIfOrderDto.getItmNo();

            // key가 있을경우 -> 해당 value update
            if(storeStockMap.containsKey(searchKey)){

                // 중계서버 미처리된 주문
                storeStockMap.get(searchKey).setIfUnOrderCnt(erpIfOrderDto.getOrdCnt());

            // key가 없을경우 -> 새로 insert
            }else{
                ItemErpStoreInfoVo stockVo = new ItemErpStoreInfoVo();
                stockVo.setIlItemCd(erpIfOrderDto.getItmNo());
                stockVo.setUrStoreId(erpIfOrderDto.getShpCd());
                stockVo.setIfUnOrderCnt(erpIfOrderDto.getOrdCnt());
                storeStockMap.put(searchKey,stockVo);
            }
        }

        // 3. 샵풀무원 미연동된 매장주문재고
        for(ErpIfOrgaShopOrderSearchResponseDto mallOrderDto : mallOrgaShopNonIfOrderList){
            //매장코드||품목코드 조합으로 key 생성
            String searchKey = mallOrderDto.getShpCd() + "||" + mallOrderDto.getItmNo();

            // key가 있을경우 -> 해당 value update
            if(storeStockMap.containsKey(searchKey)){

                // 중계서버 미처리된 주문
                storeStockMap.get(searchKey).setMallUnOrderCnt(mallOrderDto.getOrdCnt());

                // key가 없을경우 -> 새로 insert
            }else{
                ItemErpStoreInfoVo stockVo = new ItemErpStoreInfoVo();
                stockVo.setIlItemCd(mallOrderDto.getItmNo());
                stockVo.setUrStoreId(mallOrderDto.getShpCd());
                stockVo.setMallUnOrderCnt(mallOrderDto.getOrdCnt());
                storeStockMap.put(searchKey,stockVo);
            }
        }

        return storeStockMap;
    }

    /**
     * @Desc 매장품목정보 미연동 재고 수정
     * @param itemStoreInfoList
     * @return int
     */
    protected int putIlItemStoreInfoForStock(List<ItemErpStoreInfoVo> itemStoreInfoList) {
        return goodsStoreStockMapper.putIlItemStoreInfoForStock(itemStoreInfoList);
    }

    /**
     * @Desc 매장품목가격 로그 추가
     * @param itemStoreInfoVo
     * @return int
     */
    protected void saveIlItemStorePriceLog(ItemErpStoreInfoVo itemStoreInfoVo) {

        // 로그 최초 등록
        if(StringUtils.isEmpty(itemStoreInfoVo.getBeforeStoreSalePrice()) && StringUtils.isNotEmpty(itemStoreInfoVo.getStoreSalePrice())){
            ItemStorePriceLogVo itemErpStorePriceLogVo = new ItemStorePriceLogVo();
            itemErpStorePriceLogVo.setIlItemCd(itemStoreInfoVo.getIlItemCd());
            itemErpStorePriceLogVo.setUrStoreId(itemStoreInfoVo.getUrStoreId());
            itemErpStorePriceLogVo.setStoreSalePrice(itemStoreInfoVo.getStoreSalePrice());
            itemErpStorePriceLogVo.setPriceChangeYn("N");
            goodsStoreStockMapper.addIlItemStorePriceLog(itemErpStorePriceLogVo);

            // 가격변동됐을 때 로그 수정
        }else if(StringUtils.isNotEmpty(itemStoreInfoVo.getBeforeStoreSalePrice()) && StringUtils.isNotEmpty(itemStoreInfoVo.getStoreSalePrice())
                && !itemStoreInfoVo.getBeforeStoreSalePrice().equals(itemStoreInfoVo.getStoreSalePrice())){

            // 기존 로그 수정
            ItemStorePriceLogVo putItemErpStorePriceLogVo = new ItemStorePriceLogVo();
            putItemErpStorePriceLogVo.setIlItemCd(itemStoreInfoVo.getIlItemCd());
            putItemErpStorePriceLogVo.setUrStoreId(itemStoreInfoVo.getUrStoreId());
            goodsStoreStockMapper.putIlItemStorePriceLog(putItemErpStorePriceLogVo);

            // 새로운 로그 추가
            ItemStorePriceLogVo itemErpStorePriceLogVo = new ItemStorePriceLogVo();
            itemErpStorePriceLogVo.setIlItemCd(itemStoreInfoVo.getIlItemCd());
            itemErpStorePriceLogVo.setUrStoreId(itemStoreInfoVo.getUrStoreId());
            itemErpStorePriceLogVo.setStoreSalePrice(itemStoreInfoVo.getStoreSalePrice());
            itemErpStorePriceLogVo.setPriceChangeYn("Y");
            goodsStoreStockMapper.addIlItemStorePriceLog(itemErpStorePriceLogVo);

        }

    }

    /**
     * @Desc 올가매장상품정보 조회 완료 API 호출
     * @param erpIfOrgaShopStockList
     * @return
     */
    protected void putErpIfOrgaShopStockFlag(List<ErpIfOrgaShopStockSearchResponseDto> erpIfOrgaShopStockList ) {

        BaseApiResponseVo baseApiResponseVo = null;

        // API 조회 완료
        if(erpIfOrgaShopStockList.size() > 0){
            ErpGoodsOrgaShopStockHeaderRequestDto requestDto = new ErpGoodsOrgaShopStockHeaderRequestDto();

            List<ErpGoodsOrgaShopStockResultVo> headerList = new ArrayList<>();
            for(ErpIfOrgaShopStockSearchResponseDto stockInfo : erpIfOrgaShopStockList){
                ErpGoodsOrgaShopStockResultVo stockVo = new ErpGoodsOrgaShopStockResultVo();
                stockVo.setShpCd(stockInfo.getShpCd());
                stockVo.setItmNo(stockInfo.getItmNo());
                stockVo.setItfFlg("N");
                headerList.add(stockVo);
            }
            requestDto.setHeader(headerList);

            baseApiResponseVo = erpApiExchangeService.put(requestDto, ApiConstants.IF_ORGASHOP_STOCK_FLAG);
        }
    }
    
    /**
     * 상품코드로 식품 선주문 상품인지 조회
     */
    protected boolean isStockPreOrderWithGoodsId(long ilGoodsId) {
        return goodsStoreStockMapper.getStockPreOrderWithGoodsId(ilGoodsId) > 0;
    }
    
}
