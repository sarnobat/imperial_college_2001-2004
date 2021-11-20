<%@ page language="java" import="java.sql.*,java.lang.Math"%>

<%
        
		Class.forName("org.postgresql.Driver");
        
		/* Connecting to database ss401*/
		Connection conn = DriverManager.getConnection (
	    					"jdbc:postgresql://db/ss401",
							"ss401", "enosauntwo" );
		
		/* Query the relevant table*/
		Statement allRecsStatement = conn.createStatement();
		ResultSet allCds = allRecsStatement.executeQuery("SELECT * FROM cdstemp5;");
		ResultSet allBaskets = allRecsStatement.executeQuery("SELECT * FROM baskets");

%>
<%@ include file="header.html" %> 

<%	
	int status = allRecsStatement.executeUpdate("INSERT INTO orders (custid,name,address,telephone,deltype,cardtype,cardno,expmonth,expyear) VALUES (" 
						+ 
						request.getParameter("shopperid") 	+ "," +
						"'" + request.getParameter("name") 		+ "'," + 
						"'" + request.getParameter("address") 	+ "'," + 
						"'" + request.getParameter("phone") 	+ "'," +
						"'" + request.getParameter("delivery") 	+ "'," +
						"'" + request.getParameter("cardtype")	+ "'," +
						"'" + request.getParameter("cardno") 	+ "'," +
						"'" + request.getParameter("expmonth")	+ "'," +
						request.getParameter("expyear") 	+  ");"				);

%>

<p align=center>Order Confirmed. Thank you.<br>

<a href="index.jsp">Continue shopping</a></p>

<%@ include file="footer.html" %> 
<%	conn.close();	%>