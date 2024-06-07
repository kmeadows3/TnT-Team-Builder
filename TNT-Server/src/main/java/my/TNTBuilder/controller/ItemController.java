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
    public void addItemToUnit(@RequestBody int[] itemId, @PathVariable int unitId,
                              @RequestParam(defaultValue = "false") Boolean isFree, Principal principal){
        try {
            itemService.addItemToUnit(itemId[0], unitId, userDao.getUserIdByUsername(principal.getName()), isFree);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Adds a new item to the unit inventory
     * @param itemId the skill to be added
     * @param teamId the id of the unit
     * @param isFree whether the item will be free or cost money
     * @param principal the logged-in user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/teams/{teamId}/inventory", method = RequestMethod.POST)
    public void addItemToTeam(@RequestBody int[] itemId, @PathVariable int teamId,
                              @RequestParam(defaultValue = "false") Boolean isFree, Principal principal){
        try {
            itemService.addItemToTeam(itemId[0], teamId, userDao.getUserIdByUsername(principal.getName()), isFree);
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

    @RequestMapping(path="/units/{unitId}/inventory/{itemId}/transfer", method = RequestMethod.PUT)
    public void transferItem(@PathVariable int itemId, @PathVariable int unitId, Principal principal){
        try {
            itemService.transferItem(itemId, unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(path="/inventory/{itemId}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable int itemId, @RequestParam(defaultValue = "false") Boolean sell,
                           Principal principal){
        try {
            if (sell){
                itemService.sellItem(itemId, userDao.getUserIdByUsername(principal.getName()));
            } else {
                itemService.deleteItem(itemId, userDao.getUserIdByUsername(principal.getName()));
            }
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
