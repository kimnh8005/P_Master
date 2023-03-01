package kr.co.pulmuone.v1.batch.order.escrow;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.order.escrow.dto.EscrowRegistDeliveryDto;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgAccountType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.escrow.OrderEscrowBatchMapper;
import kr.co.pulmuone.v1.pg.dto.EscrowRegistDeliveryDataRequestDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
@Service
public class EscrowRegistDeliveryBatchService {

	@Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private SqlSessionTemplate masterSqlSession;

    private OrderEscrowBatchMapper orderEscrowBatchMapper;

    @Autowired
	private PgBiz pgBiz;


    /**
     * 에크스로 배송등록 위한 대상 데이타 조회
     */
    public void runEscrowRegistDelivery() throws Exception{

    	this.orderEscrowBatchMapper = masterSqlSession.getMapper(OrderEscrowBatchMapper.class);

        // 대상자 조회
        List<EscrowRegistDeliveryDto> targetList = orderEscrowBatchMapper.getTagetEscrowRegistDelivery();

        if(CollectionUtils.isNotEmpty(targetList)) {

        	for(EscrowRegistDeliveryDto dto : targetList) {

        		// 에스크로 배송등록 요청 파라미터 세팅
        		EscrowRegistDeliveryDataRequestDto reqDto = setEscrowRegistDeliveryDataRequestDto(dto);

        		// 에스크로 배송등록
        		String pgServiceTypeByPgAccountType = PgAccountType.findByCode(dto.getPgService()).getPgServiceType();
        		PgServiceType pgServiceType = PgServiceType.findByCode(pgServiceTypeByPgAccountType);
				PgAbstractService pgService = pgBiz.getService(pgServiceType);
        		pgService.escrowRegistDelivery(reqDto);

        		// 에스크로 통신완료 상태값 업데이트
        		orderEscrowBatchMapper.putEscrowConnectSuccess(dto.getOdPaymentMasterId());
        	}
        }

    }

    /**
     * 에크스로 배송등록 위한 요청 파라미터 세팅
     */
    public EscrowRegistDeliveryDataRequestDto setEscrowRegistDeliveryDataRequestDto(EscrowRegistDeliveryDto escrowRegistDeliveryDto) {
    	EscrowRegistDeliveryDataRequestDto reqDto = new EscrowRegistDeliveryDataRequestDto();
    	reqDto.setTid(escrowRegistDeliveryDto.getTid());
    	reqDto.setOdid(escrowRegistDeliveryDto.getOdid());
    	reqDto.setPaymentPrice(escrowRegistDeliveryDto.getPaymentPrice());
    	reqDto.setTrackingNo(escrowRegistDeliveryDto.getTrackingNo());
    	reqDto.setRegistTrakingNoUserName(escrowRegistDeliveryDto.getRegistTrakingNoUserName());
    	reqDto.setShippingCompanyName(escrowRegistDeliveryDto.getShippingCompanyName());
    	reqDto.setInicisSshippingCompanyCode(escrowRegistDeliveryDto.getInicisSshippingCompanyCode());
    	reqDto.setRegistTrackingNoDate(escrowRegistDeliveryDto.getRegistTrackingNoDate());
    	reqDto.setReceiverName(escrowRegistDeliveryDto.getReceiverName());
    	reqDto.setReceiverMobile(escrowRegistDeliveryDto.getReceiverMobile());
    	reqDto.setReceiverZipCode(escrowRegistDeliveryDto.getReceiverZipCode());
    	reqDto.setReceiverAddress1(escrowRegistDeliveryDto.getReceiverAddress1());

    	//송신자 정보 조회 후 세팅
    	BusinessInformationVo businessInformationVo = orderEscrowBatchMapper.getBizInfoForOrderEscrow();
    	reqDto.setSendName(businessInformationVo.getBusinessName());
    	reqDto.setSendTel(businessInformationVo.getServiceCenterPhoneNumber());
    	reqDto.setSendZipCode(businessInformationVo.getZipCode());
    	reqDto.setSendAddress1(businessInformationVo.getAddress());

    	return reqDto;
    }

}

