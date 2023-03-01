package kr.co.pulmuone.v1.comm.mapper.outmall.ezadmin;

import java.util.List;

import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminCreateOrderFailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;

@Mapper
public interface EZAdminOrderRegistrationMapper {

	/**
	 * 이지어드민 주문생성 대상 정보 조회
	 * @param ifEasyadminInfoId
	 * @return
	 * @throws Exception
	 */
    List<EZAdminOrderDto> getEZAdminOrderCreateTargetList(@Param("ifEasyadminInfoId")long ifEasyadminInfoId, @Param("ifEasyadminInfoReqDataId")long ifEasyadminInfoReqDataId) throws Exception;

	/**
	 * 이지어드민 주문 상세 PK 목록 얻기
	 * @param ifEasyadminInfoId
	 * @return
	 * @throws Exception
	 */
    List<Long> getOdOrderDetlIds(@Param(value = "odOrderIds") List<String> odOrderIds) throws Exception;


	void addEzadminCreateOrderFailSelectInsert(EZAdminCreateOrderFailDto failDto);

	void addEzadminCreateOrderFailSelectInsertDetl(EZAdminCreateOrderFailDto failDto);

	void delEzadminCreateOrderSuccess(@Param("ifEasyadminOrderSuccDetlId") Long ifEasyadminOrderSuccDetlId);

	void delEzadminCreateOrderSuccessDetl(@Param("ifEasyadminOrderSuccDetlId") Long ifEasyadminOrderSuccDetlId);

	void putEZAdminOrderSuccOrderCreateYn(@Param("collectionMallId")Long collectionMallId, @Param("ifEasyadminInfoId")Long ifEasyadminInfoId, @Param("reqDataId")Long reqDataId);

	List<Long> getIfEasyadminInfoReqDataIdList(long ifEasyadminInfoId);
}
