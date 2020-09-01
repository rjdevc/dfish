define.template({
    type: 'JigsawImg',
    src: 'jigsaw/img',
    result: {
        '@big': '$data.big',
        '@small': '$data.small',
        minValue: 0,
        '@maxValue': '$data.big.width',
        error: {'@w-if':'$data.error', '@text': '$data.error.text', '@timeout': '$data.error.timeout'}
    }
});