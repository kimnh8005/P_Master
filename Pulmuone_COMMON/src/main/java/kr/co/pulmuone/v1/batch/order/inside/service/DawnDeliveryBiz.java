package kr.co.pulmuone.v1.batch.order.inside.service;

import java.util.List;

import kr.co.pulmuone.v1.batch.order.inside.dto.DawnDeliveryInfoDto;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.DawnDeliveryAreaVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 새벽권역 sFTP 배치 Biz
 * </PRE>
 */

public interface DawnDeliveryBiz {

	/**
	 * 임시 데이터가 존재하는 조회
	 * @return
	 */
	int getDawnDelivderyTempCheck();

	/**
	 * CJ 새벽권역 sFTP 배치
	 */
	ApiResult<?> batchDwanDeliveryArea() throws BaseException, Exception;

    /**
	 * 새벽배송권역 삭제할 데이터 조회
	 * @return
	 */
	List<DawnDeliveryAreaVo> getDawnDelivderyRemoveInfoList() ;

	/**
	 * 새벽배송권역 추가할 데이터 조회
	 * @return
	 */
	List<DawnDeliveryAreaVo> getDawnDelivderyAddInfoList();

	/**
	 * 임시 새벽배송권역을 추가한다.
	 * @param dawnDeliveryVo
	 * @return
	 */
	int addDawnDeliveryTemp(DawnDeliveryInfoDto dawnDeliveryInfoDto);

	/**
	 * 새벽배송권역을 추가한다.
	 * @param dawnDeliveryVo
	 * @return
	 */
	int addDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto);

	/**
	 * 새벽배송권역을 삭제한다.
	 * @param dawnDeliveryVo
	 * @return
	 */
	int deleteDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto);

	/**
	 * 새벽배송권역 이력을 저장한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	int addDawnDeliveryHist(DawnDeliveryInfoDto dawnDeliveryInfoDto);

	/**
	 * 새벽배송권역임시 데이터를 삭제한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	int deleteDawnDeliveryTemp();

}
