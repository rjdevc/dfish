/* amap
 * 高德地图
 *--------------------------------------------------------------------------------------------------------------------
 * 在首页加上标签：<script src="https://webapi.amap.com/maps?v=1.3&key=248c8c0a0e198cc298fc48920cfd967f"></script>
 *--------------------------------------------------------------------------------------------------------------------
 * 在首页dfish全局参数上设置:
 *  alias: {
 *    'amap': 'pl/amap/amap.dfish.js'
 *  }
 *--------------------------------------------------------------------------------------------------------------------
 *  pickbox 弹出选择框的 view 范例:
 *
	{
	  "type": "view",
	  "cls": "f-bg-white",
	  "node": {
	    "type":"vert",
	    "nodes": [
	      { "type": "amap/picker", "id": "amap", "name": "amap", "height": "*", "value": "" },
	      { "type": "buttonbar", "height": 50, "space": 10, "align": "right", "valign": "middle", "style": "background:#f7f7f7;padding-right:30px", "nodes":[
	        { "type": "submitbutton", "text": "  确定  ", "cls": "f-button", "on": { "click": "$.dialog(this).commander.val(VM(this).fv('amap'),VM(this).f('amap').text());$.close(this)" } },
	      	{ "type": "button", "text": "  取消  ", "cls": "f-button", "on": { "click": "$.close(this)" } } ]
	      }
	    ]
	  }
	}
 *
 *--------------------------------------------------------------------------------------------------------------------
 */

var
$ = require( 'dfish' ),
W = require( 'widget' ),

/*
 * { "type": "amap", "id": "amap", "name": "amap", "height": "*", "value": "{\"address\":[item]}" }
 * value参数的item格式：{"lng":"118.868856","lat":"25.421089","icon":"图标地址","tip":"<div>福建省莆田市城厢区华亭镇院里 <a href=#>BBB</a></div>","focus":true}
 */
amap = define.widget( 'amap', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		this.markers = [];
		this.errorCount = 0;
		this.cssText = 'z-index:0;';
	},
	Extend: 'vert',
	Listener: {
		body: {
			ready: function() { this.render(); }
		}
	},
	Prototype: {
		className: 'w-amap',
		val: function() {
			return this.x.value;
		},
		render: function() {
			if (!window.AMap) {
				var s = document.createElement('script'), o = !$.br.css3, self = this;
				s.type = 'text/javascript';
				s.src = 'https://webapi.amap.com/maps?v=1.3&key=248c8c0a0e198cc298fc48920cfd967f';
				s[o ? 'onreadystatechange' : 'onload'] = function() {
					if (!o || (s.readyState == 'loaded' || s.readyState == 'complete'))
						self._render();
				}
				document.getElementsByTagName('head')[0].appendChild(s);
			} else
				this._render();
		},
		_render: function() {
			var v = $.jsonParse( this.val() ), self = this, opt = {};
			if ( v && v.lng && v.lat ) {
				opt.view = new AMap.View2D( {
					center: new AMap.LngLat (v.lng, v.lat )
				} );
			}
			var map = new AMap.Map(this.id, opt);
			if ( this.x.city )
				map.setCity( this.x.city );
			if ( this.x.zoom )
				map.setZoom( this.x.zoom );
			//地图中添加地图操作ToolBar插件
			map.plugin(['AMap.ToolBar'], function(){
				//设置地位标记为自定义标记
				var toolBar = new AMap.ToolBar();
				map.addControl(toolBar);		
			});
			this.map = map;
			if ( v && v.address )
				this.setMarker( v.address );
		},
		setMarker: function( addr ) {
			for ( var i = 0, d; i < addr.length; i ++ ) {
				d = addr[ i ];
				this.addMarker( d );
			}
			this.map.setFitView();
		},
		focus: function( m ) {
			this.map.panTo( m.getPosition() );
			AMap.event.trigger( m, 'mouseover' );
		},
		markByAddress: function( addr, city, i ) {
			var self = this,
				i = i == null ? 0 : i,
				f = function() {
			        var MGeocoder = new AMap.Geocoder({
			            city: city, //城市，默认：“全国”
			            radius: 1000 //范围，默认：500
			        });
			        //返回地理编码结果
			        //地理编码
			        MGeocoder.getLocation(addr[ i ], function(status, result) {
			            if ( status === 'complete' && result.info === 'OK' ) {
				            var d = result.geocodes[ 0 ];
				        	self.addMarker( { lng: d.location.lng, lat: d.location.lat, tip: d.formattedAddress } );
				        } else if ( status === 'no_data' ) {
				        	self.markers.push( { error: true, address: addr[ i ] } );
				        	self.errorCount ++;
				        }
				        i ++;
				        if ( i < addr.length ) {
				        	self.markByAddress( addr, city, i );
				        } else {
				        	// 只有一个marker时，setFitView() 会报错，以下修复此问题
				        	var n = self.markers.length - self.errorCount;
				        	if ( n > 1 ) {
				        		self.map.setFitView();
				        	} else if ( n === 1 ) {
				        		for ( var j = 0; j < self.markers.length; j ++ ) {
				        			if ( ! self.markers[ j ].error ) {
				        				self.map.setZoomAndCenter( 16, self.markers[ j ].getPosition() );
				        				break;
				        			}
				        		}
				        	}
				        }
				        if ( self.setValue && self.markers.length ) {
				        	! self.markers[ 0 ].error && self.setValue( self.markers[ 0 ].getPosition() );
				        }
			        });
				};
			AMap.Geocoder ? f() : AMap.service( ["AMap.Geocoder"], f );
		},
		addMarker: function( x ) {
		    var m = new AMap.Marker({
			    map: this.map,
			    //icon: 'http://webapi.amap.com/images/' + ( n == null ? 0 : n + 1 ) + '.png',
			    icon: x.icon || 'http://webapi.amap.com/images/marker_sprite.png',
			    position: new AMap.LngLat( x.lng, x.lat )
			});
			m.infoWindow = new AMap.InfoWindow({  
			    content: x.tip || x.address, 
			    autoMove:true, 
			    size:new AMap.Size(260, 0),  
			    offset: { x: 0, y: -30 }
			});
		   	m.on( 'mouseover', function( e ) { this.infoWindow.open( this.getMap(), this.getPosition() ) } );
		   	this.markers.push( m );
		   	x.focus && this.focus( m );
		   	return m;
		}
	}
} ),

