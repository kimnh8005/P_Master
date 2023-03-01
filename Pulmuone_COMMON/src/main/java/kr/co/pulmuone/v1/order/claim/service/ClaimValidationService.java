package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimValidationMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.ClaimValidationDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimOrderStatusVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 validation Service
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class ClaimValidationService {

    private final ClaimValidationMapper claimValidationMapper;

    /**
     * 주문 상태값 validation
     * @param claimStatusCd
     * @param goodsInfoList
     * @return ClaimValidationDto
     * @throws Exception
     */
    protected ClaimValidationDto orderStatusValidation(String claimStatusCd, List<OrderClaimGoodsInfoDto> goodsInfoList, long odClaimId, long ifUnreleasedInfoId) throws Exception {

        List<Long> odOrderDetlIds = goodsInfoList.stream().map(OrderClaimGoodsInfoDto::getOdOrderDetlId).collect(Collectors.toList());

        // 주문 상태값 조회
        List<ClaimOrderStatusVo> claimOrderStatusList = claimValidationMapper.selectOrderStatusList(odOrderDetlIds);

        // 주문 상태 validation 처리
        ClaimValidationDto claimValidationDto = orderStatusValidationProcess(claimStatusCd, claimOrderStatusList, odClaimId, ifUnreleasedInfoId);

        return claimValidationDto;
    }

    /**
     * 클레임 상태값 validation
     * @param claimStatusCd
     * @param goodsInfoList
     * @return ClaimValidationDto
     * @throws Exception
     */
    protected ClaimValidationDto claimStatusValidation(ClaimValidationDto claimValidationDto, String claimStatusCd, List<OrderClaimGoodsInfoDto> goodsInfoList, long odClaimId) throws Exception {

        // 주문 상태값 validation이 실패 또는 클레임PK가 없으면 패스
        if(odClaimId == 0 || claimValidationDto.getFailCount() > 0) return claimValidationDto;

        List<Long> odClaimDetlIds = goodsInfoList.stream().map(OrderClaimGoodsInfoDto::getOdClaimDetlId).collect(Collectors.toList());

        // 클레임 상태값 조회
        List<ClaimOrderStatusVo> claimOrderStatusList = claimValidationMapper.selectClaimStatusList(odClaimDetlIds, odClaimId);

        // 클레임 상태 validation 처리 (이미 요청한 상태값인지 확인)
        claimValidationDto = claimStatusValidationRequestProcess(claimStatusCd, claimOrderStatusList);

        if(claimValidationDto.getFailCount() > 0) return claimValidationDto;

        // 클레임 상태 validation 처리 (클레임 전 상태값 확인)
        claimValidationDto = claimStatusValidationBeforeProcess(claimStatusCd, claimOrderStatusList);

        return claimValidationDto;
    }

    /**
     * 클레임 수량 validation
     * @param claimStatusCd
     * @param goodsInfoList
     * @param odClaimId
     * @return ClaimValidationDto
     * @throws Exception
     */
    protected ClaimValidationDto claimCntValidation(ClaimValidationDto claimValidationDto, List<OrderClaimGoodsInfoDto> goodsInfoList, long odClaimId) throws Exception {

        // 클레임 상태값 validation이 실패 또는 클레임PK가 있으면 패스
        if(odClaimId > 0 || claimValidationDto.getFailCount() > 0) return claimValidationDto;

        for(OrderClaimGoodsInfoDto vo : goodsInfoList) {

            if(vo.getOdOrderDetlId() == 0) continue;

            int resultClaimCnt = claimValidationMapper.selectClaimCnt(vo.getOdOrderDetlId(), vo.getClaimCnt());
            if(resultClaimCnt < 0) {
                claimValidationDto = ClaimValidationDto.builder().failCount(1).claimResult(ClaimEnums.ClaimValidationResult.NO_CLAIM_CNT).build();
                break;
            }
        }

        return claimValidationDto;
    }

    /**
     * 주문 상태 validation 처리
     * @param claimStatusCd
     * @param claimOrderStatusList
     * @return ClaimValidationDto
     */
    private ClaimValidationDto orderStatusValidationProcess(String claimStatusCd, List<ClaimOrderStatusVo> claimOrderStatusList, long odClaimId, long ifUnreleasedInfoId) {

        // default 성공
        ClaimValidationDto claimValidationDto = ClaimValidationDto.builder().failCount(0).claimResult(ClaimEnums.ClaimValidationResult.SUCCESS).build();

        // 클레임PK가 없을때만
        if(odClaimId == 0) {
            // 변경 클레임상태: 취소완료
            if (OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
                // 현 주문상태: 결제완료가 아닌 주문 체크
                int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(vo.getOrderStatusCd())).count();
                if(ifUnreleasedInfoId > 0) failCount = 0;
                if (failCount > 0)
                    claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_INCOM_COMPLETE).build();
            }
            // 변경 클레임상태: 취소요청
            else if (OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(claimStatusCd)) {
                // 현 주문상태: 배송준비중이 아닌 주문 체크
                int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.DELIVERY_READY.getCode().equals(vo.getOrderStatusCd())).count();
                if (failCount > 0)
                    claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_DELIVERY_READY).build();
            }
        }

        // 변경 클레임상태: 반품요청, 반품승인, 반품완료
        if (OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
            // 현 주문상태: 배송중,배송완료,구매확정이 아닌 주문 체크
            int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.DELIVERY_ING.getCode().equals(vo.getOrderStatusCd())
                    && !OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode().equals(vo.getOrderStatusCd())
                    && !OrderEnums.OrderStatus.BUY_FINALIZED.getCode().equals(vo.getOrderStatusCd())).count();
            if (failCount > 0)
                claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_DELIVERY_ING_OR_DELIVERY_COMPLETE_OR_BUY_FINALIZED).build();
        }
        // 변경 클레임 상태값이 없을때
        else if ("".equals(StringUtil.nvl(claimStatusCd))) {
            claimValidationDto = ClaimValidationDto.builder().failCount(1).claimResult(ClaimEnums.ClaimValidationResult.NO_CHANGE_CLAIM_STATUS).build();
        }

        return claimValidationDto;
    }

    /**
     * 클레임 상태 validation 처리 (이미 요청한 상태값인지 확인)
     * @param claimStatusCd
     * @param claimOrderStatusList
     * @return ClaimValidationDto
     */
    private ClaimValidationDto claimStatusValidationRequestProcess(String claimStatusCd, List<ClaimOrderStatusVo> claimOrderStatusList) {

        // default 성공
        ClaimValidationDto claimValidationDto = ClaimValidationDto.builder().failCount(0).claimResult(ClaimEnums.ClaimValidationResult.SUCCESS).build();

        int failCount = (int) claimOrderStatusList.stream().filter(vo -> vo.getClaimStatusCd().equals(claimStatusCd)).count();
        if(failCount > 0)
            claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.findByCode("ALREADY_"+claimStatusCd)).build();

        return claimValidationDto;
    }

    /**
     * 클레임 상태 validation 처리 (클레임 전 상태값 확인)
     * @param claimStatusCd
     * @param claimOrderStatusList
     * @return ClaimValidationDto
     */
    private ClaimValidationDto claimStatusValidationBeforeProcess(String claimStatusCd, List<ClaimOrderStatusVo> claimOrderStatusList) {

        // default 성공
        ClaimValidationDto claimValidationDto = ClaimValidationDto.builder().failCount(0).claimResult(ClaimEnums.ClaimValidationResult.SUCCESS).build();

        // 변경 클레임상태: 취소거부, 취소철회, 취소완료
        if (OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
            // 현 클레임상태: 취소요청이 아닌 주문 체크
            int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(vo.getClaimStatusCd())).count();
            if (failCount > 0)
                claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_CANCEL_APPLY).build();
        }
        // 변경 클레임상태: 반품거부, 반품철회, 반품승인
        else if (OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(claimStatusCd)
                || OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode().equals(claimStatusCd)) {
            // 현 클레임상태: 반품요청이 아닌 주문 체크
            int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(vo.getClaimStatusCd())).count();
            if (failCount > 0)
                claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_RETURN_APPLY).build();
        }
        // 변경 클레임상태: 반품완료
        else if (OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
            // 현 클레임상태: 반품요청, 반품승인, 반품보류가 아닌 주문 체크
            int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(vo.getClaimStatusCd())
                    && !OrderEnums.OrderStatus.RETURN_ING.getCode().equals(vo.getClaimStatusCd())
                    && !OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(vo.getClaimStatusCd())).count();
            if (failCount > 0)
                claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_RETURN_APPLY).build();
        }
        // 변경 클레임상태: 반품보류
        else if (OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(claimStatusCd)) {
            // 현 클레임상태: 반품승인이 아닌 주문 체크
            int failCount = (int) claimOrderStatusList.stream().filter(vo -> !OrderEnums.OrderStatus.RETURN_ING.getCode().equals(vo.getClaimStatusCd())).count();
            if (failCount > 0)
                claimValidationDto = ClaimValidationDto.builder().failCount(failCount).claimResult(ClaimEnums.ClaimValidationResult.NO_RETURN_ING).build();
        }

        return claimValidationDto;
    }

}


