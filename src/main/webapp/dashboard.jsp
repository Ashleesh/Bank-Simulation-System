<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #e9ecef;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .dashboard-container {
            max-width: 750px;
            background-color: #fff;
            padding: 35px;
            border-radius: 15px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
            border: 1px solid #dee2e6;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .header h2 {
            font-weight: 700;
            color: #343a40;
        }
        .message-box {
            margin-bottom: 25px;
        }
        .info-section {
            background-color: #f8f9fa;
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        .info-section h5 {
            font-weight: 600;
            color: #6c757d;
        }
        .info-section .info-row {
            padding: 8px 0;
            border-bottom: 1px dashed #ced4da;
        }
        .info-section .info-row:last-child {
            border-bottom: none;
        }
        .info-section strong {
            color: #495057;
        }
        .transaction-forms-container {
            display: flex;
            gap: 25px;
        }
        .form-card {
            background-color: #f1f3f5;
            padding: 25px;
            border-radius: 12px;
            flex-grow: 1;
        }
        .form-card h3 {
            font-weight: 600;
            margin-bottom: 20px;
            text-align: center;
        }
        .form-control {
            border-radius: 8px;
            padding: 12px;
        }
        .btn-action-group {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 30px;
        }
        .btn-custom {
            padding: 12px 25px;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        .btn-delete {
            background-color: #dc3545;
            color: #fff;
        }
        .btn-delete:hover {
            background-color: #c82333;
            color: #fff;
        }
        .btn-logout {
            background-color: #6c757d;
            color: #fff;
        }
        .btn-logout:hover {
            background-color: #5a6268;
            color: #fff;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <% com.bankSiml.model.Account currentAccount = (com.bankSiml.model.Account) session.getAttribute("currentAccount"); %>
        
        <% if (currentAccount != null) { %>
            <div class="header">
                <h2>Welcome, <%= currentAccount.getHolderName() %>!</h2>
            </div>
            
            <div class="message-box">
                <% String successMessage = (String) request.getAttribute("successMessage");
                   String errorMessage = (String) request.getAttribute("errorMessage");
                   if (successMessage != null) { %>
                       <div class="alert alert-success text-center" role="alert">
                           <%= successMessage %>
                       </div>
                <% } %>
                <% if (errorMessage != null) { %>
                       <div class="alert alert-danger text-center" role="alert">
                           <%= errorMessage %>
                       </div>
                <% } %>
            </div>

            <div class="info-section">
                <h5 class="text-center mb-3">Your Account Details</h5>
                <div class="row info-row">
                    <div class="col-6"><strong>Account Number:</strong></div>
                    <div class="col-6"><%= currentAccount.getAccountNumber() %></div>
                </div>
                <div class="row info-row">
                    <div class="col-6"><strong>Email:</strong></div>
                    <div class="col-6"><%= currentAccount.getEmail() %></div>
                </div>
                <div class="row info-row">
                    <div class="col-6"><strong>Account Type:</strong></div>
                    <div class="col-6"><%= currentAccount.getAccountType() %></div>
                </div>
                <div class="row info-row">
                    <div class="col-6"><strong>Balance:</strong></div>
                    <div class="col-6">₹<%= String.format("%,.2f", currentAccount.getBalance()) %></div>
                </div>
            </div>
            
            <div class="transaction-forms-container">
                <div class="form-card">
                    <h3 class="text-success">Deposit</h3>
                    <form action="AccountController" method="post">
                        <input type="hidden" name="command" value="DEPOSIT">
                        <div class="mb-3">
                            <input type="number" name="amount" step="0.01" min="0.01" class="form-control" placeholder="Amount to Deposit" required>
                        </div>
                        <button type="submit" class="btn btn-success w-100">Deposit</button>
                    </form>
                </div>
                <div class="form-card">
                    <h3 class="text-danger">Withdraw</h3>
                    <form action="AccountController" method="post">
                        <input type="hidden" name="command" value="WITHDRAW">
                        <div class="mb-3">
                            <input type="number" name="amount" step="0.01" min="0.01" class="form-control" placeholder="Amount to Withdraw" required>
                        </div>
                        <button type="submit" class="btn btn-danger w-100">Withdraw</button>
                    </form>
                </div>
            </div>

            <div class="btn-action-group">
                <a href="AccountController?command=DELETE" class="btn-custom btn-delete" onclick="return confirm('Are you sure you want to permanently delete this account?');">Delete Account</a>
                <a href="AccountController?command=LOGOUT" class="btn-custom btn-logout">Logout</a>
            </div>

        <% } else { %>
            <p class="text-center">You are not logged in. Please <a href="login.jsp">log in</a>.</p>
        <% } %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>