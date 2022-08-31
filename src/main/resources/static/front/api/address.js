// 新增地址
function addAddressApi(data) {
    return $axios({
        url: '/addressBook',
        method: 'post',
        data
    })
}

// 获取所有地址
function addressListApi() {
    return $axios({
        url: '/addressBook',
        method: 'get',
        headers: {'Cache-Control': 'no-cache'}
    })
}

// 获取单个地址
function getAddressApi(id) {
    return $axios({
        url: `/addressBook/${id}`,
        method: 'get'
    })
}

// 修改地址
function updateAddressApi(data) {
    return $axios({
        url: '/addressBook',
        method: 'put',
        data
    })
}

// 删除地址
function deleteAddressApi(id) {
    return $axios({
        url: `/addressBook/${id}`,
        method: 'delete'
    })
}

// 获取下单地址
function getOrderAddressApi() {
    return $axios({
        url: `/addressBook/order`,
        method: 'get'
    })
}