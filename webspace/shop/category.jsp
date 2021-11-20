<%@ page language="java" import="java.sql.*"%>
<%
	Class.forName("org.postgresql.Driver");
       
	Connection conn = DriverManager.getConnection (
				"jdbc:postgresql://db/ss401",
				"ss401", "enosauntwo" );

	Statement allRecsStatment = conn.createStatement();
	ResultSet allCds = allRecsStatment.executeQuery("SELECT * FROM cdstemp5;");
%>
<%	conn.close();	%>
<%@ include file="header.html" %>			

<table width=50% align=center cellspacing=20>
<%
	String category  = (request.getParameter("category")).trim(); 


	
	int i=1;
	String userid="0";
	while ( allCds.next() )
	{
		userid = request.getParameter("shopperid");

		if(    ((allCds.getString("category")).trim()).equals(category)      )
		{
			String productId = allCds.getString("id");
		
			out.println("<tr><td>" + i + ". <font size=5><u>" + allCds.getString("name") + 
					"</u></font> - <font size=4>" + allCds.getString("author") + "</font>" +
					"<p></p><form name=\"form1\" method=\"get\" action=\"cart.jsp\">" +
					"<input type=\"text\" name=\"qty" +
					"\" value=1 size=2> <input type=\"submit\" value=\"Buy\" id=\"Buy\" size=2><input type=hidden name=id value=" + productId +
					"><input type=hidden name=shopperid value=" + userid + "></form></h2>" +
					"</td><td><img src=\"pics/" + productId + ".jpg\" width=130 height=130 align=right></td></tr>");
			i++;
		}
		
	}
//

%>
</table>
<br>
<br>
<br>
<%= "<div align=center><a href=\"cart.jsp?qty=0&id=1&shopperid=" + userid + "\">View cart</div>" %>
<br>
<%= "<div align=center><a href=\"index.jsp?shopperid=" + userid + "\">Continue Shopping</div>" %>
<%@ include file="footer.html" %>