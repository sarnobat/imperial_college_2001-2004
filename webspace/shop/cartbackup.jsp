
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

<%@ include file="header.html" %> 
<%@ include file="bluebar.html" %> 				


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
	
	int status2 = allRecsStatement.executeUpdate(	"UPDATE baskets SET qty" +
								request.getParameter("id") +
								" = " +
								request.getParameter("qty") +
								" WHERE custid = " +
								userid +";"	);//request.getParameter("shopperid")
	out.println("<h2>" + status2 + "</h2>");

	ResultSet thisCustomer = allRecsStatement.executeQuery("SELECT * FROM baskets WHERE custid=" + userid);

	/* Get print out the product desctiptions of the products this customer hss ordered */
	int i=1;
	thisCustomer.next();
	while(i<16)
	{
		String quantityString = thisCustomer.getString("qty"+ i);
		//out.println(quantityString);
		if(quantityString != null)
		{
			//out.println(quantityString);
			int qty = Integer.parseInt(quantityString);
			out.println(thisCustomer.getString("qty"+ i));
			if(qty>0)
			{
				ResultSet cd = allRecsStatement.executeQuery("SELECT * FROM cdstemp4 WHERE id=" + i);
				cd.next();
				out.println("<h2>" + cd.getString("name") + " qty = " + qty + "</h2>");
				out.println("<h2>" + cd.getString("author") + "</h2>");

			}
		
		}
		i++;
	}

%>




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
          <input name="submit" type=submit>
        </div></td>
      <td><input name="reset" type=reset></td>
    </tr>
  </table>
  <p> 
    <input type=checkbox>
    Please send me junk mail</p>
	<input type=hidden name=shopperid value= <%= userid %>>
</form>


<%	conn.close();	%>
<%@ include file="footer.html" %>

