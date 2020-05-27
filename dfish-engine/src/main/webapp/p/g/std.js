define.preload({
    "type": "dialog", "cls": "dlg", "node": {
        "type": "view", "commands": {
            "close": {"type": "js", "text": "dfish.close(this);"}
        }, "node": {
            "type": "vert", "id": "dlg_frame", "height": "*", "width": "*", "nodes": [
                {
                    "type": "horz", "id": "dlg_head", "cls": "dlg-head", "height": "40", "valign": "middle", "nodes": [
                        {
                            "type": "dialog/title",
                            "id": "dlg_title",
                            "cls": "dlg-title",
                            "width": "*",
                            "wmin": 10,
                            "on": {"dblclick": "$.dialog(this).max();"}
                        }, {
                            "type": "buttonbar",
                            "id": "dlg_head_oper",
                            "cls": "dlg-head-oper",
                            "width": "-1",
                            "align": "center",
                            "valign": "middle",
                            "pub": {"height": "*", "width": "40"},
                            "nodes": [
                                {
                                    "tip": "最大化",
                                    "cls": "x-dlg-max",
                                    "on": {"click": "app.dialog.max(this);"},
                                    "icon": ".i-dlg-max"
                                }, {
                                    "tip": "关闭",
                                    "cls": "x-dlg-close",
                                    "on": {"click": "this.cmd('close');"},
                                    "icon": ".i-dlg-close"
                                }
                            ]
                        }
                    ]
                }, {
                    "type": "vert", "id": "dlg_trunk", "cls": "dlg-trunk", "height": "*", "nodes": [
                        {
                            "type": "vert", "height": "*", "nodes": [
                                {"type": "preload/body", "id": "dlg_body", "cls": "dlg-body", "height": "*"}
                            ]
                        }, {
                            "type": "horz",
                            "id": "dlg_foot",
                            "cls": "dlg-foot",
                            "style": "padding:0 20px;",
                            "height": "50",
                            "hmin": 1,
                            "wmin": 40,
                            "nodes": [
                                {
                                    "type": "buttonbar",
                                    "id": "dlg_foot_oper",
                                    "width": "*",
                                    "align": "right",
                                    "space": 10,
                                    "pub": {"cls": "x-btn"},
                                    "nodes": [
                                        {
                                            "text": "关闭", "on": {"click": "this.cmd('close');"}
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