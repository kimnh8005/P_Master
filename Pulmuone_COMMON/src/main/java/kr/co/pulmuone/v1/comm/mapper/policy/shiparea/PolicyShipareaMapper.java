package kr.co.pulmuone.v1.comm.mapper.policy.shiparea;

import java.util.List;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.GetBackCountryResultVo;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PolicyShipareaMapper {

	int getBackCountryListCount(PolicyShipareaDto dto);

	Page<GetBackCountryResultVo> getBackCountryList(PolicyShipareaDto dto);

	int duplicateBackCountryCount(PolicyShipareaDto dto);

	int addBackCountry(PolicyShipareaDto dto);

    int putBackCountry(PolicyShipareaDto dto);

    int delBackCountry(PolicyShipareaDto dto);

    GetBackCountryResultVo getBackCountry(PolicyShipareaDto dto);

    List<GetBackCountryResultVo> getBackCountryExcelList(String zipCodes[]);

    boolean isUndeliverableArea(@Param("undeliverableType")String undeliverableType, @Param("zipCode")String zipCode) throws Exception;

    boolean isNonDeliveryArea(@Param("undeliverableTypes")String[] undeliverableTypes, @Param("zipCode")String zipCode) throws Exception;

    NonDeliveryAreaInfoVo getNonDeliveryAreaInfo(@Param("undeliverableTypes")String[] undeliverableTypes, @Param("zipCode")String zipCode) throws Exception;
}
