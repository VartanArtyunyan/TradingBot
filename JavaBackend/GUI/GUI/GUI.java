 package GUI;

 import java.awt.*;
 import java.awt.event.*;
 
 public class GUI extends Frame
 { 
   GridBagLayout grid = new GridBagLayout();
   GridBagConstraints straints = new GridBagConstraints();
   Button button;
   
   Label label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12;
   List PositionsList,list2,list3;
   Panel panel1,panel2,panel3;
   TextField text1,text2,text3;
   Button button1, button2, button3;
    
   public GUI () 
   {		  
     setTitle("TradingBot");
     setSize(1000,400);
     setLayout(grid);                             // GridBagLayout setzen
     
     addWindowListener(new TestWindowListener());
    
     straints.gridx = straints.gridy = 0;
     straints.gridheight = straints.gridwidth = 1;
     straints.fill = GridBagConstraints.BOTH;
     straints.ipadx = 200;
     straints.ipady = 10;
     label1 = new Label("Curretn Positions");
     grid.setConstraints(label1, straints);
     add(label1);
 
     straints.gridy = 1;
     straints.gridheight = straints.gridwidth = 2;
     PositionsList = new List();
     PositionsList.add("EUR_USD");
     PositionsList.add("EUR_HUF");
     grid.setConstraints(PositionsList, straints);
     add(PositionsList);
     
     straints.gridx = 0;
     straints.gridy = 3;
     straints.gridheight=1;
     straints.gridwidth=2;
     label2 = new Label("LOG:");
     grid.setConstraints(label2, straints);
     add(label2);
 
     straints.gridx = 0;
     straints.gridy = 4;
     straints.gridheight=3;
     straints.gridwidth=2;
     list2 = new List();
     list2.add("signale gefunden");
     grid.setConstraints(list2, straints);
     add(list2);
    
     straints.gridx = 2;
     straints.gridy = 0;
     straints.gridheight = 1;
     straints.gridwidth = 1;
     label3 = new Label("Current Signale");
     grid.setConstraints(label3, straints);
     add(label3);
     
     straints.gridx = 2;
     straints.gridy = 1;
     straints.gridheight = 3;
     straints.gridwidth = 1;
     list3 = new List();
     list3.add("EUR_USD MACD:1 TMACD:2");
     list3.add("HUF_EUR MACD:21 TMACD:2");
     list3.add("JPY_USD MACD:1 TMACD:21");
     grid.setConstraints(list3, straints);
     add(list3);
     
     straints.gridx = 1;
     straints.gridy = 0;
     straints.gridheight = 1;
     straints.gridwidth = 1;
     label4 = new Label("             ");
     grid.setConstraints(label4, straints);
     add(label4);
     

     
     straints.gridx = 2;
     straints.gridy = 4;
     straints.gridheight = 1;
     straints.gridwidth = 1;
     panel1 = new Panel();
     label5 = new Label("shortTolleranz");
     text1 = new TextField();
     button1 = new Button("set");
     panel1.add(label5);
     panel1.add(text1);
     panel1.add(button1);
     grid.setConstraints(panel1, straints);
     add(panel1);
     
     straints.gridx = 2;
     straints.gridy = 5;
     straints.gridheight = 1;
     straints.gridwidth = 1;
     panel2 = new Panel();
     label6 = new Label("longTolleranz");
     text2 = new TextField();
     button1 = new Button("set");
     panel2.add(label6);
     panel2.add(text2);
     panel2.add(button1);
     grid.setConstraints(panel2, straints);
     add(panel2);
     
     straints.gridx = 2;
     straints.gridy = 6;
     straints.gridheight = 1;
     straints.gridwidth = 1;
     panel3 = new Panel();
     label7 = new Label("granularity");
     text3 = new TextField();
     button1 = new Button("set");
     panel3.add(label7);
     panel3.add(text3);
     panel3.add(button1);
     grid.setConstraints(panel3, straints);
     add(panel3);
     
        
    // pack();
     setVisible(true);                           
   }
    
   class TestWindowListener extends WindowAdapter
   {
     public void windowClosing(WindowEvent e)
     {
       e.getWindow().dispose();                  
       System.exit(0);                            
     }           
   }
 
 }