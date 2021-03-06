define.template({
    type: 'ProgressLoader',
    '@src': '"progress/reload/"+($progressKey||$data.progressKey)',
    '@delay': '$delay||$data.delay',
    template: 'g/progress/single',
    success: 'if($response.data&&$response.data.finish){$.alert("进度完成",5,5000);}',
    complete: 'if($response.error||($response.data&&$response.data.finish)){$.close(this);}',
    nodes: [
        {
            '@text': '$data.progressText',
            '@percent': '$data.stepPercent'
        }
    ]
});