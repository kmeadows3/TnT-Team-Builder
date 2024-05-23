import axios from 'axios';

export default {
    retrieveItemsForPurchase() {
        return axios.get('/inventory')
      }
}