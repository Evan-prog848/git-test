import request from '@/utils/request'

// ========== 校区相关 ==========
export function getCampusList() {
  return request({
    url: '/campus/list',
    method: 'get'
  })
}

// ========== 专业课程相关 ==========
export function getMajorList(campusId) {
  return request({
    url: '/course/major/list',
    method: 'get',
    params: { campusId }
  })
}

export function getCourseList(majorId) {
  return request({
    url: '/course/list',
    method: 'get',
    params: { majorId }
  })
}

export function searchCourses(keyword) {
  return request({
    url: '/course/search',
    method: 'get',
    params: { keyword }
  })
}

// ========== 交易点相关 ==========
export function getTradingPointList(campusId) {
  return request({
    url: '/trading-point/list',
    method: 'get',
    params: { campusId }
  })
}

// ========== ISBN查询 ==========
export function queryIsbn(isbn) {
  return request({
    url: '/isbn/query',
    method: 'get',
    params: { isbn }
  })
}

// ========== 信用相关 ==========
export function getMyCreditScore() {
  return request({
    url: '/credit/score',
    method: 'get'
  })
}

export function getUserCreditScore(userId) {
  return request({
    url: `/credit/score/${userId}`,
    method: 'get'
  })
}

export function getMyCreditRecords(params) {
  return request({
    url: '/credit/records',
    method: 'get',
    params
  })
}

export function reportUser(data) {
  return request({
    url: '/credit/report',
    method: 'post',
    data
  })
}
