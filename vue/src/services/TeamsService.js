import axios from 'axios';

export default {

  retrieveTeamList() {
    return axios.get('/teams')
  },
  getUnitsForTeam(id) {
    return axios.get(`factions/${id}`);
  },
  buyUnit(newUnit){
    return axios.post('/units', newUnit);
  },
  createNewTeam(newTeam){
    return axios.post('/teams', newTeam);
  },
  getFactionList(){
    return axios.get('factions');
  },
  updateTeam(team){
    return axios.put(`/teams/${team.id}`, team);
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
  }

}
