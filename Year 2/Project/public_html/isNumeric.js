function validateSell(sale, qty, forsale){
	if (sale.value < 0){
		alert("Cannot use negative values,\nPlease re-enter value");
		sale.value = "";		// Clear the field
		sale.focus();
	}		
	if (sale.value > (qty - forsale)){
		alert("Incorrect value (too large)");
		sale.value = "";
		sale.focus();
	}
}

function checkIsNumeric(string){
	if(!IsNumeric(string.value)){
		alert("Please enter an integer");
		string.value = "";
		string.focus();
		return false;
	}
}

function checkIsFilled(string){
	if(string.value == ""){
		alert("Please enter a value");
		string.focus();
		return false;
	}
	return true;
}

function IsNumeric(strValue){
	var ValidChars = "0123456789";
	var IsNumber=true;
	var Char;

	for (i=0; i < strValue.length && IsNumber == true; i++){
		Char = strValue.charAt(i); 
		if (ValidChars.indexOf(Char) == -1){
			IsNumber = false;
		}
	}
	return IsNumber;
}

function calculateCost(mileage, cost, costpermile){
	if(mileage.value ==""){
		cost.value=0;
	}
	else{
		var x = mileage.value*costpermile;
		cost.value=x;
	}
}