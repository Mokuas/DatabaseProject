package main.dialogs;
//burdaki tum methodlar abstract
/**
 * Interface for CreateDialog classes
 *
 */
public interface CreateDialog {

    /**
     * Opens the dialog window
     */
    void display();

    /**
     * set item
     * @param object sets currently updating/deleting item id after constructing the dialog
     */
    void setItem(Object object);
}