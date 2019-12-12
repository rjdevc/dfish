define.template({
    type: 'jigsaw/img',
    src: 'jigsaw/img',
    result: {
        '@big': '$data.big',
        '@small': '$data.small',
        minvalue: 0,
        '@maxvalue': '$data.big.width',
        error: {'@w-if':'$data.error', '@msg': '$data.error.msg', '@timeout': '$data.error.timeout'}
    }
});