import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Spotify_API {
    String tokenValue;
    String userId;
    int playlistCount;
    String playlistID;

    @BeforeMethod
    public void setUp() {
        tokenValue = "Bearer BQCVI-TD3AG_VFe2aStkJEyZxpdtVWmBoaxZw2u40NS6MygcLjgNBVBq088rwy1Hklbt7Sw6r28kg_E2YD4N3xCk_jb56HW3uU5B2L4o1Apg9n11b4N1ZhXpUdrN1StOlysnJcPZYcjcIU_IzSySTxui2fbU00oKkBCfr0mDidHkOD5sIZtnKzdUrI5-lHyxZPqpWguoGa0O_350f72I67MQ_v4Y3BaZdV-kuHhyXrXcHvojleJ8VTm74HKyv6ALBxv99QsezYsrBTJLvgrX2mxn7HOrew\"";
    }

    @Test
    public void spotify_RestAssured_Automation() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        response.then().assertThat().statusCode(200);

        //Verifying the user id
        Response response1 = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue).when().get("https://api.spotify.com/v1/me").then().
                        extract().response();
        userId = response1.path("id");
        System.out.println(userId);
        response.then().assertThat().body("id", Matchers.equalTo(userId));

        //Finding the count of playlist from the response
        Response response2 = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue).when().get("https://api.spotify.com/v1/me/playlists").then().
                        extract().response();
        playlistCount = response2.path("total");

        playlistID = response2.path("items[0].id");

        System.out.println("Playlist count: "+playlistCount);
        System.out.println("Playlist ID: "+playlistID);

        //Creating the playlist
        Response response4 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .body("{\"name\":\"DDLJ SONGS\",\"description\":\"OLD MOVIE SONGS\",\"public\":\"false\"}")
                .when()
                .post("https://api.spotify.com/v1/users/userId/playlists");
        response.prettyPrint();
        response.then().assertThat().statusCode(200);

        //Updating playlist information
        Response response5 =RestAssured. given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization",tokenValue)
                .body("{\"name\":\"Marathi songs\",\"description\":\"songs are good\",\"public\":false}")
                .when()
                .put("https://api.spotify.com/v1/playlists/"+playlistID)
                .then()
                .extract().response();
        response5.prettyPrint();
    }
}

