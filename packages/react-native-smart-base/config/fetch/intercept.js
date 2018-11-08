let $fetch;
let interceptors = [];

const interceptor = (fetch, ...args) => {
  const reversedInterceptors = interceptors.reduce(
      (array, interceptor) => [interceptor].concat(array), []);
  const [, opt] = args;
  let promise = Promise.resolve(args);

  // Register request interceptors
  reversedInterceptors.forEach(({request, requestError}) => {
    if (request || requestError) {
      promise = promise.then(args => request(...args), requestError);
    }
  });

  // Register fetch call
  promise = promise.then(args => fetch(...args));

  // Register response interceptors
  reversedInterceptors.forEach(({response, responseError}) => {
    if (response || responseError) {
      promise = promise.then(res => response(res, opt),
          err => responseError(err, opt));
    }
  });

  return promise;
};

export const request = ((fetch) => {
  return (...args) => {
    return interceptor(fetch, ...args);
  };
})(fetch);

export const fetchIntercept = {
  register: (interceptor) => {
    interceptors.push(interceptor);
    return () => {
      const index = interceptors.indexOf(interceptor);
      if (index >= 0) {
        interceptors.splice(index, 1);
      }
    };
  },
  clear: () => {
    interceptors = [];
  },
};
