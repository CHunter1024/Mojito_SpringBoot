// 分页查询订单信息
const getOrderPage = (params) => {
  return $axios({
    url: '/order/page',
    method: 'get',
    params
  })
}

// 修改订单
const editOrder = (data) => {
  return $axios({
    url: '/order',
    method: 'put',
    data
  })
}
