package kr.co.pulmuone.v1.user.warehouse.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.user.warehouse.WarehouseHolidayMapper;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseHolidayService {

	private final WarehouseHolidayMapper warehouseHolidayMapper;


    /**
     * 출고처 휴일 설정 목록 조회
     * @param warehouseHolidayRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getWarehouseHolidayList(warehouseHolidayRequestDto);
    }

    /**
     * Scheduler 출고처 설정 목록
     * @param getScheduleWarehouseHolidayList
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getScheduleWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getScheduleWarehouseHolidayList(warehouseHolidayRequestDto);
    }


    /**
     * 출고처 설정 대상 목록
     * @param warehouseHolidayRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getWarehouseSetList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getWarehouseSetList(warehouseHolidayRequestDto);
    }

    /**
     * 출고처 설정 대상 수정 목록
     * @param warehouseHolidayRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getWarehouseHolidayDetail(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getWarehouseHolidayDetail(warehouseHolidayRequestDto);
    }



    /**
     * 출고처 설정 확정 목록
     * @param warehouseHolidayRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getConfirmWarehouseHolidayList(warehouseHolidayRequestDto);
    }


    /**
     * 휴일리스트
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exception
     */
    protected ArrayList<String> getHolidays(String fromDate, String toDate) throws Exception{

        final String DATE_PATTERN = "yyyy-MM-dd";
        String inputStartDate = fromDate;
        String inputEndDate = toDate;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date startDate = sdf.parse(inputStartDate);
        Date endDate = sdf.parse(inputEndDate);
        ArrayList<String> dates = new ArrayList<String>();
        Date currentDate = startDate;
        while (currentDate.compareTo(endDate) <= 0) {
        	dates.add(sdf.format(currentDate));
        	Calendar c = Calendar.getInstance();
        	c.setTime(currentDate);
        	c.add(Calendar.DAY_OF_MONTH, 1);
        	currentDate = c.getTime();
        }

    	return dates;
    }


    /**
     * 출고처 휴일설정 등록
     */
    protected ApiResult<?>  addWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception{

    	ArrayList<String> dates = new ArrayList<String>();
        dates = getHolidays(saveWarehouseHolidayRequestDto.getFromDate(), saveWarehouseHolidayRequestDto.getToDate());

        List<WarehouseHolidayResultVo> dataList = saveWarehouseHolidayRequestDto.getInsertSaveDataList();
        List<WarehouseHolidayResultVo> insertDataList = new ArrayList<WarehouseHolidayResultVo>();
        //입력할 출고처 데이터 리스트에 휴일 설정 처리
        for(int i=0;i<dataList.size();i++) {
        	WarehouseHolidayResultVo resultVo = new WarehouseHolidayResultVo();
        	resultVo = dataList.get(i);

        	//휴일 지정
        	for (String date : dates) {
        		WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();
        		vo.setUrWarehouseId(resultVo.getUrWarehouseId());
        		if(resultVo.getDawnYnName().equals("-")) {
        			vo.setDawnYn("N");
        		}else {
        			vo.setDawnYn(resultVo.getDawnYn());
        		}
        		vo.setHoliday(date);
        		if(saveWarehouseHolidayRequestDto.getUrUserId() != null) {
        			vo.setUrUserId(saveWarehouseHolidayRequestDto.getUrUserId());
        		}else {
        			vo.setUrUserId(saveWarehouseHolidayRequestDto.getUserVo().getUserId());
        		}
        		if(saveWarehouseHolidayRequestDto.getFromDate().equals(saveWarehouseHolidayRequestDto.getToDate())) {
        			vo.setGroupYn("N");
        		}else {
        			vo.setGroupYn("Y");
        		}
                int hldyCount = this.getDuplicateHldy(vo);

        		// 추가된 출고처 휴일 등록 확인
                if(hldyCount < 1) {
                    insertDataList.add(vo);
                }
        	}
        }

        saveWarehouseHolidayRequestDto.setInsertSaveDataList(insertDataList);

        if (!saveWarehouseHolidayRequestDto.getInsertSaveDataList().isEmpty()) {
        	warehouseHolidayMapper.addWarehouseHoliday(saveWarehouseHolidayRequestDto);
        }


        return ApiResult.success();
    }

    /**
     * 출고처 휴일설정 수정
     */
    protected ApiResult<?>  putWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception{

        ArrayList<String> delDates = new ArrayList<String>();
        delDates = getHolidays(saveWarehouseHolidayRequestDto.getOldFromDate(), saveWarehouseHolidayRequestDto.getOldToDate());

        WarehouseHolidayRequestDto warehouseHolidayRequestDto = new WarehouseHolidayRequestDto();
        warehouseHolidayRequestDto.setFromDate(saveWarehouseHolidayRequestDto.getOldFromDate());
        warehouseHolidayRequestDto.setToDate(saveWarehouseHolidayRequestDto.getOldToDate());
        warehouseHolidayRequestDto.setHoliday(saveWarehouseHolidayRequestDto.getHoliday());
        warehouseHolidayRequestDto.setDawnYn(saveWarehouseHolidayRequestDto.getDawnYn());
        warehouseHolidayRequestDto.setGroupYn(saveWarehouseHolidayRequestDto.getGroupYn());
        warehouseHolidayRequestDto.setUrWarehouseId(saveWarehouseHolidayRequestDto.getUrWarehouseId());
        List<WarehouseHolidayResultVo> delConfirmHolidayList = warehouseHolidayMapper.getOldConfirmWarehouseHolidayList(warehouseHolidayRequestDto);


        // 변경전 데이터 삭제
        for(int i=0;i<delConfirmHolidayList.size();i++) {
        	WarehouseHolidayResultVo resultVo = new WarehouseHolidayResultVo();
        	resultVo = delConfirmHolidayList.get(i);

        	//휴일 지정
        	for (String date : delDates) {
        		WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();
        		vo.setUrWarehouseId(resultVo.getUrWarehouseId());
        		vo.setDawnYn(resultVo.getDawnYn());
        		vo.setHoliday(date);
        		vo.setGroupYn(resultVo.getGroupYn());
        		warehouseHolidayMapper.delWarehouseHoliday(vo);
        	}
        }


        ArrayList<String> dates = new ArrayList<String>();
        dates = getHolidays(saveWarehouseHolidayRequestDto.getFromDate(), saveWarehouseHolidayRequestDto.getToDate());

        List<WarehouseHolidayResultVo> dataList = saveWarehouseHolidayRequestDto.getInsertSaveDataList();
        List<WarehouseHolidayResultVo> insertDataList = new ArrayList<WarehouseHolidayResultVo>();
        //입력할 출고처 데이터 리스트에 휴일 설정 처리
        for(int i=0;i<dataList.size();i++) {
        	WarehouseHolidayResultVo resultVo = new WarehouseHolidayResultVo();
        	resultVo = dataList.get(i);

        	//휴일 지정
        	for (String date : dates) {
        		WarehouseHolidayResultVo vo = new WarehouseHolidayResultVo();
        		vo.setUrWarehouseId(resultVo.getUrWarehouseId());
        		vo.setDawnYn(resultVo.getDawnYn());
        		vo.setHoliday(date);
        		if(saveWarehouseHolidayRequestDto.getUrUserId() != null) {
        			vo.setUrUserId(saveWarehouseHolidayRequestDto.getUrUserId());
        		}else {
        			vo.setUrUserId(saveWarehouseHolidayRequestDto.getUserVo().getUserId());
        		}
        		if(saveWarehouseHolidayRequestDto.getFromDate().equals(saveWarehouseHolidayRequestDto.getToDate())) {
        			vo.setGroupYn("N");
        		}else {
        			vo.setGroupYn("Y");
        		}

        		// 추가된 출고처 휴일 등록 확인
                int hldyCount = this.getDuplicateHldy(vo);

                if(hldyCount < 1) {
                    insertDataList.add(vo);
                }
        	}
        }

        saveWarehouseHolidayRequestDto.setInsertSaveDataList(insertDataList);

        if (!saveWarehouseHolidayRequestDto.getInsertSaveDataList().isEmpty()) {
        	warehouseHolidayMapper.addWarehouseHoliday(saveWarehouseHolidayRequestDto);
        }


        return ApiResult.success();
    }


    /**
     * Scheduler 출고처 휴일정보 조회
     * @param
     * @return
     * @throws Exception
     */
    protected WarehouseResultVo getHolidayWarehouseInfo(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	return warehouseHolidayMapper.getHolidayWarehouseInfo(warehouseHolidayRequestDto);
    }


    /**
     * 출고처 휴일리스트 조회
     * @param warehouseHolidayRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseHolidayResultVo> getWarehouseHolidayListById(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	PageMethod.startPage(warehouseHolidayRequestDto.getPage(), warehouseHolidayRequestDto.getPageSize());
        return warehouseHolidayMapper.getWarehouseHolidayListById(warehouseHolidayRequestDto);
    }


    /**
     * @Desc  출고처 휴일 등록 확인
     * @param WarehouseHolidayResultVo
     * @throws Exception
     * @return int
     */
    protected int getDuplicateHldy(WarehouseHolidayResultVo vo) throws Exception{
        return warehouseHolidayMapper.getDuplicateHldy(vo);
    }

}
