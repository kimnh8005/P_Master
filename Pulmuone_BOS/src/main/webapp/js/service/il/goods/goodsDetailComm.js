/**-----------------------------------------------------------------------------
 * description 		 : 상품관련 공통 FUnc
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.02		임상건   최초생성
 * @ 2021.06.04		김승우	 샵풀무원 상풉코드 영역 추가
 * @
 * **/
	//판매/전시 > 판매 상태 > 상태 설정
	function fnInitSaleStatusRadio() {
		var mySaleStatus = viewModel.ilGoodsDetail.get("saleStatus");
		var editableStatusDefine = {
			//fixme: '승인' 프로세스 개발 완료 후 , '저장' 상태인 경우에 수정가능한 상태는 '저장' 만으로 변경 필요.
//			"SALE_STATUS.SAVE": ['SALE_STATUS.SAVE', 'SALE_STATUS.WAIT']
			"SALE_STATUS.SAVE": ['SALE_STATUS.SAVE']
			,"SALE_STATUS.WAIT": ['SALE_STATUS.WAIT','SALE_STATUS.ON_SALE','SALE_STATUS.STOP_SALE', 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER']
			,"SALE_STATUS.ON_SALE": ['SALE_STATUS.ON_SALE','SALE_STATUS.WAIT','SALE_STATUS.STOP_SALE', 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER']
			,"SALE_STATUS.STOP_SALE": ['SALE_STATUS.STOP_SALE','SALE_STATUS.WAIT','SALE_STATUS.ON_SALE','SALE_STATUS.OUT_OF_STOCK_BY_MANAGER']
			,"SALE_STATUS.STOP_PERMANENT_SALE": []
			,"SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM": ['SALE_STATUS.STOP_SALE','SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM','SALE_STATUS.OUT_OF_STOCK_BY_MANAGER']
			//fixme: 재고 연동 후, 재고Y/N 에 처리 필요. 현재는 재고N 인 케이스만 정의함
			,"SALE_STATUS.OUT_OF_STOCK_BY_MANAGER": ['SALE_STATUS.WAIT','SALE_STATUS.ON_SALE','SALE_STATUS.STOP_SALE','SALE_STATUS.OUT_OF_STOCK_BY_MANAGER']
		}
		$("input[name=saleStatus]").attr('disabled', true);
		$.each(editableStatusDefine[mySaleStatus], function (i, v) {
			$("input[name=saleStatus][value='" + v + "']").attr('disabled', false);
		});
	}

	// 상품 상세 이미지 다운로드 팝업
	$scope.fnOpenDownloadPopup = function() {
		const goodsName = viewModel.ilGoodsDetail.goodsName;

		// 팝업 옵션
		const popupOption = {
                id: "goodsDetailImagePopup",
                title: "이미지 다운로드",
//                src: "#/inputMulti30",
                src: "#/goodsDetailImagePopup",
                param: {
                	ilGoodsId: ilGoodsId,
                	goodsName: goodsName,
                },
                width: "1200px",
                height: "1200px",
                success: function (id, data) {
                	//console.log(id, data);
                },
                key: "4355",
                nullMsg: "이미지 다운로드",
        };

		const originHost = location.origin;
		const currentPath = location.pathname;
		const path = '#/goodsDetailImagePopup';
		const queryString = '?ilGoodsId=' + encodeURIComponent(ilGoodsId) + '&goodsName=' +  encodeURIComponent(goodsName);

		const url = originHost + currentPath + path + queryString;
		const windowFeatures = 'width=960,height=900';
		window.open(url, '', windowFeatures);
	}

	/**
	 * 미구현 기능에 대한 안내 메시지.
	 */
	$scope.fnDisplayTempMessage = function() {
		fnKendoMessage({ message : "개발중입니다." });
	}

	/*
	* 상품 업데이트 내역 페이지로 이동
	*/
	$scope.fnGoodsChangeLog = function () {
		let option = {};
		option.url = "#/goodsChangeLogList";
		option.data = {"paramIlGoodsId":ilGoodsId};
		option.target = "goodsChangeLogList";

		fnGoNewPage(option);
	}

	//공통 NULL값 validation제외 validation 체크
	/*
	 * 메시지 팝업 호출 함수
	 */
	function valueCheck(nullMsg, id) {
		fnKendoMessage({
			message : nullMsg,
			ok : function focusValue() {
				$('#' + id).focus();
			}
		});

		return false;
	};

	/*
	 * 메시지 팝업 호출 함수(name 기준)
	 */
	function valueCheckName(nullMsg, seq, name) {
		fnKendoMessage({
			message : nullMsg,
			ok : function focusValue() {

				console.log($("input[name=" + name + "]:eq("+seq+")"));

				$("input[name=" + name + "]:eq("+seq+")").focus();
			}
		});

		return false;
	};

	function fnValidationCheck(){

		let goodsType = viewModel.ilGoodsDetail.get("goodsType");		//상폼타입

		/* 묶음 상품 기본 설정 */
		if(goodsType == "GOODS_TYPE.PACKAGE"){
			if(viewModel.pageMode == "create"){	//상품 등록시에만 체크
				if (aGridDs.data().length == 0) {
					//fnKendoMessage({message: '<span style="color: red;font-size: 18pt;font-weight: bolder;">[기준 상품] </span>은 필수선택입니다.'});
					valueCheck("기준 상품을 선택해 주세요.", "baseGoodsSelBtn");
					return false;
				}

				if ( aGridDs.data().length == 1) {
					if(Number(aGrid.dataSource.at(0).get("purchaseQuanity")) == 1 && bGridDs.data().length == 0) {
						valueCheck("동일 상품으로만 묶음상품 구성 시 구성수량을 최소 2개 이상으로 등록하시거나<BR>증정품을 하나 이상 선택해 주세요.","fnAddGoodsGiftSearchPopup");
						return false;
					}
				}

				if (dGridDs.data().length == 0) {
					valueCheck("금액계산 버튼을 통해서 묶음상품 구성목록을 구성해 주세요.","fnGoodsAssemble");
					return false;
				}

				if (bGridDs.data().length > 0 && eGridDs.data().length != bGridDs.data().length) {
					valueCheck("금액계산 버튼을 통해서 증정품 구성목록을 구성해 주세요.","fnGoodsAssemble");
					return false;
				}

/*
				var goodsPackageSalePrice = cGrid.dataSource.at(0).get("goodsPackageSalePrice");

				if(viewModel.ilGoodsDetail.get("totalSalePriceGoods") != goodsPackageSalePrice) {
					valueCheck("묶음상품 판매가와 묶음상품 구성목록의 판매가 합계가 동일해야 합니다.","fnGoodsAssemble");
					return false;
				}
*/
			}
		}
		/* 묶음 상품 기본 설정 */

		/* 기본 정보 */
		if(viewModel.ilGoodsDetail.get("goodsName").length == 0){		//상품명이 공백이면
			valueCheck("상품명을 입력해 주세요.","goodsName");
			return false;
		}

		if(viewModel.ilGoodsDetail.get("goodsName").length > 30){		//상품명이 30자를 넘어가면
			valueCheck("상품명을 30자 이하로 입력해 주세요.","goodsName");
			return false;
		}

		if(byteCheck(viewModel.ilGoodsDetail.get("promotionName")) > 0){	//프로모션 상품명이 존재한다면
			if(viewModel.ilGoodsDetail.get("promotionNameStartYear").length == 0){
				valueCheck("프로모션 시작일을 입력해 주세요.", "promotionNameStartYear");
				return false;
			}

			if(viewModel.ilGoodsDetail.get("promotionNameEndYear").length == 0){
				valueCheck("프로모션 종료일을 입력해 주세요.", "promotionNameEndYear");
				return false;
			}
		}

		if(byteCheck(viewModel.ilGoodsDetail.get("promotionName")) > 12){		//프로모션 상품명이 12byte를 초과하면
			valueCheck("프로모션 상품명을 12Byte 이하로 입력해 주세요.","promotionName");
			return false;
		}

		// 프로모션 + 상품명 합계 한글기준 30자(60byte) 넘길경우
		if(viewModel.ilGoodsDetail.promotionNameGoodsNameSumLength > 60) {
			valueCheck("상품명과 프로모션상품명은 통합하여 최대 30자리까지 입력 가능합니다.","goodsName");
			return false;
		}

		if(goodsType != "GOODS_TYPE.ADDITIONAL" && goodsType != "GOODS_TYPE.GIFT" && goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING") {
			if(viewModel.ilGoodsDetail.get("goodsDesc") != null && viewModel.ilGoodsDetail.get("goodsDesc") != "" && viewModel.ilGoodsDetail.get("goodsDesc").length > 20){		//상품설명이 20자를 넘어가면
				valueCheck("상품설명을 20자 이하로 입력해 주세요.","goodsDesc");
				return false;
			}

			if (ctgryGridDs.data().length == 0) {
				valueCheck("전시 카테고리는 필수입력 입니다.", "fnCtgrySelect");
				$('#goodsDisplayCategory1 input:text').focus();
				return false;
			}
		}

		if(goodsType != "GOODS_TYPE.ADDITIONAL" && goodsType != "GOODS_TYPE.GIFT" && goodsType != "GOODS_TYPE.PACKAGE" && goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING") {
			if(urSupplierId == "2"){ //공급처가 올가홀푸드이면
				if (mallInMallCtgryGridDs.data().length == 0) {
					valueCheck("몰인몰 카테고리는 필수입력 입니다.", "fnMallInMallCtgrySelect");
					$('#mallInMallCategory1 input:text').focus();
					return false;
				}
			}
			else if(urSupplierId == "5" && mallinmallCategoryId){ //공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림, 베이비밀이면
				if (mallInMallCtgryGridDs.data().length == 0) {
					valueCheck("몰인몰 카테고리는 필수입력 입니다.", "fnMallInMallCtgrySelect");
					$('#mallInMallCategory1 input:text').focus();
					return false;
				}
			}
		}

		//추후에 사용예정이라 주석처리 HGRM-4280
		/*if(goodsType == "GOODS_TYPE.PACKAGE"){
			if(viewModel.ilGoodsDetail.urSupplierId == "2"){ //공급처가 올가홀푸드이면
				if (mallInMallCtgryGridDs.data().length == 0) {
					valueCheck("몰인몰 카테고리는 필수입력 입니다.", "mallInMallCategory1");
					$('#mallInMallCategory1 input:text').focus();
					return false;
				}
			}
			else if(viewModel.ilGoodsDetail.urSupplierId == "5" && viewModel.ilGoodsDetail.mallinmallCategoryId){ //공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림, 베이비밀이면
				if (mallInMallCtgryGridDs.data().length == 0) {
					valueCheck("몰인몰 카테고리는 필수입력 입니다.", "mallInMallCategory1");
					$('#mallInMallCategory1 input:text').focus();
					return false;
				}
			}
		}*/

		if(goodsType != "GOODS_TYPE.ADDITIONAL" && goodsType != "GOODS_TYPE.GIFT" && goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING") {
			/* 판매/전시 */
			if(viewModel.ilGoodsDetail.get("purchaseTargetType").length == 0){
				valueCheck("구매 허용 범위를 한개 이상 선택해 주세요.", "purchaseTargetType_0");
				return false;
			}

			if(viewModel.ilGoodsDetail.get("purchaseTargetType").length == 1 && viewModel.ilGoodsDetail.get("purchaseTargetType")[0] == "PURCHASE_TARGET_TP.NONMEMBER"){
				valueCheck("구매허용범위는 일반 또는 임직원 회원 중 최소 1개 이상 선택하셔야 등록이 가능합니다.", "purchaseTargetType_0");
				return false;
			}

			/* 가격 정보 */
			//우선할인 Valid
			let goodsDiscountPriorityList = viewModel.ilGoodsDetail.get("goodsDiscountPriorityList");		//행사/할인 내역 > 우선할인 내역

			if(goodsDiscountPriorityList && goodsDiscountPriorityList.length > 0) {
				//console.log("goodsDiscountPriorityList : ", goodsDiscountPriorityList);

				for(let i=0; i < goodsDiscountPriorityList.length; i++){
					if(goodsDiscountPriorityList[i].approvalStatusCode == "APPR_STAT.NONE"){	// 승인요청 건이라면
						let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
						let startDateTime = kendo.parseDate((goodsDiscountPriorityList[i].discountStartDate + " " + goodsDiscountPriorityList[i].discountStartHour + ":" + goodsDiscountPriorityList[i].discountStartMinute), "yyyy-MM-dd HH:mm");
						let endDateTime = kendo.parseDate((goodsDiscountPriorityList[i].discountEndDate + " " + goodsDiscountPriorityList[i].discountEndHour + ":" + goodsDiscountPriorityList[i].discountEndMinute), "yyyy-MM-dd HH:mm");

						if( nowDateTime.getTime() >= startDateTime.getTime() ){
							valueCheck("즉시할인 시작일은 현재 이후 시간만 등록 가능합니다.", "immediateButton");
							return false;
						}else if( startDateTime.getTime() > endDateTime.getTime() ){
							valueCheck("즉시할인 현재행사 종료일보다 시작일이 큽니다.", "immediateButton");
							return false;
						}
					}
				}
			}

			//즉시할인 Valid
			let goodsDiscountImmediateList = viewModel.ilGoodsDetail.get("goodsDiscountImmediateList");		//행사/할인 내역 > 즉시할인 내역

			if(goodsDiscountImmediateList && goodsDiscountImmediateList.length > 0) {
				//console.log("goodsDiscountImmediateList : ", goodsDiscountImmediateList);

				for(let i=0; i < goodsDiscountImmediateList.length; i++){
					if(goodsDiscountImmediateList[i].approvalStatusCode == "APPR_STAT.NONE"){	// 승인요청 건이라면
						let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
						let startDateTime = kendo.parseDate((goodsDiscountImmediateList[i].discountStartDate + " " + goodsDiscountImmediateList[i].discountStartHour + ":" + goodsDiscountImmediateList[i].discountStartMinute), "yyyy-MM-dd HH:mm");
						let endDateTime = kendo.parseDate((goodsDiscountImmediateList[i].discountEndDate + " " + goodsDiscountImmediateList[i].discountEndHour + ":" + goodsDiscountImmediateList[i].discountEndMinute), "yyyy-MM-dd HH:mm");

						if( nowDateTime.getTime() >= startDateTime.getTime() ){
							valueCheck("즉시할인 시작일은 현재 이후 시간만 등록 가능합니다.", "immediateButton");
							return false;
						}else if( startDateTime.getTime() > endDateTime.getTime() ){
							valueCheck("즉시할인 현재행사 종료일보다 시작일이 큽니다.", "immediateButton");
							return false;
						}
					}
				}
			}

			if(goodsType == "GOODS_TYPE.PACKAGE") {
				//묶음상품 기본할인 Valid
				let goodsPackagePriceList = viewModel.ilGoodsDetail.get("goodsPackagePriceList");				//묶음상품 기본 할인가 > List

				if(goodsPackagePriceList && goodsPackagePriceList.length > 0) {
					//console.log("goodsPackagePriceList : ", goodsPackagePriceList);

					for(let i=0; i < goodsPackagePriceList.length; i++){
						if(goodsPackagePriceList[i].approvalStatusCode == "APPR_STAT.NONE"){	// 승인요청 건이라면
							let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
							let startDateTime = kendo.parseDate((goodsPackagePriceList[i].discountStartDate + " " + goodsPackagePriceList[i].discountStartHour + ":" + goodsPackagePriceList[i].discountStartMinute), "yyyy-MM-dd HH:mm");
							let endDateTime = kendo.parseDate((goodsPackagePriceList[i].discountEndDate + " " + goodsPackagePriceList[i].discountEndHour + ":" + goodsPackagePriceList[i].discountEndMinute), "yyyy-MM-dd HH:mm");

							if( nowDateTime.getTime() >= startDateTime.getTime() ){
								valueCheck("묶음상품 기본 판매가 시작일은 현재 이후 시간만 등록 가능합니다.", "packageButton");
								return false;
							}else if( startDateTime.getTime() > endDateTime.getTime() ){
								valueCheck("묶음상품 기본 판매가 현재행사 종료일보다 시작일이 큽니다.", "packageButton");
								return false;
							}
						}
					}
				}
			}
			/* 가격정보 */

			/* 임직원 할인 정보 */
			if(goodsType == "GOODS_TYPE.PACKAGE") {
				let goodsPackageDiscountEmployeeList = viewModel.ilGoodsDetail.get("goodsPackageDiscountEmployeeList");				//임직원 개별할인 정보 > List

				if(goodsPackageDiscountEmployeeList && goodsPackageDiscountEmployeeList.length > 0) {
					//console.log("goodsPackageDiscountEmployeeList : ", goodsPackageDiscountEmployeeList);

					for(let i=0; i < goodsPackageDiscountEmployeeList.length; i++){
						if(goodsPackageDiscountEmployeeList[i].approvalStatusCode == "APPR_STAT.NONE"){	// 승인요청 건이라면
							let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
							let startDateTime = kendo.parseDate((goodsPackageDiscountEmployeeList[i].discountStartDate + " " + goodsPackageDiscountEmployeeList[i].discountStartHour + ":" + goodsPackageDiscountEmployeeList[i].discountStartMinute), "yyyy-MM-dd HH:mm");
							let endDateTime = kendo.parseDate((goodsPackageDiscountEmployeeList[i].discountEndDate + " " + goodsPackageDiscountEmployeeList[i].discountEndHour + ":" + goodsPackageDiscountEmployeeList[i].discountEndMinute), "yyyy-MM-dd HH:mm");

							if( nowDateTime.getTime() >= startDateTime.getTime() ){
								valueCheck("임직원 개별할인 정보 시작일은 현재 이후 시간만 등록 가능합니다.", "employeeButton");
								return false;
							}else if( startDateTime.getTime() > endDateTime.getTime() ){
								valueCheck("임직원 개별할인 정보 현재행사 종료일보다 시작일이 큽니다.", "employeeButton");
								return false;
							}
						}
					}
				}
			}
			/* 임직원 할인 정보 */

			/* 혜택/구매 정보 > 추가상품 valid */
			if(viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList") !=null && viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList").length > 0) {
				for(var i=0; i <= viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList").length - 1; i++){
					let dataItem = viewModel.ilGoodsDetail.get('goodsAdditionalGoodsMappingList')[i];

					if(dataItem.salePrice == "" || dataItem.salePrice == null){
						valueCheckName("추가상품의 판매가를 입력해 주세요.", i, "addGoodsSalePrice");
						return false;
					}
				}
			}

			/* 혜택/구매 정보 > 구매 제한 설정 > 최소 구매 valid */
			if (viewModel.ilGoodsDetail.get("limitMinimumCnt") == "" || !viewModel.ilGoodsDetail.get("limitMinimumCnt")) {
				valueCheck("최소 구매 값을 1개 이상 입력해 주세요.", "limitMinimumCnt");
				return false;
			}

			/* 혜택/구매 정보 > 구매 제한 설정 > 최대 구매 valid */
			if (viewModel.ilGoodsDetail.get("limitMaximumType") != "PURCHASE_LIMIT_MAX_TP.UNLIMIT" && (viewModel.ilGoodsDetail.get("limitMaximumCnt") == "" || !viewModel.ilGoodsDetail.get("limitMaximumCnt"))) {
				fnKendoMessage({
					message : "최대 구매 값을 1개 이상 입력해 주세요.",
					ok : function focusValue() {
						$('#limitMaximumCnt').data('kendoDropDownList').focus();
					}
				});

				return false;
			}

			if(viewModel.ilGoodsDetail.get('saleType') == "SALE_TYPE.RESERVATION" && viewModel.goodsReservationOptionList.total() == 0) {
				valueCheck("예약판매 옵션정보를 확인해주세요", "saleType");
				return false;
			}

			if(viewModel.goodsReservationOptionList != null && viewModel.goodsReservationOptionList.total() > 0) {
				for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
					let dataItem = viewModel.goodsReservationOptionList.at(i);

					if(dataItem.reservationStartDate == "" || dataItem.reservationStartDate == null){
						valueCheckName("예약 주문 가능 기간 시작일을 입력해 주세요", i, "reservationStartDate");
						return false;
					}

					if(dataItem.reservationEndDate == "" || dataItem.reservationEndDate == null){
						valueCheckName("예약 주문 가능 기간 종료일을 입력해 주세요", i, "reservationEndDate");
						return false;
					}

					if(dataItem.stockQuantity == null){
						valueCheckName("주문재고를 입력해 주세요", i, "stockQuantity");
						return false;
					}

					// if(dataItem.reservationEndDate > dataItem.reservationEndDateOld){
					// 	valueCheck("예약 주문 가능 기간 종료일을 변경 후 계산을 클릭해 주세요.", "ifCalc-btn-id"+(i+1));
					// 	return false;
					// }

					if(dataItem.orderIfDate == "" || dataItem.orderIfDate == null){
						valueCheckName("주문수집 I/F일을 입력해 주세요", i, "orderIfDate");
						return false;
					}

					if(dataItem.orderIfDate != dataItem.orderIfDateOld){
						valueCheck("주문수집 I/F일 변경 후 계산을 클릭해 주세요.", "raaCalc-btn-id"+(i+1));
						return false;
					}
				}
			}
		}

		if(!viewModel.ilGoodsDetail.get('itemWarehouseList')) {
			valueCheck("배송정책이 선택되지 않았습니다.", "itemWarehouseTbody");
			return false;
		}
		else {
			for (i=0; i<viewModel.ilGoodsDetail.get('itemWarehouseList').length; i++) {

				let itemWarehouseShippingTemplate = $("#itemWarehouseShippingTemplateList"+viewModel.ilGoodsDetail.get('itemWarehouseList')[i].ilItemWarehouseId+"_"+viewModel.ilGoodsDetail.get('itemWarehouseList')[i].deliveryTypeCode);

				if (itemWarehouseShippingTemplate.val() == null || itemWarehouseShippingTemplate.val() == "") {
					fnKendoMessage({message : "배송정책이 선택되지 않았습니다.", ok : function() {
						itemWarehouseShippingTemplate.data("kendoDropDownList").focus();
					}});
					return false;
				}
			}
		}

		if(goodsType == "GOODS_TYPE.PACKAGE"){
			/* 상품 이미지 */
			var goodsPackageImageType = viewModel.ilGoodsDetail.goodsPackageImageType;
			if ((goodsPackageImageType =="GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS" && viewModel.get("packageImageList").length == 0) || (goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED") && viewModel.get("packageImageMixList").length == 0) {
				valueCheck("묶음상품 전용 이미지는 필수선택입니다.", "goodsPackageImageType_0");
				//fnKendoMessage({message: '<span style="color: red;font-size: 18pt;font-weight: bolder;">[묶음상품 전용 이미지] </span>는 필수선택입니다.'});
				return false;
			}
			/* 상품 이미지 */

			var goodsPackageBasicDescValue = $('#goodsPackageBasicDesc').data("kendoEditor").value();
			if(viewModel.ilGoodsDetail.goodsPackageBasicDescYn == "Y" && goodsPackageBasicDescValue == ""){	//상품 상세 기본 정보 직접등록이면
				valueCheck("상품 상세 기본 정보를 입력해 주세요.", "goodsPackageBasicDescYn");
				return false;
			}
		}

		/* 상품 등록 승인 관련 처리 */
		let approvalChkVal = $('input:checkbox[name="approvalCheckbox"]').is(":checked");	//승인요청 처리 여부

		if(approvalChkVal) {
			if (apprAdminGridDs.data().length == 0) {
				valueCheck("승인관리자를 지정해 주세요.", "fnApprAdmin");
				return false;
			}
		}
		/* 상품 등록 승인 관련 처리 */
	};

	//혜택/구매정보 > 상품추가 버튼 > 추가 상품 검색, 추천상품 등록 > 상품 추가 버튼
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

								if(element.goodsId != selGoodsId){																//이미 들어가 있는 추가 상품 코드를 제외하고 추가 처리
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

						console.log("goodsRecommendLength : ", goodsRecommendLength);

						data.forEach(function(element, index, array){
							if(goodsRecommendLength > 0) { //이미 입력한 값이 있다면
								var selGoodsId = "";
								for(var i=0; i < goodsRecommendLength; i++){
									if(element.goodsId == viewModel.ilGoodsDetail.get('goodsRecommendList')[i].targetGoodsId){	//이미 들어가 있는 추가 상품 코드를 체크
										selGoodsId = element.goodsId;
									}
								}

								if(element.goodsId != selGoodsId){																//이미 들어가 있는 추가 상품 코드를 제외하고 추가 처리
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

	//추가상품, 추천상품 재고 항목의 연동재고 팝업 func
	function fnGoodsStockDetailPopup(ilItemCode, ilItemWarehouseId, stockOrderYn) {
		if(ilItemWarehouseId != null && stockOrderYn == "Y") {
			fnKendoPopup({
				id: 'goodsStockMgm',
				title: '재고 상세 정보',
				param: {
					"ilItemWarehouseId":ilItemWarehouseId
				,	"baseDt":fnGetToday()
				},
				src: '#/goodsStockMgm',
				width: '1100px',
				height: '800px',
				success: function(id, data) {
				}
			});
		}
		else if(ilItemCode != null && stockOrderYn == "N") {
			let option = {};
			option.url = "#/itemNonInterfacedStock";
			option.menuId = 740;
			option.data = {"paramIlItemCode":ilItemCode};
			option.target = "itemNonInterfacedStock";

			fnGoNewPage(option);
		}
		else {
			fnKendoMessage({ message : "재고관련 정보가 존재하지 않습니다. 관리자에게 문의하세요." });
		}
	}

	function fnPoTypeDetailInformationPopup(ilItemCode, erpItemName, ilPoTypeId) { // 발주 상세정보 팝업 호출
		fnKendoPopup({
			id : 'fnPoTypeDetailInformationPopup',
			title : '발주 상세정보',
			src : '#/poTypeDetailInformationPopup',
			width : '750px',
			height : '400px',
			param : {
				erpItemCode : ilItemCode,
				erpItemName : erpItemName,
				ilPoTpId : ilPoTypeId
			}
		});
	}

	function fnWarehouseShippingTemplateList(goodsShippingTemplateList, data) {	//배송/발주 정보 > 배송유형 > 출고처별 배송정책 dropdownlist
		if(data.rows.length > 0){
			var warehouseInfo = [];
			var orgIlShippingTemplateValue = "";

			for(var i=0; i < data.rows.length; i++){
				if(i==0){
					warehouseInfo[i] = "[" + data.rows[i].warehouseName+ "]<br/>" + data.rows[i].address1 + " " + data.rows[i].address2;
				}
				else{
					warehouseInfo[i] = "[" + data.rows[i].warehouseName+ "]";
				}

				data.rows[i].warehouseInfo = warehouseInfo[i];
			}
			viewModel.ilGoodsDetail.set('itemWarehouseList', data.rows);

			//console.log("viewModel.ilGoodsDetail.itemWarehouseList : ", viewModel.ilGoodsDetail.itemWarehouseList);

			for(var i=0; i < data.rows.length; i++){
				if(goodsShippingTemplateList != "" && goodsShippingTemplateList != undefined){
					for(var j=0; j < goodsShippingTemplateList.length; j++) {
						if(data.rows[i].urWarehouseId == goodsShippingTemplateList[j].urWarehouseId) {
							orgIlShippingTemplateValue = goodsShippingTemplateList[j].origIlShippingTemplateId;
						}
					}
				}
				else{
					orgIlShippingTemplateValue = data.rows[i].orgIlShippingTemplateId;
				}
				viewModel.ilGoodsDetail.set("itemWarehouseList["+i+"].itemWarehouseShippingTemplateList", orgIlShippingTemplateValue);

				let rowIndex = i;

				//배송 정책 DropdownList 추가
				fnKendoDropDownList({
					id			: 'itemWarehouseShippingTemplateList'+data.rows[i].ilItemWarehouseId+'_'+data.rows[i].deliveryTypeCode,
					tagId		: 'itemWarehouseShippingTemplateList'+data.rows[i].ilItemWarehouseId+'_'+data.rows[i].deliveryTypeCode,
					url			: "/admin/goods/regist/itemWarehouseShippingTemplateList",
					params		: {ilItemCode : ilItemCode, urWarehouseId : data.rows[i].urWarehouseId},
					async		: false,
					style		: {},
					textField	:"shppingTemplateName",
					valueField	: "orgIlShippingTemplateId",
					dataBound: function(e) {
						var shppingTemplateName = this.text();
						viewModel.ilGoodsDetail.itemWarehouseList[rowIndex].shppingTemplateName = this.text();
						//console.log("viewModel.ilGoodsDetail.itemWarehouseList : ", viewModel.ilGoodsDetail.itemWarehouseList);
					},
				}).bind("change", function(e){
					viewModel.ilGoodsDetail.itemWarehouseList[rowIndex].shppingTemplateName = e.sender._oldText;
					//console.log("viewModel.ilGoodsDetail.itemWarehouseList : ", viewModel.ilGoodsDetail.itemWarehouseList);
				});
			}
		}
	}

	//가격 정보 > 행사/할인내역 > 할인설정 팝업 호출
	function fnGoodsDiscountPopup(discountTypeCode, discountTypeCodeName) {
		if(ilGoodsId != ""){
			let params = {};
			let itemStandardPrice = 0;			//품목 원가
			let itemRecommendedPrice = 0;		//품목 정상가

			if(viewModel.ilGoodsDetail.itemPriceList){
				itemStandardPrice = viewModel.ilGoodsDetail.itemPriceList[0].standardPrice;
				itemRecommendedPrice = viewModel.ilGoodsDetail.itemPriceList[0].recommendedPrice;
			}

			params.itemInfo = {
				goodsId : ilGoodsId,							// 상품 ID
				discountTypeCode : discountTypeCode,			// 우선, 즉시 등 할인 유형 코드
				itemStandardPrice : itemStandardPrice,			// 품목 원가
				itemRecommendedPrice : itemRecommendedPrice,	// 품목 정상가
				taxYn : viewModel.ilGoodsDetail.taxYn,			// 과세구분
				itemCode : ilItemCode						// 품목 코드
			};

			var priorityAndImmediatelyDiscountPopupName = "";
			if(discountTypeCode != "" && discountTypeCode != "undefined"){
				params.discountTypeCode = discountTypeCode;
				priorityAndImmediatelyDiscountPopupName = discountTypeCodeName + " 등록";
			}

			var goodsDiscountList = [];
			var goodsDiscountApprList = [];
			var requestIlGoodsDiscountId = "";
			if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsDiscountPriorityList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsDiscountPriorityApproList');
			}
			else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsDiscountImmediateList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsDiscountImmediateApproList');
			}
			else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsDiscountEmployeeList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsDiscountEmployeeApproList');
			}

			params.goodsDiscountList = goodsDiscountList;
			params.goodsDiscountApprList = goodsDiscountApprList;

			/*console.log("goodsDiscountList : ", goodsDiscountList);
			console.log("goodsDiscountApprList : ", goodsDiscountApprList);
			console.log("PG_SESSION : ", PG_SESSION);*/

			fnKendoPopup({
				id			: "priorityAndImmediatelyDiscountPopup",
				title		: priorityAndImmediatelyDiscountPopupName, // 해당되는 Title 명 작성
				width		: "1600px",
				height		: "500px",
				scrollable	: "yes",
				src			: "#/priorityAndImmediatelyDiscountPopup",
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){

						/*console.log("data.goodsDiscountList : ", data.goodsDiscountList);
						console.log("data.goodsDiscountApprList : ", data.goodsDiscountApprList);
						console.log("data.goodsDiscountApprList : ", data.goodsDiscountApprList);*/

						if(goodsDiscountList){
							for (var i = goodsDiscountList.length - 1; i >= 0; i--) {
								goodsDiscountList.splice(i, 1);
							}
						}

						var discountMethodTypeCodeName = "";
						let rowCountNum = 1;

						if(data.goodsDiscountList.length > 1){
							rowCountNum = 2;
						}

						if(data.goodsDiscountApprList != null && data.goodsDiscountList.length > 0 && data.goodsDiscountApprList.length > 0){	//할인 승인 관리자 정보 저장
							if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
								viewModel.ilGoodsDetail.set('goodsDiscountPriorityApproList', data.goodsDiscountApprList);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
								viewModel.ilGoodsDetail.set('goodsDiscountImmediateApproList', data.goodsDiscountApprList);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
								viewModel.ilGoodsDetail.set('goodsDiscountEmployeeApproList', data.goodsDiscountApprList);
							}
						}
						else {
							if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
								viewModel.ilGoodsDetail.set('goodsDiscountPriorityApproList', []);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
								viewModel.ilGoodsDetail.set('goodsDiscountImmediateApproList', []);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
								viewModel.ilGoodsDetail.set('goodsDiscountEmployeeApproList', []);
							}
						}

						if(data.goodsDiscountList != null && data.goodsDiscountList.length > 0){												//할인 내역 저장
							data.goodsDiscountList.forEach(function(element, index, array){
								//해당 부분 팝업에서 넘어올수 있도록 요청
								if(element.discountMethodTypeCode == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE"){
									discountMethodTypeCodeName = "고정가할인";
								}
								else if(element.discountMethodTypeCode == "GOODS_DISCOUNT_METHOD_TP.FIXED_RATE"){
									discountMethodTypeCodeName = "정률할인";
								}
								else if(element.discountMethodTypeCode == "GOODS_DISCOUNT_METHOD_TP.EMPLOYEE"){
									discountMethodTypeCodeName = "개별할인";
								}

								var trListVisible = true;
								if(index > 1){
									trListVisible = false;
								}

								var approvalStatusCodeName = "승인요청";
								if(element.approvalStatusCodeName != null) {
									approvalStatusCodeName = element.approvalStatusCodeName;
								}

								let rowNum = index+1;
								let displayAllow = ";";				//rowspan에 따른 구분, 시작일, 종료일 display 여부

								if(rowNum > 1){
									displayAllow = "none;";
								}

								let apprReqInfo = PG_SESSION.loginName + "(" + PG_SESSION.loginId + ")";
								let apprReqNm = PG_SESSION.loginName;
								let apprReqUserId = PG_SESSION.userId;
								let apprReqUserLoginId = PG_SESSION.loginId;
								let apprInfo = null;

								let apprSubNm = null;
								let apprSubUserId = null;
								let apprSubUserLoginId = null;
								let apprNm = null;
								let apprUserId = null;
								let apprUserLoginId = null;

								if(data.goodsDiscountApprList != null && data.goodsDiscountApprList.length > 1){
									for(var i=0; i < data.goodsDiscountApprList.length; i++){
										if(i == 0){
											apprInfo = data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
											apprSubNm = data.goodsDiscountApprList[i].apprUserName;
											apprSubUserId = data.goodsDiscountApprList[i].apprUserId;
											apprSubUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
										}
										if(i == 1){
											apprInfo = apprInfo + "</BR>" + data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
											apprNm = data.goodsDiscountApprList[i].apprUserName;
											apprUserId = data.goodsDiscountApprList[i].apprUserId;
											apprUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
										}
									}
								}

								if(data.goodsDiscountApprList != null && data.goodsDiscountApprList.length == 1){
									for(var i=0; i < data.goodsDiscountApprList.length; i++){
										apprInfo = data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
										apprNm = data.goodsDiscountApprList[i].apprUserName;
										apprUserId = data.goodsDiscountApprList[i].apprUserId;
										apprUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
									}
								}

								if(element.approvalStatusCode != "APPR_STAT.NONE"){
									apprReqInfo = element.apprReqInfo;
									apprReqNm = element.apprReqNm;
									apprReqUserId = element.apprReqUserId;
									apprReqUserLoginId = element.apprReqUserLoginId;
									apprInfo = element.apprInfo;
									apprSubNm = element.apprSubNm;
									apprSubUserId = element.apprSubUserId;
									apprSubUserLoginId = element.apprSubUserLoginId;
									apprNm = element.apprNm;
									apprUserId = element.apprUserId;
									apprUserLoginId = element.apprUserLoginId;
								}

								goodsDiscountList.push({
									trListVisible				: trListVisible,
									rowNum						: rowNum,
									displayAllow				: displayAllow,
									rowCountNum					: rowCountNum,
									goodsId						: ilGoodsId,
									goodsDiscountApprId			: element.goodsDiscountApprId,
									goodsDiscountId				: element.goodsDiscountId,
									apprReqInfo					: apprReqInfo,
									apprReqNm					: apprReqNm,
									apprReqUserId				: apprReqUserId,
									apprReqUserLoginId			: apprReqUserLoginId,
									apprInfo					: apprInfo,
									apprSubNm					: apprSubNm,
									apprSubUserId				: apprSubUserId,
									apprSubUserLoginId			: apprSubUserLoginId,
									apprNm						: apprNm,
									apprUserId					: apprUserId,
									apprUserLoginId				: apprUserLoginId,
									discountTypeCode			: discountTypeCode,
									discountTypeCodeName		: discountTypeCodeName,
									approvalStatusCode			: element.approvalStatusCode,
									approvalStatusCodeName		: approvalStatusCodeName,
									discountStartDateTime		: element.discountStartDateTime,
									discountStartDate			: element.discountStartDate,
									discountStartHour			: element.discountStartHour,
									discountStartMinute			: element.discountStartMinute,

									discountEndDateTime			: element.discountEndDateTime,
									discountEndDate				: element.discountEndDate,
									discountEndDateOriginal		: element.discountEndDate,
									discountEndHour				: element.discountEndHour,
									discountEndHourOriginal		: element.discountEndHour,
									discountEndMinute			: element.discountEndMinute,
									discountEndMinuteOriginal	: element.discountEndMinute,

									discountMethodTypeCode		: element.discountMethodTypeCode,
									discountMethodTypeCodeName	: discountMethodTypeCodeName,

									itemRecommendedPrice		: element.itemRecommendedPrice,
									itemStandardPrice			: element.itemStandardPrice,
									discountAmount				: element.discountAmount,
									marginRate					: element.marginRate,
									discountRatio				: element.discountRatio,
									discountSalePrice			: element.discountSalePrice
								});

								if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
									viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
									viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", true);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
								}
								else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
									viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
									viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", true);								//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
								}
								else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
									viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListNoDataVisible", false);						//임직원 할인정보 > 임직원 개별할인 정보 > Nodata Visible
									viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListInsertTbodyVisible", false);					//임직원 할인정보 > 임직원 개별할인 정보 > 초기 Insert Visible
									viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListDataVisible", true);							//임직원 할인정보 > 임직원 개별할인 정보 > Data Visible
								}
							});
						}
						else {
							if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", false);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", false);								//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
								viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListNoDataVisible", false);						//임직원 할인정보 > 임직원 개별할인 정보 > Nodata Visible
								viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListInsertTbodyVisible", true);					//임직원 할인정보 > 임직원 개별할인 정보 > 초기 Insert Visible
								viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListDataVisible", false);							//임직원 할인정보 > 임직원 개별할인 정보 > Data Visible
							}
						}
					}
					//모든 할인 설정 이후에 판매 가격 정보 Refresh 처리
					fnGoodsPriceRefresh();
				}
			});
		}
	}

	function fnGoodsPriceRefresh(){		//모든 할인 설정 이후에 판매 가격 정보 Refresh 처리
		fnAjax({
				url		: '/admin/goods/regist/goodsPriceRefresh',
				params	: {ilGoodsId : ilGoodsId},
				async	: false,
				contentType : 'application/json',
				isAction : 'select',
				success	: function(data){
					if(data.goodsPrice.length > 0){
						viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfoNodata", false);
						viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfo", true);
						viewModel.ilGoodsDetail.set("goodsPrice", data.goodsPrice);				//가격 정보 > 판매 가격정보
					}
				}
		});
	}

    function fnStoreStockInfoVisible(visible){
    	if(visible) {
    		$("#storeStockInfoUp").show();
    		$("#storeStockInfoDown").hide();
    		$("#storeStockInfoList").show();
    	}
    	else{
    		$("#storeStockInfoUp").hide();
    		$("#storeStockInfoDown").show();
    		$("#storeStockInfoList").hide();
    	}
    }

    function fnStoreScheduleModal (urStoreId, storeName) {
    	fnKendoPopup({
            id : 'itemStoreSchedule',
            title : storeName + ' 스케쥴 정보',
            src : '#/itemStoreSchedule',
            width : '1000px',
            height : '800px',
            param : {
            	urStoreId : urStoreId,
            }
        });
    }

    function fnItemStorePriceLogModal(ilItemCd, urStoreId, storeName) {
    	fnKendoPopup({
            id : 'itemStorePriceLog',
            title : 'O2O 매장 판매가 정보 <' + storeName + '>',
            src : '#/itemStorePriceLog',
            width : '1000px',
            height : '800px',
            param : {
            	ilItemCd : ilItemCd,
            	urStoreId : urStoreId
            }
        });
    }

	$scope.fnGoodsSearchPopup = function(kind){		//혜택/구매정보 > 추가상품 > 추가상품 검색 팝업 호출
		fnGoodsSearchPopup(kind);
	};

	$scope.fnGoodsStockDetailPopup = function(ilItemCode, ilItemWarehouseId, stockOrderYn){
		fnGoodsStockDetailPopup(ilItemCode, ilItemWarehouseId, stockOrderYn)
	};

	$scope.fnPoTypeDetailInformationPopup = function(ilItemCode, erpItemName, ilPoTypeId) { // 발주 상세정보 팝업
		fnPoTypeDetailInformationPopup(ilItemCode, erpItemName, ilPoTypeId);
	};

	/**
	 * HGRM-8527: 샵풀무원 상품코드 영역 추가
	 */
	(function() {
		const ADD_CLASSNAME = "js__add-goods-code";
		const REMOVE_CLASSNAME = "js__remove-goods-code";

		// 풀무원샵 상품코드 추가/삭제 버튼 클릭 이벤트
		$("#goods-code-container").off("click").on("click", function(e) {
			e.stopPropagation();

			const $target = $(e.target);

			// 추가 버튼
			if($target.hasClass(ADD_CLASSNAME)) {
				addGoodsCode();
			} else if($target.hasClass(REMOVE_CLASSNAME)) {
				// 삭제 버튼
				fnKendoMessage({
					message : fnGetLangData({key : '', nullMsg : "풀무원샵 상품코드를 삭제하시겠습니까?"}),
					type: "confirm",
					ok: function() {
						removeGoodsCode($target);
					},
				});
			}
		});

		// 풀무원샵 상품코드 입력 이벤트
		$("#goods-code-container").off("keyup").on("keyup", ".js__goods-code", function(e) {
			const $target = $(e.target),
				regEx = /[^0-9]/gm;
			let value = $target.val();

			if(!value || !value.length) return;

			// 숫자가 아닌 문자가 있을 경우
			if(value.match(regEx)) {
				value = value.replace(regEx, '');
				$target.val(value);

				fnShowValidateMessage($target, ALERT_MESSAGES.ONLY_NUM);
			}
		});

		// 추가
		function addGoodsCode() {
			const $container = $('#goods-code-container');

			if(!$container.length) return;

			const $el = $('<div class="goods-code-wrapper"></div>');
			const $input = $('<input type="text" name="goodsCode" class="comm-input goods-code js__goods-code">');
			const $button = $('<button type="button" class="btn-s btn-red js__remove-goods-code">삭제</button>');

			$el.append($input).append($button);

			$container.append($el);
		}

		// 삭제
		function removeGoodsCode($el) {
			if(!$el || !$el.hasClass('js__remove-goods-code')) return;

			const $target = $el.closest('.goods-code-wrapper');
			$target.remove();
		}
	})();