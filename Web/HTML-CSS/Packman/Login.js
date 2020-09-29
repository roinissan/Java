const form = document.getElementById("form");
form.addEventListener('submit',loginClick)

function loginClick(err){
    const username = document.getElementById("username").value;
    const password = document.getElementById("login_password").value;
    const users = JSON.parse(window.localStorage.getItem('users'));
    let foundUser = false;
    let userPass;
    for(var i = 0;i < users.length && !foundUser; i++){
        user = users[i];
        if(user.username === username && user.password === password){
            foundUser = true;
            alert('Successfull Login!')
            localStorage.setItem('currentUser',username.value);
            propertiesFunc();
        }
    }
    if(!foundUser){
        alert('Login Failed, there is no such user in the system.')
    }
}