import request from '@/utils/request'
 
/**
 * 获取趋势数据
 */
export function getTrendsApi() {
  return request({
    url: '/api/home/trends',
    method: 'get'
  })
}
 
/**
 * 获取趋势详情
 */
export function getTrendDetailApi(keyword) {
  return request({
    url: `/api/home/trends/${keyword}`,
    method: 'get'
  })
}