(function(e){function o(o){for(var n,i,u=o[0],c=o[1],p=o[2],_=0,l=[];_<u.length;_++)i=u[_],Object.prototype.hasOwnProperty.call(a,i)&&a[i]&&l.push(a[i][0]),a[i]=0;for(n in c)Object.prototype.hasOwnProperty.call(c,n)&&(e[n]=c[n]);r&&r(o);while(l.length)l.shift()();return s.push.apply(s,p||[]),t()}function t(){for(var e,o=0;o<s.length;o++){for(var t=s[o],n=!0,u=1;u<t.length;u++){var c=t[u];0!==a[c]&&(n=!1)}n&&(s.splice(o--,1),e=i(i.s=t[0]))}return e}var n={},a={"mobile/mypage/coupon/index":0},s=[];function i(o){if(n[o])return n[o].exports;var t=n[o]={i:o,l:!1,exports:{}};return e[o].call(t.exports,t,t.exports,i),t.l=!0,t.exports}i.m=e,i.c=n,i.d=function(e,o,t){i.o(e,o)||Object.defineProperty(e,o,{enumerable:!0,get:t})},i.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,o){if(1&o&&(e=i(e)),8&o)return e;if(4&o&&"object"===typeof e&&e&&e.__esModule)return e;var t=Object.create(null);if(i.r(t),Object.defineProperty(t,"default",{enumerable:!0,value:e}),2&o&&"string"!=typeof e)for(var n in e)i.d(t,n,function(o){return e[o]}.bind(null,n));return t},i.n=function(e){var o=e&&e.__esModule?function(){return e["default"]}:function(){return e};return i.d(o,"a",o),o},i.o=function(e,o){return Object.prototype.hasOwnProperty.call(e,o)},i.p="/";var u=window["webpackJsonp"]=window["webpackJsonp"]||[],c=u.push.bind(u);u.push=o,u=u.slice();for(var p=0;p<u.length;p++)o(u[p]);var r=c;s.push([52,"chunk-commons"]),t()})({52:function(e,o,t){e.exports=t("9226")},"52f4":function(e,o,t){"use strict";var n=t("9d1c"),a=t.n(n);a.a},9226:function(e,o,t){"use strict";t.r(o);var n=t("5530"),a=(t("e260"),t("e6cf"),t("cca6"),t("a79d"),function(){var e=this,o=e.$createElement,t=e._self._c||o;return e.userSession.isLogin?t("main",{staticClass:"fb__mypage "},[t("fb-header",{attrs:{type:"sub",title:"쿠폰",buttons:["back","home","setting"]}}),t("div",{staticClass:"coupon"},[t("nav",{staticClass:"coupon__tap"},[t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTap,expression:"couponTap"}],attrs:{type:"radio",name:"coupon",value:"COUPON_STATUS.NOTUSE"},domProps:{checked:e._q(e.couponTap,"COUPON_STATUS.NOTUSE")},on:{change:function(o){e.couponTap="COUPON_STATUS.NOTUSE"}}}),e._m(0)]),t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTap,expression:"couponTap"}],attrs:{type:"radio",name:"coupon",value:"COUPON_STATUS.USE"},domProps:{checked:e._q(e.couponTap,"COUPON_STATUS.USE")},on:{change:function(o){e.couponTap="COUPON_STATUS.USE"}}}),e._m(1)])]),t("div",{staticClass:"coupon__start"},["COUPON_STATUS.NOTUSE"==e.couponTap?t("div",{staticClass:"coupon__registration"},[t("a",{attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_TICKET}},[e._v(" 풀무원 이용권/쿠폰 등록 ")])]):e._e(),"COUPON_STATUS.NOTUSE"==e.couponTap?t("nav",{staticClass:"coupon__typeTap"},[t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTypeTap,expression:"couponTypeTap"}],attrs:{type:"radio",name:"couponType",value:""},domProps:{checked:e._q(e.couponTypeTap,"")},on:{change:function(o){e.couponTypeTap=""}}}),t("span",[e._v("전체 ")])]),t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTypeTap,expression:"couponTypeTap"}],attrs:{type:"radio",name:"couponType",value:"COUPON_TYPE.GOODS"},domProps:{checked:e._q(e.couponTypeTap,"COUPON_TYPE.GOODS")},on:{change:function(o){e.couponTypeTap="COUPON_TYPE.GOODS"}}}),t("span",[e._v("상품 ")])]),t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTypeTap,expression:"couponTypeTap"}],attrs:{type:"radio",name:"couponType",value:"COUPON_TYPE.CART"},domProps:{checked:e._q(e.couponTypeTap,"COUPON_TYPE.CART")},on:{change:function(o){e.couponTypeTap="COUPON_TYPE.CART"}}}),t("span",[e._v("장바구니 ")])]),t("label",[t("input",{directives:[{name:"model",rawName:"v-model",value:e.couponTypeTap,expression:"couponTypeTap"}],attrs:{type:"radio",name:"couponType",value:"COUPON_TYPE.SHIPPING_PRICE"},domProps:{checked:e._q(e.couponTypeTap,"COUPON_TYPE.SHIPPING_PRICE")},on:{change:function(o){e.couponTypeTap="COUPON_TYPE.SHIPPING_PRICE"}}}),t("span",[e._v("배송비 ")])])]):e._e(),e.loading.list&&e.couponList.length>0?t("div",{staticClass:"coupon__box"},["COUPON_STATUS.USE"==e.couponTap?t("div",{staticClass:"coupon__end"},[t("p",{staticClass:"coupon__end__info"},[e._v("최근 6개월 내역만 조회 가능합니다.")])]):e._e(),t("ul",e._l(e.couponList,(function(o,n){return t("li",{key:n,staticClass:"coupon__list",class:o.couponCount>"1"?"coupon__list--multi":""},[t("div",{staticClass:"coupon__listInner"},[t("div",{staticClass:"coupon__listTop"},[t("span",{staticClass:"coupon__badge"},[["COUPON_TYPE.GOODS","COUPON_TYPE.SALEPRICE_APPPOINT"].includes(o.couponType)?[e._v("상품 쿠폰")]:e._e(),["COUPON_TYPE.CART"].includes(o.couponType)?[e._v("장바구니 쿠폰")]:e._e(),["COUPON_TYPE.SHIPPING_PRICE"].includes(o.couponType)?[e._v("배송비 쿠폰")]:e._e()],2),e.visibleBadge(o)?t("span",{staticClass:"coupon__type"},["Y"==o.usePcYn&&"Y"==o.useMobileWebYn?[e._v("웹전용")]:"Y"==o.usePcYn?[e._v("PC전용")]:"Y"==o.useMobileWebYn?[e._v("모바일전용")]:"Y"==o.useMobileAppYn?[e._v("앱전용")]:e._e()],2):e._e()]),t("p",{staticClass:"coupon__listValue"},[e.issueDetailType.USER_JOIN==o.issueDetailType?[t("span",[e._v("신규가입 혜택")])]:e.couponType.SHIPPING_PRICE==o.couponType?[o.percentageMaxDiscountAmount>0?[t("em",[e._v(e._s(e._f("price")(o.percentageMaxDiscountAmount)))]),t("span",[e._v("원")])]:[t("span",[e._v("무료배송")])]]:e.couponType.SALEPRICE_APPPOINT==o.couponType&&[e.issueDetailType.EVENT_AWARD,e.issueDetailType.TESTER,e.issueDetailType.RECOMMENDER,e.issueDetailType.USER_GRADE].includes(o.issueDetailType)?[t("span",[e._v(e._s(e._f("price")(o.discountValue))+"원 구매 혜택")])]:[t("em",[e._v(e._s(e._f("price")(o.discountValue)))]),t("span",[e.discountType.PERCENTAGE==o.discountType?[e._v("% OFF")]:e.discountType.FIXED==o.discountType?[e._v("원")]:e._e()],2)]],2),t("p",{staticClass:"coupon__listSubValue"},[e._v(" "+e._s(o.displayCouponName)+" ")]),t("div",{staticClass:"coupon__listInfo",class:{multi:o.couponCount>1}},[t("ul",[[e.issueDetailType.EVENT_AWARD,e.issueDetailType.TESTER].includes(o.issueDetailType)?e._e():t("li",[e.couponType.SHIPPING_PRICE==o.couponType?[t("p",[o.minPaymentAmount>0?[e._v(" "+e._s(e._f("price")(o.minPaymentAmount))+"원 이상 구매 시 ")]:e._e(),[o.percentageMaxDiscountAmount>0?[e._v(e._s(e._f("price")(o.percentageMaxDiscountAmount))+"원 할인")]:o.minPaymentAmount>0?[e._v("무료배송")]:e._e()]],2)]:e.couponType.SALEPRICE_APPPOINT==o.couponType?[o.discountValue>0?t("p",[o.minPaymentAmount>0?[e._v(" "+e._s(e._f("price")(o.minPaymentAmount))+"원 이상 구매 시 ")]:e._e(),e._v(" "+e._s(e._f("price")(o.discountValue))+"원 할인 ")],2):e._e()]:[e.discountType.PERCENTAGE==o.discountType?[t("p",[o.minPaymentAmount>0?[e._v(e._s(e._f("price")(o.minPaymentAmount))+"원 이상 구매 시")]:e._e(),e._v(" "+e._s(o.discountValue)+"% 할인, 최대할인금액 "+e._s(e._f("price")(o.percentageMaxDiscountAmount))+"원")],2)]:e._e(),e.discountType.FIXED==o.discountType?[t("p",[o.minPaymentAmount>0?[e._v(e._s(e._f("price")(o.minPaymentAmount))+"원 이상 구매 시 ")]:e._e(),e._v(" "+e._s(e._f("price")(o.discountValue))+"원 할인")],2)]:e._e()]],2),t("li",[e._v(" 사용 가능 기간 "),t("span",[e._v(e._s(o.validityStartDate)+" ~ "+e._s(o.validityEndDate))])])]),o.couponCount>1?t("span",{staticClass:"coupon__listInfo__count"},[e._v(e._s(o.couponCount)+"장 보유")]):e._e()])]),"COUPON_STATUS.NOTUSE"==e.couponTap?t("button",{staticClass:"coupon__btnGoods",attrs:{type:"button"},on:{click:function(t){return e.getCouponGoods(o.pmCouponId)}}},[e._v("적용 대상 보기")]):t("button",{staticClass:"coupon__btnGoods",attrs:{type:"button",disabled:""}},[e._v(e._s(o.statusName))])])})),0),e.request.total>=e.request.limit?t("infinite-loading",{ref:"infiniteLoading",on:{infinite:e.infiniteHandler}},[t("div",{attrs:{slot:"no-more"},slot:"no-more"}),t("div",{attrs:{slot:"no-results"},slot:"no-results"}),t("div",{attrs:{slot:"error"},slot:"error"})]):e._e()],1):t("div",{staticClass:"coupon__noItem"},["COUPON_STATUS.NOTUSE"==e.couponTap?[t("span",[e._v("사용 가능한 쿠폰이 없습니다.")])]:[t("span",[e._v("지난 쿠폰이 없습니다.")])]],2)]),e.loading.list?t("div",{staticClass:"coupon__memberInfo"},[t("p",{staticClass:"coupon__memberInfo__title"},[e._v(" 쿠폰 유의사항 ")]),t("button",{staticClass:"coupon__memberInfo__btn",class:{on:e.memberInfo},attrs:{type:"button"},on:{click:function(o){e.memberInfo=!e.memberInfo}}},[e._v("보이기/감추기")]),e.memberInfo?t("ul",{staticClass:"coupon__memberInfo__list"},[e._m(2),e._m(3),e._m(4),t("li",[e._v(" 골라담아 묶음할인 상품은 쿠폰 적용이 불가합니다. ")])]):e._e()]):e._e()]),t("fb-footer"),t("fb-dockbar"),t("fb-modal",{attrs:{open:e.couponLayer.open,isCloseButton:!0,isMaskBackground:!0,isBackgroundClose:!1},on:{"close-modal":e.closeChangeDate}},[t("div",{staticClass:"coupon__modal"},[t("header",{staticClass:"coupon__modal__header"},[t("h3",{staticClass:"coupon__modal__title"},[e._v(" 쿠폰 적용 대상 ")])]),t("div",{staticClass:"coupon__modal__content"},[e.coverageList.coverage&&e.coverageList.coverage.length>0?e._l(e.coverageList.coverage,(function(o,n){return t("div",{key:"coverage-"+n,staticClass:"coupon__modal__list"},[t("h4",{staticClass:"coupon__modal__subTitle"},[e._v("적용 "+e._s(e.coverageTypeName.find((function(e){return e.type==o.coverageType})).name))]),t("ul",{staticClass:"coupon__modal__goods"},[e._l(o.coverageName,(function(o,n){return[o?t("li",{key:"coverage-list-"+n},[e._v(" "+e._s(o)+" ")]):e._e()]}))],2)])})):[t("div",{staticClass:"coupon__modal__list"},[t("h4",{staticClass:"coupon__modal__subTitle"},[e._v("적용 대상")]),t("p",{staticClass:"coupon__modal__all "},[e._v(" 전체 상품 ")])])],e.coverageList.notCoverage&&e.coverageList.notCoverage.length>0?e._l(e.coverageList.notCoverage,(function(o,n){return t("div",{key:"coverage-"+n,staticClass:"coupon__modal__list"},[t("h4",{staticClass:"coupon__modal__subTitle"},[e._v("적용 제외 "+e._s(e.coverageTypeName.find((function(e){return e.type==o.coverageType})).name))]),t("ul",{staticClass:"coupon__modal__goods"},[e._l(o.coverageName,(function(o,n){return[o?t("li",{key:"coverage-list-"+n},[e._v(" "+e._s(o)+" ")]):e._e()]}))],2)])})):e._e(),t("p",{staticClass:"coupon__modal__deco"},[e._v(" 일부 상품의 경우 쿠폰 할인 적용대상에서 제외될 수 있습니다. ")])],2)])])],1):e._e()}),s=[function(){var e=this,o=e.$createElement,t=e._self._c||o;return t("span",[t("em",[e._v("사용 가능 쿠폰")])])},function(){var e=this,o=e.$createElement,t=e._self._c||o;return t("span",[t("em",[e._v("사용/만료 쿠폰")])])},function(){var e=this,o=e.$createElement,t=e._self._c||o;return t("li",[t("b",[e._v("상품쿠폰")]),e._v(" 상품 단위로 상품 1개당 1개의 쿠폰 사용 가능합니다. ")])},function(){var e=this,o=e.$createElement,t=e._self._c||o;return t("li",[t("b",[e._v("장바구니 쿠폰")]),e._v(" 장바구니 주문금액에 대해 쿠폰 할인이 적용됩니다. ")])},function(){var e=this,o=e.$createElement,t=e._self._c||o;return t("li",[t("b",[e._v("배송비 쿠폰")]),e._v(" 배송비 할인이 적용 되며 쿠폰에 설정된 금액 만큼 할인 적용됩니다. ")])}],i=(t("99af"),t("d3b7"),t("ac1f"),t("3ca3"),t("841c"),t("ddb0"),t("2b3d"),t("96cf"),t("1da1")),u=t("8e32"),c=t("629b"),p=t("cbaa"),r=t("e166"),_=t.n(r),l=t("face"),T=t("2479"),d=t("0bf4"),m={name:"mypage-coupon",mixins:[d["a"]],components:{fbHeader:u["a"],fbFooter:c["a"],fbDockbar:p["a"],InfiniteLoading:_.a,fbAlert:l["a"],fbModal:T["a"]},data:function(){return{couponTap:"COUPON_STATUS.NOTUSE",couponTypeTap:"",loading:{list:!1},request:{status:"COUPON_STATUS.NOTUSE",couponType:"",page:1,limit:10,total:0},couponTabList:[{name:"사용가능 쿠폰",value:"COUPON_STATUS.NOTUSE"},{name:"사용만료 쿠폰",value:"COUPON_STATUS.USE"}],couponTypeList:[{name:"전체",value:""},{name:"상품 쿠폰",value:"COUPON_TYPE.GOODS"},{name:"장바구니 쿠폰",value:"COUPON_TYPE.CART"},{name:"배송비 쿠폰",value:"COUPON_TYPE.SHIPPING_PRICE"}],couponList:[],coverageList:{coverage:null,notCoverage:null},coverageTypeName:[{type:"",name:"대상"},{type:"APPLYCOVERAGE.GOODS",name:"상품"},{type:"APPLYCOVERAGE.BRAND",name:"브랜드"},{type:"APPLYCOVERAGE.DISPLAY_CATEGORY",name:"카테고리"},{type:"APPLYCOVERAGE.WAREHOUSE",name:"출고처"}],couponLayer:{open:!1},memberInfo:!1}},created:function(){},mounted:function(){},watch:{couponTap:{handler:function(e,o){this.request.page=1,this.loading.list=!1,this.request.status=e,this.request.couponType="",this.couponTypeTap="",this.getCouponList()},immediate:!0},couponTypeTap:{handler:function(e,o){"COUPON_STATUS.USE"!=this.request.status&&(this.request.page=1,this.request.couponType=this.couponTypeTap,this.getCouponList())}}},computed:{couponType:function(){return this.$FB_CODES.GOODS.COUPON_TYPE},discountType:function(){return this.$FB_CODES.GOODS.COUPON_DISCOUNT_STATUS},issueDetailType:function(){return this.$FB_CODES.GOODS.COUPON_AUTO_ISSUE_TYPE}},methods:{infiniteHandler:function(e){null!=this.couponList&&this.couponList.length<this.request.total?(this.request.page=this.request.page+1,this.getCouponList(e)):e.complete()},getCouponList:function(e){var o=this;return Object(i["a"])(regeneratorRuntime.mark((function t(){var n,a,s,i,u;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,n={status:o.request.status,couponType:o.request.couponType,page:o.request.page,limit:o.request.limit},e||null===(a=o.$refs["infiniteLoading"])||void 0===a||null===(s=a.stateChanger)||void 0===s||s.reset(),t.next=5,o.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getCouponListByUser",data:n});case 5:i=t.sent,u=i.data,i.code==o.$FB_CODES.API.SUCCESS?(o.request.total=u.total,e?u.total>0&&u.rows.length>0?(o.couponList=o.couponList.concat(u.rows),e&&e.loaded()):e&&e.complete():o.couponList=u.rows,o.loading.list=!0):o.loading.list=!1,t.next=14;break;case 10:t.prev=10,t.t0=t["catch"](0),console.error("getCouponList error...",t.t0.message),o.loading.list="error";case 14:case"end":return t.stop()}}),t,null,[[0,10]])})))()},getCouponGoods:function(e){var o=this;return Object(i["a"])(regeneratorRuntime.mark((function t(){var n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,o.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getCouponCoverage",data:{pmCouponId:e}});case 3:n=t.sent,n.code==o.$FB_CODES.API.SUCCESS&&(o.coverageList.coverage=n.data.coverage,o.coverageList.notCoverage=n.data.notCoverage,o.couponLayer.open=!0),t.next=10;break;case 7:t.prev=7,t.t0=t["catch"](0),console.error("getCouponGoods error...",t.t0.message);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))()},getQueryString:function(){return new URLSearchParams(window.location.search)},closeChangeDate:function(){this.couponLayer.open=!1},visibleBadge:function(e){var o=!0;return("Y"==e.usePcYn&&"Y"==e.useMobileWebYn&&"Y"==e.useMobileAppYn||"Y"!=e.usePcYn&&"Y"!=e.useMobileWebYn&&"Y"!=e.useMobileAppYn||"Y"==e.usePcYn&&"Y"!=e.useMobileWebYn&&"Y"==e.useMobileAppYn)&&(o=!1),o}}},v=m,C=(t("52f4"),t("2877")),f=Object(C["a"])(v,a,s,!1,null,null,null),O=f.exports,P=t("dd69");P["c"].components=Object(n["a"])({App:O},P["c"].components),P["d"].dispatch("init").then((function(){return new P["a"](P["c"])}))},"9d1c":function(e,o,t){}});