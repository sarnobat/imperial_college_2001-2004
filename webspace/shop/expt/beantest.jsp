<HTML>
<BODY>
<%-- Create an instance of the bean --%>
<jsp:useBean id="b" class="dcwbeans.bean1"/>

<table>

<tr>
	<th>b.name is </th>
	<td><jsp:getProperty name="b" property="name"/> </td>
</tr>

<tr> 	<th>modifying b.name</th>
	<td><jsp:setProperty name="b" property="name" value="wobble"/> value changed</td>
</tr>

<tr>
	<th>b.name is now</th>
	<td><jsp:getProperty name="b" property="name"/> </td>
</tr>

</table>

</BODY>
</HTML>
