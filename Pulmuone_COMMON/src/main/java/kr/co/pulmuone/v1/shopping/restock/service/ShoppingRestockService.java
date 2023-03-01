package kr.co.pulmuone.v1.shopping.restock.service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.shopping.restock.ShoppingRestockMapper;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.shopping.restock.dto.vo.ShoppingRestockVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingRestockService {

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    private final ShoppingRestockMapper shoppingRestockMapper;

    /**
     * 재입고 알림 요청
     * @param ilGoodsId
     * @param urUserId
     * @return
     * @throws Exception
     */
    protected int putRetockInfo(Long ilGoodsId, String urUserId) throws Exception {
        int result=0;
        if(shoppingRestockMapper.getDupCnt(ilGoodsId, urUserId) < 1){
            result = shoppingRestockMapper.putRetockInfo(ilGoodsId, urUserId);
        }
        return result;
    }


    /**
     * 재입고 알림 배치
     */
    public void runRestock() throws BaseException {

        //1. 30일 지난 재입고 요청 정보 삭제
        clearInfoRecently();

        //2. 대상 조회 후 SMS 발송 처리
        sendRestock();

    }


    /**
     * 30일 지난 재입고 요청 정보 삭제
     */
    protected void clearInfoRecently() {
        // 삭제 대상 조회
        List<Long> spRestockIdList = shoppingRestockMapper.getClearStockIdList();
        if (spRestockIdList == null || spRestockIdList.size() == 0) return;

        if (spRestockIdList.size() > 0) {
            shoppingRestockMapper.delRestockIdList(spRestockIdList);
        }
    }

    /**
     * 상품 재입고 시 SMS 발송처리
     */
    protected void sendRestock() throws BaseException  {
        // SMS 발송 대상 조회
        List<ShoppingRestockVo> spRestockIdList = shoppingRestockMapper.getSendGoodsIdList();
        List<Long> spRestockIdArray = new ArrayList<>();
        if (spRestockIdList == null || spRestockIdList.size() == 0) return;

        if (spRestockIdList.size() > 0) {
            // SMS 발송 서비스 호출
//            shoppingRestockBatchMapper.sendRestockAlarm(spRestockIdList);
            for(ShoppingRestockVo restockVo : spRestockIdList) {
                // 임시 테스트 서비스 호출
                sendRestockSms(restockVo);
                spRestockIdArray.add(restockVo.getSpRestockId());
            }
            // 재입고 알림 상태 Update
            shoppingRestockMapper.putRestockStatus(spRestockIdArray);
        }

    }

    /**
     * SMS 발송 처리 예정
     * @throws BaseException
     */
    protected void sendRestockSms(ShoppingRestockVo restockVo) throws BaseException {

        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.GOODS_RESTOCK_INFO.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

        //SMS 발송
        String reserveDate = ""; // 예약발송일자
        String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)


        String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, restockVo);
        String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
        AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                .content(content)
                .urUserId(restockVo.getUrUserId())
                .mobile(restockVo.getMobile())
                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                .reserveYn(reserveYn)
                .reserveDate(reserveDate)
                .build();

        sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    }

}
