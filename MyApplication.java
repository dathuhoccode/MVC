package MVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller(model, view);
            controller.initController();
        });
    }
}

// Model
class Model {
    private List<String> dataList;

    Model() {
        dataList = new ArrayList<>();
    }

    void addData(String data) {
        dataList.add(data);
    }

    List<String> getDataList() {
        return dataList;
    }

    void saveDataToFile(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadDataFromFile(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            dataList = (List<String>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

// View
class View {
    private JFrame frame;
    private JTextArea textArea;
    private JButton loadButton;
    private JButton saveButton;

    View() {
        frame = new JFrame("MVC Swing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);

        loadButton = new JButton("Load");
        saveButton = new JButton("Save");

        JPanel panel = new JPanel();
        panel.add(loadButton);
        panel.add(saveButton);

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);

        frame.pack();
        frame.setVisible(true);
    }

    JFrame getFrame() {
        return frame;
    }

    JTextArea getTextArea() {
        return textArea;
    }

    JButton getLoadButton() {
        return loadButton;
    }

    JButton getSaveButton() {
        return saveButton;
    }
}

// Controller
class Controller {
    private Model model;
    private View view;

    Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    void initController() {
        view.getLoadButton().addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                model.loadDataFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                refreshView();
            }
        });

        view.getSaveButton().addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                model.saveDataToFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private void refreshView() {
        List<String> dataList = model.getDataList();
        view.getTextArea().setText(dataList.stream().collect(Collectors.joining("\n")));
    }
}
