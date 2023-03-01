package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.comm.mapper.search.index.SearchIndexMapper;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsDocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsIndexService extends IndexServiceTemplate {

    @Autowired
    private SearchIndexMapper goodsIndexMapper;

    protected GoodsIndexService() {
        super(Indices.GOODS);
    }

    @Override
    protected List<GoodsDocumentDto> getIndexTargetList() {
        List<GoodsDocumentDto> list = goodsIndexMapper.getIndexTargetGoodsList();

        list.stream().forEach(
                g -> {
                    g.setDocumentId();
                    g.toArrayMultiValueField();
                }
        );

        return list;
    }
}
