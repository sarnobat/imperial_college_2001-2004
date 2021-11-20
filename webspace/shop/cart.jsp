<%@ page language="java" import="java.sql.*,java.lang.Math,java.text.*"%>
<%
	/*******************************
	 **	catalogue record set	**
	 *******************************/
	Class.forName("org.postgresql.Driver");
       
	Connection conn = DriverManager.getConnection (
				"jdbc:postgresql://db/ss401",
				"ss401", "enosauntwo" );

	Statement allRecsStatement = conn.createStatement();
	ResultSet allCds = allRecsStatement.executeQuery("SELECT * FROM cdstemp5;");


	/*******************************
	 **	baskets record set	**
	 *******************************/
      
	
	ResultSet allBaskets = allRecsStatement.executeQuery("SELECT * FROM baskets");

		
%>

<%@ include file="header.html" %> 



<%

	String userid = request.getParameter("shopperid");

	boolean firstPurchase = true;
	
	while(allBaskets.next())
	{
		String nextuserid = allBaskets.getString("custid");
		if(userid.equals(nextuserid))
		{
			firstPurchase = false;
		}
	}


	
	if(firstPurchase)
	{
		int status = allRecsStatement.executeUpdate("INSERT INTO baskets (custid) VALUES (" + userid +")");
	}



	ResultSet cd_qty = allRecsStatement.executeQuery("SELECT qty" + request.getParameter("id") + " FROM baskets WHERE custid=" + userid+";");

	cd_qty.next();

	String cdQtyNum = cd_qty.getString("qty" + request.getParameter("id"));


	if(cdQtyNum == null)
	{
		int status2 = allRecsStatement.executeUpdate(	"UPDATE baskets SET qty" +
								request.getParameter("id") +
								" = " +
								request.getParameter("qty") +
								" WHERE custid = " +
								userid +";"	);
	}
	else
	{
		int temp = Integer.parseInt(request.getParameter("qty")) + Integer.parseInt(cdQtyNum);

		int status2 = allRecsStatement.executeUpdate(	"UPDATE baskets SET qty" +
								request.getParameter("id") +
								" = " +
								temp +
								" WHERE custid = " +
								userid +";"	);

	}



	ResultSet thisCustomer = allRecsStatement.executeQuery("SELECT * FROM baskets WHERE custid=" + userid);
%>




<table width=50% align=center cellpadding=5>
<tr>
<td width=60%><b>Product Description</b></td><td width=10% align=center><b>Quantity</b></td><td width=20% align=center><b>Unit Price</b></td><td width=10%></td>
</tr>
<%
	/* Print out the product desctiptions of the products this customer has ordered */
	int i=1;
	double subtotal = 0;
	thisCustomer.next();
	while(i<16)
	{
		String quantityString = thisCustomer.getString("qty"+ i);
		if(quantityString != null)
		{
			int qty = Integer.parseInt(quantityString);
			if(qty>0)
			{
				ResultSet cd = allRecsStatement.executeQuery("SELECT * FROM cdstemp5 WHERE id=" + i);
				cd.next();
				out.println("<tr><td>" + cd.getString("name") + " (" + cd.getString("author")  + ")" + "</td><td align=center>" + qty + "</td>");
				out.println("<td align=center>£" + cd.getString("price") + "</td>");
				out.println("<td><a href=\"cart.jsp?shopperid=" + userid + "&qty=-" + qty + "&id=" + i +"\">Remove</a></td></tr>");
				double price = Double.parseDouble(cd.getString("price"));
				subtotal +=  price * qty;

			}
		
		}
		i++;
	}


DecimalFormat formatter = new DecimalFormat("#,##0.00");
System.out.println(" aDouble : " + formatter.format(subtotal));



//	subtotal = (Math.round(subtotal*100.0))/100.0;
	out.println("</table><hr width=50% align=center><table width=50% align=center cellpadding=5><td align=right><b>Subtotal:\t£" +  formatter.format(subtotal) + "</b></td></table>");
