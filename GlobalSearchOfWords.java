import java.io.BufferedReader;
import java.io.DataInputStream; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;    
import java.security.*;
import java.math.*;
import java.nio.*; 
import java.awt.FlowLayout; 
import java.io.*;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.applet.*;

 public class SwingBasic extends JFrame 
{
 	JLabel l;
	String fpath="E:/TEST/filtercodes.txt";
	public SwingBasic() 
	{ 
				//new scroll().run();
		l=new JLabel();
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();

		JTabbedPane jtp=new JTabbedPane();
		final JTextField txt1=new JTextField(40);
		//JTextArea txt2=new JTextArea(30,50);
		JLabel txt2=new JLabel();
		JTextField txt3=new JTextField(50);
		//JTextArea txt4=new JTextArea(30,50);
		JEditorPane txt4=new JEditorPane();
		
		JScrollPane scrollPane1 = new JScrollPane(txt2); 
		JScrollPane scrollPane2 = new JScrollPane(txt4);
		JButton b1=new JButton("Browse");
		JButton b2=new JButton("Cancel");
		//JButton b3=new JButton("Upload"); 
		JButton b3=new JButton("Upload");
		JButton b5=new JButton("Search");
		JButton b6=new JButton("Open");

		l.setText(" filter ");
		panel1.setLayout(null);
		txt1.setBounds(10,10,300,30);
		panel1.add(txt1);
		b1.setBounds(320,10,100,30);
		panel1.add(b1);
		b2.setBounds(430,10,100,30);
		panel1.add(b2);
		//b3.setBounds(540,10,100,30);
		//panel1.add(b3);
		scrollPane1.setBounds(10,50,640,300);
		panel1.add(scrollPane1);
		b3.setBounds(550,355,100,30);
		panel1.add(b3);

		panel2.setLayout(null);
		txt3.setBounds(10,10,300,30);
		panel2.add(txt3);
		b5.setBounds(320,10,100,30);
		panel2.add(b5);
		scrollPane2.setBounds(10,50,640,300);
		panel2.add(scrollPane2);
		
		b6.setBounds(550,355,100,30);
		panel2.add(b6);
		jtp.addTab("Upload",panel1);
		jtp.addTab("Search",panel2); 
		add(jtp);
		b1.addActionListener( new ActionListener()
		{
			 public void actionPerformed( ActionEvent e)
			{
			 
	
		int result=0;
		
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File file =chooser.getSelectedFile();
		String filename = file.getName();
		System.out.println("You have selected: " + filename);
		
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
       // int result = Chooser.showDialog(this,"Open/Save");
        if(result == JFileChooser.APPROVE_OPTION)
        {
            txt1.setText(file.toString());
        }
	}
		
	} );
		b2.addActionListener( new ActionListener()
		{
			 public void actionPerformed( ActionEvent e)
			{ 
				txt1.setText(null);
			}
		} );
		b3.addActionListener( new ActionListener()
		{
			String skipwords[]={"a","an","the","is","was","i","am","what","which","were","as","of","to","are"};
			List slist = (List) Arrays.asList(skipwords);
			
			public void actionPerformed( ActionEvent e)
			{ 
				try{
					MaxDuplicateWordCount mdc = new MaxDuplicateWordCount();
					Map<String, Integer> wordMap = mdc.getWordCount(txt1.getText());
					List<Entry<String, Integer>> list = mdc.sortByValue(wordMap);

					String wlist="<html><body bgcolor='white'><table border='1'><tr><th colspan='2'>Keywords</th></tr>";
					BloomFilter filter =new BloomFilter(512, 256);
					for(Map.Entry<String, Integer> entry:list)
					{	if(!slist.contains(entry.getKey()))
						{	System.out.println(entry.getKey()+" ==== "+entry.getValue());
							wlist=wlist+"<tr><td>"+entry.getKey()+"</td><td>"+entry.getValue()+"</td></tr>";
							// adding each obj to bloomfilter from list array elements
							filter.add(entry.getKey());						
						}
					}
					wlist=wlist+"</table></body></html>";
					txt2.setText(wlist);
					
					//saving Bloom filter code
					FileWriter fw = new FileWriter("C:/TEST/filtercodes.txt", true);
					PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
					pw.println(txt1.getText()+"#"+filter.getFilter());
					pw.close();
					
					}
					catch (Exception e1) {               e1.printStackTrace();		}
			}
	});

    			
	
		/*b4.addActionListener( new ActionListener()
		{
			 public void actionPerformed( ActionEvent e)
			{ 
				l.setText("Button clicked"); 
			}
		} );*/
		b5.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e)
			{ 
				String keyword=txt3.getText();
				try{
				BufferedReader reader = new BufferedReader(new FileReader(fpath));
				String line = reader.readLine();
				String flist="";
				int i=0;
				while ((line=reader.readLine()) != null) 
				{
					StringTokenizer st=new StringTokenizer(line,"#");
					while(st.hasMoreTokens())
					{	String fname=st.nextToken();
						String filter=st.nextToken();
						BloomFilter f =new BloomFilter(512, 256);
						f.setFilter(filter);
						if(f.contains(keyword))
						{
							System.out.println(fname);
							flist=flist+"<tr><td><a href='"+fname+"'>"+fname+"</a></td></tr>";
							i++;
						}
        			}
				}
				flist="<html><body bgcolor='white'><table border='1'><tr><th>"+i+" files found..</th></tr>"+flist;
				flist=flist+"</table></body></html>";
				txt4.setContentType("text/html");
				txt4.setText(flist);
				reader.close();
				}catch(Exception e2) 	{		e2.printStackTrace();	}
			}
		} );
		b6.addActionListener( new ActionListener()
		{
		 public void actionPerformed( ActionEvent e)
		{ 
		//open("txt4.getString()"); 
		}
		} );
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setTitle("Fast File Search");

 	setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	public static void main(String args[]) 
	{
	 new SwingBasic(); 
	}
}
 
