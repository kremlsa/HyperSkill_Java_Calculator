package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Calculator extends JFrame {

    String sqChar = String.valueOf((char) 0x00F7);
    ArrayList<String> operations = new ArrayList<>();
    ArrayList<String> equation = new ArrayList<>();
    FuncListener funcListener = new FuncListener();
    EquationListener equationListener = new EquationListener();
    JPanel top = new JPanel();
    JPanel bottom = new JPanel();
    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JPanel grid = new JPanel();
    JLabel equationLabel = new JLabel();
    JLabel resultLabel = new JLabel();
    Map<String, JButton> buttonsMap = new LinkedHashMap<String, JButton>() {{
        put("SquareRoot", new JButton(sqChar));
        put("Clear", new JButton("C"));
        put("Delete", new JButton("Del"));
        put("Seven", new JButton("7"));
        put("Eight", new JButton("8"));
        put("Nine", new JButton("9"));
        put("Divide", new JButton("/"));
        put("Four", new JButton("4"));
        put("Five", new JButton("5"));
        put("Six", new JButton("6"));
        put("Multiply", new JButton("x"));
        put("One", new JButton("1"));
        put("Two", new JButton("2"));
        put("Three", new JButton("3"));
        put("Add", new JButton("+"));
        put("Dot", new JButton("."));
        put("Zero", new JButton("0"));
        put("Equals", new JButton("="));
        put("Subtract", new JButton("-"));
    }};
    Map<String, Character> unicodeMap = new LinkedHashMap<String, Character>() {{
        put("+", (char) 0x002B);
//        put("-", (char) 0x2212);
        put("-", '-');
        put("/", (char) 0x00F7);
        put("*", (char) 0x00D7);
    }};

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        grid.setLayout(new GridLayout(5, 4, 30, 30));
        JButton empty1 = new JButton();
        empty1.setEnabled(false);
        empty1.setBorderPainted(false);
        empty1.setPreferredSize(new Dimension(50, 50));
        JButton empty2 = new JButton();
        empty2.setEnabled(false);
        empty2.setBorderPainted(false);
        empty2.setPreferredSize(new Dimension(50, 50));
        grid.add(empty1);
        grid.add(empty2);
        for (Map.Entry<String, JButton> entry : buttonsMap.entrySet()) {
            entry.getValue().setPreferredSize(new Dimension(50, 50));
            if (entry.getKey().equals("Equals") || entry.getKey().equals("Clear")
                                                || entry.getKey().equals("Delete")) {
                entry.getValue().addActionListener(funcListener);
            } else {
                entry.getValue().addActionListener(equationListener);
            }
            entry.getValue().setName(entry.getKey());
            grid.add(entry.getValue());
        }
        equationLabel.setName("EquationLabel");
        equationLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,50));
        equationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        equationLabel.setForeground(Color.GREEN);
        resultLabel.setName("ResultLabel");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 30));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,50));
        resultLabel.setText("0");
        top.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        bottom.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        left.setPreferredSize(new Dimension(50, Integer.MAX_VALUE));
        right.setPreferredSize(new Dimension(50, Integer.MAX_VALUE));
        top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
        resultLabel.setAlignmentX( Component.RIGHT_ALIGNMENT);
        equationLabel.setAlignmentX( Component.RIGHT_ALIGNMENT);
        top.add(resultLabel);
        top.add(equationLabel);
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(grid, BorderLayout.CENTER);
        getContentPane().add(right, BorderLayout.EAST);
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void functions(String func) {
        switch (func) {
            case "Equals":
                solver();
                break;
            case "Clear":
                clear();
                break;
            case "Delete":
                delete();
                break;
        }
    }

    public void solver() {
        String eq = equationLabel.getText();
        System.out.println("**** " + eq);
        if (!Character.isDigit(eq.charAt(eq.length() - 1)) || eq.endsWith((char) 0x00F7 + "0") || eq.startsWith(".")) {
            equationLabel.setForeground(Color.RED.darker());
        } else {
            equationLabel.setForeground(Color.GREEN);
            String[] vars = equationLabel.getText().split("[^\\d.]");
            for (int i = 0; i < vars.length - 1; i++) {
                equation.add(vars[i]);
                equation.add(operations.get(i));
            }
            equation.add(vars[vars.length - 1]);
            String result = Converter.toRPN(equation);
            resultLabel.setText(result);
        }
    }

    public void clear() {
        operations.clear();
        equation.clear();
        equationLabel.setText("");
        resultLabel.setText("0");
        equationLabel.setForeground(Color.GREEN);
        System.out.println("---- clear -----");
    }

    public void delete() {
        String text = equationLabel.getText();
        if (!text.isEmpty()) {
            String symbol = text.substring(text.length() - 1);
            if (!Character.isDigit(symbol.charAt(0)) && !symbol.equals(".")) {
                operations.remove(operations.size() - 1);
            }
            equationLabel.setText(text.substring(0, text.length() - 1));
        }
    }

    public void setEquation(String symbol) {
        String text = equationLabel.getText();
        if (!Character.isDigit(symbol.charAt(0)) && !symbol.equals(".") && text.length() > 0) {
            if (text.charAt(text.length() - 1) == '.') {
                text = text + "0";
            }
            if (text.charAt(0) == '.') {
                text = "0" + text;
            }
            if (!Character.isDigit(text.charAt(text.length() - 1))) {
                if (operations.size() > 0) {
                    operations.remove(operations.size() - 1);
                    text = text.substring(0, text.length() - 1);
                }
            }
            if (symbol.equals("x")) {
                symbol = "*";
            }
            operations.add(symbol);
            equationLabel.setText(text+ unicodeMap.get(symbol));
        } else if (Character.isDigit(symbol.charAt(0)) ||symbol.equals(".")) {
            equationLabel.setText(text + symbol);
        }
    }

    class FuncListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            functions(((JButton) e.getSource()).getName());
        }
    }

    class EquationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            setEquation(((JButton) e.getSource()).getText());
        }
    }
}
