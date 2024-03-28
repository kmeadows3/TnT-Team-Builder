package my.TNTBuilder.dao;

import my.TNTBuilder.model.Unit;



public interface UnitDao {

    Unit getUnitById(int id, int userId);

    Unit createUnit(Unit newUnit);

    int getFactionIdByUnitId(int unitId);
}
