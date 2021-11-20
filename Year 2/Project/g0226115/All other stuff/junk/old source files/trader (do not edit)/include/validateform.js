function validateForm(form,mode){
	if(form.username.value == ""){
		alert("Please choose a user name");
		return false;
	}
	else if(form.password.value == ""){
		alert("Please choose a password");
		return false;
	}
	else if(mode==2 && (form.confirm.value == "")){
		alert("Please confirm password");
		return false;
	}
	else if(mode==2 && (form.password.value != form.confirm.value)){
		alert("Passwords do not match");
		return false;
	}
	return true;
}

function validateShipName(form){
	if(form.shipname.value == ""){
		alert("Please choose a ship name");
		return false;
	}
	return true;
}

function validateTrade(form){
	alert("tHIS IS AN ALERT"); 
	if(form.sale0.value == '5'){
		alert("Value is 5");
		return false;
	}
	return true;
}