/**Implementation of Blockchains for a secure voting system 
 * Developed by Aishwarya Rao and Arjun Kini, 6th semester, Information Science, NMIT 
 */
package sixthsemjava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


class Block implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	String hash;
	String previousHash;
	private String voterID;
	private String partyID;
	private int difficulty;
	int nonce=0; //number of iterations taken to get the required hashcode based on difficulty
	int position;
	Block(String voterID, String partyID, String previous, int diff, int pos)
	{
		this.voterID=voterID;
		this.partyID=partyID;
		this.position=pos;
		previousHash= previous;
		this.difficulty=diff;
		//Create a string of diff number of zeroes as our goal for number of leading zeroes in the hash
		String goal = new String(new char[diff]).replace("\0", "0");
		hash=generateHash(this.voterID, this.partyID, goal, nonce, diff);
		System.out.println(this.voterID + " "+this.partyID + " "+ hash);
		
	}
	public Block() {
		// TODO Auto-generated constructor stub
	}
	String getPartyID()
	{
		return partyID;
	}
	void CheckHash(LinkedList<Block> bc)
	{
		
		//Check if the entire blockchain is valid
		for(int i=0; i<bc.size(); i++)
		{
			String goal = new String(new char[bc.get(i).difficulty]).replace("\0", "0");
			int l=0;
			//if hash in blockchain equal to generated hash for same data and previoushash of block equal to hash of the previous block
			if((bc.get(i).hash.equals(generateHash(bc.get(i).voterID, bc.get(i).partyID, goal, l, bc.get(i).difficulty)) && ((i!=0 && bc.get(i).previousHash.equals(bc.get(i-1).hash)) ||(i==0 && bc.get(i).previousHash==null))))
					{
					}
			else 
				System.out.println("Block chain detected error!");
		}
	}
	private String generateHash(String voterID, String partyID, String goa, int non, int diff)
	{
		//Performs SHA256 encoding to generate hash 
		StringBuffer hexString = new StringBuffer();
		MessageDigest md;
		do {
			hexString.setLength(0);
			String input=voterID+partyID+ String.valueOf(non);
			try {
				md = MessageDigest.getInstance("SHA-256");
				byte[] hash = md.digest(input.getBytes("UTF-8"));	        
				for (int i = 0; i < hash.length; i++) {
					String hex = Integer.toHexString(0xff & hash[i]);
					if(hex.length() == 1) hexString.append('0');
					hexString.append(hex);
				}
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			non++;
		}while(!hexString.toString().substring(0, diff).equals(goa));
		//until the leading zeroes match
		//non is the number of iterations it take to get the right number of zeroes
		return hexString.toString();
	}
	void display()
	{
		System.out.println(voterID +"\t"+partyID+"\t\t"+hash);
	}
	boolean checkID(String id)
	{
		if (this.voterID.equalsIgnoreCase(id))
			return true;
		else return false;
		
	}
	
}
class Threadz extends Thread
{
	int sum;
	public void run()
	{
		
		switch(Thread.currentThread().getName())
		{
		case "BJP": 
			sum=count("BJP");
			System.out.println("BJP\t\t"+sum);
			break;
		case "CONGRESS": 
			sum=count("CONGRESS");
			System.out.println("CONGRESS\t"+sum);
			break;
		case "JD(U)": 
			sum=count("JD(U)");
			System.out.println("JD(U)\t\t"+sum);
			break;
		case "JD(S)": 
			sum=count("JD(S)");
			System.out.println("JD(S)\t\t"+sum);
			break;
		case "AAP": 
			sum=count("AAP");
			System.out.println("AAP\t\t"+sum);
			break;
		case "INDE-1": 
			sum=count("INDE-1");
			System.out.println("INDE-1\t\t"+sum);
			break;
		case "INDE-2": 
			sum=count("INDE-2");
			System.out.println("INDE-2\t\t"+sum);
			break;
		default: System.out.println("Data Retrieval Error");
			
					
		}
			
		
	}
	int count(String name)
	{
		File f=new File("BChain.txt");
		int count=0;
		try {
			FileInputStream fin;
			if(f.length()!=0)
			{
				fin = new FileInputStream(f);
				ObjectInputStream in = new ObjectInputStream(fin);
				LinkedList <Block> bc=(LinkedList<Block>)in.readObject();
				for(int i=0; i<bc.size();i++)
				{
					if(name.equalsIgnoreCase(bc.get(i).getPartyID()))
					{
						count++;
					}
				}
				return count;
			}
			else return 0;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
}
public class Blockchain01{

	int first=0;
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		LinkedList <Block> bc= new LinkedList <Block>();
		Scanner sc=new Scanner(System.in);
		int log=0,k=0;
		BufferedReader br;
		String id;
		String party="";
		String partyList[]=new String[10];
		
		System.out.println("Welcome to the BlockVote");
		
		//GUI setup
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		j.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				Threadz t[]=new Threadz[7];
				for(int i=0;i<7;i++) {
					t[i]=new Threadz();
				}
				t[0].setName("BJP");
				t[1].setName("CONGRESS");
				t[2].setName("JD(U)");
				t[3].setName("JD(S)");
				t[4].setName("AAP");
				t[5].setName("INDE-1");
				t[6].setName("INDE-2");
				System.out.println("\nParty\t\tTally");
				for(int i=0;i<7;i++)
				{
					t[i].start();
					try {
						t[i].join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				j.dispose();
				System.exit(0);
				
				
			}
		});
		j.setTitle("Voting 2018");
 
		CardLayout card = new CardLayout(30,30);
	     j.getContentPane().setLayout(card);  
		JPanel panel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel3.setLayout(new GridLayout(4,1));
		JLabel user = new JLabel("Enter voter ID");
		panel.add(user);
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(200,30));
		panel1.add(tf);
		JButton login1= new JButton("Login");
		panel2.add(login1);
		panel3.add(panel);panel3.add(panel1);panel3.add(panel2);
		panel3.setSize(300, 300);
		j.setSize(500, 350);
		j.getContentPane().add(panel3, "first");
		j.setVisible(true);
		
