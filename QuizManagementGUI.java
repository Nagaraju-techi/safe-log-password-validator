import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Question {
    String question;
    String[] options;
    int correct;

    Question(String q, String[] op, int c) {
        question = q;
        options = op;
        correct = c;
    }
}

public class QuizManagementGUI {

    static ArrayList<Question> quiz = new ArrayList<>();

    public static void main(String[] args) {
        showMainMenu();
    }

    // MAIN MENU
    static void showMainMenu() {
        JFrame frame = new JFrame("Quiz System");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));

        JButton adminBtn = new JButton("Admin Panel");
        JButton startBtn = new JButton("Start Quiz");
        JButton exitBtn = new JButton("Exit");

        frame.add(adminBtn);
        frame.add(startBtn);
        frame.add(exitBtn);

        adminBtn.addActionListener(e -> openAdminPanel());
        startBtn.addActionListener(e -> {
            if (quiz.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No questions added!");
            } else {
                new QuizFrame();
                frame.dispose();
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    // ADMIN PANEL
    static void openAdminPanel() {
    JFrame frame = new JFrame("Admin Panel - Add Question");
    frame.setSize(450, 400);
    frame.setLayout(new GridLayout(8, 2, 10, 10));

    JLabel qLabel = new JLabel("Question:");
    JTextField questionField = new JTextField();

    JLabel op1Label = new JLabel("Option 1:");
    JTextField op1 = new JTextField();

    JLabel op2Label = new JLabel("Option 2:");
    JTextField op2 = new JTextField();

    JLabel op3Label = new JLabel("Option 3:");
    JTextField op3 = new JTextField();

    JLabel op4Label = new JLabel("Option 4:");
    JTextField op4 = new JTextField();

    JLabel correctLabel = new JLabel("Correct Option (1-4):");
    JTextField correct = new JTextField();

    JButton addBtn = new JButton("Add Question");

    // Add components clearly
    frame.add(qLabel); frame.add(questionField);
    frame.add(op1Label); frame.add(op1);
    frame.add(op2Label); frame.add(op2);
    frame.add(op3Label); frame.add(op3);
    frame.add(op4Label); frame.add(op4);
    frame.add(correctLabel); frame.add(correct);
    frame.add(new JLabel("")); frame.add(addBtn);

    addBtn.addActionListener(e -> {
        try {
            String q = questionField.getText();

            String[] options = {
                op1.getText(),
                op2.getText(),
                op3.getText(),
                op4.getText()
            };

            int ans = Integer.parseInt(correct.getText()) - 1;

            if (q.isEmpty() || options[0].isEmpty() || options[1].isEmpty() ||
                options[2].isEmpty() || options[3].isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            if (ans < 0 || ans > 3) {
                JOptionPane.showMessageDialog(frame, "Correct option must be 1-4!");
                return;
            }

            quiz.add(new Question(q, options, ans));

            JOptionPane.showMessageDialog(frame, "Question Added Successfully!");

            // Clear fields after adding
            questionField.setText("");
            op1.setText("");
            op2.setText("");
            op3.setText("");
            op4.setText("");
            correct.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Input!");
        }
    });

    frame.setVisible(true);
}
}

// QUIZ FRAME
class QuizFrame extends JFrame implements ActionListener {

    int current = 0, score = 0, timeLeft = 15;
    JLabel qLabel, timerLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup group = new ButtonGroup();
    JButton nextBtn;
    Timer timer;

    public QuizFrame() {
        setTitle("Quiz");
        setSize(600, 400);
        setLayout(null);

        qLabel = new JLabel();
        qLabel.setBounds(50, 50, 500, 30);
        add(qLabel);

        timerLabel = new JLabel("Time: 15");
        timerLabel.setBounds(450, 10, 100, 30);
        add(timerLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(50, 100 + i * 40, 400, 30);
            group.add(options[i]);
            add(options[i]);
        }

        nextBtn = new JButton("Next");
        nextBtn.setBounds(200, 280, 100, 30);
        nextBtn.addActionListener(this);
        add(nextBtn);

        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft == 0) {
                nextQuestion();
            }
        });

        loadQuestion();
        timer.start();

        setVisible(true);
    }

    void loadQuestion() {
        if (current < QuizManagementGUI.quiz.size()) {
            Question q = QuizManagementGUI.quiz.get(current);
            qLabel.setText("Q" + (current + 1) + ": " + q.question);

            for (int i = 0; i < 4; i++) {
                options[i].setText(q.options[i]);
            }

            group.clearSelection();
            timeLeft = 15;
        } else {
            showResult();
        }
    }

    void nextQuestion() {
        timer.stop();

        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                if (i == QuizManagementGUI.quiz.get(current).correct) {
                    score++;
                }
            }
        }

        current++;
        loadQuestion();
        timer.start();
    }

    void showResult() {
        double percent = (score * 100.0) / QuizManagementGUI.quiz.size();

        String performance;
        if (percent >= 80) performance = "Excellent";
        else if (percent >= 50) performance = "Good";
        else performance = "Needs Improvement";

        JOptionPane.showMessageDialog(this,
                "Score: " + score + "/" + QuizManagementGUI.quiz.size() +
                "\nPercentage: " + percent + "%" +
                "\nPerformance: " + performance);

        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        nextQuestion();
    }
}