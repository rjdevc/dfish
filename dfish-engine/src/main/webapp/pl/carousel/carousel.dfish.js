/* carousel
 * 幻灯片

{ "type": "carousel", "id": "f_carousel0", "width": "470", height: 540, "thumbwidth": 80, "thumbheight": 60, "bigwidth": 470, "bigheight": 470,
  "value": [
    { "thumbnail": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "text": "CBA两老板入财富榜500强 新疆冠军巨鳄令姚明望尘莫及", "href": "javascript:alert(this.id)" },
    { "thumbnail": "http://tp2.sinaimg.cn/2214257545/180/5634974717/0",  "url": "http://tp2.sinaimg.cn/2214257545/180/5634974717/0",  "text": "有钱任性！新疆队老板送30辆进口豪车 西热兴奋晒图", "href": "#1" },
    { "thumbnail": "http://tp3.sinaimg.cn/2622821682/180/5662155841/1",  "url": "http://tp3.sinaimg.cn/2622821682/180/5662155841/1",  "text": "男篮双国家队集训名单：小丁与阿联各领衔红蓝队",    "href": "#2" },
    { "thumbnail": "http://tp4.sinaimg.cn/2495996991/180/5638239663/1",  "url": "http://tp4.sinaimg.cn/2495996991/180/5638239663/1",  "text": "上港被绝杀博阿斯最开心 疯狂想碰苏宁终得偿所愿",   "href": "#3" },
    { "thumbnail": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text": "亚冠16强对阵：淘汰赛首迎中国德比 中日霸主大对决", "href": "#4" }
  ]
},
{ "type": "carousel", "id": "f_carousel1", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "face": "1",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0" }
  ]
},
{ "type": "carousel", "id": "f_carousel2", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "face": "2",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "href": "javascript:alert(this.id)" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt",  "href": "#1" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "href": "#4" }
  ]
},
{ "type": "carousel", "id": "f_carousel3", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "face": "3",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0", "text": "CBA两老板入财富榜500强 新疆冠军巨鳄令姚明望尘莫及", "href": "javascript:alert(this.id)" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt", "text": "有钱任性！新疆队老板送30辆进口豪车 西热兴奋晒图", "href": "#1" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text": "男篮双国家队集训名单：小丁与阿联各领衔红蓝队", "href": "#4" }
  ]
}

{ "type": "carousel", "id": "f_carousel3", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "face": "4",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0", "text": "CBA两老板入财富榜500强 新疆冠军巨鳄令姚明望尘莫及", "href": "javascript:alert(this.id)" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt", "text": "有钱任性！新疆队老板送30辆进口豪车 西热兴奋晒图", "href": "#1" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text": "男篮双国家队集训名单：小丁与阿联各领衔红蓝队", "href": "#4" }
  ]
}

*/

var
$ = require( 'dfish' ),
W = require( 'widget' ),
_cssLoaded = null,
cssEvent = new $.Event(),
cssLoad = function(a, b) {
	_cssLoaded ? a.call(b) : cssEvent.addEventOnce('load', a, b);
};

require.css( './carousel.css', function() {_cssLoaded = true; cssEvent.fireEvent('load');} );