%>
<%	conn.close();	%>


<p><br>
<div align=center>
<%=	"<a href=\"index.jsp?shopperid=" + userid + "\">Continue Shopping</a>"
%>
</div>
<br>
<br></p>







<p  align=center>
<form name="form1" method="get" action="confirmed.jsp" onSubmit="return validateForm(this)">
  <table width="75%" border="0">
    <tr> 
      <td><div align="right">Name </div></td>
      <td><input name="name" type="text" id="name" value=""></td>
    </tr>
    <tr> 
      <td><div align="right">Address</div></td>
      <td><textarea name="address" cols="30" rows="3"></textarea></td>
    </tr>
    <tr> 
      <td><div align="right">Telephone Number</div></td>
      <td><input name="phone" type="text" maxlength=16></td>
    </tr>
    <tr> 
      <td><div align="right">Email address</div></td>
      <td><input name="email" type="text" maxlength=50></td>
    </tr>
    <tr> 
      <td><div align="right">Delivery Type</div></td>
      <td><input type="radio" name="delivery" value="standard" checked>
        Standard Delivery<br> <input type="radio" name="delivery" value="express">
        Express Delivery</td>
    </tr>
    <tr> 
      <td><div align="right">Card Type</div></td>
      <td><select name="cardtype">
          <option>Visa</option>
          <option>Mastercard</option>
        </select></td>
    </tr>
    <tr> 
      <td><div align="right">Card Number</div></td>
      <td><input type="text" name="cardno" maxlength="16"></td>
    </tr>
    <tr> 
      <td><div align="right">Expiry Month / Year</div></td>
      <td><select name="expmonth" size=1>
          <option selected>January</option>
          <option>February</option>
          <option>March</option>
          <option>April</option>
          <option>May</option>
          <option>June</option>
          <option>July</option>
          <option>August</option>
          <option>September</option>
          <option>October</option>
          <option>November</option>
          <option>December</option>
        </select>
        / 
        <input type="text" name="expyear" maxlength="4" size=1 value="20"></td>
    </tr>
    <tr> 
      <td><div align="right"> 
          
        </div></td>
      <td><br><input name="submit" type=submit value="Place Order"><input name="reset" type=reset></td>
    </tr>
  </table>
  
<br>
    <input type=checkbox>
    Please send me promotions from selected third party companies and organizations
	<input type=hidden name=shopperid value= <%= userid %>
</form>

</p>













<%@ include file="footer.html" %>

<script language="Javascript">

<!--

function validateForm(form) {

    if (!checkString(form.name.value)){
        alert("input a valid name");
        return false;
    }
    if (!checkString(form.address.value)){
        alert("input a valid address");
        return false;
    }

    if (!checkNum(form.phone.value)){
        alert("input valid phone number with no spaces");
        return false;
    }

	if (!checkNum(form.cardno.value) || (form.cardno.value.length < 16) ){
		alert("input valid card number (16 digits)");
		return false;
	}

	if (!checkNum(form.expyear.value) || (form.expyear.value.length < 4) ){
		alert("input a valid expiry year (20**)");
		return false;
	}

}

function checkName(s){
	if (!s) return false;
	for (var i = 0; i < s.length; i++){   
		var c = s.charAt(i);
		if (!isLetter(c)) return false;
	} return true;
}
function isLetter (c){
    return ((c >= "a" && c <= "z")
         || (c >= "A" && c <= "Z"))
}
function isDigit(c){ return (c >= "0" && c <= "9") } 
function isChar(c){ return (c >= "!" && c<= ","); } 
function checkNum(s){
	if (!s) return false;
	for (var i = 0; i < s.length; i++){   
		var c = s.charAt(i);
		if (!isDigit(c)) return false;
	} return true;
}
function checkString(s){
	if (!s) return false;
	return true;
}


//-->

</script>