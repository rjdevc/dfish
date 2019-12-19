define.preload({
    type: "dialog", cls: "dlg", node: {
        type: "view", commands: {
            "close": {
                type: "js",
                text: "$.close(this);"
            },
            "no": {
                type: "js",
                text: "this.cmd('close');"
            }
        },
        node: {
            type: "vert", id: "dlg_frame", height: "*", width: "*", nodes: [
                {
                    type: "horz", id: "dlg_head", cls: "dlg-head", height: "40", valign: "middle", nodes: [
                        {
                            type: "dialog/title",
                            id: "dlg_head_title",
                            cls: "dlg-head-title",
                            width: "*",
                            wmin: 10,
                            on: {"dblclick": "$.dialog(this).max();"}
                        },
                        {
                            type: "buttonbar",
                            id: "dlg_head_oper",
                            cls: "dlg-head-oper",
                            width: "-1",
                            align: "center",
                            valign: "middle",
                            pub: {height: "*", width: 40},
                            nodes: [
                                {
                                    tip: "最大化",
                                    cls: "x-dlg-max",
                                    on: {click: "app.dialog.max(this);"},
                                    icon: ".i-dlg-max"
                                }, {
                                    tip: "关闭",
                                    cls: "dlg-oper-close",
                                    on: {click: "this.cmd('close');"},
                                    icon: ".i-dlg-close"
                                }
                            ]
                        }
                    ]
                },
                {
                    type: "vert", id: "dlg_trunk", height: "*", nodes: [
                        {
                            type: "vert", height: "*", nodes: [
                                {type: "preload/body", id: "dlg_body", height: "*"}
                            ]
                        },
                        {
                            type: "horz",
                            id: "dlg_foot",
                            cls: "dlg-foot",
                            style: "padding:0 20px;",
                            height: 50,
                            hmin: 1,
                            wmin: 40,
                            nodes: [
                                {
                                    type: "html",
                                    id: "dlg_foot_info",
                                    width: "*"
                                },
                                {
                                    type: "buttonbar",
                                    id: "dlg_foot_oper",
                                    width: "*",
                                    align: "right",
                                    space: 10,
                                    pub: {height: 30},
                                    nodes: [
                                        {
                                            type: "submitbutton", text: "确定", on: {click: "this.cmd('yes');"}
                                        },
                                        {
                                            text: "取消", on: {click: "this.cmd('no');"}
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