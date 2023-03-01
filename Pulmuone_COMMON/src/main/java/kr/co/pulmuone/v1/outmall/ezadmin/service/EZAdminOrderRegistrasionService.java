package kr.co.pulmuone.v1.outmall.ezadmin.service;


import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminCreateOrderFailDto;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.outmall.ezadmin.EZAdminOrderRegistrationMapper;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EZAdminOrderRegistrasionService {

    private final EZAdminOrderRegistrationMapper ezadminOrderRegistrationMapper;

    protected List<EZAdminOrderDto> getEZAdminOrderCreateTargetList(long ifEasyadminInfoId, long ifEasyadminInfoReqDataId) throws Exception {
        return ezadminOrderRegistrationMapper.getEZAdminOrderCreateTargetList(ifEasyadminInfoId, ifEasyadminInfoReqDataId);
    }

    protected List<Long> getOdOrderDetlIds(List<String> odOrderIds) throws Exception {
        return ezadminOrderRegistrationMapper.getOdOrderDetlIds(odOrderIds);
    }

    protected void addEzadminCreateOrderFailSelectInsert(List<Map<String, Object>> resultFailList) {
        EZAdminCreateOrderFailDto failDto = new EZAdminCreateOrderFailDto();
        failDto.setIfEasyadminOrderSuccDetlId(StringUtil.nvlLong(resultFailList.get(0).get("succId")));
        failDto.setFailType(OutmallEnums.OutmallFailType.BATCH.getCode());  // 실패구분(B:배치)
        ezadminOrderRegistrationMapper.addEzadminCreateOrderFailSelectInsert(failDto);

        for (Map<String, Object> failItem : resultFailList){
            failDto.setIfEasyadminOrderSuccDetlId(StringUtil.nvlLong(failItem.get("succId")));
            failDto.setFailMessage(StringUtil.nvl(failItem.get("failMessage")));
            ezadminOrderRegistrationMapper.addEzadminCreateOrderFailSelectInsertDetl(failDto);
        }
    }

    protected void delEzadminCreateOrderSuccess(Long ifEasyadminOrderSuccDetlId) {
        ezadminOrderRegistrationMapper.delEzadminCreateOrderSuccess(ifEasyadminOrderSuccDetlId);
        ezadminOrderRegistrationMapper.delEzadminCreateOrderSuccessDetl(ifEasyadminOrderSuccDetlId);
    }

    protected void putEZAdminOrderSuccOrderCreateYn(Long collectionMallId, Long ifEasyadminInfoId, Long reqDataId){
        ezadminOrderRegistrationMapper.putEZAdminOrderSuccOrderCreateYn(collectionMallId, ifEasyadminInfoId, reqDataId);
    }

    protected void failCreateOrderByEZAdminOrderDtoList(List<EZAdminOrderDto> ezAdminOrderDtoList, String failMessage) {
        Long ifEasyadminOrderSuccDetlId = StringUtil.nvlLong(ezAdminOrderDtoList.get(0).getIfEasyadminOrderSuccDetlId());
        
        // 1. 실패테이블에 저장
        EZAdminCreateOrderFailDto failDto = new EZAdminCreateOrderFailDto();
        failDto.setIfEasyadminOrderSuccDetlId(ifEasyadminOrderSuccDetlId);
        failDto.setFailMessage(failMessage);
        failDto.setFailType(OutmallEnums.OutmallFailType.BATCH.getCode());  // 실패구분(B:배치)
        ezadminOrderRegistrationMapper.addEzadminCreateOrderFailSelectInsert(failDto);

        for (EZAdminOrderDto dto : ezAdminOrderDtoList){
            failDto.setIfEasyadminOrderSuccDetlId(StringUtil.nvlLong(dto.getIfEasyadminOrderSuccDetlId()));
            ezadminOrderRegistrationMapper.addEzadminCreateOrderFailSelectInsertDetl(failDto);
        }

        // 2. 성공테이블의 정보 삭제
        ezadminOrderRegistrationMapper.delEzadminCreateOrderSuccess(ifEasyadminOrderSuccDetlId);
        ezadminOrderRegistrationMapper.delEzadminCreateOrderSuccessDetl(ifEasyadminOrderSuccDetlId);

    }

    protected List<Long> getIfEasyadminInfoReqDataIdList(long ifEasyadminInfoId){
        return ezadminOrderRegistrationMapper.getIfEasyadminInfoReqDataIdList(ifEasyadminInfoId);
    }

}
