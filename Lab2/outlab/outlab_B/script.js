function sanitise() {
    var text = document.lab2B.text;
    text.value = text.value.replace(/&/g, '&amp');
    text.value = text.value.replace(/</g, '&lt');
    text.value = text.value.replace(/>/g, '&gt');
    text.value = text.value.replace(/"/g, '&quot');
    text.value = text.value.replace(/'/g, '&#x27');
    text.value = text.value.replace(/\//g, '&#x2F');
    if(!(text_validate() && pass_validate() && email_validate()))
    {
        alert("Please complete the form properly!");
        return false;
    }
    return true;
}

function text_validate() {
    var text = document.lab2B.text.value;
    var border = document.lab2B.text.style;
    var re = new RegExp("^.{6,50}$");
    var err = document.getElementById('name_error');
    if(text.localeCompare("") != 1)
        err.innerHTML = "Please fill this field.";
    else if(!re.test(text))
        err.innerHTML = "Name should be from 6 to 50 characters.";
    else
    {
        err.innerHTML = "";
        border.borderColor = "green";
        return true;
    }
    border.borderColor = "red";
    return false;
}

function pass_validate() {
    var pass = document.lab2B.pass.value;
    var border = document.lab2B.pass.style;
    var re = new RegExp("^(?=.*[$!@_]).{8,20}$");
    var err = document.getElementById('pass_error');
    if(pass.localeCompare("") != 1)
        err.innerHTML = "Please fill this field.";
    else if(!re.test(pass))
        err.innerHTML = "Password length should be between 8-20 characters with atleast one special character from the set {$, !, @, _}";
    else
    {
        err.innerHTML = "";
        border.borderColor = "green";
        return true;
    }
    border.borderColor = "red";
    return false;
}

function email_validate() {
    var email = document.lab2B.email.value;
    var border = document.lab2B.email.style;
    var dot = new RegExp("@.*\\\.");
    var at = new RegExp("@");
    var re1 = new RegExp("^[0-9a-zA-Z._]{3,}@");
    var re2 = new RegExp("^[0-9a-zA-Z._]{3,}@[a-zA-Z]{3,}\\\.");
    var re3 = new RegExp("^[0-9a-zA-Z._]{3,}@[a-zA-Z]{3,}(\\\.[a-zA-Z]{2,})+$");
    var err = document.getElementById('email_error');

    if(email.localeCompare("") != 1)
        err.innerHTML = "Please fill this field.";
    else if(!at.test(email))
        err.innerHTML = "Email should contain an @ symbol.";
    else if(!dot.test(email))
        err.innerHTML = "Domain-name after @ symbol should contain atleast one .(dot)";
    else if(!re1.test(email))
        err.innerHTML = "Local-part before @ symbol should contain atleast 3 characters. Only special characters allowed are .(dot) and _(underscore).";
    else if(!re2.test(email))
        err.innerHTML = "The substring of domain-part before the first .(dot) should have 3 or more alphabetical characters.";
    else if(!re3.test(email))
        err.innerHTML = "Each substring delimited by .(dot) should have 2 or more alphabetical characters.";
    else
    {
        err.innerHTML = "";
        border.borderColor = "green";
        return true;
    }
    border.borderColor = "red";
    return false;
}

function rem(inp) {
    if(!inp.localeCompare('name'))
    {
        document.lab2B.text.style.borderColor = 'lightgrey';
        document.getElementById('name_error').innerHTML = '';
    }
    else if(!inp.localeCompare('pass'))
    {
        document.lab2B.pass.style.borderColor = 'lightgrey';
        document.getElementById('pass_error').innerHTML = '';
    }
    else if(!inp.localeCompare('email'))
    {
        document.lab2B.email.style.borderColor = 'lightgrey';
        document.getElementById('email_error').innerHTML = '';
    }
}
