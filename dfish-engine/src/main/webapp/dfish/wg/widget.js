/*!
 * widget.js v5
 * (c) 2017-2020 Mingyuan Chen
 * Released under the Apache License.
 */
// firefox调用arguments.callee会报错，因此目前还无法使用'use strict'
//'use strict';
var U, N = null, F = false, T = true, A = [], O = {}, _STR = 'string', _FUN = 'function', _OBJ = 'object', _NUM = 'number', _BOL = 'boolean',
$   = require( 'dfish' ),
Q   = require( 'jquery' ),
Loc = require( 'loc' ),
br  = $.br,
ie  = br.ie,
ie7 = br.ie7,
mbi = br.mobile,
cfg = $.x,
eve = $.abbr + '.e(this)',
evw = $.abbr + '.w(this)',
ev_down = mbi ? 'ontouchStart=' : 'onmousedown=',
plus = window.plus,
_dfopt	= cfg.defaultOptions || {},
_number = $.number,
_slice  = Array.prototype.slice,
_putin  = { append: T, prepend: T, undefined: T },

// 给第三方插件使用的全局变量
globalWindow = define( 'global/window', window ),
globalDocument = define( 'global/document', document ),

// widget实例缓存
_all = $.all, _globals = $.globals, _viewCache = {}, _formatCache = {},
// 引入dfish后会产生一个不可见的原始view，所有widget从这个view起始。调用 VM() 会返回这个view
_docView,
abbr = function( a ) { return $.abbr + '.all["' + a.id + '"]' },
// 存放有样式的 defaultOptions
_dfcls = (function() {
	var r = {};
	for ( var k in _dfopt ) {
		if ( k.indexOf( '.' ) > 0 ) {
			var a = $.strFrom( k, '.' ).replace( /\./g, ' ' ), b = $.strTo( k, '.' );
			(r[ b ] || (r[ b ] = {}))[ a ] = _dfopt[ k ];
		}
	}
	return r;
})(),
// 获取模板  /@a -> template id, b -> clone?
_getTemplate = function( a, b ) {
	var t = typeof a === _OBJ ? a : $.require( (cfg.templateDir || '') + a );
	return b && t ? $.jsonClone( t ) : t;
},
// 获取预装模板  /@a -> template id, b -> clone?
_getPreload = function( a, b ) {
	var t = typeof a === _OBJ ? a : $.require( (cfg.preloadDir || '') + a );
	return b && t ? $.jsonClone( t ) : t;
},
// 事件白名单
_white_events = $.white_events,
_event_zhover = (mbi ? '' : ' onmouseover=$.zover(this) onmouseout=$.zout(this)'),
// 把mouseOver和mouseOut修为mouseenter和mouseleave的效果
_event_enter = {
	mouseOver: function( a, e ) { return ! a.contains( e.fromElement ) },
	mouseOut:  function( a, e ) { return ! a.contains( e.toElement ) }
},
_event_stop = {
	contextMenu: function( e ) { !(cfg.debug && e.ctrlKey) && $.stop( e ) }
},
// 这些事件先触发用户事件，然后触发系统事件。用户事件可以 return false 来禁止系统事件
_event_priority = { click: T, dblClick: T, keyDown: T, keyUp: T, close: T, valid: T },
_event_hump = {};
Q.each( _white_events.all, function( k, v ) {
	_event_hump[ k.toLowerCase() ] = k;
} );
// @a -> htmlElement: 返回html元素对象所在的widget
// @a -> JSON: 参数为符合widget配置项的json对象，则创建这个widget
var
_widget = function( a ) {
	var b = typeof a === _STR ? a : a.id;
	if ( b && (b = _getWidgetById( b )) )
		return b;
	if ( a.nodeType ) { // htmlElement
		do {
			if ( a.id && (b = _getWidgetById( a.id )) )
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
// 触发事件的入口，在html标签上显示为 onEvent="$.e(this)"  /@ a -> element
_widgetEvent = function( a ) {
	var e = window.event || arguments.callee.caller.arguments[ 0 ], // _widgetEvent.caller: 解决firefox下没有window.event的问题
		t = _event_hump[ e.type ] || e.type;
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
			return _view.call( this, Function( a ).call( this ) );
		return a.charAt( 0 ) === '/' ? _viewCache[ a ] : a.charAt( 0 ) === '#' ? _globals[ a ] : (a = $.dialog( a )) && a.getContentView();
	} else {
		return _vmByElem( a );
	}
	return _docView;
},
_vmByElem = function ( o ) {
	var p = _widget( o );
	if( p ) {
		while ( p && (! p.ownerView && ! p.type_view) )
			p = p.parentNode;
		return p ? (p.type_view ? p : p.ownerView) : ((p = _widget( o ).$()) && _vmByElem( p.parentNode ));
	}
	return _docView;
},
_url_format = function( a ) {
	return a == N ? '' : (a = '' + a).indexOf( 'javascript:' ) === 0 ? this.formatJS( a ) : this.formatStr( a, N, T );
},
// 解析 html 中的 <d:wg> 标签 /@a -> html
_parseHTML = function( a ) {
	if ( (a = a + '').indexOf( '<d:wg>' ) > -1 ) {
		var self = this;
		return a.replace( /<d:wg>([\s\S]+?)<\/d:wg>/gi, function( $0, $1 ) {
			return self.add( Function( $1.indexOf( 'javascript:' ) === 0 ? $1 : 'return(' + $1 + ')' ).call( self ), -1 ).html();
		} );
	}
	return a;
},
_repaintSelfWithBox = function() {
	var b = this.box && this.box.isChecked();
	Q( this.$() ).replaceWith( this.html_self() );
	this.box && this.box.check( b );
},
//@ a ->context, b -> Extend, c -> event
_superTrigger = function( a, b, c ) {
	var t = c.runType || c.type || c,
		m = b.Listener.body[ _event_hump[ t ] || t ];
	m && (m.method || m).call( a, c );
},
// 生成html事件属性 / @a -> context, s -> 指定要有的事件
_html_on = function( s ) {
	var s = s || '', n, h = this.Const.Listener, r = _white_events[ h && h.range ] || _white_events.all;
	if ( this.x.on ) {
		for ( var i in this.x.on )
			s = _s_html_on.call( this, s, h && h.body, i, r );
	}
	if ( h && (h = h.body) ) {
		for ( var i in h ) {
			if ( h[ i ] && (typeof h[ i ].occupy === _FUN ? h[ i ].occupy.call( this ) : h[ i ].occupy) )
				s = _s_html_on.call( this, s, h, i, r );
		}
	}
	return s;
},
_s_html_on = function( s, h, i, r ) {
	var p = h && h[ i ] && h[ i ].proxy;
	if ( p ) {
		for ( var k = p.split( ' ' ), j = 0; j < k.length; j ++ ) {
			$.jsonChain( T, this, 'proxyHooks', k[ j ], i );
			s.indexOf( 'on' + k[ j ] + '=' ) < 0 && r[ k[ j ] ] && (s += ' on' + k[ j ] + '=' + eve);
		}
	} else
		s.indexOf( 'on' + i + '=' ) < 0 && r[ i ] && (s += ' on' + i + '=' + eve);
	return s;
},
// 取得一个表单的值  /@ a -> input el, b -> json mode?
_f_val = function( a, b, r ) {
	var d = a.name, v = a.value;
	if ( ! d )
		return N;
	switch ( a.type ) {
		case 'text':
			var t = a.getAttribute( 'w-valuetype' );
			if ( t === 'number' )
				v = v.replace( /[^\d\.-]/g, '' );
		break;
		case 'checkbox':
			if ( ! a.value  || (! a.checked && ! (a.indeterminate && a.getAttribute( 'w-partialsubmit' ))) )
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
		break;
		case 'datetime-local':
			v = v.replace( 'T', ' ' );
		break;
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
// @a -> widget|el, b -> top?, c -> left?, d -> frame focus?
_scrollIntoView = function( a, b, c, d ) {
	if ( a && (!a.isWidget || a.$()) ) {
		d && (Frame.focus( a ), Toggle.focus( a ));
		var s = Scroll.get( a );
		s && s.scrollTo( a, b || 'auto', c || 'auto' );
	}
},
/*	beforesend 发送请求之前调用
 *	error      请求出错时调用。传入XMLHttpRequest对象，描述错误类型的字符串以及一个异常对象（如果有的话）
 *	success    请求之后调用。传入返回后的数据，以及包含成功代码的字符串。
 *	complete   请求完成之后调用这个函数，无论成功或失败。传入XMLHttpRequest对象，以及一个包含成功或错误代码的字符串。
 */// @x -> cmd object, a -> url args, t -> post data
_ajaxCmd = function( x, a, t ) {
	var u = x.src, d;
	if ( ! u )
		return;
	if ( u.indexOf( 'javascript:' ) === 0 )
		u = this.formatJS( u );
	u = this.formatStr( u, a, T );
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
		d = this.exec( typeof x.loading === _OBJ ? $.extend( { type: 'Loading' }, x.loading ) : { type: 'Loading', text: x.loading === T ? N : x.loading } );
	this.trigger( 'lock' );
	// @fixme: view base
	$.ajaxJSON( { src: u, context: this, sync: x.sync, data: t || x.data, headers: x.headers, dataType: x.dataType, filter: x.filter != N ? x.filter : cfg.ajaxFilter, error: x.error, beforesend: x.beforesend, 
		success: function( r, j ) {
			d && (d.close(), d = N);
			if ( ! this._disposed ) {
				var v = r;
				r && x.template && (v = _compileTemplate( this, r, x.template ));
				if ( x.success )
					$.fnapply( x.success, this, '$response,$ajax', [ v, j ] );
				else if ( v ) {
					a && (v.args = a);
					this.exec( v, N, x.transfer, r );
				}
			}
		}, complete: function( r, j ) {
			d && d.close();
			if ( ! this._disposed && x.complete ) {
				var v = r;
				r && x.template && (v = _compileTemplate( this, r, x.template ));
				$.fnapply( x.complete, this, '$response,$ajax', [ v, j ] );
			}
			if ( ! this._disposed )
				this.trigger( 'unlock' );
		}
	} );
},
_cmd = function( x, d ) {
	var i = 0, e = _view.call( this, x.path ), f = x.target ? e.find( x.target ) : x.path ? e : this, l = x.nodes && x.nodes.length;
	for ( ; i < l; i ++ )
		f && f.exec( x.nodes[ i ], N, N, d );
},
_cmdWidgets = {},
_cmdHooks = {
	'Cmd': function( x, a, b ) {
		if ( x.delay != N ) {
			var self = this;
			setTimeout( function() { _cmd.call( self, x ) }, x.delay );
		} else
			_cmd.call( this, x, b );
	},
	'JS': function( x, a ) {
		var c;
		if ( a ) {
			c = {};
			for ( var i = 0; i < a.length; i ++ )
				c[ '$' + i ] = a[ i ];
		}
		return x.text && this.formatJS( x.text, c );
	},
	'Ajax': function( x, a ) {
		x.download ? $.download( x.src, x.data ) : _ajaxCmd.call( this, x, a );
	},
	'Submit': function( x, a ) {
		var v = _view( this ), d = x.validate || O, f = v.getFormList( d.range || x.range ), g = d.range && d.range != x.range ? v.getFormList( x.range ) : f;
		if ( v.valid( d.group, f, d.effect ) ) {
			var s = v.getPostData( g, !! x.download );
			x.download ? $.download( x.src, s ) : _ajaxCmd.call( this, x, a, s );
		}
	}
};
$.each( 'Menu Dialog Tip Loading Alert Confirm'.split(' '), function( v, i ) {
	_cmdWidgets[ v ] = T;
	_cmdHooks[ v ] = function( x, a, b ) {
		a && (x.args = a);
		b && (this._srcdata = b);
		x.title && (x.title = $.strFormat( x.title, a ));
		typeof x.src === _STR && (x.src = this.formatStr( x.src, a, T ));
		if ( x.hide )
			require( v ).hide( this );
		else
			return new (require( v ))( x, this ).show();
	}
} );
$.each( 'Before After Prepend Append Replace Remove'.split(' '), function( v, i ) {
	_cmdHooks[ v ] = function( x, a, b ) {
		var d = x.target || (i > 3 && x.node && x.node.id), e;
		if ( d ) {
			if ( x.section === 'cmd' ) {
				x.node && ((_view( this ).x.commands || (_view( this ).x.commands = {}))[ d ] = i === 5 ? N : x.node);
			} else if ( e = _view( this ).find( d ) ) {
				e[ v.toLowerCase() ]( x.node || x.nodes );
			}
		}
	}
} );
var
/* `node` */
Node = $.createClass( {
	Const: function() {},
	Extend: $.Event,
	Prototype: {
		length: 0,
		// 添加一个子节点  /@ a -> node, b -> nodeIndex [ number: 节点序号; /-1: 离散节点
		addNode: function( a, b ) {
			// 已有父节点时，先从父节点中解除
			if ( a.nodeIndex != N )
				Node.prototype.removeNode.call( a, T );
			var l = this.length,
				n = b == N ? l : Math.max( -1, Math.min( l, b ) );
			a.parentNode = this;
			if ( n != N ) {
				a.nodeIndex = n;
				if ( n === -1 ) {
					(this.discNodes || (this.discNodes = {}))[ $.uid( a ) ] = a;
				} else {
					// 重新整理并编号
					for ( var i = l; i > n; i -- )
						(this[ i ] = this[ i - 1 ]).nodeIndex = i;
					this.length ++;
					this[ n ] = a;
				}
				// 给有root_type的widget设置rootNode。 rootNode的用途是从根节点获取pub参数
				var r = a.ROOT_TYPE && (this.rootNode || ($.idsAny( a.ROOT_TYPE, this.type ) && this));
				if ( r )
					a.rootNode = r;
			}
			return a;
		},
		// a -> 设为true, 只解除当前节点的关系
		removeNode: function( a ) {
			if ( a !== T ) {
				for ( var i = this.length - 1, r = Node.prototype.removeNode; i >= 0; i -- )
					r.call( this[ i ] );
			}
			var p = this.parentNode;
			if ( p ) {
				if ( this.nodeIndex > -1 ) {
					for ( var i = this.nodeIndex, l = p.length - 1; i < l; i ++ )
						(p[ i ] = p[ i + 1 ]).nodeIndex = i;
					p.length --;
					delete p[ i ];
				} else if ( p.discNodes )
					delete p.discNodes[ this.id ];
			}
			delete this.nodeIndex; delete this.parentNode; delete this.rootNode;
		}
	}
} ),
Error = $.createClass( {
	Const: function( x ) { this.x = x }
} ),

_compileTemplate = function( g, d, s ) {
	var t = new Template( s || g.x.template, d, g ), r = t.compile( t.template );
	!r.type && (r.type = g.type);
	return r;
},
TemplateWidget = $.createClass( {
	Const: function( x, t ) {
		this.x = x;
		this.id = t.wg.id + '__template';
		this.parentNode = t.wg;
	},
	Prototype: {
		isWidget: T,
		data: function() {
			return _proto.data.apply( this, arguments );
		},
		closest: function( a ) {
			return this.parentNode.closest( a );
		}
	}
} ),
_regAt = function( x, k, v ) {
	(_atCache[ x.at ] || (new TemplateMark( x ))).addProp( k, v );
	return x.at;
},
_markWorkLoop = function( a ) {
	for ( var i = 0, l = a.length; i < l; i ++ )
		_markWork( a[ i ] );
},
_atCache = {},
TemplateMark = $.createClass( {
	Const: function( x ) {
		_atCache[ x.at = this.id = $.uid() ] = this;
		this.x = x;
		this.props = {};
	},
	Prototype: {
		addProp: function( k, v ) {
			k && (this.props[ k.replace( '@', '' ) ] = v);
		},
		work: function( x ) {
			if ( this.props[ 'w-for' ] ) {
				var i = this.nodes[ 0 ].nodeIndex;
			}
		}
	}
} ),
_mergeAtPropHooks = {
	'cls': function( a, b ) {
		return a + ' ' + b;
	},
	'style': function( a, b ) {
		return (a + ';' + b).replace( /;{2,}/g, ';' );
	}
},
/* `Template` */
Template = $.createClass( {
	// @ t -> template, d -> data, g -> widget, r -> target
	Const: function( t, d, g ) {
		this.wg   = g;
		this.template = _getTemplate( t );
		this.data = $.extend( {}, d );
		if ( g.x.data )
			$.extend( this.data, g.x.data );
		if ( g.x.args ) {
			for ( var i = 0, a = g.x.args; i < a.length; i ++ )
				this.data[ i ] = a[ i ];
		}
	},
	Extend: Node,
	Prototype: {
		// @a -> text, g -> templateWidget, y -> args
		format: function( a, g, y ) {
			return a ? _proto.formatJS.call( g, 'return ' + a, y, this.data ) : N;
		},
		switcher: function( s, r, g, y ) {
			if ( typeof s === _STR ) {
				r = this.format( s, g, y );
			} else {
				var b = this.compile( s, y );
				b && $.merge( r, b );
			}
			return r;
		},
		// @y -> { key: value }, z -> x.parentObject
		compile: function( x, y ) {
			if ( ! x || typeof x !== _OBJ )
				return x;
			if ( $.isArray( x ) ) {
				for ( var i = 0, l = x.length, r = []; i < l; i ++ )
					r.push( this.compile( x[ i ], y ) );
				return r;
			}
			var r = {}, b, f = {}, g = x && (new TemplateWidget( x, this ));
			if ( (b = x[ '@w-include' ]) ) {
				var d = _getTemplate( b, T );
				if ( d ) {
					d = $.extend( {}, d );
					for ( var k in x )
						if ( k.indexOf( '@w-' ) < 0 ) d[ k ] = x[ k ];
				}
				return d && this.compile( d, y );
			}
			for ( var k in x ) {
				var b = x[ k ];
				if ( k.charAt( 0 ) === '@' ) {  //以@开头的是JS表达式
					if ( k.indexOf( '@w-' ) === 0 ) {
						if ( k.indexOf( '@w-if' ) === 0 || k.indexOf( '@w-elseif' ) === 0 ) {
							if ( typeof b === _OBJ ) {
								$.jsonArray( [ $.strRange( k, '(', ')' ), b ], f, '_switch' );
							} else if ( ! this.format( b, g, y ) ) {
								r = N;
								break;
							}
						} else if ( k === '@w-else' ) {
							f._switchdefault = b;
						}
					} else {
						var d = typeof b === _STR ? this.format( b, g, y ) : this.compile( b, y );
						d != N && (r[ k.substr( 1 ) ] = d);
						//r.at = _regAt( x, k, d );
					}
				} else if ( $.isArray( b ) ) {
					for ( var i = 0, c = [], d, m, l = b.length, IF, EIF; i < l; i ++ ) if ( m = b[ i ] ) {
						if ( m[ '@w-for' ] ) {
							//_regAt( m );
							var e = m[ '@w-for' ].split( / in / ),
								v = e[ 0 ].replace( /[^\$\w,]/g, '' ).split( ',' ), // $item,$index
								h = this.format( e[ 1 ], g, y ); // array
							if ( h && typeof h === _OBJ ) {
								m = $.extend( {}, m );
								delete m[ '@w-for' ];
								if ( $.isArray( h ) ) {
									for ( var j = 0, n; j < h.length; j ++ ) {
										n = {};
										n[ v[ 0 ] ] = h[ j ];
										v[ 1 ] && (n[ v[ 1 ] ] = j);
										y && $.extend( n, y );
										(d = this.compile( m, n )) && c.push( d );
									}
								} else {
									var n, j;
									for ( j in h ) {
										n = {};
										n[ v[ 0 ] ] = h[ j ];
										v[ 1 ] && (n[ v[ 1 ] ] = j);
										y && $.extend( n, y );
										(d = this.compile( m, n )) && c.push( d );
									}
								}
							}
						} else {
							if ( m['@w-if'] !== U ) {
								EIF = F;
								if ( ! (IF = this.format( m[ '@w-if' ], g, y )) ) continue;
							} else if ( m['@w-elseif'] !== U ) {
								if ( IF || EIF || ! (EIF = this.format( m[ '@w-elseif' ], g, y )) ) continue;
							} else if ( d = (typeof m['@w-else'] === _STR) )
								if ( IF || EIF ) continue;
							if ( d ) {
								m = $.extend( {}, m );
								delete m[ '@w-if' ]; delete m[ '@w-elseif' ]; delete m[ '@w-else' ];
							}
							(d = this.compile( m, y )) && c.push( d );
						}
					}
					r[ k ] = c;
				} else if ( typeof b === _OBJ ) {
					r[ k ] = this.compile( b, y );
				} else {
					r[ k ] = b;
				}
			}
			if ( f._switch ) {
				for ( var i = 0; i < f._switch.length; i ++ )
					if ( this.format( f._switch[ i ][ 0 ], g, y ) )
						return this.switcher( f._switch[ i ][ 1 ], r, g, y );
			}
			if ( f._switchdefault )
				return this.switcher( f._switchdefault, r, g, y );
			return r;
		}		
	}
} ),
_tpl_str = {},
_tpl_ids = {},
_tpl_parse = function( a, b ) {
	var r = {}, t = 'PreloadBody';
	if ( $.isArray( a ) ) {
		for ( var i = 0, l = a.length, c; i < l; i ++ ) {
			$.merge( r, _tpl_parse( a[ i ], b ) );
		}
	} else {
		if ( a.id ) {
			r[ a.id ] = a;
			if ( b && b[ a.id ] )
				return r;
		}
		if ( ! b && a.type === t ) {
			r[ t ] = a;
		}
		for ( var k in a ) {
			if ( a[ k ] != N && typeof a[ k ] === _OBJ )
				$.merge( r, _tpl_parse( a[ k ], b ) );
		}
	}
	return r;
},
_tpl_view = function( a ) {
	if ( a.type === 'View' )
		return a;
	if ( a.node )
		return _tpl_view( a.node );
	if ( a.nodes ) {
		for ( var i = 0, l = a.nodes.length, v; i < l; i ++ )
			if ( v = _tpl_view( a.nodes[ i ] ) ) return v;
	}
},
//
_compilePreload = function( a, x ) {
	var b = typeof a === _OBJ, p = b ? a : _getPreload( a ), y = x, x = x.type === 'Dialog' ? x.node : x, n = x, v;
	if ( p ) {
		if ( x.type === 'View' && x.node ) {
			v = _tpl_view( p );
			v && (n = x.node);
		}
		if ( ! n )
			return y;
		var t = 'PreloadBody',
			s = b ? $.jsonString( p ) : (_tpl_str[ a ] || (_tpl_str[ a ] = $.jsonString( p ))),
			c = b ? _tpl_parse( p ) : (_tpl_ids[ a ] || (_tpl_ids[ a ] = _tpl_parse( p ))),
			d = _tpl_parse( n, c );
		for ( var k in c ) {
			if ( d[ k ] )
				s = s.replace( $.jsonString( c[ k ] ), $.jsonString( d[ k ] ) );
		}
		// 如果之前的替换处理后， PreloadBody 没被替换掉，那么它将替换整个node
		if ( c[ t ] ) {
			s = s.replace( $.jsonString( c[ t ] ), function() { return $.jsonString( $.extend( {}, n, c[ t ] ) ) } );
		}
		var r = $.jsonParse( s );
		if ( v && (v = _tpl_view( r )) ) {
			x.commands && $.extend( v.commands || (v.commands = {}), x.commands );
			x.on && $.extend( v.on || (v.on = {}), x.on );
		}
		if ( r.type === y.type ) {
			for ( var k in y )
				if ( k !== 'node' && k !== 'nodes' ) r[ k ] = y[ k ];
		}
		return r;
	} else
		$.winbox( Loc.ps( Loc.preload_error, a ) );
},
_setView = function( a ) {
	if ( a && !this.type_view ) {
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
	if ( this.x.gid ) _globals[ this.x.gid ] = this;
	if ( this.x.name ) $.jsonArray( this, a.names, this.x.name );
},
_regWidget = function( x, p, n ) {
	p && p.addNode( this, n );
	_all[ $.uid( this ) ] = this;
	this.init_x( x );
},
_regTarget = function( a ) {
	var a = $.proxy( this, a ), b = this.x.target.split( ',' );
	for ( var i = 0, c; i < b.length; i ++ ) {
		if ( c = this.ownerView.find( b[ i ] ) )
			a();
		else
			this.ownerView.initTargets[ b[ i ] ] = a;
	}
},
// 有模板的widget在初始化时会读取模板属性，但下列属性例外
_init_ignore = { 'node': T, 'nodes': T, 'result': T },
/* `widget`  最基础的widget类，所有widget都继承它 */
W = define( 'Widget', function() {
	return $.createClass( {
	// @x -> 配置参数, p -> parentNode, n -> number/name
	Const: function( x, p, n ) {
		this.ownerView = _view( p );
		_regWidget.call( this, x, p, n );
		p && _setView.call( this, this.ownerView );
		this.init_nodes();
		this._instanced = T;
		if ( this.x.id && this.ownerView.initTargets[ this.x.id ] ) {
			this.ownerView.initTargets[ this.x.id ]();
			delete this.ownerView.initTargets[ this.x.id ];
		}
	},
	Helper : {
		all: _all,
		vm:  _view,
		w: _widget,
		e: _widgetEvent,
		isCmd: function( a ) { return a && _cmdHooks[ a.type ] }
	},
	Extend: Node,
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
		x_node: function() {
			return this.x.node;
		},
		// @private: 返回子节点配置项的集合
		x_nodes: function() {
			return this.x.nodes;
		},
		// @private: 解析子节点配置项，获取需要的type
		x_childtype: function( t ) {
			return t;
		},
		// @private: 初始化配置参数
		_init_x: function( x ) {
			this.x = x;
			var r = this.rootNode;
			r && this.nodeIndex > -1 && r.x_childtype( this.type ) === this.type && (r = r.x.pub) && $.extendDeep( x, r );
			if ( this.x.template ) {
				var t = _getTemplate( this.x.template, T );
				if ( t ) {
					if ( !t.type || t.type === this.type ) {
						// prior_x 是优先的，不可被template重写的属性
						this.prior_x = $.extend( {}, x );
						for ( var k in t ) {
							if ( k.charAt( 0 ) !== '@' && !(k in x) && !_init_ignore[ k ] ) {
								x[ k ] = t[ k ];
							}
						}
					} else if ( t.type && !W.isCmd( t ) ) {
						$.alert( Loc.ps( Loc.debug.error_template_type, this.x.template, this.type ) );
					}
				}
			}
			!x.ownproperty && $.extendDeep( x, this.getDefaultOption( x.cls ) );
		},
		init_x: function( x ) {
			this._init_x( x );
			this.x.template && this.init_template( {} );
		},
		// @d -> 模板数据
		init_template: function( d ) {
			if ( ! d ) return;
			var x;
			if ( d instanceof Error ) {
				x = this.init_x_error( d );
			} else {
				this._srcdata = d;
				x = this.x.template ? _compileTemplate( this, d ) : this.init_x_filter( d );
			}
			if ( x ) {
				if ( x.type !== this.type && W.isCmd( x ) ) {
					this.removeElem( 'loading' );
					this.exec( x );
				} else {
					if ( this.x.preload ) {
						x = _compilePreload( this.x.preload, x );
					}
					if ( x ) {
						if ( x.type && x.type !== this.type )
							x = { type: this.type, node: x };
						var k, _x = this.prior_x || {};
						for ( k in x ) {
							if ( !(k in _x) ) this.attr( k, x[ k ] );
						}
						this.init_nodes();
					}
				}
			}
		},
		// 装载失败的处理
		init_x_error: function( d ) {
			return { type: 'Html', text: d.x.text };
		},
		// 装载失败的处理 @x -> data json
		init_x_filter: function( x ) {
			return x;
		},
		// @private: 初始化子节点
		init_nodes: function() {
			if ( this.length )
				return;
			var c = this.x_nodes();
			if ( c ) {
				for ( var j = 0, l = c.length; j < l; j ++ )
					this.parse( c[ j ] );
			} else if ( c = this.x_node() )
				this.parse( c );
		},
		// 获取widget的默认配置项 /@a -> cls
		getDefaultOption: function( a ) {
			var y = _dfopt[ this.type ];
			if ( _dfcls[ this.type ] ) {
				var b = this.className + (a ? ' ' + a : ''), c = _dfcls[ this.type ], z;
				for ( var k in c ) {
					$.idsAll( b, k, ' ' ) && $.mergeDeep( z || (z = {}), c[ k ] );
				}
				z && (y = $.extendDeep( z, y ));
			}
			return y;
		},
		getLength: function() {
			return this.length;
		},
		// 改变默认设置 @a -> key
		defaults: function( a ) {
			if ( typeof a === _STR ) {
				var d = this.defaultHooks || this.Const.Default, e = d ? d[ a ] : U;
				return typeof e === _FUN ? e.call( this ) : a === 'type' ? this.type: e;
			}
			this.defaultHooks = $.extend( a, this.Const.Default, this.defaultHooks );
		},
		// @private: 解析并生成子节点
		parse: function( x, n ) {
			var g = require( n === -1 ? x.type : this.x_childtype( x.type ) );
			if ( g )
				return new g( x, this, n );
			alert( Loc.ps( Loc.debug.error_type, $.jsonString( x, N, 2 ) ), N, N, N, F );
		},
		// 增加一个子节点 /@ a -> widget option, n -> nodeIndex?, g -> default widget option(附加的默认选项)?
		add: function( a, n, g ) {
			if ( a.isWidget ) {
				(a.type_view ? _setParent : _setView).call( a, _view( this ) );
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
				this.attrSetter( a, b, c );
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
		// a -> name, b -> value, c -> old value
		attrSetter: function( a, b, c ) {
			switch( a ) {
				case 'cls':
					c && this.removeClass( c );
					b && this.addClass( b );
				break;
				case 'style':
					this.css( b );
				break;
				case 'beforeContent':
				case 'prependContent':
				case 'appendContent':
				case 'afterContent':
					var d = a.replace( 'Content', '' ), s = this[ 'html_' + d ]();
					this.$( d ) ? Q( this.$( d ) ).replaceWith( s ) : Q( this.$() )[ d ]( s );
				break;
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
		// @a -> css options
		css: function( a, b, c ) {
			b == N ? (b = a, a = N) : (typeof b !== _OBJ && c == N && (c = b, b = a, a = N));
			this.$( a ) && $.css( this.$( a ), b, c );
			return this;
		},
		// 获取第几个子节点
		get: function( a ) {
			return a == N ? _slice.call( this ) : a < 0 ? this[ this.length + a ] : this[ a ];
		},
		// 获取所有子孙节点
		descendants: function() {
			for ( var i = 0, r = [], l = this.length; i < l; i ++ ) {
				r.push( this[ i ] );
				this[ i ].length && r.push.apply( r, this[ i ].descendants() );
			}
			return r;
		},
		// 获取某个子孙节点 /@ a -> type
		descendant: function( a ) {
			if ( this.type === a )
				return this;
			if ( this.isDialogWidget )
				return;
			for ( var i = 0, l = this.length, r; i < l; i ++ ) {
				if ( !this[ i ].type_view && (r = this[ i ].descendant( a )) )
					return r;
			}
			for ( var i in this.discNodes ) {
				if ( !this.discNodes[ i ].type_view && (r = this.discNodes[ i ].descendant( a )) )
					return r;
			}
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
					if ( a.call( p, this ) )
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
				} else if ( p.type !== 'View' && p.$() ) {
					if ( ! ( p = _widget( p.$().parentNode ) ) )
						return;
				} else
					return;
			}
		},
		snapElem: function() {
			return this.$();
		},
		srcData: function() {
			var c = this.srcParent();
			return c && c._srcdata;
		},
		srcParent: function() {
			return this.closest( function() { return this._srcdata } );
		},
		click: function() {
			this.trigger( 'click' );
			return this;
		},
		// 存取临时变量
		data: function( a, b ) {
			if ( typeof a === _OBJ )
				$.merge( this.x.data || (this.x.data = {}), a );
			else if ( arguments.length === 1 )
				return (this.x.data && this.x.data[ a ]);
			else if ( a == N )
				return this.x.data;
			else
				(this.x.data || (this.x.data = {}))[ a ] = b;
		},
		closestData: function( a ) {
			var d = this.x.data && this.x.data[ a ];
			return d !== U ? d : this.parentNode.closestData( a );
		},
		// 获取下一个兄弟节点
		next: function() {
			 return this.parentNode && this.parentNode[ this.nodeIndex + 1 ];
		},
		// 获取上一个兄弟节点
		prev: function() {
			 return this.parentNode && this.parentNode[ this.nodeIndex - 1 ];
		},
		// 当前节点跟a节点交换位置
		swap: function( a ) {
			var b = this.nodeIndex, c = a.nodeIndex, p = this.parentNode,
				d = Q( this.$() ).prev(), e = Q( a.$() ).prev();
			p.add( a, b );
			p.add( this, c );
			d.length ? d.after( a.$() ) : p.insertHTML( a.$(), 'prepend' );
			e.length ? e.after( this.$() ) : p.insertHTML( this.$(), 'prepend' );
		},
		// 显示节点位置，用于调试的方法
		track: function() {
			if ( this.$() ) {
				var d = $.dialog( this );
				d && d.front();
				_scrollIntoView( this );
				var b = $.bcr( this.$() ),
					c = Q( '<div style="width:' + (b.width - 2) + 'px;height:' + (b.height - 2) + 'px;top:' + (b.top - 1) + 'px;left:' + (b.left - 1) + 'px;position:absolute;border:3px solid red;opacity:0"></div>' );
				Q( document.body ).append( c ).trigger( 'click' ).on( 'click.track', function() { c.remove(); Q( this ).off( 'click.track' ) } );
				(function( t ) { 
					var f = arguments.callee;
					$.ease( function( p ) {
						c.css( 'opacity', p );
						p == 1 && t && f(  -- t );
					}, 300 );
				})( 2 );
			}
		},
		// 执行命令 /@a -> cmd id, arg1, arg2,...argN
		cmd: function( a ) {
			return this.exec( a, arguments.length > 1 ? _slice.call( arguments, 1 ) : N );
		},
		// 执行命令 /@a -> id[string/object], b -> args[array], c -> feature, d -> data
		exec: function( a, b, c, d ) {
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
					!_cmdWidgets[ a.type ] && _dfopt[ a.type ] && !a.ownproperty && $.extendDeep( a, _dfopt[ a.type ] );
					$.point();
					return _cmdHooks[ a.type ].call( self, a, b, d );
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
		hasBubble: function( a, b ) {
			return this.contains( a, b );
		},
		// 显示或隐藏 /@a -> 是否显示T/F, b -> 设置为true，验证隐藏状态下的表单。默认情况下隐藏后不验证
		display: function( a, b ) {
			var c = a == N || (a.isWidget ? a.x.expanded : a), p = this.parentNode, d = 'f-hide' + (b ? '' : ' f-form-hide'), o = this.$();
			a != N && a.isWidget && (c ? (o.toggleCommander = N) : (o.toggleCommander = a.id));
			o && $.classAdd( o, d, ! c );
			this.x.display = !!c;
			!!c && this.trigger( 'display' );
		},
		isDisplay: function() {
			return this.x.display != N ? this.x.display : !(this.parentNode && this.parentNode.type_frame);
		},
		toggleDisplay: function() {
			this.display( ! this.isDisplay() );
		},
		// 触发用户定义的事件 / @e -> event, a -> [args]?, f -> func string?
		triggerHandler: function( e, a, f ) {
			if ( this._disposed )
				return;
			var d = e.runType || e.type || e, t = _event_hump[ d ] || d, f = f || (this.x.on && this.x.on[ t ]), c = { 'event': e }, g = a ? [ e ].concat( a ) : [ e ];
			if ( a != N ) {
				for ( var i = 1, l = g.length; i < l; i ++ )
					c[ '$' + (i - 1) ] = g[ i ];
			}
			return f && (typeof f === _FUN ? f.apply( this, g ) : this.formatJS( f, c ));
		},
		// 触发系统事件
		triggerListener: function( e, a ) {
			if ( this._disposed )
				return;
			var b = this.Const.Listener,
				d = e.runType || e.type || e,
				t = _event_hump[ d ] || d,
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
				c = this.proxyHooks,
				d = e.runType || e.type || e,
				t = _event_hump[ d ] || d,
				h = b && b.body[ t ],
				g = arguments.length > 1 ? [ e ].concat( a ) : [ e ],
				f = h && (h.method || h),
				r, s;
			if ( b && b.block && b.block.call( this, e ) )
				return;
			if ( c && (c = c[ t ]) ) {
				for ( var j in c ) {
					var n = new Q.Event( e.type || e, e );
					n.runType = j;
					this.trigger( n, a );
				}
			}
			if ( _event_priority[ t ] ) { // 用户事件优先执行
				if ( ! (h && h.block && h.block.call( this, e )) && (r = this.triggerHandler( e, a )) === F )
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
				!this.discNodes[ i ].isDialogWidget && this.discNodes[ i ].triggerAll( e ); // 弹窗不触发来自父节点的递归事件
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
		at: function() {
			var a, b;
			if ( a = this.x.at && _atCache[ this.x.at ] ) {
				if ( b = a.children ) { // [ { before: '', at: '' } ]
					var r = b.data;
				}
				this.attr( a.props ); 
			}
			if ( a = this.x.on && this.x.on.at && _atCache[ this.x.on.at ] ) {
				for ( var k in a.props )
					this.setOn( k, a.x[ k ] );
			}
			for ( var i = 0; i < this.length; i ++ )
				this[ i ].at();
		},
		// 解析并运行包含 "$属性名" 的js语法内容  /@a -> js string, b -> args({ name: value })?, c -> data?, d -> callback?
		formatJS: function( a, b, c, d, x ) {
			var x = x || this.x, n = [ '$this' ], m = [ c || x.data ];
			if ( b ) {
				for ( var k in b ) { n.push( k ); m.push( b[ k ] ); }
			}
			var h = _formatCache[ a + n.join() ];
			if ( ! h || ! h.dfish_format_fields ) {
				var f = [], g = n.concat();
				if ( a.indexOf( '$' ) > -1 ) {
					var r = /\$(\w+)/ig, k;
					while ( k = r.exec( a ) ) {
						if( ! $.inArray( g, k[ 0 ] ) ) { g.push( k[ 0 ] ); f.push( k[ 1 ] ); }
					}
				}
				h = _formatCache[ a + n.join() ] = Function( g.join( ',' ), a );
				h.dfish_format_fields = f;
			}
			for ( var i = 0, e, f = h.dfish_format_fields, l = f.length, v; i < l; i ++ ) {
				//v = c ? c[ f[ i ] ] : (e = x.data && x.data[ f[ i ] ]) !== U ? e : x[ f[ i ] ];
				if ( c ) {
					v = c[ f[ i ] ];
				} else {
					(v = x.data && x.data[ f[ i ] ]) === U && (v = x[ f[ i ] ]) === U && (v = this.closestData( f[ i ] )) === U && (v = this.rootNode && this.rootNode.x[ k ]);
				}
				m.push( d ? d( v ) : v );
			}
			return h.apply( this, m );
		},
		// @a -> content|js, b -> args?, c -> urlEncode?, d -> callback?
		formatStr: function( a, b, c, d, x ) {
			var self = this, r = b && $.isArray( b ),
				s = a.replace( /\$\{(\w[\w.]*)\}|\$(\w[\w.]*)/gi, function( $0, $1, $2 ) {
					var e = $1 || $2, k = e, v, t;
					if ( e.indexOf( '.' ) > 0 ) {
						k = $.strTo( e, '.' );
						t = $.strFrom( e, k );
					}
					if ( b && (isNaN( k ) || r) && b[ k ] !== U ) {
						v = b[ k ];
					} else {
						x == N && (x = self.x);
						(v = x.data && x.data[ k ]) === U && (v = x[ k ]) === U && (v = self.closestData( k )) === U && (v = self.rootNode && self.rootNode.x[ k ]);
					}
					if ( t && v != N ) {
						try { eval( 'v = v' + t ); } catch( ex ) { v = N; }
					}
					d && (v = d( v ));
					return v && c ? $[ c === T ? 'urlEncode' : c ]( v ) : (v == N ? '' : v);
				} );
			return s;
		},
		// 实现兄弟节点的tab效果 /@ a -> T/F
		tabFocus: function( a ) {
			if ( ! this._disposed ) {
				var p = this.rootNode || this.parentNode, f = p.focusNode;
				if ( a == N || a ) {
					f && f != this && f.tabFocus( F );
					this.x.focus = T;
					this.addClass( 'z-on' );
					p.focusNode = this;
					this.focusOwner = p;
				} else {
					if ( f === this ) {
						this.x.focus = F;
						this.removeClass( 'z-on' );
						delete p.focusNode; delete this.focusOwner;
					}
				}
			}
		},
		isNormal: function() {
			return !this.x.status || this.x.status === 'normal';
		},
		isDisabled: function() {
			return this.x.status === 'disabled';
		},
		normal: function() {
			this.disable( F );
			return this;
		},
		readonly: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'readonly' : '';
			this.addClass( 'z-ds', a );
			this.trigger( 'statuschange' );
			return this;
		},
		validonly: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'validonly' : '';
			this.addClass( 'z-ds', a );
			this.trigger( 'statuschange' );
			return this;
		},
		disable: function( a ) {
			a = a == N || a;
			this.x.status = a ? 'disabled' : '';
			this.addClass( 'z-ds', a );
			this.trigger( 'statuschange' );
			return this;
		},
		status: function( a ) {
			if ( a == N )
				return this.x.status;
			a === 'disabled' ? this.disable() : a === 'readonly' ? this.readonly() : a === 'validonly' ? this.validonly() : (a === 'normal' || a == '') ? this.normal() : (this.x.status = a);
		},
		// 调整大小
		resize: function( w, h ) {
			if ( w != N && typeof w === _OBJ )
				h = w.height, w = w.width;
			if ( w != N )
				_w_size.width.call( this, w );
			if ( h != N )
				_w_size.height.call( this, h );
			delete this._scales;
			for ( var i = 0, l = this.length; i < l; i ++ )
				_w_rsz_all.call( this[ i ] );
			this.trigger( 'resize' );
		},
		// 替换为另一个widget /@ a -> widget option
		replace: function( a ) {
			if ( this._disposed )
				return;
			var p = this.parentNode, i = this.nodeIndex, o = this.focusOwner;
			if ( ! a.isWidget ) {
				a.type == N && (a.type = this.type);
				a.width == N && (a.width = this.x.width);
				a.height == N && (a.height = this.x.height);
			}
			var e = this.$(), f = e && this.isDisplay();
			this.dispose();
			var g = p.add( a, i );
			o && (g.focusOwner = o, o.focusNode = g);
			f === F && g.addEvent( 'ready', function() { this.display( F ) } );
			g.render( e, 'replace' );
			this.removeElem();
			p.trigger( 'resize', 'replace' );
			return g;
		},
		// 清空子节点  /@a -> 是否删除离散节点
		empty: function( a ) {
			var i = this.length, j = i;
			while ( i -- )
				this[ i ].remove( T );
			if ( a === T ) {
				for ( i in this.discNodes )
					this.discNodes[ i ].remove();
			}
			j != i && this.trigger( 'nodeChange' );
		},
		// @a -> 是否刷新子节点
		repaint: function( a ) {
			if ( this._disposed )
				return;
			if ( this.$() ) {
				if ( a ) {
					this.replace( this.x );
				} else
					this.repaintSelf();
			} else
				this.render();
		},
		repaintSelf: function() {
			var f = $.frag( this.$() );
			Q( this.$() ).replaceWith( '<' + this.tagName + this.html_prop() + '></' + this.tagName + '>' );
			Q( this.$() ).append( f );
		},
		develop: function() {
			//fixme: 用于调试定位
		},
		// 生成页面可见的 DOM 元素  /@a -> target elem, b -> method[append|prepend|before|after|replace]
		_render: function( a, b ) {
			// 没有父节点 则先加上父节点
			if ( ! this.parentNode ) {
				((a && _widget( a )) || _docView).add( this );
			}
			var p = this.parentNode, s = this.html();
			if ( this.$() && ! a )
				$.replace( this.$(), s );
			else if ( a )
				 $[ b || 'append' ]( a, s );
			else {
				for ( var i = this.nodeIndex - 1, l = p.length, c; i > -1; i -- )
					if ( p[ i ].$() ) { (c = p[ i ]).insertHTML( s, 'after' ); break; }
				if ( ! c ) {
					for ( i = this.nodeIndex + 1; i < l; i ++ )
						if ( p[ i ].$() ) { (c = p[ i ]).insertHTML( s, 'before' ); break; }
				}
				! c && p.insertHTML( s );
			}
		},
		// 生成页面可见的 DOM 元素  /@a -> target elem, b -> method[append|prepend|before|after|replace]
		render: function( a, b ) {
			this._render( a, b );
			this.triggerAll( 'ready' );
			! this._disposed && this.nodeIndex >= 0 && this.parentNode.trigger( 'nodeChange' );
			return this;
		},
		// @dao 通过js增加子节点时会调用此方法 / a -> html|widget, b -> where(prepend|append|before|after)
		insertHTML: function( a, b ) {
			this.$() && $[ b || 'append' ]( this.$(), a.isWidget ? a.$() : a );
		},
		prop_cls: function() {
			var c = this.className || '';
			if ( this.x.cls )
				c = $.idsAdd( c, this.x.cls.replace( /\./g, '' ), ' ' );
			if ( ! this.isNormal() )
				c += ' z-ds';
			if ( this.x.display === F )
				c += ' f-hide';
			if ( this.parentNode && this.parentNode.childCls )
				c += ' ' + (typeof this.parentNode.childCls === _FUN ? this.parentNode.childCls( this ) : this.parentNode.childCls);
			return c;
		},
		prop_style: function() {
			var t = '', v;
			if ( ( v = this.innerWidth() ) != N )
				t += 'width:' + v + 'px;';
			if ( ( v = this.innerHeight() ) != N )
				t += 'height:' + v + 'px;';
			if ( this.x.minWidth && (v = this.minWidth()) )
				t += 'min-width:' + v + 'px;';
			if ( this.x.maxWidth && (v = this.maxWidth()) )
				t += 'max-width:' + v + 'px;';
			if ( this.x.minHeight && (v = this.minHeight()) )
				t += 'min-height:' + v + 'px;';
			if ( this.x.maxHeight && (v = this.maxHeight()) )
				t += 'max-height:' + v + 'px;';
			this.cssText && (t += this.cssText);
			if ( this.x.style )
				t += this.x.style;
			return t;
		},
		// @a -> text, @b -> format, @c -> escape
		prop_title: function( a, b, c ) {
			var l = arguments.length;
			if ( l < 1 ) {
				var t = this.attr( 'tip' );
				a = t === T ? this.x.text : t;
			}
			l < 2 && (b = this.x.format);
			l < 3 && (c = this.x.escape);
			return a && typeof a === _STR && ! b ? ' title="' + $.strQuot( c ? a.replace( /&/g, '&amp;' ) : a, !c ) + '"' : '';
		},
		// @a -> text, b -> format, c -> escape?, d -> callback?, e -> x?
		html_format: function( a, b, c, d, e ) {
			var l = arguments.length;
			l < 1 && (a = this.x.text);
			l < 2 && (b = this.x.format);
			l < 3 && (c = this.x.escape);
			if ( b ) {
				var s = '';
				//try {
					s = b.indexOf( 'javascript:' ) === 0 ? this.formatJS( b, N, N, d, e ) : this.formatStr( b, N, c !== F && 'strEscape', d, e );
				//} catch( ex ) {	throw ex; }
				return typeof s === _STR ? _parseHTML.call( this, s ) : s;
			} else
				return c !== F ? $.strEscape( a ) : (a == N ? '' : '' + a);
		},
		html_prop: function() {
			var b = ' w-type="' + this.type + '" id=' + this.id,
				v, t = this.prop_style(), n = this.Const.Listener;
			if ( this.x.id )
				b += ' w-id="' + this.x.id + '"';
			if ( t )
				b += ' style="' + t + '"';
			if ( ! (n && n.tag) && (this.x.on || n) )
				b += _html_on.call( this );
			if ( v = this.prop_cls() )
				b += ' class="' + v + '"';
			//if ( v = this.attr( 'align' ) )
			//	b += ' align=' + v;
			if ( this.property )
				b += this.property;
			return b;
		},
		html_nodes: function() {
			for ( var i = 0, l = this.length, s = []; i < l; i ++ )
				s.push( this[ i ].html() );
			return s.join( '' );
		},
		html_self: function() {
			return this.html();
		},
		html: function() {
			return this.html_before() + '<' + this.tagName + this.html_prop() + '>' + this.html_prepend() + this.html_nodes() + this.html_append() + '</' + this.tagName + '>' + this.html_after();
		},
		removeElem: function( a ) {
			$.remove( this.$( a ) );
		},
		// @a -> 设置为true，不触发删除单个节点所需的处理
		remove: function( a ) {
			this.removeElem();
			this.x.beforeContent && this.removeElem( 'before' );
			this.x.afterContent && this.removeElem( 'after' );
			var p = this.parentNode, n = this.nodeIndex;
			this.dispose();
			if ( p && a !== T ) {
				var s = p.type_horz ? 'width' : 'height';
				p[ 0 ] && (p.type_horz || p.type_vert) && _w_size[ s ].call( p[ 0 ], p[ 0 ].x[ s ] );
				n >= 0 && p.trigger( 'nodeChange' );
			}
		},
		dispose: function( d ) {
			if ( this._disposed )
				return;
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
			if ( this.x.gid && _globals[ this.x.gid ] === this )
				delete _globals[ this.x.gid ];
			this._disposed = T;
			delete _all[ this.id ];			
		}
	}
	} );
} ),
// 模板内容
PreloadBody = define.widget( 'PreloadBody', {} ),

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
	delete this._scales;
	var l = this.length;
	if ( l ) {
		for ( var i = 0; i < l; i ++ )
			_w_rsz_all.call( this[ i ] );
	}
	for ( var i in this.discNodes ) {
		var n = this.discNodes[ i ];
		//对话框内有-1和*元素共存时，需要有$()才能调整
		if ( n.isDialogWidget && n.x.memory && ! n.$() )
			! n.hasEvent( 'show._w_rsz_all' ) && n.addEventOnce( 'show._w_rsz_all', _w_rsz_all );
		else
			_w_rsz_all.call( n );
	}
	this.trigger( 'resize' );
},
_w_rsz_layout = function() {
	delete this._scales;
	for ( var i = 0, l = this.length; i < l; i ++ )
		_w_rsz_all.call( this[ i ] );	
},
// @a -> size, b -> min, c -> max
_w_size_rng = function( a, b, c ) {
	if ( b && ! isNaN( b ) ) a = Math.max( a, b );
	if ( c && ! isNaN( c ) ) a = Math.min( a, c );
	return a;
},
// widget配置项里设置了style，又没有设置widthMinus和heightMinus，则由系统解析style，获取widthMinus和heightMinus的值
// 如果设置了cls，而cls里有 padding margin border 等，就需要业务计算并设置widthMinus和heightMinus
//@ _c -> cls, _1 -> style, _2 -> widthMinus, _3 = heightMinus
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
	return { widthMinus: _2, heightMinus: _3 };
},
_get_margin = {
	width: function( a ) {
		return _number( a.currentStyle.marginLeft ) + _number( a.currentStyle.marginRight );
	},
	height: function( a ) {
		return _number( a.currentStyle.marginTop ) + _number( a.currentStyle.marginBottom );
	}
};
$.each( [ 'width', 'height' ], function( v, j ) {
	var c = v.charAt( 0 ),
		y = "__" + v,   // __width, __height
		z = c.toUpperCase() + v.slice( 1 ),
		iu = v + 'Minus', 	// widthMinus, heightMinus
		nv = 'min' + z,
		xv = 'max' + z,
		rv = '__runtime_' + v,
		sz = 'scale' + z,	// scaleWidth, scaleHeight	为子元素分配大小
		oz = 'outer' + z,	// outerWidth, outerHeight  整体所占空间, 相当于 offset + margin
		iz = 'inner' + z,	// innerWidth, innerHeight	内部可用空间, 并为当前元素的style.width/style.height提供值
		xz = 'max' + z,     // maxWidth, maxHeight
		nz = 'min' + z;		// minWidth, minHeight

	// 实现 wg.width(), wg.height()
	// 返回为null时，去获取元素的offsetWidth/offsetHeight
	_proto[ v ] = function( a ) {
		if ( a === U ) {
			var b = this[ oz ]();
			return b == N && this.$() ? this.$()[ 'offset' + z ] + _get_margin[ v ]( this.$() ) : b;
		} else {
			delete this[ y ];
			v === 'width' ? this.resize( a ) : this.resize( N, a );
		}
	};
	_proto[ oz ] = function() {
		// @fixme: 如果在构造函数Const里调用 .width() 和 .height(), 会因为兄弟节点还没实例化而计算出错，因此添加_instanced参数做控制，当兄弟节点全部实例化时才能调用.width 和 .height。
		// 		   这是暂时的解决办法。之后的优化目标是在Const里可以获取高宽。
		if ( ! this._instanced ) { return N; }
		if ( y in this ) {
			return this[ y ];
		}
		var r = this.parentNode[ sz ]( this );
		if ( this.parentNode._instanced ) {
			this[ y ] = r;
		}
		return r;
	};
	//.minWidth, .minHeight /@u -> 是否保留 widthMinus heightMinus 部分的大小
	_proto[ nz ] = function( u ) {
		var a = this.attr( nv );
		if ( a == N ) return N;
		a = this.parentNode[ sz ]( this, a );
		return a == N ? N : u ? a : a - this[ iu ]();
	};
	//.maxWidth, .maxHeight
	_proto[ xz ] = function( u ) {
		var a = this.attr( xv );
		if ( a == N ) return N;
		a = this.parentNode[ sz ]( this, a );
		return a == N ? N : u ? a : a - this[ iu ]();
	};
	//.innerWidth, .innerHeight
	_proto[ iz ] = function() {
		var a = this[ oz ](),
			r = a == N ? N : a - this[ iu ]();
		if ( r == N )
			return N;
		return r;
	};
	// scaleWidth, scaleHeight 默认的分配给子元素高宽的方法 /@a -> widget, b -> widget size, c -> this size?
	_proto[ sz ] = function( a, b, c ) {
		if ( a == N )
			return N;
		var d = b != N ? b : a.attr( v );
		if ( $.isNumber( d ) )
			d = parseFloat( d );
		if ( typeof d === _NUM )
			return d < 0 ? N : d;
		c === U && (c = this[ iz ]());
		//if ( c != N && typeof d === _STR && d.indexOf( '%' ) > 0 )
		//	c = Math.floor( c * parseFloat( d ) / 100 );
		if ( c != N && d != N ) {
			var e = $.scale( c, [ { value: d, min: a.attr( nv ), max: a.attr( xv ) } ] );
			c = e[ 0 ];
		}
		return c;
	};
	// .widthMinus, .heightMinus
	_proto[ iu ] = function() {
		var b = this.attr( iu );
		if ( b != N )
			return b;
		// 如果用户定义了样式且没有设置widthMinus和heightMinus，则使用系统预设的样式处理机制
		if ( this.x.cls || this.x.style ) {
			var f = _size_fix( this.x.cls, this.x.style );
			if ( f )
				return (f[ iu ] || 0);
		}
		return 0;
	};
	// 根据子元素各自设置的比例，统一计算后进行高宽分配 /@a -> widget, b -> widget size?, c -> self size?
	_w_scale[ v ] = function( a, b, c ) {
		var d = b != N ? b : a.attr( v ), s = this._scales;
		if ( $.isNumber( d ) && d > -1 && ! this.isScaleCover )
			return parseFloat( d );
		if ( b != N || c != N || ! s ) {
			if ( ! this.length )
				return N;
			var o = this.$();
			for ( var i = 0, e, f, n, x, r = [], l = this.length; i < l; i ++ ) {
				e = b != N && this[ i ] === a ? b : this[ i ][ rv ] ? (this[ i ].isDisplay() ? this[ i ][ v ]() : N) : (o && this[ i ].$() && !this[ i ].isDisplay() ? N : this[ i ].attr( v ));
				f = (e == N || e < 0) && ! this[ _w_bro[ v ] ] ? '*' : e;
				r.push( { value: f, min: this[ i ].attr( nv ), max: this[ i ].attr( xv ) } );
			}
			s = $.scale( c === U ? this[ iz ]() : c, r, this.isScaleCover );
			if ( b === U && c === U )
				this._scales = s;
		}
		r = s[ a.nodeIndex ];
		r == N && (r = a.defaults( v ));
		return r < 0 || r === '*' ? N : r;
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
		if ( ! this.$() )
			return;
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
		if ( a != -1 && (t !== U || a != N) && this.$() ) {
			delete this[ y ];
			if ( t !== this[ oz ]() ) {
				t = this[ iz ]();
				if ( this.$() ) {
					this.$().style[ v ] = t == N ? '' : Math.max( t, 0 ) + 'px';
				}
			}
		}
	};
} );
$.each( 'prepend append before after'.split(' '), function( v, j ) {
	// 实现: wg.append(), wg.prepend(), wg.before, wg.after()
	_proto[ v ] = function( o ) {
		if ( typeof o === _STR )
			return this.insertHTML( o, v );
		var a = o.isWidget ? [ o ] : $.arrMake( o ), b, q, i;
		if ( a[ 0 ].isWidget ) {
			if ( a[ 0 ] === this )
				return this;
			for ( q = a[ 0 ].parentNode, i = 0; i < a.length; i ++ )
				a[ i ].removeNode( T );
			b = a[ 0 ].$();
		}
		var i = j === 3 ? this.nodeIndex + 1 : j === 2 ? this.nodeIndex : j === 1 ? this.length : 0, d = this.nodeIndex > -1,
			p = j > 1 ? this.parentNode : this, l = a.length, k = 0, s = p.type_horz ? 'width' : 'height', r = [];
		for ( ; k < l; k ++ )
			r.push( p.add( a[ k ], d || j < 2 ? i + k : -1 ) );
		if ( this.$() ) {
			if ( b ) {
				for ( k = 0; k < l; k ++ )
					this.insertHTML( a[ k ], v );
			} else {
				for ( k = 0, o = ''; k < l; k ++ )
					o += r[ k ].html();
				this.insertHTML( o, v );
			}
		}
		d && (((k = {})[ s ] = r[ 0 ].x[ s ]), r[ 0 ].resize( k ));
		if ( ! b && this.$() ) {
			for ( k = 0; k < l; k ++ )
				r[ k ].triggerAll( 'ready' );
		}
		d && q && q[ 0 ] && (((k = {})[ s ] = q[ 0 ].x[ s ]), q[ 0 ].resize( k ));
		q && q.trigger( 'nodeChange' );
		p.trigger( 'nodeChange' );
		p.trigger( 'resize', v );
		return r[ 0 ];
	};
	// 实现: wg.html_before(), wg.html_prepend(), wg.html_append(), wg.html_after()
	_proto[ 'html_' + v ] = function() {
		if ( this.x[ v + 'Content' ] ) {
			var c = this.x[ v + 'Content' ];
			if ( typeof c === _STR && c.indexOf( 'javascript:' ) === 0 )
				c = this.formatJS( c );
			if ( typeof c === _OBJ )
				c = this.add( c, -1 ).html();
			else
				c = _parseHTML.call( this, c );
			return '<dfn id=' + this.id + v + ' class=f-addcon>' + c + '</dfn>';
		}
		return '';
	}
} );
// scroll helper
var _html_resize_sensor = function( w, h ) {
	return '<div id=' + this.id + 'rsz class=f-resize-sensor><div class=f-resize-sensor-expand><div class=f-resize-sensor-expand-core></div></div><div class=f-resize-sensor-shrink><div class=f-resize-sensor-shrink-core></div></div></div>';
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
	if ( ! mbi && this._scr_usable && this.$() ) {
		var b = ie7 ? br.scroll : 0,
			d = this.$( 'cont' ).offsetHeight || this.$( 'gut' ).scrollHeight,
			g = ie7 ? (this.$( 'ovf' ).scrollWidth - b) : (this.$( 'gut' ).scrollWidth);
		if ( d > 0 ) {
			var c = this.innerHeight() || this.$().offsetHeight,
				e = Math.min( 1, c / d );
			this.css( 'y', 'display', e < 1 ? 'block' : '' )
				.css( 'ytr', 'height', Math.max( 14, c * e ) );
			this._scr_rateY = e < 1 && ((d - c) / (this.$( 'y' ).offsetHeight - this.$( 'ytr' ).offsetHeight ));
		}
		if ( g > 0 ) {
			var f = this.innerWidth() || this.$().offsetWidth,
				h = Math.min( 1, f / g );
			this.css( 'x', 'display', h < 1 ? 'block' : '' )
				.css( 'xtr', 'width', Math.max( 14, f * h ) );
			this._scr_rateX = h < 1 && ((g - f + b) / (this.$( 'x' ).offsetWidth - this.$( 'xtr' ).offsetWidth ));
		}
		this.trigger( 'scroll' );
	}
},
/* `Scroll` */
Scroll = define.widget( 'Scroll', {
	Helper: {
		// 获取某个 widget 所在的有 scroll 的面板 / a -> widget|elem
		get: function( a ) {
			a = _widget( a );
			do {
				if ( a.isScrollable && a.isScrollable() && a.innerHeight() > 0 )
					return a;
			} while ( (a = a.parentNode) && !a.isDialogWidget );
			//return _widget( a ).closest( function() { return this.isScrollable && this.isScrollable() && this.innerHeight() > 0 } );
		}
	},
	Listener: {
		body: {
			ready: function() {
				// widget的dom可能会被业务重新生成，需要重置相关变量
				delete this._scr_ready; delete this._scr_wd; delete this._scr_ht;
				this.x.swipedown && this.setSwipedown();
			},
			mouseOver: {
				occupy: T,
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
						//if ( ! this._scr_timer )
						//	this._scr_timer = setInterval( this._scr_check, 999 );
						$.classAdd( this.$(), 'z-hv' );
					}
				}
			},
			mouseOut: {
				occupy: T,
				method: function() {
					if ( this._scr_timer ) {
						clearInterval( this._scr_timer );
						delete this._scr_timer;
					}
					$.classRemove( this.$(), 'z-hv' );
				}
			},
			scroll: function( e ) {
				if ( this._scr_rateY && this.$( 'ytr' ) )
					this.$( 'ytr' ).style.top  = Math.min( this.$( 'ovf' ).scrollTop / this._scr_rateY, this.$( 'y' ).offsetHeight - _number(this.$( 'ytr' ).style.height) ) + 'px';
				if ( this._scr_rateX && this.$( 'xtr' ) )
					this.$( 'xtr' ).style.left = ( this.$( 'ovf' ).scrollLeft / this._scr_rateX ) + 'px';
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
				this.addClass( 'z-autosize', this.width() == N && ! this.x.maxWidth && ! this.x.maxHeight );
			}
		}
	},
	Prototype: {
		// 让元素滚动到可见区域。支持下面两种调用方式 /e -> el|wg, y -> (top,bottom,middle,auto,top+n,bottom+n,n), x -> (left,right,center,auto), p -> ease?, q -> divide(整除数字，让滚动的距离是这个数字的整数倍), r -> callback
		scrollTo: function( e, y, x, p, q, r ) {
			var a = this.$( 'ovf' ), b = this.$( 'gut' ) || this.$( 'cont' ), c = $.bcr( a ), d = $.bcr( b ), f = e ? $.bcr( e ) : d, t, l;
			if ( y != N || e ) {
				if ( y == N || !isNaN( y ) ) {
					t = _number( y );
					if ( e ) t = f.top - d.top - t;
				} else if ( ~y.indexOf( 'top' ) ) {
					t = f.top - d.top - (+y.slice( 3 ));
				} else if ( ~y.indexOf( 'bottom' ) ) {
					t = f.bottom - d.top - c.height + (+y.slice( 6 ));
				} else if ( y === 'middle' ) {
					t = f.top - d.top - (( c.height / 2 ) - ( f.height / 2 ));
				} else {
					//if ( f.bottom < c.top || f.top > c.bottom - br.scroll )
					//	t = f.top - d.top - (( c.height / 2 ) - ( f.height / 2 ));
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
				} else if ( ~y.indexOf( 'left' ) ) {
					l = f.left - d.left - (+y.slice( 4 ));
				} else if ( ~y.indexOf( 'right' ) ) {
					l = f.right - d.left - c.width + (+y.slice( 5 ));
				} else if ( x === 'center' ) {
					l = f.left - d.left - (( c.width / 2 ) - ( f.width / 2 ));
				} else {
					if ( f.right < c.left || f.left > c.right - br.scroll )
						l = f.left - d.left - (( c.width / 2 ) - ( f.width / 2 ));
					else if ( f.left < c.left )
						l = a.scrollLeft - f.left + c.left;
					//else if ( f.right > c.right - br.scroll )
					//	l = a.scrollLeft + f.right - c.right + br.scroll;
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
					i === 1 && r && r.call( this );
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
				$.append( this.$(), this.html_scroll() );
				$.append( this.$( 'cont' ), a );
			}
		},
		checkScroll: function() {
			! mbi && _reset_resize_sensor.call( this ) && _show_scroll.call( this );
		},
		isScrollable: function() {
			return this.attr( 'scroll' ) && ((this.innerWidth() != N || this.x.maxWidth) || (this.innerHeight() != N || this.x.maxHeight));
		},
		isScrollBottom: function() {
			return this.$( 'ovf' ).scrollTop == this.scrollHeight();
		},
		isScrollRight: function() {
			return this.$( 'ovf' ).scrollLeft == this.$( 'ovf' ).scrollWidth - this.$( 'ovf' ).clientWidth;
		},
		scrollHeight: function() {
			return this.$( 'ovf' ).scrollHeight - this.$( 'ovf' ).clientHeight;
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
		// @mobile 下拉刷新
		setSwipedown: function() {
			var o = this.$( 'ovf' ), d = this.$( 'swipedown' ), iy, rl, ht, self = this,
				cmp = function() {
					Q( o ).css( { transform: 'translate3d(0,0,0)', transition: '500ms cubic-bezier(0.165, 0.84, 0.44, 1)' } );
			    	Q( d ).css( { visibility: '', height: '' } );
			    	setTimeout( function() {
			    		$.classRemove( d, 'z-loading z-release' );
			    		Q( o ).css( { transition: '' } );
			    	}, 500 );
				};
	    	o.addEventListener( 'touchStart', function( e ) {
	    		Q( o ).css( 'transition', '' );
	    		iy = e.targetTouches[ 0 ].clientY;
	    		rl = F;
	    		if ( ! ht ) {
	    			ht = d.offsetHeight;
	    		}
	    		Q( d ).css( { visibility: 'visible', height: 0 } );
	    	});
	    	o.addEventListener( 'touchmove', function( e ) {
	    		var y = e.targetTouches[ 0 ].clientY - iy;
	    		if ( o.scrollTop == 0 && y > 0) {
	    			e.preventDefault();
	    			if ( y > ht && ! rl ) {
	    				y = ht;
	    				rl = T;
	    				$.classAdd( d, 'z-release' );
	    			}
	    			d.style.height = Math.min( ht, y ) + 'px';
	    			Q( o ).css( { 'transform': 'translate3d(0,'+ y +'px,0)' } );
	    		}
	    	});
	    	o.addEventListener( 'touchend', function( e ) {
	    		if ( rl ) {
	    			$.classRemove( d, 'z-release' );
	    			$.classAdd( d, 'z-loading' );
		    		self.exec( { type: 'Ajax', src: self.x.swipedown, complete: cmp } );
	    		} else
	    			cmp();
	    		e.preventDefault();
	    	});			
		},
		prop_cls: function() {
			return _proto.prop_cls.call( this ) + (this.attr( 'scroll' ) ? ' f-scroll-wrap' : '');
		},
		prop_cls_scroll_overflow: function() {
			return 'f-scroll-overflow';
		},
		html: function() {
			this.width() == N && ! this.x.maxWidth && ! this.x.maxHeight && $.classAdd( this, 'z-autosize' );
			// 执行 html_nodes 要在执行 html_prop 之前，以备html_nodes中可能要增加样式等操作
			var s = this.html_nodes();
			return this.html_before() + '<' + this.tagName + this.html_prop() + '>' + this.html_prepend() + (this.isScrollable() ? this.html_scroll( s ) : s) + this.html_append() + '</' + this.tagName + '>' + this.html_after();
		},
		html_scroll: function( s ) {
			this._scr_usable = T;
			var w = this.innerWidth(), h = this.innerHeight(), c = br.scroll;
			if ( mbi ) {
				return (this.x.swipedown ? '<div id=' + this.id + 'swipedown class=w-scroll-swipedown><i class="f-i"></i><em class="f-vi _desc"></em></div>' : '') + '<div id=' + this.id + 'ovf class=w-scroll-overflow style="' +
					(w ? 'width:' + w + 'px;' : '' ) + (this.x.maxWidth ? 'max-width:' + this.x.maxWidth + 'px;' : '') +
					(this.x.minWidth ? 'min-width:' + this.x.minWidth + 'px;' : '') + (h ? 'height:' + h + 'px;' : '' ) + (this.x.maxHeight ? 'max-height:' + this.x.maxHeight + 'px;' : '') +
					(this.x.minHeight ? 'min-height:' + this.x.minHeight + 'px;' : '') + '" onscroll=' + eve + '><div id=' + this.id + 'cont>' + (s || '') + '</div></div>';		
			} else {
				return '<div id=' + this.id + 'tank class=f-scroll-tank><div id=' + this.id + 'ovf class="' + this.prop_cls_scroll_overflow() + '" style="margin-bottom:-' + br.scroll + 'px;' + (w == N ? 'margin-right:-' + br.scroll + 'px;' : '') +
					(w ? 'width:' + (w + c) + 'px;' : '' ) + (this.x.maxWidth ? 'max-width:' + (+this.x.maxWidth + c) + 'px;' : '') + (this.x.minWidth ? 'min-width:' + (+this.x.minWidth + c) + 'px;' : '') +
					(h ? 'height:' + (h + c) + 'px;' : '' ) + (this.x.maxHeight ? 'max-height:' + (+this.x.maxHeight + c) + 'px;' : '') + (this.x.minHeight ? 'min-height:' + (+this.x.minHeight + c) + 'px;' : '') +
					'" onscroll=' + eve + '><div id=' + this.id + 'gut' + (ie7 ? '' : ' class=f-rel') + '><div id=' + this.id + 'cont>' + (s || '') + '</div>' + _html_resize_sensor.call( this ) + '</div></div></div><div id=' +
					this.id + 'x class=f-scroll-x><div id=' + this.id + 'xtr class=f-scroll-x-track onmousedown=' + evw + '.scrollDragX(this,event)></div></div><div id=' +
					this.id + 'y class=f-scroll-y><div id=' + this.id + 'ytr class=f-scroll-y-track onmousedown=' + evw + '.scrollDragY(this,event)></div></div>';
			}
		},
		dispose: function() {
			clearInterval( this._scr_timer );
			_proto.dispose.call( this );
		}
	}
} ),
/* `AbsSection` */
AbsSection = define.widget( 'AbsSection', {
	Const: function( x, p, n ) {
		typeof x === _STR && (x = { src: x });
		W.call( this, x, p, n );
	},
	Prototype: {
		showLoading: $.rt(),
		showLayout: $.rt(),
		init_x: function( x ) {
			this._init_x( x );
		},
		getSrc: function() {
			var u = this._runtime_src; 
			if ( ! u ) {
				var t = this.x.template;
				if ( t ) {
					typeof t === _STR && (t = _getTemplate( t ));
					t && t.src && (u = t.src);
				}
				!u && (u = this.attr( 'src' ));
			}
			u && this.x.args && (u = this.formatStr( u, this.x.args, T ));
			return u;
		},
		getSrcFilter: function() {
			var f = this.x.on && this.x.on.filter;
			if ( f == N ) {
				var t = this.x.template;
				if ( t ) {
					typeof t === _STR && (t = _getTemplate( t ));
					t && this.isContentData( t ) && t.on && t.on.filter && (f = t.on.filter);
				}
			}
			return f;
		},
		getResult: function() {
			return this.x.result;
			//return this.x.template ? this.x.result : this._srcdata;
		},
		// @force: 强制刷新，不论是否在frame内
		load: function( tar, fn, force ) {
			if ( this.loading )
				return;
			this.showLoading();
			var s = this.getSrc();
			if ( force || this.isDisplay() ) {
				this.loadData( tar, function( x ) {
					if ( this.layout ) {
						this.showLoading( F );
						this.showLayout( tar );
						fn && !(x instanceof Error) && fn.call( this, x );
					} else if ( this.getSrc() && s != this.getSrc() ) {
						this.reload( N, N, N, fn );
					}
				} );
			}
		},
		loadData: function( tar, fn, cache ) {
			this.abort();
			this.loading = T;
			var u, m, n, o, t = this.x.template, self = this,
				d = this.type_view && _viewResources[ this.path ],
				e = function() {
					if ( ! self._disposed && m && n && o ) { self._loadEnd( n ); fn && fn.call( self, n ); n = N; }
				};
			if ( t && typeof t === _STR ) {
				t = _getTemplate( t );
				o = T; e();
			} else
				o = T;
			d ? $.require( d, function() { m = T; e(); } ) : (m = T);
			(u = this.getSrc()) ? this.exec( { type: 'Ajax', src: u, filter: this.x.filter || (t && t.filter), cache: cache, loading: F, sync: this.x.sync, success: function( x ) {
				if ( this._success( x ) ) {
					n = x; e();
				}
			}, error: function( a ) {
				if ( this._error( a ) ) {
					n = new Error( { text: Loc.ps( a.request.status > 600 ? Loc.internet_error : Loc.server_error, a.request.status ) } );
					e();
				}
			} } ) : (n = {}, e());
			cache && this.addEvent( 'unload', function() { $.ajaxClean( u ) } );
		},
		_success: function( x ) {
			var t = this.x.template && _getTemplate( this.x.template ), s = this.x.success || (t && t.success);
			s && this.formatJS( s, { '$response': x } );
			if ( this._disposed ) return F;
			s = this.x.complete || (t && t.complete);
			s && this.formatJS( s, { '$response': x } );
			if ( this._disposed ) return F;
			return T;
		},
		_error: function( x ) {
			var t = this.x.template && _getTemplate( this.x.template ), s = this.x.error || (t && t.error);
			s && this.formatJS( s, { '$response': N } );
			if ( this._disposed ) return F;
			s = this.x.complete || (t && t.complete);
			s && this.formatJS( s, { '$response': N } );
			if ( this._disposed ) return F;
			return T;
		},
		// @x -> data json
		_loadEnd: function( d ) {
			this.loading = F;
			this.loaded = T;
			this.init_template( d );
		},
		srcData: function( x, tar ) {
			if ( x ) {
				this._loadEnd( x );
				this.showLayout( tar );
			}
			return this._srcdata;
		},
		isContentData: function( x ) {
			return x.type === this.type;
		},
		reload: function( src, tpl, tar, fn ) {
			this._runtime_src = src;
			src && (this.x.src = src);
			tpl && (this.x.template = tpl);
			this.reset( tar );
			if ( this.$() ) {
				var s = this.getSrc();
				typeof s === _STR ? this.load( tar, fn, T ) : this.srcData( s || {}, tar );
			} else {
				this.show();
				! this.loading && this.load( tar, fn, T );
			}
		},
		template: function( tpl, tar, fn ) {
			this.reload( N, tpl, tar, fn );
		},
		abort: function() {
			$.ajaxAbort( this );
		},
		reset: function( tar ) {
			this.abort();
			this.loaded = this.loading = F;
			if ( ! tar ) {
				this.empty();
				this.layout = N;
			}
		}
		
	}
} ),
/* `section`
 * 支持模板的widget实现顺序
 * 一、如果有node(s)，直接展示node(s)。因为node是最终结果
 * 二、有src，没有preload。这个src应当返回当前widget格式的JSON
 * 三、有src，也有preload，那么src应当返回配合preload的JSON
 *
 * src 的字符格式，可以是URL地址，或是 javascript: 开头的js语句(执行后返回URL字串或数据JSON)
 * src 的JSON格式，数据JSON，和字串格式的src返回的内容相同
 * template 的字串格式，是模板的ID。如果前端缓存没有找到模板，会以这个ID为路径去服务器获取
 * template 的JSON格式，即模板内容。
 */
Section = define.widget( 'Section', {
	Extend: AbsSection,
	Listener: {
		body: {
			ready: function() {
				this.domready = T;
				this.layout ? setTimeout( $.proxy( this, 'this.trigger("load")' ) ) : this.start( F );
			},
			display: function() {
				this.domready && this.start();
			}
		}
	},
	Prototype: {
		isSrcLayout: T,
		_x_ini: F,
		// @implement
		init_x: function( x ) {
			this.x = x;
			if ( ! x.node && ! this.isDisplay() )
				return;
			_proto._init_x.call( this, x );
			var t = x.preload && _getPreload( x.preload );
			if ( t ) {
				for ( var k in t ) {
					!(k in x) && k !== 'node' && k !== 'nodes' && (x[ k ] = t[ k ]);
				}
				if ( x.node ) {
					var n = _compilePreload( x.preload, x.node );
					n && $.merge( x, n );
				}
			}
			if ( this.domready && this.x.id ) {
				this.parent ? _setParent.call( this, this.parent ) : _setView.call( this, this.ownerView );
			}
			if ( ! x.node ) {
				var s = this.getSrc() || (this.x.template && ! this.hasCssRes() && {});
				if ( s && typeof s === _OBJ ) {
					this._loadEnd( s );
				} else
					this.className += ' z-loading';
			}
			this._x_ini = T;
		},
		// @implement
		init_nodes: function() {
			if ( (this.x.node || this.x.nodes) && ! this.layout )
				this.layout = new Layout( this.x.node ? { node: this.x.node } : { nodes: this.x.nodes }, this );
		},
		// 限制ready只触发一次。在frame中的view，layout调用render方法时会再次触发ready。理论上应该在_proto上做此限制，但出于性能考虑，目前做在这里
		triggerAll1: function( e ) {
			if ( e === 'ready' && this.layout )
				return;
			_proto.triggerAll.apply( this, arguments );
		},
		hasCssRes: function() {
			var a = this.type_view && _viewResources[ this.path ];
			if ( a ) {
				for ( var i = 0; i < a.length; i ++ ) {
					if ( 'css' === $.strFrom( $.strTo( a[ i ], '?' ) || a[ i ], '.', T ) )
						return T;
				}
			}
			return F;
		},
		// init初始化没有layout的情况下，再次开始寻求加载layout
		start: function( re ) {
			if ( !this.x.src && !this.x.template )
				return;
			if ( ! this._x_ini ) {
				this.init_x( this.x );
				this.repaint();
				this.layout && this.showLayout( N, re );
			}
			! this.layout && this.isDisplay() && this.load();
		},
		_getReplaceTargets: function( x, tar, r ) {
			! r && (r = []);
			if ( x.id && $.idsAny( tar, x.id ) )
				r.push( x );
			else if ( x.node )
				this._getReplaceTargets( x.node, tar, r );
			else if ( x.nodes ) {
				for ( var i = 0, l = x.nodes.length; i < l; i ++ )
					this._getReplaceTargets( x.nodes[ i ], tar, r );
			}
			return r;
		},
		// tar -> 目标ID, re -> ready event?
		showLayout: function( tar, re ) {
			if ( tar ) {
				for ( var i = 0, b = this._getReplaceTargets( this.x.node, tar ); i < b.length; i ++ )
					_view( this ).find( b[ i ].id ).replace( b[ i ] );
			} else if ( this.layout ) {
				this.showLoading( F );
				this.layout._render();
				re !== F && this.layout.triggerAll( 'ready' );
				this.trigger( 'load' );
			}
			this.removeClass( 'z-loading' );
		},
		// @a -> close?
		showLoading: function( a ) {
			if ( a === F ) {
				this.removeElem( 'loading' );
				this.exec( { type: 'Loading', hide: T } );
			} else {
				if ( this.x.loading || this.layout ) {
					this.exec( $.extend( this.x.loading || {}, { type: 'Loading' } ) );
				} else {
					! this.$( 'loading' ) && $.append( this.$(), this.html_loading() );
				}
			}
		},
		show: function() {
			return this.render();
		},
		html_loading: function() {
			return '<div class="w-view-loading" id=' + this.id + 'loading><i class=f-vi></i><cite class=_c>' + $.image( '%img%/loading-cir.gif' ) + ' <em class=_t>' + Loc.loading + '</em></cite></div>';
		}
	}
} ),
_initView = function() {
	this.widgets = {};
	this.names   = {};
	this.views   = {};
	this.initTargets = {};
	this.layout  = N;
},
_setPath = function( p, x ) {
	this.path = (p === _docView ? '' : p.path) + '/' + ((x || this.x).id || $.uid( this ));
	_viewCache[ this.path ] = this;
},
_setParent = function( a ) {
	this.parent && _setPath.call( this, a );
	this.parent = a;
	this.x && _regIdName.call( this, a );
},
_viewResources = cfg.viewResources || {},
/* `layout` 用于连接父节点和可装载的子节点 */
Layout = define.widget( 'Layout', {
	Const: function( x, p ) {
		this.rootNode = p;
		p.x.bind && (this.bind = p.x.bind);
		W.apply( this, arguments );
	},
	Prototype: {
		x_childtype: function( t ) {
			return this.parentNode.x_childtype ? this.parentNode.x_childtype( t ) : t;
		}
	}
} ),
/* `view` */
View = define.widget( 'View', {
	Const: function( x, p ) {
		var r = (p && _view( p )) || _docView;
		_initView.call( this );
		_setPath.call( this, r, x );
		_setParent.call( this, r );
		_regWidget.apply( this, arguments );
		_regIdName.call( this, r );
		p && p.bind && (this.bind = p.bind);
		this.init_nodes();
		this._instanced = T;

	},
	Extend: Section,
	Prototype: {
		className: 'w-view',
		type_view: T,
		// @implement
		init_x: function( x ) {
			Section.prototype.init_x.call( this, x );
			delete this.__width; delete this.__height;
		},
		reset: function( tar ) {
			Section.prototype.reset.apply( this, arguments );
			if ( ! tar ) {
				_initView.call( this );
				if ( this.x ) {
					delete this.x.on; delete this.x.node;
				}
			}
		},
		reload: function( src, tpl, tar, fn ) {
			// 兼容3.1业务的处理: 执行vm.reload()时，先判断一下如果是dialog的contentView，则让dialog刷新
			var d = $.dialog( this );
			if ( d && d.getContentView() === this ) {
				d.reload.apply( d, arguments );
			} else
				Section.prototype.reload.apply( this, arguments );
		},
		closestData: function( a ) {
			return this.data( a );
		},
		// 根据ID获取wg /@a -> id
		find: function( a ) {
			return this.widgets[ a ] || this.views[ a ];
		},
		// 返回数组 /@a -> id
		findAll: function( a ) {
			if ( typeof a === _STR )
				a = a.split( ',' );
			for ( var i = 0, c, r = []; i < a.length; i ++ )
				(c = this.find( a[ i ] )) && r.push( c );
			return r;
		},
		// 获取表单 /@a -> name, b -> range?(elem|widget)
		f: function( a, b ) {
			return this.fAll( a, b )[ 0 ];
		},
		// 读/写表单值 /@a -> name, b -> value, c -> text
		fv: function( a, b, c ) {
			if ( a = this.f( a ) )
				b === U ? (b = a.groupVal ? a.groupVal() : a.val()) : a.groupVal ? a.groupVal( b, c ) : a.val( b, c );
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
				if ( r.length > 1 ) { // 表单如果移动了位置，view.names里面的顺序不会跟着变，所以当同名表单大于一个时，通过jquery来获取
					for ( var i = 0, d = Q( 'input[name="' + r[ 0 ].input_name() + '"]', c ? (c.isWidget ? c.$() : c) : this.$() ), l = d.length, e; i < l; i ++ )
						(e = d[ i ].id) && (e = _getWidgetById( e )) && f.push( e );
				} else {
					r[ 0 ] && (!c || c.contains( r[ 0 ].$() )) && f.push( r[ 0 ] );
				}
			}
			return f;
		},
		// 获取所有表单，返回一个jQuery集合 /@ a -> range(widgetID, 可选，如果 range 以 ! 开头，表示排除), b -> 是否包含disabled的表单
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
			r && r.not( '.f-form-hide :input' );
			return r || [];
		},
		// 获取提交数据 { name: value, ... } /@ a -> range [widgetID, 可选，如果 range 以 ! 开头，表示排除], b -> json mode?
		getPostData: function( a, b ) {
			var f = this.getFormList( a ), r = b ? {} : [];
			for ( var i = 0, l = f.length; i < l; i ++ )
				_f_val( f[ i ], b, r );
			return b ? r : r.join( '&' );
		},
		// 判断表单是否更改  / @a -> range, b -> original?(设置为true，检测表单是否有修改，对照的值为初始值)
		isModified: function( a, b ) {
			if ( this.$() ) {
				for ( var i = 0, c, q = this.getFormList( a ), l = q.length; i < l; i ++ ) {
					if ( (c = _getWidgetById( q[ i ].id )) && c.isModified && c.isModified( b ) )
						return c;
				}
			}
			return F;
		},
		// 保存表单所做的更改  / @a -> range, b -> original?(设置为true，修改初始值)
		saveModified: function( a, b ) {
			if ( this.$() ) {
				for ( var i = 0, c, q = this.getFormList( a ), l = q.length; i < l; i ++ ) {
					(c = _getWidgetById( q[ i ].id )) && c.saveModified && c.saveModified( b );
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
			if ( this.layout ) {
				var q = this.getFormList( g );
				for ( var i = 0, l = q.length, c, v; i < l; i ++ ) {
					if ( (c = _getWidgetById( q[ i ].id )) && (v = c.trigger( 'valid', [ n ] )) )
						(e || (e = {}))[ v.wid ] = v;
				}
			}
			return e;
		},
		// 表单验证。验证通过返回true /@ n -> validategroup, g -> validaterange, f -> validateeffect
		// valid 用户事件返回值的处理: on: { valid: '***' }
		//   false -> 表示有错，并停止执行submit命令 (跳过引擎对当前widget的验证)
		//   "错误信息" -> 提示信息 (跳过引擎对当前widget的验证)
		//   null -> 没有错误，(继续引擎验证)
		valid: function( n, g, f ) {
			return this._valid( this.getValidError( n, g ), f ); 
		},
		_valid: function( e, f ) {
			this._cleanValidError();
			if ( e ) {
				var f = f || (cfg.validate && cfg.validate.effect) || 'red,tip', r = $.idsAny( f, 'red' ), a = $.idsAny( f, 'alert' ), h = $.idsAny( f, 'tip' ), m, n;
				for ( var k in e ) { 
					r && _all[ e[ k ].wid ].trigger( 'error' );
					n ? (m = T) : (n = e[ k ]);
				}
				if ( cfg.validate && cfg.validate.method ) {
					cfg.validate.method( e );
				} else if ( n ) {
					// fixme: 给第一个验证失败的表单加上提示，需要快速的选出"第一个"，目前用jquery查找，效率一般
					// 暂时的小优化：设置一个优化变量m(表示有多个)。当只有一个验证失败的表单时，不用去jquery查找
					if ( m ) {
						for ( var i = 0, q = Q( '.z-err', this.$() ), l = q.length, g; i < l; i ++ )
							if ( (g = q[ i ].id) && e[ g ] ) { n = e[ g ]; break; }
					}
					var o = _all[ n.wid ];
					_scrollIntoView( o, N, N, T );
					a && o.trigger( 'error', [{ type: 'Alert', text: n.text, id: $.alert_id }] );
					h && o.trigger( 'error', [ o.validTip( n.text ) ] );
				}
			}
			return !(this._err_ns = e); 
		},
		_cleanValidError: function() {
			var e = this._err_ns, k, n;
			for ( k in e )
				(n = _all[ e[ k ].wid ]) && n.trigger( 'error', F );
			_inst_hide( 'Tip' );
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
/* `DocView` *
 *  引擎初始化后会创建一个顶级的DocView。这个view不是可见元素，但统领所有view和widget，类似于window.document
 *  只提供 find, cmd 等方法
 */
DocView = define.widget( 'DocView', {
	Const: function() {
		_viewCache[ this.path ] = this;
		_initView.call( this );
		this._wd = this.width();
		this._ht = this.height();
		var self = this, f;
		$.attach( window, 'resize', f = function( e, w, h ) {
			w = w || self.width(); h = h || self.height();
			if ( self._wd !== w || self._ht !== h ) {
				Dialog.cleanPop();
				self.resize( self._wd = w, self._ht = h );
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
	Extend: View,
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

// `HorzScale`: 子元素被约束为：横向排列，高度占满，宽度按子元素设置的width属性分配
HorzScale = define.widget( 'HorzScale', {
	Prototype: {
		type_horz: T,
		scaleWidth: _w_scale.width
	}
} ),
/* `horz`   可滚动的横向布局面板，子元素如果没有设置宽度，则宽度默认为-1  */
Horz = define.widget( 'Horz', {
	Const: function( x ) {
		x.br && (this.className += ' z-br');
		Scroll.apply( this, arguments );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'Hiddens', nodes: x.hiddens }, -1 );
		if ( _w_lay.width.call( this ) )
			this.addEvent( 'resize', _w_mix.width ).addEvent( 'ready', _w_mix.width );
	},
	Extend: [ Scroll, HorzScale ],
	Listener: {
		body: {
			nodeChange: _w_rsz_layout
		}
	},
	Prototype: {
		className: 'w-horz',
		childCls: 'f-sub-horz',
		scaleWidth: _w_scale.width,
		html_nodes: function() {
			var v = this.attr( 'vAlign' );
			v && (this.childCls += ' f-va-' + v);
			// @compat: 兼容模式下，horz的没有高度的子节点，加上z-htmn样式(float:left)。如果不使用float，子节点内容不会自动撑开
			// @fixme: 当widget的高度在-1和固定高度之间转变时，z-htmn也应该跟着增加或删除
			!br.css3 && !this.innerHeight() && (!this.x.align || this.x.align === 'left') && (this.childCls += ' z-htmn');
			var s = _proto.html_nodes.call( this ) + (this._hiddens ? this._hiddens.html() : ''),
				ih = ie7 && this.x.scroll && this.innerHeight(); // ie7在有scroll和vAlign时，f-vi的100%会包含底部滚动条的高度，导致撑开。需要设置负值的margin-bottom来抵消
			return v ? '<div id=' + this.id + 'vln class="w-horizontal-vln f-nv-' + v + '">' + s + '</div><i class=f-vi-' + v + (ih ? ' style="margin-bottom:-' + br.scroll + 'px"' : '') + '></i>' : s;
		}
	}
} ),
Horizontal = define.widget( 'Horizontal', {
	Extend: Horz,
	Prototype: {
		className: 'w-horizontal'
	}
} ),
// vert/scale: 子元素被约束为：纵向排列，宽度占满，高度按子元素设置的height属性分配
VertScale = define.widget( 'VertScale', {
	Prototype: {
		type_vert: T,
		scaleHeight: _w_scale.height
	}
} ),
/* `vert`  可滚动的纵向布局面板，子元素如果没有设置高度，则高度默认为-1  */
Vert = define.widget( 'Vert', {
	Const: function( x ) {
		Scroll.apply( this, arguments );
		if ( _w_lay.height.call( this ) )
			this.addEvent( 'resize', _w_mix.height ).addEvent( 'ready', _w_mix.height );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'Hiddens', nodes: x.hiddens }, -1 );
	},
	Extend: [ Scroll, VertScale ],
	Listener: {
		body: {
			nodeChange: _w_rsz_layout
		}
	},
	Prototype: {
		className: 'w-vert',
		childCls: 'f-sub-vert',
		scaleHeight: _w_scale.height,
		html_nodes: Horz.prototype.html_nodes
	}
} ),
Vertical = define.widget( 'Vertical', {
	Extend: Vert,
	Prototype: {
		className: 'w-vertical'
	}
} ),
/* `Frame`  子元素被约束为：高度宽度占满，只有一个可见  */
Frame = define.widget( 'Frame', {
	Const: function( x, p ) {
		W.apply( this, arguments );
	},
	Listener: {
		body: {
			ready: function() {
				if ( this.length ) {
					var d = this.focusNode;
					!d && (d = this.x.dft ? this.ownerView.find( this.x.dft ) : this[ 0 ]) && this.focus( d );
				}
			}
		}
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
			return a.x.display === T ? '' : 'f-hide';
		},
		getFocus: function() {
			return this.focusNode;
		},
		// @a -> wg,id
		// animate: scrollX(横向滚动),scrollY(纵向滚动),
		focus: function( a ) {
			var o = this.getFocus(),
				n = a.isWidget ? a : this.ownerView.find( a );
			if ( n && n !== o ) {
				if ( o )
					delete o.focusOwner;
				this.focusNode = n;
				n.focusOwner = this;
				if ( this.x.animate && o && this.$() ) {
					var d = n.nodeIndex > o.nodeIndex ? 'Left' : 'Right';
					n.display( T );
					$.animate( o.$(), 'fadeOut' + d, 100 );
					$.animate( n.$(), 'fadeIn' + d, 100, function() { o.display( F ); } );
				} else {
					o && o.display( F );
					n.display( T );
				}
			}
			return n;
		}
	}
} ),
/* `EmbedWindow`  有text就显示text，否则显示src  */
EmbedWindow = define.widget( 'EmbedWindow', {
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
			a && (this.x.src = a);
			Q( this.$() ).replaceWith( this.html() );
		},
		text: function( a ) {
			var d = this.getContentWindow().document;
			d.open();
			d.write( a );
			d.close();
		},
		html: function() {
			return '<iframe' + this.html_prop() + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + ' w-abbr="' + $.abbr + '" src="' + (this.attr( 'src' ) || 'about:blank') + '" scrolling=' + (this.x.scroll ? 'auto' : 'no') + ' marginwidth=0 marginheight=0 frameborder=0 allowtransparency></iframe>';
		}
	}
} ),
/* `Blank` 空面板  */
Blank = define.widget( 'Blank', { Prototype: { className: 'w-blank' } } ),
/* `html`
 *  支持自定义标签: <d:wg></d:wg>
 *  内容为widget配置项，例: <d:wg>{type: "button", text: "click"}</d:wg>
 */
Html = define.widget( 'Html', {
	Const: function( x, p ) {
		Scroll.apply( this, arguments );
		if ( this.x.thumbWidth ) {
			this.addEvent( 'ready',  this.thumb );
			this.addEvent( 'resize', this.thumb );
		}
		this.attr( 'align' ) && (this.property = ' align=' + this.attr( 'align' ));
		if ( ! x.vAlign && p && p.x.vAlign )
			this.defaults( { vAlign: p.x.vAlign } );
	},
	Extend: Scroll,
	Prototype: {
		className: 'w-html',
		attrSetter: function( k, v ) {
			_proto.attrSetter.apply( this, arguments );
			if ( k === 'text' )
				this.text( v );
		},
		// @a -> text
		text: function( a ) {
			if ( a == N )
				return this.x.text;
			this.x.text = a;
			var o = this.$( 'cont' ) || this.$();
			o && (o.innerHTML = this.html_nodes());
			this.trigger( 'resize' );
		},
		thumb: function() {
			this.x.thumbWidth && $.thumbnail( this.$(), this.scaleWidth( this, this.x.thumbWidth ) );
		},
		html_text: function() {
			var t = this.html_format( this.x.text, this.x.format, this.x.escape === T );
			if ( typeof t === _OBJ )
				t = this.add( t, -1 ).html();
			if ( ! br.css3 ) {
				if ( ! t && this.parentNode && this.parentNode.type_horz && ! this.height() ) {
					// ie7,8 没有高度的html面板如果内容为空，即使有宽度也撑不开，所以补一个空格
					t = '&nbsp;';
				}
			}
			return t;
		},
		html_nodes: function() {
			var s = this.html_text(), v = this.attr( 'vAlign' );
			if ( v )
				s = '<i class=f-vi-' + v + '></i><span id=' + this.id + 'vln style="vertical-align:' + v + '">' + s + '</span>';
			return s;
		}
	}
} ),
/* `timeline` 时间轴 */
Timeline = define.widget( 'Timeline', {
	Extend: Scroll,
	Default: { scroll: T },
	Prototype: {
		className: 'w-timeline',
		x_childtype: $.rt( 'TimelineItem' ),
		prop_cls: function() {
			return Scroll.prototype.prop_cls.call( this ) + (this.x.align ? ' z-' + this.x.align : '');
		}
	}
} ),
/* `timelineitem` */
TimelineItem = define.widget( 'TimelineItem', {
	Default: { width: -1, height: -1 },
	Prototype: {
		className: 'w-timeline-item',
		ROOT_TYPE: 'Timeline',
		prop_cls: function() {
			var p = this.parentNode;
			return _proto.prop_cls.call( this ) + (this.nodeIndex === 0 ? ' z-first' : '') + (this.nodeIndex === p.length - 1 ? ' z-last' : '') +
				(p.x.align === 'center' ? ' z-' + (this.x.align || 'right') : '');
		},
		html_icon: function() {
			return '<div class=_i>' + (this.x.icon ? $.image( this.x.icon ) : '<div class="_cir f-inbl"></div>') + '<i class=f-vi></i></div>';
		},
		html_nodes: function() {
			return '<div class=_line></div>' + this.html_icon() + '<div class=_t>' + this.html_format() + '</div>';
		}
	}
} ),
_splitSize = function( a, b ) {
	return a && a[ a.parentNode.type_horz ? 'width' : 'height' ]( b );
},
/* `split`  可拖动调整大小的分隔条 */
Split = define.widget( 'Split', {
	Const: function( x ) {
		W.apply( this, arguments );
		x.movable && (this.className += ' z-movable');
	},
	Listener: {
		body: {
			ready: function() {
				$.classAdd( this.parentNode.$(), 'f-rel' );
				var w = this.width();
				if ( w <= 1 ) $.classAdd( this.$(), 'z-0' );
				this.x.range && this.isExpanded() && $.classAdd( this.$(), 'z-expanded' );
				ie7 && this.css( 'bg', 'backgroundColor', this.$().currentStyle.backgroundColor );
			},
			mouseOver: {
				occupy: T,
				method: function() { this.addClass( 'z-hv' ) }
			},
			mouseOut: {
				occupy: T,
				method: function() { this.removeClass( 'z-hv' ) }
			}
		}
	},
	Prototype: {
		className: 'w-split',
		// 拖动调整大小
		drag: function( a, e ) {
			if ( ! this.x.movable )
				return;
			var self = this, r = $.bcr( this.$() ), o = this.isExpanded(), p = this.prev(), n = this.next(), d = this.x.range.split( ',' ), j = _number( d[ 0 ] ), k = _number( d[ 1 ] ),
				th = this.parentNode.type_horz, t = this.x.hide === 'next', cln = th ? 'clientX' : 'clientY', pos = th ? 'left' : 'top', x = e[ cln ], b, c, f, g, h,
				down = function() {
					h = $.db( '<div style="position:absolute;top:0;bottom:0;left:0;right:0;z-index:1"></div>' );
					b = $.db( '<div style="position:absolute;width:' + (r.width) + 'px;height:' + (r.height) + 'px;left:' + r.left + 'px;top:' + r.top + 'px;background:#bbb;opacity:.6;z-index:1"></div>' );
				};
			//$.stop( e );
			down();
			$.moveup( function( e ) {
				if ( e[ cln ] !== x ) {
					f = p && (p.isDisplay() ? _splitSize( p ) : 0), g = n && (n.isDisplay() ? _splitSize( n ) : 0);
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
						self._size = t ? k : j;
						self.toggle( t ? k : j );
					}
				}
			} );
		},
		// a -> bool/数字/百分比/*
		toggle: function( a ) {
			var o = this.isExpanded(), n, m = this.getMajorMin(), v = a;
			if ( a == N ) {
				n = ! o;
				v = o ? m : this._size;
			} else if ( typeof a === _BOL ) {
				n = a;
				v = a ? this._size : m;
			}
			this.majorSize( v );
			this.minorSize( '*' );
			if ( n === U )
				n = this.isExpanded();
			o != n && this.$( 'i' ) && $.replace( this.$( 'i' ), this.html_icon( n ) );
			$.classAdd( this.$(), 'z-expanded', n );
			this.trigger( 'mouseOut' );
		},
		getMajorMin: function() {
			return (this.x.range || '').split( ',' )[ this.x.hide === 'next' ? 1 : 0 ];
		},
		isExpanded: function() {
			if ( ! this.major().isDisplay() )
				return F;
			var v = this.majorSize();
			return v === U || v > this.getMajorMin();
		},
		major: function() {
			return this[ this.x.hide || 'prev' ]();
		},
		minor: function() {
			return this[ this.x.hide === 'next' ? 'prev' : 'next' ]();
		},
		majorSize: function( a ) {
			if ( a == 0 ) {
				return this.major().display( F );
			}
			arguments.length && this.major().display();
			return _splitSize( this.major(), a );
		},
		minorSize: function( a ) {
			return _splitSize( this.minor(), a );
		},
		html_icon: function( a ) {
			a = a == N ? this.isExpanded() : a;
			return $.image( (a && this.x.expandedIcon) || this.x.icon || this.x.expandedIcon, { id: this.id + 'i', cls: '_i _' + (this.x.hide || 'prev'), click: evw + '.toggle()' } );
		},
		html_nodes: function() {
			var w = this.width(), h = this.height(), p = this.parentNode, z = p.type_horz,
				s = '<div id=' + this.id + 'bg style="position:absolute;background:inherit;height:' + (z ? '100%' : h + 'px') + ';width:' + (z ? w + 'px' : '100%') + '">';
			if ( this.x.range && (z || p.type_vert) && this.next() && this.prev() ) {
				s += '<div onmousedown=' + evw + '.drag(this,event) style="position:absolute;' + (this.x.movable ? 'cursor:' + (z ? 'col' : 'row') + '-resize;' : '') + 'height:' + (z || h >= 5 ? '100%' : '5px') + ';width:' +
					(! z || w >= 5 ? '100%' : '5px') + ';margin-' + (z ? 'left' : 'top') + ':' + ( (z ? w : h) < 5 ? ((z ? w : h) - 5) / 2 : 0 ) + 'px;z-index:1;"></div>';
				this._size = _number( this.x.range.split( ',' )[ 2 ] ) || this.majorSize();
			}
			if ( this.x.icon || this.x.expandedIcon )
				s += this.html_icon();
			return s + '</div>';
		}
	}
} ),
_setBadge = function( b ) {
	if ( b === F ) {
		this._badge && this._badge.remove();
	} else if ( this._badge ) {
		this._badge = this._badge.replace( b === T ? {} : typeof b === _OBJ ? b : { text: b } );
	} else {
		Q( this.badgeContainer ? this.badgeContainer() : this.$() ).append( this.init_badge().html() );
	}
},
/* `badge`  红点 */
Badge = define.widget( 'Badge', {
	Default: { width: -1, height: -1 },
	Prototype: {
		className: 'w-badge f-inbl',
		prop_cls: function() {
			var p = _snapPosition( this.x.position );
			return _proto.prop_cls.call( this ) + (p ? ' z-position-' + p : '') + (this.x.text != N ? ' z-t' : '');
		},
		html_nodes: function() {
			return this.x.text != N ? this.x.text : '';
		},
		dispose: function() {
			if ( this.parentNode._badge == this )
				delete this.parentNode._badge;
			_proto.dispose.apply( this, arguments );
		}
	}
} ),
/* `buttonbar` */
ButtonBar = define.widget( 'ButtonBar', {
	Const: function( x, p ) {
		this.className += ' z-dir' + (x.dir || 'h');
		x.br && (this.className += ' z-br');
		!this.length && (this.className += ' z-empty');
		W.apply( this, arguments );
		this.attr( 'align' ) && (this.property = ' align=' + this.attr( 'align' ));
		(!x.vAlign && p && p.x.vAlign) && this.defaults( { vAlign: p.x.vAlign } );
		this.x.overflow && this.defaults( { scroll: F } );
	},
	Extend: Horz,
	Default: { vAlign: 'middle', scroll: T },
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, Horz, e );
				this.isScrollable() && this.getFocus() && _scrollIntoView( this.getFocus() );
				this.x.overflow && this.overflow();
			},
			resize: function( e, f ) {
				_superTrigger( this, Horz, e );
				! f && this.x.overflow && this.overflow();
				var h = this.innerHeight();
				this.$( 'vi' ) && this.css( 'vi', 'height', h == N ? '' : h );
			},
			nodeChange: function( e ) {
				_superTrigger( this, Horz, e );
				Q( '.w-button', this.$() ).removeClass( 'z-last z-first' ).first().addClass( 'z-first' ).end().last().addClass( 'z-last' );
				Q( '.w-button-split', this.$() ).next().addClass( 'z-first' ).end().prev().addClass( 'z-last' );
				this.$( 'vi' ) && ! this.length && $.remove( this.$( 'vi' ) );
				! this.$( 'vi' ) && this.length && this.x.br !== T && Q( this.$( 'cont' ) || this.$() ).prepend( this.html_vi() );
				if ( this.x.space > 0 ) {
					var c = 'margin-' + (this.x.dir === 'v' ? 'bottom' : 'right');
					Q( '.w-button', this.$() ).css( c, this.x.space + 'px' ).last().css( c, 0 );
				}
				if ( this.x.overflow ) {
					this.overflow();
					var f = this.getFocus();
					f && f.focusOver();
				}
				this.fixLine();
				this.addClass( 'z-empty', !this.length );
			}
		}
	},
	Prototype: {
		className: 'w-buttonbar',
		childCls: '',
		x_childtype: function( t ) {
			return t === 'Split' ? 'ButtonSplit' : (t || 'Button');
		},
		scaleWidth: function() {
			return (this.x.dir === 'v' || this.x.nobr === F ? _proto.scaleWidth : _w_scale.width).apply( this, arguments );
		},
		// @a -> name
		getFocus: function( a ) {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].isFocus && this[ i ].isFocus() && (! a || a === this[ i ].x.name) ) return this[ i ];
		},
		getFocusAll: function( a ) {
			for ( var i = 0, r = []; i < this.length; i ++ )
				if ( this[ i ].isFocus && this[ i ].isFocus() && (! a || a === this[ i ].x.name) ) r.push( this[ i ] );
			return r;
		},
		getLocked: function() {
			for ( var i = 0; i < this.length; i ++ )
				if ( this[ i ].isLocked() ) return this[ i ];
		},
		fixLine: function() {
			if ( this.$( 'vi' ) ) Q( this.$( 'cont' ) || this.$() ).prepend( this.$( 'vi' ) );
			Q( this.$() ).append( Q( '.w-' + this.type.toLowerCase() + '-line', this.$() ) );
		},
		overflow: function() {
			if ( this._more ) {
				this._more.remove();
				delete this._more;
				for ( var i = 0; i < this.length; i ++ )
					this[ i ].css( { visibility: '' } );
			}
			// 调整页面百分比时，scrollWidth可能会比offsetWidth大一个像素。这里加一个像素容错
			var tw = this.$().offsetWidth + 1, o = this.x.overflow;
			if ( this.$().scrollWidth > tw ) {
				var t = $.extend( { type: 'Button', focusable: F, closable: F, on: { click: '' } }, this.x.pub || {}, o.button );
				t.text == N && t.icon == N && (t.text = Loc.more);
				this._more = this.add( t, -1 );
				this._more.render( this.$() );
				var w = this._more.width() + _number( this.x.space );
				for ( var i = 0, j; i < this.length; i ++ ) {
					j = this[ i ].width();
					if ( w + j > tw ) break;
					w += j;
				}
				$.before( this[ i ].$(), this._more.$() );
				var m = this._more.setMore( { nodes: [] } ), self = this;
				for ( ; i < this.length; i ++ ) {
					m.add( $.extend( { cls: '', focus: F, closable: this[ i ].x.closable, on: {
						ready: 'this.addClass("z-on",!!' + abbr( this[ i ] ) + '.isFocus())',
						click: 'var b=this.getCommander().parentNode;' + abbr( this[ i ] ) + '.click();b.overflow()',
						close: 'var o=' + $.abbr + '.all["' + this[ i ].id + '"],p=o.parentNode;o.close();p._more&&p._more.drop()'
					} , text: this[ i ].x.text, nodes: this[ i ].x.nodes } ) );
					this[ i ].css( { visibility: 'hidden' } );
				}
			}
		},
		html_vi: function() {
			return '<i id=' + this.id + 'vi class=f-vi-' + this.attr( 'vAlign' ) + (this.attr('scroll') === T && this.innerHeight() ? ' style="height:' + this.innerHeight() + 'px;"' : '') + '></i>';
		},
		html_nodes: function() {
			for ( var i = 0, l = this.length, s = []; i < l; i ++ ) {
				s.push( this[ i ].html() );
				if ( this.x.split && i < l - 1 && this[ i ].type !== 'ButtonSplit' && this[ i + 1 ].type !== 'ButtonSplit' ) {
					s.push( this.add( $.extend( { type: 'Split' }, this.x.split ), i + 1 ).html() );
					i ++, l ++;
				}
			}
			s = s.join( '' );
			// ie7下如果既有滚动条又有垂直对齐，按钮会发生位置偏移
			var f = (ie7 && this.isScrollable()) || ! this.length || this.x.br === T ? '' : this.html_vi(), v = this.attr( 'vAlign' );
			return (v ? f + (this.x.dir === 'v' ? '<div id=' + this.id + 'vln class="f-nv-' + v + '">' + s + '</div>' : s) : s) + '<div class=w-' + this.type.toLowerCase() + '-line></div>';
		}
	}
} ),
/* `button` */
Button = define.widget( 'Button', {
	Const: function( x ) {
		x.nodes && !x.nodes.length && (x.nodes = N);
		W.apply( this, arguments );
		x.target && x.focus && _regTarget.call( this, this._ustag );
		x.badge && this.init_badge();
	},
	Listener: {
		// 在触发事件之前做判断，如果返回true，则停止执行事件(包括系统事件和用户事件)
		block: function( e ) {
			var t = e.type || e;
			return t !== 'unlock' && t !== 'remove' && t !== 'focus' && t !== 'blur' && ! this.usa();
		},
		body: {
			ready: function() {
				this.isFocus() && this.focus();
			},
			mouseOver: {
				occupy: T,
				method: function( e ) {
					$.classAdd( $( e.elemId || this.id ), 'z-hv' );
					var m = _inst_get( 'Menu' );
					if ( this.type === 'MenuButton' ) {
						this.show();
					} else if ( (this.x.hoverDrop && this.more) || (m && this.more && this.more !== m && this.more.type === 'Menu' && m.parentNode.parentNode === this.parentNode) ) {
						this.drop();
					} else if ( m && ! this.more && m.parentNode.parentNode === this.parentNode )
						m.hide();
				}
			},
			mouseOut: {
				occupy: T,
				method: function( e ) {
					$.classRemove( $( e.elemId || this.id ), 'z-hv' );
				}
			},
			mouseDown: {
				occupy: T,
				method: function( e ) {
					$.classAdd( $( e.elemId || this.id ), 'z-dn' );
				}
			},
			mouseUp: {
				occupy: T,
				method: function( e ) {
					$.classRemove( $( e.elemId || this.id ), 'z-dn' );
				}
			},
			click: function( e ) {
				this.isToggleable() ? this.toggleFocus() : this.focus();
				if ( !(this.x.on && this.x.on.click) )
					this.drop();
			},
			lock: function() {
				if ( ! this._locked ) {
					if ( this.$() ) {
						$.classAdd( this.$(), 'z-lock' );
						var c = Q( this.$( 'c' ) ), d = c.find( '._i' ),
							e = Q( '<i class=_ld></i>' ).appendTo( d.length ? d : c );
						d.length && e.css( 'right', d.css( 'padding-right' ) );
					}
				}
				this._locked = T;
			},
			unlock: function() {
				this._locked = F;
				if ( this.$() ) {
					Q( '._ld', this.$() ).remove();
					$.classRemove( this.$(), 'z-lock' );
					Q( '.z-hv', this.$() ).add( this.$() ).removeClass( 'z-hv' );
				}
			},
			close: function() {
				if ( this.x.target ) {
					var p = this.parentNode, f = p.type === this.ROOT_TYPE && p.getFocus(), n = f && f == this && (this.next() || this.prev());
					n && n.click();
					for ( var i = 0, b = this.ownerView.find( this.x.target.split( ',' ) ); i < b.length; i ++ )
						b[ i ].remove();
				}
			}
		}
	},
	Default: { width: -1, height: -1 },
	Prototype: {
		ROOT_TYPE: 'ButtonBar',
		className: 'w-button',
		_menu_snapType: 'v',
		_menu_type: 'Menu',
		// @implement
		init_nodes: function() {
			this.setMore( this.x );
		},
		prepend: function( a ) {
			!$.isArray( a ) && (a = [ a ]);
			!this.x.nodes && (this.x.nodes = []);
			A.unshift.apply( this.x.nodes, a );
			this.init_nodes();
			this.render();
		},
		append: function( a ) {
			!$.isArray( a ) && (a = [ a ]);
			!this.x.nodes && (this.x.nodes = []);
			A.push.apply( this.x.nodes, a );
			this.init_nodes();
			this.render();
		},
		// @implement
		insertHTML: function( a, b ) {
			this.$() && $[ _putin[ b ] ? 'after' : b ]( this.$(), a.isWidget ? a.$() : a );
		},
		init_badge: function() {
			return (this._badge = new Badge( this.x.badge === T ? {} : typeof this.x.badge === _OBJ ? this.x.badge : { text: this.x.badge }, this, -1 ));
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
				if ( b != '' && b != N )
					this.$( 't' ) ? Q( 'em', this.$( 't' ) ).html( b ) : $.append( this.$( 'c' ), this.html_text( b ) );
				else
					this.removeElem( 't' );
			} else if ( a === 'status' ) {
				this.status( a, b );
			} else if ( a === 'tip' ) {
				this.$() && (this.$().title = $.strQuot( (b === T ? this.x.text : b) || '' ));
			} else if ( a === 'more' ) {
				this.setMore( b );
			} else if ( a === 'hoverDrop' ) {
				this.more && this.more.attr( 'hoverDrop', b );
			} else if ( a === 'badge' ) {
				_setBadge.call( this, b );
			}
		},
		badgeContainer: function() {
			return this.$( this.isIconOnly() ? '' : 't' );
		},
		setMore: function( x ) {
			var f;
			if ( this.more ) {
				f = this.more.isShow();
				this.more.close();
				delete this.more;
			}
			if ( x != N ) {
				if ( x.type && _cmdHooks[ x.type ] )
					x = { more: x };
				if ( x.more || x.nodes ) {
					this.more = this.add( x.more || { type: this._menu_type, nodes: x.nodes }, -1, { snap: { target: this, position: this._menu_snapType, indent: 1 }, memory: T, line: true, autoHide: T, hoverDrop: x.hoverDrop || this.x.hoverDrop } );
				}
			}
			this._combo = this.more && x.on && x.on.click;
			f && this.more && this.more.show( 0 );
			return this.more;
		},
		getMore: function() {
			return this.more;
		},
		usa: function() {
			return ! this.isDisabled() && ! this._locked;
		},
		// 给下拉 dialog 用的接口
		dropperCls: function( a ) {
			if ( a && a === this.more ) {
				var b = this.x.cls && $.strTrim( this.x.cls );
				if ( b ) {
					for ( var i = 0, c = b.split( /\s+/ ), d = []; i < c.length; i ++ )
						d.push( c[ i ] + '-' + this.more.type );
					return d.join( ' ' );
				}
			}
		},
		_ustag: function() {
			for ( var i = 0, b = this.ownerView.findAll( this.x.target ); i < b.length; i ++ ) {
				b[ i ].parentNode.type_frame && b[ i ].parentNode.focus( b[ i ] );
			}
		},
		isToggleable: function() {
			return this.rootNode && this.rootNode.x.focusMultiple;
		},
		isFocus: function() {
			return ! this.isDisabled() && this.x.focusable && this.x.focus;
		},
		focus: function( a ) {
			if ( this._disposed )
				return;
			var f = !!this.x.focus;
			if ( this._focus( a ) !== f ) {
				this.focusOver();
				this.parentNode.trigger( 'change' );
			}
		},
		_focus: function( a ) {
			if ( this._disposed )
				return;
			var a = a == N || !!a, m = this.rootNode && this.rootNode.x.focusMultiple;
			this.x.focus = a;
			if ( this.x.focusable && this.$() ) {
				$.classAdd( this.$(), 'z-on', a );
				if ( a ) {
					if ( ! m ) {
						for ( var i = 0, d = this.x.name ? this.ownerView.names[ this.x.name ] : this.parentNode; i < d.length; i ++ )
							if ( d[ i ] !== this && d[ i ].type === this.type && d[ i ].x.name == this.x.name && d[ i ].x.focusable && d[ i ].x.focus ) { d[ i ]._focus( F ); }
					}
				}
				a && this.x.target && this._ustag();
				this.trigger( a ? 'focus' : 'blur' );
			}
			return this.x.focus;
		},
		focusOver: function() {
			if ( this.x.focus ) {
				var p = this.parentNode, o = p.x.overflow;
				if ( o && o.effect === 'swap' && p._more ) {
					var q = Q( p._more.$() ).prev( '.w-button' )[ 0 ], v = q && _widget( q );
					if ( v && v.nodeIndex < this.nodeIndex ) {
						v.swap( this );
						p.overflow();
					}
				}
			}
		},
		toggleFocus: function() {
			this.focus( ! this.isFocus() );
		},
		lock: function( a ) {
			this.trigger( a === F ? 'unlock' : 'lock' );
		},
		isLocked: function() {
			return this._locked;
		},
		// 新增或更换图标。如果 a == '', 则删除图标  / @a -> image src
		icon: function( a ) {
			return this.attr( 'icon', a );
		},
		isIconOnly: function() {
			return this.x.text == N && ! this.x.format && this.x.icon;
		},
		// 文本
		text: function( a ) {
			return this.attr( 'text', a );
		},
		// 徽标
		badge: function( a ) {
			return this.attr( 'badge', a );
		},
		// @a -> delay millisenconds?
		drop: function( a ) {
			if ( this.usa() && this.more )
				this.more.show( a );
		},
		close: function( e ) {
			e && $.stop( e );
			if ( ! this._disposed && F !== this.trigger( 'close' ) )
				this.remove();
		},
		html_icon: function( a ) {
			return this.x.icon ? $.image( a || this.x.icon, { id: this.id + 'i', cls: '_i f-inbl', width: this.x.iconWidth, height: this.x.iconHeight } ) : '';
		},
		html_text: function( a ) {
			return '<div id=' + this.id + 't class="_t f-omit"' + (this.x.textstyle ? ' style="' + this.x.textstyle + '"' : '') + '><em class="_s f-omit">' + this.html_format( a || this.x.text ) +
				'</em>' + (this._badge ? this._badge.html() : '') + '<i class=f-vi></i></div>';
		},
		html: function() {
			var x = this.x, p = this.parentNode, g = this.tagName || 'div', w = this.innerWidth(),
				a = '<' + g + ' id=' + this.id + ' class="', b = this.prop_cls(), c = this._combo, d, s = '', t = x.text == N ? '' : ('' + x.text),
				io = this.isIconOnly();
			b += x.noToggle ? ' z-normal' : c ? ' z-combo' : this.more ? ' z-more' : ' z-normal';
			if ( x.closable || x.closeicon )
				b += ' z-x';
			if ( io )
				b += ' z-i';
			if ( w != N ) {
				s += 'width:' + w + 'px;';
			}
			if ( x.focus && x.focusable )
				b += ' z-on';
			if ( p.type === this.ROOT_TYPE ) {
				if ( this === p[ 0 ] || ((d = this.prev()) && d.type === 'ButtonSplit') )
					b += ' z-first';
				if ( this === p[ p.length - 1 ] || ((d = this.next()) && d.type === 'ButtonSplit') )
					b += ' z-last';
			}
			a += b + '"';
			if ( this.attr( 'height' ) != N ) {
				if ( (d = this.innerHeight()) != N )
					s += 'height:' + d + 'px;';
			}
			if ( this.x.minWidth && (v = this.minWidth()) )
				s += 'min-width:' + v + 'px;';
			if ( this.x.maxWidth && (v = this.maxWidth()) )
				s += 'max-width:' + v + 'px;';
			if ( this.x.minHeight && (v = this.minHeight()) )
				s += 'min-height:' + v + 'px;';
			if ( this.x.maxHeight && (v = this.maxHeight()) )
				s += 'max-height:' + v + 'px;';
			if ( (d = p.x.space) != N ) {
				if ( this !== p[ p.length - 1 ] )
					s += 'margin-' + ( p.x.dir === 'v' ? 'bottom' : 'right' ) + ':' + d + 'px;';
				if ( p.x.br )
					s += 'margin-bottom:' + d + 'px;';
			}
			if ( x.style )
				s += x.style;
			if ( s )
				a += ' style="' + s + '"';
			if ( x.target )
				a += ' w-target="' + ((x.target.x && x.target.x.id) || x.target) + '"';
			a += c ? ' onmouseover=' + eve + ' onmouseout=' + eve : _html_on.call( this, ' onclick=' + eve );
			if ( x.tip )
				a += this.prop_title();
			x.id && (a += ' w-id="' + x.id + '"');
			if ( this.property )
				a += this.property;
			a += ' w-type="' + this.type + '">' + this.html_prepend();
			if ( ie7 && !w )
				a += '<table cellpadding=0 cellspacing=0 height=100%><tr height=100%><td>';
			if ( ! x.noToggle && this.more )
				a += '<div class=_m id=' + this.id + 'm' + ( c ? _html_on.call( this, ' onclick=' + evw + '.drop()' ) : '' ) + '><i class=f-arw></i><i class=f-vi></i></div>';
			if ( x.closeicon )
				a += $.image( x.closeicon, { cls: '_x', click: evw + '.close()' } );
			else if ( x.closable )
				a += '<div class=_x onclick=' + evw + '.close(event)><i class=f-vi></i><i class="_xi">&times;</i></div>';
			a += '<div class=_c id=' + this.id + 'c' + (c ? _html_on.call( this, ' onclick=' + eve ) : '' ) + '>';
			a += this.html_icon();
			if ( t || this.x.format )
				a += this.html_text();
			a += '</div>' + this.html_append() + (ie7 && !w ? '</table>' : '') + (io && this._badge ? this._badge.html() : '') + '</' + g + '>';
			return this.html_before() + a + this.html_after();
		}
	}
}),
/* `submitbutton` */
SubmitButton = define.widget( 'SubmitButton', {
	Const: function( x, p ) {
		Button.apply( this, arguments );
		! this.ownerView.submitButton && (this.ownerView.submitButton = this);
	},
	Extend: Button,
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
MenuButton = define.widget( 'MenuButton', {
	Const: function() {
		Button.apply( this, arguments );
	},
	Extend: Button,
	Listener: {
		body: {
			ready: N,
			click: function() {
				if ( ! this.isDisabled() ) {
					this.x.target && this._ustag();
					this.rootNode.hide();
				}
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Menu',
		className: 'w-menu-button f-nobr',
		_menu_snapType: '',
		_menu_type: 'SubMenu',
		elemht: function() {
			if ( ! _menu_button_height ) {
				br.chdiv( 'w-menu-button', function() { _menu_button_height = this.offsetHeight || 28 } );
			}
			return _menu_button_height;
		},
		getCommander: function() {
			return this.rootNode.parentNode;
		},
		// @implement 右键菜单命点击后就会关闭，如果执行的是异步ajax命令，那么返回的命令不会执行。因此改变执行命令的context为menu的commander
		exec: function() {
			return _proto.exec.apply( this.getCommander(), arguments );
		},
		show: function() {
			_inst_hide( 'SubMenu', this.parentNode );
			if ( this.more ) {
				this.more.show();
				_inst_add( this.more, this.parentNode );
			}
		},
		hide: function() {
			this.more && this.more.hide();
		},
		close: function( e ) {
			e && $.stop( e );
			var p = this.parentNode;
			this.more && this.more.hide();
			this.trigger( 'close' );
			this.remove();
			//p.length ? p.render() : p.hide();
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
			if ( x.closable )
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
			a += _html_on.call( this, ' onclick=' + eve ) + '><div class=_c id=' + this.id + 'c>';
			a += $.image( x.icon, { cls: '_i', id: this.id + 'i' } );
			if ( x.text )
				a += '<span class="_t f-omit" id=' + this.id + 't><em>' + x.text + '</em><i class=f-vi></i></span>';
			a += '</div>';
			a += '<div class=_m>' + ( this.more ? $.arrow( 'r1' ) : '' ) + '<i class=f-vi></i></div>';
			if ( x.closable )
				a += '<div class=_x onclick=' + evw + '.close(event)><i class=f-vi></i><i class=_xi>&times;</i></div>';
			return a + '</div>';
		}
	}
} ),
MenuSubmitButton = define.widget( 'MenuSubmitButton', {
	Const: function() {
		SubmitButton.apply( this, arguments );
	},
	Extend: MenuButton,
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
ButtonSplit = define.widget( 'ButtonSplit', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		var p = this.parentNode,
			w = p.x.space;
		w && ! x.ownproperty && this.defaults( { width: w } );
		if ( p.x.br ) {
			this.defaults( { height: -1 } );
			this.cssText = 'margin-bottom:' + w + 'px;';
		}
	},
	Default: { width: -1 },
	Prototype: {
		className: 'w-button-split f-inbl',
		html_nodes: function() {
			return '<span class="f-va _vr">' + (this.x.text || '') + '</span><i class=f-vi></i>';
		}
	}
} ),
_tab_position = { top: 't', right: 'r', bottom: 'b', left: 'l' },
_tab_position_name = { t: 'top', r: 'right', b: 'bottom', l: 'left', c: 'center', m: 'middle' },
/* `tabs` */
Tabs = define.widget( 'Tabs', {
	Const: function( x, p ) {
		this.id = $.uid( this );
		this.className += ' z-position-' + this.getTabPositionName( x.position );
		var s = this.getTabPositionCode( x.position ), y = { type: s === 'r' || s === 'l' ? 'Horz' : 'Vert', width: '*', height: '*' }, b = [], c = [], d, e = this.getDefaultOption( x.cls );
		for ( var i = 0, n = x.nodes || []; i < n.length; i ++ ) {
			if ( ! Q.isPlainObject( n[ i ].target ) )
				n[ i ].target = { type: 'Blank' };
			if ( n[ i ].type === 'Split' ) {
				b.push( n[ i ] );
			} else {
				b.push( $.extend( { type: 'Tab', focusable: T }, n[ i ], x.pub, e && e.pub ) );
				c.push( n[ i ].target );
				b[ i ].target = c[ i ].id || (c[ i ].id = this.id + 'target' + i);
				!d && (d = b[ i ].focus && b[ i ]);
			}
		}
		!d && b[ 0 ] && ((d = b[ 0 ]).focus = T);
		var n = this.getTabPositionName( x.position ),
			r = { type: 'TabBar', cls: 'z-position-' + n, align: x.align, vAlign: x.vAlign || (y.type === 'Horz' ? 'top' : N), split: x.split, dir: y.type === 'Horz' ? 'v' : 'h', space: x.space, overflow: x.overflow, nodes: b };
		y.nodes = [ { type: 'Frame', cls: 'w-tabs-frame', width: '*', height: '*', dft: d && d.id, nodes: c } ];
		y.nodes[ s === 'b' || s === 'r' ? 'push' : 'unshift' ]( r );
		Vert.call( this, $.extend( { nodes:[ y ], scroll: F }, x ), p );
		this.buttonbar = this[ 0 ];
		this.frame = this[ 1 ];
	},
	Extend: Vert,
	Prototype: {
		className: 'w-tabs',
		getTabPositionCode: function( s ) {
			return s && (_tab_position[ s ] || s);
		},
		getTabPositionName: function( s ) {
			return (s && (_tab_position_name[ s ] || s)) || 'top';
		}
	}
} ),
/* `tab` */
TabBar = define.widget( 'TabBar', {
	Extend: ButtonBar,
	Prototype: {
		className: 'w-tabbar',
		x_childtype: $.rt( 'Tab' )
	}
} ),
/* `tab` */
Tab = define.widget( 'Tab', {
	Extend: Button,
	Prototype: {
		className: 'w-tab'
	}
} ),
/* `album` */
Album = define.widget( 'Album', {
	Const: function( x ) {
		Horz.apply( this, arguments );
		!this.length && $.classAdd (this, 'z-empty' );
	},
	Extend: Horz,
	Default: { scroll: T },
	Listener: {
		body: {
			nodeChange: function() {
				this.addClass( 'z-empty', !this.length );
			}
		}
	},
	Prototype: {
		className: 'w-album',
		x_childtype: $.rt( 'Img' ),
		scaleWidth: _proto.scaleWidth,
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
Img = define.widget( 'Img', {
	Const: function( x, p ) {
		x.face && (this.className += ' z-face-' + x.face);
		x.face && (this.className += ' z-on');
		W.apply( this, arguments );
		x.badge && this.init_badge();
		p.type === this.ROOT_TYPE && this.defaults( { width: -1, height: -1 } );
	},
	Listener: {
		body: {
			mouseOver: {
				occupy: T,
				method: function() {
					$.classAdd( this.$(), 'z-hv' );
				}
			},
			mouseOut: {
				occupy: T,
				method: function() {
					$.classRemove( this.$(), 'z-hv' );
				}
			},
			click: {
				occupy: T,
				block: function( e ) {
					var t = e.type || e;
					t === 'click' && this.x.badge != N && this.x.badge !== F && this.badge( F );
					return this.box && e.srcElement && e.srcElement.id === this.box.id + 't'
				},
				method: function() {
					this.x.focusable && this.focus( ! this.isFocus() );
				}
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Album',
		className: 'w-img',
		// @implement
		repaintSelf: _repaintSelfWithBox,
		width_minus: function() {
			return (p.x.space || 0);
		},
		init_badge: function() {
			return Button.prototype.init_badge.call( this );
		},
		attrSetter: function( a, b ) {
			if ( a === 'src' ) {
				this.$( 'i' ) && $.replace( this.$( 'i' ), this.html_img() );
			} else if ( a === 'text' || a === 'description' ) {
				this.$( 't' ) && $.replace( this.$( 't' ), this.html_text() );
			} else if ( a === 'badge' ) {
				_setBadge.call( this, b );
			}
		},
		isFocus: function() {
			return $.classAny( this.$(), 'z-on' );
		},
		focus: function( a, e ) {
			this._focus( a, e );
			this.box && this.box.click( a );
		},
		_focus: function( a, e ) {
			var a = a == N || a, p = this.parentNode, b = p && p.type === this.ROOT_TYPE && p.getFocus();
			$.classAdd( this.$(), 'z-on', a );
			a && b && b !== this && ! p.x.focusMultiple && b._focus( F );
		},
		toggleFocus: function() {
			this.focus( ! this.isFocus() );
		},
		badge: function( a ) {
			return this.attr( 'badge', a );
		},
		error: function() {
			this.addClass( 'z-err' );
		},
		imgLoad: function() {
			// @fixme: 父节点也是-1的情况
			if ( this.parentNode.type != this.ROOT_TYPE ) {
				var w = this.attr( 'width' );
				if ( w < 0 || w == N ) {
					this.parentNode.trigger( 'resize' );
				}
			}
			this.addClass( 'z-success' );
		},
		prop_style: function() {
			var t = this.cssText || '', v, c = this.parentNode.x.space;
			if ( (v = this.innerWidth()) != N )
				t += 'width:' + v + 'px;';
			if ( (v = this.innerHeight()) != N )
				t += 'height:' + v + 'px;';
			c && (t += 'margin-bottom:' + c + 'px;margin-right:' + c + 'px;');
			this.x.style && (t += this.x.style);
			return t;
		},
		html_badge: function() {
			return this._badge ? this._badge.html() : '';
		},
		html_img: function( t ) {
			var x = this.x, b = this.parentNode.type === 'Album', mw = this.innerWidth(), mh = this.innerHeight(), u = _url_format.call( this, this.x.src ),
				iw = this.x.imgWidth, ih = this.x.imgHeight, w = iw || mw, h = ih || mh;
			var g = $.image( u, { width: iw, height: ih, maxWidth: mw, maxHeight: mh, error: evw + '.error()', load: evw + '.imgLoad()' }, { tip: x.tip === T ? x.text + (x.description ? '\n' + x.description : '') : x.tip } );
			return '<div id=' + this.id + 'i class="w-img-i f-inbl" style="' + (w ? 'width:' + w + 'px;' : '') + (h ? 'height:' + (h - (t && !ih ? 30 : 0)) + 'px;' : '') + '">' + g + this.html_badge() + '</div>';
		},
		html_text: function() {
			var x = this.x, t = this.html_format(), w = x.textWidth;
			return t ? '<div id=' + this.id + 't class="w-img-t f-' + (x.br ? 'wdbr' : 'fix') + '"' + this.prop_title() + ' style="' + (w ? 'width:' + w + 'px' : '') + '">' +
					(typeof t === _OBJ ? this.add( t, -1 ).html() : '<span class=w-img-s>' + t + '</span>') + (x.description ? '<div class="w-img-d f-fix" title="' + $.strQuot( x.description ) + '">' + x.description + '</div>' : '') + '</div>' : '';
		},
		html_nodes: function() {
			var t = this.html_text(), s = this.html_img( t );
			if ( this.x.box ) {
				this.box = CheckBox.parseOption( this, { cls: 'w-img-box', checked: this.x.focus } );
				this.box.type === 'TripleBox' && this.box.addEvent( 'change', function() { this._focus( this.box.isChecked() ) }, this );
				s += this.box.html();
			}
			s += t;
			return s;
		}
	}
} ),
/* `toggle` 可展开收拢的工具条。可显示单行文本与(或)分割线。
 *  /@text: 文本; @hr(Bool) 显示横线; /@open(Bool): 设置初始展开收拢效果并产生一个toggle图标; /@target: 指定展开收拢的widget ID, 多个用逗号隔开
 */
Toggle = define.widget( 'Toggle', {
	Const: function( x ) {
		x.hr && (this.className += ' z-hr');
		W.apply( this, arguments );
	},
	Listener: {
		body: {
			ready: function() {
				this.x.expanded != N && this.$() && this.toggle( this.x.expanded );
			}
		}
	},
	Helper: {
		focus: function( a ) {
			var p = a;
			do {
				var b = p.$() && p.$().toggleCommander;
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
			var b = a == N || a.type ? ! (this.x.expanded == N ? T : this.x.expanded) : a, c = this.x.target, d = this.x.icon, e = this.x.expandedIcon || d;
			this.x.expanded = b;
			if ( c && (c = c.split(',')) ) {
				for ( var i = 0, g; i < c.length; i ++ ) {
					(g = this.ownerView.find( c[ i ] )) && g.display( this );
				}
			} else {
				for ( var i = this.nodeIndex + 1, p = this.parentNode, l = p.length; i < l; i ++ ) {
					if ( p[ i ].type === this.type )
						break;
					p[ i ].display( this );
				}
			}
			this.$( 'o' ) && $.replace( this.$( 'o' ), this.html_icon( b ) );
			this.addClass( 'z-expanded', !! b );
			var p = this;
			while ( (p = p.parentNode) && p.innerHeight() == N );
			p && p.triggerAll( 'resize' );
			this.trigger( 'toggle' );
			a && a.type && $.stop( a );
		},
		isExpanded: function() {
			return this.x.expanded;
		},
		// @a -> open?
		html_icon: function( a ) {
			var x = this.x, c = x.icon, d = x.expandedIcon || c, t = evw + '.toggle(event)';
			a == N && (a = x.expanded);
			return d ? $.image( a === F ? (c || d) : d, { cls: '_i f-inbl', id: this.id + 'o', click: t } ) :
				(x.expanded != N ? '<span class="_i f-inbl" id=' + this.id + 'o onclick=' + t + '>' + $.arrow( a === F ? 'r2' : 'b2' ) + '<i class=f-vi></i></span>' : '');
		},
		html_nodes: function() {
			var t = (this.x.text ? '<span class="f-omit f-va"' + this.prop_title() + '>' + this.x.text + '</span><i class=f-vi></i>' : '');
			if ( this.x.hr ) {
				t = '<table cellpadding=0 cellspacing=0 height=100%><tr><td>' + t + '<td width=100%><hr noshade class=_hr></table>';
			}
			return this.html_icon() + '<div class="_c f-oh f-fix">' + t + '</div>';
		}
	}
} ),
/* `pagebar`
 *  @target: 指向另一个widget，点击页数时让这个widget执行 .page() 方法。如果设定了此参数，那么 currentPage sumPage 等参数都从这个widget里读取
 *  @src: 带$0的url, 点击页数时把$0替换为页数并执行ajax命令
 *  @sumPage: 总页数
 *  @noFirstLast: 不显示"首页"和"尾页"
 *  @jump: 显示跳转输入框
 *  @buttonCount: 中间有几个显示页数的按钮
 *  @name: 隐藏表单值
 */
PageBar = define.widget( 'PageBar', {
	Const: function( x ) {
		this.face = x.face || 'normal';
		this.className += ' z-face-' + this.face;
		x.transparent && (this.className += ' z-trans');
		W.apply( this, arguments );
		x.sumPage && (x.sumPage = Math.ceil( x.sumPage ));
		if ( this.face === 'normal' )
			this.page_text = Loc.page_text;
		!x.currentPage && (x.currentPage = 1);
		this.attr( 'align' ) && (this.property = ' align=' + this.attr( 'align' ));
		x.target && _regTarget.call( this, function() {
			var a = this.ownerView.find( x.target );
			if ( a ) {
				a.x.limit = this.x.pageSize;
				a.pageBar = this;
				a.page( this.x.currentPage );
			}
		});
	},
	Extend: Html,
	Listener: {
		body: {
			ready: function() {
				this.x.keyJump && this.listenKeyJump( T );
			},
			// 阻止默认触发用户事件，改在 go 方法中调用
			click: { block: $.rt( T ) }
		}
	},
	Prototype: {
		className: 'w-pagebar',
		page_text: {},
		isModified: $.rt( F ),
		over: function( a ) {
			$.classAdd( a, 'z-hv' );
		},
		out: function( a ) {
			$.classRemove( a, 'z-hv' );
		},
		listenKeyJump: function( a ) {
			var self = this,
				b = this._keyJump || (this._keyJump = function( e ) {
				if ( (e.keyCode === 37 || e.keyCode === 39) && ! e.target.isContentEditable ) { //37:left, 39:right
					if ( ! Q( '.f-hide [id="' + self.id + '"]' ).length ) {
						var d = Q( '.w-dialog.z-front' );
						if ( ! d.length || d.has( '[id="' + self.id + '"]' ).length )
							self.keyJump( e.keyCode );
					}
				}
			});
			Q( document )[ a ? 'on' : 'off' ]( 'keyup', b );
		},
		keyJump: function( a ) {
			this.x.keyJump && this.go( this.x.currentPage + (a === 37 ? -1 : 1) );
		},
		go: function( i, a ) {
			if ( this._disposed )
				return;
			if ( (i = _number( i )) > 0 ) {
				i = Math.max( Math.floor( i ), 1 );
				if ( this.x.sumPage )
					i = Math.min( this.x.sumPage, i );
				this.$( 'v' ) && (this.$( 'v' ).value = i);
				if ( this.x.target ) {
					var g = this.ownerView.find( this.x.target );
					if ( g ) {
						g.page( i );
						this.x.currentPage = i;
						this.render();
					}
				} else if ( this.x.src ) {
					var s = this.x.src;
					if ( s.indexOf( 'javascript:' ) === 0 )
						this.exec( { type: 'JS', text: s }, [ i ] );
					else {
						this.exec( { type: 'Ajax', src: s, filter: this.x.filter, complete: this.x.complete,
							success: this.x.success || function( x ) {
								W.isCmd( x ) ? this.cmd( x ) : this.srcParent().reload( x );
							}, error: this.x.error }, [ i ] );
					}
				}
				// 为业务 click 事件之中的 $0 提供值
				this.triggerHandler( 'click', [ i ] );
			}
		},
		val: function( a ) {
			if ( a === U )
				return this.x.currentPage;
			this.go( a );
		},
		ego: function( e ) {
			if ( e.type === 'keyUp' && e.keyCode !== 13 )
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
			var b = this.ownerView.find( this.x.target );
			if ( b ) {
				var c = this.ownerView.find( this.x.target ).page();
				this.x.currentPage = Math.max( _number( c.currentPage ), 1 );
				this.x.sumPage = Math.max( _number( c.sumPage ), 1 );
			}
		},
		eve: function( i, b ) {
			return b ? ' onclick=' + evw + '.go(' + i + ',this) onmouseover=' + evw + '.over(this) onmouseout=' + evw + '.out(this)' : '';
		},
		prop_cls: function() {
			return _proto.prop_cls.call( this ) + (this.x.noFirstLast ? ' z-nofirstlast' : '');
		},
		html_info: function() {
			var s = '';
			if ( this.x.jump ) {
				this.jbtn = this.add( { type: 'Button', cls: '_jbtn', text: 'GO', on: { click: 'this.parentNode.ego(event)' } }, -1 );
				s += '<span class="_t _to">' + Loc.to + '</span> <input class=_jump id=' + this.id + 'j onfocus=' + evw + '.jumpFocus(event) onblur=' + evw + '.jumpFocus(event,!1) onkeyup=' + evw + '.ego(event)>' + this.jbtn.html() + ' <span class=_t>' + Loc.page + '</span>';
			}
			if ( this.x.setting ) {
				this.sbtn = this.add( { type: 'Button', cls: 'f-button _sbtn', icon: '.f-i .f-i-config', nodes: this.x.setting }, -1 );
				s += this.sbtn.html();
			}
			return s;
		},
		html_normal: function() {
			this.x.target && this.initByTarget();
			var c = this.x.currentPage, m = _number( this.x.sumPage ), n = _number( this.x.buttonCount ), f = Math.max( 1, c - Math.ceil( n / 2 ) + 1 ),
				l = Math.min( m + 1, f + n ), d = l - f < n ? Math.max( 1, l - n ) : f, h = this.x.noFirstLast, z = this.x.buttonCls ? ' ' + this.x.buttonCls : '',
				s = ( h ? '' : '<em class="_o _b _first' + ( c == 1 ? '' : ' z-us' ) + z + '"' + this.eve( 1, c != 1 ) + '>' + (this.x.firstText || this.page_text.first || '') + '</em>' ) +
					'<em class="_o _b _prev' + ( c == 1 ? '' : ' z-us' ) + z + '"' + this.eve( c - 1, c != 1 ) + '>' + (this.x.prevText || this.page_text.prev || '') + '</em>';
			if ( m && this.x.buttonSumPage && d > 1 ) {
					s += '<em class="_o _num z-us z-sum' + z + '"' + this.eve( 1, T ) + '>1<i>...</i></em>';
					s += '<em class="_o _num z-sumdot' + z + '">...</em>';
			}
			for ( var i = d; i < l; i ++ ) {
				s += '<em class="_o _num' + ( i == c ? ' _cur' : ' z-us' ) + z + '"' + this.eve( i, i != c ) + '>' + i + '</em>';
			}
			if ( m && this.x.buttonSumPage && m >= i ) {
				s += '<em class="_o _num z-sumdot' + z + '">...</em>';
				s += '<em class="_o _num z-us z-sum' + z + '"' + this.eve( m, T ) + '><i>...</i>' + m + '</em>';
			}
			s += '<em class="_o _b _next' + ( c == m ? '' : ' z-us' ) + z + '"' + this.eve( c + 1, c != m ) + '>' + (this.x.nextText || this.page_text.next || '') + '</em>' +
				( h ? '' : '<em class="_o _b _last' + ( c == m ? '' : ' z-us' ) + z + '"' + this.eve( m, c != m ) + '>' + (this.x.lastText || this.page_text.last || '') + '</em>' );
			return (this.x.name ? '<input type=hidden id="' + this.id + 'v" name="' + this.x.name + '" value="' + (c || 1) + '">' : '') + s + this.html_info() + '<i class=f-vi></i>';
		},
		html_mini: function() {
			return this.html_normal();
		},
		drop: function( a ) {
			var i = 1, c = _number( this.x.currentPage ) || 1, s = _number( this.x.sumPage ) || 1, n = [];
			for ( ; i <= s; i ++ ) {
				n.push( { text: i, on: { click: 'this.getCommander().parentNode.parentNode.go(' + i + ')' }, status: this.x.currentPage == i ? 'disabled' : '' } );
			}
			a.exec( { type: 'Menu', nodes: n, snap: { target: a, position: 'v', indent: 1 }, line: true, memory: T, focusIndex: c } );
		},
		html_simple: function() {
			this.x.target && this.initByTarget();
			var c = 'w-pagebar-button ' + (this.x.buttonCls != N ? this.x.buttonCls : 'f-button'), d = _number( this.x.currentPage ) || 1, s = _number( this.x.sumPage ) || 1,
				b = [ { type: 'Button', ownproperty: T, text: '&lt;<i class="f-arw f-arw-l4"></i>', tip: (this.x.prevText || Loc.page_prev || ''), cls: c + ' _prev', status: d == 1 ? 'disabled' : '', escape: F, on: { click: 'this.rootNode.parentNode.go(' + (d - 1) + ')' } },
					  { type: 'Button', ownproperty: T, text: '&gt;<i class="f-arw f-arw-r4"></i>', tip: (this.x.nextText || Loc.page_next || ''), cls: c + ' _next', status: d == s ? 'disabled' : '', escape: F, on: { click: 'this.rootNode.parentNode.go(' + (d + 1) + ')' } } ];
			if ( this.x.buttonCount != 0 ) {
				var g = this.x.dropAlign;
				b.splice( g === 'right' ? 2 : g === 'left' ? 0 : 1, 0, { type: 'Button', text: d + '/' + s, cls: c + ' _drop', escape: F, on: { click: 'this.rootNode.parentNode.drop(this)' } } );
			}
			this.groupbar = this.add( { type: 'ButtonBar', cls: 'w-pagebar-buttonbar f-nv f-groupbar', width: -1, nodes: b, space: -1 }, -1 );
			return (this.x.name ? '<input type=hidden id="' + this.id + 'v" name="' + this.x.name + '" value="' + d + '">' : '') + this.groupbar.html() + PageBar.prototype.html_info.call( this ) + '<i class=f-vi></i>';
		},
		html_nodes: function() {
			return this[ 'html_' + this.face ]();
		},
		dispose: function() {
			$( this.id + 'j' ) && ($( this.id + 'j' ).onblur = N); /* 如果不写这一句，谷歌浏览器会崩溃 */
			this.x.keyJump && this.listenKeyJump( F );
			_proto.dispose.call( this );
		}
	}
} ),
/* `fieldset` */
FieldSet = define.widget( 'FieldSet', {
	Extend: VertScale,
	Default: { widthMinus: 2, heightMinus: 3 },
	Prototype: {
		className: 'w-fieldset',
		init_nodes: function() {
			this.layout = new Layout( { height: '*', heightMinus: 26, nodes: this.x.nodes }, this );
			if ( this.x.box ) {
				if ( this.x.legend && this.x.box.text == N ) {
					this.x.box.text = this.x.legend;
					delete this.x.legend;
				}
				this.box = CheckBox.parseOption( this, { target: this.layout } );
			}
		},
		html: function() {
			return this.html_before() + '<fieldset' + this.html_prop() + '><legend class=w-fieldset-legend>' + (this.box ? this.box.html() : '') + '<span class="_t f-va">' + (this.x.legend || '') + '</span><i class=f-vi></i></legend>' + this.html_prepend() + this.layout.html() + this.html_append() + '</fieldset>' + this.html_after();
		}
	}
} ),
// 模板标题
DialogTitle = define.widget( 'DialogTitle', {
	Const: function( x, p ) {
		var d = Dialog.get( p );
		if ( d ) {
			d.dialogTitle = this;
			x.text = d.html_format( d.x.title, d.x.format, d.x.escape );
		}
		W.apply( this, arguments );
	},
	Extend: Html,
	Listener: {
		body: {
			mouseDown: {
				occupy: T,
				method: function( e ) { Dialog.get( this ).dragTitle( this, e ) }
			}
		}
	}
} ),
_getContentView = function( a ) {
	if ( a.type_view ) return a;
	for ( var i = 0, b; i < a.length; i ++ )
		if ( b = _getContentView( a[ i ] ) ) return b;
},
_snapHooks = { topLeft: 'tl', leftTop: 'lt', topRight: 'tr', righTop: 'rt', rightBottom: 'rb', bottomRight: 'br', bottomLeft: 'bl', leftBottom: 'lb',
				center: F, centerCenter: F, c: F, cc: F, '0': F },
_snapPosition = function( a ) {
	return a && (_snapHooks[ a ] === F ? N : '' + (_snapHooks[ a ] || a));
}
/* `dialog`
 *  id 用于全局存取 ( dfish.dialog(id) ) 并保持唯一，以及用于里面的view的 path */
Dialog = define.widget( 'Dialog', {
	Const: function( x, p, n ) {
		this._dft_wd = x.width;
		this._dft_ht = x.height;
		if ( x.fullScreen || (x.width && ! isNaN( x.width ) && x.width > $.width()) )
			x.width = '*';
		if ( x.fullScreen || (x.height && ! isNaN( x.height ) && x.height > $.height()) )
			x.height = '*';
		p == N && (p = _docView);
		Section.call( this, x, p, n == N ? -1 : n );
		Dialog.all[ this.id ] = this;
		if ( x.id ) {
			Dialog.custom[ x.id ] && Dialog.custom[ x.id ].remove();
			Dialog.custom[ x.id ] = this;
		}
		if ( p !== _docView ) {
			this.opener = p.closest( function() { return this.isDialogWidget } );
		}
		this.commander = p;
		if ( x.independent )
			delete p.discNodes[ this.id ];
		else
			p.addEventOnce( 'remove', this.remove, this );
		_docView.addEvent( 'resize', function() { this.vis && this.axis() }, this );
	},
	Extend: Section,
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
		cleanPop: function( a ) {
			for ( var k in Dialog.all ) {
				var d = Dialog.all[ k ];
				d.vis && d.attr( 'autoHide' ) && (! a || (a.id !== k && !d.contains( a ) && !a.contains( d ))) && d.close();
			}
		}
	},
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, Section, e );
				if ( this.x.resizable ) {
					var self = this;
					Q( '<div class="w-dialog-rsz z-w"></div><div class="w-dialog-rsz z-n"></div><div class="w-dialog-rsz z-e"></div><div class="w-dialog-rsz z-s"></div><div class="w-dialog-rsz z-nw"></div><div class="w-dialog-rsz z-ne"></div><div class="w-dialog-rsz z-sw"></div><div class="w-dialog-rsz z-se"></div>' )
						.appendTo( this.$() ).on( 'mousedown', function( e ) {
							if ( self.isMax() )
								return;
							var a = this.className.match( /z-(\w+)/ )[ 1 ], b = $.bcr( self.$() ), ix = e.clientX, iy = e.clientY, o;
							$.moveup( function( e ) {
								var x = e.clientX, y = e.clientY;
								if ( ! o ) {
									o = $.db( '<div style="position:absolute;top:' + b.top + 'px;left:' + b.left + 'px;width:' + (b.width - 6) + 'px;height:' + (b.height - 6) + 'px;border:3px solid #000;opacity:.2;z-index:11"></div>' );
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
									self.resize( Math.max(c.width, 20), Math.max(c.height, 20) );
									s[ l ? 'left' : 'right' ] = (l ? c.left : c.right) + 'px';
									s[ t ? 'top' : 'bottom' ] = (t ? c.top : c.bottom) + 'px';
									$.remove( o );
									s = o = N;
								}
							} );
						} );
				}
			},
			beforeload: function() {
				if ( this.$() )
					this.draggable();
			},
			load: function() {
				this.draggable( N, F );
			},
			mouseDown: {
				occupy: T,
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
		attrSetter: function( a, b ) {
			if ( a === 'title' ) {
				this.dialogTitle && this.dialogTitle.text( b );
			}
			Section.prototype.attrSetter.apply( this, arguments );
		},
		// @a -> sync?, b -> fn?, c -> cache?
		outerWidth: function() {
			var w = this.attr( 'width' );
			return w == N || w < 0 ? N : _docView.scaleWidth( this );
		},
		outerHeight: function() {
			var h = this.attr( 'height' );
			return h == N || h < 0 ? N : _docView.scaleHeight( this );
		},
		closestData: function( a ) {
			var d = this.x.data && this.x.data[ a ];
			if ( d === U )
				d = this.x.args && this.x.args[ a ];
			return d !== U ? d : this.parentNode.closestData( a );
		},
		getContentNode: function() {
			return this.layout && this.layout[ 0 ];
		},
		getContentView: function() {
			return this.contentView || (this.contentView = _getContentView( this ));
		},
		parentDialog: function() {
			return $.dialog( this.ownerView );
		},
		// 兼容3.1的处理：dialog src如果返回view，则套一层node
		init_x_filter: function( x ) {
			return x.type === 'View' ? { type: this.type, node: x } : x;
		},
		isContentData: function( x ) {
			return x.type === this.type || x.type === 'View';
		},
		_dft_pos: function() {
			var w = this.width(), h = this.height();
			return { left: Math.ceil( ( $.width() - w ) / 2 ), top: Math.max( 0, Math.ceil( ( $.height() - h ) / 2 ) ) };
		},
		draggable: function( a, b ) {
			if ( (a || (a = this)) && (a = a.isWidget ? a.$() : a) ) {
				Q( a )[ b === F ? 'off' : 'on' ]( 'mousedown', DialogTitle.Listener.body.mouseDown.method );
			}
		},
		max: function() {
			var f = this.x.fullScreen, s = this.$().style, o = this._stack_pos, w = this.width(), h = this.height(), x, y;
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
			this.x.fullScreen = ! f;
			return this;
		},
		isMax: function() {
			return !!this.x.fullScreen;
		},
		//@public 移动到指定位置 /@a -> left, b -> top
		moveTo: function( a, b ) {
			var s = this.$().style;
			s.left = a + 'px';
			s.top  = b + 'px';
			s.right = s.bottom = '';
			return this;
		},
		preload: function( a ) {
			this.loadData( F, a && $.proxy( this, a ), T );
		},
		//@public 移动到指定位置 /@a -> elem|widget, b -> snap option
		snapTo: function( a, b ) {
			var n = this.x.snap || O;
			! this._disposed && $.snapTo( this.$(), $.snap( this.width(), this.height(), a.isWidget ? a.$() : a, b || n.position, this._fitpos, n.indent != N ? n.indent : (this.x.prong && -10) ) );
			return this;
		},
		// 使当前dialog显示优先级最高，不被其他dialog遮挡
		front: function() {
			if ( this._disposed )
				return;
			if ( this.opener && !this.opener._disposed && !this.x.independent ) {
				this.opener.front();
			} else {
				var a = Dialog.all;
				for ( var i in a ) {
					a[ i ]._front( a[ i ] == this || (!a[ i ].x.independent && this.contains( a[ i ] )) );
				}
			}
		},
		_front: function( a ) {
			var z = a ? 11 : 10;
			this.vis && this.css( { zIndex: z } ).css( 'cvr', { zIndex: z } ).addClass( 'z-front', a );
		},
		// 定位 /@a -> fullScreen?
		axis: function( a ) {
			var c = this.attr( 'local' ), d = this.x.snap || O, f = _snapPosition( this.x.position ), g = a ? N : this._snapTargetElem(), vs = g && Q( g ).is( ':visible' ), w = this.$().offsetWidth, h = this.$().offsetHeight, n, r;
			// 如果有指定 snap，采用 snap 模式
			if ( vs ) {
				r = $.snap( w, h, g, d.position || this._snapType || (c && 'cc'), this._fitpos, d.indent != N ? d.indent : (this.x.prong && -10), c && (c === T ? this.getLocalParent().$() : $( c )) );
			} else if ( f ) { // 八方位浮动的起始位置
				r = $.snap( w, h, N, f, this._fitpos, d.indent );
				var d = f.charAt( 0 );
				if ( d == 1 || d == 2 || d === 't' ) {
					n = { top: 0 };
					r.top = -h;
				} else if ( d == 3 || d == 4 || d === 'r' ) {
					n = { right: 0 };
					r.right = -w;
				} else if ( d == 5 || d == 6 || d === 'b' ) {
					n = { bottom: 0 };
					r.bottom = -h;
				} else if ( d == 7 || d == 8 || d === 'l' ) {
					n = { left: 0 };
					r.left = -w;
				}
			}
			if ( ! r )
				r = this._dft_pos();
			this._pos && this._snapCls( F );
			this._pos = r;
			$.snapTo( this.$(), r );
			if ( vs && this.type === 'Dialog' ) {
				// snap的窗口如果超出屏幕高度，强制修改高度到可见范围内
				var h = this.x.height, t = r.top < 0, b = r.bottom < 0;
				t && (this.height( r.height + r.top ));
				b && (this.height( r.height + r.bottom ));
				(t || b) && (this._ori_height = h);
			}
			// 八方位浮动效果
			n && Q( this.$() ).animate( n, 200 );
			if ( this.x.prong && vs ) {
				var m = r.inner ? (r.mag_b ? 'b' : r.mag_t ? 't' : r.mag_l ? 'l' : 'r') : (r.mag_b ? 't' : r.mag_t ? 'b' : r.mag_l ? 'r' : 'l'), x = Math.floor((r.target.left + r.target.right) / 2), y = Math.floor((r.target.top + r.target.bottom) / 2), 
					l = $.numRange( x - r.left, 7, r.left + r.width - 7 ), t = $.numRange( y - r.top, 7, r.top + r.height - 7 ), s = '';
				(r.mag_b || r.mag_t) && (s += 'left:' + l + 'px;');
				(r.mag_l || r.mag_r) && (s += 'top:' + t + 'px;');
				$.append( this.$(), '<div class="w-dialog-prong z-' + m + '"' + (s ? ' style="' + s + '"' : '') +
					'><i class="_out f-arw f-arw-' + m + '5"></i><i class="_in f-arw f-arw-' + m + '4"></i></div>' );
			}
			$.classAdd( this.$(), 'z-max', !! a );
			(c = this.parentNode.dropperCls && this.parentNode.dropperCls( this )) && $.classAdd( this.$(), c );
			this._snapCls();
		},
		_snapTargetElem: function() {
			var d = this.x.snap && this.x.snap.target, e;
			return typeof d === _STR ? ((e = this.ownerView.find( d )) ? e.snapElem() : $( d )) : (d ? (d.isWidget ? d.snapElem() : $( d )) : (this.attr( 'local' ) && this.ownerView.$()));
		},
		_snapCls: function( a ) {
			var d = this._snapTargetElem(), r = this._pos;
			if ( d ) { 
				$.classAdd( d, 'z-drop', a );
				if ( r && r.type ) {
					var m = 'z-mag-' + ( r.mag_t ? 't' : r.mag_l ? 'l' : r.mag_b ? 'b' : 'r' );
					this.addClass( m + ' z-snap-' + r.type, a );
					$.classAdd( d, m, a );
				}
			}
		},
		dragTitle: function( a, e ) {
			var o = Dialog.get( a );
			if ( o ) {
				o.front();
				if ( o.x.movable !== F && ! o.x.fullScreen ) {
					var b = o._pos.pix_b ? -1 : 1, r = o._pos.pix_r ? -1 : 1, v = b < 0 ? 'bottom' : 'top', h = r < 0 ? 'right' : 'left',
						x = e.clientX, y = e.clientY, t = _number( o.$().style[ v ] ), l = _number( o.$().style[ h ] ), m, n = $.height(), w = $.width(), z = o.$().offsetWidth;
					$.moveup( function( e ) {
						! m && (m = $.db( '<div class=w-dialog-move style="width:' + $.width() + 'px;height:' + n + 'px;"></div>' ));
						o.$().style[ v ] = $.numRange( t + b * (e.clientY - y), 0, n - 30 ) + 'px';
						o.$().style[ h ] = $.numRange( l + r * (e.clientX - x), 100 - z, w - 30 ) + 'px';
					}, function( e ) {
						m && $.remove( m );
					} );
				}
			}	
		},
		getLocalParent: function() {
			var p = this;
			while ( (p = p.parentNode) && ! p.type_view && ! p.isDialogWidget );
			return p;
		},
		html_nodes: function() {
			var s = _proto.html_nodes.apply( this, arguments );
			if ( ie7 )
				s = '<table cellpadding=0 cellspacing=0 border=0><tr><td id=' + this.id + 'cont>' + s + '</td></tr></table>';
			if ( this.type === 'Dialog' && this.x.loadingHead !== F )
				s += '<div class=w-dialog-loadinghead onmousedown=' + evw + '.dragTitle(this,event)><i class=f-vi></i><span class="_t f-va">' + (typeof this.x.title === _STR ? this.x.title : '') + '</span><i class="_x f-inbl" onclick=' + $.abbr + '.close(this) ' + _event_zhover + '></i></div>';
			return s;
		},
		reset: function() {
			delete this.contentView;
			Section.prototype.reset.call( this );
		},
		render: function() {
			if ( this._disposed )
				return;
			! this.parentNode && _docView.add( this );
			this.$() && this.removeElem();
			// _ori_height表示当前窗口曾经调整过高度，再次打开时尝试恢复
			if ( this._ori_height ) {
				this.height( this._ori_height );
				delete this._ori_height;
			}
			var c = this.attr( 'local' );
			if ( c ) {
				var d = this.getLocalParent();
				d && d.type_view && d.addClass( 'f-rel' );
			}
			if ( this.x.cover )
				$.db( '<div id=' + this.id + 'cvr class="w-dialog-cover z-type-' + this.type.toLowerCase() + '"></div>', c && this.getLocalParent().$() );
			$.db( this.html(), c && this.getLocalParent().$() );
			if ( this.x.minWidth || this.x.maxWidth ) {
				var ew = Math.min( Math.max( this.$().offsetWidth, this.$().scrollWidth + 2 ), $.width() ), n = this.minWidth( T ), m = this.maxWidth( T );
				this.width( n && n > ew ? n : m && m < ew ? m : ew );
			}
			if ( this.x.minHeight || this.x.maxHeight ) {
				var eh = Math.min( Math.max( this.$().offsetHeight, this.$().scrollHeight + 2 ), $.height() ), n = this.minHeight( T ), m = this.maxHeight( T );
				this.height( n && n > eh ? n : m && m < eh ? m : eh );
			}
			// 检测object控件，如果存在则生成iframe遮盖。如果确定object不会影响dialog的显示，请给object标签加上属性 data-transparent="1"
			for ( var i = 0, o = $.tags( 'object' ); i < o.length; i ++ ) {
				if ( ! o[ i ].getAttribute( 'data-transparent' ) ) {
					this.addEventOnce( 'load', function() {
						this[ 0 ] && this[ 0 ].addClass( 'f-rel' );
						$.prepend( this.$(), '<iframe style="height:100%;width:100%;position:absolute;top:0;left:0;" src="about:blank" frameborder=0 marginheight=0 marginwidth=0 allowtransparency></iframe>' );
					} );
					break;
				}
			}
			this.axis( this.x.fullScreen );
			// 生成一条线，覆盖在对话框(或menu)和父节点连接的地方，形成一体的效果
			if ( this.x.line ) {
				$.append( this.$(), '<div id=' + this.id + 'ln class=w-menu-line></div>' );
				var r = this._pos, w = r.target.width, b = this.$().currentStyle, c = this.parentNode.$().currentStyle, lw = _number( c.borderLeftWidth ), rw = _number( c.borderRightWidth );
				this.css( 'ln', { height: _number( b.borderTopWidth ), width: w - lw - rw, backgroundColor: b.backgroundColor, borderColor: c.borderColor, borderLeftWidth: lw, borderRightWidth: rw } );
			}
			this.vis = T;
			this.attr( 'autoHide' ) && this.listenHide( T );
			this.triggerAll( 'ready' );
			if ( this.x.timeout )
				this.listenTimeout();
			this.front();
			return this;
		},
		isShow: function() {
			return this.vis;
		},
		// @a -> show delay millisenconds?
		show: function( a ) {
			if ( a == 0 )
				return this._show();
			if ( this.x.hoverDrop || a > 0 ) {
				var self = this;
				if ( ! this._show_timer ) {
					this._show_timer = setTimeout( function() { self._show(); }, a || 300 );
					var n = 'mouseover.' + self.id + ':show';
					Q( document ).on( n, function( e ) {
						if ( self._disposed || ! self.parentNode.contains( e.target ) ) {
							clearTimeout( self._show_timer );
							delete self._show_timer;
							Q( document ).off( n );
						}
					} );
				}
			} else
				this._show();
			return this;
		},
		_show: function() {
			clearTimeout( this._show_timer );
			delete this._show_timer;
			if ( this._disposed )
				return;
			if ( this.x.cache && this.$() ) {
				$.show( this.$() );
				this.x.cover && $.show( this.$( 'cvr' ) );
				this.vis = T;
				this._snapCls();
			} else
				this.render();
			this.trigger( 'show' );
			return this;
		},
		keepHover: function( a ) {
			this._keep_hover = a;
		},
		hide: function() {
			if ( this._disposed )
				return;
			this._snapCls( F );
			if ( this.x.cache )
				this.$() && ($.hide( this.$() ), (this.x.cover && $.hide( this.$( 'cvr' ) )), this.vis = F);
			else if ( this.x.memory ) {
				 this._hide();
			} else {
				this.remove();
			}
		},
		_hide: function() {
			if ( this.vis ) {
				this.getContentView() && this.getContentView().abort();
				this.listenHide( F );
				var f = _snapPosition( this.x.position );
				if ( f && br.css3 ) {
					var w = this.$().offsetWidth, h = this.$().offsetHeight, self = this,
						d = f.charAt( 0 ),
						n = d == 1 || d == 2 || d === 't' ? { top: -h } : d == 3 || d == 4 || d === 'r' ? { right: -w } : d == 5 || d == 6 || d === 'b' ? { bottom: -h } : d == 7 || d == 8 || d === 'l' ? { left: -w } : N;
					$.classAdd( this.$(), 'z-closing' ); // z-closing生成遮盖层，避免在消失过程中内容部分再被点击
					n ? Q( this.$() ).animate( n, 150, function() { self.removeElem() } ) : this.removeElem();
				} else {
					this.removeElem();
				}
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
				Dialog.cleanPop( this ); // 关闭除了自己之外的所有autoHide窗口
			}
		},
		_listenHide: function( a ) {
			var self = this, d = this.x.hoverDrop;
			$.attach( document, 'mousedown mousewheel', self.listenHide_ || (self.listenHide_ = function( e ) {
				if(!self._disposed && (e.srcElement.id == self.id + 'cvr' || !(self.hasBubble( e.srcElement ) || (!self.x.independent && self.x.snap && self.x.snap.target && _widget( self.x.snap.target ).hasBubble( e.srcElement )))) ) {self.close()};
			}), a );
			if ( d ) {
				var o = d === T ? (this.x.snap && this.x.snap.target ? $( this.x.snap.target ) : this.parentNode.$()) : d.isWidget ? d.$() : d, f = a === F ? 'off' : 'on';
				Q( [ o, self.$() ] )[ f ]( 'mouseenter', self._hover_over || (self._hover_over = function() { clearTimeout( self._hover_timer ); delete self._hover_timer; }) );
				Q( document )[ f ]( 'mousemove', self._hover_move || (self._hover_move = function( e ) {
					if ( ! o.contains( e.target ) && ! self.hasBubble( e.target ) && ! self._keep_hover ) {
						if ( ! self._hover_timer )
							self._hover_timer = setTimeout( function() { self.close() }, 300 );
					} else
						self._hover_over && self._hover_over();
				}) );
			}
		},
		listenTimeout: function() {
			setTimeout( $.proxy( this, this.close ), this.x.timeout );
		},
		close: function() {
			if ( ! this._disposed && F !== this.trigger( 'close' ) )
				this.hide();
		},
		// @implement
		removeElem: function( a ) {
			_proto.removeElem.call( this, a );
			! a && this.x.cover && $.remove( this.$( 'cvr' ) );
		},
		remove: function() {
			if ( this._disposed )
				return;
			this._hide();
			this.dispose();
		},
		dispose: function() {
			if ( this._disposed )
				return;
			this.commander && this.commander.removeEvent( 'remove', N, this );
			_docView.removeEvent( 'resize', N, this );
			delete Dialog.all[ this.id ];
			delete this.opener;
			delete this.commander;
			if( this.x.id )
				delete Dialog.custom[ this.x.id ];
			_proto.dispose.call( this );
		}
	}
} ),
_operexe = function( x, g, a ) {
	return x && (typeof x === _OBJ ? g.exec( x, a ) : typeof x === _FUN ? x.apply( g, a || [] ) : g.formatJS( x ));
},
/* `alertbutton` */
AlertButton = define.widget( 'AlertButton', {
	Extend: Button,
	Listener: {
		body: {
			click: {
				block: function() {
					var d = $.dialog( this );
					_operexe( (this.x.on && this.x.on.click) || (this.type === 'AlertSubmitButton' ? d.x.yes : d.x.no), d.commander, d.x.args );
					d.remove();
					return T;
				}
			}
		}
	}
} ),
AlertSubmitButton = define.widget( 'AlertSubmitButton', {
	Extend: SubmitButton,
	Listener: { body: { click: { block: AlertButton.Listener.body.click.block } } }
} ),
/*  `alert`  */
Alert = define.widget( 'Alert', {
	Const: function( x, p ) {
		var a = this.type === 'Alert', r = x.args, s = x.buttonCls || 'f-button',
			b = { type: 'AlertSubmitButton', cls: s, text: '    ' + Loc.confirm + '    ' },
			c = { type: 'AlertButton', cls: s, text: '    ' + Loc.cancel + '    ' },
			d,
			o = this.getDefaultOption( x.cls ),
			t = x.preload || (!x.ownproperty && o && o.preload);
		if ( x.buttons ) {
			for ( var i = 0, d = []; i < x.buttons.length; i ++ ) {
				x.buttons[ i ].type = 'alert/' + x.buttons[ i ].type;
				! x.buttons[ i ].cls && (x.buttons[ i ].cls = s);
				d.push( x.buttons[ i ] );
			}
		}
		if ( t && (this._tpl = _getPreload( t )) ) {
			var f = this.html_format( $.strFormat( x.text == N ? '' : ('' + x.text), x.args || [] ), x.format, x.escape );
			x.escape !== F && (f = f.replace( /\n/g, '<br>' ));
			$.extend( x, { preload: t, minWidth: 260, maxWidth: 700, maxHeight: 600, title: Loc.opertip, node: { type: 'Vert', height: '*', nodes: [
				{ type: 'Html', scroll: T, height: '*', text: '<div class=w-alert-content><table border=0 class=w-alert-table><tr><td align=center valign=top>' +
				$.image( x.icon ? x.icon : '.f-i-alert' + (a ? 'warn' : 'ask'), { cls: 'w-alert-icon' } ) + '<td><div class=w-alert-text>' + f + '</div></table></div>' },
				{ type: 'ButtonBar', cls: 'z-sub-' + this.type, align: 'center', height: 60, space: 10, nodes: d || (a ? [ b ] : [ b, c ]) }
			] } } );
		}
		(x.yes || x.no) && this.addEventOnce( 'close', function() {
			_operexe( a ? x.yes : x.no, this.commander, x.args );
		} );
		Dialog.call( this, x, a ? _docView : p );
	},
	Extend: Dialog,
	Prototype: {
		className: 'w-dialog w-alert',
		// alert 类型对话框z-index固定值为3，总在最前面，不做修改
		front: $.rt( F ),
		_front: $.rt( F ),
		render: function() {
			var s = document.readyState;
			if ( this._tpl && (s === 'complete' || s === U) ) {
				return Dialog.prototype.render.call( this );
			} else {
				var x = this.x;
				if ( this.type === 'Alert' ) {
					$.winbox( x.text );
					x.yes && _operexe( x.yes, this.commander, x.args );
				} else {
					_operexe( confirm( x.text ) ? x.yes : x.no, this.commander, x.args );
				}
			}
		}
	}
} ),
/*  `confirm`  */
Confirm = define.widget( 'Confirm', {
	Extend: Alert
} ),
_instCache = {},
// 唯一实例 /@a -> widget, b -> owner widget?
_inst_add = function( a, b ) {
	b = (b || _docView).id + (a.type || a);
	_instCache[ b ] && _instCache[ b ].hide();
	delete _instCache[ b ];
	a.type && (_instCache[ b ] = a);
},
_inst_hide = _inst_add,
_inst_get = function( a, b ) {
	var c = _instCache[ (b || _docView).id + a ];
	return c && !c._disposed && c;
},
_inst_del = function( a, b ) {
	delete _instCache[ (b || _docView).id + (a.type || a) ];
},

/*  `tip`  */
Tip = define.widget( 'Tip', {
	Const: function( x, p ) {
		$.extendDeep( x, { prong: x.prong == N ? T : x.prong, autoHide: T, independent: T, snap: { target: p, position: 'tb,rl,lr,bt,rr,ll,bb,tt,cc' },
			node: { type: 'Html', text: '<div class=w-tip-text><span class=f-va>' + this.html_format( x.text, x.format, x.escape ) + '</span><i class=f-vi></i></div>' + (x.closable !== F ? $.image('.f-i-close',{cls: 'w-tip-x', click:$.abbr + '.close(this)'}) : '') }
		} );
		x.closable !== F && (this.className += ' z-x');
		Dialog.apply( this, arguments );
		! this.x.multiple && _inst_add( this );
	},
	Extend: Dialog,
	Helper: {
		hide: function() { _inst_hide( 'Tip' ); }
	},
	Prototype: {
		className: 'w-dialog w-tip',
		showLoading: $.rt(),
		// alert 类型对话框z-index固定值为11，总在最前面，不做修改
		front: $.rt( F ),
		_front: $.rt( F ),
		text: function( a ) {
			Q( '.w-tip-text', this.$() ).html( a );
		}
	}
} ),
/*  `loading`  */
Loading = define.widget( 'Loading', {
	Const: function( x, p ) {
		$.extend( x, { width: x.node ? 200 : -1 } );
		Dialog.apply( this, arguments );
		_inst_add( this, this.ownerView );
	},
	Extend: Dialog,
	Default: { local: T },
	Helper: {
		hide: function( a ) {
			var d = $.dialog( a );
			d && d.type == 'Loading' ? d.close() : _inst_hide( 'Loading', _view( a ) );
		}
	},
	Prototype: {
		className: 'w-dialog w-loading f-shadow',
		showLoading: $.rt(),
		html_nodes: function() {
			if ( this.x.node ) {
				return Dialog.prototype.html_nodes.apply( this, arguments );
			} else {
				return '<cite class=_c>' + $.image( '%img%/loading-cir.gif' ) + ' <em class=_t>' + this.html_format( this.x.text || Loc.loading ) + '</em></cite><i class=f-vi></i>';
			}
		}
	}
} ),
// 以 src 为 key 存储 progress 实例。相同 src 的实例进程将被合并。
_progressCache = {},
/*  `progress`  */
Progress = define.widget( 'Progress', {
	Const: function( x, p ) {
		x.guide && (this._guide = x.guide);
		W.apply( this, arguments );
		//var s = this.getSrc();
		//s && $.jsonArray( this, _progressCache, s );
		!x.percent && (x.percent = 0);
		this.cssText = 'height:auto;';
	},
	Extend: Section,
	Prototype: {
		className: 'w-progress',
		x_childtype: $.rt( 'ProgressItem' ),
		init_nodes: function() {
			this.layout && this.reset();
			Section.prototype.init_nodes.call( this );
		},
		// @a -> close?
		showLoading: function( a ) {
			a === F && this.removeElem( 'loading' );
		},
		showLayout: function() {
			Section.prototype.showLayout.apply( this, arguments );
			var d = $.dialog( this );
			d && d.type === 'Loading' && d.axis();
		},
		stop: function() {
			this._stopped = T;
			clearTimeout( this._timer );
		},
		// @s -> src, t -> template
		reload: function( s, t ) {
			this.stop();
			Section.prototype.reload.apply( this, arguments );
		},
		// @t -> template
		template: function( t ) {
			this.stop();
			this.reload( N, t );
		},
		getSrc: function() {
			return this._guide ? this.formatStr( this._guide, this.x.args, T ) : Section.prototype.getSrc.call( this );
		},
		start: function( re ) {
			if ( !this.x.src && !this.x.template )
				return;
			if ( ! this._x_ini ) {
				this.init_x( this.x );
				this.repaint();
				this.layout && this.showLayout( re );
			}
			! this.layout && this.isDisplay() && this.load( N, function() { delete this._guide; this._load() } );
		},
		_load: function() {
			if ( this._stopped )
				return;
			var self = this;
			clearTimeout( this._timer );
			this._timer = setTimeout( function() {
				self.load( N, function() { this._load() } );
			}, this.x.delay );
		},
		isHead: function() {
			return _progressCache[ this.getSrc() ][ 0 ] === this;
		},
		html_nodes: function() {
			return this.layout ? this.layout.html() : this.x.src || this.x.guide ? this.html_loading() : '';
		},
		dispose: function() {
			this.stop();
			$.arrPop( _progressCache[ this.getSrc() ], this );
			_proto.dispose.call( this );
		}
	}
} ),
ProgressItem = define.widget( 'ProgressItem', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		!x.percent && (x.percent = 0);
		if ( x.range ) {
			for ( var i = 0, r = x.range.split( ',' ), v = -1; i < r.length; i ++ ) {
				if ( _number( x.percent ) >= _number( r[ i ] ) )
					v = Math.max( v, r[ i ] );
			}
			v > -1 && (this.className += ' z-' + v);
		}
		this.cssText = 'height:auto;';
	},
	Listener: {
		body: {
			ready: function() {
				var p = this.$( 'p' );
				p.style.width = (this.width() || p.parentNode.parentNode.parentNode.offsetWidth) + 'px';
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Progress',
		className: 'w-progress-item',
		html_nodes: function() {
			var p = this.x.percent, h = this.innerHeight();
			return (this.x.text != N ? '<div class="_t f-fix">' + this.html_format() + '</div>' : '') +
				'<div class=_bar'+ (h != N ? ' style="height:' + h + 'px;"' : '') + '><div class=_dn><i class=f-vi></i><span class="_s f-va">' + p + '%</span></div><div class=_up style="width:' + p + '%"><div class=_gut id=' + this.id + 'p><i class=f-vi></i><span class="_s f-va">' + p + '%</span></div></div></div>';
		}
	}
} ),
MenuSplit = define.widget( 'MenuSplit', {
	Prototype: {
		show: $.rt(), hide: $.rt(), elemht: function() { return 5 },
		html: function() {
			return '<div class=w-menu-split>&nbsp;</div>';
		}
	}
} ),
/* `menu` */
Menu = define.widget( 'Menu', {
	Const: function( x, p, n ) {
		p == N && (p = _docView);
		W.call( this, x, p, n == N ? -1 : n );
		Dialog.all[ this.id ] = this;
		this._fitpos = T;
		(this.commander = p).addEventOnce( 'remove', this.remove, this );
	},
	Extend: Dialog,
	Default: {
		width: -1, height: -1, autoHide: T
	},
	Listener: {
		body: {
			// menu的DOM渲染做两次，第一次测量并调整可用范围，第二次渲染menuButton。所以第一次装载完毕时不触发用户定义的load事件，等第二次渲染menuButton时再触发
			ready: $.rt( F ),
			mouseWheel: function( e ) {
				if ( this.$( 'up' ) )
					this.scroll( e.wheelDelta > 0 ? -1 : 1 );
				$.stop( e );
			},
			// 覆盖 dialog.Listener.body.click 方法，点击不作处理
			click: N
		}
	},
	Prototype: {
		className: 'w-menu w-dialog',
		// menu的z-index固定为3，总在最前面
		front: $.rt( F ),
		_front: $.rt( F ),
		//init_x: _proto.init_x,
		init_nodes: _proto.init_nodes,
		// menu有两种子节点: menuSplit, menButton
		x_childtype: function( t ) {
			return t && t.indexOf( 'Menu' ) !== 0 ? 'Menu' + t : 'MenuButton';
		},
		_dft_pos: function() {
			var c = $.point;
			if ( ! c.srcElement ) {
				var b = $.bcr( this.parentNode.$() );
				c = { clientX: b.left, clientY: b.top };
			}
			return $.snap( this.width(), this.height(), c, '21,12', this._fitpos );
		},
		_hide: function() {
			if ( this.$() ) {
				var i = this.length;
				while ( i -- )
					this[ i ].hide();
				Dialog.prototype._hide.call( this );
				this.type === 'Menu' && _inst_del( this );
			}
		},
		listenHide: function( a ) {
			this.type === 'Menu' && Dialog.prototype.listenHide.call( this, a );
		},
		// 检查是否有高度溢出，并填充内容
		checkOverflow: function() {
			this.echoStart = 0;
			this.echoEnd   = this.length -1;
			var r = $.bcr( this.$() ), m = 0, g = this.$( 'g' ), b = MenuButton.prototype.elemht(), h = $.height();
			if ( r.top < 0 )
				m -= r.top;
			if ( r.bottom > h )
				m += r.bottom - h;
			if ( m ) {
				// realht 是按钮可展现区域的高度 / 22 = 上下两个翻页按钮高度 + menu的padding border高度
				this.realht = g.offsetHeight - 22 - m;
				this.realht -= this.realht % b;
				g.style.height = this.realht + 'px';
				$.before( g, '<div id=' + this.id + 'up class="_ar" onclick=' + evw + '.scroll(-1)>' + $.arrow( 't2' ) + '<i class=f-vi></i></div>' );
				$.after( g,  '<div id=' + this.id + 'dn class="_ar" onclick=' + evw + '.scroll(1)>'  + $.arrow( 'b2' ) + '<i class=f-vi></i></div>' );
				$.classAdd( this.$(), 'z-scroll' );
				// 如果有设置focusIndex, 需要让这个menuButton滚动显示在中间
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
				var i = a > 0 ? this.echoEnd  : this.echoStart, l = this.length, c = 0;
				for ( ; i > -1 && i < l; i += ( a === 0 ? 1 : a ) ) {
					c += this[ i ].elemht();
					if ( c > this.realht )
						break;
				}
				a > 0 ? (this.echoStart = this.echoEnd + 1, this.echoEnd = Math.min( i, l - 1 )) : (this.echoEnd = this.echoStart, this.echoStart = Math.max( 0, i ));
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
			if ( this.type === 'Menu' )
				_inst_add( this );
			for ( var i = 0, l = this.length, e = h = 0, m = $.height(); i < l; i ++ ) {
				h += this[ i ].elemht();
				if ( h > m && ! e )
					this.echoEnd = e = i;
			}
			this.virht = h;
			Dialog.prototype.render.call( this );
			this.checkOverflow();
			if ( this.type === 'Menu' ) {
				var w = 0;
				// x.line: 实现一条和button结合效果的线
				this.x.line && (w = Math.max( this._pos.target.width - 2, w ));
				this.css( 'min-width', w );
			}
			this.axis();
			this.triggerHandler( 'ready' );
			return this;
		}
	}
} ),
/* `submenu` */
SubMenu = define.widget( 'SubMenu', {
	Extend: Menu,
	Prototype: {
		ROOT_TYPE: 'Menu',
		className: 'w-menu w-submenu w-dialog',
		_snapType: '21,12'
	}
} ),
/* `collapse` */
Collapse = define.widget( 'Collapse', {
	Const: function( x, p ) {
		this.id = $.uid( this );
		var y = { type: 'Vert' }, b = [], d, e = this.getDefaultOption( x.cls );
		for ( var i = 0, n = x.nodes || [], g; i < n.length; i ++ ) {
			g = $.extend( { display: !!n[ i ].focus }, n[ i ].target );
			b.push( $.extend( { type: 'CollapseButton', width: '*', focusable: T, target: N }, n[ i ], x.pub, e && e.pub ) );
			b.push( g );
			//d == N && b[ i * 2 ].focus && (d = (i * 2));
		}
		/*// 单选模式下，至少有一个节点默认展开
		if ( ! x.focusMultiple ) {
			d == N && b[ 0 ] && (d = 0, b[ 0 ].focus = T);
			d != N && (b[ d + 1 ].display = T);
		}*/
		y.nodes = b;
		Vert.call( this, $.extend( y, x ), p );
	},
	Extend: Vert,
	Default: { scroll: T, widthMinus: 2, heightMinus: 2 },
	Prototype: {
		className: 'w-collapse',
		getFocus: function() {
			for ( var i = 0; i < this.length; i += 2 )
				if ( this[ i ].isFocus() ) return this[ i ];
		}
	}
} ),
/* `CollapseButton` */
CollapseButton = define.widget( 'CollapseButton', {
	Const: function( x, p ) {
		this.rootNode = p;
		Button.apply( this, arguments );
		this.nodeIndex == 0 && (this.className += ' z-first');
		!p.length && (this.className += ' z-first');
	},
	Extend: Button,
	Listener: {
		body: {
			focus: function() {
				var n = this.next();
				n.display();
				this.addClass( 'z-expanded' );
				$.arrow( this.$( 'clp-arw' ), 'b2' );
				n.next() && n.next().addClass( 'z-expanded-after' );
				this.rootNode.trigger( 'resize' );
			},
			blur: function() {
				var n = this.next();
				n.display( F );
				this.removeClass( 'z-expanded' );
				$.arrow( this.$( 'clp-arw' ), 'r2' );
				n.next() && n.next().removeClass( 'z-expanded-after' );
				this.rootNode.trigger( 'resize' );
			}
		}
	},
	Prototype: {
		className: 'w-button w-collapse-button',
		isToggleable: $.rt( T ),
		prop_cls: function() {
			return _proto.prop_cls.call( this ) + (this.nodeIndex === 0 ? ' z-first' : '') + (this.nodeIndex === this.parentNode.length - 2 ? ' z-last' : '');
		},
		html_icon: function() {
			return this.x.icon ? Button.prototype.html_icon.call( this ) : '<div class=_clp_arw>' + $.arrow( this.id + 'clp-arw', 'r2' ) + '<i class=f-vi></i></div>';
		}
	}
} ),
/* `label` */
Label = define.widget( 'Label', {
	Const: function( x, p ) {
		this._pad = x.space != N ? x.space : 5;
		this.className += ' f-nv' + (x.vAlign ? '-' + x.vAlign : '') + ' z-type-' + p.type.toLowerCase();
		var td = p.parentNode, w = td.x.labelWidth;
		if ( w == N && td.col )
			w = td.col.x.labelWidth;
		w != N && x.width == N && (x.width = w);
		this.defaults( { widthMinus: this._pad } );
		W.apply( this, arguments );
		if ( ie7 ) { // IE7下需要根据td高度来手动调整label高度
			var td = p.closest( 'TD' );
			if ( td ) {
				(this.ie7td = td).addEvent( 'nodeChange', function() {
					Q( '.w-label', this.parentNode.parentNode.$() ).each( function() {
						var o = _widget( this );
						$.replace( o.$(), o.html() );
					}).each( function() {
						_widget( this ).triggerListener( 'ready' );
					} );
				} );
			}
		}
	},
	Extend: Html,
	Default: { align: 'right', vAlign: 'middle', height: -1 },
	Listener: {
		body: {
			ready: function() {
				if ( this.ie7td ) {
					var b = $( this.id + 'bg' ), c = $.bcr( this.ie7td.$() ), d = $.bcr( b );
					Q( b ).css( {
						height: c.height -1,
						marginLeft: c.left - d.left,
						marginTop: c.top - d.top
					} );
				}
			},
			resize: function() {
				if ( this.parentNode.label == this ) {
					$( this.id + 'bg' ).style.width = Math.max( this.innerWidth() -1, 0 ) + 'px';
					this.ie7td && this.ie7td.fireEvent( 'nodeChange' );
				}
			}
		}
	},
	Prototype: {
		className: 'w-label f-wdbr',
		setValidate: function( x ) {
			if ( x && x.required ) {
				this.$() && ! $.get( '.f-required', this.$() ) && $.prepend( this.$( 'lb' ), this.html_star() );
			} else {
				this.$() && Q( '.f-required', this.$() ).remove();
			}
		},
		prop_cls: function() {
			return _proto.prop_cls.call( this, this.parentNode.parentNode );
		},
		prop_style: function() {
			return (this._pad ? 'margin-right:' + this._pad + 'px;' : '') + _proto.prop_style.call( this );
		},
		html_star: function() {
			return '<span class=f-required>*</span>';
		},
		html_text: function() {
			var t = this.html_format();
			if ( typeof t === _OBJ )
				t = this.add( t, -1 ).html();
			return (this.parentNode.isRequired() ? this.html_star() : '') + '<span>' + t + (this.x.suffix || '') + '</span>';
		},
		html_bg: function() {
			return '<div id=' + this.id + 'bg class="_bg" style="width:' + (this.innerWidth() - 1) + 'px;padding-left:' + this._pad + 'px"><div class=_pad></div></div>';
		},
		html_nodes: function() {
			var s = this.html_text(), v = this.attr( 'vAlign' );
			return (br.css3 ? '<div id=' + this.id + 'lb class=_lb>' + s + '</div>' : '<i class=f-vi></i><div id=' + this.id + 'lb class="_lb f-nv">' + s + '</div>') + this.html_bg();
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
	}
},
_validate_dft_value = { required: T, beforeNow: T, afterNow: T },
// /@ a -> valid object, b -> valid code, c -> args
_form_err = function( a, b, c ) {
	var t = this.x.label;
	typeof t === _OBJ && (t = t.text || '');
	return { wid: this.id, name: this.x.name, code: b, label: t, text: (a && a[ b ] && a[ b ].text) || Loc.ps.apply( N, [ Loc.form[ b === 'required' && ! t ? 'complete_required' : b ], t || Loc.field ].concat( c || [] ) ) || '' };
},
_valid_err = function( b, v ) {
	if ( typeof v !== _STR )
		v = $.isArray(v) ? v.join(',') : ('' + v);
	v = $.strTrim(v);
	var k = this.validHooks || O, c, d, e;
	if ( k.valid && (e = k.valid.call(this, b, v)) )
		return e;
	if ( ! b )
		return;
	if ( this.vv('required', b) && (k.required ? k.required.call(this, b, v) : (! v)) )
		return _form_err.call( this, b, 'required' );
	if ( (c = this.vv('minLength', b)) && (k.minLength ? k.minLength.call(this, b, v) : ($.strLen(v) < c)) )
		return _form_err.call( this, b, 'minLength', [ c ] );
	if ( (c = this.vv('maxLength', b)) && (k.maxLength ? k.maxLength.call(this, b, v) : ((d = $.strLen(v)) > c)) )
		return _form_err.call( this, b, 'maxLength', [ d - c ] );
	if ( (c = this.vv('minValue', b)) && v && (k.minValue ? k.minValue.call(this, b, v) : ($.isNumber(c) ? (_number(v) < _number(c)) : (v < c))) )
		return _form_err.call( this, b, 'minValue', [ c ] );
	if ( (c = this.vv('maxValue', b)) && v && (k.maxValue ? k.maxValue.call(this, b, v) : ($.isNumber(c) ? (_number(v) > _number(c)) : (v > c))) )
		return _form_err.call( this, b, 'maxValue', [ c ] );
	if ( (c = this.vv('minSize', b)) && v && (k.minSize ? k.minSize.call(this, b, v) : (v.split(',').length < _number(c))) )
		return _form_err.call( this, b, 'minSize', [ c ] );
	if ( (c = this.vv('maxSize', b)) && v && (k.maxSize ? k.maxSize.call(this, b, v) : (v.split(',').length > _number(c))) )
		return _form_err.call( this, b, 'maxSize', [ c ] );
	if ( (c = this.vv('pattern', b)) && v && (k.pattern ? k.pattern.call(this, b, c) : (! eval( c + '.test(v)' ))) )
		return _form_err.call( this, b, 'pattern' );
	if ( b.compare && v && (c = this.ownerView.f( b.compare.target )) && (d = c.val()) && ( k.compare ? k.compare.call( this, b, v, c, d ) : ! eval( '"' + $.strQuot( v ) + '"' + (b.compare.mode || '==') + '"' + $.strQuot( d ) + '"' ) ) )
			return _form_err.call( this, b, 'compare', [ b.compare.mode, c.x.label ? (c.x.label.text || c.x.label) : '' ] );
	if ( b.method && (d = this.formatJS( b.method )) )
		return { wid: this.id, name: this.x.name, code: 'method', text: d };
},
_valid_opt = function( a ) {
	return a == N || a === T ? this.x.validate : a === F ? N : typeof a === _OBJ ? a : (this.x.validateGroup && this.x.validateGroup[ a ]);
},
_enter_submit = function( k, a ) {
	k === 13 && a.ownerView.submitButton && a.ownerView.submitButton.click();
},
// 检查是否正在用中文输入法
_listen_ime = function( a, b ) {
	$.attach( b || a.$t(), 'compositionstart', function() { a._imeMode = T } );
	$.attach( b || a.$t(), 'compositionend',   function() { a._imeMode = F } );
},
/* `absform` */
AbsForm = define.widget( 'AbsForm', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.init_label();
	},
	Listener: {
		tag: 't',
		body: {
			focus: {
				occupy: T,
				method: function( e ) {
					e == N && this.$t().focus();
					if ( mbi ) { // mobile选中表单会弹出键盘，表单可能会被键盘遮住看不到，所以要让表单滚动到可视范围
						var r = Scroll.get( this ), self = this;
						r && r.addEventOnce( 'resize', function() {
							self.contains( document.activeElement ) && r.scrollTop( self, 'middle' );
						});						
					}
					_z_on.call( this );
				}
			},
			blur: {
				occupy: T,
				method: function() { this.$( 'f' ) && !this.$( 'f' ).contains( document.activeElement ) && _z_on.call( this, F ) }
			},
			resize: function() {
				if ( this.$() ) {
					var w = this.formWidth();
					w != N && w >= 0 && this.css( 'f', 'width', w );
				}
			},
			valid: function( e, a ) {
				return this.getValidError( a );
			},
			error: function( e, a ) {
				typeof a === _OBJ ? this.exec( a ) : this.warn( a );
			}
		}
	},
	Default: { height: -1 },
	Prototype: {
		isFormWidget: T,
		_warncls: '',
		validHooks: F,
		init_label: function() {
			var a = this.x.label;
			if ( a && typeof a === _OBJ ) {
				var c = new Label( a, this, -1 ), w = c.attr( 'width' );
				if ( w != N && w != 0 )
					this.label = c;
				if ( w == -1 )
					this.addEvent( 'ready', this.fixLabelWidth );
			}
		},
		fixLabelWidth: function() {
			this.label.width( this.label.width() + 1 );
			this.trigger( 'resize' );
		},
		validTip: function( t ) {
			return { type: 'Tip', text: t };
		},
		form_minus:  function() {
			return (this.label ? this.label.outerWidth() : 0);
		},
		formWidth: function() {
			var w = this.innerWidth();
			return w == N || w < 0 ? N : w - this.form_minus();
		},
		formHeight: function() {
			return this.innerHeight();
		},
		hasBubble: function( a ) {
			return this.$( 'f' ).contains( a.isWidget ? a.$() : a );
		},
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
		isRequired: function() {
			return this.x.validate && this.x.validate.required && (this.x.validate.required === T ? T : this.x.validate.required.value);
		},
		// @a -> original?
		isModified: function( a ) {
			var u = this.x.value, v = a ? u : this._modval;
			return this.val() != (v == N ? (u == N ? '' : u) : v);
		},
		saveModified: function( b ) {
			this._modval = this.$v().value || '';
			b && (this.x.value = this._modval);
		},
		//@implement
		attrSetter: function( a, b ) {
			_proto.attrSetter.apply( this, arguments );
			if ( a === 'status' ) {
				b === 'readonly' ? this.readonly() : b === 'validonly' ? this.validonly() : b === 'disabled' ? this.disable() : this.normal();
			}
		},
		normal: function() {
			this.$v().removeAttribute( 'readOnly' );
			this.$v().removeAttribute( 'disabled' );
			$.classRemove( this.$(), 'z-ds' );
			_proto.normal.call( this );
			return this;
		},
		readonly: function( a ) {
			a = a == N || a;
			this.$v().readOnly = a;
			this.$v().removeAttribute( 'disabled' );
			_proto.readonly.call( this, a );
			return this;
		},
		validonly: function( a ) {
			a = a == N || a;
			this.$v().readOnly = a;
			this.$v().removeAttribute( 'disabled' );
			_proto.validonly.call( this, a );
			return this;
		},
		disable: function( a ) {
			a = a == N || a;
			this.$v().disabled = a;
			this.$v().removeAttribute( 'readOnly' );
			_proto.disable.call( this, a );
			return this;
		},
		isReadonly: function() {
			return this.x.status === 'readonly';
		},
		isValidonly: function() {
			return this.x.status === 'validonly';
		},
		// validate value
		vv: function( a, b ) {
			b = b || this.x.validate || O;
			return typeof b[ a ] !== _OBJ ? b[ a ] : b[ a ].value !== U ? b[ a ].value : _validate_dft_value[ a ];
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
				c = (x.validateGroup || (x.validateGroup = {}));
				a ? $.merge( (c[ b ] || (c[ b ] = {})), a ) : (c[ b ] = N);
			} else {
				c = (x.validate || (x.validate = {}));
				a ? $.merge( c, a ) : (x.validate = N);
				this.$() && $.classAdd( this.$(), 'z-required', !!c.required );
				this.label && this.label.setValidate( this.x.validate );
			}
			return this;
		},
		// @a -> valid item name, b -> group name, v -> value
		valid: function( a, b, v ) {
			var c = b;
			if ( a ) {
				var d = b ? (this.x.validateGroup || F)[ b ] : this.x.validate, c = {};
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
				this.$v() && this.checkPlaceholder();
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
		snapElem: function() {
			return this.$( 'f' );
		},
		reset: function( a ) {
			this.val( a || this.x.value == N ? '' : this.x.value );
		},
		input_prop_value: function() {
			return $.strEscape(this.x.value == N ? '' : '' + this.x.value);
		},
		input_prop: function() {
			var t = this.attr( 'tip' ), v = this.input_prop_value();
			return ' id="' + this.id + 't" class=_t name="' + this.input_name() + '"' + (t ? ' title="' + $.strQuot((t === T ? (this.x.text || this.x.value) : t) || '') + '"' : '') +
				(this.isReadonly() || this.isValidonly() ? ' readonly' : '') + (this.isDisabled() ? ' disabled' : '') + (v ? ' value="' + v + '"' : '') + _html_on.call( this );
		},
		prop_cls: function() {
			var c = _proto.prop_cls.call( this );
			return 'w-f w-' + this.type.toLowerCase() + (c ? ' ' + c: '') + (this.x.validate && this.x.validate.required ? ' z-required' : '') + ' f-nv f-nobr';
		},
		prop_style: function() {
			return this.x.style ? this.x.style : '';
		},
		form_prop: function() {
			var w = this.formWidth(), h = this.formHeight(), s = '';
			w != N && w >= 0 && (s += 'width:' + w + 'px;');
			h != N && h >= 0 && (s += 'height:' + h + 'px;');
			return ' id=' + this.id + 'f class="' + this.form_cls() + '"' + (s ? ' style="' + s + '"' : '');
		},
		form_cls: function() {
			return 'f-nv';
		},
		html_placeholder: $.rt( '' ),
		html: function() {
			return this.html_before() + '<div ' + this.html_prop() + '><i class=f-vi></i>' + (this.label ? this.label.html() : '') + '<div' + this.form_prop() + '>' + this.html_prepend() + this.html_nodes() + this.html_append() + '</div></div>' + this.html_after();
		}
	}
} ),
/* `absinput` */
AbsInput = define.widget( 'AbsInput', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		if ( x.cls || x.style || x.transparent ) {
			this.defaults( _size_fix( x.cls, x.style, x.transparent ? 0 : this.Const.Default.widthMinus, x.transparent ? 0 : this.Const.Default.heightMinus ) );
			x.transparent && $.classAdd( this, 'z-trans' );
		}
	},
	Extend: AbsForm,
	Listener: {
		range: 'input',
		body: {
			change: {
				proxy: ie ? 'paste keyUp' : 'input',
				block: $.rt( T ),
				occupy: function() {
					return (this.x.validate && this.x.validate.maxLength && cfg.inputDetect && cfg.inputDetect.maxLength) || this.attr( 'tip' ) === T || this.getPlaceholder();
				},
				method: function( e ) {
					if ( this.attr( 'tip' ) === T )
						this.$t().title = this.text();
					if ( ! this.isNormal() )
						return;
					var m = cfg.inputDetect && cfg.inputDetect.maxLength && this.x.validate && this.x.validate.maxLength, v = this.val(), u = v;
					if ( this.lastValue === U )
						this.lastValue = this.x.value;
					if ( this.lastValue != v ) {
						this.lastValue = v;
						var f = m && $.strLen( v ) > m;
						f && this.val( $.strSlice( v, m ) );
						if ( this.x.on && this.x.on.change ) {
							var self = this;
							clearTimeout( this._change_timer );
							this._change_timer = setTimeout( function() { self.triggerHandler( e ) }, 300 );
						}
						this.checkPlaceholder( v );
						Dialog.close( this.id + 'mxltip' );
						f && this.exec( { type: 'Tip', id: this.id + 'mxltip', text: Loc.form.reach_maxLength } );
					}
				}
			}
		}
	},
	Default: {
		tip: T, widthMinus: _dft_min, heightMinus: _dft_min
	},
	Prototype: {
		focus: function( a ) {
			this.$t()[ a === F ? 'blur' : 'focus' ]();
			_z_on.call( this, a == N || a );
		},
		isFocus: function() {
			return this.hasClass( 'z-on' );
		},
		toggleFocus: function() {
			this.focus( ! this.isFocus() );
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
		form_cls: function() {
			return 'w-input f-rel f-nv';
		},
		getPlaceholder: function() {
			var s = this.x.placeholder;
			s == N && cfg.autoPlaceholder && (s = Loc.ps( Loc[ this.placeholder_type || 'placeholder_input' ], this.label ? this.label.x.text : this.x.label && (this.x.label.text || this.x.label) ));
			return s;
		},
		checkPlaceholder: function( v ) {
			this.$( 'ph' ) && $.classAdd( this.$( 'ph' ), 'f-none', !!(arguments.length === 0 ? this.val() : v) );
		},
		html_placeholder: function() {
			var v = this.x.value, s = this.getPlaceholder();
			return s ? '<label class="w-input-placeholder f-fix' + (v != N && v !== '' ? ' f-none' : '') +
				'" id="' + this.id + 'ph" onclick=' + evw + '.clkhdr(event) ondblclick=' + evw + '.clkhdr(event) title="' + $.strQuot( s ) + '"><i class=f-vi></i><span class=f-va id="' + this.id + 'pht">' + s + '</span></label>' : '';
		},
		html_btn: function() {
			return '';
		},
		html_input: function() {
			return '';
		},
		html_nodes: function() {
			return this.html_btn() + '<div class=_c id="' + this.id + 'c">' + this.html_placeholder() + this.html_input() + '</div>';
		}
	}
} ),
/* `formgroup` */
FormGroup = define.widget( 'FormGroup', {
	Const: function( x ) {
		Horz.apply( this, arguments );
		this.init_label();
	},
	Extend: [ AbsForm, Horz ],
	Prototype: {
		scaleWidth: function( a, b ) {
			return _w_scale.width.call( this, a, b, a == this.label ? U : this.formWidth() );
		},
		prop_cls: function() {
			var c = _proto.prop_cls.call( this );
			return 'w-horz w-f w-formgroup f-nv' + (this.x.br ? ' z-br' : '') + (c ? ' ' + c: '');
		},
		html_nodes: function() {
			return Horz.prototype.html_nodes.call( this );
		}
	}
} ),
/* `formlabel` */
FormLabel = define.widget( 'FormLabel', {
	Const: function( x ) {
		Html.apply( this, arguments );
		this.init_label();
	},
	Extend: [ AbsForm, Html ],
	Prototype: {
		prop_cls: function() {
			var c = _proto.prop_cls.call( this );
			return 'w-f w-formlabel f-nv' + (c ? ' ' + c: '');
		},
		form_cls: function() {
			return 'w-formlabel-text f-nv';
		},
		html_nodes: function() {
			return Html.prototype.html_nodes.call( this );
		}
	}
} ),
/* `range` */
Range = define.widget( 'Range', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		this.begin = x.begin && this.add( x.begin );
		this.to    = (x.begin && x.end) && this.add( typeof x.to === _OBJ ? x.to : { type: 'Html', text: x.to || Loc.to, width: 30, align: 'center' } );
		this.end   = x.end && this.add( x.end );
		this.className = 'w-horz w-range f-nv';
		if ( ! x.vAlign && p && p.x.vAlign )
			this.defaults( { vAlign: p.x.vAlign } );
	},
	Extend: [ AbsForm, Horz ],
	Default: { width: -1 },
	Prototype: {
		x_nodes: $.rt(),
		form_minus:  function() {
			return this.label ? this.label.outerWidth() : 0;
		},
		scaleWidth: function( a, b ) {
			return _w_scale.width.call( this, a, b, a == this.label ? U : this.formWidth() );
		}
	}
} ),
/* `hidden` */
Hidden = define.widget( 'Hidden', {
	Const: function( x ) {
		x.width = x.height = 0;
		W.apply( this, arguments );
	},
	Extend: AbsForm,
	Prototype: {
		isHiddenWidget: T,
		isModified: $.rt( F ),
		saveModified: $.rt( F ),
		readonly: $.rt(),
		validonly: $.rt(),
		normal: function() { this.disable( F ) },
		disable: function( a ) {
			this.$().disabled = a == N || a;
			_proto.disable.call( this, a );
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
Textarea = define.widget( 'Textarea', {
	Extend: AbsInput,
	Default: { height: 60, tip: F },
	Listener: {
		body: {
			resize: function( e ) {
				_superTrigger( this, AbsInput, e );
				var h = this.innerHeight();
				if ( h > 0 && this.$t() ) {
					this.$t().style.height = h + 'px';
				}
			}
		}
	},
	Prototype: {
		isModified: function( a ) {
			var v = a ? this.x.value : this._modval;
			v == N && (v = this.x.value || '');
			return this.val().replace( /\r\n/g, '\n' ) != v.replace( /\r\n/g, '\n' );
		},
		input_prop_value: $.rt(),
		form_cls: function() {
			return 'w-input z-ah f-nv';
		},
		html_input: function() {
			return '<textarea' + this.input_prop() + '>' + $.strEscape(this.x.value || '').replace( /<\/textarea>/g, '&lt;\/textarea&gt;' ) + '</textarea>';
		}
	}
} ),
/* `text` */
Text = define.widget( 'Text', {
	Extend: AbsInput,
	Listener: {
		body: {
			keyUp: {
				occupy: T,
				method: function( e ) { _enter_submit( e.keyCode, this ); }
			}
		}
	},
	Prototype: {
		fixhdr: function() {
			this.x.placeholder && $.classAdd( this.$( 'ph' ), 'f-none', this.$t().value != '' );
		},
		html_input: function() {
			return '<input type=' + (this.formType || this.type) + this.input_prop() + '>';
		}
	}
} ),
/* `password` */
Password = define.widget( 'Password', {
	Extend: Text,
	Default: { tip: F },
	Prototype: {
		input_prop: function() { return AbsForm.prototype.input_prop.call( this ) + (this.x.autocomplete === T ? '' : ' autocomplete="new-password"'); }
	}
} ),
/* `checkboxgroup` */
CheckBoxGroup = define.widget( 'CheckBoxGroup', {
	Const: function( x, p ) {
		AbsForm.apply( this, arguments );
		for ( var i = 0, t; i < this.length; i ++ ) {
			if ( Q.isPlainObject( this[ i ].x.target ) ) { t = []; break; }
		}
		if ( t ) {
			for ( var i = 0, o; i < this.length; i ++ ) {
				o = Q.isPlainObject( this[ i ].x.target ) ? this[ i ].x.target : { type: 'Blank' };
				! o.id && (o.id = this.id + 'target' + i);
				t.push( this.add( o, -1 ) );
				this[ i ].x.target = o.id;
			}
			this.targets = t;
		}
	},
	Extend: AbsForm,
	Listener: {
		range: 'option',
		body: {
			/*ready: function() {
				if ( this.targets && this[ 0 ].attr( 'width' ) == -1 ) {
					var w = 0;
					for ( var i = 0; i < this.length; i ++ )
						w = Math.max( this[ i ].width(), w );
					for ( var i = 0; i < this.length; i ++ )
						this[ i ].attr( 'width', w );
					_w_rsz_all.call( this );
				}
			}*/
		}
	},
	Prototype: {
		type_horz: T,
		isBoxGroup: T,
		x_childtype: $.rt( 'CheckBox' ),
		scaleWidth: function( a, m ) {
			if ( a.nodeIndex < 0 && a != this.label ) {
				var i = $.arrIndex( this.targets, a ),
					w = a.attr( 'width' ),
					c = $.scale( this.formWidth(), [ this[ i ] ? this[ i ].width() || 0 : 0, w == N ? '*' : w < 0 ? '*' : w ] );
				return c[ 1 ];
			} else {
				return _proto.scaleWidth.call( this, a, m, a == this.label ? U : this.formWidth() );
			}
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
		isRequired: function() {
			return AbsForm.prototype.isRequired.call( this ) || (this[ 0 ] && this[ 0 ].isRequired());
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
			this.trigger( 'statuschange' );
		},
		disable: function( a ) {
			this.getOptions().each( function() { this.disable( a ) } );
			this.trigger( 'statuschange' );
		},
		readonly: function( a ) {
			this.getOptions().each( function() { this.readonly( a ) } );
			this.trigger( 'statuschange' );
		},
		validonly: function( a ) {
			this.getOptions().each( function() { this.validonly( a ) } );
			this.trigger( 'statuschange' );
		},
		isNormal: function() {
			return this[ 0 ] && this[ 0 ].isNormal();
		},
		isDisabled: function() {
			return this[ 0 ] && this[ 0 ].isDisabled();
		},
		isReadonly: function() {
			return this[ 0 ] && this[ 0 ].isReadonly();
		},
		isValidonly: function() {
			return this[ 0 ] && this[ 0 ].isValidonly();
		},
		form_cls: function() {
			return 'f-nv f-oh f-wdbr';
		},
		html_nodes: function() {
			if ( this.targets ) {
				for ( var i = 0, s = '', l = Math.max( this.length, this.targets.length ); i < l; i ++ )
					s += '<div class="w-' + this.type.toLowerCase() + '-list' + (i === 0 ? ' z-firt' : '') + (i == l - 1 ? ' z-last' : '') + '" onclick=' + evw + '.evwClickList(' + i + ',event)>' + (this[ i ] ? this[ i ].html() : '') + (this.targets[ i ] ? this.targets[ i ].html() : '') + '</div>';
				return s;
			} else
				return AbsForm.prototype.html_nodes.call( this );
		}
	}
} ),
_checked_states_hooks = { 'false': '0', 'true': '1', unchecked: '0', checked: '1', partial: '2', '0': '0', '1': '1', '2': '2' },
_checked_states = function( a ) {
	return _checked_states_hooks[ a ] || 0;
},
/* `checkbox` */
CheckBox = define.widget( 'CheckBox', {
	Const: function( x, p ) {
		if ( p && p.isBoxGroup ) {
			this.defaults( { widthMinus: 6, width: p.x.targets ? 62 : -1 } );
			p.x.status && $.extend( x, { status: p.x.status } );
		}
		this._dft_modchk = this._modchk = !!(x.checked != N ? x.checked : (p && p.isBoxGroup && p.x.value && x.value && $.idsAny( p.x.value, x.value )));
		AbsForm.apply( this, arguments );
	},
	Extend: AbsForm,
	Helper: {
		parseOption: function( p, f ) {
			var b = $.extend( {}, p.x.box, { type: 'CheckBox' }, f ), d = p.x.data, g = b.field, j;
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
				occupy: T,
				block: function() { return ! this.isNormal() },
				method: function() {
					if ( this.isNormal() ) {
						this.siblings( function() { this.x.target && this._ustag() } );
						//this.elements( '[w-target]' ).each( function() { _widget( this )._ustag() } );
						this.parentNode.isBoxGroup && this.parentNode.triggerHandler( 'change' );
					}
				}
			},
			click: {
				occupy: T,
				block: function() { return ! this.isNormal() },
				method: function( e ) {
					if ( ! this.isNormal() ) {
						this.elements().each( function() { this.checked = this.defaultChecked } );
						return F;
					}
					if ( e.type ) { // 修复ie下onchange失效问题
						if ( ie ) {
							var o = document.activeElement;
							this.$t().blur(), this.$t().focus();
							o.focus();
						} else if ( br.fox )
							this.trigger( 'change' );
					}
					this.parentNode.warn && this.parentNode.warn( F );
					this.attr( 'bubble' ) === F && $.cancel( e );
				}
			},
			resize: function() {
				var w = ! this.x.br && ! this.innerWidth() && this.parentNode.innerWidth();
				w && this.css( 'max-width', w );
			}
		}
	},
	Default: { width: -1, widthMinus: 1, heightMinus: 6, tip: T },
	Prototype: {
		ROOT_TYPE: 'CheckBoxGroup',
		tagName: 'cite',
		formType: 'checkbox',
		attrSetter: function( a, b ) {
			if ( a === 'text' ) {
				this.$( 's' ) ? $.replace( this.$( 's' ), this.html_text() ) : (this.$() && $.append( this.$(), this.html_text() ));
			}
		},
		getValidError: function( a ) {
			var e;
			if ( this.isNormal() || this.isValidonly() )
				e = _valid_err.call( this, _valid_opt.call( this, a ), this.groupVal() );
			return e || (this.parentNode.isBoxGroup && this.parentNode.getValidError( a ));
		},
		warn: function( a ) {
			this.parentNode.isBoxGroup ? this.parentNode.warn( a ) : AbsForm.prototype.warn.call( this, a );
		},
		snapElem: function() {
			return this.$();
		},
		_ustag: function( a ) {
			var b = this.x.target.isWidget ? [ this.x.target ] : this.ownerView.findAll( this.x.target );
			for ( var i = 0, c; i < b.length; i ++ ) {
				for ( var j = 0, c = this.ownerView.fAll( '*', b[ i ] ); j < c.length; j ++ )
					c[ j ].disable( ! this.isChecked() );
			}
		},
		elements: function( a, b ) {
			return Q( '[name="' + this.input_name() + '"]' + (b ? ':not(:disabled)' : '') + (a === T ? ':checked' : a === F ? ':not(:checked)' : (a || '')), this.ownerView.$() );
		},
		siblings: function( a ) {
			for ( var i = 0, b = this.elements(), l = b.length; i < l; i ++ ) {
				a.call( _widget( b[ i ] )  );
			}
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
		getSiblings: function( a ) {
			for ( var i = 0, b = this.elements( a ), c, r = []; i < b.length; i ++ ) {
				if ( (c = $.widget( b[ i ] )) && b[ i ].id === c.id + 't' && ! c.x.checkAll ) r.push( c );
			}
			return r;
		},
		isChecked: function() {
			return this.$t().checked;
		},
		isModified: function( a ) {
			return this.isChecked() !== (a ? this._dft_modchk : this._modchk);
		},
		saveModified: function( b ) {
			this._modchk = this.isChecked();
			b && (this._dft_modchk = this._modchk);
		},
		click: function( a ) {
			this.check( a == N ? ! this.isChecked() : a );
			this.trigger( 'click' );
		},
		text: function() {
			return this.x.text;
		},
		groupVal: CheckBoxGroup.prototype.val,
		val: function() {
			var t = this.$t();
			return t.disabled || ! t.checked ? '' : t.value;
		},
		htmlFor: function( a, e ) {
			if ( ! this.isNormal() )
				return;
			this.$t().focus(); // for ie9-
			a.previousSibling.click ? a.previousSibling.click() : Q( a.previousSibling.previousSibling ).click();
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
				if ( c != d && this.parentNode.isBoxGroup ) {
					this.parentNode.targets && this.check();
					this.parentNode.trigger( 'change' );
				}
			}
		},
		tip: function() {
			this.exec( $.extend( {}, this.attr( 'tip' ), { type: 'Tip', hoverDrop: true } ) );
		},
		html_text: function() {
			return (br.css3 ? '<label for=' + this.id + 't onclick=' + $.abbr + '.cancel()></label>' : '') +
				(this.x.text ? '<span class="_tit f-oh ' + (this.x.br ? 'f-wdbr' : 'f-fix') + '" id=' + this.id + 's onclick="' + evw + '.htmlFor(this,event)">' + this.html_format() + '</span>' : '');			
		},
		html: function() {
			var p = this.parentNode, w = this.formWidth(), s = this.prop_cls(), y = '';
			if ( w ) {
				y += 'width:' + w + 'px;';
			} else {
				if ( !this.x.br && (w = p.innerWidth()) )
					y += 'max-width:' + w + 'px;';
			}
			this.x.style && (y += this.x.style);
			return (this.label ? this.label.html() : '') + '<' + this.tagName + ' id=' + this.id + ' class="' + s + (this.x.br ? '' : ' f-fix') + '"' + this.prop_title() +
				(y ? ' style="' + y + '"' : '') + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + '>' + '<input id=' + this.id + 't type=' + this.formType + ' name="' + this.input_name() + '" value="' +
				$.strQuot(this.x.value || '') +	'" class=_t' + (this._modchk ? ' checked' : '') + (this.isDisabled() ? ' disabled' : '') + (this.formType === 'radio' ? ' w-name="' + (p.x.name || this.x.name || '') + '"' : '') + 
				_html_on.call( this ) + '>' + this.html_text() + '</' + this.tagName + '>' +
				(p.x.dir === 'v' && p[ p.length - 1 ] != this ? '<br>' : '');
		}
	}
} ),
/* `triplebox`
 *	checkstate: 0(未选),1(已选),2(半选)
 */
TripleBox = define.widget( 'TripleBox', {
	Extend: CheckBox,
	Listener: {
		body: {
			ready: function() {
				var c = this.checkstate();
				if ( this.x.checkAll ) {
					if ( c == 1 ) {
						this.relate();
					} else {
						for ( var i = 0, b = this.getRelates(), l = b.length; i < l; i ++ ) {
							if ( b[ i ] !== this ) { b[ i ].relate(); break; }
						}
					}
				}
			},
			change: {
				occupy: T,
				block: function() { return ! this.isNormal() },
				method: function( e ) {
					if ( this.isNormal() ) {
						_superTrigger( this, CheckBox, e );
						this.x.checkAll && e.srcElement && this.relate();
					}
				}
			},
			click: {
				occupy: T,
				method: function( e ) {
					var s = this.checkstate();
					_superTrigger( this, CheckBox, e );
					$.classAdd( this.$(), 'z-half', this.checkstate() == 2 );
					if ( e.srcElement ) {
						this.relate();
						br.ms && this.x.checkAll && s == 2 && this.checkstate( this.isChecked() ? 1 : 0 );; // ie点击半选状态的box 要手动触发change
					}	
				}
			}
		}
	},
	Prototype: {
		className: 'w-f w-checkbox',
		checkstate: function( a ) {
			var b = this.$() ? (this.$t().indeterminate ? 2 : this.$t().checked ? 1 : 0) : _checked_states( this.x.checked );
			if ( a == N ) {
				return b;
			}
			this.$t().checked = a == 1;
			this.$t().indeterminate = a == 2;
			$.classAdd( this.$(), 'z-half', a == 2 );
			//this.x.checked = a;
			if ( _checked_states( a ) != b || this.x.checkAll )
				this.trigger( 'change' );
		},
		check: function( a ) {
			this.checkstate( a );
			this.relate();
		},
		isPartial: function() {
			return this.checkstate() == 2;
		},
		getRelates: function() {
			return this.ownerView.fAll( this.x.name );
		},
		relate: function() {
			if ( this.x.name ) {
				for ( var i = 0, b = this.getRelates(), l = b.length, c0 = 0, c1 = 0, c2 = 0, d; i < l; i ++ ) {
					if ( this.x.checkAll ) {
						this != b[ i ] && b[ i ].checkstate( this.isChecked() );
					} else if ( b[ i ].x.checkAll ) {
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
			var c = this.checkstate();
			return '<' + this.tagName + ' id=' + this.id + ' class="' + this.prop_cls() + (c == 2 ? ' z-half' : '') + '"' + (this.x.id ? ' w-id="' + this.x.id + '"' : '') + '><input type=checkbox id=' + this.id + 't name="' + this.x.name + '" value="' + (this.x.value || '') + '" class=_t' +
				(c == 1 ? ' checked' : '') + (c == 2 ? ' indeterminate' : '') + (this.isDisabled() ? ' disabled' : '') + (this.x.partialsubmit ? ' w-partialsubmit="1"' : '') + _html_on.call( this ) + '>' + (br.css3 ? '<label for=' + this.id + 't onclick=' + $.abbr + '.cancel()></label>' : '') +
				(this.x.text ? '<span class=_tit id=' + this.id + 's onclick="' + evw + '.htmlFor(this,event)">' + this.html_format() + '</span>' : '') + '</' + this.tagName + '>';
		}
	}
} ),
/* `switch` */
Switch = define.widget( 'Switch', {
	Const: function( x, p ) {
		x.checked && (this.className += ' z-checked');
		CheckBox.apply( this, arguments );
	},
	Extend: CheckBox,
	Default: { width: -1 },
	Listener: {
		body: {
			change: {
				method: function( e ) {
					if ( this.isNormal() ) {
						var c = this.isChecked();
						this.addClass( 'z-checked', c );
						this.$( 'n' ).innerHTML = (c ? this.x.checkedText : this.x.uncheckedText) || '&nbsp;';
						_superTrigger( this, CheckBox, e );
					}
				}
			}			
		}
	},
	Prototype: {
		tagName: 'div',
		click: function( a, e ) {
			if ( br.css3 ) {
				$.cancel( e );
			} else {
				this.$t().checked = !this.$t().checked;
				this.trigger( 'change' );
			}
		},
		html_text: function() {
			return '<label class="_l" for=' + this.id + 't onclick=' + evw + '.click(this,event)><em class=_o></em><i id=' + this.id + 'n class=_n>' + ((this.x.checked ? this.x.checkedText : this.x.uncheckedText) || '&nbsp;') + '</i></label>' +
				(this.x.text ? '<span class=_tit onclick="' + evw + '.htmlFor(this,event)">' + this.html_format() + '</span>' : '');			
		}		
	}
} ),
/* `radiogroup` */
RadioGroup = define.widget( 'RadioGroup', {
	Extend: CheckBoxGroup,
	Prototype: {
		x_childtype: $.rt( 'Radio' )
	}
} ),
/* `radio` */
Radio = define.widget( 'Radio', {
	Extend: CheckBox,
	Prototype: {
		ROOT_TYPE: 'RadioGroup',
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
Select = define.widget( 'Select', {
	Const: function( x ) {
		AbsInput.apply( this, arguments );
		x.size && $.classAdd( this, 'w-select-multiple' );
	},
	Extend: AbsInput,
	Default: { width: -1 },
	Listener: {
		range: 'option',
		body: {
			change: {
				occupy: T,
				proxy: N,
				block: N,
				method: function() {
					this.warn( F );
					if ( this.attr( 'tip' ) === T )
						this.$t().title = this.text();
				}
			}
		}
	},
	Prototype: {
		x_nodes: $.rt( N ),
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
					return (a = $.arrFind( this.x.nodes, 'v.checked' )) ? a.value : this.x.value;
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
			var s = '', v = this.x.value, o = this.x.nodes, i = o && o.length, k = 0, e = this.x.escape !== F;
			while ( i -- ) {
				var f = o[ i ].text;
				if ( this.x.format )
					f = this.html_format( o[ i ].text, this.x.format, this.x.escape, N, o[ i ] );
				s = '<option value="' + (o[ i ].value || '') + '"' + (o[ i ].checked || o[ i ].value == v ? (k = i, ' selected') : '') + this.prop_title( o[ i ].text ) + '>' + f + '</option>' + s;
			}
			var w = this.formWidth(), z = this.x.size, t = (t === T ? (o[ k ] && o[ k ].text) : t) || '';
			return '<select class=_t id=' + this.id + 't ' + _html_on.call( this ) + ' name="' + this.input_name() + '"' + ( this.x.multiple ? ' multiple' : '' ) +
				(t ? ' title="' + $.strQuot( $.strEscape( t ) ) + '"' : '') + (z ? ' size=' + z : '') + '>' + s + '</select><div class=_cvr></div>';
		}
	}
} ),
CalendarNum = define.widget( 'CalendarNum', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		(x.focusable || x.value === p.x.focusDate) && x.focus && $.classAdd( this, 'z-on' );
		x.value === p.nowValue && $.classAdd( this, 'z-today' );
	},
	Listener: {
		body: {
			click: {
				occupy: T,
				method: function() {
					if ( ! this.isDisabled() ) {
						var p = this.parentNode, d = $.dateParse( this.val(), 'yyyy-mm-dd' );;
						p.date.setFullYear( d.getFullYear() );
						p.date.setMonth( d.getMonth() );
						p.date.setDate( d.getDate() );
						! this._disposed && this.x.focusable && this.trigger( 'focus' );
						if ( p.x.callback ) {
							this.x.focusable && this.trigger( 'focus' );
							p.x.format && !/[his]/.test( p.x.format ) && p.backfill();
						}
					}
				}
			},
			mouseOver: {
				occupy: T,
				method: function() {
					!this.isDisabled() && this.addClass( 'z-hv' );
				}
			},
			mouseOut: {
				occupy: T,
				method: function() {
					!this.isDisabled() && this.removeClass( 'z-hv' );
				}
			},
			focus: function( e ) {
				if ( this.x.focusable && ! this.isDisabled() ) {
					var b = this.parentNode.getFocus();
					if ( b && b !== this )
						$.classRemove( b.$(), 'z-on' );
					$.classAdd( this.$(), 'z-on' );
				}
			}
		}
	},
	Default: { width: -1, height: -1 },
	Prototype: {
		ROOT_TYPE: 'Calendar',
		className: '_td',
		tagName: 'td',
		val: function() { return this.x.value },
		focus: function() { this.trigger( 'focus' ) },
		isFocus: function() { return this.hasClass( 'z-on' ) },
		toggleFocus: function() { this.focus( ! this.isFocus() ) },
		prop_cls: function() { return _proto.prop_cls.call( this ) + (this.x.pad ? ' z-pad' : '') },
		html_prop:  function() { return _proto.html_prop.call( this ) + ' w-urn="' + this.val() + '"' },
		html_nodes: function() { return '<div class=_num>' + this.x.num + '</div>' + (this.x.text ? '<div class=_tx>' + this.x.text + '</div>' : '') }
	}
} ),
_calendarformatter = { date: 'yyyy-mm-dd', week: 'yyyy-mm-dd', month: 'yyyy-mm', year: 'yyyy' },
/* `calendar` */
Calendar = define.widget( 'Calendar', {
	Const: function( x ) {
		!x.date && x.focusDate && (x.date = x.focusDate);
		var d = x.date ? this._ps( x.date ) : new Date(),
			b = x.begindate && this._ps( x.begindate ), e = x.enddate && this._ps( x.enddate );
		this.face = x.face || 'date';
		this.date = $.numRange( d, b, e );
		this.formatter = _calendarformatter[ this.face ];
		this._nav_unit = this.face === 'date' ? 'm' : 'y';
		this._nav_radix = this.face === 'year' ? 10 : 1;
		this.className += ' w-calendar-' + this.face;
		W.apply( this, arguments );
		this.nowValue = this._fm( new Date() );
	},
	Helper: {
		// @a -> commander, b -> format, c -> date, d -> focusDate, e -> begindate, f -> enddate, g -> complete
		pop: function( a, b, c, d, e, f, g ) {
			var o = _widget( a ), t = !/[ymd]/.test( b ) && /[his]/.test( b ),
				x = { type: 'Calendar', face: ( b === 'yyyy' ? 'year' : b === 'yyyy-mm' ? 'month' : b === 'yyyy-ww' ? 'week' : 'date' ), format: b, callback: g, timebtn: /[ymd]/.test( b ) && /[his]/.test( b ),
					date: (t ? new Date().getFullYear() + '-01-01 ' : '') + c, begindate: e, enddate: f, fillBlank: T, pub: { focusable: T }, on: t && { ready: function() { this.popTime() } } };
			return o.exec( { type: 'Dialog', ownproperty: T, snap: { target: a.isFormWidget ? a.$( 'f' ) : a, indent: 1 }, cls: 'w-calendar-dialog w-f-dialog f-shadow-snap', width: -1, height: -1, widthMinus: 2, autoHide: T, cover: mbi, node: x,
				on: {close: function(){ o.isFormWidget && !o.contains(document.activeElement) && o.focus(F); }}} );
		}
	},
	Prototype: {
		className: 'w-calendar',
		x_nodes: $.rt( N ),
		x_childtype: $.rt( 'CalendarNum' ),		
		_fm: function( a ) {
			if ( this.face === 'week' ) {
				typeof a === _STR && (a = $.dateParse( a ));
				var b = $.dateWeek( a, this.x.cg, this.x.start );
				return b[ 0 ] + '-' + $.strPad( b[ 1 ] );
			}
			return $.dateFormat( typeof a === _STR ? $.dateParse( a, this.formatter ) : a, this.formatter );
		},
		_ps: function( a ) {
			return $.dateParse( a, this.formatter );
		},
		nav: function( e ) {
			var a = e.srcElement;
			if ( $.classAny( a, '_year' ) ) { //年
				mbi ? this.$( 'iptm' ).click() : this.popYM( 'y', a );
			} else if ( $.classAny( a, '_month' ) ) { //月
				mbi ? this.$( 'iptm' ).click() : this.popYM( 'm', a );
			} else if ( $.classAny( a, 'f-arw' ) ) { // 前 后
				this.go( $.dateAdd( this.date, this._nav_unit, (a.id === this.id + 'al' ? -1 : 1) * this._nav_radix ) );
			} else if ( $.classAny( a, '_today' ) && ! $.classAny( a, 'z-ds' ) ) { // 今天
				this.date = new Date();
				if ( this.x.callback ) {
					this.backfill();
				} else {
					var f = this.getNum( this._fm( new Date() ) );
					f ? f.focus() : this.go();
				}
			}
		},
		backfill: function() {
			var n = this.x.begindate && this._fm( this.x.begindate ), m = this.x.enddate && this._fm( this.x.enddate ), t = this._fm( this.date );
			this.x.callback && this.x.callback( (!n || n <= t) && (!m || m >= t) && this.date );
		},
		inputMonth: function() {
			var v = this.$( 'iptm' ).value, f = 'yyyy-mm';
			if ( ! v ) {
				v = this.$( 'iptm' ).value = $.dateFormat( this.date, f );
			}
			v = $.dateParse( v, f );
			this.date.setFullYear( v.getFullYear() );
			this.date.setMonth( v.getMonth() );
			this.go();
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
		go: function( d ) {
			if ( this.x.src ) {
				this.cmd( { type: 'Ajax', src: this.x.src, success: function( x ) {
					W.isCmd( x ) ? this.cmd( x ) : this.replace( x );
				} }, this._fm( d || this.date ) );
			} else {
				d && (this.date = d);
				$.replace( this.$(), this.html() );
			}
		},
		getNum: function( a ) {
			if ( a.length != this.formatter.length ) {
				a = $.dateFormat( $.dateParse( a ), this.formatter );
			}
			var b = $.get( '[w-urn="' + a + '"]', this.$() );
			return b && _widget( b );
		},
		getFocus: function() {
			var b = $.get( '._td.z-on', this.$() );
			return b && _widget( b );
		},
		focus: function( a ) {
			var b = this.getNum( a );
			b && b.focus();
		},
		// 执行某个日期的点击事件
		click: function( a ) {
			var b = this.getNum( a );
			b && b.click();
		},
		// 年、月的浮动选择器
		popYM: function( a, e ) {
			var Y = a === 'y', M = a === 'm', d = this.date, h = 18, l = M ? 12 : 10, c = [], g = d.getMinutes(), self = this,
				b = Y ? d.getFullYear() : d.getMonth() + 1,
				y = Y ? (b - 5) : 1,
				s = (M ? '' : '<div class="_b _scr">-</div>') + '<div class="_wr">',
				htm = function() {
					var d = e.type == 'CalendarNum' ? self._ps( e.val() ) : self.date, g = $.dateFormat( d, 'yyyy-mm-dd' ), f = Y ? 'yyyy' : 'yyyy-mm',
						n = self.x.begindate && $.dateFormat( self._ps( self.x.begindate ), f ), m = self.x.enddate && $.dateFormat( self._ps( self.x.enddate ), f );
					for ( var i = 0, t, s, r = ''; i < l; i ++ ) {
						t = y + i;
						s = Y ? t : (d.getFullYear() + '-' + $.strPad( t ));
						r += '<div class="_b _i' + ( t == b ? ' _c' : '' ) + ( (n && n > s) || (m && m < s) ? ' z-ds' : '' ) + '">' + t + '</div>';
					}
					return r;
				};
			s += htm() + '</div>' + ( M ? '' : '<div class="_b _scr">+</div>' );
			var d = this.exec( { type: 'Dialog', ownproperty: T, width: 60, height: h * 12, cls: 'w-calendar-select', snap: { target: e, position: e.dropSnapType || 'cc' }, autoHide: T, node: { type: 'Html', text: s },
					on: { mouseleave: function(){ this.close() }, close: function() { clearTimeout( t ); Q( d.$() ).off(); } }
				} ),
				r = Q( '._wr', d.$() ),
				f = function( k ) {
					if ( Y ) {
						y = k === '+' ? y + l : y - l;
						Y && (y = $.numRange( y, 1900 ));
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
				Y ? self.date.setFullYear( h ) : self.date.setMonth( h - 1 );
				d.close();
				self.go();
			} );
		},
		// 弹出时间选择器 /@a -> el
		popTime: function( a ) {
			if ( this._dlg_time ) {
				this._dlg_time.close();
				delete this._dlg_time;
				a && (a.innerHTML = Loc.calendar.picktime);
			} else {
				var b = Dialog.get( this.$() ), c = [], d = this.date, f = this.x.format, h = [], self = this,
					y = { set: { h: 'setHours', i: 'setMinutes', s: 'setSeconds' }, get: { h: 'getHours', i: 'getMinutes', s: 'getSeconds' } },
					bd = self.x.begindate && self._ps( self.x.begindate ), bh = bd && bd.getHours(), bi = bd && bd.getMinutes(), bs = bd && bd.getSeconds(),
					ed = self.x.enddate && self._ps( self.x.enddate ),     eh = ed && ed.getHours(), ei = ed && ed.getMinutes(), es = ed && ed.getSeconds(),
					ld = $.dateFormat( d, 'yyyy-mm-dd' );
					sameBeginDay = bd && $.dateFormat( bd, 'yyyy-mm-dd' ) == ld;
					sameEndDay   = ed && $.dateFormat( ed, 'yyyy-mm-dd' ) == ld;
				function list( t, m ) {
					var sameBeginHour = sameBeginDay && bh == d.getHours(),
						sameEndHour   = sameEndDay && eh == d.getHours(),
						sameBeginMin = sameBeginHour && bi == d.getMinutes(),
						sameEndMin   = sameEndHour && ei == d.getMinutes();
					for ( var i = 0, s = '', ds; i < m; i ++ ) {
						ds = t === 'h' ? ((sameBeginDay && bh > i) || (sameEndDay && eh < i)) :
							 t === 'i' ? ((sameBeginHour && bi > i) || (sameEndHour && ei < i)) :
							 ((sameBeginMin && bs > i) || (sameEndMin && es < i));
						s += '<div class="_o' + (ds ? ' z-ds' : '') + '" data-v="' + i + '" ' + _event_zhover + '>' + $.strPad( i ) + '</div>';
					}
					c.push( { type: 'Html', id: t, cls: c.length ? ' _bl' : '', width: '*', widthMinus: c.length ? 1 : 0, scroll: T, text: s, data: { max: m } } );
					h.push( { type: 'Html', width: '*', cls: '_th' + (h.length ? ' _bl' : ''), widthMinus: c.length ? 1 : 0, heightMinus: 1, text: Loc.calendar[ t ] } );
				}
				function recls( g ) {
					if ( !g || !(sameBeginDay || sameEndDay) ) return;
					var t = g.x.id,
						sameBeginHour = sameBeginDay && bh == d.getHours(),
						sameEndHour   = sameEndDay && eh == d.getHours(),
						sameBeginMin = sameBeginHour && bi == d.getMinutes(),
						sameEndMin   = sameEndHour && ei == d.getMinutes();
					Q( '.z-ds', g.$() ).removeClass( 'z-ds' );
					if ( (t === 'i' && (sameBeginHour || sameEndHour)) || (t === 's' && (sameBeginMin || sameEndMin)) ) {
						for ( var i = 0, c = Q( '._o', g.$() ); i < 60; i ++ ) {
							if ( t === 'i' ? ((sameBeginHour && bi > i) || (sameEndHour && ei < i)) : ((sameBeginMin && bs > i) || (sameEndMin && es < i)) )
								c.eq( i ).addClass( 'z-ds' );
						}
						if ( Q( '.z-on', g.$() ).hasClass( 'z-ds' ) ) {
							Q( '.z-on', g.$() ).removeClass( 'z-on' );
							var o = Q( '._o:not(.z-ds)', g.$() ).first();
							if ( o.length ) {
								o.addClass( 'z-on' );
								d[ y.set[ g.x.id ] ]( o.attr( 'data-v' ) );
								_scrollIntoView( o[ 0 ] );
							}
						}
					}
				}
				~f.indexOf( 'h' ) && list( 'h', 24 );
				~f.indexOf( 'i' ) && list( 'i', 60 );
				~f.indexOf( 's' ) && list( 's', 60 );
				this._dlg_time = this.exec( { type: 'Dialog', ownproperty: T, cls: 'w-calendar-time-dlg f-white', width: b.width() - 2, height: b.height() - 33, snap: { target: b, position: '11' }, autoHide: T, node: {
					 type: 'View', node: {
					 type: 'Vert', nodes: [
					 	{ type: 'Horz', height: 29, nodes: h },
					 	{ type: 'Horz', height: '*', nodes: c,
					 		on: {
					 			// 让时分秒的初始焦点项对齐
					 			ready: function() {
						 			var r = [], k = 0, i = 0, v, n = 60, t;
						 			for ( ; i < this.length; i ++ ) {
						 				v = d[ y.get[ this[ i ].x.id ] ]();
						 				r.push( $.get( '._o[data-v="' + v + '"]', this[ i ].$() ) );
						 				t = Math.min( v, this[ i ].x.data.max - v );
						 				if ( n > t ) { n = t; k = i; }
						 			}
						 			_scrollIntoView( r[ k ] );
						 			for ( i = 0, t = $.bcr( r[ k ] ).top - $.bcr( this[ k ].$() ).top; i < r.length; i ++ ) {
						 				$.classAdd( r[ i ], 'z-on' );
						 				i !== k && _scrollIntoView( r[ i ], 'top+' + t );
						 			}
						 		},
								click: function( e ) {
									var o = e.srcElement, g = $.widget( o ), m = g.ownerView;
									if ( $.classAny( o, '_o' ) && !$.classAny( o, 'z-ds' ) ) {
										Q( '._o', g.$() ).removeClass( 'z-on' );
										$.classAdd( o, 'z-on' );
										var p = g.x.id;
										d[ y.set[ p ] ]( o.getAttribute( 'data-v' ) );
										p === 'h' && (recls( m.find( 'i' ) ), recls( m.find( 's' ) ));
										p === 'i' && recls( m.find( 's' ) );
									}
								}
					 		}
					 	}
					 ] }
				} } );
				a && (a.innerHTML = Loc.calendar.backdate);
			}
		},
		getItemMap: function() {
			if ( this.x.nodes ) {
				for ( var i = 0, n = this.x.nodes, r = {}; i < n.length; i ++ )
					r[ n[ i ].value ] = n[ i ];
				return r;
			}
		},
		html_ok: function() {
			return this.x.callback ? '<div class=w-calendar-time>' + (this.x.timebtn ? '<div class=_time onclick=' + evw + '.popTime(this)>' + Loc.calendar.picktime + '</div>' : '') + '<div class=_ok onclick=' + evw + '.backfill(this)>' + Loc.confirm + '</div></div>' : '';
		},
		html_date: function() {
			var a = this.date, b = new Date( a.getTime() ), c = b.getMonth(), d = new Date( b.getTime() ), e = [], f = this.x.focusDate ? this.x.focusDate.slice( 0, 10 ) : (this.x.format && this._fm( a )), 
				n = this.x.begindate && this._fm( this.x.begindate ), m = this.x.enddate && this._fm( this.x.enddate ), t = this._fm( new Date() ), o = this.getItemMap(),
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( this.id + 'al', mbi ? 'l5' : 'l2' ) + Loc.ps( Loc.calendar.ym, a.getFullYear(), c + 1 ) + $.arrow( this.id + 'ar', mbi ? 'r5' : 'r2' ) +
					'<input type=month id=' + this.id +'iptm value="' + $.dateFormat( b, 'yyyy-mm' ) + '" class=_iptm onchange=' + evw + '.inputMonth()><div class="_today' + ((n && n > t) || (m && m < t) ? ' z-ds' : '') + '">' + Loc.calendar.today + '</div></div>' +
					'<div class=w-calendar-body><table class=w-calendar-tbl cellspacing=0 cellpadding=5><thead><tr><td>' + Loc.calendar.day_title.join( '<td>' ) + '</thead><tbody>';
			b.setDate( 1 );
			d.setDate( new Date( b.getFullYear(), c + 1, 0 ).getDate() );
			var g = 7 - (b.getDay() + d.getDate()) % 7;
			if ( g && g < 7 )
				d.setDate( d.getDate() + g );
			if ( b.getDay() > 0 )
				b.setDate( 1 - b.getDay() );
			if ( this.x.fillBlank ) {
				g = 1 + (d.getTime() - b.getTime()) / $.DATE_DAY;
				g < 42 && d.setDate( d.getDate() + 42 - g );
			}
			while ( b <= d ) {
				var v = this._fm( b ),
					t = { value: v, num: b.getDate(), pad: b.getMonth() !== c, status: (n && n > v) || (m && m < v) ? 'disabled' : N, focus: f === v };
				o && o[ v ] && $.merge( t, o[ v ] );
				e.push( (b.getDay() === 0 ? '<tr>' : '') + this.add( t ).html() );
				b.setDate( b.getDate() + 1 );
			}
			return s + e.join( '' ) + '</tbody></table></div>' + this.html_ok();
		},
		html_week: function() {
			var a = this.date, w = $.dateWeek( a, this.x.cg, this.x.start ), y = w[ 0 ], n = this.x.begindate, m = this.x.enddate, t = this._fm( new Date() ),
				b = $.dateWeek( new Date( y, 11, 31 ), this.x.cg, this.x.start ), e = [], f = this.x.focusDate && this._fm( this.x.focusDate ), o = this.getItemMap(),
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( this.id + 'al', mbi ? 'l5' : 'l2' ) + Loc.ps( Loc.calendar.y, y )  + $.arrow( this.id + 'ar', mbi ? 'r5' : 'r2' ) +
				'<div class="_today' + ((n && n > t) || (m && m < t) ? ' z-ds' : '') + '">' + Loc.calendar.weeknow + '</div></div><div class=w-calendar-body><table class=w-calendar-tbl cellspacing=0 cellpadding=5><tbody>';
			this._year = y;
			if ( b[ 0 ] !== y )
				b = $.dateWeek( new Date( y, 11, 31 - 7 ), this.x.cg, this.x.start );
			for ( var i = 1, l = b[ 1 ]; i <= l; i ++ ) {
				var v = y + '-' + $.strPad( i ), g = { value: v, num: i, status: (n && n > v) || (m && m < v) ? 'disabled' : '', focus: f === v };
				o && o[ v ] && $.extend( g, o[ v ] );
				e.push( ( (i - 1) % 7 === 0 ? '<tr>' : '' ) + this.add( g ).html() );
			}
			if ( (n = 7 - (i % 7)) > 0 && n < 7 ) {
				while ( n -- ) e.push( '<td class="_td z-pad">&nbsp;' );
			}
			return s + e.join( '' ) + '</tbody></table></div>' + this.html_ok();
		},
		html_month: function() {
			var a = this.date, e = [], f = this.x.focusDate ? this.x.focusDate.slice( 0, 7 ) : (this.x.format && this._fm( a )), y = a.getFullYear(),
				n = this.x.begindate && this._fm( this.x.begindate ), m = this.x.enddate && this._fm( this.x.enddate ), t = this._fm( new Date() ), o = this.getItemMap(),
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( this.id + 'al', mbi ? 'l5' : 'l2' ) + Loc.ps( Loc.calendar.y, y ) + $.arrow( this.id + 'ar', mbi ? 'r5' : 'r2' ) +
					'<div class="_today' + ((n && n > t) || (m && m < t) ? ' z-ds' : '') + '">' + Loc.calendar.monthnow + '</div></div><div class=w-calendar-body><table class=w-calendar-tbl cellspacing=0 cellpadding=5><tbody>';
			for ( var i = 0; i < 12; i ++ ) {
				var v = y + '-' + $.strPad( i + 1 ), g = { value: v, num: Loc.calendar.monthname[ i ], status: (n && n > v) || (m && m < v) ? 'disabled' : '', focus: f === v };
				o && o[ v ] && $.extend( g, o[ v ] );
				e.push( (i % 4 === 0 ? '<tr class=_tr>' : '') + this.add( g ).html() );
			}
			return s + e.join( '' ) + '</tbody></table></div>' + this.html_ok();
		},
		html_year: function() {
			var a = this.date, e = [], f = _number( this.x.focusDate ? this.x.focusDate.slice( 0, 7 ) : (this.x.format && this._fm( a )) ), y = a.getFullYear() - ( a.getFullYear() % 10 ) - 1,
				n = this.x.begindate && this._fm( this.x.begindate ), m = this.x.enddate && this._fm( this.x.enddate ), t = this._fm( new Date() ), o = this.getItemMap(),
				s = '<div class="w-calendar-head f-clearfix" onclick=' + evw + '.nav(event)>' + $.arrow( this.id + 'al', mbi ? 'l5' : 'l2' ) + Loc.ps( Loc.calendar.y, (y + 1) + ' - ' + (y + 10) ) + $.arrow( this.id + 'ar', mbi ? 'r5' : 'r2' ) +
				'<div class="_today' + ((n && n > t) || (m && m < t) ? ' z-ds' : '') + '">' + Loc.calendar.yearnow + '</div></div><div class=w-calendar-body><table class=w-calendar-tbl cellspacing=0 cellpadding=5><tbody>';
			for ( var i = 0; i < 12; i ++ ) {
				var v = y + i, g = { value: v, num: y + i, status: (n && n > v) || (m && m < v) ? 'disabled' : '', focus: f === v };
				o && o[ v ] && $.extend( g, o[ v ] );
				e.push( ( i % 4 === 0 ? '<tr class=_tr>' : '' ) + this.add( g ).html() );
			}
			return s + e.join( '' ) + '</tbody></table></div>' + this.html_ok();
		},
		html_nodes: function() {
			return this[ 'html_' + this.face ]();
		}
	}
} ),
_date_formtype = {
	'yyyy-mm-dd hh:ii': 'datetime-local',
	'yyyy-mm-dd': 'date',
	'yyyy-mm': 'month',
	'yyyy': 'month',
	'hh:ii': 'time'
},
/* `datepicker` */
DatePicker = define.widget( 'DatePicker', {
	Const: function( x, p ) {
		Text.apply( this, arguments );
		if ( ! x.format )
			x.format = 'yyyy-mm-dd';
		var a = $.strEscape( x.value == N ? '' : ('' + x.value) ), r = '';
		if ( a ) {
			for ( var i = 0, a = a.split( ',' ), l = a.length, r = []; i < l; i ++ )
				r.push( $.dateFormat( $.dateParse( a[ i ] ), this.x.format ));
			r = r.join( ',' );
		}
		x.value = r;
		this.defaults( { width: 67 + this.x.format.length * 6 + (this.x.multiple ? 20 : 0) } );
	},
	Extend: Text,
	Listener: {
		body: {
			click: {
				occupy: T,
				method: function() {
					if ( ! mbi && this.isNormal() )
						!this.x.multiple || !this.val() ? this.popCalendar() : this.popList();
				}
			},
			input: mbi && {
				occupy: T,
				method: function() {
					this.$( 'a' ).innerHTML = this.val();
					this.focus( F );
				}
			}
		}
	},
	Prototype: {
		dropSnapType: 'v',
		placeholder_type: 'placeholder_select',
		form_minus: function() {
			var w = this.x.width;
			return (w == N || w < 0 ? 0 : (this.label ? this.label.outerWidth() : 0));
		},
		validHooks: {
			minValue: function( b, v ) {
				return $.dateParse( v, this.x.format ) < $.dateParse( this.vv( 'minValue', b ), this.x.format );
			},
			maxValue: function( b, v ) {
				return $.dateParse( v, this.x.format ) > $.dateParse( this.vv( 'maxValue', b ), this.x.format );
			},
			compare: function( b, v, c, d ) {
				if ( this.x.format && c.x.format ) {
					var f = (this.x.format.length > c.x.format.length ? this : c).x.format;
					v = $.dateFormat( $.dateParse( v, this.x.format ), f );
					d = $.dateFormat( $.dateParse( d, c.x.format ), f );
				}
				return ! eval( '"' + $.strQuot( v ) + '"' + (b.compare.mode || '==') + '"' + $.strQuot( d ) + '"' );
			},
			valid: function( b, v ) {
				if ( this.x.multiple )
					return;
				if ( v ) {
					var c = v.replace( /\b(\d)\b/g, '0$1' ), d = $.dateParse( c, this.x.format ), y = d.getFullYear(), m = cfg.max_year || 3000, n = cfg.min_year || 1000;
					if ( y >= m || y <= n )
						return _form_err.call( this, b, 'time_exceed', [ n, m ] );
					if ( c != $.dateFormat( d, this.x.format ) ) {
						return _form_err.call( this, b, 'time_format', [ $.dateFormat( d, this.x.format ) ] );
					}
				}
				if ( b && this.vv( 'beforeNow', b ) && v && v > $.dateFormat( new Date(), this.x.format ) )
					return _form_err.call( this, b, 'beforeNow', [ b.beforeNow ] );
				if ( b && this.vv( 'afterNow', b ) && v && v < $.dateFormat( new Date(), this.x.format ) )
					return _form_err.call( this, b, 'afterNow', [ b.afterNow ] );
				if ( this === this.parentNode.begin ) {
					var c = this.parentNode.end, d = c.val();
					if ( v && d && this.validHooks.compare.call( this, { compare: { mode: '<=' } }, v, c, d ) )
						return _form_err.call( this, b, 'compare', [ '<=', c.x.label ] );
				}
			}
		},
		$v: function() { return $( this.id + (this.x.multiple ? 'v' : 't') ) },
		val: function( a ) {
			if ( a == N )
				return this.$v() ? this.$v().value.replace( 'T', ' ' ) : this.x.value;
			if ( this.$() ) {
				a = $.strTrim( a );
				mbi && (a = a.replace( ' ', 'T' ));
				this.$v().value = a;
				this.$v() && this.checkPlaceholder();
				this.x.multiple && this.text( this.v2t( a ) );
				this.trigger( 'change' );
			} else
				this.x.value = a;
		},
		text: function( t ) {
			return this.x.multiple ? (t != N ? (this.$t().innerText = t) : this.$t().innerText) : Text.prototype.text.call( this, t );
		},
		v2t: function( v ) {
			for ( var i = 0, b = v.split( ',' ), s = []; v && i < b.length; i ++ )
				s.push( '"' + b[ i ] + '"' );
			return s.join( ' ' );
		},
		clkhdr: mbi ? function( e ) {
			this.$t().click();
		} : Text.prototype.clkhdr,
		popList: function() {
			for ( var i = 0, v = this.val().split( ',' ), b = []; i < v.length; i ++ )
				b.push( this.li_str( v[ i ] ) );
			b.push( this.li_str() );
			this.mh = Math.floor(($.height() - this.height()) / 60) * 30 + 2;
			var h = (v.length + 1) * 30 + 2, c = this.list, d = c && c.isShow();
			this.closePop();
			if ( ! d ) 
				this.list = this.exec( { type: 'Dialog', ownproperty: T, cls: 'w-calendar-dialog w-muldate-dialog w-f-dialog f-shadow-snap', width: 200, height: Math.min( this.mh, h ), heightMinus: 2, widthMinus: 2, autoHide: T, snap: { target: this, indent: 1 },
					node: { type: 'Html', text: b.join( '' ), scroll: T }, on: { close: function() { this.commander.focus(F) } } } );
			this.focus();
		},
		popCalendar: function() {
			var b = $.dateFormat( new Date, this.x.format ), c = this.cal, d = this.x.validate, e = c && c.isShow(), f = d && d.compare, g = f && f.mode,
				m = this.vv( 'maxValue' ) || (this.vv( 'beforeNow' ) && b), n = this.vv( 'minValue' ) || (this.vv( 'afterNow' ) && b), p = this.parentNode, t, v = this.val() || b;
			if ( (this === p.begin && p.end && (t = p.end.val())) || (g && g.indexOf( '<' ) == 0 && (t = this.ownerView.fv( f.target ))) )
				m = m ? (m > t ? t : m) : t;
			if ( (this === p.end && p.begin && (t = p.begin.val())) || (g && g.indexOf( '>' ) == 0 && (t = this.ownerView.fv( f.target ))) )
				n = n ? (n < t ? t : n) : t;
			this.closePop();
			if ( ! e ) {
				var self = this;
				this.cal = Calendar.pop( this, this.x.format, v, v, n, m, function( d ) { d && self.val( $.dateFormat( d, self.x.format ) ); self.focus(); self.cal.close(); } );
			}
			this.focus();
		},
		// @a -> el, b -> act
		addValue: function( a, b ) {
			var self = this, t = Q( a ).closest( 'table' );
			if ( b == '+' ) {
				Calendar.pop( a, this.x.format, N, N, N, N, function( v ) {
					v = $.dateFormat( v, self.x.format );
					t.before( self.li_str( v ) );
					self.li_ref();
					Dialog.get( a ).close();
				} );
			} else if ( b == '-' ) {
				t.remove();
				self.li_ref();
			} else {
				var d = t.data( 'value' );
				Calendar.pop( a, this.x.format, d, d, N, N, function( v ) {
					v = $.dateFormat( v, self.x.format );
					a.innerText = v;
					t.attr( 'data-value', v );
					self.li_ref();
				} );
			}
		},
		li_ref: function() {
			var r = Q( 'table[data-value]', this.list.$() ).map( function() { return this.getAttribute( 'data-value' ) } ).get();
			this.val( r.join() );
			r.length ? this.list.height( Math.min( (r.length + 1) * 30 + 2, this.mh ) ) : this.list.close();
		},
		li_str: function( v ) {
			var k = abbr( this ) + '.addValue';
			return '<table width=100% height=30 cellspacing=0 cellpadding=0' + (v ? ' data-value="' + v + '"' : '') + '><tr><td>&nbsp; ' + (v ? '<a href=javascript: onclick=' + k + '(this,"=")>' + v + '</a>' : '') + '<td width=70 align=center>' +
				$.image( '.f-i-minus ._i', { style: 'visibility:' + (v ? 'visible' : 'hidden'), click: k + '(this,"-")' } ) + ' &nbsp; ' + $.image( '.f-i-plus ._i', { click: k + '(this,"+")' } ) + '</table>';
		},
		closePop: function() {
			this.cal && this.cal.close();
			this.list && this.list.close();
		},
		html_btn: function() {
			return '<label ' + (mbi ? 'for="' + this.id + 't"' : 'onclick=' + eve) + ' class="f-boxbtn _pick"><i class="f-i _pick_i"></i><i class=f-vi></i></label>';
		},
		input_prop_value: function() {
			var v = this.x.value;
			return mbi ? v.replace( ' ', 'T' ) : v;
		},
		html_input: function() {
			var v = (mbi || this.x.multiple) && this.input_prop_value();
			return mbi ? '<input type=' + (_date_formtype[ this.x.format ] || 'date') + this.input_prop() + '><label id="' + this.id + 'a" for="' + this.id + 't" class="f-fix _a">' + v.replace( 'T', ' ' ) + '</label>' :
				this.x.multiple ? '<input type=hidden id=' + this.id + 'v name="' + this.x.name + '" value="' + v + '"><div id=' + this.id + 't class="f-fix _t"' + _html_on.call( this ) + '>' + this.v2t( v ) + '</div>' : '<input type=text' + this.input_prop() + '>';
		}
	}
} ),
/* `spinner` */
Spinner = define.widget( 'Spinner', {
	Const: function( x, p ) {
		AbsInput.apply( this, arguments );
		x.format && x.value && (x.value = $.numFormat( x.value, x.format.length, x.format.separator, x.format.rightward ));
	},
	Extend: Text,
	Default: {
		width: mbi ? 160 : 100
	},
	Listener: {
		body: {
			format: {
				occupy: function() { return this.x.format },
				proxy: ie ? 'cut paste keyUp' : 'input',
				method: function( e ) {
					ie ? setTimeout( $.proxy( this, this.doFormat ) ) : this.doFormat();
				}
			},
			beforeDeactivate: {
				occupy: ie,
				method: function( e ) {
					_superTrigger( this, OnlineBox, e );
				}
			}
		}
	},
	Prototype: {
		_csr_pos: 0,
		form_minus: function() {
			var w = this.x.width;
			return w == N || w < 0 ? 0 : (this.label ? this.label.outerWidth() : 0);
		},
		validHooks: {
			minValue: function( b, v ) {
				return _number( v ) < _number( this.vv( 'minValue', b ) );
			},
			maxValue: function( b, v ) {
				return _number( v ) > _number( this.vv( 'maxValue', b ) );
			},
			compare: function( b, v, c, d ) {
				return ! eval( _number( v ) + (b.compare.mode || '==') + _number( d ) );
			},
			valid: function( b, v ) {
				this.x.format && (v = v.replace( RegExp( this.x.format.separator || ',', 'g' ), '' ));
				if ( v ) {
					if ( isNaN( v ) || /\s/.test( v ) )
						return _form_err.call( this, b, 'number_invalid' );
					var d = this.x.decimal;
					if ( ! d && ~v.indexOf( '.' ) )
						return _form_err.call( this, b, 'number_integer' );
					if ( d && d > 0 && $.strFrom( v, '.' ).length > d )
						return _form_err.call( this, b, 'number_decimal_digit', [ d ] );
				}
				if ( this == this.parentNode.begin ) {
					var c = this.parentNode.end, d = c.val();
					if ( v && d && this.validHooks.compare.call( this, { compare: { mode: '<=' } }, v, c, d ) )
						return _form_err.call( this, b, 'compare', [ '<=', c.x.label ] );
				}
			}
		},
		val: function( a ) {
			Text.prototype.val.call( this, a );
			a != N && this.x.format && this.trigger( 'format' );
			return this.$t().value;
		},
		doFormat: function() {
			if ( ! this.x.format )
				return;
			var v = this.$t().value, s = this.x.format.separator || ',', r = RegExp( s.replace( /[\\^$.*+?()[\]{}|]/g, '\\$&' ), 'g' ), t = $.numFormat( v, this.x.format.length, s, this.x.format.rightward );
			if ( v !== t ) {
				var n = OnlineBox.prototype.getSelectionStart.call( this ), b = v.slice( 0, n ).replace( r, '' );
				this.$t().value = t;
				if ( document.activeElement === this.$t() ) {
					var a = t.slice( 0, n ).replace( r, '' );
					$.rngCursor( this.$t(), n + (b.length - a.length) );
				}
			}
		},
		step: function( a ) {
			if ( this.isNormal() ) {
				var d = this.x.validate, m = d && d.maxValue, n = d && d.minValue, v = $.numAdd( _number( this.val() ), a * (this.x.step || 1) );
				m != N && (v = Math.min( m, v ));
				n != N && (v = Math.max( n, v ));
				this.focus();
				this.val( v );
			}
		},
		input_prop_value: function() {
			var v = $.strEscape( this.x.value == N ? '' : '' + this.x.value );
			return v ? $.numDecimal( v, this.x.decimal ) : '';
		},
		html_btn: function() {
			return this.x.noButton ? '' : mbi ? '<cite class="f-inbl _l" onclick=' + evw + '.step(-1)>&minus;</cite><cite class="f-inbl _r" onclick=' + evw + '.step(1)>&plus;</cite>' :
				'<cite class=_b><em onclick=' + evw + '.step(1)><i class=f-vi></i><i class="f-arw f-arw-t2"></i></em><em onclick=' + evw + '.step(-1)><i class=f-vi></i><i class="f-arw f-arw-b2"></i></em></cite>';
		},
		html_input: function() {
			return mbi ? '<input type=number' + this.input_prop() + '>' :
				'<input type=text' + this.input_prop() + ' w-valuetype="number">';
		}
	}
} ),
/* `slider` */
/* 值的范围默认 0-100 */
Slider = define.widget( 'Slider', {
	Extend: AbsForm,
	Default: {
		width: '*', widthMinus: 0
	},
	Listener: {
		body: {
			ready: function() {
				this.fixPos();
			},
			resize: function( e ) {
				_superTrigger( this, AbsForm, e );
				this.css( 't', 'width', this.formWidth() );
				this.val( this.val() );
			},
			change: N
		}
	},
	Prototype: {
		fixPos: function( v ) {
			v == N && (v = this.x.value);
			if ( v != N ) {
				var l = this._left( v );
				this.css( 'thumb', 'left', l ).css( 'track', 'width', l == 0 ? 0 : l + this.thumbWidth() );
			}
		},
		$v: function() { return $( this.id + 'v' ) },
		val: function( a ) {
			if ( a == N )
				return this.$v().value;
			a = $.numRange( a, this.min(), this.max() );
			this.$v().value = a;
			this.fixPos( a );
		},
		hover: function( a, b ) {
			this.isNormal() && $.classAdd( a, 'z-hv' );
		},
		hout: function( a, b ) {
			this.isNormal() && ! a.contains( b.toElement ) && $.classRemove( a, 'z-hv' );
		},
		usa: function() {
			return this.isNormal();
		},
		dragStart: function( a, b ) {
			if ( ! this.usa() )
				return;
			var x = b.targetTouches ? b.targetTouches[ 0 ].clientX : b.clientX, m = this.max(), n = this.min(), f = _number( a.style.left ), v = this.$v().value, 
				g = this.thumbWidth(), w = this.formWidth() - g, self = this, t = this.attr( 'tip' ) === T ? '$0' : this.attr( 'tip' ),
				d = this.tip( v || this.min() );
			self.trigger( 'dragStart' );
			$.moveup( function( e ) {
				var c = e.targetTouches ? e.targetTouches[ 0 ].clientX : e.clientX,
					l = $.numRange(f + c - x, 0, w);
				v = Math.floor( Math.floor((m - n) * l / w) );
				a.style.left = l + 'px';
				$( self.id + 'track' ).style.width = (l + g) + 'px';
				d && d.snapTo( a ).text( self.tipText( v ) );
				$( self.id + 'v' ).value = v;
				self.addClass( 'z-drag' );
				self.trigger( 'drag', [ v ] );
			}, function( e ) {
				d && d.close();
				self.trigger( 'drop', [ v ] );
				self.removeClass( 'z-drag' );
			} );
		},
		max: function() {
			var v = this.x.validate && this.x.validate.maxValue;
			return v == N ? 100 : v;
		},
		min: function() {
			var v = this.x.validate && this.x.validate.minValue;
			return v == N ? 0 : v;
		},
		thumbWidth: function() {
			if ( ! this._thumb_wd )
				this._thumb_wd = this.$( 'thumb' ).offsetWidth;
			return this._thumb_wd;
		},
		tip: function( v ) {
			var t = this.attr( 'tip' ) === T ? '$value' : this.attr( 'tip' );
			if ( t && ! this._tip ) {
				this._tip = this.exec( { type: 'Tip', text: this.tipText( v ), escape: F, snap: { target: this.$( 'thumb' ), position: 'tb,bt' }, closable: F, on: { close: 'this.parentNode._tip=null' } } );
			}
			return this._tip;
		},
		tipText: function( v ) {
			var t = this.attr( 'tip' ) === T ? '$value' : this.attr( 'tip' );
			return '<code class="f-inbl w-slider-tip-text">' + this.formatStr( t, { value: v } ) + '</code>';
		},
		_left: function( v ) {
			var m = this.max(), n = this.min();
			return (this.formWidth() - this.thumbWidth()) * (_number( v ) - n) / (m - n);
		},
		form_cls: function() {
			return 'f-nv';
		},
		html_nodes: function() {
			var w = this.formWidth(), v = this.x.value == N ? 0 : this.x.value;
			return '<input type=hidden id=' + this.id + 'v name="' + this.input_name() + '" value="' + v + '"' + (this.isDisabled() ? ' disabled' : '') + '><div id=' + this.id +
				't class=_t style="width:' + w + 'px"><div id=' + this.id + 'track class=_track></div><div id=' + this.id + 'thumb class=_thumb ' + ev_down + evw + '.dragStart(this,event) onmouseover=' + evw + '.hover(this,event) onmouseout=' + evw + '.hout(this,event)><i class=f-vi></i><i class="f-i _i"></i></div></div>' + this.html_placeholder();
		}
	}
} ),
/* `Jigsawimg` */
JigsawImg = define.widget( 'JigsawImg', {
	Extend: AbsSection,
	Prototype: {
		load: function( fn ) {
			this.loadData( N, function( d ) {
				var p = this.parentNode, r = this.getResult();
				if ( ! r || (d instanceof Error) ) {
					r = { error: { msg: Loc.form.jigsaw_errorimg } };
				}
				p.loaded = T;
				if ( r.error ) {
					p.lock( r );
				} else {
					r.minValue != N && p.setValidate( { minValue: r.minValue } );
					r.maxValue != N && p.setValidate( { maxValue: r.maxValue } );
					p.trigger( 'load' );
					fn && fn.call( this );
				}
			} );
		},
		init_x_error: function() {
			this.parentNode.lock( { error: { msg: Loc.ps( Loc.server_error, 'error' ) } } );
		}
	}
} ),
/* `Jigsawauth` */
JigsawAuth = define.widget( 'JigsawAuth', {
	Extend: AbsSection,
	Prototype: {
		load: function( fn ) {
			this.loadData( N, function() {
				var x = this.getResult(), p = this.parentNode;
				p.success( x && x.success );
				p.valid();
				if ( p.isSuccess() ) {
					p.more && p.more.close();
					p.addClass( 'z-success' );
					Q( p.$( 'pht' ) ).html( p.html_info( x ) );
					var t = Q( p.$( 'thumb' ) ), l = _number( t.css( 'left' ) ), w = p.more.innerWidth(),
						f = l + (t.width() / 2) < (w / 2);
					Q( p.$( 'ph' ) ).css( { left: f ? l + t.width() : 0, right: f ? 0 : w - l } );
				} else {
					if ( p.isNormal() )
						p.reload( p.more.vis );
					p.val( p.min() );
					p.removeClass( 'z-on z-success' );
				}
				p.removeClass( 'z-drag z-on z-authing' );
				$.classRemove( p.$( 'thumb' ), 'z-hv' );
				p.trigger( 'auth' );
			} );
		}
	}
} ),
/* `jigsaw` */
Jigsaw = define.widget( 'Jigsaw', {
	Const: function( x ) {
		Slider.apply( this, arguments );
		this.more = this.add( { type: 'Dialog', ownproperty: T, cls: 'w-jigsaw-dialog', width: 'javascript:return this.parentNode.popWidth()',
			height: 'javascript:return this.parentNode.popHeight()', snap: { target: this.id + 'f', position: 'tb,bt', indent: -1 }, memory: T, autoHide: T, hoverDrop: T, node: {
			type: 'View',
			node: {
				type: 'Html', cls: 'f-rel', id: 'img', format: 'javascript:return ' + abbr( this ) + '.html_img()'
			}
		}, on: { resize: 'this.parentNode.fixImg()' } }, -1 );
		x.img && (this.img = new JigsawImg( x.img, this, -1 )).load();
		x.auth && (this.auth = new JigsawAuth( x.auth, this, -1 ));
	},
	Extend: Slider,
	Default: {
		widthMinus: 2
	},
	Listener: {
		tag: '',
		body: {
			mouseOver: {
				occupy: T,
				proxy: br.mobile ? 'touchStart' : N,
				method: function() {
					if ( ! this.usa() ) return;
					if ( this.loaded ) {
						this.pop( T );
					} else
						this.addEventOnce( 'load.mouseOver', this.pop );
				}
			},
			mouseOut: {
				occupy: T,
				method: function() {
					if ( ! this.usa() ) return;
					this.removeEvent( 'load.mouseOver' );
				}
			},
			dragStart: function( e ) {
				this.pop();
				this.more.keepHover( T );
			},
			drag: function( e, v ) {
				if ( this.more ) {
					this.draging = T;
					var a = Q( '._small', this.more.$() ), m = this.max(), n = this.min();
					a.css( 'left', (this.more.innerWidth() - a.width()) * (v / (m - n)) );
				}
			},
			drop: function( e, v ) {
				if ( ! this.usa() ) return;
				if ( ! this.auth ) { this.reset(); return; }
				this.draging = F;
				this.addClass( 'z-authing' );
				this.auth.data( { value: v, token: this.img.getResult().token } );
				this.auth.load();
				this.more.keepHover( F );
			},
			resize: function( e ) {
				_superTrigger( this, AbsForm, e );
				this.css( 't', 'width', this.formWidth() );
			}
		}
	},
	Prototype: {
		validHooks: {
			valid: function( b, v ) {
				if ( ! this.isSuccess() )
					return _form_err.call( this, b, 'jigsaw_required' );
			}
		},
		isModified: $.rt( F ),
		validTip: function( t ) {
			return { type: 'Tip', text: t, snap: { position: this.more ? 'rl,lr' : 'tb,bt' } };
		},
		usa: function() {
			return this.img && this.isNormal();
		},
		pop: function( a ) {
			! this.more.vis && (a ? this.more.show() : this.more._show());
		},
		popWidth: function() {
			return this.$( 'f' ).offsetWidth;
		},
		popHeight: function() {
			var r = this.img && this.img.getResult();
			return r ? Math.ceil( this.popWidth() * (r.big.height / r.big.width) ) : N;
		},
		fixImg: function() {
			this.more && this.more.$() && Q( '._small', this.more.$() ).width( this.smallWidth() );
		},
		smallWidth: function() {
			var r = this.img && this.img.getResult();
			return r ? this.popHeight() * (r.small.width / r.small.height) : N;
		},
		success: function( a ) {
			this.x.status = a ? 'success' : '';
		},
		isSuccess: function( a ) {
			return this.x.status === 'success';
		},
		reload: function( a ) {
			this.loaded = F;
			this.img.load( function() {
				a && this.parentNode.pop();
				this.parentNode.more.getContentView().find( 'img' ).repaint( T );
				this.parentNode.fixImg();
			} );
		},
		reset: function() {
			this.val( this.min() );
			this.removeClass( 'z-on z-success z-err z-lock' );
			Q( this.$( 'ph' ) ).css( { left: '', right: '' } );
			Q( this.$( 'pht' ) ).html( this.html_info() );
			this.normal();
			this.reload();
		},
		lock: function( d ) {
			this.addClass( 'z-err z-lock' );
			Q( this.$( 'pht' ) ).html( this.html_info( d ) );
			this.readonly();
			this.more && this.more.close();
			var a = Math.abs( d.error.timeout || 0 ), self = this;
			if ( a ) {
				this._cntdn_inter = setInterval( function() {
					if ( a < 2 ) {
						clearInterval( self._cntdn_inter );
						self.reset();
					} else {
						a -= 1;
						Q( 'em', self.$( 'pht' ) ).html( a );
					}
				}, 1000 );
			}
		},
		dragSmall: function( a ) {
			this.dragStart( this.$( 'thumb' ), a );
		},
		form_cls: function() {
			return 'w-input f-nv';
		},
		html_info: function( d ) {
			return d && d.error ? '<var class=_err>' + (d.error.msg != N ? d.error.msg : Loc.auth_fail) + (d.error.timeout ? '(<em>' + Math.abs( d.error.timeout ) + '</em>)' : '') + '</var>' :
					d && d.success ? '<var class=_ok>' + (d.msg != N ? d.msg : Loc.auth_success) + '</var>' : 
					(this.x.placeholder || Loc.form.jigsaw_drag_right);
		},
		html_img: function() {
			var d = this.img.getResult();
			if ( ! d._date )
				d._date = { "_date": new Date().getTime() };
			return '<img class=_big src=' + $.urlParam( d.big.src, d._date ) + ' width=100% height=100% ondragstart=return(!1)><img class=_small src=' + $.urlParam( d.small.src, d._date ) +
				' width=' + this.smallWidth() + ' height=100% ' + ev_down + abbr( this ) + '.dragSmall(event) ondragstart=return(!1)><span onclick=' + abbr( this ) + '.reload(true) class=_ref>' + Loc.refresh + '</span>';
		},
		html_placeholder: function() {
			return '<div class="_s f-fix" id="' + this.id + 'ph"><i class=f-vi></i><span class=f-va id="' + this.id + 'pht">' + this.html_info() + '</span></div>';
		}
	}
} ),
/* `dropbox` */
_boxbtn_width = mbi ? 36 : 20,
DropBox = define.widget( 'DropBox', {
	Const: function( x ) {
		this._sel = [];
		AbsInput.apply( this, arguments );
		if ( ! x.nodes ) {
			var s = this.attr( 'src' );
			if ( s && typeof s === _OBJ )
				this._loadEnd( s );
		}

	},
	Extend: [ AbsInput, Section ],
	Listener: {
		//tag: N,
		body: {
			ready: function() {
				! this.x.nodes && this.x.src && this.load();
			},
			click: {
				occupy: T,
				method: function() {
					this.isNormal() && this.drop();
				}
			},
			change: N
		}
	},
	Prototype: {
		x_nodes: $.rt( N ),
		$v: function() { return $( this.id + 'v' ) },
		init_nodes: function() {
			this._sel.length = 0;
			var x = this.x, o = x.nodes || (x.nodes = []), v = x.value == N ? '' : '' + x.value, e, g = ! this.x.multiple;
			for ( var i = 0, l = o.length; i < l; i ++ ) {
				e = o[ i ].value = o[ i ].value == N ? '' : '' + o[ i ].value;
				if ( o[ i ].checked || (v && e && $.idsAny( v, o[ i ].value )) ) {
					this._sel.push( o[ i ] );
					if ( g ) break;
				}
				if ( o[ i ].checkAll )
					this._chkall = o[ i ];
			}
			if( o.length ) {
				! this._sel.length && ! this.x.cancelable && ! this.x.multiple && this._sel.push( o[ 0 ] );
				for ( var i = 0, l = this._sel.length, s = []; i < l; i ++ )
					s.push( this._sel[ i ].value );
				x.value = s.join(); // 设一下value，给 isModified() 用
			}
		},
		// @a -> options, b -> index?
		setOptions: function( a, b ) {
			var v = this.val();
			if ( b != N ) {
				var o = this.x.nodes || [];
				o[ b ] && (o[ b ] = a);
			} else
				this.x.nodes = a;
			this._dropper && this._dropper.close();
			this.init_nodes();
			this.val( v );
			if ( v && !this.x.multiple && !this._sel.length ) {
				a && a[ 0 ] && this.val( a[ 0 ].value || '' );
			}
		},
		addOption: function( a, i ) {
			this.x.nodes.splice( i == N ? this.x.nodes.length : i, 0, a );
			this.setOptions( this.x.nodes );
		},
		removeOption: function( i ) {
			this.x.nodes.splice( i == N ? -1 : i, 1 );
			this.setOptions( this.x.nodes );
		},
		getFocusOption: function( a ) {
			for ( var i = 0, v = this.val(), o = this.x.nodes || [], l = o.length; i < l; i ++ ) {
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
		getLength: function() {
			return (this.x.nodes || []).length;
		},
		isEmpty: function() {
			return ! this.val();
		},
		focus: function( a ) {
			_z_on.call( this, a == N || a );
		},
		val: function( a ) {
			if ( this.$() ) {
				var v = this.$v().value;
				if ( a == N )
					return v;
				var b = [];
				if ( a.jquery ) {
					for ( var i = 0, q = a.parent().find( '._o.z-on' ), l = q.length; i < l; i ++ )
						b.push( this.x.nodes[ q[ i ].getAttribute( '_i' ) ] );
				} else {
					for ( var i = 0, q = this.x.nodes, l = q.length, c; i < l; i ++ )
						if ( q[ i ].value == '' ? a == '' : $.idsAny( a, q[ i ].value ) ) b.push( q[ i ] );
				}
				for ( var i = 0, s = [], t = [], u = []; i < b.length; i ++ ) {
					s.push( this.html_li( b[ i ], T ) );
					t.push( b[ i ].text );
					b[ i ].value && u.push( b[ i ].value );
				}
				this._sel = b;
				this.$v().value = u = u.join();
				this._val( s.join( ', ' ) );
				if ( this.attr( 'tip' ) === T )
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
		fixCheckAll: function() {
			var q = Q( this._dropper.$() ).find( '._o.z-on' );
		},
		choose: function( a ) {
			var d = Q( a ).closest( '._o' ), v = '' + this.x.nodes[ d.attr( '_i' ) ].value;
			if ( this.x.multiple || this.x.cancelable ) {
				d.toggleClass( 'z-on' );
			}
			if ( ! this.x.multiple ) {
				! this.x.cancelable && d.addClass( 'z-on' );
				d.siblings().removeClass( 'z-on' );
			}
			if ( this.x.multiple && this._chkall ) {
				if( d.attr( '_all' ) ) {
					d.hasClass( 'z-on' ) && d.parent().find( '._o' ).not( d ).removeClass( 'z-on' );
				} else {
					var a = d.parent().find( '._o[_all]' ), l = d.parent().find( '._o.z-on:not([_all])' ).length;
					l && a.removeClass( 'z-on' );
				}
			}
			if ( ! (this.x.on && this.x.on.beforeChange && (this.x.multiple || this.$v().value != v) &&
				this.triggerHandler( 'beforeChange', [ v ] ) === F) )
					this.val( d );
			! this.x.multiple && this._dropper.close();
		},
		drop: function() {
			var o = this.x.nodes;
			if ( this.loading || ! o || ! o.length )
				return;
			var a = $.bcr( this.$() ), d = this._dropper;
			if ( d ) {
				this.focus( F );
				d.close();
			} else {
				this.focus();
				this._dropper = this.exec( { type: 'Dialog', ownproperty: T, minWidth: this.formWidth() + 2, maxWidth: Math.max( $.width() - a.left - 2, a.right - 2 ), maxHeight: Math.max( $.height() - a.bottom, a.top ), widthMinus: 2, heightMinus: 2, id: this.id,
					cls: 'w-dropbox-dialog w-f-dialog' + (this.x.multiple ? ' z-mul' : (this.x.cancelable ? ' z-cancel' : '')), autoHide: T,
					snap: { target: this.$( 'f' ), position: 'v', indent: 1 }, node: { type: 'Html', id: 'list', scroll: T, text: this.html_options() },
					on: { load: 'this.commander.listenPop(!0)', close: 'this.commander.listenPop(!1)' }
				} );
			}
		},
		listenPop: function( a ) {
			if ( a ) {
				var c = this._dropper.getContentNode(), d = $.get( '.z-on', c.$() ), self = this;
				d && c.scrollTop( d, 'middle', N, d.offsetHeight );
				! this.x.multiple && Q( document ).on( 'keydown.' + this.id, function( e ) {
					if ( e.keyCode === 38 || e.keyCode === 40 ) {
						var g = Q( '.z-on', c.$() ), h;
						if ( g.length ) {
							h = g[ e.keyCode === 38 ? 'prev' : 'next' ]();
							h.length && h.addClass( 'z-on' ).end().removeClass( 'z-on' );
						} else {
							h = Q( '._o:' + (e.keyCode === 38 ? 'last' : 'first'), c.$() );
							h.addClass( 'z-on' );
						}
						h.length && c.scrollTop( h[ 0 ], 'auto' );
						e.preventDefault();
					} else if ( e.keyCode === 13 ) {
						var g = $.get( '.z-on', c.$() );
						g && self.choose( g );
					}
				} );
			} else {
				this.focus( F );
				this._dropper = N;
				! this.x.multiple && Q( document ).off( 'keydown.' + this.id );
			}
		},
		showLoading: function( a ) {
			this.addClass( 'z-loading', a );
			a !== F && (this.$( 'p' ).innerHTML = this.x.loadingText || Loc.loading);
		},
		showLayout: function() {
			this.val( this.x.value );
		},
		form_prop: function() {
			var s = AbsInput.prototype.form_prop.call( this );
			return s + _html_on.call( this );
		},
		option_text: function( a ) {
			var t = a.text || a.value;
			if ( this.x.format )
				t = this.html_format( t, this.x.format, this.x.escape, N, a );
			return t;
		},
		html_placeholder: function() {
			return '';
		},
		html_li: function( a, b ) {
			return a ? (a.icon ? $.image( a.icon, { cls: 'w-dropbox-ico' } ) : '') + this.option_text( a ) + (!b ? '<i class=_box></i>' : '') : '';
		},
		html_text: function() {
			if ( this.loading ) {
				return Loc.loading;
			} else if ( this.x.multiple ) {
				for ( var i = 0, b = this._sel, s = []; i < b.length; i ++ ) {
					s.push( this.option_text( b[ i ] ) );
				}
				s = s.join( ', ' );
				return s;
			} else
				return this.html_li( this._sel[ 0 ], T );
		},
		html_options: function() {
			for ( var i = 0, s = [], v = this.$v().value, o = this.x.nodes || [], b, l = o.length, t; i < l; i ++ ) {
				s.push( '<div class="_o f-fix' + (o[ i ].value && $.idsAny( v, o[ i ].value ) ? ' z-on' : '') + '" _i="' + i + '"' + (o[ i ].checkAll ? ' _all=1' : '') +
				(this.attr( 'tip' ) ? this.prop_title( o[ i ].text ) : '') + _event_zhover + '>' + this.html_li( o[ i ] ) + '</div>' );
			}
			return '<div id=' + this.id + 'opts class=_drop onclick=' + evw + '.choose(event.srcElement)>' + s.join( '' ) + '</div>';
		},
		html_btn: function() {
			return '<em class=f-boxbtn><i class=f-vi></i>' + $.arrow( mbi ? 'b3' : 'b2' ) + '</em>';
		},
		html_input: function() {
			var s = this._sel[ 0 ], t = this.attr( 'tip' ) && this.html_text();
			return '<input type=hidden name="' + this.x.name + '" id=' + this.id + 'v value="' + (this.x.value || '') + '"><div class="f-omit _t" id=' + this.id + 't ' +
				(t ? ' title="' + $.strQuot( t.replace(/<[^>]+>/g, '') ) + '"' : '') + '><span id=' + this.id + 'p>' + this.html_text() + '</span></div>';
		}
	}
}),
/* `imgbox` */
ImgBox = define.widget( 'ImgBox', {
	Const: function( x, p ) {
		DropBox.apply( this, arguments );
		this.imgw = x.imgWidth == N ? 80 : _number( x.imgWidth );
		this.imgh = x.imgHeight == N ? 80 : _number( x.imgHeight );
		this.txth = x.nodes && x.nodes[ 0 ] && x.nodes[ 0 ].text ? 22 : 0;
		this.txth && $.classAdd( this, 'z-tx' );
	},
	Extend: DropBox,
	Default: { width: -1 },
	Listener: {
		//tag: N,
		body: {
			focus: N, blur: N, resize: N
		}
	},
	Prototype: {
		x_nodes: $.rt( N ),
		drop: function() {
			var d = this.x.nodes,
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
				s = [ '<div class=w-imgbox-list id=' + this.id + 'opts>' ];
			if ( g.top < 0 ) h += g.top;
			if ( l ) {
				for ( var i = 0, v = this.val(); i < l; i ++ ) {
					s.push( '<div class="w-imgbox-c f-inbl' + (d[ i ].value == v ? ' z-on' : '') + '"' + _event_zhover + ' onclick=' + evw + '.choose(' + i + ')>' + this.html_img( d[ i ] ) + '</div>' );
				}
				this.addClass( 'z-on' );
				this.dg = this.add( { type: 'Dialog', ownproperty: T, width: e, height: h, cls: 'w-imgbox-dialog w-f-dialog' + (this.txth ? ' z-tx': ''), snap: { target: this.$( 'f' ), position: g.type, indent: 1 }, autoHide: T,
					node: { type: 'Html', scroll: T, text: s.join( '' ) + '</div>' }, on: { close: 'this.parentNode.removeClass("z-on z-m-' + g.mag + '")' } } ).render();
				this.addClass( 'z-m-' + g.mag );
			}
		},
		choose: function( a ) {
			this._sel = [ this.x.nodes[ a ] ];
			this._val();
			this.trigger( 'change' );
			this.dg.close();
		},
		_val: function( o ) {
			this.$( 'f' ).innerHTML = this.html_nodes();
		},
		form_cls: function() {
			return 'w-input w-imgbox-c f-rel f-nv';
		},
		html_img: function( a ) {
			return '<div class=_g id=' + this.id + 'p style="width:' + this.imgw + 'px;height:' + this.imgh + 'px;">' + $.image( a.icon, { width: this.imgw, height: this.imgh } ) + '</div>' +
				(this.txth ? '<div class="_s f-fix" style="width:' + this.imgw + 'px;"' + this.prop_title( a.text ) + '>' + (a.text || '') + '</div>' : '');
		},
		html_nodes: function() {
			var a = this._sel[ 0 ] || {icon:'.f-dot'};
			return (a ? this.html_img( a ) : '') + '<input type=hidden name="' + this.x.name + '" id=' + this.id + 'v value="' + ((a && a.value) || '') + '">';
		}
	}	
}),
_value_comma = function( a ) {
	return a.replace( /^,+|,+$/g, '' ).replace( /,{2,}/g, ',' );
},
/* `combobox`
 *	注1: 当有设置初始value时，text一般可以不写，程序将会从数据岛(more属性)中匹配。如果数据岛不是完整展示的(比如树)，那么text属性必须加上。
 */
ComboBox = define.widget( 'ComboBox', {
	Const: function( x ) {
		x.face && (this.className += ' z-face-' + x.face);
		AbsInput.apply( this, arguments );
		x.value && (x.value = _value_comma(x.value));
		this.addEvent( 'focus', function() { this.focusNode && this.focusNode.tabFocus( F ) } );
		this._init_more();
	},
	Extend: AbsInput,
	Listener: {
		block: function( e ) {
			return e.srcElement && e.srcElement.id.indexOf( this.id ) < 0;
		},
		body: {
			ready: function() {
				this._init_ready();
			},
			blur: {
				occupy: T,
				method: function( e ) {
					_superTrigger( this, AbsInput, e );
					var t = this.$t();
					! t.innerText && t.nextSibling && ! this.contains( document.activeElement ) && t.parentNode.appendChild( t );
				}
			},
			click: {
				occupy: T,
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
						t && ((t != this._query_text) || !(this.sugger && this.sugger.vis)) && this.suggest( t, 10 );
					}
				}
			},
			keyDown: {
				occupy: T,
				method: function( e ) {
					if ( this.usa() ) {
						var k = e.keyCode, n;
						if ( k === 13 )
							return $.stop( e );
						if ( ! this._imeMode ) {
							if ( k === 8 ) { // 8:backspace
								if ( $.rngCursorOffset() == 0 ) {
									var a = this.$t().previousSibling;
									a && (a = $.widget( a )).type === 'ComboBoxOption' && a.close();
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
			// chrome中文模式打完字后按回车时，不会响应keyUp事件，因此设置input事件来触发suggest()
			input: br.ms ? N : {
				occupy: T,
				method: function( e ) {
					if ( this._imeMode ) {
						var t = this.queryText();
						this.suggest( t );
						this.checkPlaceholder();
					}
				}
			},
			keyUp: {
				occupy: T,
				method: function( e ) {
					if ( this.usa() && ! this._imeMode ) {
						var k = e.keyCode, t, m;
						if ( k === 13 || k === 37 || k === 38 || k === 39 || k === 40 ) { // 13:enter, 37: left, 38:up, 39:right, 40:down
							$.stop( e );
							var d = this.pop(), t = this.queryText();
							if ( k === 13 && t != this._query_text ) { // 中文输入法按回车，是把文本放入输入框里的动作，不是提交动作
								this.suggest( t );
							} else if ( d.vis && (m = this.store( d )) ) {
								k === 13 && ! m.getFocus() ? _enter_submit( k, this ) : t || d == this.dropper ? m.keyUp( k ) : this.closePop();
							} else
								_enter_submit( k, this );
						} else if ( !(e.ctrlKey && k === 86) && !(k === 17) ) { //86: ctrl+v, 17: Ctrl
							t = this.$t().innerText;
							var s = String.fromCharCode( 160 ) + ' '; // 160: chrome的空格
							if ( k === 32 && s.indexOf( t.charAt( t.length - 1 ) ) > -1 && (t = $.strTrim( t.slice( 0, -1 ) )) ) { // 最后一个字符是分隔符，则生成一个已选项
								this.queryText( '' );
								var o = this.addOpt( t );
								o.x.error && this._online && this.match( o ); // 新增的选项如果没即时匹配成功，并且有suggest，则以隐藏模式去后台匹配一次数据。
								this.closePop();
								$.stop( e );
							} else if ( !e.ctrlKey ) { 
								this.suggest( t );
							}
						}
						this.checkPlaceholder();
					}
				}
			},
			paste: {
				occupy: T,
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
			// 覆盖textarea的change事件定义，仅当已选项有变化时触发
			change: N
		}
	},
	Prototype: {
		domready: F,
		_query_text: '',
		x_node: $.rt(),
		x_childtype: $.rt( 'ComboBoxOption' ),		
		validHooks: {
			valid: function( b, v ) {
				if ( this.x.strict !== F && this.$() && (Q( '._o.z-err', this.$() ).length || this.queryText()) )
					return _form_err.call( this, b, 'invalid_option' );
			}
		},
		$v: function() { return $( this.id + 'v' ) },
		_init_more: function() {
			$.classAdd( this, 'z-loading' );
			this.loading = T;
			this.more && this.more.dispose();
			this.sugger && (this.sugger.dispose(), this.sugger = N);
			this.dropper && (this.dropper.dispose(), this.dropper = N);
			this.more = this.createPop( this.x.suggest || { type: 'Dialog', node: { type: 'View', node: { type: 'Table' } } }, { value: this._val() } );
			if ( this.x.suggest ) {
				this._online = typeof this.more.x.src === _STR && /\$text\b/.test( this.more.x.src );
			}
			var c = this.more.getContentView();
			c && c.layout && this._init_load();
		},
		_init_ready: function() {
			this.domready = T;
			var c = this.more.getContentView();
			!(c && c.layout) && this.more.preload( $.proxy( this, this._init_load ) );
			!this.loading && this.init();
		},
		_init_load: function() {
			this.loading = F;
			$.classRemove( this, 'z-loading' );
			this.domready && this.init();
		},
		init: function( v ) {
			if ( ! this.$() )
				return;
			this._initOptions( this._val() );
			this.queryText( '' );
			this.save();
			$.classRemove( this.$(), 'z-loading' );
			this.usa() && (this.$t().contentEditable = T);
			this.$().title = this.text();
			_listen_ime( this, this.$t() );
			this.trigger( 'load' );
		},
		// 根据value设置已选项, 初始化时调用 /@v -> value
		_initOptions: function( v ) {
			var i = this.length;
			while ( i -- ) this[ i ].remove();
			if ( v && (v = v.split( ',' )) ) {
				for ( var i = 0, t = t && t.split( ',' ), o, s = [], l = v.length; i < l; i ++ ) {
					if ( v[ i ] ) {
						if ( o = this.param( v[ i ], T ) )
							this.append( o );
						else if ( this.x.strict === F )
							this.append( { text: v[ i ], error: T } );
					}
				}
			}
		},
		resetOptions: function() {
			this._init_more();
			this._init_ready();
		},
		// @implement
		insertHTML: function( a, b ) {
			! b || b === 'append' ? $.before( this.$t(), a ) : b === 'prepend' ? $.prepend( this.$( 'c' ), a ) : _proto.insertHTML.apply( this, arguments );
		},
		isEmpty: function() {
			return !(this._val() || this.queryText() || this.length);
		},
		usa: function() {
			return ! this.loading && this.isNormal();
		},
		// 读/写隐藏值
		_val: function( a ) {
			if ( a == N )
				return (this.domready ? this.$v() : this.x).value || '';
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
				this.addEventOnce( 'load', function() { this.val( a ) } );
			} else {
				this.empty();
				this._val( '' );
				this.$t().innerHTML = '';
				a ? this.match( { value: a } ) : this.checkPlaceholder();
			}
		},
		_storeView: function( a ) {
			return (a || this.more).getContentView();
		},
		store: function( a ) {
			var b = this._storeView( a );
			if ( b.combo )
				return b.combo;
			if ( this.x.bind ) {
				var c = this.x.bind.target ? b.find( this.x.bind.target ) : (b.descendant( 'Table' ) || b.descendant( 'Tree' ));
				return c && (b.combo = new _comboHooks[ c.type ]( c, this.x.bind ));
			}
		},
		// @a -> text|value|Leaf|TR, b -> isValue?
		param: function( a, b ) {
			var s = this.store();
			return s && s.getParam( a, b );
		},
		checkPlaceholder: function( v ) {
			this.$( 'ph' ) && $.classAdd( this.$( 'ph' ), 'f-none', !!(arguments.length === 0 ? (this.length || this.queryText()) : v) );
		},
		// 获取当前的选项对话框
		pop: function() {
			return this.dropper && this.dropper.vis ? this.dropper : this.sugger && this.sugger.vis ? this.sugger : this.more;
		},
		// 创建选项窗口 /@ u -> dialogOption, r -> replace object?
		createPop: function( u, r ) {
			var d = { ownproperty: T, cls: 'w-combobox-dialog w-f-dialog', snap: { indent: 1 }, loadingHead: F, bind: this };
			$.extendDeep( d, u );
			u.cls && (d.cls += ' ' + u.cls);
			var o = { type: 'Dialog', autoHide: T, memory: T, snap: { target: this.id + 'f', position: 'v' }, widthMinus: 2, heightMinus: 2 },
				w = 'javascript:return this.parentNode.$("f").offsetWidth';
			//如果用户设置宽度为*或百分比，则设置maxWidth为不超过combobox的宽度
			if ( u.width ) {
				if ( isNaN( u.width ) )
					o.maxWidth = w;
			} else if ( ! o.minWidth ) {
				o.width = w;
			}
			$.extendDeep( d, o );
			d.data = $.extend( d.data || {}, r );
			var self = this;
			return this.add( d, -1 ).addEvent( 'close', function() {
				! self.$().contains( document.activeElement ) && self.focus( F );
				var d = self.pop();
				d && d.vis && self.focusNode && self.focusNode.tabFocus( F );
			} ).addEvent( 'load', function() {
				this.axis();
				var s = self.store( this );
				s && s.showFocus();
			} );
		},
		closePop: function() {
			clearTimeout( this._sug_timer );
			var d = this.pop();
			d && d.close();
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
		getHighlightKey: function() {
			return this.queryText();
		},
		// 根据文本增加已选项, 多个用逗号或空白符隔开 /@ t -> text|replaceObject, a -> param data?
		addOpt: function( t, a ) {
			var v = this._val(), e = this.$t(), k = e.nextSibling ? _widget( e.nextSibling ).nodeIndex : N;
			if ( t.value ) {
				this._initOptions( t.value );
			} else if ( (t = typeof t === _STR ? t : t.text) && (t = t.split( ',' )).length ) {
				for ( var i = 0, d, o, s = []; i < t.length; i ++ ) {
					if ( t[ i ] ) {
						if ( d = (a || this.param( t[ i ] )) )
							! $.idsAny( v, d.value ) && ( s.push( this.add( d, k ) ), $.idsAdd( v, d.value ) );
						else
							s.push( this.add( { text: t[ i ], error: T }, k ) );
						if ( ! this.x.multiple && s.length )
							break;
					}
				}
				for ( var i = 0; i < s.length; i ++ )
					s[ i ].render();
			}
			this.save();
			return this[ this.length - 1 ];
		},
		// 完成一个尚未匹配成功的项 /@o -> ComboBoxOption, a -> param data?
		fixOpt: function( o, a ) {
			var d = a || this.param( o.x.text );
			if ( d && o.x.error ) {
				$.idsAny( this._val(), d.value ) ? o.remove() : o.replace( d );
				this.save();
			}
		},
		//@public(用于 combo 窗口的点击事件) 完成正在输入的文本，或是没有匹配成功的项 / @a -> tr|leaf|xml
		complete: function( a ) {
			var r = this.store();
			if ( r ) {
				!$.isArray( a ) && (a = [ a ]);
				for ( var i = 0, d, f; i < a.length; i ++ ) {
					r.merge( a[ i ] );
					if ( d = r.getParam( a[ i ] ) )
						(f = this.focusNode) ? this.fixOpt( f, d ) : (this.queryText( '' ), this.addOpt( d.text, d ));
				}
				this.focus();
			}
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
		// 弹出模糊匹配的选项窗口  /@ a -> text|ComboBoxOption
		doSuggest: function( a ) {
			this._currOpt = a;
			var t = this._suggest_text( a );
			if ( this._online ) {
				var self = this;
				if ( ! this.sugger )
					this.sugger = this.createPop( this.x.suggest );
				this.sugger.data( { value: this.val(), text: t } );
				this.sugger.reload( N, N, N, function() { ! self._disposed && self._suggest_end( a, this ) } );
			} else
				this._suggest_end( a, this.more );
		},
		_suggest_end: function( a, m ) {
			var c = this.store( m );
			if ( c ) {
				var t = this._suggest_text( a ), d = c.getXML( t ), e = this.pop(), s = this.store(), u = this._online;
				d && s != m && s.merge( d );
				e && e != m && e.close();
				a.x && m.addEvent( 'close', function() { !a._disposed && a.tabFocus( F ) } );
				if ( (u ? c.getLength() : c.filter( t )) || c.isKeepShow() ) {
					!(u && m.$()) && m.show();
				} else
					m.close();
			}
		},
		_suggest_text: function( a ) {
			return $.strTrim( a.x ? a.x.text : a );
		},
		// 精确匹配，在隐藏状态下进行 /@ a -> replaceObject|ComboBoxOption
		// 多个立即匹配成功; 单个显示下拉选项
		match: function( a ) {
			var c = a.x ? a : this;
			if ( this._online ) {
				var d = this.createPop( this.x.suggest, a.x || a ), self = this;
				c.setLoading();
				d.preload( function() {
					if ( ! c._disposed ) {
						c.setLoading( F );
						var o = self.store( this );
						if ( o ) {
							self.store().merge( o );
							a.x ? self.fixOpt( a ) : self.addOpt( a );
						}
						self.checkPlaceholder();
					}
					this.remove();
				} );
			} else {
				a.x ? this.fixOpt( a ) : this.addOpt( a );
				this.checkPlaceholder();
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
			t = this.queryText();
			f && t && s.push( t );
			s = s.join( ',' );
			this._val( s );
			this.$().title = this.text();
			this.checkPlaceholder( this.length || t );
			if ( this.x.on && this.x.on.change && v != this._val() )
				this.triggerHandler( 'change' );
		},
		drop: function() {
			if ( this.usa() ) {
				var d = this.dropper, b = d && d.vis;
				this.closePop();
				if ( ! b ) {
					this.focus();
					(d || (this.dropper = this.createPop( this.x.drop ))).show();
				}
			}
		},
		pick: function() {
			if ( this.x.picker && this.isNormal() ) {
				if ( this.x.picker.type === 'Dialog' || ! this.x.picker.type ) {
					var c = $.extend( $.jsonClone( this.x.picker ), { type: 'Dialog' } );
					c.data = $.extend( c.data || {}, { value: this.val() } );
					this.exec( c ).addEvent( 'close', function() { ! this.$().contains( document.activeElement ) && this.focus( F ); }, this );
				} else if ( W.isCmd( this.x.picker ) ) {
					this.cmd( this.x.picker, this.val() );
				}
				this.warn( F );
			}
		},
		setLoading: function( a ) {
			a = a == N || a;
			this.loading = a;
			this.queryText( a ? (this.x.loadingText || Loc.loading) : '' );
			this.$t().contentEditable = ! a;
		},
		readonly: function( a ) {
			AbsForm.prototype.readonly.call( this, a );
			this.$t().contentEditable = this.isNormal();
			return this;
		},
		validonly: function( a ) {
			AbsForm.prototype.validonly.call( this, a );
			this.$t().contentEditable = this.isNormal();
			return this;
		},
		disable: function( a ) {
			AbsForm.prototype.disable.call( this, a );
			this.$t().contentEditable = this.isNormal();
			return this;
		},
		normal: function( a ) {
			AbsForm.prototype.readonly.call( this, a );
			this.$t().contentEditable = this.isNormal();
			return this;
		},
		form_cls: function() {
			return AbsInput.prototype.form_cls.call( this ) + (this.x.br ? ' z-ah f-wdbr' : '');
		},
		html_btn: function() {
			var s = '';
			if ( this.x.picker ) {
				if ( W.isCmd( this.x.picker ) ) {
					s += '<em class="f-boxbtn _pick" onclick=' + evw + '.pick()><i class="f-i _pick_i"></i><i class=f-vi></i></em>';
				} else {
					var g = this.add( this.x.picker, -1, { width: -1 } );
					g.className += ' f-pick';
					return g.html();
				}
			}
			if ( this.x.drop )
				s += '<em class="f-boxbtn _drop" onclick=' + evw + '.drop()><i class=f-vi></i>' + $.arrow( mbi ? 'b3' : 'b2' ) + '</em>';
			return s;
		},
		html_input: function() {
			return '<input type=hidden id=' + this.id + 'v name="' + this.input_name() + '" value="' + (this.x.value || '') + '"' + (this.isDisabled() ? ' disabled' : '') +
				'><var class="_e f-nobr" id=' + this.id + 't' + ( this.usa() ? ' contenteditable' : '' ) + ' ' + _html_on.call( this ) + '>' + (this.x.loadingText || Loc.loading) + '</var>';
		},
		html_nodes: function() {
			return this.html_btn() + '<div class="_c' + (this.x.br ? '' : ' f-nobr') + '" id="' + this.id + 'c" onclick=' + eve + '>' + this.html_placeholder() + this.html_input() + '</div>';
		}
	}
} ),
/* `comboboxoption`
	@x: { value: String, text: String, error: Boolean }
 */
ComboboxOption = define.widget( 'ComboBoxOption', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		x.error && (this.className += ' z-err');
	},
	Default: { width: -1, height: -1 },
	Listener: {
		body: {
			ready: function() {
				this.fixSize();
			},
			touchStart: {
				occupy: T, 
				method: function() {
					this._focus = $.classAny( this.parentNode.$(), 'z-on' );
				}
			},
			click: {
				occupy: T,
				// 禁用用户事件
				block: $.rt( T ),
				method: function() {
					if ( this.loading || this._disposed )
						return;
					// 没有成功匹配文本时只做聚焦和搜索，不执行用户设置的click事件
					var p = this.parentNode, f = this._focus;
					if ( mbi ) {
						this.close();
						f && p.focus();
					} else {
						if ( this.x.error ) {
							if ( p.focusNode !== this ) {
								this.tabFocus();
								p.suggest( this );
							}
						} else {
							this.triggerHandler( 'click' );
						}
					}
					p.warn( F );
				}
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'ComboBox',
		className: 'w-combobox-opt f-nv',
		val: function() {
			return this.x.value;
		},
		text: function() {
			return this.x.text;
		},
		close: function( e ) {
			var p = this.parentNode;
			if ( ! this.isDisabled() && F !== this.triggerHandler( 'close' ) ) {
				this.remove();
				p.save();
				p.checkPlaceholder();
				e && $.stop( e );
			}
		},
		write: function( e ) {
			var p = this.parentNode, t = p.$t();
			if ( p.isNormal() ) {
				t.parentNode.insertBefore( t, this.$() );
				p.focus();
				e && $.stop( e );
			}
		},
		fixSize: function() {
			if ( this.$() ) {
				var w = this.$().parentNode.offsetWidth - 12, m = this.x.maxWidth || 0;
				if ( m > w || m == 0 ) m = w;
				this.$().style.maxWidth = m + 'px';
				if ( ie7 && !this.innerWidth() && this.$().offsetWidth > w ) {
					this.$().style.width = w + 'px';
					Q( 'table', this.$() ).css( 'table-layout', 'fixed' );
				}
			}
		},
		setLoading: function( a ) {
			$.classAdd( this.$(), 'z-loading', this.loading = a === F ? a : T );
		},
		html_nodes: function() {
			var p = this.parentNode, t = $.strEscape( this.x.text ), r = this.x.remark ? $.strEscape( this.x.remark ) : N,
				s = '<i class=_b onclick=' + evw + '.write(event)></i><div class=_x onclick=' + evw + '.close(event)><i class=f-vi></i><i class=_xi>&times;</i></div><div class="_s f-omit" title="' + $.strQuot( t ) + '"><i class=f-vi></i><span class="f-omit f-va">' +
					( this.x.forbid ? '<s>' : '' ) + t + (ie7 && $.strLen( t, 2 ) < 3 ? '<em>' + $.strRepeat( '&nbsp;', 3 - $.strLen( t, 2 ) ) + '</em>' : '') + (r ? '<em class=_r>' + r + '</em>' : '') + ( this.x.forbid ? '</s>' : '' ) + '</span></div>';
			return ie7 && !this.innerWidth() ? '<table cellspacing=0 cellpadding=0 height=100%><tr height=100%><td>' + s + '</table>' : s;
		}
	}
} ),
/* `linkboxoption` */
LinkBoxOption = define.widget( 'LinkBoxOption', {
	Default: { width: -1, height: -1 },
	Extend: 'ComboBoxOption',
	Listener: {
		body: {
			ready: N,
			touchStart: N,
			click: N
		}
	},
	Prototype: {
		ROOT_TYPE: 'LinkBox'
	}
} ),
/* `linkbox` */
LinkBox = define.widget( 'LinkBox', {
	Const: function( x ) {
		ComboBox.apply( this, arguments );
		x.pub && x.pub.on && x.pub.on.dblClick && $.classAdd( this, 'z-dc' );
	},
	Extend: ComboBox,
	Listener: {
		block: N,
		body: {
			click: {
				occupy: T,
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
			dblClick: N, /*{
				occupy: T,
				method: function( e ) {
					var v = e.srcElement.getAttribute( 'data-value' ), d = this.x.on && this.x.on.dblClick;
					v && d && this.cmd( { type: 'js', text: d }, v );
					return F;
				}
			},*/
			blur: {
				occupy: T,
				method: AbsInput.Listener.body.blur.method
			},
			// chrome中文模式打完字后按回车时，不会响应keyUp事件，因此设置input事件来触发suggest()
			// fixme: 在chrome的中文模式下输入英文按回车，没有响应
			input: br.ms ? N : {
				occupy: T,
				method: function( e ) {
					//this._imeMode && this.fixStyle();
					this.checkPlaceholder();
				}
			},
			keyDown: {
				occupy: T,
				method: function( e ) {
					if ( this.usa() && e.keyCode === 13 )
						return $.stop( e );
					this._KC = e.keyCode;
				}
			},
			keyUp: {
				occupy: T,
				method: function( e ) {
					if ( this.usa() && ! this._imeMode ) {
						var k = this._KC = e.keyCode, m;
						if ( k === 13 || k === 38 || k === 40 || k === 17 ) { // 38:up, 40:down, 17:ctrl
							$.stop( e );
							var d = this.pop();
							if ( d.vis )
								(m = this.store( d )) && m.keyUp( k );
							else if ( k === 13 )
								this.fixStyle();
						} else {
							//if ( ! (k === 39 || k === 37) ) // 39: right, 37: left
								this.fixStyle();
						}
						delete this._KC;
						if ( this.x.validate && this.x.validate.maxLength ) {
							this.save();
						}
						this.checkPlaceholder();
					}
				}
			},
			resize: AbsInput.Listener.body.resize
		}
	},
	Prototype: {
		x_childtype: $.rt( 'LinkBoxOption' ),		
		validHooks: {
			valid: function( b, v ) {
				if ( this.x.strict && Q( 'u:not([data-value]):not(:empty)', this.$() ).length )
					return _form_err.call( this, b, 'invalid_option' );
			}
		},
		init: function() {
			if ( ! this.$() )
				return;
			this._initOptions( this._val() );
			this.save();
			this.usa() && (this.$t().contentEditable = T);
			this.$().title = this.text();
			$.classRemove( this.$(), 'z-loading' );
			_listen_ime( this, this.$t() );
		},
		// 根据value设置已选项, 初始化时调用 /@v -> value, t -> text
		_initOptions: function( v ) {
			var p = this.x.separator || ',', s = '';
			this.val( '' );
			if ( v && (v = v.split( ',' )) ) {
				for ( var i = 0, t = t && t.split( p ), o, b = [], l = v.length; i < l; i ++ ) {
					if ( v[ i ] ) {
						if ( o = this.param( v[ i ], T ) )
							b.push( '<u class=_o data-value="' + v[ i ] + '">' + $.strEscape( o.text ) + '</u>' );
						else if ( ! this.x.strict )
							b.push( '<u>' + $.strEscape( (t && t[ i ]) || v[ i ] ) + '</u>' );
					} 
				}
				b.length && (s = b.join( '<i>' + p + '</i>' ) + (this.x.multiple ? '<i>' + p + '</i>' : ''));
			}
			this.$t().innerHTML = s;
			this.checkPlaceholder( v );
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
			this.empty();
			this._val( '' );
			a ? this.match( { value: a }, function() {
				this._initOptions( a );
				this.save();
				this.checkPlaceholder();
			} ) : this.checkPlaceholder();
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
		checkPlaceholder: function( v ) {
			this.$( 'ph' ) && $.classAdd( this.$( 'ph' ), 'f-none', !!(arguments.length === 0 ? (this._val() || this.text()) : v) );
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
			if ( c.id === this.id + 't' ) {
				if ( br.ms ) {
					c.innerHTML = '<u></u>';
					if ( t ) {
						(c = c.children[ 0 ]).innerText = t;
						$.rngCursor( c );
					} else
						return this.closePop();
				} else {
					if ( t ) {
						if ( c.firstChild.nodeType === 3 ) {
							var r = $.rngCursor( c ),
								u = document.createElement( 'u' );
							r.extractContents();
							u.appendChild( document.createTextNode( t ) );
							r.insertNode( u );
							$.rngCursor( u );
							c = u;
						} else {
							Q( 'br', this.$t() ).remove();
							if ( c.firstChild )
								c = c.firstChild;
							else
								$.rngCursor( c );
						}
					} else
						return this.closePop();
				}
			}
			! br.ms && Q( 'br', this.$t() ).remove();
			// 输入框内之只允许存在U和I两种标签。输入过程中浏览器可能会自动生成 <font> 标签，需要去除它
			if ( c.tagName === 'FONT' ) {
				if ( c.parentNode.tagName === 'U' ) {
					var d = c.previousSibling.nodeValue, c = c.parentNode, t = c.innerText;
					this._rng_text( c, t.indexOf( d ) + d.length + 1, t );
				} else if ( c.parentNode.id === this.id + 't' ) {
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
						g = this.param( h );
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
				m.length && (m.remove(), Q( 'i + i, i:first-child', this.$t() ).remove()); // 删除重复数据
				g = T;
				if ( (c = $.rngElement()) && c.tagName === 'U' && ! c.getAttribute( 'data-value' ) )
					this.suggest( c );
				else
					this.closePop();
			} else {
				this.closePop();
				if ( t ) {
					if ( $.classAny( c, '_o' ) && ! this.param( t ) ) {
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
				this._online && n.length > 1 && this.match( { text: n.join( p ) } );
			}
			delete this._paste_rng;
		},
		attachPub: function() {
			var b = this.x.pub;
			if ( b ) {
				var m = Q( '._o', this.$t() ), o = b.on, self = this;
				m.each( function() {
					for ( var k in o )
						this[ 'on' + k ] = function(e) { self.firePub(this, e) };
				} );
			}
		},
		firePub: function( o, e ) {
			var v = o.getAttribute( 'data-value' );
			if ( v ) {
				if ( ! o.id )
					o.id = this.add( {} ).id;
				var n = $.all[ o.id ];
				n.x.value = v;
				n.x.text  = Q( o ).text();
				$.all[ o.id ].trigger( e );
			}
		},
		fixOpt: function() {
			var self = this;
			Q( 'u:not([data-value]):not(:empty)', this.$t() ).each( function() {
				var g = self.param( this.innerText );
				if ( g ) {
					$.classAdd( this, '_o' );
					this.setAttribute( 'data-value', g.value );
				}
			} );
			this.save();
		},
		_suggest_text: function( a ) {
			return a.innerText.replace( /^[\s,]+|[\s,]+$/g, '' );
		},
		// 精确匹配，在隐藏状态下进行 /@ a -> replaceObject, b -> fn?
		match: function( a, b ) {
			if ( this._online ) {
				var d = this.createPop( this.x.suggest, a.x || a ), self = this;
				d.preload( function() {
					var m;
					if ( ! self._disposed && (m = self.store( this )) ) {
						self.store().merge( m );
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
		// @r -> range, s -> text
		_paste: function( r, s ) {
			this.focus();
			$.rngCursor( r );
			$.rngText( r, s );
			! this._KC && this.fixStyle();
		},
		//@public(用于 combo 窗口的点击事件) 完成正在输入的文本，或是没有匹配成功的项  / @a -> tr|leaf|xml
		complete: function( a ) {
			var s = this.store();
			if ( ! s )
				return;
			s.merge( a );
			this.focus();
			var d = this.param( a ), u = this._currOpt || $.rngElement(), p = this.x.separator || ',', t = this.text();
			if ( d && u ) { //&& ! $.idsAny( t, d.text, p ) ) {
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
					s.push( d || Q( q[ i ] ).text() ); // chrome下，当linkbox在隐藏状态，用innerText取不到文本。改用jQuery获取
			}
			this._val( s = s.join( ',' ) );
			if ( this.x.validate && this.x.validate.maxLength ) {
				_inst_hide( 'Tip' );
				var l = $.strLen( s );
				l > this.x.validate.maxLength && this.exec( { type: 'Tip', text: Loc.ps( Loc.form.over_maxLength, [ l - this.x.validate.maxLength ] ) } );
			}
			var t = this.text();
			this.$t().title = t;
			this.checkPlaceholder( s || t );
			if ( this.x.on && this.x.on.change && v != s )
				this.triggerHandler( 'change' );
			this.attachPub();
		},
		drop: function() {
			if ( this.usa() ) {
				this.closePop();
				this.focus();
				this._query_text = N;
				(this.dropper || (this.dropper = this.createPop( this.x.drop ))).show();
			}
		},
		bookmark: function() {
			this._currOpt = $.rngElement();
		},
		html_btn: function() {
			var s = '';
			if ( this.x.picker ) {
				if ( W.isCmd( this.x.picker ) ) {
					s += '<em class="f-boxbtn _pick" onclick=' + evw + '.pick()><i class="f-i _pick_i"></i><i class=f-vi></i></em>';
				} else {
					var g = this.add( this.x.picker, -1, { width: -1 } );
					g.className += ' f-pick';
					return g.html();
				}
			}
			if ( this.x.drop )
				s += '<em class="f-boxbtn _drop" onmousedown=' + evw + '.bookmark() onclick=' + evw + '.drop()><i class=f-vi></i>' + $.arrow( mbi ? 'b3' : 'b2' ) + '</em>';
			return s;
		},
		html_input: function() {
			return '<input type=hidden id=' + this.id + 'v name="' + this.input_name() + '" value="' + (this.x.value || '') + '"' + (this.isDisabled() ? ' disabled' : '') +
				'><var class="f-nv _t" id=' + this.id + 't' + (this.usa() ? ' contenteditable' : '') + ' ' + _html_on.call( this ) + '>' + (this.x.loadingText || Loc.loading) + '</var>';
		}
	}
} ),
/* `onlinebox` */
OnlineBox = define.widget( 'OnlineBox', {
	Const: function( x, p ) {
		x.value && (x.value = _value_comma( x.value ));
		Text.apply( this, arguments );
	},
	Extend: [ Text, ComboBox ],
	Default: { tip: T },
	Listener: {
		body: {
			ready: function() {
				_listen_ime( this );
			},
			click: {
				occupy: T,
				method: function( e ) {
					if ( this.usa() && e.srcElement.id === this.id + 't' ) {
						var t = this.cursorText();
						t && this.suggest( t );
					}
				}
			},
			keyUp: {
				occupy: T,
				method: function( e ) {
					if ( ! this._imeMode && ! this.isDisabled() && ! this._disposed && this.x.suggest ) {
						var k = e.keyCode, m;
						if ( k === 13 || k === 38 || k === 40 ) { // 上下键
							var d = this.pop(), t;
							if ( k === 13 && (t = this.cursorText()) != this._query_text ) {
								this.suggest( t );
							} else if ( d.vis && (m = this.store( d )) ) {
								k === 13 && ! m.getFocus() ? _enter_submit( k, this ) : m.keyUp( k );
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
			beforeDeactivate: {
				occupy: ie,
				method: function() {
					var r = document.selection.createRange(), d = this.$t().createTextRange();
					d.setEndPoint( 'EndToEnd', r );
					this._csr_pos = d.text.length - r.text.length;
				}
			},
			keyDown: N,
			paste: N
		}
	},
	Prototype: {
		formType: 'text',
		_csr_pos: 0,
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
					this.triggerListener( 'beforeDeactivate' );
				return this._csr_pos;
			} else
				return this.$t().selectionStart;
		},
		complete: function( a ) {
			if ( ! this.store() )
				return;
			var d = this.store( this.pop() ).getParam( a );
			this.cursorText( d.text || d.value );
			this.closePop();
			this.focus();
		},
		suggest: function( t ) {
			this.x.suggest && ComboBox.prototype.suggest.apply( this, arguments );
		},
		doSuggest: function( t ) {
			if ( this.x.suggest ) {
				if ( ! this.more )
					this.more = this.createPop( this.x.suggest );
				this.more.data( { value: this.val(), text: t } );
				this.more.reload();
			}
		},
		html_btn: function() {
			return ComboBox.prototype.html_btn.apply( this, arguments );
		}
	}
} ),
/* `pickbox` */
PickBox = define.widget( 'PickBox', {
	Extend: OnlineBox,
	Listener: {
		tag: 't',
		body: {
			ready: function() {
				this._init_ready();
			},
			click: {
				occupy: T,
				block: $.rt( T ),
				method: function( e ) {
					if ( this.x.on && this.x.on.click )
						this.triggerHandler( 'click' );
					else if ( this.x.drop )
						this.drop();
					else if ( this.x.picker )
						this.pick();
				}
			},
			beforeDeactivate: N
		}
	},
	Prototype: {
		loading: F,
		$v: function() { return $( this.id + 'v' ) },
		_init_ready: function() {
			// 如果有设置value而text为空时，尝试从drop中匹配文本
			var v = this._val();
			if ( v && ! this.x.text && this.x.drop ) {
				this.loading = T;
				this.addClass( 'z-loading' );
				var self = this;
				this.text( Loc.loading );
				(this.dropper = this.createPop( this.x.drop )).preload( function() {
					var r = self.param( v, T );
					r ? self.val( r.value, r.text ) : self.val( '', '' );
					self.loading = F;
					self.removeClass( 'z-loading' );
				} );
			}
		},
		resetOptions: function() {
			this.x.text = '';
			this._init_ready();
		},
		_storeView: function( a ) {
			return (a || this.dropper).getContentView();
		},
		usa: function() {
			return ! this.loading && this.isNormal();
		},
		val: function( v, t ) {
			v != N && this.text( t || v );
			var v = AbsForm.prototype.val.apply( this, arguments );
			v != N && this.checkPlaceholder( v );
			return v;
		},
		text: function( t ) {
			if ( t == N )
				return this.x.text;
			this.x.text = t;
			this.$() && (this.$t().innerText = t);
		},
		complete: function( a ) {
			if ( ! this.store() )
				return;
			var d = this.store( this.pop() ).getParam( a );
			this.val( d.value, d.text );
			a.focus && a.focus( T );
		},
		html_input: function() {
			return '<input type=hidden id=' + this.id + 'v' + (this.x.name ? ' name="' + this.x.name + '"' : '') + ' value="' + $.strQuot(this.x.value || '') + '"><div id="' + this.id + 
				't" class="f-fix _t" ' + _html_on.call( this ) + ' title="' + $.strQuot(this.x.text || '') + '">' + $.strEscape( this.x.text ) + '</div>';
		}
	}
} ),
/* `rate` */
/* @fixme: 增加参数: 最多几颗星，是否允许半星，以及满星的分数是多少 */
Rate = define.widget( 'Rate', {
	Extend: AbsForm,
	Default: { width: -1 },
	Listener: {
		body: {
			resize: N
		}
	},
	Prototype: {
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
			var a = _number( a ), b = a + (a % 2), c = Q( this.$( b ) );
			c.prevAll().addBack().addClass( 'z-on' ).removeClass( 'z-in' );
			c.nextAll().removeClass( 'z-on z-in' );
			(a % 2) && c.addClass( 'z-in' );
			//(c = c && Q( c )) && c.prevAll().addBack().addClass( 'z-on' ).removeClass( 'z-in' );
			//(c ? c.nextAll() : Q( 'i', this.$() )).removeClass( 'z-on z-in' );
			//(c && (a % 2)) && c.next().addClass( 'z-in' );
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
		form_cls: function() {
			return '_f f-nv';
		},
		form_prop: function() {
			return AbsForm.prototype.form_prop.call( this ) + ' onmouseout=' + evw + '.out()';
		},
		html_nodes: function() {
			for ( var i = 2, s = '', v = _number( this.x.value ); i <= 10; i += 2 ) {
				s += '<em id=' + this.id + i + ' class="f-inbl _b' + (v >= i ? ' z-on' : v > i - 2 ? ' z-in' : '') + '"><i class=f-inbl onmouseover=' + evw + '.over(' + (i -1) + ') onclick=' + evw + '.click(' + (i -1) + ')></i><i class=f-inbl onmouseover=' + evw + '.over(' + i + ') onclick=' + evw + '.click(' + i + ')></i></em>';
			}
			return s + '<input type=hidden id=' + this.id + 'v' + (this.x.name ? ' name=' + this.x.name : '') + ' value="' + (v || '') + '"' + (this.isDisabled() ? ' disabled' : '') + '>';
		}
	}
} ),
_comboHooks = {},
// `treecombo` 树搜索过滤器
TreeCombo = _comboHooks.Tree = $.createClass( {
	// a -> tree, b -> bind
	Const: function( a, b ) {
		this.cab = a;
		this.bind = b;
		this.xml = this.node2xml( a );
		b && (this._keep_show = b.keepShow);
		a.x.highlight && (this._matchlength = a.x.highlight.matchlength);
	},
	Prototype: {
		type: 'Tree',
		isCombo: T,
		showFocus: function() {
			var b = Dialog.get( this.cab ), c = b.parentNode;
			if ( c.dropper == b ) {
				var v = c.val();
				if ( v ) {
					var d = this.getXML( $.strFrom( v, '/', T ) || v, T ),
						e = d && _all[ d.getAttribute( 'i' ) ];
					e && e != this.cab.getFocus() && e.focus();
				}
			}
		},
		node2xml: function( a ) {
			var b = this.bind.field, f = b.search && b.search.split( ',' ), g = f && f.length;
			this._sch = g;
			return $.xmlParse( this._node2xml( a ) );
		},
		// 把 json 转成 xml，以便使用 xpath 查询
		_node2xml: function( a ) {
			for ( var i = 0, b = this.bind.field, c = [], d, e, f = b.search && b.search.split( ',' ), g = f && f.length, l = a.length, r, s; i < l; i ++ ) {
				e = a[ i ].x, d = e.data || F, r = d[ b.remark ] || e[ b.remark ], s = d[ b.search ] || e[ b.search ];
				s = '<d v="' + $.strEscape( d[ b.value ] || e[ b.value ] || '' ) + '" t="' + $.strEscape(d[ b.text ] || e[ b.text ]) + '" i="' + a[ i ].id + '"';
				r && (s += ' r="' + $.strEscape( r ) + '"');
				(d[ b.forbid ] || e[ b.forbid ]) && (s += ' x="1"');
				if ( f ) {
					for ( var j = 0; j < g; j ++ )
						s += ' s' + j + '="' + $.strEscape( d[ f[ j ] ] || e[ f[ j ] ] ) + '"';
				}
				a[ i ].isDisabled() && (s += ' ds="1"');
				c.push( s + '>' );
				a[ i ].length && c.push( this._node2xml( a[ i ] ) );
				c.push( '</d>' );
			}
			return '<doc>' + c.join( '' ) + '</doc>';
		},
		first: function() {
			return this.cab._filter_leaves ? this.cab._filter_leaves[ 0 ] : this.cab[ 0 ];
		},
		keyUp: function( a ) {
			this.cab.keyUp( a );
		},
		isKeepShow: function() {
			return this._keep_show;
		},
		getFocus: function() {
			return this.cab.getFocus();
		},
		// 根据文本返回一个ComboBoxOption参数 /@a -> text|xml|leaf, b -> is value?
		getParam: function( a, b ) {
			var d = a.nodeType === 1 ? a : this.getXML( a, b );
			if ( d ) {
				var v = d.getAttribute( 'v' ), t = d.getAttribute( 't' ), g = _all[ d.getAttribute( 'i' ) ];
				if ( this.bind.fullPath && g ) {
					var p = g;
					while ( (p = p.parentNode) && p.level > -1 ) {
						var x = this.getXML( p, b ), f = x.getAttribute( 't' );
						f && (t = f + ' / ' + t);
					}
				}
				return { value: v, text: t, remark: d.getAttribute( 'r' ), forbid: d.getAttribute( 'x' ) === '1', data: g && g.x.data };
			}
		},
		// /@a -> text|value|leaf, b -> attr name
		getXML: function( a, b ) {
			typeof a === _STR && this.bind.fullPath && (a = $.strFrom( a, '/', T ) || a);
			return $.xmlQuery( (a.isWidget ? this.getCombo( a ) : this).xml, './/d[' + (typeof a === _STR ? '@' + (b ? 'v' : 't') + '="' + $.strTrim( a ).replace(/\"/g,'\\x34') : '@i="' + a.id) + '"]' );
		},
		getCombo: function( a ) {
			return a.ownerView.combo || (a.ownerView.combo = new _comboHooks[ this.type ]( a.rootNode, this.bind ));
		},
		// 合并来自另一个table的某一行的combo xml /@a -> tr|xml|treeCombo
		merge: function( a ) {
			a.isWidget && (a = this.getCombo( a ).getXML( a ) || F);
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
				var b = $.strSplitword( a, this._matchlength ), f = [];
				for ( var i = 0, c, s; i < b.length; i ++ ) {
					c = $.strQuot( b[ i ] );
					s = 'contains(@t,"' + c + '") or contains(@r,"' + c + '")';
					if ( this._sch ) {
						for ( var j = 0; j < this._sch; j ++ )
							s += ' or contains(@s' + j + ',"' + c + '")';
					}
					for ( var j = 0, d = $.xmlQueryAll( this.xml, './/d[(' + s + ') and @v!="" and not(@ds) and not(@f)]' ), l = d.length; j < l; j ++ ) { //translate(@t,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')
						d[ j ].setAttribute( 'f', '1' );
						f.push( d[ j ] );
					}
				}
				for ( var i = 0, l = f.length; i < l; i ++ ) {
					f[ i ].removeAttribute( 'f' );
					f[ i ] = _all[ f[ i ].getAttribute( 'i' ) ];
				}
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
AbsLeaf = define.widget( 'AbsLeaf', {
	Prototype: {
		_pad_left: 5,
		_pad_level: 14,
		padLeft: function() {
			return (this.x.line ? 0 : this.level * this._pad_level) + this._pad_left;
		},
		// 给 dnd sort 用的
		padSort: function() {
			return this.level * this._pad_level + this._pad_left + 14;
		},
		// @implement
		insertHTML: function( a, b ) {
			var c = a.isWidget && a.$(), d = c && a.$( 'c' );
			$[ b || 'append' ]( this.$( b === 'before' ? N : 'c' ), c || a );
			if ( d ) {
				$.after( a.$(), d );
				a.indent();
			}
		},
		// @implement
		append: function( a ) {
			// 尚未装载的节点不直接增加子节点
			if ( this.getSrc() && !this.loaded )
				a.isWidget && a.remove();
			else
				_proto.append.apply( this, arguments );
		},
		// @implement
		prepend: function( a ) {
			// 尚未装载的节点不直接增加子节点
			if ( this.getSrc() && !this.loaded )
				a.isWidget && a.remove();
			else
				_proto.prepend.apply( this, arguments );
		},
		// @implement
		attrSetter: function( a, b, c ) {
			_proto.attrSetter.apply( this, arguments );
			if ( a === 'text' ) {
				if ( this.$( 't' ) ) {
					$.html( this.$( 't' ), this.html_text() );
					if ( this.x.tip === T ) {
						this.$().title = $.strQuot( this.x.text, T );
					}
				}
			} else if ( a === 'icon' || a === 'expandedIcon' ) {
				this.$( 'i' ) ? $.replace( this.$( 'i' ), this.html_icon() ) : (this.$( 't' ) && $.before( this.$( 't' ), this.html_icon() ));
			} else if ( a === 'focus' ) {
				this.focus( b );
			} else if ( a === 'src' ) {
				this.fixFolder();
			} else if ( a === 'cls' ) {
				this.removeClass( c );
				this.addClass( b );
			} else if ( a === 'badge' ) {
				_setBadge.call( this, b );
			}
		},
		srcData: function() {
			return this._srcdata;
		},
		getSrc: function() {
			return Section.prototype.getSrc.call( this );
		},
		isContentData: function() {
			return T;
		},
		isFolder: function() {
			//return this.length || (this.x.src && !this.loaded) ? T : F;
			return this.length || this.x.folder ? T : F;
		},
		fixFolder: function() {
			this.addClass( 'z-folder', this.isFolder() );
			if ( this.$( 'o' ) && ! this.$( 'r' ) )
				$.prepend( this.$( 'o' ), $.arrow( this.id + 'r', this.isExpanded() ? 'b1' : 'r1' ) );
		},
		_success: function( x ) {
			return Section.prototype._success.apply( this, arguments );
		},
		_error: function() {
			if ( this.x.folder && !this.length ) {
				this.x.folder = F;
				this.fixFolder();
			}
			return Section.prototype._error.apply( this, arguments );
		},
		// @a -> sync? b -> fn?
		request: function( a, b ) {
			this.loading = T;
			var f = this.x.filter, t = this.x.template;
			t && (t = _getTemplate( t ));
			this.exec( { type: 'Ajax', src: this.getSrc(), filter: this.x.filter || (t && t.filter), sync: a, loading: F,
				success: function( x ) {
					if ( ! this._success( x ) )
						return;
					this.loaded = T;
					this._srcdata = x;
					if ( t ) {
						x = _compileTemplate( this, x );
						x.pub && $.merge( (this.x.pub || (this.x.pub = {})), x.pub );
					}
					if ( W.isCmd( x ) ) {
						this.exec( x );
					} else {
						var n = x.nodes || x;
						n.length && this.render_nodes( n );
						this.trigger( 'load' );
					}
					b && b.call( this );
				},
				error: function( a ) {
					this._error( a );
				},
				complete: function( x ) { // complete
					this.loading = F;
					this.loaded = T;
					if ( ! this.getLength() ) {
						this.x.folder = F;
						this.toggle( F );
					}
					this.fixFolder();
					if ( this.$( 'o' ) ) {
						$.classRemove( this.$(), 'z-loading' );
						if ( x = this.html_icon() ) {
							this.$( 'i' ) && $.replace( this.$( 'i' ), x );
						} else
							$.remove( this.id + 'ld' );
					}
				} } );
		},
		render_nodes: function( n ) {
			for ( var j = 0, l = n.length; j < l; j ++ )
				this.add( n[ j ] );
			this.$( 'c' ) && (this.$( 'c' ).innerHTML = _proto.html_nodes.call( this ));
			if ( this.ownerView.combo ) {
				var o = new TreeCombo( this ).xml, m = this.ownerView.combo.getXML( this );
				while ( o.firstChild )
					m.appendChild( o.firstChild );
			}
			for ( j = 0; j < l; j ++ )
				this[ j ].triggerAll( 'ready' );
			this.trigger( 'nodeChange' );
		},
		toggle_nodes: function( a ) {
			if ( this.isFolder() ) {
				this.$( 'c' ) && ($.classAdd( this.$( 'c' ), 'z-expanded', a ), $.classAdd( this.$( 'c' ), 'f-hide', !a ));
				this.addClass( 'z-expanded', a );
			}
		},
		// 展开或收拢 /@a -> T/F/event, b -> sync?, f -> fn?
		toggle: function( a, b, f ) {
			var c = typeof a === _BOL ? a : !this.x.expanded, d = !!this.x.expanded;
			this.x.expanded = c;
			this.toggle_nodes( c );
			if ( this.isFolder() && this.getSrc() && a !== F && !this.loaded && !this.loading ) 
				this.request( b, f );
			if ( this.$( 'r' ) )
				$.arrow( this.$( 'r' ), c ? 'b1' : 'r1' );
			if ( this.loading ) {
				! this.$( 'ld' ) && $.append( this.$( 'i' ) || this.$( 'o' ), '<i id=' + this.id + 'ld class=_ld></i>' );
				$.classAdd( this.$(), 'z-loading' );
			} else if ( this.$( 'i' ) && c != d ) {
				var s = this.html_icon();
				s && $.replace( this.$( 'i' ), s );
			}
			a && a.type && $.stop( a );
		},
		// 当前节点展开时，其他兄弟节点全部收起 /@a -> T/F/event, b -> sync?, f -> fn?
		toggleOne: function( a, b, f ) {
			this.toggle.apply( this, arguments );
			if ( this.isExpanded() ) {
				for ( var i = 0, p = this.parentNode; i < p.length; i ++ ) {
					p[ i ] !== this && p[ i ].toggle( F );
				}
			}
		},
		compare: function( x ) {
			if ( x.text || x.format ) {
				var _x = this.x, b = [ 'icon', 'expandedIcon', 'src', 'cls', 'focus' ];
				this.init_x( x );
				for ( var i = 0, l = b.length, e; i < l; i ++ ) {
					e = b[ i ];
					if ( _x[ e ] !== x[ e ] )
						this.attr( e, x[ e ] || '' );
				}
				if ( this.x.format || (_x.text !== x.text) )
					this.attr( 'text', x.text );
			}
			var n = x.nodes, l = n && n.length;
			if ( l && ! this.loading ) {
				if ( ! this.length ) {
					this.render_nodes( n );
					x.expanded && this.toggle( T );
				} else {
					for ( var i = 0, b, c; i < l; i ++ ) {
						b = n[ i ];
						if ( this[ i ] && b.id === this[ i ].x.id ) {
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
		expandTo: function( a, b, c ) {
			var f = (this.rootNode || this).getFocus();
			typeof b === _FUN && (c = b, b = N);
			this.exec( { type: 'Ajax', src: a, sync: b,
				success: function( x ) {
					if ( this.x.template ) {
						x = _compileTemplate( this, x );
					}
					if ( W.isCmd( x ) ) {
						this.exec( x );
					} else {
						var d = x.id ? this.ownerView.find( x.id ) : this;
						d && d.compare( x );
					}
				},
				complete: function() {
					var d = (this.rootNode || this).getFocus();
					d && d !== f && d.scrollIntoView();
					c && c.call( this );
				} } );
		},
		// @a -> sync?
		reload: function( a ) {
			if ( ! this.loading && this.getSrc() ) {
				this.toggle( F );
				$.ajaxAbort( this );
				var d = this.focusNode && this.focusNode.id, i = this.length;
				while( i -- ) this[ i ].remove();
				this.css( 'o', 'visibility', '' );
				this.loaded = this.loading = F;
				this.toggle( T, a, function() { d && (d = this.ownerView.find( d )) && d.focus() } );
			}
			return this;
		},
		// 获取最新的子节点数据，对比原有数据，如果有新增节点就显示出来 / @a -> sync?, b -> fn?
		reloadForAdd: function( a, b ) {
			if ( this._disposed || this.loading )
				return;
			var u = this.getSrc();
			if ( u ) {
				this.expandTo( u, a, b );
			} else {
				this.reloadForModify( a, function() { u && this.toggle( T, b ) } );
			}
		},
		// 获取父节点的所有子节点数据，取出id相同的项进行更新 / @a -> sync?, b -> fn?
		reloadForModify: function( a, b ) {
			if ( this._disposed )
				return;
			if ( this.loading )
				$.ajaxAbort( this );
			var u = this.parentNode.getSrc();
			if ( u && ! this.parentNode.loading )
				this.parentNode.expandTo( u, a, b );
		},
		// @implement
		removeElem: function( a ) {
			a == N && $.remove( this.$( 'c' ) );
			_proto.removeElem.call( this, a );
		}
	}
} ),
/* `leaf` */
Leaf = define.widget( 'Leaf', {
	Const: function( x, p ) {
		this.level = p.level + (p.x.rootInvisible ? 0 : 1);
		W.apply( this, arguments );
		this.loaded  = this.length ? T : F;
		this.loading = F;
		x.focus && x.focusable !== F && this.tabFocus();
		x.badge && this.init_badge();
	},
	Extend: AbsLeaf,
	Default: { width: -1, height: -1 },
	Listener: {
		body: {
			ready: function() {
				this.length && this.trigger( 'load' );
				this.x.expanded && !this.loaded && this.getSrc() && this.toggle( T );
				this.x.focus && this.focus();
				if ( this.box && ! this.isFolder() && this.nodeIndex === this.parentNode.length -1 ) {
					this._triple();
				}
				this.textNode && this.fixTextNodeWidth();
			},
			mouseOver: {
				occupy: T,
				method: function() { this.isNormal() && $.classAdd( this.$(), 'z-hv' ); }
			},
			mouseOut: {
				occupy: T,
				method: function() { $.classRemove( this.$(), 'z-hv' ); }
			},
			focus: {
				block: T
			},
			click: {
				occupy: T,
				// 点击box不触发业务设置的click事件
				block: function( e ) {
					return this.isDisabled();
				},
				method: function( e ) {
					//mbi && this.toggle();
					if ( this.isDisabled() )
						return;
					this.box && this.box.x.sync === 'click' && ! this.isEvent4Box( e ) && this.box.click();
					e.srcElement ? this._focus( T, e ) : this.focus( T, e );
					if( this.ownerView.combo && !(this.x.on && this.x.on.click) ) {
						$.dialog( this ).commander.complete( this );
						$.close( this );
					}
				}
			},
			dblClick: {
				occupy: T,
				method: function( e ) {
					if ( ! ( this.$( 'o' ) && this.$( 'o' ).contains( e.srcElement ) ) )
						this.toggle();
				}
			},
			nodeChange: function() {
				this.fixFolder();
				Q( '>.w-leaf', this.$( 'c' ) ).removeClass( 'z-first z-last' ).first().addClass( 'z-first' ).end().last().addClass( 'z-last' );
				this.indent();
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Tree',
		className: 'w-leaf',
		// @implement
		x_childtype: $.rt( 'Leaf' ),
		// @implement
		repaintSelf: _repaintSelfWithBox,
		// @a 设为 true 时，获取视觉范围内可见的相邻的下一个节点
		init_badge: function() {
			return Button.prototype.init_badge.call( this );
		},
		scaleWidth: function( a, b ) {
			if ( a == this.textNode && a.isReady ) {
				var w = this.offsetParent().innerWidth();
				return w == N ? N : _proto.scaleWidth.call( this, a, b, w - a.$().offsetLeft );
			}
		},
		fixTextNodeWidth: function() {
			this.textNode.isReady = T;
			_w_rsz_all.call( this.textNode );
		},
		offsetParent: function() {
			return this.rootNode;
		},
		next: function( a ) {
			if ( a == N )
				return _proto.next.call( this );
			if ( a && this.offsetParent() ) {
				if ( this.length && this.x.expanded )
					return this[ 0 ];
				if ( this.nodeIndex === this.parentNode.length - 1 ) {
					var p = this.parentNode;
					while ( p.offsetParent() && ! p.next() ) p = p.parentNode;
					return p.offsetParent() && p.next();
				} else
					return _proto.next.call( this );
			}
		},
		// @a 设为 true 时，获取视觉范围内可见的相邻的下一个节点
		prev: function( a ) {
			var b = _proto.prev.call( this );
			if ( a ) {
				if ( ! b && this.parentNode !== this.offsetParent() )
					return this.parentNode;
				while ( b && b.length && b.x.expanded )
					b = b[ b.length - 1 ];
			}
			return b;
		},
		// 获取类型为leaf的父节点
		parent: function() {
			return this.parentNode !== this.rootNode && this.parentNode;
		},
		focus: function( a, e ) {
			this._focus( a, e );
			a !== F && this.scrollIntoView( 'auto' );
		},
		_focus: function( a, e ) {
			if ( this.x.focusable === F )
				return;
			a = a == N ? T : !!a;
			this.tabFocus( a );
			a !== F && this.triggerHandler( 'focus' );
			if ( this.box && this.box.x.sync === 'focus' && ! this.isEvent4Box( e ) ) {
				if ( a ) {
					for ( var i = 0, r = this.rootNode.descendants(), l = r.length; i < l; i ++ )
						r[ i ] !== this && r[ i ].check( F );
				}
				this.check( a );
			}
		},
		indent: function( a ) {
			this.level = this.parentNode.level + 1;
			if ( this.x.line ) {
				Q( '._pd,._pdvl', this.$() ).remove();
				$.before( this.$( 'o' ), this.html_linepad() );
			} else
				this.css( 'paddingLeft', this.padLeft() );
			for ( var i = 0; i < this.length; i ++ )
				this[ i ].indent();
		},
		toggleFocus: function() {
			this.focus( ! this.isFocus() );
		},
		isFocus: function() {
			return this.rootNode.focusNode === this;
		},
		isExpanded: function() {
			return this.x.expanded;
		},
		isEvent4Box: function( e ) {
			return this.box && e && e.srcElement && e.srcElement.id === this.box.id + 't';
		},
		isBoxChecked: function() {
			return this.box && this.box.isChecked();
		},
		isEllipsis: function() {
			return this.rootNode.x.ellipsis;
		},
		isFirst: function() {
			return this.nodeIndex === 0;
		},
		isLast: function() {
			return this.nodeIndex === this.parentNode.length - 1;
		},
		badge: function( a ) {
			return this.attr( 'badge', a );
		},
		check: function( a ) {
			this.box && this.box.click( a == N || a );
		},
		scrollIntoView: function( a, b ) {
			var n = this;
			while ( (n = n.parentNode) && n.type === this.type )
				n.toggle( T );
			_scrollIntoView( this, a, N, b );
		},
		// triplebox 级联勾选
		_triple: function() {
			var p = this;
			while ( (p = p.parentNode) && p.rootNode === this.rootNode ) {
				if ( p.box ) {
					for ( var i = 0, b, m = [ 0, 0, 0 ], l = p.length; i < l; i ++ ) {
						(b = p[ i ].box) && m[ b.checkstate() ] ++;
					}
					p.box.check( m[ 0 ] === l ? 0 : m[ 1 ] === l ? 1 : 2 );
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
		html_badge: function() {
			return this._badge ? this._badge.html() : '';
		},
		html_icon: function() {
			var c = (this.x.expanded && this.length && this.x.expandedIcon) || this.x.icon;
			return c ? $.image( c, { id: this.id + 'i', cls: 'w-leaf-i' } ) : '';
		},
		html_text: function() {
			var t = this.html_format(), h;
			if ( typeof t === _STR && (h = this.rootNode.x.highlight) && ! this.isDisabled() ) {
				var key = h.key == N ? (this.ownerView.bind && this.ownerView.bind.getHighlightKey()) : h.key;
				key && (t = $.strHighlight( t, key, h.matchlength, h.keyCls ));
			}
			if ( typeof t === _OBJ ) {
				t = (this.textNode = this.add( t, -1 )).addClass( 'w-leaf-node' ).html();
			} else
				t = '<span class=w-leaf-s>' + t + '</span><i class=f-vi></i>';
			return t;
		},
		html_linepad: function() {
			for ( var i = 0, e = '', p; i < this.level; i ++ ) {
				p = (p || this).parent();
				e = '<i class=_pd>' + (p.level == 0 || p.isLast() ? '' : '<u class=_pl></u>') + '</i>' + e;
			}
			return e;
		},
		html_self: function( a ) {
			var x = this.x, r = this.rootNode, p = this.parent(), c = this.x.line, d = x.data, e = c ? this.html_linepad() : '', h = this.innerHeight(), s = 'padding-left:' + this.padLeft() + 'px;';
			if ( x.box ) {
				this.box = CheckBox.parseOption( this, { cls: 'w-leaf-b', bubble: F } );
				this.box.type === 'TripleBox' && this.box.addEvent( 'click', this._triple, this );
			}
			h != N  && (s += 'height:' + h + 'px;');
			x.style && (s += x.style);
			a == N  && (a = this.length);
			var t = this.html_text();
			return this.html_before() + '<dl class="' + this.className + ' z-level' + this.level + (x.cls ? ' ' + x.cls : '') + (c ? ' z-line' : '') + (this.isFirst() ? ' z-first' : '') + (this.isLast() ? ' z-last' : '') + (this.isDisabled() ? ' z-ds' : '') + (this.isFolder() ? ' z-folder' : '') + (this.isFolder() && x.expanded ? ' z-expanded' : '') + (this.isEllipsis() && !this.textNode ? ' f-omit' : ' f-nobr') +
				'" id=' + this.id + this.prop_title() + _html_on.call( this ) + (x.id ? ' w-id="' + x.id + '"' : '') + ' style="' + s + '">' + this.html_prepend() +
				'<dt class="w-leaf-a">' + e + (x.noToggle ? '' : '<b class=w-leaf-o id=' + this.id + 'o onclick=' + evw + '.toggle(event)><i class=f-vi></i>' + (this.isFolder() ? $.arrow( this.id + 'r', x.expanded ? 'b1' : 'r1' ) : '') + (c ? '<i class=_vl></i><i class=_hl></i>' : '') + '</b>') +
				(this.box ? this.box.html() : '') + this.html_icon() + '<cite class=w-leaf-t id=' + this.id + 't>' + t + '</cite></dt>' + this.html_append() + this.html_badge() + '</dl>' + this.html_after();
		},
		html: function() {
			var f = this.rootNode._filter_leaves, b = !f, s = this.html_nodes();
			if ( f ) {
				for ( var i = 0; i < f.length; i ++ ) {
					if ( this.contains( f[ i ] ) ) { b = T; break; }
				}
				this.x.expanded = b;
				this.x.status = $.inArray( f, this ) ? '' : 'disabled';
				$.classAdd( this, 'z-notg', !!(this.length && !s) ); // 子节点都被过滤时，隐藏tg
			}
			return b ? this.html_self() + '<div class="w-leaf-cont' + (this.isFolder() && this.x.expanded ? ' z-expanded' : ' f-hide') + '" id=' + this.id + 'c>' + s + '</div>' : '';
		}
	}
} ),
/* `tree` */
Tree = define.widget( 'Tree', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		if ( x.hiddens )
			this._hiddens = this.add( { type: 'hiddens', nodes: x.hiddens }, -1 );
		this.loaded  = this.length ? T : F;
		this.loading = F;
	},
	Extend: [ Scroll, AbsLeaf ],
	Default: { scroll: T },
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, Scroll, e );
				this.length && this.trigger( 'load' );
				if ( !this.length && this.getSrc() )
					Leaf.prototype.request.call( this );
			},
			nodeChange: function() {
				this.addClass( 'z-empty', !this.length );
			}
		}
	},
	Prototype: {
		className: 'w-tree',
		level: -1,
		// @implement
		x_childtype: $.rt( 'Leaf' ),
		reloadForModify: $.rt(),
		focus: function() {	this[ 0 ] && this[ 0 ].focus() },
		getFocus: function() { return this.focusNode },
		// @f -> filter leaves(经过筛选的行)
		setFilter: function( f ) { this._filter_leaves = f },
		//@implement  /@ a -> html|widget, b -> method(prepend,append,after,before)
		insertHTML: function( a, b ) {
			Scroll.prototype.insertHTML.call( this, a, b );
			if ( a.isWidget && a.$() ) {
				$.after( a.$(), a.$( 'c' ) );
				a.indent();
			}
		},
		// @k -> keycode
		keyUp: function( k ) {
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
		html_nodes: function() {
			var s = _proto.html_nodes.call( this );
			$.classAdd( this, 'z-empty', !s );
			return '<div id=' + this.id + 'c class=w-tree-gut>' + s + '</div>' + (this._hiddens ? this._hiddens.html() : '')
		}
	}
} ),
Hiddens = define.widget( 'Hiddens', {
	Default: { width: 0, height: 0 },
	Prototype: {
		x_childtype: $.rt( 'Hidden' ),
		html: _proto.html_nodes
	}
} ),
// `tablecombo` 表格搜索过滤器
TableCombo = _comboHooks.Table = $.createClass( {
	Const: function( a, b ) {
		this.cab = a;
		this.bind = b;
		this.xml = this.node2xml( a );
		this._keep_show = b.keepShow;
		for ( var i = 0, c = a.x.columns, l = c && c.length; i < l; i ++ ) {
			if ( c[ i ].highlight ) {
				this._matchlength = c[ i ].highlight.matchlength;
				break;
			}
		}
	},
	Extend: TreeCombo,
	Prototype: {
		type: 'Table',
		node2xml: function( a ) {
			for ( var i = 0, j, b = this.bind.field, c = [], d, t = a.contentBody(), e = b.search && b.search.split( ',' ), f = e && e.length, l = t && t.length, r, s; i < l; i ++ ) {
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
			a.contentBody() && a.setFilter( this._filter( t ) );
			return a.getEchoRows().length;
		},
		getLength: function() {
			return this.cab.getEchoRows().length;
		}
	}
} ),
/* Table的column设置了fixed时，会产生分身Table(LeftTable, RightTable)。当调用Table的方法比如append remove等，分身也需要同样处理，以保持数据一致。
   _parallel_methods使Table的方法能作用到分身Table上。 */
_parallel_methods = function( a, b ) {
	$.each( b.split( ' ' ), function( v ) {
		var f = a.prototype[ v ];
		a.prototype[ v ] = function() {
			if ( (this.rootNode || this).fixed.length ) {
				for ( var j = 0, c = this.getParallel(); j < c.length; j ++ )
					(c[ j ][ v ] === arguments.callee ? f : c[ j ][ v ]).apply( c[ j ], arguments );
			} else
				f.apply( this, arguments );
		}
	} );
},
/* Table 辅助方法和专用类 */
_table_f_attr = function( v ) {
	return typeof v === _OBJ && v && (! v.type || v.type === TD.type) ? (v.text || '') : v;
},
_table_tr = function() {
	return this.closest( function() { return this.type_tr || this.type_thr || this.type_tfr } );
},
/* `Tableleaf` */
TableLeaf = define.widget( 'TableLeaf', {
	Const: function( x, p ) {
		Leaf.apply( this, arguments );
		var r = this.tr();
		this.level = r.level;
		x.src && (this.loaded = !!r.length);
		x.expanded == N && (x.expanded = !x.src || !!r.length);
		r.tableLeaf = this;
		this.row = r;
	},
	Extend: Leaf,
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, Leaf, e );
				this.textNode && this.fixTextNodeWidth();
				this.x.expanded === F && this.toggle( F );
			},
			resize: function() {
				this.textNode && _w_css.width.call( this.textNode );
			},
			mouseOver: N,
			mouseOut: N
		}
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		className: 'w-leaf w-table-leaf',
		_pad_left: 0,
		tr: _table_tr,
		_focus: $.rt(),
		isEllipsis: $.rt( T ),
		getLength: function() {
			return this.row.length;
		},
		parent: function() {
			return this.row.parentNode.tableLeaf;
		},
		offsetParent: function() {
			return this.parentNode;
		},
		isFirst: function() {
			return this.row.nodeIndex === 0;
		},
		isLast: function() {
			return this.row.nodeIndex === this.row.parentNode.length - 1;
		},
		isFolder: function() {
			return this.row.length || (this.x.src && !this.loaded) ? T : F;
		},
		fix_text_size: function() {
			var t = this.textNode, a = $.bcr( this.$() ), b = $.bcr( t.$() ), c = b.left - a.left;
			if ( c > 0 ) {
				t.attr( 'widthMinus', (t.attr( 'widthMinus' ) || 0) + c );
				t.width( t.attr( 'width' ) || '*' );
			}
		},
		// leaf接口
		toggle_nodes: function( a ) {
			this.row.toggle_rows( a );
		},
		// leaf接口
		render_nodes: function( x ) {
			this.tr().append( x );
			//for ( var i = 0, l = x.length, r = this.tr(); i < l; i ++ )
			//	r.add( x[ i ] );
			//l && Q( r.$() ).after( r.html_nodes() );
			this.loaded = T;
		},
		// leaf接口
		indent: function( a ) {
			var p = this.parent();
			this.level = p ? p.level + 1 : 0;
			if ( this.x.line ) {
				Q( '._pd,._pdvl', this.$() ).remove();
				$.before( this.$( 'o' ), this.html_linepad() );
			} else
				this.css( 'paddingLeft', this.padLeft() );
			Q( this.$() ).removeClass( 'z-first z-last' );
			this.isFirst() && $.classAdd( this.$(), 'z-first' );
			this.isLast() && $.classAdd( this.$(), 'z-last' );
			for ( var i = 0, l = this.row.length; i < l; i ++ )
				this.row[ i ].tableLeaf && this.row[ i ].tableLeaf.indent();
		},
		html: function() {
			var r = this.tr(), s = this.html_self( r.length );
			return this.x.hr ? '<table class=w-hr-table cellspacing=0 cellpadding=0><tr><td>' + s + '<td width=100%><hr class=w-hr-line noshade></table>' : s;
		}
	}
} ),
/* `tabletoggle` */
TableToggle = define.widget( 'TableToggle', {
	Const: function( x, p ) {
		Toggle.apply( this, arguments );
		this.tr().tableToggle = this;
	},
	Extend: Toggle,
	Listener: {
		body: { click: N }
	},
	Prototype: {
		tr: function() {
			return this.closest( 'TR' );
		},
		isExpanded: function() {
			return this.x.expanded;
		},
		toggle: function( a ) {
			Toggle.prototype.toggle.apply( this, arguments );
			var t = this.tr();
			t.toggle_rows( this.x.expanded );
			for ( var i = t.nodeIndex + 1, d = t.parentNode, c, l = d.length; i < l; i ++ ) {
				c = d[ i ];
				if ( c.tableToggle )
					break;
				c.display( this );
			}
			t.table.parallel( 'fixFoot' );
		}
	}
} ),
/* `tablerownum` */
TableRowNum = define.widget( 'TableRowNum', {
	Const: function( x, p ) {
		Html.apply( this, arguments );
		var r = this.tr(), t = _number( this.attr( 'start' ) );
		x.text = (t + r.nodeIndex);
		r.rownum = this;
	},
	Extend: Html,
	Default: { start: 1 },
	Prototype: {
		className: 'w-tablerownum',
		tr: _table_tr,
		reset: function() {
			this.text( _number( this.attr( 'start' ) ) + this.tr().nodeIndex );
		}
	}
} ),
/* `tabletriplebox` */
TableTripleBox = define.widget( 'TableTripleBox', {
	Const: function( x, p ) {
		TripleBox.apply( this, arguments );
		this.tr().box = this;
	},
	Extend: TripleBox,
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, TripleBox, e );
				this.checkstate() > 0 && this.triggerListener( 'change' );
			},
			change: {
				occupy: T,
				method: function() {
					var r = this.tr();
					r.type_tr && r.addClass( 'z-checked', this.isChecked() );
				}
			}
		}
	},
	Prototype: {
		className: 'w-checkbox w-triplebox',
		tr: _table_tr,
		getRelates: function() {
			var r = [], g = this.tr().table;
			g.head && r.push.apply( r, this.ownerView.fAll( this.x.name, g.head ) );
			return r.concat( this.ownerView.fAll( this.x.name, g.body ) );
		}
	}
} ),
/* `tableradio` */
TableRadio = define.widget( 'TableRadio', {
	Const: function( x, p ) {
		Radio.apply( this, arguments );
		this.tr().box = this;
	},
	Extend: Radio,
	Listener: {
		body: {
			ready: function( e ) {
				_superTrigger( this, Radio, e );
				this.x.checked && this.triggerListener( 'change' );
			},
			change: {
				occupy: T,
				method: function() {
					var r = this.tr();
					Q( '>.z-checked', r.parentNode.$() ).removeClass( 'z-checked' );
					r.type_tr && r.addClass( 'z-checked', this.isChecked() );
				}
			}
		}
	},
	Prototype: {
		className: 'w-f w-radio',
		tr: _table_tr
	}
} ),
/* `tablerow` tr 和 hr 的基础类 */
TableRow = define.widget( 'TableRow', {
	Const: function( x, p, n ) {
		this.level = p.level == N ? 0 : p.level + 1;
		if ( typeof x.data !== _OBJ )
			x = { data: x };
		this.table = p.table;
		W.call( this, x, p, n );
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		className: 'w-tr',
		// @implement
		repaintSelf: _repaintSelfWithBox,
		getData: function() {
			var d = {}, l = this.length;
			if ( l ) {
				for ( var i = 0, r = []; i < l; i ++ )
					r.push( this[ i ].getData() );
				d.nodes = r;
			}
			this.x.data && (d.data = this.x.data);
			return d;
		},
		// 获取平行的行 /@a -> get root row?
		getParallel: function( a ) {
			var b = this.type_tr ? 'body' : this.type_thr ? 'head' : 'foot', i = this.$().rowIndex, u = this.rootNode, r = this.table === this.rootNode ? this : _widget( u[ b ].contentTable.$().rows[ i ] );
			if ( a )
				return r;
			for ( var j = 0, r = [ r ], t; j < u.fixed.length; j ++ ) {
				(t = u.fixed[ j ][ b ].contentTable.$().rows[ i ]) && r.push( _widget( t ) );
			}
			return r;
		},
		// x -> widget option, i -> colIndex
		addCell: function( x, i ) {
			var e = this.rootNode.x.escape !== F;
			if ( e )
				$.extend( x, { escape: e } );
			var n = (this.tcell || (this.tcell = new TCell( {}, this ))).add( x );
			n.col = this.table.getColGroup()[ i ];
			return n;
		},
		cellElem: function( i ) {
			var c = this.$().cells;
			if ( c.length === this.table.getColGroup().length )
				return c[ i ];
			for ( var i = 0, l = c.length, n = 0; i < l; i ++ ) {
				if ( n == i )
					return c[ i ];
				n += c[ i ].colSpan;
			}
		},
		offsetParent: function() {
			var p = this.parentNode;
			while( p.level != N )
				p = p.parentNode;
			return p;
		},
		prev: Leaf.prototype.prev,
		// @a -> T/F, b -> src
		toggle: function( a, b ) {
			if ( this.tableToggle || this.tableLeaf ) {
				(this.tableToggle || this.tableLeaf).toggle( a );
			} else {
				if ( this.length ) {
					a == N && (a = ! this.x.expanded);
					this.toggle_rows( a );
				} else if ( this.x.src ) {
					var c = this.table.getColGroup(), d = {}, r;
					d[ c[ 0 ].x.field ] = { colSpan: c.length, node: { type: 'View', src: this.x.src } };
					this.append( d ).isExpandRow = T;
					this.x.expanded = T;
				}
				if ( this.length ) {
					this.trigger( this.isExpanded() ? 'expand' : 'collapse' );
				}
			}
		},
		toggle_rows: function( a ) {
			var i = 0, l = this.length;
			if ( l ) {
				for ( ; i < l; i ++ ) {
					//this[ i ].css( 'display', a ? '' : 'none' )
					this[ i ].display( a );
					this[ i ].toggle_rows( a ? this[ i ].isExpanded() : a );
				}
				this.x.expanded = a;
			}
		},
		// 高亮某个字段的关键字 /@ a -> colIndex, b -> key, c -> matchlength, d -> keyCls
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
			var a = this.nodeIndex, b = [], u = this.table, c = u.getColGroup(), d = this.x.data, e = this.type_tr, h = u.x.escape !== F, r = this.offsetParent()._rowSpan,
				i = i == N ? 0 : i, t, k, L = c.length - 1, l = l == N ? L : l;
			for ( ; i <= l; i ++ ) {
				if ( r && r[ this.level ] && r[ this.level ][ a ] && r[ this.level ][ a ][ i ] ) {
					i += r[ this.level ][ a ][ i ] - 1;
					continue;
				}
				var k = i, s = t = '', f = c[ i ].x, v = d && d[ f.field ];
				if ( v != N && typeof v === _OBJ ) {
					if ( v.rowSpan > 1 ) {
						for ( var j = 1; j < v.rowSpan; j ++ )
							$.jsonChain( v.colSpan || 1, r, this.level, a + j, i );
					}
					if ( v.colSpan > 1 )
						i += v.colSpan - 1;
					t = this.addCell( v, k );
				} else {
					if ( e ) {
						var m = this.html_format( v, f.format, h, _table_f_attr );
						if ( typeof m === _OBJ )
							t = this.addCell( m, i );
						else
							v = m;
					} else {
						h && (v = $.strEscape( v ));
					}
					if ( v != N && e && f.highlight ) {
						var key = f.highlight.key == N ? (this.ownerView.bind && this.ownerView.bind.getHighlightKey()) : f.highlight.key;
						key && (v = $.strHighlight( v, key, f.highlight.matchlength, f.highlight.keyCls ));
					}
				}
				f.align  && (s += ' align='  + f.align);
				f.vAlign && (s += ' valign=' + f.vAlign);
				if ( t ) {
					b.push( t.html( k, i, L ) );
				} else {
					v = v == N ? '' : v;
					var g = '';
					if ( !e || u.x.br === F )
						g += ' class="f-fix"';
					if ( this.type_thr && f.sort )
						v += c[ i ].html_sortarrow();
					if ( f.tip )
						g += this.prop_title( (d && d[ f.tip.field || f.field ]) || '', f.format, h );
					g && (v = '<div' + g + '>' + v + '</div>');
					b.push( '<td class="w-td z-face-' + u._face + (k === 0 ? ' z-first' : '') + (i === L ? ' z-last' : '') + (this.type_thr ? ' w-th' + (f.sort ? ' w-th-sort' + (c[ i ]._sort ? ' z-' + c[ i ]._sort : '') : '') : '') +
						(!ie7 && f.fixed ? ' f-form-hide' : '') + (f.cls ? ' ' + f.cls : '') + '"' + s + (f.style ? ' style="' + f.style + '"' : '') + '>' + (v == N ? (ie7 ? '&nbsp;' : '') : v) + '</td>' );
				}
			}
			return b.join( '' );
		},
		html: function( i ) {
			var a = '', c = this.x.cls, h = this.attr( 'height' ),
				s = '<tr id=' + this.id + ' class="' + this.className + (this.x.focus ? ' z-on': '') + (this.type_tr ? ' z-' + ((i == N ? this.nodeIndex : i) % 2) : '') + (c ? ' ' + c : '') + '"';
			if ( h ) {
				ie7 && (h -= this.table._pad * 2 + (this.table._face === 'none' ? 0 : 1));
				a += 'height:' + h + 'px;';
			}
			this.x.id && (s += ' w-id="' + this.x.id + '"');
			this.x.style && (a += this.x.style);
			a && (s += ' style="' + a + '"');
			s += _html_on.call( this ) + '>' + this.html_cells() + '</tr>';
			return this.length ? s + this.html_nodes() : s;
		},
		remove: function() {
			var i = this.length;
			while ( i -- ) this[ i ].remove( T );
			_proto.remove.apply( this, arguments );
		}
	}
} );
_parallel_methods( TableRow, 'addClass removeClass display before after append prepend replace remove' );
var
_td_wg = { Leaf: T, Toggle: T },
/* `td` tcell 的子节点。当字段是一个 widget 时会先产生一个 TD 实例，包裹这个 widget 子节点。 */
TD = define.widget( 'TD', {
	Const: function( x, p ) {
		if ( x.type && x.type !== 'TD' )
			x = { node: x };
		x.colSpan == -1 && (x.colSpan = p.table.getColGroup().length);
		x.node && p.table.x.escape !== F && $.extend( x.node, { escape: T } );
		this.table = p.table;
		W.call( this, x, p );
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		// @implement
		x_childtype: function( t ) {
			return _td_wg[ t ] ? 'Table' + t : t;
		},
		html: function( k, i, L ) {
			var r = this.parentNode.parentNode, g = this.table, u = this.rootNode, c = this.col, d = this.x.escape == N ? g.x.escape : this.x.escape, e = r.type_tr, s = '<td id=' + this.id, t = '', v;
			this.className = 'w-td z-face-' + g._face + (k === 0 ? ' z-first' : '') + (i === L ? ' z-last' : '') + (r.type_tr ? '' : ' w-th' + (r.type_thr && c.x.sort ? ' w-th-sort' : ''));
			s +=  ' class="' + this.prop_cls() + (c.x.cls ? ' ' + c.x.cls : '') + (!ie7 && c.x.fixed ? ' f-form-hide' : '') + '"';
			if ( this.x.on || this.Const.Listener )
				s += _html_on.call( this );
			if ( c.x.align || this.x.align )
				s += ' align='  + (this.x.align || c.x.align);
			if ( c.x.vAlign || this.x.vAlign )
				s += ' valign=' + (this.x.vAlign || c.x.vAlign);
			this.x.colSpan > 1 && (s += ' colspan=' + this.x.colSpan);
			this.x.rowSpan > 1 && (s += ' rowspan=' + this.x.rowSpan);
			c.x.style && (t += c.x.style);
			this.x.style && (t += this.x.style);
			t && (s += ' style="' + t + '"');
			this.x.id && (s += ' w-id="' + this.x.id + '"');
			t = this.x.text;
			if ( t ) {
				t = this.html_format( t, e ? this.x.format || c.x.format : this.x.format, d, _table_f_attr );
				if ( typeof t === _OBJ )
					this.x.node = t;
			}
			s += '>' + this.html_prepend() + this.html_nodes();
			if ( ! this.x.node ) {
				var f = '';
				if ( ! e || g.x.br )
					f += ' class="f-fix"';
				if ( ! e && c._sort )
					t += c.html_sortarrow();
				if ( e && c.x.tip ) {
					var g = r.x.data && r.x.data[ c.x.tip.field || c.x.field ];
					f += this.prop_title( typeof g === _OBJ ? g.text : f, c.x.format );
				}
				f && (t = '<div' + f + '>' + t + '</div>');
				s += t || (ie7 ? '&nbsp;' : '');
			}
			return s + this.html_append() + '</td>';
		}
	}
} ),
/* `tr` */
TR = define.widget( 'TR', {
	Extend: TableRow,
	Default: ie7 && {
		heightMinus: function() {
			return this.rootNode._pad * 2 + (this.rootNode._face == 'none' ? 0 : 1);
		}
	},
	Listener: {
		body: {
			ready: function() {
				if ( this.table != this.rootNode )
					Q( this.$() ).height( Q( this.getParallel( T ).$() ).height() );
			},
			click: {
				occupy: T,
				block: function( e ) { return this.isExpandRow || this.tableToggle },
				method: function( e ) {
					var r = this.rootNode, b = this.getBox(), s = b && b.x.sync;
					! this.tableToggle && this.focus( r.x.focusMultiple ? (!this.isFocus()) : T, e );
					b && ! this.isEvent4Box( e ) && s === 'click' && b.click();
					this.x.src && this.toggle();
					if( this.ownerView.combo && ! (this.x.on && this.x.on.click) ) {
						$.dialog( this ).commander.complete( this );
						$.close( this );
					}
				}
			},
			mouseOver: {
				occupy: T,
				method: function() { this.addClass( 'z-hv' ) }
			},
			mouseOut: {
				occupy: T,
				method: function() { this.removeClass( 'z-hv' ) }
			},
			nodeChange: function() {
				var l = this.tableLeaf;
				if ( l ) {
					l.fixFolder();
					l.toggle( this.isExpanded() );
					l.indent();
				}
				this.closest( 'ContentTableBody' ).trigger( 'nodeChange' );
			}
		}
	},
	Prototype: {
		type_tr: T,
		x_childtype: $.rt( 'TR' ),
		getPageIndex: function() {
			var r = this.rootNode, a = $.arrIndex( r._filter_rows || r.contentBody() || [], this );
			return r.x.limit ? Math.floor( a / r.x.limit ) + 1 : 1;
		},
		focus: function( a, e ) {
			if ( this._disposed )
				return;
			var a = a == N ? T : a, r = this.rootNode;
			if ( a && ! this.$() && r.x.limit ) {
				r.page( this );
			}
			if ( this.x.focusable ) {
				a && ! r.x.focusMultiple && (f = r.getFocus()) && f !== this && f.focus( F );
				if ( this.$() ) {
					this.getParallel( T ).x.focus = a;
					var b = this.getBox(), f, g = this.$();
					this.addClass( 'z-on', a );
					b && b.x.sync === 'focus' && !this.isEvent4Box( e ) && this.check( a );
				}
				a && this.trigger( 'focus' );
			}
		},
		isFocus: function( a ) {
			return this.x.focus;
		},
		toggleFocus: function() {
			this.focus( ! this.isFocus() );
		},
		// 获取所有焦点行 / @a -> visible?
		getFocusAll: function( a ) {
			var r;
			if ( this.x.focus && (!a || this.$()) )
				r = [ this ];
			if ( this.length ) {
				var i = this.length, c;
				while ( i -- ) {
					(c = this[ i ].getFocusAll( a )) && c.length && (r = (r || (r = [])).concat( c ));
				}
			}
			return r;
		},
		isEvent4Box: function( e ) {
			var b = this.getBox();
			return b && e && e.srcElement && e.srcElement.id == b.id + 't';
		},
		getBox: function() {
			return this.box;
		},
		// 勾选  /@ a -> T/F
		check: function( a ) {
			var b = this.getBox();
			b && b.check( a );
		},
		isBoxChecked: function() {
			var b = this.getBox();
			return b && b.isChecked();
		},
		isExpanded: function() {
			var g = this.tableToggle || this.tableLeaf;
			return g ? g.isExpanded() : this.x.expanded;
		},
		next: function( n ) {
			if ( this.rootNode._echo_rows ) {
				var b = Q( this.$() )[ n === F ? 'prev' : 'next' ]();
				return b.length && _widget( b[ 0 ] );
			} else
				return this.parentNode[ this.nodeIndex + ( n === F ? -1 : 1 ) ];
		},
		prev: function() {
			return this.next( F );
		},
		// 计算所有子孙节点共有多少行
		_childLen: function() {
			for ( var l = this.length, i = 0, j = 0; j < l; j ++ )
				this[ j ].$() && (i += this[ j ]._childLen() + 1);
			return i;
		},
		// @a -> index|"+=index"
		move: function( a ) {
			var c = a;
			if ( typeof a === _STR && a.charAt( 1 ) == '=' )
				eval( 'c=this.nodeIndex;c' + a );
			if ( (c = this.parentNode[ c ]) && c != this )
				c[ c.nodeIndex > this.nodeIndex ? 'after' : 'before' ]( this );
		},
		// @implement tr 子节点的特殊处理
		insertHTML: function( a, b ) {
			if ( ! this.$() )
				return;
			var s = a;
			if ( a.isWidget ) {
				s = [];
				for ( var i = a.$().rowIndex, j = 0, p = a.$().parentNode.parentNode, l = a._childLen(); j <= l; j ++ )
					s.push( p.rows[ i + j ] );
			}
			if ( b === 'before' )
				Q( this.$() )[ b ]( s );
			else
				Q( b === 'prepend' ? this.$() : this.$().parentNode.parentNode.rows[ this.$().rowIndex + this._childLen() ] ).after( s );
		}
	}
} ),
/* `thr`  thead 的子节点 */
THR = define.widget( 'THR', {
	Const: function( x, p, n ) {
		TableRow.apply( this, arguments );
		var u = this.rootNode.x.pub;
		if ( u && u.height )
			this.defaults( { height: u.height } );
		//this.className += ' w-' + this.type.toLowerCase();
	},
	Extend: TableRow,
	Default: TR.Default,
	Listener: {
		body: {
			ready: TR.Listener.body.ready
		}
	},
	Prototype: {
		type_thr: T,
		getBox: TR.prototype.getBox,
		isEvent4Box: TR.prototype.isEvent4Box,
		// a -> T/F|event
		check: function( a ) {
			var b = this.getBox();
			if ( b ) {
				for ( var i = 0, c = b.isChecked(), r = this.rootNode.rows(), l = r.length; i < l; i ++ ) {
					r[ i ].check( a );
				}
			}
		}
	}
} ),
/* `tfr`  tfoot 的子节点 */
TFR = define.widget( 'TFR', {
	Extend: THR,
	Prototype: {
		type_thr: F,
		type_tfr: T
	}
} ),
// `TCell` 是 tr 的离散节点。当 tr 添加 td 时，先创建一个 tcell ，然后 tcell 添加这个 td
TCell = define.widget( 'TCell', {
	Const: function( x, p ) {
		this.table = p.table;
		W.call( this, x, p, -1 );
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		x_childtype: $.rt( 'TD' ),
		scaleWidth: function( a ) {
			var w = 0, l = a.x.colSpan || 1, r = this.table, g = this.table.getColGroup(), c = a.col, d = r._pad, e;
			while ( l -- )
				(e = g[ a.col.nodeIndex + l ]) != N && (w += e.width());
			if ( isNaN( w ) )
				return N;
			if ( r._face == 'cell' && a.col.nodeIndex < g.length - 1 )
				w -= ie7 ? 2 : 1; //fixme: ie7下需要多减一个像素，原因待查
			w -= c.x.widthMinus != N ? c.x.widthMinus : c.x.style ? _size_fix( N, 'padding:0 ' + d + 'px 0 ' + d + 'px;' + c.x.style ).widthMinus : d * 2;
			return w;
		},
		scaleHeight: $.rt( N ),
		html: $.rt( '' )
	}
} ),
/* `ContentTableBody` */
ContentTableBody = define.widget( 'ContentTableBody', {
	Const: function( x, p ) {
		this.table = p.table;
		W.apply( this, arguments );
		this._rowSpan = {};
	},
	Listener: {
		body: {
			nodeChange: function() {
				this.table.fixRowCls();
				this.table.fixFoot();
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		// @implement
		x_childtype: $.rt( 'TR' ),
		// @implement
		insertHTML: function( a, b ) {
			this.$() && Q( this.$() )[ b || 'append' ]( a.isWidget ? a.$() : a );
		},
		// 获取平行的tablebody/contentHead /@a -> get root contentBody?
		getParallel: function( a ) {
			var u = this.rootNode, r = this.table === u ? this : u[ this.instanceType ]();
			if ( a )
				return r;
			for ( var j = 0, r = [ r ]; j < u.fixed.length; j ++ )
				r.push( u.fixed[ j ][ this.instanceType ]() );
			return r;
		},
		// @a -> rows json, b -> index
		insertCol: function( a, b ) {
			var r = this.rootNode, g = this.parentNode.colgroup;
			b = b == N || ! g[ b ] ? g.length - 1 : b;
			for( var i = 0, l = Math.min( a.length, this.length ); i < l; i ++ ) {
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
		html_nodes: function() {
			for ( var i = 0, r = this.table.getEchoRows(), l = r.length, s = []; i < l; i ++ )
				s.push( r[ i ].html( i ) );
			return s.join( '' );
		},
		html: function() { return '<tbody id=' + this.id + '>' + this.html_nodes() + '</tbody>' }
	}
} );
_parallel_methods( ContentTableBody, 'prepend append before after' );
var
/* `ContentTableHead` */
ContentTableHead = define.widget( 'ContentTableHead', {
	Extend: ContentTableBody,
	Listener: {
		body: {
			ready: function() {
				// 拖动表头调整大小
				var r = this.rootNode, g = this.table, c = this.parentNode.colgroup;
				if ( r.x.resizable ) {
					Q( 'td', this.$() ).append( '<div class=w-th-rsz></div>' );
					Q( '.w-th-rsz', this.$() ).height( this.height() ).on( 'mousedown', function( e ) {
						var b = this.parentNode, n = Column.index( b ), x = e.pageX,
							o = $.db( '<div style="position:absolute;width:1px;height:' + r.height() + 'px;top:' + $.bcr( this ).top + 'px;background:#aaa;"></div>' );
						$.moveup( function( e ) {
							o.style.left = e.pageX + 'px';
						}, function( e ) {
							g.colWidth( n, g.colWidth( n ) + e.pageX - x );
							if ( r != g ) {
								var d = c[ n ].x.fixedIndex;
								r.colWidth( d, r.colWidth( d ) + e.pageX - x );
							}
							o && $.remove( o );
						} );
					} );
				}
				// 排序
				for ( var i = 0, d = F; i < c.length; i ++ ) {
					if ( c[ i ].x.sort ) {
						Q( '.w-th-sort', this.$() ).each( function() {
							Q( this ).click( function( e ) {
								var o = Q( e.target );
								r.order( c[ Column.index( this ) ].x.field, o.hasClass( 'f-arw-t1' ) ? 'asc' : o.hasClass( 'f-arw-b1' ) ? 'desc' : N );
							} );
						} );
						break;
					}
				}
			}
		}
	},
	Prototype: {
		x_childtype: $.rt( 'THR' ),
		html_nodes: _proto.html_nodes
	}
} ),
/* `ContentTableFoot` */
ContentTableFoot = define.widget( 'ContentTableFoot', {
	Extend: ContentTableHead,
	Listener: {
		body: {
			ready: N
		}
	},
	Prototype: {
		x_childtype: $.rt( 'TFR' )
	}
} ),
/* `column` */
Column = define.widget( 'Column', {
	Const: function( x, p ) {
		this.table = p.table;
		W.apply( this, arguments );
		if ( x.sort )
			this._sort = x.sort.status;
	},
	Default: { widthMinus: 0, width: '*' },
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
				this.css( 'width', Math.max( this.innerWidth() - this.width_minus(), 0 ) );
				this.fixWidth();
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		th: function() {
			var t = this.table.contentHead();
			return t && t[ 0 ].$().cells[ this.nodeIndex ];
		},
		width_minus: function() {
			var n = 0, r = this.rootNode;
			if ( ie7 ) {
				var d = r._pad,
					n = _size_fix( N, 'padding:0 ' + d + 'px 0 ' + d + 'px;' + (r._face === 'cell' && this.nodeIndex < this.parentNode.length - 1 ? 'border-right:1px solid #eee;' : '') + this.x.style ).widthMinus;
			}
			if ( this.table != r && r._face === 'cell' && this.nodeIndex === 0 ) {
				n -= 1;
			}
			return n;
		},
		fixWidth: function() {
			if ( this.table !== this.rootNode ) {
				var w = this.rootNode.getColGroup()[ this.x.fixedIndex ].width();
				this.width() !== w && this.width( w );
			}
		},
		html_sortarrow: function( a ) {
			return '<div class="w-th-sortwrap f-wdbr f-nv">' + $.arrow( 't1' ) + $.arrow( 'b1' ) + '</div>';
		},
		html: function() {
			return '<col id=' + this.id + ' style=width:' + (this.innerWidth() - this.width_minus()) + 'px>';
		}
	}
} ),
/* `colgroup` */
ColGroup = define.widget( 'ColGroup', {
	Const: function( x, p ) {
		this.table = p.table;
		W.apply( this, arguments );
	},
	Extend: HorzScale,
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		isScaleCover: T,
		x_childtype: $.rt( 'Column' ),
		scaleWidth: function( a ) {
			return this.table !== this.rootNode ? this.rootNode.getColGroup()[ a.x.fixedIndex ].width() : HorzScale.prototype.scaleWidth.call( this, a );
		},
		insertCol: function( a, b ) {
			 b == N || ! this[ b ] ? this.append( a ) : this[ b ].before( a );
		},
		deleteCol: function( a ) {
			this[ a ] && this[ a ].remove();
		},
		html: function() { return '<colgroup id=' + this.id + '>' + this.html_nodes() + '</colgroup>' }
	}
} ),
/* `ContentTable` */
ContentTable = define.widget( 'ContentTable', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.table = p.table;
		this.colgroup = new ColGroup( { nodes: x.columns }, this );
		if ( x.tHead )
			this.contentHead = new ContentTableHead( x.tHead, this );
		else if ( x.tFoot )
			this.contentFoot = new ContentTableFoot( x.tFoot, this );
		else
			this.contentBody = new ContentTableBody( x.tBody || {}, this );
	},
	Listener: {
		body: {
			resize: function() {
				if ( (br.ms || mbi) && this.$() ) {
					var w = this.innerWidth();
					this.css( 'width', w == N ? '' : w );
				}
			}
		}
	},
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		html: function() {
			var s = '<table id=' + this.id + ' class=w-' + this.rootNode.type.toLowerCase() + '-tbl cellspacing=0 cellpadding=' + this.table._pad;
			if ( br.ms || mbi ) {
				var w = this.innerWidth();
				w != N && (s += ' style="width:' + w + 'px"');
			}
			s += '>';
			for ( var t = '<tr>', i = 0, c = this.colgroup, l = c.length; i < l; i ++ )
				t += '<td' + (c[ i ].x.style ? ' style="' + c[ i ].x.style + '"' : '') + '>'; // 如果不加这个style，兼容模式下宽度可能会失调
			//IE下如果不加这个thead，当有colSpan的列时，宽度可能会失调
			if ( br.ms )
				s += '<thead class="_fix_thead">' + t + '</thead>';
			s += this.html_nodes();
			// Chrome下如果不加这个tfoot，当table没有数据时，表格不会自动撑开，无法触发滚动条
			// 虽然用thead也能实现效果，但没有适合这种效果的原生CSS，需要JS代码里去做。用tfoot则可以用 tbody:empty + ._fix_tfoot 的原生CSS来实现，比用代码更安全
			if ( ! br.ms )
				s += '<tfoot class="_fix_tfoot">' + t + '</tfoot>';
			return s + '</table>';
		}
	}
} ),
/* `THead` */
THead = define.widget( 'THead', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.table = p;
		this.contentTable = new ContentTable( x.table, this );
		p.attr( 'scroll' ) && $.classAdd( this, 'f-oh' );
		this.className = 'w-thead';
	},
	Extend: Vert,
	Prototype: {
		ROOT_TYPE: 'Table,Form',
		x_nodes: $.rt(),
		// 表头固定在外部滚动面板的上方
		fixOnTop: function() {
			var a = Scroll.get( this.rootNode ), b, f;
			a && a.addEvent( 'scroll', function() {
				var e = this.$(),
					c = b || (b = $.after( e, '<div style=height:' + e.offsetHeight + ';display:none>&nbsp;</div>' )),
					m = $.bcr( a.$( 'ovf' ) ),
					n = $.bcr( this.rootNode.$() ),
					f = n.top < m.top;
				e.style.position = f ? 'fixed' : '';
				e.style.zIndex = f ? 2 : '';
				c.style.display = f ? 'block' : 'none';
				$.classAdd( e, 'f-oh f-white f-shadow-bottom', f );
				e.scrollLeft = f ? a.scrollLeft() : 0;
			}, this );
		}
	}
} ),
/* `TFoot` */
TFoot = define.widget( 'TFoot', {
	Const: function( x, p ) {
		THead.apply( this, arguments );
		this.className = 'w-tfoot';
	},
	Extend: THead,
	Prototype: {
		html: function() {
			return W.prototype.html.call( this ) + '<div id=' + this.id +'pad style=display:none></div>';
		}
	}
	
} ),
/* `TBody` */
TBody = define.widget( 'TBody', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.table = p;
		this.contentTable = new ContentTable( x.table, this );
		!p.rootNode && !p.attr( 'scroll' ) && (this.className += ' w-' + this.rootNode.type.toLowerCase() + '-bg');
	},
	Extend: THead,
	Listener: {
		body: {
			scroll: function( e ) {
				_superTrigger( this, Scroll, e );
				this.rootNode.trigger( 'scroll' );
			},
			mouseOver: {
				method: function( e ) {
					this.table != this.rootNode ? this.rootNode.body.trigger( e ) : this.table.head && _superTrigger( this, Scroll, e );
				}
			},
			mouseOut: {
				method: function( e ) {
					this.table != this.rootNode ? this.rootNode.body.trigger( e ) : this.table.head && _superTrigger( this, Scroll, e );
				}
			},
			mouseWheel: {
				occupy: function() { return this.table !== this.rootNode },
				method: function( e ) {
					var r = this.rootNode;
					if ( r != this.table ) {
						var b = r.isScrollable() ? r : r.body.isScrollable() ? r.body : N;
						b && b.scrollTop( b.scrollTop() + (e.wheelDelta > 0 ? -20 : 20) );
					}
				}
			}			
		}
	},
	Prototype: {
		className: 'w-tbody',
		html_nodes: function() {
			return this.contentTable.html() + (this.table.foot ? this.table.foot.html() : '');
		}
	}
} ),
/* `AbsTable` */
AbsTable = define.widget( 'AbsTable', {
	Extend: Vert,
	Prototype: {
		x_childtype: $.rt( 'TR' ),
		initBody: function( x ) {
			var r = x.tHead && x.tHead.nodes, s = this.attr( 'scroll' );
			if ( r && r.length ) {
				this.head = new THead( $.extend( { table: { tHead: x.tHead, columns: x.columns } }, x.tHead, { width: '*' } ), this );
				delete x.scroll;
			}
			r = x.tFoot && x.tFoot.nodes;
			if ( r && r.length ) {
				this.foot = new TFoot( $.extend( { table: { tFoot: x.tFoot, columns: x.columns } }, x.tFoot, { width: '*' } ), this, -1 );
			}
			this.body = new TBody( $.extend( { table: { tBody: x.tBody, columns: x.columns } }, x.tBody, { width: '*', height: this.head ? '*' : -1, scroll: this.head && s } ), this );
			x.limit && this.limit();
			if ( this.head && _w_lay.height.call( this ) )
				this.addEvent( 'resize', _w_mix.height ).addEvent( 'ready', _w_mix.height );
		},
		contentHead: function() {
			return this.head && this.head.contentTable.contentHead;
		},
		contentBody: function() {
			return this.body && this.body.contentTable.contentBody;
		},
		contentFoot: function() {
			return this.foot && this.foot.contentTable.contentFoot;
		},
		// 获取符合条件的某一行  /@ a -> condition?
		row: function( a ) {
			return this.rows( a == N ? 0 : a, T )[ 0 ];
		},
		// 获取符合条件的所有行  /@ a -> condition?, b -> one?
		rows: function( a, b ) {
			var d = this.contentBody(), r = [];
			if ( d ) {
				if ( a == N ) {
					r = _slice.call( d );
				} else if ( $.isNumber( a ) ) {
					a = parseFloat( a );
					if ( a < 0 ) a = d.length + a;
					d[ a ] && r.push( d[ a ] );
				} else if ( a.isWidget ) {
					var g = a.closest( 'TR' );
					g && r.push( g );
				} else if ( a.nodeType === 1 ) {
					var g = _widget( a );
					g && (g = g.closest( 'TR' )) && r.push( g );
				} else
					try {
						var r = this._rows( a, b, d );
					} catch( e ) {
						r.push( e );
					}
			}
			return r;
		},
		_rows: function( a, b, d ) {
			R: for ( var i = 0, k, l = d.length, c, e = typeof a === _FUN, r = []; i < l; i ++ ) {
				if ( e ) {
					if ( ! a.call( d[ i ], d[ i ].x.data ) ) {
						d[ i ].length && r.push.apply( r, this._rows( a, b, d[ i ] ) );
						continue R;
					}
				} else {
					for ( k in a ) {
						if ( d[ i ].x.data[ k ] !== a[ k ] ) {
							d[ i ].length && r.push.apply( r, this._rows( a, b, d[ i ] ) );
							continue R;
						}
					}
				}
				if ( b ) { throw d[ i ]; }
				r.push( d[ i ] );
			}
			return r;
		},
		// 获取显示中的tbody的所有行
		getEchoRows: function() {
			return this._echo_rows || this.contentBody() || [];
		},
		// 获取符合条件的所有行的 data json  /@ a -> condition?, b -> one?
		rowsData: function( a, b ) {
			for ( var i = 0, d = this.rows( a, b ), l = d.length, r = []; i < l; i ++ )
				r.push( d[ i ].getData() );
			return r;
		},
		// a -> data, b -> index
		_addRow: function( a, b ) {
			var p = this.contentBody();
			p._rowSpan = {};
			b == N && (b = p.length);
			p[ b ] ? p[ b ].before( a ) : p.append( a );
		},
		prepend: function( a ) {
			this._addRow( a, 0 );
		},
		append: function( a ) {
			this._addRow( a );
		},
		// 获取平行的table /@a -> get root row?
		getParallel: function( a ) {
			var r = this.rootNode || this;
			return a ? r : [ r ].concat( r.fixed );
		},
		// table和平行table都执行同一方法 /a -> method name, b -> args
		parallel: function( a, b ) {
			for ( var i = 0, c = this.getParallel(); i < c.length; i ++ )
				c[ i ][ a ].apply( c[ i ], b || A );
		},
		// @a -> elem
		getCellAxis: function( a ) {
			var b = this.contentBody(), c = Q( a ).closest( 'TD' ), r = c[ 0 ] && _widget( c[ 0 ] ).closest( 'TR' );
			if ( b && r && r.$().parentNode === b.$() ) {
				return { rowIndex: r.$().rowIndex - 1, cellIndex: c[ 0 ].cellIndex };
			}
		},
		getColGroup: function( i ) {
			return this.body.contentTable.colgroup;
		},
		// @a -> col index, b -> width?
		colWidth: function( a, b ) {
			var g = [ this.getColGroup() ];
			this.head && g.push( this.head.contentTable.colgroup );
			if ( b == N )
				return g[ 0 ] && g[ 0 ][ a ] && g[ 0 ][ a ].width();
			for ( var i = 0; i < g.length; i ++ ) {
				for ( var j = 0; j < g[ i ].length; j ++ ) {
					g[ i ][ j ].x.width = g[ i ][ j ].innerWidth();
				}
				g[ i ].isScaleCover = F;
			}
			for ( var i = 0; i < g.length; i ++ ) {
				g[ i ][ a ] && g[ i ][ a ].width( b );
			}
			_w_rsz_all.call( this );
			for ( var i = 0, w = 0, v, l = g[ 0 ].length; i < l; i ++ ) {
				w += g[ 0 ][ i ].width();
			}
			for ( var i = 0; i < g.length; i ++ ) {
				! isNaN( w ) && (g[ i ].parentNode.$().style.width = w + 'px');
			}
		},
		// a -> data, b -> data|index
		insertRow: function( a, b ) {
			(b != N && (b = this.row( b ))) ? b.before( a ) : this.append( a );
		},
		// a -> data, b -> data|index
		updateRow: function( a, b ) {
			b != N && (b = this.row( b )) && b.replace( a );
		},
		// a -> data|index
		deleteRow: function( a ) {
			if ( a != N && (a = this.rows( a )) ) {
				var i = a.length;
				while ( i -- ) a[ i ].remove();
			}
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
				b[ i ].check( a );
				if ( b[ i ].length )
					this.checkAllRows( a, b[ i ] );
			}
		},
		// a -> data|index
		focusRow: function( a ) {
			(a = this.row( a )) && (a.focus(), _scrollIntoView( a ));
		},
		_col_parse: function( a ) {
			if ( a != N && isNaN( a ) ) {
				for ( var j = 0, g = this.getColGroup(); j < g.length; j ++ )
					if ( g[ j ].x.field === a ) { a = j; break; }
			}
			return a;
		},
		//@public 插入一列 / @a -> 一列的数据table json, b -> colIndex|colField
		insertColumn: function( a, b ) {
			b = this._col_parse( b );
			var g = this.getColGroup();
			g.insertCol( a.columns[ 0 ], b );
			if ( g = this.head && this.head.contentTable.colgroup )
				g.insertCol( a.columns[ 0 ], b );
			(g = this.contentHead()) && g.insertCol( a.tHead.nodes, b );
			(g = this.contentBody()) && g.insertCol( a.tBody.nodes, b );
			(g = this.contentFoot()) && g.insertCol( a.tFoot.nodes, b );
			this.resize();
			this.attr( 'scroll' ) && (this.head ? this.body : this).checkScroll();
		},
		//@public 删除一列 / @a -> colIndex|colField
		deleteColumn: function( a ) {
			a = this._col_parse( a );
			var g = this.getColGroup();
			g.deleteCol( a );
			if ( g = this.head && this.head.contentTable.colgroup )
				g.deleteCol( a );
			(g = this.contentHead()) && g.deleteCol( a );
			(g = this.contentBody()) && g.deleteCol( a );
			(g = this.contentFoot()) && g.deleteCol( a );
			this.resize();
			this.attr( 'scroll' ) && (this.head ? this.body : this).checkScroll();
		},
		//@public 更新一列 / @a -> 一列的数据table json, b -> colIndex|colField
		updateColumn: function( a, b ) {
			b = this._col_parse( b );
			var g = this.getColGroup();
			if ( g[ b ] ) {
				this.deleteColumn( b );
				this.insertColumn( a, b );
			}
		},
		// 获取焦点行 / @a -> visible(是否可见)?
		getFocus: function( a ) {
			return this.getFocusAll( a )[ 0 ];
		},
		// 获取所有焦点行 / @a -> visible?
		getFocusAll: function( a ) {
			for ( var i = 0, b = this.contentBody(), c, l = b.length, r = []; i < l; i ++ ) {
				(c = b[ i ].getFocusAll( a )) && c.length && (r = r.concat( c ));
			}
			return r;
		},
		// 获取所有选中行
		getCheckedAll: function() {
			for ( var i = 0, b = this.rows(), l = b.length, r = []; i < l; i ++ )
				b[ i ].isBoxChecked() && r.push( b[ i ] );
			return r;
		},
		fixRowCls: function() {
			if ( this.contentBody() ) {
				for ( var i = 0, r = this.getEchoRows(), l = r.length; i < l; i ++ ) {
					$.classRemove( r[ i ].$(), 'z-0 z-1 z-first z-last' );
					$.classAdd( r[ i ].$(), 'z-' + (i % 2) );
					i === 0 && $.classAdd( r[ i ].$(), 'z-first' );
					i === l - 1 && $.classAdd( r[ i ].$(), 'z-last' );
					r[ i ].rownum && r[ i ].rownum.reset();
				}
			}
			this.addClass( 'z-empty', ! this.getEchoRows().length );
		},
		// 高亮某个字段的关键字 /@ a -> field name, b -> key, c -> matchlength, d -> keyCls
		highlight: function( a, b, c, d ) {
			for ( var k = 0, e = this.getColGroup(), l = e.length; k < l; k ++ ) {
				if ( e[ k ].x.field == a ) break;
			}
			if ( k < l ) {
				for ( var i = 0, r = this.getEchoRows(), l = r.length; i < l; i ++ ) {
					r[ i ].highlight( k, b, c, d );
				}
			}
		},
		getPageByTarget: function() {
			var b = Q( '.w-page', this.ownerView.$() );
			for ( var i = 0, c; i < b.length; i ++ )
				if ( (c = _widget(b[ i ])) && c.x.target === this.x.id )
					return c;
		},
		// @a -> tr|pageIndex
		page: function( a ) {
			var n = a;
			if ( a && a.type_tr ) {
				n = a.getPageIndex();
				var g = this.getPageByTarget();
				if ( g )
					return g.go( n );
			}
			if ( a != N ) {
				this.x.page = n;
				this.limit();
				this.$() && this.render();
			}
			return { currentPage: this.x.page, sumPage: Math.ceil((this._filter_rows || this.contentBody() || []).length / this.x.limit) };
		},
		limit: function() {
			if ( this.x.limit && this.contentBody() ) {
				var g = this.x.page || 1, i = (g - 1) * this.x.limit, j = g * this.x.limit;
				this._echo_rows = _slice.call( this._filter_rows || this.contentBody(), i, j );
			}
		},
		// @f -> filter rows(经过筛选的行)
		setFilter: function( f ) {
			this._filter_rows = this._echo_rows = f;
			this.x.page = 1;
			this.x.limit && this.limit();
		},
		// @a -> condition?
		filter: function( a ) {
			this.setFilter( this.rows( a ) );
			this.body.render();
		},
		// a -> column
		_sortRow: function( a ) {
			var b = a._sort, c = a.x.sort, d = c.field || a.x.field, r = $.arrMake( this.getEchoRows() );
			r.sort( function( m, n ) {
				var e = m.x.data[ d ], f = n.x.data[ d ];
				if ( c.number ) { e = _number( e ); f = _number( f ); }
				return b === 'asc' ? (e < f ? -1 : e == f ? 0 : 1) : (e < f ? 1 : e == f ? 0 : -1);
			} );
			for ( var i = 0, l = r.length, o = this.contentBody().$().rows; i < l; i ++ ) {
				if ( o[ i ].id != r[ i ].id ) {
					r[ i ].parentNode.addNode( r[ i ], i );
					Q( o[ i ] ).before( r[ i ].$() );
				}
			}
			Q( '.w-th-sort', this.head.$() ).removeClass( 'z-desc z-asc' );
			for ( var k = 0, e = this.getColGroup(), l = e.length; k < l; k ++ ) {
				if ( e[ k ].x.field == a ) {
					Q( e[ k ].th() ).addClass( 'z-' + b );
					break;
				}
			}
			this.fixRowCls();
		},
		//排序 /@a -> field, b -> [asc,desc]
		order: function( a, b ) {
			for ( var k = 0, e = this.getColGroup(), l = e.length; k < l; k ++ ) {
				if ( e[ k ].x.field == a ) break;
			}
			if ( k < l ) {
				var c = e[ k ].x.sort, d = c.field || a, r = $.arrMake( this.getEchoRows() );
				if ( b == N ) {
					b = e[ k ]._sort === 'desc' ? 'asc' : 'desc';
				}
				if ( c.src ) {
					var s = c.src;
					if ( s.indexOf( 'javascript:' ) == 0 )
						s = e[ k ].formatJS( s, { '$0': b, '$1': e[ k ].x.field } );
					if ( s && typeof s === _STR && ! e[ k ]._sorting ) {
						e[ k ]._sorting = T;
						e[ k ].cmd( { type: 'Ajax', src: $.urlFormat( s, [ b, e[ k ].x.field ] ), complete: function() { e[ k ]._sorting = F; Q( '.f-arw', o ).show(); Q( '.f-i-loading', o ).remove(); } }, b );
					}
				} else {
					e[ k ]._sort = b;
					this._sortRow( e[ k ] );
				}
			}
		},
		// @k -> keycode
		keyUp: function( k ) {
			if ( ! this.contentBody() )
				return;
			var r = this.getEchoRows(), d = k === 40, a;
			if ( d || k === 38 ) { // key down/up
				var a = this.getFocus( T ), b = a ? (d ? a.next() : a.prev()) : r[ d ? 0 : r.length - 1 ];
				b ? b.focus() : (a && a.focus( F ));
			} else if ( k === 13 ) {
				(a = this.getFocus( T )) && a.click();
			} else if ( k === 37 || k === 39 ) {
				this.pageBar && this.pageBar.keyJump( k );
			}
		},
		// @implement
		prop_cls_scroll_overflow: function() {
			return 'f-scroll-overflow' + (!this.head && this.attr( 'scroll' ) ? ' w-' + this.type + '-bg' : '');
		},
		fixScroll: function() {
			if ( this.head ) {
				this.head.$().style.overflow = 'hidden';
				this.head.$().scrollLeft = this.scrollLeft();
			}
			if ( this.foot && this.foot.holdBottom ) {
				this.foot.$().style.overflow = 'hidden';
				this.foot.$().scrollLeft = this.scrollLeft();
			}
		},
		fixFoot: function() {
			if ( this.foot ) {
				var f = this.foot, r = this.rootNode || this,
					b = r.body.isScrollable() ? r.body : r.isScrollable() ? r : N;
				if ( b ) {
					var h = b.scrollHeight();
					f.holdBottom = !!h;
					f.css( 'pad', { height: h ? f.$().offsetHeight -1 : '', display: h ? 'block' : 'none', overflow: h ? 'hidden' : 'visible' } );
					f.addClass( 'z-cover', !!h );
					h ? $.append( b === r ? this.$() : this.body.$(), f.$() ) : $.before( f.$( 'pad' ), f.$() );
				}
			}
		}		
	}
} );
_parallel_methods( AbsTable, '_sortRow fixScroll' );
var
/* `table` */
Table = define.widget( 'Table', {
	Const: function( x, p ) {
		this._pad = x.cellPadding != N ? x.cellPadding : 5;
		W.apply( this, arguments );
		x.scroll == N && (x.scroll = T);
		this._face = x.face || 'none';
		this.initBody( x );
		if ( x.hiddens )
			this._hiddens = new Hiddens( { type: 'hiddens', nodes: x.hiddens }, this, -1 );
		this.fixed = [];
		if ( ! ie7 ) { //@fixme: 暂不支持ie7
			var xl = [], xr = [];
			for ( var i = 0, f, c = x.columns, l = c ? c.length : 0; i < l; i ++ ) {
				(f = c[ i ].fixed) && (f === 'left' ? xl : xr).push( $.extend( { fixed: N, fixedIndex: i }, c[ i ] ) );
			}
			if ( xl.length ) {
				this.left = new LeftTable( { columns: xl, tHead: x.tHead, tBody: x.tBody, tFoot: x.tFoot }, this, -1 );
				this.fixed.push( this.left );
			}
			if ( xr.length ) {
				this.right = new RightTable( { columns: xr, tHead: x.tHead, tBody: x.tBody, tFoot: x.tFoot }, this, -1 );
				this.fixed.push( this.right );
			}
		}
		x.width === -1 && $.classAdd( this, 'z-auto' );
		$.classAdd( this, this.head ? 'z-head' : 'z-nohead' );
		!this.getEchoRows().length && $.classAdd( this, 'z-empty' );
	},
	Extend: AbsTable,
	Listener: {
		body: {
			ready: function() {
				var a = this.contentBody();
				if ( a ) {
					var b = a.$().rows, c = this.getFocusAll();
					b.length && ($.classAdd( b[ 0 ], 'z-first' ), $.classAdd( b[ b.length - 1 ], 'z-last' ));
					if ( c.length ) {
						_scrollIntoView( c[ 0 ] );
						for ( var i = 0, d; i < c.length; i ++ ) {
							if ( (d = c[ i ].getBox()) && d.x.sync === 'focus' ) c[ i ].check();
						}
					}
				}
				this.fixFoot();
			},
			scroll: function( e ) {
				_superTrigger( this, Vert, e );
				this.fixScroll();
			},
			resize: function( e ) {
				_superTrigger( this, Vert, e );
				for ( var i = 0; i < this.fixed.length; i ++ )
					this.fixed[ i ].trigger( 'resize' );
				this.$() && $.classAdd( this.$(), 'z-auto', this.innerWidth() == N );
				this.fixFoot();
			}
		}
	},
	Prototype: {
		className: 'w-table',
		scrollTop: function() {
			return this.head ? this.body.scrollTop.apply( this.body, arguments ) : AbsTable.prototype.scrollTop.apply( this, arguments );
		},
		scrollLeft: function() {
			return this.head ? this.body.scrollLeft.apply( this.body, arguments ) : AbsTable.prototype.scrollLeft.apply( this, arguments );
		},
		isScrollBottom: function() {
			return this.head ? this.body.isScrollBottom() : AbsTable.prototype.isScrollBottom.call( this );
		},
		isScrollRight: function() {
			return this.head ? this.body.isScrollRight() : AbsTable.prototype.isScrollRight.call( this );
		},
		html_append: function() {
			for ( var i = 0, s = ''; i < this.fixed.length; i ++ )
				s += this.fixed[ i ].html();
			return _proto.html_append.apply( this, arguments ) + s;
		}
	}
}),
/* `LeftTable`  table的column设置了fixed时，会产生分身table(LeftTable, RightTable)。*/
LeftTable = define.widget( 'LeftTable', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this._pad  = p._pad;
		this._face = p._face;
		this.initBody( x );
	},
	Extend: AbsTable,
	Default: { width: -1 },
	Listener: {
		body: {
			ready: function() {
				var p = this.parentNode;
				if ( p.head ) {
					for ( var i = 0, h = this.contentHead(), ph = p.contentHead(); i < h.length; i ++ )
						h[ i ].height( ph[ i ].height() );
				}
				this.fixSize();
				this.fixFoot();
			},
			resize: function( e ) {
				_superTrigger( this, Vert, e );
				this.fixSize();
				this.fixFoot();
			}
		}
	},
	Prototype: {
		className: 'w-lefttable w-fixedtable',
		ROOT_TYPE: 'Table,Form',
		fixSize: function() {
			this.body.height( (this.rootNode.head ? this.rootNode.body : this.rootNode).innerHeight() );
		},
		fixScroll: function() {
			this.body.$().scrollTop = this.rootNode.scrollTop();
			this.addClass( 'z-cover', this.rootNode.scrollLeft() > 0 );
		}
	}
} ),
/* `RightTable`  table的column设置了fixed时，会产生分身table(LeftTable, RightTable)。*/
RightTable = define.widget( 'RightTable', {
	Extend: LeftTable,
	Prototype: {
		className: 'w-righttable w-fixedtable',
		fixScroll: function() {
			this.body.$().scrollTop = this.rootNode.scrollTop();
			this.addClass( 'z-cover', !this.rootNode.isScrollRight() );
		}
	}
} ),
/* `form`  nodes内部可以是任何Widget。如果有colSpan、rowSpan需求，需要在Widget外套一层TD。 */
Form = define.widget( 'Form', {
	Const: function( x, p ) {
		var c = [], d = this.getDefaultOption( x.cls ), rows = [], cols = x.cols || 12;
		for ( var i = 0; i < cols; i ++ ) {
			c.push( { field: '' + i, width: '*' } );
		}
		for ( var i = 0, j = 0, n = x.nodes ? x.nodes.concat() : [], tr = {}, rp = {}; i < n.length; i ++ ) {
			j == 0 && rows.push( tr );
			var e = n[ i ], h = rows.length - 1,
				td = typeof e === _STR ? { text: e } : e.type && e.type !== 'TD' ? { node: e } : e;
			$.extend( td, x.pub, d && d.pub );
			!td.colSpan && (td.colSpan = -1);
			td.colSpan < 0 && (td.colSpan = cols + 1 + td.colSpan);
			(td.colSpan > cols) && (td.colSpan = cols);
			if ( td.rowSpan ) {
				for ( var k = 0; k < td.rowSpan - 1; k ++ ) {
					(rp[ h + k + 1 ] || (rp[ h + k + 1 ] = {}))[ j ] = Math.min( cols, td.colSpan );
				}
			}
			if ( rp[ h ] ) {
				for ( var k = j, l = Math.min( cols, j + td.colSpan ); k < l; k ++ ) {
					if ( rp[ h ][ k ] ) {
						j = k + rp[ h ][ k ];
						k = j;
					}
				}
			}
			if ( j + td.colSpan > cols ) {
				(tr = {})[ 0 ] = td;
				j = td.colSpan;
				rows.push( tr );
			} else {
				tr[ j ] = td;
				j += td.colSpan;
				if ( j >= cols ) {
					j = 0;
					tr = {};
				}
			}
		}
		var y = $.extend( {}, x, { columns: c, tBody: { nodes: rows } } );
		delete y.pub; delete y.nodes;
		Table.call( this, y, p );
	},
	Extend: Table,
	Prototype: {
		className: 'w-form'
	}
} );

// 扩展全局方法
$.scrollIntoView = _scrollIntoView;
$.dialog = Dialog.get;
$.widget = $.w = _widget;
$.vm = _view;
$.e = _widgetEvent;

// 附件上传模块
require( './upload/upload' );

// 本文件的导出
module.exports = W;

