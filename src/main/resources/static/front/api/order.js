// 提交订单
function addOrderApi(data) {
    return $axios({
        url: '/order',
        method: 'post',
        data
    })
}

// 分页查询订单
function getOrderPage(params) {
    return $axios({
        url: '/order/page',
        method: 'get',
        params,
        headers: {'Cache-Control': 'no-cache'}
    })
}

//查询所有订单
function orderListApi() {
  return $axios({
    'url': '/order/list',
    'method': 'get',
  })
}

// 再来一单
function addOrderAgainApi(data) {
  return $axios({
      url: '/order/again',
      method: 'post',
      data
  })
}

// 获取单个订单
function getOrderApi(id) {
    return $axios({
        url: `/order/${id}`,
        method: 'get'
    })
}

// 删除订单
function deleteOrderApi(id) {
    return $axios({
        url: `/order/${id}`,
        method: 'delete'
    })
}