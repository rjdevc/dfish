/**
 * ??暂时想不清
 * @author lamontYu
 */
define.preload({
    type: 'Dialog',
    node: {
        type: 'View', commands: {
            'close': {type: 'JS', text: '$.close(this);'},
            'no': {type: 'JS', text: 'this.cmd("close");'}
        },
        node: {
            type: 'Vertical', id: 'dlg_frame', height: '*', width: '*', nodes: [
                {
                    type: 'Horizontal',
                    id: 'dlg_head',
                    cls: 'dlg-head',
                    width: '*',
                    height: 40,
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
                        {
                            type: 'Vertical', height: '*', nodes: [
                                {type: 'PreloadBody', id: 'dlg_body', height: '*'}
                            ]
                        }, {
                            type: 'Horizontal',
                            id: 'dlg_foot',
                            cls: 'dlg-foot',
                            style: 'padding:0 20px;',
                            height: 50,
                            widthMinus: 40,
                            nodes: [
                                {
                                    type: 'Html',
                                    id: 'dlg_foot_info',
                                    cls: 'dlg-foot-info',
                                    width: '*'
                                },
                                {
                                    type: 'ButtonBar',
                                    id: 'dlg_foot_operation',
                                    cls: 'dlg-foot-operation',
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