<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create New Account</title>
<style>
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #f0f2f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .form-container {
        width: 400px;
        background-color: #fff;
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        text-align: center;
    }
    h2 {
        color: #333;
        margin-bottom: 25px;
    }
    .form-group {
        margin-bottom: 20px;
        text-align: left;
    }
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: #555;
    }
    .form-group input,
    .form-group select {
        width: 100%;
        padding: 12px;
        border: 1px solid #ccc;
        border-radius: 8px;
        box-sizing: border-box;
        font-size: 16px;
    }
    .form-buttons {
        margin-top: 30px;
        display: flex;
        justify-content: space-between;
    }
    .btn {
        flex: 1;
        padding: 12px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 16px;
        font-weight: 700;
        text-decoration: none;
        transition: background-color 0.3s ease;
        text-align: center;
    }
    .btn-primary {
        background-color: #007bff;
        color: white;
        margin-right: 10px;
    }
    .btn-primary:hover {
        background-color: #0056b3;
    }
    .btn-secondary {
        background-color: #6c757d;
        color: white;
    }
    .btn-secondary:hover {
        background-color: #5a6268;
    }
    .message {
        padding: 12px;
        border-radius: 8px;
        margin-bottom: 20px;
        font-weight: 600;
        text-align: center;
    }
    .success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }
</style>
</head>
<body>
    <div class="form-container">
        <h2>Create New Account</h2>
        <% String successMessage = (String) request.getAttribute("successMessage");
           if (successMessage != null) { %>
               <div class="message success"><%= successMessage %></div>
        <% } %>
        <form action="AccountController?command=CREATE" method="post">
            <div class="form-group">
                <label for="account_number">Account Number:</label>
                <input type="text" id="account_number" name="account_number" required>
            </div>
            <div class="form-group">
                <label for="holder_name">Holder Name:</label>
                <input type="text" id="holder_name" name="holder_name" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="account_type">Account Type:</label>
                <select id="account_type" name="account_type" required>
                    <option value="SAVINGS">Savings</option>
                    <option value="CURRENT">Current</option>
                </select>
            </div>
            <div class="form-group">
                <label for="balance">Initial Balance:</label>
                <input type="number" id="balance" name="balance" step="0.01" value="0.00" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-buttons">
                <button type="submit" class="btn btn-primary">Create Account</button>
                <a href="login.jsp" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>