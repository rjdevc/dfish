define.preload({
    type: 'Dialog',
    node: {
        type: 'View',
        commands: {
            'close': {
                type: 'JS',
                text: '$.close(this);'
            },
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
                            widthMinus: 10,
                            on: {dblClick: '$.dialog(this).max();'}
                        },
                        {
                            type: 'ButtonBar',
                            id: 'dlg_head_oper',
                            cls: 'dlg-head-oper',
                            width: -1,
                            align: 'right',
                            vAlign: 'middle',
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
                    type: 'Vertical', id: 'dlg_trunk', cls: 'dlg-trunk', width: '*', height: '*', nodes: [
                        {
                            type: 'Vertical', width: '*', height: '*', nodes: [
                                {type: 'PreloadBody', id: 'dlg_body', cls: 'dlg-body', height: '*'}
                            ]
                        },
                        {
                            type: 'Horizontal',
                            id: 'dlg_foot',
                            cls: 'dlg-foot',
                            style: 'padding:0 20px;',
                            width: '*',
                            height: 50,
                            heightMinus: 1,
                            widthMinus: 40,
                            nodes: [
                                {
                                    type: 'Html',
                                    id: 'dlg_foot_info',
                                    cls: 'dlg-foot-info',
                                    width: '*',
                                    height: '*'
                                },
                                {
                                    type: 'ButtonBar',
                                    id: 'dlg_foot_oper',
                                    cls: 'dlg-foot-oper',
                                    width: '*',
                                    height: '*',
                                    align: 'right',
                                    space: 10,
                                    pub: {height: 30},
                                    nodes: [
                                        {
                                            type: 'SubmitButton', text: '确定', on: {click: 'this.cmd("yes");'}
                                        },
                                        {
                                            text: '取消', on: {click: 'this.cmd("no");'}
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