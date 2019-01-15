/* ueditor
 * {type: 'ueditor', option: {}}
 */

var
$ = require( 'dfish' ),
W = require( 'widget' ),
Q = require( 'jquery' ),
Loc = require( 'loc' ),
AbsForm = require( 'abs/form' ),
us = {};

define.widget( 'ueditor', {
	Const : function( x ) {
		AbsForm.apply( this, arguments );
		if ( x.transparent ) {
			this.defaults( { wmin: 0, hmin: 0 } );
			this.className += ' z-trans';
		}
		var o = x.option || {};
		this.options = $.extend( {}, o, { zIndex: 0, initialContent: x.value, readonly: o.readonly || o.disabled || ! this.isNormal() } );
		us[ this.id ] = this;
	},
	Extend: AbsForm,
	Helper: {
		isModified: function( vm ) {
			for ( var k in us ) {
				if ( ( ! vm || us[ k ].vm() === vm ) && us[ k ].isModified() )
					return true;
			}
		}
	},
	Listener: {
		tag: 'ueditor',
		body: {
			//"initialFrameHeight": 120, "maximumWords": 0
			ready: function() {
				var self = this;
				require.async( ['./ueditor.config', './ueditor.all.min'], function() {
					_patch();
					var h = self.innerHeight();
					$.extend( self.options, { initialFrameHeight: (h ? Math.max( 0, h - 31 ) : 100), toolbars: UEDITOR_CONFIG[ self.options.advance ? 'toolbars' : 'simpleToolbars' ] } );
					self._render();
				} );
			},
			valid: function( e, a ) {
				this.save();
				return AbsForm.prototype.getValidError.call( this, a );
			},
			resize: function() {
				if ( this.u ) {
					if ( this.u.isReady ) {
						this._resize();
					} else {
						var self = this;
						this.u.ready( function() { self._resize() } );
					}
				}
			}
		}
	},
	Default: { wmin: 2, hmin: 2 },
	Prototype : {
		className: 'w-ueditor w-input-border w-input f-oh f-inbl f-va',
		val: function( a ) {
			if ( a == null ) {
				this.save();
				return this.getContent();
			}
			this.setContent( a );
		},
		_render: function() {
			var u = this.u, self = this;
			if ( u ) {
				if ( u.isReady ) {
					this.u.destroy();
				} else {
					u.ready( function() {
						setTimeout( function() { self._render() }, 10 );
					} );
					return;
				}
			}
			Q( $(this.id + 'u') ).replaceWith( '<div id=' + this.id + 'u></div>' );
			this.u = u = UE.getEditor( this.id + 'u', this.options );
			u.dfishWidget = this;
			var e = this.options.on, k;
			for ( k in e ) {
				u.addListener( k, $.proxy( self, e[ k ] ) );
			}
			u.ready( function() {
				//$.br.ie && Q( self.u.document ).on( 'keydown', function( e ) { index.KEY_F5 = e.keyCode === 116; } );
				self.options.initialContent = self.getContent();
				self._resize();
			} );
			u.addListener( 'focus', function() {
				$.classRemove( self.$(), 'z-err' );
				$.cleanPop();
			} );
		},
		_resize: function() {
			if ( ! this.u.ui.isFullScreen() ) {
				var u = this.u, w = this.formWidth(), h = this.formHeight(), t = u.ui.getDom('toolbarbox'), b = u.ui.getDom('bottombar');
				w && (u.container.style.width = u.ui.getDom('iframeholder').style.width = w + 'px');
				h && (u.ui.getDom('iframeholder').style.height = Math.max( 0, h - t.offsetHeight - (b && this.options.wordCount ? b.offsetHeight : 0) ) + 'px');
			}
		},
		setAdvanceMode: function( a ) {
			this.options.initialContent = this.getContent();
			this.options.toolbars = UEDITOR_CONFIG[ a === false ? 'simpleToolbars' : 'toolbars' ];
			this.options.elementPathEnabled = a !== false;
			this.options.wordCount = a !== false && this.options.maximumWords;
			this.options.fullscreen = this.u.ui.isFullScreen();
			this._render();
		},
		getContent: function() {
			if ( this.u && this.u.isReady ) {
				this.u.queryCommandState( 'source' ) && this.u.execCommand( 'source' );
				return this.u.getContent();
			}
			return this.options.initialContent;
		},
		setContent: function( a ) {
			this.u && this.u.isReady ? this.u.setContent( a ) : (this.options.initialContent = a);
		},
		// 执行submitCommand时会调此方法
		save: function() {
			this.$( 'v' ).value = this.getContent();
		},
		isModified: function() {
			return this.u.isReady && this.options.initialContent != this.getContent();
		},
		saveModified: function() {
			this.options.initialContent = this.getContent();
		},
		beforeunload: function( e ) {
			if ( this.isModified() ) {
				return e.returnValue = '--------------------------------------\n提示：未保存的内容将会丢失。\n--------------------------------------';
			}
		},
		readonly: function( a ) {
			this.x.readonly = a !== false;
			this.u[ a === false ? 'enable' : 'disable' ]();
			$.classAdd( this.$(), 'z-ds', a );
		},
		disable: function( a ) {
			this.x.disabled = a !== false;
			this.u[ a === false ? 'enable' : 'disable' ]();
			$.classAdd( this.$(), 'z-ds', a );
			$( this.id + 'v' ).disabled = a !== false;
		},
		html_nodes: function() {
			return '<div id=' + this.id + 'u></div><input type=hidden id=' + this.id + 'v name=' + this.x.name + (this.x.disabled ? ' disabled' : '') + '>';
		},
		destroy: function() {
			delete PL.UEditor._a[ this.id ];
			try {
				this.u.isReady && this.u.destroy();
			} catch( e ) {}
			delete this.u;
		}
	}
} );

function _patch() {
	if ( window.UE && ! UE.commands[ 'advancemode' ] ) {
		UE.commands['advancemode'] = {
		    execCommand: function(){
		 		this.dfishWidget.setAdvanceMode();
		   }
		};
		UE.commands['simplemode'] = {
		    execCommand: function(){
		 		this.dfishWidget.setAdvanceMode( false );
		   }
		};
	}
}
