package kr.co.pulmuone.v1.comm.excel.validate;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.calculate.collation.service.CalOutmallBiz;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovUploadDto;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import kr.co.pulmuone.v1.policy.shippingarea.service.PolicyShippingAreaBiz;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class DefaultRowItemValidator {


	private CalOutmallBiz calOutmallBiz;

	private PolicyShippingAreaBiz policyShippingAreaBiz;

    /**
     * Calculate Pg 내역 Validation
     * @param dto
     * @return
     */
    public MessageCommEnum calculatePgValidateOrder(CalPgUploadDto dto) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        // 필수 값 확인

        // 신용카드, 실시간계좌이체, 가상계좌 , KCP
        if (StringUtil.isEmpty(dto.getOdid())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.odid.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 신용카드, 실시간계좌이체, 가상계좌
        if (StringUtil.isEmpty(dto.getTid())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.tid.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        // 신용카드, 실시간계좌이체, 가상계좌, KCP
        if (StringUtil.isEmpty(dto.getApprovalDt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.approvalDt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 신용카드, 실시간계좌이체, 가상계좌, KCP
        if (StringUtil.isEmpty(dto.getTransAmt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.transAmt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 신용카드, 실시간계좌이체, 가상계좌, KCP
        if (StringUtil.isEmpty(dto.getCommission())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.commission.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 신용카드, 실시간계좌이체, 가상계좌, KCP
        if (StringUtil.isEmpty(dto.getVat())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.vat.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        // 신용카드, 실시간계좌이체, 가상계좌, KCP
        if (StringUtil.isEmpty(dto.getGiveDt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.giveDt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }


        if(dto.getInicisType() == null){        // PG 대사 - KCP
        	// KCP
            if (StringUtil.isEmpty(dto.getEscrowCommission())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.escrowCommission.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // KCP
            if (StringUtil.isEmpty(dto.getEscrowVat())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.escrowVat.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // KCP.
            if (StringUtil.isEmpty(dto.getAccountDt())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.accountDt.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }



        if(ExcelUploadEnums.InicisType.bank.getCode().equals(dto.getInicisType()) || ExcelUploadEnums.InicisType.virtualBank.getCode().equals(dto.getInicisType())){

        	// 실시간계좌이체
        	if (StringUtil.isEmpty(dto.getCertificationCommission()) && ExcelUploadEnums.InicisType.bank.getCode().equals(dto.getInicisType())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.certificationCommission.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}

        	// 실시간계좌이체
        	if (StringUtil.isEmpty(dto.getCertificationVat()) && ExcelUploadEnums.InicisType.bank.getCode().equals(dto.getInicisType())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.certificationVat.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}

        	// 실시간계좌이체, 가상계좌
            if (StringUtil.isEmpty(dto.getBankNm())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.bankNm.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 실시간계좌이체, 가상계좌
            if (StringUtil.isEmpty(dto.getBankAccountNumber())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.bankAccountNumber.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }else if(ExcelUploadEnums.InicisType.card.getCode().equals(dto.getInicisType())){

        	// 신용카드
            if (StringUtil.isEmpty(dto.getMPoint())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.mPoint.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        	// 신용카드
            if (StringUtil.isEmpty(dto.getMPointCommission())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.mPointCommission.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 신용카드
            if (StringUtil.isEmpty(dto.getMPointVat())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.mPointVat.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 신용카드
            if (StringUtil.isEmpty(dto.getMarketingCommission())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.marketingCommission.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        	// 신용카드
            if (StringUtil.isEmpty(dto.getMarketingVat())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.marketingVat.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        	// 신용카드
        	if (StringUtil.isEmpty(dto.getFreeCommission())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.freeCommission.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}

        	// 신용카드
        	if (StringUtil.isEmpty(dto.getCardNm())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.cardNm.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}

        	// 신용카드
        	if (StringUtil.isEmpty(dto.getCardAuthNum())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.cardAuthNum.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}

        	// 신용카드
        	if (StringUtil.isEmpty(dto.getCardQuota())) {
        		dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePgCols.cardQuota.getCodeName()));
        		returnStatus = BaseEnums.Default.FAIL;
        	}
        }


        return returnStatus;
    }

    public MessageCommEnum calculateOutmallValidateOrder(CalOutmallUploadDto dto) {


    	BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;

        // 외부몰주문번호
        if (StringUtil.isEmpty(dto.getOutmallId())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.outomallId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 판매처
        if (StringUtil.isEmpty(dto.getSellersNm())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.sellersNm.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        calOutmallBiz = BeanUtils.getBeanByClass(CalOutmallBiz.class);
        CalOutmallUploadDto sellDto = new CalOutmallUploadDto();
        sellDto = calOutmallBiz.getSellerInfo(dto.getSellersNm());


        // 판매처 PK
        if (sellDto ==  null || StringUtil.isEmpty(sellDto.getOmSellersId())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.sellersNm.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 매출금액
        if (StringUtil.isEmpty(dto.getOrderAmt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.orderAmt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 판매수량
        if (StringUtil.isEmpty(dto.getOrderCnt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.orderCnt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 결제일자
        if (StringUtil.isEmpty(dto.getIcDt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.icDt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 주문일자
        if (StringUtil.isEmpty(dto.getOrderIfDt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.orderIfDt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 매출확정일자
        if (StringUtil.isEmpty(dto.getSettleDt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.settleDt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 상품명
        if (StringUtil.isEmpty(dto.getGoodsNm())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.goodsNm.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 에누리금액
        if (StringUtil.isEmpty(dto.getDiscountPrice())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.discountPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 쿠폰금액
        if (StringUtil.isEmpty(dto.getCouponPrice())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.couponPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 기타 공제금액
        if (StringUtil.isEmpty(dto.getEtcDiscountPrice())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.etcDiscountPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 최종 매출금액
        if (StringUtil.isEmpty(dto.getSettlePrice())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.settlePrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 최종 판매수량
        if (StringUtil.isEmpty(dto.getSettleItemCnt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculateOutmallCols.settleItemCnt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }



        return returnStatus;

    }


    public MessageCommEnum calculatePovValidateOrder(CalPovUploadDto dto) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        // 필수 값 확인
//        if (StringUtil.isEmpty(dto.getA())) {
//            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePovCols.A.getCodeName()));
//            returnStatus = BaseEnums.Default.FAIL;
//        }
//        if (StringUtil.isEmpty(dto.getB())) {
//            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.CalculatePovCols.B.getCodeName()));
//            returnStatus = BaseEnums.Default.FAIL;
//        }

        return returnStatus;
    }

    public MessageCommEnum ifDayChangeValidate(IfDayChangeDto dto) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getOdOrderDetlSeq())) {
            dto.setFailMessage(dto.getFailMessage() + " " + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.odOrderDetlSeq.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isNumeric(StringUtil.nvl(dto.getOdOrderDetlSeq()))) {
            dto.setFailMessage(dto.getFailMessage() + " " + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.odOrderDetlSeq.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }



        if (StringUtil.isEmpty(dto.getOdid())) {
            dto.setFailMessage(dto.getFailMessage() + " " + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.odid.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getOdid()) != ExcelUploadValidateEnums.ByteLength.BYTE_14.getByteLength()) {
            dto.setFailMessage(dto.getFailMessage() + " " + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_NOT_EQUALS.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.odid.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_14.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_MOBILE_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isEmpty(dto.getIfDay())) {
            dto.setFailMessage(dto.getFailMessage() + " " + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.ifDay.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (DateUtil.validationDate(dto.getIfDay().toString()) != true) {
            dto.setFailMessage(dto.getFailMessage() + " " + ExcelUploadValidateEnums.ValidateType.COLUMN_DATE_FORMAT_FAIL.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
        }


        return returnStatus;
    }

    public MessageCommEnum claimExcelUploadValidate(ClaimInfoExcelUploadDto dto) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getOdid())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.odid.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        /*
        if (StringUtil.getByteLength(dto.getOdid()) != ExcelUploadValidateEnums.ByteLength.BYTE_16.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_NOT_EQUALS.getMessage(), ExcelUploadEnums.InterfaceDayChangeCols.odid.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_16.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_MOBILE_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }
        */

        if (StringUtil.isEmpty(dto.getOdOrderDetlSeq())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.odOrderDetlSeq.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isNumeric(StringUtil.nvl(dto.getOdOrderDetlSeq()))) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.odOrderDetlSeq.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isEmpty(dto.getClaimCnt())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.claimCnt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isNumeric(StringUtil.nvl(dto.getClaimCnt()))) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.claimCnt.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        /*
            SPMO-661 클레임구분 값 체크 제외
        if (StringUtil.isEmpty(dto.getClaimStatusTp())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.claimStatusTp.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if("".equals(StringUtil.nvl(OrderClaimEnums.ClaimStatusTp.findByCodeNm(dto.getClaimStatusTp()), ""))){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.claimStatusTp.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        */

        if (StringUtil.isEmpty(dto.getPsClaimBosId())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.psClaimBosId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isNumeric(StringUtil.nvl(dto.getPsClaimBosId()))) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.ClaimExcelUploadCols.psClaimBosId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        calOutmallBiz = BeanUtils.getBeanByClass(CalOutmallBiz.class);
        int psClaimBosCnt = calOutmallBiz.getPsClaimBosCount(dto.getPsClaimBosId());
        if(psClaimBosCnt < 1) {
            dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.CLAIM_BOS_ID_NOT_CODE.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
        }

        return returnStatus;
    }

    public MessageCommEnum shippingAreaExcelUploadValidate(ShippingAreaExcelUploadDto dto) {
        int checkZipCdCount = 0;
        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;

        policyShippingAreaBiz = BeanUtils.getBeanByClass(PolicyShippingAreaBiz.class);

        // 사용여부가 등록인 경우만 체크
        if(BaseEnums.EnumUseYn.INSERT.getCodeName().equals(dto.getUseYn())){
            checkZipCdCount = policyShippingAreaBiz.getShippingAreaZipCd(dto.getZipCd(), dto.getUndeliverableTp());
        }

        // 기존 우편번호가 존재할 경우
        if(checkZipCdCount > 0 ) {
            dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.DUPLICATE_ZIP_CODE.getMessage(), dto.getZipCd()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 우편번호가 5자리를 초과하는 경우 - 구 우편번호의 경우
        if(BaseEnums.EnumUseYn.INSERT.getCodeName().equals(dto.getUseYn()) && dto.getZipCd().length() > 5){
            dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.NEW_ZIP_CODE.getMessage(), 5));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 우편번호가 공란인 경우
        if (StringUtil.isEmpty(dto.getZipCd())) {
            dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ShippingAreaExcelUploadCols.zipCd.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 사용여부 (등록 or 삭제)가 공란인 경우
        if (StringUtil.isEmpty(dto.getUseYn())) {
            dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.ShippingAreaExcelUploadCols.zipCd.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isNotEmpty(dto.getUseYn())) {
            // 사용여부 값이 등록 또는 삭제가 아닌 경우
            if (!(BaseEnums.EnumUseYn.INSERT.getCodeName().equals(dto.getUseYn()) || BaseEnums.EnumUseYn.DELETE.getCodeName().equals(dto.getUseYn()))) {
                dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.SHIPPING_AREA_REGIST_NOT_CODE.getMessage(),ExcelUploadEnums.ShippingAreaExcelUploadCols.useYn.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        // 대체배송값이 존재할 경우
        if(StringUtil.isNotEmpty(dto.getAlternateDeliveryTp())){
            // 현재는 Y값만 사용. 추후 타입 변경시 해당 로직 점검 필요
            if(!BaseEnums.AlternateDeliveryType.Y.getCode().equals(dto.getAlternateDeliveryTp())){
                dto.setFailMsg(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.SHIPPING_AREA_REGIST_NOT_CODE.getMessage(),ExcelUploadEnums.ShippingAreaExcelUploadCols.useYn.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        return returnStatus;
    }

}
