package kr.co.pulmuone.v1.comm.mapper.policy.holiday;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayGroupListResultVo;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayGroupResultVo;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;

@Mapper
public interface PolicyHolidayMapper {

	List<GetHolidayListResultVo> getHolidayList();

	int deleteHoliday();

	int addHoliday(SaveHolidayRequestDto voList);

	List<GetHolidayGroupListResultVo> getHolidayGroupList(GetHolidayGroupListRequestDto dto);

	int getHolidayGroupListCount(GetHolidayGroupListRequestDto dto);

	List<GetHolidayGroupResultVo> getHolidayGroup(GetHolidayGroupRequestDto dto);

    void addHolidayGroup(SaveHolidayGroupRequestDto dto);

	void putHolidayGroup(SaveHolidayGroupRequestDto dto) ;

	void delHolidayGroupAddDate(SaveHolidayGroupRequestDto dto);

	void addHolidayGroupAddDate(SaveHolidayGroupRequestDto dto);

	int validateHolidayGroupName(SaveHolidayGroupRequestDto dto);

	List<GetHolidayListResultVo> getAllHolidayList();
}
