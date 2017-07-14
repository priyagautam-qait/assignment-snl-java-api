package org.qait.snlservices;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

public class SnlServiceTest_v2 {

	
	@BeforeTest
	public void set_base_uri()
	{
	
		RestAssured.baseURI="http://10.0.1.86/snl/";		
		
	}
	
	@Test
	public void get_listofboard()
	{
		  RestAssured.given().
	        auth().
	        preemptive().
	        basic("su","root_pass").
	    when().
	        get("/rest/v1/board.json").
	    then().
	        assertThat().
	        statusCode(200);	
		
		
		
	}
	
	
	
	
}
