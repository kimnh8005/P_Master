/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 추적
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.15		김승우          최초생성
 * **/
"use strict";

var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {

	// Initialize Page Call
	fnInitialize();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "deliverySearchPopup",
			callback : fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI(){
		fnTranslate();		// 다국어 변환
		fnSetView();		// 화면 셋팅
	}

	//--------------------------------- Button Start-------------------------------------------
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	// 화면 셋팅
	function fnSetView() {

		if (paramData.iframeYn == 'Y') {
			$('#deliverySearchIframe').show();
			$('#deliverySearchPopup').hide();

			paramData.httpRequestTp == 'GET' ? fnRenderGetIframe() : fnRenderPostIframe();

		} else if (paramData.iframeYn == 'N') {
			$('#deliverySearchIframe').hide();
			$('#deliverySearchPopup').show();
			fnRenderStatus();	// 배송 상태 렌더링
			fnRenderTimeLine();	// 타임라인 렌더링
		}
	}

    // 배송 상태 렌더링
    function fnRenderStatus() {
    	$('#logisticsNm').text(fnNvl(paramData.shippingCompNm));	// 택배사명
    	$('#trackingNo').text(fnNvl(paramData.trackingNo));			// 송장번호

    	let deliveryStatus = '';
    	let cartArr 	= ['운송장등록', '집화', '발송(구간/셔틀)', '집화처리', '간선하차', '간선상차', '집하', '집하처리', '구간발송', '셔틀발송', '셔틀도착', '구간도착'];
    	let deliveryArr = ['배달전', '배송출발', '배달출발'];
    	let completeArr = ['배달완료', '배송완료', '인수자등록'];

    	if (paramData.trackingArr.length < 1) {
    		deliveryStatus = 'home';
    	} else if (cartArr.indexOf(paramData.trackingArr[0].trackingStatusName) != -1) {
    		deliveryStatus = 'cart';
    	} else if (deliveryArr.indexOf(paramData.trackingArr[0].trackingStatusName) != -1) {
    		deliveryStatus = 'delivery';
    	} else if (completeArr.indexOf(paramData.trackingArr[0].trackingStatusName) != -1) {
    		deliveryStatus = 'complete';
    	}

    	let selectedEl = document.querySelector(".deliverySearch__status[data-delivery-status=" + deliveryStatus + "]");
    	selectedEl.classList.add("active");

    	while (selectedEl = selectedEl.nextElementSibling) {
    		selectedEl.classList.add("default");
    	}
    }

    // 타임라인 렌더링
    function fnRenderTimeLine() {
    	$(".deliverySearch__timeLineList").html('');
    	const $target = document.querySelector(".deliverySearch__timeLineList");

    	if (paramData.trackingArr.length > 0) {
    		const htmlString = paramData.trackingArr.map((d, index) => {
        		return `<li class="deliverySearch__timeLine ${index === 0 ? 'on' : '' }">
    	                	<span class="deliverySearch__timeLine__dot"></span>
    	                	<div class="deliverySearch__timeLine__dateCont">
    	                  		<p class="deliverySearch__timeLine__date">${d.scanDate.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3')}</p>
    	                  		<p class="deliverySearch__timeLine__time">${d.scanTime.replace(/(\d{2})(\d{2})(\d{2})/g, '$1:$2')}</p>
    	                	</div>
    	                	<div class="deliverySearch__timeLine__status">
    	                  		<span class="deliverySearch__timeLine__shippingStatus">${d.trackingStatusName}</span>
    	                  		<span class="deliverySearch__timeLine__location">${  fnIsEmpty(d.shopName) ? fnNvl(d.processingShopName) : fnNvl(d.shopName)}</span>
								${  !fnIsEmpty(d.processingEmployeeName) ? " / (배송담당: " + fnNvl(d.processingEmployeeName) +" "+ fnNvl(d.processingEmployeeTelephone) + ")"  : ""}    	                  		
    	                	</div>
    	              	</li>`;
        	}).join("");
        	$target.innerHTML = htmlString;
        	$('#noticeMessage').removeClass("show");
        	$('#deliveryList').addClass('show');
    	} else {
    		$('#noticeMessage').addClass("show");
    		$('#deliveryList').removeClass('show');
    	}
    }

    // Iframe Get 렌더링
    function fnRenderGetIframe() {
    	console.log(paramData.trackingUrl + paramData.trackingNo);
    	$('#deliverySearchIframe').prop('src', paramData.trackingUrl + paramData.trackingNo);
    }

    // Iframe Post 렌더링
    function fnRenderPostIframe() {
    	let popForm 	= document.createElement('form');
		popForm.name 	= 'popForm';
		popForm.method 	= 'POST';
		popForm.action 	= paramData.trackingUrl;
		popForm.target 	= 'deliverySearchIframe';

		let input = document.createElement('input');
		input.setAttribute("type"	, "hidden");
		input.setAttribute("name"	, paramData.invoiceParam);
		input.setAttribute("value"	, paramData.trackingNo);

		popForm.appendChild(input);
		document.body.appendChild(popForm);
		popForm.submit();
	}
    //-------------------------------  Common Function end ------------------------------------


    //------------------------------- Html 버튼 바인딩  Start --------------------------------------
    //------------------------------- Html 버튼 바인딩  End ----------------------------------------
}); // document ready - END
