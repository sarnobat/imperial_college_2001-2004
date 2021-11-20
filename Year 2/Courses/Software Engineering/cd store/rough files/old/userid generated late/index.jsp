<%@ page language="java" import="java.sql.*" %>

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

<%

		ResultSet all_categories = allRecsStatement.executeQuery("SELECT DISTINCT category FROM cdstemp4;");
		while ( all_categories.next() ) {
		String category = all_categories.getString("category");
		out.println("<td align=center bgcolor=\"#0033CC\"><a href=\"category.jsp?category="  + category + "\"><font color=\"C0C0C0\">"+ category + "</font></a></td>");//+ "&shopperid=" + userid 
		}
		

%>

<%@ include file="bluebar.html" %> 
<%@ include file="footer.html" %> 
<%	conn.close();	%>