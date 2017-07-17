package org.qait.snlservices;


import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class SnlServiceTest_v2 {

	String b_id,boardID,p_id,player_id,player_name;
	
	@BeforeTest
	public void set_base_uri()
	{
	   RestAssured.baseURI="http://10.0.1.86/snl/";		
	}
	
	@Test(priority=1)
	public void get_listofboard()
	{
		  RestAssured.given().auth().preemptive().basic("su","root_pass").when(). get("/rest/v1/board.json").then(). assertThat(). statusCode(200);	
		  RestAssured.given().get("/rest/v2/board.json").then().statusCode(401);
	}
	
	@Test(priority = 2)
	public void create_new_board()
	{
		
		Response res= RestAssured.given().auth().preemptive(). basic("su","root_pass"). when(). get("/rest/v1/board/new.json").then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);
		JsonPath jsonPath = JsonPath.from(res.body().asString());
		b_id= jsonPath.getString("response.board.id");
		boardID=b_id.concat(".json");
	  
	}
	
	@Test(priority = 3)
	public void testing_id_get()
	{
        Response res= RestAssured.given().auth().preemptive(). basic("su","root_pass"). when(). get("/rest/v2/board/{id}" ,boardID).then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);
		
	} 
	
   	@Test(priority = 9)
	public void testing_id_put()
	{
		 Response res= RestAssured.given().auth().preemptive(). basic("su","root_pass"). when().put("/rest/v2/board/{id}" ,boardID).then().extract().response();
		 Assert.assertEquals (res.statusCode (), 200);
		 RestAssured.given().auth().preemptive().basic("su", "root_pass").param("response.board.players"," ").when().put("/rest/v2/board/{id}" ,boardID).then().extract().response().then().statusCode(200);
		 
	}
	

	@Test(priority = 4)
	public void test_players_post()
	{
		JSONObject ob = new JSONObject();
		ob.put("board", b_id);
	    JSONObject ob1 = new JSONObject();
		ob1.put("name", "abc");
		ob.put("player", ob1);
		
		
		 Response res= RestAssured.given().body(ob).auth().preemptive().basic("su", "root_pass").when().post("/rest/v2/player.json").then().extract().response();
		 Assert.assertEquals (res.statusCode (), 200);
		 Assert.assertNotNull(res);		 
		 
		 JsonPath jpath= res.then().extract().jsonPath();
         p_id=jpath.getString("response.player.id");
	    
     	JsonPath content = RestAssured.given().auth().preemptive().basic("su", "root_pass").when().
			      get("http://10.0.1.86/snl/rest/v2/board/{id}",boardID).
			    then().
			        extract().jsonPath();
	    player_id =content.getString("response.board.players.id");
	    String exactPlayerId= player_id.substring(player_id.indexOf("[")+1, player_id.indexOf("]"));
		
		Assert.assertEquals(p_id, exactPlayerId);
		
	}

	@Test(priority = 5)
   public void test_players_id_get()
   {
		 p_id =p_id.concat(".json");
		 Response res= RestAssured.given().auth().preemptive().basic("su", "root_pass").when().get("/rest/v2/player/{id}",p_id).then().extract().response();
		 Assert.assertEquals (res.statusCode (), 200);
		
   }
	
	@Test(priority = 6)
	   public void test_players_id_put()
	   {
		
		JSONObject jo1=new JSONObject();
        JSONObject jo2=new JSONObject();
        String newname = "priya";
        jo2.put("name", newname);
		jo1.put("player", jo2);
	 
		Response res= RestAssured.given().body(jo1).auth().preemptive().basic("su", "root_pass").when().put("/rest/v2/player/{id}",p_id).then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);
	 
		JsonPath jpath= res.then().extract().jsonPath();
		player_name=jpath.getString("response.player.name");
		
		Assert.assertEquals(player_name, newname);
		
	   }
	
	@Test(priority = 8)
	   public void test_players_id_delete()
	 {
		
		Response res= RestAssured.given().auth().preemptive().basic("su", "root_pass").when().delete("/rest/v2/player/{id}",p_id).then().extract().response();
		Assert.assertEquals (res.statusCode (), 200);
		
		
		JsonPath jpath= res.then().extract().jsonPath();
		String message= jpath.getString("response.success");
		Assert.assertEquals(message, "OK");
		
	
	   }
	
	
	
	@Test(priority = 7)
	   public void test_roll_dice_get()
	   {
		
		
		RestAssured.given().auth().preemptive().basic("su", "root_pass").parameters("response.player.id", p_id, "response.player.board_id",boardID,"response.player.name",player_name)
		.when().get("/rest/v2/move/{boardid}?player_id={player_id}",boardID,p_id).then().assertThat().statusCode(200);
	

		Response res= RestAssured.given().auth().preemptive().basic("su", "root_pass").param("response.status","1").when().get("/rest/v2/board/{id}",boardID);
		
		JsonPath jpath= res.then().extract().jsonPath();
		String position=jpath.getString("response.board.players.position");
		Assert.assertNotNull(position);
		
		
	   }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
