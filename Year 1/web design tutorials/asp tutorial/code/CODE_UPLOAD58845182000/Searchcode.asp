<html>

<head>
<meta name="GENERATOR" content="Microsoft FrontPage 3.0">
<title>SearchDaData!</title>
</head>

<body bgcolor="#FFFFFF">

<p><strong><font face="Arial"><big><big><font color="#FF0080"><big><big>The ASP Trenches</big></big></font>
</big></big>by, Glenn Cook</font></strong></p>

<p><font face="Arial"><strong><big><big>Want to Search?</big> Search no more!</big></strong>
&nbsp; <br>
It's here.&nbsp; Download, read, reread, test, scrutinize, edit, add, and improve.</font></p>

<p><font face="Arial">Here is the <a href="searchcode.zip">download file</a> that you will
need to get the most out of this tutorial.&nbsp; If you just want to download it, change
the data in the Access database, make a DSN called &quot;Employees,&quot; and use
it,....go for it!</font></p>

<p><font face="Arial">There are three files in the &quot;searchcode.zip&quot; file that
make this work: The database (employees.mdb); The HTML file that posts to the ASP file
(ASPSearch.htm); The ASP file that does all the work(DaEngine.asp). I have not documented
the Search.htm file because It just does a simple posting using a drop-down menu and an
input box.&nbsp; Although it's worth opening the other two files to understand the code
completely, I decided not to annotate the contents because there is nothing even the
slightest bit complex about them.</font></p>

<p><strong><big><font face="Arial" color="#408080"><big><big>ASPSearch!</big></big></font></big><br>
<font face="Arial"><font color="#800080">Purple= HTML Code</font><br>
Black= HTML Text<br>
<font color="#008000">Green= Server-side ASP code</font><br>
<font color="#FF0000">Red= My Comments</font></font></strong></p>

