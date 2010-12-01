import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BaseUnitTest extends UnitTest {

	@Before
    public void setup() {
        Fixtures.deleteAll();
    }


	@Test
	public void dummyTest() {

	}
}