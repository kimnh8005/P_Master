package kr.co.pulmuone.v1.batch.display.contents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DisplayContentsBatchBizImpl implements DisplayContentsBatchBiz {

    @Autowired
    private DisplayContentsBatchService displayContentsBatchService;

    // 샵풀무원 메인
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsBrandList() {
        displayContentsBatchService.runDisplayContentsBrandList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsNowSaleGoods() {
        displayContentsBatchService.runDisplayContentsNowSaleGoods();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsLohasGoods() {
        displayContentsBatchService.runDisplayContentsLohasGoods();
    }

    // 올가 메인
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsOrgaMainNewGoodsJob() throws Exception {
        displayContentsBatchService.runDisplayContentsOrgaMainNewGoodsJob();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsOrgaMainPbGoods() throws Exception {
        displayContentsBatchService.runDisplayContentsOrgaMainPbGoods();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsOrgaMainDirectGoods() throws Exception {
        displayContentsBatchService.runDisplayContentsOrgaMainDirectGoods();
    }

    // 올가
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsOrgaPbGoods() throws Exception {
        displayContentsBatchService.runDisplayContentsOrgaPbGoods();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDisplayContentsOrgaDirectGoods() throws Exception {
        displayContentsBatchService.runDisplayContentsOrgaDirectGoods();
    }

}
