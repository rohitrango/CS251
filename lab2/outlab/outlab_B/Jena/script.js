var nameVisited=false,emailVisited=false,passVisited=false;

function sanitize() {
		var text = document.lab2B.text.value;
		text = text.replace(/&/g, '&amp');
		text = text.replace(/</g, '&lt');
		text = text.replace(/>/g, '&gt');
		text = text.replace(/"/g, '&quot');
		text = text.replace(/'/g, '&#x27');
		text = text.replace(/\//g, '&#x2F');
		alert(text);
}

function validateName() {
	var name = document.getElementById("name").value;
	if(name.length >=6 && name.length<=50) {
		return true;
	}
	else {
		return false;
	}

}

function validateEmail() {
	var regex = /^[0-9a-zA-Z._]{3,}@[a-zA-Z]{3,}(\.[a-zA-Z]{2,}){1,}$/;
	var email = document.getElementById("email").value;
	console.log(Boolean(email.match(regex)));
	if(Boolean(email.match(regex))) {
		return true;
	}
	else {
		return false;
	}
}

function validatePassword() {
	var regex = /^(?=.*[$!@_])[0-9a-zA-Z!@$_]{8,20}$/;
	var password = document.getElementById("password").value;
	if(Boolean(password.match(regex))) {
		return true;
	}
	else {
		return false;
	}
}

function validateForm(parameter) {
	var a = validateEmail(),b=validateName(),c=validatePassword();

	switch(parameter) {
		case 'name':
			if(b==false)
				document.getElementById("nameMessage").style.display = "block";		
			else
				document.getElementById("nameMessage").style.display = "none";		
			break;
		case 'email':
			if(a==false)
				document.getElementById("emailMessage").style.display = "block";
			else
				document.getElementById("emailMessage").style.display = "none";
				break;		
		case 'password':
			if(c==false)
				document.getElementById("passwordMessage").style.display = "block";
			else
				document.getElementById("passwordMessage").style.display = "none";
				break;		
		default:
			console.log("Some error.");
	}

	if(a && b && c) {
		document.getElementById("submit").removeAttribute("disabled");
	}
	else {
		document.getElementById("submit").setAttribute("disabled","disabled");
	}
}