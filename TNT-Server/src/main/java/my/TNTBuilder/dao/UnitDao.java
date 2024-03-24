package my.TNTBuilder.dao;

import my.TNTBuilder.model.Unit;



public interface UnitDao {

    Unit getUnitById(int id);

    Unit createUnit(Unit newUnit);
}
