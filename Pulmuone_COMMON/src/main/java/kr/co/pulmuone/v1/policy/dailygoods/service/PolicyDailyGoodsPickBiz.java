package kr.co.pulmuone.v1.policy.dailygoods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;

public interface PolicyDailyGoodsPickBiz {

	ApiResult<?> getPolicyDailyGoodsPickList(PolicyDailyGoodsPickDto dto);
	ApiResult<?> putPolicyDailyGoodsPick(PolicyDailyGoodsPickDto dto);
	ExcelDownloadDto getPolicyDailyGoodsPickListExportExcel(PolicyDailyGoodsPickDto dto);

}
