package kr.co.pulmuone.v1.policy.dailygoods.service;

import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;

@Service
public class PolicyDailyGoodsPickBizImpl implements PolicyDailyGoodsPickBiz {

    @Autowired
    private PolicyDailyGoodsPickService policyDailyGoodsPickService;
    /**
     * 일일상품 골라담기 허용여부 목록 조회
     *
     * @param PolicyDailyGoodsPickDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyDailyGoodsPickList(PolicyDailyGoodsPickDto dto) {
        dto.setSupplierId(String.valueOf(GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID));
    	return ApiResult.success(policyDailyGoodsPickService.getPolicyDailyGoodsPickList(dto));
    }
    /**
     * 일일상품 골라담기 허용여부 수정
     *
     * @param PolicyDailyGoodsPickDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putPolicyDailyGoodsPick(PolicyDailyGoodsPickDto dto) {
    	dto.setUpdateCount(policyDailyGoodsPickService.putPolicyDailyGoodsPick(dto));
    	return ApiResult.success(dto);
    }

    /**
     * 일일상품 골라담기 허용여부 목록 조회 엑셀다운로드
     *
     * @param PolicyDailyGoodsPickDto
     * @return ExcelDownloadDto
     */
    @Override
    public ExcelDownloadDto getPolicyDailyGoodsPickListExportExcel(PolicyDailyGoodsPickDto dto) {
        dto.setSupplierId(String.valueOf(GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID));
        return policyDailyGoodsPickService.getPolicyDailyGoodsPickListExportExcel(dto);
    }
}


