function checkAll(field){
	for (i = 0; i < field.length; i++)
		field[i].checked = true ;
}

function uncheckAll(checkbox,field){
	checkbox.checked=false;
	for (i = 0; i < field.length; i++)
		field[i].checked = fa e ;
}

function checkfield(checkbox,field){
	if(!checkbox.checked)
		uncheckAll(checkbox,field);
	else
		checkAll(field);
}