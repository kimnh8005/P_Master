/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 공통 검색
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * **/
var calPovSearchUtil = {
	getCalculateMonth: function(){
        // 정산 시작 년도
        calPovSearchUtil.getDropDownLocalData("findYear", '', calPovOptionUtil.optFindYear(-5*12), '년도').bind("change", function (e){
        	var month = [];
        	if(this.value() != ''){
	        	var lastMonth = 12;
	        	if(this.value() == calPovOptionUtil.optNowYear(0)){
	        		lastMonth = calPovOptionUtil.optNowMonth(0);
	        	}
	        	month = calPovOptionUtil.optFindMonth(lastMonth);
        	}
        	// 정산 시작 변경
        	calPovSearchUtil.getDropDownLocalData("findMonth", '',  month, '월');
        }).trigger("change");
    },
    getCalculateAllMonth: function(){
            // 정산 시작 년도
            calPovSearchUtil.getDropDownLocalData("findYear", '', calPovOptionUtil.optFindYear(-5*12), '년도').bind("change", function (e){
            	var month = [];
            	if(this.value() != ''){
    	        	var lastMonth = 12;
    	        	month = calPovOptionUtil.optFindMonth(lastMonth);
            	}
            	// 정산 시작 변경
            	calPovSearchUtil.getDropDownLocalData("findMonth", '',  month, '월');
            }).trigger("change");
        },
    getDropDownLocalData : function(id, chkVal, data, optBlank) {
        return fnKendoDropDownList({
            id :        id,
            tagId :     id,
            value :     chkVal,
            data :      data,
            blank       : optBlank
        });
    },

    setGridTitle : function (year, month) {
        var title = "온라인사업부 " + year + "년 " + month + "월 정산 지표";
        $("#gridTitle").text(title);
    }
}

var calPovOptionUtil = {
	optNowYear: function(addMonth){
        let thisDate    = new Date();
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let thisYear    = thisDate.getFullYear();

        return thisYear;
    },
    optNowMonth: function(addMonth){
        let thisDate    = new Date();
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let thisMonth   = (thisDate.getMonth()+1).zf(2);
        return thisMonth;
    },

    optFindYear: function(addMonth){
        let thisDate    = new Date();
        let thisYear    = thisDate.getFullYear();
        let startYear   = thisYear;
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let checkYear    = thisDate.getFullYear();
        if (startYear > checkYear){
            startYear = checkYear;
        }

        let data = [];
        for (let i=thisYear;i>=startYear;i--){
            let item = new Object();
            item.CODE = i.zf(4);
            item.NAME = i.zf(4) + "년";
            data.push(item);
        }
        return data;
    },
    optFindMonth: function(lastMonth){
        let data = [];
        for (let i=lastMonth;i>=1;i--){
            let item = new Object();
            item.CODE = i.zf(2);
            item.NAME = i.zf(2) + "월";
            data.push(item);
        }
        return data;
    },
}