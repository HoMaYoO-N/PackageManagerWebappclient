package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IncompleteInfoPackageCreatingWindow extends JDialog implements ActionListener {
    JButton okButton;
    IncompleteInfoPackageCreatingWindow(NewPackageWindow newPackageWindow) {
        super(newPackageWindow,true);
        this.setTitle("Warning Page");
        this.setSize(200,200);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel containerForMessage = new JPanel();
        JTextPane message = new JTextPane();
        message.setText("Information incomplete. Some fields are empty. Please Change them.");
        message.setEditable(false);
        message.setPreferredSize(new Dimension(120,120));
        containerForMessage.add(message);
        mainPanel.add(containerForMessage);

        okButton = new JButton("Okay");
        okButton.setPreferredSize(new Dimension(50,20));
        okButton.addActionListener(this);
        mainPanel.add(okButton,BorderLayout.SOUTH);

        this.add(mainPanel);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton);
        {
            this.dispose();
        }
    }
}
