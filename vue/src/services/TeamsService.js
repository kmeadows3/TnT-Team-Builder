import axios from 'axios';

export default {

  retrieveTeamList() {
    return axios.get('/teams')
  }

}
