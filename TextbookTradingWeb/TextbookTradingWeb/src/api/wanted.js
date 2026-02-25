import request from '@/utils/request'

export const getWantedList = (params) => request.get('/wanted/list', { params })
export const getMyWanted = (params) => request.get('/wanted/my', { params })
export const publishWanted = (data) => request.post('/wanted/publish', data)
export const updateWanted = (data) => request.put('/wanted/update', data)
export const closeWanted = (id) => request.put(`/wanted/close/${id}`)
export const deleteWanted = (id) => request.delete(`/wanted/${id}`)
