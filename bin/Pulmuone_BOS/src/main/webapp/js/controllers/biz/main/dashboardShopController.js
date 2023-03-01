/**-----------------------------------------------------------------------------
 * description 		 : 매장 대시보드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2018.05.25		choo   최초생성
 * @
 * **/

'use strict';

$(document).ready(function() {
	fnInitialize();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });
		$('#navigation').find('.active').removeClass('active');
		$('article#container').addClass('no-lnb');
		$('#ng-view').css({"display":""});

		fnPageInfo({
			PG_ID  : 'dashboardShop',
			callback : fnUI
		});

	}

	function fnUI(){
		fnInitOptionBox();
		fnTranslate();
		fnGetDatas();

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

	function fnInitOptionBox(){
	}

	//쿠키 생성
	function setCookiePop(name, value, expiredays) {
		var today = new Date();
		today.setDate(today.getDate() + expiredays);
		document.cookie = name + '=' + escape(value) + '; path=/; expires=' + today.toDateString() + ';';
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

	function fnGetDatas(){

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'STATUS' : 'IC'},//주문접수
			success :
				function( data ){
					var d = data.rows.data;
					$('#pickOdCountIC').html(d.TOT_OD_PICK_CNT);
					$('#shopOdCountIC').html(d.TOT_OD_SHOP_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});
		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'STATUS' : 'DR'},//배송준비
			success :
				function( data ){
					var d = data.rows.data;
					$('#pickOdCountDR').html(d.TOT_OD_PICK_CNT);
					$('#shopOdCountDircDR').html(d.TOT_OD_SHOP_DIRC_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'OD_TARGET' : 'SELF' , 'STATUS' : 'DR'}, //자체배송 배송준비중
			success :
				function( data ){
					var d = data.rows.data;

					if( data.OD_STORE_SERVICE_YN == 'Y' ){
						$('#shopOdCountSelfDR_area').show();
					}
					$('#shopOdCountSelfDR').html(d.TOT_OD_SHOP_REDY_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'OD_TARGET' : 'SELF' , 'STATUS' : 'DI'}, //자체배송 배송중
			success :
				function( data ){
					var d = data.rows.data;

					if( data.OD_STORE_SERVICE_YN == 'Y' ){
						$('#shopOdCountSelfDI_area').show();
					}
					$('#shopOdCountSelfDI').html(d.TOT_OD_SHOP_REDY_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'STATUS' : 'DC'},//배송완료
			success :
				function( data ){
					var d = data.rows.data;
					$('#pickOdCountDC').html(d.TOT_OD_PICK_CNT);
					$('#shopOdCountDircDC').html(d.TOT_OD_SHOP_DIRC_CNT);

				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'OD_TARGET' : 'SELF' , 'STATUS' : 'DC'}, //자체배송 배송완료
			success :
				function( data ){
					var d = data.rows.data;

					if( data.OD_STORE_SERVICE_YN == 'Y' ){
						$('#shopOdCountSelfDC_area').show();
					}
					$('#shopOdCountSelfDC').html(d.TOT_OD_SHOP_REDY_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'OD_TARGET' : 'SELF' , 'STATUS' : 'BF'}, //구매확정
			success :
				function( data ){
					var d = data.rows.data;

					if( data.OD_STORE_SERVICE_YN == 'Y' ){
						$('#shopOdCountSelfBF_area').show();
					}
					$('#shopOdCountSelfBF').html(d.TOT_OD_SHOP_REDY_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdExpiration',
			success :
				function( data ){
					var d = data.rows.data;
					$('#pickOdCountExpire').html(d.TOT_OD_PICK_EXPIRED_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});


		fnAjax({
			url     : '/comn/dashboard/od/getOdCount',
			params  : {'STATUS' : 'CC'},//픽업취소
			success :
				function( data ){
					var d = data.rows.data;
					$('#pickOdCountCC').html(d.TOT_OD_PICK_CNT);

				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

		fnAjax({
			url     : '/comn/dashboard/od/getOdPickDelay',
			params  : {'CSTMZ_SHIPPING_DIV' : 'M'},//직접배송 배송지연
			success :
				function( data ){
					var d = data.rows.data;
					$('#shopOdCountDircDelay').html(d.OD_DELAY_CNT);
				},
			isAction : 'select',
			isDupUrl : 'Y'
		});

	}


	function fnOdGoUrl( cstmzShippingDiv, odStatus, odTarget ){

		var option = new Object();

		if( cstmzShippingDiv == 'P' ){
			if( odStatus == 'IC' ){
				option.url    = '#/storePickupTp1';
				option.data   = { 'STATUS' : odStatus};
			}else if( odStatus == 'DR' ){
				option.url    = '#/storePickupTp1';
				option.data   = { 'STATUS' : odStatus};
			}
		}else if( cstmzShippingDiv == 'M' ){
			if( odTarget == 'self' ){
				option.url    = '#/orderStoreTp4';
				option.data   = { 'STATUS' : 'TRCKNO_N'};
			}else{
				if( odStatus == 'IC' ){
					option.url    = '#/orderPayCmpltTp1';
				}else if( odStatus == 'DR' ){
					option.url    = '#/orderStoreTp1';
					option.data   = { 'STATUS' : odStatus};
				}
			}

		}

		$scope.$emit('goPage', option);

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
	$scope.fnOdGoUrl = function( cstmzShippingDiv, odStatus, odTarget ){ fnOdGoUrl( cstmzShippingDiv, odStatus, odTarget ); };

	/** Common More*/
	$scope.fnMore = function( ){	fnMore();};
	/** Common BbsDetail*/
	$scope.fnBbsDetail = function(id){  fnBbsDetail(id);};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
