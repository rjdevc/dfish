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