import qs from 'qs';
import merge from 'lodash.merge';
import {request as fetch, fetchIntercept} from './intercept';
import {getStore} from '../store/index';

let domain = '';

export const setDomain = (url) => {
  domain = url || '';
};

fetchIntercept.register({
  request(url, config = {}) {
    config.fetchModal && getStore().dispatch({type: 'START_FETCH'});
    const fetchUrl = /^https?:\/\//.test(url) ? url : `${domain}${url}`;
    config._url = fetchUrl;
    return [fetchUrl, config];
  },
  requestError(error) {
    // Called when an error occurred during another 'request' interceptor call
    // reject 后会进入 responseError
    return Promise.reject(error);
  },
  response(response, opt) {
    opt.fetchModal && getStore().dispatch({type: 'END_FETCH'});
    const {status} = response;
    const {expect: Result = Object} = opt;
    if (Math.floor(status / 100) === 5) {
      return new Result();
    }
    const res = response.clone();
    const r = response.clone();
    return response.json().catch(() => res.text()).catch(() => r);
  },
  responseError(error, opt = {}) {
    console.warn(error, opt);
    opt.fetchModal && getStore().dispatch({type: 'END_FETCH'});
    // 报错时期望的返回结果
    const {expect: Result = Object} = opt;
    return Promise.resolve(new Result());
  },
});

// fetch.shoutMethod(url, config)
const createShortMethods = (...names) => {
  names.forEach(name => {
    fetch[name] = (url, config = {}) => {
      const data = config.data || config.body;
      const queryString = data ? ('?' + qs.stringify(data)) : '';
      return fetch(url + queryString, Object.assign(config, {
        method: name,
      }));
    };
  });
};
// fetch.shoutMethod(url, data, config)
const createShortMethodsWithData = (...names) => {
  names.forEach(name => {
    fetch[name] = (url, data = {}, config = {}) => fetch(url,
        merge({
          body: qs.stringify(data),
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        }, config, {
          method: name,
        }));
  });
};

createShortMethods('get', 'delete', 'head', 'jsonp');

createShortMethodsWithData('post', 'put', 'patch');

// 使用文档 https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
// 配置中多一个 expect 配置, 出错时返回期望的结果构造函数. 默认 Object
export default fetch;

export const intercept = fetchIntercept;
