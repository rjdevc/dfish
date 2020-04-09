define.preload({
    type: 'Dialog', node: {
        type: 'View',
        commands: {
            // 实际关闭命令(原因是顶部关闭也需要调用)
            'close': {
                type: 'JS',
                text: 'if(VM(this).isModified()){this.cmd({type:"Confirm",text:"您有内容尚未保存，确认关闭窗口吗？",' +
                    'yes:{type:"JS",text:"$.close(this);"}});}else{$.close(this);}'
            },
            // 关闭按钮命令,这边绕一层的原因是为了能够将顶部关闭按钮和底部命令有个性化区分时使用,预先声明,未想到使用场景
            'no': {type: 'JS', text: 'this.cmd("close");'}
        },
        node: {
            type: 'Vertical', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                // 这里是标题栏
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
                            pub: {height: 40, width: 40},
                            nodes: [
                                { type: 'DialogMaxButton' },
                                {
                                	type: 'DialogCloseButton',
                                	on: {click: 'this.cmd("close")'}
                                }
                            ]
                        }
                    ]
                },
                {
                    type: 'Vertical',
                    id: 'dlg_trunk',
                    cls: 'dlg-trunk bd-main bd-notop',
                    widthMinus: 2,
                    heightMinus: 1,
                    height: '*',
                    nodes: [
                        // 主内容加载区
                        {type: 'PreloadBody', id: 'dlg_body', cls: 'dlg-body', height: '*'},
                        // 底部按钮栏
                        {
                            type: 'Horizontal',
                            id: 'dlg_foot',
                            cls: 'dlg-foot',
                            style: 'padding:0 20px;',
                            height: 50,
                            widthMinus: 40,
                            nodes: [
                                {type: 'Html', id: 'dlg_foot_info', width: '*'},
                                {
                                    type: 'ButtonBar',
                                    id: 'dlg_foot_operation',
                                    cls: 'dlg-foot-operation',
                                    width: '*',
                                    align: 'right',
                                    space: 10,
                                    pub: {height: 30},
                                    nodes: [
                                        {type: 'SubmitButton', text: '保存', on: {click: 'this.cmd("yes");'}},
                                        {text: '关闭', on: {click: 'this.cmd("no");'}}
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
