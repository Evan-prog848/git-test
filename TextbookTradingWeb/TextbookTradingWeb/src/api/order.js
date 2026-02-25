import request from '@/utils/request'

export const createOrder = (data) => request.post('/order/create', data)
export const getOrderDetail = (id) => request.get(`/order/detail/${id}`)
export const getBuyerOrders = (params) => request.get('/order/buyer', { params })
export const getSellerOrders = (params) => request.get('/order/seller', { params })
export const confirmOrder = (id) => request.put(`/order/confirm/${id}`)
export const rejectOrder = (id) => request.put(`/order/reject/${id}`)
export const completeOrder = (id) => request.put(`/order/complete/${id}`)
export const cancelOrder = (id) => request.put(`/order/cancel/${id}`)


