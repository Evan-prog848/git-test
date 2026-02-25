import request from '@/utils/request'

export const publishTextbook = (data) => request.post('/textbook/publish', data)
export const updateTextbook = (data) => request.put('/textbook/update', data)
export const getTextbookList = (params) => request.get('/textbook/list', { params })
export const getTextbookDetail = (id) => request.get(`/textbook/detail/${id}`)
export const getMyPublish = (params) => request.get('/textbook/my', { params })
export const updateTextbookStatus = (id, status) => request.put(`/textbook/status/${id}`, null, { params: { status } })
export const deleteTextbook = (id) => request.delete(`/textbook/${id}`)


