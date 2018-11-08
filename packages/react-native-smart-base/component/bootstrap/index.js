import React from 'react';
import PropTypes from 'prop-types';
import {Provider} from 'react-redux';
import {PersistGate} from 'redux-persist/integration/react';
import {init} from '../../config/store/index';
import FetchLoading from '../fetch-loading';

export default class Bootstrap extends React.PureComponent {
  static propTypes = {
    name: PropTypes.string.isRequired,
    loading: PropTypes.node,
    reducer: PropTypes.object,
  };

  static defaultProps = {
    loading: null,
    reducer: {},
  };

  render() {
    const {name, loading, reducer, children} = this.props;
    const {store, persistor} = init({key: name}, reducer);
    return (
        <Provider store={store}>
          <PersistGate persistor={persistor} loading={loading}>
            {children}
            <FetchLoading />
          </PersistGate>
        </Provider>
    );
  }
}