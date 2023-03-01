package kr.co.pulmuone.v1.comm.mappers.batch.master.monitoring;

import kr.co.pulmuone.v1.batch.monitoring.dto.vo.MonitoringSmsSendResultVo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MonitoringSmsSendBatchMapper {

    /**
     * 회원가입자수, 주문건수, 결제금액 table에 insert
     * @param
     * @return void
     */
    void addUserOrderPaymentInfo();

    /**
     * 회원가입자수, 주문건수, 결제금액 데이터 조회
     * @param
     * @return MonitoringSmsSendResultVo
     */
    MonitoringSmsSendResultVo getUserOrderPaymentInfo();


    /**
     * 전송된 데이터 전송여부 Y 업데이트
     */
    void upUserOrderPaymentInfo();

    /**
     * 회원가입자수, 주문건수, 결제금액 데이터 조회 (어제 하루 종일 데이터)
     * @param
     * @return MonitoringSmsSendResultVo
     */
    MonitoringSmsSendResultVo getUserOrderPaymentInfoYesterdayAll();
}
