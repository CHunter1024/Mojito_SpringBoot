//获取url地址上面的参数
function getUrlParam(argName) {
    let url = location.href
    let arrStr = url.substring(url.indexOf('?') + 1).split("&")
    for (let i = 0; i < arrStr.length; i++) {
        let index = arrStr[i].lastIndexOf(argName + '=')
        if (index !== -1) {
            return arrStr[i].substring(index + (argName + '=').length)
        }
    }
    return undefined
}

// 获取文件 Url
const getFileUrl = (fileName, subDir, defaultUrl) => {
    let url = `/common/download?fileName=${fileName}`
    return fileName ? (subDir ? `${url}&subDir=${subDir}` : url) : defaultUrl
}