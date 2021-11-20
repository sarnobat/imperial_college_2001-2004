<%@ page language="java" import="java.sql.*,java.lang.Math"%>
<table>
  <tr>
    <td align="right">Username:</td>
    <td><%=handler.getUsernameField(20, "")%></td>
  </tr>
  <tr>
    <td align="right">Password:</td>
    <td><%=handler.getPasswordField(10, "")%></td>
  </tr>
  <tr>
    <td colspan="2"><input type="submit" value="Login"></td>
  </tr>
</table>


<%

    // this generates a HTML input tag like this:
    //   <input type="text" name="username" size="[value of length var]"
    //             maxlength="30" [value of js string] >
    public String getUsernameField(int length, String js) {
        return getForm().getTextfield(USERNAME, length, 30, js);
    }

    //   <input type="password" name="password" size="[value of length var]"
    //             maxlength="30" [value of js string] >
    public String getPasswordField(int length, String js) {
        return getForm().getPassword(PASSWORD, length, 30, js);
    }

	String username = getForm().getValue(USERNAME);
  String password = getForm().getValue(PASSWORD);

// Error states
    public boolean usernameIsBlank;
    public boolean passwordIsBlank;
    public boolean passwordIsInvalid;
    public boolean formIsValid = true;

  private void validateFormData() {
    username = getForm().getValue(USERNAME);
    String password = getForm().getValue(PASSWORD);

    if(Validator.isBlank(username)) {
        formIsValid = false;
	usernameIsBlank = true;
    }

    if(Validator.isBlank(password)) {
	formIsValid = false;
	passwordIsBlank = true;
    }

    if(formIsValid) {
        // try to authenticate user
	try {
	     MemberManager.getInstance().authenticate(username, password);
	}
	catch(PasswordInvalid e) {
	     passwordIsInvalid = true;
	     formIsValid = false;
	}
    }
  }

    public boolean process() {
        if(isPost()) {
            validateFormData();
    	    if(formIsValid) {
                ...
            }
        }
    }




%>