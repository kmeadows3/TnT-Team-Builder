package my.TNTBuilder.controller;

import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class UnitController {
    private UnitService unitService;
    private UserDao userDao;

    public UnitController(UnitService unitService, UserDao userDao){
        this.unitService = unitService;
        this.userDao = userDao;
    }

    @RequestMapping(path="/unit", method = RequestMethod.POST)
    public Unit newUnit(@Valid @RequestBody Unit unit, Principal principal){
        int userId = userDao.getUserIdByUsername(principal.getName());
        Unit newUnit = null;
        try{
            newUnit = unitService.createNewUnit(unit, userId);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        if (newUnit == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to create unit");
        }
        return newUnit;
    }

}
