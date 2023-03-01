package kr.co.pulmuone.v1.comm.mapper.order.order;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListRequestDto;
import kr.co.pulmuone.v1.od.order.dto.vo.GetOutmallOrderDetailListResultVo;


@Mapper
public interface OutmallOrderDetailListMapper {

	int getUserDormantHistoryListCount(GetOutmallOrderDetailListRequestDto dto) throws Exception;

	Page<GetOutmallOrderDetailListResultVo> getOutmallOrderDetailList(GetOutmallOrderDetailListRequestDto dto) throws Exception;

}
