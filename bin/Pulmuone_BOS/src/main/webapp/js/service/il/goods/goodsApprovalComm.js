/**-----------------------------------------------------------------------------
 * description 		 : 상품승인 공통 FUnc
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.08		임상건   최초생성
 * @
 * **/
	var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

//	let loadDate = new Date();
//	let loadDateTime = loadDate.oFormat("yyyy-MM-dd HH:mm:ss");
	var loadDate = new Date();
	var loadDateTime = loadDate.oFormat("yyyy-MM-dd HH:mm:ss");

	fnApproInitialize();
	//Initialize PageR
	function fnApproInitialize(){
		fnApproInit();
		fnApprAdminInit();

		if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") {
			$("#inputForm input").attr("disabled", true);
		}
	}

	function fnApproInit(){
		//승인관리자 정보 - 상품생성시 기본값 설정 필요.
		$('#approvalCheckbox').prop("checked",true);
		$('#apprDiv').show();

		//승인 요청처리 여부
		$("#approvalCheckbox").click(function(){
			if($("#approvalCheckbox").prop("checked") == true){
				$('#approvalCheckbox').prop("checked",true);
				$('#apprDiv').show();
			}else{
				$('#approvalCheckbox').prop("checked",false);
				$('#apprDiv').hide();
			}
		});
	}

	// 승인관리자 선택 팝업 호출
	function fnApprAdmin(){
//		console.log("### apprKindTp  ===>"+ apprKindTp);
		var param = {'taskCode' : apprKindTp };
		fnKendoPopup({
			id		: 'approvalManagerSearchPopup',
			title	: '승인관리자 선택',
			src		: '#/approvalManagerSearchPopup',
			param	: param,
			width	: '1300px',
			heigh	: '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					$('#apprGrid').gridClear(true);

					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					if(authManager1.apprUserId != undefined){							//1차 승인관리자가 미지정이라면
						var objManager1 = new Object();

						objManager1["apprAdminInfo"] = "1차 승인관리자";
						objManager1["adminTypeName"] = authManager1.adminTypeName;
						objManager1["apprUserName"] = authManager1.apprUserName;
						objManager1["apprKindTp"] = authManager1.apprKindType;
						objManager1["apprManagerTp"] = authManager1.apprManagerType
						objManager1["apprUserId"] = authManager1.apprUserId;
						objManager1["apprLoginId"] = authManager1.apprLoginId;
						objManager1["organizationName"] = authManager1.organizationName;
						objManager1["userStatusName"] = authManager1.userStatusName;
						objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
						objManager1["grantAuthYn"] = authManager1.grantAuthYn;
						objManager1["grantUserName"] = authManager1.grantUserName;
						objManager1["grantLoginId"] = authManager1.grantLoginId;
						objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
						objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
						objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;
						apprAdminGridDs.add(objManager1);
					}

					var objManager2 = new Object();

					objManager2["apprAdminInfo"] = "2차 승인관리자";
					objManager2["adminTypeName"] = authManager2.adminTypeName;
					objManager2["apprUserName"] = authManager2.apprUserName;
					objManager2["apprKindTp"] = authManager2.apprKindType;
					objManager2["apprManagerTp"] = authManager2.apprManagerType
					objManager2["apprUserId"] = authManager2.apprUserId;
					objManager2["apprLoginId"] = authManager2.apprLoginId;
					objManager2["organizationName"] = authManager2.organizationName;
					objManager2["userStatusName"] = authManager2.userStatusName;
					objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
					objManager2["grantAuthYn"] = authManager2.grantAuthYn;
					objManager2["grantUserName"] = authManager2.grantUserName;
					objManager2["grantLoginId"] = authManager2.grantLoginId;
					objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
					objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
					objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;
					apprAdminGridDs.add(objManager2);
				}
			}
		});
	}

	// 승인관리자 그리드 초기화
	function fnApprClear(){
		$('#apprGrid').gridClear(true);
		$('#apprSubUserId').val('');
		$('#apprUserId').val('');
	}

	//승인관리자 정보 Grid
	function fnApprAdminInit(){
		apprAdminGridDs =  new kendo.data.DataSource();

		apprAdminGridOpt = {
			dataSource : apprAdminGridDs,
			editable : false,
			noRecordMsg: '승인관리자를 선택해 주세요.',
			columns : [{
				field : 'apprAdminInfo',
				title : '승인관리자정보',
				width : '100px',
				attributes : {style : 'text-align:center'}
				},{
				field : 'adminTypeName',
				title : '계정유형',
				width : '100px',
				attributes : {style : 'text-align:center'}
				},{
					field : 'apprUserName',
					title : '관리자이름/아이디',
					width : '100px',
					attributes : {style : 'text-align:center'},
					template : function(dataItem){
						let returnValue;
						returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
						return returnValue;
					}
				},{
					field : 'organizationName',
					title : '조직/거래처 정보',
					width : '100px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'teamLeaderYn',
					title : '조직장여부',
					width : '80px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'userStatusName',
					title : 'BOS 계정상태',
					width : '80px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'grantUserName',
					title : '권한위임정보<BR/>(이름/ID)',
					width : '100px',
					attributes : {style : 'text-align:center'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field : 'userStatusName',
					title : '권한위임기간',
					width : '150px',
					attributes : {style : 'text-align:left'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field : 'grantUserStatusName',
					title : '권한위임자<BR/>BOS 계정상태',
					width : '100px',
					attributes : {style : 'text-align:left'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantUserStatusName;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field:'addCoverageId', hidden:true
				},{
					field:'includeYn', hidden:true
				}
			]
		};

		apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();
	}

	function fnApprCancel() {
//		if( viewModel.ilGoodsDetail.ilGoodsApprId == null && viewModel.ilGoodsDetail.ilGoodsApprId == undefined){
//			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
//			return;
//		}

		let params = {};
		params.ilGoodsApprIdList = [];
		let url = "";


		if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS"){
			if( viewModel.ilGoodsDetail.goodsRegistGoodsApprId == null && viewModel.ilGoodsDetail.goodsRegistGoodsApprId == undefined){
				fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
				return;
			}

			params.ilGoodsApprIdList[0] = viewModel.ilGoodsDetail.goodsRegistGoodsApprId;
			url = "/admin/approval/goods/putCancelRequestApprovalGoodsRegist";
		}
		else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT"){
			if( viewModel.ilGoodsDetail.goodsClientGoodsApprId == null && viewModel.ilGoodsDetail.goodsClientGoodsApprId == undefined){
				fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
				return;
			}

			params.ilGoodsApprIdList[0] = viewModel.ilGoodsDetail.goodsClientGoodsApprId;
			url = "/admin/approval/goods/putCancelRequestApprovalGoodsClient";
		}

		fnKendoMessage({
			type : "confirm",
			message : "요청철회 하시겠습니까?",
			ok : function() {
				fnAjax({
					url			: url,
					params		: params,
					contentType	: "application/json",
					success		: function( data ){
						fnKendoMessage({  message : "요청철회가 완료되었습니다."
							, ok : function(){
								window.location.reload(true);
							}
						});
					},
					fail : function(data, code){
						fnKendoMessage({
							message : code.message,
							ok : function(e) {
								window.location.reload(true);
							}
						});
					},
					error		: function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			}
		});
	}

	function fnGoodsRegistApprDetailPopup(){
		if( viewModel.ilGoodsDetail.goodsRegistGoodsApprId == null && viewModel.ilGoodsDetail.goodsRegistGoodsApprId == undefined){
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

//		fnKendoPopup({
//			id		: 'goodsApprovalDetailPopup',
//			title	: "상품등록 승인내역",
//			src		: '#/approvalHistoryPopup',
//			param	: { "taskCode" : "APPR_KIND_TP.GOODS_REGIST", "taskPk" : viewModel.ilGoodsDetail.goodsRegistGoodsApprId},
//			width	: '680px',
//			height	: '585px',
//			success	: function( id, data ){
//			}
//		});

		let companyType = PG_SESSION.companyType;		// 회사타입
		if (companyType == "COMPANY_TYPE.HEADQUARTERS") {
			var option = new Object();
			if (
				(viewModel.ilGoodsDetail.get("goodsRegistApprReqUserId") == PG_SESSION.userId) // 상품등록 승인 요청자이면서
				&& (viewModel.ilGoodsDetail.get("goodsRegistApprStat") != 'APPR_STAT.APPROVED') // 상품등록 승인완료 상태가 아니면
			)
				option.url = "#/approvalGoodsRegistRequest"; // 상품등록 승인요청 URL
			else
				option.url = "#/approvalGoodsRegistAccept"; // 상품등록 승인관리 URL

			option.target = "_blank";
			option.data = { // 승인 화면으로 전달할 Data
				ilGoodsId : ilGoodsId
			};
			fnGoNewPage(option);
		}
	}

	function fnItemClientApprDetailPopup() {
		if( viewModel.ilGoodsDetail.itemClientItemApprId == null && viewModel.ilGoodsDetail.itemClientItemApprId == undefined){
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

		let companyType = PG_SESSION.companyType;		// 회사타입
		var option = new Object();
		if (companyType == "COMPANY_TYPE.HEADQUARTERS")
			option.url = "#/approvalItemClientAccept"; // 거래처 품목 승인관리 URL
		else
			option.url = "#/approvalItemClientRequest"; // 거래처 품목 승인요청 URL

		option.target = "_blank";
		option.data = { // 승인 화면으로 전달할 Data
			ilItemCode : ilItemCode
		};
		fnGoNewPage(option);
	}

	function fnGoodsClientApprDetailPopup() {
		if( viewModel.ilGoodsDetail.goodsClientGoodsApprId == null && viewModel.ilGoodsDetail.goodsClientGoodsApprId == undefined){
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

//		fnKendoPopup({
//			id		: 'goodsApprovalDetailPopup',
//			title	: "거래처 상품수정 승인내역",
//			src		: '#/approvalHistoryPopup',
//			param	: { "taskCode" : "APPR_KIND_TP.GOODS_CLIENT", "taskPk" : viewModel.ilGoodsDetail.goodsClientGoodsApprId},
//			width	: '680px',
//			height	: '585px',
//			success	: function( id, data ){
//			}
//		});

		let companyType = PG_SESSION.companyType;		// 회사타입
		var option = new Object();
		if (companyType == "COMPANY_TYPE.HEADQUARTERS")
			option.url = "#/approvalGoodsClientAccept"; // 거래처 상품 승인관리 URL
		else
			option.url = "#/approvalGoodsClientRequest"; // 거래처 상품 승인요청 URL

		option.target = "_blank";
		option.data = { // 승인 화면으로 전달할 Data
			ilGoodsId : ilGoodsId
		};
		fnGoNewPage(option);
	}

/*
	function fnApprHtml(apprStat, saleStatus){

//		console.log("apprStat : ", apprStat);
//		console.log("saleStatus : ", saleStatus);

		if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS"
			|| (PG_SESSION.companyType == "COMPANY_TYPE.CLIENT" && saleStatus != "SALE_STATUS.SAVE")){
			if(apprStat == "APPR_STAT.REQUEST"){				//승인요청
				viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);
				viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);
				viewModel.ilGoodsDetail.set("apprCancelBtnVisible", true);
				viewModel.ilGoodsDetail.set("saveBtnVisible", false);

				$('#approvalCheckbox').prop("checked",false);
				$('#apprDiv').hide();
				$('#apprChkDiv').hide();
			}
			else if(apprStat == "APPR_STAT.SUB_APPROVED"){		//승인 완료(부)
				viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);	// 승인 상태 > 전체 내역 Visible
				viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);		// 승인 상태 > 승인 상태 > 승인내역상세 Button Visible
				viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
				viewModel.ilGoodsDetail.set("saveBtnVisible", false);					// 상품 저장 Button Visible
				viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible

				$('#approvalCheckbox').prop("checked",false);
				$('#apprDiv').hide();
				$('#apprChkDiv').hide();
			}
			else if(apprStat == "APPR_STAT.APPROVED"){		//승인완료
				if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS") {
					viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);	// 승인 상태 > 전체 내역 Visible
					viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);		// 승인 상태 > 승인 상태 > 승인내역상세 Button Visible
					viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
					viewModel.ilGoodsDetail.set("saveBtnVisible", true);					// 상품 저장 Button Visible
					viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible
					viewModel.set("discountBtnVisible", true);								// 모든 할인 > 할인설정 버튼 Visaible

					$('#approvalCheckbox').prop("checked",false);
					$('#apprDiv').hide();
					$('#apprChkDiv').hide();
				}
				else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") {	//거래처라면 승인완료 이후 다시 승인 진행을 할수 있도록
					viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", false);
					viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);
					viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
					viewModel.ilGoodsDetail.set("saveBtnVisible", true);					// 상품 저장 Button Visible
					viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible

					$('#approvalCheckbox').prop("checked",true);
					$('#apprDiv').show();
				}
			}
			else if(apprStat == "APPR_STAT.DENIED"){			//승인 반려
				viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);
				viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", true);
				viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
				viewModel.ilGoodsDetail.set("saveBtnVisible", true);					// 상품 저장 Button Visible
				viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", true);		// 반려 사유 Visible

				$('#approvalCheckbox').prop("checked",false);
				$('#apprDiv').hide();
			}
			else if(apprStat == "APPR_STAT.CANCEL"){			//요청 철회
				viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);
				viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", true);
				viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
				viewModel.ilGoodsDetail.set("saveBtnVisible", true);					// 상품 저장 Button Visible
				viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible

				if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS") {
					$('#approvalCheckbox').prop("checked",false);
					$('#apprDiv').hide();
				}
				else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") {
					$('#approvalCheckbox').prop("checked",true);
					$('#apprDiv').show();
				}
			}
			else{												//승인 요청에 대한 지정을 아무것도 하지 않았을때
				viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", false);
				viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);
				viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
				viewModel.ilGoodsDetail.set("saveBtnVisible", true);					// 상품 저장 Button Visible
				viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible

				$('#approvalCheckbox').prop("checked",true);
				$('#apprDiv').show();
			}
		}
		else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT" && saleStatus == "SALE_STATUS.SAVE"){
			viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", false);
			viewModel.ilGoodsDetail.set("visibleApprDetailPopupButton", false);
			viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);				// 요청철회 Button Visible
			viewModel.ilGoodsDetail.set("saveBtnVisible", false);					// 상품 저장 Button Visible
			viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);		// 반려 사유 Visible

			$('#approvalCheckbox').prop("checked",false);
			$('#apprChkDiv').hide();
			$('#apprDiv').hide();
		}
	}
*/
	function fnApprHtml() {
		// 승인관련 화면 초기값 설정
		viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", true);
		viewModel.ilGoodsDetail.set("approvalStatusNotification", '');
		viewModel.ilGoodsDetail.set("itemClientApprVisible", true);
		viewModel.ilGoodsDetail.set("goodsClientApprVisible", true);
		viewModel.ilGoodsDetail.set("visibleGoodsRegistApprDetailPopupButton", false);
		viewModel.ilGoodsDetail.set("visibleItemClientApprDetailPopupButton", false);
		viewModel.ilGoodsDetail.set("visibleGoodsClientApprDetailPopupButton", false);
		viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", false);
		viewModel.ilGoodsDetail.set("visibleClientApprDetailPopupButton", false);
		viewModel.ilGoodsDetail.set("saveBtnVisible", false);
		viewModel.ilGoodsDetail.set("apprCancelBtnVisible", false);
		$('#apprChkDiv').hide();
		$('#approvalCheckbox').prop("checked", false);
		$('#apprDiv').hide();

		if (viewModel.pageMode == "create") { // 상품등록시 data 초기화
			viewModel.ilGoodsDetail.set("goodsRegistGoodsApprId", "");
			viewModel.ilGoodsDetail.set("goodsRegistApprReqUserId", "");
			viewModel.ilGoodsDetail.set("goodsRegistApprStat", "");
			viewModel.ilGoodsDetail.set("goodsRegistApprStatName", "");
			viewModel.ilGoodsDetail.set("goodsRegistApprStatusCmnt", "");
			viewModel.ilGoodsDetail.set("goodsClientGoodsApprId", "");
			viewModel.ilGoodsDetail.set("goodsClientApprReqUserId", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStat", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStatName", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStatusCmnt", "");
			viewModel.ilGoodsDetail.set("saleStatus", "SALE_STATUS.SAVE");
		}
		else if (viewModel.ilGoodsDetail.goodsType == 'GOODS_TYPE.PACKAGE') {
			viewModel.ilGoodsDetail.set("goodsClientGoodsApprId", "");
			viewModel.ilGoodsDetail.set("goodsClientApprReqUserId", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStat", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStatName", "");
			viewModel.ilGoodsDetail.set("goodsClientApprStatusCmnt", "");
		}

		if (pageParam.apprKindType != undefined && (pageParam.apprKindType == "APPR_KIND_TP.GOODS_CLIENT")) { // 거래처 승인리스트에서 넘어온 경우
			viewModel.ilGoodsDetail.set("goodsApprovalStatusViewVisible", false);
			fnKendoMessage({ message : "승인요청 중인 상품정보 입니다.<BR>해당 정보는 수정이 불가하며, 승인완료 후 반영됩니다." });
		}
		else {
			if (PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS") { // 임직원인 경우
				// 상품할인 버튼노출 S
				if (viewModel.ilGoodsDetail.saleStatus != "SALE_STATUS.SAVE") // 상품 등록이 완료된 상태에서
					viewModel.set("discountBtnVisible", true); // 모든 할인 > 할인설정 버튼 Visaible
				// 상품할인 버튼노출 E

				// 상품등록 관련 S
				if (!fnIsEmpty(viewModel.ilGoodsDetail.goodsRegistApprStat))
					viewModel.ilGoodsDetail.set("visibleGoodsRegistApprDetailPopupButton", true); // 상품등록 승인 상세 팝업 버튼 노출

				if (viewModel.ilGoodsDetail.goodsRegistApprStat == 'APPR_STAT.DENIED')
					viewModel.ilGoodsDetail.set("goodsApprovalDeniedVisible", true); // 반려 사유 노출

				if (viewModel.ilGoodsDetail.goodsRegistApprStat == "APPR_STAT.REQUEST" && viewModel.ilGoodsDetail.goodsRegistApprReqUserId == PG_SESSION.userId) // 품목등록 요청상태에서 요청자일 경우
					viewModel.ilGoodsDetail.set("apprCancelBtnVisible", true); // 상품 등록 승인철회 버튼 노출
				// 상품등록 관련 E

				// 거래처 상품수정 관련 S
				if (viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품 수정 상세 팝업 버튼 노출 조건
					viewModel.ilGoodsDetail.set("visibleGoodsClientApprDetailPopupButton", true); // 거래처 상품수정 상세 팝업 버튼 노출
				else
					viewModel.ilGoodsDetail.set("goodsClientApprStatName", ''); // 승인건이 아니면 상품등록 상태명을 노출하지 않는다.
				// 거래처 상품수정 관련 E

				// 거래처 품목수정 관련 S
				if (viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목 수정 상세 팝업 버튼 노출 조건
					viewModel.ilGoodsDetail.set("visibleItemClientApprDetailPopupButton", true); // 거래처 품목수정 상세 팝업 버튼 노출
				else
					viewModel.ilGoodsDetail.set("itemClientApprStatName", ''); // 임직원의 경우 진행중인 승인건이 아니면 거래처 품목수정 상태명을 노출하지 않는다.
				// 거래처 품목수정 관련 E

				// 임직원인 경우, 저장버튼 및 승인관리자 지정 화면 처리 S
				if (
					!(viewModel.ilGoodsDetail.goodsRegistApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") // 상품등록 승인 처리중이 아니고
					&& !(viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목수정 승인중이 아닐 경우
					&& !(viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품수정 승인중이 아닐 경우
				) {
					viewModel.ilGoodsDetail.set("saveBtnVisible", true); // 저장버튼 노출
					if (viewModel.ilGoodsDetail.saleStatus == "SALE_STATUS.SAVE") { // 상품등록 완료가 아닐 경우 상품등록 승인 필요
						$('#apprChkDiv').show(); // 승인체크박스 노출
						$('#approvalCheckbox').prop("checked", true); // 승인체크박스 설정
						$('#apprDiv').show(); // 승인관리자 영역 노출
					}
				}
				else {
					if (viewModel.ilGoodsDetail.goodsRegistApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.ilGoodsDetail.set("approvalStatusNotification", "*상품 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "상품 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
					else if (viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.ilGoodsDetail.set("approvalStatusNotification", "*거래처 품목 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
					else if (viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.ilGoodsDetail.set("approvalStatusNotification", "*거래처 상품 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "거래처 상품 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
				}
				// 임직원인 경우, 저장버튼 및 승인관리자 지정 화면 처리 E
			}
			else if (PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") { // 거래처인 경우
				if (viewModel.ilGoodsDetail.saleStatus != "SALE_STATUS.SAVE") { // 거래처인 경우 상품 등록이 완료된 상태에서만 수정 가능
					// 거래처 상품수정 관련 S
					if (!fnIsEmpty(viewModel.ilGoodsDetail.goodsClientApprStat) && viewModel.ilGoodsDetail.goodsClientApprReqUserId == PG_SESSION.userId) // 거래처 상품 수정 요청자가 본인일 경우
						viewModel.ilGoodsDetail.set("visibleGoodsClientApprDetailPopupButton", true); // 거래처 상품수정 상세 팝업 버튼 노출

					if (viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" && viewModel.ilGoodsDetail.goodsClientApprReqUserId == PG_SESSION.userId) // 거래처 상품수정 요청상태에서 요청자일 경우
						viewModel.ilGoodsDetail.set("apprCancelBtnVisible", true); // 승인철회 버튼 노출
					// 거래처 상품수정 관련 E

					// 거래처 품목수정 관련 S
					if (viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") { // 거래처 품목 수정 상세 팝업 버튼 노출 조건
						if (viewModel.ilGoodsDetail.itemClientApprReqUserId == PG_SESSION.userId)
							viewModel.ilGoodsDetail.set("visibleItemClientApprDetailPopupButton", true); // 거래처 품목수정 상세 팝업 버튼 노출
					}
					else
						viewModel.ilGoodsDetail.set("itemClientApprStatName", ''); // 진행중인 승인건이 아니면 거래처 품목수정 상태명을 노출하지 않는다.
					// 거래처 품목수정 관련 E

					// 거래처인 경우, 저장버튼 및 승인관리자 지정 화면 처리 S
					if (
						!(viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목수정 승인 처리중이 아닐 경우
						&& !(viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품수정 승인 처리중이 아닐 경우
					) {
						viewModel.ilGoodsDetail.set("saveBtnVisible", true); // 저장버튼 노출
						$('#apprChkDiv').show(); // 승인체크박스 노출
						$('#approvalCheckbox').prop("checked", true); // 승인체크박스 설정
						$("#approvalCheckbox").attr("disabled", true); // 승인체크박스 설정 수정불가처리 - 항상 승인을 요청해야됨.
						$('#apprDiv').show(); // 승인관리자 영역 노출
					}
					else {
						if (viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.itemClientApprStat == "APPR_STAT.SUB_APPROVED") {
							viewModel.ilGoodsDetail.set("approvalStatusNotification", "*거래처 품목 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
							fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
						}
						else if (viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.REQUEST" || viewModel.ilGoodsDetail.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") {
							viewModel.ilGoodsDetail.set("approvalStatusNotification", "*거래처 상품 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
							fnKendoMessage({ message : "거래처 상품 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
						}
					}
					// 거래처인 경우, 저장버튼 및 승인관리자 지정 화면 처리 E
				}
				else {
					viewModel.ilGoodsDetail.set("approvalStatusNotification", "*상품 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
					fnKendoMessage({ message : "상품 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
				}
			}
		}

		if (viewModel.ilGoodsDetail.goodsType == 'GOODS_TYPE.PACKAGE') {
			viewModel.ilGoodsDetail.set("itemClientApprVisible", false); // 거래처 품목 승인 요청 영역 숨김
			viewModel.ilGoodsDetail.set("goodsClientApprVisible", false); // 거래처 상품 승인 요청 영역 숨김
		}
	}

	function fnSetItemApprData(itemData) {
		viewModel.ilGoodsDetail.set("itemClientItemApprId", itemData.itemClientItemApprId);
		viewModel.ilGoodsDetail.set("itemClientApprReqUserId", itemData.itemClientApprReqUserId);
		viewModel.ilGoodsDetail.set("itemClientApprStat", itemData.itemClientApprStat);
		viewModel.ilGoodsDetail.set("itemClientApprStatName", itemData.itemClientApprStatName);
		viewModel.ilGoodsDetail.set("itemClientApprStatusCmnt", itemData.itemClientApprStatusCmnt);

		// 승인정보 관련 화면 제어
		fnApprHtml();
	}

	function fnGoodsAuthInputAllow() {
		let companyType = PG_SESSION.companyType		// 회사타입
		let clientType = PG_SESSION.clientType;			// 거래처타입

		if(companyType == "COMPANY_TYPE.HEADQUARTERS") {
			apprKindTp = "APPR_KIND_TP.GOODS_REGIST";
		}
		else if(companyType == "COMPANY_TYPE.CLIENT") {
			apprKindTp = "APPR_KIND_TP.GOODS_CLIENT";
			$("#inputForm input").attr("disabled", true);
			$("#inputForm button").attr("disabled", true);

			// SPMO-1603 샵풀보스 상품 상세이미지 다운로드 권한 요청 - 벤더사
			if(clientType == "CLIENT_TYPE.VENDOR"){
				$("#fnOpenDownloadPopup").attr("disabled", false);
				$("#btnGoodsPreview").attr("disabled", false);
			}

			$(".clientAllow").attr("disabled", false);

			viewModel.set("saleStartYearDisabled", false);
			viewModel.set("saleStartHourDisabled", false);
			viewModel.set("saleStartMinuteDisabled", false);

			viewModel.set("saleEndYearDisabled", false);
			viewModel.set("saleEndHourDisabled", false);
			viewModel.set("saleEndMinuteDisabled", false);

			fnInitSaleStatusRadio();
		}
	}

	function fnGoodsApprDetail(ilGoodsApprId) {
		if (pageParam.apprKindType != undefined && pageParam.apprKindType == "APPR_KIND_TP.GOODS_CLIENT") {

			fnAjax({
				url     : "/admin/approval/goods/getGoodsClientDetail",
				params  : {"ilGoodsApprId":ilGoodsApprId},
				method : "POST",
				success : function( data ){
				    if((data.goodsName && viewModel.ilGoodsDetail.goodsName != data.goodsName) ||
				        (data.goodsDesc && viewModel.ilGoodsDetail.goodsDesc != data.goodsDesc) ||
                        (data.searchKywrd && viewModel.ilGoodsDetail.searchKywrd != data.searchKywrd)) $('#labelItemBaseInfo').addClass('approvalChanged');
					if (data.goodsName && viewModel.ilGoodsDetail.goodsName != data.goodsName) $('#labelItemGoodsName').addClass('approvalChanged');
					if (data.goodsName) viewModel.ilGoodsDetail.set("goodsName", data.goodsName);
					if (data.goodsDesc && viewModel.ilGoodsDetail.goodsDesc != data.goodsDesc) $('#labelItemGoodsDesc').addClass('approvalChanged');
					if (data.goodsDesc) viewModel.ilGoodsDetail.set("goodsDesc", data.goodsDesc);
					if (data.searchKywrd && viewModel.ilGoodsDetail.searchKywrd != data.searchKywrd) $('#labelItemSearchKeyword').addClass('approvalChanged');
					if (data.searchKywrd) viewModel.ilGoodsDetail.set("searchKeyword", data.searchKywrd);
					if (data.dispYn) viewModel.ilGoodsDetail.set("displayYn", data.dispYn);
					if (data.saleStartDt) viewModel.ilGoodsDetail.set("saleStartDate", data.saleStartDt);
					if (data.saleEndDt) viewModel.ilGoodsDetail.set("saleEndDate", data.saleEndDt);
					if (data.saleStatus) viewModel.ilGoodsDetail.set("saleStatus", data.saleStatus);

                    if(data.goodsMemo !== undefined){
                        if(viewModel.ilGoodsDetail.get("goodsMemo") != data.goodsMemo)$('#labelImteGoodsMemo').addClass('approvalChanged');     // 상품메모 변경여부
                        if(viewModel.ilGoodsDetail.get("goodsMemo") != data.goodsMemo)$('#labelItemEtcInfo').addClass('approvalChanged');       // 기타정보 변경여부
                        viewModel.ilGoodsDetail.set("goodsMemo", data.goodsMemo);
                    }

					viewModel.fnSaleDateReset();
				},
				isAction : "select"
			});
		}
	}

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	$scope.fnApprAdmin = function(){fnApprAdmin();};				//승인관리자 지증
	$scope.fnApprClear = function(){fnApprClear();};				//승인관리자 초기화
	$scope.fnApprCancel = function(){fnApprCancel();};				//요청철회
	$scope.fnGoodsRegistApprDetailPopup = function() {fnGoodsRegistApprDetailPopup();};	//승인내역 상세보기 팝업
	$scope.fnItemClientApprDetailPopup = function() {fnItemClientApprDetailPopup();};	//거래처 상품 수정 승인내역 상세보기 팝업
	$scope.fnGoodsClientApprDetailPopup = function() {fnGoodsClientApprDetailPopup();};	//거래처 상품 수정 승인내역 상세보기 팝업
	//------------------------------- Html 버튼 바인딩  End -------------------------------