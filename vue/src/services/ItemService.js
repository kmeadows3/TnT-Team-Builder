import axios from 'axios';

export default {
    retrieveItemsForPurchase() {
        return axios.get('/inventory')
      },
    purchaseItemForUnit(unitId, itemRefId){
      let body = [itemRefId];
      return axios.post(`/units/${unitId}/inventory`, body);
    },
    gainItemForFree(unitId, itemRefId){
      let body = [itemRefId];
      return axios.post(`/units/${unitId}/inventory?isFree=True`, body);
    },
    transferItem(itemId, unitId){
      return axios.put(`/units/${unitId}/inventory/${itemId}/transfer`)
    }
}