package kr.co.pulmuone.v1.comm.mappers.batch.master.monitoring;

import kr.co.pulmuone.v1.batch.monitoring.dto.vo.YunginNotiSmsSendVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YunginNotiSmsSendBatchMapper {

    /**
     * 용인물류 D1, D2출고 주문 택배, 새벽을 기준으로 주문건수(냉동건수), 상품수량 데이터 조회
     * @param
     * @return YunginNotiSmsSendVo
     */
    YunginNotiSmsSendVo getYunginNotiOrderInfo(int ifDt);
}
