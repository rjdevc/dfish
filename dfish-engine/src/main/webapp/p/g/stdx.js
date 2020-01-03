define.preload({
    type: 'dialog',
    node: {
        type: 'view', commands: {
            'close': {type: 'js', text: '$.close(this);'},
            'no': {type: 'js', text: 'this.cmd("close");'}
        },
        node: {
            type: 'vert', id: 'dlg_frame', height: '*', width: '*', nodes: [
                {
                    type: 'horz', id: 'dlg_head', cls: 'dlg-head', width: '*', height: 40, valign: 'middle', nodes: [
                        {
                            type: 'dialog/title',
                            id: 'dlg_head_title',
                            cls: 'dlg-head-title',
                            width: '*',
                            wmin: 10,
                            on: {dblclick: '$.dialog(this).max();'}
                        },
                        {
                            type: 'buttonbar',
                            id: 'dlg_head_oper',
                            cls: 'dlg-head-oper',
                            width: -1,
                            align: 'right',
                            valign: 'middle',
                            pub: {height: 40, width: 40},
                            nodes: [
                                {
                                    tip: '关闭',
                                    cls: 'dlg-oper-close',
                                    on: {click: 'this.cmd("close");'},
                                    icon: '.i-dlg-close'
                                }
                            ]
                        }
                    ]
                },
                {
                    type: 'vert', id: 'dlg_trunk', height: '*', nodes: [
                        {
                            type: 'vert', height: '*', nodes: [
                                {type: 'preload/body', id: 'dlg_body', height: '*'}
                            ]
                        }, {
                            type: 'horz',
                            id: 'dlg_foot',
                            cls: 'dlg-foot',
                            style: 'padding:0 20px;',
                            height: 50,
                            hmin: 1,
                            wmin: 40,
                            nodes: [
                                {
                                    type: 'html',
                                    id: 'dlg_foot_info',
                                    cls: 'dlg-foot-info',
                                    width: '*'
                                },
                                {
                                    type: 'buttonbar',
                                    id: 'dlg_foot_oper',
                                    cls: 'dlg-foot-oper',
                                    width: '*',
                                    align: 'right',
                                    space: 10,
                                    pub: {height: 30},
                                    nodes: [
                                        {
                                            text: '关闭', on: {click: 'this.cmd("no");'}
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    }
});