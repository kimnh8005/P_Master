package kr.co.pulmuone.v1.batch.order.inside;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.inside.ClaimExcelUploadMapper;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimReShippingGoodsPaymentInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadSuccessVo;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailConsultRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderConsultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimExcelUploadService {

    private final ClaimExcelUploadMapper claimExcelUploadMapper;

    @Autowired
    private ClaimProcessBiz claimProcessBiz;

    protected void putClaimExcelUpload() throws Exception {
        
        // 엑셀 업로드 상태가 배치 대기중인 건 조회
        List<ClaimInfoExcelUploadDto> list = claimExcelUploadMapper.getClaimExcelTargetList(OutmallEnums.OutmallBatchStatusCd.READY.getCode());
        int updateCount = 0;
        ClaimInfoExcelUploadInfoVo claimInfoExcelUploadInfoVo = new ClaimInfoExcelUploadInfoVo();
        for(ClaimInfoExcelUploadDto item : list) {

            log.info("---------------------------------------- 클레임 엑셀 업로드 상태 업데이트 처리 [" + item.getIfClaimExcelInfoId() + "] ING");
            claimInfoExcelUploadInfoVo.setIfClaimExcelInfoId(item.getIfClaimExcelInfoId());
            claimInfoExcelUploadInfoVo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.ING.getCode());
            LocalDateTime startTime = LocalDateTime.now();
            claimInfoExcelUploadInfoVo.setBatchStartDateTime(startTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));

            claimExcelUploadMapper.putClaimChangeExcelInfo(claimInfoExcelUploadInfoVo);

            updateCount = 0;
            // 엑셀 업로드 상태가 배치 대기중인 건의 성공정보 조회
            List<ClaimInfoExcelUploadSuccessVo> succList = claimExcelUploadMapper.getClaimChangeSuccExcelTargetList(item.getIfClaimExcelInfoId());
            // 주문번호, 취소구분, 회수여부로 Grouping
            Map<String, List<ClaimInfoExcelUploadSuccessVo>> groupOdid = succList.stream().collect(Collectors.groupingBy(ClaimInfoExcelUploadSuccessVo::getExcelGroup, LinkedHashMap::new, Collectors.toList()));
            for(String groupItem : groupOdid.keySet()) {
                List<ClaimInfoExcelUploadSuccessVo> items = groupOdid.get(groupItem);
                try {
                    // 주문상세번호로 Sort
                    items.sort(new Comparator<ClaimInfoExcelUploadSuccessVo>() {
                        @Override
                        public int compare(ClaimInfoExcelUploadSuccessVo dto1, ClaimInfoExcelUploadSuccessVo dto2) {
                            return (dto1.getOdOrderDetlSeq() <= dto2.getOdOrderDetlSeq() ? -1 : 1);
                        }
                    });

                    String[] groupItemSplit = groupItem.split(Constants.ARRAY_SEPARATORS);
                    String odid = groupItemSplit[0];            // 주문번호
                    String claimStatusTp = groupItemSplit[1];   // 클레임상태구분
                    String returnsYn = groupItemSplit[2];       // 회수여부
                    String overCount = groupItemSplit.length > 3?groupItemSplit[3]:"";
                    // 주문번호가 조회되지 않는 경우
                    if (odid.isEmpty()) {
                        // ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_ORDER.getMessage();
                        claimExcelUploadMapper.putIfClaimExcelFail(item.getIfClaimExcelInfoId(), items, MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_ORDER.getMessage(), odid));
                        continue; // 이후 로직 처리안함
                    }
                    // 클레임 상태 구분이 없는 경우
                    if (claimStatusTp.isEmpty()) {
                        claimExcelUploadMapper.putIfClaimExcelFail(item.getIfClaimExcelInfoId(), items, MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NOT_EXISTS_CLAIM_STATUS_TP.getMessage(), ""));
                        continue; // 이후 로직 처리안함
                    }
                    // 취소가능수량을 초과한 경우
                    if (overCount.startsWith("F")) {
                        claimExcelUploadMapper.putIfClaimExcelFail(item.getIfClaimExcelInfoId(), items, MessageFormat.format(ExcelUploadValidateEnums.ValidateType.CLAIM_COUNT_OVER.getMessage(), overCount.substring(1)));
                        continue; // 이후 로직 처리안함
                    }
                    String odStatusCd = groupItemSplit[4];      // 현재 주문 상태

                    long psClaimBosId = items.get(0).getPsClaimBosId(); // BOS클레임 사유

                    // odid로 주문정보 조회
                    OrderClaimRegisterRequestDto claimReqInfo = claimExcelUploadMapper.getOrderInfoByOdid(odid);

                    claimStatusTp = OrderClaimEnums.ClaimStatusTp.findByCodeNm(claimStatusTp).getCode();
                    // 클레임상태구분 Set
                    claimReqInfo.setClaimStatusTp(claimStatusTp);

                    // 귀책구분 Set
                    String targetTp = claimExcelUploadMapper.getClaimTargetTpByPsClaimBosId(psClaimBosId);
                    claimReqInfo.setTargetTp(targetTp);

                    String claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode();

                    // String claimStatusCd = OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode();
                    // 클레임상태구분이 반품일 경우 클레임상태코드 반품 완료 처리
                    if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
                        claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode();
                        // 회수 여부가 Y일 경우 반품 승인 처리
                        if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(returnsYn)) {
                            claimStatusCd = OrderEnums.OrderStatus.RETURN_ING.getCode();
                        }
                    }

                    // 입금대기 상태인 케이스
                    if(OrderEnums.OrderStatus.INCOM_READY.getCode().equals(odStatusCd)) {
                        // 입금대기상태인데 취소 클레임 케이스
                        claimStatusCd = OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode();
                    }

                    // 클레임상태코드 Set
                    claimReqInfo.setClaimStatusCd(claimStatusCd);
                    // 반품여부 Set
                    claimReqInfo.setReturnsYn(returnsYn);

                    // odid, 주문상세순번으로 주문 상세정보 조회
                    List<OrderClaimGoodsInfoDto> goodsInfoList = claimExcelUploadMapper.getOrderDetlInfoListByExcelUploadInfo(items);
                    // 클레임 상품 상세 정보 Set
                    claimReqInfo.setGoodsInfoList(goodsInfoList);
                    claimReqInfo.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
                    claimReqInfo.setCustomUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
                    claimReqInfo.setFrontTp(2);

                    // 클레임생성
                    claimProcessBiz.addOrderClaim(claimReqInfo);

                    // 고객상담내용 등록
                    String odConsultMsg = items.stream().map(x -> x.getConsultMsg()).distinct().collect(Collectors.joining(","));
                    OrderConsultVo orderConsultVo = new OrderConsultVo();
                    orderConsultVo.setOdOrderId(claimReqInfo.getOdOrderId());   // 주문 PK
                    orderConsultVo.setOdConsultMsg(odConsultMsg);               // 상담메시지
                    orderConsultVo.setCreateId(Constants.BATCH_CREATE_USER_ID); // 상담등록자
                    claimExcelUploadMapper.addOrderConsult(orderConsultVo);

                    updateCount += groupOdid.get(groupItem).size();

                    // 클레임 엑셀 업로드 성공 정보 수정
                    claimExcelUploadMapper.putIfClaimExcelSucc(item.getIfClaimExcelInfoId(), items);
                }
                catch (Exception e){
                    if(items != null && items.size() > 0) {
                        claimExcelUploadMapper.putIfClaimExcelFail(item.getIfClaimExcelInfoId(), items, e.getMessage());
                    }
                    log.error(e.getMessage());
                }
            }

            log.info("---------------------------------------- 클레임 엑셀 업로드 상태 업데이트 처리 [" + item.getIfClaimExcelInfoId() + "] END");

            LocalDateTime endTime = LocalDateTime.now();
            claimInfoExcelUploadInfoVo.setBatchEndDateTime(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
            claimInfoExcelUploadInfoVo.setBatchExecutionTime(String.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)));
            claimInfoExcelUploadInfoVo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.END.getCode());
            claimInfoExcelUploadInfoVo.setUpdateCount(updateCount);
            claimExcelUploadMapper.putClaimChangeExcelInfo(claimInfoExcelUploadInfoVo);
        }
    }

}