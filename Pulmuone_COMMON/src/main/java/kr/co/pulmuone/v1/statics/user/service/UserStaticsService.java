package kr.co.pulmuone.v1.statics.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.statics.user.UserStaticsMapper;
import kr.co.pulmuone.v1.statics.user.dto.*;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserCountStaticsVo;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserGroupStaticsVo;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserTypeStaticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.07.22.              이원호         최초작성
 * =======================================================================
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStaticsService {

    private final UserStaticsMapper userStaticsMapper;

    /**
     * 회원 유형별 판매/매출 현황 통계
     *
     * @param dto UserTypeStaticsRequestDto
     * @return UserTypeStaticsResponseDto
     */
    protected UserTypeStaticsResponseDto getUserTypeStaticsList(UserTypeStaticsRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<UserTypeStaticsVo> list = userStaticsMapper.getUserTypeStaticsList(dto);

        List<UserTypeStaticsVo> resultList = list.getResult();
        int sum1 = 0, sum3 = 0, sum4 = 0, sum5 = 0;
        int sum7 = 0, sum8 = 0;
        long sum2 = 0, sum6 = 0;

        for (UserTypeStaticsVo vo : resultList) {
            sum1 += vo.getStandardUserCount();
            sum2 += vo.getStandardPaidPrice();
            sum3 += vo.getStandardOrderCount();
            sum4 += vo.getStandardGoodsCount();
            sum5 += vo.getContrastUserCount();
            sum6 += vo.getContrastPaiPrice();
            sum7 += vo.getContrastOrderCount();
            sum8 += vo.getContrastGoodsCount();
        }

        UserTypeStaticsVo sumVo = new UserTypeStaticsVo();
        sumVo.setGubun("총계");
        sumVo.setStandardUserCount(sum1);
        sumVo.setStandardPaidPrice(sum2);
        sumVo.setStandardOrderCount(sum3);
        sumVo.setStandardGoodsCount(sum4);
        sumVo.setContrastUserCount(sum5);
        sumVo.setContrastPaiPrice(sum6);
        sumVo.setContrastOrderCount(sum7);
        sumVo.setContrastGoodsCount(sum8);
        resultList.add(sumVo);

        return UserTypeStaticsResponseDto.builder()
                .total(list.getTotal())
                .rows(resultList)
                .build();
    }

    /**
     * 일반 회원 등급별 판매현황 통계
     *
     * @param dto UserGradeStaticsRequestDto
     * @return UserGradeStaticsResponseDto
     */
    protected UserGroupStaticsResponseDto getUserGroupStaticsList(UserGroupStaticsRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<UserGroupStaticsVo> list = userStaticsMapper.getUserGroupStaticsList(dto);

        List<UserGroupStaticsVo> resultList = list.getResult();
        int sum1 = 0, sum3 = 0, sum4 = 0;
        long sum2 = 0;

        for (UserGroupStaticsVo vo : resultList) {
            sum1 += vo.getUserCount();
            sum2 += vo.getPaidPrice();
            sum3 += vo.getOrderCount();
            sum4 += vo.getGoodsCount();
        }

        UserGroupStaticsVo sumVo = new UserGroupStaticsVo();
        sumVo.setGroupMasterName("총계");
        sumVo.setGroupName("총계");
        sumVo.setUserCount(sum1);
        sumVo.setPaidPrice(sum2);
        sumVo.setOrderCount(sum3);
        sumVo.setGoodsCount(sum4);
        resultList.add(sumVo);

        return UserGroupStaticsResponseDto.builder()
                .total(list.getTotal())
                .rows(resultList)
                .build();
    }

    /**
     * 일반 회원 등급별 판매현황 통계
     *
     * @param dto UserCountStaticsRequestDto
     * @return UserCountStaticsResponseDto
     */
    protected UserCountStaticsResponseDto getUserCountStaticsList(UserCountStaticsRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<UserCountStaticsVo> list = userStaticsMapper.getUserCountStaticsList(dto);

        return UserCountStaticsResponseDto.builder()
                .total(list.getTotal())
                .rows(list.getResult())
                .build();
    }

}
