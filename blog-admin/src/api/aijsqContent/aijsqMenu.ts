import request from '@/utils/request'

/**
 * 获取菜单列表
 */
export function getMenuListApi(params: any) {
  return request({
    url: '/sys/aijsqMenu/tree',
    method: 'get',
    params
  })
}

/**
 * 获取菜单详情
 */
export function getMenuDetailApi(id: string) {
  return request({
    url: `/sys/aijsqMenu/${id}`,
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export function saveMenuApi(data: any) {
  return request({
    url: '/sys/aijsqMenu/add',
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 */
export function updateMenuApi(data: any) {
  return request({
    url: `/sys/aijsqMenu/update`,
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 */
export function deleteMenuApi(id: string) {
  return request({
    url: `/sys/aijsqMenu/${id}`,
    method: 'delete'
  })
}


export function listRoutes() {
  return request({
    url: '/sys/aijsqMenu',
    method: 'post',
  })
}