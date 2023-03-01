package kr.co.pulmuone.v1.comm.mappers.batch.master.order.calculate;

import kr.co.pulmuone.v1.batch.order.calculate.dto.ErpIfSalSrchLineResponseDto;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleEmployeeMasterVo;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleOuMngVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산 배치 Mapper
 * </PRE>
 */

@Mapper
public interface CalculateMapper {

    /**
     * 전일 임직원정산 마감여부 체크
     * @param
     * @return int
     * @throws BaseException
     */
    int selectDayBeforeEmployeeCalculateDeadlineCheck() throws BaseException;

    /**
     * 전일 임직원정산 일마감
     * @param
     * @return void
     * @throws BaseException
     */
    void addDayBeforeEmployeeCalculateDayDeadline() throws BaseException;

    /**
     * 정산용 OU 목록 조회
     * @param
     * @return List<CaSettleOuMngVo>
     * @throws BaseException
     */
    List<CaSettleOuMngVo> selectCalculateOuList() throws BaseException;

    /**
     * 당월 임직원정산 마스터 저장여부 체크
     * @param ouId
     * @return int
     * @throws BaseException
     */
    int selectEmployeeCalculateMasterCheck(@Param("ouId") String ouId) throws BaseException;

    /**
     * 임직원정산 마스터 저장
     * @param ouId
     * @return void
     * @throws BaseException
     */
    void addEmployeeCalculateMaster(@Param("ouId") String ouId) throws BaseException;

    /**
     * 임직원정산 마스터 수정
     * @param ouId
     * @return void
     * @throws BaseException
     */
    void putEmployeeCalculateMaster(@Param("ouId") String ouId) throws BaseException;

    /**
     * 정산을 위한 임직원 정보 저장
     * @param
     * @return void
     * @throws BaseException
     */
    void addEmployeeCalculateInfo() throws BaseException;

    /**
     * 정산을 위한 임직원 정보 삭제
     * @param
     * @return void
     * @throws BaseException
     */
    void putEmployeeCalculateInfo() throws BaseException;

    /**
     * 매출확정된 데이터 저장
     * @param lineResponseDto
     * @param hdrTyp
     * @return void
     * @throws BaseException
     */
    void addSalesConfirm(@Param("lineResponseDto") ErpIfSalSrchLineResponseDto lineResponseDto, @Param("hdrTyp") String hdrTyp) throws BaseException;

    /**
     * 하이톡 매출확정된 데이터 저장
     * @param lineResponseDto
     * @param hdrTyp
     * @return void
     * @throws BaseException
     */
    void addHitokSalesConfirm(@Param("lineResponseDto") ErpIfSalSrchLineResponseDto lineResponseDto, @Param("hdrTyp") String hdrTyp) throws BaseException;

    /**
     * 임직원 정산 월마감 대상 조회
     * @return List<CaSettleEmployeeMasterVo>
     * @throws BaseException
     */
    List<CaSettleEmployeeMasterVo> selectEmployeeCalculateMonthDeadlineList() throws BaseException;

}
