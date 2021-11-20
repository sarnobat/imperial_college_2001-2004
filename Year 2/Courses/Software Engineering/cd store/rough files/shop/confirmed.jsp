<%@ page language="java" import="java.sql.*,java.lang.Math"%>

<%
        
		Class.forName("org.postgresql.Driver");
        
		/* Connecting to database ss401*/
		Connection conn = DriverManager.getConnection (
	    					"jdbc:postgresql://db/ss401",
							"ss401", "enosauntwo" );
		
		/* Query the relevant table*/
		Statement allRecsStatement = conn.createStatement();
		ResultSet allCds = allRecsStatement.executeQuery("SELECT * FROM cdstemp4;");
		ResultSet allBaskets = allRecsStatement.executeQuery("SELECT * FROM baskets");

%>
<%@ include file="header.html" %> 
<%@ include file="bluebar.html" %> 

<%	
/*						out.println("<h1>");

						out.println(request.getParameter("shopperid") +
						request.getParameter("name")		+ "," + 
						request.getParameter("address") 	+ "," + 
						request.getParameter("phone") 	+ "," +
						request.getParameter("delivery") 	+ "," +
						request.getParameter("cardtype")	+ "," +
						request.getParameter("cardno") 	+ "," +
						request.getParameter("expmoth")	+ "," +
						request.getParameter("expyear")	+ "</h1>");
*/
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

	//int status = allRecsStatement.executeUpdate("INSERT INTO orders (telephone) VALUES ("  + request.getParameter("phone") + ");");
	out.println("<h1>" + status + "</h1>");
%>

<%@ include file="footer.html" %> 
<%	conn.close();	%>