import myAxios from "../plugins/myAxios.js";
import {getCurrentUserState, setCurrentUserState} from "../states/user.ts";
import router from "../config/router.ts";


export const getCurrentUser = async () => {

    /**
     * 如果网站用户使用量小 建议不要使用缓存 使用缓存的操作会麻烦
     * 用户体量大 使用缓存会好
     */
        // const currentUser = getCurrentUserState();
    // //存在直接返回
    // if(currentUser){
    //     return currentUser;
    // }
    //不存在 那就远端获取
    const res = await myAxios.get('/user/current');
    if(res.code === 0 ){
        setCurrentUserState(res.data);
        // console.log('service user 打印返回值',res.data)
        return res.data;
    }
    //重定向到
    router.replace('user/login')


}