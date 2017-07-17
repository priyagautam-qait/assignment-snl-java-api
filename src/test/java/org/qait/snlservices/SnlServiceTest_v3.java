package org.qait.snlservices;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class SnlServiceTest_v3 {
	String	accessToken ,b_id,boardID,p_id,player_id,player_name;
	@BeforeTest
	public void before_tasks()
	{
		String response = RestAssured.given().parameters("username","su","password","root_pass" ,"grant_type", "client_credentials",  "client_id",
				"a9516732bf53f83eb67c207f1f655e7696d44f7fef8e89e4471dfba16f50ff0d", "client_secret",
				"257de5446c48ee1a6f3a5d1345348ab821bf5968dd3015d116c6164a204a8415")
				.auth().preemptive()
				.basic("a9516732bf53f83eb67c207f1f655e7696d44f7fef8e89e4471dfba16f50ff0d",
						"257de5446c48ee1a6f3a5d1345348ab821bf5968dd3015d116c6164a204a8415")
				.when().post("http://10.0.1.86/snl/oauth/token").asString();
		
		
		 JsonPath jpath= new JsonPath(response);
		 
     	 accessToken = jpath.getString("access_token");

		RestAssured.baseURI = "http://10.0.1.86/snl";
		System.out.println(" "+accessToken);
	}
	@Test(priority = 1)
		public void get_listofboard()
		{
			 RestAssured.given().auth().oauth2(accessToken).when().get("/rest/v3/board.json").then().assertThat().statusCode(200);
			
		}
	@Test(priority = 2)	
	public void create_new_board()
	{
		Response res= RestAssured.given().auth().oauth2(accessToken).param("response.status","1").when().get("/rest/v3/board/new.json");		
		Assert.assertEquals (res.statusCode (), 200);
	
		JsonPath content = RestAssured.given().auth().oauth2(accessToken).
				    when().
				        get("http://10.0.1.86/snl/rest/v3/board/new.json").
				    then().
				        extract().jsonPath();
		 b_id=content.getString("response.board.id");
		 boardID=b_id.concat(".json");
	     System.out.println(b_id);
		} 
	@Test(priority = 3)
	public void testing_id_get()
	{
		Response res= RestAssured.given().auth().oauth2(accessToken).param("response.status","1").when().get("/rest/v3/board/{id}",boardID);
		Assert.assertEquals (res.statusCode (), 200);
		RestAssured.given().auth().oauth2(accessToken).param("response.board.players"," ").when().get("/rest/v3/board/{id}",boardID).then().statusCode(200);
	} 
	

	@Test(priority = 9)
	public void testing_id_put()
	{
		RestAssured.given().auth().oauth2(accessToken).put("/rest/v3/board/{id}",boardID).then().statusCode(200);
		RestAssured.given().auth().oauth2(accessToken).param("response.board.players"," ").when().put("/rest/v3/board/{id}",boardID).then().statusCode(200);
		
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
	
		Response res=RestAssured.given().auth().oauth2(accessToken).body(ob).when().post("/rest/v1/player.json");
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
	    String exactPlayerId= player_id.substring(player_id.indexOf("[")+1, player_id.indexOf("]"));
	    Assert.assertEquals(p_id, exactPlayerId); 
	}
	
	@Test(priority = 5)
	   public void test_players_id_get()
		{
			
			p_id=p_id.concat(".json");
			Response res= RestAssured.given().auth().oauth2(accessToken).when().get("/rest/v3/player/{id}",p_id).then().extract().response();
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
			
			Response res= RestAssured.given().auth().oauth2(accessToken).body(jo1).when().put("/rest/v1/player/{id}",p_id).then().extract().response();
			
			/**
			 * Fetching updated player's name
			 */
			
			JsonPath jpath= res.then().extract().jsonPath();
			player_name=jpath.getString("response.player.name");
			
			
			Assert.assertEquals (res.statusCode (), 200);
			Assert.assertEquals(player_name, "priya");
			
		   }
		
	
		@Test(priority = 8)
		   public void test_players_id_delete()
		 {
			
			Response res= RestAssured.given().auth().oauth2(accessToken).when().delete("/rest/v3/player/{id}",p_id).then().extract().response();
			Assert.assertEquals (res.statusCode (), 200);
			
			
			JsonPath jpath= res.then().extract().jsonPath();
			String message= jpath.getString("response.success");
			Assert.assertEquals(message, "OK");
			
		   }
		
		
		
		@Test(priority = 7)
		   public void test_roll_dice_get()
		   {
			
			
			RestAssured.given().auth().oauth2(accessToken).parameters("response.player.id", p_id, "response.player.board_id",boardID,"response.player.name",player_name)
			.when().get("/rest/v3/move/{boardid}?player_id={player_id}",boardID,p_id).then().assertThat().statusCode(200);
		

			Response res= RestAssured.given().auth().oauth2(accessToken).param("response.status","1").when().get("/rest/v3/board/{id}",boardID);
			
			JsonPath jpath= res.then().extract().jsonPath();
			String position=jpath.getString("response.board.players.position");
			Assert.assertNotNull(position);
			
			
			
		   }
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


