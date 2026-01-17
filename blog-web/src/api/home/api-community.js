import request from '@/utils/request'

/**
 * 获取社区讨论列表
 */
export function getDiscussionsApi(params) {
  return request({
    url: '/api/community/discussions',
    method: 'get',
    params: params
  })
}

/**
 * 获取热门讨论
 */
export function getHotDiscussionsApi() {
  return request({
    url: '/api/community/discussions/hot',
    method: 'get'
  })
}

/**
 * 获取在线专家
 */
export function getOnlineExpertsApi() {
  return request({
    url: '/api/community/experts/online',
    method: 'get'
  })
}

/**
 * 咨询专家
 */
export function consultExpertApi(data) {
  return request({
    url: '/api/community/experts/consult',
    method: 'post',
    data
  })
}