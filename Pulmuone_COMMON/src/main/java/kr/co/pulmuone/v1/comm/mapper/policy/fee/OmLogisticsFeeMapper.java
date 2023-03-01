package kr.co.pulmuone.v1.comm.mapper.policy.fee;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 물류 수수료 관리 Mapper
 * </PRE>
 *
 */

@Mapper
public interface OmLogisticsFeeMapper {

	Page<OmLogisticsFeeListDto> getOmLogisticsFeeList(OmLogisticsFeeListRequestDto OmLogisticsFeeListRequestDto) throws Exception;

	OmLogisticsFeeDto getOmLogisticsFee(OmLogisticsFeeRequestDto OmLogisticsFeeRequestDto) throws Exception;

	Page<OmLogisticsFeeHistListDto> getOmLogisticsFeeHistList(OmLogisticsFeeHistListRequestDto OmLogisticsFeeHistListRequestDto) throws Exception;

	int addOmLogisticsFee(OmLogisticsFeeVo OmLogisticsFeeVoParam) throws Exception;

	int addOmLogisticsFeeHist(OmLogisticsFeeHistVo OmLogisticsFeeHistVo) throws Exception;

	int putOmLogisticsFee(OmLogisticsFeeVo OmLogisticsFeeVoParam) throws Exception;

}
