package my.TNTBuilder.controller;

import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class UnitController {
    private UnitService unitService;
    private UserDao userDao;

    public UnitController(UnitService unitService, UserDao userDao){
        this.unitService = unitService;
        this.userDao = userDao;
    }

    /**
     * Validates and initializes a potential unit, then creates a copy in the database
     * @param unit the unit to be added, with the minimum details needed to initialize the unit
     * @param principal the logged-in user
     * @return the fully initialized unit
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/units", method = RequestMethod.POST)
    public Unit newUnit(@Valid @RequestBody Unit unit, Principal principal){
        Unit newUnit = null;
        try{
            newUnit = unitService.createNewUnit(unit, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


        if (newUnit == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to create unit");
        }
        return newUnit;
    }

    /**
     * Returns the base template of a class
     * @param unitClass the class of the unit
     * @return a unit that matches the base template of the class before any advances
     */
    @RequestMapping(path = "/units", method = RequestMethod.GET)
    public Unit getReferenceUnitByClass(@RequestParam(value="class") String unitClass) {
        Unit refUnit = null;
        try{
            refUnit = unitService.getReferenceUnitByClass(unitClass);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (refUnit == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reference unit");
        }

        return refUnit;
    }


    /**
     * Updates a unit
     * @param unit the unit from the client to be validated then updated in the database
     * @param principal the logged-in user
     * @return the updated unit
     */
    @RequestMapping(path="/units/{unitId}", method = RequestMethod.PUT)
    public Unit updateUnit(@RequestBody Unit unit, Principal principal){
        Unit updatedUnit = null;
        try {
            updatedUnit = unitService.updateUnit(unit, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (updatedUnit == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update unit");
        }

         return updatedUnit;
    }

    /**
     * Returns a list of valid skills for a unit
     * @param unitId the id of the unit
     * @return the list of skills available to the unit
     */
    @RequestMapping(path="/units/{unitId}/skills", method = RequestMethod.GET)
    public List<Skill> getSkillsForPurchase(@PathVariable int unitId){
        List<Skill> skills = null;
        try {
            skills = unitService.getPotentialSkills(unitId);
            if (skills == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No valid skills returned.");
            }
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return skills;
    }

    /**
     * Adds a new skill to the unit
     * @param skill the skill to be added
     * @param unitId the id of the unit
     * @param principal the logged-in user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/units/{unitId}/skills", method = RequestMethod.POST)
    public void addSkillToUnit(@RequestBody Skill skill, @PathVariable int unitId, Principal principal){
        try {
            unitService.addSkillToUnit(skill, unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    /**
     * Returns a specific unit based on the unit's id
     * @param unitId the unit id
     * @param principal the logged-in user
     * @return the unit matching the id
     */
    @RequestMapping(path="/units/{unitId}", method = RequestMethod.GET)
    public Unit getUnitById(@PathVariable int unitId, Principal principal){
        Unit unit = null;
        try {
            unit = unitService.getUnitById(unitId, userDao.getUserIdByUsername(principal.getName()));
            if (unit == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find a unit with that id");
            }
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user with this username");
        }
        return unit;
    }

    /**
     * deletes a unit
     * @param unitId the unit id
     * @param principal the logged-in user
     */
    @RequestMapping(path="/units/{unitId}", method = RequestMethod.DELETE)
    public void deleteUnit(@PathVariable int unitId,  @RequestParam(defaultValue = "true") boolean deleteItems, Principal principal){
        try {
            unitService.deleteUnit(unitId, userDao.getUserIdByUsername(principal.getName()), deleteItems);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user with this username");
        }
    }

    /**
     * Returns a list of injuries a unit can gain
     * @param unitId the id of the unit
     * @return the list of injuries the unit doesn't already have
     */
    @RequestMapping(path="/units/{unitId}/injuries", method = RequestMethod.GET)
    public List<Skill> getNewInjuries(@PathVariable int unitId, Principal principal){
        List<Skill> injuries = null;
        try {
            injuries = unitService.getPotentialInjuries(unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return injuries;
    }


}
