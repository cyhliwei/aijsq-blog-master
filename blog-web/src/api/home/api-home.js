import request from '@/utils/request'

/**
 * 获取首页数据
 */
export function getHomeDataApi() {
  return request({
    url: '/api/home/data',
    method: 'get'
  })
}

/**
 * 获取轮播图数据
 */
export function getCarouselsApi() {
  return request({
    url: '/api/carousels',
    method: 'get'
  })
}

/**
 * 获取快速导航数据
 */
export function getQuickNavsApi() {
  return request({
    url: '/api/home/quick-navs',
    method: 'get'
  })
}

/**
 * 获取首页统计数据
 */
export function getHomeStatsApi() {
  return request({
    url: '/api/home/stats',
    method: 'get'
  })
}