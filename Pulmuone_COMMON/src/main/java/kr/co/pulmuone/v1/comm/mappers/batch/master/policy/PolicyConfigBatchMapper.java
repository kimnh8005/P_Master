package kr.co.pulmuone.v1.comm.mappers.batch.master.policy;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PolicyConfigBatchMapper {

    String getConfigValue(String psKey);

    String getHolidayYn(String nowDate);

    void delHolidayYM(String yearMonth);

    void addHoliday(String holidayDate);

}
