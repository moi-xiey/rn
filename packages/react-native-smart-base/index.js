import React from 'react';
import fetch from './config/fetch/index';
import Bootstrap from './component/bootstrap';

React.Component.prototype.fetch = fetch;

export const Provider = Bootstrap;