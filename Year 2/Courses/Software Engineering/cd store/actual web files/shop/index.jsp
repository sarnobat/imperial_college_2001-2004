<%@ page language="java" import="java.sql.*,java.lang.Math"%>

<%
//        
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


<table width="70%" align="center">
<tr>
<%

		int userid;		

		if(request.getParameter("shopperid") == null)
		{
			userid = (int) (Math.random() * 1000000000);
		}
		else
		{
			userid = Integer.parseInt(request.getParameter("shopperid"));
		}

		

		ResultSet all_categories = allRecsStatement.executeQuery("SELECT DISTINCT category FROM cdstemp5;");
		while ( all_categories.next() )
		{
			String category = all_categories.getString("category");
			out.println("<td align=\"middle\" width=\"33%\"><b><a href=\"category.jsp?category="  + category + "&shopperid=" + userid + "\">" + category + "</a></b></td>");
		}
		


%>
</tr>
</table>
<br>
<br>
<br>
<%= "<div align=center><a href=\"cart.jsp?qty=0&id=1&shopperid=" + userid + "\">View cart</div>" %>


<%@ include file="footer.html" %> 
<%	conn.close();	%>