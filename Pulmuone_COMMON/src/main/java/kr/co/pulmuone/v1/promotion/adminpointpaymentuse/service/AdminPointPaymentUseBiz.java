package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;

public interface AdminPointPaymentUseBiz {

	/**
	 * 관리자 적립금 지급/차감 내역 리스트 조회
	 * @param adminPointPaymentUseListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> getAdminPointPaymentUseList(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception;

    ExcelDownloadDto adminPointPaymentUseListExportExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception;

    List<AdminPointPaymentUseVo> adminPointPaymentUseListExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception;

}
