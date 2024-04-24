package my.TNTBuilder.controller;

import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.service.UnitService;
import org.springframework.data.relational.core.sql.In;
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

    @RequestMapping(path="/units/{unitId}/skills", method = RequestMethod.GET)
    public List<Skill> getSkillsForPurchase(@PathVariable int unitId){
        List<Skill> skills = null;
        try {
            skills = unitService.getPotentialSkills(unitId);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return skills;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/units/{unitId}/skills", method = RequestMethod.POST)
    public void addSkillToUnit(@RequestBody Skill skill, @PathVariable int unitId, Principal principal){
        try {
            unitService.addSkillToUnit(skill.getId(), unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(path="/units/{unitId}", method = RequestMethod.GET)
    public Unit getUnit(@PathVariable int unitId, Principal principal){
        Unit unit = null;
        try {
            unit = unitService.getUnitById(unitId, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return unit;
    }


}
