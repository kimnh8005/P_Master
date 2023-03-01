package kr.co.pulmuone.v1.comm.mappers.batch.master.order.outmall;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OutmallExcelOrderMapper {

	/**
	 * outmall 엑셀 업로드 정보 조회
	 * @param batchStatusList
	 * @return
	 * @throws Exception
	 */
	List<OutMallOrderDto> getOutmallExcelOrderTargetList(@Param("batchStatusList") List<String> batchStatusList) throws Exception;

	/**
	 * outmall 엑셀 업로드 정보 수정
	 * @param outMallOrderDto
	 * @return
	 * @throws Exception
	 */
	int putOutmallExcelInfo(OutMallExcelInfoVo outMallExcelInfoVo) throws Exception;
}
