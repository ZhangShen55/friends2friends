import axios from "axios";

const myAxios = axios.create({
    baseURL: 'http://localhost:8080/api',

});
// 默认携带cookie请求
myAxios.defaults.withCredentials = true


// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    // console.log("在发送请求之前做些什么。。。 返回了一个config")
    return config;
}, function (error) {
    // 对请求错误做些什么
    // console.log("对请求错误做些什么。。。 返回了一个Promise.reject(error)")
    return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    // console.log("对响应数据做点什么。。。")
    return response.data;
}, function (error) {
    // 对响应错误做点什么
    // console.log("对响应错误做点什么。。。")
    return Promise.reject(error);
});

//导出
export default myAxios;