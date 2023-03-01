package kr.co.pulmuone.v1.batch.display.contents;

public interface DisplayContentsBatchBiz {

    void runDisplayContentsBrandList();

    void runDisplayContentsNowSaleGoods();

    void runDisplayContentsLohasGoods();

    void runDisplayContentsOrgaMainNewGoodsJob() throws Exception;

    void runDisplayContentsOrgaMainPbGoods() throws Exception;

    void runDisplayContentsOrgaMainDirectGoods() throws Exception;

    void runDisplayContentsOrgaPbGoods() throws Exception;

    void runDisplayContentsOrgaDirectGoods() throws Exception;

}
