import React from 'react';
import {View, ActivityIndicator} from 'react-native';
import {connect} from 'react-redux';

class FetchLoading extends React.Component {
  render() {
    if (this.props.fetchCount <= 0) {
      return null;
    }
    return (
        <View
            style={{
              position: 'absolute',
              left: 0, right: 0, top: 0, bottom: 0,
              backgroundColor: 'rgba(0,0,0,0)',
              justifyContent: 'center',
              alignItems: 'center',
              zIndex: 999,
            }}
        >
          <View style={{
            padding: 20,
            backgroundColor: 'rgba(0,0,0,0.6)',
            borderRadius: 5,
          }}>
            <ActivityIndicator animating={true} color="#fff"/>
          </View>
        </View>
    );
  }
}

const mapStateToProps = state => {
  return {
    fetchCount: state.$fetch.count,
  };
};

export default connect(mapStateToProps)(FetchLoading);