/*
 * @city: 城市名(如：福州)
 * @zoom: 缩放级别，默认取值范围为[3,18]
 * @value: {"lng":"118.868856","lat":"25.421089","icon":"图标地址","tip":"提示内容","focus":true}
 */
AMapPicker = define.widget( 'amap/picker', {
	Const: function( x, p ) {
		amap.apply( this, arguments );
		var v = $.jsonParse( this.x.value ), c = this.add( { type: 'vert', height: '*' } ), self = this;
		this.hd_lng = c.add( { type: 'hidden', value: v ? v.lng : '' } );
		this.hd_lat = c.add( { type: 'hidden', value: v ? v.lat : '' } );
		this.cn_map = c.add( { type: 'html', height: '*' } );
		var d = c.add( { type: 'horz', height: 50, style: 'padding:10px', nodes: [ { type: 'html', text: '地点: &nbsp; ', width: 50, style: 'text-align:right;line-height:30px;' } ] } );
		this.cn_adr = d.add( { type: 'text', cls: 'z-clear', width: '*', on: { keydown: function( e ) { (! e.keyCode || e.keyCode===13)&&self.markByAddress([this.val()]); } },
			prependcontent: '<div style="float:right;width:28px;height:100%;text-align:center;cursor:pointer;left:auto;padding:0;right:0;" onclick="' + $.abbr + '.widget(this).trigger(\'keydown\')"><i class=f-vi></i><i class="f-i f-i-search"></i><input type=hidden id=' + this.id + 'v name="' + (x.name || '') + '"></div>' } );
	},
	Extend: amap,
	Prototype: {
		val: function() {
			this.save();
			return this._val();
		},
		_val: function() {
			return this.text() ? $.jsonString( { lng: this.hd_lng.val(), lat: this.hd_lat.val(), address: this.text() } ) : '';
		},
		text: function() {
			return this.cn_adr.val();
		},
		_render: function() {
			var v = $.jsonParse( this.x.value ), opt = {}, self = this;
			if ( v && v.lng && v.lat ) {
				opt.view = new AMap.View2D( {
					center: new AMap.LngLat(v.lng, v.lat),
					zoom: this.x.zoom
				} );
			}
			var map = new AMap.Map(this.cn_map.id, opt);
			if ( this.x.city )
				map.setCity( this.x.city );
			if ( this.x.zoom )
				map.setZoom( this.x.zoom );
			//地图中添加地图操作ToolBar插件
			map.plugin(['AMap.ToolBar'],function(){
				//设置地位标记为自定义标记
				var toolBar = new AMap.ToolBar();
				map.addControl(toolBar);		
			});
			this.map = map;
			AMap.event.addListener( this.map, 'click', function( e ) {
				var lng = e.lnglat.getLng(), lat = e.lnglat.getLat();
				self.hd_lng.value = lng;
				self.hd_lat.value = lat;
				self.setMarker( e.lnglat );
			} );
			if ( v ) {
				if ( v.lng && v.lat )
					this.setMarker( new AMap.LngLat(v.lng, v.lat) );
				else if ( v.address ) {
					self.cn_adr.val( v.address );
					self.cn_adr.trigger( 'keydown' );
				}
			}
		},
		setMarker: function( lnglatXY ) {
			var MGeocoder, self = this;
			this.lnglat = lnglatXY;
		    //加载地理编码插件
		    AMap.service(["AMap.Geocoder"], function() {        
		        MGeocoder = new AMap.Geocoder({ 
		            radius: 1000,
		            extensions: "all"
		        });
		        //逆地理编码
		        MGeocoder.getAddress(lnglatXY, function(status, result){
		        	if(status === 'complete' && result.info === 'OK' && lnglatXY === self.lnglat) {
	        			var m = self.addMarker( { lng: lnglatXY.lng, lat: lnglatXY.lat, tip: result.regeocode.formattedAddress } );
		        	}
		        });
		    });
		},
		addMarker: function( x ) {
		    this.cn_adr.val( x.tip );
		    this.hd_lng.val( x.lng );
		    this.hd_lat.val( x.lat );
		    var m = this.markers[ 0 ], n = new AMap.LngLat( x.lng, x.lat );
		    if ( m ) {
				 m.setPosition( n );
				 m.infoWindow.setContent( x.tip || x.address );
				 m.infoWindow.open( this.map, n );
		    } else
		    	m = amap.prototype.addMarker.call( this, x );
			AMap.event.trigger( m, 'mouseover' );
			return m;
		},
		// 执行submitCommand时会调此方法
		save : function() {
			$( this.id + 'v' ).value = this._val();
		}
	}
} );
