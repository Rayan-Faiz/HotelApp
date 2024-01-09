<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Hotel Reservation Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
          integrity="sha384-ez+4nBbae8PeKteFz5zD7uXjnDJoK9JGPnHroArz8trbSRZI+97eW8zI3T5E9bU"
          crossorigin="anonymous">
    <style>
        body {
            font-family: 'Parisienne', cursive;
            background: url('bg-img2.jpg') no-repeat center center fixed;
            background-size: cover;
            text-align: center;
            margin: 0;
            padding: 0;
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

        .profile-icon {
            margin-right: 10px;
        }
        .form-container {
            margin-top: 20px;
        }
        .dashboard-form {
            display: inline-block;
            margin-right: 15px;
        }
        .dashboard-btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 16px;
        }
        .dashboard-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

    <h2>
        <span class="profile-icon"><i class="fas fa-user-circle"></i></span>
        Welcome to the Hotel Reservation Dashboard
    </h2>

    <div class="form-container">
        <form action="ProfileInfoServlet" method="get" class="dashboard-form">
            <input type="submit" value="View and Update Profile Information" class="btn btn-success dashboard-btn">
        </form>

        
        <form action="ReservationForm.jsp" method="get" class="dashboard-form">
            <input type="submit" value="Make a Reservation" class="btn btn-primary dashboard-btn">
        </form>
    </div>
    <script src="https://kit.fontawesome.com/your-font-awesome-key.js" crossorigin="anonymous"></script>
</body>
</html>
