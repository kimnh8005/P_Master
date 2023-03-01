/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 쿠폰 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.12		이원호          최초생성
 *
**/
"use strict";

var paramData = parent.POP_PARAM['parameter'];

// ----------------------------------------------------------------------------
// 쿠폰 선택 정보
// ----------------------------------------------------------------------------
var gGoodsCouponMaps = new Map();
var gCartCoupon;
var gShippingCouponMaps = new Map();

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "orderCouponPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		initInputFormMain();

		fnSelectCartCouponPage();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnConfirm").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	//수정
	function initInputFormMain(){
		//상품 쿠폰
		fnSelectCartCouponPage();
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ---------------------------------
	// 배송지목록 > 선택버튼
    function fnSelectCartCouponPage(){
        fnAjax({
	        url     : '/admin/order/create/getCouponPageInfo',
	        params  : { urUserId : paramData.urUserId,
                        spCartIdListData : paramData.spCartIdListData,
                        arrivalScheduledListData : paramData.arrivalScheduledListData},
	        success :
	            function( data ){
	        		//상품 쿠폰
	        		if(data.goodsList == undefined || data.goodsList == 0 ) {
	        		    $('#goodsCouponDiv').hide();
	        		}else{
	        		    fnSetGoodsCouponList(data.goodsList);
	        		}

                    //장바구니 쿠폰
                    if(data.cartCouponList == undefined || data.cartCouponList == 0 ) {
                        $('#cartCouponDiv').hide();
                    }else{
                        fnSetCartCoupon(data.cartCouponList);
                    }

                    //배송비 쿠폰
                    if(data.shippingCoupon == undefined || data.shippingCoupon == 0 ) {
                        $('#shippingCouponDiv').hide();
                    }else{
                        fnSetShippingCouponList(data.shippingCoupon);
                    }
		        }
	    });


    }

    function fnSetGoodsCouponList(data){
        const $target = $("#goodsCouponListArea .goodsCoupon__list");
        $target.empty();

        let tpl = $('#goodsCouponItem').html();
        let tplObj = null;

        // 배송지 목록 데이터
        let rows = data;

        for(let item of rows){
            tplObj = $(tpl);
            tplObj.find(".goodsCoupon__goodsName").html(item.goodsName).attr("id", "goodsCoupon__goodsName__"+item.ilGoodsId);
            tplObj.find(".fb__custom__select").attr("id", item.ilGoodsId);   // 상품 쿠폰 유형
            tplObj.find(".goodsCoupon__goodsDiscountHidden").attr("id", "goodsCoupon__goodsDiscountHidden__"+item.ilGoodsId);

            $target.append(tplObj);

            for(let val of item.couponList){
                val.spCartId = item.spCartId;
            }

            fnKendoDropDownList({
              id : item.ilGoodsId,
              data : item.couponList,
              valueField : "pmCouponIssueId",
              textField : "displayCouponName",
              blank : "선택해주세요."
            }).bind('change', function(e){
                //select 때문에 -1처리
                var index = this.select() - 1;
                var selectDiscountPrice = 0;
                var param;
                if(index != -1) {
                    param = this.dataSource.data()[index];
                    selectDiscountPrice = Number(param.discountPrice);

                    // 상품쿠폰 중복 예외처리
                    for(let key of gGoodsCouponMaps.keys()){
                        var value = gGoodsCouponMaps.get(key);
                        if(value == undefined) continue;
                        if(param.pmCouponIssueId == value.pmCouponIssueId){
                            gGoodsCouponMaps.set(key, undefined);
                            $('#'+key).data('kendoDropDownList').value("");
                            break;
                        }
                    }
                }

                // 상품 할인 금액 계산
                var elementId = e.sender.element[0].id;
                var discountPrice = Number($("#goodsCouponDiscount").text());
                var asisDiscount = Number($("#goodsCoupon__goodsDiscountHidden__" + elementId).text());
                if(asisDiscount != null && asisDiscount > 0){
                    discountPrice = discountPrice - asisDiscount;
                }
                discountPrice += selectDiscountPrice;
                $("#goodsCouponDiscount").text(discountPrice);
                $("#goodsCoupon__goodsDiscountHidden__" + elementId).text(selectDiscountPrice);

                // 선택된 쿠폰정보 저장
                gGoodsCouponMaps.set(elementId, param);

                // 장바구니 쿠폰 조회
                fnSelectCartCoupon();
            });
        }
    }

    // 장바구니 쿠폰 조회
    function fnSelectCartCoupon(){
        var useGoodsCouponListData = new Array();
        for (let value of gGoodsCouponMaps.values()) {
            if(value == undefined) continue;
            useGoodsCouponListData.push({'spCartId' : value.spCartId, 'pmCouponIssueId' : value.pmCouponIssueId});
        }

        fnAjax({
            url     : '/admin/order/create/getCartCouponList',
            params  : {urUserId : paramData.urUserId,
                        spCartIdListData : paramData.spCartIdListData,
                        arrivalScheduledListData : paramData.arrivalScheduledListData,
                        useGoodsCouponListData : JSON.stringify(useGoodsCouponListData)
                        },
            success :
                function( data ){
                    fnSetCartCoupon(data);
                }
        });
    }

    // 장바구니 쿠폰 설정
    function fnSetCartCoupon(data){
        $("#cartCouponDiscount").text(0);
        gCartCoupon = undefined;

        const $target = $("#cartCouponListArea .cartCoupon__list");
        $target.empty();

        let tplObj = $('#cartCouponItem').html();
        $target.append(tplObj);

        fnKendoDropDownList({
          id : "cartCouponType",
          data : data,
          valueField : "pmCouponIssueId",
          textField : "displayCouponName",
          blank : "선택해주세요."
        }).bind("change", function(e){
            //select 때문에 -1처리
            var index = this.select() - 1;
            var selectDiscountPrice = 0;
            var discountPrice = Number($("#cartCouponDiscount").text());
            var param;
            if(index != -1) {
                param = this.dataSource.data()[index];
                gCartCoupon = param;
                selectDiscountPrice = Number(param.discountPrice)
                discountPrice = selectDiscountPrice;
            }else{
                discountPrice = 0;
                gCartCoupon = undefined;
            }
            $("#cartCouponDiscount").text(discountPrice);
        });
    }

    //배송비 쿠폰
    function fnSetShippingCouponList(data){
        const $target = $("#shippingCouponListArea .shippingCoupon__list");
        $target.empty();

        let tpl = $('#shippingCouponItem').html();
        let tplObj = null;

        // 배송지 목록 데이터
        let rows = data;

        for(let item of rows){
            tplObj = $(tpl);
            tplObj.find(".shippingCoupon__goodsName").html(item.compositionGoodsName).attr("id", "shippingCoupon__goodsName__"+item.shippingIndex);
            tplObj.find(".fb__custom__select").attr("id", item.shippingIndex);   // 상품 쿠폰 유형
            tplObj.find(".shippingCoupon__discountHidden").attr("id", "shippingCoupon__discountHidden__"+item.shippingIndex);

            $target.append(tplObj);

            fnKendoDropDownList({
              id : item.shippingIndex,
              data : item.couponList,
              valueField : "pmCouponIssueId",
              textField : "displayCouponName",
              blank : "선택해주세요."
            }).bind('change', function(e){
                //select 때문에 -1처리
                var index = this.select() - 1;
                var selectDiscountPrice = 0;
                var param;
                if(index != -1) {
                    param = this.dataSource.data()[index];
                    selectDiscountPrice = Number(param.discountPrice);

                    // 배송비쿠폰 중복 예외처리
                    for(let key of gShippingCouponMaps.keys()){
                        var value = gShippingCouponMaps.get(key);
                        if(value == undefined) continue;
                        if(param.pmCouponIssueId == value.pmCouponIssueId){
                            gShippingCouponMaps.set(key, undefined);
                            $('#'+key).data('kendoDropDownList').value("");
                            break;
                        }
                    }
                }

                // 배송비 할인 금액
                var elementId = e.sender.element[0].id;
                var discountPrice = Number($("#shippingCouponDiscount").text());
                var asisDiscount = Number($("#shippingCoupon__discountHidden__" + elementId).text());
                if(asisDiscount != null && asisDiscount > 0){
                    discountPrice = discountPrice - asisDiscount;
                }
                discountPrice += selectDiscountPrice;
                $("#shippingCouponDiscount").text(discountPrice);
                $("#shippingCoupon__discountHidden__" + elementId).text(selectDiscountPrice);

                // 배송비 쿠폰정보 저장
                gShippingCouponMaps.set(elementId, param);
            });
        }
    }

    // 적용 버튼
    function fnConfirm(){
        var goodsCoupon = Array.from(gGoodsCouponMaps, ([name, value]) => ({ 'ilGoodsId' : name, 'coupon' : value }))
                .filter(it => it.coupon != undefined);
        var shippingCoupon = Array.from(gShippingCouponMaps, ([name, value]) => ({ 'shippingIndex' : name, 'coupon' : value }))
                .filter(it => it.coupon != undefined);
        let data = {
            goodsCoupon : goodsCoupon,
            cartCoupon : gCartCoupon,
            shippingCoupon : shippingCoupon,
            goodsCouponDiscount : $("#goodsCouponDiscount").text(),
            cartCouponDiscount : $("#cartCouponDiscount").text(),
            shippingCouponDiscount : $("#shippingCouponDiscount").text()
        };
        parent.POP_PARAM = data;

        fnClose();
    }

    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnConfirm = function() { fnConfirm();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
