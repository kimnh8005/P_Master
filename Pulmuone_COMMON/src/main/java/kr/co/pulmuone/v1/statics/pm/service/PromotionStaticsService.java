package kr.co.pulmuone.v1.statics.pm.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.statics.pm.PromotionStaticsMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsRequestDto;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsResponseDto;
import kr.co.pulmuone.v1.statics.pm.dto.vo.PromotionStaticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionStaticsService {

    private final PromotionStaticsMapper promotionStaticsMapper;

    /**
     * 내부광고코드별 매출현황통계 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {

        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getContentCd())) {
            List<String> filterStr = Stream.of(dto.getContentCd().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setContentCd(filterStr.get(1));
        }

        // # 조회
        List<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsInternalAdvertisingList(dto);

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // 합계 row 추가
        if (resultList != null && resultList.size() > 0) {

            long paidPrice = 0l, orderCnt = 0l, orderUnitPrice = 0l, userCnt = 0l, userUnitPrice = 0l;

            for (PromotionStaticsVo vo : resultList) {
                paidPrice += vo.getPaidPrice();
                orderCnt += vo.getOrderCnt();
                orderUnitPrice += vo.getOrderUnitPrice();
                userCnt += vo.getUserCnt();
                userUnitPrice += vo.getUserUnitPrice();
            }

            PromotionStaticsVo addRowVo = new PromotionStaticsVo();
            addRowVo.setPageNm("합계");
            addRowVo.setContentNm("");
            addRowVo.setPaidPrice(paidPrice);
            addRowVo.setPaidPriceFm(numberFormat(paidPrice));
            addRowVo.setOrderCntFm(numberFormat(orderCnt));
            addRowVo.setOrderCnt(orderCnt);
            addRowVo.setOrderUnitPriceFm(numberFormat(orderUnitPrice));
            addRowVo.setOrderUnitPrice(orderUnitPrice);
            addRowVo.setUserCntFm(numberFormat(userCnt));
            addRowVo.setUserCnt(userCnt);
            addRowVo.setUserUnitPriceFm(numberFormat(userUnitPrice));
            addRowVo.setUserUnitPrice(userUnitPrice);

            resultList.add(addRowVo);
        }

        result.setRows(resultList);
        result.setTotal(resultList.size());

        return result;
    }


    /**
     * 외부광고코드별 매출현황 통계 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException {

        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getMedium())) {
            List<String> filterStr = Stream.of(dto.getMedium().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
        }
        if (!StringUtil.isEmpty(dto.getCampaign())) {
            List<String> filterStr = Stream.of(dto.getCampaign().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setCampaign(filterStr.get(2));
        }
        if (!StringUtil.isEmpty(dto.getContent())) {
            List<String> filterStr = Stream.of(dto.getContent().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setContent(filterStr.get(3));
        }

        // # 조회
        List<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsAdvertisingList(dto);

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // 합계 row 추가
        if (resultList != null && resultList.size() > 0) {

            long paidPrice = 0l, orderCnt = 0l, orderUnitPrice = 0l, userCnt = 0l, userUnitPrice = 0l;

            for (PromotionStaticsVo vo : resultList) {
                paidPrice += vo.getPaidPrice();
                orderCnt += vo.getOrderCnt();
                orderUnitPrice += vo.getOrderUnitPrice();
                userCnt += vo.getUserCnt();
                userUnitPrice += vo.getUserUnitPrice();
            }

            PromotionStaticsVo addRowVo = new PromotionStaticsVo();
            addRowVo.setSource("합계");
            addRowVo.setMedium("");
            addRowVo.setCampaign("");
            addRowVo.setContent("");
            addRowVo.setPaidPriceFm(numberFormat(paidPrice));
            addRowVo.setPaidPrice(paidPrice);
            addRowVo.setOrderCntFm(numberFormat(orderCnt));
            addRowVo.setOrderCnt(orderCnt);
            addRowVo.setOrderUnitPriceFm(numberFormat(orderUnitPrice));
            addRowVo.setOrderUnitPrice(orderUnitPrice);
            addRowVo.setUserCntFm(numberFormat(userCnt));
            addRowVo.setUserCnt(userCnt);
            addRowVo.setUserUnitPriceFm(numberFormat(userUnitPrice));
            addRowVo.setUserUnitPrice(userUnitPrice);

            resultList.add(addRowVo);
        }

        result.setRows(resultList);
        result.setTotal(resultList.size());

        return result;
    }



    /**
     * 쿠폰 별 매출현황 통계 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException {

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // # 조회
        Page<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsCouponSaleStatusList(dto);

        Page<PromotionStaticsVo> resultListAdd = new Page<PromotionStaticsVo>();
        if (resultList != null && resultList.size() > 0) {
            for (int i=0 ;i< resultList.size(); i++) {
                String useStr = "";

                if("Y".equals(resultList.get(i).getUsePcYn())){
                    useStr += StaticsEnums.useType.PC.getCodeName() + ',';
                }
                if("Y".equals(resultList.get(i).getUseMoWebYn())){
                    useStr += StaticsEnums.useType.MOBILE.getCodeName() + ',';
                }else{
                    if("N".equals(resultList.get(i).getUsePcYn())) {
                        useStr = StringUtils.chop(useStr);
                    }
                }
                if("Y".equals(resultList.get(i).getUseMoAppYn())){
                    useStr += StaticsEnums.useType.APP.getCodeName();
                }else{
                    useStr = StringUtils.chop(useStr);
                }
                resultList.get(i).setUseStr(useStr);
                resultListAdd.add(resultList.get(i));
            }
        }

        result.setRows(resultListAdd);
        result.setTotal(resultListAdd.size());

        return result;
    }



    /**
     * 회원등급 코폰현황 통계 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getFindYear()) && !StringUtil.isEmpty(dto.getFindMonth())) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.of(Integer.parseInt(dto.getFindYear()), Integer.parseInt(dto.getFindMonth()), 1);
            dto.setSearchDateStart(localDate.format(dateTimeFormatter));
            dto.setSearchDateEnd(DateUtil.getLastDaysFormat(localDate, dateTimeFormatter));
        }

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // # 조회
        Page<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsUserGroupCouponStatusList(dto);

        // 합계 row 추가
        if (resultList != null && resultList.size() > 0) {

            long issueCnt = 0l, issuePrice = 0l, useCnt = 0l, usePrice = 0l, expirationCnt = 0l, expirationPrice = 0l;

            for (PromotionStaticsVo vo : resultList) {
                issueCnt += vo.getIssueCnt();
                issuePrice += vo.getIssuePrice();
                useCnt += vo.getUseCnt();
                usePrice += vo.getUsePrice();
                expirationCnt += vo.getExpirationCnt();
                expirationPrice += vo.getExpirationPrice();
            }

            PromotionStaticsVo addRowVo = new PromotionStaticsVo();
            addRowVo.setGroupMasterNm("합계");
            addRowVo.setIssueCnt(issueCnt);
            addRowVo.setIssueCntFm(numberFormat(issueCnt));
            addRowVo.setIssuePrice(issuePrice);
            addRowVo.setIssuePriceFm(numberFormat(issuePrice));
            addRowVo.setUseCnt(useCnt);
            addRowVo.setUseCntFm(numberFormat(useCnt));
            addRowVo.setUsePriceFm(numberFormat(usePrice));
            addRowVo.setUsePrice(usePrice);
            addRowVo.setExpirationCnt(expirationCnt);
            addRowVo.setExpirationCntFm(numberFormat(expirationCnt));
            addRowVo.setExpirationPrice(expirationPrice);
            addRowVo.setExpirationPriceFm(numberFormat(expirationPrice));

            resultList.add(addRowVo);
        }

        result.setRows(resultList);
        result.setTotal(resultList.size());

        return result;
    }


    /**
     * 내부광고코드별 매출현황통계 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException {
        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getFindYear()) && !StringUtil.isEmpty(dto.getFindMonth())) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.of(Integer.parseInt(dto.getFindYear()), Integer.parseInt(dto.getFindMonth()), 1);
            dto.setSearchDateStart(localDate.format(dateTimeFormatter));
            dto.setSearchDateEnd(DateUtil.getLastDaysFormat(localDate, dateTimeFormatter));
        }

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // # 조회
        Page<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsPointStatusList(dto);

        result.setRows(resultList);
        result.setTotal(resultList.size());

        return result;
    }

    /**
     * 내부광고 코드관리 단건 수정
     *
     * @param dto AddAdvertisingExternalRequestDto
     * @return GetCodeListResponseDto
     */
    protected PromotionStaticsResponseDto getAdvertisingType(PromotionStaticsRequestDto dto) throws Exception {
        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();
        result.setRows(promotionStaticsMapper.getAdvertisingType(dto));
        return result;
    }


    private String numberFormat(long inValue) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(inValue);
    }

    /**
     * 외부광고코드별 매출현황 통계 상품별 리스트 조회
     *
     * @param dto PromotionStaticsRequestDto
     * @return PromotionStaticsResponseDto
     * @throws BaseException
     */
    protected PromotionStaticsResponseDto getStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException {
        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getMedium())) {
            List<String> filterStr = Stream.of(dto.getMedium().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
        }
        if (!StringUtil.isEmpty(dto.getCampaign())) {
            List<String> filterStr = Stream.of(dto.getCampaign().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setCampaign(filterStr.get(2));
        }
        if (!StringUtil.isEmpty(dto.getContent())) {
            List<String> filterStr = Stream.of(dto.getContent().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setContent(filterStr.get(3));
        }

        // # 조회
        List<PromotionStaticsVo> resultList = promotionStaticsMapper.getStaticsAdvertisingGoodsList(dto);

        PromotionStaticsResponseDto result = new PromotionStaticsResponseDto();

        // 합계 row 추가
        if (resultList != null && resultList.size() > 0) {
            long paidPrice = 0l, orderCnt = 0l, orderUnitPrice = 0l, userCnt = 0l, userUnitPrice = 0l;

            for (PromotionStaticsVo vo : resultList) {
                paidPrice += vo.getPaidPrice();
                orderCnt += vo.getOrderCnt();
                orderUnitPrice += vo.getOrderUnitPrice();
                userCnt += vo.getUserCnt();
                userUnitPrice += vo.getUserUnitPrice();
            }

            PromotionStaticsVo addRowVo = new PromotionStaticsVo();
            addRowVo.setSource("합계");
            addRowVo.setMedium("");
            addRowVo.setCampaign("");
            addRowVo.setContent("");
            addRowVo.setPakageGoodsId("");
            addRowVo.setPakageGoodsNm("");
            addRowVo.setIlGoodsId("");
            addRowVo.setGoodsNm("");
            addRowVo.setPaidPriceFm(numberFormat(paidPrice));
            addRowVo.setPaidPrice(paidPrice);
            addRowVo.setOrderCntFm(numberFormat(orderCnt));
            addRowVo.setOrderCnt(orderCnt);
            addRowVo.setOrderUnitPriceFm(numberFormat(orderUnitPrice));
            addRowVo.setOrderUnitPrice(orderUnitPrice);
            addRowVo.setUserCntFm(numberFormat(userCnt));
            addRowVo.setUserCnt(userCnt);
            addRowVo.setUserUnitPriceFm(numberFormat(userUnitPrice));
            addRowVo.setUserUnitPrice(userUnitPrice);

            resultList.add(addRowVo);
        }

        result.setRows(resultList);
        result.setTotal(resultList.size());

        return result;
    }
}
