import request from '@/utils/request'

export const login = (data) => request.post('/user/login', data)
export const register = (data) => request.post('/user/register', data)
export const getInfo = () => request.get('/user/info')
export const updateInfo = (data) => request.put('/user/update', data)
export const updatePassword = (data) => request.put('/user/password', data)
export const getUserProfile = (userId) => request.get(`/user/profile/${userId}`)
export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', 'avatar')
  return request.post('/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}


