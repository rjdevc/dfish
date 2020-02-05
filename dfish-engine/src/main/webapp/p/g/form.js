define.preload({
    type: 'Dialog', node: {
        type: 'View',
        commands: {
            'close': {
                type: 'JS',
                text: 'if(VM(this).isModified()){this.cmd({type:"Confirm",text:"您有内容尚未保存，确认关闭窗口吗？",' +
                    'yes:{type:"JS",text:"$.close(this);"}});}else{$.close(this);}'
            },
            'no': {
                type: 'JS',
                text: 'this.cmd("close");'
            }
        },
        node: {
            type: 'Vertical', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                {
                    type: 'Horizontal',
                    id: 'dlg_head',
                    cls: 'dlg-head',
                    height: 40,
                    width: '*',
                    vAlign: 'middle',
                    nodes: [
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
                                }, {
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
                    type: 'Vertical', id: 'dlg_trunk', cls: 'dlg-trunk', height: '*', nodes: [
                        {
                            type: 'Vertical', height: '*', nodes: [
                                {type: 'PreloadBody', id: 'dlg_body', cls: 'dlg-body', height: '*'}
                            ]
                        },
                        {
                            type: 'Horizontal',
                            id: 'dlg_foot',
                            cls: 'dlg-foot',
                            style: 'padding:0 20px;',
                            height: 50,
                            heightMinus: 1,
                            widthMinus: 40,
                            nodes: [
                                {
                                    type: 'Html',
                                    id: 'dlg_foot_info',
                                    width: '*'
                                },
                                {
                                    type: 'ButtonBar',
                                    id: 'dlg_foot_oper',
                                    width: '*',
                                    align: 'right',
                                    space: 10,
                                    pub: {height: 30},
                                    nodes: [
                                        {
                                            type: 'SubmitButton', text: '保存', on: {click: 'this.cmd("yes");'}
                                        },
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