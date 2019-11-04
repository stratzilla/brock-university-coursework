function todaysdate(){
	var today = new Date();
	var day = today.getDate();
	//Is in range 0-11, so add one
	var month = today.getMonth()+1;
	var year = today.getFullYear();
	today = month+'/'+day+'/'+year;
	document.write(today);
}

function starttime(){
	var today = new Date();
	var hour = today.getHours();
	var min = today.getMinutes();
	var sec = today.getSeconds();
	min = checktime(min);
	sec = checktime(sec);
	document.getElementById('time').innerHTML=hour+":"+min+":"+sec;
	t = setTimeout(function(){starttime()},500);
}

function checktime(i){
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}

//I ended up using HTML5 validation since it's easier to use
//and requires less lines
/*function validate(){
	var x = document.forms["myForm"][""].value;
	if (x == null || x == ""){
		alert("All fields must be filled out");
		return false;
	}
}*/

function jspopupadm(){
	if (confirm('Are you sure you want to make this user an admin?')) {
		return true;
	}
	else {
		return false;
	}
}

function jspopupdel(){
	if (confirm('Are you sure you want to delete this user?')) {
		return true;
	}
	else {
		return false;
	}
}

function jspopupunreg(){
	if (confirm('Are you sure you want to delete your account?')) {
		return true;
	}
	else {
		return false;
	}
}

function jspopuprem(){
	if (confirm('Are you sure you wish to delete this book entry?')) {
		return true;
	}
	else {
		return false;
	}
}

