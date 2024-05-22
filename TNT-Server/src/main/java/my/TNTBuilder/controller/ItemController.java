package my.TNTBuilder.controller;

import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class ItemController {

    private ItemService itemService;
    private UserDao userDao;

    public ItemController(ItemService itemService, UserDao userDao){
        this.itemService = itemService;
        this.userDao = userDao;
    }


    /**
     * Adds a new item to the unit inventory
     * @param itemId the skill to be added
     * @param unitId the id of the unit
     * @param principal the logged-in user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/units/{unitId}/inventory", method = RequestMethod.POST)
    public void addSkillToUnit(@RequestBody Integer itemId, @PathVariable int unitId, Principal principal){
        try {
            itemService.purchaseItemForUnit(itemId, unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    /**
     * Returns list of all inventory that can be bought at this time.
     * @return
     */
    @RequestMapping(path="/inventory", method = RequestMethod.GET)
    public List<Item> getItemsForPurchase(){
        List<Item> purchaseList;
        try {
            purchaseList = itemService.getItemsForPurchase();
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return purchaseList;
    }


}
