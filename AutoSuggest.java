import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class UserInterface implements ActionListener
{
	JFrame f;
	JLabel l;
	JTextField t;
	JButton b;
	UserInterface()
	{
		f = new JFrame("AutoSuggest");
		f.setSize(300, 100);
		l = new JLabel("Enter any word here:");
		t = new JTextField(10);
		b = new JButton("Clear");
		f.setLayout(new FlowLayout());
		b.addActionListener(this);
		f.add(l);f.add(t);f.add(b);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent a)
	{
		String bname = a.getActionCommand();
		if(bname.equals("Clear"))
		{
			t.setText(null);
		}
	}

	public static void main(String args[])
	{
		new UserInterface();
	}
}

class TNode
{
	boolean isEOW;
	TNode[] next = new TNode[26];
}

class AutoSuggest
{
	final int ALPHA_SIZE = 26;

	TNode createNode()
	{
		TNode nn = new TNode();
		nn.isEOW = false;
		for(int i=0; i<ALPHA_SIZE; i++)
			nn.next[i] = null;
		return nn;
	}

	boolean insertNode(TNode root, String word)
	{
		for(int i=0; i<word.length(); i++)
		{
			int idx = word.charAt(i) - 'a';
			if(root.next[idx] == null)
				root.next[idx] = createNode();
			root = root.next[idx];
		}
		if(root.isEOW == true)
			return false;
		return (root.isEOW = true);
	}

	void printAllWordsHelper(TNode root, String p)
	{
		if(root.isEOW == true)
			System.out.println(p);
		for(int i=0; i<ALPHA_SIZE; i++)
		{
			if(root.next[i] != null)
			{
				String np = new String();
				char c = (char)(i + (int)'a');
				np = p + c;
				printAllWordsHelper(root.next[i], np);
			}
		}
	}

	void printAllWords(TNode root)
	{
		printAllWordsHelper(root, "");
	}

	void autoSuggest(TNode root, String prefix)
	{
		for(int i=0; i<prefix.length(); i++)
		{
			int idx = prefix.charAt(i) - 'a';
			if(root.next[idx] != null)
				root = root.next[idx];
			else
			{
				System.out.println("No such word exists");
				return;
			}
		}
		printAllWordsHelper(root, prefix);
	}

	public static void main(String args[])
	{
		try
		{
			AutoSuggest a = new AutoSuggest();
			TNode root = a.createNode();
			String line = null;
			BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
			while((line = reader.readLine()) != null)
			{
				a.insertNode(root, line);
			}
			reader.close();
			/*a.printAllWords(root);*/
			System.out.println("Enter a prefix:");
			Scanner s = new Scanner(System.in);
			String prefix = s.next();
			a.autoSuggest(root, prefix);
		}
		catch(FileNotFoundException fnfe)
		{fnfe.printStackTrace();}
		catch(IOException ioe)
		{ioe.printStackTrace();}
	}
}