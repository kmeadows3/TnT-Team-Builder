package my.TNTBuilder.dao;

import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Unit;

import java.util.List;


public interface UnitDao {

    Unit getUnitById(int id, int userId);

    Unit createUnit(Unit newUnit);

    int getFactionIdByUnitId(int unitId);

    List<Unit> getListOfUnitsByFactionId(int factionId);

    List<Unit> getAllUnitsForTeam(int teamId);

    void updateUnit(Unit updatedUnit);

    Unit convertReferenceUnitToUnit(int referenceUnitId);

    Unit convertReferenceUnitToUnit(String unitClass);

    List<Skill> getPotentialSkills(int unitId);

    void addSkillToUnit(int skillId, int unitId);
}
