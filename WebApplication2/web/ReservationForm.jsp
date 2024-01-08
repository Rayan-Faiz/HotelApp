<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reservation Form</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
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

        select, input {
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
    </style>
</head>
<body>
    <form action="ReservationServlet" method="post">
        <label for="roomType">Select Room Type:</label>
        <select id="roomType" name="roomType" required>
            <option value="Standard">Standard</option>
            <option value="Deluxe">Deluxe</option>
            <option value="Suite">Suite</option>
            <option value="Presidential">Presidential</option>
            <!-- Add other room types as needed -->
        </select><br>

        <label for="bedType">Select Bed Type:</label>
        <select id="bedType" name="bedType" required>
            <option value="Single">Single Bed</option>
            <option value="Double">Double Bed</option>
            <option value="Queen">Queen Bed</option>
            <option value="King">King Bed</option>
            <!-- Add other bed types as needed -->
        </select><br>

        <label for="checkInDate">Check-in Date:</label>
        <input type="date" id="checkInDate" name="checkInDate" required><br>

        <label for="checkOutDate">Check-out Date:</label>
        <input type="date" id="checkOutDate" name="checkOutDate" required><br>

        <input type="submit" value="Submit Reservation">
    </form>
</body>
</html>
