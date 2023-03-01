package kr.co.pulmuone.v1.batch.order.outmall;

import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderRegistrationBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OutmallExcelOrderBizImpl implements OutmallExcelOrderBiz {

	@Autowired
	private OutmallExcelOrderService outmallExcelOrderService;

	@Autowired
	private OutmallOrderRegistrationBiz outmallOrderRegistrationBiz;

	@Override
	public void putOutmallCreateOrder(int maxThreadCnt, String targetStatusList) throws Exception {

		// 시작시간
		LocalDateTime startTime = LocalDateTime.now();

		// 시간이 오래걸리는 배치로인해 배치당 1건으로 제한을 했지만..
		// 시간이 몇초 안걸리는 것들은 5분 대기를 타야하는 문제가 발생하여..
		// 그래서 실행시간이 4분을 넘지 않았다면 4분을 넘거가..  다음 배치내용이 없을때까지 무한으로 동작.
		while (true) {

			Thread.sleep(100);

			// 실행 그룹 갯수(한개씩만 가져오기때문에 최대가 1임.. 상세 갯수는 못가져와서 아래 시간으로 체크함.)
			int runCnt = this.runPutOutmallCreateOrder(maxThreadCnt, targetStatusList);

			// 종료시간
			LocalDateTime endTime = LocalDateTime.now();

			// 시작시간과 종료시간을 체크
			Duration duration = Duration.between(startTime, endTime);

			// 처리대상이 없을경우 또는 실행시간이 4분을 초과했을경우 배치를 종료한다.
			// 현재 코드에서만 사용하는 값이라 상수처리하지 않고 주석으로 대신함. (240 -> 240초 -> 4분)
			if(runCnt == 0 || duration.getSeconds() > 240) {
				log.info("---------------------------------------- 연속실행 시간초과로 진행 중단 ({})", duration.getSeconds());
				log.info("----- startTime : {}", startTime);
				log.info("----- endTime : {}", endTime);
				System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) + "] ############ 더이상 없어서 진짜끝낸다...");
				break;
			}

			System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) + "] ############ 다음건이 있을수 있으니 한번더 돌린다...");
		}
	}

	public int runPutOutmallCreateOrder(int maxThreadCnt, String targetStatusList) throws Exception {

		// 대기중,진행중 모두 조회
		List<String> batchStatusList = new ArrayList<>();

		// 대기중인것 가져오기
		if(targetStatusList.contains(OutmallEnums.OutmallBatchStatusCd.READY.getCode())) {
			batchStatusList.add(OutmallEnums.OutmallBatchStatusCd.READY.getCode());
		}
		// 진행중인거 가져오기
		if(targetStatusList.contains(OutmallEnums.OutmallBatchStatusCd.ING.getCode())) {
			batchStatusList.add(OutmallEnums.OutmallBatchStatusCd.ING.getCode());
		}
		// 검색조건이 없으면 대기중인것으로 셋팅
		if(batchStatusList.size() == 0) {
			batchStatusList.add(OutmallEnums.OutmallBatchStatusCd.READY.getCode());
		}

		List<OutMallOrderDto> list = outmallExcelOrderService.getOutmallExcelOrderTargetList(batchStatusList);

		OutMallExcelInfoVo outmallExcelInfo = new OutMallExcelInfoVo();
		for(OutMallOrderDto outmallOrderItem : list) {

			log.info("---------------------------------------- 외부몰 상태 업데이트 처리 [" + outmallOrderItem.getIfOutmallExcelInfoId() + "] ING");
			outmallExcelInfo.setIfOutmallExcelInfoId(outmallOrderItem.getIfOutmallExcelInfoId());
			outmallExcelInfo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.ING.getCode());
			LocalDateTime startTime = LocalDateTime.now();
			outmallExcelInfo.setBatchStartDateTime(startTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));

			outmallExcelOrderService.putOutmallExcelInfo(outmallExcelInfo);

			// 주문서 생성
			Map<String,Integer> orderCreateResult = outmallOrderRegistrationBiz.setBindOrderOrder(outmallOrderItem.getIfOutmallExcelInfoId(), startTime);

			// 주문생성 안된건 실패처리
			outmallOrderRegistrationBiz.verificationOrderCreateSuccess(outmallOrderItem.getIfOutmallExcelInfoId());

			// 주문생성 COUNT 조회
			int orderCreateCnt = outmallOrderRegistrationBiz.getOrderCreateCount(outmallOrderItem.getIfOutmallExcelInfoId());

			// 진행중 중단된 경우
			if(orderCreateResult.containsKey(OutmallEnums.OutmallBatchStatusCd.ING.getCode())){
				log.info("---------------------------------------- 외부몰 상태 업데이트 처리 [" + outmallOrderItem.getIfOutmallExcelInfoId() + "] 진행중 중단");
				LocalDateTime endTime = LocalDateTime.now();
				outmallExcelInfo.setBatchEndDateTime(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
				outmallExcelInfo.setBatchExecutionTime(String.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)));
				outmallExcelInfo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.ING.getCode());
				//outmallExcelInfo.setOrderCreateCnt(orderCreateResult.get(OutmallEnums.OutmallBatchStatusCd.ING.getCode()));
				outmallExcelInfo.setOrderCreateCnt(orderCreateCnt);

				outmallExcelOrderService.putOutmallExcelInfo(outmallExcelInfo);
				break;
			}else{
				// 완료
				log.info("---------------------------------------- 외부몰 상태 업데이트 처리 [" + outmallOrderItem.getIfOutmallExcelInfoId() + "] END");

				LocalDateTime endTime = LocalDateTime.now();
				outmallExcelInfo.setBatchEndDateTime(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
				outmallExcelInfo.setBatchExecutionTime(String.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)));
				outmallExcelInfo.setBatchStatusCd(OutmallEnums.OutmallBatchStatusCd.END.getCode());
				//outmallExcelInfo.setOrderCreateCnt(orderCreateResult.get(OutmallEnums.OutmallBatchStatusCd.END.getCode()));
				outmallExcelInfo.setOrderCreateCnt(orderCreateCnt);

				outmallExcelOrderService.putOutmallExcelInfo(outmallExcelInfo);
			}
		}

		return list.size();
	}
}