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

// 获取预置配置列表
const getConfigListHand = () => [
    {name: '温度', value: ['正常冰', '少冰', '常温', '热']},
    {name: '糖度', value: ['正常糖', '少糖', '无糖']},
    {name: '杯型', value: ['中杯', '大杯']},
    {name: '底料', value: ['原味', '双炫']},
    {name: '冰淇淋口味', value: ['草莓味+原味']},
]
