package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.comm.mapper.search.index.SearchIndexMapper;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.StoreGoodsDocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreGoodsIndexService extends IndexServiceTemplate {

    @Autowired
    private SearchIndexMapper goodsIndexMapper;

    protected StoreGoodsIndexService() {
        super(Indices.STORE_GOODS);
    }

    @Override
    protected List<StoreGoodsDocumentDto> getIndexTargetList() {
        List<StoreGoodsDocumentDto> list = goodsIndexMapper.getIndexTargetStoreGoodsList();

        list.stream().forEach(
                g -> {
                    g.setDocumentId();
                    g.toArrayMultiValueField();
                }
        );

        return list;
    }
}