class MaxDuplicateWordCount {
     
    public Map<String, Integer> getWordCount(String fileName){
 
        FileInputStream fis = null;
        DataInputStream dis = null;
        BufferedReader br = null;
        Map<String, Integer> wordMap = new HashMap<String, Integer>();
        try {
            fis = new FileInputStream(fileName);
            dis = new DataInputStream(fis);
            br = new BufferedReader(new InputStreamReader(dis));
            String line = null;
            while((line = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line, " ");
                while(st.hasMoreTokens()){
                    String tmp = st.nextToken().toLowerCase();
                    if(wordMap.containsKey(tmp)){
                        wordMap.put(tmp, wordMap.get(tmp)+1);
                    } else {
                        wordMap.put(tmp, 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{if(br != null) br.close();}catch(Exception ex){}
        }
        return wordMap;
    }
     
    public List<Entry<String, Integer>> sortByValue(Map<String, Integer> wordMap){
         
        Set<Entry<String, Integer>> set = wordMap.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        return list;
    }
     
 /*   public static void main(String a[]){
    	
    	try{
        MaxDuplicateWordCount mdc = new MaxDuplicateWordCount();
        Map<String, Integer> wordMap = mdc.getWordCount("txt1.getText()");
        List<Entry<String, Integer>> list = mdc.sortByValue(wordMap);
     
        
        //create bloomfilter obj 
        
        BloomFilter filter =new BloomFilter(512, 256);
        for(Map.Entry<String, Integer> entry:list)
		{
            System.out.println(entry.getKey()+" ==== "+entry.getValue());
             // adding each obj to bloomfilter from list array elements
                  filter.add(entry.getValue());
        }
    	}catch (Exception e) {
                 e.printStackTrace();
		}
    }*/
}
class BloomFilter
{    
    private byte[] set;
    private int keySize=256, setSize=256, size;
    private MessageDigest md;
	/*Set filter string */
	public void setFilter(String fstring)
	{
        for(int i=0;i<fstring.length();i++)
        {    if(fstring.charAt(i)=='1')
                set[i]=1;
            else
                set[i]=0;
		}
	}
	/* Get filter string */
    public String getFilter() 
    {   String t="";
        for(int i=0;i<set.length;i++)
        {    if(set[i]==1)
                t=t+"1";
            else
                t=t+"0";
		}
        return(t);
    }

    public BloomFilter(int capacity, int k) throws Exception
    {
        setSize = capacity;
        set = new byte[setSize];
        keySize = k;
        size = 0;
        md = MessageDigest.getInstance("MD5");
    }
    /* Function to add an object */
    public void add(Object obj) throws Exception
    {   
        int[] tmpset = getSetArray(obj);
        for (int i : tmpset)
           set[i] = 1;
        size++;
    }
    /* Function to check is an object is present */
    public boolean contains(Object obj) 
    {
        int[] tmpset = getSetArray(obj);
        for (int i : tmpset)
            if (set[i] != 1)
                return false;
        return true;
    }
    /* Function to get hash - MD5 */
    private int getHash(int i)
    {
        md.reset();
        byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
        md.update(bytes, 0, bytes.length);
        return Math.abs(new BigInteger(1, md.digest()).intValue()) % (set.length - 1);
    }
    /* Function to get set array for an object */
    private int[] getSetArray(Object obj)
    {
        int[] tmpset = new int[setSize];
        tmpset[0] = getHash(obj.hashCode());
        for (int i = 1; i < setSize; i++)
            tmpset[i] = (getHash(tmpset[i - 1]));
        return tmpset;
    }    
    
    /*to make empty*/
    public void makeEmpty() throws Exception
    {
        set = new byte[setSize];
        size = 0;
        md = MessageDigest.getInstance("MD5");
    }
    /* Function to get size of objects added */
    public int getSize()   {        return size;    }
}

