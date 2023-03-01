package kr.co.pulmuone.v1.comm.mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.base.dto.vo.PmCouponCommonResultVo;
import kr.co.pulmuone.v1.base.dto.vo.PmPointCommonResultVo;



@Mapper
public interface PmCommonMapper {

	List<PmPointCommonResultVo> getPmPointList();

	List<PmCouponCommonResultVo> getPmCpnList();
}
