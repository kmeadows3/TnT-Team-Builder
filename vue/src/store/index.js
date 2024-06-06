import { createStore as _createStore } from 'vuex';
import axios from 'axios';
import TeamsService from '../services/TeamsService';
import UnitService from '../services/UnitService';

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
      currentUnit: {},
      showError: false,
      errorMessage: '',
      unitInventoryTraits: [],
      teamInventoryTraits: [],
      manageInventory: false
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
      },
      CHANGE_TEAM_NAME(state, newName){
        state.currentTeam.name = newName;
      },
      GAIN_MONEY(state, addedMoney){
        state.currentTeam.money += addedMoney;
      },
      LOSE_MONEY(state, lostMoney){
        if(state.currentTeam.money - lostMoney >= 0){
            state.currentTeam.money -= lostMoney;
        } else {
          throw "Not enough money for this action";
        }
      },
      CHANGE_UNIT_NAME(state, newName){
        state.currentUnit.name = newName;
      },
      GAIN_UNSPENT_EXP(state, expToGain){
        if(expToGain > 0){
          state.currentUnit.unspentExperience += expToGain;
        } else {
          throw "Experience gained must be positive."
        }
      },
      SHOW_ERROR_OFF(state){
        state.showError = false;
        state.errorMessage = '';
      },
      SHOW_ERROR_ON(state, newMessage){
        state.errorMessage = newMessage;
        state.showError = true;
      },
      SET_UNIT_INVENTORY_TRAITS(state, traits){
        state.unitInventoryTraits = traits;
      },
      SET_MANAGE_INVENTORY(state, value){
        state.manageInventory = value;
      }

    },
    actions: {
      loadTeams() {
        TeamsService.retrieveTeamList()
          .then(response => {
            store.commit('SET_TEAM_LIST', response.data);
          })
          .catch(err => {
            store.commit('SHOW_ERROR_ON', err.response.data.message)
          });
      },
      reloadCurrentUnit(context) {
        UnitService.getUnit(context.state.currentUnit.id)
          .then(response => {
            store.commit('SET_CURRENT_UNIT', response.data);
            store.dispatch('updateUnitInventoryTraits');
          })
          .catch(err => store.commit('SHOW_ERROR_ON', err.response.data.message));
      },
      reloadCurrentTeam(context){
        TeamsService.getTeamById(context.state.currentTeam.id)
        .then(response => {
          store.commit('SET_CURRENT_TEAM', response.data);
        })
        .catch(err => store.commit('SHOW_ERROR_ON', err.response.data.message));
      },
      showError(context, error){
        if(error.response){
          store.commit('SHOW_ERROR_ON', error.response.data.message);
        } else {
          store.commit('SHOW_ERROR_ON', error);
        }
        store.dispatch('loadTeams');
        if(context.state.currentUnit.id){
          store.dispatch('reloadCurrentUnit');
        }
      },
      updateUnitInventoryTraits(context){
        let unitInventory = store.state.currentUnit.inventory;
        let unitTraits = [];
        
        unitInventory.forEach(item => item.itemTraits.forEach(
            trait => {
                if (!unitTraits.some((x) => x.id == trait.id)) {
                    unitTraits.push(trait)
                }
            })
        );

        unitTraits = unitTraits.sort((a, b) => a.name.localeCompare(b.name));

        store.commit('SET_UNIT_INVENTORY_TRAITS', unitTraits);
      }
    }
  });
  return store;
}
