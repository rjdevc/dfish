/*!
 *  widget
 *	@version	3.0
 *	@author		cmy
 *  @modified	2016-03-29
 */

var U, N = null, F = false, T = true, O = {}, _STR = 'string', _FUN = 'function', _OBJ = 'object', _NUM = 'number', _BOL = 'boolean',
$   = require( 'dfish' ),
Q   = require( 'jquery' ),
Loc = require( 'loc' ),
br  = $.br,
ie  = br.ie,
ie7 = br.ie7,
cfg = $.x,
eve = $.abbr + '.e(this)',
evw = $.abbr + '.w(this)',
_dfopt	= cfg.default_option || {},
_number = $.number,
_slice  = Array.prototype.slice,
_putin  = { append: T, prepend: T, undefined: T },

// widget实例缓存
_all = {}, _viewCache = {}, _globalCache = $.globals,
// 引入dfish后会产生一个不可见的原始view，所有widget从这个view起始。调用 VM() 会返回这个view
_docView,
// @a -> htmlElement: 返回html元素对象所在的widget
// @a -> JSON: 参数为符合widget配置项的json对象，则创建这个widget
_widget = function( a ) {
	var b = typeof a === _STR ? a : a.id;
	if ( b && (b = _getWidgetById( b )) )
		return b;
	if ( a.nodeType ) { // htmlElement
		do {
			if ( a.id && ( b = _getWidgetById( a.id ) ) )
				return b;
		} while ( (a = a.parentNode) && a.nodeType === 1 );
	} else if ( a.isWidget ) { // widget
		return a;
	} else if ( a.type && Q.isPlainObject( a ) ) {
		return new (require( a.type ))( a );
	}
},
_getWidgetById = function( a ) {
	if ( a ) {
		if ( _all[ a ] )
			return _all[ a ];
		var b = a.indexOf( ':' );
		if ( b > 0 && (b = a.slice( 0, b + 1 )) )
			return _all[ b ];
	}
},
// 鼠标点击时记录位置
// 把mouseover和mouseout修为mouseenter和mouseleave的效果
_event_enter = {
	mouseover: function( a, e ) { return ! a.contains( e.fromElement ) },
	mouseout:  function( a, e ) { return ! a.contains( e.toElement ) }
},
_event_stop = {
	contextmenu: function( e ) {
		if ( !(cfg.debug && e.ctrlKey) ) $.stop( e );
	}
},
// 触发事件的入口，在html标签上显示为 onEvent="$.e(this)"  /@ a -> element, t -> type, e -> event
_widgetEvent = function( a, t, e ) {
	if ( t ) {
		e = new jQuery.Event( t, e );
	} else {
		e = window.event || arguments.callee.caller.arguments[ 0 ]; // _widgetEvent.caller: 解决firefox下没有window.event的问题
		t = e.type;
	}
	if ( ! _event_enter[ t ] || _event_enter[ t ]( a, e ) ) {
		e.elemId = a.id;
		(a = _widget( a )) ? a.trigger( e ) : $.stop( e );
	}
	_event_stop[ t ] && _event_stop[ t ]( e );
},
// _view(), _view( '/' ) 返回docView
// _view( '/abc' ) 根据路径返回view
// _view( '#g' )   根据globalId返回view
_view = function( a ) {
	if ( ! a || a === '/' )
		return _docView;
	if ( a.type_view )
		return a;
	if ( a.ownerView )
		return a.ownerView;
	if ( typeof a === _STR ) {
		if ( a.indexOf( 'javascript:' ) === 0 )
			a = Function( a ).call( this );
		return a.charAt( 0 ) === '/' ? _viewCache[ a ] : a.charAt( 0 ) === '#' ? _globalCache[ a ] : (a = $.dialog( a )) && a.contentView;
	} else {
		return _vmByElem( a );
	}
	return _docView;
},
_vmByElem = function ( o ) {
	var p = _widget( o );
	if( p ) {
		while ( p && ! p.ownerView )
			p = p.parentNode;
		return p ? p.ownerView : ((p = _widget( o ).$()) && _vmByElem( p.parentNode ));
	}
	return _docView;
},
_rangeEvents = function( a ) {
	for ( var i = 0, r = {}, j, k, v; i < arguments.length; i += 2 ) {
		k = arguments[ i ], r[ k ] = {}, v = [];
		for ( j = 1; j < i + 2; j += 2 )
			v.push.apply( v, arguments[ j ].split( ',' ) );
		for( j = 0; j < v.length; j ++ )
			r[ k ][ v[ j ] ] = T;
	}
	return r;
},
_range_events = _rangeEvents( 'all', 'mouseover,mouseout,mousedown,mouseup,mousemove,mousewheel,mouseenter,mouseleave,contextmenu,click,dblclick,drag,dragend,dragenter,dragleave,dragover,dragstart,drop,keydown,keypress,keyup,copy,cut,paste,scroll,select,selectstart,propertychange,paste,beforepaste,beforedeactivate,tap',
	'input', 'focus,blur,input', 'option', 'change' ),
// 生成html事件属性 / @a -> context, s -> 指定要有的事件
_html_on = function( a, s ) {
	var s = s || '', n;
	if ( a.isWidget ) {
		var h = a.Const.Listener, r = _range_events[ h && h.range ] || _range_events.all;
		if ( a.x.on ) {
			for ( var i in a.x.on )
				s = _s_html_on( s, h && h.body, i, r );
		}
		if ( h && (h = h.body) ) {
			for ( var i in h ) {
				if ( h[ i ] && (typeof h[ i ].prop === _FUN ? h[ i ].prop.call( a ) : h[ i ].prop) )
					s = _s_html_on( s, h, i, r );
			}
		}
	} else {
		i = a.length;
		while ( i -- ) s += ' on' + a[ i ] + '=' + eve;
	}
	return s;
},
// 用于leaf, row 这样的多层次子节点
_html_on_child = function( a, s ) {
	var s = _html_on( a, s ), r = _range_events[ a.Const.Listener && a.Const.Listener.range ] || _range_events.all,
		e = a.nodeIndex > -1 && a.rootNode && a.rootNode.Const.Child === a.type && a.rootNode.x.pub && a.rootNode.x.pub.on;
	if ( e ) {
		for ( var i in e )
			s = _s_html_on( s, a.Const.Listener && a.Const.Listener.body, i, r );
	}
	return s;
},
_s_html_on = function( s, h, i, r ) {
	for ( var p = h && h[ i ] && h[ i ].proxy, k = p ? p.split( ' ' ) : [ i ], j = 0; j < k.length; j ++ ) {
		if ( s.indexOf( 'on' + k[ j ] + '=' ) < 0 && r[ k[ j ] ] )
			s += ' on' + k[ j ] + '=' + eve;
	}
	return s;
},
_render = function( s, k ) {
	for ( var i = this.nodeIndex - 1, p = this.parentNode, l = p.length, c; i > -1; i -- )
		if ( (c = p[ i ]).$() ) return c.insertHTML( s, 'after' );
	for ( i = this.nodeIndex + (k || 1); i < l; i ++ )
		if ( (c = p[ i ]).$() ) return c.insertHTML( s, 'before' );
	p.insertHTML( s );
},
_jsf_cache = {},
// 解析并运行包含 "$属性名" 的js语法内容  /@a -> js, b -> arg array, c -> value array, d -> callback?
_jsformat = function( a, b, c , d ) {
	if ( ! _jsf_cache[ a ] ) {
		var e = b.concat(), f = c.concat();
		if ( a.indexOf( '$' ) > -1 ) {
			var h = /\$(\w+)/g, k;
			while ( k = h.exec( a ) )
				e.push( k[ 0 ] ), f.push( k[ 1 ] );
		}
		_jsf_cache[ a ] = { hdl: Function( e.join( ',' ), a.indexOf( 'function' ) === 0 ? 'return(' + a + ').call(this)' : a ), fld: f };
	}
	for ( var i = c.length, x = this.x, g = _jsf_cache[ a ].fld, l = g.length, v; i < l; i ++ ) {
		v = (typeof x.data === _OBJ && (g[ i ] in x.data) ? x.data : x)[ g[ i ] ];
		c.push( d ? d( v ) : v );
	}
	return _jsf_cache[ a ].hdl.apply( this, c );
},
// 取得一个表单的值  /@ a -> input el, b -> json mode?
_f_val = function( a, b, r ) {
	var d = a.name, v = a.value;
	if ( ! d )
		return N;
	switch ( a.type ) {
		case 'checkbox':
			if ( ! a.value  || (! a.checked && !(a.indeterminate && a.getAttribute( 'w-partialsubmit' ))) )
				return N;
		break;
		case 'radio':
			if ( ! a.value || ! a.checked )
				return N;
			d = a.getAttribute( 'w-name' ) || d;
		break;
		case 'select-one':
			if ( a.selectedIndex < 0 )
				return N;
		break;
		case 'textarea':
			// ie7-8的换行是\r\n，如果在ie7-8编辑内容后，在谷歌上浏览，换行会变成两个，所以替换掉\r
			ie && (v = v.replace( /\\r\\n/g, '\n' ));
	}
	v && (v = $.strTrim( v ));
	if ( b ) {
		if ( d in r ) {
			if ( ! $.isArray( r[ d ] ) )
				r[ d ] = [ r[ d ] ];
			r[ d ].push( v );
		} else
			r[ d ] = v;
	} else
		r.push( d + '=' + $.urlEncode( v ) );
},
/*	beforesend 发送请求之前调用
 *	error      请求出错时调用。传入XMLHttpRequest对象，描述错误类型的字符串以及一个异常对象（如果有的话）
 *	success    请求之后调用。传入返回后的数据，以及包含成功代码的字符串。
 *	complete   请求完成之后调用这个函数，无论成功或失败。传入XMLHttpRequest对象，以及一个包含成功或错误代码的字符串。
 */// @x -> cmd object, a -> url args, t -> post data
_ajaxCmd = function( x, a, t ) {
	var u = x.src, d;
	if ( a )
		u = $.urlFormat( u, a );
	if ( t && x.data ) {
		if ( typeof x.data === _OBJ ) {
			if ( $.isArray( x.data ) ) {
				for ( var i = 0, l = x.data.length; i < l; i ++ )
					t += '&' + x.data[ i ].name + '=' + $.urlEncode( x.data[ i ].value );
			} else {
				for ( var i in x.data )
					t += '&' + i + '=' + $.urlEncode( x.data[ i ] );
			}
		} else
			t += '&' + x.data;
	}
	if ( x.loading )
		d = this.cmd( typeof x.loading === _OBJ ? $.extend( { type: 'loading' }, x.loading ) : { type: 'loading', text: x.loading === T ? N : x.loading } );
	this.trigger( 'lock' );
	var g = '$value,$ajax';
	_view( this ).ajax( { src: u, context: this, sync: x.sync, data: t || x.data, headers: x.headers,
		error: x.error && $.fncreate( x.error, this, '$ajax' ), beforesend: x.beforesend && $.fncreate( x.beforesend, this, '$ajax' ), 
		success: function( v, a ) {
			d && (d.close(), d = N);
			if ( ! this._disposed && x.filter )
				v = $.fnapply( x.filter, this, [ v, a ], g );
			if ( ! this._disposed && x.success )
				$.fnapply( x.success, this, [ v, a ], g );
			if ( ! this._disposed )
				this.exec( v, N, x.transfer );
		}, complete: function( v, a ) {
			d && d.close();
			if ( ! this._disposed && x.complete )
				$.fnapply( x.complete, this, [ v, a ], g );
			if ( ! this._disposed )
				this.trigger( 'unlock' );
		}
	} );
},
// @a -> widget, b -> frame focus?, c -> pos?
_scrollIntoView = function( a, b, c ) {
	if ( a && a.$() ) {
		b && (Frame.focus( a ), Toggle.focus( a ));
		var s = Scroll.get( a );
		if ( s ) {
			s.scrollTop( a, c === U ? 'middle' : c );
			//s.scrollLeft( a, 'center' );
		}
	}
},
_cmd = function( x ) {
	var i = 0, e = _view.call( this, x.path ), f = x.target ? e.find( x.target ) : x.path ? e : this, l = x.nodes && x.nodes.length;
	for ( ; i < l; i ++ )
		f && f.exec( x.nodes[ i ] );
},
_cmdHooks = {
	'cmd': function( x ) {
		if ( x.delay != N ) {
			var self = this;
			setTimeout( function() { _cmd.call( self, x ) }, x.delay * 1000 );
		} else
			_cmd.call( this, x );
	},
	'js': function( x, a ) {
		var c = '$0,$1,$2,$3,$4,$5,$6,$7,$8,$9';
		if ( a && a.length > 10 ) {
			for ( var i = 10; i < a.length; i ++ )
				c += ',$' + i;
		}
		return Function( c, x.text ).apply( this, a || [] );
	},
	'ajax': function( x, a ) {
		x.download ? $.download( x.src, x.data ) : _ajaxCmd.call( this, x, a );
	},
	'submit': function( x, a ) {
		var v = _view( this ), r = x.validaterange, f = v.getFormList( r || x.range ), g = r && r != x.range ? v.getFormList( x.range ) : f;
		if ( v.valid( x.validate, f, x.validateeffect ) ) {
			var d = v.getPostData( g, !! x.download );
			x.download ? $.download( x.src, d ) : _ajaxCmd.call( this, x, a, d );
		}
	},
	'menu': function( x ) {
		return new Menu( x, this ).show();
	},
	'dialog': function( x, a ) {
		if ( typeof x.src === _STR )
			x.src = $.urlFormat( x.src, a );
		x.title && (x.title = $.strFormat( x.title, a ));
		return new Dialog( x, this ).show();
	},
	'tip': function( x, a ) {
		if ( typeof x.src === _STR )
			x.src = $.urlFormat( x.src, a );
		if ( x.hide )
			_inster( 'tip', N );
		else
			return new Tip( x, this ).show();
	},
	'loading': function( x, a ) {
		if ( x.hide ) {
			_inster( 'loading', N );
		} else {
			return new Loading( x, this ).show();
		}
	},
	'alert': function( x, a ) {
		x.args = a;
		return new Alert( x, this ).show();
	},
	'confirm':  function( x, a ) {
		x.args = a;
		return new Confirm( x, this ).show();
	}
};
$.arrEach( 'before after prepend append replace remove'.split(' '), function( v, i ) {
	_cmdHooks[ v ] = function( a ) {
		var d = a.target || (i > 3 && a.node && a.node.id), e;
		if ( d ) {
			if ( a.section === 'cmd' ) {
				a.node && ((_view( this ).x.commands || (_view( this ).x.commands = {}))[ d ] = i === 5 ? N : a.node);
			} else if ( a.section === 'template' ) {
				a.node && ((_view( this ).x.templates || (_view( this ).x.templates = {}))[ d ] = i === 5 ? N : a.node);
			} else if ( e = _view( this ).find( d ) )
				e[ v ]( a.node || a.nodes );
		}
	}
} );
var
_setView = function( a ) {
	if ( a && ! this.ownerView ) {
		this.ownerView = a;
		_regIdName.call( this, a );
		var i = this.length;
		while ( i -- ) _setView.call( this[ i ], a );
		if ( this.discNodes ) {
			for ( i in this.discNodes )
				_setView.call( this.discNodes[ i ], a );
		}
	}
},
_regIdName = function( a ) {
	if ( this.x.id )   a.widgets[ this.x.id ] = this;
	if ( this.x.name ) $.jsonArray( this, a.names, this.x.name );
},
_regWidget = function( x, p, n ) {
	p && p.addNode( this, n );
	x.gid && (_globalCache[ x.gid ] = this);
	_all[ $.uid( this ) ] = this;
	this.init_x( x );
},
/* `widget`  最基础的widget类，所有widget都继承它 */
W = define( 'widget', function() {
	return $.createClass( {
	// @x -> 配置参数, p -> parentNode, n -> number/name
	Const: function( x, p, n ) {
		_regWidget.call( this, x, p, n );
		p && _setView.call( this, _view( p ) );
		this.init_nodes( x );
		this._instanced = T;
	},
	Helper : {
		all: _all,
		vm:  _view,
		w: _widget,
		e: _widgetEvent,
		isCmd: function( a ) { return a && _cmdHooks[ a.type ] },
		jsformat: _jsformat,
		scrollIntoView: _scrollIntoView
	},
	Extend: [ $.Event, $.Node ],
	Prototype: {
		// 据此变量把widget实例和json对象区别开
		isWidget: T,
		// 生成html时使用的样式名
		className: '',
		// html标签名
		tagName: 'div',
		// 获取 DOM 元素
		$: function( a ) {
			return document.getElementById( a == N ? this.id : this.id + a );
		},
		// @private: 初始化配置参数
		init_x: function( x ) {
			this.x = x;
			var n;
			this.Const.Rooter && (n = this.rootNode) && this.nodeIndex !== -1 && (n = n.x.pub) && $.extendDeep( x, n );
			_dfopt[ this.type ] && $.extendDeep( x, _dfopt[ this.type ] );
		},
		// @private: 初始化子节点
		init_nodes: function( x ) {
			var c = this.x_nodes();
			if ( c ) {
				for ( var j = 0, l = c.length; j < l; j ++ )
					this.parse( c[ j ] );
			} else if ( c = this.x_node() )
				this.parse( c );
		},
		// 改变默认设置 @a -> key
		defaults: function( a ) {
			if ( typeof a === _STR ) {
				var d = this.defaultHooks || this.Const.Default, e = d && d[ a ];
				if ( typeof e === _FUN )
					e = e.call( this );
				return e;
			}
			this.defaultHooks = $.extend( a, this.Const.Default, this.defaultHooks );
		},
		x_node: function() {
			return this.x.node;
		},
		// @private: 返回子节点配置项的集合
		x_nodes: function() {
			return this.x.nodes;
		},
		// @private: 解析子节点配置项，获取需要的type
		x_type: function( t, n ) {
			return n === -1 ? t : this.Const.Child || t;
		},
		// @private: 解析并生成子节点
		parse: function( x, n ) {
			return new (require( this.x_type( x.type, n ) ))( x, this, n );
		},
		// 增加一个子节点 /@ a -> widget option, n -> nodeIndex?, g -> default widget option(附加的默认选项)?
		add: function( a, n, g ) {
			if ( a.isWidget ) {
				( a.type_view ? _setParent : _setView ).call( a, _view( this ) );
				g && a.attr( g );
				return this.addNode( a, n );
			}
			g && (a = $.extend( {}, a, g ));
			// 离散节点直接调用
			return this.parse( a, n );
		},
		// 读/写属性
		attr: function( a, b ) {
			if ( b !== U ) {
				var c = this.x[ a ];
				this.x[ a ] = b;
				this.attrSetter && this.attrSetter( a, b, c );
			} else if ( typeof a === _STR ) {
				var c = this.x[ a ];
				if ( c != N )
					return typeof c === _STR && c.indexOf( 'javascript:' ) === 0 ? Function( c ).call( this ) : c;
				return this.defaults( a );
			} else { /* typeof a === 'object' */
				$.merge( this.x, a );
				for ( b in a ) this.attr( b, a[ b ] );
			}
		},
		addClass: function( a, b ) {
			$.classAdd( this, a, b );
			this.$() && $.classAdd( this.$(), a, b );
			return this;
		},
		removeClass: function( a ) {
			$.classRemove( this, a );
			this.$() && $.classRemove( this.$(), a );
			return this;
		},
		hasClass: function( a ) {
			return $.classAny( this.$() || this, a );
		},
		css: function( a, b ) {
			$.css( this.$(), a, b );
			return this;
		},
		// 获取第几个子节点
		get: function( a ) {
			return a == N ? _slice.call( this ) : a < 0 ? this[ this.length + a ] : this[ a ];
		},
		// 获取所有子孙节点
		getAll: function() {
			for ( var i = 0, r = [], l = this.length; i < l; i ++ ) {
				r.push( this[ i ] );
				this[ i ].length && r.push.apply( r, this[ i ].getAll() );
			}
			return r;
		},
		// 查找符合条件的祖先元素
		// a -> string: 根据type查找
		// a -> json:   符合所有条件则返回
		// a -> function 返回true则返回
		closest: function( a ) {
			var p = this, b = typeof a;
			while ( p ) {
				if ( b === _STR ) {
					if ( p.type === a )
						return p;
				} else if ( b === _FUN ) {
					if ( a.call( p, this ) === T )
						return p;
				} else if ( b === _OBJ ) {
					var i, r = T;
					for ( i in a ) {
						if ( p.x[ i ] !== a[ i ] ) {
							r = F;
							break;
						}
					}
					if ( r )
						return p;
				}
				if ( p.parentNode ) {
					p = p.parentNode;
				} else if ( p.type !== 'view' && p.$() ) {
					if ( ! ( p = _widget( p.$().parentNode ) ) )
						return;
				} else
					return;
			}
		},
		click: function() {
			this.trigger( 'click' );
			return this;
		},
		// 获取下一个兄弟节点
		next: function() {
			 return this.parentNode && this.parentNode[ this.nodeIndex + 1 ];
		},
		// 获取上一个兄弟节点
		prev: function() {
			 return this.parentNode && this.parentNode[ this.nodeIndex - 1 ];
		},
		// 执行命令 /@a -> cmd id, arg1, arg2,...argN
		cmd: function( a ) {
			return this.exec( a, arguments.length > 1 ? _slice.call( arguments, 1 ) : N );
		},
		// 执行命令 /@a -> id[string/object], b -> args[array], c -> feature
		exec: function( a, b, c ) {
			var self = this._disposed ? this.ownerView : this;
			if ( ! self || self._disposed )
				return;
			if ( typeof a === _STR ) {
				var e = _view( self ).x.commands;
				if ( e && (e = e[ a ]) ) {
					a = $.jsonClone( e );
				} else if ( /^\{[\s\S]+\}$/.test( a ) ) {
					a = $.jsonParse( a );
				} else {
					$.alert( Loc.ps( Loc.debug.no_command, a, _view( self ).path ) ); // 没有找到命令
					return;
				}
			}
			if ( a ) {
				if ( c )
					a = $.merge( {}, a, c );
				if ( _cmdHooks[ a.type ] ) {
					_dfopt[ a.type] && $.extendDeep( a, _dfopt[ a.type] );
					$.point();
					return _cmdHooks[ a.type ].call( self, a, b );
				}
			}
		},
		// 是否包含某wg或元素 /@a -> elem|widget, b -> strict mode?
		contains: function( a, b ) {
			if ( a == N )
				return;
			if ( a.isWidget ) {
				if ( a.$() && this.$() && this.$().contains( a.$() ) )
					return T;
				if ( ! b ) {
					do { if ( a === this ) return T } while ( a = a.parentNode );
				}
			} else {
				if ( this.$() && this.$().contains( a ) )
					return T;
				return ! b && this.contains( _widget( a ) );
			}
		},
		// 存取临时变量
		data: function( a, b ) {
			var c = this.x.data || (this.x.data = {});
			return b === U ? c[ a ] : ( c[ a ] = b );
		},
		// 显示或隐藏
		display: function( a ) {
			var b = a == N || ( a.isWidget ? a.x.open : a ), o = this.$();
			if ( o.tagName === 'TR' ) {
				$.classAdd( o, 'f-none', ! b );
			} else {
				if ( this.__disp_none == N ) {
					this.__disp_none = o.currentStyle.display == 'none' ? T : F;
				}
				o.style.display = b ? (this.__disp_none === T ? 'block' : '') : 'none';
			}
			a.isWidget && (b ? o.removeAttribute( 'w-toggle' ) : o.setAttribute( 'w-toggle', a.id ));
		},
		isDisplay: function() {
			return this.$().currentStyle.display != 'none';
		},
		// 触发用户定义的事件 / @e -> event, @a -> [args]?, f -> func string?
		triggerHandler: function( e, a, f ) {
			var t = e.runType || e.type || e,
				f = f || (this.x.on && this.x.on[ t ]);
			if ( f ) {
				var g = [ 'event' ], r = [ e ];
				return typeof f === _STR ? _jsformat.call( this, f, g, r ) : ( typeof f === _FUN ? f : Function( g[ 0 ], f ) ).apply( this, arguments.length > 1 ? r.concat( a ) : r );
			}
		},
		// 触发系统事件
		triggerListener: function( e, a ) {
			if ( this._disposed )
				return;
			var b = this.Const.Listener,
				t = e.runType || e.type || e,
				h = b && b.body[ t ],
				g = arguments.length > 1 ? [ e ].concat( a ) : [ e ],
				f = h && (h.method || h);
				if ( typeof f === _FUN && f.apply( this, g ) === F )
					return F;
		},
		// 触发用户定义的事件和系统事件 / @e -> event, @a -> [data]
		// 优先返回用户事件的返回值，其次返回系统事件的值
		trigger: function( e, a ) {
			if ( this._disposed )
				return;
			var b = this.Const.Listener,
				c = this.Const.ListenerProxy,
				t = e.runType || e.type || e,
				h = b && b.body[ t ],
				g = arguments.length > 1 ? [ e ].concat( a ) : [ e ],
				f = h && (h.method || h),
				r, s;
			if ( b && b.block && b.block.call( this, e ) )
				return;
			if ( c = c && c[ t ] ) {
				for ( var j in c ) {
					var n = new Q.Event( e.type || e, e );
					n.runType = j;
					this.trigger( n, a );
				}
			}
			if ( _userPriority[ t ] ) { // 用户事件优先执行
				if ( ! ( h && h.block && h.block.call( this, e ) ) && (r = this.triggerHandler( e, a )) === F )
					return F;
				if ( this._disposed )
					return;
				if ( typeof f === _FUN )
					s = f.apply( this, g );
				this.fireEvent( e, a );
				if ( ! r )
					r = s;
			} else { // 系统事件优先
				if ( typeof f === _FUN && ( s = f.apply( this, g ) ) === F )
					return F;
				this.fireEvent( e, a );
				if ( ! ( h && h.block && h.block.call( this, e ) ) )
					r = this.triggerHandler( e, a );
				if ( ! r )
					r = s;
			}
			return r;
		},
		// 递归所有子节点触发事件
		triggerAll: function( e ) {
			this.trigger( e );
			for ( var i = 0, l = this.length; i < l; i ++ )
				this[ i ].triggerAll( e );
			for ( i in this.discNodes )
				! this.discNodes[ i ].isDialogWidget && this.discNodes[ i ].triggerAll( e ); // 弹窗不触发来自父节点的递归事件
		},
		// 设置事件  /@a -> event type, b -> fn
		setOn: function( a, b ) {
			$.jsonChain( b, this.x, 'on', a );
			if ( b && this.$() ) {
				var n = this.Const.Listener, d = n && n.body, e = n && n.tag ? this.$( n.tag ) : this.$(), f = (d && d[ a ] && d[ a ].proxy) || a;
				for ( var i = 0, g = f.split( ' ' ); i < g.length; i ++ ) {
					if ( e && ! e[ 'on' + g[ i ] ] ) e[ 'on' + g[ i ] ] = function( e ) { _widgetEvent( this ) };
				}
			}
			return this;
		},
		// 实现兄弟节点的tab效果 /@ a -> true/false
		tabFocus: function( a, b ) {
			if ( ! this._disposed ) {
				var p = this.rootNode || this.parentNode, f = p.focusNode;
				if ( a == N || a ) {
					f && f !== this && f.tabFocus( F );
					this.$() && $.classAdd( this.$(), b || 'z-on' );
					p.focusNode = this;
					this.focusOwner = p;
				} else {
					if ( f === this ) {
						this.$() && $.classRemove( this.$(), b || 'z-on' );
						delete p.focusNode; delete this.focusOwner;
					}
				}
			}
		},
		isNormal: function() {
			return ! this.x.status || this.x.status === 'normal';
		},
		isDisabled: function() {
			return this.x.status === 'disabled';
		},
		normal: function() {
			this.disable( F );
			return this;
		},
		disable: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'disabled' : '';
			$.classAdd( this.$(), 'z-ds', a );
			return this;
		},
		// 调整大小
		resize: function( w, h ) {
			if ( w != N && typeof w === _OBJ )
				h = w.height, w = w.width;
			if ( w != N )
				_w_size.width.call( this, w );
			if ( h != N )
				_w_size.height.call( this, h );
			if ( w != N || h != N ) {
				delete this._scales;
				for ( var i = 0, l = this.length; i < l; i ++ )
					_w_rsz_all.call( this[ i ] );
				this.trigger( 'resize' );
			}
		},
		// 替换为另一个widget /@ a -> widget option
		replace: function( a ) {
			if ( this._disposed )
				return;
			if ( a.type !== this.type && ( this.type === 'hidden' || a.type === 'hidden' ) )
				return $.msg( Loc.ps( Loc.debug.hidden_replace, this.x.name ) );
			var p = this.parentNode, i = this.nodeIndex, o = this.focusOwner;
			if ( ! a.isWidget ) {
				a.width == N && (a.width = this.x.width);
				a.height == N && (a.height = this.x.height);
			}
			var e = this.$();
			this.dispose();
			var g = p.add( a, i );
			o && (g.focusOwner = o, o.focusNode = g);
			g.render( e, 'replace' );
			this.removeElem();
			p.trigger( 'resize' );
			return g;
		},
		// 清空子节点
		empty: function( a ) {
			var i = this.length;
			while ( i -- )
				this[ i ].remove();
			if ( a !== T ) {
				for ( i in this.discNodes )
					this.discNodes[ i ].remove();
			}
		},
		// 生成页面可见的 DOM 元素  /@a -> target elem, b -> method[append|prepend|before|after|replace]
		render: function( a, b ) {
			// 没有父节点 则先加上父节点
			if ( ! this.parentNode ) {
				((a && _widget( a )) || _docView).add( this );
			}
			var p = this.parentNode, s = this.html();
			if ( this.$() && ! a )
				$.replace( this.$(), s );
			else
				a ? $[ b || 'append' ]( a || document.body, s ) : _render.call( this, s );
			this.trigger( 'render' );
			this.triggerAll( 'ready' );
			this.parentNode.trigger( 'nodechange' );
			return this;
		},
		// @dao 通过js增加子节点时会调用此方法 / a -> html|widget, b -> where(prepend|append|before|after)
		insertHTML: function( a, b ) {
			this.$() && $[ b || 'append' ]( this.$(), a.isWidget ? a.$() : a );
		},
		prop_cls: function() {
			var p = this.parentNode, c = (this.className || '') + (this.x.cls ? ' ' + this.x.cls.replace( /\./g, '' ) : '');
			if ( ! this.isNormal() )
				c += ' z-ds';
			if ( p && p.childCls )
				c += ' ' + (typeof p.childCls === _FUN ? p.childCls( this ) : p.childCls);
			return c;
		},
		html_prop: function() {
			var b = ' w-type="' + this.type + '" id=' + this.id,
				v, t = this.cssText || '', n = this.Const.Listener;
			if ( this.x.id )
				b += ' w-id="' + this.x.id + '"';
			if ( ( v = this.innerWidth() ) != N )
				t += 'width:' + v + 'px;';
			if ( ( v = this.innerHeight() ) != N )
				t += 'height:' + v + 'px;';
			if ( this.x.minwidth && (v = this.innerWidth( 'min' )) )
				t += 'min-width:' + v + 'px;';
			if ( this.x.maxwidth && (v = this.innerWidth( 'max' )) )
				t += 'max-width:' + v + 'px;';
			if ( this.x.minheight && (v = this.innerHeight( 'min' )) )
				t += 'min-height:' + v + 'px;';
			if ( this.x.maxheight && (v = this.innerHeight( 'max' )) )
				t += 'max-height:' + v + 'px;';
			if ( this.x.style )
				t += this.x.style;
			if ( t )
				b += ' style="' + t + '"';
			if ( ! (n && n.tag) && (this.x.on || n) )
				b += _html_on( this );
			if ( v = this.prop_cls() )
				b += ' class="' + v + '"';
			if ( this.x.align )
				b += ' align=' + this.x.align;
			if ( this.property )
				b += this.property;
			return b;
		},
		html_before: function() {
			return this.x.beforecontent ? this.html_after( this.x.beforecontent ) : '';
		},
		html_after: function( a ) {
			if ( a || (a = this.x.aftercontent) ) {
				if ( (typeof a === _FUN && (a = a.toString())) || a.indexOf( 'javascript:' ) === 0 )
					a = _jsformat.call( this, a, [], [] );
				if ( typeof a === _OBJ )
					a = this.add( a, -1 ).html();
			}
			return a || '';
		},
		html_nodes: function() {
			for ( var i = 0, l = this.length, s = []; i < l; i ++ )
				s.push( this[ i ].html() );
			return s.join( '' );
		},
		html: function() {
			return '<' + this.tagName + this.html_prop() + '>' + this.html_before() + this.html_nodes() + this.html_after() + '</' + this.tagName + '>';
		},
		removeElem: function( a ) {
			$.remove( this.$( a ) );
		},
		remove: function() {
			this.removeElem();
			var p = this.parentNode;
			this.dispose();
			if ( p ) {
				var s = p.type_horz ? 'width' : 'height';
				p[ 0 ] && (p.type_horz || p.type_vert) && _w_size[ s ].call( p[ 0 ], p[ 0 ].x[ s ] );
				p.trigger( 'nodechange' );
			}
		},
		dispose: function( d ) {
			if ( ! this._disposed ) {
				this.trigger( 'remove' );
				var i = this.length;
				while ( i -- )
					this[ i ].dispose( T );
				for ( i in this.discNodes ) {
					this.discNodes[ i ].dispose( T );
					delete this.discNodes[ i ]; delete this[ i ];
				}
				if ( this.focusOwner ) {
					delete this.focusOwner.focusNode; delete this.focusOwner;
				}
				if ( this.ownerView ) {
					if ( this.x.id )
						delete this.ownerView.widgets[ this.x.id ];
					if ( this.x.name && this.ownerView.names[ this.x.name ] )
						$.arrPop( this.ownerView.names[ this.x.name ], this );
				}
				if ( this.parentNode ) {
					delete this.parentNode._scales;
				}
				if ( ! d ) {
					this.removeEvent();
					this.removeNode();
				}
				if ( this.x.gid && _globalCache[ this.x.gid ] === this )
					delete _globalCache[ this.x.gid ];
				this._disposed = T;
				delete _all[ this.id ];
			}
		}
	}
	} );
} ),

_proto   = W.prototype,
_w_scale = {},
_w_size  = {},
_w_css   = {},
_w_mix   = {},
_w_lay   = {},
// 兄弟节点是否需要调整大小
_w_bro   = { 'width': 'type_horz', 'height': 'type_vert' },
_w_rsz_all = function() {
	_w_css.width.call( this );
	_w_css.height.call( this );
	var l = this.length;
	if ( l ) {
		delete this._scales;
		for ( var i = 0; i < l; i ++ )
			_w_rsz_all.call( this[ i ] );
	}
	for ( i in this.discNodes )
		_w_rsz_all.call( this.discNodes[ i ] );
	this.trigger( 'resize' );
},
_w_rsz_layout = function() {
	delete this._scales;
	for ( var i = 0, l = this.length; i < l; i ++ )
		_w_rsz_all.call( this[ i ] );	
},
// widget配置项里设置了style，又没有设置wmin和hmin，则由系统解析style，获取wmin和hmin的值
// 如果设置了cls，而cls里有 padding margin border 等，就需要人工计算并设置wmin和hmin
//@ _c -> cls, _1 -> style, _2 -> wmin, _3 = hmin
_size_fix = function( _c, _1, _2, _3 ) {
	var m, p, d, _2 = _2 == N ? 0 : _2, _3 = _3 == N ? 0 : _3, r = cfg.border_cls_regexp, o = cfg.border_cls_only;
	if ( _c && r && r.test( _c ) ) {
		if ( o && ($.idsAny( _c, o.top, ' ' ) || $.idsAny( _c, o.bottom, ' ' )) )
			_3 = 1;
		else if( o && ($.idsAny( _c, o.left, ' ' ) || $.idsAny( _c, o.right, ' ' )) )
			_2 = 1;
		else
			_2 = _3 = 2;
	}
	if ( _1 && ( _1.indexOf( 'margin' ) > -1 || _1.indexOf( 'padding' ) > -1 || _1.indexOf( 'border' ) > -1 ) ) {
		for ( var i = 0, c = _1.split( ';' ), e, f, k; i < c.length; i ++ ) {
			e = $.strTrim( c[ i ] ).toLowerCase().split( /\s*:\s*/ );
			if ( e.length !== 2 ) continue;
			k = e[ 0 ].indexOf( 'margin' ) === 0;
			if ( k || e[ 0 ].indexOf( 'padding' ) === 0 ) {
				if ( ! m ) m = {};
				if ( ! p ) p = {};
				k = k ? m : p;
				if ( f = $.strFrom( e[ 0 ], '-' ) )
					k[ f ] = _number( e[ 1 ] );
				else {
					f = e[ 1 ].split( /\s* \s*/ );
					k.top    = _number( f[ 0 ] );
					k.right  = _number( f.length > 1 ? f[ 1 ] : f[ 0 ] );
					k.bottom = _number( f.length > 2 ? f[ 2 ] : f[ 0 ] );
					k.left   = _number( f.length > 3 ? f[ 3 ] : k.right );
				}
			} else if ( e[ 0 ].indexOf( 'border' ) === 0 ) {
				if ( ! d ) d = {};
				if ( e[ 0 ] === 'border' ) {
					var r = e[ 1 ].match( /\d+(?:px|pt:em)/ );
					if ( r ) d.top = d.right = d.bottom = d.left = _number( r[ 0 ] );
				} else if ( e[ 0 ] === 'border-width' ) {
					f = e[ 1 ].split( /\s* \s*/ );
					d.top    = _number( f[ 0 ] );
					d.right  = _number( f[ 1 ] || f[ 0 ] );
					d.bottom = _number( f[ 2 ] || f[ 0 ] );
					d.left   = _number( f[ 3 ] || f[ 1 ] || f[ 0 ] );
				} else if ( e[ 0 ].indexOf( 'width' ) > 0 ) {
					d[ e[ 0 ].split( '-' )[ 1 ] ] = _number( e[ 1 ] );
				} else {
					var r = e[ 1 ].match( /\d+(?:px|pt:em)/ );
					if ( r ) {
						if ( e[ 0 ] === 'border' )
							d.top = d.right = d.bottom = d.left = _number( r[ 0 ] );
						else
							d[ e[ 0 ].split( '-' )[ 1 ] ] = _number( r[ 0 ] );
					}
				}
			}
		}
		if ( d ) {
			var t = _3 ? ( d.top || 1 )    : ( d.top || 0 ),
				o = _3 ? ( d.bottom || 1 ) : ( d.bottom || 0 ),
				l = _2 ? ( d.left || 1 )   : ( d.left || 0 ),
				r = _2 ? ( d.right || 1 )  : ( d.right || 0 );
			_2 = l + r;
			_3 = t + o;
		}
		if ( m ) {
			if ( m.left )   _2 += m.left;
			if ( m.right )  _2 += m.right;
			if ( m.top )    _3 += m.top;
			if ( m.bottom ) _3 += m.bottom;
		}
		if ( p ) {
			if ( p.left )   _2 += p.left;
			if ( p.right )  _2 += p.right;
			if ( p.top )    _3 += p.top;
			if ( p.bottom ) _3 += p.bottom;
		}
	}
	return { wmin: _2, hmin: _3 };
},
_get_margin = {
	width: function( a ) {
		return _number( a.currentStyle.marginLeft ) + _number( a.currentStyle.marginRight );
	},
	height: function( a ) {
		return _number( a.currentStyle.marginTop ) + _number( a.currentStyle.marginBottom );
	}
};
$.arrEach( [ 'width', 'height' ], function( v, j ) {
	var c = v.charAt( 0 ),
		y = "__" + v,   // __width, __height
		z = c.toUpperCase() + v.slice( 1 ),
		iu = c + 'min', 	// wmin, hmin
		nv = 'min' + v,
		xv = 'max' + v,
		rv = '__runtime_' + v,
		sz = 'scale' + z,	// scaleWidth, scaleHeight	为子元素分配大小
		oz = 'outer' + z,	// outerWidth, outerHeight  整体所占空间, 相当于 offset + margin
		iz = 'inner' + z;	// innerWidth, innerHeight	内部可用空间, 并为当前元素的style.width/style.height提供值

	// 实现 wg.width(), wg.height()
	// 返回为null时，去获取元素的offsetWidth/offsetHeight
	_proto[ v ] = function( a ) {
		if ( a == N ) {
			var b = this[ oz ]();
			return b == N && this.$() ? this.$()[ 'offset' + z ] + _get_margin[ v ]( this.$() ) : b;
		} else {
			delete this[ y ];
			v === 'width' ? this.resize( a ) : this.resize( N, a );
		}
	};
	_proto[ oz ] = function( m ) {
		// @fixme: 如果在构造函数Const里调用 .width() 和 .height(), 会因为兄弟节点还没实例化而计算出错，因此添加_instanced参数做控制，当兄弟节点全部实例化时才能调用.width 和 .height。
		// 		   这是暂时的解决办法。之后的优化目标是在Const里可以获取高宽。
		if ( ! this._instanced ) { return N; }
		if ( y in this && ! m ) {
			return this[ y ];
		}
		var r = this.parentNode[ sz ]( this, m );
		return this.parentNode._instanced && ! m ? (this[ y ] = r) : r;
	};
	_w_size[ v ] = function( a ) {
		if ( (v in this.x) || v != N )
			this.x[ v ] = a;
		var p = this.parentNode;
		if ( p ) {
			delete p._scales;
			if ( p[ _w_bro[ v ] ] ) {
				for ( var i = 0; i < p.length; i ++ ) {
					p[ i ] !== this && _w_rsz_all.call( p[ i ] );
				}
			}
		}
		_w_css[ v ].call( this );
	};
	_w_css[ v ] = function() {
		var t = this[ y ], a = this.attr( v );
		if ( t !== U || a != N ) {
			delete this[ y ];
			if ( t !== this[ oz ]() ) {
				t = this[ iz ]();
				if ( this.$() ) {
					this.$().style[ v ] = t == N ? '' : Math.max( t, 0 ) + 'px';
					this.x[ nv ] && (t = this[ iz ]( 'min' )) && $.css( this.$(), 'min-' + v, t );
					this.x[ xv ] && (t = this[ iz ]( 'max' )) && $.css( this.$(), 'max-' + v, t );
				}
			}
		}
	};
	//.innerWidth, .innerHeight
	_proto[ iz ] = function( m ) {
		var a = this[ oz ]( m );
		if ( a == N )
			return a;
		// 如果用户定义了样式且没有设置wmin和hmin，则使用系统预设的样式处理机制
		if ( ( this.x.cls || this.x.style ) && this.attr( 'wmin' ) == N && this.attr( 'hmin' ) == N ) {
			var f = _size_fix( this.x.cls, this.x.style );
			if ( f )
				return a - (f[ iu ] || 0);
		}
		return a - (this.attr( iu ) || 0);
	};
	// scaleWidth, scaleHeight 默认的分配给子元素高宽的方法 /@a -> widget|size, m -> max, min
	_proto[ sz ] = function( a, m ) {
		if ( a == N )
			return N;
		var b = a.isWidget ? a.attr( v ) : a;
		if ( b && ! isNaN( b ) )
			b = parseFloat( b );
		if ( typeof b === _NUM && ! m )
			return b < 0 ? N : b;
		var c = this[ iz ]();
		if ( c != N && typeof b === _STR && b.indexOf( '%' ) > 0 )
			c = Math.floor( c * parseFloat( b ) / 100 );
		return c == N ? N : a.isWidget ? $.scaleRange( c, { min: a.attr( nv ), max: a.attr( xv ) } ) : c;
	};
	// 根据子元素各自设置的比例，统一计算后进行高宽分配 /@a -> widget, m -> max, min
	_w_scale[ v ] = function( a, m ) {
		var b = a.attr( v ), c = this[ iz ](), s = this._scales;
		if ( typeof b === _NUM && b > -1 && ! m )
			return b;
		if ( ! s || m ) {
			if ( ! this.length )
				return N;
			for ( var i = 0, d, e, f, n, x, r = [], l = this.length; i < l; i ++ ) {
				d = this[ i ][ rv ] ? this[ i ][ v ]() : this[ i ].attr( v ), e = (d == N || d < 0) && ! this[ _w_bro[ v ] ] ? '*' : d, n = this[ i ].attr( nv ), x = this[ i ].attr( xv );
				r.push( { value: (m && (m === 'min' ? n : x)) || e, min: n, max: x } );
			}
			s = $.scale( c, r );
		}
		! m && (this._scales = s);
		r = s[ a.nodeIndex ];
		r == N && (r = a.defaults( v ));
		return r < 0 || r == '*' ? N : r;
	};
	// 当 -1 和 * 混用时，在dom渲染后，先计算 -1 部分的宽/高，再重新分配 * 的部分
	_w_lay[ v ] = function() {
		for ( var i = 0, h, f, d; i < this.length; i ++ ) {
			h = this[ i ].attr( v );
			if( h < 0 || h == N ) f = T;
			else if ( h && isNaN( h ) ) d = T;
			if ( f && d )
				break;
		}
		return f && d;
	};
	_w_mix[ v ] = function() {
		delete this._scales;
		for ( var i = 0, d; i < this.length; i ++ ) {
			d = this[ i ].x[ v ];
			if ( d < 0 || d == N ) {
				this[ i ][ rv ] = T;
				delete this[ i ].x[ v ];
			}
		}
		_w_rsz_layout.call( this );
	};
} );
// 实现方法： wg.append(), wg.prepend(), wg.before, wg.after()
$.arrEach( 'prepend append before after'.split(' '), function( v, j ) {
	_proto[ v ] = function( o ) {
		if ( typeof o === _STR )
			return this.insertHTML( o, v );
		a = o.isWidget ? [ o ] : $.arrMake( o );
		var q, i;
		if ( a[ 0 ].isWidget ) {
			if ( a[ 0 ] === this )
				return this;
			for ( q = a[ 0 ].parentNode, i = 0; i < a.length; i ++ )
				a[ 0 ].removeNode( T );
			q = q[ 0 ];
		}
		var i = j === 3 ? this.nodeIndex + 1 : j === 2 ? this.nodeIndex : j === 1 ? this.length : 0, d = this.nodeIndex > -1,
			p = j > 1 ? this.parentNode : this, l = a.length, k = 0, s = p.type_horz ? 'width' : 'height', r = [];
		for ( ; k < l; k ++ )
			r.push( p.add( a[ k ], d || j < 2 ? i + k : -1 ) );
		if ( this.$() ) {
			if ( a[ 0 ].isWidget && a[ 0 ].$() ) {
				for ( k = 0; k < l; k ++ )
					this.insertHTML( a[ k ], v );
			} else {
				for ( k = 0, o = ''; k < l; k ++ )
					o += r[ k ].html();
				this.insertHTML( o, v );
				for ( k = 0; k < l; k ++ )
					r[ k ].trigger( 'render' ), r[ k ].triggerAll( 'ready' );
			}
		}
		d && (((k = {})[ s ] = r[ 0 ].x[ s ]), r[ 0 ].resize( k ));
		d && q && (((k = {})[ s ] = q.x[ s ]), q.resize( k ));
		p.trigger( 'nodechange' );
		p.trigger( 'resize' );
		return p[ i ];
	}
} );
// scroll helper
var _html_resize_sensor = function( w, h ) {
	var e = '', s = '';
	if ( w != N ) {
		e += 'width:' + w + 'px;';
		s += 'width:200%;';
	}
	if ( h != N ) {
		e += 'height:' + h + 'px;';
		s += 'height:200%;';
	}
	return '<div id=' + this.id + 'rsz class=f-resize-sensor><div class=f-resize-sensor-expand><div class=f-resize-sensor-expand-core></div></div><div class=f-resize-sensor-shrink><div class=f-resize-sensor-shrink-core></div></div></div>'
},
_reset_resize_sensor = function() {
	var b = this.$( 'rsz' ).children, i = b.length,
		w = this.$( 'ovf' ).scrollWidth, h = this.$( 'cont' ).offsetHeight;
	if ( this._scr_wd !== w || this._scr_ht !== h ) {
		b[ 0 ].firstChild.style.width  = w + 'px';
		b[ 0 ].firstChild.style.height = h + 'px';
		while ( i -- ) {
			b[ i ].scrollTop = b[ i ].offsetHeight;
			b[ i ].scrollLeft = b[ i ].scrollWidth;
			if ( ! this._scr_ready )
				_bind_resize_sensor.call( this, b[ i ] );
		}
		if ( this._scr_wd === U )
			_show_scroll.call( this );
		this._scr_wd = w;
		this._scr_ht = h;
		return T;
	}
},
_bind_resize_sensor = function( a ) {
	var c = this._scr_check;
	setTimeout( function() { a.onscroll = c } );
},
_show_scroll = function() {
	if ( this._scr_usable && this.$() ) {
		var b = ie7 ? br.scroll : 0,
			d = this.$( 'cont' ).offsetHeight || this.$( 'gut' ).scrollHeight,
			g = this.$( 'ovf' ).scrollWidth - b;
		if ( d > 0 ) {
			var c = this.innerHeight() || this.$().offsetHeight,
				e = Math.min( 1, c / d );
			this.$( 'y' ).style.display  = e < 1 ? 'block' : '';
			this.$( 'ytr' ).style.height = Math.max( 14, c * e ) + 'px';
			this._scr_rateY = e < 1 && ((d - c) / (this.$( 'y' ).offsetHeight - this.$( 'ytr' ).offsetHeight ));
		}
		if ( g > 0 ) {
			var f = this.innerWidth() || this.$().offsetWidth,
				h = Math.min( 1, f / g );
			this.$( 'x' ).style.display  = h < 1 ? 'block' : '';
			this.$( 'xtr' ).style.width  = Math.max( 14, f * h ) + 'px';
			this._scr_rateX = h < 1 && ((g - f + b) / (this.$( 'x' ).offsetWidth - this.$( 'xtr' ).offsetWidth ));
		}
		this.trigger( 'scroll' );
	}
},
_html_scroll = function( s ) {
	this._scr_usable = T;
	var w = this.innerWidth(), h = this.innerHeight(), c = br.scroll;
	return '<div id=' + this.id + 'tank class=f-scroll-tank><div id=' + this.id + 'ovf class=f-scroll-overflow style="margin-bottom:-' + br.scroll + 'px;' +
		(w ? 'width:' + (w + c) + 'px;' : '' ) + (this.x.maxwidth ? 'max-width:' + (+this.x.maxwidth + c) + 'px;' : '') + (this.x.minwidth ? 'min-width:' + (+this.x.minwidth + c) + 'px;' : '') +
		(h ? 'height:' + (h + c) + 'px;' : '' ) + (this.x.maxheight ? 'max-height:' + (+this.x.maxheight + c) + 'px;' : '') + (this.x.minheight ? 'min-height:' + (+this.x.minheight + c) + 'px;' : '') +
		'" onscroll=' + eve + '><div id=' + this.id + 'gut' + (ie7 ? '' : ' class=f-rel') + '><div id=' + this.id + 'cont>' + (s || '') + '</div><div id=' + this.id +
		'rsz class=f-resize-sensor><div class=f-resize-sensor-expand><div class=f-resize-sensor-expand-core></div></div><div class=f-resize-sensor-shrink><div class=f-resize-sensor-shrink-core></div></div></div></div></div></div><div id=' +
		this.id + 'y class=f-scroll-y><div id=' + this.id + 'ytr class=f-scroll-y-track onmousedown=' + evw + '.scrollDragY(this,event)></div></div><div id=' +
		this.id + 'x class=f-scroll-x><div id=' + this.id + 'xtr class=f-scroll-x-track onmousedown=' + evw + '.scrollDragX(this,event)></div></div>';
},
/* `Scroll` */
Scroll = define.widget( 'scroll', {
	Helper: {
		// 获取某个 widget 所在的有 scroll 的面板 / a -> widget|elem
		get: function( a ) {
			return _widget( a ).closest( function() { return this.isScrollable && this.isScrollable() && this.innerHeight() > 0 } );
		}
	},
	Listener: {
		body: {
			ready: function() {
				// widget的dom可能会被业务重新生成，需要重置相关变量
				delete this._scr_ready; delete this._scr_wd; delete this._scr_ht;
			},
			mouseover: {
				prop: T,
				method: function() {
					if ( this._scr_usable ) {
						if ( ! this._scr_check ) {
							this._scr_check = $.proxy( this, function() {
								if ( ! this._disposed ) {
									if ( this.$() ) {
										_reset_resize_sensor.call( this ) && _show_scroll.call( this );
									} else {
										clearInterval( this._scr_timer );
										delete this._scr_check; delete this._scr_timer;
									}
								}
							} );
						}
						if ( ! this._scr_ready ) {
							_reset_resize_sensor.call( this );
							this._scr_ready = T;
						}
						if ( ! this._scr_timer )
							this._scr_timer = setInterval( this._scr_check, 777 );
					}
				}
			},
			mouseout: {
				prop: T,
				method: function() {
					if ( this._scr_timer ) {
						clearInterval( this._scr_timer );
						delete this._scr_timer;
					}
				}
			},
			scroll: function( e ) {
				if ( this._scr_rateY )
					this.$( 'ytr' ).style.top  = Math.min( this.$( 'ovf' ).scrollTop / this._scr_rateY, this.$( 'y' ).offsetHeight - _number(this.$( 'ytr' ).style.height) ) + 'px';
				if ( this._scr_rateX ) {
					this.$( 'xtr' ).style.left = ( this.$( 'ovf' ).scrollLeft / this._scr_rateX ) + 'px';
				}
			},
			resize: function() {
				if ( this.attr( 'scroll' ) && ! this._scr_usable && this.innerWidth() != N && this.innerHeight() != N )
					this.setScroll();
				if ( this._scr_usable ) {
					var d = this.id + 'ovf';
					if ( $( d ) ) {
						var w = this.innerWidth(), h = this.innerHeight();
						w != N && ($( d ).style.width  = (w + br.scroll) + 'px');
						h != N && ($( d ).style.height = (h + br.scroll) + 'px');
					}
					_show_scroll.call( this );
				}
			}
		}
	},
	Prototype: {
		prop_cls: function() {
			return _proto.prop_cls.call( this ) + (this.x.scroll ? ' f-scroll-wrap' : '');
		},
		// 让元素滚动到可见区域。支持下面两种调用方式 /e -> el|wg, y -> (top,bottom,middle,auto), x -> (left,right,center,auto), p -> ease?, q -> divide(整除数字，让滚动的距离是这个数字的整数倍), r -> callback
		scrollTo: function( e, y, x, p, q, r ) {
			var a = this.$( 'ovf' ), b = this.$( 'gut' ), c = $.bcr( a ), d = $.bcr( b ), f = e ? $.bcr( e ) : d, t, l;
			if ( y != N || e ) {
				if ( y == N || ! isNaN( y ) ) {
					t = _number( y );
					if ( e ) t = f.top - d.top - t;
				} else if ( y === 'top' ) {
					t = f.top - d.top;
				} else if ( y === 'bottom' ) {
					t = f.bottom - d.top - c.height;
				} else if ( y === 'middle' ) {
					t = f.top - d.top - ( ( c.height / 2 ) - ( f.height / 2 ) );
				} else {
					if ( f.top < c.top )
						t = a.scrollTop - c.top + f.top;
					else if ( f.bottom > c.bottom - br.scroll )
						t = a.scrollTop + f.bottom - c.bottom + br.scroll;
				}
			}
			if ( x != N || e ) {
				if ( x == N || ! isNaN( x ) ) {
					l = _number( x );
					if ( e ) l = f.left - d.left - l;
				} else if ( x === 'left' ) {
					l = f.left - d.left;
				} else if ( x === 'right' ) {
					l = f.right - d.left - c.width;
				} else if ( x === 'center' ) {
					l = f.left - d.left - ( ( c.width / 2 ) - ( f.width / 2 ) );
				} else {
					if ( f.left < c.left )
						l = a.scrollLeft - f.left + c.left;
					else if ( f.right > c.right - br.scroll )
						l = a.scrollLeft + f.right - c.right + br.scroll;
				}
			}
			if ( q != N ) {
				 t != N && (t = t - (t % q));
				 l != N && (l = l - (l % q));
			}
			if ( p ) {
				var g = a.scrollTop, h = a.scrollLeft;
				$.ease( function( i ) {
					if ( t != N ) a.scrollTop  = g + (t - g) * i;
					if ( l != N ) a.scrollLeft = h + (l - h) * i;
					i == 1 && r && r.call( this );
				}, p );
			} else {
				if ( t != N ) a.scrollTop  = t;
				if ( l != N ) a.scrollLeft = l;
				r && r.call( this );
			}
		},
		//@public  /@ e -> el, y -> (top,bottom,middle), p -> ease?, q -> divide, r -> callback
		scrollTop: function( e, y, p, q, r ) {
			if ( arguments.length === 0 )
				return this.$( 'ovf' ).scrollTop;
			if ( typeof e !== _OBJ )
				q = p, p = y, y = e, e = N;
			this.scrollTo( e, y, N, p, q );
		},
		//@public  /@ e -> el, x -> (left,right,center), p -> ease?, q -> divide, r -> callback
		scrollLeft: function( e, x, p, q, r ) {
			if ( arguments.length === 0 )
				return this.$( 'ovf' ).scrollLeft;
			if ( typeof e !== _OBJ )
				q = p, p = x, x = e, e = N;
			this.scrollTo( e, N, x, p, q );
		},
		setScroll: function() {
			if ( ! this._scr_usable && this.$() ) {
				var a = $.frag( this.$() );
				$.append( this.$(), _html_scroll.call( this ) );
				$.append( this.$( 'cont' ), a );
			}
		},
		checkScroll: function() {
			_reset_resize_sensor.call( this ) && _show_scroll.call( this );
		},
		isScrollable: function() {
			return this.attr( 'scroll' ) && ((this.innerWidth() != N || this.x.maxwidth) || (this.innerHeight() != N || this.x.maxheight));
		},
		isScrollBottom: function() {
			return this.$( 'ovf' ).scrollTop == this.$( 'ovf' ).scrollHeight - this.$( 'ovf' ).clientHeight;
		},
		scrollDragY: function( a, e ) {
			var b = this.id,
				t = $( b + 'ovf' ).scrollTop,
				y = e.clientY;
			$.stop( e );
			$( b + 'y' ).style.visibility = 'visible';
			$.moveup( function( e ) {
				if ( _all[ b ]._scr_rateY )
					$( b + 'ovf' ).scrollTop = t + (e.clientY - y) * _all[ b ]._scr_rateY;
			}, function() {
				$( b ) && ($( b + 'y' ).style.visibility = '');
			} );
		},
		scrollDragX: function( a, e ) {
			var b = this.id,
				t = $( b + 'ovf' ).scrollLeft,
				x = e.clientX;
			$.stop( e );
			$( b + 'x' ).style.visibility = 'visible';
			$.moveup( function( e ) {
				if ( _all[ b ]._scr_rateX )
					$( b + 'ovf' ).scrollLeft = t + (e.clientX - x) * _all[ b ]._scr_rateX;
			}, function() {
				$( b ) && ($( b + 'x' ).style.visibility = '');
			} );
		},
		//@implement  /@ a -> html|widget, b -> method(prepend,append,after,before)
		insertHTML: function( a, b ) {
			$[ b || 'append' ]( (_putin[ b ] ? (this.$( 'vln' ) || this.$( 'cont' )) : N) || this.$(), a.isWidget ? a.$() : a );
		},
		html: function() {
			this.width() == N && ! this.x.maxwidth && ! this.x.maxheight && $.classAdd( this, 'z-autosize' );
			return '<' + this.tagName + this.html_prop() + '>' + this.html_before() + ( this.isScrollable() ? _html_scroll.call( this, this.html_nodes() ) : this.html_nodes() ) + this.html_after() + '</' + this.tagName + '>';
		},
		dispose: function() {
			clearInterval( this._scr_timer );
			_proto.dispose.call( this );
		}
	}
} ),
_initView = function() {
	this.widgets = {};
	this.names   = {};
	this.views   = {};
	this.layout  = N;
},
_setParent = function( a ) {
	if ( a ) {
		this.parent = a;
		_regIdName.call( this, a );
		this.path   = ( a === _docView ? '' : a.path ) + '/' + ( this.x.id || this.id );
		_viewCache[ this.path ] = this;
	}
},
_userPriority = { 'click': T, 'close': T, 'valid': T },
// view的占据空间的widget，可见元素都隶属于此
ViewLayout = define.widget( 'view/layout', {} ),
// template实例集合
_templates = {},
/* `view` */
View = define.widget( 'view', {
	Const: function( x, p ) {
		//if ( cfg.debug )
		//	x.attr = 'onclick=' + $.abbr + '.debug(this)';
		if ( x.templates )
			$.merge( _templates, x.templates );
		_initView.call( this );
		_regWidget.apply( this, arguments );
		p && _setParent.call( this, _view( p ) );
		if ( this.x.node )
			this._loadEnd( this.x );
		this.dft_x = $.extend( {}, this.x );
		this._instanced = T;
	},
	Listener: {
		body: {
			ready: {
				block: function() { return this.layout },
				method: function() {
					if ( this.layout ) {
						this.triggerHandler( 'ready' );
						this.trigger( 'load' );
					} else {
						this.showLoading();
						if ( this.loading ) {
							this.addEventOnce( 'layoutload', _renderView );
						} else {
							var f = Frame.edge( this );
							if ( f && f.parentNode.getFocus() !== f )
								f.addEventOnce( 'view', this.init, this );
							else
								this.init();
						}
					}
				}
			},
			framefocus: function() {
				! this.layout && this.triggerListener( 'ready' );
			}
		}
	},
	Prototype: {
		className: 'w-view',
		type_view: T,
		// @a -> sync?, b -> fn?
		init: function( a, b ) {
			this.showLoading();
			this._load( a, function( x ) {
				_renderView.call( this );
				b && b.call( this, x );
			} );
		},
		init_x: function( x ) {
			if ( this.dft_x ) {
				$.extend( x, this.dft_x );
				x.on && this.dft_x.on && $.extend( x.on, this.dft_x.on );
			}
			delete this.__width; delete this.__height;
			this.x = x;
		},
		attrSetter: function( a, b ) {
			this.dft_x && (this.dft_x[ a ] = b);
		},
		// @a -> sync?, b -> fn?, c -> cache?
		_load: function( a, b, c ) {
			this.abort();
			this.loading = T;
			this.trigger( 'loading' );
			var u = this.attr( 'src' );
			u && (this.parent || this).ajax( {
				src: (u = $.urlLoc( cfg.path, u )), context: this, sync: a, cache: c,
				success: function( x ) {
					this._loadEnd( x );
					b && b.call( this, x );
				}
			} );
			c && this.addEvent( 'unload', function() { $.ajaxClean( u ) } );
		},
		// @x -> view json
		_loadEnd: function( x ) {
			this.loading = F;
			if ( ! this.x )
				return;
			if ( x.type === 'view' ) {
				if ( this.x !== x )
					this.init_x( x );
				if ( x.templates )
					$.merge( _templates, x.templates );
				if ( x.node )
					this.layout = new ViewLayout( { node: x.node }, this );
				this.loaded = T;
				this.trigger( 'layoutload' );
			} else
				this.cmd( x );
		},
		// a -> ajax settings
		ajax: function( a ) {
			this.x.base && (a.base = this.x.base);
			!a.context &&(a.context = this);
			return $.ajaxJSON( a );
		},
		abort: function() {
			$.ajaxAbort( this );
			this.loading = F;
		},
		// @a -> src, b -> sync?, c -> fn
		reload: function( a, b, c ) {
			this.trigger( 'unload' );
			if ( typeof b === _FUN )
				c = b, b = U;
			this.abort();
			this.empty();
			_initView.call( this );
			this.loaded = F;
			a && this.attr( 'src', a );
			if ( this.$() ) {
				this.attr( 'src' ) ? this.init( b, c ) : this._loadEnd( this.x );
			} else
				this._load( b, c );
		},
		// @a -> close?
		showLoading: function( a ) {
			a === F ? this.removeElem( 'loading' ) : $.html( this.$(), '<div class="w-view-loading" id=' + this.id + 'loading><i class=f-vi></i><cite class=_c>' + $.image( '%img%/loading-cir.gif' ) + ' <em class=_t>' + Loc.loading + '</em></cite></div>' );
		},
		// 根据ID获取wg /@a -> id
		find: function( a ) {
			var r;
			if ( typeof a === _STR ) {
				r = this.widgets[ a ] || this.views[ a ];
			} else {
				for ( var i = 0, c, r = []; i < a.length; i ++ )
					(c = this.widgets[ a[ i ] ] || this.views[ a[ i ] ]) && r.push( c );
			}
			return r;
		},
		// 获取表单 /@a -> name, b -> range?(elem|widget)
		f: function( a, b ) {
			return this.fAll( a, b )[ 0 ];
		},
		// 读/写表单值 /@a -> name, b -> value, c -> text
		fv: function( a, b, c ) {
			if ( a = this.f( a ) )
				b === U ? (b = a.val()) : a.val( b, c );
			return b;
		},
		// 获取范围内的所有表单 /@a -> name, b -> range?(elem|widget)
		fAll: function( a, b ) {
			if ( a === '*' || a == N ) {
				for ( var i = 0, q = this.getFormList( b, T ), l = q.length, d, e, f = []; i < l; i ++ )
					(d = _getWidgetById( q[ i ].id )) && d != e && f.push( e = d );
				return f;
			}
			var r = this.names[ a ], c = b && r && (typeof b === _STR ? this.find( b ) : b), f = [];
			if ( r ) {
				if ( r.length > 1 ) { // 表单如果移动了位置，view.names里面的顺序不会跟着变，所以当表单大于一个时，通过jquery来获取
					for ( var i = 0, d = Q( 'input[name="' + r[ 0 ].input_name() + '"]', c ? (c.isWidget ? c.$() : c) : this.$() ), l = d.length, e; i < l; i ++ )
						(e = d[ i ].id) && (e = _getWidgetById( e )) && f.push( e );
				} else {
					r[ 0 ] && (! c || c.contains( r[ 0 ].$() )) && f.push( r[ 0 ] );
				}
			}
			return f;
		},
		// 获取所有表单，返回一个jQuery集合 /@ a -> range(widgetID, 可选，如果 range 以 ! 开头，表示排除), b -> include disabled?
		getFormList: function( a, b ) {
			if ( a && a.jquery )
				return a;
			var r, f = ':input' + (b ? '' : ':not(:disabled)');
			if ( typeof a === _STR ) {
				var d = a.charAt( 0 ) === '!', e = [];
				if ( d )
					a = a.replace( /\!/g, '' );
				for ( var i = 0, a = a.split( ',' ), g; i < a.length; i ++ ) {
					if ( g = this.find( a[ i ] ) )
						e.push( '[id="' + g.id + '"] ' + f );
				}
				if ( e = e.join() )
					r = Q( this.$() ).find( d ? f + ':not(' + e + ')' : e );
			} else {
				r = Q( (a && $( a )) || this.$() ).find( f );
			}
			return r || [];
		},
		// 获取提交数据 { name: value, ... } /@ a -> range [widgetID, 可选，如果 range 以 ! 开头，表示排除], b -> json mode?
		getPostData: function( a, b ) {
			var f = this.getFormList( a ), r = b ? {} : [];
			for ( var i = 0, l = f.length; i < l; i ++ )
				_f_val( f[ i ], b, r );
			return b ? r : r.join( '&' );
		},
		// 判断表单是否更改  / @a -> range, b -> original?
		isModified: function( a, b ) {
			if ( this.$() ) {
				for ( var i = 0, c, q = this.getFormList( a ), l = q.length; i < l; i ++ ) {
					if ( (c = _getWidgetById( q[ i ].id )) && c.isModified && c.isModified( b ) )
						return c;
				}
			}
			return F;
		},
		// 保存表单所做的更改  / @a -> range
		saveModified: function( a ) {
			if ( this.$() ) {
				for ( var i = 0, c, q = this.getFormList( a ), l = q.length; i < l; i ++ ) {
					(c = _getWidgetById( q[ i ].id )) && c.saveModified && c.saveModified();
				}
			}
		},
		// @a -> range, b -> empty value
		resetForm: function( a, b ) {
			for ( var i = 0, q = this.getFormList( a ), l = q.length, c; i < l; i ++ ) {
				(c = _getWidgetById( q[ i ].id )) && c.reset && c.reset( b );
			}
		},
		// 表单验证。如果检测到错误，将返回一个包含错误信息的数组 /@ n -> validate name, g -> range
		getValidError: function( n, g ) {
			var e;
			if ( this.$() ) {
				for ( var i = 0, q = this.getFormList( g ), l = q.length, c, v; i < l; i ++ ) {
					if ( (c = _getWidgetById( q[ i ].id )) && (v = c.trigger( 'valid', [ n ] )) )
						(e || (e = {}))[ v.wid ] = v;
				}
			}
			return e;
		},
		// 表单验证。验证通过返回true /@ n -> validate name, g -> range
		// valid 用户事件返回值的处理: on: { valid: '***' }
		//   false -> 表示有错，并停止执行submit命令 (跳过引擎对当前widget的验证)
		//   "错误信息" -> 提示信息 (跳过引擎对当前widget的验证)
		//   null -> 没有错误，(继续引擎验证)
		valid: function( n, g ) {
			return this._valid( this.getValidError( n, g ) ); 
		},
		_valid: function( e ) {
			this._cleanValidError();
			if ( e ) {
				var f = f || cfg.validate_effect || 'red,tip', r = $.idsAny( f, 'red' ), a = $.idsAny( f, 'alert' ), h = $.idsAny( f, 'tip' ), m, n;
				for ( var k in e ) { 
					r && _all[ e[ k ].wid ].trigger( 'error' );
					n ? (m = T) : (n = e[ k ]);
				}
				if ( cfg.validate_handler ) {
					cfg.validate_handler( e );
				} else if ( n ) {
					// fixme: 给第一个验证失败的表单加上提示，需要快速的选出"第一个"，目前用jquery查找，效率一般
					// 暂时的小优化：设置一个优化变量m(表示有多个)。当只有一个验证失败的表单时，不用去jquery查找
					if ( m ) {
						for ( var i = 0, q = Q( '.z-err', this.$() ), l = q.length, g; i < l; i ++ )
							if ( (g = q[ i ].id) && e[ g ] ) { n = e[ g ]; break; }
					}
					var o = _all[ n.wid ];
					_scrollIntoView( o, T );
					a && o.trigger( 'error', [{ type: 'alert', text: n.text, id: $.ID_ALERT }] );
					h && o.trigger( 'error', [{ type: 'tip', text: n.text }] );
				}
			}
			return !(this._err_ns = e); 
		},
		_cleanValidError: function() {
			var e = this._err_ns, k, n;
			for ( k in e )
				(n = _all[ e[ k ].wid ]) && n.trigger( 'error', F );
			_inster( 'tip', N );
		},
		// @a -> target id, b -> T/F
		linkTarget: function( a, b ) {
			var c = a.isWidget ? [ a ] : this.find( a.split( ',' ) );
			for ( var i = 0; i < c.length; i ++ ) {
				if ( c[ i ].parentNode && c[ i ].parentNode.type_frame ) {
					b && c[ i ].parentNode.view( c[ i ] );
				} else {
					for ( var j = 0, r = this.fAll( '*', c[ i ] ), l = r.length; j < l; j ++ )
						r[ j ].disable( ! b );
				}
			}
		},
		append: function() {
			this.layout.append.apply( this.layout, arguments );
		},
		prepend: function() {
			this.layout.prepend.apply( this.layout, arguments );
		},
		html_nodes: function() {
			return this.layout ? this.layout.html() : '';
		},
		dispose: function() {
			this.trigger( 'unload' );
			this.abort();
			_proto.dispose.call( this );
			delete this.layout;
		}
	}
} ),
_renderView = function() {
	if ( this.x ) {
		this.showLoading( F );
		this.layout && this.render();
	}
},
/* `DocView` *
 *  引擎初始化后会创建一个顶级的DocView。这个view不是可见元素，但统领所有view和widget，类似于window.document
 *  只提供 find, cmd 等方法
 */
DocView = define.widget( 'docview', {
	Const: function() {
		_initView.call( this );
		this._wd = this.width();
		this._ht = this.height();
		var self = this, f;
		$.attach( window, 'resize', f = function( e, w, h ) {
			w = w || self.width(); h = h || self.height();
			if ( self._wd !== w || self._ht !== h ) {
				self.resize( self._wd = w, self._ht = h );
				Dialog.cleanPop();
			}
		} );
		if ( ie ) { // ie7监视缩放
			var dpi = screen.deviceXDPI;
			setInterval( function() {
				if ( screen.deviceXDPI != dpi ) {
					dpi = screen.deviceXDPI;
					f();
				}
			}, 999 );
		}
	},
	Extend: 'view',
	Prototype: {
		x: {},
		path: '/',
		loaded: T,
		$: function() {	return document.body },
		outerWidth: function() { return $.width() },
		outerHeight: function() { return $.height() },
		// 调整大小
		resize: function( w, h ) {
			if ( w != N || h != N ) {
				for ( var i = 0, l = this.length; i < l; i ++ )
					_w_rsz_all.call( this[ i ] );
				this.trigger( 'resize' );
			}
		}
	}
} ),
// 创建有且仅有一个的docView实例
_docView = new DocView(),

// horz/scale: 子元素被约束为：横向排列，高度占满，宽度按子元素设置的width属性分配
HorzScale = define.widget( 'horz/scale', {
	Prototype: {
		type_horz: T,
		scaleWidth: _w_scale.width
	}
} ),
/* `horz`   可滚动的横向布局面板，子元素如果没有设置宽度，则宽度默认为-1  */
Horz = define.widget( 'horz', {
	Const: function( x ) {
		x.nobr === F && (this.className += ' z-br');
		W.apply( this, arguments );
		if ( _w_lay.width.call( this ) )
			this.addEvent( 'resize', _w_mix.width ).addEvent( 'ready', _w_mix.width );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'hiddens', nodes: x.hiddens }, -1 );
	},
	Extend: [ 'scroll', 'horz/scale' ],
	Listener: {
		body: {
			nodechange: _w_rsz_layout
		}
	},
	Prototype: {
		className: 'w-horz',
		childCls: 'f-sub-horz',
		scaleWidth: _w_scale.width,
		html_nodes: function() {
			this.x.valign && (this.childCls += ' f-va-' + this.x.valign);
			// @compat: 兼容模式下，horz的没有高度的子节点，加上z-htmn样式(float:left)。如果不使用float，子节点内容不会自动撑开
			// @fixme: 当widget的高度在-1和固定高度之间转变时，z-htmn也应该跟着增加或删除
			!br.css3 && !this.innerHeight() && (!this.x.align || this.x.align == 'left') && (this.childCls += ' z-htmn');
			var s = _proto.html_nodes.call( this ) + (this._hiddens ? this._hiddens.html() : ''), v = this.attr( 'valign' );
			return v ? '<div id=' + this.id + 'vln class="f-inbl f-va-' + v + '">' + s + '</div><i class=f-vi-' + v + '></i>' : s;
		}
	}
} ),
// vert/scale: 子元素被约束为：纵向排列，宽度占满，高度按子元素设置的height属性分配
VertScale = define.widget( 'vert/scale', {
	Prototype: {
		type_vert: T,
		scaleHeight: _w_scale.height
	}
} ),
/* `vert`  可滚动的纵向布局面板，子元素如果没有设置高度，则高度默认为-1  */
Vert = define.widget( 'vert', {
	Const: function( x ) {
		W.apply( this, arguments );
		if ( _w_lay.height.call( this ) )
			this.addEvent( 'resize', _w_mix.height ).addEvent( 'ready', _w_mix.height );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'hiddens', nodes: x.hiddens }, -1 );
	},
	Extend: [ 'scroll', 'vert/scale' ],
	Listener: {
		body: {
			nodechange: _w_rsz_layout
		}
	},
	Prototype: {
		className: 'w-vert',
		childCls: 'f-sub-vert',
		scaleHeight: _w_scale.height,
		html_nodes: Horz.prototype.html_nodes
	}
} ),
/* `Frame`  子元素被约束为：高度宽度占满，只有一个可见  */
Frame = define.widget( 'frame', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		(this.focusNode = this.getFocus()) && (this.focusNode.focusOwner = this);
	},
	Helper: {
		// 获取a节点最接近 frame 的祖先元素
		edge: function( a ) {
			var n = _widget( a );
			do {
				if ( n.parentNode && n.parentNode.type_frame )
					return n;
			} while( (n = n.parentNode) && ! n.isDialogWidget );
		},
		// 让 frame 中的a可见 /@a -> widget|elem
		focus: function( a ) {
			var e = a && Frame.edge( a );
			if ( e ) {
				if ( e.parentNode.getFocus() != e ) {
					var n = $.get( '[w-target="' + e.x.id + '"]', e.parentNode.ownerView.$() );
					n && _widget( n ).focus();
				}
				Frame.focus( e.parentNode );
			}
		}
	},
	Prototype: {
		type_frame: T,
		className: 'w-frame',
		childCls: function( a ) {
			return 'f-sub-frame' + ( a === this.focusNode ? '-on' : '' );
		},
		getFocus: function() {
			return this.focusNode || (this.x.dft && this.ownerView.find( this.x.dft ));
		},
		// @a -> wgid
		// animate: scrollX(横向滚动),scrollY(纵向滚动),
		view: function( a ) {
			if ( this.$() ) {
				var o = this.getFocus(), c = 'f-sub-frame', d = c + '-on',
					n = a.isWidget ? a : this.ownerView.find( a );
				if ( n && n !== o ) {
					if ( this.x.animate && o ) {
						var d = n.nodeIndex > o.nodeIndex ? 'Left' : 'Right';
						$.classReplace( n.$(), c, d );
						$.animate( o.$(), 'fadeOut' + d, 100 );
						$.animate( n.$(), 'fadeIn' + d, 100, function() {
							o.$() && $.classReplace( o.$(), d, c );
						} );
					} else {
						o && $.classReplace( o.$(), d, c );
						$.classReplace( n.$(), c, d );
					}
					if ( o )
						delete o.focusOwner;
					this.focusNode = n;
					n.focusOwner = this;
					n.trigger( 'framefocus' );
				}
			}
		}
	}
} ),
/* `ewin` */
/*  embedwindow 有text就显示text，否则显示src  */
Ewin = define.widget( 'ewin', {
	Listener: {
		body: {
			ready: function() {
				this.x.text && this.text( this.x.text );
			}
		}
	},
	Prototype: {
		getContentWindow: function() {
			return this.$().contentWindow;
		},
		reload: function( a ) {
			this.getContentWindow().location.reload( a );
		},
		text: function( a ) {
			with ( this.getContentWindow().document ){ open(), write( a ), close() };
		},
		html: function() {
			return '<iframe' + this.html_prop() + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + ' w-abbr="' + $.abbr + '" src="' + (this.attr( 'src' ) || 'about:blank') + '" marginwidth=0 marginheight=0 frameborder=0 allowtransparency></iframe>';
		}
	}
} ),
/* `html`
 *  支持自定义标签: <d:wg></d:wg>
 *  内容为widget配置项，例: <d:wg>{type: "button", text: "click"}</d:wg>
 */
Html = define.widget( 'html', {
	Const: function( x, p ) {
		Scroll.apply( this, arguments );
		if ( this.x.thumbwidth ) {
			this.addEvent( 'ready',  this.thumb );
			this.addEvent( 'resize', this.thumb );
		}
		if ( ! x.valign && p && p.x.valign )
			this.defaults( { valign: p.x.valign } );
	},
	Extend: 'scroll',
	Prototype: {
		className: 'w-html',
		// @a -> text
		text: function( a ) {
			var o = this.$( 'vln' ) || this.$( 'cont' ) || this.$();
			o && (o.innerHTML = $.parseHTML( a, this ));
			this.trigger( 'resize' );
		},
		thumb: function() {
			this.x.thumbwidth && $.thumbnail( this.$(), this.scaleWidth( this.x.thumbwidth ) );
		},
		html_nodes: function() {
			var t = this.x.text == N ? '' : this.x.text, s = $.parseHTML( this.x.escape ? $.strEscape( t ) : t, this ), v = this.attr( 'valign' );
			if ( ! br.css3 ) {
				if ( ! s && this.parentNode && this.parentNode.type_horz && ! this.height() ) {
					// ie7,8 没有高度的html面板如果内容为空，即使有宽度也撑不开，所以补一个空格
					s = '&nbsp;';
				} else {
					// 解决 ie7,8 下的video标签播放问题
					s = s.replace( /<video([^>]+)>[\s\S]+?<\/video>/ig, function( $0, $1 ) {
						var w = $1.match( / width="(\d+)"/ )[ 1 ], h = $1.match( / height="(\d+)"/ )[ 1 ], u = $1.match( / src="([^"]+)"/ )[ 1 ];
						return '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" bgcolor="#000000" width="' + w + '" height="' + h + '">' +
							'<param name="quality" value="high"/><param name="allowFullScreen" value="true"/>' +
							'<param name="movie" value="' + cfg.lib + 'wg/upload/flvplayer.swf"/>' +
							'<param name="FlashVars" value="vcastr_file=' + u + '"/>' +
							'<embed src="' + cfg.lib + 'wg/upload/flvplayer.swf" allowfullscreen="true" flashvars="vcastr_file=' + u + '" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="' + w + '" height="' + h + '"></embed></object>';
					} );
				}
			}
			if ( v )
				s = '<i class=f-vi-' + v + '></i><div id=' + this.id + 'vln class="f-inbl f-va-' + v + '">' + s + '</div>';
			return s;
		}
	}
} ),
_splitSize = function( a, b ) {
	return a && a[ a.parentNode.type_horz ? 'width' : 'height' ]( b );
},
/* `split`  可拖动调整大小的分隔条 */
Split = define.widget( 'split', {
	Listener: {
		body: {
			ready: function() {
				$.classAdd( this.parentNode.$(), 'f-rel' );
				var w = this.width();
				if ( w <= 1 ) $.classAdd( this.$(), 'z-0' );
				ie7 && (this.$( 'bg' ).style.backgroundColor = this.$().currentStyle.backgroundColor);
			}
		}
	},
	Prototype: {
		className: 'w-split',
		// 拖动调整大小
		drag: function( a, e ) {
			var r = $.bcr( e.srcElement.parentNode ), o = this.isOpen(), p = this.prev(), n = this.next(), d = this.x.range.split( ',' ), j = _number( d[ 0 ] ), k = _number( d[ 1 ] ),
				h = this.parentNode.type_horz, t = this.x.target == 'next', cln = h ? 'clientX' : 'clientY', pos = h ? 'left' : 'top', x = e[ cln ], self = this, b, c, f, g, h,
				down = function() {
					h = $.db( '<div style="position:absolute;top:0;bottom:0;left:0;right:0;z-index:1"></div>' );
					b = $.db( '<div style="position:absolute;width:' + (h ? 5 : r.width) + 'px;height:' + (h ? r.height : 5) + 'px;left:' + r.left + 'px;top:' + r.top + 'px;background:#bbb;opacity:.6;z-index:1"></div>' );
				};
			//$.stop( e );
			down();
			$.moveup( function( e ) {
				if ( e[ cln ] !== x ) {
					f = p && _splitSize( p ), g = n && _splitSize( n );
				}
				if ( b ) {
					c = e[ cln ];
					f && (c = Math.max( c, x - f + j ));
					g && (c = Math.min( c, x + g - k ));
					b.style[ pos ] = (c + r[ pos ] - x) + 'px';
				}
			}, function( e ) {
				if ( b ) {
					$.remove( b ), $.remove( h );
					var j = f + c - x, k = g + x - c;
					if ( j > -1 && k > -1 ) {
						p && _splitSize( p, n && t ? '*' : j );
						n && _splitSize( n, p && ! t ? '*' : k );
						if ( j && k )
							self._size = t ? k : j;
						if ( self.$( 'i' ) && o != (j = self.isOpen()) )
							$.replace( self.$( 'i' ), self.html_icon( j ) );
					}
				}
			} );
		},
		// a -> bool/数字/百分比/*
		toggle: function( a ) {
			var o = this.isOpen(), n, v = a;
			if ( a == N ) {
				n = ! o;
				v = o ? 0 : this._size;
			} else if ( typeof a === _BOL ) {
				n = a;
				v = a ? this._size : 0;
			}
			this.major( v );
			this.minor( '*' );
			if ( n === U )
				n = this.isOpen();
			o != n && this.$( 'i' ) && $.replace( this.$( 'i' ), this.html_icon( n ) );
		},
		isOpen: function() {
			return this.major() > (this.x.range || '').split( ',' )[ this.x.target == 'next' ? 1 : 0 ];
		},
		major: function( a ) {
			return _splitSize( this[ this.x.target || 'prev' ](), a );
		},
		minor: function( a ) {
			return _splitSize( this[ this.x.target == 'next' ? 'prev' : 'next' ](), a );
		},
		html_icon: function( a ) {
			a = a == N ? this.isOpen() : a;
			return $.image( ( ! a && this.x.openicon) || this.x.icon, { id: this.id + 'i', cls: '_' + (this.x.target || 'prev'), click: evw + '.toggle()' } );
		},
		html_nodes: function() {
			var w = this.width(), h = this.height(), p = this.parentNode, z = p.type_horz,
				s = '<div id=' + this.id + 'bg style="position:absolute;background:inherit;height:' + (z ? '100%' : h + 'px') + ';width:' + (z ? w + 'px' : '100%') + '">';
			if ( this.x.range && (z || p.type_vert) && this.next() && this.prev() ) {
				s += '<div onmousedown=' + evw + '.drag(this,event) style="position:absolute;cursor:' + (z ? 'col' : 'row') + '-resize;height:' + (z || h >= 5 ? '100%' : '5px') + ';width:' +
					(! z || w >= 5 ? '100%' : '5px') + ';margin-' + (z ? 'left' : 'top') + ':' + ( (z ? w : h) < 5 ? ((z ? w : h) - 5) / 2 : 0 ) + 'px;z-index:1;"></div>';
				this._size = _number( this.x.range.split( ',' )[ 2 ] ) || this.major();
			}
			if ( this.x.icon )
				s += '<table cellspacing=0 cellpadding=0 border=0 width=100% height=100%><tr><td align=' + ( this.x.target === 'next' ? 'right' : 'left' ) + '>' + this.html_icon() + '</table>';
			return s + '</div>';
		}
	}
} ),
/* `buttonbar` */
Buttonbar = define.widget( 'buttonbar', {
	Const: function( x, p ) {
		var d = x.dir || 'h';
		if ( d !== 'h' || x.nobr !== F )
			this.className += ' z-dir' + d;
		W.apply( this, arguments );
		if ( ! x.valign && p && p.x.valign )
			this.defaults( { valign: p.x.valign } );
	},
	Extend: 'horz',
	Child: 'button',
	Default: { valign: 'middle'	},
	Listener: {
		body: {
			nodechange: function() {
				Horz.Listener.body.nodechange.apply( this, arguments );
				Q( '.w-button', this.$() ).removeClass( 'z-last z-first' ).first().addClass( 'z-first' ).end().last().addClass( 'z-last' );
				Q( '.w-button-split', this.$() ).next().addClass( 'z-first' ).end().prev().addClass( 'z-last' );
			}
		}
	},
	Prototype: {
		type_buttonbar: T,
		NODE_ROOT: T,
		className: 'w-buttonbar',
		childCls: '',
		x_type: function( t, n ) {
			return t === 'split' ? 'button/split' : (t || 'button');
		},
		draggable: function( a ) {
			for ( var i = 0, l = this.length; i < l; i ++ )
				$.draggable( this[ i ], a );
			return this;
		},
		droppable: function( a ) {
			$.droppable( this, a );
			for ( var i = 0, l = this.length; i < l; i ++ )
				$.droppable( this[ i ], a );
			return this;
		},
		getFocus: function() {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].isFocus() ) return this[ i ];
		},
		getLocked: function() {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].isLocked() ) return this[ i ];
		},
		scaleWidth: function() {
			return (this.x.dir === 'v' ? _proto.scaleWidth : _w_scale.width).apply( this, arguments );
		},
		scaleHeight: function() {
			return (this.x.dir === 'v' ? _w_scale.height : _proto.scaleHeight).apply( this, arguments );
		},
		html_nodes: function() {
			for ( var i = 0, l = this.length, s = [], v = this.attr( 'valign' ); i < l; i ++ ) {
				s.push( this[ i ].html() );
				if ( this.x.split && i < l - 1 && this[ i ].type !== 'button/split' && this[ i + 1 ].type !== 'button/split' ) {
					s.push( this.add( $.extend( { type: 'split' }, this.x.split ), i + 1 ).html() );
					i ++, l ++;
				}
			}
			s = s.join( '' );
			// ie7下如果既有滚动条又有垂直对齐，按钮会发生位置偏移
			var f = ie7 && this.isScrollable() ? '' : '<i class=f-vi-' + v + '></i>';
			return ( v ? f + (this.x.dir === 'v' ? '<div id=' + this.id + 'vln class="f-inbl f-va-' + v + '">' + s + '</div>' : s) : s ) + '<div class=w-buttonbar-line></div>';
		}
	}
} ),
/* `button` */
Button = define.widget( 'button', {
	Listener: {
		block: function( e ) {
			return e !== 'unlock' && e !== 'remove' && ! this.usa();
		},
		body: {
			ready: function() {
				this.x.target && this._ustag();
				this.x.maxwidth && this.fixsize();
			},
			mouseover: {
				prop: T,
				method: function( e ) {
					$.classAdd( $( e.elemId || this.id ), 'z-hv' );
					var m = _inster( 'menu' );
					if ( this.type === 'menu/button' ) {
						for ( var i = 0, p = this.parentNode; i < p.length; i ++ )
							p[ i ] === this ? p[ i ].show() : p[ i ].hide();
					} else if ( (this.x.hoverdrop && this.more) || (m && this.more && this.more !== m && this.more.type === 'menu' && m.parentNode.parentNode === this.parentNode) ) {
						this.drop();
					} else if ( m && ! this.more && m.parentNode.parentNode === this.parentNode )
						m.hide();
				}
			},
			mouseout: {
				prop: T,
				method: function( e ) {
					$.classRemove( $( e.elemId || this.id ), 'z-hv' );
				}
			},
			mousedown: {
				prop: T,
				method: function( e ) {
					$.classAdd( $( e.elemId || this.id ), 'z-dn' );
				}
			},
			mouseup: {
				prop: T,
				method: function( e ) {
					$.classRemove( $( e.elemId || this.id ), 'z-dn' );
				}
			},
			click: function( e ) {
				this.x.focusable && this.trigger( this.parentNode.x.focusmultiple && this.isFocus() ? 'blur' : 'focus' );
				if ( ! ( this.x.on && this.x.on.click ) )
					this.drop();
			},
			focus: function() { this._focus( T ) },
			blur: function() { this._focus( F ) },
			lock: function() {
				if ( ! this._locked ) {
					if ( this._dft_icon === U )
						this._dft_icon = this.x.icon || '';
					if ( this.$() ) {
						if ( this.x.icon )
							$.append( this.$( 'i' ), '<i class=_ld></i>' );
						else
							this.attr( 'icon', '%img%/loading-dot.gif' );
						$.classAdd( this.$(), 'z-lock' );
						$.append( this.$(), '<div class=f-locker id=' + this.id + 'lock></div>' );
					}
				}
				this._locked = T;
			},
			unlock: function() {
				this._locked = F;
				this.attr( 'icon', this._dft_icon );
				if ( this.$() ) {
					$.classRemove( this.$(), 'z-lock' );
					this.removeElem( 'lock' );
					Q( '.z-hv', this.$() ).add( this.$() ).removeClass( 'z-hv' );
				}
			}
		}
	},
	Default: { width: -1 },
	Rooter: 'buttonbar',
	Prototype: {
		className: 'w-button',
		_menu_snaptype: 'v',
		// @implement
		init_nodes: function( x ) {
			if ( x.more || x.nodes )
				this.more = this.add( x.more || { type: 'menu', nodes: x.nodes }, -1, { snap: this, snaptype: this._menu_snaptype, indent: 1, memory: T, line: true, hoverdrop: x.hoverdrop } );
			if ( this.more && x.on && x.on.click )
				this._combo = T;
		},
		// @implement
		insertHTML: function( a, b ) {
			this.$() && $[ _putin[ b ] ? 'after' : b ]( this.$(), a.isWidget ? a.$() : a );
		},
		usa: function() {
			return ! this.isDisabled() && ! this._locked;
		},
		root: function() {
			return this.parentNode.type_buttonbar && this.parentNode;
		},
		fixsize: function() {
			if ( this.x.maxwidth ) {
				var t = this.$( 't' );
				if ( t ) {
					var r, o;
					if ( (o = this.$( 'm' )) && (o = $.bcr( o )) && o.width ) {
						r = o.left;
					} else if ( (o = this.$()) && (o = $.bcr( o )) && o.width ) {
						r = o.right;
					}
					if ( r && (o = this.$( 't' )) ) {
						o.style.width = $.boxwd( o, r - $.bcr( o ).left ) + 'px';
					}
				}
			}
		},
		_ustag: function() {
			this.ownerView.linkTarget( this.x.target, this.isFocus() );
		},
		_focus: function( a ) {
			if ( this._disposed )
				return;
			var a = a == N || a, p = this.parentNode, d;
			if ( this.x.focusable && this.x.focus !== a && this.$() ) {
				$.classAdd( this.$(), 'z-on', a );
				if ( a ) {
					if ( ! p.x.focusmultiple ) {
						for ( var i = 0, d = this.x.name ? this.ownerView.names[ this.x.name ] : p; i < d.length; i ++ )
							if ( d[ i ] !== this && d[ i ].x.focusable && d[ i ].x.focus ) { d[ i ].trigger( 'blur' ); }
					}
					if ( (d = this.x.target) && (d = d.split(',')) ) {
						for ( var i = 0, e; i < d.length; i ++ )
							(e = this.ownerView.find( d[ i ] )) && e.parentNode && e.parentNode.type_frame && e.parentNode.view( d[ i ] );
					}
				}
			}
			this.x.focus = a;
		},
		isFocus: function() {
			return ! this.isDisabled() && this.x.focusable && this.x.focus;
		},
		focus: function( a ) {
			this.trigger( a === F ? 'blur' : 'focus' );
		},
		drop: function() {
			if ( this.usa() && this.more ) {
				this.more.show();
			}
		},
		close: function( e ) {
			e && $.stop( e );
			if ( ! this._disposed && F !== this.trigger( 'close' ) )
				this.remove();
		},
		lock: function( a ) {
			this.trigger( a === F ? 'unlock' : 'lock' );
		},
		isLocked: function() {
			return this._locked;
		},
		draggable: function( a ) {
			$.draggable( this, a );
			return this;
		},
		droppable: function( a ) {
			$.draggable( this, a );
			return this;
		},
		// 新增或更换图标。如果 a == '', 则删除图标  / @a -> image src
		icon: function( a ) {
			this.attr( 'icon', a );
		},
		// 新增或更换文本。如果 a == ''  / @a -> text
		text: function( a ) {
			this.attr( 'text', a );
		},
		// @implement
		attrSetter: function( a, b ) {
			if ( ! this.$() )
				return;
			if ( a === 'icon' ) {
				if ( b )
					this.$( 'i' ) ? $.replace( this.$( 'i' ), this.html_icon( b ) ) : $.prepend( this.$( 'c' ), this.html_icon( b ) );
				else
					this.removeElem( 'i' );
			} else if ( a === 'text' ) {
				if ( b )
					this.$( 't' ) ? Q( 'em', this.$( 't' ) ).html( b ) : $.append( this.$( 'c' ), this.html_text( b ) );
				else
					this.removeElem( 't' );
			}
		},
		html_icon: function( a ) {
			return $.image( a || this.x.icon, { id: this.id + 'i', cls: '_i', width: this.x.iconwidth, height: this.x.iconheight } );
		},
		html_text: function( a ) {
			return '<span id=' + this.id + 't class="_t f-omit"' + ( this.x.textstyle ? ' style="' + this.x.textstyle + '"' : '' ) + '><em>' + (a || this.x.text) + '</em><i class=f-vi></i></span>';
		},
		html: function() {
			var x = this.x, p = this.parentNode, t = this.tagName || 'div',
				a = '<' + t + ' id=' + this.id + ' class="',
				b = this.prop_cls(),
				c = this._combo, d, s = '';
			b += x.hidetoggle ? ' z-normal' : c ? ' z-combo' : this.more ? ' z-more' : ' z-normal';
			if ( x.closeable || x.closeicon )
				b += ' z-x';
			if ( (d = this.innerWidth()) != N ) {
				s += 'width:' + d + 'px;';
				b += ' z-fixed';
			}
			if ( x.focus && x.focusable )
				b += ' z-on';
			if ( p.type_buttonbar ) {
				if ( this === p[ 0 ] || ((d = this.prev()) && d.type === 'button/split') )
					b += ' z-first';
				if ( this === p[ p.length - 1 ] || ((d = this.next()) && d.type === 'button/split') )
					b += ' z-last';
			}
			a += b + '"';
			if ( this.attr( 'height' ) != N ) {
				if ( (d = this.innerHeight()) != N )
					s += 'height:' + d + 'px;';
			}
			if ( this.x.minwidth && (v = this.innerWidth( 'min' )) )
				s += 'min-width:' + v + 'px;';
			if ( this.x.maxwidth && (v = this.innerWidth( 'max' )) )
				s += 'max-width:' + v + 'px;';
			if ( this.x.minheight && (v = this.innerHeight( 'min' )) )
				s += 'min-height:' + v + 'px;';
			if ( this.x.maxheight && (v = this.innerHeight( 'max' )) )
				s += 'max-height:' + v + 'px;';
			if ( (d = p.x.space) != N && this !== p[ p.length - 1 ] )
				s += 'margin-' + ( p.x.dir === 'v' ? 'bottom' : 'right' ) + ':' + d + 'px;';
			if ( x.style )
				s += x.style;
			if ( s )
				a += ' style="' + s + '"';
			if ( x.target )
				a += ' w-target="' + ((x.target.x && x.target.x.id) || x.target) + '"';
			a += c ? ' onmouseover=' + eve + ' onmouseout=' + eve : _html_on( this, ' onclick=' + eve );
			if ( x.tip )
				a += ' title="' + $.strQuot( x.tip === T ? x.text : x.tip ) + '"';
			x.id && (a += ' w-id="' + x.id + '"');
			if ( this.property )
				a += this.property;
			a += ' w-type="' + this.type + '">' + this.html_before() + '<div class=_c id=' + this.id + 'c' + (c ? _html_on(this, ' onclick=' + eve) : '' ) + '>';
			if ( x.icon )
				a += this.html_icon();
			if ( x.text )
				a += this.html_text();
			a += '</div>';
			if ( ! x.hidetoggle && this.more )
				a += '<div class=_m id=' + this.id + 'm' + ( c ? _html_on( this, ' onclick=' + evw + '.drop()' ) : '' ) + '><em class=f-arw></em><i class=f-vi></i></div>';
			if ( x.closeicon )
				a += $.image( x.closeicon, { cls: '_x', click: evw + '.close()' } );
			else if ( x.closeable )
				a += '<div class=_x onclick=' + evw + '.close(event)><i class=f-vi></i><i class="_xi">&times;</i></div>';
			a += this.html_after() + '</' + t + '>';
			return a;
		}
	}
}),
/* `submitbutton` */
Submitbutton = define.widget( 'submitbutton', {
	Const: function( x, p ) {
		Button.apply( this, arguments );
		! this.ownerView.submitButton && (this.ownerView.submitButton = this);
	},
	Extend: Button,
	Rooter: 'buttonbar',
	Prototype: {
		className: 'w-submit w-button',
		dispose: function() {
			if ( this.ownerView.submitButton === this )
				delete this.ownerView.submitButton;
			Button.prototype.dispose.call( this );
		}
	}
} ),
/* `menubutton` */
_menu_button_height,
MenuButton = define.widget( 'menu/button', {
	Extend: Button,
	Rooter: 'menu',
	Listener: {
		body: {
			click: function() {
				if ( ! this.isDisabled() && this.x.on && this.x.on.click ) {
					this.root().hide();
				}
			}
		}
	},
	Prototype: {
		className: 'w-menu-button f-nobr',
		_menu_snaptype: '',
		elemht: function() {
			if ( ! _menu_button_height ) {
				br.chdiv( 'w-menu-button', function() { _menu_button_height = this.offsetHeight || 28 } );
			}
			return _menu_button_height;
		},
		x_type: function( t, n ) {
			return t === 'menu' ? 'submenu' : t;
		},
		root: function() {
			return this.closest( 'menu' );
		},
		getCommander: function() {
			return this.root().commander;
		},
		exec: function() {
			return _proto.exec.apply( this.getCommander(), arguments );
		},
		show: function() {
			if ( this.more ) {
				this.more.show();
			}
			return this;
		},
		hide: function() {
			if ( this.more ) {
				this.more.hide();
			}
			return this;
		},
		dispose: function() {
			this.more && ( this.more.dispose(), this.more = N );
			_proto.dispose.call( this );
		},
		html: function() {
			var x = this.x,
				a = '<div id=' + this.id + ' class="',
				b = this.prop_cls(),
				s = ''; // style
			if ( x.closeable )
				b += ' z-close';
			var w = this.innerWidth();
			if ( w != null ) {
				s += 'width:' + w + 'px;';
				b += ' w-button-fixed';
			}
			if ( this.more )
				b += ' z-more';
			if ( x.focus )
				b += ' z-on';
			if ( x.style )
				s += x.style;
			a += b + '"';
			if ( s )
				a += ' style="' + s + '"';
			x.id && (a += ' w-id="' + x.id + '"');
			a += _html_on( this, ' onclick=' + eve ) + '><div class=_c id=' + this.id + 'c>';
			a += $.image( x.icon || '', { cls: '_i', id: this.id + 'i' } );
			if ( x.text )
				a += '<span class="_t f-omit" id=' + this.id + 't><em>' + x.text + '</em><i class=f-vi></i></span>';
			a += '</div>';
			a += '<div class=_m>' + ( this.more ? $.arrow( 'r1' ) : '' ) + '<i class=f-vi></i></div>';
			if ( x.closeable )
				a += '<div class=_x></div>';
			return a + '</div>';
		}
	}
} ),
MenuSubmitbutton = define.widget( 'menu/submitbutton', {
	Const: function() {
		Submitbutton.apply( this, arguments );
	},
	Extend: 'menu/button',
	Prototype: {
		className: 'w-menu-button w-submit f-nobr',
		dispose: function() {
			if ( this.ownerView.submitButton === this )
				delete this.ownerView.submitButton;
			MenuButton.prototype.dispose.call( this );
		}
	}
} ),
/* `buttonsplit` */
ButtonSplit = define.widget( 'button/split', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		var s = _dfopt.buttonbar && _dfopt.buttonbar.space;
		s && this.defaults( { width: s } );
	},
	Prototype: {
		className: 'w-button-split f-inbl',
		html_nodes: function() {
			return '<span class="f-va _vr">' + (this.x.text || '') + '</span><i class=f-vi></i>';
		}
	}
} ),
/* `album` */
Album = define.widget( 'album', {
	Extend: 'horz',
	Child: 'img',
	Prototype: {
		NODE_ROOT: T,
		className: 'w-album',
		getFocus: function() {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].isFocus() ) return this[ i ];
		},
		getFocusAll: function() {
			for ( var i = 0, r = []; i < this.length; i ++ )
				if ( this[ i ].isFocus() ) r.push( this[ i ] );
			return r;
		},
		focusAll: function( a ) {
			for ( var i = 0; i < this.length; i ++ )
				this[ i ].focus( a );
		}
	}
} ),
/* `img` */
Img = define.widget( 'img', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		x.focusable && x.focus && $.classAdd( this, 'z-on' );
		x.face && $.classAdd( this, 'z-face-' + x.face );
		if ( p.type == Album.type ) {
			var c = p.x.space || 0;
			c && (this.cssText = 'margin-top:' + c + 'px;margin-left:' + c + 'px;');
			this.defaults( { wmin: 22 + c } );
		}
	},
	Rooter: 'album',
	Listener: {
		body: {
			mouseover: {
				prop: T,
				method: function() {
					$.classAdd( this.$(), 'z-hv' );
				}
			},
			mouseout: {
				prop: T,
				method: function() {
					$.classRemove( this.$(), 'z-hv' );
				}
			},
			click: {
				prop: T,
				block: function( e ) { return this.box && e.srcElement && e.srcElement.id === this.box.id + 't' },
				method: function() {
					this.x.focusable && this.focus( ! this.isFocus() );
				}
			}
		}
	},
	Default: { height: -1 },
	Prototype: {
		className: 'w-img',
		width_minus: function() {
			return (p.x.space || 0);
		},
		attrSetter: function( a, b ) {
			if ( a === 'src' ) {
				this.$( 'i' ) && $.replace( this.$( 'i' ), this.html_img() );
			} else if ( a === 'text' || a === 'description' ) {
				this.$( 't' ) && $.replace( this.$( 't' ), this.html_text() );
			}
		},
		isFocus: function() {
			return $.classAny( this.$(), 'z-on' );
		},
		focus: function( a ) {
			var a = a == N || a, p = this.parentNode, b = p.getFocus();
			$.classAdd( this.$(), 'z-on', a );
			this.box && this.box.click( a );
			a && b && b !== this && ! p.x.focusmultiple && b.focus( F );
		},
		html_img: function() {
			var x = this.x, b = this.parentNode.type === 'album', w = x.imgwidth, h = x.imgheight,
				g = $.image( this.x.src, { width: w, height: h }, { tip: x.tip === T ? x.text + (x.description ? '\n' + x.description : '') : x.tip } );
			return '<div id=' + this.id + 'i class="w-img-i f-inbl" style="width:' + ( w ? (isNaN( w ) ? w : w + 'px') : 'auto' ) + ';height:' + ( h ? (isNaN( h ) ? h : h + 'px') : '100%' ) + ';">' + g + '</div>';
		},
		html_text: function() {
			var x = this.x, p = this.parentNode, f = p.x.format, s, t = x.text;
			if ( typeof t !== _OBJ && f )
				t = _grid_format.call( this, f, p.x.escape );
			else if ( typeof t === _STR && p.x.escape )
				t = $.strEscape( t );
			return t ? '<div id=' + this.id + 't class="w-img-t f-' + ( x.nobr ? 'fix' : 'wdbr' ) + '"' + ( x.nobr && this.x.text ? ' title="' + $.strQuot( this.x.text ) + '"' : '' ) + ' style="width:' + (x.textwidth ? x.textwidth + 'px' : 'auto') + '">' +
					(typeof t === _OBJ ? this.add( t, -1 ).html() : '<span class=w-img-s>' + t + '</span>') + (x.description ? '<div class="w-img-d f-fix" title="' + $.strQuot( x.description ) + '">' + x.description + '</div>' : '') + '</div>' : '';
		},
		html_nodes: function() {
			var s = this.html_img();
			if ( this.x.box ) {
				this.box = Checkbox.parseOption( this, { cls: 'w-img-box', checked: this.x.focus } );
				s = this.box.html() + s;
			}
			s += this.html_text();
			return s;
		}
	}
} ),
/* `toggle` 可展开收拢的工具条。可显示单行文本与(或)分割线。
 *  /@text: 文本; @hr(Bool) 显示横线; /@open(Bool): 设置初始展开收拢效果并产生一个toggle图标; /@target: 指定展开收拢的widget ID, 多个用逗号隔开
 */
Toggle = define.widget( 'toggle', {
	Listener: {
		body: {
			ready: function() {
				this.x.open != N && this.toggle( this.x.open );
			},
			click: {
				prop: T,
				method: function() {
					this.toggle();
				}
			}
		}
	},
	Helper: {
		focus: function( a ) {
			var p = a;
			do {
				var b = p.$() && p.$().getAttribute( 'w-toggle' );
				if ( b ) {
					_all[ b ].toggle( T );
					break;
				}
			} while ( p = p.parentNode );
		}
	},
	Prototype: {
		className: 'w-toggle',
		toggle: function( a ) {
			var a = a == N ? ! (this.x.open == N ? T : this.x.open) : a, c = this.x.target, d = this.x.icon, e = this.x.openicon || d;
			this.x.open = a;
			if ( c && (c = c.split(',')) ) {
				for ( var i = 0, g; i < c.length; i ++ ) {
					(g = this.ownerView.find( c[ i ] )) && g.display( this );
				}
			}
			for ( var i = this.nodeIndex + 1, p = this.parentNode, l = p.length; i < l; i ++ ) {
				if ( p[ i ].type === this.type )
					break;
				p[ i ].display( this );
			}
			if ( this.$( 'o' ) ) {
				if ( e ) {
					$.replace( this.$( 'o' ), $.image( a === F ? (d || e) : e, { cls: 'w-toggle-icon', id: this.id + 'o' } ) );
				} else
					$.arrow( this.$( 'o' ), a ? 'b1' : 'r1' );
			}
			$.classAdd( this.$(), 'z-collapse', ! a );
			this.trigger( a ? 'expand' : 'collapse' );
		},
		html_nodes: function() {
			var x = this.x, s = '<table class="w-toggle-table' + ( this.x.hr ? ' z-hr' : '' ) + '" cellspacing=0 cellpadding=0><tr><td class=f-nobr>', c = x.icon, d = x.openicon || c;
			if ( d ) {
				s += $.image( x.open === F ? (c || d) : d, { cls: 'w-toggle-icon', id: this.id + 'o' } );
			} else
				s += ( x.open != N ? $.arrow( this.id + 'o', x.open === F ? 'r1' : 'b1' ) : '' );
			return s + ' <span class=w-toggle-text><em>' + this.x.text + '</em></span>' + ( x.hr ? '<td width=100%><hr class=w-toggle-hr noshade>' : '' ) + '</table>';
		}
	}
} ),
/* `page`
 *  @target: 指向另一个widget，点击页数时让这个widget执行 .page() 方法。如果设定了此参数，那么 currentpage sumpage 等参数都从这个widget里读取
 *  @src: 带$0的url, 点击页数时把$0替换为页数并执行ajax命令
 *  @sumpage: 总页数
 *  @nofirstlast: 不显示"首页"和"尾页"
 *  @jump: 显示跳转输入框
 *  @btncount: 中间有几个显示页数的按钮
 *  @name: 隐藏表单值
 */
Page = define.widget( 'page/mini', {
	Const: function( x ) {
		W.apply( this, arguments );
		x.transparent && (this.className += ' z-trans');
	},
	Extend: 'html',
	Listener: {
		body: {
			// 阻止默认触发用户事件，改在 go 方法中调用
			click: { block: $.rt_true }
		}
	},
	Prototype: {
		className: 'w-page w-page-mini',
		isModified: $.rt_false,
		over: function( a ) {
			$.classAdd( a, 'z-hv' );
		},
		out: function( a ) {
			$.classRemove( a, 'z-hv' );
		},
		go: function( i, a ) {
			if ( (i = _number( i )) > 0 ) {
				this.$( 'v' ) && (this.$( 'v' ).value = i);
				if ( this.x.target ) {
					var g = this.ownerView.find( this.x.target );
					if ( g ) {
						g.page( i );
						this.x.currentpage = i;
						this.render();
					}
				} else if ( this.x.src ) {
					var s = this.x.src;
					if ( s.indexOf( 'javascript:' ) === 0 ) {
						s = Function( '$0', s ).call( this, i );
						if ( typeof s === _OBJ )
							return this.cmd( s );
					}
					s && this.cmd( { type: 'ajax', src: $.urlFormat( s, [ i ] ) } );
				}
				// 为业务 click 事件之中的 $0 提供值
				this.data( '0', i );
				this.triggerHandler( 'click' );
			}
		},
		val: function( a ) {
			if ( a === U )
				return this.x.currentpage;
			this.go( a );
		},
		ego: function( e ) {
			if ( e.type === 'keyup' && e.keyCode !== 13 )
				return;
			else if ( e.type === 'click' )
				this.$( 'j' ).focus();
			this.go( this.$( 'j' ).value );
		},
		jumpFocus: function( e, a ) {
			a = a == N || a;
			if ( e.type === 'blur' && this.jbtn.$().contains( document.activeElement ) )
				return;
			$.classAdd( this.$( 'j' ), 'z-on', a );
			$.classAdd( this.$( 'j' ).nextSibling, 'z-on', a );
		},
		initByTarget: function() {
			var c = this.ownerView.find( this.x.target ).page();
			this.x.currentpage = Math.max( _number( c.currentpage ), 1 );
			this.x.sumpage = Math.max( _number( c.sumpage ), 1 );
		},
		eve: function( i, b ) {
			return b ? ' onclick=' + evw + '.go(' + i + ',this) onmouseover=' + evw + '.over(this) onmouseout=' + evw + '.out(this)' : '';
		},
		html_nodes: function() {
			this.x.target && this.initByTarget();
			var c = this.x.currentpage, m = _number( this.x.sumpage ), n = _number( this.x.btncount ), f = Math.max( 1, c - Math.ceil( n / 2 ) + 1 ),
				l = Math.min( m + 1, f + n ), d = l - f < n ? Math.max( 1, l - n ) : f, h = this.x.nofirstlast, z = this.x.btncls ? ' ' + this.x.btncls : '',
				s = ( h ? '' : '<em class="_o _b _first' + ( c == 1 ? '' : ' z-us' ) + z + '"' + this.eve( 1, c != 1 ) + '>' + (this.x.labelfirst || this.page_first || '') + '</em>' ) +
					'<em class="_o _b _prev' + ( c == 1 ? '' : ' z-us' ) + z + '"' + this.eve( c - 1, c != 1 ) + '>' + (this.x.labelprev || this.page_prev || '') + '</em>';
			if ( m && this.x.btnsumpage && d > 1 ) {
					s += '<em class="_o _num z-us z-sum' + z + '"' + this.eve( 1, T ) + '>1<i>...</i></em>';
					s += '<em class="_o _num z-sumdot' + z + '">...</em>';
			}
			for ( var i = d; i < l; i ++ ) {
				s += '<em class="_o _num' + ( i == c ? ' _cur' : ' z-us' ) + z + '"' + this.eve( i, i != c ) + '>' + i + '</em>';
			}
			if ( m && this.x.btnsumpage && m >= i ) {
				s += '<em class="_o _num z-sumdot' + z + '">...</em>';
				s += '<em class="_o _num z-us z-sum' + z + '"' + this.eve( m, T ) + '><i>...</i>' + m + '</em>';
			}
			s += '<em class="_o _b _next' + ( c == m ? '' : ' z-us' ) + z + '"' + this.eve( c + 1, c != m ) + '>' + (this.x.labelnext || this.page_next || '') + '</em>' +
				( h ? '' : '<em class="_o _b _last' + ( c == m ? '' : ' z-us' ) + z + '"' + this.eve( m, c != m ) + '>' + (this.x.labellast || this.page_last || '') + '</em>' );
			return (this.x.name ? '<input type=hidden id="' + this.id + 'v" name="' + this.x.name + '" value="' + (c || 1) + '">' : '') + s + this.html_info() + '<i class=f-vi></i>';
		},
		html_info: function() {
			var s = '';
			if ( this.x.info )
				s += '<span class="_t _inf">' + this.x.info + '</span>';
			if ( this.x.jump ) {
				this.jbtn = this.add( { type: 'button', cls: '_jbtn', text: 'GO', on: { click: 'this.parentNode.ego(event)' } }, -1 );
				s += '<span class="_t _to">' + Loc.to + '</span> <input class=_jump id=' + this.id + 'j onfocus=' + evw + '.jumpFocus(event) onblur=' + evw + '.jumpFocus(event,!1) onkeyup=' + evw + '.ego(event)>' + this.jbtn.html() + ' <span class=_t>' + Loc.page + '</span>';
			}
			if ( this.x.setting ) {
				this.sbtn = this.add( { type: 'button', cls: 'f-button _sbtn', icon: '.f-i .f-i-config', nodes: this.x.setting }, -1 );
				s += this.sbtn.html();
			}
			return s;
		},
		dispose: function() {
			$( this.id + 'j' ) && ($( this.id + 'j' ).onblur = N); /* 如果不写这一句，谷歌浏览器会崩溃 */
			_proto.dispose.call( this );
		}
	}
} ),
PageText = define.widget( 'page/text', {
	Extend: 'page/mini',
	Prototype: {
		className: 'w-page w-page-text',
		page_next: Loc.page_next,
		page_prev: Loc.page_prev,
		page_first: Loc.page_first,
		page_last: Loc.page_last
	}
} ),
PageGroup = define.widget( 'page/buttongroup', {
	Extend: 'horz',
	Listener: {
		body: {
			click: { block: $.rt_true }
		}
	},
	Prototype: {
		className: 'w-page w-page-buttongroup',
		isModified: $.rt_false,
		val: Page.prototype.val,
		go: Page.prototype.go,
		drop: function( a ) {
			var i = 1, c = _number( this.x.currentpage ) || 1, s = _number( this.x.sumpage ) || 1, n = [];
			for ( ; i <= s; i ++ ) {
				n.push( { text: i, on: { click: 'this.root().parentNode.parentNode.parentNode.go(' + i + ')' }, status: this.x.currentpage == i ? 'disabled' : '' } );
			}
			a.cmd( { type: 'menu', nodes: n, snap: a, snaptype: 'v', line: true, memory: T, indent: 1, focusIndex: c } );
		},
		html_nodes: function() {
			this.x.target && this.initByTarget();
			var c = 'w-page-button ' + (this.x.btncls != N ? this.x.btncls : 'f-button'), d = _number( this.x.currentpage ) || 1, s = _number( this.x.sumpage ) || 1,
				b = [ { type: 'button', text: '&lt;<i class="f-arw f-arw-l4"></i>', cls: c + ' _prev', status: d == 1 ? 'disabled' : '', on: { click: 'this.rootNode.parentNode.go(' + (d - 1) + ')' } },
					  { type: 'button', text: '&gt;<i class="f-arw f-arw-r4"></i>', cls: c + ' _next', status: d == s ? 'disabled' : '', on: { click: 'this.rootNode.parentNode.go(' + (d + 1) + ')' } } ];
			if ( this.x.btncount != 0 ) {
				var g = this.x.dropalign;
				b.splice( g === 'right' ? 2 : g === 'left' ? 0 : 1, 0, { type: 'button', text: d + '/' + s, cls: c + ' _drop', on: { click: 'this.rootNode.parentNode.drop(this)' } } );
			}
			this.groupbar = this.add( { type: 'buttonbar', cls: 'f-groupbar', nodes: b }, -1 );
			return (this.x.name ? '<input type=hidden id="' + this.id + 'v" name="' + this.x.name + '" value="' + d + '">' : '') + this.groupbar.html() + Page.prototype.html_info.call( this ) + '<i class=f-vi></i>';
		}
	}
} ),
/* `fieldset` */
Fieldset = define.widget( 'fieldset', {
	Extend: 'vert/scale',
	Default: { wmin: 2, hmin: 2 },
	Prototype: {
		className: 'w-fieldset',
		init_nodes: function( x ) {
			this.gut = this.add( { type: 'vert', hmin: 23, nodes: x.nodes } );
			if ( x.box )
				this.box = Checkbox.parseOption( this, { target: this.gut } );
		},
		add: function( x, n ) {
			return _proto.add.call( (n !== -1 && this.gut) || this, x, n );
		},
		html: function() {
			return '<fieldset' + this.html_prop() + '><legend class=w-fieldset-legend>' + ( this.box ? this.box.html() : '' ) + this.x.legend + '</legend>' + this.html_before() + this.gut.html() + this.html_after() + '</fieldset>'
		}
	}
} ),
// 模板标题
TemplateTitle = define.widget( 'template/title', {
	Const: function( x, p ) {
		var d = Dialog.get( p );
		if ( d ) {
			d.templateTitle = this;
			x.text = d.x.title;
		}
		W.apply( this, arguments );
	},
	Extend: 'html',
	Listener: {
		body: {
			mousedown: {
				prop: T,
				method: function( e ) {
					var o = Dialog.get( this );
					if ( o ) {
						o.front();
						var b = o._pos.pix_b ? -1 : 1, r = o._pos.pix_r ? -1 : 1, v = b < 0 ? 'bottom' : 'top', h = r < 0 ? 'right' : 'left',
							x = e.clientX, y = e.clientY, t = _number( o.$().style[ v ] ), l = _number( o.$().style[ h ] ), self = this, m, n = $.height(), w = $.width(), z = o.$().offsetWidth;
						if ( o.x.moveable !== F && ! o._fullscreen ) {
							$.moveup( function( e ) {
								! m && (m = $.db( '<div class=w-dialog-move style="width:' + $.width() + 'px;height:' + n + 'px;"></div>' ));
								o.$().style[ v ] = $.numRange( t + b * (e.clientY - y), 0, n - 30 ) + 'px';
								o.$().style[ h ] = $.numRange( l + r * (e.clientX - x), 100 - z, w - 30 ) + 'px';
							}, function( e ) {
								m && $.remove( m );
							} );
						}
					}
				}
			}
		}
	}
} ),
// 模板内容
TemplateBody = define.widget( 'template/body', {} ),

_tpl_str = {},
_tpl_ids = {},
_tpl_parse = function( a, b ) {
	var r = {}, t = 'template/body';
	if ( $.arrIs( a ) ) {
		for ( var i = 0, l = a.length, c; i < l; i ++ ) {
			$.merge( r, _tpl_parse( a[ i ], b ) );
		}
	} else {
		if ( a.id ) {
			r[ a.id ] = a;
			if ( b && b[ a.id ] ) {
				return r;
			}
		}
		if ( ! b && a.type === t )
			r[ t ] = a;
		for ( var k in a ) {
			if ( a[ k ] != N && typeof a[ k ] === _OBJ ) {
				$.merge( r, _tpl_parse( a[ k ], b ) );
			}
		}
	}
	return r;
},
/* `templateview` */
TemplateView = define.widget( 'template/view', {
	Const: function( x, p, n ) {
		var d = Dialog.get( p );
		this.tplNode = x.node;
		this.tplCmds = x.commands;
		this.tplID = d ? d.x.template : $.jsonString( x.node );
		if ( d ) {
			d.contentView = this;
			if ( d._viewNode ) {
				x = $.extend( {}, d._viewNode, x );
			} else if ( d.x.src )
				x.src = d.x.src;
			if ( ! x.id && d.x.id )
				x.id = d.x.id;
		}
		View.call( this, x, p, n );
	},
	Extend: 'view',
	Prototype: {
		init_x: function( x ) {
			View.prototype.init_x.call( this, x );
			// 有src 的情况下，init_x 会执行两次，第二次是 _instanced = T
			if ( this.tplNode && (!x.src || this._instanced) ) {
				var a = this.tplID, t = 'template/body',
					s = _tpl_str[ a ] || (_tpl_str[ a ] = $.jsonString( this.tplNode )),
					c = _tpl_ids[ a ] || (_tpl_ids[ a ] = _tpl_parse( this.tplNode )),
					d = _tpl_parse( x.node, c );
				for ( var k in c ) {
					if ( d[ k ] ) {
						s = s.replace( $.jsonString( c[ k ] ), $.jsonString( d[ k ] ) );
					}
				}
				// 如果之前的替换处理后， template/body 没被替换掉，那么它将替换整个node
				if ( c[ t ] ) {
					s = s.replace( $.jsonString( c[ t ] ), function() { return $.jsonString( x.node ) } );
				}
				this.x.node = $.jsonParse( s );
				if ( this.tplCmds ) {
					this.x.commands = $.extend( x.commands || {}, $.jsonClone( this.tplCmds ) );
				}
			}
		}
	}
} ),
/* `dialog`
 *  id 用于全局存取 ( dfish.dialog(id) ) 并保持唯一，以及用于里面的view的 path */
Dialog = define.widget( 'dialog', {
	Const: function( x, p, n ) {
		if ( x.node ) {
			if ( x.node.type !== 'view'  )
				x.node = { type: 'view', node: x.node };
			if ( ! x.node.id && x.id )
				x.node.id = x.id;
		}
		if ( x.template ) {
			if ( t = Dialog.tpl( x.template ) ) {
				if ( x.node )
					this._viewNode = x.node;
				x.node = $.jsonClone( t.node );
				if ( x.cls && t.cls )
					x.cls = t.cls + ' ' + x.cls;
				$.extend( x, t );
			} else {
				var s = Loc.ps( Loc.debug.no_template, x.template );
				t === F ? alert( s ) : $.winbox( s );
			}
		}
		this._dft_wd = x.width;
		this._dft_ht = x.height;
		this._fullscreen = x.fullscreen;
		if ( x.fullscreen || (x.width && ! isNaN( x.width ) && x.width > $.width()) )
			x.width = '*';
		if ( x.fullscreen || (x.height && ! isNaN( x.height ) && x.height > $.height()) )
			x.height = '*';
		p == N && (p = _docView);
		W.call( this, x, p, n == N ? -1 : n );
		if ( ! x.node && ! x.template && x.src )
			this.add( { type: 'view', src: x.src, id: x.id } );
		if ( this[ 0 ] && this[ 0 ].type_view )
			this.contentView = this[ 0 ];
		if ( this.contentView ) {
			this.contentView.addEvent( 'load', function() { this.trigger( 'load' ) }, this )
				.addEvent( 'loading', function() { this.trigger( 'loading' ) }, this );
		}
		if ( x.id ) {
			Dialog.custom[ x.id ] && Dialog.custom[ x.id ].remove();
			Dialog.custom[ x.id ] = this;
		}
		Dialog.all[ this.id ] = this;
		if ( p !== _docView ) {
			this.parentDialog = p.closest( function() { return this.type === 'dialog' } );
		}
		(this.commander = p).addEventOnce( 'remove', this.remove, this );
	},
	Helper: {
		all: {},
		custom: {},
		get: function( a ) {
			if ( typeof a === _STR )
				return Dialog.custom[ a ];
			if ( a.isWidget )
				return ! a._disposed && a.closest( function() { return this.isDialogWidget } );
			for ( var k in Dialog.all ) {
				if ( Dialog.all[ k ].contains( a, T ) )
					return Dialog.all[ k ];
			}
		},
		close: function( a ) {
			(a = Dialog.get( a )) && a.close();
		},
		tpl: function( a ) {
			var t = typeof a === _STR ? _templates[ a ] : a;
			if ( ! t && cfg.template_src )
				$.ajaxJSON( $.urlFormat( cfg.template_src, [ a ] ), function( x ) { t = _templates[ a ] = x; }, N, T, N, function() { t = F; } );
			return t;
		},
		cleanPop: function() {
			for ( var k in Dialog.all ) {
				Dialog.all[ k ].attr( 'pophide' ) && Dialog.all[ k ].close();
			}
		}
	},
	Listener: {
		body: {
			ready: function() {
				if ( this.x.resizable ) {
					var self = this;
					Q( '<div class="w-dialog-rsz z-w"></div><div class="w-dialog-rsz z-n"></div><div class="w-dialog-rsz z-e"></div><div class="w-dialog-rsz z-s"></div><div class="w-dialog-rsz z-nw"></div><div class="w-dialog-rsz z-ne"></div><div class="w-dialog-rsz z-sw"></div><div class="w-dialog-rsz z-se"></div>' )
						.appendTo( this.$() ).on( 'mousedown', function( e ) {
							if ( self._fullscreen )
								return;
							var a = this.className.match( /z-(\w+)/ )[ 1 ], b = $.bcr( self.$() ), ix = e.clientX, iy = e.clientY, o;
							$.moveup( function( e ) {
								var x = e.clientX, y = e.clientY;
								if ( ! o ) {
									o = $.db( '<div style="position:absolute;top:' + b.top + 'px;left:' + b.left + 'px;width:' + (b.width - 6) + 'px;height:' + (b.height - 6) + 'px;border:3px solid #000;opacity:.2;z-index:1"></div>' );
								}
								if ( a.indexOf( 'w' ) > -1 ) {
									o.style.width = (b.width - 6 + ix - x) + 'px';
									o.style.left = (b.left + x - ix) + 'px';
								}
								if ( a.indexOf( 'e' ) > -1 ) {
									o.style.width = (b.width - 6 + x - ix) + 'px';
								}
								if ( a.indexOf( 'n' ) > -1 ) {
									o.style.height = (b.height - 6 + iy - Math.max( y, 0 )) + 'px';
									o.style.top =(b.top + Math.max( y, 0 ) - iy) + 'px';
								}
								if ( a.indexOf( 's' ) > -1 ) {
									o.style.height = (b.height - 6 + y - iy) + 'px';
								}
							}, function( e ) {
								if ( o ) {
									var c = $.bcr( o ), s = self.$().style, l = s.left, t = s.top;
									self.resize( Math.max( c.width, 20), Math.max( c.height, 20) );
									s[ l ? 'left' : 'right' ] = (l ? c.left : c.right) + 'px';
									s[ t ? 'top' : 'bottom' ] = (t ? c.top : c.bottom) + 'px';
									$.remove( o );
									s = o = N;
								}
							} );
						} );
				}
			},
			loading: function() {
				if ( this.$() ) {
					this.addClass( 'z-loading' );
					this.draggable();
				}
			},
			load: function() {
				this.removeClass( 'z-loading' );
				this.draggable( N, F );
			},
			mousedown: {
				prop: T,
				method: function() {
					this.front();
				}
			}
		}
	},
	Prototype: {
		loaded: F,
		className: 'w-dialog',
		isDialogWidget: T,
		outerWidth: function() {
			var w = this.attr( 'width' );
			return w == N || w < 0 ? N : _docView.scaleWidth( this );
		},
		outerHeight: function() {
			var h = this.attr( 'height' );
			return h == N || h < 0 ? N : _docView.scaleHeight( this );
		},
		attrSetter: function( a, b ) {
			if ( a === 'title' ) {
				this.templateTitle && this.templateTitle.text( b );
			}
		},
		_dft_pos: function() {
			var w = this.width(), h = this.height();
			return { left: Math.ceil( ( $.width() - w ) / 2 ), top: Math.max( 0, Math.ceil( ( $.height() - h ) / 2 ) ) };
		},
		draggable: function( a, b ) {
			if ( (a || (a = this)) && (a = a.isWidget ? a.$() : a) ) {
				Q( a )[ b === F ? 'off' : 'on' ]( 'mousedown', TemplateTitle.Listener.body.mousedown.method );
			}
		},
		max: function() {
			var f = this._fullscreen, s = this.$().style, o = this._stack_pos, w = this.width(), h = this.height(), x, y;
			this.resize( f ? (o ? o.width : this._dft_wd) : '*', f ? (o ? o.height : this._dft_ht) : '*' );
			if ( f ) {
				if ( o ) {
					x = o.x, y = o.y;
				} else
					this.axis( ! f );
			} else {
				this._stack_pos = { width: w, height: h, y: s.top || s.bottom, x: s.left || s.right };
				x = y = 0;
			}
			x != N && (s[ s.left ? 'left' : 'right' ] = x);
			y != N && (s[ s.top ? 'top' : 'bottom' ]  = y);
			$.classAdd( this.$(), 'z-max', ! f );
			this._fullscreen = ! f;
		},
		//@public 移动到指定位置 /@a -> left, b -> top
		moveTo: function( a, b ) {
			var s = this.$().style;
			s.left = a + 'px';
			s.top  = b + 'px';
			s.right = s.bottom = '';
		},
		reload: function( a, b, c ) {
			if ( typeof b === _FUN )
				c = b, b = N;
			this.contentView.reload( a, b, c && $.proxy( this, c ) );
		},
		preload: function( a ) {
			this.contentView._load( F, a && $.proxy( this, a ), T );
		},
		//@public 移动到指定位置 /@a -> elem|widget, b -> snap option
		snapTo: function( a, b ) {
			! this._disposed && $.snapTo( this.$(), $.snap( this.width(), this.height(), a.isWidget ? a.$() : a, b || this.x.snaptype, this._fitpos, this.x.indent != N ? this.x.indent : (this.x.prong && -10) ) );
			return this;
		},
		// 使当前dialog显示优先级最高，不被其他dialog遮挡
		front: function() {
			if ( this._disposed )
				return;
			if ( this.parentDialog ) {
				this.parentDialog.front();
			} else {
				var a = Dialog.all, i, z;
				for ( i in a ) {
					a[ i ]._front( a[ i ] == this || this.contains( a[ i ] ) );
				}
			}
		},
		_front: function( a ) {
			var z = a ? 2 : 1;
			this.$() && (this.$().style.zIndex = z);
			this.$( 'cvr' ) && (this.$( 'cvr' ).style.zIndex = z);
		},
		// 定位 /@a -> fullscreen?
		axis: function( a ) {
			var f = $.number( this.x.position ), g = a ? N : this._snapElem(), w = this.$().offsetWidth, h = this.$().offsetHeight, n, r;
			// 如果有指定 snap，采用 snap 模式
			if ( g ) {
				r = $.snap( w, h, g, this.x.snaptype || this._snaptype, this._fitpos, this.x.indent != N ? this.x.indent : (this.x.prong && -10) );
			} else if ( f ) { // 八方位浮动的起始位置
				var b = '11,22,22,33,33,44,44,11'.split( ',' );
				r = $.snap( w, h, N, b[ f - 1 ], this._fitpos, this.x.indent );
				if ( f == 1 || f == 2 ) {
					n = { top: 0 };
					r.top = -h;
				} else if ( f == 3 || f == 4 ) {
					n = { right: 0 };
					r.right = -w;
				} else if ( f == 5 || f == 6 ) {
					n = { bottom: 0 };
					r.bottom = -h;
				} else if ( f == 7 || f == 8 ) {
					n = { left: 0 };
					r.left = -w;
				}
			}
			if ( ! r )
				r = this._dft_pos();
			this._pos = r;
			$.snapTo( this.$(), r );
			// 八方位浮动效果
			n && Q( this.$() ).animate( n, 100 );
			if ( this.x.prong && g ) {
				var m = r.mag_b ? 't' : r.mag_t ? 'b' : r.mag_l ? 'r' : 'l', x = Math.floor((r.target.left + r.target.right) / 2), y = Math.floor((r.target.top + r.target.bottom) / 2), 
					l = $.numRange( x - r.left, 7, r.left + r.width - 7 ), t = $.numRange( y - r.top, 7, r.top + r.height - 7 );
				$.append( this.$(), '<div class="w-dialog-prong z-' + m + '" style="' + (r.mag_b || r.mag_t ? 'left:' + l + 'px' : '') + (r.mag_l || r.mag_r ? 'top:' + t + 'px' : '') +
					'"><i class="_out f-arw f-arw-' + m + '5"></i><i class="_in f-arw f-arw-' + m + '4"></i></div>' );
			}
			$.classAdd( this.$(), 'z-max', !! a );
			this._snapCls();
		},
		_snapElem: function() {
			var d = this.x.snap;
			return typeof d === _STR ? ((d = this.ownerView.find( d )) && (d = d.$())) : (d && $( d ));
		},
		_snapCls: function( a ) {
			var d = this._snapElem(), r = this._pos;
			if ( d ) { 
				$.classAdd( d, 'z-drop', a );
				if ( r && r.type ) {
					var m = 'z-mag-' + ( r.mag_t ? 't' : r.mag_l ? 'l' : r.mag_b ? 'b' : 'r' );
					this.addClass( m + ' z-snap-' + r.type, a );
					$.classAdd( d, m, a );
				}
			}
		},
		html_nodes: ie7 ? function() {
			return '<table cellpadding=0 cellspacing=0 border=0><tr><td id=' + this.id + 'cont>' + _proto.html_nodes.apply( this, arguments ) + '</td></tr></table>';
		} : _proto.html_nodes,
		render: function() {
			if ( this._disposed )
				return;
			! this.parentNode && _docView.add( this );
			this.$() && this.removeElem();
			if ( this.x.cover )
				$.db( '<div id=' + this.id + 'cvr class="w-dialog-cover z-type-' + this.type +  '"></div>' );
			$.db( this.html() );
			if ( (this.x.minwidth || this.x.maxwidth) && ! this.x.width ) {
				var w = Math.max( this.$().offsetWidth, this.$().scrollWidth + 2 ), n = this.attr( 'minwidth' ), m = this.attr( 'maxwidth' );
				this.width( n && n > w ? n : m && m < w ? m : w );
			}
			if ( (this.x.minheight || this.x.maxheight) && ! this.x.height ) {
				var h = Math.max( this.$().offsetHeight, this.$().scrollHeight + 1 ), n = this.attr( 'minheight' ), m = this.attr( 'maxheight' );
				this.height( n && n > h ? n : m && m < h ? m : h );
			}
			// 检测object控件，如果存在则生成iframe遮盖。如果确定object不会影响dialog的显示，请给object标签加上属性 data-transparent="1"
			for ( var i = 0, o = $.tags( 'object' ); i < o.length; i ++ ) {
				if ( ! o[ i ].getAttribute( 'data-transparent' ) ) {
					this[ 0 ] && this[ 0 ].addClass( 'f-rel' );
					$.prepend( this.$(), '<iframe style="height:100%;width:100%;position:absolute;top:0;left:0;" src="about:blank" frameborder=0 marginheight=0 marginwidth=0></iframe>' );
					break;
				}
			}
			this.axis( this._fullscreen );
			// 生成一条线，覆盖在对话框(或menu)和父节点连接的地方，形成一体的效果
			if ( this.x.line ) {
				$.append( this.$(), '<div id=' + this.id + 'ln class=w-menu-line></div>' );
				var r = this._pos, w = r.target.width, b = this.$().currentStyle, c = this.parentNode.$().currentStyle, lw = _number( c.borderLeftWidth ), rw = _number( c.borderRightWidth );
				$.css( this.$( 'ln' ), { height: _number( b.borderTopWidth ), width: w - lw - rw, backgroundColor: b.backgroundColor, borderColor: c.borderColor, borderLeftWidth: lw, borderRightWidth: rw } );
			}
			this.attr( 'pophide' ) && this.listenHide( T );
			this.triggerAll( 'ready' );
			if ( this.x.timeout )
				this.listenTimeout();
			this.vis = T;
			this.front();
			return this;
		},
		isShow: function() {
			return this.vis;
		},
		show: function( a ) {
			if ( this.x.cache && this.$() ) {
				$.show( this.$() );
				this.x.cover && $.show( this.$( 'cvr' ) );
				this.vis = T;
				this._snapCls();
			} else
				this.render();
			return this;
		},
		hide: function() {
			if ( this._disposed )
				return;
			this._snapCls( F );
			if ( this.x.cache )
				this.$() && ($.hide( this.$() ), (this.x.cover && $.hide( this.$( 'cvr' ) )), this.vis = F);
			else if ( this.x.memory )
				 this._hide();
			else 
				this.remove();
		},
		_hide: function() {
			if ( this.vis ) {
				this.contentView && this.contentView.abort();
				this.listenHide( F );
				var f = this.x.position;
				if ( f && br.css3 ) {
					var w = this.$().offsetWidth, h = this.$().offsetHeight, d = this.id,
						n = f == 1 || f == 2 ? { top: -h } : f == 3 || f == 4 ? { right: -w } : f == 5 || f == 6 ? { bottom: -h } : { left: -w };
					$.classAdd( this.$(), 'z-closing' ); // z-closing生成遮盖层，避免在消失过程中内容部分再被点击
					Q( this.$() ).animate( n, 100, function() { $.remove( this ); $.remove( d + 'vcr' ); } );
				} else
					this.removeElem();
				this.vis = F;
			}
		},
		// 绑定鼠标监听 /@a -> 为 false 时解除监听
		listenHide: function( a ) {
			if ( a === F ) {
				this.listenHide_ && this._listenHide( a );
			} else {
				var self = this;
				setTimeout( function() { ! self._disposed && self._listenHide( a ); }, 200 ); // 延时处理，避免出现后立即消失的情况
			}
		},
		_listenHide: function( a ) {
			var self = this, d = this.x.hoverdrop;
			$.attach( document, 'mousedown mousewheel', self.listenHide_ || ( self.listenHide_ = function( e ) {
				! self._disposed && (e.srcElement.id == self.id + 'cvr' || ! ( self.contains( e.srcElement ) || ( ! self.x.independent && self.x.snap && self.x.snap.contains( e.srcElement ) ) )) && self.close();
			} ), a );
			if ( d ) {
				var o = d === T ? ($( this.x.snap ) || this.parentNode.$()) : d.isWidget ? d.$() : d, f = a === F ? 'off' : 'on';
				Q( [ o, self.$() ] )[ f ]( 'mouseenter', self._hover_over || (self._hover_over = function() { clearTimeout( self._hover_timer ); delete self._hover_timer; }) );
				Q( document )[ f ]( 'mousemove', self._hover_move || (self._hover_move = function( e ) {
					if ( ! o.contains( e.target ) && ! self.contains( e.target ) ) {
						if ( ! self._hover_timer )
							self._hover_timer = setTimeout( function() { self.close() }, 300 );
					} else
						self._hover_over && self._hover_over();
				}) );
			}
		},
		listenTimeout: function() {
			setTimeout( $.proxy( this, this.close ), this.x.timeout * 1000 );
		},
		removeElem: function( a ) {
			_proto.removeElem.call( this, a );
			! a && this.x.cover && $.remove( this.$( 'cvr' ) );
		},
		remove: function() {
			if ( ! this._disposed ) {
				this._hide();
				this.dispose();
			}
		},
		close: function() {
			if ( ! this._disposed && F !== this.trigger( 'close' ) )
				this.hide();
		},
		dispose: function() {
			if ( ! this._disposed ) {
				this.commander && this.commander.removeEvent( 'remove', this.remove, this );
				delete Dialog.all[ this.id ];
				delete this.parentDialog;
				delete this.commander;
				if( this.x.id )
					delete Dialog.custom[ this.x.id ];
				delete _inst_cache[ this.type ];
				_proto.dispose.call( this );
			}
		}
	}
} ),
_operexe = function( x, g, a ) {
	return typeof x === _OBJ ? g.exec( x, a ) : (x && $.fnapply( x, g, a ));
},
/* `alert/button` */
AlertButton = define.widget( 'alert/button', {
	Extend: Button,
	Listener: {
		body: {
			click: {
				block: function() {
					var d = $.dialog( this );
					_operexe( (this.x.on && this.x.on.click) || (this.type === 'alert/submitbutton' ? d.x.yes : d.x.no), d.commander, d.x.args );
					d.close();
					return T;
				}
			}
		}
	}
} ),
AlertSubmitButton = define.widget( 'alert/submitbutton', {
	Extend: 'submitbutton',
	Listener: { body: { click: { block: AlertButton.Listener.body.click.block } } }
} ),
/*  `alert`  */
Alert = define.widget( 'alert', {
	Const: function( x, p ) {
		var a = this.type === 'alert', k, t = cfg.template_alert || cfg.template, r = x.args, s = x.btncls || 'f-button',
			b = { type: 'alert/submitbutton', cls: s, text: '    ' + Loc.confirm + '    ' },
			c = { type: 'alert/button', cls: s, text: '    ' + Loc.cancel + '    ' }, d;
		if ( x.buttons ) {
			for ( var i = 0, d = []; i < x.buttons.length; i ++ ) {
				x.buttons[ i ].type = 'alert/' + x.buttons[ i ].type;
				! x.buttons[ i ].cls && (x.buttons[ i ].cls = s);
				d.push( x.buttons[ i ] );
			}
		}
		if ( this._tpl = Dialog.tpl( t ) ) {
			$.extend( x, { template: t, minwidth: 260, maxwidth: 700, maxheight: 600, title: Loc.opertip, node: { type: 'vert', nodes: [
				{ type: 'html', scroll: T, height: '*', text: '<table border=0 style="margin:10px 20px 20px 5px;word-wrap:break-word;"><tr><td align=center valign=top><div style=width:65px;padding-top:5px>' +
				$.image( x.icon ? x.icon : '.f-i-alert' + (a ? 'warn' : 'ask') ) + '</div><td>' + $.strFormat( (x.text || '') + '', x.args ).replace( /\n/g, '<br>' ) + '</table>' },
				{ type: 'buttonbar', align: 'center', height: 60, space: 10, nodes: d || (a ? [ b ] : [ b, c ]) }
			] } } );
		}
		Dialog.call( this, x, a ? _docView : p );
	},
	Extend: Dialog,
	Prototype: {
		className: 'w-dialog w-alert',
		// alert 类型对话框z-index固定值为3，总在最前面，不做修改
		front: $.rt_false,
		_front: $.rt_false,
		render: function() {
			var s = document.readyState;
			if ( this._tpl && (s === 'complete' || s === U) ) {
				return Dialog.prototype.render.call( this );
			} else {
				var x = this.x, t = $.strFormat( (x.text || '') + '', x.args );
				if ( this.type === 'alert' ) {
					$.winbox( t );
					_operexe( x.yes, this.commander, x.args );
				} else {
					_operexe( confirm( t ) ? x.yes : x.no, this.commander, x.args );
				}
			}
		}
	}
} ),
/*  `confirm`  */
Confirm = define.widget( 'confirm', {
	Extend: Alert
} ),
_inst_cache = {},
_inster = function( a, b ) {
	if ( arguments.length == 2 ) {
		_inst_cache[ a ] && _inst_cache[ a ].hide();
		delete _inst_cache[ a ];
		_inst_cache[ a ] = b;
	} else
		return _inst_cache[ a ];
},
/*  `tip`  */
Tip = define.widget( 'tip', {
	Const: function( x, p ) {
		$.extend( x, { template: { prong: x.prong == N ? T : x.prong, cls: 'w-tip' + (x.closable !== F ? ' z-x' : ''), node: { type: 'template/view', height: '*' } },
			node: { type: 'html', text: '<div class=w-tip-text>' + (x.text && /^<\w+/g.test( x.text ) ? x.text : '<span class=f-va' + (x.closable ? ' style="padding-right:20px;"' : '') + '>' + $.strFormat( x.text || '', x.args ) + '</span><i class=f-vi></i>') + '</div>' + (x.closable !== F ? $.image('.f-i-close',{cls: 'w-tip-x', click:$.abbr + '.close(this)'}) : '') },
			pophide: T, independent: T, snap: p, snaptype: 'tb,rl,lr,bt,rr,ll,bb,tt,cc' } );
		Dialog.apply( this, arguments );
		! this.x.multiple && _inster( this.type, this );
	},
	Extend: Dialog,
	Prototype: {
		text: function( a ) {
			Q( '.w-tip-text', this.contentView.$() ).html( a );
		}
	}
} ),
/*  `loading`  */
Loading = define.widget( 'loading', {
	Const: function( x, p ) {
		$.extend( x, { width: x.node ? 200 : -1, independent: T } );
		Dialog.apply( this, arguments );
		_inster( this.type, this );
	},
	Extend: Dialog,
	Prototype: {
		className: 'w-dialog w-loading f-shadow',
		html_nodes: function() {
			if ( this.x.node ) {
				return Dialog.prototype.html_nodes.apply( this, arguments );
			} else {
				return '<cite class=_c>' + $.image( '%img%/loading-cir.gif' ) + ' <em class=_t>' + (this.x.text || Loc.loading) + '</em></cite><i class=f-vi></i>';
			}
		}
	}
} ),
// 以 src 为 key 存储 progress 实例。相同 src 的实例进程将被合并。
_progressCache = {},
/*  `progress`  */
/*  { type: 'progress', percent: 10, delay: 5, src: '' }   */
Progress = define.widget( 'progress', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		x.src && $.jsonArray( this, _progressCache, x.src );
	},
	Listener: {
		body: {
			ready: function() {
				var x = this.x, s = x.src, self = this;
				if ( s ) {
					this._timer = setTimeout( function() {
						// 相同 src 的实例，只让第一个去请求ajax
						if ( ! self._disposed && self.isHead() ) {
							self.ownerView.ajax( { src: s, context: this, success: function( x ) {
								// 返回数据可以是 command | {type:'progress'} | {nodes:[{type:'progress'}]}
								if ( _cmdHooks[ x.type ] ) {
									_cmdHooks[ x.type ].call( self, x );
								} else {
									for ( var i = 0, n = x.nodes || [ x ], l = n.length, d, o; i < l; i ++ ) {
										o = ((d = n[ i ]).id && self.ownerView.find( d.id )) || self;
										o.replace( ! d.id || o === self ? $.extend( d, { text: self.x.text } ) : d );
									}
								}
							} } );
						}
					}, x.delay * 1000 );
				}
			}
		}
	},
	Prototype: {
		stop: function() {
			clearTimeout( this._timer );
		},
		start: function() {
			this.triggerListener( 'ready' );
		},
		isHead: function() {
			return _progressCache[ this.x.src ][ 0 ] === this;
		},
		html_nodes: function() {
			var t = this.x.text, p = this.x.percent;
			return '<table id=' + this.id + 'g class="w-progress-table">' + (t != N ? '<tr><td colspan=2>' + t : '') +
				'<tr><td width=100%><div class=_bar><div class=_cur id=' + this.id + 'b style="width:' + p + '%"></div></div><td width=30><span class=_num id=' + this.id + 'p>' + p + '%</span></table>';
		},
		dispose: function() {
			this.stop();
			$.arrPop( _progressCache[ this.x.src ], this );
			_proto.dispose.call( this );
		}
	}
} ),
MenuSplit = define.widget( 'menu/split', {
	Prototype: {
		show: $.rt_null, hide: $.rt_null, elemht: function() { return 5 },
		html: function() {
			return '<div class=w-toggle-hr>&bnsp;</div>';
		}
	}
} ),
/* `menu` */
Menu = define.widget( 'menu', {
	Const: function( x, p, n ) {
		p == N && (p = _docView);
		W.call( this, x, p, n == N ? -1 : n );
		Dialog.all[ this.id ] = this;
		this._fitpos = T;
		(this.commander = p).addEventOnce( 'remove', this.remove, this );
	},
	Extend: Dialog,
	Child: 'menu/button',
	Default: { width: -1, height: -1, pophide: T },
	Listener: {
		body: {
			// menu的DOM渲染做两次，第一次测量并调整可用范围，第二次渲染menu/button。所以第一次装载完毕时不触发用户定义的load事件，等第二次渲染menu/button时再触发
			ready: $.rt_false,
			mousewheel: function( e ) {
				if ( this.$( 'up' ) )
					this.scroll( e.wheelDelta > 0 ? -1 : 1 );
				$.stop( e );
			},
			// 覆盖 dialog.Listener.body.click 方法，点击不作处理
			click: N
		}
	},
	Prototype: {
		NODE_ROOT: T,
		className: 'w-menu w-dialog',
		// menu的z-index固定为3，总在最前面
		front: $.rt_false,
		_front: $.rt_false,
		// menu有两种子节点: menu/split, menu/button
		x_type: function( t ) {
			return t && t.indexOf( 'menu/' ) !== 0 ? 'menu/' + t : 'menu/button';
		},
		_dft_pos: function() {
			return $.snap( this.width(), this.height(), $.point, '21,12', this._fitpos );
		},
		_hide: function() {
			if ( this.$() ) {
				var i = this.length;
				while ( i -- )
					this[ i ].hide();
				Dialog.prototype._hide.call( this );
			}
		},
		listenHide: function( a ) {
			this.type === 'menu' && Dialog.prototype.listenHide.call( this, a );
		},
		// 检查是否有高度溢出，并填充内容
		checkOverflow: function() {
			this.echoStart = 0;
			this.echoEnd   = this.length -1;
			var r = this._pos, m = 0, g = this.$( 'g' ), b = MenuButton.prototype.elemht();
			if ( r.top < 0 )
				m -= r.top;
			if ( r.bottom < 0 )
				m -= r.bottom;
			if ( m ) {
				// realht 是按钮可展现区域的高度 / 22 = 上下两个翻页按钮高度 + menu的padding border高度
				this.realht = g.offsetHeight - 22 - m;
				this.realht -= this.realht % b;
				g.style.height = this.realht + 'px';
				$.before( g, '<div id=' + this.id + 'up class="_ar" onclick=' + evw + '.scroll(-1)>' + $.arrow( 't2' ) + '<i class=f-vi></i></div>' );
				$.after( g,  '<div id=' + this.id + 'dn class="_ar" onclick=' + evw + '.scroll(1)>'  + $.arrow( 'b2' ) + '<i class=f-vi></i></div>' );
				$.classAdd( this.$(), 'z-scroll' );
				// 如果有设置focusIndex, 需要让这个menu/button滚动显示在中间
				var f = this.x.focusIndex;
				if ( f != N && f > 0 && f < this.length ) {
					var j = f - 1, k = f + 1, l = this.length, h = this[ f ].elemht();
					while ( j > - 1 || k < l ) {
						if ( ( j > -1 && (h += this[ j -- ].elemht()) >= this.realht ) || ( k < l && (h += this[ k ++ ].elemht()) >= this.realht ) )
							break;
					}
					this.echoStart = j + 1, this.echoEnd = k - 1;
				}
			} else
				this.realht = this.virht;
			this.fill();
		},
		scroll: function( a ) {
			if ( $.classAny( this.$( a > 0 ? 'dn': 'up' ), 'z-us' ) ) {
				var i = a > 0 ? this.echoEnd : this.echoStart, l = this.length, c = 0;
				for ( ; i > -1 && i < l; i += ( a === 0 ? 1 : a ) ) {
					c += this[ i ].elemht();
					if ( c > this.realht )
						break;
				}
				a > 0 ? (this.echoStart = this.echoEnd, this.echoEnd = Math.min( i, l - 1 )) : (this.echoEnd = this.echoStart, this.echoStart = Math.max( 0, i ));
				if ( c < this.realht ) {
					for ( i = this.echoEnd; i < l; i ++ ) {
						c += this[ i ].elemht();
						if ( c > this.realht )
							break;
					}
					this.echoEnd = Math.min( i, l - 1 );
				}
				this.fill();
			}
		},
		fill: function() {
			for ( var i = this.echoStart, l = this.echoEnd, s = '', e = h = 0, m = this.realht; i <= l; i ++ ) {
				h += this[ i ].elemht();
				if ( h > m ) {
					this.echoEnd = i - 1;
					break;
				}
				s += this[ i ].html();
			}
			this.$( 'g' ).innerHTML = s;
			if ( this.realht !== this.virht ) {
				$.classAdd( this.$( 'up' ), 'z-us', this.echoStart > 0 ? T : F );
				$.classAdd( this.$( 'dn' ), 'z-us', this.echoEnd === this.length - 1 && (this.$( 'g' ).offsetHeight === this.$( 'g' ).scrollHeight) ? F : T );
			}
		},
		html_nodes: function() {
			//取最长的一个做snap测试
			for ( var i = 0, l = this.length, m = 0, t, j; i < l; i ++ ) {
				t = (this[ i ].x.text || '').length;
				m <= t && (m = t, j = i);
			}
			var s = '<div class=_g id=' + this.id + 'g onmousewheel=' + eve + ' style="max-width:' + (Math.floor(($.width() / 2)) - 2) + 'px;height:' + this.virht + 'px">' + ( j != N ? this[ j ].html() : '' ) + '</div>';
			return ie7 ? '<table cellspacing=0 cellpadding=0 border=0><tr><td>' + s + '</table>' : s;
		},
		render: function() {
			if ( this.type === 'menu' )
				_inster( this.type, this );
			for ( var i = 0, l = this.length, e = h = 0, m = $.height(); i < l; i ++ ) {
				h += this[ i ].elemht();
				if ( h > m && ! e )
					this.echoEnd = e = i;
			}
			this.virht = h;
			Dialog.prototype.render.call( this );
			this.checkOverflow();
			if ( this.type === 'menu' ) {
				// 如果 menu 是从对话框中点出来的，那么把menu的DOM对象放置到dialog中去。以便dialog中的元素的z-index属性能对menu产生效果
				var a = $.dialog( this.parentNode ), w = 0;
				if ( a ) {
					for ( var i = 0, b = $.offset( a.$() ), c = [ 'top', 'left', 'right', 'bottom' ]; i < c.length; i ++ )
						this.$().style[ c[ i ] ] && $.css( this.$(), c[ i ], '-=' + (b[ c[ i ] ] + 1) );
					w = this.$().offsetWidth;
					$.append( a.$(), this.$() );
				}
				this.x.line && (w = Math.max( this._pos.target.width - 2, w ));
				$.css( this.$(), 'min-width', w );
			}
			this.triggerHandler( 'ready' );
			return this;
		}
	}
} ),
/* `submenu` */
SubMenu = define.widget( 'submenu', {
	Extend: Menu,
	Prototype: {
		className: 'w-menu w-submenu w-dialog',
		_snaptype: '21,12'
	}
} );
define.widget( 'deck/button', {
	Const: function( x, p ) {
		var d = p.parentNode,
			h = d.x.buttonheight;
		// 如果button没有定义这些参数，就加上系统默认的参数值
		$.extend( x, { width: '*', height: h, cls: 'f-deck-button', hmin: 2, focusable: T } );
		Button.apply( this, arguments );
	},
	Extend: Button,
	Listener: {
		body: {
			click: function() {
				var f = this.parentNode.parentNode.getFocus();
				if ( f && f !== this ) {
					f.parentNode.height( this.parentNode.parentNode.x.buttonheight - 1 );
					f.focus( F );
				}
				if ( ! this.isFocus() ) {
					this.parentNode.height( '*' );
					this.focus();
				}
			}
		}
	}
} );
define.widget( 'deck/item', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		x.height = x.button.focus ? '*' : p.x.buttonheight - 1;
		this.button  = this.add( x.button );
		this.content = this.add( x.content );
	},
	Extend: 'vert',
	Prototype: {
		className: 'w-vert f-rel',
		x_type: function( t ) {
			return t === 'button' ? 'deck/' + t : t;
		}
	}
} );
/* `deck` */
define.widget( 'deck', {
	Extend: 'vert',
	Child: 'deck/item',
	Prototype: {
		x_nodes: function() {
			for ( var i = 0, d = this.x.nodes, l = d.length, r = []; i < l; i += 2 )
				r.push( { button: d[ i ], content: d[ i + 1 ] } );
			return r;
		},
		getFocus: function() {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].button.isFocus() ) return this[ i ].button;
		}
	}
} );

/* 表单控件部分 包括输入表单和按钮 */
var
_dft_min = 2,
_z_on = function( a ) {
	if ( this.isNormal() ) {
		$.classAdd( this.$(), 'z-on', a );
		a !== F && this.warn( F );
		var c = this.isEmpty();
		this.x.placeholder && this.$( 'ph' ) && $.classAdd( this.$( 'ph' ), 'f-none', a !== F || ! c );
	}
},
// /@ a -> valid object, b -> valid code, c -> args
_form_err = function( a, b, c ) {
	return { wid: this.id, name: this.x.name, code: b, label: this.x.label, text: ( a && a[ b + 'text' ] ) || Loc.ps.apply( N, [ Loc.form[ b === 'required' && ! this.x.label ? 'complete_required' : b ], this.x.label || Loc.field ].concat( c || [] ) ) || '' };
},
_valid_err = function( b, v ) {
	if ( typeof v !== _STR )
		v = $.isArray( v ) ? v.join( ',' ) : ('' + v);
	v = $.strTrim( v );
	var k = this.validHooks || O, c, d, e;
	if ( k.valid && (e = k.valid.call( this, b, v )) )
		return e;
	if ( ! b )
		return;
	if ( b.required && (k.required ? k.required.call( this, b, v ) : (! v)) )
		return _form_err.call( this, b, 'required' );
	if ( b.minlength && (k.minlength ? k.minlength.call( this, b, v ) : ((c = $.strLen( v )) < b.minlength)) )
		return _form_err.call( this, b, 'minlength', [ b.minlength ] );
	if ( b.maxlength && (k.maxlength ? k.maxlength.call( this, b, v ) : ((c = $.strLen( v )) > b.maxlength)) )
		return _form_err.call( this, b, 'maxlength', [ c - b.maxlength ] );
	if ( b.minvalue && v && (k.minvalue ? k.minvalue.call( this, b, v ) : (v < b.minvalue)) )
		return _form_err.call( this, b, 'minvalue', [ b.minvalue ] );		
	if ( b.maxvalue && v && (k.maxvalue ? k.maxvalue.call( this, b, v ) : (v > b.maxvalue)) )
		return _form_err.call( this, b, 'maxvalue', [ b.maxvalue ] );
	if ( b.minsize && v && (k.minsize ? k.minsize.call( this, b, v ) : (v.split( ',' ).length < _number( b.minsize ))) )
		return _form_err.call( this, b, 'minsize', [ b.minsize ] );
	if ( b.maxsize && v && (k.maxsize ? k.maxsize.call( this, b, v ) : (v.split( ',' ).length > _number( b.maxsize ))) )
		return _form_err.call( this, b, 'maxsize', [ b.maxsize ] );
	if ( b.pattern && v && ! eval( b.pattern + '.test(v)' ) )
		return _form_err.call( this, b, 'pattern' );
	if ( b.compare && v && (c = this.ownerView.f( b.compare )) && (d = c.val()) && ( k.compare ? k.compare.call( this, b, v, c, d ) : ! eval( '"' + $.strQuot( v ) + '"' + (b.comparemode || '==') + '"' + $.strQuot( d ) + '"' ) ) )
			return _form_err.call( this, b, 'compare', [ b.comparemode, c.x.label ] );
	if ( b.method && (d = Function( b.method ).call( this, v, b )) )
		return { wid: this.id, name: this.x.name, code: 'method', text: d };
},
_valid_opt = function( a ) {
	return a == N || a === T ? this.x.validate : a === F ? N : typeof a === _OBJ ? a : (this.x.validategroup && this.x.validategroup[ a ]);
},
_enter_submit = function( k, a ) {
	k === 13 && a.ownerView.submitButton && a.ownerView.submitButton.click();
},
// 检查是否正在用中文输入法
_listen_ime = function( a, b ) {
	$.attach( b || a.$t(), 'compositionstart', function() { a._imeMode = T } );
	$.attach( b || a.$t(), 'compositionend',   function() { a._imeMode = F } );
},
// 检测表单高度和缩进
_input_indent_value,
_input_indent = function() {
	if ( ! _input_indent_value ) {
		br.chdiv( 'w-form w-input w-text', '<input class=_t style=width:0>', function() {
			_input_indent_value = this.children[ 0 ].offsetWidth || 5;
		} );
	}
	return _input_indent_value;
},
/* `absform` */
AbsForm = define.widget( 'abs/form', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.className += ' w-' + this.type.replace( '/', '-' );
	},
	Listener: {
		tag: 't',
		body: {
			focus: {
				prop: T,
				method: function( e ) {
					e == N && this.$t().focus();
					_z_on.call( this );
				}
			},
			blur: {
				prop: T,
				method: function() { ! this.contains( document.activeElement ) && _z_on.call( this, F ) }
			},
			valid: function( e, a ) {
				return this.getValidError( a );
			},
			error: function( e, a ) {
				typeof a === _OBJ ? this.cmd( a ) : this.warn( a );
			}
		}
	},
	Default: { height: -1 },
	Prototype: {
		isFormWidget: T,
		className: 'w-form',
		_warncls: '',
		validHooks: F,
		usa: function() {
			return this.isNormal();
		},
		$v: function() {
			return this.$t();
		},
		$t: function() {
			return $( this.id + 't' );
		},
		input_name: function() {
			return this.x.name || '';
		},
		isEmpty: function() {
			return !(this.val() || this.text());
		},
		// @a -> original?
		isModified: function( a ) {
			var u = this.x.value, v = a ? u : this._modval;
			return this.val() != (v == N ? (u == N ? '' : u) : v);
		},
		saveModified: function() {
			this._modval = this.$v().value || '';
		},
		//@implement
		attrSetter: function( a, b ) {
			if ( a == 'readonly' )
				this.readonly( b );
			else if ( a == 'validonly' )
				this.validonly( b );
			else if ( a == 'disabled' )
				this.disable( b );
			else if ( a == 'normal' )
				this.normal( b );
		},
		normal: function() {
			this.x.status = 'normal';
			this.$v().removeAttribute( 'readOnly' );
			this.$v().removeAttribute( 'disabled' );
			$.classRemove( this.$(), 'z-ds' );
			return this;
		},
		readonly: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'readonly' : '';
			this.$v().readOnly = a;
			this.$v().removeAttribute( 'disabled' );
			$.classAdd( this.$(), 'z-ds', a );
			return this;
		},
		validonly: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'validonly' : '';
			this.$v().readOnly = a;
			this.$v().removeAttribute( 'disabled' );
			$.classAdd( this.$(), 'z-ds', a );
			return this;
		},
		disable: function( a ) {
			a = a == N || a;
			_proto.disable.call( this, a );
			this.$v().disabled = a;
			this.$v().removeAttribute( 'readOnly' );
			return this;
		},
		isReadonly: function() {
			return this.x.status === 'readonly';
		},
		isValidonly: function() {
			return this.x.status === 'validonly';
		},
		// @a -> valid name
		getValidError: function( a ) {
			if ( this.isNormal() || this.isValidonly() )
				return _valid_err.call( this, _valid_opt.call( this, a ), this.val() );
		},
		// @a -> validate object, b -> group name
		setValidate: function( a, b ) {
			var x = this.x, c;
			if ( b ) {
				c = (x.validategroup || (x.validategroup = {}));
				a ? $.merge( (c[ b ] || (c[ b ] = {})), a ) : (c[ b ] = N);
			} else {
				c = (x.validate || (x.validate = {}));
				a ? $.merge( c, a ) : (x.validate = N);
				this.$() && $.classAdd( this.$(), 'z-required', !!c.required );
			}
			return this;
		},
		// @a -> valid item name, b -> group name, v -> value
		valid: function( a, b, v ) {
			var c = b;
			if ( a ) {
				var d = b ? (this.x.validategroup || F)[ b ] : this.x.validate, c = {};
				c[ a ] = d[ a ];
				c[ a + 'text' ] = d[ a + 'text' ];
			}
			var e = _valid_err.call( this, _valid_opt.call( this, c ), v !== U ? v : this.val() );
			this.ownerView._valid( e && $.jsonChain( e, {}, e.wid ) );
			return ! e;
		},
		val: function( a ) {
			if ( a == N )
				return this.$v() ? this.$v().value : this.x.value;
			if ( this.$() ) {
				this.$v().value = a;
				this.$v() && this.resetEffect();
				this.trigger( 'change' );
			} else
				this.x.value = a;
		},
		text: function() {
			return this.val();
		},
		// @a -> T/F/className
		warn: function( a ) {
			if ( this._warncls ) {
				$.classRemove( this.$(), this._warncls );
				this._warncls = N;
			}
			if ( typeof a === _STR ) {
				$.classAdd( this.$(), this._warncls = a );
			} else
				$.classAdd( this.$(), 'z-err', a );
			return this;
		},
		reset: function( a ) {
			this.val( a || this.x.value == N ? '' : this.x.value );
		},
		resetEffect: function() {
			if ( this.x.placeholder && this.$( 'ph' ) )
				$.classAdd( this.$( 'ph' ), 'f-none', !! this.val() || this.$().contains( document.activeElement ) );
		},
		form_prop: function( a ) {
			var t = '', w = this.innerWidth(), h = this.innerHeight();
			if ( w != N )
				t += 'width:' + (w - this.width_minus()) + 'px;';
			if ( h != N )
				t += 'height:' + (h - this.height_minus()) + 'px;';
			if ( t )
				t = ' style="' + t + '"';
			return ' id="' + this.id + 't" class=_t name="' + this.input_name() + '"' + (this.x.tip ? ' title="' + $.strQuot((this.x.tip === T ? (this.x.text || this.x.value) : this.x.tip) || '') + '"' : '') +
				(this.isReadonly() || this.isValidonly() ? ' readonly' : '') + (this.isDisabled() ? ' disabled' : '') + (a === F ? '' : ' value="' + $.strEscape(this.x.value == N ? '' : '' + this.x.value) + '"') + t + _html_on( this );
		}
	}
} ),
/* `absinput` */
AbsInput = define.widget( 'abs/input', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		if ( x.cls || x.style || x.transparent ) {
			this.defaults( _size_fix( x.cls, x.style, x.transparent ? 0 : this.Const.Default.wmin, x.transparent ? 0 : this.Const.Default.hmin ) );
			x.transparent && (this.className += ' z-trans');
		}
		if ( x.validate && x.validate.required )
			this.className += ' z-required';
	},
	Extend: AbsForm,
	Listener: {
		range: 'input',
		body: {
			change: {
				proxy: ie ? 'paste keyup' : 'input',
				block: $.rt_true,
				prop: function() {
					return (this.x.validate && this.x.validate.maxlength && cfg.input_detect && cfg.input_detect.maxlength) || this.x.tip === T || this.x.placeholder;
				},
				method: function( e ) {
					if ( this.x.tip === T ) {
						this.$t().title = this.text();
					}
					var m = cfg.input_detect && cfg.input_detect.maxlength && this.x.validate && this.x.validate.maxlength, v = this.val(), u = v, c = this.x.on && this.x.on.change;
					if ( this.lastValue === U )
						this.lastValue = this.x.value;
					if ( this.lastValue != v ) {
						this.lastValue = v;
						var e = m && $.strLen( v ) > m;
						e && this.val( $.strSlice( v, m ) );
						if ( c ) {
							var self = this;
							clearTimeout( this._change_timer );
							this._change_timer = setTimeout( function() { $.fncall( c, self ) }, 300 );
						}
						v && this.$( 'ph' ) && $.classAdd( this.$( 'ph' ), 'f-none' );
						Dialog.close( this.id + 'mxltip' );
						e && this.cmd( { type: 'tip', id: this.id + 'mxltip', text: Loc.form.reach_maxlength } );
					}
				}
			},
			resize: function() {
				var w = this.innerWidth();
				if ( w > 0 && this.$t() ) {
					w = Math.max( w - this.width_minus(), 0 );
					this.$t().style.width = w + 'px';
					this.$( 'ph' ) && (this.$( 'ph' ).style.width = w + 'px');
				}
			}
		}
	},
	Default: {
		wmin: _dft_min, hmin: _dft_min
	},
	Prototype: {
		className: 'w-form w-input',
		width_minus:  function() { return _input_indent() },
		height_minus: function() { return 0 },
		focus: function( a ) {
			this.$t()[ a === F ? 'blur' : 'focus' ]();
			_z_on.call( this, a == N || a );
		},
		focusEnd: function() {
			this.focus();
			this.cursorEnd();
		},
		cursorEnd: function() {
			$.rngCursor( this.$t() );
		},
		clkhdr: function( e ) {
			this.focus();
			this.trigger( e );
		},
		html_placehoder: function() {
			var w = this.innerWidth(), v = this.x.value;
			return this.x.placeholder ? '<label style="width:' + ( w ? (w - this.width_minus()) + 'px' : 'auto' ) + '" class="w-input-placeholder f-fix' + ( v != N && v !== '' ? ' f-none' : '' ) +
				'" id="' + this.id + 'ph" onclick=' + evw + '.clkhdr(event) ondblclick=' + evw + '.clkhdr(event)><i class=f-vi></i><span class=f-va id="' + this.id + 'pht">' + this.x.placeholder + '</span></label>' : '';
		},
		html: function() {
			return '<div' + this.html_prop() + '>' + this.html_before() + this.html_placehoder() + this.html_nodes() + this.html_after() + '</div>';
		}
	}
} ),
/* `hidden` */
Hidden = define.widget( 'hidden', {
	Const: function( x ) {
		x.width = x.height = 0;
		W.apply( this, arguments );
	},
	Extend: AbsForm,
	Prototype: {
		isModified: $.rt_false,
		saveModified: $.rt_false,
		readonly: $.rt_null,
		validonly: $.rt_null,
		normal: function() { this.disable( F ) },
		disable: function( a ) {
			_proto.disable.call( this, a );
			this.$().disabled = a == N || a;
		},
		val: function( a ) {
			return a === U ? ($( this.id ) || this.x).value : (($( this.id ) || this.x).value = a);
		},
		html: function() {
			return '<input type=hidden id="' + this.id + '" name="' + this.input_name() + ( this.isDisabled() ? ' disabled' : '' ) + '" value="' + $.strQuot(this.x.value || '') + '"' + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + '>';
		}
	}
} ),
/* `textarea` */
Textarea = define.widget( 'textarea', {
	Extend: AbsInput,
	Default: { height: 60 },
	Prototype: {
		isModified: function( a ) {
			var v = a ? this.x.value : this._modval;
			v == N && (v = this.x.value || '');
			return this.val().replace( /\r\n/g, '\n' ) != v.replace( /\r\n/g, '\n' );
		},
		html_nodes: function() {
			return '<textarea' + this.form_prop( F ) + '>' + $.strEscape(this.x.value || '').replace( /<\/textarea>/g, '&lt;\/textarea&gt;' ) + '</textarea>';
		}
	}
} ),
/* `text` */
Text = define.widget( 'text', {
	Extend: AbsInput,
	Listener: {
		body: {
			keyup: {
				prop: T,
				method: function( e ) { _enter_submit( e.keyCode, this ); }
			}
		}
	},
	Prototype: {
		fixhdr: function() {
			this.x.placeholder && $.classAdd( this.$( 'ph' ), 'f-none', this.$t().value != '' );
		},
		html_nodes: function() {
			return '<input type=' + (this.formType || this.type) + this.form_prop() + '>';
		}
	}
} ),
/* `password` */
Password = define.widget( 'password', {
	Extend: Text,
	Prototype: {
		form_prop: function() { return AbsForm.prototype.form_prop.call( this ) + (this.x.autocomplete === T ? '' : ' autocomplete="new-password"'); }
	}
} ),
/* `checkboxgroup` */
CheckboxGroup = define.widget( 'checkboxgroup', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		if ( x.targets ) {
			for ( var i = 0, l = x.targets.length, t = l && [], o; i < l; i ++ ) {
				t.push( o = this.add( x.targets[ i ], -1 ) );
				this[ i ] && (this[ i ].x.target = o);
			}
			this.targets = t;
		}
		this.childCls = x.dir === 'v' ? 'f-bl' : 'f-va f-inbl';
	},
	Extend: AbsForm,
	Child: 'checkbox',
	Default: {
		wmin: 2, hmin: 2
	},
	Listener: {
		range: 'option'
	},
	Prototype: {
		className: 'w-form f-oh',
		NODE_ROOT: T,
		type_horz: T,
		_boxgroup: T,
		x_nodes: function() {
			return this.x.options || [ { value: this.x.value, text: this.x.text, checked: this.x.checked, target: this.x.target } ];
		},
		val: function( a ) {
			if ( a == N )
				return $.arrSelect( this.elements( T, T ), 'v.value', T ).join( ',' );
			this.elements().each( function() { this.checked = $.idsAny( a, this.value ) } );
			this.trigger( 'change' );
		},
		// @a -> valid name
		getValidError: function( a ) {
			var i = this.length;
			while ( i -- ) {
				if ( this[ i ].isNormal() || this[ i ].isValidonly() )
					return _valid_err.call( this, _valid_opt.call( this, a ), this.val() );
			}
		},
		// 设置事件  /@a -> event type, b -> fn
		setOn: function( a, b ) {
			$.jsonChain( b, this.x, 'on', a );
			for ( var i = 0; i < this.length; i ++ )
				this[ i ].setOn( a, b );
			return this;
		},
		// @a -> T/F 是否只获取所有选中项
		elements: function( a, b ) {
			return this[ 0 ] ? this[ 0 ].elements( a, b ) : Q([]);
		},
		getOptions: function( a ) {
			return this.elements( a ).map( function() { return _widget( this ) } );
		},
		// 点击 target 时直接切换选项
		evwClickList: function( i, e ) {
			if ( this[ i ].isNormal() && ! this[ i ].isChecked() && ! this[ i ].$().contains( e.srcElement ) ) {
				this[ i ].check();
				try { _widget( e.srcElement ).focus(); } catch( ex ) {}
				Q( e.srcElement ).click();
			}
		},
		checkAll: function( a ) {
			this.getOptions().each( function() { this.check( a ) } );
		},
		normal: function() {
			this.getOptions().each( function() { this.normal() } );
			this.x.status = 'normal';
		},
		disable: function( a ) {
			this.getOptions().each( function() { this.disable( a ) } );
			this.x.status = a == N || a ? 'disabled' : '';
		},
		readonly: function( a ) {
			this.getOptions().each( function() { this.readonly( a ) } );
			this.x.status = a == N || a ? 'readonly' : '';
		},
		validonly: function( a ) {
			this.getOptions().each( function() { this.validonly( a ) } );
			this.x.validonly = a == N || a ? 'validonly' : '';
		},
		scaleWidth: function( a ) {
			if ( a.nodeIndex < 0 ) {
				var i = $.arrIndex( this.targets, a ), c = $.scale( this.innerWidth(), [ this[ i ] ? this[ i ].width() : 0, a.attr( 'width' ) || '*' ] );
				return c[ 1 ];
			} else
				return AbsForm.prototype.scaleWidth.call( this, a );
		},
		html_nodes: function() {
			if ( this.targets ) {
				for ( var i = 0, s = '', l = Math.max( this.length, this.x.targets.length ); i < l; i ++ )
					s += '<div class=w-' + this.type + '-list onclick=' + evw + '.evwClickList(' + i + ',event) style=margin-bottom:' + (i < l - 1 ? (this.x.space != N ? this.x.space : 10) : 0) + 'px>' + (this[ i ] ? this[ i ].html() : '') + (this.targets[ i ] ? this.targets[ i ].html() : '') + '</div>';
				return s;
			} else
				return AbsForm.prototype.html_nodes.call( this );
		}
	}
} ),
/* `checkbox` */
Checkbox = define.widget( 'checkbox', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		if ( p && p._boxgroup ) {
			p.x.on && ! this.x.on && (this.x.on = p.x.on);
			p.x.status && ! this.x.status && (this.x.status = p.x.status);
			this.defaults( { wmin: 7, width: p.x.targets ? 62 : -1 } );
		}
		this._dft_modchk = this._modchk = !!(x.checked != N ? x.checked : (p && p._boxgroup && p.x.value && x.value && $.idsAny( p.x.value, x.value )));
	},
	Extend: AbsForm,
	Helper: {
		parseOption: function( p, f ) {
			var b = $.extend( {}, p.x.box, { type: 'checkbox' }, f ), d = p.x.data, g = b.field, j;
			for ( j in g )
				d && (g[ j ] in d) && (b[ j ] = d[ g[ j ] ]);
			return p.add( b, -1 );
		}
	},
	Listener: {
		range: 'option',
		body: {
			ready: function() {
				this.x.target && this._ustag();
			},
			change: {
				prop: T,
				block: function() { return ! this.isNormal() },
				method: function() {
					this.isNormal() && this.elements( '[w-target]' ).each( function() { _widget( this )._ustag() } );
				}
			},
			click: {
				prop: T,
				block: function() { return ! this.isNormal() },
				method: function( e ) {
					if ( this.isReadonly() || this.isValidonly() ) {
						this.elements().each( function() { this.checked = this.defaultChecked } );
						return F;
					}
					if ( ie && e.type ) { // 修复ie下onchange失效问题
						var o = document.activeElement;
						this.$t().blur(), this.$t().focus();
						o.focus();
					}
					this.parentNode.warn && this.parentNode.warn( F );
					this.attr( 'bubble' ) === F && $.cancel( e );
				}
			},
			resize: function() {
				if ( this.x.nobr && ! this.innerWidth() && this.parentNode.innerWidth() )
					this.$().style.maxWidth = this.parentNode.innerWidth() + 'px';
			}
		}
	},
	Default: { width: -1, wmin: 1 },
	Rooter: 'checkboxgroup',
	Prototype: {
		className: 'w-form',
		formType: 'checkbox',
		getValidError: function( a ) {
			return this.parentNode._boxgroup && this.parentNode.getValidError( a );
		},
		_ustag: function( a ) {
			this.ownerView.linkTarget( this.x.target, ! this.isDisabled() && this.isChecked() );
		},
		elements: function( a, b ) {
			return Q( '[name="' + this.input_name() + '"]' + (b ? ':not(:disabled)' : '') + (a === T ? ':checked' : a === F ? ':not(:checked)' : (a || '')), this.ownerView.$() );
		},
		input_name: function() {
			return this.x.name || this.parentNode.x.name || '';
		},
		check: function( a ) {
			var b = this.$t().checked;
			this.$t().checked = (a = a == N || a);
			(a != b) && this.trigger( 'change' );
		},
		checkstate: function( a ) {
			this.check( a == 1 || a == N || a === T ? T : F );
		},
		// a -> true返回所有选中项，false -> 返回所有为选项，不填写本参数，返回所有兄弟项
		getSibling: function( a ) {
			for ( var i = 0, b = this.elements( a ), c, r = []; i < b.length; i ++ ) {
				if ( (c = $.widget( b[ i ] )) && b[ i ].id == c.id + 't' ) r.push( c );
			}
			return r;
		},
		isChecked: function() {
			return this.$t().checked;
		},
		isModified: function( a ) {
			return this.isChecked() !== (a ? this._dft_modchk : this._modchk);
		},
		saveModified: function() {
			this._modchk = this.isChecked();
		},
		click: function( a ) {
			this.check( a == N ? ! this.isChecked() : a );
			this.trigger( 'click' );
		},
		mode: function( a ) {
			if ( a == N )
				return this.$t().checked ? 1 : 0;
			this.check( a == 1 ? T : F );
		},
		val: CheckboxGroup.prototype.val,
		selfVal: function() {
			var t = this.$t();
			return t.disabled || ! t.checked ? '' : t.value;
		},
		htmlFor: function( a, e ) {
			this.$t().focus(); // for ie9-
			a.previousSibling.click();
			$.cancel( e );
		},
		readonly: function( a ) {
			AbsForm.prototype.readonly.call( this, a );	
			this.$t().defaultChecked = this.$t().checked;
		},
		validonly: function( a ) {
			AbsForm.prototype.validonly.call( this, a );	
			this.$t().defaultChecked = this.$t().checked;
		},
		disable: function( a ) {
			AbsForm.prototype.disable.call( this, a );
			this.x.target && this._ustag();
		},
		// @a -> 设置为true, 强制清空
		reset: function( a ) {
			if ( a ) {
				this.val( '' );
			} else {
				var c = this.$t().checked, d = this.$t().defaultChecked;
				this.$t().checked = d;
				if ( c != d && this.parentNode.type.indexOf( 'group' ) > 0 ) {
					this.parentNode.targets && this.check();
					this.parentNode.trigger( 'change' );
				}
			}
		},
		tip: function() {
			this.cmd( $.extend( {}, this.x.tip, { type: 'tip', hoverdrop: true } ) );
		},
		html: function() {
			var p = this.parentNode, c = this.className, g = p.type_horz && (!p._boxgroup || p.targets), w = this.innerWidth(),
				k = this._modchk, s = this.prop_cls(), t = this.x.tip, y = '';
			if ( w ) {
				y += 'width:' + w + 'px;';
			} else {
				if ( this.x.nobr && (w = p.innerWidth()) )
					y += 'max-width:' + w + 'px;';
				s = s.replace( / f-inbl/g, '' );
			}
			this.x.style && (y += this.x.style);
			return '<cite id=' + this.id + ' class="' + s + (this.x.nobr ? ' f-inbl f-fix' : '') + '"' + (t && typeof t !== _OBJ ? 'title="' + $.strQuot( (t === T ? this.x.text : this.x.tip) || '' ) + '"' : '') + (y ? ' style="' + y + '"' : '') + (this.x.id ? ' w-id="' + this.x.id + '"' : '') +
				'><input id=' + this.id + 't type=' + this.formType + ' name="' + this.input_name() + '" value="' + $.strQuot(this.x.value || '') + '" class=_t' + (k ? ' checked' : '') + (this.isDisabled() ? ' disabled' : '') + (this.type === 'radio' ? ' w-name="' + (p.x.name || this.x.name || '') + '"' : '') + 
				(this.x.target ? ' w-target="' + ((this.x.target.x && this.x.target.x.id) || this.x.target.id || this.x.target)+ '"' : '') + _html_on( this ) + '>' + (br.css3 ? '<label for=' + this.id + 't onclick=' + $.abbr + '.cancel()></label>' : '') +
				( this.x.text ? '<span class=_tit onclick="' + evw + '.htmlFor(this,event)"' + (t && typeof t === _OBJ ? ' onmouseover="' + evw + '.tip()"' : '') + '>' + ((this.x.escape != N ? this.x.escape : p.x.escape) ? $.strEscape( this.x.text ) : this.x.text) + '</span>' : '' ) + (g ? '<i class=f-vi></i>' : '') + '</cite>';
		}
	}
} ),
/* `triplebox`
 *	checkstate: 0(未选),1(已选),2(半选)
 */
Triplebox = define.widget( 'triplebox', {
	Extend: 'checkbox',
	Listener: {
		body: {
			ready: function() {
				if ( this.x.checkstate == 2 ) 
					this.$t().indeterminate = T;
			},
			click: {
				prop: T,
				method: function() {
					Checkbox.Listener.body.click.method.apply( this, arguments );
					if ( ie && this.$t().value == 2 ) // IE半选状态点击不会触发onchange，需要手动触发
						this.checkstate( this.isChecked() ? 1 : 0 );
					$.classAdd( this.$(), 'z-half', this.checkstate() == 2 );
					this.relate();
				}
			}
		}
	},
	Prototype: {
		className: 'w-checkbox',
		checkstate: function( a ) {
			var b = this.$t().checked ? 1 : this.$t().indeterminate ? 2 : 0;
			if ( a == N )
				return b;
			this.$t().checked = a == 1;
			this.$t().indeterminate = a == 2;
			$.classAdd( this.$(), 'z-half', a == 2 );
			a != b && this.trigger( 'change' );
		},
		check: function( a ) {
			this.checkstate( a === F ? 0 : (a === T || a == N) ? 1 : a );
			this.relate();
		},
		relate: function() {
			if ( this.x.name ) {
				for ( var i = 0, b = this.ownerView.fAll( this.x.name ), l = b.length, c0 = 0, c1 = 0, c2 = 0, d; i < l; i ++ ) {
					if ( this.x.checkall ) {
						this != b[ i ] && b[ i ].checkstate( this.isChecked() );
					} else if ( b[ i ].x.checkall ) {
						d = b[ i ];
					} else {
						b[ i ].isChecked() ? (c1 ++) : b[ i ].checkstate() == 2 ? (c2 ++) : (c0 ++);
					}
				}
				if ( d && (c0 + c1 + c2) )
					d.checkstate( c0 == l - 1 ? 0 : c1 == l - 1 ? 1 : 2 );
			}			
		},
		html: function() {
			return '<cite id=' + this.id + ' class="' + this.prop_cls() + (this.x.checkstate == 2 ? ' z-half' : '') + '"' + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + '><input type=checkbox id=' + this.id + 't name="' + this.x.name + '" value="' + (this.x.value || '') + '" class=_t' +
				(this.x.checkstate == 1 ? ' checked' : '') + (this.isDisabled() ? ' disabled' : '') + (this.x.partialsubmit ? ' w-partialsubmit="1"' : '') + _html_on( this ) + '>' + (br.css3 ? '<label for=' + this.id + 't onclick=' + $.abbr + '.cancel()></label>' : '') + (this.x.text ? '<span class=_tit onclick="' + evw + '.htmlFor(this,event)">' + this.x.text + '</span>' : '') + '</cite>';
		}
	}
} ),
/* `radiogroup` */
Radiogroup = define.widget( 'radiogroup', {
	Extend: 'checkboxgroup',
	Child: 'radio'
} ),
/* `radio` */
Radio = define.widget( 'radio', {
	Extend: 'checkbox',
	Rooter: 'radiogroup',
	Prototype: {
		formType: 'radio',
		// 为避免页面内出现相同name的radio组(如果同name，选中效果会出问题)，需要给name加上前缀
		input_name: function() {
			return this.ownerView.id + 'radio@' + (this.x.name || this.parentNode.x.name || this.parentNode.id);
		},
		click: function( a ) {
			this.check( a == N || a );
			this.trigger( 'click' );
		}
	}
} ),
/* `select` */
Select = define.widget( 'select', {
	Const: function( x ) {
		AbsInput.apply( this, arguments );
		x.size && (this.className += ' w-select-multiple');
	},
	Extend: AbsInput,
	Default: { width: -1 },
	Listener: {
		range: 'option',
		body: {
			change: {
				prop: T,
				proxy: N,
				block: N,
				method: function() {
					this.warn( F );
					if ( this.x.tip === T )
						this.$t().title = this.text();
				}
			}
		}
	},
	Prototype: {
		width_minus: function() { return 0 },
		isEmpty: function() {
			return this.$t().selectedIndex !== -1 && (this.val() || this.text());
		},
		text: function() { return this.$t().selectedIndex !== -1 && this.$t().options[ this.$t().selectedIndex ].text },
		val: function( a ) {
			if ( this.$() ) {
				if ( a == N )
					return this.$t().selectedIndex !== -1 && this.$t().options[ this.$t().selectedIndex ].getAttribute( 'value' );
				var b = this.$t().value;
				this.$t().value = a;
				b != a && this.trigger( 'change' );
			} else {
				if ( a == N )
					return (a = $.arrFind( this.x.options, 'v.checked' )) ? a.value : this.x.value;
				this.x.value = a;
			}
		},
		getFocusOption: function( a ) {
			return this.$t().selectedIndex !== -1 && this.$t().options[ this.$t().selectedIndex + ( a == N ? 0 : a ) ];
		},
		getPrevOption: function() {
			return this.getFocusOption( -1 );
		},
		getNextOption: function() {
			return this.getFocusOption( 1 );
		},
		html_nodes: function() {
			var s = '', v = this.x.value, o = this.x.options, i = o && o.length, k = 0;
			while ( i -- ) {
				s = '<option value="' + (o[ i ].value || '') + '"' + (o[ i ].checked || o[ i ].value == v ? (k = i, ' selected') : '') + (this.x.tip ? ' title="' + $.strQuot( o[ i ].text ).replace( /<[^>]+>/g, '' ) + '"' : '') + '>' + o[ i ].text + '</option>' + s;
			}
			var w = this.innerWidth(), z = this.x.size;
			return '<select class=_t id=' + this.id + 't ' + _html_on( this ) + ' style="width:' + ( w ? w + 'px' : 'auto' ) + '" name="' + this.input_name() + '"' + ( this.x.multiple ? ' multiple' : '' ) +
				(this.x.tip ? ' title="' + $.strQuot((this.x.tip === T ? (o[ k ] && o[ k ].text) : this.x.tip) || '') + '"' : '') +
				( z ? ' size=' + z : '' ) + '>' + s + '</select><div class=_cvr></div>';
		}
	}
} ),
CalendarNum = define.widget( 'calendar/num', {
	Const: function( x, p ) {
		this.rootNode = p;
		W.apply( this, arguments );
		x.focus && (this.className += ' z-on');
	},
	Rooter: 'calendar/date',
	Listener: {
		body: {
			click: {
				prop: T,
				method: function() {
					if ( ! this.isDisabled() ) {
						var p = this.parentNode;
						if ( p.x.pickhour ) {
							p.pop( 'h', this );
						} else { //表单赋值
							p.date = $.dateParse( this.val(), 'yyyy-mm-dd' );
							p.trigger( 'complete' );
							! this._disposed && this.trigger( 'focus' );
						}
					}
				}
			},
			focus: function( e ) {
				var b = this.parentNode.getFocus();
				if ( b && b !== this )
					$.classRemove( b.$(), 'z-on' );
				$.classAdd( this.$(), 'z-on' );
			}
		}
	},
	Default: { width: -1, height: -1 },
	Prototype: {
		className: '_a _d',
		tagName: 'td',
		val: function() { return this.x.value },
		focus: function() { this.trigger( 'focus' ) },
		html_prop:  function() { return _proto.html_prop.call( this ) + ' w-urn="' + this.val() + '"' },
		html_nodes: function() { return this.x.text }
	}
} ),
// 今天
CalendarNow = define.widget( 'calendar/now', {
	Extend: 'calendar/num',
	Prototype: {
		className: '_t',
		tagName: 'div',
		val: function() { return this.parentNode._fm( new Date() ) },
		html_prop: function() { return _proto.html_prop.call( this ) + ' w-urn="now"' }
	}
} ),
/* `calendar` */
Calendar = CalendarDate = define.widget( 'calendar/date', {
	Const: function( x ) {
		var d = x.date ? this._ps( x.date ) : new Date(),
			b = x.begindate && this._ps( x.begindate ), e = x.enddate && this._ps( x.enddate );
		this.date = $.numRange( d, b, e );
		W.apply( this, arguments );
	},
	Child: 'calendar/num',
	Helper: {
		// @a -> commander, b -> format, c -> date, d -> focusdate, e -> begindate, f -> enddate, g -> complete
		pop: function( a, b, c, d, e, f, g ) {
			var o = _widget( a ), h = b.indexOf( 'h' ),
				x = { type: 'calendar/' + ( b == 'yyyy' ? 'year' : b == 'yyyy-mm' ? 'month' : b == 'yyyy-ww' ? 'week' : 'date' ), pickhour: h > 0,
					date: c, focusdate: d, begindate: e, enddate: f, on: { complete: function() { g && g.call( a, $.dateFormat( this.date, b ) ); o.isFormWidget && o.focus(); h !== 0 && $.close( this ) } } };
			if ( h === 0 ) {
				var f = $.dateFormat( new Date(), 'yyyy-mm-dd' ) + ' ';
				x.date = x.focusdate = f + $.strTrim( c );
				x.begindate && (x.begindate = f + x.begindate);
				x.enddate && (x.enddate = f + x.enddate);
				o.add( x, -1 ).pop( 'h', o );
			} else
				return o.cmd( { type: 'dialog', snap: a, cls: 'w-calendar-dialog f-shadow-snap', width: 240, height: -1, wmin: 12, indent: 1, pophide: T,
					node: x, on:{close:function(){o.isFormWidget&&!o.contains(document.activeElement)&&o.focus(F)}}} );
		}
	},
	Prototype: {
		className: 'w-calendar w-calendar-date',
		_formatter: 'yyyy-mm-dd',
		_nav_unit: 'm',
		_nav_radix: 1,
		_fm: function( a ) {
			return $.dateFormat( a, this._formatter );
		},
		_ps: function( a ) {
			return $.dateParse( a, this._formatter );
		},
		nav: function( e ) {
			var a = e.srcElement, b;
			if ( $.classAny( a, '_y' ) ) { //年
				this.pop( 'y', a );
			} else if ( $.classAny( a, '_m' ) ) { //月
				this.pop( 'm', a );
			} else if ( b = $.classAny( a, 'f-arw-l2 f-arw-r2' ) ) { // 前 后
				this.go( $.dateAdd( this.date, this._nav_unit, b === 1 ? - this._nav_radix : this._nav_radix ) );
			} else if ( $.classAny( a, '_t' ) ) { // 今天
				this.date = new Date();
				if ( this.x.on && this.x.on.complete ) {
					this.trigger( 'complete' );
				} else {
					var f = this.get( this._fm( new Date() ) );
					f ? f.focus() : this.go();
				}
			}
		},
		// 获取选中的值
		val: function() {
			var f = this.getFocus();
			return f && f.val();
		},
		today: function() {
			this.date = new Date();
			this.trigger( 'complete' );
		},
		go: function( d, f ) {
			if ( this.x.src ) {
				this.cmd( { type: 'ajax', src: $.urlFormat( this.x.src, [ this._fm( d || this.date ) ] ), success: f } );
			} else {
				d && (this.date = d);
				$.replace( this.$(), this.html() );
				f && f.call( this );
			}
		},
		get: function( a ) {
			if ( a.length != this._formatter.length ) {
				a = $.dateFormat( $.dateParse( a ), this._formatter );
			}
			var b = $.get( '[w-urn="' + a + '"]', this.$() );
			return b && _widget( b );
		},
		getFocus: function() {
			var b = $.get( '._a.z-on', this.$() );
			return b && _widget( b );
		},
		focus: function( a ) {
			var b = this.get( a );
			b && b.focus();
		},
		// 执行某个日期的点击事件
		click: function( a ) {
			var b = this.get( a );
			b && b.click();
		},
		// 年、月、时的浮动选择器
		pop: function( a, e ) {
			var Y = a === 'y', M = a === 'm', H = a === 'h', d = this.date, h = 18, l = M ? 12 : 10, c = [], g = d.getMinutes(), self = this,
				b = Y ? d.getFullYear() : M ? d.getMonth() + 1 : d.getHours(),
				y = Y ? ( b - b % 10 ) : M ? 1 : Math.max( 0, b * 2 - 5 ),
				s = (M ? '' : '<div class="_b _scr">-</div>') + '<div class="_wr">',
				htm = function() {
					var d = e.type == 'calendar/num' ? self._ps( e.val() ) : self.date, g = $.dateFormat( d, 'yyyy-mm-dd' ), f = Y ? 'yyyy' : M ? 'yyyy-mm' : 'yyyy-mm-dd hh:ii',
						n = self.x.begindate && $.dateFormat( self._ps( self.x.begindate ), f ), m = self.x.enddate && $.dateFormat( self._ps( self.x.enddate ), f );
					for ( var i = 0, t, s, r = ''; i < l; i ++ ) {
						t = H ? c[ Math.min(y, 38) + i ] : y + i;
						s = Y ? t : M ? (d.getFullYear() + '-' + $.strPad( t )) : (g + ' ' + t);
						r += '<div class="_b _i' + ( t == b ? ' _c' : '' ) + ( (n && n > s) || (m && m < s) ? ' z-ds' : '' ) + '">' + t + '</div>';
					}
					return r;
				};
			if ( H ) {
				for ( var i = 0, o; i < 24; i ++ )
					c.push( (o = $.strPad( i ) + ':') + '00', o + '30' );
				b = $.strPad( b ) + ':' + $.strPad( g );
			}
			s += htm() + '</div>' + ( M ? '' : '<div class="_b _scr">+</div>' );
			var d = this.cmd( { type: 'dialog', width: 60, height: h * 12, cls: 'w-calendar-select', snap: e, snaptype: e.dropSnapType || 'cc', pophide: T, node: { type: 'html', text: s },
					on: { mouseleave: function(){ this.close() }, close: function() { clearTimeout( t ); Q( d.$() ).off(); } }
				} ),
				r = Q( '._wr', d.$() ),
				f = function( k ) {
					if ( Y || H ) {
						y = k === '+' ? y + l : y - l;
						if ( Y ) y = $.numRange( y, 1900 );
						if ( H ) y = $.numRange( y, 0, 38 );
						r.html( htm() );
					}
				}, t;
			M && r.height( h * l );
			Q( d.$() ).on( 'mousedown', '._scr', function() {
				var a = this.innerText;
				f( a );
				t = setTimeout( function() { f( a ); t = setTimeout( arguments.callee, 90 ); }, 600 );
			} ).on( 'mouseup', '._scr', function() {
				clearTimeout( t );
			} ).on( 'mousewheel', function( e ) {
				t = setTimeout( function() { f( e.originalEvent.wheelDelta > 0 ? '-' : '+' ) } );
			} ).on( 'click', '._i:not(.z-ds)', function() {
				var h = this.innerText;
				with( self.date ) { Y ? setFullYear( h ) : M ? setMonth( h - 1 ) : ( h = h.split( ':' ), e.type == 'calendar/num' && setDate( e.x.text ), setHours( h[ 0 ] ), setMinutes( h[ 1 ] ) ) }
				d.close();
				H ? self.trigger( 'complete' ) : self.go();
			} );
		},
		html_nodes: function() {
			var a = this.date, n = this.x.begindate && this._fm( this._ps( this.x.begindate ) ), m = this.x.enddate && this._fm( this._ps( this.x.enddate ) ), k = 0,
				b = new Date( a.getTime() ), c = b.getMonth(), y = b.getFullYear(), d = new Date( y, c + 1, 1 ), e = [], f = this.x.focusdate && this.x.focusdate.slice( 0, 10 ), o = this.x.css,
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( 'l2' ) + Loc.ps( Loc.calendar.ym, a.getFullYear(), c + 1 ) + $.arrow( 'r2' ) + '<div class=_t>' + Loc.calendar.today + '</div></div>' +
					'<div style="padding:0 5px 5px 5px"><table class=w-calendar-tbl cellspacing=0 cellpadding=0 width=100%><thead><tr><td>' + Loc.calendar.day_title.join( '<td>' ) + '</thead><tbody>';
			b.setDate( 1 ), b.setDate( - b.getDay() + 1 );
			while ( b < d ) {
				var v = this._fm( b ), g = { value: v, text: b.getDate(), status: (n && n > v) || (m && m < v) ? 'disabled' : N, focus: f === v, style: h && o && o.value && o[ o.value[ k ++ ] ] },
					h = b.getMonth() === c;
				e.push( ( b.getDay() === 0 ? '<tr>' : '' ) + ( h ? this.add( g ).html() : '<td>&nbsp;' ) );
				b.setDate( b.getDate() + 1 );
			}
			if ( (n = 7 - ( e.length % 7 )) > 1 )
				e.push( '<td colspan=' + n + '>&nbsp;' );
			return s + e.join( '' ) + '</tbody></table></div>';
		}
	}
} ),
CalendarWeek = define.widget( 'calendar/week', {
	Extend: 'calendar/date',
	Child: 'calendar/num',
	Prototype: {
		className: 'w-calendar w-calendar-week',
		_nav_unit: 'y',
		_fm: function( a ) {
			var b = $.dateWeek( a, this.x.cg, this.x.start );
			return b[ 0 ] + '-' + $.strPad( b[ 1 ] );
		},
		html_nodes: function() {
			var a = this.date, w = $.dateWeek( a, this.x.cg, this.x.start ), y = w[ 0 ], t = this.x.begindate, m = this.x.enddate,
				b = $.dateWeek( new Date( y, 11, 31 ), this.x.cg, this.x.start ), e = [], f = this.x.focusdate && this.x.focusdate.slice( 0, 7 ), n = 0, o = this.x.css,
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( 'l2' ) + Loc.ps( Loc.calendar.y, y )  + $.arrow( 'r2' ) + '<div class=_t>' + Loc.calendar.weeknow + '</div></div>' +
					'<div style="padding:0 5px"><table class=w-calendar-tbl cellspacing=0 cellpadding=0 width=100%><tbody>';
			this._year = y;
			if ( b[ 0 ] !== y )
				b = $.dateWeek( new Date( y, 11, 31 - 7 ), this.x.cg, this.x.start );
			for ( var i = 1, l = b[ 1 ]; i <= l; i ++ ) {
				var v = y + '-' + $.strPad( i ), g = { value: v, text: i, status: (t && t > v) || (m && m < v) ? 'disabled' : '', focus: f === v, style: o && o.value && o[ o.value[ n ++ ] ] };
				e.push( ( (i - 1) % 7 === 0 ? '<tr>' : '' ) + this.add( g ).html() );
			}
			if ( (n = 7 - (i%7)) > 1 )
				e.push( '<td colspan=' + n + '>&nbsp;' );
			return s + e.join( '' ) + '</tbody></table></div>';
		}

	}
} ),
CalendarMonth = define.widget( 'calendar/month', {
	Extend: 'calendar/date',
	Child: 'calendar/num',
	Prototype: {
		className: 'w-calendar w-calendar-month',
		_nav_unit: 'y',
		_formatter: 'yyyy-mm',
		html_nodes: function() {
			var a = this.date, y = a.getFullYear(), t = this.x.begindate, m = this.x.enddate, e = [], f = this.x.focusdate && this.x.focusdate.slice( 0, 7 ), n = 0, o = this.x.css,
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( 'l2' ) + Loc.ps( Loc.calendar.y, y ) + $.arrow( 'r2' ) + '<div class=_t>' + Loc.calendar.monthnow + '</div></div>' +
					'<div style="padding:0 5px"><table class=w-calendar-tbl cellspacing=0 cellpadding=0 width=100%><tbody>';
			for ( var i = 0; i < 12; i ++ ) {
				var v = y + '-' + $.strPad( i + 1 ), g = { value: v, text: Loc.calendar.monthname[ i ], status: (t && t > v) || (m && m < v) ? 'disabled' : '', focus: f === v, style: o && o.value && o[ o.value[ n ++ ] ] };
				e.push( ( i % 4 === 0 ? '<tr class=_tr>' : '' ) + this.add( g ).html() );
			}
			return s + e.join( '' ) + '</tbody></table></div>';
		}
	}
} ),
CalendarYear = define.widget( 'calendar/year', {
	Extend: 'calendar/date',
	Child: 'calendar/num',
	Prototype: {
		className: 'w-calendar w-calendar-month',
		_nav_unit: 'y',
		_nav_radix: 10,
		_formatter: 'yyyy',
		html_nodes: function() {
			var a = this.date, t = this.x.begindate, m = this.x.enddate,
				y = a.getFullYear() - ( a.getFullYear() % 10 ) - 1,
				e = [], f = _number( this.x.focusdate && this.x.focusdate.slice( 0, 7 ) ), n = 0, o = this.x.css,
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( 'l2' ) + Loc.ps( Loc.calendar.y, (y + 1) + ' - ' + (y + 10) ) + $.arrow( 'r2' ) + '<div class=_t>' + Loc.calendar.yearnow + '</div></div>' +
					'<div style="padding:0 5px"><table class=w-calendar-tbl cellspacing=0 cellpadding=0 width=100%><tbody>';
			for ( var i = 0; i < 12; i ++ ) {
				var v = y + i, g = { value: v, text: y + i, status: (t && t > v) || (m && m < v) ? 'disabled' : '', focus: f === v, style: o && o.value && o[ o.value[ n ++ ] ] };
				e.push( ( i % 4 === 0 ? '<tr class=_tr>' : '' ) + this.add( g ).html() );
			}
			return s + e.join( '' ) + '</tbody></table></div>';
		}

	}
} ),
/* `date` */
_Date = define.widget( 'date', {
	Const: function( x, p ) {
		Text.apply( this, arguments );
		if ( ! x.format )
			x.format = 'yyyy-mm-dd';
		this.defaults( { width: 67 + this.x.format.length * 6 } );
	},
	Extend: Text,
	Listener: {
		body: {
			click: {
				prop: T,
				method: function() {
					this.isNormal() && this.popCalendar();
				}
			},
			resize: function() {
				this.$t().style.width = (this.innerWidth() - this.width_minus()) + 'px';
			}
		}
	},
	Prototype: {
		dropSnapType: 'v',
		validHooks: {
			minvalue: function( b, v ) {
				return $.dateParse( v, this.x.format ) < $.dateParse( b.minvalue, this.x.format );
			},
			maxvalue: function( b, v ) {
				return $.dateParse( v, this.x.format ) > $.dateParse( b.maxvalue, this.x.format );
			},
			compare: function( b, v, c, d ) {
				if ( this.x.format && c.x.format ) {
					var f = (this.x.format.length > c.x.format.length ? this : c).x.format;
					v = $.dateFormat( $.dateParse( v, this.x.format ), f );
					d = $.dateFormat( $.dateParse( d, c.x.format ), f );
				}
				return ! eval( '"' + $.strQuot( v ) + '"' + (b.comparemode || '==') + '"' + $.strQuot( d ) + '"' );
			},
			valid: function( b, v ) {
				if ( v && ! $.dateValid( v, this.x.format ) )
					return _form_err.call( this, b, 'time_invalid', [ $.x.min_year, $.x.max_year ] );
				if ( b && b.beforenow && v && v > $.dateFormat( new Date(), this.x.format ) )
					return _form_err.call( this, b, 'beforenow', [ b.beforenow ] );
				if ( b && b.afternow && v && v < $.dateFormat( new Date(), this.x.format ) )
					return _form_err.call( this, b, 'afternow', [ b.afternow ] );
				if ( this === this.parentNode.begin ) {
					var c = this.parentNode.end, d = c.val();
					if ( v && d && this.validHooks.compare.call( this, { comparemode: '<=' }, v, c, d ) )
						return _form_err.call( this, b, 'compare', [ '<=', c.x.label ] );
				}
			}
		},
		popCalendar: function() {
			var b = $.dateFormat( new Date, this.x.format ), c = this.cal, d = this.x.validate, e = c && c.isShow(), f = d && d.compare, g = f && d.comparemode,
				m = d && (d.maxvalue || (d.beforenow && b)), n = d && (d.minvalue || (d.afternow && b)), p = this.parentNode, t, v = this.val() || b;
			if ( (this === p.begin && p.end && (t = p.end.val())) || (g && g.indexOf( '<' ) == 0 && (t = this.ownerView.fv( f ))) )
				m = m ? (m > t ? t : m) : t;
			if ( (this === p.end && p.begin && (t = p.begin.val())) || (g && g.indexOf( '>' ) == 0 && (t = this.ownerView.fv( f ))) )
				n = n ? (n < t ? t : n) : t;
			this.closePop();
			if ( ! e )
				this.cal = Calendar.pop( this, this.x.format, v, this.val(), n, m, function( v ) { this.val( v ) } );
			this.focus();
		},
		closePop: function() {
			this.cal && this.cal.close();
			this.list && this.list.close();
		},
		width_minus: function() { return 20 + _input_indent() },
		html_nodes: function() { return '<input type=text' + this.form_prop() + '><em class="f-boxbtn" onclick=' + eve + '></em>' }
	}
} ),
/* `range` */
Range = define.widget( 'range', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.begin = x.begin && this.add( $.merge( { width: '*' }, x.begin ) );
		this.to    = (x.begin && x.end) && this.add( typeof x.to === _OBJ ? x.to : { type: 'html', text: x.to || Loc.to, width: 30, align: 'center' } );
		this.end   = x.end && this.add( $.merge( { width: '*' }, x.end ) );
		this.className = 'w-horz w-range';
		if ( ! x.valign && p && p.x.valign )
			this.defaults( { valign: p.x.valign } );
	},
	Extend: 'horz',
	Default: { width: -1 },
	Prototype: {
		x_nodes: $.rt_null
	}
} ),
/* `muldate` */
Muldate = define.widget( 'muldate', {
	Extend: 'date',
	Listener: {
		body: {
			click: {
				prop: T,
				method: function() {
					if ( this.isNormal() ) {
						this.val() ? this.popList() : this.popCalendar();
					}
				}
			}
		}
	},
	Prototype: {
		validHooks: N,
		width_minus: function() { return 20 + _input_indent() },
		$v: function() { return $( this.id + 'v' ) },
		v2t: function( v ) {
			for ( var i = 0, b = v.split( ',' ), s = []; v && i < b.length; i ++ )
				s.push( '"' + b[ i ] + '"' );
			return s.join( ' ' );
		},
		val: function( a ) {
			if ( a == N )
				return this.$v().value;
			this.$v().value = a;
			this.text( this.v2t( a ) );
			this.trigger( 'change' );
		},
		text: function( t ) {
			return t != N ? (this.$t().innerText = t) : this.$t().innerText;
		},
		popList: function() {
			for ( var i = 0, v = this.val().split( ',' ), b = []; i < v.length; i ++ )
				b.push( this.li_str( v[ i ] ) );
			b.push( this.li_str() );
			this.mh = Math.floor(($.height() - this.height()) / 60) * 30 + 2;
			var h = (v.length + 1) * 30 + 2, c = this.list, d = c && c.isShow();
			this.closePop();
			if ( ! d ) 
				this.list = this.cmd( { type: 'dialog', cls: 'w-calendar-dialog w-muldate-dialog f-shadow-snap', width: 200, height: Math.min( this.mh, h ), hmin: 2, wmin: 2, pophide: T, snap: this, indent: 1,
					node: { type: 'html', text: b.join( '' ), scroll: T }, on:{ close: function() { this.commander.focus(F) } } } );
			this.focus();
		},
		// @a -> el, b -> act
		li_act: function( a, b ) {
			var self = this, t = Q( a ).closest( 'table' );
			if ( b == '+' ) {
				Calendar.pop( a, this.x.format, N, N, N, N, function( v ) {
					t.before( self.li_str( v ) );
					self.li_ref();
				} );
			} else if ( b == '-' ) {
				t.remove();
				self.li_ref();
			} else {
				var d = t.data( 'value' );
				Calendar.pop( a, this.x.format, d, d, N, N, function( v ) {
					a.innerText = v;
					t.attr( 'data-value', v );
					self.li_ref();
				} );
			}
		},
		li_ref: function() {
			var r = Q( 'table[data-value]', this.list.$() ).map( function() { return this.getAttribute( 'data-value' ) } ).get();
			this.val( r.join() );
			this.list.height( Math.min( (r.length + 1) * 30 + 2, this.mh ) );
		},
		li_str: function( v ) {
			var k = $.abbr + '.all["' + this.id + '"].li_act';
			return '<table width=100% height=30 cellspacing=0 cellpadding=0' + (v ? ' data-value="' + v + '"' : '') + '><tr><td>&nbsp; ' + (v ? '<a href=javascript: onclick=' + k + '(this,"=")>' + v + '</a>' : '') + '<td width=70 align=center>' +
				$.image( '.f-i-minus ._i', { style: 'visibility:' + (v ? 'visible' : 'hidden'), click: k + '(this,"-")' } ) + ' &nbsp; ' + $.image( '.f-i-plus ._i', { click: k + '(this,"+")' } ) + '</table>';
		},
		html_nodes: function() {
			return '<input type=hidden id=' + this.id + 'v name="' + this.x.name + '" value="' + (this.x.value || '') + '"><div id=' + this.id + 't class="f-inbl f-fix _c" style="width:' +
				( this.innerWidth() - this.width_minus() ) + 'px"' + _html_on( this ) + '>' + this.v2t( this.x.value || '' ) + '</div><em class="f-boxbtn" onclick=' + eve + '></em>';
		}
	}
} ),
/* `spinner` */
Spinner = define.widget( 'spinner', {
	Extend: Text,
	Default: {
		width: 100
	},
	Prototype: {
		validHooks: {
			minvalue: function( b, v ) {
				return _number( v ) < _number( b.minvalue );
			},
			maxvalue: function( b, v ) {
				return _number( v ) > _number( b.maxvalue );
			},
			compare: function( b, v, c, d ) {
				return ! eval( _number( v ) + (b.comparemode || '==') + _number( d ) );
			},
			valid: function( b, v ) {
				if ( v && ( isNaN( v ) || /\s/.test( v ) ) )
					return _form_err.call( this, b, 'number_invalid' );
				if ( this === this.parentNode.begin ) {
					var c = this.parentNode.end, d = c.val();
					if ( v && d && this.validHooks.compare.call( this, { comparemode: '<=' }, v, c, d ) )
						return _form_err.call( this, b, 'compare', [ '<=', c.x.label ] );
				}
			}
		},
		width_minus: function() { return 20 + _input_indent() },
		step: function( a ) {
			if ( this.isNormal() ) {
				var d = this.x.validate, m = d && d.maxvalue, n = d && d.minvalue, v = $.numAdd( _number( this.val() ), a * ( this.x.step || 1 ) );
				m != N && (v = Math.min( m, v ));
				n != N && (v = Math.max( n, v ));
				this.val( v );
				this.focus();
				this.trigger( 'change' );
			}
		},
		html_nodes: function() {
			return '<input type=text' + this.form_prop() + '><cite class=_b><em onclick=' + evw + '.step(1)><i class=f-vi></i><i class="f-arw f-arw-t2"></i></em><em onclick=' + evw + '.step(-1)><i class=f-vi></i><i class="f-arw f-arw-b2"></i></em></cite>';
		}
	}
} ),
/* `slider` */
Slider = define.widget( 'slider', {
	Extend: Spinner,
	Default: {
		width: -1, wmin: 0, thumbwidth: 10
	},
	Listener: {
		body: {
			resize: function() {
				$( this.id + 'bar' ).style.width = this.innerWidth() + 'px';
				this.val( this.val() );
			},
			change: N
		}
	},
	Prototype: {
		className: 'w-form',
		val: function( a ) {
			if ( a == N ) {
				return $( this.id + 't' ).value;
			}
			$( this.id + 't' ).value = a;
			var l = this._left( a );
			$( this.id + 'thumb' ).style.left  = l + 'px';
			$( this.id + 'track' ).style.width = (l + (this.attr( 'thumbwidth' ) / 2)) + 'px';
		},
		dragstart: function( a, b ) {
			if ( ! this.isNormal() )
				return;
			var x = b.clientX, c = this.x.validate, m = (c && c.maxvalue) || 100, n = (c && c.minvalue) || 0, f = _number( a.style.left ), 
				g = this.attr( 'thumbwidth' ), w = this.innerWidth() - g, self = this, t = this.x.tip === T ? '$0' : this.x.tip,
				d = t && this.cmd({ type: 'tip', text: $.strFormat( t, [ this.x.value ] ), snap: a, snaptype: 'tb,bt', closable: F }), v = $( self.id + 't' ).value;
			$.moveup( function( e ) {
				var l = $.numRange(f + e.clientX - x, 0, w);
				v = Math.floor( Math.floor((m - n) * l / w) );
				a.style.left = l + 'px';
				$( self.id + 'track' ).style.width = (l + (g / 2)) + 'px';
				d && d.snapTo( a ).text( $.strFormat( t, [ v ] ) );
				$( self.id + 't' ).value = v;
				self.trigger( 'change' );
			}, function( e ) {
				d && d.close();
			} );
		},
		_left: function( v ) {
			var w = this.innerWidth(), d = this.x.validate, m = (d && d.maxvalue) || 100, n = (d && d.minvalue) || 0;
			return (w - this.attr( 'thumbwidth' )) * (v - n) / (m - n);
		},
		html_nodes: function() {
			var w = this.innerWidth(), v = this.x.value == N ? n : this.x.value, f = this._left( v );
			return '<input type=hidden id=' + this.id + 't name="' + this.input_name() + '" value="' + v + '"' + (this.isDisabled() ? ' disabled' : '') + '><i class=f-vi></i><div id=' + this.id + 'bar class="f-va f-inbl _bar" style="width:' + w + 'px"><div id=' + this.id + 'track class=_track style="width:' + (f + 5) + 'px"></div><div id=' + this.id + 'thumb class=_thumb style="left:' + f + 'px" onmousedown=' + evw + '.dragstart(this,event)></div></div>';
		}
	}
} ),
/* `xbox` */
_boxbtn_width = 20,
XBox = define.widget( 'xbox', {
	Const: function( x ) {
		this.initOptions( x );
		AbsInput.apply( this, arguments );
	},
	Extend: AbsInput,
	Listener: {
		tag: N,
		body: {
			click: {
				prop: T,
				method: function() {
					this.isNormal() && this.drop();
				}
			},
			change: N
		}
	},
	Prototype: {
		$v: function() { return $( this.id + 'v' ) },
		width_minus: function() { return 20 + _input_indent() },
		initOptions: function( x ) {
			var o = x.options || (x.options = []), i = o.length;
			this._sel = [];
			while ( i -- ) {
				o[ i ].value == N && (o[ i ].value = '');
				(o[ i ].checked || o[ i ].value == x.value) && this._sel.push( o[ i ] );
			}
			if( o.length ) {
				! this._sel.length && this._sel.push( o[ 0 ] );
				x.value = this._sel[ 0 ].value; // 设一下value，给 isModified() 用
			}
		},
		// @a -> options, b -> index
		setOptions: function( a, b ) {
			var v = this.val();
			if ( b ) {
				var c = this.x.options[ b ];
				if ( c ) {
					this.x.options[ b ] = $.extend( a[ 0 ] || a, c );
					if ( c && c == this._sel )
						v = this.x.options[ b ].value;
				}
			} else
				this.x.options = a;
			this._dropper && this._dropper.close();
			this.initOptions( this.x );
			this.val( v );
		},
		getFocusOption: function( a ) {
			for ( var i = 0, v = this.val(), o = this.x.options, l = o.length; i < l; i ++ ) {
				if ( v == o[ i ].value ) break;
			}
			if ( i >= l ) i = 0;
			return o[ i + ( a == N ? 0 : a ) ];
		},
		getPrevOption: function() {
			return this.getFocusOption( -1 );
		},
		getNextOption: function() {
			return this.getFocusOption( 1 );
		},
		isEmpty: function() {
			return ! this.val();
		},
		val: function( a ) {
			if ( this.$() ) {
				var v = this.$v().value;
				if ( a == N )
					return v;
				var b = [];
				if ( this.x.multiple ) {
					if ( a.jquery ) {
						for ( var i = 0, q = a.parent().find( '._o.z-on' ); i < q.length; i ++ )
							b.push( this.x.options[ q[ i ].getAttribute( '_i' ) ] );
					} else {
						for ( var i = 0, q = this.x.options, l = q.length; i < l; i ++ )
							if ( q[ i ].value && $.idsAny( a, q[ i ].value ) ) b.push( q[ i ] );
					}
				} else {
					var o = a.jquery ? this.x.options[ a.attr( '_i' ) ] : $.arrFind( this.x.options, 'v.value=="' + a + '"' );
					o && b.push( o );
				}
				if ( ! b.length )
					b = [ this.x.options[ 0 ] ];
				for ( var i = 0, s = [], t = [], u = []; i < b.length; i ++ ) {
					s.push( this.htm_li( b[ i ], T ) );
					t.push( b[ i ].text );
					u.push( b[ i ].value );
				}
				this._sel = b;
				this.$v().value = u = u.join();
				this._val( s.join( ', ' ) );
				if ( this.x.tip === T )
					this.$t().title = t.join().replace(/<[^>]+>/g, '');
				if ( v != u )
					this.trigger( 'change' );
			} else {
				if ( a == N )
					return this.x.value;
				this.x.value = a;
			}
		},
		_val: function( s ) {
			this.$( 'p' ).innerHTML = s;
		},
		text: function() {
			return $.strTrim( this.$( 'p' ).innerText );
		},
		choose: function( a, e ) {
			var d = Q( e.srcElement ).closest( '._o' );
			this.x.multiple && d.toggleClass( 'z-on' );
			this.val( d );
			!this.x.multiple && this._dropper.close();
		},
		drop: function() {
			var o = this.x.options;
			if ( ! o || ! o.length )
				return;
			var a = $.bcr( this.$() ), d = this._dropper;
			if ( d ) {
				this.focus( F );
				d.close();
			} else {
				this.focus();
				this._dropper = this.cmd( { type: 'dialog', minwidth: this.innerWidth(), maxwidth: Math.max( $.width() - a.left - 2, a.right - 2 ), maxheight: Math.max( $.height() - a.bottom, a.top ), hmin: 2, indent: 1, id: this.id, cls: 'w-xbox-dialog' + (this.x.multiple ? ' z-mul' : ''), pophide: T,
					snaptype: 'v', snap: this, node: { type: 'html', scroll: T, text: this.html_options(), on: { ready: 'var t=$.get(".z-on",this.$());t&&this.scrollTop(t,"middle",null,t.offsetHeight);' } },
					on: { close: 'this.commander.focus(!1);this.commander._dropper=null;' } } );
			}
		},
		html_placehoder: function() {
			return '';
		},
		htm_li: function( a, b ) {
			return a ? (a.icon ? $.image( a.icon, { cls: 'w-xbox-ico' } ) : '') + (this.x.escape ? $.strEscape( a.text ) : a.text) + (!b && this.x.multiple ? '<i class=_box></i>' : '') : '';
		},
		html_options: function() {
			for ( var i = 0, s = [], v = this.$v().value, o = this.x.options || [], b, l = o.length, t; i < l; i ++ ) {
				s.push( '<div class="_o f-fix' + (o[ i ].value && $.idsAny( v, o[ i ].value ) ? ' z-on' : '') + '" _i="' + i + '"' + (this.x.tip ? ' title="' + $.strQuot( o[ i ].text ).replace( /<[^>]+>/g, '' ) + '"' : '') + '>' + this.htm_li( o[ i ] ) + '</div>' );
			}
			return '<div id=' + this.id + 'opts class=_drop onclick=$.all["' + this.id + '"].choose(this,event)>' + s.join( '' ) + '</div>';
		},
		html_nodes: function() {
			var s = this._sel[ 0 ];
			return '<input type=hidden name="' + this.x.name + '" id=' + this.id + 'v value="' + (s ? (s.value || '') : '') + '"><div class="f-inbl f-omit _t" id=' + this.id + 't ' +
				(this.x.tip ? ' title="' + $.strQuot(((this.x.tip === T ? (s && s.text) : this.x.tip) || '').replace(/<[^>]+>/g, '')) + '"' : '') +
				' style="width:' + (this.innerWidth() - this.width_minus()) + 'px"><span id=' + this.id + 'p>' + this.htm_li( s, T ) + '</span></div><em class=f-boxbtn><i class=f-vi></i>' + $.arrow( 'b2' ) + '</em>';
		}
	}
}),
/* `imgbox` */
Imgbox = define.widget( 'imgbox', {
	Const: function( x, p ) {
		this.imgw = x.imgwidth == N ? 80 : _number( x.imgwidth );
		this.imgh = x.imgheight == N ? 80 : _number( x.imgheight );
		this.txth = x.options && x.options[ 0 ] && x.options[ 0 ].text ? 22 : 0;
		this.txth && (this.className += ' z-tx');
		XBox.apply( this, arguments );
	},
	Extend: XBox,
	Default: { width: -1 },
	Listener: {
		tag: N,
		body: {
			focus: N, blur: N
		}
	},
	Prototype: {
		className: 'w-form w-input-border w-imgbox-c f-rel f-inbl',
		drop: function() {
			var d = this.x.options,
				l = d.length,
				r = $.bcr( this.$() ),
				csw = _number( this.x.dropwidth ),
				avw = Math.max( r.left, $.width() - r.left - r.width ),
				opw = this.imgw + 22,
				oph = this.imgh + 12 + (this.txth || 10),
				adw = ( Math.min( l, Math.ceil( avw / opw ) - 1 ) ) * opw,
				e = Math.max( opw, csw ? Math.min( csw, adw ) : adw ),
				f = Math.ceil( l / Math.ceil( e / opw ) ) * oph,
				g = $.snap( e, f, r, 'h' ),		// pos object
				ah = Math.min( g.mag_t ? r.top : $.height() - g.top, f ), // avail height
				h = ah < f ? ah - 2 : f,
				s = [ '<div class=w-imgbox-list>' ];
			if ( g.top < 0 ) h += g.top;
			if ( l ) {
				for ( var i = 0, v = this.val(); i < l; i ++ ) {
					s.push( '<div class="w-imgbox-c f-inbl' + (d[ i ].value == v ? ' z-on' : '') + '" onclick=$.all["' + this.id + '"].choose(' + i + ')>' + this.html_img( d[ i ] ) + '</div>' );
				}
				this.dg = this.add( { type: 'dialog', width: e, height: h, cls: 'w-input-border' + (this.txth ? ' z-tx': ''), snap: this, snaptype: g.type, pophide: T, indent: 1,
					node: { type: 'html', scroll: T, text: s.join( '' ) + '</div>' }, on: { close: '$.classRemove( this.parentNode.$(),"z-m-' + g.mag + '")' } } ).render();
				$.classAdd( this.$(), 'z-m-' + g.mag );
			}
		},
		choose: function( a ) {
			this._sel = [ this.x.options[ a ] ];
			this._val();
			this.dg.close();
		},
		_val: function( o ) {
			this.$().innerHTML = this.html_nodes();
		},
		html_img: function( a ) {
			return '<div class=_g id=' + this.id + 'p style="width:' + this.imgw + 'px;height:' + this.imgh + 'px;">' + $.image( a.icon, { width: this.imgw, height: this.imgh } ) + '</div>' +
				( this.txth ? '<div class="_t f-fix" style=width:' + this.imgw + 'px;>' + (a.text || '') + '</div>' : '' );
		},
		html_nodes: function() {
			var a = this._sel[ 0 ] || {icon:'.f-dot'};
			return (a ? this.html_img( a ) : '') + '<div class=_r><i class=f-vi></i>' + $.arrow( 'r2' ) + '</div><input type=hidden name="' + this.x.name + '" id=' + this.id + 'v value="' + ((a && a.value) || '') + '">';
		}
	}	
}),
/* `pickbox` */
Pickbox = define.widget( 'pickbox', {
	Extend: Text,
	Listener: {
		tag: 't',
		body: {
			click: {
				prop: T,
				block: $.rt_true,
				method: function( e ) {
					if ( this.x.on && this.x.on.click )
						this.triggerHandler( 'click' );
					else
						this.pick();
					this.focus( F );
				}
			}
		}
	},
	Prototype: {
		$v: function() { return $( this.id + 'v' ) },
		width_minus: function() { return 20 + _input_indent() },
		val: function( v, t ) {
			v != N && this.text( t || v );
			return AbsForm.prototype.val.apply( this, arguments );
		},
		text: function( t ) {
			if ( t == N )
				return this.x.text;
			this.x.text = t;
			this.$() && (this.$t().innerText = t);
		},
		pick: function() {
			if ( this.x.picker && this.isNormal() ) {
				if ( this.x.picker.type === 'dialog' ) {
					var c = $.jsonClone( this.x.picker );
					c.src && (c.src = $.urlFormat( c.src, $.extend( { value: this.val() }, this.x ) ));
					this.cmd( c ).addEvent( 'close', function() { ! this.$().contains( document.activeElement ) && this.focus( F ); }, this );
				} else if ( W.isCmd( this.x.picker ) ) {
					this.cmd( this.x.picker );
				}
				this.warn( F );
			}
		},
		html_nodes: function() {
			return '<input type=hidden id=' + this.id + 'v' + (this.x.name ? ' name="' + this.x.name + '"' : '') + ' value="' + $.strQuot(this.x.value || '') + '"><div id="' + this.id + 
				't" class="f-inbl f-fix _t" style="width:' + (this.innerWidth() - this.width_minus()) + 'px" ' + _html_on( this ) + '>' + $.strEscape( this.x.text ) + '</div><em class="f-boxbtn _dot" onclick=' + evw + '.pick()></em>';
		}
	}
} ),
/* `combobox`
	{ "type": "combobox", "name": "combobox", "id": "combobox", "value": "0001,aa@rj.com", "text": "", "dropsrc": "webapp/demo.json.php?act=combo", "readonly": true,
	  "src": "webapp/demo.json.php?act=combo&v=$value&t=$text", "pick": "this.cmd({type:'dialog',template:'std',src:'webapp/demo.json.php?act=pick',width:600,height:400})", "multiple": true,
	  "suggest": true, "placeholder": "124567893", "on": { "change": "" }, "pub": { "width": 100, "on": { "click": "" } } },
 *	注1: 当有设置初始value时，text一般可以不写，程序将会从数据岛(more属性)中匹配。如果数据岛不是完整展示的(比如树)，那么text属性必须加上。
 */
Combobox = define.widget( 'combobox', {
	Const: function( x ) {
		AbsInput.apply( this, arguments );
		this.className += ' z-loading';
		//if ( x.height && x.height != -1 )
		//	x.nobr = T;
		x.nobr && (this.className += ' z-nobr');
		x.face && (this.className += ' z-face-' + x.face);
		this.more = this.createPop( x.node || x.src || {type:'dialog',node:{type:'grid',combo:{field:{}}}}, { value: x.value } );
		if ( this.more.contentView.layout )
			this.trigger( 'load' );
		else
			this.more.preload( $.proxy( this, function() { this.trigger( 'load' ) } ) );
		this.addEvent( 'focus', function() { this.focusNode && this.focusNode.tabFocus( F ) } );
	},
	Extend: AbsInput,
	Child: 'combobox/option',
	Listener: {
		block: function( e ) {
			return e.srcElement && e.srcElement.id.indexOf( this.id ) < 0;
		},
		body: {
			ready: function() {
				this.domready = T;
				! this.loading && this.init();
			},
			load: function() {
				this.loading = F;
				$.classRemove( this, 'z-loading' );
				this.domready && this.init();
			},
			blur: {
				prop: T,
				method: function() {
					AbsForm.Listener.body.blur.method.apply( this, arguments );
					var t = this.$t();
					! t.innerText && t.nextSibling && ! this.contains( document.activeElement ) && t.parentNode.appendChild( t );
				}
			},
			click: {
				prop: T,
				method: function( e ) {
					// 点击空白地方，光标移到文本末尾
					if ( this.usa() && e.srcElement ) {
						if ( e.srcElement.id === this.id + 'c' && ! $.rngText() ) {
							var t = this.$t();
							t.nextSibling && t.parentNode.appendChild( t );
							this.focus();
							$.rngCursor( this.$t() );
						}
						var t = this.queryText();
						t && ((t != this._query_text) || !(this.sugger && this.sugger.isShow())) && this.suggest( t, 10 );
					}
				}
			},
			keydown: {
				prop: T,
				method: function( e ) {
					if ( this.usa() ) {
						var k = e.keyCode, n;
						if ( k === 13 )
							return $.stop( e );
						if ( ! this._imeMode ) {
							if ( k === 8 ) { // 8:backspace
								if ( $.rngCursorOffset() == 0 ) {
									var a = this.$t().previousSibling;
									a && $.widget( a ).close();
								}
							} else if ( k === 35 ) { // 35:end
								var a = this.$t(), t = a.innerText;
								if ( $.rngCursorOffset() == t.length && a.nextSibling ) {
									(t = $.strTrim( t )) && (this.addOpt( t ), a.innerText = '');
									(a.parentNode.appendChild( a ), this.focus());
								}
							} else if ( k === 36 ) { // 36:home
								var a = this.$t(), t;
								if ( $.rngCursorOffset() == 0 && a.previousSibling ) {
									(t = $.strTrim( a.innerText )) && (this.addOpt( t ), a.innerText = '');
									(a.parentNode.insertBefore( a, this[ 0 ].$() ), this.focus());
								}
							} else if ( k === 37 ) { // 37:left
								var a = this.$t(), b, t;
								if ( $.rngCursorOffset() == 0 && (b = a.previousSibling) ) {
									(t = $.strTrim( a.innerText )) && (this.addOpt( t ), a.innerText = '');
									b && (b.parentNode.insertBefore( a, b ), this.focus());
								}
							} else if ( k === 39 ) { // 39:right
								var a = this.$t(), b, t = a.innerText;
								if ( $.rngCursorOffset() == t.length && (b = a.nextSibling) ) {
									(t = $.strTrim( a.innerText )) && (this.addOpt( t ), a.innerText = '');
									b && (b.parentNode.insertBefore( b, a ), this.focus());
								}
							} else if ( k === 46 ) { // 46:delete
								var a = this.$t(), t = a.innerText;
								if ( $.rngCursorOffset() == t.length && a.nextSibling ) {
									_widget( a.nextSibling ).close();
								}
							}
						}
					}
				}
			},
			// chrome中文模式打完字后按回车时，不会响应keyup事件，因此设置input事件来触发suggest()
			input: br.ms ? N : {
				prop: T,
				method: function( e ) {
					this._imeMode && this.suggest( this.queryText() );
				}
			},
			keyup: {
				prop: T,
				method: function( e ) {
					//clearTimeout( this._sug_timer );
					if ( this.usa() && ! this._imeMode ) {
						var k = e.keyCode;
						if ( k === 13 || k === 38 || k === 40 ) { // 13:enter, 38:up, 40:down
							$.stop( e );
							var d = this.pop(), t;
							if ( k === 13 && (t = this.queryText()) != this._query_text ) { // 中文输入法按回车，是把文本放入输入框里的动作，不是提交动作
								this.suggest( t );
							} else if ( d.isShow() && d.contentView.combo ) {
								k === 13 && ! d.contentView.combo.getFocus() ? _enter_submit( k, this ) : d.contentView.combo.keyup( k );
							} else
								_enter_submit( k, this );
						} else if ( !(e.ctrlKey && k === 86) && !(k === 17) ) { //86: ctrl+v, 17: Ctrl, 37: left, 39: right
							var t = this.$t().innerText, s = String.fromCharCode( 160 ) + ' '; // 160: chrome的空格
							if ( k === 32 && s.indexOf( t.charAt( t.length - 1 ) ) > -1 && (t = $.strTrim( t.slice( 0, -1 ) )) ) { // 最后一个字符是分隔符，则生成一个已选项
								this.queryText( '' );
								var o = this.addOpt( t );
								o.x.error && this.x.suggest && this.match( o ); // 新增的选项如果没即时匹配成功，并且有suggest，则以隐藏模式去后台匹配一次数据。
								this.closePop();
								$.stop( e );
							} else if ( ! e.ctrlKey ) {
								this.suggest( t );
							}
						}
					}
				}
			},
			paste: {
				prop: T,
				//proxy: ie ? 'beforepaste' : N,
				method: function( e ) {
					this.focus();
					var r = $.rngSelection(), h = this.$( 'ph' ), p = this.x.separator || ',', g = new RegExp( '[,\\s' + String.fromCharCode( 61453 ) + String.fromCharCode( 12288 ) + ']+' ), // 61453,12288: 从word文档和chrome复制来的文本可能存在的空白符
						c = window.clipboardData ? window.clipboardData.getData( 'text' ) : (e.originalEvent || e).clipboardData.getData( 'text/plain' );
				    $.stop( e );
					this._paste_rng = r;
				    h && (h.style.display = 'none');
					this._paste( r, $.strTrim( c ).split( g ).join( p ).replace( /^,+|,+$/g, '' ) );
					h && (h.style.display = '');
				}
			},
			resize: function() {
				var w = (this.innerWidth() - this.width_minus()) + 'px';
				this.$( 'c' ) && (this.$( 'c' ).style.width = w);
				this.$( 'ph' ) && (this.$( 'ph' ).style.width = w);
			},
			// 覆盖textarea的change事件定义，仅当已选项有变化时触发
			change: N
		}
	},
	Prototype: {
		NODE_ROOT: T,
		loading: T,
		domready: F,
		_query_text: '',
		x_node: $.rt_null,
		validHooks: {
			valid: function( b, v ) {
				if ( this.x.strict !== F && this._inited && (Q( '._o.z-err', this.$() ).length || this.queryText()) )
					return _form_err.call( this, b, 'invalid_option' );
			}
		},
		$v: function() { return $( this.id + 'v' ) },
		width_minus: function() { return (this.x.dropsrc ? 20 : 0) + (this.x.picker ? 20 : 0); },
		init: function() {
			if ( ! this.$() )
				return;
			this._initOptions( this.x.value, this.x.text );
			this.queryText( '' );
			this.save();
			this.usa() && (this.$t().contentEditable = T);
			$.classRemove( this.$(), 'z-loading' );
			_listen_ime( this, this.$t() );
			this._inited = T;
		},
		// 根据value设置已选项, 初始化时调用 /@v -> value, t -> text
		_initOptions: function( v, t ) {
			if ( v && (v = v.split( ',' )) ) {
				for ( var i = 0, t = t && t.split( ',' ), o, s = '', l = v.length; i < l; i ++ ) {
					if ( v[ i ] ) {
						if ( t ) {
							var r = { value: v[ i ], text: t[ i ] };
							s += this.add( r ).html();
							this.store().merge( r );
						} else if ( o = this.store().getParamByValue( v[ i ] ) )
							s += this.add( o ).html();
						else if ( this.x.strict === F )
							s += this.add( { text: v[ i ], error: T } ).html();
					}
				}
				s && $.before( this.$t(), s );
			}
		},
		// @implement
		insertHTML: function( a, b ) {
			! b || b === 'append' ? $.before( this.$t(), a ) : b === 'prepend' ? $.prepend( this.$( 'c' ), a ) : _proto.insertHTML.apply( this, arguments );
		},
		isEmpty: function() {
			return !(this._val() || this.queryText());
		},
		usa: function() {
			return ! this.loading && this.isNormal();
		},
		store: function( a ) {
			return (a || this.more).contentView.combo;
		},
		// 获取当前的选项对话框
		pop: function() {
			return this.dropper && this.dropper.isShow() ? this.dropper : this.sugger && this.sugger.isShow() ? this.sugger : this.more;
		},
		// 创建选项窗口 /@ u -> url|dialogOption, r -> replace object?
		createPop: function( u, r ) {
			var d = { type: 'dialog', cls: 'w-combobox-dialog', indent: 1 };
			if ( typeof u === _STR ) {
				d.src = u;
			} else {
				d.node = u;
			}
			var o = { pophide: T, memory: T, snap: this, snaptype: 'v', wmin: 2, hmin: 2 },
				w = 'javascript:return this.parentNode.$().offsetWidth';
			//如果用户设置宽度为*或百分比，则设置maxwidth为不超过combobox的宽度
			if ( u.width ) {
				if ( isNaN( u.width ) )
					o.maxwidth = w;
			} else {
				o.width = w;
			}
			$.extend( d, o );
			d.src && (d.src = this.parseSrc( d.src, r ));
			var self = this;
			return this.add( d, -1 ).addEvent( 'close', function() {
				! self.$().contains( document.activeElement ) && self.focus( F );
				var d = self.pop();
				d && d.isShow() && self.focusNode && self.focusNode.tabFocus( F );
			} ).addEvent( 'load', function() {
				this.$().style.width = this.$().scrollWidth + 'px';
				this.axis();
			} );
		},
		closePop: function() {
			clearTimeout( this._sug_timer );
			var d = this.pop();
			d && d.close();
		},
		// 解析变量: $value(值), $text(文本) /@ u -> url, r -> replace object
		parseSrc: function( u, r ) {
			/*for ( var k in r )
				u = u.replace( '$' + k, $.urlEncode( r[ k ] ) );
			return u.replace( /\$\w+/g, '' );*/
			return $.urlFormat( u, $.extend( r || {}, { value: this.val(), text: '' }, this.x ) );
		},
		// 读/写隐藏值
		_val: function( a ) {
			if ( a == N )
				return (this.$() ? this.$v() : this.x).value || '';
			(this.$() ? this.$v() : this.x).value = a;
		},
		// @a -> value
		val: function( a ) {
			if ( a == N ) {
				this.save();
				return this._val();
			}
			if ( this.loading ) {
				this._val( a );
				return this.addEventOnce( 'load', function() { this.val( a ) } );
			}
			this.empty( T );
			this._val( '' );
			this.$t().innerHTML = '';
			a ? this.match( { value: a } ) : this.resetEffect();
		},
		// 获取正在输入中的用于后台查询的文本
		queryText: function( a ) {
			var e = this.$t();
			if ( a == N )
				return e ? $.strTrim( e.innerText ) : '';
			e && (e.innerText = this._query_text = a);
		},
		// 获取所有选项文本
		text: function() {
			for ( var i = 0, s = []; i < this.length; i ++ )
				s.push( this[ i ].x.text );
			return $.idsAdd( s.join( ',' ), this.queryText() );
		},
		// 根据value值返回对应的文本值
		val2txt: function( v ) {
			for ( var i = 0, s = [], v = (v || '').split( ',' ), o, l = v.length; i < l; i ++ )
				v[ i ] && (o = this.store().getParamByValue( v[ i ] )) && s.push( o.text );
			return s.join( ',' );
		},
		// 根据文本增加已选项, 多个用逗号或空白符隔开 /@ t -> text|replaceObject, a -> param data?
		addOpt: function( t, a ) {
			var v = this._val(), e = this.$t(), k = e.nextSibling ? _widget( e.nextSibling ).nodeIndex : N;
			if ( t.value ) {
				this._initOptions( t.value );
			} else if ( (t = typeof t === _STR ? t : t.text) && (t = t.split( ',' )).length ) {
				for ( var i = 0, c = this.store(), d, o, s = []; i < t.length; i ++ ) {
					if ( t[ i ] ) {
						if ( d = (a || c.getParam( t[ i ] )) )
							! $.idsAny( v, d.value ) && ( s.push( this.add( d, k ).html() ), $.idsAdd( v, d.value ) );
						else
							s.push( this.add( { text: t[ i ], error: T }, k ).html() );
						if ( ! this.x.multiple && s.length )
							break;
					}
				}
				$.before( e, s.join( '' ) );
			}
			this.save();
			return this[ this.length - 1 ];
		},
		// 完成一个尚未匹配成功的项 /@o -> comboboxOption, a -> param data?
		fixOpt: function( o, a ) {
			var d = a || this.store().getParam( o.x.text );
			if ( d && o.x.error ) {
				$.idsAny( this._val(), d.value ) ? o.remove() : o.replace( d );
				this.save();
			}
		},
		//@public(用于 combo 窗口的点击事件) 完成正在输入的文本，或是没有匹配成功的项 / @a -> tr|leaf|xml
		complete: function( a ) {
			this.store().merge( a );
			var d = this.store().getParam( a ), f = this.focusNode;
			if ( d )
				f ? this.fixOpt( f, d ) : (this.queryText( '' ), this.addOpt( d.text, d ));
			this.focus();
		},
		// @t -> query text, s -> milliseconds?
		suggest: function( a, s ) {
			clearTimeout( this._sug_timer );
			var t = this._suggest_text( a );
			if ( t ) {
				var self = this, d = this.x.delay;
				this._sug_timer = setTimeout( function() { self.doSuggest( a ) }, s != N ? s : d != N ? d : 500 );
			} else
				this.closePop();
			this._query_text = t;
		},
		// 弹出模糊匹配的选项窗口  /@ a -> text|comboboxOption
		doSuggest: function( a ) {
			this._currOpt = a;
			var t = this._suggest_text( a ), u = this.x.suggest && this.x.src;
			if ( u && (u = this.parseSrc( u, { text: t } )) ) {
				var self = this;
				(this.sugger || (this.sugger = this.createPop( u ))).reload( u, function() { ! self._disposed && self._suggest_end( a, this ) } );
			} else
				this._suggest_end( a, this.more );
		},
		_suggest_text: function( a ) {
			return $.strTrim( a.x ? a.x.text : a );
		},
		_suggest_end: function( a, m ) {
			var c = this.store( m );
			if ( c ) {
				var t = this._suggest_text( a ), d = c.getXML( t ), s = this.store(), u = this.x.suggest && this.x.src;
				d && (m != s) && s.merge( d );
				this.closePop();
				if ( u ? (c.isKeepShow() || c.getLength()) : c.filter( t ) ) {
					m.render();
					if ( a.x ) {
						m.addEvent( 'close',  t = function() { a.tabFocus( F ) } );
						a.addEvent( 'remove', function() { m.removeEvent( 'close', t ) } );
					}
				}
			}
		},
		// 精确匹配，在隐藏状态下进行 /@ a -> replaceObject|comboboxOption
		// 多个立即匹配成功; 单个显示下拉选项
		match: function( a ) {
			var c = a.x ? a : this;
			if ( this.x.suggest ) {
				var d = this.createPop( this.x.src || this.x.node, a.x || a ), self = this;
				c.setLoading();
				d.preload( function() {
					if ( ! c._disposed ) {
						c.setLoading( F );
						var o = this.contentView.combo;
						if ( o ) {
							self.store().merge( o );
							a.x ? self.fixOpt( a ) : self.addOpt( a );
						}
						self.resetEffect();
					}
					this.remove();
				} );
			} else {
				a.x ? this.fixOpt( a ) : this.addOpt( a );
				this.resetEffect();
			}
		},
		// @r -> range, s -> text
		_paste: function( r, s ) {
			if ( s ) {
				var p = this.x.separator || ',';
				if ( s.indexOf( p ) < 0 ) {
					$.rngText( r, s );
					$.rngCursor( this.$t() );
					this.suggest( this.queryText() );
				} else {
					if ( ! this.x.multiple )
						s = $.strTrim( $.strTo( s, ',' ) );
					this.match( { text: s } );
				}
			}
			this.focus();
		},
		save: function() {
			if ( this.loading )
				return;
			var v = this._val(), f = this.x.strict === F, t;
			if ( ! this.x.multiple ) {
				while ( this.length > 1 ) this[ 0 ].remove();
			}
			for ( var i = 0, l = this.length, s = []; i < l; i ++ ) {
				if ( f )
					s.push( this[ i ].x.value || this[ i ].x.text );
				else
					this[ i ].x.value && s.push( this[ i ].x.value );
			}
			f && (t = this.queryText() ) && s.push( t );
			this._val( s.join( ',' ) );
			if ( this.x.on && this.x.on.change && v != this._val() )
				this.triggerHandler( 'change' );
		},
		drop: function() {
			if ( this.usa() ) {
				var d = this.dropper, b = d && d.isShow();
				this.closePop();
				this.focus();
				if ( ! b )
					(d || (this.dropper = this.createPop( this.x.dropsrc || this.x.node ))).show();
			}
		},
		pick: Pickbox.prototype.pick,
		setLoading: function( a ) {
			a = a == N || a;
			this.loading = a;
			this.queryText( a ? (this.x.loadingtext || 'loading..') : '' );
			this.$t().contentEditable = ! a;
		},
		html_nodes: function() {
			var s = '<input type=hidden id=' + this.id + 'v name="' + this.input_name() + '" value="' + (this.x.value || '') + '"' + (this.isDisabled() ? ' disabled' : '') + '><div class="f-inbl _c' + (this.x.nobr ? ' f-nobr' : '') + '" id=' + this.id + 'c' + _html_on( this ) +
				' style="width:' + ( this.innerWidth() - this.width_minus() ) + 'px"><var class="_e f-nobr" id=' + this.id + 't' + ( this.usa() ? ' contenteditable' : '' ) + ' onfocus=' + eve + ' onblur=' + eve + '>' + (this.x.loadingtext || 'loading..') + '</var></div>';
			if ( this.x.dropsrc )
				s += '<em class="f-boxbtn _drop" onclick=' + evw + '.drop()><i class=f-vi></i>' + $.arrow( 'b2' ) + '</em>';
			if ( this.x.picker )
				s += '<em class="f-boxbtn _dot" onclick=' + evw + '.pick()></em>';
			return s;
		}
	}
} ),
/* `comboboxoption`
	@x: { value: String, text: String, error: Boolean }
 */
ComboboxOption = define.widget( 'combobox/option', {
	Const: function( x, p ) {
		this.className = '_o f-inbl' + ( x.error ? ' z-err' : '' );
		W.apply( this, arguments );
	},
	Default: { width: -1, height: -1 },
	Listener: {
		tag: 'g',
		body: {
			click: {
				prop: T,
				// 禁用用户事件
				block: $.rt_true,
				method: function() {
					if ( this.loading || this._disposed )
						return;
					// 没有成功匹配文本时只做聚焦和搜索，不执行用户设置的click事件
					var p = this.parentNode;
					if ( this.x.error ) {
						if ( p.focusNode !== this ) {
							this.tabFocus();
							p.suggest( this );
						}
					} else {
						this.triggerHandler( 'click' );
					}
					p.warn( F );
				}
			}
		}
	},
	Rooter: 'combobox',
	Prototype: {
		val: function() {
			return this.x.value;
		},
		text: function() {
			return this.x.text;
		},
		close: function( e ) {
			var p = this.parentNode;
			if ( ! this.isDisabled() && F !== this.trigger( 'close' ) ) {
				this.remove();
				p.save();
				p.resetEffect();
				e && $.stop( e );
			}
		},
		write: function() {
			var p = this.parentNode, t = p.$t();
			t.parentNode.insertBefore( t, this.$() );
			p.focus();
		},
		setLoading: function( a ) {
			$.classAdd( this.$(), 'z-loading', this.loading = a === F ? a : T );
		},
		html_nodes: function() {
			var p = this.parentNode, w = this.width(), t = $.strEscape( this.x.text ), r = this.x.remark ? $.strEscape( this.x.remark ) : N;
			return '<i class="f-inbl _b" onclick=' + evw + '.write()></i><div class="f-inbl _g"' + _html_on( this ) + '><cite class="_v f-omit" style="' + ( w ? 'width:' + (w - 32) : 'max-width:' + ( p.innerWidth() - p.width_minus() - 50 ) ) + 'px" title="' + t + (r ? '\n' + r : '') + '"><i class=f-vi></i><span class=f-va>' +
				( this.x.forbid ? '<s>' : '' ) + t + (r ? '<em class=_r>' + r + '</em>' : '') + ( this.x.forbid ? '</s>' : '' ) + '</span></cite><i class="_x" onclick=' + evw + '.close(event)>&times;</i></div>';
		}
	}
} ),
/* `linkbox` */
Linkbox = define.widget( 'linkbox', {
	Const: function( x ) {
		Combobox.apply( this, arguments );
		x.on && x.on.dblclick && (this.className += ' z-u');
	},
	Extend: 'combobox',
	Listener: {
		block: N,
		body: {
			click: {
				prop: T,
				method: function( e ) {
					if ( this.usa() ) {
						if ( e.srcElement && e.srcElement.tagName === 'U' && ! e.srcElement.getAttribute( 'data-value' ) ) {
							this.suggest( e.srcElement );
						} else {
							var c = $.rngElement();
							c.tagName === 'U' ? this.suggest( c ) : this.closePop();
						}
					}
				}
			},
			dblclick: {
				prop: T,
				method: function( e ) {
					var v = e.srcElement.getAttribute( 'data-value' ), d = this.x.on && this.x.on.dblclick;
					v && d && Function( '$0', d ).call( this, v );
					return F;
				}
			},
			blur: {
				prop: T,
				method: AbsForm.Listener.body.blur.method
			},
			// chrome中文模式打完字后按回车时，不会响应keyup事件，因此设置input事件来触发suggest()
			// fixme: 在chrome的中文模式下输入英文按回车，没有响应
			input: br.ms ? N : {
				prop: T,
				method: function( e ) {
					//this._imeMode && this.fixStyle();
				}
			},
			keydown: {
				prop: T,
				method: function( e ) {
					if ( this.usa() && e.keyCode === 13 )
						return $.stop( e );
					this._KC = e.keyCode;
				}
			},
			keyup: {
				prop: T,
				method: function( e ) {
					if ( this.usa() && ! this._imeMode ) {
						var k = this._KC = e.keyCode;
						if ( k === 13 || k === 38 || k === 40 || k === 17 ) { // 38:up, 40:down, 17:ctrl
							$.stop( e );
							var d = this.pop();
							if ( d.isShow() )
								d.contentView.combo && d.contentView.combo.keyup( k );
							else if ( k === 13 )
								this.fixStyle();
						} else {
							//if ( ! (k === 39 || k === 37) ) // 39: right, 37: left
								this.fixStyle();
						}
						delete this._KC;
						if ( this.x.validate && this.x.validate.maxlength ) {
							this.save();
						}
					}
				}
			},
			resize: function() {
				this.$t().style.width = ( this.innerWidth() - this.width_minus() ) + 'px';
			}
		}
	},
	Prototype: {
		validHooks: {
			valid: function( b, v ) {
				if ( this.x.strict && Q( 'u:not([data-value])', this.$() ).length )
					return _form_err.call( this, b, 'invalid_option' );
			}
		},
		width_minus: function() { return (this.x.dropsrc ? 20 : 0) + (this.x.picker ? 20 : 0) + _input_indent(); },
		init: function() {
			if ( ! this.$() )
				return;
			this._initOptions( this.x.value, this.x.text );
			this.save();
			this.usa() && (this.$t().contentEditable = T);
			$.classRemove( this.$(), 'z-loading' );
			_listen_ime( this, this.$t() );
		},
		// 根据value设置已选项, 初始化时调用 /@v -> value, t -> text
		_initOptions: function( v, t ) {
			var p = this.x.separator || ',';
			if ( v && (v = v.split( ',' )) ) {
				for ( var i = 0, t = t && t.split( p ), o, s = [], l = v.length; i < l; i ++ ) {
					if ( v[ i ] ) {
						if ( t ) {
							var r = { value: v[ i ], text: t[ i ] };
							s.push( '<u class=_o data-value="' + v[ i ] + '">' + $.strEscape( t[ i ] ) + '</u>' );
							this.store().merge( r );
						} else if ( o = this.store().getParamByValue( v[ i ] ) )
							s.push( '<u class=_o data-value="' + v[ i ] + '">' + $.strEscape( o.text ) + '</u>' );
						else if ( ! this.x.strict )
							s.push( '<u>' + $.strEscape( (t && t[ i ]) || v[ i ] ) + '</u>' );
					}
				}
				s.length && (this.$t().innerHTML = s.join( '<i>' + p + '</i>' ) + (this.x.multiple ? '<i>' + p + '</i>' : ''));
			}
		},
		// @a -> value
		val: function( a ) {
			if ( a == N ) {
				this.save();
				return this._val();
			}
			if ( this.loading ) {
				this._val( a );
				return this.addEventOnce( 'load', function() { this.val( a ) } );
			}
			this.empty( T );
			this._val( '' );
			a ? this.match( { value: a }, function() {
				this._initOptions( a );
				this.save();
				this.resetEffect();
			} ) : this.resetEffect();;
		},
		text: function() {
			return this.$t().innerText;
		},
		// 获取正在输入中的用于后台查询的文本
		queryText: function() {
			return this._query_text || '';
		},
		isEmpty: function() {
			return !(this.val() || this.text());
		},
		cursorEnd: function() {
			this._rng_text( this.$t() );
		},
		// 选项上的光标移动到指定位置 /@a -> elem, b -> cursor point, c -> text?
		_rng_text: function( a, b, c ) {
			var n = $.rngSelection();
			c != N && (a.innerText = c);
			n.moveToElementText( a );
			$.rngCursor( n, b );
		},
		// 校订样式
		fixStyle: function() {
			var c = $.rngElement(), g = F, t;
			if ( ! this.$t().contains( c ) )
				c = this.$t();
			t = c.innerText;
			if( this._paste_rng )
				c = this._paste_rng.parentElement();
			if ( c.id === this.id )
				c = this.$t();
			if ( c.tagName === 'VAR' ) {
				c.innerHTML = '<u></u>';
				if ( t ) {
					(c = c.children[ 0 ]).innerText = t;
					$.rngCursor( c );
				} else
					return this.closePop();
			}
			// 输入框内之只允许存在U和I两种标签。输入过程中浏览器可能会自动生成 <font> 标签，需要去除它
			if ( c.tagName === 'FONT' ) {
				if ( c.parentNode.tagName === 'U' ) {
					var d = c.parentNode.previousSibling.nodeValue, c = c.parentNode.parentNode, t = c.innerText;
					this._rng_text( c, t.indexOf( d ) + d.length + 1, t );
				} else if ( c.parentNode.tagName === 'VAR' ) {
					t = c.parentNode.innerText;
					(c = $.replace( c, '<u></u>' )).innerText = t;
				}
			}
			// 如果U标签后面跟的标签不是 <i>,</i> 就要合并
			if ( c.tagName !== 'I' && c.nextSibling && c.nextSibling.tagName !== 'I' ) {
				if ( c.nextSibling.nodeType === 3 ) {
					$.remove( c.nextSibling );
				} else {
					t = c.innerText;
					this._rng_text( c, t.length, t + c.nextSibling.innerText );
					$.remove( c.nextSibling );
					g = T; // 有更新
				}
			}
			! br.ms && Q( 'u:has(br)', this.$t() ).remove();
			t = c.innerText;
			// match text
			var n = [], p = this.x.separator || ',';
			// 文本中如果包含分隔符 , 则分成若干选项
			if ( this.x.multiple && t.length > 1 && t.indexOf( p ) > -1 ) {
				for ( var i = 0, d = [], f = t.length - $.rngCursorOffset(), g, h, t = t.split( p ), m = []; i < t.length; i ++ ) {
					if ( h = $.strTrim( t[ i ] ) ) {
						g = this.store().getParam( h );
						d.push( '<u' + (g ? ' class=_o data-value="' + g.value + '"' : '') + '>' + h + '</u>' );
						g ? m.push( 'u[data-value="' + g.value + '"]' ) : n.push( h );
					} else
						d.push( '' );
				}
				m.length && (m = Q( m.join( ',' ), this.$t() ).next( 'i' ).addBack()); // 收集重复数据，以备删除
				var o = $.replace( c, d.join( '<i>' + p + '</i>' ) ), k = 0;
				if ( ! this._paste_rng || this._KC === 188 ) { // 保留原有光标位置
					do {
						if ( (k += o.innerText.length) >= f ) {
							this._rng_text( o, k - f );
							break;
						}
					} while ( o = o.previousSibling );
				}
				m.length && (m.remove(), Q( 'i + i, i:first-child', this.$t() ).remove());// 删除重复数据
				g = T;
				if ( (c = $.rngElement()) && c.tagName === 'U' && ! c.getAttribute( 'data-value' ) )
					this.suggest( c );
				else
					this.closePop();
			} else {
				this.closePop();
				if ( t ) {
					if ( $.classAny( c, '_o' ) && ! this.store().getParam( t ) ) {
						$.classRemove( c, '_o' );
						c.removeAttribute( 'data-value' );
						g = T;
					}
					this.suggest( c );
				} else
					g = T;
			}
			if ( g ) {
				this.save();
				if ( this.x.suggest ) {
					n.length > 1 && this.match( { text: n.join( p ) } );
				}	
			}
			delete this._paste_rng;
		},
		_suggest_text: function( a ) {
			return a.innerText.replace( /^[\s,]+|[\s,]+$/g, '' );
		},
		// 精确匹配，在隐藏状态下进行 /@ a -> replaceObject, b -> fn?
		match: function( a, b ) {
			if ( this.x.suggest ) {
				var d = this.createPop( this.x.src || this.x.node, a.x || a ), self = this;
				d.preload( function() {
					if ( ! self._disposed && this.contentView.combo ) {
						self.store().merge( this.contentView.combo );
						self.fixOpt();
						b && b.call( self );
					}
					this.remove();
				} );
			} else {
				this.fixOpt();
				b && b.call( this );
			}
		},
		fixOpt: function() {
			var self = this;
			Q( 'u:not([data-value])', this.$t() ).each( function() {
				var g = self.store().getParam( this.innerText );
				if ( g ) {
					$.classAdd( u, '_o' );
					u.setAttribute( 'data-value', g.value );
				}
			} );
			this.save();
		},
		// @r -> range, s -> text
		_paste: function( r, s ) {
			this.focus();
			$.rngCursor( r );
			$.rngText( r, s );
			! this._KC && this.fixStyle();
		},
		//@public(用于 combo 窗口的点击事件) 完成正在输入的文本，或是没有匹配成功的项  / @a -> tr|leaf|xml
		complete: function( a ) {
			this.store().merge( a );
			this.focus();
			var d = this.store().getParam( a ), u = this._currOpt || $.rngElement(), p = this.x.separator || ',', t = this.text();
			if ( d && u && ! $.idsAny( t, d.text, p ) ) {
				if ( u.tagName === 'U' ) {
					this._rng_text( u, 0, d.text );
				} else {
					if ( u.tagName === 'I' ) {
						if ( u.nextSibling && u.nextSibling.tagName === 'U' ) {
							u = u.nextSibling;
						} else {
							u = $.after( u, '<u></u>' );
							this.x.multiple && $.after( u, '<i>' + p + '</i>' );
						}
					} else
						u = (!this.x.multiple && $.get( 'u', this.$t() )) || $.append( this.$t(), '<u></u>' );
					this._rng_text( u, 0, d.text );
				}
				$.classAdd( u, '_o' );
				u.setAttribute( 'data-value', d.value );
				var n = u.nextSibling;
				$.rngCursor( n && ! n.nextSibling ? n : u );
				this.x.multiple && ! n && $.rngCursor( $.after( u, '<i>' + p + '</i>' ) ); // 光标在末尾则自动添加逗号分隔符
				this.save();
			}
		},
		empty: function() {
			this._query_text = this.$t().innerHTML = '';
		},
		save: function() {
			if ( this.loading )
				return;
			var v = this._val();
			for ( var i = 0, q = Q( 'u', this.$() ), l = q.length, s = [], d; i < l; i ++ ) {
				d = q[ i ].getAttribute( 'data-value' );
				if ( this.x.strict )
					d && s.push( d );
				else
					s.push( d || q[ i ].innerText );
			}
			this._val( s = s.join( ',' ) );
			if ( this.x.validate && this.x.validate.maxlength ) {
				_inster( 'tip', N );
				var l = $.strLen( s );
				l > this.x.validate.maxlength && this.cmd( { type: 'tip', text: Loc.ps( Loc.form.over_maxlength, [ l - this.x.validate.maxlength ] ) } );
			}
			if ( this.x.on && this.x.on.change && v != s )
				this.triggerHandler( 'change' );
			if ( this.x.tip === T )
				this.$t().title = this.text();
		},
		drop: function() {
			if ( this.usa() ) {
				this.closePop();
				this.focus();
				this._query_text = N;
				(this.dropper || (this.dropper = this.createPop( this.x.dropsrc || this.x.node ))).show();
			}
		},
		bookmark: function() {
			this._currOpt = $.rngElement();
		},
		html_nodes: function() {
			var s = '<input type=hidden id=' + this.id + 'v name="' + this.input_name() + '" value="' + (this.x.value || '') + '"' + (this.isDisabled() ? ' disabled' : '') + '><var class="f-inbl _t" id=' + this.id + 't' +
				(this.x.tip ? ' title="' + $.strQuot((this.x.tip === T ? this.x.text : this.x.tip) || '') + '"' : '') +
				( this.usa() ? ' contenteditable' : '' ) + _html_on( this ) + ' style="width:' + ( this.innerWidth() - this.width_minus() ) + 'px"></var>';
			if ( this.x.dropsrc )
				s += '<em class="f-boxbtn _drop" onmousedown=' + evw + '.bookmark() onclick=' + evw + '.drop()><i class=f-vi></i>' + $.arrow( 'b2' ) + '</em>';
			if ( this.x.picker )
				s += '<em class="f-boxbtn _dot" onclick=' + evw + '.pick()></em>';
			return s;
		}
	}
} ),
/* `onlinebox` */
Onlinebox = define.widget( 'onlinebox', {
	Extend: [ Text, Combobox ],
	Listener: {
		body: {
			ready: function() {
				_listen_ime( this );
			},
			click: {
				prop: T,
				method: function( e ) {
					if ( this.usa() && e.srcElement.id === this.id + 't' ) {
						var t = this.cursorText();
						t && this.suggest( t );
					}
				}
			},
			keyup: {
				prop: T,
				method: function( e ) {
					if ( ! this._imeMode && ! this.isDisabled() && ! this._disposed && this.x.src ) {
						var k = e.keyCode;
						if ( k === 13 || k === 38 || k === 40 ) { // 上下键
							var d = this.pop(), t;
							if ( k === 13 && (t = this.cursorText()) != this._query_text ) {
								this.suggest( t );
							} else if ( d.isShow() && d.contentView.combo ) {
								k === 13 && ! d.contentView.combo.getFocus() ? _enter_submit( k, this ) : d.contentView.combo.keyup( k );
							} else
								_enter_submit( k, this );
						} else if ( ! e.ctrlKey && k !== 17 ) { // 17: Ctrl
							var t = this.cursorText();
							clearTimeout( this._sug_timer );
							this.suggest( t );
						}
					}
				}
			},
			beforedeactivate: {
				prop: ie,
				method: function() {
					var r = document.selection.createRange(), d = this.$t().createTextRange();
					d.setEndPoint( 'EndToEnd', r );
					this._csr_pos = d.text.length - r.text.length;
				}
			},
			keydown: N,
			paste: N
		}
	},
	Prototype: {
		formType: 'text',
		_csr_pos: 0,
		width_minus: function() { return (this.x.dropsrc ? 20 : 0) + (this.x.picker ? 20 : 0) + _input_indent(); },
		// @a -> text /读/写光标所在的有效文本(以逗号为分隔符)
		cursorText: function( a ) {
			var b = this.val(),
				c = this.getSelectionStart(),
				i = c, j = c, l = b.length, d, p = this.x.separator || ',';
			while ( i -- && b.charAt( i ) !== p );
			while ( j < l && b.charAt( j ) !== p ) j ++;
			if ( a ) {
				this.val( b.slice( 0, i + 1 ) + a + (j === l && this.x.multiple ? p : '') + b.slice( j ) );
			} else
				return this.x.multiple ? b.substring( i + 1, j ) : b;
		},
		// 获取正在输入中的用于后台查询的文本
		queryText: function() {
			return this._query_text || '';
		},
		getSelectionStart: function() {
			if ( ie ) { // ie下需要在失去焦点前记下光标位置。其他浏览器会自动记忆上次位置
				if ( document.activeElement === this.$t() )
					this.triggerListener( 'beforedeactivate' );
				return this._csr_pos;
			} else
				return this.$t().selectionStart;
		},
		complete: function( a ) {
			var d = this.store( this.pop() ).getParam( a );
			this.cursorText( d.text || d.value );
			this.closePop();
			this.focus();
		},
		suggest: function( t ) {
			this.x.src && Combobox.prototype.suggest.apply( this, arguments );
		},
		doSuggest: function( t ) {
			var u = $.urlFormat( this.x.src, { text: t, value: t } );
			if ( ! this.more )
				this.more = this.createPop.call( this, u );
			this.more.reload( u );
			this.more.show();
		},
		html_nodes: function() {
			var s = Text.prototype.html_nodes.apply( this, arguments );
			if ( this.x.dropsrc )
				s += '<em class="f-boxbtn _drop" onclick=' + evw + '.drop()><i class=f-vi></i>' + $.arrow( 'b2' ) + '</em>';
			if ( this.x.picker )
				s += '<em class="f-boxbtn _dot" onclick=' + evw + '.pick()></em>';
			return s;
		}
	}
} ),
/* `rate` */
Rate = define.widget( 'rate', {
	Extend: AbsForm,
	Default: {
		width: -1
	},
	Prototype: {
		className: 'w-rate',
		$v: function() { return $( this.id + 'v' ) },
		val: function( a ) {
			if ( a === U )
				return (this.$v() || this.x).value;
			if ( this.lastValue === U )
				this.lastValue = this.x.value;
			this._val( a );
			if ( a != this.lastValue ) {
				this.lastValue = a;
				this.trigger( 'change' );
			}
		},
		_val: function( a ) {
			if ( this.$() ) {
				this.star( a );
				this.$v().value = a;
			} else
				this.x.value = a;
		},
		star: function( a ) {
			var a = _number( a ), b = Math.floor( a / 2 ), c = this.$( b * 2 );
			(c = c && Q( c )) && c.prevAll().addBack().addClass( 'z-on' ).removeClass( 'z-in' );
			(c ? c.nextAll() : Q( 'i', this.$() )).removeClass( 'z-on z-in' );
			(c && (a % 2)) && c.next().addClass( 'z-in' );
		},
		click: function( a ) {
			this.usa() && this.val( a );
		},
		over: function( a ) {
			this.usa() && this.star( a );
		},
		out: function() {
			this.usa() && this._val( this.val() );
		},
		html_nodes: function() {
			for ( var i = 2, s = '', v = _number( this.x.value ); i <= 10; i += 2 ) {
				s += '<i id=' + this.id + i + ' class="_i' + (v >= i ? ' z-on' : v > i - 2 ? ' z-in' : '') + '" onmouseover=' + evw + '.over(' + i + ') onclick=' + evw + '.click(' + i + ')></i>';
			}
			return '<span onmouseout=' + evw + '.out()>' + s + '</span><input type=hidden id=' + this.id + 'v name=' + this.x.name + ' value="' + (v || '') + '"' + (this.isDisabled() ? ' disabled' : '') + '>';
		}
	}
} ),
// 树搜索过滤器
TreeCombo = $.createClass( {
	Const: function( a ) {
		this.cab = a;
		this.xml = this.node2xml( a );
		a.x.combo && (this._keep_show = a.x.combo.keepshow);
		a.x.highlight && (this._matchlength = a.x.highlight.matchlength);
	},
	Prototype: {
		isCombo: T,
		node2xml: function( a ) {
			var b = (a.rootNode || a).x.combo.field, f = b.search && b.search.split( ',' ), g = f && f.length;
			this._sch = g;
			return $.xmlParse( this._node2xml( a ) );
		},
		// 把 json 转成 xml，以便使用 xpath 查询
		_node2xml: function( a ) {
			for ( var i = 0, b = (a.rootNode || a).x.combo.field, c = [], d, e, f = b.search && b.search.split( ',' ), g = f && f.length, l = a.length, s; i < l; i ++ ) {
				e = a[ i ].x, d = e.data || F, r = d[ b.remark ] || e[ b.remark ], s = d[ b.search ] || e[ b.search ];
				s = '<d v="' + $.strEscape( d[ b.value ] || e[ b.value ] || '' ) + '" t="' + $.strEscape(d[ b.text ] || e[ b.text ]) + '" i="' + a[ i ].id + '"';
				r && (s += ' r="' + $.strEscape( r ) + '"');
				(d[ b.forbid ] || e[ b.forbid ]) && (s += ' x="1"');
				if ( f ) {
					for ( j = 0; j < g; j ++ )
						s += ' s' + j + '="' + $.strEscape( d[ f[ j ] ] ) + '"';
				}
				a[ i ].isDisabled() && (s += ' ds="1"');
				c.push( s + '>' );
				a[ i ].length && c.push( arguments.callee( a[ i ] ) );
				c.push( '</d>' );
			}
			return '<doc>' + c.join( '' ) + '</doc>';
		},
		first: function() {
			return this.cab._filter_leaves ? this.cab._filter_leaves[ 0 ] : this.cab[ 0 ];
		},
		keyup: function( a ) {
			this.cab.keyup( a );
		},
		isKeepShow: function() {
			return this._keep_show;
		},
		getFocus: function() {
			return this.cab.getFocus();
		},
		// 根据文本返回一个combobox/option参数 /@a -> text|xml|leaf
		getParam: function( a, b ) {
			var d = a.nodeType === 1 ? a : this.getXML( a, b );
			return d && { value: d.getAttribute( 'v' ), text: d.getAttribute( 't' ), remark: d.getAttribute( 'r' ), forbid: d.getAttribute( 'x' ) === '1', data: (b = _all[ d.getAttribute( 'i' ) ]) && b.x.data };
		},
		// 根据文本返回一个combobox/option参数 /@a -> text
		getParamByValue: function( a ) {
			return this.getParam( a, 'v' );
		},
		// /@a -> text|value|leaf, b -> attrname
		getXML: function( a, b ) {
			return $.xmlQuery( (a.isWidget ? a.ownerView.combo : this).xml, './/d[' + ( typeof a === _STR ? '@' + ( b || 't' ) + '="' + $.strTrim( a ).replace(/\"/g,'\\x34') : '@i="' + a.id ) + '"]' );
		},
		// 合并来自另一个grid的某一行的combo xml /@a -> tr|xml|treeCombo
		merge: function( a ) {
			a.isWidget && (a = a.ownerView.combo.getXML( a ) || F);
			if ( a.nodeType === 1 ) {
				if ( ! $.xmlQuery( this.xml, './/d[@v="' + a.getAttribute( 'v' ) + '"]' ) )
					this.xml.appendChild( a.cloneNode( T ) );
			} else if ( a.isCombo ) {
				for ( var i = 0, b = $.xmlQueryAll( a.xml, './/d' ), l = b.length; i < l; i ++ )
					this.merge( b[ i ] );
			} else if ( a.value ) {
				if ( ! $.xmlQuery( this.xml, './/d[@v="' + a.value + '"]' ) ) {
					var d = this.xml.ownerDocument.createElement( 'd' );
					d.setAttribute( 'v', a.value ), d.setAttribute( 't', a.text );
				}
			}
		},
		// 根据关键词过滤得到有效节点 /@a -> keyword
		_filter: function( a ) {
			if ( a ) {
				var b = $.strSplitword( a, this._matchlength ), e = [];
				for ( var i = 0, c; i < b.length; i ++ ) {
					c = $.strQuot( b[ i ] );
					e.push( 'contains(@t,"' + c + '")', 'contains(@r,"' + c + '")' );
					if ( this._sch ) {
						for ( var j = 0; j < this._sch; j ++ )
							e.push( 'contains(@s' + j + ',"' + c + '")' );
					}
				}
				for ( var i = 0, f = [], d = $.xmlQueryAll( this.xml, './/d[(' + e.join( ' or ' ) + ') and @v!="" and not(@ds)]' ), l = d.length; i < l; i ++ ) //translate(@t,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')
					f.push( _all[ d[ i ].getAttribute( 'i' ) ] );
				return f;
			}
		},
		filter: function( t ) {
			var a = this.cab, f;
			if ( a.length )
				a.setFilter( this._filter( t ) );
			return a._filter_leaves && a._filter_leaves.length;
		},
		getKey: function() {
			var d = Dialog.get( this.cab.ownerView );
			return d && d.commander.queryText();
		},
		getLength: function() {
			return this.cab.length;
		}
	}
} ),
/* `absleaf` */
AbsLeaf = define.widget( 'abs/leaf', {
	Listener: {
		body: {
			dnd_sort: function( e, o ) {
				if ( o ) o.style.left = (this.level * this._pad_level + this._pad_left + 14) + 'px';
			}
		}
	},
	Prototype: {
		_pad_left: 5,
		_pad_level: 12,
		// @implement
		insertHTML: function( a, b ) {
			var c = a.isWidget && a.$(), d = c && a.$( 'c' );
			$[ b || 'append' ]( this.$( b === 'before' ? N : 'c' ), c || a );
			if ( d ) {
				$.after( a.$(), d );
				a.indent();
			}
		},
		// 尚未装载的节点不直接增加子节点
		append: function( a ) {
			if ( this.x.src && ! this.loaded )
				a.isWidget && a.remove();
			else
				_proto.append.apply( this, arguments );
		},
		// 尚未装载的节点不直接增加子节点
		prepend: function( a ) {
			if ( this.x.src && ! this.loaded )
				a.isWidget && a.remove();
			else
				_proto.prepend.apply( this, arguments );
		},
		attrSetter: function( a, b, c ) {
			switch( a ) {
				case 'text':
					this.$( 't' ) && $.html( this.$( 't' ), this.html_text() );
				break;
				case 'icon': case 'openicon':
					this.$( 'i' ) && $.replace( this.$( 'i' ), this.html_icon() );
				break;
				case 'focus':
					this.focus( b );
				break;
				case 'src':
					this.fixFolder();
				break;
				case 'cls':
					this.removeClass( c );
					this.addClass( b );
				break;
			}
		},
		indent: function() {
			this.level = this.parentNode.level + 1;
			this.$().style.paddingLeft = (this.level * this._pad_level + this._pad_left) + 'px';
			for ( var i = 0; i < this.length; i ++ )
				this[ i ].indent();
		},
		isFolder: function() {
			return this.length || (this.x.src && !this.loaded) ? T : F;
		},
		fixFolder: function() {
			this.addClass( 'z-folder', this.isFolder() );
			if ( this.$( 'o' ) && ! this.$( 'r' ) )
				$.prepend( this.$( 'o' ), $.arrow( this.id + 'r', this.isOpen() ? 'b1' : 'r1' ) );
		},
		// @a -> sync? b -> fn?
		request: function( a, b ) {
			this.loading = T;
			this._request( this.x.src, function( x ) { // success
				var n = x.nodes || x;
				n.length && this.render_nodes( n );
				this.trigger( 'load' );
				b && b.call( this );
			}, function( x ) { // complete
				this.loading = F;
				! this.loaded && this.toggle( F );
				W.isCmd( x ) && this.cmd( x );
				this.fixFolder();
				if ( this.$( 'o' ) ) {
					$.classRemove( this.$(), 'z-loading' );
					if ( x = this.html_icon() ) {
						this.$( 'i' ) && $.replace( this.$( 'i' ), x );
					} else
						Q( '._ld', this.$() ).remove();
				}
			}, a );
		},
		//@a -> src, b -> success, c -> complete, d -> sync?
		_request: function( a, b, c, d ) {
			a.indexOf( '$' ) > -1 && (a = $.urlFormat( a, this ));
			this.cmd( { type: 'ajax', src: a, success: b, complete: c, sync: d } );
		},
		render_nodes: function( x ) {
			for ( var j = 0, l = x.length; j < l; j ++ )
				this.add( x[ j ] );
			this.$( 'c' ) && (this.$( 'c' ).innerHTML = _proto.html_nodes.call( this ));
			if ( (this.rootNode || this).x.combo ) {
				var o = new TreeCombo( this ).xml, m = this.ownerView.combo.getXML( this );
				while ( o.firstChild )
					m.appendChild( o.firstChild );
			}
			this.loaded = T;
			for ( j = 0; j < l; j ++ )
				this[ j ].triggerAll( 'ready' );
			//this.trigger( 'nodechange' );
		},
		toggle_nodes: function( a ) {
			if ( this.x.src || this.length ) {
				var c = typeof a === _BOL ? a : ! this.x.open;
				this.x.open = c;
				this.$( 'c' ) && $.classAdd( this.$( 'c' ), 'f-none', ! c );
			}
		},
		// 展开或收拢 /@a -> T/F/event, b -> sync?, f -> fn?
		toggle: function( a, b, f ) {
			this.toggle_nodes( a );
			var c = this.x.open, d;
			if ( this.x.src && c && ! this.loaded && ! this.loading ) 
				this.request( b, f );
			if ( this.$( 'r' ) )
				$.arrow( this.$( 'r' ), c ? 'b1' : 'r1' );
			if ( this.loading ) {
				$.append( this.$( 'i' ) || this.$( 'o' ), '<i class=_ld></i>' );
				$.classAdd( this.$(), 'z-loading' );
			} else if ( this.$( 'i' ) && (d = this.html_icon()) ) {
				$.replace( this.$( 'i' ), d );
			}
			a && a.type && $.stop( a );
			this.trigger( this.x.open ? 'expand' : 'collapse' );
		},
		// 当前节点展开时，其他兄弟节点全部收起 /@a -> T/F/event, b -> sync?, f -> fn?
		toggleOne: function( a, b, f ) {
			this.toggle.apply( this, arguments );
			if ( this.isOpen() ) {
				for ( var i = 0, p = this.parentNode; i < p.length; i ++ ) {
					p[ i ] !== this && p[ i ].toggle( F );
				}
			}
		},
		compare: function( x ) {
			if ( x.text ) {
				var _x = this.x, b = [ 'icon', 'openicon', 'src', 'cls' ];
				this.init_x( x );
				for ( var i = 0, l = b.length, e; i < l; i ++ ) {
					e = b[ i ];
					if ( _x[ e ] != x[ e ] )
						this.attr( e, x[ e ] || '' );
				}
				if ( this.x.format || (_x.text != x.text) ) {
					this.$( 't' ) && $.html( this.$( 't' ), this.html_text() );
				}
			}
			var n = x.nodes, l = n && n.length;
			if ( l && ! this.loading ) {
				if ( ! this.length ) {
					this.render_nodes( n );
					x.open && this.toggle( T );
				} else {
					for ( var i = 0, b, c; i < l; i ++ ) {
						b = n[ i ];
						if ( this[ i ] && b.id == this[ i ].x.id ) {
							this[ i ].compare( b );
						} else {
							c = (c = this.ownerView.find( b.id )) ? c.compare( b ) : b;
							if ( this[ i ] ) {
								this[ i ].before( c );
							} else {
								this[ i - 1 ] ? this[ i - 1 ].after( c ) : this.append( c );
							}
						}
					}
					for ( i = this.length - 1; i >= l; i -- ) {
						this[ i ].remove();
					}
				}
			}
			return this;
		},
		// 深度展开。leaf需要有id进行对比  /@a -> src, b -> sync?, c -> fn
		openTo: function( a, b, c ) {
			if ( typeof b === _FUN ) {
				c = b, b = N;
			}	
			var f = (this.rootNode || this).getFocus();
			this._request( a, function( x ) {
				if ( ! this._disposed ) {
					var d = x.id ? this.ownerView.find( x.id ) : this;
					d && d.compare( x );
				}
			}, function() {
				 if ( ! this._disposed ) {
				 	var d = (this.rootNode || this).getFocus();
					d && d !== f && d.scrollIntoView();
					c && c.call( this );
				}
			}, b === T );
		},
		draggable: function( a ) {
			for ( var i = 0, b = this.getAll(), l = b.length; i < l; i ++ ) {
				$.draggable( b[ i ], a );
				b[ i ].x.src && ! b[ i ].loaded && b[ i ].addEvent( 'load', function() { Tree.prototype.draggable.call( this, a ) } );
			}
			return this;
		},
		droppable: function( a ) {
			for ( var i = 0, b = this.getAll(), l = b.length; i < l; i ++ ) {
				$.droppable( b[ i ], a );
				b[ i ].x.src && ! b[ i ].loaded && b[ i ].addEvent( 'load', function() { Tree.prototype.droppable.call( this, a ) } );
			}
			return this;
		},
		// @a -> sync?
		reload: function( a ) {
			if ( ! this.loading && this.x.src ) {
				this.toggle( F );
				$.ajaxAbort( this );
				var d = this.focusNode && this.focusNode.id, i = this.length;
				while( i -- ) this[ i ].remove();
				this.$( 'o' ).style.visibility = '';
				this.loaded = this.loading = F;
				this.toggle( T, a, function() { d && (d = this.ownerView.find( d )) && d.focus() } );
			}
		},
		// 获取最新的子节点数据，对比原有数据，如果有新增节点就显示出来 / @a -> sync?, b -> fn?
		reloadForAdd: function( a, b ) {
			if ( this._disposed || this.loading ) return;
			if ( typeof a === _FUN ) {
				b = a, a = N;
			}
			if ( this.x.src ) {
				this.openTo( this.x.src, function() { b && b.call( this ); } );
			} else {
				this.reloadForModify( a, function() { this.x.src && this.toggle( T, b ) } );
			}
		},
		// 获取父节点的所有子节点数据，取出id相同的项进行更新 / @a -> sync?, b -> fn?
		reloadForModify: function( a, b ) {
			if ( this._disposed )
				return;
			if ( this.loading )
				$.ajaxAbort( this );
			if ( this.parentNode.x.src && ! this.parentNode.loading ) {
				var self = this;
				this.parentNode.openTo( this.parentNode.x.src, function() { b && b.call( self ); } );
			}
		},
		removeElem: function( a ) {
			$.remove( this.$( 'c' ) );
			_proto.removeElem.call( this, a );
		}
	}
} ),
/* `leaf` */
Leaf = define.widget( 'leaf', {
	Const: function( x, p ) {
		this.level = p.level + 1;
		W.apply( this, arguments );
		this.loaded  = this.length ? T : F;
		this.loading = F;
		this.x.focus && this.tabFocus();
	},
	Extend: AbsLeaf,
	Child: 'leaf',
	Rooter: 'tree',
	Default: { width: -1, height: -1 },
	Listener: {
		body: {
			ready: function() {
				this.x.src && this.x.open && ! this.loaded && this.toggle( T );
				this.x.focus && this.focus();
			},
			mouseover: {
				prop: T,
				method: function() { ! this.isDisabled() && $.classAdd( this.$(), 'z-hv' ); }
			},
			mouseout: {
				prop: T,
				method: function() { ! this.isDisabled() && $.classRemove( this.$(), 'z-hv' ); }
			},
			click: {
				prop: T,
				// 点击box不触发业务设置的click事件
				block: function( e ) {
					return this.isDisabled();
				},
				method: function( e ) {
					if ( this.isDisabled() )
						return;
					this.box && this.box.x.sync === 'click' && ! this.isEvent4Box( e ) && this.box.click();
					e.srcElement ? this._focus( T, e ) : this.focus( T, e );
					if( this.rootNode && this.rootNode.x.combo ) {
						$.dialog( this ).commander.complete( this );
						$.close( this );
					}
				}
			},
			dblclick: {
				prop: T,
				method: function( e ) {
					if ( ! ( this.$( 'o' ) && this.$( 'o' ).contains( e.srcElement ) ) )
						this.toggle();
				}
			},
			nodechange: function() {
				this.fixFolder();
				if ( ! this.length )
					this.loaded = this.x.open = F;
			}
		}
	},
	Prototype: {
		className: 'w-leaf',
		// @a 设为 true 时，获取视觉范围内可见的相邻的下一个节点
		next: function( a ) {
			if ( a == N )
				return _proto.next.call( this );
			if ( a && this.rootNode ) {
				if ( this.length && this.x.open )
					return this[ 0 ];
				if ( this.nodeIndex === this.parentNode.length - 1 ) {
					var p = this.parentNode;
					while ( p.rootNode && ! p.next() ) p = p.parentNode;
					return p.rootNode && p.next();
				} else
					return _proto.next.call( this );
			}
		},
		// @a 设为 true 时，获取视觉范围内可见的相邻的下一个节点
		prev: function( a ) {
			var b = _proto.prev.call( this );
			if ( a ) {
				if ( ! b && this.parentNode !== this.rootNode )
					return this.parentNode;
				while ( b && b.length && b.x.open )
					b = b[ b.length - 1 ];
			}
			return b;
		},
		focus: function( a, e ) {
			this._focus( a, e );
			a !== F && this.scrollIntoView( 'auto' );
		},
		_focus: function( a, e ) {
			a = a == N ? T : a;
			this.tabFocus( a );
			a !== F && this.triggerHandler( 'focus' );
			if ( this.box && this.box.x.sync === 'focus' && ! this.isEvent4Box( e ) ) {
				if ( a ) {
					for ( var i = 0, r = this.rootNode.getAll(), l = r.length; i < l; i ++ )
						r[ i ] !== this && r[ i ].checkBox( F );
				}
				this.checkBox( a );
			}
		},
		isEvent4Box: function( e ) {
			return this.box && e && e.srcElement && e.srcElement.id == this.box.id + 't';
		},
		offsetParent: function() {
			return this.rootNode;
		},
		scrollIntoView: function( a ) {
			var n = this;
			while ( (n = n.parentNode) && n.type === this.type )
				n.toggle( T );
			_scrollIntoView( this, T, a );
		},
		isFocus: function() {
			return this.rootNode.focusNode === this;
		},
		isOpen: function() {
			return this.x.open;
		},
		checkBox: function( a ) {
			this.box && this.box.click( a == N || a );
		},
		isBoxChecked: function() {
			return this.box && this.box.isChecked();
		},
		// triplebox 级联勾选
		_triple: function() {
			var p = this;
			while ( (p = p.parentNode) && p.rootNode === this.rootNode ) {
				if ( p.box ) {
					for ( var i = 0, b, m = [ 0, 0, 0 ], l = p.length; i < l; i ++ )
						(b = p[ i ].box) && m[ b.checkstate() ] ++;
					p.box.checkstate( m[ 0 ] === l ? 0 : m[ 1 ] === l ? 1 : 2 );
				}
			}
			this._tripleAll( this.box.checkstate() == 1 );
		},
		_tripleAll: function( a ) {
			for ( var i = 0, b, l = this.length; i < l; i ++ ) {
				(b = this[ i ].box) && b.check( a );
				this[ i ]._tripleAll( a );
			}
		},
		isEllipsis: function() {
			return this.rootNode.x.ellipsis;
		},
		html_icon: function() {
			var c = (this.x.open && this.length && this.x.openicon) || this.x.icon;
			return c ? $.image( c, { id: this.id + 'i', cls: 'w-leaf-i' } ) : '';
		},
		html_text: function() {
			var r = this.rootNode, t = this.x.text, h;
			if ( this.x.format )
				t = _grid_format.call( this, this.x.format, r.x.escape );
			else if ( r && r.x.escape && typeof t === _STR )
				t = $.strEscape( t );
			if ( t != N && (h = r.x.highlight) && ! this.isDisabled() ) {
				var key = h.key == N ? (this.ownerView.combo && this.ownerView.combo.getKey()) : h.key;
				key && (t = $.strHighlight( t, key, h.matchlength, h.keycls ));
			}
			if ( typeof t === _OBJ )
				t = (this.textNode = this.add( t, -1 )).addClass( 'w-leaf-node' ).html();
			else
				t = '<span class=w-leaf-s>' + t + '</span><i class=f-vi></i>';
			return t;
		},
		html_self: function( l ) {
			var x = this.x, r = this.rootNode, c = this.html_icon(), d = x.data, e = '<i class=f-vi></i>', f = this.level * this._pad_level + this._pad_left, h = this.innerHeight(), s = '';
			if ( x.box ) {
				this.box = Checkbox.parseOption( this, { cls: 'w-leaf-b', bubble: F } );
				this.box.type === 'triplebox' && this.box.addEvent( 'click', this._triple, this );
			}
			h != N  && (s += 'height:' + h + 'px;');
			x.style && (s += x.style);
			l == N  && (l = this.length);
			return '<dl class="' + this.className + (x.cls ? ' ' + x.cls : '') + (this.isDisabled() ? ' z-ds' : '') + (x.src || l ? ' z-folder' : '') + (this.isEllipsis() ? ' f-omit' : ' f-nobr') + '" id=' + this.id + (x.tip ? ' title="' + $.strQuot( x.tip === T ? (typeof x.text === _OBJ ? '' : x.text) : x.tip ) + '"' : '') + _html_on_child( this ) +
				(x.id ? ' w-id="' + x.id + '"' : '') + ' style="padding-left:' + f + 'px;' + s + '">' + this.html_before() + '<dt class="w-leaf-a">' +
				(x.hidetoggle ? '' : '<b class=w-leaf-o id=' + this.id + 'o onclick=' + evw + '.toggle(event)>' + (x.src || l ? $.arrow( this.id + 'r', x.open ? 'b1' : 'r1' ) : '') + e + '</b>') +
				( this.box ? this.box.html() : '' ) + c + '<cite class=w-leaf-t id=' + this.id + 't>' + this.html_text() + '</cite></dt>' + this.html_after() + '</dl>';
		},
		html: function() {
			var f = this.rootNode._filter_leaves, b = !f, s = this.html_nodes();
			if ( f ) {
				for ( var i = 0; i < f.length; i ++ ) {
					if ( this.contains( f[ i ] ) ) { b = T; break; }
				}
				this.x.status = $.arrIn( f, this ) ? '' : 'disabled';
				$.classAdd( this, 'z-notg', !!(this.length && !s) ); // 子节点都被过滤时，隐藏tg
			}
			return b ? this.html_self() + '<div class="w-leaf-cont' + ( this.x.open ? '' : ' f-none' ) + '" id=' + this.id + 'c>' + s + '</div>' : '';
		}
	}
} ),
/* `tree` */
Tree = define.widget( 'tree', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		if ( x.combo )
			this.ownerView.combo = new TreeCombo( this );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'hiddens', nodes: x.hiddens }, -1 );
		this.loaded  = this.length ? T : F;
		this.loading = F;
	},
	Extend: [ Scroll, AbsLeaf ],
	Child: 'leaf',
	Listener: {
		body: {
			ready: function() {
				_scrollIntoView( this.getFocus() );
				if ( this.x.src && ! this.length )
					Leaf.prototype.request.call( this );
			}
		}
	},
	Prototype: {
		NODE_ROOT: T,
		className: 'w-tree',
		level: -1,
		reloadForModify: $.rt_null,
		focus: function() {	this[ 0 ] && this[ 0 ].focus(); },
		getFocus: function() { return this.focusNode },
		// @f -> filter leaves(经过筛选的行)
		setFilter: function( f ) { this._filter_leaves = f },
		//@implement  /@ a -> html|widget, b -> method(prepend,append,after,before)
		insertHTML: function( a, b ) {
			Scroll.prototype.insertHTML.call( this, a, b );
			a.isWidget && a.indent();
		},
		// @k -> keycode
		keyup: function( k ) {
			var f = this._filter_leaves, d = k === 40, a;
			if ( d || k === 38 ) { // key down/up
				var a = this.getFocus();
				 b = a ? ( f ? f[ $.arrIndex( f, a ) + ( d ? 1 : -1 ) ] : ( d ? a.next( T ) : a.prev( T ) ) ) : this[ d ? 0 : r.length - 1 ];
				b ? b.focus() : (a && a.focus( F ));
			} else if ( k === 13 ) {
				(a = this.getFocus()) && a.click();
			}
		},
		droppable: function( a ) {
			$.droppable( this, $.extend( { sort: F }, a || {} ) );
			return AbsLeaf.prototype.droppable.call( this, a );
		},
		html_nodes: function() { return '<div id=' + this.id + 'c class=w-tree-gut>' + _proto.html_nodes.call( this ) + '</div>' + (this._hiddens ? this._hiddens.html() : '') }
	}
} ),
Hiddens = define.widget( 'hiddens', {
	Prototype: { html: _proto.html_nodes },
	Default: { width: 0, height: 0 },
	Child: 'hidden'
} ),
/* grid 辅助方法和专用类 */
// @a -> content|js, b -> escape?, k -> key, s -> key style
_grid_format = function( a, b ) {
	if ( (typeof a === _FUN && (a = a.toString())) || a.indexOf( 'javascript:' ) === 0 )
		return _jsformat.call( this, a, [], [], _grid_f_attr );
	var x = this.x;
	return a.replace( /\$(\w+|\{[\w.]+\})/g, function( $0, $1 ) {
		$1 = $1.replace( /[\{\}]/g, '' );
		var c = $1.indexOf( '.' ) > 0, k = $1, v, t;
		if ( c ) {
			k = $.strTo( k, '.' );
			t = $.strFrom( $1, k );
		}
		v = (typeof x.data === _OBJ && (k in x.data) ? x.data : x)[ k ];
		if ( t && v != N ) {
			try { eval( 'v = v' + t ); } catch( ex ) { v = N; }
		}
		v = _grid_f_attr( v );
		return v && b ? $.strEscape( v ) : (v == N ? '' : v);
	} );
},
_grid_f_attr = function( v ) {
	return typeof v === _OBJ && v && (! v.type || v.type === TD.type) ? (v.text || '') : v;
},
_grid_tr = function() {
	return this.closest( function() { return this.type_tr || this.type_hr } );
},
/* `gridleaf` */
GridLeaf = define.widget( 'grid/leaf', {
	Const: function( x, p ) {
		Leaf.apply( this, arguments );
		var r = this.tr();
		this.level = r.level;
		this.rootNode = r.rootNode;
		x.src && (this.loaded = !!r.length);
		x.open == N && (x.open = !x.src || !!r.length);
		r.leaf = this;
	},
	Extend: Leaf,
	Child: 'grid/leaf',
	Rooter: N,
	Listener: {
		body: {
			ready: function() {
				Leaf.Listener.body.ready.apply( this, arguments );
				this.textNode && this.fix_text_size();
				this.x.open === F && this.toggle( F );
			},
			resize: function() {
				this.textNode && _w_css.width.call( this.textNode );
			},
			mouseover: N,
			mouseout: N
		}
	},
	Prototype: {
		className: 'w-leaf w-grid-leaf',
		_pad_left: 0,
		tr: _grid_tr,
		_focus: $.rt_null,
		isEllipsis: $.rt_true,
		scaleWidth: function( a ) {
			return this.parentNode.scaleWidth( a );
		},
		fix_text_size: function() {
			var t = this.textNode, a = $.bcr( this.$() ), b = $.bcr( t.$() ), c = b.left - a.left;
			if ( c > 0 ) {
				t.attr( 'wmin', (t.attr( 'wmin' ) || 0) + c );
				t.width( t.attr( 'width' ) || '*' );
			}
		},
		isFolder: function() {
			return this.tr().length || (this.x.src && !this.loaded) ? T : F;
		},
		// leaf接口
		toggle_nodes: function( a ) {
			var t = this.tr();
			if ( this.x.src || t.length ) {
				var c = typeof a === _BOL ? a : ! this.x.open;
				t.toggle_rows( c );
				this.x.open = c;
			}
		},
		// leaf接口
		render_nodes: function( x ) {
			for ( var i = 0, l = x.length, r = this.tr(); i < l; i ++ )
				r.add( x[ i ] );
			l && Q( r.$() ).after( r.html_nodes() );
			this.loaded = T;
		},
		offsetParent: function() {
			return this.closest( 'td' );
		},
		html: function() {
			var r = this.tr(), s = this.html_self( r.length );
			return this.x.hr ? '<table class=w-hr-table cellspacing=0 cellpadding=0><tr><td>' + s + '<td width=100%><hr class=w-hr-line noshade></table>' : s;
		}
	}
} ),
/* `gridtoggle` */
GridToggle = define.widget( 'grid/toggle', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.tr().tgl = this;
	},
	Extend: Toggle,
	Listener: {
		body: { click: N }
	},
	Prototype: {
		tr: function() {
			return this.closest( 'tr' );
		},
		isOpen: function() {
			return this.x.open;
		},
		toggle: function( a ) {
			var a = a == N ? ! (this.x.open == N ? T : this.x.open) : a, t = this.tr();
			this.x.open = a;
			t.toggle_rows( a );
			for ( var i = t.$().rowIndex + 1, b = t.$().offsetParent.rows, c, l = b.length; i < l; i ++ ) {
				c = _widget( b[ i ] );
				if ( c.tgl )
					break;
				c.display( this );
			}
			return Toggle.prototype.toggle.apply( this, arguments );
		}
	}
} ),
/* `gridrownum` */
GridRownum = define.widget( 'grid/rownum', {
	Const: function( x, p ) {
		Html.apply( this, arguments );
		var r = this.tr(), t = _number( this.attr( 'start' ) );
		x.text = (t + r.nodeIndex);
		r.rownum = this;
	},
	Extend: Html,
	Default: { start: 1 },
	Prototype: {
		tr: _grid_tr,
		reset: function() {
			this.text( _number( this.attr( 'start' ) ) + this.tr().nodeIndex );
		}
	}
} ),
/* `gridtriplebox` */
GridTriplebox = define.widget( 'grid/triplebox', {
	Const: function( x, p ) {
		Triplebox.apply( this, arguments );
		this.tr().box = this;
	},
	Extend: Triplebox,
	Listener: {
		body: {
			ready: function() {
				Triplebox.Listener.body.ready.apply( this, arguments );
				this.x.checked && this.triggerListener( 'change' );
			},
			change: {
				prop: T,
				method: function() {
					var r = this.tr();
					r.type_tr && r.addClass( 'z-checked', this.isChecked() );
				}
			}
		}
	},
	Prototype: {
		className: 'w-checkbox w-triplebox',
		tr: _grid_tr
	}
} ),
/* `gridradio` */
GridRadio = define.widget( 'grid/radio', {
	Const: function( x, p ) {
		Radio.apply( this, arguments );
		this.tr().box = this;
	},
	Extend: Radio,
	Listener: {
		body: {
			ready: function() {
				Radio.Listener.body.ready.apply( this, arguments );
				this.x.checked && this.triggerListener( 'change' );
			},
			change: {
				prop: T,
				method: function() {
					var r = this.tr();
					Q( '>.z-checked', r.parentNode.$() ).removeClass( 'z-checked' );
					r.type_tr && r.addClass( 'z-checked', this.isChecked() );
				}
			}
		}
	},
	Prototype: {
		tr: _grid_tr
	}
} ),
/* `gridrow` tr 和 hr 的基础类 */
GridRow = define.widget( 'grid/row', {
	Const: function( x, p, n ) {
		this.rootNode = p.rootNode;
		this.level = p.level == N ? 0 : p.level + 1;
		if ( typeof x.data !== _OBJ )
			x = { data: x };
		W.call( this, x, p, n );
		x.focus && $.classAdd( this, 'z-on' );
	},
	Prototype: {
		className: 'w-tr',
		getData: function() {
			var d = { data: this.x.data }, l = this.length;
			if ( l ) {
				for ( var i = 0, r = []; i < l; i ++ )
					r.push( this[ i ].getData() );
				d.rows = r;
			}
			return d;
		},
		// x -> widget option, i -> colIndex
		addCell: function( x, i ) {
			if ( this.rootNode.x.escape != N )
				$.extend( x, { escape: this.rootNode.x.escape } );
			var n = ( this.tcell || (this.tcell = new TCell( {}, this )) ).add( x );
			n.colIndex = i;
			return n;
		},
		cellElem: function( i ) {
			var c = this.$().cells;
			if ( c.length === this.rootNode.colgrps[ 0 ].length )
				return c[ i ];
			for ( var i = 0, l = c.length, n = 0; i < l; i ++ ) {
				if ( n == i )
					return c[ i ];
				n += c[ i ].colSpan;
			}
		},
		// 高亮某个字段的关键字 /@ a -> colIndex, b -> key, c -> matchlength, d -> keycls
		highlight: function( a, b, c, d ) {
			var f = this.cellElem( a );
			if ( f ) {
				f.innerHTML = $.strHighlight( (f._innerhtml || (f._innerhtml = f.innerHTML)), b, c, d );
			}
			for ( var i = 0; i < this.length; i ++ ) {
				this[ i ].highlight( a, b, c, d );
			}
		},
		html_cells: function( i, l ) {
			var a = this.nodeIndex, b = [], u = this.rootNode, c = u.colgrps[ 0 ], d = this.x.data, e = this.type_tr, h = u.x.escape, r = this.parentNode._rowspan,
				i = i == N ? 0 : i, t, k, L = c.length - 1, l = l == N ? L : l;
			for ( ; i <= l; i ++ ) {
				if ( r && r[ a ] && r[ a ][ i ] ) {
					i += r[ a ][ i ] - 1;
					continue;
				}
				var s = t = '', f = c[ i ].x, v = d && d[ f.field ];
				if ( v != N && typeof v === _OBJ ) {
					k = i;
					if ( v.rowspan > 1 ) {
						for ( var j = 1; j < v.rowspan; j ++ )
							$.jsonChain( v.colspan || 1, r, a + j, i );
					}
					if ( v.colspan > 1 )
						i += v.colspan - 1;
					t = this.addCell( v, k );
				} else {
					if ( e && f.format ) {
						var m = _grid_format.call( this, f.format, h );
						if ( typeof m === _OBJ )
							t = this.addCell( m, i );
						else
							v = m;
					} else {
						h && (v = $.strEscape( v ));
					}
					if ( v != N && e && f.highlight ) {
						var key = f.highlight.key == N ? (this.ownerView.combo && this.ownerView.combo.getKey()) : f.highlight.key;
						key && (v = $.strHighlight( v, key, f.highlight.matchlength, f.highlight.keycls ));
					}
				}
				f.align  && (s += ' align='  + f.align);
				f.valign && (s += ' valign=' + f.valign);
				if ( t ) {
					b.push( t.html( i >= L ) );
				} else {
					v = v == N ? '' : v;
					var g = '';
					if ( !e || u.x.nobr )
						g += ' class="w-td-t f-fix"';
					if ( !e && c[ i ]._sort )
						v += c[ i ].html_sortarrow();
					if ( e && f.tip )
						g += ' title="' + $.strQuot( (d && d[ f.tip.field || f.field ]) || '' ) + '"';
					g && (v = '<div' + g + '>' + v + '</div>');
					b.push( '<td class="w-td-' + u._face + (i === L ? ' z-last' : '') + (!e ? ' w-th' + (f.sort ? ' w-th-sort' : '') : '') +
						(f.cls ? ' ' + f.cls : '') + '"' + s + (f.style ? ' style="' + f.style + '"' : '') + '>' + (v == N ? (ie7 ? '&nbsp;' : '') : v) + '</td>' );
				}
			}
			return b.join( '' );
		},
		html: function( i ) {
			var a = '', s, r = this.rootNode.x.pub, c = this.x.cls || (r && r.cls), h = this.x.height || (r && r.height),
				s = '<tr id=' + this.id + ' class="' + this.className + (this.type_tr ? ' z-' + ((i == N ? this.nodeIndex : i) % 2) : '') + (c ? ' ' + c : '') + '"';
			if ( h ) {
				ie7 && (h -= this.rootNode._pad * 2 + (this.rootNode._face === 'none' ? 0 : 1));
				a += ' style="height:' + h + 'px"';
			}
			this.x.id && (s += ' w-id="' + this.x.id + '"');
			s += a + _html_on_child( this ) + '>' + this.html_cells() + '</tr>';
			return this.length ? s + this.html_nodes() : s;
		},
		remove: function() {
			var i = this.length;
			while ( i -- ) this[ i ].remove();
			_proto.remove.call( this );
		}
	}
} ),
_td_wg = { leaf: T, toggle: T },
/* `td` tcell 的子节点。当字段是一个 widget 时会先产生一个 TD 实例，包裹这个 widget 子节点。 */
TD = define.widget( 'td', {
	Const: function( x, p ) {
		this.rootNode = p.rootNode;
		if ( x.type && x.type !== 'td' )
			x = { node: x };
		x.node && this.rootNode.x.escape != N && $.extend( x.node, { escape: this.rootNode.x.escape } );
		W.call( this, x, p );
	},
	Prototype: {
		x_type: function( t ) {
			return _td_wg[ t ] ? 'grid/' + t : t;
		},
		html: function( a ) {
			var r = this.parentNode.parentNode, g = this.rootNode, c = g.colgrps[ 0 ][ this.colIndex ], e = r.type_tr, s = '<td id=' + this.id, t = '', v;
			this.className = 'w-td-' + g._face + (a ? ' z-last' : '') + (r.type_hr ? ' w-th' + (c.x.sort ? ' w-th-sort' : '') : '');
			s +=  ' class="' + this.prop_cls() + (c.x.cls ? ' ' + c.x.cls : '') + '"';
			if ( this.x.on || this.Const.Listener )
				s += _html_on( this );
			if ( c.x.align || this.x.align )
				s += ' align='  + (this.x.align || c.x.align);
			if ( c.x.valign || this.x.valign )
				s += ' valign=' + (this.x.valign || c.x.valign);
			this.x.colspan > 1 && (s += ' colspan=' + this.x.colspan);
			this.x.rowspan > 1 && (s += ' rowspan=' + this.x.rowspan);
			c.x.style && (t += c.x.style);
			this.x.style && (t += this.x.style);
			t && (s += ' style="' + t + '"');
			this.x.id && (s += ' w-id="' + this.x.id + '"');
			s += '>' + this.html_before() + this.html_nodes();
			if ( ! this.x.node ) {
				t = this.x.text || '';
				if ( e && c.x.format ) {
					t = _grid_format.call( r, c.x.format, g.x.escape );
				} else {
					g.x.escape && (t = $.strEscape( t ));
				}
				var d = '';
				if ( ! e || g.x.nobr )
					d += ' class="w-td-t f-fix"';
				if ( ! e && c._sort )
					t += c.html_sortarrow();
				if ( e && c.x.tip ) {
					var f = r.x.data && r.x.data[ c.x.tip.field || c.x.field ];
					d += ' title="' + $.strQuot( (typeof f === _OBJ ? f.text : f) || '' ) + '"';
				}
				d && (t = '<div' + d + '>' + t + '</div>');
				s += t || (ie7 ? '&nbsp;' : '');
			}
			return s + this.html_after() + '</td>';
		}
	}
} ),
/* `tr` */
TR = define.widget( 'tr', {
	Extend: GridRow,
	Child: 'tr',
	Rooter: 'grid',
	Default: ie7 && {
		hmin: function() {
			return this.rootNode._pad * 2 + (this.rootNode._face == 'none' ? 0 : 1);
		}
	},
	Listener: {
		body: {
			click: {
				prop: T,
				block: function( e ) {	return this.isExpandRow || this.tgl },
				method: function( e ) {
					var r = this.rootNode, b = this.getBox(), s = b && b.x.sync;
					this.tgl ? this.toggle() : this.focus( r.x.focusmultiple ? (!this.isFocus()) : T, e );
					b && ! this.isEvent4Box( e ) && s === 'click' && b.click();
					this.x.src && this.toggle();
					if( r.x.combo && ! (this.x.on && this.x.on.click) ) {
						$.dialog( this ).commander.complete( this );
						$.close( this );
					}
				}
			},
			mouseover: {
				prop: T,
				method: function() { $.classAdd( this.$(), 'z-hv' ) }
			},
			mouseout: {
				prop: T,
				method: function() { $.classRemove( this.$(), 'z-hv' ) }
			},
			nodechange: function( e ) {
				if ( this.leaf ) {
					this.leaf.fixFolder();
					this.leaf.toggle( this.isOpen() );
				}
			},
			dnd_over: function( e ) {
				$.classAdd( this.$(), 'z-dnd-over' );
			},
			dnd_out: function( e ) {
				$.classRemove( this.$(), 'z-dnd-over' );
			}
		}
	},
	Prototype: {
		type_tr: T,
		x_nodes: function() {
			return this.x.rows;
		},
		focus: function( a, e ) {
			var a = a == N ? T : a, r = this.rootNode, b = this.getBox(), f;
			if ( this.x.focusable && this.$() ) {
				! r.x.focusmultiple && (f = r.getFocus()) && f !== this && f.focus( F );
				$.classAdd( this.$(), 'z-on', a );
				if ( b && b.x.sync === 'focus' ) {
					! this.isEvent4Box( e ) && this.checkBox( a );
				}
				a && this.trigger( 'focus' );
			}
		},
		isFocus: function( a ) {
			return $.classAny( this.$(), 'z-on' );
		},
		isEvent4Box: function( e ) {
			var b = this.getBox();
			return b && e && e.srcElement && e.srcElement.id == b.id + 't';
		},
		getBox: function() {
			return this.box;
		},
		// 勾选  /@ a -> T/F
		checkBox: function( a ) {
			var b = this.getBox();
			b && b.check( a );
		},
		isBoxChecked: function() {
			var b = this.getBox();
			return b && b.isChecked();
		},
		isOpen: function() {
			var g = this.tgl || this.leaf;
			return g ? g.isOpen() : this.x.open;
		},
		next: function( n ) {
			if ( this.rootNode._echo_rows ) {
				var b = this.$().offsetParent.rows[ this.$().rowIndex + ( n === F ? -1 : 1 ) ];
				return b && _widget( b );
			} else
				return this.parentNode[ this.nodeIndex + ( n === F ? -1 : 1 ) ];
		},
		prev: function() {
			return this.next( F );
		},
		// 计算所有子孙节点共有多少行
		_rowlen: function() {
			for ( var l = this.length, i = 0, j = 0; j < l; j ++ )
				this[ j ].$() && (i += this[ j ]._rowlen() + 1);
			return i;
		},
		// @a -> index|"+=index"
		move: function( a ) {
			var c = a;
			if ( typeof a === _STR && a.charAt( 1 ) === '=' )
				eval( 'c=this.nodeIndex;c' + a );
			if ( (c = this.parentNode[ c ]) && c !== this )
				c[ c.nodeIndex > this.nodeIndex ? 'after' : 'before' ]( this );
		},
		// @implement tr 子节点的特殊处理
		insertHTML: function( a, b ) {
			if ( ! this.$() )
				return;
			var s = a;
			if ( a.isWidget ) {
				s = [];
				var i = a.$().rowIndex, p = a.$().offsetParent, j = a._rowlen();
				do { s.push( p.rows[ i ] ) } while ( j -- );
			}
			if ( b === 'before' )
				Q( this.$() )[ b ]( s );
			else
				Q( b === 'prepend' ? this.$() : this.$().offsetParent.rows[ this.$().rowIndex + this._rowlen() ] ).after( s );
		},
		// @a -> T/F, b -> src
		toggle: function( a, b ) {
			if ( this.tgl || this.leaf ) {
				(this.tgl || this.leaf).toggle( a );
			} else {
				if ( this.length ) {
					a == N && (a = ! this.x.open);
					this.toggle_rows( a );
				} else if ( this.x.src ) {
					var c = this.rootNode.colgrps[ 0 ], d = {}, r;
					d[ c[ 0 ].x.field ] = { colspan: c.length, node: { type: 'view', src: this.x.src } };
					this.append( d ).isExpandRow = T;
					this.x.open = T;
				}
				if ( this.length ) {
					this.trigger( this.isOpen() ? 'expand' : 'collapse' );
				}
			}
		},
		toggle_rows: function( a ) {
			var i = 0, l = this.length;
			if ( l ) {
				for ( ; i < l; i ++ ) {
					this[ i ].$().style.display = a ? '' : 'none';
					this[ i ].toggle_rows( a ? this[ i ].isOpen() : a );
				}
				this.x.open = a;
			}
		}
	}
} ),
/* `thr`  thead 的子节点 */
THR = define.widget( 'thead/tr', {
	Extend: GridRow,
	Listener: {
		body: {
			click: {
				prop: T,
				method: function( e ) {
					var b = this.getBox();
					b && this.isEvent4Box( e ) && this.checkBox( b.isChecked() );
				}
			}
		}
	},
	Prototype: {
		type_hr: T,
		getBox: TR.prototype.getBox,
		isEvent4Box: TR.prototype.isEvent4Box,
		// a -> T/F|event
		checkBox: function( a ) {
			var b = this.getBox();
			if ( b ) {
				for ( var i = 0, c = b.isChecked(), r = this.rootNode.rows(), l = r.length; i < l; i ++ ) {
					r[ i ].checkBox( a );
				}
			}
		}
	}
} ),
// `TCell` 是 tr 的离散节点。当 tr 添加 td 时，先创建一个 tcell ，然后 tcell 添加这个 td
TCell = define.widget( 'tcell', {
	Const: function( x, p ) {
		this.rootNode = p.rootNode;
		W.call( this, x, p, -1 );
	},
	Child: 'td',
	Prototype: {
		scaleWidth: function( a ) {
			var w = 0, l = a.x.colspan || 1, r = this.rootNode, c = r.colgrps[ 0 ][ a.colIndex ], d = r._pad, e;
			while ( l -- )
				(e = r.colgrps[ 0 ][ a.colIndex + l ]) != N && (w += e.width());
			if ( isNaN( w ) )
				return N;
			if ( r._face === 'cell' && a.colIndex < r.colgrps[ 0 ].length - 1 )
				w -= 1;
			w -= c.x.wmin != N ? c.x.wmin : c.x.style ? _size_fix( N, 'padding:0 ' + d + 'px 0 ' + d + 'px;' + c.x.style ).wmin : d * 2;
			return w;
		},
		scaleHeight: function( a ) {
			return N;
			var r = this.outerHeight();
			return r == N ? N : r - this.rootNode._pad * 2 - (this.rootNode._face === 'none' ? 0 : 1);
		},
		html: $.rt_empty
	}
} ),
/* `tbody` */
TBody = define.widget( 'tbody', {
	Const: function( x, p ) {
		this.rootNode = p.rootNode;
		W.apply( this, arguments );
		this._rowspan = {};
	},
	Child: 'tr',
	Listener: {
		body: {
			nodechange: function() {
				this.rootNode.resetRowCls();
			}
		}
	},
	Prototype: {
		// @implement
		insertHTML: function( a, b ) {
			this.$() && Q( this.$() )[ b || 'append' ]( a.isWidget ? a.$() : a );
		},
		// @a -> rows json, b -> index
		insertCol: function( a, b ) {
			var r = this.rootNode, g = r.colgrps[ 0 ];
			b = b == N || ! g[ b ] ? g.length - 1 : b;
			for( var i = 0, l = a.length; i < l; i ++ ) {
				$.extendDeep( this[ i ].x, typeof a[ i ].data !== _OBJ ? { data: a[ i ] } : a[ i ] );
			}
			for( var i = 0, l = this.length; i < l; i ++ ) {
				var c = this[ i ].$().cells[ b ], d = Q( this[ i ].html_cells( b, b ) );
				c ? d.insertBefore( c ) : d.appendTo( Q( this[ i ].$() ) );
				! d.next().length && d.prev().removeClass( 'z-last' );
			}
		},
		deleteCol: function( a ) {
			for( var i = 0, l = this.length; i < l; i ++ ) {
				Q( this[ i ].$().cells[ a ] ).remove();
				Q( this[ i ] ).last().addClass( 'z-last' );
			}
		},
		x_nodes: TR.prototype.x_nodes,
		html_nodes: function() {
			for ( var i = 0, r = this.rootNode.getEchoRows(), l = r.length, s = []; i < l; i ++ )
				s.push( r[ i ].html( i ) );
			return s.join( '' );
		},
		html: function() { return '<tbody id=' + this.id + '>' + this.html_nodes() + '</tbody>' }
	}
} ),
/* `thead` */
THead = define.widget( 'thead', {
	Extend: TBody,
	Child: 'thead/tr',
	Listener: {
		body: {
			ready: function() {
				// 拖动表头调整大小
				var r = this.rootNode;
				if ( r.x.resizable ) {
					Q( 'td', this.$() ).append( '<div class=w-th-rsz></div>' );
					Q( '.w-th-rsz', this.$() ).height( this.height() ).on( 'mousedown', function( e ) {
						var c = this, d = c.parentNode, x = e.pageX,
							o = $.db( '<div style="position:absolute;width:1px;height:' + r.height() + 'px;top:' + $.bcr( c ).top + 'px;background:#aaa;"></div>' );
						$.moveup( function( e ) {
							o.style.left = e.pageX + 'px';
						}, function( e ) {
							r.colWidth( d.cellIndex, r.colWidth( d.cellIndex ) + e.pageX - x );
						}, o );
					} );
				}
				// 排序
				for ( var i = 0, c = r.colgrps[ 0 ], d = F; i < c.length; i ++ ) {
					if ( c[ i ].x.sort ) {
						Q( '.w-th-sort', this.$() ).each( function() {
							Q( this ).click( function() { r.order( c[ Col.index( this ) ].x.field ); } );
						} );
						break;
					}
				}
			}
		}
	},
	Prototype: {
		html_nodes: _proto.html_nodes
	}
} ),
/* `col` */
Col = define.widget( 'col', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		if ( x.sort ) {
			this._sort = x.sort.status;
		}
	},
	Default: { wmin: 0, width: '*' },
	Helper: {
		index: function( a ) {
			var i = a.cellIndex;
			while ( a = a.previousSibling )
				i += a.colSpan - 1;
			return i;
		}
	},
	Listener: {
		body: {
			resize: function() {
				if ( this.$() )
					this.$().style.width = Math.max( this.innerWidth() - this.width_minus(), 0 ) + 'px';
			}
		}
	},
	Prototype: {
		root: function() {
			return this.parentNode.parentNode.rootNode;
		},
		th: function() {
			return this.root().headrow().$().cells[ this.nodeIndex ];
		},
		html_sortarrow: function( a ) {
			return $.arrow( this.root().id + 'sortarrow', (a || this._sort) === 'asc' ? 't1' : 'b1' );
		},
		remove_sortarrow: function() {
			$.remove( this.root().id + 'sortarrow' );
		},
		width_minus: function() {
			var n = 0;
			if ( ie7 ) {
				var r = this.root(), d = r._pad,
					n = _size_fix( N, 'padding:0 ' + d + 'px 0 ' + d + 'px;' + (r._face === 'cell' && this.nodeIndex < this.parentNode.length - 1 ? 'border-right:1px solid #eee;' : '') + this.x.style ).wmin;
			}
			return n;
		},
		html: function() {
			return '<col id=' + this.id + ' style=width:' + (this.innerWidth() - this.width_minus()) + 'px>';
		}
	}
} ),
/* `colgroup` */
Colgroup = define.widget( 'colgroup', {
	Extend: 'horz/scale',
	Child: 'col',
	Prototype: {
		insertCol: function( a, b ) {
			 b == N || ! this[ b ] ? this.append( a ) : this[ b ].before( a );
		},
		deleteCol: function( a ) {
			this[ a ] && this[ a ].remove();
		},
		inBody: function() {
			return this.parentNode.tbody;
		},
		html: function() { return '<colgroup id=' + this.id + '>' + this.html_nodes() + '</colgroup>' }
	}
} ),
/* `table` */
Table = define.widget( 'table', {
	Const: function( x, p ) {
		this.rootNode = p.rootNode;
		W.apply( this, arguments );
		p.rootNode.colgrps.push( new Colgroup( { nodes: x.columns }, this ) );
		if ( x.thead )
			this.thead = new THead( x.thead || {}, this );
		else
			this.tbody = new TBody( x.tbody || {}, this );
	},
	Listener: {
		body: {
			resize: function() {
				if ( br.ms && this.$() ) {
					var w = this.innerWidth();
					this.$().style.width = w == N ? 'auto' : w + 'px';
				}
			}
		}
	},
	Prototype: {
		html: function() {
			var s = '<table id=' + this.id + ' class="w-grid-table w-grid-face-' + this.rootNode._face + '" cellspacing=0 cellpadding=' + this.rootNode._pad;
			if ( br.ms ) {
				var w = this.innerWidth();
				w != N && (s += ' style="width:' + w + 'px"');
			}
			s += '>';
			if ( br.ms ) {
				for ( var t = '<thead class=_ie_thead><tr>', i = 0, c = this.rootNode.colgrps[ 0 ], l = c.length; i < l; i ++ )
					t += '<td' + (c[ i ].x.style ? ' style="' + c[ i ].x.style + '"' : '') + '>'; // 如果不加这个style，兼容模式下宽度可能会失调
				s += t + '</thead>';
			}
			return s + this.html_nodes() + '</table>';
		}
	}
} ),
/* `gridlist` */
GridList = define.widget( 'grid/list', {
	Const: function( x, p ) {
		this.rootNode = p;
		W.apply( this, arguments );
		this.body = new GridBody( $.extend( { table: x.table }, x.table.tbody ), this );
	},
	Extend: 'vert',
	Listener: {
		body: {
			ready: function() {
				Scroll.Listener.body.ready.apply( this, arguments );
				var r = this.rootNode;
				if ( r.x.maxheight && ! r.innerHeight() ) {
					var h = r.x.maxheight - (r.head ? r.head.$().offsetHeight : 0);
					this.x.maxheight = h;
					this.$().style.maxHeight = h + 'px';
					this.setScroll();
				}
			},
			scroll: function() {
				Scroll.Listener.body.scroll.apply( this, arguments );
				var d = this.rootNode.head;
				if ( d ) {
					// 滚动条在list上，则临时设置overflow样式。如果把hidden写在css里，那么当滚动条在grid的上级面板时，thead会被截掉
					d.$().style.overflow = 'hidden';
					d.$().scrollLeft = this.scrollLeft();
				}
			}
		}
	},
	Prototype: {
		className: 'w-grid-list'
	}
} ),
/* `gridbody` */
GridBody = define.widget( 'grid/body', {
	Const: function( x, p ) {
		this.rootNode = (p.rootNode || p);
		W.apply( this, arguments );
		this.table = new Table( x.table, this );
	},
	Extend: 'vert',
	Prototype: {
		className: 'w-grid-tbody'
	}
} ),

/* `gridhead` */
GridHead = define.widget( 'grid/head', {
	Extend: GridBody,
	Listener: {
		body: {
			ready: function() {
				// 表头固定
				if ( this.x.table.thead.fix ) {
					var a = Scroll.get( this.rootNode ), b, f;
					a && a.addEvent( 'scroll', function() {
						var e = this.$(),
							c = b || (b = $.after( e, '<div style=height:' + e.offsetHeight + ';display:none>&nbsp;</div>' )),
							m = $.bcr( a.$( 'ovf' ) ),
							n = $.bcr( this.rootNode.$() ),
							f = n.top < m.top;
						e.style.position = f ? 'fixed' : '';
						e.style.zIndex = f ? 1 : '';
						c.style.display = f ? 'block' : 'none';
						$.classAdd( e, 'f-oh f-white f-shadow-bottom', f );
						e.scrollLeft = f ? a.scrollLeft() : 0;
					}, this );
				}
			}
		}
	},
	Prototype: {
		className: 'w-grid-thead'
	}
} ),
// 表格搜索过滤器
GridCombo = $.createClass( {
	Const: function( a ) {
		this.cab = a;
		this.xml = this.node2xml( a );
		this._keep_show = a.x.combo.keepshow;
		for ( var i = 0, c = a.x.columns; i < c.length; i ++ ) {
			if ( c[ i ].highlight ) {
				this._matchlength = c[ i ].highlight.matchlength;
				break;
			}
		}
	},
	Extend: TreeCombo,
	Prototype: {
		node2xml: function( a ) {
			for ( var i = 0, j, b = a.x.combo.field, c = [], d, t = a.tbody(), e = b.search && b.search.split( ',' ), f = e && e.length, l = t && t.length, r, s; i < l; i ++ ) {
				d = t[ i ].x.data, r = d[ b.remark ];
				s = '<d v="' + $.strEscape( d[ b.value ] ) + '" t="' + $.strEscape( d[ b.text ] ) + '" i="' + t[ i ].id + '"';
				r && (s += ' r="' + $.strEscape( r ) + '"');
				d[ b.forbid ] && (s += ' x="1"');
				if ( f ) {
					for ( j = 0; j < f; j ++ )
						s += ' s' + j + '="' + $.strEscape( d[ e[ j ] ] ) + '"';
				}
				c.push( s + '/>' );
			}
			this._sch = f;
			return $.xmlParse( '<doc>' + c.join( '' ) + '</doc>' );
		},
		first: function() {
			return this.cab.getEchoRows()[ 0 ];
		},
		filter: function( t, s ) {
			var a = this.cab;
			a.tbody() && a.setFilter( this._filter( t ) );
			return a.getEchoRows().length;
		},
		getLength: function() {
			return this.cab.getEchoRows().length;
		}
	}
} ),
/* `grid` */
Grid = define.widget( 'grid', {
	Const: function( x, p ) {
		this._pad  = x.cellpadding != N ? x.cellpadding : 5;
		this._face = x.face || 'none';
		W.apply( this, arguments );
		this.colgrps = [];
		var r = x.thead && x.thead.rows, l = r && r.length;
		if ( l ) {
			for ( var i = 0, c, h = 0; i < l; i ++ )
				h += $.number( r[ i ].height );
			if ( ! h && x.thead.height )
				h = $.number( x.thead.height );
			if ( ! h && ( c = x.pub && x.pub.height ) )
				h = l * $.number( c );
			this.head = new GridHead( $.extend( { table: { thead: x.thead, columns: x.columns } }, x.thead, { width: '*', height: h || 32 } ), this );
		}
		var y = { table: { tbody: x.tbody, columns: x.columns }, width: '*', height: '*', scroll: x.scroll, on: { scroll: 'this.parentNode.trigger(event)' } };
		// 为适应滚动条的位置，当没有head时把grid的样式转到list上。如果有head，这样转移样式可能会出问题，暂不做
		! this.head && $.jsonCut( y, x, 'wmin,hmin,cls,style' );
		this.list = new GridList( y, this );
		if ( x.hiddens )
			new Hiddens( { type: 'hiddens', nodes: x.hiddens }, this );
		if ( x.combo ) {
			this.ownerView.combo = new GridCombo( this );
		}
		x.limit && this.limit();
		if ( x.width == -1 )
			this.className += ' z-auto';
	},
	Extend: 'vert/scale',
	Child: 'tr',
	Listener: {
		body: {
			ready: function() {
				var a = this.tbody();
				if ( a ) {
					var b = a.$().rows, d = this.getFocus();
					b.length && ($.classAdd( b[ 0 ], 'z-first' ), $.classAdd( b[ b.length - 1 ], 'z-last' ));
					(d = this.getFocus()) && _scrollIntoView( d );
				}
			},
			scroll: function( e ) {
				if ( this.head && this.list )
					this.head.$().scrollLeft = this.list.$( 'ovf' ).scrollLeft;
			},
			resize: function() {
				this.$() && $.classAdd( this.$(), 'z-auto', this.innerWidth() == N );
			}
		}
	},
	Prototype: {
		NODE_ROOT: T,
		className: 'w-grid',
		headrow: function( a ) { return this.head && this.head.table.thead[ a == N ? 0 : a ] },
		tbody: function() { return this.list && this.list.body && this.list.body.table.tbody },
		// 获取符合条件的某一行  /@ a -> condition?
		row: function( a ) {
			return this.rows( a == N ? 0 : a, T )[ 0 ];
		},
		// 获取符合条件的所有行  /@ a -> condition?, b -> one?
		rows: function( a, b ) {
			var d = this.tbody(), r = [];
			if ( d ) {
				if ( a == N ) {
					r = _slice.call( d );
				} else if ( typeof a === _NUM ) {
					if ( a < 0 ) a = d.length + a;
					d[ a ] && r.push( d[ a ] );
				} else if ( a.isWidget ) {
					var g = a.closest( 'tr' );
					g && r.push( g );
				} else if ( a.nodeType === 1 ) {
					var g = _widget( a );
					g && (g = g.closest( 'tr' )) && r.push( g );
				} else
					try {
						var r = this._rows( a, b, d );
					} catch( ex ) {
						r.push( ex );
					}
			}
			return r;
		},
		_rows: function( a, b, d ) {
			R: for ( var i = 0, k, l = d.length, c, r = []; i < l; i ++ ) {
				for ( k in a ) if ( d[ i ].x.data[ k ] !== a[ k ] ) {
					d[ i ].length && r.push.apply( r, this._rows( a, b, d[ i ] ) );
					continue R;
				}
				if ( b ) { throw d[ i ]; }
				r.push( d[ i ] );
			}
			return r;
		},
		// 获取显示中的tbody的所有行
		getEchoRows: function() {
			return this._echo_rows || this.tbody() || [];
		},
		// 获取符合条件的所有行的 data json  /@ a -> condition?, b -> one?
		rowsData: function( a, b ) {
			for ( var i = 0, d = this.rows( a, b ), l = d.length, r = []; i < l; i ++ )
				r.push( d[ i ].getData() );
			return r;
		},
		// a -> data, b -> index
		_addRow: function( a, b ) {
			if ( this.list ) {
				var p = this.tbody();
				p._rowspan = {};
				b == N && (b = p.length);
				p[ b ] ? p[ b ].before( a ) : p.append( a );
			} else {
				this.list = new GridList( { table: { tbody: { rows : $.arrIs( a ) ? a : [ a ] }, columns: this.x.columns }, width: '*', height: '*', scroll: this.x.scroll, on: { scroll: 'this.parentNode.trigger(event)' } }, this );
				this.list.render();
			}
		},
		prepend: function( a ) {
			this._addRow( a, 0 );
		},
		append: function( a ) {
			this._addRow( a );
		},
		draggable: function( a ) {
			for ( var i = 0, b = this.getEchoRows(), l = b.length; i < l; i ++ ) {
				$.draggable( b[ i ], a );
			}
			return this;
		},
		droppable: function( a ) {
			for ( var i = 0, b = this.getEchoRows(), l = b.length; i < l; i ++ ) {
				$.droppable( b[ i ], a );
			}
			return this;
		},
		// @a -> elem
		getCellAxis: function( a ) {
			var b = this.tbody(), c = Q( a ).closest( 'td' ), r = c[ 0 ] && _widget( c[ 0 ] ).closest( 'tr' );
			if ( b && r && r.$().parentNode === b.$() ) {
				return { rowIndex: r.$().rowIndex - 1, cellIndex: c[ 0 ].cellIndex };
			}
		},
		// @a -> col index, b -> width?
		colWidth: function( a, b ) {
			var g = this.colgrps, i = g.length, v = g[ 0 ][ a ].x.width;
			if ( b == N )
				return i && g[ 0 ][ a ] && g[ 0 ][ a ].width();
			while ( i -- ) {
				g[ i ][ a ] && g[ i ][ a ].width( b );
			}
			_w_rsz_all.call( this );
			for ( var i = 0, w = 0, v, l = g[ 0 ].length; i < l; i ++ ) {
				w += g[ 0 ][ i ].width();
			}
			i = g.length;
			while ( i -- ) {
				! isNaN( w ) && (g[ i ].parentNode.$().style.width = w + 'px');
				g[ i ][ a ].x.width = v;
			}
		},
		// a -> data, b -> data|index
		insertRow: function( a, b ) {
			if ( b == N )
				return this.append( a );
			(b = this.row( b )) && b.before( a );
			this.resetRowCls();
		},
		// a -> data, b -> data|index
		updateRow: function( a, b ) {
			b != N && (b = this.row( b )) && b.replace( a );
			this.resetRowCls();
		},
		// a -> data|index
		deleteRow: function( a ) {
			if ( a != N && (a = this.rows( a )) ) {
				var i = a.length;
				while ( i -- ) a[ i ].remove();
			}
			this.resetRowCls();
		},
		deleteAllRows: function() {
			var a = this.rows(), i = a.length;
			while ( i -- ) a[ i ].remove();
		},
		//@public 移动行  /@ a -> data|index, b -> index
		moveRow: function( a, b ) {
			if ( a != N && (a = this.rows( a )) ) {
				var i = a.length;
				if ( typeof a === _STR && a.charAt( 0 ) === '-' ) {
					for ( var j = 0; j < i; j ++ )
						a[ j ].move( b );
				} else
					while ( i -- ) a[ i ].move( b );
			}
		},
		//@public 选中某行的checkbox /@a -> data|index, b -> T/F
		checkRow: function( a, b ) {
			if ( a != N && (a = this.rows( a )) ) {
				var i = a.length;
				while ( i -- ) a[ i ].checkBox( b );
			}
		},
		checkAllRows: function( a, b ) {
			var b = b || this.rows(), i = b.length;
			while ( i -- ) {
				b[ i ].checkBox( a );
				if ( b[ i ].length )
					arguments.callee.call( this, a, b[ i ] );
			}
		},
		// a -> data|index
		focusRow: function( a ) {
			(a = this.row( a )) && (a.focus(), _scrollIntoView( a ));
		},
		_col_parse: function( a ) {
			if ( a != N && isNaN( a ) ) {
				for ( var j = 0, g = this.colgrps[ 0 ]; j < g.length; j ++ )
					if ( g[ j ].x.field === a ) { a = j; break; }
			}
			return a;
		},
		//@public 插入一列 / @a -> 一列的数据grid json, b -> colIndex|colField
		insertColumn: function( a, b ) {
			b = this._col_parse( b );
			for ( var j = 0; j < this.colgrps.length; j ++ )
				this.colgrps[ j ].insertCol( a.columns[ 0 ], b );
			this.head && this.head.table.thead.insertCol( a.thead.rows, b );
			this.list && this.list.table.tbody.insertCol( a.rows, b );
			this.x.scroll && this.list.checkScroll();
		},
		//@public 删除一列 / @a -> colIndex|colField
		deleteColumn: function( a ) {
			a = this._col_parse( a );
			for ( var j = 0; j < this.colgrps.length; j ++ )
				this.colgrps[ j ].deleteCol( a );
			this.head && this.head.table.thead.deleteCol( a );
			this.list && this.list.table.tbody.deleteCol( a );
			this.x.scroll && this.list.checkScroll();
		},
		//@public 更新一列 / @a -> 一列的数据grid json, b -> colIndex|colField
		updateColumn: function( a, b ) {
			b = this._col_parse( b );
			if ( this.colgrps[ 0 ][ b ] ) {
				this.deleteColumn( b );
				this.insertColumn( a, b );
			}
		},
		// 获取焦点行
		getFocus: function() {
			return this.getFocusAll()[ 0 ];
		},
		// 获取所有焦点行
		getFocusAll: function() {
			var b = this.tbody(), r = [];
			b && Q( '>.z-on', b.$() ).each( function() { r.push( _widget( this ) ) } );
			return r;
		},
		// 获取所有选中行
		getCheckedAll: function() {
			for ( var i = 0, b = this.rows(), l = b.length, r = []; i < l; i ++ )
				b[ i ].isBoxChecked() && r.push( b[ i ] );
			return r;
		},
		resetRowCls: function() {
			if ( this.tbody() ) {
				for ( var i = 0, r = this.getEchoRows(), l = r.length; i < l; i ++ ) {
					$.classRemove( r[ i ].$(), 'z-0 z-1 z-first z-last' );
					$.classAdd( r[ i ].$(), 'z-' + (i % 2) + ( i === 0 ? ' z-first' : i === l - 1 ? ' z-last' : '' ) );
					r[ i ].rownum && r[ i ].rownum.reset();
				}
				if ( (r = this.headrow()) && Q( '.w-triplebox', r.$() ).length )
					(r = $.get( '.w-triplebox', this.tbody().$() )) && (r = $.widget( r )) && r.relate();
			}
		},
		// 高亮某个字段的关键字 /@ a -> field name, b -> key, c -> matchlength, d -> keycls
		highlight: function( a, b, c, d ) {
			for ( var k = 0, e = this.colgrps[ 0 ], l = e.length; k < l; k ++ ) {
				if ( e[ k ].x.field == a ) break;
			}
			if ( k < l ) {
				for ( var i = 0, r = this.getEchoRows(), l = r.length; i < l; i ++ ) {
					r[ i ].highlight( k, b, c, d );
				}
			}
		},
		// @a -> page num
		page: function( a ) {
			if ( a != N ) {
				this.x.page = a;
				this.limit();
				this.$() && this.render();
			}
			return { currentpage: this.x.page, sumpage: Math.ceil((this._filter_rows || this.tbody() || []).length / this.x.limit) };
		},
		limit: function() {
			if ( this.x.limit && this.tbody() ) {
				var g = this.x.page || 1, i = (g - 1) * this.x.limit, j = g * this.x.limit;
				this._echo_rows = _slice.call( this._filter_rows || this.tbody(), i, j );
			}
		},
		// @f -> filter rows(经过筛选的行)
		setFilter: function( f ) {
			this._filter_rows = this._echo_rows = f;
			this.x.page = 1;
			this.x.limit && this.limit();
		},
		//排序 /@a -> field, b -> [asc,desc]
		order: function( a, b ) {
			for ( var k = 0, e = this.colgrps[ 0 ], l = e.length; k < l; k ++ ) {
				if ( e[ k ].x.field == a ) break;
			}
			if ( k < l ) {
				var c = e[ k ].x.sort, d = c.field || a, r = $.arrMake( this.getEchoRows() );
				if ( b == N ) {
					b = e[ k ]._sort === 'desc' ? 'asc' : 'desc';
					e[ k ]._sort = b;
				}
				if ( c.src ) {
					var s = c.src;
					if ( s.indexOf( 'javascript:' ) == 0 )
						s = Function( s ).call( r, o );
					if ( s && typeof s === _STR && ! e[ k ]._sorting ) {
						e[ k ]._sorting = T;
						this.cmd( { type: 'ajax', src : $.urlFormat( s, [ b ] ), complete: function() { e[ k ]._sorting = F; Q( '.f-arw', o ).show(); Q( '.f-i-loading', o ).remove(); } } );
					}
				} else {
					$.remove( this.id + 'sortarrow' );
					Q( '.w-td-t', e[ k ].th() ).append( e[ k ].html_sortarrow() );
					r.sort( function( m, n ) {
						var e = m.x.data[ d ], f = n.x.data[ d ];
						if ( c.isnumber ) { e = _number( e ); f = _number( f ); }
						return b === 'asc' ? (e < f ? -1 : e == f ? 0 : 1) : (e < f ? 1 : e == f ? 0 : -1);
					} );
					for ( var i = 0, l = r.length, o = this.tbody().$().rows; i < l; i ++ ) {
						if ( o[ i ].id != r[ i ].id ) {
							r[ i ].parentNode.addNode( r[ i ], i );
							Q( o[ i ] ).before( r[ i ].$() );
						}
					}
					this.resetRowCls();
				}
			}
		},
		// @k -> keycode
		keyup: function( k ) {
			if ( ! this.tbody() )
				return;
			var r = this.getEchoRows(), d = k === 40, a;
			if ( d || k === 38 ) { // key down/up
				var a = this.getFocus(), b = a ? ( d ? a.next() : a.prev() ) : r[ d ? 0 : r.length - 1 ];
				b ? b.focus() : (a && a.focus( F ));
			} else if ( k === 13 ) {
				(a = this.getFocus()) && a.click();
			}
		},
		isScrollBottom: function() {
			return this.list.isScrollBottom();
		}
	}
});

// 附件上传模块
require( './upload/upload' );

// 本文件的导出
module.exports = W;