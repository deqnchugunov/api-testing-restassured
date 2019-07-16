package fixtures;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public class BaseFixture {

    protected String todos;

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "http://100.80.104.56";
        RestAssured.port = 8088;
        todos = "/api/todo";
    }
}
