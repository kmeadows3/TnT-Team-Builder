package my.TNTBuilder.model;

import my.TNTBuilder.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;

public class InjuryTests {
    private Injury sut;

    @Before
    public void setSut(){
        sut = new Injury(1, "Name", "Description", false, null, false, true, 1, null);
    }

    @Test
    public void setCount_correctly_sets_count_for_valid_change() throws ValidationException{
        sut.setCount(2);
    }

    @Test (expected = ValidationException.class)
    public void setCount_throws_exception_when_count_reduced() throws ValidationException{
        sut.setCount(0);
    }

    @Test(expected = ValidationException.class)
    public void setCount_throws_exception_when_count_increased_on_unstackable_injury() throws ValidationException{
        sut.setStackable(false);
        sut.setCount(2);
    }

}
