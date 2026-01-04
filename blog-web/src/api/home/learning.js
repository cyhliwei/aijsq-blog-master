import request from '@/utils/request'
 
/**
 * 获取学习路径列表
 */
export function getLearningPathsApi() {
  return request({
    url: '/api/home/learning-paths',
    method: 'get'
  })
}
 
/**
 * 获取学习路径详情
 */
export function getLearningPathDetailApi(id) {
  return request({
    url: `/api/home/learning-paths/${id}`,
    method: 'get'
  })
}