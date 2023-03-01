package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.comm.mapper.search.index.SearchIndexMapper;
import kr.co.pulmuone.v1.search.indexer.dto.PromotionSuggestionDocumentDto;
import kr.co.pulmuone.v1.search.indexer.dto.PromotionSuggestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionSuggestionIndexService extends IndexServiceTemplate{

    @Autowired
    private SearchIndexMapper searchIndexMapper;

    protected PromotionSuggestionIndexService() {
        super(Indices.PROMOTION_SUGGESTION);
    }

    @Override
    protected List<PromotionSuggestionDocumentDto> getIndexTargetList() {

        List<PromotionSuggestionDocumentDto> promotionSuggestionList = new ArrayList<>();

        List<PromotionSuggestionDto> list = searchIndexMapper.getPromotionSuggestionList();
        list.stream().forEach(
                l -> {
                    PromotionSuggestionDocumentDto doc = PromotionSuggestionDocumentDto.builder()
                                                        .typeCode(l.getTypeCode())
                                                        .typeDetailCode(l.getTypeDetailCode())
                                                        .seq(l.getId())
                                                        .name(l.getTitle())
                                                        .createDate(l.getCreateDate())
                                                        .displayWebPcYn(l.getDisplayWebPcYn())
                                                        .displayWebMobileYn(l.getDisplayWebMobileYn())
                                                        .displayAppYn(l.getDisplayAppYn())
                                                        .build();

                    promotionSuggestionList.add(doc);
                }
        );

        return promotionSuggestionList;

    }
}
