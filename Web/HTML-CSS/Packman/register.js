

function fieldsNotEmpty(user_name,password,full_name,email,birthdate){
    if(user_name==""||password==""||full_name==""||email==""||birthdate==""){
        alert("You must fill all the fields!");
        return false;
    }
    return true;
}

function checkPassword(password){
    if(!password.match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/)){
        alert("The password inserted is not valid.");
        return false;
    }
    return true;      
}

function checkFullName(full_name){
    if(!full_name.match(/^[a-z]([-']?[a-z]+)*( ?[a-z]([-']?[a-z]+)*)+$/)){
        alert("The full name inserted is not valid.");
        return false;
    }
    return true;
}

function checkEmail(email){
    if(!email.match(/\S+@\S+\.\S+/)){
        alert("The Email address inserted is not valid.");
        return false;
    }
    return true;
}

function checkUserNameExist(user_name){
    const users = JSON.parse(window.localStorage.getItem('users'));
    for(i=0;i<users.length;i++){
        if(users[i].username==user_name){
            alert("User with this Username already exist.");
            return false;
        }
    }
    return true;
}

function validation(){
    var date = $('#birthday').val();
    var user_name = $('#user_name').val();
    var user_password = $('#password').val();
    var full_name = $('#full_name').val();
    var email = $('#email').val();


    if(fieldsNotEmpty(user_name,user_password,full_name,email,date)){
        if(checkUserNameExist(user_name)){
            if(checkPassword(user_password)){
                if(checkFullName(full_name)){
                    if(checkEmail(email)){
                        const users = JSON.parse(window.localStorage.getItem('users'));
                        users.push({username:user_name,password:user_password});
                        localStorage.setItem('users',JSON.stringify(users));
                        alert("New User have been created, you can log in and play now!");
                        homeFunc();
                    }
                }
            }
        }
    }
}


