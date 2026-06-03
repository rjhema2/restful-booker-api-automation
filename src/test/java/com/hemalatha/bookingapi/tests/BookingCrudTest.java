package com.hemalatha.bookingapi.tests;

import com.hemalatha.bookingapi.base.RequestSpecFactory;
import com.hemalatha.bookingapi.base.ResponseSpecFactory;
import com.hemalatha.bookingapi.endpoints.ApiEndPoints;
import com.hemalatha.bookingapi.models.Booking;
import com.hemalatha.bookingapi.models.BookingDates;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BookingCrudTest {

  private int bookingId;
  private Booking bookingPayload;

  @BeforeClass
  public void setup() {
    // Create booking payload
    BookingDates dates = new BookingDates();
    dates.setCheckin("2025-01-01");
    dates.setCheckout("2025-01-05");

    bookingPayload = new Booking();
    bookingPayload.setFirstname("Hemalatha");
    bookingPayload.setLastname("Jay");
    bookingPayload.setTotalprice(150);
    bookingPayload.setDepositpaid(true);
    bookingPayload.setBookingdates(dates);
    bookingPayload.setAdditionalneeds("Breakfast");
  }

  @Test(priority = 1, description = "Create a new booking")
  public void testCreateBooking() {
    Response response = RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .body(bookingPayload)
            .when()
            .post(ApiEndPoints.BOOKING)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(200))
            .extract().response();

    bookingId = response.jsonPath().getInt("bookingid");
    Assert.assertTrue(bookingId > 0, "Booking ID should be greater than 0");
    System.out.println("Created Booking ID: " + bookingId);
  }

  @Test(priority = 2, description = "Get booking and verify data persistence")
  public void testGetBookingAndVerifyPersistence() {
    Response response = RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .when()
            .get(ApiEndPoints.BOOKING + "/" + bookingId)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(200))
            .extract().response();

    Booking retrieved = response.as(Booking.class);

    // Data persistence check
    Assert.assertEquals(retrieved.getFirstname(), bookingPayload.getFirstname());
    Assert.assertEquals(retrieved.getLastname(), bookingPayload.getLastname());
    Assert.assertEquals(retrieved.getTotalprice(), bookingPayload.getTotalprice());
    Assert.assertEquals(retrieved.isDepositpaid(), bookingPayload.isDepositpaid());
    Assert.assertEquals(retrieved.getAdditionalneeds(), bookingPayload.getAdditionalneeds());
    System.out.println("Data persistence verified successfully!");
  }

  @Test(priority = 3, description = "Update booking with PUT")
  public void testUpdateBooking() {
    bookingPayload.setFirstname("Updated");
    bookingPayload.setTotalprice(200);

    Response response = RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .header("Cookie", "token=" + getAuthToken())
            .body(bookingPayload)
            .when()
            .put(ApiEndPoints.BOOKING + "/" + bookingId)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(200))
            .extract().response();

    Booking updated = response.as(Booking.class);
    Assert.assertEquals(updated.getFirstname(), "Updated");
    Assert.assertEquals(updated.getTotalprice(), 200);
    System.out.println("Booking updated successfully!");
  }

  @Test(priority = 4, description = "Partial update booking with PATCH")
  public void testPartialUpdateBooking() {
    String patchBody = "{\"firstname\": \"Patched\"}";

    Response response = RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .header("Cookie", "token=" + getAuthToken())
            .body(patchBody)
            .when()
            .patch(ApiEndPoints.BOOKING + "/" + bookingId)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(200))
            .extract().response();

    String updatedName = response.jsonPath().getString("firstname");
    Assert.assertEquals(updatedName, "Patched");
    System.out.println("Booking partially updated successfully!");
  }

  @Test(priority = 5, description = "Delete booking")
  public void testDeleteBooking() {
    RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .header("Cookie", "token=" + getAuthToken())
            .when()
            .delete(ApiEndPoints.BOOKING + "/" + bookingId)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(201));

    System.out.println("Booking deleted successfully!");
  }

  @Test(priority = 6, description = "Verify booking is deleted")
  public void testVerifyBookingDeleted() {
    RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .when()
            .get(ApiEndPoints.BOOKING + "/" + bookingId)
            .then()
            .spec(ResponseSpecFactory.getResponseSpec(404));

    System.out.println("Booking deletion verified - 404 returned as expected!");
  }

  private String getAuthToken() {
    String authBody = "{\"username\": \"admin\", \"password\": \"password123\"}";

    return RestAssured
            .given()
            .spec(RequestSpecFactory.getRequestSpec())
            .body(authBody)
            .when()
            .post(ApiEndPoints.AUTH)
            .then()
            .extract().response()
            .jsonPath().getString("token");
  }
}