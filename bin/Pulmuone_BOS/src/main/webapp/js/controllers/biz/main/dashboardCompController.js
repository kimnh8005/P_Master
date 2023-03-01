/**-----------------------------------------------------------------------------
 * description 		 : 통합 대시보드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.03.31		choo   최초생성
 * @
 * **/
'use strict';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });
		$('#navigation').find('.active').removeClass('active');
		$('article#container').addClass('no-lnb');
		$('#ng-view').css({"display":""});

		fnPageInfo({
			PG_ID  : 'dashboardComp',
			callback : fnUI
		});

	}

	function fnUI(){
		fnInitOptionBox();
		fnCsBbsSearch();
		fnInitCsDashboardData();
		fnTranslate();	// 다국어 변환--------------------------------------------
		/*
		if(getCookiePop("DrDelayComp")!="Y"){
			fnAjax({
				url     : '/comn/cs/dashboardTot/getDrDelayCount',
				success :
					function( data ){
						var cnt = data.rows.DR_DI_DAY;
						var kgcShopYn = data.rows.KGC_SHOP_YN;
						if(cnt >0){
							fnKendoMessage({message : fnGetLangData({key : "",nullMsg : "배송준비중 상태로 5일 이상 경과된 주문이 " + cnt +"건 있습니다.<br>확인 버튼을 누르시어 해당 주문을 확인 바랍니다."})
								,ok : function(){
									setCookiePop("DrDelayComp","Y", 1);
									//가맹점/직영점이면
									if(kgcShopYn == "Y"){
										location.href="#/orderUnityTp1?DrDelay=Y";
									}else{
										location.href="#/orderShppgReady?DrDelay=Y";
									}
								}
							});
						}
					},
				isAction : 'select'
			});
		}
		*/

		fnAjax({
			url     : '/comn/cs/dashboardTot/getDrDelayCount',
			success :
				function( data ){
					var drCnt = data.rows.DR_DAY;
					var diCnt = data.rows.DI_DAY;
					var resultMsg = "";
					if(drCnt > 0 && diCnt == 0){
						resultMsg = "배송준비 지연 건수 : "+ drCnt +"건";
					}else if(drCnt == 0 && diCnt > 0){
						resultMsg = "배송중 지연 건수 : "+ diCnt +"건";
					}else if(drCnt >0 && diCnt > 0){
						resultMsg = "배송준비 지연 건수 : "+ drCnt +"건  <br> 배송중 지연 건수 : "+ diCnt +"건";
					}
					if((drCnt >0 || diCnt >0) && resultMsg !=''){
						fnKendoMessage({message : fnGetLangData({key : "",nullMsg : resultMsg})
						});
					}
				},
			isAction : 'select'
		});

		fnAjax({
			url     : '/comn/cs/dashboardTot/getDrDelayCount',
			success :
				function( data ){
					var drCnt = data.rows.DR_DAY;
					var diCnt = data.rows.DI_DAY;
					var resultMsg = "";
					if(drCnt > 0 && diCnt == 0){
						resultMsg = "배송준비 지연 건수 : "+ drCnt +"건";
					}else if(drCnt == 0 && diCnt > 0){
						resultMsg = "배송중 지연 건수 : "+ diCnt +"건";
					}else if(drCnt >0 && diCnt > 0){
						resultMsg = "배송준비 지연 건수 : "+ drCnt +"건  <br> 배송중 지연 건수 : "+ diCnt +"건";
					}
					if((drCnt >0 || diCnt >0) && resultMsg !=''){
						fnKendoMessage({message : fnGetLangData({key : "",nullMsg : resultMsg})
						});
					}
				},
			isAction : 'select'
		});

		//---------팝업 데이타-------------
		fnAjax({
			url : '/comn/dp/popup/getDpPopup',
			success :
			function(data){
				var result = data.rows;
				if(result.length > 0){
					for(var i = 0; i < result.length; i++){
						if(getCookiePop("Today_"+result[i].DP_POPUP_ID)!="Y" && result[i].AGREE_STATE != "D"){
							var addPopup = "";

							addPopup += "<div class='intro-popup' id='dpPopup_"+ result[i].DP_POPUP_ID +"'>";
							addPopup += "<div class='popup-cont'>"+fnTagConvert (result[i].POPUP_DESC_HTML)+"</div>";
							addPopup += "<span class='popup-bt'>";

							if(result[i].POPUP_TODAY_YN == 'Y'){
								addPopup += "<span>";
								addPopup += "<input type='checkbox' class='custom-checkbox' id='popchk_"+ result[i].DP_POPUP_ID +"' checked='checked'>";
								addPopup += "<label for='popchk_"+result[i].DP_POPUP_ID +"'>오늘은 그만 보기</label>";
								addPopup += "</span>";
							}

							if(result[i].AGREE_STATE == 'Y'){
								addPopup += "<button id='fngree_"+result[i].DP_POPUP_ID+"' class='set-btn-type2' msg-key='6696'>동의</button>";
							}

							addPopup += "</span></div>";

							$('#kendoPopupArea').append(addPopup);

							$('#dpPopup_'+ result[i].DP_POPUP_ID).kendoWindow({
								visible: true,
								modal: true,
								width: 400,
								height: 400,
								title : result[i].POPUP_NAME,
								close: function(e) {
									if($(this.wrapper).find(".custom-checkbox").prop("checked")){
										var id = $(this.wrapper).find(".custom-checkbox").attr("id").replace("popchk_", "");
										setCookiePop('Today_'+id,'Y', 1);
									}
								}
							});

							var kendoWindow =$('#dpPopup_'+ result[i].DP_POPUP_ID).data('kendoWindow');
							kendoWindow.center();

							$(".intro-popup .popup-bt button").on("click",function(){
								var DP_POPUP_ID = $(this).attr("id").replace(/fngree_/, '');
								fnAgree(DP_POPUP_ID);
							});
						}
					}
				}
			},
			isAction : 'select'

		});
		//---------팝업 데이타-------------
	}

	function fnInitOptionBox(){	//---------------Initialize Option Box------------------------------------------------
		//var tabToActivate = $("#tabCon1");
		//$("#tabstrip").kendoTabStrip().data("kendoTabStrip").activateTab(tabToActivate);
	}

	//쿠키 생성
	function setCookiePop(name, value, expiredays) {
		var today = new Date();
		today.setDate(today.getDate() + expiredays);
		document.cookie = name + '=' + escape(value) + '; path=/; expires=' + today.toDateString() + ';'
	}

	//쿠키 가져옴
	function getCookiePop(name){
		var cName = name + "=";
		var x = 0;

		while(x <= document.cookie.length)
		{
			var y = (x+cName.length);

			if(document.cookie.substring(x, y) == cName)
			{
				var endOfCookie = document.cookie.indexOf(";", y);

				if(endOfCookie == -1){
					endOfCookie = document.cookie.length;
				}

				return unescape(document.cookie.substring(y, endOfCookie));
			}
			x = document.cookie.indexOf(" ", x ) + 1;

			if (x == 0)
			break;
		}
		return "";
	}
	function fnAgree(DP_POPUP_ID){
		fnAjax({
			url     : '/comn/dp/popup/addDpPopupAgree',
			params  : {"DP_POPUP_ID" : DP_POPUP_ID},
			success :
				function( data ){
					$('#dpPopup_'+ DP_POPUP_ID).data('kendoWindow').close();
				},
			isAction : 'batch'
		});
	}

	function fnInitCsDashboardData(){

		// 문의/답변관리(30일간)
		fnAjax({
			url     : '/comn/cs/dashboardComp/getCsDashboardCompQnaMngt',
			params  : {},
			success :
				function( data ){
					for(var i=0; i<data.rows.length; i++){
						$("#qnaPercent"+i).html(data.rows[i].PERCENT.toFixed(1)+"%");
						$("#qnaNoCnt"+i).html(data.rows[i].NO_CNT.toFixed(1)+"건");
					}
				},
			isAction : 'select'
		});

		// 금일 주요현황
		fnAjax({
			url     : '/comn/cs/dashboardComp/getTodayDashboardComp',
			params  : {},
			success :
				function( data ){
					$('#NEW_ORDER').text(data.rows.NEW_ORDER + "명");
					$('#TOT_AMT').text(fnNumberWithCommas(data.rows.TOT_AMT)+ "원");
					$('#CANCLE_AMT').text(fnNumberWithCommas(data.rows.CANCLE_AMT)+ "원");
					$('#CALCULATE').text(fnNumberWithCommas(data.rows.CALCULATE)+ "원");
					$('#REMITTANCE').text(fnNumberWithCommas(data.rows.REMITTANCE)+ "원");
				},
			isAction : 'select'
		});

		// 상품현황
		fnAjax({
			url : '/comn/cs/dashboardComp/getCsDashboardCompItemMngt',
			params  : {},
			success :
				function(data){
					$('#saleItem').text(data.rows.saleItem + "건");
					$('#approvalItem').text(data.rows.approvalItem + "건");
					$('#stockItem').text(data.rows.stockItem + "건");
				},
			isAction : 'select'
		});

		// 주문현황
		fnAjax({
			url : '/comn/cs/dashboardComp/getCsDashboardCompOrderMngt',
			params  : {},
			success :
				function(data){
					$('#orderReception').text(data.rows.ORDER_RECEPTION + "건");
					$('#deliveryPrepareing').text(data.rows.DELIVERY_PREPAREING + "건");
					$('#shipping').text(data.rows.SHIPPING + "건");
					$('#deliveryCompleted').text(data.rows.DELIVERY_COMPLETED + "건");
					$('#cancelRequest').text(data.rows.CANCEL_REQUEST + "건");
				},
			isAction : 'select'
		});

		// 처리지연 현황
		fnAjax({
			url : '/comn/cs/dashboardComp/getCsDashboardCompDelayMngt',
			params  : {},
			success :
				function(data){
					$('#delayOrder').text(data.rows.IR_IC_DAY + "건");
					$('#delayDelivery').text(data.rows.DR_DI_DAY + "건");
					$('#delayCancellation').text(data.rows.CR_CC_DAY + "건");
					$('#delayExchange').text(data.rows.ERR_ERA_DAY + "건");
					$('#delayReturn').text(data.rows.ERA_ERC_DAY + "건");
				},
			isAction : 'select'
		});

		// 취소/반품/교환 현황
		fnAjax({
			url : '/comn/cs/dashboardComp/getCsDashboardCompCancelReturnsMngt',
			params  : {},
			success :
				function(data){
					// 취소요청 : CA, 반품요청 : RA, 환불요청 : FA
					for(var i=0; i<data.rows.data.length; i++){
						$("#cancelReturnsType_"+data.rows.data[i].TYPE).text(data.rows.data[i].CNT + "건");
					}
				},
			isAction : 'select'
		});
	}

	// 공지사항
	function fnCsBbsSearch(){
		var data = {};
		fnAjax({
			url     : '/comn/cs/dashboardComp/getCsDashboardCompBbsMngt',
			params  : data,
			success :
				function( data ){
					$("#bbsTable0 > tbody").html("");
					for(var i=0; i<data.rows.length; i++){
						var row = "<tr>";
							row += "<td class='title'><a id='bbsDetail"+i+"' class='bbsDetail' style='cursor:pointer'>"+ data.rows[i].TITLE +"</a></td>";
							row += "<td class='date'>"+ data.rows[i].CREATED +"</td>";
							row += "</tr>";
						$("#bbsTable0 > tbody").append(row);
						row = "";

						// 입점사
						$('.bbsDetail').on('click', function(e){
							e.preventDefault();
							var index =  this.id.replace("bbsDetail",'');
							var id = data.rows[index].ID;
							fnBbsDetail(id);
						});
					}
				},
			isAction : 'select'
		});
	}
	function fnMore(){
		var option = new Object();
		option.url = "#/compBbs003";
		option.menuId = 185;
		option.data = { S_COMP_BBS_ID:"1"};
		$scope.$emit('goPage', option);
	}

	function fnBbsDetail(id){
		var option = new Object();
		//console.log(id);
		option.url = "#/compBbs001";
		option.menuId = 200;
		option.data = { ID :id };

		$scope.goPage(option);
	}

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common More*/
	$scope.fnMore = function( ){	fnMore();};
	/** Common BbsDetail*/
	$scope.fnBbsDetail = function(id){  fnBbsDetail(id);};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
