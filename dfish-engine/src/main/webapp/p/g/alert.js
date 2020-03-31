define.preload({
    type: 'Dialog', node: {
        type: 'View',
        commands: {
            'close': {type: 'JS', text: '$.close(this);'},
            'no': {
                type: 'JS',
                text: 'this.cmd("close");'
            }
        },
        node: {
            type: 'Vertical', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                {
                    type: 'Horizontal', id: 'dlg_head', cls: 'dlg-head', height: 40, width: '*', vAlign: 'middle', nodes: [
                        {
                            type: 'DialogTitle',
                            id: 'dlg_head_title',
                            cls: 'dlg-head-title',
                            width: '*',
                            widthMinus: 10
                        },
                        {
                            type: 'ButtonBar',
                            id: 'dlg_head_operation',
                            cls: 'dlg-head-operation',
                            width: -1,
                            align: 'right',
                            vAlign: 'middle',
                            pub: {height: 40, width: 40, widthMinus: 2, heightMinus: 2},
                            nodes: [
                                {
                                    tip: '最大化',
                                    cls: 'dlg-max',
                                    on: {click: 'app.dialog.max(this);'},
                                    icon: '.i-dlg-max'
                                },
                                {
                                    tip: '关闭',
                                    cls: 'dlg-close',
                                    on: {click: 'this.cmd("close");'},
                                    icon: '.i-dlg-close'
                                }
                            ]
                        }
                    ]
                },
                {
                    type: 'Vertical', id: 'dlg_trunk', cls: 'dlg-trunk bd-main bd-notop', widthMinus: 2, heightMinus: 1, height: '*', width: '*', nodes: [
                        {
                            type: 'Vertical', height: '*', nodes: [
                                {type: 'PreloadBody', id: 'dlg_body', cls: 'dlg-body', height: '*'}
                            ]
                        }
                    ]
                }
            ]
        }
    }
});