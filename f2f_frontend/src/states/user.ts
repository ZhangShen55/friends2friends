import {UserType} from "../models/user"


// 公共变量
let currentUser : UserType ;



//这两个方法表示从变量中取出的信息 而不是从远程取出的信息
const setCurrentUserState = (user : UserType) => {
    currentUser = user;
}

const getCurrentUserState = () : UserType => {
    return currentUser;
}
export {
    getCurrentUserState,
    setCurrentUserState,
}