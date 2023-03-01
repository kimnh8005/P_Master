package kr.co.pulmuone.v1.comm.mapper.calculate.collation;

import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CalSalesMapper {

    /**
     * 통합몰 매출 대사 리스트 조회
     *
     * @param dto CalSalesListRequestDto
     * @return Page<CalSalesListDto>
     */
    List<CalSalesListDto> getSalesList(CalSalesListRequestDto dto);


    /**
     * 통합몰 매출 대사 리스트 엑셀 다운로드
     *
     * @param dto CalSalesListRequestDto
     * @return List<CalSalesListDto>
     */
    List<CalSalesListDto> getSalesExcelList(CalSalesListRequestDto dto);

    /**
     * 통합몰 매출 대사 리스트 조회 카운트 조회
     * @param dto CalSalesListRequestDto
     * @return
     */
    CalSalesListDto getSalesListCount(CalSalesListRequestDto dto);

}
