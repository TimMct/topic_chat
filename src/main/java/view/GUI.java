package view;

import token_ring_sockets.ServerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class GUI {

    private boolean type;


    private ServerThread serverThread;
    private String name;
    private String pubSubMessage = "";


    private List<JLabel> labelsForMessages_1;
    private List<JLabel> labelsForMessages_2;
    private List<JLabel> labelsForMessages_3;

    // 3 topicuri
    private JPanel panelForTopicMessages_1;
    private JPanel panelForTopicMessages_2;
    private JPanel panelForTopicMessages_3;




    private JFrame frameForMessages;

    public GUI(boolean type) {
        this.type = type;
    }

    public GUI(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPubSubMessage() {
        return pubSubMessage;
    }

    public void setPubSubMessage(String pubSubMessage) {
        this.pubSubMessage = pubSubMessage;
    }


    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }


    public void addMessage2GUI(String topic, int allignment, String newMessage){

        if(newMessage.equals("")){
            return;
        }

        JLabel newLabel1, newLabel2, newLabel3;

        if(topic.equals("Cooking")){
            newLabel1 = new JLabel(newMessage);
            newLabel1.setHorizontalAlignment(allignment);
            labelsForMessages_1.add(newLabel1);
            panelForTopicMessages_1.setLayout(new GridLayout(labelsForMessages_1.size(), 1));
            panelForTopicMessages_1.add(newLabel1);
        }
        if(topic.equals("Sport")){
            newLabel2 = new JLabel(newMessage);
            newLabel2.setHorizontalAlignment(allignment);
            labelsForMessages_2.add(newLabel2);
            panelForTopicMessages_2.setLayout(new GridLayout(labelsForMessages_2.size(), 1));
            panelForTopicMessages_2.add(newLabel2);
        }
        if(topic.equals("Harry Potter")){
            newLabel3 = new JLabel(newMessage);
            newLabel3.setHorizontalAlignment(allignment);
            labelsForMessages_3.add(newLabel3);
            panelForTopicMessages_3.setLayout(new GridLayout(labelsForMessages_3.size(), 1));
            panelForTopicMessages_3.add(newLabel3);
        }



        frameForMessages.validate();
        frameForMessages.setVisible(true);
    }



    public void produce(){
        synchronized(this){
            System.out.println("Main gui running...");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Resumed... and the name is: "+this.name);
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.frameForMessages = new JFrame("chat - "+this.name);
        frameForMessages.getContentPane().setLayout(new GridLayout(3, 1));
        frameForMessages.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameForMessages.setSize(600, 400);
        frameForMessages.setLocationRelativeTo(null);

        this.panelForTopicMessages_1 = new JPanel();
        this.panelForTopicMessages_2 = new JPanel();
        this.panelForTopicMessages_3 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();



        this.labelsForMessages_1 = new ArrayList<JLabel>();
        this.labelsForMessages_2 = new ArrayList<JLabel>();
        this.labelsForMessages_3 = new ArrayList<JLabel>();



        JTextArea textArea = new JTextArea("");
        textArea.setColumns(40);
        textArea.setRows(6);
        JButton button = new JButton("Send text");

        GUI currentGUI = this;

        JComboBox chooseTopic = new JComboBox();
        chooseTopic.addItem("Cooking");
        chooseTopic.addItem("Sport");
        chooseTopic.addItem("Harry Potter");

        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pubSubMessage = textArea.getText() == null ? "" : textArea.getText();

                if(currentGUI.serverThread.hasToken()){
                    pubSubMessage = currentGUI.serverThread.getActualName().toUpperCase() +": "+ pubSubMessage;
                    currentGUI.serverThread.getPublisher().publishMessage(chooseTopic.getSelectedItem().toString(), pubSubMessage);
                }else{
                    System.out.println("You don't have token");
                }

                textArea.setText("");

            }
        });





        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        panelForTopicMessages_1.setLayout(new GridLayout(labelsForMessages_1.size(), 1));
        panelForTopicMessages_2.setLayout(new GridLayout(labelsForMessages_2.size(), 1));
        panelForTopicMessages_3.setLayout(new GridLayout(labelsForMessages_3.size(), 1));

        for(JLabel label : labelsForMessages_1){
            panelForTopicMessages_1.add(label);
        }
        for(JLabel label : labelsForMessages_2){
            panelForTopicMessages_2.add(label);
        }
        for(JLabel label : labelsForMessages_3){
            panelForTopicMessages_3.add(label);
        }

        panel2.add(scroll);
        panel3.add(chooseTopic);
        panel3.add(button);


        JScrollPane scrollLabels1 = new JScrollPane (panelForTopicMessages_1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollLabels2 = new JScrollPane(panelForTopicMessages_2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollLabels3 = new JScrollPane(panelForTopicMessages_3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);



        JPanel panelLabels = new JPanel();
        panelLabels.setLayout(new GridLayout(1, 3));
        panelLabels.add(scrollLabels1);
        panelLabels.add(scrollLabels2);
        panelLabels.add(scrollLabels3);

        frameForMessages.add(panelLabels);

        JPanel panelChooseTopic = new JPanel();
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Cooking");
        comboBox.addItem("Sport");
        comboBox.addItem("Harry Potter");
        panelChooseTopic.add(comboBox);

        frameForMessages.add(panel2);
        frameForMessages.add(panel3);

        frameForMessages.setVisible(true);
    }


    public void configure(){
            System.out.println("Configuration...");

            JFrame frame =  new JFrame("window");
            frame.getContentPane().setLayout(new GridLayout(4, 1));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 200);
            frame.setLocationRelativeTo(null);

            JPanel firstPanel = new JPanel();
            JPanel secondPanel = new JPanel();
            JPanel thirdPanel = new JPanel();
            JPanel forthPanel = new JPanel();

            JLabel enterName = new JLabel("Please enter your name below.");


            JTextField textField = new JTextField("");
            textField.setColumns(10);
            JButton button = new JButton("Submit");

            List<JCheckBox> chooseTopics = new ArrayList<JCheckBox>();
            chooseTopics.add(new JCheckBox("Cooking"));
            chooseTopics.add(new JCheckBox("Sport"));
            chooseTopics.add(new JCheckBox("Harry Potter"));



            GUI currentGUI = this;
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (currentGUI){
                        name = textField.getText();
                        currentGUI.notify();

                        List<String> listOfTopics = new ArrayList<String>();

                        for(JCheckBox checkBox : chooseTopics){
                            if(checkBox.isSelected()){
                                listOfTopics.add(checkBox.getText());
                            }
                        }
                        currentGUI.getServerThread().setListOfTopics(listOfTopics);

                        frame.dispose();
                    }
                }
            });


            firstPanel.setLayout(new GridLayout(1, 1));
            firstPanel.add(enterName);

            secondPanel.setLayout(new GridLayout(1, 1));
            secondPanel.add(textField);

            thirdPanel.setLayout(new GridLayout(1, chooseTopics.size()));
            for(JCheckBox checkBox : chooseTopics){
                thirdPanel.add(checkBox);
            }
            forthPanel.setLayout(new GridLayout(1, 1));
            forthPanel.add(button);


            frame.add(firstPanel);
            frame.add(secondPanel);
            frame.add(thirdPanel);
            frame.add(forthPanel);

            frame.setVisible(true);



    }


}
