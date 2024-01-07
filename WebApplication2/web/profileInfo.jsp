<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Information</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
    </style>
</head>
<body>
    <h2>User Profile Information</h2>

    <p>First Name: ${userData.email}</p>
    <p>Last Name: ${userData.firstName}</p>
    <p>Email: ${userData.lastName}</p>
    <p>Address: ${userData.address}</p>

    <h2>Update user info</h2>

    <form action="UpdateProfileServlet" method="post">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName"><br>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName"><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email"><br>

        <label for="address">Address:</label>
        <input type="text" id="address" name="address"><br>

        <hr>

        <h3>Change Password</h3>
        <label for="currentPassword">Current Password:</label>
        <input type="password" id="currentPassword" name="currentPassword"><br>

        <label for="newPassword">New Password:</label>
        <input type="password" id="newPassword" name="newPassword"><br>

        <label for="confirmNewPassword">Confirm New Password:</label>
        <input type="password" id="confirmNewPassword" name="confirmNewPassword"><br>

        <input type="submit" value="Update Profile">
    </form>

</body>
</html>
