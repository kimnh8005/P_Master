/**-----------------------------------------------------------------------------
 * description 		 : 클레임관리 공통 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		천혜현   최초생성
 * @
 * **/

var searchCtgryCommonUtil = {

	LClaimCtgryDropDownList: function(){
		return fnKendoDropDownList({
 			id         : 'lclaimCtgryId',
 			tagId	   : 'lclaimCtgryId',
 			url        : "/admin/policy/claim/searchPsClaimBosList",
 			params 	   : {"categoryCode" : "10"},
 			blank      : '클레임 사유(대)',
 			valueField : "psClaimCtgryId",
 			textField  : "claimName"
 				, async       : false
				, isDupUrl    : 'Y'
 		});
    },

    MClaimCtgryDropDownList: function(id) {
		return fnKendoDropDownList({
			id         : 'mclaimCtgryId',
			tagId      : 'mclaimCtgryId',
			url        : "/admin/policy/claim/searchPsClaimBosList",
			params	   : {"categoryCode" : "20"},
			blank      : '클레임 사유(중)',
			valueField : "psClaimCtgryId",
			textField  : "claimName",
			cscdId 	   : "lclaimCtgryId",
			cscdField  : "searchLClaimCtgryId"
				, async       : false
				, isDupUrl    : 'Y'
		});
    },

    SClaimCtgryDropDownList: function(id){
		var opt = {
			id         : 'sclaimCtgryId',
			tagId      : 'sclaimCtgryId',
			url        : "/admin/policy/claim/searchPsClaimBosList",
			params	   : {"categoryCode" : "30"},
			blank      : '귀책처',
			valueField : "psClaimCtgryId",
			textField  : "claimName",
			cscdId 	   : "mclaimCtgryId",
			cscdField  : "searchMClaimCtgryId",
			async       : false,
			isDupUrl    : 'Y',
			dataBound : function(e) {
			    const _ds = this.dataSource;
			    const _data = _ds.data();

			    if (_data != undefined && _data != null && _data.length > 0) {
			        sClaimCtgryDropDownListData = _data.toJSON().slice();
			    }

			}
		};
    	var kendoDropDown = fnKendoDropDownList(opt);
		var originalParameterMap = kendoDropDown.dataSource.transport.parameterMap;

		kendoDropDown.dataSource.transport.parameterMap = function(data, type) {
			return {
				...originalParameterMap(data, type),
				"searchLClaimCtgryId": $("#lclaimCtgryId").val()
			};
		}

		return kendoDropDown;
    },

    //ID에 따른 dropdownlist
    LClaimCtgryDropDownListById: function(i){
    	return fnKendoDropDownList({
    		id         : 'lclaimCtgryId_'+i,
    		tagId	   : 'lclaimCtgryId',
    		url        : "/admin/policy/claim/searchPsClaimBosList",
    		params 	   : {"categoryCode" : "10"},
    		blank      : '클레임 사유(대)',
    		valueField : "psClaimCtgryId",
    		textField  : "claimName"
    			, async       : false
    			, isDupUrl    : 'Y'
    	});
    },

    MClaimCtgryDropDownListById: function(i) {
		var orderGoodList = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		var urSupplierId	= orderGoodList[i].urSupplierId;
		var odOrderId		= orderGoodList[i].odOrderId;
		var odOrderDetlId	= orderGoodList[i].odOrderDetlId;

		//console.log("MClaimCtgryDropDownListById : ", i)
    	return fnKendoDropDownList({
    		id         : 'mclaimCtgryId_'+i,
    		tagId      : 'mclaimCtgryId',
    		url        : "/admin/policy/claim/searchPsClaimBosList",
    		params	   : {"categoryCode" : "20"},
    		blank      : '클레임 사유(중)',
    		valueField : "psClaimCtgryId",
    		textField  : "claimName",
    		cscdId 	   : "lclaimCtgryId_"+i,
    		cscdField  : "searchLClaimCtgryId"
    			, async       : false
    			, isDupUrl    : 'Y'
    	});
    },

    SClaimCtgryDropDownListById: function(i){
		var orderGoodList 		= $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		var urSupplierId		= stringUtil.getInt(orderGoodList[i].urSupplierId, 0);
		var psClaimBosId		= stringUtil.getString(orderGoodList[i].psClaimBosId, "");
		var odOrderDetlId		= stringUtil.getInt(orderGoodList[i].odOrderDetlId, 0);
		var odClaimId			= stringUtil.getInt(orderGoodList[i].odClaimId, 0);

		var params = {
			"categoryCode": "30",
			"urSupplierId": urSupplierId,
			"psClaimBosId": psClaimBosId,
			"odOrderDetlId": odOrderDetlId,
			"odClaimId": odClaimId
		}

    	var opt = {
    			id         : 'sclaimCtgryId_'+i,
    			tagId      : 'sclaimCtgryId',
    			url        : "/admin/policy/claim/searchPsClaimBosList",
				//params	   : {"categoryCode" : "30", "urSupplierId" : urSupplierId, "odOrderId" : odOrderId, "odOrderDetlId" : odOrderDetlId},
				params	   : params,
    			blank      : '귀책처',
    			valueField : "psClaimCtgryId",
    			textField  : "claimName",
    			cscdId 	   : "mclaimCtgryId_"+i,
    			cscdField  : "searchMClaimCtgryId",
    			async       : false,
    			isDupUrl    : 'Y',
    			dataBound : function(e) {
    				const _ds = this.dataSource;
    				const _data = _ds.data();

    				if (_data != undefined && _data != null && _data.length > 0) {
    					sClaimCtgryDropDownListData[i] = _data.toJSON().slice();
    				}

    			}
    	};

    	var kendoDropDown = fnKendoDropDownList(opt);
    	var originalParameterMap = kendoDropDown.dataSource.transport.parameterMap;

    	kendoDropDown.dataSource.transport.parameterMap = function(data, type) {
    		return {
    			...originalParameterMap(data, type),
    			"searchLClaimCtgryId": $("#lclaimCtgryId_"+i).val()
    		};
    	}

    	return kendoDropDown;
    }
}