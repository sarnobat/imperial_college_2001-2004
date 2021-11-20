<%@ page language="java" import="java.sql.*"%>
<%
	Class.forName("org.postgresql.Driver");
       
	Connection conn = DriverManager.getConnection (
				"jdbc:postgresql://db/ss401",
				"ss401", "enosauntwo" );

	Statement allRecsStatment = conn.createStatement();
	ResultSet allCds = allRecsStatment.executeQuery("SELECT * FROM cdstemp4;");
%>
<%	conn.close();	%>
<%@ include file="header.html" %> 
<%@ include file="bluebar.html" %> 				


<%
	String category  = (request.getParameter("category")).trim(); 

	while ( allCds.next() )
	{
		
		if(    ((allCds.getString("category")).trim()).equals(category)      )
		{
			out.println("<h2><font color=\"#C0C0C0\">" + allCds.getString("name") + "</h2>");
			String productId = allCds.getString("id");
			out.println("<a href=\"cart.jsp?id=" + productId  + "&qty=" + "1" + "\">" + "Buy" + "</a>");//+ "&shopperid=" + request.getParameter("shopperid")
			out.println("<hr width=\"75%\">");
		}
	}
%>


<%@ include file="footer.html" %>


