export function getToken() {
  return localStorage.getItem('token')
}

export function setToken(token) {
  localStorage.setItem('token', token)
}

export function removeToken() {
  localStorage.removeItem('token')
}

export function getUser() {
  const user = localStorage.getItem('userInfo')
  return user ? JSON.parse(user) : null
}

export function setUser(user) {
  localStorage.setItem('userInfo', JSON.stringify(user))
}

export function removeUser() {
  localStorage.removeItem('userInfo')
}

export function clearAll() {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
}


