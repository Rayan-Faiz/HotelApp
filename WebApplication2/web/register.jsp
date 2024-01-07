<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Registration Form</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Parisienne&display=swap">
    <style>
        body {
            margin: 0;
            padding: 0;
            background: url('bg-img.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: 'Parisienne', cursive;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        form {
            background-color: rgba(255, 255, 255, 0.8);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 18px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 18px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
        }

        #loginLink {
            display: block;
            text-align: center;
            margin-top: 16px;
            color: #333;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <form action="RegisterServlet" method="post">
        <label for="firstname">Firstname:</label>
        <input type="text" id="firstname" name="firstname" required>

        <label for="lastname">Lastname:</label>
        <input type="text" id="lastname" name="lastname" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="address">Address:</label>
        <input type="text" id="address" name="address" required>

        <input type="submit" value="Register">
        
        <a id="loginLink" href="index.html">Already have an account?</a>
    </form>
</body>
</html>
