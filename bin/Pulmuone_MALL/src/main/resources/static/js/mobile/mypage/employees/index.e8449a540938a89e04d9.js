(function(t){function e(e){for(var n,r,i=e[0],l=e[1],c=e[2],_=0,p=[];_<i.length;_++)r=i[_],Object.prototype.hasOwnProperty.call(a,r)&&a[r]&&p.push(a[r][0]),a[r]=0;for(n in l)Object.prototype.hasOwnProperty.call(l,n)&&(t[n]=l[n]);u&&u(e);while(p.length)p.shift()();return o.push.apply(o,c||[]),s()}function s(){for(var t,e=0;e<o.length;e++){for(var s=o[e],n=!0,i=1;i<s.length;i++){var l=s[i];0!==a[l]&&(n=!1)}n&&(o.splice(e--,1),t=r(r.s=s[0]))}return t}var n={},a={"mobile/mypage/employees/index":0},o=[];function r(e){if(n[e])return n[e].exports;var s=n[e]={i:e,l:!1,exports:{}};return t[e].call(s.exports,s,s.exports,r),s.l=!0,s.exports}r.m=t,r.c=n,r.d=function(t,e,s){r.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:s})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,e){if(1&e&&(t=r(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var s=Object.create(null);if(r.r(s),Object.defineProperty(s,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var n in t)r.d(s,n,function(e){return t[e]}.bind(null,n));return s},r.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(e,"a",e),e},r.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},r.p="/";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],l=i.push.bind(i);i.push=e,i=i.slice();for(var c=0;c<i.length;c++)e(i[c]);var u=l;o.push([55,"chunk-commons"]),s()})({55:function(t,e,s){t.exports=s("e611")},c09c:function(t,e,s){},cb97:function(t,e,s){"use strict";var n=s("c09c"),a=s.n(n);a.a},e611:function(t,e,s){"use strict";s.r(e);var n=s("5530"),a=(s("e260"),s("e6cf"),s("cca6"),s("a79d"),function(){var t=this,e=t.$createElement,s=t._self._c||e;return t.userSession.isLogin?s("main",{staticClass:"fb__mypage "},[s("fb-header",{attrs:{type:"sub",title:"임직원 할인 정보",buttons:["back","home","setting"]}}),s("div",{staticClass:"employees"},[s("div",{staticClass:"employees__content"},[s("div",{staticClass:"employees__inner"},[s("h3",{staticClass:"employees__title"},[s("b",[t._v(t._s(t.userSession.name))]),t._v("님의 임직원 할인 정보 현황 "),s("button",{attrs:{type:"button"},on:{click:t.pastInfo}},[t._v(" 과거내역 검색 ")])]),s("div",{staticClass:"employees__info"},[s("div",{staticClass:"employees__info__inner"},[s("table",[t._m(0),t._m(1),s("tbody",[t.loading.list?[t._l(t.lists,(function(e,n){return[t._l(e.list,(function(a,o){return t._l(a.brand,(function(r,i){return s("tr",{key:n+"-"+o+"-"+i},[s("td",[t._v(" "+t._s(r.brandName)+" ")]),0==i?[s("td",{staticClass:"employees__info__percent",attrs:{rowspan:a.brand.length}},[t._v(" "+t._s(a.discountRatio)+" % ")])]:t._e(),0==i&&0==o?[s("td",{attrs:{rowspan:e.rowCount}},[t._v(" "+t._s(e.employeeDiscountLimitCycleTypeName)+" ")]),s("td",{staticClass:"employees__info__reaminAmount",attrs:{rowspan:e.rowCount}},[s("b",[s("span",[t._v(t._s(t._f("price")(e.limitAmount-e.useAmount)))]),t._v("원")])]),s("td",{staticClass:"employees__info__useAmount",attrs:{rowspan:e.rowCount}},[s("span",[t._v(t._s(t._f("price")(e.useAmount)))]),t._v("원 ")]),s("td",{staticClass:"employees__info__limitAmount",attrs:{rowspan:e.rowCount}},[s("span",[t._v(t._s(t._f("price")(e.limitAmount)))]),t._v("원 ")])]:t._e()],2)}))}))]}))]:s("tr",[s("td",{attrs:{colspan:"6"}},[t._v(" loading ")])])],2)]),t._e()])]),t._m(5)])])]),s("fb-footer"),s("fb-dockbar"),s("fb-modal",{attrs:{open:t.pastInfoPopup,isCloseButton:!0,classes:"employees__popup",isMaskBackground:!0,isBackgroundClose:!0},on:{"close-modal":t.closeAddressList}},[s("div",{staticClass:"employees__popup__wrap"},[s("header",{staticClass:"employees__popup__header"},[s("h3",{staticClass:"header__title"},[t._v(" 임직원 할인내역 조회")])]),s("div",{staticClass:"employees__popup__content"},[s("div",{staticClass:"content"},[s("div",{staticClass:"content__filter"},[s("div",{staticClass:"content__year"},[s("fb-select-box",{attrs:{classes:"large full",rows:t.year},scopedSlots:t._u([{key:"option",fn:function(e){var n=e.row;return[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:t.yearResult,expression:"yearResult"}],staticClass:"blind",attrs:{type:"radio",name:"select-box"},domProps:{value:n,checked:t._q(t.yearResult,n)},on:{change:function(e){t.yearResult=n}}}),s("span",[t._v(t._s(n))])])]}}],null,!1,3537489032)},[t._v(" "+t._s(t.yearResult)+" ")])],1),s("div",{staticClass:"content__month"},[s("fb-select-box",{attrs:{classes:"large full",rows:t.month},scopedSlots:t._u([{key:"option",fn:function(e){var n=e.row;return[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:t.monthResult,expression:"monthResult"}],staticClass:"blind",attrs:{type:"radio",name:"select-box"},domProps:{value:n,checked:t._q(t.monthResult,n)},on:{change:function(e){t.monthResult=n}}}),s("span",[t._v(t._s(n))])])]}}],null,!1,2038642952)},[t._v(" "+t._s(t.monthLabel)+" ")])],1)]),s("div",{staticClass:"content__box"},[t.loading.modal?[s("p",{staticClass:"content__price"},[s("span",[t._v(" 사용 합계금액 : ")]),s("b",[t._v(t._s(t._f("price")(t.modalList.sumAmount)))]),t._v(" 원 ")]),s("div",{staticClass:"content__table"},[s("table",[s("colgroup",[s("col"),s("col")]),s("thead",[s("tr",[s("th",[t._v("브랜드")]),s("th",[t._v("사용금액")])])]),s("tbody",[t.modalList.rows&&t.modalList.rows.length?t._l(t.modalList.rows,(function(e,n){return s("tr",{key:n+"list"},[s("td",[t._v(" "+t._s(e.brandName)+" ")]),s("td",{staticClass:"content__tablePrice"},[s("b",[t._v(t._s(t._f("price")(e.useAmount)))]),t._v("원 ")])])})):s("tr",[t.monthResult?s("td",{attrs:{colspan:"2"}},[t._v(" 내역이 없습니다. ")]):s("td",{attrs:{colspan:"2"}},[t._v(" 기간을 선택해주세요. ")])])],2)])])]:s("div",[s("p",[t._v(" loading ")])])],2)])])])])],1):t._e()}),o=[function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("colgroup",[s("col"),s("col"),s("col"),s("col"),s("col"),s("col")])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("thead",[s("tr",[s("th",[t._v("브랜드")]),s("th",[t._v("할인율")]),s("th",[t._v("기간")]),s("th",[t._v("잔여")]),s("th",[t._v("사용")]),s("th",[t._v("지원금액")])])])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("colgroup",[s("col",{attrs:{width:"34%"}}),s("col",{attrs:{width:"20%"}}),s("col",{attrs:{width:"*"}})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("thead",[s("tr",[s("th",[t._v(" 브랜드/할인율 ")]),s("th",[t._v(" 사용기간 ")]),s("th",[t._v(" 사용/잔여금액 ")])])])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("tbody",[s("tr",[s("td",[s("ul",{staticClass:"employees__brand"},[s("li",[t._v(" 아임리얼"),s("span",[t._v("40%")])]),s("li",[t._v(" 풀무원녹즙"),s("span",[t._v("40%")])])])]),s("td",[s("p",{staticClass:"employees__term"},[t._v("분기")])]),s("td",{staticClass:"noLine"},[s("ul",{staticClass:"employees__price"},[s("li",{staticClass:"employees__support"},[s("span",[t._v("총 지원금액")]),s("em",[s("b",[t._v("500,000")]),t._v(" "),s("span",{staticClass:"unit"},[t._v("원")])])]),s("li",{staticClass:"employees__use"},[s("span",[t._v("사용")]),s("em",[s("b",[t._v("90,000 ")]),t._v(" "),s("span",{staticClass:"unit"},[t._v("원")])])]),s("li",{staticClass:"employees__residual"},[s("span",[t._v("잔여")]),s("em",[s("b",[t._v("10,000")]),t._v(" "),s("span",{staticClass:"unit"},[t._v("원")])])])])])])])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"employees__guidance"},[s("h4",[t._v(" 임직원 할인 안내 ")]),s("ul",{staticClass:"employees__guidance__box"},[s("li",{staticClass:"employees__guidance__list"},[t._v(" 1. 임직원 할인은 제품의 할인금액을 회사에서 지원하는 구조입니다. "),s("br"),t._v("[임직원 판매가 = 제품 할인가(회사 지원분) + 본인 결제금액]​ "),s("ul",[s("li",[t._v(" 임직원 할인은 본인, 배우자, 직계존비속만 사용가능하며 제 3자 양도 및 재판매는 불가합니다.​ ")]),s("li",[t._v(" 부적절한 사례가 발견될 시, 사안에 따라 개별조치 됩니다. ")]),s("li",[t._v(" 임직원몰 할인금액은 개인소득에 합산되어 소득세로 부과됩니다.(회사 할인지원액 중 25% 과세) ")])])]),s("li",{staticClass:"employees__guidance__list"},[t._v(" 2. 월별 한도액은 지원 한도 기준에 의해 매월 1일 자동으로 초기화됩니다. ")]),s("li",{staticClass:"employees__guidance__list"},[t._v(" 3. 한도액은 이월되지 않습니다. ")]),s("li",{staticClass:"employees__guidance__list"},[t._v(" 4. 임직원 할인적용 기준은 브랜드 단위로 다르며, 일부 상품의 경우는 할인적용이 되지 않습니다. ")]),s("li",{staticClass:"employees__guidance__list"},[t._v(" 5. 회사 지원 한도를 초과하는 금액은 소비자가로 결제되오니 참고해주시기 바랍니다. (결제 시 초과분 자동 알림 참고) ")])])])}],r=(s("a4d3"),s("e01a"),s("d28b"),s("99af"),s("a9e3"),s("d3b7"),s("ac1f"),s("3ca3"),s("5319"),s("841c"),s("ddb0"),s("2b3d"),s("96cf"),s("1da1")),i=s("8e32"),l=s("629b"),c=s("cbaa"),u=s("face"),_=s("2479"),p=s("0bf4"),d=s("533e"),m=s("e096"),h=s("c1df"),f=s.n(h),v={name:"mypage-employees",mixins:[p["a"]],components:{fbHeader:i["a"],fbFooter:l["a"],fbDockbar:c["a"],fbAlert:u["a"],fbModal:_["a"],fbSelectBox:d["a"],VueRecaptcha:m["default"]},data:function(){return{pastInfoPopup:!1,isPopup:!1,minYear:2019,defaultYear:0,minMonth:0,year:[],yearResult:"",month:[],monthResult:"",monthLabel:"월 선택",loading:{list:!1,modal:!1},lists:null,modalList:null}},created:function(){if(this.userSession.isLogin)if(this.userSession.isEmployees){this.getQueryString();this.getEmployeeDiscount()}else location.replace(this.$APP_CONFIG.PAGE_URL.MYPAGE_ROOT)},mounted:function(){},watch:{yearResult:{handler:function(t,e){if(this.isPopup){if(t==this.defaultYear){this.month=[];for(var s=1;s<=Number(this.minMonth);s++)this.month.push(1==String(s).length?"0"+String(s):s)}else{this.month=[];for(var n=1;n<=12;n++)this.month.push(1==String(n).length?"0"+String(n):n)}this.monthLabel="월 선택",this.monthResult=""}}},monthResult:{handler:function(t,e){this.isPopup&&t&&(this.monthLabel=t,this.getEmployeeDiscountPastInfo())}}},computed:{},methods:{getQueryString:function(){return new URLSearchParams(window.location.search)},changeDate:function(t){var e=t.start,s=t.end;console.log("start",e),console.log("end",s)},getEmployeeDiscount:function(){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function e(){var s,n,a,o,r,i,l,c,u,_,p,d,m,h,f;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,t.$store.dispatch("network/request",{method:"get",url:"/user/buyer/getEmployeeDiscount"});case 3:if(s=e.sent,s.code!=t.$FB_CODES.API.SUCCESS){e.next=53;break}n=s.data,t.lists=n,a=!0,o=!1,r=void 0,e.prev=10,i=t.lists[Symbol.iterator]();case 12:if(a=(l=i.next()).done){e.next=38;break}for(c=l.value,u=0,_=!0,p=!1,d=void 0,e.prev=18,m=c.list[Symbol.iterator]();!(_=(h=m.next()).done);_=!0)f=h.value,u+=f.brand.length;e.next=26;break;case 22:e.prev=22,e.t0=e["catch"](18),p=!0,d=e.t0;case 26:e.prev=26,e.prev=27,_||null==m.return||m.return();case 29:if(e.prev=29,!p){e.next=32;break}throw d;case 32:return e.finish(29);case 33:return e.finish(26);case 34:t.$set(c,"rowCount",u);case 35:a=!0,e.next=12;break;case 38:e.next=44;break;case 40:e.prev=40,e.t1=e["catch"](10),o=!0,r=e.t1;case 44:e.prev=44,e.prev=45,a||null==i.return||i.return();case 47:if(e.prev=47,!o){e.next=50;break}throw r;case 50:return e.finish(47);case 51:return e.finish(44);case 52:t.loading.list=!0;case 53:e.next=58;break;case 55:e.prev=55,e.t2=e["catch"](0),console.error("getEmployeeDiscount error...",e.t2.message);case 58:case"end":return e.stop()}}),e,null,[[0,55],[10,40,44,52],[18,22,26,34],[27,,29,33],[45,,47,51]])})))()},getEmployeeDiscountPastInfo:function(){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function e(){var s,n;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,s=f()().add(-1,"M").format("YYYY-MM"),e.next=4,t.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getEmployeeDiscountPastInfo",data:{searchDate:t.monthResult?"".concat(t.yearResult,"-").concat(t.monthResult):s}});case 4:n=e.sent,n.code==t.$FB_CODES.API.SUCCESS&&(t.modalList=n.data,t.loading.modal=!0,t.pastInfoPopup=!0),t.$nextTick((function(){t.isPopup=!0})),e.next=12;break;case 9:e.prev=9,e.t0=e["catch"](0),console.error("getEmployeeDiscount error...",e.t0.message);case 12:case"end":return e.stop()}}),e,null,[[0,9]])})))()},pastInfo:function(){var t=new Date,e=t.getFullYear();this.defaultYear=0,t.getMonth()+1<f()(t.getTime()).add("-1","M").format("MM")?this.defaultYear=e-1:this.defaultYear=e;for(var s=this.minYear;s<=this.defaultYear;s++)this.year.unshift(s);for(var n=1;n<=f()(t.getTime()).add("-1","M").format("MM");n++)this.month.push(1==String(n).length?"0"+String(n):n);this.yearResult=this.defaultYear,this.minMonth=f()(t.getTime()).add("-1","M").format("MM"),this.monthResult=1==f()(t.getTime()).add("-1","M").format("MM").length?"0"+f()(t.getTime()).add("-1","M").format("MM"):f()(t.getTime()).add("-1","M").format("MM"),this.monthLabel=this.monthResult,this.getEmployeeDiscountPastInfo()},closeAddressList:function(){this.year=[],this.yearResult="",this.month=[],this.monthResult="",this.isPopup=!1,this.pastInfoPopup=!1}}},b=v,y=(s("cb97"),s("2877")),g=Object(y["a"])(b,a,o,!1,null,null,null),C=g.exports,w=s("dd69");w["c"].components=Object(n["a"])({App:C},w["c"].components),w["d"].dispatch("init").then((function(){return new w["a"](w["c"])}))}});