package kr.co.pulmuone.v1.comm.mappers.batch.master.inside;

import kr.co.pulmuone.v1.batch.order.etc.dto.TrackingNumberOrderInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadSuccessVo;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailConsultRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderConsultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClaimExcelUploadMapper {

    /**
     * 클레임 생성 대상 PK조회
     * @param batchStatusCd
     * @return
     * @throws Exception
     */
    List<ClaimInfoExcelUploadDto> getClaimExcelTargetList(String batchStatusCd) throws Exception;

    /**
     * 클레임 생성 대상 엑셀 업로드 성공정보 조회
     * @param ifIfDayExcelInfoId
     * @return
     * @throws Exception
     */
    List<ClaimInfoExcelUploadSuccessVo> getClaimChangeSuccExcelTargetList(long ifIfDayExcelInfoId) throws Exception;

    /**
     * 클레임 엑셀업로드 변경 정보 수정
     * @param claimInfoExcelUploadInfoVo
     * @return
     * @throws Exception
     */
    int putClaimChangeExcelInfo(ClaimInfoExcelUploadInfoVo claimInfoExcelUploadInfoVo) throws Exception;

    /**
     * odid로 주문 정보 조회
     * @param odid
     * @return
     * @throws Exception
     */
    OrderClaimRegisterRequestDto getOrderInfoByOdid(String odid) throws Exception;

    /**
     * odid로 주문 정보 조회 (송장배치)
     * @param odid
     * @return
     * @throws Exception
     */
    OrderClaimRegisterRequestDto getOrderInfoByTarckingNumber(@Param(value = "odid") String odid, @Param(value = "odClaimId") long odClaimId) throws Exception;

    /**
     * psClaimBosId 귀책구분 조회
     * @param psClaimBosId
     * @return
     * @throws Exception
     */
    String getClaimTargetTpByPsClaimBosId(long psClaimBosId) throws Exception;

    /**
     * 클레임 엑셀 업로드 정보로 주문 상세 정보 조회
     * @param claimInfoExcelUploadSuccessVoList
     * @return
     * @throws Exception
     */
    List<OrderClaimGoodsInfoDto> getOrderDetlInfoListByExcelUploadInfo(@Param(value = "claimInfoExcelUploadSuccessVoList") List<ClaimInfoExcelUploadSuccessVo> claimInfoExcelUploadSuccessVoList) throws Exception;

    /**
     * 클레임 엑셀 업로드 성공 정보 수정
     * @param ifClaimExcelInfoId
     * @param claimInfoExcelUploadSuccessVoList
     * @return
     * @throws Exception
     */
    int putIfClaimExcelSucc(@Param(value = "ifClaimExcelInfoId") long ifClaimExcelInfoId, @Param(value = "claimInfoExcelUploadSuccessVoList") List<ClaimInfoExcelUploadSuccessVo> claimInfoExcelUploadSuccessVoList) throws Exception;

    /**
     * 클레임 엑셀 업로드 성공 정보 수정
     * @param ifClaimExcelInfoId
     * @param claimInfoExcelUploadSuccessVoList
     * @return
     * @throws Exception
     */
    int putIfClaimExcelFail(@Param(value = "ifClaimExcelInfoId") long ifClaimExcelInfoId, @Param(value = "claimInfoExcelUploadSuccessVoList") List<ClaimInfoExcelUploadSuccessVo> claimInfoExcelUploadSuccessVoList, @Param(value="claimExcelFailMsg") String claimExcelFailMsg) throws Exception;

    /**
     * 송장정보 클레임 정보로 주문 상세 정보 조회
     * @param odid
     * @param odClaimId
     * @return
     * @throws Exception
     */
    List<OrderClaimGoodsInfoDto> getOrderDetlInfoListByTarckingNumber(@Param(value = "odid") String odid, @Param(value = "odClaimId") long odClaimId) throws Exception;

    /**
     * 주문 상담 내용 등록
     * @param orderConsultVo
     * @return
     * @throws Exception
     */
    int addOrderConsult(OrderConsultVo orderConsultVo) throws Exception;
}
