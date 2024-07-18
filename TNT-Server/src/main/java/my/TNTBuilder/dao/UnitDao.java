package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Unit;

import java.util.List;


public interface UnitDao {

    /**
     * Retrieves a single unit from the database based on the id of the unit and user. This should throw an exception
     * if the user does not own the unit
     * @param id the id of the unit
     * @param userId the id of the logged-in user
     * @return the unit matching the id provided
     */
    Unit getUnitById(int id, int userId) throws DaoException;

    /**
     * Adds a unit to the database
     * @param newUnit the unit from which the data in the database will be drawn
     * @return the unit, updated with the new id in the database
     */
    Unit createUnit(Unit newUnit) throws DaoException;

    /**
     * retrieves the faction id of a reference unit
     * @param unitId the id of the reference unit
     * @return the faction id of the reference unit
     */
    int getFactionIdByUnitReferenceId(int unitId) throws DaoException;

    /**
     * Get the list of all possible units that can be bought by a faction
     * @param factionId the id of the faction
     * @return the list of units belonging to the faction
     */
    List<Unit> getListOfUnitsByFactionId(int factionId) throws DaoException;

    /**
     * Retrieves a list of all units that belong to a team
     * @param teamId the id of the team
     * @return the list of units
     */
    List<Unit> getAllUnitsForTeam(int teamId) throws DaoException;

    /**
     * Updates a unit in the database
     * @param updatedUnit the updated unit whose data will go in the database
     */
    void updateUnit(Unit updatedUnit) throws DaoException;

    /**
     * Initializes a new unit with the data from the matching reference unit, based on ID
     * @param referenceUnitId the id of the reference unit
     * @return the initialized unit
     */
    Unit convertReferenceUnitToUnit(int referenceUnitId) throws DaoException;

    /**
     * Initializes a new unit with the data from the matching reference unit, based on className
     * @param unitClass the id of the reference unit
     * @return the initialized unit
     */
    Unit convertReferenceUnitToUnit(String unitClass) throws DaoException;

    /**
     * Gets a list of all skills that can be purchased by a unit
     * @param unitId the id of the unit
     * @return a list of skills the unit can buy
     */
    List<Skill> getPotentialSkills(int unitId) throws DaoException;

    /**
     * Adds a new skill to the unit
     * @param skillId the id of the skill to be added
     * @param unitId the id of the unit who will have the skill
     */
    void addSkillToUnit(int skillId, int unitId) throws DaoException;

    void deleteUnit(Unit unit) throws DaoException;

    List<Skill> getPotentialInjuries(Unit unit) throws DaoException;
}
