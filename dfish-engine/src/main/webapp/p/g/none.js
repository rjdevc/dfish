/**
 * 纯内容框(无标题,按钮栏等)
 * @author lamontYu
 */
define.preload({
    type: 'Dialog', node: {
        type: 'View',
        node: {
            type: 'Vertical', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                {
                    type: 'Vertical', id: 'dlg_trunk', cls: 'dlg-trunk', height: '*', width: '*', nodes: [
                        {
                            type: 'Vertical', height: '*', width: '*', nodes: [
                                {type: 'PreloadBody', id: 'dlg_body', cls: 'dlg-body', height: '*', width: '*'}
                            ]
                        }
                    ]
                }
            ]
        }
    }
});