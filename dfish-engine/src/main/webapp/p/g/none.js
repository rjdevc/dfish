define.preload({
    "type": "dialog", "cls": "dlg", "node": {
        "type": "view", "commands": {
            "close": {"type": "js", "text": "dfish.close(this);"}
        }, "node": {
            "type": "vert", "id": "dlg_frame", "height": "*", "width": "*", "nodes": [
                {
                    "type": "vert", "id": "dlg_trunk", "cls": "dlg-trunk", "height": "*", "nodes": [
                        {
                            "type": "vert", "height": "*", "nodes": [
                                {"type": "preload/body", "id": "dlg_body", "cls": "dlg-body", "height": "*"}
                            ]
                        }
                    ]
                }
            ]
        }
    }
});