		login1.addActionListener(new ActionListener() {
			String id;
			public void actionPerformed(ActionEvent e)
			{
				id = tf.getText();
				int log=0, k1=0;
				int first=0;

				File login=new File("LoginDetails.txt");
				
				try {
					BufferedReader br=new BufferedReader(new FileReader(login));
					String line=br.readLine();
					while(line!=null)
					{
						if(id.matches(line))
						{
							System.out.println("Login Successful!");
							JPanel k = new JPanel();
							
							k.setLayout(new BoxLayout(k, BoxLayout.Y_AXIS));

							File f=new File("BChain.txt");
							try {
								FileInputStream fin;
								if(f.length()!=0)
								{
									fin = new FileInputStream(f);
									ObjectInputStream in = new ObjectInputStream(fin);
									LinkedList <Block> bc=(LinkedList<Block>)in.readObject();
									k.setVisible(true);
									k.setSize(1000,1000);
									if(bc!=null)
									{
										System.out.println("VoterID\tPartyID\t\tHash");
										for(int i=0;i<bc.size();i++)
										{
											bc.get(i).display();
										}
										for(int i=0;i<bc.size();i++)
										{
											if(bc.get(i).checkID(id))
											{
												JPanel l = new JPanel();
												l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
										
												JLabel cheat = new JLabel("You have already voted. Voting Details : ");
												String p;
												JLabel partydeets = new JLabel(p=bc.get(i).getPartyID());
												cheat.setSize(50, 50);
												l.add(cheat);
												l.add(partydeets);
												j.add(l, "fourth");
												card.show(j.getContentPane(), "fourth");
												return;
											}
										}
										in.close();
										fin.close();
									}
								}
								else
								{
									first=1;
									System.out.println("blockchain empty");
								}
								
								k1=0;
								log=0;
							
								File parties= new File("PartyNames.txt");
								try {
									br=new BufferedReader(new FileReader(parties));
									String line1=br.readLine();
									JLabel party= new JLabel("Your party options are");
									k.add(party);
									JPanel radio = new JPanel();
									radio.setLayout(new BoxLayout(radio, BoxLayout.Y_AXIS));
									ButtonGroup g = new ButtonGroup();
									while(line1!=null)
									{										
										partyList[k1++]=line1;
										JRadioButton b = new JRadioButton(line1, false);
										g.add(b);
										radio.add(b);
										line1=br.readLine();
											
									}
								
									br.close();
									k.add(radio);
									JButton sub= new JButton("Vote");
									k.add(sub);
									j.add(k, "second");										
									card.next(j.getContentPane());
									sub.addActionListener(new ActionListener()
									{
										public void actionPerformed(ActionEvent e)
										{
											String partysel= null;
											for (Enumeration<AbstractButton> buttons = g.getElements(); buttons.hasMoreElements();) 
											{
												AbstractButton button = buttons.nextElement();

												if (button.isSelected()) {	
													partysel = button.getText();
												}
											}
											if(!partysel.equals(null))
											{
												File f=new File("BChain.txt");
												LinkedList <Block> bc=new LinkedList<Block>();
												try {
													FileInputStream fin;
													if(f.length()!=0)
													{
														fin = new FileInputStream(f);
														ObjectInputStream in = new ObjectInputStream(fin);
														bc=(LinkedList<Block>)in.readObject();
													}
												} catch (Exception ex)
												{
													ex.getStackTrace();
												}
												Block b;
												if(f.length()==0)
													b =new Block(id, partysel.toUpperCase(),null, 2, 0);
												else
												{
													b=new Block(id, partysel.toUpperCase(),bc.getLast().hash, 2, 0);
												}
												bc.add(b);
												new Block().CheckHash(bc);
										
												FileOutputStream fout;
												try {
													fout = new FileOutputStream(new File("BChain.txt"));
													ObjectOutputStream out = new ObjectOutputStream(fout);
													out.writeObject(bc);
													out.flush();
													out.close();
													fout.close();
													System.out.println("Written Successfully to file");
													JPanel done = new JPanel();
													JLabel suc = new JLabel("Successfully voted");
													done.add(suc);
													j.add(done, "Third");
													card.next(j.getContentPane());
												} catch (FileNotFoundException e2) {
													// TODO Auto-generated catch block
													e2.printStackTrace();
												} catch (IOException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
											}
										}
									});
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}	
								} catch (Exception E) {
								// TODO Auto-generated catch block
								E.printStackTrace();
								} 
								log=1;
								break;
						}
						line=br.readLine();
					}
					if(log==0)
					{
						panel3.setVisible(false);
						
						JLabel l = new JLabel("Username wrong");
						JPanel panel4 = new JPanel();
						panel4.add(l);
						if(panel3.getComponentCount()>3)
							panel3.remove(3);
						l.setSize(100, 100);
						panel3.add(panel4);
						panel3.setVisible(true);
					}
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
		}});
		
	}

}
