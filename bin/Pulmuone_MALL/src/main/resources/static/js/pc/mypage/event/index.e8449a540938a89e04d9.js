(function(e){function t(t){for(var a,o,r=t[0],c=t[1],_=t[2],E=0,d=[];E<r.length;E++)o=r[E],Object.prototype.hasOwnProperty.call(s,o)&&s[o]&&d.push(s[o][0]),s[o]=0;for(a in c)Object.prototype.hasOwnProperty.call(c,a)&&(e[a]=c[a]);l&&l(t);while(d.length)d.shift()();return i.push.apply(i,_||[]),n()}function n(){for(var e,t=0;t<i.length;t++){for(var n=i[t],a=!0,r=1;r<n.length;r++){var c=n[r];0!==s[c]&&(a=!1)}a&&(i.splice(t--,1),e=o(o.s=n[0]))}return e}var a={},s={"pc/mypage/event/index":0},i=[];function o(t){if(a[t])return a[t].exports;var n=a[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,o),n.l=!0,n.exports}o.m=e,o.c=a,o.d=function(e,t,n){o.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},o.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},o.t=function(e,t){if(1&t&&(e=o(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(o.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var a in e)o.d(n,a,function(t){return e[t]}.bind(null,a));return n},o.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return o.d(t,"a",t),t},o.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},o.p="/";var r=window["webpackJsonp"]=window["webpackJsonp"]||[],c=r.push.bind(r);r.push=t,r=r.slice();for(var _=0;_<r.length;_++)t(r[_]);var l=c;i.push([138,"chunk-commons"]),n()})({138:function(e,t,n){e.exports=n("9364")},9364:function(e,t,n){"use strict";n.r(t);var a=n("5530"),s=(n("e260"),n("e6cf"),n("cca6"),n("a79d"),function(){var e=this,t=e.$createElement,n=e._self._c||t;return e.userSession.isLogin?n("main",{staticClass:"fb__mypage fb__mypage__event"},[n("fb-header",{attrs:{type:"sub"}}),n("div",{ref:"scrollWrapper",staticClass:"product-layout__body"},[n("section",{staticClass:"product-layout__body__left"},[n("fb-mypage-menu")],1),n("section",{staticClass:"product-layout__body__right"},[n("h2",{staticClass:"fb__mypage__title"},[e._v("이벤트 참여 현황")]),n("div",{staticClass:"event"},[n("a",{staticClass:"event__notice",attrs:{href:e.$APP_CONFIG.PAGE_URL.CUSTOMER_NOTICES}},[e._v("당첨자 공지 보기")]),n("div",{staticClass:"event__search"},[n("h3",{staticClass:"event__search__title"},[e._v("조회기간")]),n("div",{staticClass:"event__search__form"},[n("fb-months",{on:{"change-date":e.changeDate}}),n("button",{staticClass:"form__submit",attrs:{type:"button",disabled:e.userActionDisabled},on:{click:e.handleSubmit}},[e._v("조회하기")])],1)]),e.$FB_CODES.FETCHES.WAIT===e.fetches.list?n("div",{staticClass:"fb__fetching"}):e.$FB_CODES.FETCHES.SUCCESS===e.fetches.list?[e.lists.length?n("ul",{staticClass:"event__list"},e._l(e.lists,(function(t,a){return n("li",{key:a+"list",staticClass:"event__item"},[n("div",{staticClass:"event__item__info"},[n("span",{staticClass:"info__type"},[e._v(" "+e._s(e.eventType[t.eventType].name)+" ")]),n("p",{staticClass:"info__title"},[n("a",{attrs:{href:e.eventType[t.eventType].link+t.evEventId}},[e._v(" "+e._s(t.title)+" ")])]),n("div",{staticClass:"info__badge"},["EVENT_TP.EXPERIENCE"==t.eventType?n("span",{staticClass:"info__badge__icon exp"},[e._v("체험단")]):e._e(),"IN_PROGRESS"==t.status?n("span",{staticClass:"info__badge__icon progress"},[e._v("진행중")]):n("span",{staticClass:"info__badge__icon end"},[e._v("종료")])])]),n("div",{staticClass:"event__item__detail"},[n("dl",{staticClass:"detail__status"},[n("dt",[e._v("이벤트 기간")]),n("dd",[e._v(e._s(t.startDate)+" ~ "+e._s(t.endDate))])]),[t.winnerInforDate&&"Y"==t.winnerExistYn?n("dl",{staticClass:"detail__status"},[n("dt",["EVENT_TP.EXPERIENCE"==t.eventType?[e._v("체험단")]:[e._v("당첨자")],e._v(" 발표 ")],2),n("dd",[e._v(e._s(t.winnerInforDate))])]):e._e(),["EVENT_TP.ATTEND","EVENT_TP.MISSION","EVENT_TP.PURCHASE"].includes(t.eventType)?n("dl",{staticClass:"detail__status"},[n("dt",[e._v("참여횟수")]),n("dd",[n("em",[e._v(e._s(t.stampCount)+" / "+e._s(t.stampMaxCount))])])]):n("dl",{staticClass:"detail__status"},[n("dt",["EVENT_TP.EXPERIENCE"==t.eventType?[e._v("선정")]:[e._v("당첨")],e._v("여부 ")],2),n("dd",[n("span",{staticClass:"prize",class:{check:"Y"==t.userWinnerYn}},["Y"==t.userWinnerYn?["EVENT_TP.EXPERIENCE"==t.eventType?[e._v("선정")]:[e._v("당첨")]]:["EVENT_TP.EXPERIENCE"==t.eventType?[e._v("미선정")]:[e._v("미당첨")]],t.experienceOrderCode?n("span",{staticClass:"prize__shipping"},["INCOM_COMPLETE"==t.experienceOrderCode?[e._v(" 상품 배송 예정 ")]:e._e()],2):e._e()],2)])])]],2),"EVENT_TP.EXPERIENCE"==t.eventType?n("div",{staticClass:"event__item__btn"},["ORDER_BEFORE"==t.experienceOrderCode?n("a",{attrs:{href:e.$APP_CONFIG.PAGE_URL.GOODS_VIEW+"?goods="+t.experienceIlGoodsId}},[e._v("체험단 상품 바로보기")]):"Y"!=t.feedbackYn&&["DELIVERY_DOING","DELIVERY_COMPLETE"].includes(t.experienceOrderCode)?n("button",{attrs:{type:"button"},on:{click:function(n){return e.openEventLayer(t.feedback)}}},[e._v("체험단 상품후기 작성")]):"Y"==t.feedbackYn?n("button",{attrs:{type:"button",disabled:""}},[e._v("체험단 상품후기 작성완료")]):e._e()]):e._e()])})),0):n("div",{staticClass:"event__list__empty"},[n("p",[e._v("참여한 이벤트 내역이 없습니다.")])]),n("div",{staticClass:"event__pagination"},[n("fb-pagination",{attrs:{pagination:e.pagination},on:{movePaging:e.movePaging}})],1)]:e._e()],2)])]),n("fb-footer"),n("fb-modal",{attrs:{open:e.eventLayer.open,isCloseButton:!0,classes:"fb__modal__event",isMaskBackground:!0,isBackgroundClose:!1},on:{"close-modal":function(t){return e.closeEventLayer({e:t,response:"close"})}}},[n("event-write",{attrs:{open:e.eventLayer.open,item:e.eventLayer.item},on:{"close-modal":e.closeEventLayer}})],1),n("fb-alert",{attrs:{open:e.alert.open,message:e.alert.message},on:{"close-alert":function(t){return e.closeAlert(t)}}}),n("fb-confirm",{attrs:{open:e.confirm.open,width:e.confirm.width,height:e.confirm.height,title:e.confirm.title,message:e.confirm.message,ok:e.confirm.ok,cancel:e.confirm.cancel,"is-background":e.confirm.isBackground,"is-background-close":e.confirm.isBackgroundClose},on:{"close-confirm":function(t){return e.closeConfirm(t)}}})],1):e._e()}),i=[],o=(n("d3b7"),n("ac1f"),n("3ca3"),n("841c"),n("ddb0"),n("2b3d"),n("96cf"),n("1da1")),r=n("d3a8"),c=n("dcbe"),_=n("4402"),l=n("7fcc"),E=n("7e74"),d=n("9032"),u=n("9d0b"),p=n("aa0e"),v=n("863d"),f=n("0bf4"),m=n("8fc3"),C={name:"mypage-event",mixins:[f["a"],r["a"]],components:{fbHeader:c["a"],fbFooter:_["a"],fbMypageMenu:l["a"],fbPagination:E["a"],fbMonths:d["a"],fbAlert:u["a"],fbConfirm:p["a"],fbModal:v["a"],fbHistory:f["a"],eventWrite:m["a"]},data:function(){return{eventLayer:{open:!1,item:null},date:{startDate:"",endDate:""},lists:[],eventType:{"EVENT_TP.NORMAL":{name:"일반이벤트",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_COMMENT,"?event=")},"EVENT_TP.SURVEY":{name:"설문이벤트",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_SURVEY,"?event=")},"EVENT_TP.ATTEND":{name:"스탬프(출석)",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_STAMP,"?event=")},"EVENT_TP.MISSION":{name:"스탬프(미션)",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_STAMP,"?event=")},"EVENT_TP.PURCHASE":{name:"스탬프(구매)",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_STAMP,"?event=")},"EVENT_TP.ROULETTE":{name:"룰렛이벤트",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_ROULETTE,"?event=")},"EVENT_TP.EXPERIENCE":{name:"체험단이벤트",link:"".concat(this.$APP_CONFIG.PAGE_URL.EVENT_EXPERIENCE,"?event=")}},pagination:{id:"event-pagination",currentPage:1,goodsCount:20,totalCount:0,disabled:!1}}},created:function(){},mounted:function(){this.getEventListFromMyPage()},watch:{},computed:{},methods:{getQueryString:function(){return new URLSearchParams(window.location.search)},handleSubmit:function(){this.clearPage(),this.getEventListFromMyPage()},getEventListFromMyPage:function(){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function t(){var n,s,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n=e.processKeys.list,t.prev=1,e.checkInProcessing(n),e.fetches.list=e.$FB_CODES.FETCHES.WAIT,t.next=6,e.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getEventListFromMyPage",data:Object(a["a"])({},e.date,{page:e.pagination.currentPage,limit:e.pagination.goodsCount})});case 6:s=t.sent,s.code==e.$FB_CODES.API.SUCCESS&&(i=s.data,e.pagination.totalCount=i.total,e.lists=i.list,e.fetches.list=e.$FB_CODES.FETCHES.SUCCESS,e.waitProcesses(n)),t.next=13;break;case 10:t.prev=10,t.t0=t["catch"](1),e.isNonHandlingException(t.t0)||(e.fetches.list=e.$FB_CODES.FETCHES.ERROR,console.error("getEmployeeDiscount error...",t.t0.message));case 13:case"end":return t.stop()}}),t,null,[[1,10]])})))()},movePaging:function(e){this.pagination.currentPage=e,this.getEventListFromMyPage(e)},openEventLayer:function(e){e?(this.eventLayer.item=e,this.eventLayer.open=!0):this.openAlert(this.$FB_MESSAGES.ERROR.DEFAULT)},closeEventLayer:function(e){var t;(null===e||void 0===e||null===(t=e.response)||void 0===t?void 0:t.code)==this.$FB_CODES.API.SUCCESS?(this.eventLayer.item={},this.eventLayer.open=!1,location.reload()):"close"==(null===e||void 0===e?void 0:e.response)&&this.openConfirm({type:"cancel",message:this.$FB_MESSAGES.SYSTEM.CONFIRM_07})},changeDate:function(e){var t=e.start,n=e.end;this.date.startDate=t,this.date.endDate=n},closeConfirm:function(e){var t=e.value;if(t)switch(this.confirm.type){case"cancel":this.eventLayer.item={},this.eventLayer.open=!1;break}this.resetConfirm()}}},h=C,g=(n("f052"),n("2877")),P=Object(g["a"])(h,s,i,!1,null,null,null),b=P.exports,T=n("8f7d"),y=n("caf9");T["a"].use(y["a"],{error:T["b"].IMAGES.NOT_FOUND,loading:T["b"].IMAGES.IMG_LOADING}),T["c"].components=Object(a["a"])({App:b},T["c"].components),T["d"].dispatch("init").then((function(){return new T["a"](T["c"])}))},"98de":function(e,t,n){},f052:function(e,t,n){"use strict";var a=n("98de"),s=n.n(a);s.a}});