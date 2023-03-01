/**-----------------------------------------------------------------------------
 * description 		 : 정기배송 주문 신청 내역 상품 목록 컬럼 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.07		김명진   최초생성
 * @
 * **/

var regularDeliveryReqGoodsGridUtil = {
		regularDeliveryReqGoodsModelFields: function() {
			return {
				 chk			    : { editable: false	, type: 'string', validation: { required: false }}
				,reqId				: { editable: false	, type: 'string', validation: { required: true  }}
				,dataItem 			: { editable: false	, type: 'string', validation: { required: true  }}
				,ilGoodsId 			: { editable: false	, type: 'string', validation: { required: true  }}
				,goodsNm			: { editable: false	, type: 'string', validation: { required: true  }}
				,storageMethodTpNm 	: { editable: false	, type: 'string', validation: { required: true  }}
				,orderCnt			: { editable: true	, type: 'number'
						, validation: { required: true
						, pattern : "[0-9]+"
						, validationMessage : "숫자만 입력가능합니다."
					}
				}
				,reqDetailStatusCdNm: { editable: false	, type: 'string', validation: { required: true  }}
				,goodsTpNm			: { editable: false	, type: 'string', validation: { required: true  }}
				,urWarehouseId		: { editable: false	, type: 'string', validation: { required: true  }}
				,ilShippingTmplId	: { editable: false	, type: 'string', validation: { required: true  }}
				,shippingPrice		: { editable: false	, type: 'number', validation: { required: true  }}
				,deliveryTmplNm		: { editable: false	, type: 'string', validation: { required: true  }}
				,recommendedPrice	: { editable: false	, type: 'string', validation: { required: true  }}
				,discountPrice		: { editable: false	, type: 'number', validation: { required: true  }}
			}
		},
	    regularDeliveryReqGoodsList: function(){
	        return [
	        	 { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />'
	        		 , template: function(dataItem){
	        			if(dataItem.reqDetailStatusCd == "REGULAR_DETL_STATUS_CD.CANCEL_BUYER" || dataItem.reqDetailStatusCd == "REGULAR_DETL_STATUS_CD.CANCEL_SELLER") {
	        				return "";
	        			}
	        			if(dataItem.addGoodsFlag == true) {
	        				return 	"<div class='btn-area' style='text-align: center;'>" +
										"<button type='button' id='regularReqExtensionBtn' class='btn-point btn-m' onclick='$scope.goodsDel(this);'>삭제</button>" +
									"</div>";
	        			}
	                    return '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />';
	               }, width : "100px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
	        	 }
				,{ field:'reqId'				,title : '신청번호'					, width:'120px'		,attributes:{ style:'text-align:center;' }, editable: false}
				,{ field:'dataItem'				,title : '마스터품목코드<br />품목바코드'	, width:'140px'		,attributes:{style : "text-align:center; text-decoration: underline; cursor: pointer;"}, editable: false
					, template: function(dataItem){
	                    return "<div class='popGoodsClick'>" + dataItem.ilItemCd + "<br />" + dataItem.itemBarcode + "</div>";
	                }
				}
				,{ field:'ilGoodsId'			,title : '상품코드'					, width:'80px'		,attributes:{style : "text-align:center; text-decoration: underline; cursor: pointer;"}, editable: false
					, template: function(dataItem){
						return "<div class='popGoodsClick'>" + dataItem.ilGoodsId + "</div>";
					}
				}
				,{ field:'goodsNm'				,title : '상품명'						, width:'270px'		,attributes:{style : "text-align:center; text-decoration: underline; cursor: pointer;"}, editable: false
					, template: function(dataItem){
						return "<div class='popGoodsClick'>" + dataItem.goodsNm + "</div>";
					}
				}
				,{ field:'storageMethodTpNm'	,title : '보관방법'					, width:'100px'		,attributes:{ style:'text-align:center;' }, editable: false}
				,{ field:'orderCnt'				,title : '수량'						, width:'80px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}"
					, editable: function(dataItem) {
						if(dataItem.addGoodsFlag == true) {
							return true;
						}
						return false;
					}
				}
				,{ field:'reqDetailStatusCdNm'	,title : '신청상세상태'					, width:'100px'		,attributes:{ style:'text-align:center;' }, editable: false}
				,{ field:'goodsTpNm'			,title : '상품유형<br />(출고지)'		, width:'150px'		,attributes:{ style:'text-align:center;' }, editable: false
					, template: function(dataItem){
						return dataItem.goodsTpNm + "<br />(" + dataItem.warehouseNm + ")";
	                }
				}
				,{ field:'ilShippingTmplId'		,title : '배송번호'					, width:'80px'		,attributes:{ style:'text-align:center;' }, editable: false}
				,{ field:'shippingPrice'		,title : '배송비'						, width:'80px'		,attributes:{ style:'text-align:center;' }, format: "{0:n0}", editable: false}
				,{ field:'deliveryTmplNm'		,title : '배송정책명'					, width:'118px'		,attributes:{ style:'text-align:center;' }, editable: false}
				,{ field:'recommendedPrice'		,title : '총상품금액'					, width:'100px'		,attributes:{ style:'text-align:center;' }, editable: false
					, template: function(dataItem){
	                    return kendo.toString(dataItem.recommendedPrice * dataItem.orderCnt, "n0");
	                }
				}
				,{ field:'discountPrice'		,title : '상품별할인금액'				, width:'100px'		,attributes:{ style:'text-align:center;' }, editable: false
					, template: function(dataItem){
	                    //return kendo.toString((dataItem.recommendedPrice - dataItem.salePrice) * dataItem.orderCnt, "n0");
	                    return kendo.toString(dataItem.basicDiscountPrice, "n0");
	                }
				}
			]
	    }
	};


var regularDeliveryReqHistoryGridUtil = {
	    regularDeliveryReqHistoryList: function(){
	        return [
	        	 { field:''						,title : 'No'						, width:'10px'		,attributes:{ style:'text-align:center;'}, template : '<span class="row-number"></span>'}
				,{ field:'odRegularReqHistoryId',title : '주문상세번호'					, width:'20px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'createDt'				,title : '변경일자'					, width:'30px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'regularReqGbnCdNm'	,title : '처리상태'					, width:'30px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'userNm'				,title : '작업자'						, width:'30px'		,attributes:{ style:'text-align:center;' }}
				,{ field:'regularReqCont'		,title : '비고'						, width:'100px'		,attributes:{ style:'text-align:center;' }
					, template: function(dataItem){
						var regularReqCont = new String(dataItem.regularReqCont);
	                    return regularReqCont.replace(/\r\n/ig, '<br />');
	                }
				}
			]
	    }
	}