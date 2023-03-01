package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.ClaimValidationDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 validation Implements
 * </PRE>
 */
@Slf4j
@Service
public class ClaimValidationBizImpl implements ClaimValidationBiz {

    @Autowired
    private ClaimValidationService claimValidationService;

    /**
     * 클레임 validation 처리
     * @param claimStatusCd
     * @param requestDto
     * @return ClaimRequestProcessDto
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ClaimValidationDto claimValidationProcess(String claimStatusCd, OrderClaimRegisterRequestDto requestDto) throws Exception {

        List<OrderClaimGoodsInfoDto> goodsInfoList = requestDto.getGoodsInfoList();

        // 클레임 대상 상품이 존재하지 않을 경우
        if(goodsInfoList.isEmpty()) {
            ClaimValidationDto claimValidationDto = ClaimValidationDto.builder()
                                                                        .claimResult(ClaimEnums.ClaimValidationResult.NO_CLAIM_GOODS)
                                                                        .build();
            return claimValidationDto;
        }

        // 증정품 제외 하여 유효성 체크
        List<OrderClaimGoodsInfoDto> claimGoodsList = goodsInfoList.stream().filter(x -> !GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(x.getGoodsTpCd())).collect(Collectors.toList());

        ClaimValidationDto claimValidationDto = ClaimValidationDto.builder()
                                                                    .claimResult(ClaimEnums.ClaimValidationResult.SUCCESS)
                                                                    .build();

        if(!claimGoodsList.isEmpty()) {
            // 주문 상태값 validation
            claimValidationDto = claimValidationService.orderStatusValidation(claimStatusCd, claimGoodsList, requestDto.getOdClaimId(), requestDto.getIfUnreleasedInfoId());

            // 클레임 상태값 validation
            claimValidationDto = claimValidationService.claimStatusValidation(claimValidationDto, claimStatusCd, claimGoodsList, requestDto.getOdClaimId());

            // 클레임 수량 validation
            claimValidationDto = claimValidationService.claimCntValidation(claimValidationDto, claimGoodsList, requestDto.getOdClaimId());
        }

        return claimValidationDto;
    }

}

