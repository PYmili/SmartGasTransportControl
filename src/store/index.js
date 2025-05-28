import { createStore } from 'vuex';
import { verifyJwtRequest } from '@/components/util/JwtUtil';

export default createStore({
  state() {
    return {
      // 用户登录状态，默认为 false
      isLoggedIn: false,
      jwtToken: localStorage.getItem('jwt') || '',
    };
  },
  mutations: {
    // 设置用户登录状态
    SET_LOGIN_STATUS(state, status) {
      state.isLoggedIn = status;
    },
    // 设置用户的JWT_TOKEN
    SET_JWT_TOKEN(state, token) {
      state.jwtToken = token;
      localStorage.setItem('jwt', token);
    }
  },
  actions: {
    // 登录操作
    login({ commit }, token) {
      commit('SET_LOGIN_STATUS', true);
      commit('SET_JWT_TOKEN', token);
    },
    // 退出登录操作
    logout({ commit }) {
      commit('SET_LOGIN_STATUS', false);
      commit('SET_JWT_TOKEN', '');
      localStorage.removeItem('jwt');
    },
  // 验证 JWT
    async verifyJwt({ commit, state }) {
      try {
        // 等待 verifyJwtRequest 的结果
        const isValid = await verifyJwtRequest(state.jwtToken);
        // console.log(`verify jwt ${isValid}`);
        commit('SET_LOGIN_STATUS', isValid);
      } catch (error) {
        console.error('Error verifying JWT:', error);
        commit('SET_LOGIN_STATUS', false);
      }
    }
  },
  getters: {
    // 判断用户是否登录
    isUserLoggedIn: (state) => state.isLoggedIn
  }
});