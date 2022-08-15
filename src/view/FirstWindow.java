package view;


import control.SendOrGetDataFromServer;
import model.Package;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static control.PackageManagement.packageList;
import static control.SendOrGetDataFromServer.GetCustomPackageListFromServer;


public class FirstWindow implements WindowListener,ActionListener, ItemListener {


    JFrame applicationFrame;

    //For pages:
    JPanel mainTab;
    JPanel panelWest;
    JPanel panelEast;
    JPanel panelTop;
    JTabbedPane tabbedPane;


    //Center Side:
    JPanel panelCenter;
    JPanel panelInsideSP;
    JPanel secondTabPanelInsideSP;
    JPanel thirdTabPanelInsideSP;

    JButton removeButton;

    //Bottom Side:
    JPanel panelBot;
    JButton addPackageButton;


    private void NoPackageToShow(JPanel thePanel)
    {
            JTextPane NoPackageMessage = new JTextPane();
            NoPackageMessage.setText("No items to show.");
            NoPackageMessage.setEditable(false);
            thePanel.add(NoPackageMessage);
    }

    private void drawAllPackages() {
        packageList = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listAll");
        if (packageList.size() == 0) {
            NoPackageToShow(panelInsideSP);
        }
        else
        {
            for (int i = 0; i < packageList.size(); i++) {
                JPanel containerForAPackage = new JPanel();
                containerForAPackage.setLayout(new BorderLayout());

                //creating the container for bottom side of each package.
                JPanel bottomofPackageContainer = new JPanel();
                bottomofPackageContainer.setLayout(new FlowLayout(FlowLayout.TRAILING));

                //adding isDelivered CheckBox.
                JCheckBox isDeliveredCheckBox = new JCheckBox("Delivered?");
                if(packageList.get(i).getIsDelivered() == true) {
                    isDeliveredCheckBox.setSelected(true);
                }
                else {
                    isDeliveredCheckBox.setSelected(false);
                }

                bottomofPackageContainer.add(isDeliveredCheckBox);
                int finalI = i;
                isDeliveredCheckBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        SendOrGetDataFromServer.ChangeIsDeliveredStatus(finalI);
                        packageList = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listAll");


                        secondTabPanelInsideSP.removeAll();
                        List<Package> overDue = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listOverduePackage");
                        drawCustomPackages(secondTabPanelInsideSP,overDue);
                        secondTabPanelInsideSP.validate();
                        secondTabPanelInsideSP.repaint();


                        thirdTabPanelInsideSP.removeAll();
                        List<Package> upcoming = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listUpcomingPackage");
                        drawCustomPackages(thirdTabPanelInsideSP,upcoming);
                        thirdTabPanelInsideSP.validate();
                        thirdTabPanelInsideSP.repaint();
                    }
                });

                //adding Remove button.
                removeButton = new JButton("Remove");
                bottomofPackageContainer.add(removeButton);

                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            SendOrGetDataFromServer.RemovePackageFromServer(finalI);
                            packageList = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listAll");
                            panelInsideSP.removeAll();
                            drawAllPackages();
                            panelInsideSP.validate();
                            panelInsideSP.repaint();
                        }
                    }
                });
                containerForAPackage.add(bottomofPackageContainer, BorderLayout.SOUTH);

                String title = "Package #" + (i + 1) + "(" + packageList.get(i).getType() + ")";
                containerForAPackage.setBorder(BorderFactory.createTitledBorder(title));
                String mystr = packageList.get(i).toString();
                //from https://stackoverflow.com/questions/1090098/newline-in-jlabel
                String mystr2 = ("<html>" + mystr.replaceAll("\n", "<br/>") + "</html>");
                containerForAPackage.add(new JLabel(mystr2), BorderLayout.CENTER);

                panelInsideSP.add(containerForAPackage);
            }
        }
    }

    private void drawCustomPackages(JPanel thePanel, List<Package> thePackageList)
    {
        if(thePackageList.size() == 0)
        {
            NoPackageToShow(thePanel);
        }
        else {
            for (int i = 0; i < thePackageList.size(); i++) {
                JPanel containerForAPackage = new JPanel();
                containerForAPackage.setLayout(new BorderLayout());

                String title = "Package #" + (i + 1) + "(" + thePackageList.get(i).getType() + ")";
                containerForAPackage.setBorder(BorderFactory.createTitledBorder(title));
                String mystr = thePackageList.get(i).toString();
                //from https://stackoverflow.com/questions/1090098/newline-in-jlabel
                String mystr2 = ("<html>" + mystr.replaceAll("\n", "<br/>") + "</html>");
                containerForAPackage.add(new JLabel(mystr2), BorderLayout.CENTER);

                thePanel.add(containerForAPackage);
            }
        }
    }

    public FirstWindow()
    {
        packageList = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listAll");

        applicationFrame = new JFrame("My Package Deliveries Tracker");
        applicationFrame.setSize(500,500);
        applicationFrame.addWindowListener(this);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationFrame.setVisible(true);


        //instantiate the tabs.
        mainTab = new JPanel();
        mainTab.setLayout(new BorderLayout(10,10));

        JPanel overDueTab = new JPanel();
        overDueTab.setLayout(new BorderLayout(10,10));

        JPanel upcomingTab = new JPanel();
        upcomingTab.setLayout(new BorderLayout(10,10));


        //add the tabs to tab pane;
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("All", null , mainTab, null);
        tabbedPane.addTab("Overdue", null , overDueTab, null);
        tabbedPane.addTab("Upcoming", null , upcomingTab, null);

        applicationFrame.add(tabbedPane);
        //finished adding the tabs.

        //tab1:
        //instantiation, colors, dimensions.
        panelTop = new JPanel();
        panelBot = new JPanel();
        panelWest = new JPanel();
        panelEast = new JPanel();
        panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());


        panelTop.setPreferredSize(new Dimension(100,0));
        panelBot.setPreferredSize(new Dimension(100,80));
        panelWest.setPreferredSize(new Dimension(25,100));
        panelEast.setPreferredSize(new Dimension(25,100));


        ///Center Side:
        panelInsideSP = new JPanel();
        panelInsideSP.setLayout(new BoxLayout(panelInsideSP,BoxLayout.Y_AXIS));

        //We have to define this here even though it is for second tab. The reason is updating second tab when isDelivered in first tab is clicked!


        drawAllPackages();
        JScrollPane scrollPane = new JScrollPane(panelInsideSP);
        panelCenter.add(scrollPane);


        ///Bottom Side:
        panelBot.setLayout(new FlowLayout(FlowLayout.CENTER,0,35));
        addPackageButton = new JButton("Add Package");
        addPackageButton.addActionListener(this);
        panelBot.add(addPackageButton);

        //adding to main panel.
        mainTab.add(panelTop,BorderLayout.NORTH);
        mainTab.add(panelBot,BorderLayout.SOUTH);
        mainTab.add(panelWest,BorderLayout.WEST);
        mainTab.add(panelEast,BorderLayout.EAST);
        mainTab.add(panelCenter,BorderLayout.CENTER);

        // end of tab1.

