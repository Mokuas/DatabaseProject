package main.dialogs;

import main.models.Publisher;
import main.services.PublisherService;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Dialog responsible for creating/updating/deleting publishers
 */
public class CreatePublisherDialog implements CreateDialog {
    JFrame frame;
    PublisherService publisherService;

    Callable<Void> refreshCallback;

    int publisherId = 0;

    /**
     * Constructor
     * @param publisherService Publisher Service
     * @param publisherId publisher record to delete/update (pass 0 for create)
     */
    public CreatePublisherDialog(PublisherService publisherService, int publisherId) {
        System.out.println("CreatePublisherDialog.java(dialogs)");
        this.publisherService = publisherService;
        this.publisherId = publisherId;
    }

    /**
     * set refresh callback
     * @param refreshCallback callback will be called when a CREATE, UPDATE or DELETE is done
     */
    public void setRefreshCallback(Callable<Void> refreshCallback) {
        System.out.println("setRefreshCallback CreatePublisherDialog.java(dialogs)");
        this.refreshCallback = refreshCallback;
    }

    @Override
    public void setItem(Object object) {
        System.out.println("setItem CreatePublisherDialog.java(dialogs)");
        if(object instanceof Publisher p) {
            publisherId = p.getId();
        } else {
            publisherId = 0;//burasinin 0 olma sebebi eger burasi 0 ise yeni bir yayin evi olusturulacagi anlamina geliyor
        }
    }

    @Override
    public void display() {
        System.out.println("display CreatePublisherDialog.java(dialogs)");
        Publisher publisherPlaceholder = new Publisher(0, "Name", "Address", "Phone Number");
        if(publisherId != 0) {
            publisherPlaceholder = publisherService.getById(publisherId);
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setTitle(publisherId == 0 ? "Create publisher" : "Edit publisher");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

        JTextField nameField = new JTextField(publisherPlaceholder.getName());
        nameField.setToolTipText("Name");
        nameField.setBounds(20, 50, 360, 30);
        frame.add(nameField);

        JTextField addressField = new JTextField(publisherPlaceholder.getAddress());
        addressField.setToolTipText("Email");
        addressField.setBounds(20, 100, 360, 30);
        frame.add(addressField);

        JTextField numberField = new JTextField(publisherPlaceholder.getPhoneNumber());
        numberField.setToolTipText("Phone Number");
        numberField.setBounds(20, 150, 360, 30);
        frame.add(numberField);

        JButton createButton = new JButton(publisherId == 0 ? "Create" : "Update");
        createButton.setBounds(50, 400, 100, 30);
        frame.add(createButton);

        createButton.addActionListener(e -> {
            try {
                Publisher publisher = new Publisher(publisherId, nameField.getText(), addressField.getText(), numberField.getText());
                if(publisherId == 0) {
                    publisherService.persist(publisher);
                } else {
                    publisherService.merge(publisher);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            callRefreshCallback();
            frame.setVisible(false);
            frame.dispose();
        });

        if(publisherId != 0) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBounds(150, 400, 100, 30);
            frame.add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    publisherService.remove(publisherService.getById(publisherId));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                callRefreshCallback();

                frame.setVisible(false);
                frame.dispose();
            });
        }

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(250, 400, 100, 30);
        frame.add(cancelButton);

        cancelButton.addActionListener(e -> {
            System.out.println("Cancelled");

            frame.setVisible(false);
            frame.dispose();
        });
    }

    /**
     * Calls refresh callback
     */
    private void callRefreshCallback() {
        System.out.println("callRefreshCallback CreatePublisherDialog.java(dialogs)");
        if(refreshCallback != null) {
            try {
                refreshCallback.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
