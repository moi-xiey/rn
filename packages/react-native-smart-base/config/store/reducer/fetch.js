const initialState = {
  count: 0, // 开始请求 count+1, 请求结束 count-1. count>0 时有 modal
};

export default (state = initialState, action) => {
  switch (action.type) {
    case 'START_FETCH': {
      return {
        ...state,
        count: state.count + 1,
      };
    }
    case 'END_FETCH': {
      const count = state.count - 1;
      return {
        ...state,
        count: Math.max(count, 0),
      };
    }
    default:
      return state;
  }
};