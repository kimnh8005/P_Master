package kr.co.pulmuone.v1.batch.order.ezadmin;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminCreateOrderFailDto;
import kr.co.pulmuone.v1.outmall.ezadmin.service.EZAdminOrderRegistrationBiz;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminInfoDto;
import kr.co.pulmuone.v1.comm.mappers.batch.master.collectionmall.CollectionMallEZAdminBatchMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.ezadmin.EZAdminOrderMapper;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class EZAdminOrderService {

    private final EZAdminOrderMapper ezadminOrderMapper;

    private final CollectionMallEZAdminBatchMapper collectionMallEZAdminBatchMapper;

    private final EZAdminOrderRegistrationBiz eZAdminOrderRegistrationBiz;

    protected List<EZAdminOrderDto> getEZAdminOrderTargetList(List<String> syncCdList, String easyadminBatchTp) throws Exception {
    	return ezadminOrderMapper.getEZAdminOrderTargetList(syncCdList, easyadminBatchTp);
    }

    protected int putEZAdminOrderStatus(EZAdminInfoDto ezadminInfoDto) throws Exception {
    	return collectionMallEZAdminBatchMapper.putEasyAdminInfo(ezadminInfoDto);
    }

    protected void verificationOrderCreateSuccess(Long ifEasyadminInfoId) throws Exception{
        // 1. 주문생성 안된 건 조회
        List<EZAdminOrderDto> notOrderCreateList = ezadminOrderMapper.getNotOrderCreateInIfEasyadminOrderSucc(ifEasyadminInfoId);

        if(CollectionUtils.isNotEmpty(notOrderCreateList)){
            try{

                // 2. 합포번호 기준 그룹핑
                Map<String, List<EZAdminOrderDto>> notOrderCreateDto = notOrderCreateList.stream()
                        .collect(groupingBy(EZAdminOrderDto::getIfEasyadminOrderSuccId, LinkedHashMap::new,toList()));

                for(String ifEasyadminOrderSuccId : notOrderCreateDto.keySet()){

                    // 3. 실패테이블에 저장, 성공테이블에서 삭제
                    String failMessage = OutmallEnums.orderCreateMsg.ORDER_CREATE_FAIL_IN_VERIFICATION.getMessage();
                    List<EZAdminOrderDto> ezadminOrderDto = notOrderCreateDto.get(ifEasyadminOrderSuccId);
                    eZAdminOrderRegistrationBiz.failCreateOrderByEZAdminOrderDtoList(ezadminOrderDto, failMessage);
                }

            }catch(Exception e){
                e.printStackTrace();
                log.error(" EzadminOrderRegistrationBizImpl verificationOrderCreateSuccess error ==== ::{}", ifEasyadminInfoId);
            }
        }
    }

    protected int getOrderCreateCount(long ifEasyadminInfoId) throws Exception{
        return ezadminOrderMapper.getOrderCreateCount(ifEasyadminInfoId);
    }
}
