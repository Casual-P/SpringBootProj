window.onload = function () {
    let btn = document.getElementById('btn');
    let repeatP = document.getElementById('repeatPasswordInput');
    let password = document.getElementById('passwordInput');
    let repeatAlert = document.getElementById('alertRepeatPassword');
    let validAlert = document.getElementById('alertValidPassword');
    let emailLabel = document.getElementById('labelForEmail');
    let username = document.getElementById('usernameInput');
    let usernameLabel = document.getElementById('usernameLabel');
    let alertValidUsername = document.getElementById('alertValidUsername');
    let email = document.getElementById('emailInput');
    let passwordLabel = document.getElementById('passwordLabel');
    let repeatPasswordLabel = document.getElementById('repeatPasswordLabel');
    let passwordInvalid = false;
    let repeatPasswordInvalid = false;
    let usernameInvalid = false;
    let emailInvalid = false;

    btn.onclick = function () {
        let result;

        if (password.value !== repeatP.value) {
            passwordLabel.style.color = "#ff4543";
            repeatPasswordLabel.style.color = "#ff4543";
            repeatAlert.textContent = "Passwords should be equals"
            repeatAlert.hidden = false;
            repeatPasswordInvalid = true;
        }
        else {
            passwordLabel.style.color = "#000000";
            repeatPasswordLabel.style.color = "#000000";
            repeatAlert.hidden = true;
            repeatPasswordInvalid = false;
        }

        if (password.value.match(/[a-z]/g) && password.value.match(/[A-Z]/g) && password.value.match(/\d/g) &&
            password.value.match(/[^a-zA-Z\d]/g) && password.value.length >= 8)
        {
            validAlert.hidden = true;
            result = "Valid Password";
            passwordLabel.style.color = "#000000";
            passwordInvalid = false;
        }
        else
        {
            validAlert.hidden = false;
            result = "Password should contains at least 8 characters with one digit, one character uppercase and one special symbol";
            passwordInvalid = true;
            passwordLabel.style.color = "#ff4543";
        }

        validAlert.textContent = result;

        if (email.value.trim().length === 0) {
            emailLabel.style.color = "#ff4543";
            emailInvalid = true;
        }
        else {
            emailLabel.textContent = "Email address";
            emailLabel.style.color = "#000000";
            emailLabel.style.opacity = "1";
            emailInvalid = false;
        }

        if (username.value.trim().length < 5) {
            usernameInvalid = true;
            alertValidUsername.hidden = false;
            usernameLabel.style.color = "#ff4543";
        }
        else {
            alertValidUsername.hidden = true;
            usernameLabel.style.color = "#000000";
            usernameInvalid = false;
        }

        return passwordInvalid === false && repeatPasswordInvalid === false
            && usernameInvalid === false && emailInvalid === false;
    }
}