package com.qainfotech.tap.training.snl.api;


import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

  public class BoardTest {
	  Board board_obj;
  @BeforeMethod
  public void beforeTest() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
	  board_obj=new Board();
	  board_obj.registerPlayer("Priya");
	  board_obj.registerPlayer("Ankush");
	   }

  @AfterTest
  public void afterTest() {
	//  board_obj=null;
  }


  @Test
  public void deletePlayer() throws NoUserWithSuchUUIDException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException{
  //throw new RuntimeException("Test not implemented");
	
	  board_obj.registerPlayer("Mansi");
	  board_obj.registerPlayer("Karan");
	  
	
	 
      UUID u= (UUID) board_obj.getData().getJSONArray("players").getJSONObject(1).get("uuid");
      System.out.println("u");
	  board_obj.deletePlayer(u);
	  Assert.assertEquals(board_obj.getData().getJSONArray("players").length(),3);
	  
 }

  @Test
  public void registerPlayer() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
    //throw new RuntimeException("Test not implemented");
	
	  board_obj.registerPlayer("Mansi");
	  board_obj.registerPlayer("Yashi");
	  
	
	  
	  Assert.assertEquals(board_obj.getData().getJSONArray("players").length(), 4);
	  Assert.assertEquals(board_obj.getData().getJSONArray("players").getJSONObject(0).get("name"), "Priya");
	  Assert.assertEquals(board_obj.getData().getJSONArray("players").getJSONObject(2).get("name"), "Mansi");
  }
  
  
 
  @Test(expectedExceptions=PlayerExistsException.class)
  public void registerPlayer_playerexistexp() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, IOException, MaxPlayersReachedExeption 
  {
	  
	
	
  board_obj.registerPlayer("Priya");
  }
  
  @Test(expectedExceptions=NoUserWithSuchUUIDException.class)
  public void registerPlayer_nouserexp() throws NoUserWithSuchUUIDException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
  {
	
	  board_obj.registerPlayer("Mansi");
	  board_obj.registerPlayer("Shagufta");
	  
	  board_obj.deletePlayer(UUID.fromString("30c7cab8-80be-4542-9939-aeca5f7c9"));
	  
	  Assert.assertEquals(board_obj.getData().getJSONArray("players").length(), 4);
  
}
  
  @Test
  public void rollDice() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, JSONException, InvalidTurnException {
  // throw new RuntimeException("Test not implemented");
	  
	
	  board_obj.registerPlayer("Mansi");
	
	  int pos_i=board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");
	  System.out.println(+pos_i);
	  
	  UUID uid = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	  int dice=   board_obj.rollDice(uid).getInt("dice");
	  System.out.println(+dice);
	 
	  int pos_a = board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");
	  System.out.println( +pos_a);
	
	  int x=pos_i+dice;
	  
	  int target=board_obj.getData().getJSONArray("steps").getJSONObject(x).getInt("target");
	 
	  int pos_e= target;
	  System.out.println(+pos_e);
	  
	  Assert.assertEquals(pos_a, pos_e);
	  Assert.assertTrue(dice<=6);
	  
	 
	    
  	  }

  @Test
  public void rollDice_snakendladder() throws JSONException, FileNotFoundException, UnsupportedEncodingException, InvalidTurnException
  {
  
	  int pos_i=board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");  
	  System.out.println("pos_i"+pos_i);
	  UUID uid = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	  int dice = board_obj.rollDice(uid).getInt("dice");
	  
	  System.out.println("dice" +dice);
	  int pos_a = board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");
	 //System.out.println("pos_a"+pos_a);
	  int target=board_obj.getData().getJSONArray("steps").getJSONObject(dice).getInt("target");
	  int pos_e=pos_i+dice;
	  int pos_f=target;
	  int type=board_obj.getData().getJSONArray("steps").getJSONObject(pos_e).getInt("type");
	  
	  if(type==0)
	  {
		  Assert.assertEquals(pos_e, pos_f);
		  System.out.println(" nothing");
	  }
	  if(type==1)
	  {
		  Assert.assertTrue(pos_e>pos_f);
		  System.out.println("snake");
	  }
	  if(type==2)
	  {
		  Assert.assertTrue(pos_e<pos_f);
	  System.out.println("ladder");
	  }
	  
	  System.out.println("expected "+pos_e);
	  System.out.println("final "+pos_f);
  }

  @Test(expectedExceptions=GameInProgressException.class)
  public void rollDice_gameinprgexp() throws FileNotFoundException, UnsupportedEncodingException, IOException, JSONException, InvalidTurnException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption
  {
	//  throw new RuntimeException("Test not implemented");
      
	
    
	  board_obj.rollDice( (UUID) board_obj.getData().getJSONArray("players").getJSONObject(0).get("uuid"));
  
      board_obj.registerPlayer("avi");
  
   }

  @Test
  public void rollDice_turncheck() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException
  {
	  
	  UUID uuid1 = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	  UUID uuid2 = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(1).get("uuid");
	  
	  
	  board_obj.rollDice(uuid1);
	  int turn1=  board_obj.data.getInt("turn");
	  Assert.assertEquals(turn1,1);
	  
	  board_obj.rollDice(uuid2);
	  
	  int turn2=  board_obj.data.getInt("turn");
	  Assert.assertEquals(turn2, 0);
	  
  }
    
  @Test(expectedExceptions=InvalidTurnException.class)

  public void rollDice_invalidexp() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException
  {
	  board_obj.registerPlayer("Mansi");
	  UUID uid = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(1).get("uuid");
	  board_obj.rollDice(uid);
  }

  @Test(expectedExceptions=InvalidTurnException.class)
  public void rollDice_end_game() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException
  {
	  board_obj.data.getJSONArray("players").getJSONObject(0).put("position", 100);
	  UUID uid = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(1).get("uuid");
	  board_obj.rollDice(uid);
	  }
  
  @Test
  public void rollDice_invalidturn() throws JSONException, FileNotFoundException, UnsupportedEncodingException, InvalidTurnException
  {

	  UUID uid = (UUID) board_obj.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	  board_obj.getData().getJSONArray("players").getJSONObject(0).put("position", 97); 
	 int curr_pos= board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");
	  System.out.println(+curr_pos);
	  board_obj.rollDice(uid).put("dice", 5);
	  int new_pos= board_obj.getData().getJSONArray("players").getJSONObject(0).getInt("position");
	  System.out.println(+new_pos);
	Assert.assertEquals(new_pos,97);
  
 }
  }
