/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * @
 * **/

var calPovGridColUtil = {
	corporationName: function(row){
        return { field : "corporationName", title : "법인", width: "150px", attributes : {style : "text-align:center;"}, footerTemplate: "합계", footerAttributes: {style : "text-align: center;"}}
    },
    tempMeCost: function(row){
        return { field : "tempMeCost", title : "MO", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    tempOvCost: function(row){
        return { field : "tempOvCost", title : "O.V", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    tempVdcCost: function(row){
        return { field : "tempVdcCost", title : "V.DC", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    tempMogeCost: function(row){
        return { field : "tempMogeCost", title : "MOGE", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    finalMeCost: function(row){
        return { field : "finalMeCost", title : "MO", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    finalOvCost: function(row){
        return { field : "finalOvCost", title : "O.V", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    finalVdcCost: function(row){
        return { field : "finalVdcCost", title : "V.DC", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    finalMogeCost: function(row){
        return { field : "finalMogeCost", title : "MOGE", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    diffMeCost: function(row){
        return { field : "diffMeCost", title : "MO", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    diffOvCost: function(row){
        return { field : "diffOvCost", title : "O.V", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    diffVdcCost: function(row){
        return { field : "diffVdcCost", title : "V.DC", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
    diffMogeCost: function(row){
        return { field : "diffMogeCost", title : "MOGE", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}", aggregates: ["sum"], footerTemplate: "#=kendo.format('{0:n0}',sum)#", footerAttributes: {style : "text-align: right;"}}
    },
}

var calPovGridUtil = {
    /** 통합몰 매출 대사 내역 */
    calPovList: function(){
        return [
        	calPovGridColUtil.corporationName(this),
        	{
        		title: "가마감",
        		columns: [
        			calPovGridColUtil.tempMeCost(this),
        			calPovGridColUtil.tempOvCost(this),
        			calPovGridColUtil.tempVdcCost(this),
                    calPovGridColUtil.tempMogeCost(this)
        		]
        	},
        	{
        		title: "마감",
        		columns: [
        			calPovGridColUtil.finalMeCost(this),
        			calPovGridColUtil.finalOvCost(this),
        			calPovGridColUtil.finalVdcCost(this),
                    calPovGridColUtil.finalMogeCost(this)
        		]
        	},
        	{
        		title: "확정차액(가마감-마감)",
        		columns: [
        			calPovGridColUtil.diffMeCost(this),
        			calPovGridColUtil.diffOvCost(this),
        			calPovGridColUtil.diffVdcCost(this),
                    calPovGridColUtil.diffMogeCost(this)
        		]
        	}
        ]
    },

}