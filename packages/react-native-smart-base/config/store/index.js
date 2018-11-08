import React from 'react';
import thunk from 'redux-thunk';
import merge from 'lodash.merge';
import storage from 'redux-persist/lib/storage';
import {persistReducer, persistStore} from 'redux-persist';
import {applyMiddleware, createStore, combineReducers} from 'redux';
import initReducer from './reducer';

const DefaultPersistConfig = {
  key: `smart-${Date.now()}`,
  storage,
  blacklist: ['$fetch'],
};

let $reducer = {};
let $store;
let $persistor;
let hasInit = false;

export const addReducer = (reducer) => {
  $reducer = Object.assign($reducer, reducer);
};

addReducer(initReducer);

/**
 * @orivate
 * @param persistConfig 具体参见 https://github.com/rt2zz/redux-persist#persistreducerconfig-reducer 的 config 参数
 * @param reducer
 * @returns {{store: *, persistor: *}}
 */
export const init = (persistConfig = {}, reducer) => {
  if (hasInit) {
    return {store: $store, persistor: $persistor};
  }
  hasInit = true;
  const persistedReducer = persistReducer(merge(DefaultPersistConfig, persistConfig), combineReducers(Object.assign($reducer, reducer)));
  $store = createStore(persistedReducer, applyMiddleware(thunk));
  $persistor = persistStore($store);
  return {store: $store, persistor: $persistor};
};

/**
 * 在 {@link init} 后才能获取到 store
 */
export const getStore = () => {
  return $store;
};