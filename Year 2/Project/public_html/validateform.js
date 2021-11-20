/* Login and Register check */
function validateForm(form, mode){
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
		alert("Passwords do not match\nPlease re-choose password");
		form.password.value = "";
		form.confirm.value = "";
		form.password.focus();
		return false;
	}
	else if(mode==2 && (!isvalidemail(form.email.value))){
		alert("Please enter a valid email address");
		return false;
	}

	return true;
}

function validateShipName(form){
	if(form.shipname.value == ""){
		alert("Please choose a ship name");
		form.shipname.focus();
		return false;
	}
	return true;
}

function validatepasswordrequest(form){
	if(!isvalidemail(form.email.value)){
		alert("Please enter a valid email address");
		return false;
	}

	return true;
}

function isvalidemail(address){
	if (address.indexOf('@') < 1) return false;
	var name = address.substring(0, address.indexOf('@'));
	var domain = address.substring(address.indexOf('@') + 1);
	if (name.indexOf('(') != -1 || name.indexOf(')') != -1 || name.indexOf('<') != -1 || name.indexOf('>') != -1 || name.indexOf(',') != -1 || name.indexOf(';') != -1 || name.indexOf(':') != -1 || name.indexOf('\\') != -1 || name.indexOf('"') != -1 || name.indexOf('[') != -1 || name.indexOf(']') != -1 || name.indexOf(' ') != -1) return false;
	if (domain.indexOf('(') != -1 || domain.indexOf(')') != -1 || domain.indexOf('<') != -1 || domain.indexOf('>') != -1 || domain.indexOf(',') != -1 || domain.indexOf(';') != -1 || domain.indexOf(':') != -1 || domain.indexOf('\\') != -1 || domain.indexOf('"') != -1 || domain.indexOf('[') != -1 || domain.indexOf(']') != -1 || domain.indexOf(' ') != -1) return false;
	return true;
	/*for (i=1;i<email.length;i++){
			if (email.charAt(i) == "@"){
				return true;
			}
	}
	return false;*/
}

function validateplanet(form){
	
	for(i=0;i<form.star.length;i++){
		if (form.star[i].checked){
			return true;
		}				
	}
	alert ("Please select a star");
	return false;
}