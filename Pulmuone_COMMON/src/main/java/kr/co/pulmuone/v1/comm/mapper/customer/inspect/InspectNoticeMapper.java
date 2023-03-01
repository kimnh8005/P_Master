package kr.co.pulmuone.v1.comm.mapper.customer.inspect;

import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeIpVo;
import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeVo;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InspectNoticeMapper {

	InspectNoticeVo getInspectNotice() throws SQLException;

	List<InspectNoticeIpVo> getAllInspectNoticeIpList() throws SQLException;

	int addInspectNotice(InspectNoticeVo inspectNoticeVo) throws SQLException;

	int addAllInspectNoticeIp(List<InspectNoticeIpVo> inspectNoticeIpVoList) throws SQLException;

	int deleteInspectNotice(int inspectNoticeId) throws SQLException;

	int deleteAllInspectNoticeIp(int inspectNoticeId) throws SQLException;

	Boolean getIsBetweenServerTime(LocalDateTime serverDatetime);

}
