package Setup;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class Base {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://www.magicbricks.com";
        RestAssured.basePath = ""; // IMPORTANT
        RestAssured.proxy = null;
    }
}
