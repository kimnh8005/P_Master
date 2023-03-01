package kr.co.pulmuone.v1.comm.mappers.batch.master.order.ezadmin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminInfoDto;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;

@Mapper
public interface EZAdminOrderMapper {

	/**
	 * 이지어드민 정보 조회
	 * @param syncCd
	 * @return
	 * @throws Exception
	 */
	List<EZAdminOrderDto> getEZAdminOrderTargetList(@Param(value = "syncCdList") List<String> syncCdList, @Param(value = "easyadminBatchTp") String easyadminBatchTp) throws Exception;

	List<EZAdminOrderDto> getNotOrderCreateInIfEasyadminOrderSucc(Long ifEasyadminInfoId) throws Exception;

	int getOrderCreateCount(long ifEasyadminInfoId) throws Exception;
}
