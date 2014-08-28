import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import lang.Token;
import lang.lexer.Lexer;


public class Main {
	public static void main(String args[]) {
		JFrame frame = new JFrame("debug");
		frame.setSize(300, 300);
		JTextArea textInput = new JTextArea();
		JButton button = new JButton();
		button.addActionListener(e -> {
			String str = textInput.getText();
			Token[] token = Lexer.analyze(str);
			// 出力
			for(Token t: token) System.out.print(t.getString() + "(" + t.getKind() + ") ");
			System.out.println("\n\n");
		});

		frame.add(textInput, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
		
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	
}
