import request from '@/utils/request'

export const addFavorite = (textbookId) => request.post(`/favorite/add/${textbookId}`)
export const removeFavorite = (textbookId) => request.delete(`/favorite/remove/${textbookId}`)
export const toggleFavorite = (textbookId) => request.post(`/favorite/toggle/${textbookId}`)
export const checkFavorite = (textbookId) => request.get(`/favorite/check/${textbookId}`)
export const getMyFavorites = (params) => request.get('/favorite/list', { params })


