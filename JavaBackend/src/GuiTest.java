import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class GuiTest extends Frame implements ActionListener
{
Label label1,label2,label3;
TextField text1,text2,text3;
Button button1,button2,button3,button4,button5;
public GuiTest()
{
label1 = new Label("Var 1");
label2 = new Label("Var 2");
label3 = new Label("Result");
text1 = new TextField(10);
text2 = new TextField(10);
text3 = new TextField(10);
button1 = new Button("Add");
button2 = new Button("Sub");
button3 = new Button("Multi");
button4 = new Button("Div");
button5 = new Button("Close");
add(label1);
add(text1);
add(label2);
add(text2);
add(label3);
add(text3);
add(button1);
add(button2);
add(button3);
add(button4);
add(button5);
setSize(200,200);
setTitle("AWTCalculator");
setLayout(new FlowLayout());
button1.addActionListener(this);
button2.addActionListener(this);
button3.addActionListener(this);
button4.addActionListener(this);
button5.addActionListener(this);
}
public void actionPerformed(ActionEvent action) {
double a1=0,b1=0,c1=0;
try
{
a1 = Double.parseDouble(text1.getText());
}
catch (NumberFormatException e) {
text1.setText("Invalid input entered");
}
try
{
b1 = Double.parseDouble(text2.getText());
}
catch (NumberFormatException e) {
text2.setText("Invalid input entered");
}
if(action.getSource()==button1)
{
c1 = a1 + b1;
text3.setText(String.valueOf(c1));
}
if(action.getSource()==button2)
{
c1 = a1 - b1;
text3.setText(String.valueOf(c1));
}
if(action.getSource()==button3)
{
c1 = a1 * b1;
text3.setText(String.valueOf(c1));
}
if(action.getSource()==button4)
{
c1 = a1 / b1;
text3.setText(String.valueOf(c1));
}
if(action.getSource() == button5)
{
System.exit(0);
}
}

}