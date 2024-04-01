package my.TNTBuilder.dao;

import my.TNTBuilder.model.Unit;

import java.util.List;


public interface UnitDao {

    Unit getUnitById(int id, int userId);

    Unit createUnit(Unit newUnit);

    int getFactionIdByUnitId(int unitId);

    List<Unit> getListOfUnitsByFactionId(int factionId);

    List<Unit> getAllUnitsForTeam(int teamId);
}
