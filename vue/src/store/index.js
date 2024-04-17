import { createStore as _createStore } from 'vuex';
import axios from 'axios';
import TeamsService from '../services/TeamsService';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      showTeamList: true,
      showTeamDetail: false,
      showUnitDetail: false,
      showNewUnitForm: false,
      showNewTeamForm: false,
      teamList: [],
      currentTeam: {},
      currentUnit: {}
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        state.teamList = [];
        state.currentTeam = {};
        state.currentUnit = {};
        axios.defaults.headers.common = {};
      },
      SET_TEAM_LIST(state, teamList){
        state.teamList = teamList;
      },
      SET_CURRENT_TEAM(state, team){
        state.currentTeam = team;
        state.showTeamDetail = true;
        state.showTeamList = false;
      },
      CLEAR_CURRENT_TEAM(state){
        state.currentTeam = {};
        state.showTeamDetail = false;
        state.showTeamList = true;
      },
      SET_CURRENT_UNIT(state, unit){
        state.currentUnit = unit;
        state.showUnitDetail = true;
        state.showTeamDetail = false;
      },
      CLEAR_CURRENT_UNIT(state){
        state.currentUnit = {};
        state.showUnitDetail = false;
        state.showTeamDetail = true;
      },
      TOGGLE_NEW_UNIT_FORM(state){
        state.showNewUnitForm = !state.showNewUnitForm;
      },
      TOGGLE_NEW_TEAM_FORM(state){
        state.showNewTeamForm = !state.showNewTeamForm;
      }
    },
    actions: {
      loadTeams() {
        TeamsService.retrieveTeamList()
          .then(response => {
            store.commit('SET_TEAM_LIST', response.data);
          })
          .catch(err => {
            console.error(err)
          });
      }
    }
  });
  return store;
}
