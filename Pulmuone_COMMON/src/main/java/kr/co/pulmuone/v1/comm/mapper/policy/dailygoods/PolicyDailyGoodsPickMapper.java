package kr.co.pulmuone.v1.comm.mapper.policy.dailygoods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.dailygoods.dto.PolicyDailyGoodsPickDto;
import kr.co.pulmuone.v1.policy.dailygoods.dto.vo.PolicyDailyGoodsPickVo;

@Mapper
public interface PolicyDailyGoodsPickMapper {

	Page<PolicyDailyGoodsPickVo> getPolicyDailyGoodsPickList(PolicyDailyGoodsPickDto dto);
	int putPolicyDailyGoodsPick(PolicyDailyGoodsPickDto dto);
	List<PolicyDailyGoodsPickVo> getPolicyDailyGoodsPickListExportExcel(PolicyDailyGoodsPickDto dto);
}