//tab2 starts:
        panelTop = new JPanel();
        panelBot = new JPanel();
        panelWest = new JPanel();
        panelEast = new JPanel();
        panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        panelTop.setPreferredSize(new Dimension(100,0));
        panelBot.setPreferredSize(new Dimension(100,80));
        panelWest.setPreferredSize(new Dimension(25,100));
        panelEast.setPreferredSize(new Dimension(25,100));


        ///Center Side:
        List<Package> overDue = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listOverduePackage");

        secondTabPanelInsideSP = new JPanel();
        secondTabPanelInsideSP.setLayout(new BoxLayout(secondTabPanelInsideSP,BoxLayout.Y_AXIS));
        drawCustomPackages(secondTabPanelInsideSP, overDue);
            JScrollPane scrollPane2 = new JScrollPane(secondTabPanelInsideSP);
            panelCenter.add(scrollPane2);
        //adding to main panel.
        overDueTab.add(panelTop,BorderLayout.NORTH);
        overDueTab.add(panelBot,BorderLayout.SOUTH);
        overDueTab.add(panelWest,BorderLayout.WEST);
        overDueTab.add(panelEast,BorderLayout.EAST);
        overDueTab.add(panelCenter,BorderLayout.CENTER);

//end of tab2.


//tab3 starts:
        panelTop = new JPanel();
        panelBot = new JPanel();
        panelWest = new JPanel();
        panelEast = new JPanel();
        panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        panelTop.setPreferredSize(new Dimension(100,0));
        panelBot.setPreferredSize(new Dimension(100,80));
        panelWest.setPreferredSize(new Dimension(25,100));
        panelEast.setPreferredSize(new Dimension(25,100));


        ///Center Side:
        List<Package> upcoming = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listUpcomingPackage");
        thirdTabPanelInsideSP = new JPanel();
        thirdTabPanelInsideSP.setLayout(new BoxLayout(thirdTabPanelInsideSP,BoxLayout.Y_AXIS));
        drawCustomPackages(thirdTabPanelInsideSP, upcoming);
        JScrollPane scrollPane3 = new JScrollPane(thirdTabPanelInsideSP);
        panelCenter.add(scrollPane3);


        //adding to main panel.
        upcomingTab.add(panelTop,BorderLayout.NORTH);
        upcomingTab.add(panelBot,BorderLayout.SOUTH);
        upcomingTab.add(panelWest,BorderLayout.WEST);
        upcomingTab.add(panelEast,BorderLayout.EAST);
        upcomingTab.add(panelCenter,BorderLayout.CENTER);
//end of tab3.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPackageButton)
        {
            NewPackageWindow newPackageWindow = new NewPackageWindow(applicationFrame);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        SendOrGetDataFromServer.SaveJSONInServer();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {
        panelInsideSP.removeAll();
        drawAllPackages();
        panelInsideSP.validate();
        panelInsideSP.repaint();

        secondTabPanelInsideSP.removeAll();
        List<Package> overDue = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listOverduePackage");
        ;
        drawCustomPackages(secondTabPanelInsideSP,overDue);
        secondTabPanelInsideSP.validate();
        secondTabPanelInsideSP.repaint();


        thirdTabPanelInsideSP.removeAll();
        List<Package> upcoming = GetCustomPackageListFromServer("curl -H Content-Type: application/json -X GET localhost:8080/listUpcomingPackage");
        drawCustomPackages(thirdTabPanelInsideSP,upcoming);
        thirdTabPanelInsideSP.validate();
        thirdTabPanelInsideSP.repaint();

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}