<%@ page language="java" import="java.sql.*"%>
<%
        
 	    Class.forName("org.postgresql.Driver");
        
	    Connection conn = DriverManager.getConnection (
	    	"jdbc:postgresql://db/ss401",
		"ss401", " osauntwo" );

            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT * FROM cdstemp;");
	    
            while ( rs.next() ) {
                out.println(rs.getString("name"));
            }
	        conn.close();
        
%>