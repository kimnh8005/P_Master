package kr.co.pulmuone.v1.batch.collectionmall.ezadmin;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminOrderInfoRequestDto;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class CollectionMallEZAdminBatchThread implements Runnable{

    private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;

    private int startPage = 0;
    private int endPage = 0;
    private long ifEasyadminInfoId = 0L;
    private EZAdminOrderInfoRequestDto reqDto = null;
    private String batchTp = "";

    public CollectionMallEZAdminBatchThread(int startPage, int endPage, long ifEasyadminInfoId, EZAdminOrderInfoRequestDto reqDto, String batchTp, CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz){
        this.startPage = startPage;
        this.endPage = endPage;
        this.ifEasyadminInfoId = ifEasyadminInfoId;
        this.reqDto = new EZAdminOrderInfoRequestDto().copy(reqDto);
        this.batchTp = batchTp;
        this.collectionMallEZAdminBatchBiz = collectionMallEZAdminBatchBiz;
    }

    @Override
    public void run() {

        for(int i = this.startPage ; i < this.endPage +1 ; i++){
            log.info("===========CollectionMallEZAdminBatchThread call API START=============== Page " + i);

            try{
                collectionMallEZAdminBatchBiz.addOrderInfo(i, ifEasyadminInfoId, reqDto, batchTp);
            }catch(Exception e){
                log.error("===========CollectionMallEZAdminBatchThread call API FAIL =============== Page " + i);
                e.printStackTrace();
            }
        }

        System.out.println("################################################");
        System.out.println("############### [" + (new SimpleDateFormat("yyyy / MM / dd / HH:mm:ss").format(Calendar.getInstance().getTime())) + "] " + Thread.currentThread().getName() + " end");
        System.out.println("################################################");
    }

}
