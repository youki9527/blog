// 对浏览器来说，使用 Web Storage 存储键值对比存储 Cookie 方式更直观，而且容量更大，它包含两种：localStorage 和 sessionStorage
//
// sessionStorage（临时存储） ：为每一个数据源维持一个存储区域，在浏览器打开期间存在，包括页面重新加载
//
// localStorage（长期存储） ：与 sessionStorage 一样，但是浏览器关闭后，数据依然会一直存在
export function getToken() {
  return localStorage.token
}

export function setToken(token) {

  return localStorage.token = token
}

export function removeToken() {
  return localStorage.removeItem('token')
}