define.widget( 'carousel', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		if ( typeof x.value === 'string' )
			x.value = $.jsonParse( x.value );
		for ( var i = 0, t = [], f = [], g = $.abbr + '.all["' + this.id + '"]', s, v = x.value, l = v && v.length; i < l; i ++ ) {
			t.push( { icon: v[ i ].thumbnail || v[ i ].url, text: i + 1, width: x.thumbwidth, height: x.thumbheight, target: this.id + 'i' + i, focus: i === 0, on: { mouseover: g + '.pause(this)' } } );
			s = '<img class=_big onload=' + g + '.imgLoad(this) src=' + (v[ i ].url || v[ i ].thumbnail) + ' width="' + (x.bigwidth || '100%') + '">';
			if ( v[ i ].text )
				s += '<b class=_b></b><span class="_t f-omit" title="' + $.strQuot(v[ i ].text) + '">' + v[ i ].text + '</span>';
			if ( v[ i ].href )
				s = '<a ' + (v[ i ].href.indexOf( 'javascript:' ) === 0 ? ' onclick=' + g + '.click(' + i + ')' : 'href=' + v[ i ].href + ' target=_blank') + '>' + s + '</a>';
			f.push( { type: 'html', cls: 'w-carousel-big', id: this.id + 'i' + i, width: '*', height: '*', text: s } );
		}
		this.tab = this.add( { type: 'buttonbar', cls: 'w-carousel-bbr', width: '*', height: -1, pub: { name: this.id + 'name', cls: 'w-carousel-btn', focusable: true }, nodes: t, on: { mouseout: g + '.play()' } } );
		this.fra = this.add( { type: 'frame', width: '*', height: x.bigheight || '*', animate: x.animate, dft: this.id + 'i0', nodes: f, on: { mouseover: g + '.pause()', mouseout: g + '.play()' } } );
		if (this.tab.length <= 1) this.className += ' z-one';
		if (x.arrow) this.className += ' z-arrow';
		this.className += ' carousel-' + (x.face || 0);
	},
	Extend: 'vert',
	Listener: {
		body: {
			ready: function() { cssLoad(this.fixText, this); this.fixImg(); this.play();},
			resize: function() { cssLoad(this.fixText, this); this.fixImg();}
		}
	},
	Prototype: {
		className: 'w-carousel',
		index: 0,
		fixText: function() {
			if (this.x.face == 3 || this.x.face == 4) {
				var w = this.tab.$().offsetWidth;
				Q('.w-carousel-big ._t', this.$()).css({right: w + 15});
			}
		},
		fixImg: function() {
			var self = this;
			Q('._big', this.$()).each(function() {
				self.fixImgSize(this);
			});
		},
		imgLoad: function(a) {
			//a.isLoaded = true;
			//this.fixImgSize(a);
		},
		fixImgSize: function(a) {
			if (!this.x.bigwidth && !this.x.bigheight) {
				var b = $.widget(a), bw = b.innerWidth(), bh = b.innerHeight();
				if (bw && bh) {
					var gw = a.width, gh = a.height, gd = gw/gh, bd = bw/bh;
					if ((this.x.cover && gd < bd) || (!this.x.cover && gd > bd)) {
						a.width = bw; a.removeAttribute('height');
					} else {
						a.height = bh; a.removeAttribute('width');
					}
					if (!$.br.css3) {
						Q(a).css({marginLeft: -a.width / 2, marginTop: -a.height / 2});
					}
				}
			}
		},
		play: function() {
			var self = this;
			clearTimeout( this.timer );
			this.timer = setTimeout( function() {
				if ( self._disposed )
					return;
				var t = self.tab.getFocus();
				(t.next() || self.tab[0]).focus();
				self.play();
			}, (this.x.time || 3) * 1000 );
		},
		pause: function( a ) {
			clearTimeout( this.timer );
			a != null && a.focus();
		},
		click: function( a ) {
			this.formatJS( this.x.value[ a ].href );
		},
		prev: function() {
			var f = this.tab.getFocus();
			this.pause(f.prev() || f.parentNode.get(-1));
		},
		next: function() {
			var f = this.tab.getFocus();
			this.pause(f.next() || f.parentNode[0]);
		},
		html_nodes: function() {
			!this.innerHeight() && this.addClass('z-autoht');
			return this.fra.html() + this.tab.html() + '<div class="_prev f-i-prev" onclick="' + $.abbr + '.widget(this).prev()"></div><div class="_next f-i-next" onclick="' + $.abbr + '.widget(this).next()"></div>';
		},
		dispose: function() {
			clearTimeout( this.timer );
			W.prototype.dispose.call( this );
		}
	}
} );
