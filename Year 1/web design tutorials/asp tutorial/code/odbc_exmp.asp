<% Response.buffer = true %>
<html>
<head>
<title>Stardeveloper.com Database Tutorial</title>
</head>
<body>
<% 

DIM rs
Set rs = Server.CreateObject("ADODB.Connection")

objConn.ConnectionString = "Provider=Microsoft.Jet.OLEDB.4.0;Data Source=" & _
Server.MapPath ("D:\Sridhar\Atletico Madrid\Editable\Data\atletico_players.mdb
") & ";"

objConn.Open

While Not rs.EOF
Response.Write "ID : " & rs("id") & "<br>"
Response.Write "First Name : " & rs("first_name") & "<br>"
Response.Write "Last Name : " & rs("last_name") & "<br>"
Response.Write "<br>"
rs.MoveNext
Wend

rs.Close
Set rs = Nothing %>
</body>
</html>