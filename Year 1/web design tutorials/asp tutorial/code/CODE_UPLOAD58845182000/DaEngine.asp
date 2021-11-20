<%@ LANGUAGE="VBSCRIPT" %>
<html>
<head>
<title>Da Search Results!</title>
</head>

<body>
<%

Dim SqlJunk


Set dbGlobalWeb = Server.CreateObject("ADODB.Connection")
dbGlobalWeb.Open("Employees")

SqlJunk = "SELECT * FROM Employees"

If Request.Form("TypeSearch") = "FirstName" Then
	SqlJunk = SqlJunk & " WHERE FirstName LIKE '%" & Request.Form("DaInBox") & "%'"
End If

If Request.Form("TypeSearch") = "LastName" Then	
	SqlJunk = SqlJunk & " WHERE LastName LIKE '%" & _
	    Request.Form("DaInBox") & "%'"
End If

If Request.Form("TypeSearch") = "Title" Then
	SqlJunk = SqlJunk & " WHERE Title LIKE '%" & _
	    Request.Form("DaInBox") & "%'"
End If

If Request.Form("TypeSearch") = "Division" Then
	SqlJunk = SqlJunk & " WHERE Division LIKE '%" & _
	    Request.Form("DaInBox") & "%'"
End If

If Request.Form("TypeSearch") = "Phone" Then
	SqlJunk = SqlJunk & " WHERE Phone LIKE '%" & _
	    Request.Form("DaInBox") & "%'"
End If

If Request.Form("TypeSearch") = "Email" Then
	SqlJunk = SqlJunk & " WHERE EMail LIKE '%" & Request.Form("DaInBox") & "%'"
End If

Set rsGlobalWeb = Server.CreateObject("ADODB.Recordset")
rsGlobalWeb.Open SqlJunk, dbGlobalWeb, 3
%>
<%
If rsGlobalWeb.BOF and rsGlobalWeb.EOF Then%>

<h2 align="center">We did not find a match!</h2>
<%Else%>


<%If Not rsGlobalWeb.BOF Then%>

<h2>Here are the results of your search:</h2>

<table BORDER="0" width="100%" cellpadding="3">
  <tr>
    <th bgcolor="#800000"><font face="Arial" color="#FFFFFF">Name </font></th>
    <th bgcolor="#800000"><font face="Arial" color="#FFFFFF">Title </font></th>
    <th bgcolor="#800000"><font face="Arial" color="#FFFFFF">Division </font></th>
    <th bgcolor="#800000"><font face="Arial" color="#FFFFFF">Phone </font></th>
    <th bgcolor="#800000"><font face="Arial" color="#FFFFFF">E-Mail </font></th>
  </tr>
<%
	Do While Not rsGlobalWeb.EOF
	%>
  <tr>
    <td><%=rsGlobalWeb("FirstName")%>&#32 
<%=rsGlobalWeb("LastName")%>
</td>
    <td><%=rsGlobalWeb("Title")%>
</td>
    <td><%=rsGlobalWeb("Division")%>
</td>
    <td><%=rsGlobalWeb("Phone")%>
</td>
    <td><a href="mailto:<%=rsGlobalWeb("Email")%>"><%=rsGlobalWeb("Email")%></a> </td>
  </tr>
<% rsGlobalWeb.MoveNext
	Loop
	%>
</table>
<%End If%>
<%End If%>
<%
rsGlobalWeb.Close
dbGlobalWeb.Close
%>
</body>
</html>
