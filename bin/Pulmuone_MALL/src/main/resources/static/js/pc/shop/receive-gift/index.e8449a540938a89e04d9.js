(function(e){function t(t){for(var r,u,a=t[0],i=t[1],l=t[2],f=0,p=[];f<a.length;f++)u=a[f],Object.prototype.hasOwnProperty.call(o,u)&&o[u]&&p.push(o[u][0]),o[u]=0;for(r in i)Object.prototype.hasOwnProperty.call(i,r)&&(e[r]=i[r]);s&&s(t);while(p.length)p.shift()();return c.push.apply(c,l||[]),n()}function n(){for(var e,t=0;t<c.length;t++){for(var n=c[t],r=!0,a=1;a<n.length;a++){var i=n[a];0!==o[i]&&(r=!1)}r&&(c.splice(t--,1),e=u(u.s=n[0]))}return e}var r={},o={"pc/shop/receive-gift/index":0},c=[];function u(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,u),n.l=!0,n.exports}u.m=e,u.c=r,u.d=function(e,t,n){u.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},u.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},u.t=function(e,t){if(1&t&&(e=u(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(u.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)u.d(n,r,function(t){return e[t]}.bind(null,r));return n},u.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return u.d(t,"a",t),t},u.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},u.p="/";var a=window["webpackJsonp"]=window["webpackJsonp"]||[],i=a.push.bind(a);a.push=t,a=a.slice();for(var l=0;l<a.length;l++)t(a[l]);var s=i;c.push([179,"chunk-commons"]),n()})({179:function(e,t,n){e.exports=n("b840")},b840:function(e,t,n){"use strict";n.r(t);var r=n("5530"),o=(n("e260"),n("e6cf"),n("cca6"),n("a79d"),function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("fb-alert",{attrs:{open:e.alert.open,message:e.alert.message},on:{"close-alert":function(t){return e.closeAlert(t)}}})}),c=[],u=(n("96cf"),n("1da1")),a=n("9d0b"),i={name:"receive-gift",components:{fbAlert:a["a"]},mounted:function(){var e=this;return Object(u["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$nextTick();case 2:e.openAlert(e.$FB_MESSAGES.SYSTEM.ALERT_329);case 3:case"end":return t.stop()}}),t)})))()},methods:{closeAlert:function(){window.location.href=this.$APP_CONFIG.PAGE_URL.ROOT}}},l=i,s=n("2877"),f=Object(s["a"])(l,o,c,!1,null,null,null),p=f.exports,d=n("8f7d");d["c"].components=Object(r["a"])({App:p},d["c"].components),d["d"].dispatch("init").then((function(){return new d["a"](d["c"])}))}});