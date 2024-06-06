import axios from 'axios';

export default {

  retrieveTeamList() {
    return axios.get('/teams')
  },
  createNewTeam(newTeam){
    return axios.post('/teams', newTeam);
  },
  getFactionList(team){
    return axios.get('factions');
  },
  updateTeam(team){
    return axios.put(`/teams/${team.id}`, team);
  },
  getTeamById(teamId){
    return axios.get(`/teams/${teamId}`);
  }

}
