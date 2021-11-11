import request from '@/request'
//获取个人信息
export function getUserDetail( token) {
  return request({
    headers: {'Authorization': token},
    url: '/users/currentUserDetail',
    method: 'get'
  })
}

//修改个人信息
export function updateUserDetail(token,data) {
  return request({
    headers: {'Authorization': token},
    url: '/users/updateUserDetail',
    method:'post',
    data
  })
}

//修改密码
export function updatePassword(token,data) {
  return request({
    headers: {'Authorization': token},
    url: '/users/updatePassword',
    method:'post',
    data
  })
}

// 头像上传审核
export function checkImg(image) {
  return request({
    url: '/check/img',
    method: 'post',
    data: image
  })
}
