package kr.co.pulmuone.v1.comm.excel.validate;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovUploadDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.order.ifday.IfDayExcelMapper;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeOrderCntDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultRowValidator {

    private final IfDayExcelMapper ifDayExcelMapper;

    private final DefaultRowItemValidator defaultRowItemValidator;

    public List<CalPgUploadDto> getCalculatePgRowItemValidator(List<CalPgUploadDto> excelList) {
        for (CalPgUploadDto dto : excelList) {
            MessageCommEnum responseCommEnum = defaultRowItemValidator.calculatePgValidateOrder(dto);

            dto.setSuccess(true);
            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
            }
        }
        return excelList;
    }


    public List<CalOutmallUploadDto> getCalculateOutmallRowItemValidator(List<CalOutmallUploadDto> excelList) {
        for (CalOutmallUploadDto dto : excelList) {
            MessageCommEnum responseCommEnum = defaultRowItemValidator.calculateOutmallValidateOrder(dto);

            dto.setSuccess(true);
            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
            }
        }
        return excelList;
    }

    public List<CalPovUploadDto> getCalculatePovRowItemValidator(List<CalPovUploadDto> excelList) {
        for (CalPovUploadDto dto : excelList) {
            MessageCommEnum responseCommEnum = defaultRowItemValidator.calculatePovValidateOrder(dto);

//            dto.setSuccess(true);
//            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
//                dto.setSuccess(false);
//            }
        }
        return excelList;
    }

    public List<IfDayChangeDto> getIfDayChangeRowItemValidator(List<IfDayChangeDto> excelList) {
        int orderCnt        = 0;
        int orderDetlCnt    = 0;
        int outmallCnt      = 0;
        for (IfDayChangeDto dto : excelList) {
            dto.setFailMessage("");
            IfDayChangeOrderCntDto ifDayChangeOrderCntDto = ifDayExcelMapper.getOrderDetlCount(dto.getOdid(), dto.getOdOrderDetlSeq());
            orderCnt        = ifDayChangeOrderCntDto.getOrderCnt();
            orderDetlCnt    = ifDayChangeOrderCntDto.getOrderDetlCnt();
            outmallCnt      = ifDayChangeOrderCntDto.getOutmallCnt();
            if (orderCnt > 0 && orderDetlCnt > 0) {
                MessageCommEnum responseCommEnum = defaultRowItemValidator.ifDayChangeValidate(dto);

                dto.setSuccess(true);
                if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                    dto.setSuccess(false);
                }

                // 변경하려는 주문I/F일자가 기존 주문I/F일자와 같을때(HGRM-9923)
                Boolean isSameOrderIfDay = ifDayExcelMapper.isSameOrderIfDay(dto);
                if(isSameOrderIfDay){
                    dto.setSuccess(false);
                    dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.IS_SAME_ORDER_IF_DAY.getMessage());
                }


            } else {
                dto.setSuccess(false);


                if (orderDetlCnt <= 0) {
                    dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_ORDER_DETAIL.getMessage());
                }

                // [SPMO-497] BOS_[I/F일자 조정] 자사몰 주문 허용의 건
//                if (outmallCnt <= 0) {
//                    dto.setFailMessage(dto.getFailMessage() + (!"".equals(dto.getFailMessage()) ? " " : "") + ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_OUTMALL.getMessage());
//                }

                if (orderCnt <= 0) {
                    dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_ORDER.getMessage());
                }

            }
        }
        return excelList;
    }

    public List<ClaimInfoExcelUploadDto> getClaimExcelUploadRowItemValidator(List<ClaimInfoExcelUploadDto> excelList) {
//        int orderCnt        = 0;
        int orderDetlCnt, cancelAbleCnt;
        for (ClaimInfoExcelUploadDto dto : excelList) {
            IfDayChangeOrderCntDto ifDayChangeOrderCntDto = ifDayExcelMapper.getOrderDetlCountByClaim(dto.getOdid(), dto.getOdOrderDetlSeq());
//            orderCnt        = ifDayChangeOrderCntDto.getOrderCnt();
            orderDetlCnt    = ifDayChangeOrderCntDto.getOrderDetlCnt();
            cancelAbleCnt   = ifDayChangeOrderCntDto.getOutmallCnt();

            if (orderDetlCnt > 0 && cancelAbleCnt >= dto.getClaimCnt()) {
                MessageCommEnum responseCommEnum = defaultRowItemValidator.claimExcelUploadValidate(dto);

                dto.setSuccess(true);
                if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                    dto.setSuccess(false);
                }
            } else {
                dto.setSuccess(false);
                if (orderDetlCnt <= 0) {
                    dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_ORDER.getMessage(), dto.getOdid(), dto.getOdOrderDetlSeq()));
                } else if (cancelAbleCnt < dto.getClaimCnt()) {
                    dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.CLAIM_COUNT_OVER.getMessage(), cancelAbleCnt));
                }
            }
        }
        return excelList;
    }

    // 도서산간/배송불가 권역 엑셀 업로드 검증
    public List<ShippingAreaExcelUploadDto> getShippingAreaExcelUploadRowItemValidator(List<ShippingAreaExcelUploadDto> excelList){

        for(ShippingAreaExcelUploadDto dto : excelList){
            MessageCommEnum responseCommEnum = defaultRowItemValidator.shippingAreaExcelUploadValidate(dto);

            dto.setSuccess(true);
            if(!responseCommEnum.equals(BaseEnums.Default.SUCCESS)){
                dto.setSuccess(false);
            }
        }

        return excelList;
    }

}
