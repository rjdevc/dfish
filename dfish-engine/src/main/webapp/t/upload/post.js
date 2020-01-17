define.template({
    type: 'UploadPost',
    src: 'file/upload/file?scheme=$scheme',
    result: {
        '@id': '$data.id',
        '@name': '$data.name'
    }
});