<table border="1" width="100%" cellpadding="3">
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000">&lt;</font><font SIZE="1"
    COLOR="#ff00ff">%</font><font SIZE="1" COLOR="#000000">@ LANGUAGE=&quot;VBSCRIPT&quot; </font><font
    SIZE="1" COLOR="#ff00ff">%</font><font SIZE="1" COLOR="#000000">&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">html</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">head</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">title</font>&gt;Da Search Results!&lt;<font SIZE="1"
    COLOR="#800080">/title</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">/head</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">body</font>&gt;</font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small>Here is the simple
    stuff!&nbsp; Line one tells the IIS server to expect some VBScript ahead and the rest of
    it just builds the HTML file that will be displayed.</small></font></td>
  </tr>
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000"></font><font color="#008000"><font
    SIZE="1">&lt;%</font><br>
    </font><font SIZE="1" color="#008000">Dim SqlJunk<br>
    <br>
    Set dbGlobalWeb = Server.CreateObject(&quot;ADODB.Connection&quot;)<br>
    dbGlobalWeb.Open(&quot;Employees&quot;)</font><font SIZE="1"><p><font color="#008000">SqlJunk
    = &quot;SELECT * FROM Employees&quot;</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;FirstName&quot;
    Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE FirstName LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp;_ <br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;LastName&quot;
    Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE LastName LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp; _<br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;Title&quot; Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE Title LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp;
    _<br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;Division&quot;
    Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE Division LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp; _<br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;Phone&quot; Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE Phone LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp; _<br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></p>
    <p><font color="#008000">If Request.Form(&quot;TypeSearch&quot;) = &quot;Email&quot; Then<br>
    SqlJunk = SqlJunk &amp; &quot; WHERE EMail LIKE '%&quot;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &amp;_ <br>
    Request.Form(&quot;DaInBox&quot;) &amp; &quot;%'&quot;<br>
    End If</font></font><font SIZE="1" COLOR="#000000"></font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small>We start off the
    ASP code by defining a variable called &quot;SqlJunk&quot; which I will use later on to
    build a string which I will plug into a SQL statement that defines the Recordset.</small></font><p><font
    face="Arial" color="#FF0000"><small>The next paragraph &quot;Set dbGlobalWeb....&quot;
    creates an ADODB connection.&nbsp; After I create an object, I have to open the database
    that I will be using.&nbsp; I tell it to open a connection I have called Employees.</small><br>
    <small><em><strong>Here is where you have to do a little server administration:</strong>
    Make a DSN through the ODBC icon in the Control Panel called &quot;Employees.&quot;
    &nbsp; If you're saying, &quot;huh?&quot; right now, just follow directions and you'll
    understands you go along.&nbsp; The DSN tab is the second tab from the left and you will
    need to &quot;Add&quot; a new DSN to an Access database. Find the Access MDB file I called
    &quot;Employees&quot; and point your &quot;Employees&quot; DSN to the
    &quot;employees.mdb&quot; file I have supplied for you. (By the way, you could have named
    your DSN &quot;KrustyTheClown&quot; and it would work just fine as long as you change the
    &quot;dbGlobalWeb.Open(&quot;Employees&quot;) to
    dbGlobalWeb.Open(&quot;KrustyTheClown&quot;))</em></small></font></p>
    <p><font face="Arial" color="#FF0000"><small>The next paragraph fills the
    variable(&quot;SqlJunk&quot;) with the first part of your SQL statement.&nbsp; The
    statement says, &quot;SELECT every field in the &quot;Employees&quot; table.&nbsp; (When I
    reread this I realized that many people might be getting confused here because I named my
    DSN &quot;Employees&quot;, my database &quot;Employees&quot; and the table within the
    database &quot;Employees.&quot;&nbsp; Force yourself to look at the source code closely to
    figure out which &quot;Employees&quot; I'm talking about.)</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>The next six &quot;If...Then&quot; statements
    could have been written as a &quot;Select...Case&quot;- oh well. Initially I only wrote it
    for two fields and then someone said, &quot;you should make it so they can search all the
    the fields so I just cut and pasted the rest.&nbsp; Hey, it works, alright?! (The
    &nbsp; &amp;_&nbsp; just continues the code on a new line like VB)</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>Anyway, the first line of every <strong>If</strong>
    statement <strong>Request</strong>s the <strong>Form</strong> field I call <strong>&quot;TypeSearch&quot;</strong>
    and asks if it's equal to a value- <strong>&quot;FirstName&quot;</strong>, <strong>&quot;LastName&quot;</strong>,<strong>&quot;Title&quot;</strong>,<strong>&quot;Division&quot;</strong>,
    etc.</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>The second line of every &quot;If&quot;
    statement says, &quot;If the above line was true, <strong>Then </strong>add the rest of
    the appropriate SQL string code onto the end of the existing &quot;SqlJunk&quot;
    string.&quot; The second half of my SQL statement that I'm sticking into the
    &quot;SqlJunk&quot; variable completes the SQL statement that I will use to find the
    records that match your search criteria. The SQL statement now says, &quot;<strong>SELECT</strong>/Find
    every record in the <strong>Employees</strong> table <strong>WHERE</strong> the <strong>&quot;such&amp;such&quot;</strong>
    field has data that is similar/<strong>LIKE</strong> the input data the user has put in
    the box called <strong>&quot;DaInBox.&quot; </strong>You'll notice that the
    Request.Form(&quot;DaInBox&quot;) is surrounded by </small><strong>&amp;</strong><small>,</small><strong>%</strong><small>,</small><strong>&quot;</strong><small>,</small><strong>'</strong><small>.
    &nbsp; Ok here's what they're for:&nbsp; The % is used by SQL as wildcards like DOS used
    the *.&nbsp; The &amp; is used to paste the two separate strings to make one long string
    in the SqlJunk variable.&nbsp; The ' holds the wildcards and the &quot; holds the
    statement.</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>Finally, each &quot;If&quot; statement
    concludes with an &quot;End If.&quot;</small></font></td>
  </tr>
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000"></font><font SIZE="1"
    color="#008000">Set rsGlobalWeb = Server.CreateObject(&quot;ADODB.Recordset&quot;)<br>
    rsGlobalWeb.Open SqlJunk, dbGlobalWeb, 3,3<br>
    %&gt;</font><font SIZE="1" COLOR="#000000"></font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small>Here is where I
    create the <strong>ADODB.Recordset</strong>!&nbsp; It seems like I've done a lot at this
    point but really all I've done is made a connection to a database and stick pieces of an
    SQL statement into a variable I call SqlJunk.&nbsp; In the process of defining what my
    recordset is going to be, I'm also going create and name my recordset
    variable(rsGlobalWeb)&nbsp; that I will use to extract the field data within my recordset.
    </small></font><p><font face="Arial" color="#FF0000"><small>The second line on this page
    defines the attributes of your <strong>ADODB.Recordset</strong> object that I have named <strong>rsGlobalWeb</strong>.
    This line says, &quot;Open the r<strong>sGlobalWeb</strong> recordset with the following
    SQL statement contained within the <strong>SqlJunk</strong> variable using the connection
    object we established earlier called <strong>dbGobalWeb</strong>.&quot;&nbsp; The 3 stuff
    just opens the database with &quot;optimistic&quot; options- meaning it allows multiple
    connections and assumes no cruddy people are going to try to hack your data.</small></font></td>
  </tr>
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000"></font><font color="#008000"><font
    SIZE="1">&lt;%</font> <font SIZE="1">If rsGlobalWeb.BOF and rsGlobalWeb.EOF Then</font> <font
    SIZE="1">%&gt;</font></font><font SIZE="1" COLOR="#000000"><br>
    &lt;<font SIZE="1" COLOR="#800080">h2 </font><font SIZE="1" COLOR="#ff0000">align=&quot;</font><font
    SIZE="1" COLOR="#0000ff">center</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;We
    did not find a match!&lt;<font SIZE="1" COLOR="#800080">/h2</font>&gt;<br>
    </font><font SIZE="1" color="#008000">&lt;%Else%&gt;</font><font SIZE="1" COLOR="#000000"></font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small> What this code
    says is, &quot;If the the recordset(<strong>rsGlobalWeb</strong>) is at the beginning(<strong>BOF</strong>)
    and end(<strong>EOF</strong>) of the recordset at the same time Then logically there is no
    recordset and logically there is no match!&nbsp; If there is something <strong>else, </strong>go
    to the next line.</small></font></td>
  </tr>
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000"></font><font color="#008000"
    SIZE="1">&lt;%If Not rsGlobalWeb.BOF Then%&gt;</font><font SIZE="1" COLOR="#000000"><p>&lt;</font><font
    SIZE="1" COLOR="#800080">h2</font><font SIZE="1" COLOR="#000000">&gt;Here are the results
    of your search:&lt;</font><font SIZE="1" COLOR="#800080">/h2</font><font SIZE="1"
    COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">table </font><font SIZE="1" COLOR="#ff0000">BORDER=&quot;</font><font
    SIZE="1" COLOR="#0000ff">0</font><font SIZE="1" COLOR="#ff0000">&quot; width=&quot;</font><font
    SIZE="1" COLOR="#0000ff">100%</font><font SIZE="1" COLOR="#ff0000">&quot;
    cellpadding=&quot;</font><font SIZE="1" COLOR="#0000ff">3</font><font SIZE="1"
    COLOR="#ff0000">&quot;</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">tr</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">th </font><font SIZE="1" COLOR="#ff0000">bgcolor=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#800000</font><font SIZE="1" COLOR="#ff0000">&quot;</font><font
    SIZE="1" COLOR="#000000">&gt;&lt;</font><font SIZE="1" COLOR="#800080">font </font><font
    SIZE="1" COLOR="#ff0000">face=&quot;</font><font SIZE="1" COLOR="#0000ff">Arial</font><font
    SIZE="1" COLOR="#ff0000">&quot; color=&quot;</font><font SIZE="1" COLOR="#0000ff">#FFFFFF</font><font
    SIZE="1" COLOR="#ff0000">&quot;</font><font SIZE="1" COLOR="#000000">&gt;Name &lt;</font><font
    SIZE="1" COLOR="#800080">/font</font><font SIZE="1" COLOR="#000000">&gt;&lt;</font><font
    SIZE="1" COLOR="#800080">/th</font><font SIZE="1" COLOR="#000000">&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">th </font><font SIZE="1" COLOR="#ff0000">bgcolor=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#800000</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;&lt;<font
    SIZE="1" COLOR="#800080">font </font><font SIZE="1" COLOR="#ff0000">face=&quot;</font><font
    SIZE="1" COLOR="#0000ff">Arial</font><font SIZE="1" COLOR="#ff0000">&quot; color=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#FFFFFF</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;Title
    &lt;<font SIZE="1" COLOR="#800080">/font</font>&gt;&lt;<font SIZE="1" COLOR="#800080">/th</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">th </font><font SIZE="1" COLOR="#ff0000">bgcolor=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#800000</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;&lt;<font
    SIZE="1" COLOR="#800080">font </font><font SIZE="1" COLOR="#ff0000">face=&quot;</font><font
    SIZE="1" COLOR="#0000ff">Arial</font><font SIZE="1" COLOR="#ff0000">&quot; color=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#FFFFFF</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;Division
    &lt;<font SIZE="1" COLOR="#800080">/font</font>&gt;&lt;<font SIZE="1" COLOR="#800080">/th</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">th </font><font SIZE="1" COLOR="#ff0000">bgcolor=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#800000</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;&lt;<font
    SIZE="1" COLOR="#800080">font </font><font SIZE="1" COLOR="#ff0000">face=&quot;</font><font
    SIZE="1" COLOR="#0000ff">Arial</font><font SIZE="1" COLOR="#ff0000">&quot; color=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#FFFFFF</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;Phone
    &lt;<font SIZE="1" COLOR="#800080">/font</font>&gt;&lt;<font SIZE="1" COLOR="#800080">/th</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">th </font><font SIZE="1" COLOR="#ff0000">bgcolor=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#800000</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;&lt;<font
    SIZE="1" COLOR="#800080">font </font><font SIZE="1" COLOR="#ff0000">face=&quot;</font><font
    SIZE="1" COLOR="#0000ff">Arial</font><font SIZE="1" COLOR="#ff0000">&quot; color=&quot;</font><font
    SIZE="1" COLOR="#0000ff">#FFFFFF</font><font SIZE="1" COLOR="#ff0000">&quot;</font>&gt;E-Mail
    &lt;<font SIZE="1" COLOR="#800080">/font</font>&gt;&lt;<font SIZE="1" COLOR="#800080">/th</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">/tr</font>&gt;<br>
    </font><font SIZE="1" color="#008000">&lt;% Do While Not rsGlobalWeb.EOF %&gt;</font><font
    SIZE="1" COLOR="#000000"><br>
    &lt;<font SIZE="1" COLOR="#800080">tr</font>&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">td</font><font SIZE="1" COLOR="#000000">&gt;</font><font
    color="#008000" SIZE="1">&lt;%=rsGlobalWeb(&quot;FirstName&quot;)%&gt;&amp;#32
    &lt;%=rsGlobalWeb(&quot;LastName&quot;)%&gt;</font><font SIZE="1" COLOR="#000000"></p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/td</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">td</font><font SIZE="1" COLOR="#000000">&gt;</font><font
    color="#008000" SIZE="1">&lt;%=rsGlobalWeb(&quot;Title&quot;)%&gt;</font><font SIZE="1"
    COLOR="#000000"></p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/td</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">td</font><font SIZE="1" COLOR="#000000">&gt;</font><font
    color="#008000" SIZE="1">&lt;%=rsGlobalWeb(&quot;Division&quot;)%&gt;</font><font SIZE="1"
    COLOR="#000000"></p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/td</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">td</font><font SIZE="1" COLOR="#000000">&gt;</font><font
    color="#008000" SIZE="1">&lt;%=rsGlobalWeb(&quot;Phone&quot;)%&gt;</font><font SIZE="1"
    COLOR="#000000"></p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/td</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">td</font><font SIZE="1" COLOR="#000000">&gt;&lt;</font><font
    SIZE="1" COLOR="#800080">a </font><font SIZE="1" COLOR="#ff0000">href=&quot;</font><font
    SIZE="1" COLOR="#0000ff">mailto:</font><font color="#008000" SIZE="1">&lt;%=rsGlobalWeb(&quot;Email&quot;)%&gt;&quot;&gt;&lt;%=rsGlobalWeb(&quot;Email&quot;)%&gt;</font><font
    SIZE="1" COLOR="#000000">&lt;</font><font SIZE="1" COLOR="#800080">/a</font><font SIZE="1"
    COLOR="#000000">&gt; &lt;</font><font SIZE="1" COLOR="#800080">/td</font><font SIZE="1"
    COLOR="#000000">&gt;</p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/tr</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p></font><font color="#008000" SIZE="1">&lt;% <br>
    rsGlobalWeb.MoveNext<br>
    Loop<br>
    %&gt;</font><font SIZE="1" COLOR="#000000"></p>
    <p>&lt;</font><font SIZE="1" COLOR="#800080">/table</font><font SIZE="1" COLOR="#000000">&gt;</p>
    <p></font><font SIZE="1"><font color="#008000">&lt;%End If%&gt;</font></p>
    <p><font color="#008000">&lt;%End If%&gt;</font></font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small>The first line here
    basically says, &quot;<strong>If</strong> the recordset is not at the <strong>BOF</strong>
    or &quot;Beginning Of File&quot; <strong>Then</strong> execute this HTML code which
    defines the header information for the query results.&quot;</small></font><p><font
    face="Arial" color="#FF0000"><small>We make a pretty table with field headers that
    corresponds to the fields in the database.</small></font></p>
    <p><font face="Arial" color="#FF0000"><small><strong>&lt;%Do While Not....%&gt;</strong>Now
    we tell it to execute the following code as long as we are not at the end of the
    recordset(EOF). Now the ASP code starts sticking records underneath the field headers and
    extracts the field data from the recordset until it reaches the end of the records it has
    found.&nbsp; I extract the field data within the database using <strong>&lt;%=rsGlobalWeb%&gt;</strong></small></font></p>
    <p><font face="Arial" color="#FF0000"><small>The<strong> Loop </strong>statement just
    tells ASP to back up to the <strong>Do While</strong> statement until it is finished.</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>&nbsp;</small></font></p>
    <p><font face="Arial" color="#FF0000"><small>Finally, remember to <strong>End</strong>
    your <strong>If</strong>'s.</small></font></td>
  </tr>
  <tr>
    <td width="51%" valign="top"><font SIZE="1" COLOR="#000000"></font><font color="#008000"><font
    SIZE="1">&lt;%</font><br>
    <font SIZE="1">rsGlobalWeb.Close<br>
    dbGlobalWeb.Close<br>
    <font SIZE="1">%</font>&gt;</font></font><font SIZE="1" COLOR="#000000"><br>
    &lt;<font SIZE="1" COLOR="#800080">/body</font>&gt;<br>
    &lt;<font SIZE="1" COLOR="#800080">/html</font>&gt;</font></td>
    <td width="49%" valign="top"><font face="Arial" color="#FF0000"><small>I close the
    recordset and the connection to the database and I finish the HTML coding for the results
    page. </small></font></td>
  </tr>
</table>

<p><small><font face="Arial">Questions, Suggestions, Ideas? <a
href="mailto:%22glenn@gwsinc.com%22">Email me!</a></font></small></p>

<p><a href="http://www.activeserverpages.com/glenncook/"><small><font face="Arial">Return
to main column page!</font></small></a></p>
</body>
</html>
