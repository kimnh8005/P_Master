package kr.co.pulmuone.v1.comm.mapper.promotion.adminpointpaymentuse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;

@Mapper
public interface AdminPointPaymentUseMapper {

	Page<AdminPointPaymentUseVo> getAdminPointPaymentUseList(AdminPointPaymentUseListRequestDto adminPointPaymentUseRequestDto) throws Exception;

	List<AdminPointPaymentUseVo> adminPointPaymentUseListExcel(AdminPointPaymentUseListRequestDto adminPointPaymentUseRequestDto) throws Exception;

	int getAdminPointPaymentUseListCount(AdminPointPaymentUseListRequestDto adminPointPaymentUseRequestDto) throws Exception;

}
