<%  @  Language = jscript  %>
<%
function JScriptDatabaseRead() {
	
	var DataConn, r, strOut = "";
	var sql = "SELECT exampleName FROM examples WHERE exampleLanguage = 'jscript' ORDER BY exampleName;";
	var cnstr = "";

	var vbCrLf = String.fromCharCode( 13, 10 );

	cnstr+= "Provider=Microsoft.Jet.OLEDB.4.0;";

	cnstr+= "Data Source=" + Server.Mappath("mydata.mdb");
	DataConn = new ActiveXObject("ADODB.Connection");

	// DataConn.Open(cnstr);

	DataConn.Open( Application("dbConn"), Application("dbUsr"), Application("dbPass") );

	r = DataConn.Execute(sql);

	while (!r.BOF & !r.EOF){
		strOut+= r(0) + "<BR>" + vbCrLf;
		r.MoveNext();
	} // end while


	/*
	   Close and free variables. Notice that there is 
	   no equivalent to the "set r = Nothing" statement
	   in jscript...
	*/
	r.Close();
	DataConn.Close();
	
	/*
	   The return Statement exits the function and returns a value.
	      In this case, return outputs the name of every example in
	      the ASP Emporium test database.
	*/
	return(strOut);
}
%>



















<% @LANGUAGE = JScript %>
<%
var DataConn=Server.CreateObject("ADODB.Connection");
DataConn.Provider="Microsoft.Jet.OLEDB.4.0";
DataConn.Open("c:\\progfacil\\lauro\\my_database.mdb");

var table=Server.CreateObject("ADODB.RecordSet");
table.Open("my_table", DataConn);

Response.Write("<TABLE Border=1 CellPadding=5>"); 
Response.Write("<TR>");
var i,total_rows;
total_rows=table.Fields.Count -1
for(i=0;i <= total_rows ; i++)
{ Response.Write("<TH>" +table.Fields(i).Name + "</TH>" ); };
Response.Write("</TR>");
while(!table.EOF){
Response.Write("<TR>");
for(i = 0; i <=total_rows; i++)
{Response.Write("<TD>" +table.Fields(i).Value+"</TD>");};
Response.Write("</TR>");
table.MoveNext;
};
Response.Write("</TABLE><br>");

table.Close;
table = null;
DataConn.Close;
DataConn = null;
%>
