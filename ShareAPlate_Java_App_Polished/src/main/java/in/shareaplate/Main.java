package in.shareaplate;

import in.shareaplate.db.Database;
import in.shareaplate.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Database.testConnection();
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not connect to MySQL.\n\n" + ex.getMessage()
                    + "\n\nCheck that MySQL is running and src/main/resources/db.properties is configured.",
                    "ShareAPlate - Database connection error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
