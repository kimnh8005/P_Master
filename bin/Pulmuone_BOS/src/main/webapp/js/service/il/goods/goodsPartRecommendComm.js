/**-----------------------------------------------------------------------------
 * description 		 : 추천상품 등록 공통 FUnc
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.10		임상건   최초생성
 * @
 * **/
	//혜택/구매정보 > 상품추가 버튼 > 추가 상품 검색
	//추천상품 등록 > 추천 상품 > 상품 추가 버튼
	function fnGoodsSearchPopup(kind) {
		let params = {};
		let title = "";

//		console.log("urSupplierId : ", urSupplierId);
//		console.log("urWarehouseId : ", urWarehouseId);
//		console.log("undeliverableAreaType : ", undeliverableAreaType);

		if(kind == "additional"){
			params.goodsType = "GOODS_TYPE.ADDITIONAL";					// 상품유형 ( 추가상품 )
			params.undeliverableAreaType = undeliverableAreaType;		// 배송불가지역 ( 상품유형이 추가 일 경우 필수 )
			title = "추가상품 선택";
			params.goodsCallType = "addGoods";
		}
		else if(kind == "recommend"){
			// 상품유형 ( 일반, 폐기임박, 묶음, 매장전용, 렌탈, 무형 )
			params.goodsType = "GOODS_TYPE.DISPOSAL,GOODS_TYPE.INCORPOREITY,GOODS_TYPE.NORMAL,GOODS_TYPE.PACKAGE,GOODS_TYPE.RENTAL,GOODS_TYPE.SHOP_ONLY";
			title = "상품조회";
			params.goodsCallType = "goodsRecommend";
			params.saleStatus = "SALE_STATUS.ON_SALE";					// 판매상태(기본값 : 판매중)
		}
		params.selectType = "multi";								// 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.supplierId = urSupplierId;							// 공급처ID ( 상품유형이 추가 일 경우 필수 )
		params.warehouseId = urWarehouseId;							// 출고처 ID ( 상품유형이 추가 일 경우 필수 )

		fnKendoPopup({
			id			: "goodsSearchPopup",
			title		: title,  // 해당되는 Title 명 작성
			width		: "1700px",
			height		: "800px",
			scrollable	: "yes",
			src			: "#/goodsSearchPopup",
			param		: params,
			success		: function( id, data ){
				if(data.parameter == undefined){
					if(kind == "additional"){
						viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingNoDataTbody", false)			//추가상품 > NoData Tbody Visible
						viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingTbody", true)					//추가상품 > Data Tbody Visible

						var goodsAdditionalGoodsMappingLength = viewModel.ilGoodsDetail.get('goodsAdditionalGoodsMappingList').length;
						data.forEach(function(element, index, array){
							if(goodsAdditionalGoodsMappingLength > 0) { //이미 입력한 값이 있다면
								var selGoodsId = "";
								for(var i=0; i < goodsAdditionalGoodsMappingLength; i++){
									if(element.goodsId == viewModel.ilGoodsDetail.get('goodsAdditionalGoodsMappingList')[i].targetGoodsId){	//이미 들어가 있는 추가 상품 코드를 체크
										selGoodsId = element.goodsId;
									}
								}

								if(element.goodsId != selGoodsId){			//이미 들어가 있는 추가 상품 코드를 제외하고 추가 처리
									viewModel.ilGoodsDetail.get('goodsAdditionalGoodsMappingList').push({
										targetGoodsId : element.goodsId,				//상품코드
										goodsName : element.goodsName,					//상품명
										ilItemCode : element.itemCode,					//품목ID
										ilItemWarehouseId : element.ilItemWarehouseId,	//품목출고처ID
										stockOrderYn : element.stockOrderYn,			//재고발주여부
										standardPrice : element.standardPrice,			//원가
										recommendedPrice : element.recommendedPrice,	//정상가
										salePrice : "",									//판매가
									});
								}
							}
							else{
								viewModel.ilGoodsDetail.get('goodsAdditionalGoodsMappingList').push({
									targetGoodsId : element.goodsId,				//상품코드
									goodsName : element.goodsName,					//상품명
									ilItemCode : element.itemCode,					//품목ID
									ilItemWarehouseId : element.ilItemWarehouseId,	//품목출고처ID
									stockOrderYn : element.stockOrderYn,			//재고발주여부
									standardPrice : element.standardPrice,			//원가
									recommendedPrice : element.recommendedPrice,	//정상가
									salePrice : "",									//판매가
								});
							}
						});
					}
					else if(kind == "recommend"){
						viewModel.ilGoodsDetail.set("isGoodsRecommendNoDataTbody", false)			//추천상품 > NoData Tbody Visible
						viewModel.ilGoodsDetail.set("isGoodsRecommendTbody", true)					//추천상품 > Data Tbody Visible

						var goodsRecommendLength = viewModel.ilGoodsDetail.get('goodsRecommendList').length;

						//console.log("goodsRecommendLength : ", goodsRecommendLength);

						data.forEach(function(element, index, array){
							if(goodsRecommendLength > 0) { //이미 입력한 값이 있다면
								var selGoodsId = "";
								for(var i=0; i < goodsRecommendLength; i++){
									if(element.goodsId == viewModel.ilGoodsDetail.get('goodsRecommendList')[i].targetGoodsId){	//이미 들어가 있는 추가 상품 코드를 체크
										selGoodsId = element.goodsId;
									}
								}

								if(element.goodsId != selGoodsId){		// 이미 들어가 있는 추천 상품 코드 제외
									if(element.goodsId != ilGoodsId) {	// 자기자신을 제외
										viewModel.ilGoodsDetail.get('goodsRecommendList').push({
											targetGoodsId : element.goodsId,				//상품코드
											goodsName : element.goodsName,					//상품명
											ilItemCode : element.itemCode,					//품목ID
											ilItemWarehouseId : element.ilItemWarehouseId,	//품목출고처ID
											stockOrderYn : element.stockOrderYn,			//재고발주여부
											standardPrice : element.standardPrice,			//원가
											recommendedPrice : element.recommendedPrice,	//정상가
											salePrice : "",									//판매가
										});
									}
								}
							}
							else{
								viewModel.ilGoodsDetail.get('goodsRecommendList').push({
									targetGoodsId : element.goodsId,				//상품코드
									goodsName : element.goodsName,					//상품명
									ilItemCode : element.itemCode,					//품목ID
									ilItemWarehouseId : element.ilItemWarehouseId,	//품목출고처ID
									stockOrderYn : element.stockOrderYn,			//재고발주여부
									standardPrice : element.standardPrice,			//원가
									recommendedPrice : element.recommendedPrice,	//정상가
									salePrice : "",									//판매가
								});
							}
						});
					}
				}
			}
		});
	};


	$scope.fnGoodsSearchPopup = function(kind){		//혜택/구매정보 > 추가상품 > 추가상품 검색 팝업 호출
		fnGoodsSearchPopup(kind);
	};