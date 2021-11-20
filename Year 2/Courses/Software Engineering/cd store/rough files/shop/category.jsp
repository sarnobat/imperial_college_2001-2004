
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
		String userid = request.getParameter("shopperid");

		if(    ((allCds.getString("category")).trim()).equals(category)      )
		{
			String productId = allCds.getString("id");
			/*out.print("id=" + productId+
						);*/
			out.println("<h2><font color=\"#C0C0C0\">" + allCds.getString("name") + "</h2>");
			

			out.println("<h1>" + userid + "</h1>");
			out.println("	<form name=\"form1\" method=\"get\" action=\"cart.jsp\">"
						);

			/*out.println("<a href=\"cart.jsp?id=" + productId  + "&qty=" + "1" + "&shopperid=" + userid + "\">" + "Buy" + "</a>");//+ "&shopperid=" + userid
*/
			out.println("	<input type=\"text\" name=\"qty"
						+
						//productId
						//+ 
						"\" value=1><input type=\"submit\" value=\"Buy\" id=\"Buy\"><input type=hidden name=id value=" + productId + "><input type=hidden name=shopperid value=" + userid + ">");
			out.println("</form>");
			out.println("<hr width=\"75%\">");
		}
	}

%>


<%@ include file="footer.html" %>


/*
?shopperid="
						+
						userid
/