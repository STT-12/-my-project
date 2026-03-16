import axios from 'axios'

const withoutToken = axios.create({
    baseURL: import.meta.env.VITE_APP_API_URL,// 已经包含 /api
    timeout: 5000,
})

export default{
    getAllCategory(){
        return get("/category/list");  // 移除 /api 前缀
    },
    getBookByCategory(categoryId){
        return get("/book/list-category", {categoryId});  // 移除 /api 前缀
    },
    getBookByKeyword(keyword){
        return get("/book/list-keyword", {keyword});  // 移除 /api 前缀
    },
    getBookPaginate( page, limit ){
        return get("/book/list-page", {page, limit});  // 移除 /api 前缀
    },
    getBookComplex(page, limit, keyword, categoryId){
        return get("/book/list-complex", {
            page,
            limit,
            keyword,
            categoryId: categoryId || 0
        });  // 移除 /api 前缀
    }
};

const get = function(url, options, ...others){
    return withoutToken.get(url, {params:options}, ...others );
}

const post = function(url, options, ...others){
    return withoutToken.post(url, options, ...others);
}