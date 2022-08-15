package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import control.SendOrGetDataFromServer;
import model.Package;
import model.PackageFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import static control.PackageManagement.packageList;


//Inspired by this example: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/DialogDemoProject/src/components/CustomDialog.java
public class NewPackageWindow extends JDialog implements ActionListener {
    JComboBox packageType;

    JButton createButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");
    JPanel mainPanel;
    JPanel childField;

    JTextField name;
    JTextField notes;
    JTextField price;
    JTextField weight;
    DateTimePicker expectedDeliveryDate;
    JTextField author;
    DateTimePicker expiryDate;
    JTextField environmentalHandlingFee;


    private void addNewLayerToLayout(String description, Container toBeContained, Container container)
    {
        JPanel layout = new JPanel(new FlowLayout());
        layout.add(new JLabel(description + ":"));
        layout.add(toBeContained);
        toBeContained.setPreferredSize(new Dimension(300,20));
        container.add(layout);
    }

    private int numOfCharsInString(String text)
    {
        char[] characters = text.toCharArray();
        int noOfletters = 0;
        for(int i = 0; i < characters.length; i++)
        {
            if(Character.isLetter(characters[i]))
            {
                noOfletters++;
            }
        }
        return noOfletters;
    }
    NewPackageWindow(JFrame applicationFrame){
        super(applicationFrame,true);
        this.setTitle("Add New Package Window");
        this.setSize(400,400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));



        packageType = new JComboBox();
        packageType.addItem("Book");
        packageType.addItem("Perishable");
        packageType.addItem("Electronic");
        packageType.setSelectedIndex(-1);
        packageType.addActionListener(this);

        name = new JTextField("");

        notes = new JTextField("");
        price = new JTextField("");
        weight = new JTextField("");
        expectedDeliveryDate = new DateTimePicker();


        addNewLayerToLayout("Type",packageType,mainPanel);
        addNewLayerToLayout("Name",name,mainPanel);
        addNewLayerToLayout("Notes",notes,mainPanel);
        addNewLayerToLayout("Price",price,mainPanel);
        addNewLayerToLayout("Weight",weight,mainPanel);
        addNewLayerToLayout("Delivery",expectedDeliveryDate,mainPanel);


        //Here adding all the optionable fields like author and ...
        childField = new JPanel();
        mainPanel.add(childField);

        JPanel CancelOrAddButtons = new JPanel(new FlowLayout());
        CancelOrAddButtons.add(cancelButton);
        CancelOrAddButtons.add(createButton);
        mainPanel.add(CancelOrAddButtons);

        environmentalHandlingFee = new JTextField("");
        expiryDate = new DateTimePicker();
        author = new JTextField("");

        cancelButton.addActionListener(this);
        createButton.addActionListener(this);

        add(mainPanel);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == packageType)
        {
            String type = (String) packageType.getSelectedItem();
            if (type == "Book")
            {
                childField.removeAll();
                author = new JTextField();
                addNewLayerToLayout("Author",author,childField);
                getContentPane().validate();
                getContentPane().repaint();
            }
            if (type == "Perishable")
            {
                childField.removeAll();
                expiryDate = new DateTimePicker();
                addNewLayerToLayout("Expiry",expiryDate,childField);
                getContentPane().validate();
                getContentPane().repaint();
            }
            if (type == "Electronic")
            {
                childField.removeAll();
                environmentalHandlingFee = new JTextField();
                addNewLayerToLayout("Environ Fee",environmentalHandlingFee,childField);
                getContentPane().validate();
                getContentPane().repaint();
            }
        }
        else if(e.getSource() == cancelButton)
        {
            dispose();
        }
        else if(e.getSource() == createButton)
        {
            if(name.getText().replaceAll("\\s+","").equals("")
            || price.getText().replaceAll("\\s+","").equals("")
            || weight.getText().replaceAll("\\s+","").equals("")
            || expectedDeliveryDate.toString() == ""
            || (author.getText().replaceAll("\\s+","").equals("")
                    && environmentalHandlingFee.getText().replaceAll("\\s+","").equals("")
                    && expiryDate.toString() == "")
            || numOfCharsInString(weight.getText()) > 0
            || numOfCharsInString(price.getText()) > 0
            || numOfCharsInString(environmentalHandlingFee.getText()) > 0
            )
            {
                //information incomplete. Do not proceed further.
                IncompleteInfoPackageCreatingWindow incomplete = new IncompleteInfoPackageCreatingWindow(this);
            }
            else {
                String type = (String) packageType.getSelectedItem();
                if (type == "Book")
                {
                    Package newPackage = PackageFactory.getInstance("Book",name.getText(),notes.getText(), Double.parseDouble((price.getText())),Double.parseDouble((weight.getText())),expectedDeliveryDate.getDateTimeStrict(),author.getText(),null,0);
                    SendOrGetDataFromServer.AddPackageToServer(newPackage, "Book");

                }
                if (type == "Perishable")
                {
                    Package newPackage = PackageFactory.getInstance("Perishable",name.getText(),notes.getText(), Double.parseDouble((price.getText())),Double.parseDouble((weight.getText())),expectedDeliveryDate.getDateTimeStrict(),null,expiryDate.getDateTimeStrict(),0);
                    SendOrGetDataFromServer.AddPackageToServer(newPackage, "Perishable");

                }
                if (type == "Electronic")
                {
                    Package newPackage = PackageFactory.getInstance("Electronic",name.getText(),notes.getText(), Double.parseDouble((price.getText())),Double.parseDouble((weight.getText())),expectedDeliveryDate.getDateTimeStrict(),null,null,Double.parseDouble(environmentalHandlingFee.getText()));
                    SendOrGetDataFromServer.AddPackageToServer(newPackage, "Electronic");
                }
                //Collections.sort(packageList);
                dispose();
            }
        }
    }
}