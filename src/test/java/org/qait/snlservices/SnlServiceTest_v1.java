package org.qait.snlservices;


import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class SnlServiceTest_v1 {
	
	String b_id,boardID,name,player_id,p_id,pid,player_name;
	
	@BeforeTest
	public void setBaseURL()
	{
		RestAssured.baseURI="http://10.0.1.86/snl/";
		
		}
	/**
	 * this will return list of boards
	 */
	
	@Test(priority = 1)
	public void list_of_boards()
	{
		Response res= RestAssured.given().param("response.status","1").when().get("/rest/v1/board.json");
		Assert.assertEquals (res.statusCode (), 200);
	}
	/**
	 * creating new board
	 */
	
	@Test(priority = 2)
	public void create_new_board()
	{
		Response res= RestAssured.given().param("response.status","1").when().get("/rest/v1/board/new.json");		
		Assert.assertEquals (res.statusCode (), 200);
	
		JsonPath content = RestAssured.given().
				    when().
				        get("http://10.0.1.86/snl/rest/v1/board/new.json").
				    then().
				        extract().jsonPath();
		 b_id=content.getString("response.board.id");
		 boardID=b_id.concat(".json");
	     System.out.println(b_id);
		} 
	
	/**
	 * display board of a particular id
	 */
	@Test(priority = 3)
	public void testing_id_get()
	{
		Response res= RestAssured.given().param("response.status","1").when().get("/rest/v1/board/{id}",boardID);
		Assert.assertEquals (res.statusCode (), 200);
		RestAssured.given().param("response.board.players"," ").when().get("/rest/v1/board/{id}",boardID).then().statusCode(200);
	} 
	
	@Test(priority = 9)
	public void testing_id_put()
	{
		RestAssured.given().put("/rest/v1/board/{id}",boardID).then().statusCode(200);
		RestAssured.given().param("response.board.players"," ").when().put("/rest/v1/board/{id}",boardID).then().statusCode(200);
		
	}
	
	@Test(priority = 4)
	public void test_players_post()
	{
	   /* Creating json object which need to be pass in body()
	    * */
		JSONObject ob = new JSONObject();
		ob.put("board", b_id);
	    JSONObject ob1 = new JSONObject();
		ob1.put("name", "abc");
		ob.put("player", ob1);
	
		Response res=RestAssured.given().body(ob).when().post("/rest/v1/player.json");
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertNotNull(res);
		
		/*Fetching player id after post method
		 * */
		JsonPath jpath= res.then().extract().jsonPath();
		p_id=jpath.getString("response.player.id");
		
	    /*Fetching player id after GET mehod
	     * */
	
		JsonPath content = RestAssured.given().when().
			      get("http://10.0.1.86/snl/rest/v1/board/{id}",boardID).
			    then().
			        extract().jsonPath();
	    player_id =content.getString("response.board.players.id");
	
	}
	
	/**
	 * Getting details of player
	 */
	
	
	@Test(priority = 5)
   public void test_players_id_get()
	{
		
		pid=p_id.concat(".json");
		Response res= RestAssured.given().when().get("/rest/v1/player/{id}",pid).then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);

	}
	
	/**
	 * This updates player details (name)
	 */
	
	
	@Test(priority = 6)
	   public void test_players_id_put()
	   {
		/**
		 * Creating a json object which need to be pass in body()
		 */
		
		JSONObject jo1=new JSONObject();
        JSONObject jo2=new JSONObject();
        jo2.put("name", "priya");
		jo1.put("player", jo2);
		
		Response res= RestAssured.given().body(jo1).when().put("/rest/v1/player/{id}",pid).then().extract().response();
		
		/**
		 * Fetching updated player's name
		 */
		
		JsonPath jpath= res.then().extract().jsonPath();
		player_name=jpath.getString("response.player.name");
		
		
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(player_name, "priya");
		
	   }
	
	/**
	 * Deleting player
	 */
	@Test(priority = 8)
	   public void test_players_id_delete()
	 {
		
		Response res= RestAssured.given().when().delete("/rest/v1/player/{id}",pid).then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);
		
	   }
	
	@Test(priority = 7)
	   public void test_roll_dice_get()
	   {
		
		
		RestAssured.given().parameters("response.player.id", p_id, "response.player.board_id",boardID,"response.player.name",name)
		.when().get("/rest/v1/move/{boardid}?player_id={player_id}",boardID,p_id).then().assertThat().statusCode(200);
		
		
	   }
		

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
