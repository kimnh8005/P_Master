package kr.co.pulmuone.v1.comm.mappers.slaveCjFront;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;


@Mapper
public interface CJLogisticsMapper {

	// CJ 택배 주문 접수
	int addCJLogisticsOrderAccept(CJLogisticsOrderAcceptDto dto);

}
