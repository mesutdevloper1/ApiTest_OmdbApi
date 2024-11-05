import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class OmdbApiTest {

    private static final String BASE_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "d09fbaa5"; // Kendi API anahtarınızı buraya ekleyin

    @Test
    public void testSearchAndFetchMovieById() {
        // Step 1: "Harry Potter" araması yapıp, ID'sini alıyoruz
        Response searchResponse = RestAssured.given()
                .queryParam("apikey", API_KEY)
                .queryParam("s", "Harry Potter")
                .when()
                .get(BASE_URL);

        // "Harry Potter and the Sorcerer's Stone" filminin ID'sini alıyoruz
        String movieId = searchResponse.jsonPath()
                .getString("Search.find { it.Title == 'Harry Potter and the Sorcerer's Stone' }.imdbID");

        // Film ID'sinin boş olmadığını doğruluyoruz
        assert movieId != null;

        // Step 2: Film ID ile film bilgilerini alıyoruz
        Response movieResponse = RestAssured.given()
                .queryParam("apikey", API_KEY)
                .queryParam("i", movieId)
                .when()
                .get(BASE_URL);

        // Gelen response içinde alanların kontrolü
        movieResponse.then()
                .statusCode(200)
                .body("Title", equalTo("Harry Potter and the Sorcerer's Stone"))
                .body("Year", notNullValue())
                .body("Released", notNullValue());
    }
}
