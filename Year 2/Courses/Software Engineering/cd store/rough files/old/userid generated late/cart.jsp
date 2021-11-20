<%@ page language="java" import="java.sql.*,java.lang.Math"%>
<%
	/*******************************
	 **	catalogue record set	**
	 *******************************/
	Class.forName("org.postgresql.Driver");
       
	Connection conn = DriverManager.getConnection (
				"jdbc:postgresql://db/ss401",
				"ss401", "enosauntwo" );

	Statement allRecsStatement = conn.createStatement();
	ResultSet allCds = allRecsStatement.executeQuery("SELECT * FROM cdstemp4;");


	/*******************************
	 **	baskets record set	**
	 *******************************/
      
	//Statement allRecsStatement2 = conn.createStatement();
	ResultSet allBaskets = allRecsStatement.executeQuery("SELECT * FROM baskets");

	//out.println("<h3>" + allBaskets.getFetchSize() +"</h3>");
		
%>
<%

	String userid = request.getParameter("shopperid");
	if(userid == null)
	{
		int userid2 = (int) (Math.random() * 1000000000);
		int status = allRecsStatement.executeUpdate("INSERT INTO baskets (custid) VALUES (" + userid2 +")");

		out.println("<h2>" + status + "</h2>");
		out.println("<h1>" + userid2 + "</h1>");

		userid = String.valueOf(userid2);
		
		
	}

	int status2 = allRecsStatement.executeUpdate(	"UPDATE baskets SET qty" +
								request.getParameter("id") +
								" = " +
								request.getParameter("qty") +
								" WHERE custid = " +
								userid +";"	);//request.getParameter("shopperid")



	

	out.println("<h2>" + status2 + "</h2>");

%>
<%@ include file="header.html" %> 
<%@ include file="bluebar.html" %> 				


<%	conn.close();	%>
<%@ include file="footer.html" %>

