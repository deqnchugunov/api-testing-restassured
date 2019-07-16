package tests;

import fixtures.BaseFixture;
import io.restassured.response.Response;
import models.Todo;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BaseTests extends BaseFixture {

    @Test
    public void getAllTodoItems() {
        /*
           {
            "id": 2,
            "name": "Feed the dog",
            "isComplete": false,
            "dateDue": "2019-12-30T00:00:00"
           }
        */
        Todo[] response = given()
            .header("canAccess", "true")
        .when()
            .get(todos)
        .then()
            .statusCode(200)
            .extract()
            .as(Todo[].class);
        // assert second Todos item from response
        Assert.assertEquals(response[1].id, 2);
        Assert.assertEquals(response[1].name, "Feed the dog");
        Assert.assertEquals(response[1].isComplete, "false");
        Assert.assertEquals(response[1].dateDue, "2019-12-30T00:00:00");
    }

    @Test
    public void GetSingleTodoItem() {
        /*
             {
              "id": 1,
              "name": "Walk the dog",
              "isComplete": false,
              "dateDue": "2019-12-31T00:00:00"
             }
        */
        given()
            .header("canAccess", "true")
        .when()
            .get(todos + "/1")
        .then()
            .body("id", equalTo(1))
            .body("name", equalTo("Walk the dog"))
            .body("isComplete", equalTo(false))
            .statusCode(200);
    }

    @Test
    public void PostTodoItemAddBodyAnonymous()
    {
        String requestBody = "{\"name\": \"name-001\",\"isComplete\": \"false\",\"dateDue\" : \"2019-10-10T00:00:00Z\"}";
        Response response =
            given().log().all()
                .header("canAccess", "true")
                .contentType("application/json")
                .body(requestBody)
            .when()
                .post(todos)
            .then()
                .body("name", equalTo("name-001"))
                .body("isComplete", equalTo(false))
                .body("dateDue", equalTo("2019-10-10T00:00:00Z"))
                .extract()
                .response();

        int newUserId = response.path("id");
        System.out.println(newUserId);

            given()
                .header("canAccess", "true")
            .when()
                .get(todos + "/" + newUserId)
            .then()
                .statusCode(200)
                .body("id", equalTo(newUserId))
                .body("name", equalTo("name-001"))
                .body("isComplete", equalTo(false))
                .statusCode(200);
    }

    @Test
    public void PostTodoItemClassType()
    {
        Todo todo = new Todo() {
            {
                id = 0;
                name = "name-002";
                isComplete = "false";
                dateDue = "2019-10-11T00:00:00Z";
            }
        };

        Todo response =
            given().log().all()
                .header("canAccess", "true")
                .contentType("application/json")
                .body(todo)
            .when()
                .post(todos)
            .then()
                .body("name", equalTo("name-002"))
                .body("isComplete", equalTo(false))
                .body("dateDue", equalTo("2019-10-11T00:00:00Z"))
                .extract()
                .as(Todo.class);

        int newUserId = response.getId();
        System.out.println(newUserId);

            given()
                .header("canAccess", "true")
            .when()
                .get(todos + "/" + newUserId)
            .then()
                .body("id", equalTo(newUserId))
                .body("name", equalTo("name-002"))
                .body("isComplete", equalTo(false))
                .statusCode(200);
    }

    @Test
    public void PostTodoItemGenericDeserialization()
    {
        Todo todo = new Todo() {
            {
                id = 0;
                name = "name-003";
                isComplete = "false";
                dateDue = "2019-10-11T00:00:00Z";
            }
        };

        Todo response =
            given().log().all()
                .header("canAccess", "true")
                .contentType("application/json")
                .body(todo)
            .when()
                .post(todos)
            .then()
                .body("name", equalTo("name-003"))
                .body("isComplete", equalTo(false))
                .body("dateDue", equalTo("2019-10-11T00:00:00Z"))
                .extract()
                .as(Todo.class);

        Assert.assertEquals(response.name, todo.name);
        Assert.assertEquals(response.isComplete, todo.isComplete);
        Assert.assertEquals(response.dateDue, todo.dateDue);
    }
}
