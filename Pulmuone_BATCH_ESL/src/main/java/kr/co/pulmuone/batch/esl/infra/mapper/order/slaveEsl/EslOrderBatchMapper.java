package kr.co.pulmuone.batch.esl.infra.mapper.order.slaveEsl;

import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo.EatsslimOrderListVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EslOrderBatchMapper {

	int addEatsslimOrder(EatsslimOrderListVo vo);

}
