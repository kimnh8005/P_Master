/**-----------------------------------------------------------------------------
 * description 		 : 정기배송 주문 신청 리스트 컬럼 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.17		안지영   최초생성
 * @ 2021.02.07     김명진   컬럼정보 수정
 * @
 * **/

var regularDeliveryReqGridUtil = {
	    regularDeliveryReqList: function(){
	        return [
	             { field:'odRegularReqId'		,title : 'No'						, width:'40px'		,attributes:{ style:'text-align:center;'}, template : '<span class="row-number"></span>' }
				,{ field:'createDt'				,title : '신청일자'					, width:'120px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'reqId'				,title : '신청번호'					, width:'120px'		,attributes:{ style:'text-align:center;text-decoration: underline;' }}
				,{ field:''						,title : '회차정보'					, width:'80px'		,attributes:{ style:'text-align:center;' }
					, template: function(dataItem){
	                    return dataItem.reqRound + "/" + dataItem.totCnt;
	                }
				}
				,{ field:''						,title : '신청기간<br />(주기/요일)'		, width:'100px'		,attributes:{ style:'text-align:center;' }
					, template: function(dataItem) {
	                    return dataItem.goodsCycleTermTpNm + "<br />(" + dataItem.goodsCycleTpNm + "/" + dataItem.weekCdNm + ")";
	                }
				}
				,{ field:'regularStatusCdNm'	,title : '신청상태'					, width:'100px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'buyerNm'				,title : '주문자정보'					, width:'100px'		,attributes:{ style:'text-align:center;' }
					, template: function(dataItem) {
						if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
							return "<u>" + dataItem.buyerNm + "<br />" + dataItem.loginId + "</u>";
						} else {
							return dataItem.buyerNm + "<br />" + dataItem.loginId;
						}
	                }
				}
				,{ field:'recvNm'				,title : '수취인명'					, width:'100px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'goodsNm'				,title : '상품명'						, width:'200px'		,attributes:{ style:'text-align:ce;nter' }}
				,{ field:'goodsCnt'				,title : '상품수'						, width:'60px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}"}
				,{ field:'salePrice'			,title : '판매가'						, width:'80px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}"}
				,{ field:'discountPrice'		,title : '할인금액'					, width:'80px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}"}
				,{ field:'paidPrice'			,title : '결제예정금액'					, width:'80px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}"}
				,{ field:'agentTypeCdNm'		,title : '유형'						, width:'80px'		,attributes:{ style:'text-align:center;' }}
			]
	    }
	}