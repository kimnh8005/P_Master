package kr.co.pulmuone.v1.comm.mapper.policy.fee;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 기본 수수료 관리 Mapper
 * </PRE>
 *
 */

@Mapper
public interface OmBasicFeeMapper {

	Page<OmBasicFeeListDto> getOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception;

	OmBasicFeeDto getOmBasicFee(OmBasicFeeRequestDto omBasicFeeRequestDto) throws Exception;

	Page<OmBasicFeeHistListDto> getOmBasicFeeHistList(OmBasicFeeHistListRequestDto omBasicFeeHistListRequestDto) throws Exception;

	int addOmBasicFee(OmBasicFeeVo omBasicFeeVoParam) throws Exception;

	int addOmBasicFeeHist(OmBasicFeeHistVo omBasicFeeHistVo) throws Exception;

	int putOmBasicFee(OmBasicFeeVo omBasicFeeVoParam) throws Exception;

	int putOmBasicFeeSellersGroup(OmBasicFeeVo omBasicFeeVoParam) throws Exception;

}
