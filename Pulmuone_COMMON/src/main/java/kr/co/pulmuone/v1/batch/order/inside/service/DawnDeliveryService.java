package kr.co.pulmuone.v1.batch.order.inside.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.order.inside.dto.DawnDeliveryInfoDto;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.DawnDeliveryAreaVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.inside.DwanDeliveryMapper;




/**
 * <PRE>
 * Forbiz Korea
 *  CJ 새벽권역 sFTP 배치 Service
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class DawnDeliveryService {

    private final DwanDeliveryMapper dwanDeliveryMapper;

	/**
	 * 임시 데이터가 존재하는 조회
	 * @return
	 */
	protected int getDawnDelivderyTempCheck() {
		return dwanDeliveryMapper.getDawnDelivderyTempCheck();
	}

    /**
	 * 새벽배송권역 삭제할 데이터 조회
	 * @return
	 */
	protected List<DawnDeliveryAreaVo> getDawnDelivderyRemoveInfoList() {
		return dwanDeliveryMapper.getDawnDelivderyRemoveInfoList();
	}

	/**
	 * 새벽배송권역 추가할 데이터 조회
	 * @return
	 */
	protected List<DawnDeliveryAreaVo> getDawnDelivderyAddInfoList() {
		return dwanDeliveryMapper.getDawnDelivderyAddInfoList();
	}

	/**
	 * 임시 새벽배송권역을 추가한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	public int addDawnDeliveryTemp(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dwanDeliveryMapper.addDawnDeliveryTemp(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역을 추가한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	protected int addDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dwanDeliveryMapper.addDawnDelivery(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역을 삭제한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	protected int deleteDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dwanDeliveryMapper.deleteDawnDelivery(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역 이력을 저장한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	protected int addDawnDeliveryHist(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dwanDeliveryMapper.addDawnDeliveryHist(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역임시 데이터를 삭제한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	protected int deleteDawnDeliveryTemp() {
		return dwanDeliveryMapper.deleteDawnDeliveryTemp();
	}

}
