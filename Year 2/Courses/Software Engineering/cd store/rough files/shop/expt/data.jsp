<%@ page language="java" import="java.sql.*"%>
<head>
<title>Sridhar's attempt to connect to a database</title>
</head>
<body bgcolor="white">
<%
	
		Class.forName("org.postgresql.Driver");
	
	
	
		Connection co  = 
			DriverManager.getConnection ("jdbc:postgresql://db/cdstemp",
										 "ss401",
										 "enosauntwo" );

        Statement stmt = conn.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT * FROM cdstemp");
			
		while ( rs.next() )
		{		    
			out.println("<tr><td>"+ rs.getString("name") +"</td>" );
        }
		
        conn.close();
	
%>
</body>
</html>