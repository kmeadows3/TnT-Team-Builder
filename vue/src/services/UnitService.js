import axios from 'axios';

export default {

  buyUnit(newUnit){
    return axios.post('/units', newUnit);
  },
  getPotentialSkills(id){
    return axios.get(`/units/${id}/skills`);
  },
  addSkill(id, skill){
    return axios.post(`/units/${id}/skills`, skill);
  },
  getUnit(id){
    return axios.get(`/units/${id}`);
  },
  updateUnit(unit){
    return axios.put(`/units/${unit.id}`, unit);
  },
  getBaseUnit(unitClass){
    return axios.get(`/units?class=${unitClass}`);
  },
  getUnitsForTeam(team) {
    return axios.post(`factions/${team.factionId}`, team);
  },
  killUnit(unit) {
    return axios.delete(`/units/${unit.id}`);
  },
  dismissUnit(unit){
    return axios.delete(`/units/${unit.id}?deleteItems=false`);
  }

}
