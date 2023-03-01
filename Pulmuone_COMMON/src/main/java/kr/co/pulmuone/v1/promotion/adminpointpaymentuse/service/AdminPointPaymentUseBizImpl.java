package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;

@Service
public class AdminPointPaymentUseBizImpl implements AdminPointPaymentUseBiz{

	@Autowired
	AdminPointPaymentUseService adminPointPaymentUseService;

    @Override
    public ApiResult<?> getAdminPointPaymentUseList(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
    	AdminPointPaymentUseListResponseDto result = new AdminPointPaymentUseListResponseDto();

    	if(adminPointPaymentUseListRequestDto.getCondiValue() != null && !adminPointPaymentUseListRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(adminPointPaymentUseListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			adminPointPaymentUseListRequestDto.setCondiValueArray(array);
		}

    	adminPointPaymentUseListRequestDto.setPointType(PointEnums.PointType.ADMIN.getCode());
    	Page<AdminPointPaymentUseVo> adminPointPaymentUseList = adminPointPaymentUseService.getAdminPointPaymentUseList(adminPointPaymentUseListRequestDto);

        result.setTotal(adminPointPaymentUseList.getTotal());
        result.setRows(adminPointPaymentUseList.getResult());

        return ApiResult.success(result);

    }


    @Override
    public ExcelDownloadDto adminPointPaymentUseListExportExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
        return adminPointPaymentUseService.adminPointPaymentUseListExportExcel(adminPointPaymentUseListRequestDto);
    }

    @Override
    public List<AdminPointPaymentUseVo> adminPointPaymentUseListExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
        return adminPointPaymentUseService.adminPointPaymentUseListExcel(adminPointPaymentUseListRequestDto);
    }

}
