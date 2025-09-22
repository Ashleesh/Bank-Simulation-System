// All CURD operations here !!
package com.bankSiml.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  
import java.util.ArrayList;
import java.util.List;

import com.bankSiml.model.Account;
import com.bankSiml.util.DBConnection;

public class AccountDAO {
    Connection conn = null;

    // list all (sorted by DESC) !!
    public List<Account> listAll() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY id DESC";
        List<Account> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Account a = mapRowToAccount(rs, false);
                list.add(a);
            }
        }
        return list;
    }

    // create account (registration)
    public void createAccount(Account a) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, holder_name, email, account_type, balance, password) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getHolderName()); 
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getAccountType());
            ps.setDouble(5, a.getBalance());
            ps.setString(6, a.getPassword()); // Setting normal password

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    // delete account by id !!
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    // find account by id
    public Account findById(int id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAccount(rs, true);
                }
            }
        }
        return null;
    }

    // find account by account number for login
    public Account findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAccount(rs, true);
                }
            }
        }
        return null;
    }

    // Withdraw 
    public double withdraw(int accountId, double amount) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            double current = 0.0;
            String q1 = "SELECT balance FROM accounts WHERE id = ? FOR UPDATE";
            try (PreparedStatement ps1 = conn.prepareStatement(q1)) {
                ps1.setInt(1, accountId); 
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        current = rs.getDouble("balance");
                    } else {
                        throw new SQLException("Account not found : Id = " + accountId);
                    }
                }
            }

            if (current < amount) {
                throw new SQLException("Insufficient funds");
            }

            double newBal = current - amount;

            String q2 = "UPDATE accounts SET balance = ? WHERE id = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(q2)) {
                ps2.setDouble(1, newBal);
                ps2.setInt(2, accountId);
                ps2.executeUpdate();
            }

            // log transaction
            String q3 = "INSERT INTO transactions (account_id, txn_type, amount, balance_after, note) VALUES (?, 'WITHDRAW', ?, ?, ?)";
            try (PreparedStatement ps3 = conn.prepareStatement(q3)) {
                ps3.setInt(1, accountId);
                ps3.setDouble(2, amount);
                ps3.setDouble(3, newBal);
                ps3.setString(4, "Withdrawal done");
                ps3.executeUpdate();
            }

            conn.commit();
            return newBal;
        } catch (Exception e) {
            throw new SQLException("Withdraw failed: " + e.getMessage(), e);
        }
    }

    // Deposit
	public double deposit(int accountId, double amount) throws SQLException {
		if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");

		try (Connection conn = DBConnection.getConnection()){
			conn.setAutoCommit(false); 

			double current = 0.0;
			String q1 = "SELECT balance FROM accounts WHERE id = ? FOR UPDATE";
			try (PreparedStatement ps1 = conn.prepareStatement(q1)) {
				ps1.setInt(1, accountId);
				try (ResultSet rs = ps1.executeQuery()) {
					if (rs.next()) {
						current = rs.getDouble("balance");
					} else {
						throw new SQLException("Account not found : Id = " + accountId);
					}
				}
			}
			double newBal = current + amount;
			String q2 = "UPDATE accounts SET balance = ? WHERE id = ?";
			try (PreparedStatement ps2 = conn.prepareStatement(q2)) {
				ps2.setDouble(1, newBal);
				ps2.setInt(2, accountId);
				ps2.executeUpdate();
			}
			String q3 = "INSERT INTO transactions (account_id, txn_type, amount, balance_after, note) VALUES (?, 'DEPOSIT', ?, ?, ?)";
			try (PreparedStatement ps3 = conn.prepareStatement(q3)) {
				ps3.setInt(1, accountId);
				ps3.setDouble(2, amount);
				ps3.setDouble(3, newBal);
				ps3.setString(4, "Deposit done");
				ps3.executeUpdate();
			}
			conn.commit();
			return newBal;
		} catch (Exception e) {
			throw new SQLException("Deposit failed: " + e.getMessage(), e);
		}
	}

    // utility to map db row -> Account object
    private Account mapRowToAccount(ResultSet rs, boolean includePassword) throws SQLException {
        Account a = new Account();
        a.setId(rs.getInt("id"));
        a.setAccountNumber(rs.getString("account_number"));
        a.setHolderName(rs.getString("holder_name"));
        a.setEmail(rs.getString("email"));
        a.setAccountType(rs.getString("account_type"));
        a.setBalance(rs.getDouble("balance"));
        if (includePassword)
            a.setPassword(rs.getString("password"));
        else
            a.setPassword(null);
        return a;
    }
}