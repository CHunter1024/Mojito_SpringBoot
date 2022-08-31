// 获取所有分类信息
function getCategoryList() {
    return $axios({
        url: '/category/list',
        method: 'get'
    })
}
// 获取商品信息
function getCommodityList(params) {
    return $axios({
        url: '/commodity/list',
        method: 'get',
        params
    })
}
// 获取套餐信息
function getComboList(params) {
    return $axios({
        url: '/combo/list',
        method: 'get',
        params
    })
}

// 购物车中添加商品
function addCartApi(data) {
    return $axios({
        url: '/shoppingCart',
        method: 'post',
        data
    })
}
// 删除购物车中的商品
function clearCartApi() {
    return $axios({
        url: '/shoppingCart',
        method: 'delete'
    })
}
// 修改购物车中的商品
function updateCartApi(data) {
    return $axios({
        url: '/shoppingCart',
        method: 'put',
        data
    })
}
// 获取购物车信息
function getCartApi() {
    return $axios({
        url: '/shoppingCart',
        method: 'get',
        headers: {'Cache-Control': 'no-cache'}
    })
}

// 获取当前用户信息
function getCurrUser() {
    return $axios({
        url: '/user/getCurrUser',
        method: 'get',
        headers: {'Cache-Control': 'no-cache'}
    })
}