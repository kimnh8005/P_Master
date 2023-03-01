package kr.co.pulmuone.v1.dashboard.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.DashboardEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.dashboard.dto.DashboardRequestDto;
import kr.co.pulmuone.v1.dashboard.dto.DashboardResponseDto;
import kr.co.pulmuone.v1.dashboard.dto.vo.DashboardVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private DashboardService dashboardService;

    @Test
    public void test_대시보드리스트조회() throws BaseException {
        //given
        DashboardRequestDto dashboardRequestDto = new DashboardRequestDto();
        dashboardRequestDto.setUrUserId("1");

        //when
        DashboardResponseDto result = dashboardService.selectDashboardList(dashboardRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_대시보드수정() throws BaseException {
        //given
        List<DashboardVo> dashboardList = new ArrayList<>();

        DashboardVo vo = new DashboardVo();
        vo.setUrUserDashboardId("4");
        vo.setXAxis("0.5");
        vo.setYAxis("0");
        vo.setSort(1);
        vo.setDispYn("Y");
        dashboardList.add(vo);

        vo = new DashboardVo();
        vo.setUrUserDashboardId("5");
        vo.setXAxis("0.75");
        vo.setYAxis("0");
        vo.setSort(2);
        vo.setDispYn("Y");
        dashboardList.add(vo);

        vo = new DashboardVo();
        vo.setUrUserDashboardId("6");
        vo.setXAxis("");
        vo.setYAxis("");
        vo.setSort(3);
        vo.setDispYn("Y");
        dashboardList.add(vo);

        DashboardRequestDto dashboardRequestDto = new DashboardRequestDto();
        dashboardRequestDto.setDashboardList(dashboardList);

        //when
        DashboardResponseDto result = dashboardService.putDashboardList(dashboardRequestDto);

        //then
        assertEquals(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

}