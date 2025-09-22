package com.bankSiml.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bankSiml.dao.AccountDAO;
import com.bankSiml.model.Account;

@WebServlet("/AccountController")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AccountDAO accountDAO;

	public void init() {
		accountDAO = new AccountDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getParameter("command");
		if (command == null) {
			command = "LOGIN_FORM";
		}

		try {
			switch (command) {
			case "LOGIN_FORM":
				showLoginForm(request, response);
				break;
			case "DASHBOARD":
				showDashboard(request, response);
				break;
			case "SIGNUP_FORM":
				showSignupForm(request, response);
				break;
			case "LOGOUT":
				logout(request, response);
				break;
			case "DELETE":
				deleteAccount(request, response);
				break;
			default:
				showLoginForm(request, response);
				break;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getParameter("command");
		if (command == null) {
			command = "LOGIN";
		}

		try {
			switch (command) {
			case "LOGIN":
				login(request, response);
				break;
			case "CREATE":
				createAccount(request, response);
				break;
			case "WITHDRAW":
				withdrawMoney(request, response);
				break;
			case "DEPOSIT":
				depositMoney(request, response);
				break;
			default:
				showLoginForm(request, response);
				break;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}
	
	private void showSignupForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/signup.jsp");
		dispatcher.forward(request, response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String accountNumber = request.getParameter("account_number");
		String password = request.getParameter("password");
		
		Account account = accountDAO.findByAccountNumber(accountNumber);
		
		if (account != null && password.equals(account.getPassword())) {
			HttpSession session = request.getSession();
			session.setAttribute("currentAccount", account);
			response.sendRedirect("AccountController?command=DASHBOARD");
		} else {
			request.setAttribute("errorMessage", "Invalid account number or password.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("login.jsp");
	}
	
	private void showDashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("currentAccount") != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboard.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String accountNumber = request.getParameter("account_number");
		String holderName = request.getParameter("holder_name");
		String email = request.getParameter("email");
		String accountType = request.getParameter("account_type");
		String password = request.getParameter("password");
		double balance = Double.parseDouble(request.getParameter("balance"));

		Account newAccount = new Account();
		newAccount.setAccountNumber(accountNumber);
		newAccount.setHolderName(holderName);
		newAccount.setEmail(email);
		newAccount.setAccountType(accountType);
		newAccount.setBalance(balance);
		newAccount.setPassword(password);

		try {
			accountDAO.createAccount(newAccount);
			request.setAttribute("successMessage", "Account created successfully! Please log in.");
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error creating account: " + e.getMessage());
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("currentAccount") != null) {
			Account currentAccount = (Account) session.getAttribute("currentAccount");
			try {
				accountDAO.delete(currentAccount.getId());
				logout(request, response);
			} catch (Exception e) {
				request.setAttribute("errorMessage", "Error deleting account: " + e.getMessage());
				showDashboard(request, response);
			}
		} else {
			response.sendRedirect("login.jsp");
		}
	}

	private void withdrawMoney(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("currentAccount") != null) {
			Account currentAccount = (Account) session.getAttribute("currentAccount");
			double amount = Double.parseDouble(request.getParameter("amount"));
			try {
				double newBalance = accountDAO.withdraw(currentAccount.getId(), amount);
				request.setAttribute("successMessage", "Withdrawal successful! New balance: " + newBalance);
			} catch (SQLException e) {
				request.setAttribute("errorMessage", "Withdrawal failed: " + e.getMessage());
			}
			Account updatedAccount = accountDAO.findById(currentAccount.getId());
			session.setAttribute("currentAccount", updatedAccount);
			showDashboard(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}

	private void depositMoney(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("currentAccount") != null) {
			Account currentAccount = (Account) session.getAttribute("currentAccount");
			double amount = Double.parseDouble(request.getParameter("amount"));
			try {
				double newBalance = accountDAO.deposit(currentAccount.getId(), amount);
				request.setAttribute("successMessage", "Deposit successful! New balance: " + newBalance);
			} catch (SQLException e) {
				request.setAttribute("errorMessage", "Deposit failed: " + e.getMessage());
			}
			Account updatedAccount = accountDAO.findById(currentAccount.getId());
			session.setAttribute("currentAccount", updatedAccount);
			showDashboard(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}
	
	private String generateAccountNumber() {
		return "ACC-" + System.currentTimeMillis();
	}
}