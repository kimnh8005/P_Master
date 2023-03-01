package kr.co.pulmuone.v1.statics.outbound.service;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.statics.outbound.OutboundStaticsMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.MissOutboundStaticsVo;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.OutboundStaticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 통계관리 출고통계 COMMON Service
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.04.07.              dgyoun         최초작성
 * 1.1       2021.05.12              이원호          수정사항 반영-기획변경 적용
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboundStaticsService {

    private final OutboundStaticsMapper outboundStaticsMapper;

    /**
     * 출고 통계 리스트 조회
     *
     * @param dto OutboundStaticsRequestDto
     * @return OutboundStaticsResponseDto
     * @throws BaseException
     */
    protected OutboundStaticsResponseDto getOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException {
        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getFindYear()) && !StringUtil.isEmpty(dto.getFindMonth())) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.of(Integer.parseInt(dto.getFindYear()), Integer.parseInt(dto.getFindMonth()), 1);
            dto.setSearchDateStart(localDate.format(dateTimeFormatter));
            dto.setSearchDateEnd(DateUtil.getLastDaysFormat(localDate, dateTimeFormatter));
        }
        dto.setSupplierList(StringUtil.getArrayListWithoutAll(dto.getSupplierFilter()));
        dto.setStorageMethodList(StringUtil.getArrayListWithoutAll(dto.getStorageMethodFilter()));

        // # 조회
        List<OutboundStaticsVo> resultList = outboundStaticsMapper.getOutboundStaticsList(dto);

        // 총계 row 추가
        if (resultList != null && resultList.size() > 0) {
            // 출고
            int sumDay1 = 0, sumDay2 = 0, sumDay3 = 0, sumDay4 = 0, sumDay5 = 0;
            int sumDay6 = 0, sumDay7 = 0, sumDay8 = 0, sumDay9 = 0, sumDay10 = 0;
            int sumDay11 = 0, sumDay12 = 0, sumDay13 = 0, sumDay14 = 0, sumDay15 = 0;
            int sumDay16 = 0, sumDay17 = 0, sumDay18 = 0, sumDay19 = 0, sumDay20 = 0;
            int sumDay21 = 0, sumDay22 = 0, sumDay23 = 0, sumDay24 = 0, sumDay25 = 0;
            int sumDay26 = 0, sumDay27 = 0, sumDay28 = 0, sumDay29 = 0, sumDay30 = 0;
            int sumDay31 = 0, sumSumCnt = 0, sumAvgCnt = 0;

            for (OutboundStaticsVo vo : resultList) {
                sumDay1 += vo.getDay1Cnt();
                sumDay2 += vo.getDay2Cnt();
                sumDay3 += vo.getDay3Cnt();
                sumDay4 += vo.getDay4Cnt();
                sumDay5 += vo.getDay5Cnt();
                sumDay6 += vo.getDay6Cnt();
                sumDay7 += vo.getDay7Cnt();
                sumDay8 += vo.getDay8Cnt();
                sumDay9 += vo.getDay9Cnt();
                sumDay10 += vo.getDay10Cnt();
                sumDay11 += vo.getDay11Cnt();
                sumDay12 += vo.getDay12Cnt();
                sumDay13 += vo.getDay13Cnt();
                sumDay14 += vo.getDay14Cnt();
                sumDay15 += vo.getDay15Cnt();
                sumDay16 += vo.getDay16Cnt();
                sumDay17 += vo.getDay17Cnt();
                sumDay18 += vo.getDay18Cnt();
                sumDay19 += vo.getDay19Cnt();
                sumDay20 += vo.getDay20Cnt();
                sumDay21 += vo.getDay21Cnt();
                sumDay22 += vo.getDay22Cnt();
                sumDay23 += vo.getDay23Cnt();
                sumDay24 += vo.getDay24Cnt();
                sumDay25 += vo.getDay25Cnt();
                sumDay26 += vo.getDay26Cnt();
                sumDay27 += vo.getDay27Cnt();
                sumDay28 += vo.getDay28Cnt();
                sumDay29 += vo.getDay29Cnt();
                sumDay30 += vo.getDay30Cnt();
                sumDay31 += vo.getDay31Cnt();
                sumSumCnt += vo.getSumCnt();
                sumAvgCnt += vo.getAvgCnt();
            }

            OutboundStaticsVo addRowVo = new OutboundStaticsVo();
            addRowVo.setDiv1("총계");
            addRowVo.setDiv2("");
            addRowVo.setDay1Cnt(sumDay1);
            addRowVo.setDay2Cnt(sumDay2);
            addRowVo.setDay3Cnt(sumDay3);
            addRowVo.setDay4Cnt(sumDay4);
            addRowVo.setDay5Cnt(sumDay5);
            addRowVo.setDay6Cnt(sumDay6);
            addRowVo.setDay7Cnt(sumDay7);
            addRowVo.setDay8Cnt(sumDay8);
            addRowVo.setDay9Cnt(sumDay9);
            addRowVo.setDay10Cnt(sumDay10);
            addRowVo.setDay11Cnt(sumDay11);
            addRowVo.setDay12Cnt(sumDay12);
            addRowVo.setDay13Cnt(sumDay13);
            addRowVo.setDay14Cnt(sumDay14);
            addRowVo.setDay15Cnt(sumDay15);
            addRowVo.setDay16Cnt(sumDay16);
            addRowVo.setDay17Cnt(sumDay17);
            addRowVo.setDay18Cnt(sumDay18);
            addRowVo.setDay19Cnt(sumDay19);
            addRowVo.setDay20Cnt(sumDay20);
            addRowVo.setDay21Cnt(sumDay21);
            addRowVo.setDay22Cnt(sumDay22);
            addRowVo.setDay23Cnt(sumDay23);
            addRowVo.setDay24Cnt(sumDay24);
            addRowVo.setDay25Cnt(sumDay25);
            addRowVo.setDay26Cnt(sumDay26);
            addRowVo.setDay27Cnt(sumDay27);
            addRowVo.setDay28Cnt(sumDay28);
            addRowVo.setDay29Cnt(sumDay29);
            addRowVo.setDay30Cnt(sumDay30);
            addRowVo.setDay31Cnt(sumDay31);
            addRowVo.setSumCnt(sumSumCnt);
            addRowVo.setAvgCnt(sumAvgCnt);

            resultList.add(addRowVo);
        }

        return OutboundStaticsResponseDto.builder()
                .total(resultList != null ? resultList.size() : 0)
                .rows(resultList)
                .build();
    }

    /**
     * 미출 통계 리스트 조회
     *
     * @param dto OutboundStaticsRequestDto
     * @return OutboundStaticsResponseDto
     * @throws BaseException
     */
    protected MissOutboundStaticsResponseDto getMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException {
        dto.setStartDe(DateUtil.convertFormatNew(dto.getStartDe(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setEndDe(DateUtil.convertFormatNew(dto.getEndDe(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setSupplierList(StringUtil.getArrayListWithoutAll(dto.getSupplierFilter()));
        dto.setSearchDelivList(StringUtil.getArrayListWithoutAll(dto.getSearchDelivFilter()));

        // # 조회
        List<MissOutboundStaticsVo> resultList = outboundStaticsMapper.getMissOutboundStaticsList(dto);

        // 총계 row 추가
        if (resultList != null && resultList.size() > 0) {

            // 미출
            int sumDeliveryGoodsCnt = 0;
            int sumMissGoodsCnt = 0;
            double sumMissGoodsRate = 0;
            int sumMissGoodsPrice = 0;
            int sumDeliveryOrderCnt = 0;
            int sumMissOrderCnt = 0;
            double sumMissOrderRate = 0;

            for (MissOutboundStaticsVo vo : resultList) {
                sumDeliveryGoodsCnt += vo.getDeliveryGoodsCnt();
                sumMissGoodsCnt += vo.getMissGoodsCnt();
                sumMissGoodsRate += vo.getMissGoodsRate();
                sumMissGoodsPrice += vo.getMissGoodsPrice();
                sumDeliveryOrderCnt += vo.getDeliveryOrderCnt();
                sumMissOrderCnt += vo.getMissOrderCnt();
                sumMissOrderRate += vo.getMissOrderRate();

                vo.setMissGoodsRateName(vo.getMissGoodsRate() + "%");
                vo.setMissGoodsPriceName(StringUtil.numberFormat(vo.getMissGoodsPrice()));
                vo.setMissOrderRateName(vo.getMissOrderRate() + "%");
            }

            MissOutboundStaticsVo addRowVo = new MissOutboundStaticsVo();
            addRowVo.setDt("총계");
            addRowVo.setDeliveryGoodsCnt(sumDeliveryGoodsCnt);
            addRowVo.setMissGoodsCnt(sumMissGoodsCnt);
            addRowVo.setMissGoodsRate(sumMissGoodsRate);
            addRowVo.setMissGoodsRateName(sumMissGoodsRate + "%");
            addRowVo.setMissGoodsPrice(sumMissGoodsPrice);
            addRowVo.setMissGoodsPriceName(StringUtil.numberFormat(sumMissGoodsPrice));
            addRowVo.setDeliveryOrderCnt(sumDeliveryOrderCnt);
            addRowVo.setMissOrderCnt(sumMissOrderCnt);
            addRowVo.setMissOrderRate(sumMissOrderRate);

            sumMissOrderRate = Math.round(sumDeliveryOrderCnt*100/sumMissOrderCnt);
            addRowVo.setMissOrderRateName(sumMissOrderRate + "%");

            resultList.add(addRowVo);
        }

        return MissOutboundStaticsResponseDto.builder()
                .total(resultList != null ? resultList.size() : 0)
                .rows(resultList)
                .build();
    }

}
