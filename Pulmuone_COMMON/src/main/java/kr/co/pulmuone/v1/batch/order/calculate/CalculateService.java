package kr.co.pulmuone.v1.batch.order.calculate;

import kr.co.pulmuone.v1.batch.order.calculate.dto.ErpIfSalSrchLineResponseDto;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleEmployeeMasterVo;
import kr.co.pulmuone.v1.batch.order.calculate.dto.vo.CaSettleOuMngVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.calculate.CalculateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateService {

    private final CalculateMapper calculateMapper;

    /**
     * 전일 임직원정산 마감여부 체크
     * @param
     * @return int
     * @throws BaseException
     */
    protected int selectDayBeforeEmployeeCalculateDeadlineCheck() throws BaseException {
        return calculateMapper.selectDayBeforeEmployeeCalculateDeadlineCheck();
    }

    /**
     * 전일 임직원정산 일마감
     * @param
     * @return void
     * @throws BaseException
     */
    protected void addDayBeforeEmployeeCalculateDayDeadline() throws BaseException {
        calculateMapper.addDayBeforeEmployeeCalculateDayDeadline();
    }

    /**
     * 정산용 OU 목록 조회
     * @param
     * @return List<CaSettleOuMngVo>
     * @throws BaseException
     */
    protected List<CaSettleOuMngVo> selectCalculateOuList() throws BaseException {
        return calculateMapper.selectCalculateOuList();
    }

    /**
     * 당월 임직원정산 마스터 저장여부 체크
     * @param ouId
     * @return int
     * @throws BaseException
     */
    protected int selectEmployeeCalculateMasterCheck(String ouId) throws BaseException {
        return calculateMapper.selectEmployeeCalculateMasterCheck(ouId);
    }

    /**
     * 임직원정산 마스터 저장
     * @param ouId
     * @return void
     * @throws BaseException
     */
    protected void addEmployeeCalculateMaster(String ouId) throws BaseException {
        calculateMapper.addEmployeeCalculateMaster(ouId);
    }

    /**
     * 임직원정산 마스터 수정
     * @param ouId
     * @return void
     * @throws BaseException
     */
    protected void putEmployeeCalculateMaster(String ouId) throws BaseException {
        calculateMapper.putEmployeeCalculateMaster(ouId);
    }

    /**
     * 정산을 위한 임직원 정보 저장
     * @param
     * @return void
     * @throws BaseException
     */
    protected void addEmployeeCalculateInfo() throws BaseException {
        calculateMapper.addEmployeeCalculateInfo();
    }

    /**
     * 정산을 위한 임직원 정보 삭제
     * @param
     * @return void
     * @throws BaseException
     */
    protected void putEmployeeCalculateInfo() throws BaseException {
        calculateMapper.putEmployeeCalculateInfo();
    }

    /**
     * 매출확정된 데이터 저장
     * @param lineResponseDto
     * @param hdrTyp
     * @return void
     * @throws BaseException
     */
    protected void addSalesConfirm(ErpIfSalSrchLineResponseDto lineResponseDto, String hdrTyp) throws BaseException {
        calculateMapper.addSalesConfirm(lineResponseDto, hdrTyp);
    }

    /**
     * 하이톡 매출확정된 데이터 저장
     * @param lineResponseDto
     * @param hdrTyp
     * @return void
     * @throws BaseException
     */
    protected void addHitokSalesConfirm(ErpIfSalSrchLineResponseDto lineResponseDto, String hdrTyp) throws BaseException {
        calculateMapper.addHitokSalesConfirm(lineResponseDto, hdrTyp);
    }

    /**
     * 임직원 정산 월마감 대상 조회
     * @return List<CaSettleEmployeeMasterVo>
     * @throws BaseException
     */
    protected List<CaSettleEmployeeMasterVo> selectEmployeeCalculateMonthDeadlineList() throws BaseException {
        return calculateMapper.selectEmployeeCalculateMonthDeadlineList();
    }

}