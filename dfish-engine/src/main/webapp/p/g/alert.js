define.preload({
    type: 'dialog', node: {
        type: 'view',
        commands: {
            'close': {type: 'js', text: '$.close(this);'},
            'no': {
                type: 'js',
                text: 'this.cmd("close");'
            }
        },
        node: {
            type: 'vert', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                {
                    type: 'horz', id: 'dlg_head', cls: 'dlg-head', height: 40, width: '*', valign: 'middle', nodes: [
                        {
                            type: 'dialog/title',
                            id: 'dlg_head_title',
                            cls: 'dlg-head-title',
                            width: '*',
                            wmin: 10
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
                                    tip: '最大化',
                                    cls: 'x-dlg-max',
                                    on: {click: 'app.dialog.max(this);'},
                                    icon: '.i-dlg-max'
                                }, {
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
                    type: 'vert', id: 'dlg_trunk', cls: 'dlg-trunk', height: '*', width: '*', nodes: [
                        {
                            type: 'vert', height: '*', nodes: [
                                {type: 'preload/body', id: 'dlg_body', cls: 'dlg-body', height: '*'}
                            ]
                        }
                    ]
                }
            ]
        }
    }
});