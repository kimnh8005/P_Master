package kr.co.pulmuone.v1.batch.promotion.point;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointExpireService {

	private final PointBiz pointBiz;


    public void runExpirePoint() throws Exception {

    	String toDay = null;
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        toDay = sdformat.format(calDate.getTime());

        pointBiz.expirePoint(toDay);

    }

}
