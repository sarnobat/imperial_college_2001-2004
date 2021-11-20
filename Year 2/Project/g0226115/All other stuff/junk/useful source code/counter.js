<script language="JavaScript">	
	
		function checkSMS(frm) {
							
			MAX = 160;
			Length = frm.m.value.length;
			if (Length > MAX) {				
				frm.m.value = frm.m.value.substring(0, MAX);
				frm.m.focus();
			}
			frm.COUNTER.value=MAX - frm.m.value.length;
			//window.status=MAX - frm.m.value.length + " character left";
		}
		function frmReset() {
			frm=document.forms.frmnewsmsuser;
			frm.hp.value="";
			frm.m.value="";
			checkSMS(frm);
		}
		function frmSubmit() {
			frm=document.forms.frmnewsmsuser;
			if(frm.hp.value=="") {
				alert("The destination mobile phone number field is empty!");
				frm.hp.focus();
				return false;
			}
			firstkar = frm.hp.value.substring(0,1);
	
			if(frm.m.value=="") {
				alert("The message field is empty!");
				frm.m.focus();
				return false;
			}
			return true;
		}		

	function keyTrap(){
		checkSMS(document.forms.frmnewsmsuser);
	}	
	if ((navigator.appName == "Netscape") && (parseInt(navigator.appVersion) >= 4)) {
		document.captureEvents(Event.KEYUP)
		document.onkeyup = keyTrap;
	}
	if ((navigator.appName == "Microsoft Internet Explorer") && (parseInt(navigator.appVersion) >= 4)) {
		document.onkeyup = keyTrap;
	}
		
		function fungsipicker(){
		
			NAME = "picker";
			OPTIONS = "toolbars=0,location=0,directories=0,status=1,menubar=0,scrollbars=0,resizable=0,width=430,height=300";
		
			windowpicker=window.open("/partners/go.php?PHPSESSID=&sestime=09012002043840&pid=addressbook&aid=picker" , NAME, OPTIONS);
									
 		}
			
</script>