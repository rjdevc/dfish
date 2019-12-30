define.preload({
    type: 'dialog', node: {
        type: 'view',
        node: {
            type: 'vert', id: 'dlg_frame', cls: 'dlg-frame', height: '*', width: '*', nodes: [
                {
                    type: 'vert', id: 'dlg_trunk', cls: 'dlg-trunk', height: '*', width: '*', nodes: [
                        {
                            type: 'vert', height: '*', width: '*', nodes: [
                                {type: 'preload/body', id: 'dlg_body', cls: 'dlg-body', height: '*', width: '*'}
                            ]
                        }
                    ]
                }
            ]
        }
    }
});