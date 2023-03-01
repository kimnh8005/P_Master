'use strict';
var pageParam = parent.POP_PARAM["parameter"];

var urStoreId = pageParam['urStoreId'];

$(document).ready(function() {
    // Initialize Page Call
    fnInitialize();

    // Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {
            flag : false
        });

        fnPageInfo({
            PG_ID : 'itemStoreSchedule',
            callback : fnUI
        });
    };

    //전체화면 구성
    function fnUI() {
    	fnDefaultValue(); // 조회
    };

    function fnDefaultValue() {
    	fnAjax({
			url     : '/admin/ur/store/getStoreDetail',
			params  : {urStoreId : pageParam.urStoreId },
			success :
				function( data ){

					var storeDeliveryAreaGroup = [];
					var storeDeliveryAreaList = [];
					var urDeliveryAreaId = "";

					for (var i = 0; i < data.storeDeliveryArea.length ; i++) {

						if(i!=0 && urDeliveryAreaId != data.storeDeliveryArea[i].urDeliveryAreaId) {
							storeDeliveryAreaGroup.push({deliveryAreaNm : storeDeliveryAreaList[0].deliveryAreaNm, storeDeliveryAreaList : storeDeliveryAreaList});
							storeDeliveryAreaList = [];
						}

						storeDeliveryAreaList.push(data.storeDeliveryArea[i]);

						urDeliveryAreaId = data.storeDeliveryArea[i].urDeliveryAreaId;
					}
					storeDeliveryAreaGroup.push({deliveryAreaNm : storeDeliveryAreaList[0].deliveryAreaNm, storeDeliveryAreaList : storeDeliveryAreaList});

					kendo.bind($("#itemStoreScheduleContainer"), {storeDeliveryAreaGroup: storeDeliveryAreaGroup});
				},
			isAction : 'get'
		});
    };

    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END
