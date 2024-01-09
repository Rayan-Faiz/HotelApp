<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Information</title>
    <style>
        body {
            font-family: 'Parisienne', cursive;
            background: url('bg-img2.jpg') no-repeat center center fixed;
            background-size: cover;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        .profile-container {
            background: rgba(255, 255, 255, 0.8);
            padding: 20px;
            border-radius: 10px;
            margin: 20px;
        }

        h2 {
            background-color: #ff9800;
            color: #ffffff;
            padding: 15px;
            margin-bottom: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        p {
            color: #333;
            font-size: 18px;
            margin-bottom: 10px;
        }

        form {
            display: inline-block;
            margin-top: 20px;
            background-color: rgba(255, 255, 255, 0.8);
            padding: 20px;
            border-radius: 10px;
        }

        label {
            font-size: 16px;
            margin-bottom: 5px;
            display: block;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

    <h2>User Profile Information</h2>

    <div class="profile-container">
        <p>First Name: ${userData.firstName}</p>
        <p>Last Name: ${userData.lastName}</p>
        <p>Email: ${userData.email}</p>
        <p>Address: ${userData.address}</p>
    </div>

    <h2>Update User Info</h2>

    <form action="UpdateProfileServlet" method="post">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName">

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName">

        <label for="email">Email:</label>
        <input type="email" id="email" name="email">

        <label for="address">Address:</label>
        <input type="text" id="address" name="address">

        <hr>

        <h3>Change Password</h3>
        <label for="currentPassword">Current Password:</label>
        <input type="password" id="currentPassword" name="currentPassword">

        <label for="newPassword">New Password:</label>
        <input type="password" id="newPassword" name="newPassword">

        <label for="confirmNewPassword">Confirm New Password:</label>
        <input type="password" id="confirmNewPassword" name="confirmNewPassword">

        <input type="submit" value="Update Profile">
    </form>

</body>
</html>
