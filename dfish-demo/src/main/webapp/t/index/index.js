define.template({
    type: "view", node: {
        type: "horizontal", scroll: true, nodes: [{
            type: "vertical", width: "*", minwidth: 1200, style: "background:#edf0f2;", nodes: [
                {
                    type: "horizontal",
                    cls: "index-head",
                    height: "50",
                    nodes: [{type: "html", width: "*"}, {
                        type: "horizontal", width: 1200, nodes: [{
                            type: "img",
                            src: ".i-logo",
                            width: 210,
                            height: "*",
                            style: "padding-right:10px;",
                            wmin: 10
                        }, {
                            type: "buttonbar",
                            id: "memuBar",
                            width: "*",
                            space: 0,
                            cls: "index-head-nav",
                            pub: {height: "*", width: 100, focusable: true},
                            nodes: [
                                {text: "首页", target: "home"},
                                {text: "范例", target: "demo", focus: true}
                            ]
                        }]
                    }, {type: "html", width: "*"}
                    ]
                }, {
                    type: "frame", id: "main", height: "*", nodes: [
                        {type: "view", id: "home", template: "index/home"},
                        {type: "view", id: "demo", template: "index/demo"},
                    ]
                }
            ]
        }]
    }
});