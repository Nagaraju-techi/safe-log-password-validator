import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

class User {
    String username, password;
    double balance;
    ArrayList<String> history;

    public User(String u, String p) {
        username = u;
        password = p;
        balance = 0;
        history = new ArrayList<>();
    }
}

public class BankingGUI {

    static HashMap<String, User> users = new HashMap<>();
    static final String FILE = "users.txt";

    public static void main(String[] args) {
        loadUsers();
        loginUI();
    }

    // ================= FILE HANDLING =================
    static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                User u = new User(p[0], p[1]);
                u.balance = Double.parseDouble(p[2]);

                if (p.length > 3) {
                    u.history = new ArrayList<>(Arrays.asList(p[3].split(";")));
                }
                users.put(u.username, u);
            }
        } catch (Exception e) {
            System.out.println("No data file found (first run).");
        }
    }

    static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (User u : users.values()) {
                String hist = String.join(";", u.history);
                bw.write(u.username + "," + u.password + "," + u.balance + "," + hist);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Save error!");
        }
    }

    // ================= LOGIN UI =================
    static void loginUI() {
        JFrame f = new JFrame("Banking System");
        f.setSize(350, 250);
        f.setLayout(new BorderLayout());

        JLabel title = new JLabel("Bank Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(user);
        panel.add(new JLabel("Password:"));
        panel.add(pass);

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        JPanel btn = new JPanel();
        btn.add(login);
        btn.add(register);

        f.add(title, BorderLayout.NORTH);
        f.add(panel, BorderLayout.CENTER);
        f.add(btn, BorderLayout.SOUTH);

        // LOGIN
        login.addActionListener(e -> {
            String u = user.getText();
            String p = new String(pass.getPassword());

            if (users.containsKey(u) && users.get(u).password.equals(p)) {
                new Dashboard(users.get(u));
                f.dispose();
            } else {
                JOptionPane.showMessageDialog(f, "Invalid Login!");
            }
        });

        // REGISTER
        register.addActionListener(e -> {
            String u = JOptionPane.showInputDialog("Enter Username:");
            String p = JOptionPane.showInputDialog("Enter Password:");

            if (users.containsKey(u)) {
                JOptionPane.showMessageDialog(f, "User exists!");
            } else {
                users.put(u, new User(u, p));
                saveUsers();
                JOptionPane.showMessageDialog(f, "Registered!");
            }
        });

        f.setVisible(true);
    }
}

// ================= DASHBOARD =================
class Dashboard extends JFrame {
    User user;

    public Dashboard(User u) {
        user = u;

        setTitle("Welcome " + user.username);
        setSize(400, 350);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Dashboard", JLabel.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(0, 153, 76));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton dep = new JButton("Deposit");
        JButton wit = new JButton("Withdraw");
        JButton bal = new JButton("Check Balance");
        JButton his = new JButton("History");
        JButton logout = new JButton("Logout");

        JButton[] btns = {dep, wit, bal, his, logout};
        for (JButton b : btns) {
            b.setBackground(new Color(0, 102, 204));
            b.setForeground(Color.WHITE);
        }

        panel.add(dep);
        panel.add(wit);
        panel.add(bal);
        panel.add(his);
        panel.add(logout);

        add(header, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // ACTIONS
        dep.addActionListener(e -> {
            double amt = Double.parseDouble(JOptionPane.showInputDialog("Amount:"));
            user.balance += amt;
            user.history.add("Deposited ₹" + amt);
            BankingGUI.saveUsers();
        });

        wit.addActionListener(e -> {
            double amt = Double.parseDouble(JOptionPane.showInputDialog("Amount:"));
            if (user.balance >= amt) {
                user.balance -= amt;
                user.history.add("Withdraw ₹" + amt);
                BankingGUI.saveUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient!");
            }
        });

        bal.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Balance: ₹" + user.balance);
        });

        his.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, String.join("\n", user.history));
        });

        logout.addActionListener(e -> {
            dispose();
            BankingGUI.loginUI();
        });

        setVisible(true);
    }
}